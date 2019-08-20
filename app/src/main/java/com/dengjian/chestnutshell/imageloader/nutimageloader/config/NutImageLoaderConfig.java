package com.dengjian.chestnutshell.imageloader.nutimageloader.config;

import com.dengjian.chestnutshell.imageloader.nutimageloader.cache.IImageCache;
import com.dengjian.chestnutshell.imageloader.nutimageloader.cache.MemoryCache;
import com.dengjian.chestnutshell.imageloader.nutimageloader.policy.LoadPolicy;
import com.dengjian.chestnutshell.imageloader.nutimageloader.policy.SerialPolicy;

public class NutImageLoaderConfig {
    public IImageCache mImageCache = new MemoryCache();
    public int mThreadCount = Runtime.getRuntime().availableProcessors() + 1;
    public LoadPolicy mLoadPolicy = new SerialPolicy(); // 加载策略
    public DisplayConfig mDisplayConfig = new DisplayConfig();  // loading失败时图片配置对象

    private NutImageLoaderConfig() {

    }

    public static class Builder {
        IImageCache mBitmapCache = new MemoryCache();
        private DisplayConfig mDisplayConfig = new DisplayConfig();  // loading失败时图片配置对象
        private LoadPolicy mLoadPolicy = new SerialPolicy(); // 加载策略
        private int mThreadCount = Runtime.getRuntime().availableProcessors() + 1;

        public Builder setThreadCount(int count) {
            mThreadCount = Math.max(1, count);
            return this;
        }

        public Builder setCache(IImageCache cache) {
            mBitmapCache = cache;
            return this;
        }

        public Builder setLoadingPlaceholder(int resId) {
            mDisplayConfig.loadingResId = resId;
            return this;
        }

        public Builder setNotFoundPlaceholder(int resId) {
            mDisplayConfig.failedResId = resId;
            return this;
        }

        public Builder setLoadPolicy(LoadPolicy policy) {
            if (null != policy) {
                mLoadPolicy = policy;
            }
            return this;
        }

        void applyConfig(NutImageLoaderConfig config) {
            config.mImageCache = this.mBitmapCache;
            config.mDisplayConfig = this.mDisplayConfig;
            config.mLoadPolicy = this.mLoadPolicy;
            config.mThreadCount = this.mThreadCount;
        }

        public NutImageLoaderConfig create() {
            NutImageLoaderConfig config = new NutImageLoaderConfig();
            applyConfig(config);
            return config;
        }
    }
}
