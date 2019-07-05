package com.dengjian.chestnutshell.imageloader.nutimageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dengjian.chestnutshell.utils.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DiskCache implements IImageCache {
    private static final String mCacheDir = "/mnt/sdcard/nutimageloader/cache/";    // TODO

    @Override
    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(mCacheDir + url);
    }

    @Override
    public void put(String url, Bitmap bmp) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mCacheDir + url);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(fos);
        }
    }
}
