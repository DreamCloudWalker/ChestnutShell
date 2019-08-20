package com.dengjian.chestnutshell.imageloader.nutimageloader.loader;

import android.graphics.Bitmap;

import com.dengjian.chestnutshell.imageloader.nutimageloader.request.NutImageRequest;
import com.dengjian.chestnutshell.utils.LogUtil;

public class NullLoader extends AbsLoader {

    @Override
    public Bitmap onLoadImage(NutImageRequest requestBean) {
        LogUtil.e(NullLoader.class.getSimpleName(), "onLoadImage, wrong schema, your image uri is : "
                + requestBean.mImageUri);
        return null;
    }
}
