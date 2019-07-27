package com.dengjian.chestnutshell.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static boolean copyFile(File srcFile, File destFile) {
        boolean ret = false;
        if (null == srcFile || null == destFile) {
            LogUtil.e(TAG, "copyFile, srcFile or destFile is null");
            return ret;
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            if (destFile.exists()) {
                destFile.delete();
            }

            is = new FileInputStream(srcFile);
            os = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            ret = true;
        } catch (IOException e) {
            ret = false;
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(is);
            Utils.closeQuietly(os);
        }

        return ret;
    }
}
