package com.dengjian.chestnutshell.ioc;

import com.dengjian.chestnutshell.ioc.typs.NetMode;

import java.lang.reflect.Method;

/**
 * 保存符合要求的网络监听注解方法
 */
public class NetMethodManager {
    private Class<?> mParameterClazz;
    private NetMode mNetMode;
    private Method mMethod;

    public NetMethodManager(Class<?> clazz, NetMode mode, Method method) {
        this.mParameterClazz = clazz;
        this.mNetMode = mode;
        this.mMethod = method;
    }

    public Class<?> getParameterClazz() {
        return mParameterClazz;
    }

    public void setParameterClazz(Class<?> parameterClazz) {
        this.mParameterClazz = parameterClazz;
    }

    public NetMode getMode() {
        return mNetMode;
    }

    public void setMode(NetMode mode) {
        this.mNetMode = mode;
    }

    public Method getMethod() {
        return mMethod;
    }

    public void setMethod(Method method) {
        this.mMethod = method;
    }

}
