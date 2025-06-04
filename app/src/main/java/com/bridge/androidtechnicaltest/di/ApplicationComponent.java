package com.bridge.androidtechnicaltest.di;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.bridge.androidtechnicaltest.ui.AddPupilFragment;
import com.bridge.androidtechnicaltest.ui.MainActivity;
import com.bridge.androidtechnicaltest.ui.PupilListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        DatabaseModule.class,
        ViewModelModule.class
})
public interface ApplicationComponent {
    // Add this method to inject dependencies into the App class
    void inject(App app);

    void inject(PupilListFragment fragment);
    void inject(AddPupilFragment fragment);
    void inject(MainActivity mainActivity);

    PupilRepository pupilRepository();
    IPupilRepository iPupilRepository();
}