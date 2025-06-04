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
    @ViewModelKey(PupilViewModel.class) // Replace with your actual ViewModel class
    abstract ViewModel bindPupilListViewModel(PupilViewModel viewModel);

    // Bind other ViewModels here similarly
    // @Binds
    // @IntoMap
    // @ViewModelKey(OtherViewModel.class)
    // abstract ViewModel bindOtherViewModel(OtherViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
