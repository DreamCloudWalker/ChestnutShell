package com.dengjian.chestnutshell.imageloader.nutimageloader.policy;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;

/**
 * 顺序加载策略
 */
public class SerialPolicy implements LoadPolicy {

    @Override
    public int compare(NutImageRequest request1, NutImageRequest request2) {
        // 如果优先级相等,那么按照添加到队列的序列号顺序来执行
        return request1.mSerialNum - request2.mSerialNum;
    }
}
