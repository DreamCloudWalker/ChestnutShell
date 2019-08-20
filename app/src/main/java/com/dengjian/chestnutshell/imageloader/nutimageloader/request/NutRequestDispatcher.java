package com.dengjian.chestnutshell.imageloader.nutimageloader.request;

import com.dengjian.chestnutshell.imageloader.nutimageloader.loader.Loader;
import com.dengjian.chestnutshell.imageloader.nutimageloader.loader.LoaderManager;
import com.dengjian.chestnutshell.utils.LogUtil;

import java.util.concurrent.BlockingQueue;

/**
 * 网络请求Dispatcher,继承自Thread,从网络请求队列中循环读取请求并且执行
 */
public class NutRequestDispatcher extends Thread {
    /**
     * 网络请求队列
     */
    private BlockingQueue<NutImageRequest> mRequestQueue;

    /**
     * @param queue
     */
    public NutRequestDispatcher(BlockingQueue<NutImageRequest> queue) {
        mRequestQueue = queue;
    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                final NutImageRequest request = mRequestQueue.take();
                if (request.mIsCancel) {
                    continue;
                }

                final String schema = parseSchema(request.mImageUri);
                Loader imageLoader = LoaderManager.getInstance().getLoader(schema);
                imageLoader.loadImage(request);
            }
        } catch (InterruptedException e) {
            LogUtil.i("", "### 请求分发器退出");
        }
    }

    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        } else {
            LogUtil.e(getName(), "### wrong scheme, image uri is : " + uri);
        }

        return "";
    }
}
