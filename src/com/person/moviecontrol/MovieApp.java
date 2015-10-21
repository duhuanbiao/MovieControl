package com.person.moviecontrol;

import com.person.module.http.DataFetchModule;
import com.person.module.image.ImageFetcherModule;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MovieApp extends Application {
	public static String s_serverIp = "";
	public static String s_roomId = "";
	public static String s_movieIp = "";
	
	public static boolean s_inplay = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		DataFetchModule.getInstance().init(getApplicationContext());
		ImageFetcherModule.getInstance().init(getApplicationContext());
		
		s_serverIp = ConfigUtils.getString(this, null, "ip");
		s_roomId = ConfigUtils.getString(this, null, "roomId");
	}
	
	public static void updateConfig(Context c, String ip, String roomid){
		s_serverIp = ip;
		s_roomId  = roomid;
		
		ConfigUtils.saveData(c, null, "ip", ip);
		ConfigUtils.saveData(c, null, "roomId", roomid);
	}
	
	public static void updateMovieIp(String ip){
		Log.d("duhuanbiao", "movIp:" + ip);
		s_movieIp = ip;
	}
	
	public static String generateQueryUrl(){
		return String.format(Config.HTTP_QUERY_URL, s_serverIp);
	}
	
	public static String generateWebUrl(){
		return String.format(Config.HTTP_WEB_URL, s_serverIp);
	}
	
	public static String generateAboutUrl(){
		return String.format(Config.HTTP_ABOUT, s_serverIp);
	}
	
	public static String generateMovieUrl(){
		return String.format(Config.HTTP_MOVIE, s_movieIp);
	}
}
