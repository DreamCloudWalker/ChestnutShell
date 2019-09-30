package com.dengjian.chestnutshell.ioc;

import com.dengjian.chestnutshell.ioc.annotation.NetSubscribe;
import com.dengjian.chestnutshell.ioc.typs.NetType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStatusReceiver {
    private Map<Object, List<NetMethodManager>> mNetworkList;
    private NetType mNetType;

    protected NetStatusReceiver() {
        mNetType = NetType.NONE;
        mNetworkList = new HashMap<>();
    }

    protected void post(NetType netType) {
        Set<Object> subscribeClazzSet = mNetworkList.keySet();
        this.mNetType = netType;
        for (Object subscribeClazz : subscribeClazzSet) {
            List<NetMethodManager> methodManagerList = mNetworkList.get(subscribeClazz);
            executeInvoke(subscribeClazz, methodManagerList);
        }
    }

    private void executeInvoke(Object subscribeClazz, List<NetMethodManager> methodManagerList) {
        if (methodManagerList != null) {
            for (NetMethodManager subscribeMethod : methodManagerList) {
                switch (subscribeMethod.getMode()) {
                    case AUTO:
                        invoke(subscribeMethod, subscribeClazz, mNetType);
                        break;

                    case WIFI:
                        if (mNetType == NetType.WIFI || mNetType == NetType.NONE) {
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        }
                        break;

                    case WIFI_CONNECT:
                        if (mNetType == NetType.WIFI) {
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        }
                        break;

                    case MOBILE:
                        if (mNetType == NetType.MOBILE || mNetType == NetType.NONE) {
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        }
                        break;

                    case MOBILE_CONNECT:
                        if (mNetType == NetType.MOBILE) {
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        }
                        break;

                    case NONE:
                        if (mNetType == NetType.NONE) {
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void invoke(NetMethodManager subscribeMethod, Object subscribeClazz, NetType netType) {
        Method execute = subscribeMethod.getMethod();
        try {
            // 有参数时
            if (subscribeMethod.getParameterClazz() != null) {
                if (subscribeMethod.getParameterClazz().isAssignableFrom(mNetType.getClass())) {
                    execute.invoke(subscribeClazz, netType);
                }
            } else {
                execute.invoke(subscribeClazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void registerObserver(Object object) {
        List<NetMethodManager> methodList = mNetworkList.get(object);
        if (methodList == null) {
            // 开始添加
            methodList = findAnnotationMethod(object);
            mNetworkList.put(object, methodList);
        }
        executeInvoke(object, mNetworkList.get(object));
    }

    private List<NetMethodManager> findAnnotationMethod(Object object) {
        List<NetMethodManager> methodManagerList = new ArrayList<>();
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            NetSubscribe netSubscribe = method.getAnnotation(NetSubscribe.class);
            if (null == netSubscribe) {
                continue;
            }
            // 注解方法校验返回值
            Type genericReturnType = method.getGenericReturnType();
            if (!"void".equalsIgnoreCase(genericReturnType.toString())) {
                throw new IllegalArgumentException("you " + method.getName() + "method return value must be void");
            }

            // 判断参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            NetMethodManager methodManager;
            if (parameterTypes.length == 0) {
                methodManager = new NetMethodManager(null, netSubscribe.mode(), method);
            } else if (parameterTypes.length == 1) {
                methodManager = new NetMethodManager(parameterTypes[0], netSubscribe.mode(), method);
            } else {
                throw new IllegalArgumentException("Your method " + method.getName() + " can have at most one parameter of type NetType ");
            }
            methodManagerList.add(methodManager);
        }
        return methodManagerList;
    }

    public void unRegisterObserver(Object mContext) {
        if (!mNetworkList.isEmpty()) {
            mNetworkList.remove(mContext);
        }
    }

    public void unRegisterAllObserver() {
        if (!mNetworkList.isEmpty()) {
            mNetworkList.clear();
            mNetworkList = null;
        }
    }
}
