package com.keju.park.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.keju.park.R;

/**
 * 用户引导页
 * 
 * @author Zhoujun
 * @version 创建时间：2013-5-2 下午5:23:24
 */
public class HelperActivity extends Activity {
	private ViewPager vpPager;
	private ArrayList<View> alPages;
	// 包裹滑动图片LinearLayout
	private ViewGroup vgWelcomepage;
	// 包裹小圆点的LinearLayout
	private ViewGroup vgWelcomeflag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		init();
	}

	// 指引页面数据适配器
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return alPages.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(alPages.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPager) arg0).addView(alPages.get(arg1));
			} catch (Exception e) {
				
			}
			return alPages.get(arg1%alPages.size());   
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

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			changeState(arg0);
		}
	}
	/**
	 * 改变原点的状态
	 * 
	 * @param position
	 */
	private void changeState(int position) {
		int pos = position % 3;
		int count = vgWelcomeflag.getChildCount();
		for (int i = 0; i < count; i++) {
			ImageView ivItem = (ImageView) vgWelcomeflag.getChildAt(i);
			if (i == pos) {
				ivItem.setImageResource(R.drawable.ic_slide_point_sel);
			} else {
				ivItem.setImageResource(R.drawable.ic_slide_point_nor);
			}
		}
	}


	private void init() {
		LayoutInflater inflater = getLayoutInflater();
		alPages = new ArrayList<View>();
		alPages.add(inflater.inflate(R.layout.help_1, null));
		alPages.add(inflater.inflate(R.layout.help_2, null));
		alPages.add(inflater.inflate(R.layout.help_3, null));

		vgWelcomepage = (ViewGroup) inflater.inflate(R.layout.guide, null);

		vgWelcomeflag = (ViewGroup) vgWelcomepage.findViewById(R.id.viewGroup);
		vpPager = (ViewPager) vgWelcomepage.findViewById(R.id.guidePages);
		for (int i = 0; i < 3; i++) {
			ImageView ivRound = new ImageView(this);
			LinearLayout.LayoutParams pRound = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			pRound.leftMargin = 5;
			pRound.rightMargin = 5;
			ivRound.setLayoutParams(pRound);
			if (i == 0) {
				ivRound.setImageResource(R.drawable.ic_slide_point_sel);
			} else {
				ivRound.setImageResource(R.drawable.ic_slide_point_nor);
			}
			vgWelcomeflag.addView(ivRound, i);

		}

		setContentView(vgWelcomepage);
				
		vpPager.setAdapter(new GuidePageAdapter());
		vpPager.setOnPageChangeListener(new GuidePageChangeListener());
		vpPager.setCurrentItem(0);
		View view = alPages.get(2);
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HelperActivity.this, MainActivity.class));
				finish();
			}
		});
	}
}
