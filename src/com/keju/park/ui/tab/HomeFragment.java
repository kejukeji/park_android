package com.keju.park.ui.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.db.DataBaseAdapter;
import com.keju.park.ui.base.BaseFragment;
import com.keju.park.ui.searchparking.ParkingListActivity;
import com.keju.park.ui.searchparking.SearchParkingActivity;


/**
 * 首页
 * @author Zhoujun
 * @version 创建时间：2014-5-2 下午3:32:30
 */
public class HomeFragment extends BaseFragment implements OnClickListener{
	private static final String TAG = "HomeFragment";
	
	private View viewSearchPark;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点

	private int currentItem ; // 当前图片的索引号
	
	private ScheduledExecutorService scheduledExecutorService;
	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};
	/**
	 * 数据库操作对象
	 */
	private DataBaseAdapter dba;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	private void initView(){
		dba = ((CommonApplication) getActivity().getApplicationContext()).getDbAdapter();
		
		viewSearchPark = getActivity().findViewById(R.id.viewSearchPark);
		viewSearchPark.setOnClickListener(this);
		
		imageResId = new int[] { R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3,R.drawable.slide_1 };
		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}
		dots = new ArrayList<View>();
		dots.add(getActivity().findViewById(R.id.v_dot0));
		dots.add(getActivity().findViewById(R.id.v_dot1));
		dots.add(getActivity().findViewById(R.id.v_dot2));
		dots.add(getActivity().findViewById(R.id.v_dot3));

		viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerBanner);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		currentItem = imageViews.size()*100-1; 
		viewPager.setCurrentItem(imageViews.size()*100);
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.viewSearchPark:
			openActivity(SearchParkingActivity.class);
			break;

		default:
			break;
		}
	}
	@Override
	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleWithFixedDelay(new ScrollTask(), 0, 4, TimeUnit.SECONDS);
		super.onStart();
	}

	
	@Override
	public void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (viewPager) {
				Log.i(TAG, "--currentItem-->>" + currentItem);
				currentItem = currentItem + 1;
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}
	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition % imageViews.size()).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position % imageViews.size()).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	/**
	 * 填充ViewPager页面的适配器
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View v, int position) {
			View view = imageViews.get((position % imageViews.size()));
			((ViewPager) v).addView(view);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
			return imageViews.get(position % imageViews.size());
		}

		@Override
		public void destroyItem(View arg0, int position, Object arg2) {
			((ViewPager) arg0).removeView(imageViews.get(position % imageViews.size()));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
}
