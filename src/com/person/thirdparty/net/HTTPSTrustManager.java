package com.person.thirdparty.net;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author zhangrm at 2014/12/11 16:22:58
 */
public class HTTPSTrustManager implements X509TrustManager {
	
	private static TrustManager[] trustManager;
	private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return _AcceptedIssuers;
	}
	
	public static void allowAllSSL() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){

			@Override
			public boolean verify(String hostname, SSLSession session) {
				//默认全部信任
				return true;
			}
			
		});
		
		SSLContext context = null;
		if (trustManager == null) {
			trustManager = new TrustManager[] {new HTTPSTrustManager()};
		}
		
		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustManager, new SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

}
