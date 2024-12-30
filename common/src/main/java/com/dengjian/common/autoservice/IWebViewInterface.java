package com.dengjian.common.autoservice;

import android.content.Context;

public interface IWebViewInterface {
  void startWebViewActivity(Context context, String url, String title, boolean showActionBar);
}
