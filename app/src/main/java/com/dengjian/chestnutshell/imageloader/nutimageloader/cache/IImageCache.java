package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.graphics.Bitmap;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;

public interface IImageCache {
    Bitmap get(NutImageRequest key);
    void put(NutImageRequest key, Bitmap bmp);
    void remove(NutImageRequest key);
}
