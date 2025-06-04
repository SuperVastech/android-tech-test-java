package com.bridge.androidtechnicaltest.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.bridge.androidtechnicaltest.db.PupilRepository;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class SyncPupilsWorker extends Worker {

    private final PupilRepository pupilRepository;

    public SyncPupilsWorker(@NonNull Context context,
                            @NonNull WorkerParameters params,
                            @NonNull PupilRepository pupilRepository) {
        super(context, params);
        this.pupilRepository = pupilRepository;
    }

    @NonNull
    @Override
    public Result doWork() {
        Completable completable = pupilRepository.syncUnsyncedPupils();
        try {
            completable.subscribeOn(Schedulers.io()).blockingAwait();
            return Result.success();
        } catch (Exception e) {
            return Result.retry();
        }
    }
}
