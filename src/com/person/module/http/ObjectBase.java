package com.person.module.http;

import java.io.Serializable;

/**
 * 网络获取实体类的基类�? �?要实现createFromResponse
 */
public abstract class ObjectBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用于解析格式，由调用者提�?
	 * 
	 * @param response
	 *             服务端返回的数据
	 */
	public abstract void createFromResponse(String response)
			throws DataFetchException;
}
