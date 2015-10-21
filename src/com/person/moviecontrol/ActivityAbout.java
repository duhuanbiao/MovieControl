package com.person.moviecontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;

public class ActivityAbout extends Activity{

	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
		
		initView();
		
		initWeb();
		
		mWebView.loadUrl(MovieApp.generateAboutUrl());
	}
	
	private void initView() {
		mWebView = (WebView) findViewById(R.id.web);
	}
	
	private void initWeb() {
		mWebView.setBackgroundColor(0);
		
		WebSettings webSettings = mWebView.getSettings();
		
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		
		webSettings.setNeedInitialFocus(false);
		webSettings.setSupportMultipleWindows(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setDefaultTextEncodingName("UTF-8");
		webSettings.setBuiltInZoomControls(true);

		// webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		// webSettings.setSavePassword(true);
		// webSettings.setSaveFormData(true);
		webSettings.setSupportZoom(false);
		// webSettings.setPluginsEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setRenderPriority(RenderPriority.HIGH);

		// ����ĳЩ�������泤��OK���ᵯ������ճ���������Ĳ˵�
		mWebView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return true;
			}

		});
		
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	public void onClose(View v){
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		
		overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
	}
}
