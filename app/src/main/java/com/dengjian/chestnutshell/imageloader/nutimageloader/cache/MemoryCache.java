package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;

public class MemoryCache implements IImageCache {
    public static final int KB = 1024;
    private final LruCache<String, Bitmap> mCache;

    public MemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / KB);
        final int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / KB;
            }
        };
    }

    @Override
    public Bitmap get(NutImageRequest key) {
        return mCache.get(key.mImageUri);
    }

    @Override
    public void put(NutImageRequest key, Bitmap bmp) {
        mCache.put(key.mImageUri, bmp);
    }

    @Override
    public void remove(NutImageRequest key) {
        mCache.remove(key.mImageUri);
    }
}
