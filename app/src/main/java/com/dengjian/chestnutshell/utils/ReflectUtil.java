package com.dengjian.chestnutshell.utils;

import java.lang.reflect.Field;

public class ReflectUtil {
    /**
     * 通过反射获取某对象，并设置私有可访问
     * @param obj   该属性所属类对象
     * @param clazz 该属性所属类
     * @param field 属性名
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static Object getField(Object obj, Class<?> clazz, String field)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    /**
     * 给某属性赋值，并设置私有可访问
     * @param obj   该属性所属类对象
     * @param clazz 该属性所属类
     * @param value 值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void setField(Object obj, Class<?> clazz, Object value)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        Field localField = clazz.getDeclaredField("dexElements");   // TODO
        localField.setAccessible(true);
        localField.set(obj, value);
    }

    /**
     * 通过反射获取BaseDexClassLoader对象中的PathList对象
     * @param baseDexClassLoader
     * @return PathList对象
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws ClassNotFoundException
     */
    public static Object getPathList(Object baseDexClassLoader)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, ClassNotFoundException {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 通过反射获取BaseDexClassLoader对象中的PathList对象，再获取dexElements对象
     * @param paramObject PathList对象
     * @return dexElements对象
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object getDexElements(Object paramObject)
        throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        return getField(paramObject, paramObject.getClass(), "dexElements");
    }
}
