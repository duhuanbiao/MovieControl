/*
 * ImageFetcherInterface.java
 * classes : com.wasu.imageloader.ImageFetcherInterface
 * @author Administrator
 * V 1.0.0
 * Create at 2014骞�8鏈�7鏃� 涓嬪崍2:49:45
 */
package com.person.module.image;

import com.person.thirdparty.image.BitmapDisplayer;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * com.wasu.imageloader.ImageFetcherInterface
 * @author Administrator <br/>
 * create at 2014骞�8鏈�7鏃� 涓嬪崍2:49:45
 */
public interface ImageFetcherInterface {

	/**
	 * 鏍规嵁url鍦板潃鍔犺浇鍒版寚瀹歩mageview
	 * @param imgUrl
	 * @param imageView
	 */
	public void attachImage(String imgUrl, ImageView imageView);
	
	/**
	 * 鏍规嵁url鍦板潃鍔犺浇鍒版寚瀹歩mageview
	 * @param imgUrl
	 * @param imageView
	 * @param radius 鍦嗚鍗婂緞
	 */
	public void attachImage(String imgUrl, ImageView imageView, int radius);

	/**
	 * 鏍规嵁url鍦板潃鍔犺浇鍒版寚瀹歩mageview,涓斿甫鏈夊姞杞藉洖璋�
	 * @param imgUrl 
	 * @param imageView
	 * @param listener
	 */
	public void attachImage(String imgUrl, ImageView imageView, ImageFetchListener listener);

	/**
	 * 鏍规嵁url鑾峰彇bitmap
	 * @param imgUrl
	 * @return
	 */
	public Bitmap getImage(String imgUrl);

	/**
	 * 鏍规嵁url鑾峰彇鎸囧畾澶у皬bitmap
	 * @param imgUrl
	 * @param targetImageWidth 瀹�
	 * @param targetImageHeight 楂�
	 * @return bitmap
	 */
	public Bitmap getImage(String imgUrl, int targetImageWidth, int targetImageHeight);

	/**
	 * 鍙栨秷鏌愪釜imageview鐨勫姞杞戒换鍔�
	 * @param imageView
	 */
	public void cancelTask(ImageView imageView);
	
	/**
	 * 鍙栨秷鎵�鏈夊姞杞戒换鍔�
	 */
	public void stopAllTask();

	/**
	 * 娓呴櫎鍐呭瓨缂撳瓨
	 */
	public void clearMemoryCache();

	/**
	 * 娓呮鏂囦欢缂撳瓨
	 */
	public void clearDiskCache();
	
	/**
	 * 璁剧疆绾跨▼姹犵嚎绋嬫暟閲�
	 * @param size
	 */
	public void setThreadPoolSize(int size);
	
	/**
	 * 璁剧疆鏄剧ず鏁堟灉
	 * @param bitmapDisplayer
	 */
	public void setDisplayer(BitmapDisplayer bitmapDisplayer);

}