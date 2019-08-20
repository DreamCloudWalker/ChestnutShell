package com.dengjian.chestnutshell.imageloader.nutimageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.dengjian.chestnutshell.imageloader.nutimageloader.NutImageLoader;
import com.dengjian.chestnutshell.imageloader.nutimageloader.cache.IImageCache;
import com.dengjian.chestnutshell.imageloader.nutimageloader.config.DisplayConfig;
import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;
import com.dengjian.chestnutshell.utils.LogUtil;

public abstract class AbsLoader implements Loader {
    private static IImageCache mCache = NutImageLoader.getInstance().getConfig().mImageCache;

    @Override
    public final void loadImage(NutImageRequest request) {
        Bitmap resultBitmap = mCache.get(request);
        LogUtil.i("", "### 是否有缓存 : " + resultBitmap + ", uri = " + request.mImageUri);
        if (resultBitmap == null) {
            showLoading(request);
            resultBitmap = onLoadImage(request);
            cacheBitmap(request, resultBitmap);
        } else {
            request.mJustCacheInMem = true;
        }

        deliveryToUIThread(request, resultBitmap);
    }

    /**
     * @param result
     * @return
     */
    protected abstract Bitmap onLoadImage(NutImageRequest result);

    /**
     * @param request
     * @param bitmap
     */
    private void cacheBitmap(NutImageRequest request, Bitmap bitmap) {
        // 缓存新的图片
        if (bitmap != null && mCache != null) {
            synchronized (mCache) {
                mCache.put(request, bitmap);
            }
        }
    }

    /**
     * 显示加载中的视图,注意这里也要判断imageview的tag与image uri的相等性,否则逆序加载时出现问题
     *
     * @param request
     */
    protected void showLoading(final NutImageRequest request) {
        final ImageView imageView = request.getImageView();
        if (request.isImageViewTagValid()
                && hasLoadingPlaceholder(request.mDisplayConfig)) {
            imageView.post(new Runnable() {

                @Override
                public void run() {
                    imageView.setImageResource(request.mDisplayConfig.loadingResId);
                }
            });
        }
    }

    /**
     * 将结果投递到UI,更新ImageView
     *
     * @param request
     * @param bitmap
     */
    protected void deliveryToUIThread(final NutImageRequest request,
                                      final Bitmap bitmap) {
        final ImageView imageView = request.getImageView();
        if (imageView == null) {
            return;
        }
        imageView.post(new Runnable() {
            @Override
            public void run() {
                updateImageView(request, bitmap);
            }
        });
    }

    /**
     * 更新ImageView
     *
     * @param request
     * @param result
     */
    private void updateImageView(NutImageRequest request, Bitmap result) {
        final ImageView imageView = request.getImageView();
        final String uri = request.mImageUri;
        if (result != null && imageView.getTag().equals(uri)) {
            imageView.setImageBitmap(result);
        }

        // 加载失败
        if (result == null && hasFaildPlaceholder(request.mDisplayConfig)) {
            imageView.setImageResource(request.mDisplayConfig.failedResId);
        }

        // 回调接口
        if (request.mImageListener != null) {
            request.mImageListener.onComplete(imageView, result, uri);
        }
    }

    private boolean hasLoadingPlaceholder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.loadingResId > 0;
    }

    private boolean hasFaildPlaceholder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.failedResId > 0;
    }
}
