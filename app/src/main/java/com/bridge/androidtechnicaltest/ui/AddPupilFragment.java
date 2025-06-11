package com.bridge.androidtechnicaltest.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.google.android.material.textfield.TextInputEditText;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.location.Location;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

public class AddPupilFragment extends Fragment {

    @Inject
    PupilRepository pupilRepository;

    @Inject
    IPupilRepository iPupilRepository;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 102;
    private FusedLocationProviderClient fusedLocationClient;


    private double latitude = 0.0;
    private double longitude = 0.0;
    private static final int IMAGE_PICK_REQUEST = 101;



    private PupilViewModel pupilViewModel;


    private PupilViewModelFactory viewModelFactory;

    private TextInputEditText editTextName, editTextCountry;
    private View imageViewProfile;
    private View btnSelectImage, btnSubmit;
    private ProgressBar progressBar;

    private String encodedImageBase64 = "";



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((App) requireActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelFactory = new PupilViewModelFactory(pupilRepository, iPupilRepository);

        pupilViewModel = new ViewModelProvider(this, viewModelFactory).get(PupilViewModel.class);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        checkLocationPermissionAndFetch();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pupil, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextCountry = view.findViewById(R.id.editTextCountry);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        progressBar = view.findViewById(R.id.progressBar);

        pupilViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSubmit.setEnabled(!isLoading);
        });

        pupilViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        btnSelectImage.setOnClickListener(v -> openImagePicker());



        btnSubmit.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String country = editTextCountry.getText().toString().trim();

            if (name.isEmpty() || country.isEmpty() || encodedImageBase64.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            Pupil newPupil = new Pupil(name, country, encodedImageBase64, latitude, longitude);
            pupilViewModel.addPupil(newPupil);

            Toast.makeText(requireContext(), "Pupil submitted!", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });



        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try (InputStream imageStream = requireContext().getContentResolver().openInputStream(imageUri)) {
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImageBase64 = encodeImageToBase64WebP(selectedImage);
                if (encodedImageBase64.length() > 1000) {
                    Toast.makeText(requireContext(), "Image is too large even after compression.", Toast.LENGTH_LONG).show();
                    encodedImageBase64 = "";
                    return;
                }

                ((android.widget.ImageView) imageViewProfile).setImageBitmap(selectedImage);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fetchLastLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        } else {
                            // fallback or keep default for continuity
                            latitude = 6.5244;  // Lagos nigeria lat
                            longitude = 3.3792; // Lagos nigeria lng
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            } else {
                Toast.makeText(requireContext(), "Location permission denied. Using default location.", Toast.LENGTH_SHORT).show();
                latitude = 6.5244;
                longitude = 3.3792;
            }
        }
    }


    private Bitmap getOptimallyScaledBitmap(Bitmap original, int targetSize) {
        int width = original.getWidth();
        int height = original.getHeight();

        float scale = Math.min((float) targetSize / width, (float) targetSize / height);

        int newWidth = Math.round(width * scale);
        int newHeight = Math.round(height * scale);

        //  Avoid overly tiny images
        newWidth = Math.max(newWidth, 8);
        newHeight = Math.max(newHeight, 8);

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
    }

    private String encodeImageToBase64WebP(Bitmap bitmap) {
        int targetSize = 40;
        Bitmap scaledBitmap = getOptimallyScaledBitmap(bitmap, targetSize);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String base64String;

        do {
            baos.reset();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                scaledBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 60, baos);
            } else {
                scaledBitmap.compress(Bitmap.CompressFormat.WEBP, 60, baos);
            }

            byte[] byteArray = baos.toByteArray();
            base64String = android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_WRAP);

            if (base64String.length() > 1000 && targetSize > 16) {
                targetSize -= 4;
                scaledBitmap.recycle();
                scaledBitmap = getOptimallyScaledBitmap(bitmap, targetSize);
            }

        } while (base64String.length() > 1000 && targetSize > 16);

        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle();
        }

        Log.d("ImageBase64Length", "WebP Final length: " + base64String.length());
        return base64String;
    }

}
