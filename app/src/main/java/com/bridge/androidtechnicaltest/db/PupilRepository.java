package com.bridge.androidtechnicaltest.db;

import com.bridge.androidtechnicaltest.network.PupilRequest;
import com.bridge.androidtechnicaltest.network.PupilResponse;
import com.bridge.androidtechnicaltest.network.PupilService;
import com.bridge.androidtechnicaltest.network.RequestHeaderHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class PupilRepository implements IPupilRepository {

    private final PupilDao pupilDao;
    private final PupilService pupilService;

    @Inject
    public PupilRepository(AppDatabase db, PupilService service) {
        this.pupilDao = db.getPupilDao();
        this.pupilService = service;
    }

    public Completable insertPupilAsync(Pupil pupil) {
        return Completable.fromAction(() -> pupilDao.insertPupil(pupil))
                .subscribeOn(Schedulers.io());
    }

    private String requestId() {
        return RequestHeaderHelper.generateRequestId();
    }

    private String userAgent() {
        return RequestHeaderHelper.getUserAgent();
    }

    @Override
    public Single<List<Pupil>> getLocalPupils() {
        return pupilDao.getPupils().subscribeOn(Schedulers.io());
    }

    @Override
    public Completable syncPupilsFromRemote(int page) {
        return pupilService.getAllPupils(page, requestId(), userAgent())
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(response -> Completable.fromAction(() -> {
                    List<Pupil> remotePupils = response.getItems();
                    if (remotePupils != null) {
                        for (Pupil p : remotePupils) {
                            p.setSynced(true);
                        }
                        pupilDao.insertAll(remotePupils);
                    }
                }));
    }

    @Override
    public Completable clearAndSyncFromRemote() {
        return Completable.fromAction(() -> {
            pupilDao.clearSyncedPupils();
        }).andThen(
                syncPupilsFromRemote(1)
                        .andThen(syncPupilsFromRemote(2))
                        .andThen(syncPupilsFromRemote(3))
                        .andThen(syncPupilsFromRemote(4))
                        .andThen(syncPupilsFromRemote(5))
        ).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable loadMorePupils(int page) {
        return syncPupilsFromRemote(page);
    }

    @Override
    public Completable syncUnsyncedPupils() {
        return pupilDao.getUnsyncedPupils()
                .flatMapObservable(Observable::fromIterable)
                .flatMapCompletable(pupil -> {
                    PupilRequest request = new PupilRequest(pupil);
                    return pupilService.addPupil(request, requestId(), userAgent())
                            .andThen(Completable.fromAction(() -> {
                                pupil.setSynced(true);
                                pupilDao.insertPupil(pupil);
                            }))
                            .doOnError(throwable -> {
                                android.util.Log.e("PupilRepository", "Failed to sync pupil: " + pupil.getName() +
                                        ", Error: " + throwable.getMessage());
                            })
                            .onErrorComplete();
                }).subscribeOn(Schedulers.io());
    }

    // Method to clear problematic unsynced pupils if needed
    public Completable clearUnsyncedPupils() {
        return Completable.fromAction(() -> {
            pupilDao.deleteUnsyncedPupils();
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Pupil> getPupilById(int id) {
        return pupilDao.getPupilById(id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<PupilList> getOrFetchPupils() {
        return pupilService.getAllPupils(1, requestId(), userAgent())
                .flatMap(response -> {
                    return Completable.fromAction(() -> {
                        List<Pupil> remotePupils = response.getItems();
                        if (remotePupils != null) {
                            for (Pupil p : remotePupils) {
                                p.setSynced(true);
                            }
                            pupilDao.insertAll(remotePupils);
                        }
                    }).andThen(

                            pupilDao.getPupils().map(PupilList::new)
                    );
                })
                .onErrorResumeNext(throwable -> {
                    // If network fails, return local pupils
                    return pupilDao.getPupils()
                            .map(PupilList::new);
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable addPupil(Pupil pupil) {
        pupil.setSynced(false);
        return Completable.fromAction(() -> pupilDao.insertPupil(pupil))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deletePupil(Pupil pupil) {
        // Delete from server first, then from local database
        return pupilService.deletePupil(pupil.getPupilId(), requestId(), userAgent())
                .andThen(Completable.fromAction(() -> pupilDao.deletePupil(pupil)))
                .onErrorResumeNext(throwable -> {
                    // still delete locally if server delete fails
                    return Completable.fromAction(() -> pupilDao.deletePupil(pupil));
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> getUnsyncedPupilsCount() {
        return pupilDao.getUnsyncedPupilCount();
    }
}