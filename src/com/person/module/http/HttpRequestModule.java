/**
 * 缃戠粶璇锋眰
 */
package com.person.module.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;

import com.person.thirdparty.net.AuthFailureError;
import com.person.thirdparty.net.DefaultRetryPolicy;
import com.person.thirdparty.net.HttpHeaderParser;
import com.person.thirdparty.net.NetworkResponse;
import com.person.thirdparty.net.ParseError;
import com.person.thirdparty.net.Request;
import com.person.thirdparty.net.RequestQueue;
import com.person.thirdparty.net.Response;
import com.person.thirdparty.net.Volley;
import com.person.thirdparty.net.VolleyError;
import com.person.thirdparty.net.Response.ErrorListener;

/**
 * 鐢ㄤ簬缃戠粶璇锋眰锛屾槸瀵圭涓夋柟妗嗘灦velley鐨勫皝瑁�?
 */
public class HttpRequestModule extends IModule {
	private final static String tag = HttpRequestModule.class.getSimpleName();
	
	/** 缃戠粶寮曟搸瀹炵幇绫�? **/
	private RequestQueue mRequestQueue;

	private RequestParams.RequestListener mDefaultListener;

	/*******************************************************************************************/
	/**
	 * single instance
	 */
	private volatile static HttpRequestModule g_Instance;

	/**
	 * 鑾峰彇鍗曚緥
	 * 
	 * @return HttpRequestModule鍗曚�?
	 */
	public static HttpRequestModule getInstance() {
		if (g_Instance == null) {
			synchronized (HttpRequestModule.class) {
				if (g_Instance == null) {
					g_Instance = new HttpRequestModule();
				}
			}
		}
		return g_Instance;
	}

	/**
	 * 妯�?�潡鍒濆鍖�?
	 * 
	 * @param c
	 *            涓婁笅鏂囩幆澧�
	 */
	public void init(Context c) {
		if (isInited()) {
			return;
		}

		setContext(c);
		mRequestQueue = Volley.newRequestQueue(getContext());
		mRequestQueue.start();
		setInited(true);
	}

	@Override
	public void unInit() {
		if (!isInited()) {
			return;
		}
		super.unInit();
		mRequestQueue.stop();
	}

	public void addTask(RequestParams requestParam) {
		StringRequest request = new StringRequest(requestParam);
		mRequestQueue.add(request);
	}

	/**
	 * 鍙栨秷鏌愪竴绫诲瀷鏁版嵁
	 * 
	 * @param tag
	 *            鍙戦�佽姹傛椂RequestParams甯︾殑鏍囪
	 */
	public void cancelTask(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	/**
	 * 鐢ㄤ簬鏋勫缓涓�涓甫鍙傛暟鐨剈ri锛� 绫讳�?浜巙rlencode,瀵�?�簬涓枃�?�楃鍋�?16杩涘埗澶勭悊
	 * 
	 * @param url
	 *            鍏冪礌鐨勫湴鍧�
	 * @param params
	 *            鎴愬鐨勫弬鏁�
	 * @return 瀹屾暣鐨剈rl, 濡俬ttp://www.test.com/s?wd=%E6%96%B0%E6%B5%AA
	 */
	public static String composeUri(String url, Map<String, String> params) {
		if (url == null || params == null) {
			return url;
		}

		if (url.indexOf("?") > 0) {
			return url + "&" + encodeParameters(params, "utf-8");
		} else {
			return url + "?" + encodeParameters(params, "utf-8");
		}
	}

	/**
	 * 璁剧疆榛樿鐨勫鐞嗗嚱鏁�
	 * 
	 * @param l
	 *            榛樿澶勭悊鍥炶�?
	 */
	public void setDefaultResponseListener(RequestParams.RequestListener l) {
		mDefaultListener = l;
	}

	/** ------------------------鍐呴儴�?�炵�?------------------------------------------- **/
	/**
	 * 鎺ュ彛�?�炵幇绫�?
	 * 
	 * 浼犲叆鐨刢ontext闄嶄細鍏ㄥ眬瀛樺�?
	 * **/
	protected HttpRequestModule() {
	}

	/**
	 * Converts <code>params</code> into an application/x-www-form-urlencoded
	 * encoded string.
	 */
	private static String encodeParameters(Map<String, String> params,
			String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(),
						paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(),
						paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString();
		} catch (UnsupportedEncodingException uee) {
		}
		return "";
	}

	/**
	 * 鍐呴儴灏佽绫�
	 * 
	 */
	private class StringRequest extends Request<String> {
		private RequestParams mRequestParam;

		private String contentType = null; 
		public StringRequest(RequestParams request) {
			this(request.getMethod(), request.getUrl(), null);
			this.mRequestParam = request;

			//parse content type
			if (request.getHeaders() != null && request.getHeaders().containsKey("Content-Type")){
				contentType = request.getHeaders().get("Content-Type");
				request.getHeaders().remove("Content-Type");
			}
			
			if (request.getTag() != null) {
				setTag(request.getTag());
			}

			setRetryPolicy(new DefaultRetryPolicy(request.getTimeOut(),
					request.getRetries(), 1.0f));

			setShouldCache(request.isCached());
		}

		private StringRequest(int method, String url, ErrorListener listener) {
			super(method, url, listener);
		}

		@Override
		public Map<String, String> getHeaders() throws AuthFailureError {
			if (mRequestParam.getHeaders() == null) {
				return super.getHeaders();
			}
			return mRequestParam.getHeaders();
		}
		
		@Override
		public String getBodyContentType() {
			if (contentType != null){
				return contentType;
			}
			
			return super.getBodyContentType();
		}

		@Override
		public byte[] getBody() throws AuthFailureError {
			if (mRequestParam.getBody() != null) {
				return mRequestParam.getBody();
			}
			return super.getBody();
		}

		@Override
		protected Response<String> parseNetworkResponse(NetworkResponse response) {
			try {
				String strResponseData = null;
				
				if(mRequestParam.getEncryptImpl() != null){
					strResponseData = mRequestParam.getEncryptImpl().decrypt(response.headers, response.data);
				}else{
					strResponseData = new String(response.data, "utf-8");
				}
				
				return Response.success(strResponseData,
						HttpHeaderParser.parseCacheHeaders(response));
			} catch (UnsupportedEncodingException e) {
				return Response.error(new ParseError(e));
			} catch (Exception je) {
				return Response.error(new ParseError(je));
			}
		}

		@Override
		protected void deliverResponse(String response) {
			boolean bProcessed = false;
			if (mRequestParam.getRequestListener() != null) {
				bProcessed = mRequestParam.getRequestListener().onResponse(
						0, response, 0, null);
			}

			if (!bProcessed && mDefaultListener != null) {
				mDefaultListener.onResponse(0, response, 0,
						null);
			}
			mRequestParam = null;
		}

		@Override
		public void deliverError(VolleyError error) {
			boolean bProcessed = false;
			
			int errorCode = -1;
			if (error.networkResponse == null){
				if (error.getCause() != null){
					if (error.getCause() instanceof UnsupportedEncodingException){
						errorCode = -2;
					}
				}
			}else{
				errorCode = error.networkResponse.statusCode;
			}
			
			if (mRequestParam.getRequestListener() != null) {
				bProcessed = mRequestParam.getRequestListener().onResponse(errorCode, error.getMessage(), 0, null);
			}

			if (!bProcessed && mDefaultListener != null) {
				mDefaultListener.onResponse(errorCode, error.getMessage(), 0, null);
			}
			
			mRequestParam = null;
		}
	}
}
