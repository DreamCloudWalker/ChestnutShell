package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;
import com.dengjian.chestnutshell.imageloader.nutimageloader.util.BitmapDecoder;
import com.dengjian.chestnutshell.utils.LogUtil;
import com.dengjian.chestnutshell.utils.MD5Helper;
import com.dengjian.chestnutshell.utils.Utils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskCache implements IImageCache {
    private static final String TAG = "DiskCache";
    private static final int MB = 1024 * 1024;
    private volatile static DiskCache sInstance = null;

    /**
     * cache dir
     */
    private static final String IMAGE_DISK_CACHE = "cache";
    /**
     * Disk LRU Cache
     */
    private DiskLruCache mDiskLruCache;

    private DiskCache() {
    }

    /**
     * @param context only accept application context
     * @return
     */
    public static DiskCache getInstance(Context context) {
        if (null == sInstance) {
            synchronized (DiskCache.class) {
                if (null == sInstance) {
                    sInstance = new DiskCache();
                }
            }
        }

        return sInstance;
    }

    /**
     * 初始化sdcard缓存
     */
    private void initDiskCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, IMAGE_DISK_CACHE);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 50 * MB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            LogUtil.d(TAG, "### context : " + context + ", dir = " + context.getExternalCacheDir());
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public synchronized Bitmap get(final NutImageRequest request) {
        // 图片解析器
        BitmapDecoder decoder = new BitmapDecoder() {

            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                final InputStream inputStream = getInputStream(request.mImageUriMd5);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                        options);
                Utils.closeQuietly(inputStream);
                return bitmap;
            }
        };

        return decoder.decodeBitmap(request.getImageViewWidth(),
                request.getImageViewHeight());
    }

    private InputStream getInputStream(String md5) {
        DiskLruCache.Snapshot snapshot;
        try {
            snapshot = mDiskLruCache.get(md5);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(NutImageRequest request, Bitmap bmp) {
        if (request.mJustCacheInMem) {
            LogUtil.i(IMAGE_DISK_CACHE, "仅缓存在内存中");
            return;
        }

        DiskLruCache.Editor editor = null;
        try {
            // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
            editor = mDiskLruCache.edit(request.mImageUriMd5);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writeBitmapToDisk(bmp, outputStream)) {
                    // 写入disk缓存
                    editor.commit();
                } else {
                    editor.abort();
                }
                Utils.closeQuietly(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeBitmapToDisk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        boolean result = true;
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            Utils.closeQuietly(bos);
        }

        return result;
    }

    @Override
    public void remove(NutImageRequest key) {
        try {
            mDiskLruCache.remove(MD5Helper.toMD5(key.mImageUriMd5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
