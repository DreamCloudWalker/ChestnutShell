package com.dengjian.chestnutshell.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
    /**
     * 通过反射获取某对象，并设置私有可访问
     * @param obj   该属性所属类对象
     * @param clazz 该属性所属类
     * @param name 属性名
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static Object getField(Object obj, Class<?> clazz, String name)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        Field localField = clazz.getDeclaredField(name);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static Field getField(Object instance, String name) {
        Class<?> cls = instance.getClass();
        while (cls != Object.class) {   // 跟Object那就不用循环了，Null判断也可以，会多一次循环
            try {
                Field field = cls.getDeclaredField(name);
                if (null != field) {
                    field.setAccessible(true);
                    return field;
                }
                cls = cls.getSuperclass();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException("no source field" + name);
    }

    public static Method getMethod(Object instance, String name, Class<?>... parameterTypes) {
        Class<?> cls = instance.getClass();
        while (cls != Object.class) {   // 跟Object那就不用循环了，Null判断也可以，会多一次循环
            try {
                Method method = cls.getDeclaredMethod(name, parameterTypes);
                if (null != method) {
                    method.setAccessible(true);
                    return method;
                }
                cls = cls.getSuperclass();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException("no source method" + name);
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
