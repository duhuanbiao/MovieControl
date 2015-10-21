package com.person.moviecontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.SearchView;

public class ActivityDetail extends Activity{

	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_detail);
		
		initView();
		
		initWeb();
		
		mWebView.loadUrl(getIntent().getStringExtra("url"));
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

		mWebView.addJavascriptInterface(new WR(this), "WR");
		
		// 禁用某些机型上面长按OK键会弹出复制粘贴的上下文菜单
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
