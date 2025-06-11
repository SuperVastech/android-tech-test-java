package com.bridge.androidtechnicaltest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.bridge.androidtechnicaltest.di.ApplicationComponent;
import com.bridge.androidtechnicaltest.di.ApplicationModule;
import com.bridge.androidtechnicaltest.di.ApplicationComponent;
import com.bridge.androidtechnicaltest.di.DaggerApplicationComponent;
import com.bridge.androidtechnicaltest.sync.SyncPupilsWorkerFactory;

import javax.inject.Inject;

public class App extends Application implements Configuration.Provider {

    private ApplicationComponent applicationComponent;

    private SyncPupilsWorkerFactory workerFactory;

    @Inject
    PupilRepository pupilRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);

        workerFactory = new SyncPupilsWorkerFactory(pupilRepository);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }


}
