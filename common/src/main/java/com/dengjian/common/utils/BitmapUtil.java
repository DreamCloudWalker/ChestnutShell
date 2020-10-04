package com.dengjian.common.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class BitmapUtil {
    private static final Canvas CANVAS = new Canvas();

    public static Bitmap createBitmapFromView(View view) {
        view.clearFocus();
        Bitmap bitmap = createBitmapSafely(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888, 1);
        if (null != bitmap) {
            synchronized (CANVAS) {
                CANVAS.setBitmap(bitmap);
                view.draw(CANVAS);
                CANVAS.setBitmap(null);
            }
        }

        return bitmap;
    }

    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config,
            int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
            return null;
        }
    }
}
