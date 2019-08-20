package com.dengjian.chestnutshell.imageloader.nutimageloader.policy;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;

/**
 * 逆序加载策略,即从最后加入队列的请求进行加载
 */
public class ReversePolicy implements LoadPolicy {

    @Override
    public int compare(NutImageRequest request1, NutImageRequest request2) {
        // 注意Bitmap请求要先执行最晚加入队列的请求,ImageLoader的策略
        return request2.mSerialNum - request1.mSerialNum;
    }
}
