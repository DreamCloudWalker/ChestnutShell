package com.dengjian.chestnutshell.imageloader.nutimageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;
import com.dengjian.chestnutshell.imageloader.nutimageloader.util.BitmapDecoder;

import java.io.File;

public class LocalLoader extends AbsLoader {
    @Override
    public Bitmap onLoadImage(NutImageRequest request) {
        final String imagePath = Uri.parse(request.mImageUri).getPath();
        final File imgFile = new File(imagePath);
        if (!imgFile.exists()) {
            return null;
        }

        // 从sd卡中加载的图片仅缓存到内存中,不做本地缓存
        request.mJustCacheInMem = true;

        // 加载图片
        BitmapDecoder decoder = new BitmapDecoder() {

            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(imagePath, options);
            }
        };

        return decoder.decodeBitmap(request.getImageViewWidth(),
                request.getImageViewHeight());
    }
}
