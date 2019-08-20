package com.dengjian.chestnutshell.imageloader.nutimageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.dengjian.chestnutshell.imageloader.nutimageloader.cache.IImageCache;
import com.dengjian.chestnutshell.imageloader.nutimageloader.cache.MemoryCache;
import com.dengjian.chestnutshell.imageloader.nutimageloader.config.DisplayConfig;
import com.dengjian.chestnutshell.imageloader.nutimageloader.config.NutImageLoaderConfig;
import com.dengjian.chestnutshell.imageloader.nutimageloader.policy.SerialPolicy;
import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;
import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequestQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutImageLoader {
    private static volatile NutImageLoader sInstance;
    private final ExecutorService mExecutorService;
    private IImageCache mImageCache = new MemoryCache();
    private NutImageRequestQueue mRequestQueue;
    private NutImageLoaderConfig mConfig;
    /**
     * 图片加载Listener
     */
    public static interface ImageListener {
        public void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }

    private NutImageLoader() {
        mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static NutImageLoader getInstance() {
        if (null == sInstance) {
            synchronized (NutImageLoader.class) {
                if (null == sInstance) {
                    sInstance = new NutImageLoader();
                }
            }
        }

        return sInstance;
    }

    public void init(NutImageLoaderConfig config) {
        mConfig = config;
        mImageCache = mConfig.mImageCache;
        checkConfig();
        mRequestQueue = new NutImageRequestQueue(mConfig.mThreadCount);
        mRequestQueue.start();
    }

    private void checkConfig() {
        if (mConfig == null) {
            throw new RuntimeException(
                    "The config of SimpleImageLoader is Null, please call the init(ImageLoaderConfig config) method to initialize");
        }

        if (mConfig.mLoadPolicy == null) {
            mConfig.mLoadPolicy = new SerialPolicy();
        }
        if (mImageCache == null) {
            mImageCache = new MemoryCache();
        }
    }

    public void displayImage(ImageView imageView, String url) {
        displayImage(imageView, url, null, null);
    }

    public void displayImage(ImageView imageView, String url, DisplayConfig config) {
        displayImage(imageView, url, config, null);
    }

    public void displayImage(ImageView imageView, String url, ImageListener listener) {
        displayImage(imageView, url, null, listener);
    }

    public void displayImage(final ImageView imageView, final String url,
                             final DisplayConfig config, final ImageListener listener) {
        NutImageRequest request = new NutImageRequest(imageView, url, config, listener);
        request.mDisplayConfig = (null != request.mDisplayConfig) ? request.mDisplayConfig : mConfig.mDisplayConfig;
        mRequestQueue.addRequest(request);
    }

    public NutImageLoaderConfig getConfig() {
        return mConfig;
    }

    public void stop() {
        mRequestQueue.stop();
    }
}
