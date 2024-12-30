package com.dengjian.webview;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dengjian.webview.databinding.ActivityWebviewBinding;
import com.dengjian.webview.utils.WebViewConstants;

public class WebviewActivity extends AppCompatActivity {
  private ActivityWebviewBinding mBinding;

  @Override
  protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
    mBinding.webview.getSettings().setJavaScriptEnabled(true);
    mBinding.webview.loadUrl(getIntent().getStringExtra(WebViewConstants.WEBVIEW_URL));
    mBinding.tvTitle.setVisibility(getIntent().getBooleanExtra(WebViewConstants.WEBVIEW_SHOW_ACTION_BAR,
            true) ? View.VISIBLE : View.GONE);
  }
}
