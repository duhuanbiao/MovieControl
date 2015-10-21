package com.person.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;

public class BigButton extends Button {
    private int mTouchAdditionBottom = 30;  
    private int mTouchAdditionLeft = 30;  
    private int mTouchAdditionRight = 30;  
    private int mTouchAdditionTop = 30;  
    private int mPreviousLeft = -1;  
    private int mPreviousRight = -1;  
    private int mPreviousBottom = -1;  
    private int mPreviousTop = -1;  
    
	public BigButton(Context context) {
		super(context);
	}

	public BigButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BigButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		
		if (left != mPreviousLeft || top != mPreviousTop  
                || right != mPreviousRight || bottom != mPreviousBottom) {  
            mPreviousLeft = left;  
            mPreviousTop = top;  
            mPreviousRight = right;  
            mPreviousBottom = bottom;  
            final View parent = (View) this.getParent();  
            parent.setTouchDelegate(new TouchDelegate(new Rect(left  
                    - mTouchAdditionLeft, top - mTouchAdditionTop, right  
                    + mTouchAdditionRight, bottom + mTouchAdditionBottom), this));  
        }  
	}
}
