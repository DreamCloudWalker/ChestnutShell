package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements IImageCache {
    public static final int BYTES_PER_K = 1024;
    private final LruCache<String, Bitmap> mCache;

    public MemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / BYTES_PER_K);
        final int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / BYTES_PER_K;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        return mCache.get(url);
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mCache.put(url, bmp);
    }
}
