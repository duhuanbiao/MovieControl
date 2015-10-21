/*
 * ImageFetcher.java
 * classes : com.wasu.imageloader.ImageFetcher
 * @author Administrator
 * V 1.0.0
 * Create at 2014骞�8鏈�7鏃� 涓婂崍11:10:40
 */
package com.person.module.image;

import java.io.File;

import com.person.module.http.IModule;
import com.person.thirdparty.image.BaseImageDecoder;
import com.person.thirdparty.image.BaseImageDownloader;
import com.person.thirdparty.image.BitmapDisplayer;
import com.person.thirdparty.image.DisplayImageOptions;
import com.person.thirdparty.image.HashCodeFileNameGenerator;
import com.person.thirdparty.image.ImageLoader;
import com.person.thirdparty.image.ImageLoaderConfiguration;
import com.person.thirdparty.image.ImageLoadingListener;
import com.person.thirdparty.image.ImageScaleType;
import com.person.thirdparty.image.ImageSize;
import com.person.thirdparty.image.LruMemoryCache;
import com.person.thirdparty.image.QueueProcessingType;
import com.person.thirdparty.image.RoundedBitmapDisplayer;
import com.person.thirdparty.image.SimpleBitmapDisplayer;
import com.person.thirdparty.image.StorageUtils;
import com.person.thirdparty.image.UnlimitedDiscCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

/**
 * com.wasu.imageloader.ImageFetcher
 * @author Administrator <br/>
 * create at 2014骞�8鏈�7鏃� 涓婂崍11:10:40
 */
public class ImageFetcherModule extends IModule implements ImageFetcherInterface {
	private static final String tag = ImageFetcherModule.class.getSimpleName();
	/**
	 * loader瀹炰緥
	 */
	private ImageLoader mImageLoader;
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	/**
	 * 鍏ㄥ眬閰嶇疆
	 */
	private ImageLoaderConfiguration mImageLoaderConfiguration;
	/**
	 * 榛樿鏄剧ず閰嶇疆
	 */
	private DisplayImageOptions mDisplayImageOptions;
	
	/**
	 * 鍦嗚鏄剧ず閰嶇疆
	 */
	private DisplayImageOptions mRoundedDisplayImageOptions;
	
	/**
	 * single instance
	 */
	private volatile static ImageFetcherModule g_instance;

	/**
	 * 鑾峰彇瀹炰綋绫诲崟渚�
	 * @return
	 * 		ImageFetcherModule鍗曚緥
	 */
	public static ImageFetcherModule getInstance() {
		if (g_instance == null) {
			synchronized (ImageLoader.class) {
				if (g_instance == null) {
					g_instance = new ImageFetcherModule();
				}
			}
		}
		return g_instance;
	}
	
	private DisplayImageOptions generateDisplayOptions() {
		return new DisplayImageOptions.Builder()
		// .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
		// .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
		// .showImageOnFail(R.drawable.ic_error) // resource or drawable
		.resetViewBeforeLoading(false) // default
		.cacheInMemory(true) // default
		.cacheOnDisk(true) // default
		// .preProcessor(...)
		// .postProcessor(...)
		// .extraForDownloader(...)
		.considerExifParams(false) // default
		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
		.bitmapConfig(Bitmap.Config.RGB_565) // default
		// .decodingOptions(...)
		.displayer(new SimpleBitmapDisplayer()) // default
		.handler(new Handler()) // default
		.build();
	}

	/**
	 * 鍒濆鍖�
	 * @param c 涓婁笅鏂囩幆澧�
	 */
	public void init(Context c){
		if (isInited()){
			return;
		}
		setContext(c);
		this.mImageLoader = ImageLoader.getInstance();
		File cacheDir = StorageUtils.getCacheDirectory(getContext());
		mImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getContext())// default = device screen dimensions
			// .taskExecutor(...)
			// .taskExecutorForCachedImages(...)
			.threadPoolSize(2) // default
			.threadPriority(Thread.MIN_PRIORITY - 1) // default
			.tasksProcessingOrder(QueueProcessingType.LIFO) // default
			.denyCacheImageMultipleSizesInMemory()
			.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
			.memoryCacheSize(2 * 1024 * 1024)
			.memoryCacheSizePercentage(13) // default
			.diskCache(new UnlimitedDiscCache(cacheDir)) // default
			.diskCacheSize(50 * 1024 * 1024)
			.diskCacheFileCount(100)
			.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
			.imageDownloader(new BaseImageDownloader(getContext())) // default
			.imageDecoder(new BaseImageDecoder(false)) // default
			.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
			.build();
		mDisplayImageOptions = new DisplayImageOptions.Builder()
			// .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
			// .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
			// .showImageOnFail(R.drawable.ic_error) // resource or drawable
			.resetViewBeforeLoading(false) // default
			.cacheInMemory(false) // default
			.cacheOnDisk(true) // default
			// .preProcessor(...)
			// .postProcessor(...)
			// .extraForDownloader(...)
			.considerExifParams(false) // default
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
			.bitmapConfig(Bitmap.Config.RGB_565) // default
			// .decodingOptions(...)
			.displayer(new SimpleBitmapDisplayer()) // default
			.handler(new Handler()) // default
			.build();
		mRoundedDisplayImageOptions = new DisplayImageOptions.Builder()
			// .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
			// .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
			// .showImageOnFail(R.drawable.ic_error) // resource or drawable
			.resetViewBeforeLoading(false) // default
			.cacheInMemory(false) // default
			.cacheOnDisk(true) // default
			// .preProcessor(...)
			// .postProcessor(...)
			// .extraForDownloader(...)
			.considerExifParams(false) // default
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
			.bitmapConfig(Bitmap.Config.RGB_565) // default
			// .decodingOptions(...)
			.handler(new Handler()) // default
			.build();
		
		init();
		
		setInited(true);
	}
	
	protected ImageFetcherModule() {
		
	}

	/**
	 * 鍒濆鍖�
	 */
	private synchronized void init() {
		mImageLoader.init(mImageLoaderConfiguration);
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#attachImage(java.lang.String, android.widget.ImageView)
	 */
	@Override
	public void attachImage(String imgUrl, ImageView imageView) {
		mImageLoader.displayImage(imgUrl, imageView, mDisplayImageOptions);
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#attachImage(java.lang.String, android.widget.ImageView, java.lang.Boolean)
	 */
	@Override
	public void attachImage(String imgUrl, ImageView imageView, int radius) {
		mRoundedDisplayImageOptions.setDisplayer(new RoundedBitmapDisplayer(radius));
		mImageLoader.displayImage(imgUrl, imageView, mRoundedDisplayImageOptions);
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#attachImage(java.lang.String, android.widget.ImageView, com.wasu.imageloader.ImageFetchListener)
	 */
	@Override
	public void attachImage(String imgUrl, ImageView imageView, final ImageFetchListener listener) {
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#getImage(java.lang.String)
	 */
	@Override
	public Bitmap getImage(String imgUrl) {
		return mImageLoader.loadImageSync(imgUrl, mDisplayImageOptions);
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#getImage(java.lang.String, int, int)
	 */
	@Override
	public Bitmap getImage(String imgUrl, int targetImageWidth, int targetImageHeight) {
		return mImageLoader.loadImageSync(imgUrl, new ImageSize(targetImageWidth, targetImageHeight), mDisplayImageOptions);
	}
	
	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#cancelTask(android.widget.ImageView)
	 */
	@Override
	public void cancelTask(ImageView imageView) {
		mImageLoader.cancelDisplayTask(imageView);
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#stopAllTask()
	 */
	@Override
	public void stopAllTask() {
		mImageLoader.stop();
	}
	
	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#clearMemoryCache()
	 */
	@Override
	public void clearMemoryCache() {
		mImageLoader.clearMemoryCache();
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#clearDiskCache()
	 */
	@Override
	public void clearDiskCache() {
		mImageLoader.clearDiskCache();
		mImageLoader.stop();
	}
	
	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#setThreadPoolSize(int size)
	 */
	@Override
	public void setThreadPoolSize(int size) {
		mImageLoaderConfiguration.setThreadPoolSize(size);
	}

	/* (non-Javadoc)
	 * @see com.wasu.imageloader.ImageFetcherInterface#setDisplayer(com.wasu.thirdparty.image.BitmapDisplayer)
	 */
	@Override
	public void setDisplayer(BitmapDisplayer bitmapDisplayer) {
		mDisplayImageOptions.setDisplayer(bitmapDisplayer);
	}
	
}
