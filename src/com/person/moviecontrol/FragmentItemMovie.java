package com.person.moviecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentItemMovie extends Fragment {

	private WebView mWebView;
	private String curUrl = "";
	
	private View rootView;//����Fragment view  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView==null){  
            rootView=inflater.inflate(R.layout.fragment_item_movie, null);  
            mWebView = (WebView) rootView.findViewById(R.id.webview);
            initWeb();
        }  
		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���  
        ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }   
        return rootView; 
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

		mWebView.addJavascriptInterface(new WR(getActivity()), "WR");
		
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
	
	public void updateUrl(String url){
		Log.d("duhuanbiao", "updateUrl: " + url);
		if (!curUrl.equals(url)){
			curUrl = url;
			mWebView.loadUrl(url);
		}
	}
}