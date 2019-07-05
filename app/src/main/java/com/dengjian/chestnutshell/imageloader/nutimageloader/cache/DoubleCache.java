package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.graphics.Bitmap;

public class DoubleCache implements IImageCache {
    private final IImageCache mMemoryCache = new MemoryCache();
    private final IImageCache mDiskCache = new DiskCache();

    @Override
    public Bitmap get(String url) {
        Bitmap ret = mMemoryCache.get(url);
        if (null == ret) {
            ret = mDiskCache.get(url);
        }
        return ret;
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mMemoryCache.put(url, bmp);
        mDiskCache.put(url, bmp);
    }
}
