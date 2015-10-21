package com.person.moviecontrol;

import com.person.model.InfoFood;
import com.person.model.InfoFood.FoodItem;
import com.person.module.http.DataFetchListener.ObjectListener;
import com.person.module.http.DataFetchModule;
import com.person.module.http.ObjectBase;
import com.person.module.image.ImageFetcherModule;
import com.person.widget.DialogImagePreview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentItemFood extends Fragment {

	private ListView mListView;
	private InfoFood mInfoFood;
	
	private FoodAdapter mFoodAdapter;
	
	private FragmentFood mFragmentFood;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mListView == null) {
			mListView = (ListView) inflater.inflate(
					R.layout.fragment_item_food, null);
			Bundle bundle = getArguments();
			if (bundle != null){
				String url = bundle.getString("url");
				fetchFoodDetail(url, false);
			}
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) mListView.getParent();
		if (parent != null) {
			parent.removeView(mListView);
		}
		return mListView;
	}

	public void setParantFragment(FragmentFood fragmentFood){
		mFragmentFood = fragmentFood;
	}
	
	public void reset(){
		if (mInfoFood == null || mListView == null){
			return;
		}
		
		for (FoodItem item : mInfoFood.mFoodList){
			item.orderCount = 0;
		}
		
		((FoodAdapter)mListView.getAdapter()).notifyDataSetChanged();
	}
	
	public void updateData(String url){
		fetchFoodDetail(url, true);
	}
	
	private void fetchFoodDetail(String url, final boolean bUpdate) {
		DataFetchModule.getInstance().fetchObjectGet(url, InfoFood.class, new ObjectListener() {
			
			@Override
			public void onObjectGet(int retcode, String extraMsg, ObjectBase data) {
				if (retcode != 0){
//					Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_LONG).show();
					return;
				}
				
				mInfoFood = (InfoFood) data;
//				if (mInfoFood.mFoodList.size() == 0){
//					Toast.makeText(getActivity(), "暂时没有数据", Toast.LENGTH_LONG).show();
//					return;
//				}
				if (bUpdate && mFoodAdapter != null){
					mFoodAdapter.notifyDataSetChanged();
				}else{
					init();
				}
			}
		});
	}
	
	private void init() {
		mFoodAdapter = new FoodAdapter();
		mListView.setAdapter(mFoodAdapter);
	}
	
	class FoodAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mInfoFood.mFoodList.size();
		}

		@Override
		public Object getItem(int position) {
			return mInfoFood.mFoodList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FoodItem item = (FoodItem) getItem(position);
			if (convertView == null){
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_food, null);
			}
			
			ViewStub viewStub = null;
			if (convertView.getTag() != null){
				viewStub = (ViewStub) convertView.getTag();
				viewStub.item = item;
			}else{
				viewStub = new ViewStub();
				viewStub.vSub = convertView.findViewById(R.id.sub);
				viewStub.vAdd = convertView.findViewById(R.id.add);
				viewStub.ivImg = (ImageView) convertView.findViewById(R.id.icon);
				viewStub.tvTitle = (TextView) convertView.findViewById(R.id.name);
				viewStub.tvPrice = (TextView) convertView.findViewById(R.id.price);
				viewStub.tvCount = (TextView) convertView.findViewById(R.id.count);
				
				viewStub.vSub.setTag(viewStub);
				viewStub.vAdd.setTag(viewStub);
				viewStub.ivImg.setTag(viewStub);
				
				viewStub.item = item;
				
				convertView.setTag(viewStub);
				
				viewStub.vSub.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						ViewStub stub = (ViewStub) v.getTag();
						stub.item.orderCount--;
						if (stub.item.orderCount <= 0){
							stub.item.orderCount = 0;
							v.setVisibility(View.INVISIBLE);
						}
						if (stub.item.orderCount > 0){
							stub.tvCount.setText(""+stub.item.orderCount);
						}else{
							stub.tvCount.setText("0");
						}
						
						mFragmentFood.updateItem(stub.item.menuid, stub.item.orderCount, stub.item.price);
					}
				});
				
				viewStub.vAdd.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ViewStub stub = (ViewStub) v.getTag();
						stub.item.orderCount++;
						stub.vSub.setVisibility(View.VISIBLE);
						stub.tvCount.setText(""+stub.item.orderCount);
						
						mFragmentFood.updateItem(stub.item.menuid, stub.item.orderCount, stub.item.price);
					}
				});
				
				viewStub.ivImg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ViewStub stub = (ViewStub) v.getTag();
						if (!TextUtils.isEmpty(stub.item.bigimg) && stub.item.bigimg.length() > 7){
							new DialogImagePreview(getActivity(), stub.item.bigimg).show();
						}
					}
				});
			}
			
			viewStub.tvTitle.setText(item.menuname);
			viewStub.tvPrice.setText("￥" + item.price);
			viewStub.tvCount.setText(""+ item.orderCount);
			ImageFetcherModule.getInstance().attachImage(item.smallimg, viewStub.ivImg);
			
			if (item.orderCount > 0){
				viewStub.vSub.setVisibility(View.VISIBLE);
			}else{
				viewStub.vSub.setVisibility(View.INVISIBLE);
			}
			
			return convertView;
		}
		
		class ViewStub{
			View vSub;
			View vAdd;
			ImageView ivImg;
			TextView tvTitle;
			TextView tvPrice;
			TextView tvCount;
			FoodItem item;
		}
	}
}