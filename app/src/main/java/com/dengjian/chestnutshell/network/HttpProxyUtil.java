package com.dengjian.chestnutshell.network;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class HttpProxyUtil {

    /**
     * 需要得到class字节码
     * @param object
     * @return
     */
    public static Class<?> analysisClassInfo(Object object) {
        if (null == object) {
            return null;
        }
        Type genType = object.getClass().getGenericSuperclass();    // 获取当前new的对象的 泛型的父类 类型
        if (!(genType instanceof ParameterizedType)) {
            return object.getClass();
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments(); // 获取第一个类型参数的真实类型
        return (Class<?>) params[0];
    }

    public static Class<?> analysisInterfaceInfo(Object object) {
        if (null == object) {
            return null;
        }
        Type[] genType = object.getClass().getGenericInterfaces();
        Type[] params = ((ParameterizedType) genType[0]).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
