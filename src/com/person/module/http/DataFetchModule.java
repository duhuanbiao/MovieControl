package com.person.module.http;

import java.util.Map;

import org.json.JSONObject;

import com.person.module.http.DataFetchListener.JsonListener;
import com.person.module.http.DataFetchListener.ObjectListener;

import android.content.Context;

/**
 * 瀵圭綉缁滄ā鍧楃殑杩涗竴姝ュ皝瑁咃紝�?��?�簬璇锋眰锛�?洿鎺ヨ繑鍥炰竴涓疄浣撶�?
 */
public class DataFetchModule extends IModule {
	private final String tag = DataFetchModule.class.getSimpleName();
	
	protected DataFetchModule() {}

	/** 鍗曚緥妯″紡 **/
	private static DataFetchModule g_Instance;

	/*----------------------------------瀵瑰鎺ュ彛----------------------------------------------*/
	/**
	 * 鑾峰彇绫荤殑鍗曚�?
	 * 
	 * @return DataFetchModule鍗曚�?
	 */
	public static DataFetchModule getInstance() {
		synchronized (DataFetchModule.class) {
			if (g_Instance == null) {
				g_Instance = new DataFetchModule();
			}

			return g_Instance;
		}
	}

	public void init(Context context) {
		if (isInited()) {
			return;
		}

		setContext(context);

		/** 鍒濆鍖栧叾瀹冩ā鍧� **/
		HttpRequestModule.getInstance().init(context);

		setInited(true);
	}

	@Override
	public void unInit() {
		super.unInit();
	}

	/**
	 * 绠�鍖栫増鏈�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param clazz
	 *            瑙ｆ瀽杩斿洖鐨勬暟鎹殑绫�
	 * @param callback
	 * 			  鏁版嵁鑾峰彇鍚庣殑鍥炶皟
	 * @param decryptImpl
	 * 			 鍔犲瘑�?�炵�?
	 */
	public void fetchObjectGet(String url,
			final Class<? extends ObjectBase> clazz, ObjectListener callback, IDecryptInterface decryptImpl) {
		processHttpRequest(RequestParams.Method.GET, type_object, url, null,
				null, null, clazz, callback, -1, decryptImpl);
	}
	
	/**
	 * 绠�鍖栫増鏈�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param clazz
	 *            瑙ｆ瀽杩斿洖鐨勬暟鎹殑绫�
	 * @param callback
	 * 			  鏁版嵁鑾峰彇鍚庣殑鍥炶皟
	 */
	public void fetchObjectGet(String url,
			final Class<? extends ObjectBase> clazz, ObjectListener callback){
		fetchObjectGet(url, clazz, callback, null);
	}

	/**
	 * 绠�鍖栫増鏈�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param clazz
	 *            瑙ｆ瀽杩斿洖鐨勬暟鎹殑绫�
	 * @param callback
	 * 			      鏁版嵁鑾峰彇鍚庣殑鍥炶皟
	 * @param decryptImpl
	 * 			 鍔犲瘑�?�炵�?
	 */
	public void fetchJsonGet(String url, JsonListener callback, IDecryptInterface decryptImpl) {
		processHttpRequest(RequestParams.Method.GET, type_json, url, null,
				null, null, null, callback, -1, decryptImpl);
	}
	
	/**
	 * 绠�鍖栫増鏈�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param clazz
	 *            瑙ｆ瀽杩斿洖鐨勬暟鎹殑绫�
	 * @param callback
	 * 			      鏁版嵁鑾峰彇鍚庣殑鍥炶皟
	 */
	public void fetchJsonGet(String url, JsonListener callback) {
		fetchJsonGet(url, callback, null);
	}

	/**
	 * 鍙戦�乭ttp get璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param clazz
	 *            瑙ｆ瀽鐨勭被
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 * @param decryptImpl
	 * 			 鍔犲瘑�?�炵�?
	 */
	public void fetchObjectGet(String url, Map<String, String> param,
			Map<String, String> headers, Class<? extends ObjectBase> clazz,
			int msgid, ObjectListener callback, IDecryptInterface decryptImpl) {
		processHttpRequest(RequestParams.Method.GET, type_object, url, param,
				headers, null, clazz, callback, msgid, decryptImpl);
	}
	
	/**
	 * 鍙戦�乭ttp get璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param clazz
	 *            瑙ｆ瀽鐨勭被
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 */
	public void fetchObjectGet(String url, Map<String, String> param,
			Map<String, String> headers, Class<? extends ObjectBase> clazz,
			int msgid, ObjectListener callback) {
		fetchObjectGet(url, param, headers, clazz, msgid, callback, null);
	}

	/**
	 * 鍙戦�乭ttp post璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param clazz
	 *            瑙ｆ瀽鐨勭被
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 * @param decryptImpl
	 * 			 鍔犲瘑�?�炵�?
	 */
	public void fetchObjectPost(String url, Map<String, String> param,
			Map<String, String> headers, byte[] body,
			Class<? extends ObjectBase> clazz, int msgid,
			ObjectListener callback, IDecryptInterface decryptImpl) {
		processHttpRequest(RequestParams.Method.POST, type_object, url, param,
				headers, body, clazz, callback, msgid, decryptImpl);
	}
	
	/**
	 * 鍙戦�乭ttp post璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param clazz
	 *            瑙ｆ瀽鐨勭被
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 */
	public void fetchObjectPost(String url, Map<String, String> param,
			Map<String, String> headers, byte[] body,
			Class<? extends ObjectBase> clazz, int msgid,
			ObjectListener callback) {
		fetchObjectPost(url, param, headers, body, clazz, msgid, callback, null);
	}

	/**
	 * 鍙戦�乭ttp get璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 * @param decryptImpl
	 * 			 鍔犲瘑�?�炵�?
	 */
	public void fetchJsonGet(String url, Map<String, String> param,
			Map<String, String> headers, int msgid, JsonListener callback, IDecryptInterface decryptImpl) {
		processHttpRequest(RequestParams.Method.GET, type_json, url, param,
				headers, null, null, callback, msgid, decryptImpl);
	}
	
	/**
	 * 鍙戦�乭ttp get璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 */
	public void fetchJsonGet(String url, Map<String, String> param,
			Map<String, String> headers, int msgid, JsonListener callback) {
		fetchJsonGet(url, param, headers, msgid, callback, null);
	}

	/**
	 * 鍙戦�乭ttp post璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 */
	public void fetchJsonPost(String url, Map<String, String> param,
			Map<String, String> headers, byte[] body, int msgid,
			JsonListener callback, IDecryptInterface decryptImpl) {
		processHttpRequest(RequestParams.Method.POST, type_json, url, param,
				headers, body, null, callback, msgid, decryptImpl);
	}
	
	/**
	 * 鍙戦�乭ttp post璇锋�?
	 * @param url
	 *            璇锋眰鐨剈rl
	 * @param param
	 *            璇锋眰鐨勫ご鍙傛�?
	 * @param headers
	 *            璇锋眰鐨刪eader鍙傛�?
	 * @param msgid
	 *            鍙戦�佹秷鎭殑id锛� 涓嶉渶瑕佸～-1
	 * @param callback
	 *            鎺ユ敹鐨勫洖璋�
	 */
	public void fetchJsonPost(String url, Map<String, String> param,
			Map<String, String> headers, byte[] body, int msgid,
			JsonListener callback) {
		fetchJsonPost(url, param, headers, body, msgid, callback, null);
	}

	/**-------------------------------鍐呴儴�?�炵�?------------------------------**/
	protected void doNotify(int retcode, String extraMsg, ObjectBase data,
			ObjectListener callback, int msgId) {
		if (callback != null) {
			callback.onObjectGet(retcode, extraMsg, data);
		}
	}

	protected void doNotify(int retcode, String extraMsg, JSONObject data,
			JsonListener callback, int msgId) {
		if (callback != null) {
			callback.onJsonGet(retcode, extraMsg, data);
		}
	}

	protected final static int type_object = 1;
	protected final static int type_json = 2;

	protected void processHttpRequest(int requestType, final int processtype,
			String url, Map<String, String> param, Map<String, String> headers,
			byte[] body, final Class<? extends ObjectBase> clazz,
			final Object callback, final int msgid, IDecryptInterface decryptImpl) {
		/** 缁勫悎url **/
		url = HttpRequestModule.composeUri(url, param);
		RequestParams requestParam = new RequestParams(requestType, url,
				headers, body, new com.person.module.http.RequestParams.RequestListener() {
					@Override
					public boolean onResponse(int errorCode, String data,
							int arg1, Object extra) {
						if (processtype == type_object) {
							if (errorCode != 0) {
								doNotify(errorCode, data, null,
										(ObjectListener) callback, msgid);
								return true;
							}
							try {
								ObjectBase myObject = clazz.newInstance();
								myObject.createFromResponse(data);
								doNotify(errorCode, null, myObject,
										(ObjectListener) callback, msgid);
							} catch (DataFetchException e) {
								doNotify(e.code, e.extraMsg, null,
										(ObjectListener) callback, msgid);
							} catch (Exception e) {
								doNotify(-1, e.toString(),null, (ObjectListener) callback, msgid);
							}
							return true;
						} else if (processtype == type_json) {
							if (errorCode != 0) {
								doNotify(errorCode, data, null,
										(JsonListener) callback, msgid);
								return true;
							}
							try {
								JSONObject myObject = new JSONObject(data);
								doNotify(errorCode, null, myObject,
										(JsonListener) callback, msgid);
							} catch (Exception e) {
								e.printStackTrace();
								doNotify(-1, e.toString(), null, (JsonListener) callback, msgid);
							}
							return true;
						}

						return true;
					}
				});

		requestParam.setEncryptImpl(decryptImpl);
		HttpRequestModule.getInstance().addTask(requestParam);
	}

}
