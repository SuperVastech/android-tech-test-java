package com.bridge.androidtechnicaltest.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.utils.ImageLoader;

public class PupilDetailFragment extends Fragment {

    private static final String ARG_PUPIL_ID = "pupil_id";
    private static final String ARG_PUPIL_NAME = "pupil_name";

    private static final String ARG_PUPIL_COUNTRY = "pupil_country";
    private static final String ARG_PUPIL_IMAGE = "pupil_image";
    private static final String ARG_PUPIL_LATITUDE = "pupil_latitude";
    private static final String ARG_PUPIL_LONGITUDE = "pupil_longitude";


    private static final String ARG_PUPIL_SYNCED = "pupil_synced";

    private Pupil pupil;

    public static PupilDetailFragment newInstance(Pupil pupil) {
        PupilDetailFragment fragment = new PupilDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PUPIL_ID, pupil.getPupilId());
        args.putString(ARG_PUPIL_NAME, pupil.getName());
        args.putString(ARG_PUPIL_COUNTRY, pupil.getCountry());
        args.putString(ARG_PUPIL_IMAGE, pupil.getImage());

        args.putDouble(ARG_PUPIL_LATITUDE, pupil.getLatitude() != null ? pupil.getLatitude() : 0.0);
        args.putDouble(ARG_PUPIL_LONGITUDE, pupil.getLongitude() != null ? pupil.getLongitude() : 0.0);
        args.putBoolean(ARG_PUPIL_SYNCED, pupil.isSynced());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            pupil = new Pupil(
                    getArguments().getInt(ARG_PUPIL_ID),
                    getArguments().getString(ARG_PUPIL_NAME),
                    getArguments().getString(ARG_PUPIL_COUNTRY),
                    getArguments().getString(ARG_PUPIL_IMAGE),
                    getArguments().getDouble(ARG_PUPIL_LATITUDE),
                    getArguments().getDouble(ARG_PUPIL_LONGITUDE)
            );
            pupil.setSynced(getArguments().getBoolean(ARG_PUPIL_SYNCED));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pupildetail, container, false);

        ImageView imageViewPupil = view.findViewById(R.id.imageViewPupil);
        TextView textViewName = view.findViewById(R.id.textPupilName);
        TextView textViewCountry = view.findViewById(R.id.textPupilCountry);
        TextView textViewLatitude = view.findViewById(R.id.textLatitude);
        TextView textViewLongitude = view.findViewById(R.id.textLongitude);
        TextView textViewSyncStatus = view.findViewById(R.id.textSyncStatus);
        ImageView btnBack = view.findViewById(R.id.btnBack);


        if (pupil != null) {
            textViewName.setText(pupil.getName());
            textViewCountry.setText(pupil.getCountry());
            textViewLatitude.setText(String.valueOf(pupil.getLatitude()));
            textViewLongitude.setText(String.valueOf(pupil.getLongitude()));


            if (pupil.isSynced()) {
                textViewSyncStatus.setText("Yes");
                textViewSyncStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {

                textViewSyncStatus.setText("No");
                textViewSyncStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            if (pupil.getImage() != null && !pupil.getImage().isEmpty()) {

                ImageLoader.loadCircularImage(requireContext(), pupil.getImage(), imageViewPupil);
            }

        }



        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());


        return view;
    }

}