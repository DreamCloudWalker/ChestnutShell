package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.graphics.Bitmap;

public interface IImageCache {
    Bitmap get(String url);
    void put(String url, Bitmap bmp);
}
