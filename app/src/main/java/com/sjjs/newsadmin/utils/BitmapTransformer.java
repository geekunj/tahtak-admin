package com.sjjs.newsadmin.utils;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

;

/**
 * Created by nikun on 02-08-2017.
 */

public class BitmapTransformer extends BitmapTransformation {

    private final int maxWidth;
    private final int maxHeight;

    public BitmapTransformer(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int targetWidth, targetHeight;
        double aspectRatio;

        if (toTransform.getWidth() > toTransform.getHeight()) {
            targetWidth = maxWidth;
            aspectRatio = (double) toTransform.getHeight() / (double) toTransform.getWidth();
            targetHeight = (int) (targetWidth * aspectRatio);
        } else {
            targetHeight = maxHeight;
            aspectRatio = (double) toTransform.getWidth() / (double) toTransform.getHeight();
            targetWidth = (int) (targetHeight * aspectRatio);
        }

        Bitmap result = Bitmap.createScaledBitmap(toTransform, targetWidth, targetHeight, false);
        if (result != toTransform) {
            toTransform.recycle();
        }
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
