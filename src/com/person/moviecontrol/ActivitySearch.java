package com.person.moviecontrol;

import com.person.widget.ClearEditText;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;

public class ActivitySearch extends Activity{

	private WebView mWebView;
	private ClearEditText mSearchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search);
		
		initView();
		
		initWeb();
	}
	
	private void initView() {
		mSearchView = (ClearEditText) findViewById(R.id.searchTxt);
		mWebView = (WebView) findViewById(R.id.web);
		
		mSearchView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String keyword = s.toString().trim();
				if (TextUtils.isEmpty(keyword)){
					mWebView.loadUrl("about:blank");
				}else{
					updateSearchKeyWord(keyword);
				}
			}
		});
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

	private void updateSearchKeyWord(String keyword){
		String url = MovieApp.generateWebUrl();
		
		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon().appendQueryParameter("keyword", keyword).build();
		
		Log.d("duhuanbiao", "search url: " + dstfetch.toString());
		
		mWebView.loadUrl(dstfetch.toString());
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
