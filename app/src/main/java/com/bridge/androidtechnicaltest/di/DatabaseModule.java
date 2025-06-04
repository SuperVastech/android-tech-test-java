package com.bridge.androidtechnicaltest.di;

import android.content.Context;

import androidx.room.Room;

import com.bridge.androidtechnicaltest.db.AppDatabase;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.bridge.androidtechnicaltest.network.PupilService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private static final String APP_DATABASE = "bridgetest.db";

    @Provides
    @Singleton
    public AppDatabase provideLocalGameDatabase(Context applicationContext) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class, APP_DATABASE).build();
    }

    @Provides
    public PupilDao providePupilDao(AppDatabase localGameDatabase) {
        return localGameDatabase.getPupilDao();
    }
}
