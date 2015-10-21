package com.person.moviecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;

public class FragmentNotes extends Fragment {
	private WebView mWebView;
	
	private long mLastUpdate = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mWebView==null){  
			mWebView = (WebView) inflater.inflate(R.layout.fragment_notes, null);  
            initWeb();
            
            mWebView.loadUrl(MovieApp.generateAboutUrl());
        }else{
    		if (System.currentTimeMillis() - mLastUpdate > 60 * 1000/** 60���ˢ�� **/){
    			mWebView.loadUrl(MovieApp.generateAboutUrl());
    		}
        }
		
		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���  
        ViewGroup parent = (ViewGroup) mWebView.getParent();  
        if (parent != null) {  
            parent.removeView(mWebView);  
        }   
        return mWebView; 
	}
	
	private void initWeb(){
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
}
