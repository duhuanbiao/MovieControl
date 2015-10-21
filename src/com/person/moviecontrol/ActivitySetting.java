package com.person.moviecontrol;

import org.json.JSONObject;

import com.person.module.http.DataFetchModule;
import com.person.module.http.DataFetchListener.JsonListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivitySetting extends Activity {

	private EditText roomId;
	private EditText ipAddr;
	private EditText password;
	
	private ProgressDialog mPbWaiting;
	
	private int mNeedBackToMain = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		
		if (getIntent() != null){
			mNeedBackToMain = getIntent() .getIntExtra("needBackToMain", 0);
		}
		
		initView();
	}

	private void initView() {
		roomId		= (EditText) findViewById(R.id.edRoomId);
		ipAddr 		= (EditText) findViewById(R.id.edIp);
		password	= (EditText) findViewById(R.id.edPwd);
		
		roomId.setText(MovieApp.s_roomId);
	}

	public void onClose(View v){
		if (!TextUtils.isEmpty(MovieApp.s_serverIp)){
			finish();
		}else{
			Toast.makeText(ActivitySetting.this, "请先设置正确的参数！", Toast.LENGTH_LONG).show();
		}
	}
	
	private void showWaiting(String msg){
		if (mPbWaiting == null){
			mPbWaiting = new ProgressDialog(this);
		}
		mPbWaiting.setMessage(msg);
		
		mPbWaiting.show();
	}
	
	private void hideWaiting(){
		if (mPbWaiting != null){
			mPbWaiting.dismiss();
			mPbWaiting = null;
		}
	}
	
	public void onSubmit(View v){
		final String room		= roomId.getText().toString().trim();
		final String ip 		= ipAddr.getText().toString().trim();
		final String pwd 		= password.getText().toString().trim();	
		if (TextUtils.isEmpty(ip) || ip.length() < 7 || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(room)){
			Toast.makeText(this, "请填写完整信息", Toast.LENGTH_LONG).show();
			return;
		}
		
		String url = String.format(Config.HTTP_QUERY_URL, ip);
		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon()
				.appendQueryParameter("roomid", room)
				.appendQueryParameter("get", "" + Config.CODE_PASSWORD)
				.appendQueryParameter("pwd", pwd)
				.build();
		
		Log.d("duhuanbiao", dstfetch.toString());
		
		showWaiting("正在验证...");
		
		DataFetchModule.getInstance().fetchJsonGet(dstfetch.toString(), new JsonListener() {
			
			@Override
			public void onJsonGet(int retcode, String extraMsg, JSONObject jsondata) {
				hideWaiting();
				if (retcode != 0 || jsondata == null || jsondata.optInt("errcode") != 0){
					new AlertDialog.Builder(ActivitySetting.this)
						.setTitle("设置失败")
						.setMessage("验证失败，请重新输入？")
						.setPositiveButton("确定", null).create().show();
				}else{
					Toast.makeText(ActivitySetting.this, "提交成功", Toast.LENGTH_LONG).show();
					
					MovieApp.updateConfig(ActivitySetting.this, ip, room);
					
					if (mNeedBackToMain == 1){
						Intent i = new Intent(ActivitySetting.this, ActivityHome.class);
						startActivity(i);
						overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
					}
					
					overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
				}
			}
		});
		
		
	}
	
	@Override
	public void onBackPressed() {
		if (!TextUtils.isEmpty(MovieApp.s_serverIp)){
			super.onBackPressed();
		}else{
			Toast.makeText(ActivitySetting.this, "请先设置正确的参数！", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		
		overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
	}
}
