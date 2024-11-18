package com.dengjian.common.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static Thread.UncaughtExceptionHandler sDefaultUncaughtExceptionHandler;
    private static Context sContext;

    private CrashHandler() {

    }

    public static void init(Context applicationContext) {
        sContext = applicationContext;
        sDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }


    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        File catchDir = new File(sContext.getExternalCacheDir(), "catch_info");
        if (!catchDir.exists()) {
            catchDir.mkdirs();
        }
        long curTime = System.currentTimeMillis();
        File file = new File(catchDir, curTime + ".txt");

        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(file));
//            printWriter.println("time: " + Calendar.getInstance().new Date(curTime));
            printWriter.println("threadName: " + t.getName());
            e.printStackTrace(printWriter);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (null != sDefaultUncaughtExceptionHandler) {
                sDefaultUncaughtExceptionHandler.uncaughtException(t, e);
            }
        }
    }
}
