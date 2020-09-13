package com.dengjian.network.dns;

import android.content.Context;

import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

public class AlibabaDns implements Dns {
    private final HttpDnsService mHttpDns;

    public AlibabaDns(Context context) {
        mHttpDns = HttpDns.getService(context, "169929");
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        // 通过异步解析接口获取ip
        String ip = mHttpDns.getIpByHostAsync(hostname);
        if (null != ip) {
            List<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(ip));
            return inetAddresses;
        }

        return Dns.SYSTEM.lookup(hostname); // 走系统DNS服务进行域名解析
    }
}
