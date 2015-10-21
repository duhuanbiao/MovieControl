package com.person.moviecontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ActivityHome extends SlidingFragmentActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        //init menu
        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.RIGHT);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        sm.setBehindOffset((int)getResources().getDimension(R.dimen.d_110dp));
		sm.setFadeDegree(0.35f);
		
		setContentView(R.layout.activity_home);
		setBehindContentView(R.layout.layout_control);
		
        FragmentTransaction  Manage = this.getSupportFragmentManager().beginTransaction();
        FragmentMain main= new FragmentMain();
        Manage.replace(R.id.content, main);
        Manage.commit();
        
        registerNetReceive();
        
        initView();
	}
	
	private void initView() {
		findViewById(R.id.volume_sub).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					mHandle.sendEmptyMessageDelayed(MSG_TIMEOUT_VSUB, 2000);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					mHandle.removeMessages(MSG_TIMEOUT_VSUB);
				}
				return false;
			}
		});
		
		findViewById(R.id.volume_add).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					mHandle.sendEmptyMessageDelayed(MSG_TIMEOUT_VADD, 2000);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					mHandle.removeMessages(MSG_TIMEOUT_VADD);
				}
				return false;
			}
		});
		
		findViewById(R.id.prev).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					mHandle.sendEmptyMessageDelayed(MSG_TIMEOUT_PREV, 2000);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					mHandle.removeMessages(MSG_TIMEOUT_PREV);
				}
				return false;
			}
		});
		
		findViewById(R.id.next).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					mHandle.sendEmptyMessageDelayed(MSG_TIMEOUT_NEXT, 2000);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					mHandle.removeMessages(MSG_TIMEOUT_NEXT);
				}
				return false;
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		unregisterNetReceive();
		
		mHandle.removeMessages(MSG_TIMEOUT_VADD);
		mHandle.removeMessages(MSG_TIMEOUT_VSUB);
		mHandle.removeMessages(MSG_TIMEOUT_PREV);
		mHandle.removeMessages(MSG_TIMEOUT_NEXT);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP){
			mHandle.removeMessages(MSG_TIMEOUT_VADD);
			mHandle.removeMessages(MSG_TIMEOUT_VSUB);
			mHandle.removeMessages(MSG_TIMEOUT_PREV);
			mHandle.removeMessages(MSG_TIMEOUT_NEXT);
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		getSlidingMenu().showSecondaryMenu(false);
	}
	
	public void onControl(View v){
		getSlidingMenu().showSecondaryMenu(true);
	}
	
	public void onSearch(View v){
		Intent i = new Intent(this, ActivitySearch.class);
		startActivity(i);
		overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
	}
	
	public void onSetting(View v){
		Intent i = new Intent(this, ActivitySetting.class);
		startActivity(i);
		overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
	}
	
	public void onAbout(View v){
		Intent i = new Intent(this, ActivityAbout.class);
		startActivity(i);
		overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
	}
	
	/**************************************/
	//控制
	public void onMute(View v){
		Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_MUTE, null);
	}
	
	public void onStop(View v){
		if (MovieApp.s_inplay){
			new AlertDialog.Builder(this)
			.setTitle("操作确认")
			.setMessage("是否结束当前播放？")
			.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_STOP, null);
				}
			}).setNegativeButton("取消", null).create().show();			
		}else{
			Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_STOP, null);
		}
	}
	
	public void onVAdd(View v){
		Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_V_ADD, null);
	}
	
	public void onVSub(View v){
		Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_V_SUB, null);
	}
	
	public void onOk(View v){
		Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_PLAY, null);
	}
	
	public void onPrev(View v){
		Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_PREV, null);
	}
	
	public void onNext(View v){
		Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_NEXT, null);
	}
	
	/***************************************/
	//长按快进
	private static final int MSG_TIMEOUT_VADD = 1;
	private static final int MSG_TIMEOUT_VSUB = 2;
	private static final int MSG_TIMEOUT_PREV = 3;
	private static final int MSG_TIMEOUT_NEXT = 4;
	private Handler mHandle = new Handler(){
		public void dispatchMessage(Message msg) {
			switch(msg.what){
			case MSG_TIMEOUT_VADD:
			{
				Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_V_ADD, "2");
				sendEmptyMessageDelayed(MSG_TIMEOUT_VADD, 500);
			}
				break;
			case MSG_TIMEOUT_VSUB:
			{
				Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_V_SUB, "2");
				sendEmptyMessageDelayed(MSG_TIMEOUT_VSUB, 500);
			}
				break;
			case MSG_TIMEOUT_PREV:
			{
				Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_PREV, "60");
				sendEmptyMessageDelayed(MSG_TIMEOUT_PREV, 1000);
			}
				break;
			case MSG_TIMEOUT_NEXT:
			{
				Cmd.sendCmd(MovieApp.generateMovieUrl(), Cmd.CMD_NEXT, "60");
				sendEmptyMessageDelayed(MSG_TIMEOUT_NEXT, 1000);
			}
				break;
			}
		};
	};
	
	
	/***************************************/
	private void showNetError(){
		if (dlgNetAlert != null){
			dlgNetAlert.dismiss();
		}
		
		dlgNetAlert = new AlertDialog.Builder(this)
					.setTitle("网络错误")
					.setMessage("当前网络已断开")
					.setPositiveButton("确定", null)
					.create();
		
		dlgNetAlert.show();
	}
	
	private void hideNetError() {
		if (dlgNetAlert != null) {
			dlgNetAlert.dismiss();
			dlgNetAlert = null;
		}
	}

	// IP地址转化为字符串格式
	private static String FormatIP(int IpAddress) {
		return Formatter.formatIpAddress(IpAddress);
	}

	private void parseWifiNet() {
		// 网关获取
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

		MovieApp.updateMovieIp(FormatIP(dhcpInfo.gateway));
	}
	
	//网络监听
	/*
	 * linsten net state
	 * */
	private ConnectionChangeReceiver mConnectReceive;
	private Dialog dlgNetAlert;
	private class ConnectionChangeReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			if (!NetUtils.isNetConnected(ActivityHome.this)){
				if (dlgNetAlert != null){
					return;
				}
				showNetError();
			}else{
				parseWifiNet();
				
				hideNetError();
			}
		}
	}
	private void registerNetReceive(){
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		mConnectReceive = new ConnectionChangeReceiver();
		this.registerReceiver(mConnectReceive, filter);
	}

	private void unregisterNetReceive(){
		if (mConnectReceive != null){
			this.unregisterReceiver(mConnectReceive);
			mConnectReceive = null;			
		}
	}
}
