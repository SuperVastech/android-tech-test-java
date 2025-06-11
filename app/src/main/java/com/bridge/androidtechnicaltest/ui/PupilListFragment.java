package com.bridge.androidtechnicaltest.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

public class PupilListFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private PupilViewModel viewModel;
    private TextView textPageNumber;
    private Button buttonPrevious;
    private Button buttonNext;
    private Button buttonPublish;
    private int currentPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_refresh) {

            viewModel.syncUnsyncedPupils();
            return true;
        } else if (itemId == R.id.action_reset) {

            showResetConfirmation();
            return true;
        } else {

            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((App) requireActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pupillist, container, false);

        textPageNumber = view.findViewById(R.id.textViewPageNumber);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonPublish = view.findViewById(R.id.buttonPublish);

        RecyclerView recyclerView = view.findViewById(R.id.pupil_list);

        PupilAdapter adapter = new PupilAdapter(new PupilAdapter.OnPupilClickListener() {
            @Override
            public void onPupilClick(Pupil pupil) {
                showPupilDetails(pupil);

            }

            @Override
            public void onDeleteClick(Pupil pupil) {
                showDeleteConfirmation(pupil);

            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this, viewModelFactory).get(PupilViewModel.class);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        viewModel.getPaginatedPupils().observe(getViewLifecycleOwner(), pupils -> {
            adapter.setPupils(pupils);
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fabAddPupil = view.findViewById(R.id.fabAddPupil);

        buttonPrevious.setOnClickListener(v -> viewModel.loadPreviousPage());
        buttonNext.setOnClickListener(v -> viewModel.loadNextPage());

        buttonPublish.setOnClickListener(v -> {
            showPublishConfirmation();


        });

        fabAddPupil.setOnClickListener(v -> {
            AddPupilFragment addPupilFragment = new AddPupilFragment();

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, addPupilFragment)
                    .addToBackStack(null)
                    .commit();
        });


        viewModel.getCurrentPage().observe(getViewLifecycleOwner(), page -> {
            textPageNumber.setText("Page " + page);
            buttonPrevious.setEnabled(page > 1);
        });


        viewModel.getTotalPages().observe(getViewLifecycleOwner(), totalPages -> {
            Integer currentPageValue = viewModel.getCurrentPage().getValue();
            int currentPage = currentPageValue != null ? currentPageValue : 1;
            buttonNext.setEnabled(currentPage < totalPages);

        });


        viewModel.getUnsyncedPupilsCount().observe(getViewLifecycleOwner(), count -> {
            buttonPublish.setVisibility(count > 0 ? View.VISIBLE : View.GONE);

        });

        viewModel.fetchPupils();


        return view;
    }

    private void showPublishConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Publish Pupils")
                .setMessage("This will sync all unsynced pupils to the server. Continue?")
                .setPositiveButton("Publish", (dialog, which) -> {
                    viewModel.syncUnsyncedPupils();

                    Toast.makeText(requireContext(), "Publishing pupils...", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showResetConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Reset Data")
                .setMessage("This will clear all unsynced pupils. Are you sure?")
                .setPositiveButton("Reset", (dialog, which) -> {
                    viewModel.clearUnsyncedPupils();
                    Toast.makeText(requireContext(), "Unsynced pupils cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showPupilDetails(Pupil pupil) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.container, PupilDetailFragment.newInstance(pupil))
                .addToBackStack(null)
                .commit();
    }

    private void showDeleteConfirmation(Pupil pupil) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Pupil")
                .setMessage("Are you sure you want to delete " + pupil.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deletePupil(pupil);
                    Toast.makeText(requireContext(), "Deleting " + pupil.getName(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}