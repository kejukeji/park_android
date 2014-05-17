package com.keju.park.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.keju.park.Constants;
import com.keju.park.R;
import com.keju.park.listener.ActivityClickListener;
import com.keju.park.ui.more.MoreFragment;
import com.keju.park.ui.searchparking.VoiceSearchActivity;
import com.keju.park.ui.tab.FootFragment;
import com.keju.park.ui.tab.SearchParkingFragment;
import com.keju.park.util.AndroidUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

public class MainActivity extends FragmentActivity implements ActivityClickListener{
	private View footLayout;
	private final static int HOME_TAB_ID = 1;// 职位
	private final static int MORE_TAB_ID = 2;// 更多
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		initView();
	}
	private void initView(){
		footLayout = findViewById(R.id.footLayout);
		//
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.mainLayout, new SearchParkingFragment(),HOME_TAB_ID + "");
		transaction.replace(R.id.footLayout, new FootFragment());
		transaction.commit();
		
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
	}
	@Override
	public void OnSetActivityListener(int activityId) {
		switch (activityId) {
		case Constants.FRAGMENT_VOICE:
//			hideFootLayout();
			Intent intent = new Intent(this, VoiceSearchActivity.class);
			startActivity(intent);
			break;
		case Constants.FRAGMENT_HOME:
			showPager(HOME_TAB_ID);
			break;
		case Constants.FRAGMENT_MORE:
			showPager(MORE_TAB_ID);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 界面跳转
	 * @param tab_id
	 */
	private void showPager(int tab_id){
		FragmentManager fm = getSupportFragmentManager();
		SearchParkingFragment homeFragment = (SearchParkingFragment) fm.findFragmentByTag(HOME_TAB_ID + "");
		MoreFragment moreFragment = (MoreFragment) fm.findFragmentByTag(MORE_TAB_ID + "");
		FragmentTransaction transaction = fm.beginTransaction();
		switch (tab_id) {
		case HOME_TAB_ID:
			if(homeFragment == null){
				transaction.add(R.id.mainLayout,new SearchParkingFragment(), HOME_TAB_ID + "");
			}else{
				transaction.show(homeFragment);
			}
			if(moreFragment != null){
				transaction.hide(moreFragment);
			}
			break;
		case MORE_TAB_ID:
			if(moreFragment == null){
				transaction.add(R.id.mainLayout,new MoreFragment(), MORE_TAB_ID + "");
			}else{
				transaction.show(moreFragment);
			}
			if(homeFragment != null){
				transaction.hide(homeFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}
	private long exitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, "再按一次退出停车宝", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				AndroidUtil.exitApp(this);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);       //统计时长
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
