package com.dengjian.chestnutshell.imageloader.nutimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.dengjian.chestnutshell.imageloader.nutimageloader.cache.IImageCache;
import com.dengjian.chestnutshell.imageloader.nutimageloader.cache.MemoryCache;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutImageLoader {
    private static volatile NutImageLoader sInstance;
    private final ExecutorService mExecutorService;
    private IImageCache mImageCache = new MemoryCache();
    private int mLoadingImgId;
    private int mLoadingFailedImgId;


    private NutImageLoader() {
        mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public NutImageLoader getInstance() {
        if (null == sInstance) {
            synchronized (NutImageLoader.class) {
                if (null == sInstance) {
                    sInstance = new NutImageLoader();
                }
            }
        }

        return sInstance;
    }

    // 注入缓存
    public void setImageCache(IImageCache cache) {
        mImageCache = cache;
    }

    public void setLoadingFailedImgId(int loadingFailedImgId) {
        this.mLoadingFailedImgId = loadingFailedImgId;
    }

    public void setLoadingImgId(int loadingImgId) {
        this.mLoadingImgId = loadingImgId;
    }

    public void displayImage(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            return ;
        }
        Bitmap bitmap = mImageCache.get(url);
        if (null != bitmap) {
            imageView.setImageBitmap(bitmap);
            return ;
        }
        submitLoadBmpRequest(url, imageView);
    }

    private void submitLoadBmpRequest(final String url, final ImageView imageView) {
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (null == bitmap) {
                    return ;
                }
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                }
                mImageCache.put(url, bitmap);
            }
        });
    }

    // TODO check
    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
