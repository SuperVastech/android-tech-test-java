package com.bridge.androidtechnicaltest.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import com.bridge.androidtechnicaltest.db.PupilRepository;

public class SyncPupilsWorkerFactory extends WorkerFactory {

    private final PupilRepository pupilRepository;

    public SyncPupilsWorkerFactory(PupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context context,
                                         @NonNull String workerClassName,
                                         @NonNull WorkerParameters workerParameters) {
        if (workerClassName.equals(SyncPupilsWorker.class.getName())) {
            return new SyncPupilsWorker(context, workerParameters, pupilRepository);
        }


        return null;
    }
}
