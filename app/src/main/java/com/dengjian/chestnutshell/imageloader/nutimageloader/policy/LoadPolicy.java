package com.dengjian.chestnutshell.imageloader.nutimageloader.policy;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;

public interface LoadPolicy {
    public int compare(NutImageRequest request1, NutImageRequest request2);
}
