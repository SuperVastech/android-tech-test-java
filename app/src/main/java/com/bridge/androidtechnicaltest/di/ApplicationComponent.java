package com.bridge.androidtechnicaltest.di;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.bridge.androidtechnicaltest.ui.AddPupilFragment;
import com.bridge.androidtechnicaltest.ui.MainActivity;
import com.bridge.androidtechnicaltest.ui.PupilDetailFragment;
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
    void inject(App app);

    void inject(PupilListFragment fragment);
    void inject(AddPupilFragment fragment);
    void inject(PupilDetailFragment fragment);
    void inject(MainActivity mainActivity);

    PupilRepository pupilRepository();
    IPupilRepository iPupilRepository();
}