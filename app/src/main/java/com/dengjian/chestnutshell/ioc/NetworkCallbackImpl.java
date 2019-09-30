package com.dengjian.chestnutshell.ioc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.dengjian.chestnutshell.utils.NetworkUtil;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    private final Context mContext;
    private final NetStatusReceiver mReceiver;

    public NetworkCallbackImpl(Context context, NetStatusReceiver receiver) {
        mContext = context;
        mReceiver = receiver;
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        mReceiver.post(NetworkUtil.getNetType(mContext));
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        mReceiver.post(NetworkUtil.getNetType(mContext));
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
    }
}
