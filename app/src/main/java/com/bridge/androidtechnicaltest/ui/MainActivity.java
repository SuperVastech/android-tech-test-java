package com.bridge.androidtechnicaltest.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.db.PupilList;
import com.bridge.androidtechnicaltest.network.PupilService;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Inject
    PupilDao pupilDao;
    @Inject
    PupilService pupilService;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private PupilViewModel pupilViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_main);

        pupilViewModel = new ViewModelProvider(this, viewModelFactory).get(PupilViewModel.class);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.container, new PupilListFragment())
                    .commit();
        }
    }





    private void addPupil() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, new AddPupilFragment())
                .addToBackStack(null)
                .commit();
    }

    private void resetApiData() {

    }

    private void onDataResetFailed() {
        Snackbar.make(findViewById(R.id.main_layout),
                R.string.data_reset_failed, Snackbar.LENGTH_SHORT).show();
    }

    private void onDataReset() {
        Snackbar.make(findViewById(R.id.main_layout),
                R.string.data_reset, Snackbar.LENGTH_SHORT).show();
    }
}
