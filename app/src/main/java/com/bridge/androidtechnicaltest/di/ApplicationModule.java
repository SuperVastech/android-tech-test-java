package com.bridge.androidtechnicaltest.di;

import android.content.Context;

import com.bridge.androidtechnicaltest.db.AppDatabase;
import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.bridge.androidtechnicaltest.network.PupilService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Context applicationContext;

    public ApplicationModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    public Context provideApplicationContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    PupilRepository providePupilRepository(AppDatabase db, PupilService service, Context context) {
        return new PupilRepository(db, service);
    }

    @Provides
    @Singleton
    IPupilRepository provideIPupilRepository(PupilRepository repo) {
        return repo; // PupilRepository implements IPupilRepository
    }

}
