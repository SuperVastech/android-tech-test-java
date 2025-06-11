package com.bridge.androidtechnicaltest.di;




import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bridge.androidtechnicaltest.ui.PupilViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PupilViewModel.class)
    abstract ViewModel bindPupilListViewModel(PupilViewModel viewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
