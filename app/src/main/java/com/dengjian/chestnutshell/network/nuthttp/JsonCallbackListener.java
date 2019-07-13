package com.dengjian.chestnutshell.network.nuthttp;

import com.dengjian.chestnutshell.network.ICallbackListener;
import com.dengjian.chestnutshell.utils.Utils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallbackListener<T> implements ICallbackListener {
    private final Gson mGson = new Gson();
    private final Class<T> mResponse;   // UserBean.class
    private final IJsonDataListener mIJsonDataListener;

    public JsonCallbackListener(Class<T> response, IJsonDataListener listener) {
        mResponse = response;
        mIJsonDataListener = listener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String content = getContent(inputStream);
        T t = mGson.fromJson(content, mResponse);
        mIJsonDataListener.onSuccess(t);
    }

    @Override
    public void onFailure(String result) {

    }

    private String getContent(InputStream is) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while (null != (line = bufferedReader.readLine())) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(bufferedReader);
        }

        return sb.toString();
    }
}
