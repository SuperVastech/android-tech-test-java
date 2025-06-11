package com.bridge.androidtechnicaltest.db;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface IPupilRepository {
    Single<PupilList> getOrFetchPupils();
    Completable addPupil(Pupil pupil);
    Completable deletePupil(Pupil pupil);
    Single<List<Pupil>> getLocalPupils();
    Completable syncPupilsFromRemote(int page);
    Completable clearAndSyncFromRemote();
    Completable clearUnsyncedPupils();

    Completable loadMorePupils(int page);
    Completable syncUnsyncedPupils();
    Single<Pupil> getPupilById(int id);
    Single<Integer> getUnsyncedPupilsCount();
}