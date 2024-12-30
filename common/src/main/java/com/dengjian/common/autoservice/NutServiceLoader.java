package com.dengjian.common.autoservice;

import android.util.Log;

import java.util.ServiceLoader;

public final class NutServiceLoader {
  private static final String TAG = "NutServiceLoader";
  private NutServiceLoader() {

  }

  public static <T> T load(Class<T> service) {
    try {
      return ServiceLoader.load(service).iterator().next();
    } catch (Exception e) {
      Log.e(TAG, "load service catch: ", e);
      return null;
    }
  }
}
