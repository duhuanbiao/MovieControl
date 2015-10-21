package com.person.moviecontrol;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.util.Log;

public class Cmd {
	public static final int CMD_MUTE 	= 1;

	public static final int CMD_OPEN 	= 2;
	
	public static final int CMD_PLAY 	= 3;
	
	public static final int CMD_PAUSE 	= 4;
	
	public static final int CMD_V_ADD 	= 5;
	
	public static final int CMD_V_SUB 	= 6;
	
	public static final int CMD_PREV 	= 7;
	
	public static final int CMD_NEXT 	= 8;
	
	public static final int CMD_STOP 	= 9;
	
	
	public static final void sendCmd(String url, int cmd, String param){
		if (param == null){
			param = "";
		}
		
		Log.d("duhuanbiao", "before url:" + param);
		
		Uri dstfetch = Uri.parse(url);
		final String realUrl = dstfetch.buildUpon()
					.appendQueryParameter("cmd", "" + cmd)
					.appendQueryParameter("param", param)
					.build()
					.toString();
		
		if (cmd == CMD_OPEN){
			MovieApp.s_inplay = true;
			param = URLEncoder.encode(param);
		}else if(cmd == CMD_STOP){
			MovieApp.s_inplay = false;
		}
		
		Log.d("duhuanbiao", "url:" + realUrl);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					Log.d("duhuanbiao", "http send cmd");
					HttpGet httpGet = new HttpGet(realUrl);
		            HttpClient httpClient = new DefaultHttpClient();
		            HttpResponse response = httpClient.execute(httpGet);
		            Log.d("duhuanbiao", "httpGet return: " + response.getStatusLine());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
}
