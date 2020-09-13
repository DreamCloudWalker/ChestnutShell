package com.dengjian.network;

import android.content.Context;

public interface INetworkRequiredInfo {
    String getAppVersionName();
    Context getApplicationContext();

    boolean isDebug();
}
