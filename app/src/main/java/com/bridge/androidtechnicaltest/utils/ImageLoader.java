// ImageLoader.java - Improved version
package com.bridge.androidtechnicaltest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bridge.androidtechnicaltest.R;

public class ImageLoader {

    private static final String TAG = "ImageLoader";

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.user_sample)
                .into(imageView);
    }

    public static void loadCircularImage(Context context, String imageData, ImageView imageView) {
        if (imageData == null || imageData.isEmpty()) {
            Log.d(TAG, "Image data is null or empty, showing placeholder");
            imageView.setImageResource(R.drawable.ic_placeholder);
            return;
        }

        Log.d(TAG, "Loading image, data length: " + imageData.length());
        Log.d(TAG, "Image data preview: " + imageData.substring(0, Math.min(50, imageData.length())));

        if (isBase64Image(imageData)) {
            Log.d(TAG, "Detected base64 image");
            loadBase64Image(context, imageData, imageView);
        } else {
            Log.d(TAG, "Loading as regular URL");
            // Regular URL loading
            Glide.with(context)
                    .load(imageData)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.user_sample)
                    .circleCrop()
                    .into(imageView);
        }
    }

    private static boolean isBase64Image(String imageData) {
        return imageData.startsWith("data:image/") ||
                imageData.startsWith("/9j/") ||
                imageData.startsWith("iVBORw0KGgo") ||
                imageData.startsWith("UklGR") ||
                isValidBase64(imageData);
    }

    private static boolean isValidBase64(String str) {
        try {

            Base64.decode(str.substring(0, Math.min(100, str.length())), Base64.NO_WRAP);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void loadBase64Image(Context context, String base64Data, ImageView imageView) {
        try {
            byte[] decodedBytes;

            if (base64Data.startsWith("data:image/")) {
                String base64 = base64Data.substring(base64Data.indexOf(",") + 1);
                decodedBytes = Base64.decode(base64, Base64.NO_WRAP);
            } else {
                decodedBytes = Base64.decode(base64Data, Base64.NO_WRAP);
            }

            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap != null) {
                Log.d(TAG, "Successfully decoded base64 to bitmap: " + bitmap.getWidth() + "x" + bitmap.getHeight());
                Glide.with(context)
                        .load(bitmap)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.user_sample)
                        .circleCrop()
                        .into(imageView);
            } else {
                Log.e(TAG, "Failed to decode base64 to bitmap");
                imageView.setImageResource(R.drawable.user_sample);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading base64 image: " + e.getMessage(), e);
            imageView.setImageResource(R.drawable.user_sample);
        }
    }
}