// ImageLoader.java
package com.bridge.androidtechnicaltest.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bridge.androidtechnicaltest.ui.PupilAdapter;
import com.bumptech.glide.Glide;
import com.bridge.androidtechnicaltest.R;

public class ImageLoader {

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.user_sample)
                .into(imageView);
    }

    public static void loadCircularImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.user_sample)
                .circleCrop()
                .into(imageView);
    }
}