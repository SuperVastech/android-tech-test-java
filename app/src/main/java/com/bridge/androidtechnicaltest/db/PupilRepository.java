package com.bridge.androidtechnicaltest.db;

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

    public Single<List<Pupil>> getLocalPupils() {
        return pupilDao.getPupils().subscribeOn(Schedulers.io());
    }

    public Completable syncPupilsFromRemote(int page) {
        return pupilService.getAllPupils(page, requestId(), userAgent())
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(remotePupils -> Completable.fromAction(() -> {
                    for (Pupil p : remotePupils) p.isSynced = true;
                    pupilDao.insertAll(remotePupils);
                }));
    }

    public Completable syncUnsyncedPupils() {
        return pupilDao.getUnsyncedPupils()
                .flatMapObservable(Observable::fromIterable)
                .flatMapCompletable(pupil ->
                        pupilService.addPupil(pupil, requestId(), userAgent())
                                .andThen(Completable.fromAction(() -> {
                                    pupil.isSynced = true;
                                    pupilDao.insertPupil(pupil);
                                }))
                ).subscribeOn(Schedulers.io());
    }

    public Single<Pupil> getPupilById(int id) {
        return pupilDao.getPupilById(id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<PupilList> getOrFetchPupils() {
        return pupilDao.getPupils()
                .flatMap(localPupils -> {
                    if (!localPupils.isEmpty()) {
                        return Single.just(new PupilList(localPupils));
                    } else {
                        return pupilService.getAllPupils(1, requestId(), userAgent())
                                .flatMap(remotePupils -> {
                                    for (Pupil p : remotePupils) p.isSynced = true;
                                    pupilDao.insertAll(remotePupils);
                                    return Single.just(new PupilList(remotePupils));
                                });
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable addPupil(Pupil pupil) {
        pupil.setSynced(false);
        // Create RxJava wrapper since DAO insert() now returns void
        return Completable.fromAction(() -> pupilDao.insert(pupil))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deletePupil(Pupil pupil) {
        // Create RxJava wrapper since DAO delete() now returns void
        return Completable.fromAction(() -> pupilDao.delete(pupil))
                .subscribeOn(Schedulers.io());
    }
}