package com.dengjian.chestnutshell.utils;

import java.lang.reflect.Array;

public class ArrayUtil {

    /**
     * 合并数组
     * @param arrayLhs 前数组（插队数组）
     * @param arrayRhs 后数组（已有数组）
     * @return
     */
    public static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int oldLength = Array.getLength(arrayLhs);
        int newLength = oldLength + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, newLength);   // 生成返回的数组对象
        for (int i = 0; i < newLength; ++i) {
            if (i < oldLength) {
                Array.set(result, i, Array.get(arrayLhs, i));
            } else {
                Array.set(result, i, Array.get(arrayRhs, i - oldLength));
            }
        }

        return result;
    }
}
