package com.person.moviecontrol;

import java.util.HashMap;
import java.util.Iterator;

import com.person.model.CategoryMovie;
import com.person.model.CategoryMovie.CategoryMovieItem;
import com.person.model.InfoFilter;
import com.person.module.http.DataFetchListener.ObjectListener;
import com.person.module.http.DataFetchModule;
import com.person.module.http.ObjectBase;
import com.person.widget.CategoryView;
import com.person.widget.CategoryView.OnClickCategoryListener;
import com.viewpagerindicator.TabPageIndicator;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMovie extends Fragment {
	private CategoryMovie mCategoryMovie;
	private InfoFilter mInfoFilter;

	private TextView tvFilter;
	private CategoryView filterView;

	private TabPageIndicatorAdapter mTabPageIndicatorAdapter;
	private TabPageIndicator mTabPageIndicator;

	private HashMap<String, String> mFilterMap = new HashMap<String, String>();

	private SparseArray<FragmentItemMovie> mItemFragmentMap = new SparseArray<FragmentItemMovie>();

	private ViewPager mViewPager;

	private View rootView;// 缓存Fragment view

	private long mLastUpdate = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_movie, null);
			fetchMovieCategory(false);
			fetchFilterInfo(false);

			mLastUpdate = System.currentTimeMillis();
		} else {
			if (System.currentTimeMillis() - mLastUpdate > 10 * 1000/** 20秒后刷新 **/) {
				fetchMovieCategory(true);
				fetchFilterInfo(true);
			}
		}

		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void fetchMovieCategory(final boolean bUpdate) {
		String url = MovieApp.generateQueryUrl();
		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon()
				.appendQueryParameter("roomid", MovieApp.s_roomId)
				.appendQueryParameter("get", "" + Config.CODE_CATEGORY_MOVIE)
				.build();
		
		DataFetchModule.getInstance().fetchObjectGet(dstfetch.toString(), CategoryMovie.class,
				new ObjectListener() {
					@Override
					public void onObjectGet(int retcode, String extraMsg,
							ObjectBase data) {
						if (retcode != 0) {
							Toast.makeText(getActivity(), "获取数据失败",
									Toast.LENGTH_LONG).show();
							return;
						}

						mCategoryMovie = (CategoryMovie) data;
						// if (mCategoryMovie.mCategoryMovieList.size() == 0){
						// Toast.makeText(getActivity(), "暂时没有数据",
						// Toast.LENGTH_LONG).show();
						// return;
						// }

						if (bUpdate) {
							update();
						} else {
							init();
						}
					}
				});
	}

	private void fetchFilterInfo(final boolean bUpdate) {
		String url = MovieApp.generateQueryUrl();
		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon()
				.appendQueryParameter("roomid", MovieApp.s_roomId)
				.appendQueryParameter("get", "" + Config.CODE_FILTER)
				.build();
		
		DataFetchModule.getInstance().fetchObjectGet(dstfetch.toString(), InfoFilter.class,
						new ObjectListener() {
							@Override
							public void onObjectGet(int retcode,
									String extraMsg, ObjectBase data) {
								if (retcode != 0) {
									Toast.makeText(getActivity(), "筛选获取数据失败",
											Toast.LENGTH_LONG).show();
									return;
								}

								mInfoFilter = (InfoFilter) data;
								// if (mInfoFilter.mFilterPair.size() == 0){
								// Toast.makeText(getActivity(), "暂时没有数据",
								// Toast.LENGTH_LONG).show();
								// return;
								// }

								if (bUpdate) {
									update();
								} else {
									init();
								}
							}
						});
	}

	private void update() {
		if (mCategoryMovie == null || mInfoFilter == null) {
			return;
		}

		// 清楚帅选
		mFilterMap.clear();
		filterView.removeAllViews();
		filterView.add(mInfoFilter);
		filterView.setVisibility(View.GONE);

		// 更新page
		mTabPageIndicator.notifyDataSetChanged();
		mTabPageIndicatorAdapter.notifyDataSetChanged();

		// 更新页面
		FragmentItemMovie cur = mItemFragmentMap.get(mViewPager
				.getCurrentItem());
		if (cur != null) {
			cur.updateUrl(generateLoadUrl(mCategoryMovie.mCategoryMovieList
					.get(mViewPager.getCurrentItem())));
		}
	}

	private void init() {
		if (mCategoryMovie == null || mInfoFilter == null) {
			return;
		}

		FragmentActivity activity = (FragmentActivity) getActivity();
		mTabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.indicator);
		rootView.findViewById(R.id.indicateView).setVisibility(View.VISIBLE);

		// ViewPager的adapter
		mTabPageIndicatorAdapter = new TabPageIndicatorAdapter(
				activity.getSupportFragmentManager());
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		mViewPager.setAdapter(mTabPageIndicatorAdapter);

		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		mTabPageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				FragmentItemMovie cur = mItemFragmentMap.get(arg0);
				if (cur != null) {
					cur.updateUrl(generateLoadUrl(mCategoryMovie.mCategoryMovieList
							.get(arg0)));
					
					mFilterMap.clear();
					filterView.removeAllViews();
					filterView.add(mInfoFilter);
					filterView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mTabPageIndicator.setViewPager(mViewPager);

		FragmentItemMovie cur = mItemFragmentMap.get(mViewPager
				.getCurrentItem());
		if (cur != null) {
			cur.updateUrl(generateLoadUrl(mCategoryMovie.mCategoryMovieList
					.get(mViewPager.getCurrentItem())));
		}

		tvFilter = (TextView) rootView.findViewById(R.id.filter);
		filterView = (CategoryView) rootView.findViewById(R.id.category);

		filterView.add(mInfoFilter);
		filterView.setVisibility(View.GONE);

		tvFilter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag() == null) {
					filterView.setVisibility(View.VISIBLE);
					v.setTag(1);
				} else {
					filterView.setVisibility(View.GONE);
					v.setTag(null);
				}
			}
		});

		filterView.setOnClickCategoryListener(new OnClickCategoryListener() {

			@Override
			public void click(RadioGroup group, int checkedId) {
				RadioButton button = (RadioButton) group
						.findViewById(checkedId);
				mFilterMap.put((String) button.getTag(), button.getText()
						.toString());

				FragmentItemMovie cur = mItemFragmentMap.get(mViewPager
						.getCurrentItem());
				if (cur != null) {
					cur.updateUrl(generateLoadUrl(mCategoryMovie.mCategoryMovieList
							.get(mViewPager.getCurrentItem())));
				}
			}
		});
	}

	private String generateLoadUrl(CategoryMovieItem item) {
		String url = MovieApp.generateWebUrl();
		if (item == null) {
			return null;
		}

		Uri dstfetch = Uri.parse(url);
		dstfetch = dstfetch.buildUpon()
				.appendQueryParameter("typeid", item.typeId).build();

		Iterator<String> iterator = mFilterMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			dstfetch = dstfetch.buildUpon()
					.appendQueryParameter(key, mFilterMap.get(key)).build();
		}

		Log.d("duhuanbiao", dstfetch.toString());

		return dstfetch.toString();
	}

	/**
	 * ViewPager适配器
	 * 
	 * @author len
	 * 
	 */
	class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 新建一个Fragment来展示ViewPager item的内容，并传递参数
			FragmentItemMovie mCurItemFragment = new FragmentItemMovie();
			mItemFragmentMap.append(position, mCurItemFragment);

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
