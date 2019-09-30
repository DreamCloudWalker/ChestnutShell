package com.dengjian.chestnutshell.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.dengjian.chestnutshell.ioc.typs.NetType;

@SuppressLint("MissingPermission")
public class NetworkUtil {
    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取当前的网络类型
     */
    public static NetType getNetType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return NetType.NONE;
        }
        // 获取当前激活的网络连接信息
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return NetType.NONE;
        }
        int type = info.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NetType.MOBILE;

        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Context context, int requestCode) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }
}
