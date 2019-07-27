package com.dengjian.chestnutshell.network.nuthttp;

import com.dengjian.chestnutshell.network.ICallbackListener;
import com.dengjian.chestnutshell.network.IHttpProcessor;
import com.dengjian.chestnutshell.utils.Utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class JsonHttpProcessor implements IHttpRequest {
    private String mUrl;
    private byte[] mData;
    private HttpURLConnection mHttpUrlConnecton;
    private ICallbackListener mCallBackListener;

    public void setListener(ICallbackListener callBackListener) {
        mCallBackListener = callBackListener;
    }

    public void execute() {
        URL url = null;
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            url = new URL(mUrl);
            mHttpUrlConnecton.setConnectTimeout(6000);
            mHttpUrlConnecton.setUseCaches(false);
            mHttpUrlConnecton.setInstanceFollowRedirects(true);
            mHttpUrlConnecton.setReadTimeout(3000);
            mHttpUrlConnecton.setDoInput(true);
            mHttpUrlConnecton.setDoOutput(true);
            mHttpUrlConnecton.setRequestMethod("POST");
            mHttpUrlConnecton.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            mHttpUrlConnecton.connect();
            out = mHttpUrlConnecton.getOutputStream();
            bos = new BufferedOutputStream(out);
            bos.write(mData);
            bos.flush();

            if (mHttpUrlConnecton.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = mHttpUrlConnecton.getInputStream();
                mCallBackListener.onSuccess(readStream(is));
            } else {
                throw new RuntimeException("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求失败");
        } finally {
            Utils.closeQuietly(out);
            Utils.closeQuietly(bos);
            mHttpUrlConnecton.disconnect();
        }
    }

    private String readStream(InputStream inputStream) {
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(reader);
            return result.toString();
        }
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setData(byte[] data) {
        mData = data;
    }
}
