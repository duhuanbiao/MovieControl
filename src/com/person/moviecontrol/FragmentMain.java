package com.person.moviecontrol;
import com.person.moviecontrol.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class FragmentMain extends Fragment {
	//����FragmentTabHost����  
    private FragmentTabHost mTabHost;
    
	//����һ������
	private LayoutInflater layoutInflater;
		
	//�������������Fragment����
	private Class<?> fragmentArray[] = {FragmentMovie.class,FragmentFood.class,FragmentNotes.class};
	
	//������������Ű�ťͼƬ
	private int mImageViewArray[] = {R.drawable.tab_home_movie,R.drawable.tab_home_food,R.drawable.tab_home_notes};
	
	//Tabѡ�������
	private String mTextviewArray[] = {"��Ӱ", "��ʳ", "����"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
        if (TextUtils.isEmpty(MovieApp.s_serverIp)){
        	Intent i = new Intent(getActivity(), ActivitySetting.class);
        	i.putExtra("needBackToMain", 1);
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(i);
        	getActivity().finish();
        }
        
        initView();
	}
	 
	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ�������ֶ���
		layoutInflater = LayoutInflater.from(getActivity());
				
		//ʵ����TabHost���󣬵õ�TabHost
		mTabHost = (FragmentTabHost)getActivity().findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(), getFragmentManager(), R.id.realtabcontent);	
		
		//�õ�fragment�ĸ���
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//��Tab��ť��ӽ�Tabѡ���
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//����Tab��ť�ı���
//			mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(0xffffff);
		}
	}
				
	/**
	 * ��Tab��ť����ͼ�������
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.item_menu, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
}
