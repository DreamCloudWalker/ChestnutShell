package com.dengjian.chestnutshell.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ListenerInvocationHandler implements InvocationHandler {
    // 拦截的对象
    private Object mTarget;
    // 需要拦截的方法
    private HashMap<String, Method> mMethodHashMap = new HashMap<>();

    public ListenerInvocationHandler(Object target) {
        mTarget = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (null != mTarget) {
            String name = method.getName();
            method = mMethodHashMap.get(name);
            if (null != method) {
                method.setAccessible(true);
                if (method.getGenericParameterTypes().length == 0) {
                    return method.invoke(mTarget);
                }
                return method.invoke(mTarget, args);
            }
        }
        return null;
    }

    /**
     * @param methodName 需要拦截的方法
     * @param method 执行自定义的方法
     */
    public void addMethod(String methodName, Method method) {
        mMethodHashMap.put(methodName, method);
    }
}
