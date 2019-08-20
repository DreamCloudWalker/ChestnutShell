package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;

public class DoubleCache implements IImageCache {
    private final IImageCache mMemoryCache = new MemoryCache();
    private final IImageCache mDiskCache;

    public DoubleCache(Context context) {
        mDiskCache = DiskCache.getInstance(context);
    }

    @Override
    public Bitmap get(NutImageRequest key) {
        Bitmap value = mMemoryCache.get(key);
        if (value == null) {
            value = mDiskCache.get(key);
            saveBitmapIntoMemory(key, value);
        }
        return value;
    }

    private void saveBitmapIntoMemory(NutImageRequest key, Bitmap bitmap) {
        // 如果Value从disk中读取,那么存入内存缓存
        if (bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @Override
    public void put(NutImageRequest key, Bitmap bmp) {
        mMemoryCache.put(key, bmp);
        mDiskCache.put(key, bmp);
    }

    @Override
    public void remove(NutImageRequest key) {
        mDiskCache.remove(key);
        mMemoryCache.remove(key);
    }
}
