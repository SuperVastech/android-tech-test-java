package com.bridge.androidtechnicaltest.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bridge.androidtechnicaltest.db.IPupilRepository;

public class PupilViewModelFactory implements ViewModelProvider.Factory {

    private final IPupilRepository repository;

    public PupilViewModelFactory(IPupilRepository repository, IPupilRepository iPupilRepository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(PupilViewModel.class)) {
            return (T) new PupilViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
