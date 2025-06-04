package com.bridge.androidtechnicaltest.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.container, new PupilListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            resetApiData();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
