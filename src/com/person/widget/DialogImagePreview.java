package com.person.widget;

import com.person.module.image.ImageFetcherModule;
import com.person.moviecontrol.R;
import com.person.moviecontrol.SystemUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;

/**
 * 璁剧疆鐢佃鍓х殑閫夐泦妗�
 * **/
public class DialogImagePreview extends Dialog {

	private String mUrl;
	private ImageView mImagePrev;
	
	/**
	 * 璁剧疆鐢佃鍓х殑閫夐泦妗�
	 * @param Context 涓婁笅鏂�
	 * @param int 鎬婚泦鏁�
	 * @param int 涓婃鎾斁鐨勯泦鏁�
	 * @param onSeriesListener 鐐瑰嚮鐨勭洃鍚�
	 * **/
	public DialogImagePreview(Context context, String imageUrl) {
		super(context, R.style.dialog);
		
		this.mUrl = imageUrl;
		init(context);
	}

	private void init(Context context) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_preview);
		
		Rect screenRect = SystemUtils.getScreenSize(getContext());
		getWindow().setLayout(screenRect.width(), screenRect.height());
		
		mImagePrev = (ImageView) findViewById(R.id.image);
		
		ImageFetcherModule.getInstance().attachImage(mUrl, mImagePrev);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN){
			dismiss();
		}
		return super.dispatchTouchEvent(ev);
	}
}
