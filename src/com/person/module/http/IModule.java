package com.person.module.http;

import android.content.Context;


public abstract class IModule {
	/** 用于记录该模块是否被初始化， 没有被初始化的模块将无法使用 **/
	private boolean mbIsInited = false;
	
	/** 用于缓存上下�? **/
	private Context mContext;
	
	/**查询是否已经初始�?
	 * @return 是否已经初始�?
	 */
	public boolean isInited() {
		return mbIsInited;
	}

	/**
	 * 设置初始化状�?
	 * @param isInited�? true或�?�false
	 */
	public void setInited(boolean isInited) {
		this.mbIsInited = isInited;
	}

	/**
	 * 释放
	 */
	public void unInit(){
		setInited(false);
	}
	
	/**
	 * 获取上下文环�?
	 * @return 上下文环�?
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * 设置上下文环�?
	 * @param Context
	 */
	public void setContext(Context Context) {
		this.mContext = Context.getApplicationContext();
	}
}
