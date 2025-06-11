package com.bridge.androidtechnicaltest.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PupilViewModel extends ViewModel {

    private final IPupilRepository pupilRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<Pupil>> allPupilsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Pupil>> paginatedPupilsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalPagesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> unsyncedPupilsCountLiveData = new MutableLiveData<>();

    private int currentPage = 1;
    private int totalPages = 1;
    private static final int PUPILS_PER_PAGE = 5;

    @Inject
    public PupilViewModel(IPupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;

        currentPageLiveData.setValue(1);


        totalPagesLiveData.setValue(1);
        unsyncedPupilsCountLiveData.setValue(0);
    }

    public LiveData<List<Pupil>> getPupils() {
        return allPupilsLiveData;
    }

    public LiveData<List<Pupil>> getPaginatedPupils() {
        return paginatedPupilsLiveData;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Integer> getCurrentPage() {
        return currentPageLiveData;
    }

    public LiveData<Integer> getTotalPages() {
        return totalPagesLiveData;
    }

    public LiveData<Integer> getUnsyncedPupilsCount() {
        return unsyncedPupilsCountLiveData;
    }

    public void fetchPupils() {
        currentPage = 1;

        currentPageLiveData.setValue(currentPage);

        loading.postValue(true);
        disposable.add(
                pupilRepository.syncPupilsFromRemote(1)
                        .andThen(pupilRepository.getLocalPupils())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pupils -> {
                                    loading.setValue(false);
                                    allPupilsLiveData.setValue(pupils);


                                    List<Pupil> firstPagePupils = getPupilsForPage(pupils, 1);
                                    paginatedPupilsLiveData.setValue(firstPagePupils);

                                    totalPages = 5;

                                    totalPagesLiveData.setValue(totalPages);

                                    checkUnsyncedPupils();
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to fetch pupils: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void updatePagination(List<Pupil> allPupils) {
        if (allPupils == null || allPupils.isEmpty()) {
            totalPages = 1;
            totalPagesLiveData.setValue(totalPages);
            paginatedPupilsLiveData.setValue(new ArrayList<>());
            return;
        }



        totalPages = (int) Math.ceil((double) allPupils.size() / PUPILS_PER_PAGE);
        totalPagesLiveData.setValue(totalPages);


        List<Pupil> currentPagePupils = getPupilsForPage(allPupils, currentPage);
        paginatedPupilsLiveData.setValue(currentPagePupils);
    }

    private List<Pupil> getPupilsForPage(List<Pupil> allPupils, int page) {
        int startIndex = (page - 1) * PUPILS_PER_PAGE;
        int endIndex = Math.min(startIndex + PUPILS_PER_PAGE, allPupils.size());

        if (startIndex >= allPupils.size()) {
            return new ArrayList<>();
        }

        return allPupils.subList(startIndex, endIndex);
    }

    public void loadNextPage() {
        if (currentPage < totalPages) {
            currentPage++;

            currentPageLiveData.setValue(currentPage);
            loadPageFromServer(currentPage);
        }
    }

    public void loadPreviousPage() {
        if (currentPage > 1) {
            currentPage--;

            currentPageLiveData.setValue(currentPage);
            loadPageFromServer(currentPage);
        }
    }

    private void loadPageFromServer(int page) {
        loading.postValue(true);
        disposable.add(
                pupilRepository.syncPupilsFromRemote(page)
                        .andThen(pupilRepository.getLocalPupils())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pupils -> {
                                    loading.setValue(false);

                                    int startIndex = (page - 1) * PUPILS_PER_PAGE;
                                    int endIndex = Math.min(startIndex + PUPILS_PER_PAGE, pupils.size());

                                    List<Pupil> currentPagePupils = new ArrayList<>();

                                    if (startIndex < pupils.size()) {
                                        currentPagePupils = pupils.subList(startIndex, endIndex);
                                    }

                                    paginatedPupilsLiveData.setValue(currentPagePupils);

                                    allPupilsLiveData.setValue(pupils);
                                    checkUnsyncedPupils();
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to load page " + page + ": " + throwable.getMessage());
                                }
                        )
        );
    }

    private void checkUnsyncedPupils() {
        disposable.add(
                pupilRepository.getUnsyncedPupilsCount()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                count -> unsyncedPupilsCountLiveData.setValue(count),
                                throwable -> {

                                    unsyncedPupilsCountLiveData.setValue(0);
                                }
                        )
        );
    }

    public void addPupil(Pupil pupil) {
        loading.postValue(true);
        disposable.add(
                pupilRepository.addPupil(pupil)
                        .andThen(pupilRepository.syncUnsyncedPupils())
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


                                    fetchPupils();
                                }
                        )
        );
    }

    public void syncUnsyncedPupils() {
        loading.postValue(true);
        disposable.add(
                pupilRepository.syncUnsyncedPupils()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    loading.setValue(false);
                                    fetchPupils();

                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to sync pupils: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void clearUnsyncedPupils() {
        loading.postValue(true);
        disposable.add(
                pupilRepository.clearUnsyncedPupils()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {


                                    loading.setValue(false);

                                    fetchPupils();
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to clear unsynced pupils: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void refreshFromServer() {
        currentPage = 1;
        loading.postValue(true);
        disposable.add(
                pupilRepository.clearAndSyncFromRemote()
                        .andThen(pupilRepository.getLocalPupils())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pupils -> {
                                    loading.setValue(false);
                                    allPupilsLiveData.setValue(pupils);
                                    updatePagination(pupils);
                                    checkUnsyncedPupils();
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    error.setValue("Failed to refresh from server: " + throwable.getMessage());
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

    public void loadMorePupils() {
        currentPage++;
        loading.postValue(true);
        disposable.add(
                pupilRepository.loadMorePupils(currentPage)
                        .andThen(pupilRepository.getLocalPupils())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pupils -> {
                                    loading.setValue(false);
                                    allPupilsLiveData.setValue(pupils);
                                    updatePagination(pupils);
                                },
                                throwable -> {
                                    loading.setValue(false);
                                    currentPage--;
                                    error.setValue("Failed to load more pupils: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void loadPupilsPage(int page) {
        currentPage = page;
        loading.postValue(true);
        disposable.add(
                pupilRepository.syncPupilsFromRemote(page)
                        .andThen(pupilRepository.getLocalPupils())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pupils -> {
                                    loading.setValue(false);

                                    allPupilsLiveData.setValue(pupils);
                                    currentPageLiveData.setValue(currentPage);

                                    updatePagination(pupils);
                                },
                                throwable -> {
                                    loading.setValue(false);

                                    error.setValue("Failed to load page " + page + ": " + throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}