package com.dengjian.webview;

import android.content.Context;
import android.content.Intent;

import com.dengjian.common.autoservice.IWebViewInterface;
import com.dengjian.webview.utils.WebViewConstants;
import com.google.auto.service.AutoService;

@AutoService(IWebViewInterface.class)
public class WebViewServiceImpl implements IWebViewInterface {
  @Override
  public void startWebViewActivity(Context context, String url, String title, boolean showActionBar) {
    if (null != context) {
      Intent intent = new Intent(context, WebviewActivity.class);
      intent.putExtra(WebViewConstants.WEBVIEW_URL, url);
      intent.putExtra(WebViewConstants.WEBVIEW_TITLE, title);
      intent.putExtra(WebViewConstants.WEBVIEW_SHOW_ACTION_BAR, showActionBar);
      context.startActivity(intent);
    }
  }
}
