package com.person.moviecontrol;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class WR {
	private Context mContext;
	
	public WR(Context mContext){
		this.mContext = mContext;
	}
	
	@android.webkit.JavascriptInterface
	public void openDetail(String result){
		//�ж��Ƿ�Ϊ��Ե�ַ
		if(TextUtils.isEmpty(result)){
			Toast.makeText(mContext, "��Ч�ĵ�ַ", Toast.LENGTH_LONG).show();
			return;
		}
		
		result = result.trim();
		if (!result.startsWith("http://") && !result.startsWith("HTTP://")){
			result = "http://" + MovieApp.s_serverIp + result;
		}
		
		Log.d("duhuanbiao", "openDetail: " + result);
		Intent i = new Intent(mContext, ActivityDetail.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("url", result);
		mContext.startActivity(i);
		if (mContext instanceof Activity){
			((Activity)mContext).overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
		}
	}
	
	@android.webkit.JavascriptInterface
	public void openVideo(String url){
		//�ж��Ƿ�Ϊ��Ե�ַ
		if(TextUtils.isEmpty(url)){
			Toast.makeText(mContext, "��Ч�Ĳ��ŵ�ַ", Toast.LENGTH_LONG).show();
			return;
		}
		
		url = url.trim();
		if (!url.startsWith("http://") && !url.startsWith("HTTP://")){
			url = "http://" + MovieApp.s_serverIp + url;
		}
		
		final String result = url;
		if (MovieApp.s_inplay){
			new AlertDialog.Builder(mContext)
			.setTitle("����ȷ��")
			.setMessage("�Ƿ������ǰ���ţ�")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(mContext, ActivityHome.class);
					mContext.startActivity(i);
					
//					result = "http://120.26.121.35:81/UploadFiles/moviepath/20150628083523453.mp4";
					
					Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_OPEN, result);
					
					if (mContext instanceof Activity){
						((Activity)mContext).overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
					}
				}
			}).setNegativeButton("ȡ��", null).create().show();
		}else{
			Intent i = new Intent(mContext, ActivityHome.class);
			mContext.startActivity(i);
			
//			result = "http://120.26.121.35:81/UploadFiles/moviepath/20150628083523453.mp4";
			
			Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_OPEN, result);
			
			if (mContext instanceof Activity){
				((Activity)mContext).overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
			}
		}
	}
}
