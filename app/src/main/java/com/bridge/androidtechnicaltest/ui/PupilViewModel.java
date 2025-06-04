package com.bridge.androidtechnicaltest.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilList;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PupilViewModel extends ViewModel {

    private final IPupilRepository pupilRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<Pupil>> pupilsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    public PupilViewModel(IPupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;
    }



    public LiveData<List<Pupil>> getPupils() {
        return pupilsLiveData;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchPupils() {
        loading.postValue(true);
        disposable.add(
                pupilRepository.getOrFetchPupils()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (PupilList list) -> {
                                    loading.setValue(false);
                                    pupilsLiveData.setValue(list.getPupilList());
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to fetch pupils: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void addPupil(Pupil pupil) {
        loading.postValue(true);
        disposable.add(
                pupilRepository.addPupil(pupil)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    loading.setValue(false);
                                    fetchPupils();
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to add pupil: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void deletePupil(Pupil pupil) {
        loading.postValue(true);
        disposable.add(
                pupilRepository.deletePupil(pupil)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    loading.setValue(false);
                                    fetchPupils();
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to delete pupil: " + throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        // Optionally log
        // Log.d("PupilViewModel", "Cleared disposables.");
    }
}
