package com.person.module.http;

import org.json.JSONObject;

/**
 * 网络数据请求回调
 */
public interface DataFetchListener {
	/**
	 * 当请求需要返回一个ObjectBase实体类时的回�?
	 */
	public interface ObjectListener {
		public void onObjectGet(int retcode, String extraMsg, ObjectBase data);
	}

	/**
	 * 当请求需要返回一个json实体类的回调
	 */
	public interface JsonListener {
		public void onJsonGet(int retcode, String extraMsg, JSONObject jsondata);
	}
}
