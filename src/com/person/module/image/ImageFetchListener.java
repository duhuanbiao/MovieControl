/*
 * ImageFetcherInterface.java
 * classes : com.wasu.imageloader.ImageFetcherInterface
 * @author Administrator
 * V 1.0.0
 * Create at 2014骞�8鏈�7鏃� 涓嬪崍1:37:22
 */
package com.person.module.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * com.wasu.imageloader.ImageFetcherInterface
 * @author Administrator <br/>
 * create at 2014骞�8鏈�7鏃� 涓嬪崍1:37:22
 */
public interface ImageFetchListener {

	/**
	 * 鍔犺浇浠诲姟寮�濮�
	 * @param imageView
	 * @param imgUrl
	 */
	public void onFetchAdded(ImageView imageView, String imgUrl);

	/**
	 * 鍙栨秷鍔犺浇浠诲姟
	 * @param imageView
	 * @param imgUrl
	 */
	public void onFetchCancelled(ImageView imageView, String imgUrl);

	/**
	 * 鍔犺浇澶辫触
	 * @param imageView
	 * @param imgUrl
	 * @param e
	 */
	public void onFetchFailed(ImageView imageView, String imgUrl, Exception e);

	/**
	 * 鍔犺浇瀹屾垚
	 * @param imageView
	 * @param imgUrl
	 * @param bm
	 */
	public void onFetchCompleted(ImageView imageView, String imgUrl, Bitmap bm);

}
