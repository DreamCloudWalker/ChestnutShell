package com.dengjian.chestnutshell.imageloader.nutimageloader.request;

import android.widget.ImageView;

import com.dengjian.chestnutshell.imageloader.nutimageloader.NutImageLoader;
import com.dengjian.chestnutshell.imageloader.nutimageloader.config.DisplayConfig;
import com.dengjian.chestnutshell.imageloader.nutimageloader.policy.LoadPolicy;
import com.dengjian.chestnutshell.imageloader.nutimageloader.util.ImageViewUtil;
import com.dengjian.chestnutshell.utils.MD5Helper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 网络请求类. 注意GET和DELETE不能传递参数,因为其请求的性质所致,用户可以将参数构建到url后传递进来到Request中.
 *
 */
public class NutImageRequest implements Comparable<NutImageRequest> {
    Reference<ImageView> mImageViewRef;
    public DisplayConfig mDisplayConfig;
    public NutImageLoader.ImageListener mImageListener;
    public String mImageUri = "";
    public String mImageUriMd5 = "";
    /**
     * 请求序列号
     */
    public int mSerialNum = 0;
    /**
     * 是否取消该请求
     */
    public boolean mIsCancel = false;
    public boolean mJustCacheInMem = false;

    /**
     * 加载策略
     */
    LoadPolicy mLoadPolicy = NutImageLoader.getInstance().getConfig().mLoadPolicy;

    /**
     * @param imageView
     * @param uri
     * @param config
     * @param listener
     */
    public NutImageRequest(ImageView imageView, String uri, DisplayConfig config,
                         NutImageLoader.ImageListener listener) {
        mImageViewRef = new WeakReference<ImageView>(imageView);
        mDisplayConfig = config;
        mImageListener = listener;
        mImageUri = uri;
        imageView.setTag(uri);
        mImageUriMd5 = MD5Helper.toMD5(mImageUri);
    }

    /**
     * @param policy
     */
    public void setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            mLoadPolicy = policy;
        }
    }

    /**
     * 判断imageview的tag与uri是否相等
     *
     * @return
     */
    public boolean isImageViewTagValid() {
        return mImageViewRef.get() != null ? mImageViewRef.get().getTag().equals(mImageUri) : false;
    }

    public ImageView getImageView() {
        return mImageViewRef.get();
    }

    public int getImageViewWidth() {
        return ImageViewUtil.getImageViewWidth(mImageViewRef.get());
    }

    public int getImageViewHeight() {
        return ImageViewUtil.getImageViewHeight(mImageViewRef.get());
    }

    @Override
    public int compareTo(NutImageRequest another) {
        return mLoadPolicy.compare(this, another);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mImageUri == null) ? 0 : mImageUri.hashCode());
        result = prime * result + ((mImageViewRef == null) ? 0 : mImageViewRef.get().hashCode());
        result = prime * result + mSerialNum;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NutImageRequest other = (NutImageRequest) obj;
        if (mImageUri == null) {
            if (other.mImageUri != null)
                return false;
        } else if (!mImageUri.equals(other.mImageUri))
            return false;
        if (mImageViewRef == null) {
            if (other.mImageViewRef != null)
                return false;
        } else if (!mImageViewRef.get().equals(other.mImageViewRef.get()))
            return false;
        if (mSerialNum != other.mSerialNum)
            return false;
        return true;
    }
}
