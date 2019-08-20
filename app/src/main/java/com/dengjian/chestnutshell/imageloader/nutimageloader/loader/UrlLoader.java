package com.dengjian.chestnutshell.imageloader.nutimageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;
import com.dengjian.chestnutshell.utils.Utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlLoader extends AbsLoader {
    @Override
    public Bitmap onLoadImage(NutImageRequest request) {
        final String imageUrl = request.mImageUri;
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap = null ;
        HttpURLConnection conn = null ;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            bitmap =  BitmapFactory.decodeStream(is, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(is);
            Utils.closeQuietly(fos);
            if (conn != null) {
                // 关闭流
                conn.disconnect();
            }
        }
        return bitmap;
    }
}
