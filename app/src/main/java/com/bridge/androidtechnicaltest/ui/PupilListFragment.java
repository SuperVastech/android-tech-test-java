package com.bridge.androidtechnicaltest.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.R;

import javax.inject.Inject;

public class PupilListFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private PupilViewModel viewModel;

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

        RecyclerView recyclerView = view.findViewById(R.id.pupil_list);
        PupilAdapter adapter = new PupilAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this, viewModelFactory).get(PupilViewModel.class);

        viewModel.getPupils().observe(getViewLifecycleOwner(), pupils -> {
            adapter.setPupils(pupils);
        });

        return view;
    }

}
