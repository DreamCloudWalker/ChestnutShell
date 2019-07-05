package com.dengjian.chestnutshell.utils;

import java.io.Closeable;
import java.io.IOException;

public class Utils {

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
