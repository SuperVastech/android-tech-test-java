package com.bridge.androidtechnicaltest.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.utils.ImageLoader;

import java.util.List;

public class PupilAdapter extends RecyclerView.Adapter<PupilAdapter.PupilViewHolder> {

    private List<Pupil> pupils;
    private Context context;


    public void setPupils(List<Pupil> pupils) {
        this.pupils = pupils;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PupilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pupil, parent, false);
        return new PupilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PupilViewHolder holder, int position) {
        Pupil pupil = pupils.get(position);
        holder.textPupilName.setText(pupil.getName());
        holder.textPupilCountry.setText(pupil.getCountry());

        // Load image using the utility class
        ImageLoader.loadCircularImage(
                context,
                pupil.getImage(),
                holder.imagePupil
        );
    }

    @Override
    public int getItemCount() {
        return pupils == null ? 0 : pupils.size();
    }

    static class PupilViewHolder extends RecyclerView.ViewHolder {
        TextView textPupilName;
        TextView textPupilCountry;
        ImageView imagePupil;

        PupilViewHolder(View itemView) {
            super(itemView);
            textPupilName = itemView.findViewById(R.id.textPupilName);
            textPupilCountry = itemView.findViewById(R.id.textPupilCountry);
            imagePupil = itemView.findViewById(R.id.imagePupil);
        }
    }
}
