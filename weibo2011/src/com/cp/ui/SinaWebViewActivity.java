package com.cp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class SinaWebViewActivity extends Activity{
	WebView sinaWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sina_webview);
		sinaWebView=(WebView) findViewById(R.id.sinaWebView);
		sinaWebView.loadUrl("http://www.sina.com.cn");
	}
}
