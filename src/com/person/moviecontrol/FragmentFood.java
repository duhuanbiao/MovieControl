package com.person.moviecontrol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.person.model.CategoryMovie;
import com.person.model.CategoryMovie.CategoryMovieItem;
import com.person.module.http.DataFetchListener.JsonListener;
import com.person.module.http.DataFetchModule;
import com.person.module.http.ObjectBase;
import com.person.module.http.DataFetchListener.ObjectListener;
import com.viewpagerindicator.TabPageIndicator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentFood extends Fragment {
	private CategoryMovie mCategoryMovie;

	private ViewPager mViewPager;

	private View rootView;// ����Fragment view
	
	private TabPageIndicatorAdapter mTabPageIndicatorAdapter;
	private TabPageIndicator mTabPageIndicator;
	
	private HashMap<String, OrderItem> mOrderMap = new HashMap<String, FragmentFood.OrderItem>();

	private SparseArray<FragmentItemFood> mFragmentItemFoodMap = new SparseArray<FragmentItemFood>();
	
	private TextView tvTotalCount;
	private TextView tvTotalPrice;
	private Button btnSubmit;
	
	private long mLastUpdate = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_food, null);
			fetchFoodCategory(false);
		}else{
			if (System.currentTimeMillis() - mLastUpdate > 30 * 1000/** 60���ˢ�� **/ && mOrderMap.isEmpty()) {
				fetchFoodCategory(true);
			}
		}
		// �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent��
		// �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void fetchFoodCategory(final boolean bUpdate) {
		String url = MovieApp.generateQueryUrl();
		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon()
				.appendQueryParameter("roomid", MovieApp.s_roomId)
				.appendQueryParameter("get", "" + Config.CODE_CATEGORY_FOOD)
				.build();
		
		DataFetchModule.getInstance().fetchObjectGet(dstfetch.toString(),
				CategoryMovie.class, new ObjectListener() {
					@Override
					public void onObjectGet(int retcode, String extraMsg,
							ObjectBase data) {
						if (retcode != 0) {
//							Toast.makeText(getActivity(), "��ȡ����ʧ��",
//									Toast.LENGTH_LONG).show();
							return;
						}

						mCategoryMovie = (CategoryMovie) data;
//						if (mCategoryMovie.mCategoryMovieList.size() == 0) {
//							Toast.makeText(getActivity(), "��ʱû������",
//									Toast.LENGTH_LONG).show();
//							return;
//						}

						if (bUpdate) {
							update();
						} else {
							init();
						}
					}
				});
	}
	
	private void update(){
		// ����page
		mTabPageIndicator.notifyDataSetChanged();
		mTabPageIndicatorAdapter.notifyDataSetChanged();
		
		if (mFragmentItemFoodMap.size() < mCategoryMovie.mCategoryMovieList.size()){
			return;
		}
		
		for(int i = 0; i < mCategoryMovie.mCategoryMovieList.size(); i++){
			mFragmentItemFoodMap.get(i).updateData(generateLoadUrl( mCategoryMovie.mCategoryMovieList.get(i)));
		}
	}

	private void init() {
		FragmentActivity activity = (FragmentActivity) getActivity();

		rootView.findViewById(R.id.indicatorFood).setVisibility(View.VISIBLE);

		tvTotalCount = (TextView) rootView.findViewById(R.id.count);
		tvTotalPrice = (TextView) rootView.findViewById(R.id.price);
		
		// ViewPager��adapter
		mTabPageIndicatorAdapter = new TabPageIndicatorAdapter(
				activity.getSupportFragmentManager());
		mViewPager = (ViewPager) rootView.findViewById(R.id.pagerFood);
		mViewPager.setAdapter(mTabPageIndicatorAdapter);

		// ʵ����TabPageIndicatorȻ������ViewPager��֮����
		mTabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.indicatorFood);
		// �������Ҫ��ViewPager���ü�������indicator���þ�����
		mTabPageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mTabPageIndicator.setViewPager(mViewPager);
		
		btnSubmit = (Button) rootView.findViewById(R.id.submit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOrderMap.size() == 0){
					Toast.makeText(getActivity(), "���ȵ��", Toast.LENGTH_LONG).show();
					return;
				}
				
				new AlertDialog.Builder(getActivity())
					.setTitle("����ȷ��")
					.setMessage("�����Ƿ���Ҫ���ͣ�")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							submitOrder();
						}
					}).setNegativeButton("ȡ��", null).create().show();
			}
		});
	}
	
	private void resetOrder(){
		mOrderMap.clear();
		
		for(int i = 0, nsize = mFragmentItemFoodMap.size(); i < nsize; i++) {
			FragmentItemFood tmp = mFragmentItemFoodMap.valueAt(i);
			tmp.reset();
		}
		
		tvTotalCount.setText("0");
		tvTotalPrice.setText("0 Ԫ");
	}
	
	private void submitOrder(){
		final ProgressDialog pbWaiting = new ProgressDialog(getActivity());
		pbWaiting.setMessage("�����ύ����...");
		pbWaiting.show();
		
		//��������
		JSONObject object = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject tmp;
		OrderItem item;
		Iterator<Entry<String, OrderItem>> iter = mOrderMap.entrySet().iterator();
		try{
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String menuId = (String) entry.getKey();
				item = (OrderItem) entry.getValue();
				tmp = new JSONObject();
				
				tmp.put("menuid", menuId);
				tmp.put("count", item.count);
				jsonArray.put(tmp);
			}
			object.put("cmdbody", jsonArray);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String url = MovieApp.generateQueryUrl();
		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon()
				.appendQueryParameter("roomid", MovieApp.s_roomId)
				.appendQueryParameter("get", "" + Config.CODE_INFO_ORDER)
				.appendQueryParameter("json", object.toString())
				.build();
		
		Log.d("duhuanbiao", dstfetch.toString());
		
		DataFetchModule.getInstance().fetchJsonGet(dstfetch.toString(), new JsonListener() {
			
			@Override
			public void onJsonGet(int retcode, String extraMsg, JSONObject jsondata) {
				pbWaiting.dismiss();
				if (retcode != 0 || jsondata == null || jsondata.optInt("errcode") != 0){
					new AlertDialog.Builder(getActivity())
						.setMessage("�����ύʧ��")
						.setPositiveButton("ȷ��", null).create().show();
				}else{
					new AlertDialog.Builder(getActivity())
					.setMessage("�����ύ�ɹ������Ժ�")
					.setPositiveButton("ȷ��", null).create().show();	
					
					//�������
					resetOrder();
				}
			}
		});
		
		
//		new AlertDialog.Builder(getActivity())
//		.setMessage("�뵽ǰ̨����!")
//		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				resetOrder();				
//			}
//		}).create().show();	
	}

	private String generateLoadUrl(CategoryMovieItem item) {
		String url = MovieApp.generateQueryUrl();
		if (item == null) {
			return null;
		}

		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon()
				.appendQueryParameter("typeid", item.typeId)
				.appendQueryParameter("get", "" + Config.CODE_INFO_FOOD)
				.build();

		Log.d("duhuanbiao", dstfetch.toString());

		return dstfetch.toString();
	}

	public void updateItem(String id, int count, int price){
		OrderItem item = mOrderMap.get(id);
		if (item == null){
			item = new OrderItem();
		}
		if (count <= 0){
			mOrderMap.remove(id);
		}else{
			item.count = count;
			item.price = price;
			if (!mOrderMap.containsKey(id)){
				mOrderMap.put(id, item);
			}
		}

		//���¼۸�
		Iterator<Entry<String, OrderItem>> iter = mOrderMap.entrySet().iterator();
		int totleCount = 0;
		int totalPrice = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			item = (OrderItem) entry.getValue();
			totleCount += item.count;
			totalPrice += item.count * item.price;
		}
		
		tvTotalCount.setText("" + totleCount);
		tvTotalPrice.setText("" + totalPrice + "Ԫ");
		
	}
	
	class OrderItem{
		int count;
		int price;
	}
	
	/**
	 * ViewPager������
	 * 
	 * @author len
	 * 
	 */
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// �½�һ��Fragment��չʾViewPager item�����ݣ������ݲ���
			FragmentItemFood mCurItemFragment = new FragmentItemFood();
			Bundle args = new Bundle();
			args.putString("url",
					generateLoadUrl(mCategoryMovie.mCategoryMovieList
							.get(position)));
			mCurItemFragment.setArguments(args);

			mCurItemFragment.setParantFragment(FragmentFood.this);
			
			mFragmentItemFoodMap.append(position, mCurItemFragment);
			
			return mCurItemFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mCategoryMovie.mCategoryMovieList.get(position).typeName;
		}

		@Override
		public int getCount() {
			return mCategoryMovie.mCategoryMovieList.size();
		}
	}
}
