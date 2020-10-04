package com.dengjian.common.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

public class Utils {
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
