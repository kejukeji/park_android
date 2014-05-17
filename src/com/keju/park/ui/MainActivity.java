package com.keju.park.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.keju.park.Constants;
import com.keju.park.R;
import com.keju.park.listener.ActivityClickListener;
import com.keju.park.ui.searchparking.VoiceSearchActivity;
import com.keju.park.ui.tab.FootFragment;
import com.keju.park.ui.tab.SearchParkingFragment;

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
	 * 隐藏底部菜单；
	 */
	private void hideFootLayout(){
		footLayout.setVisibility(View.GONE);
	}
	/**
	 * 跳转界面；
	 */
	private void toPager(Fragment fragment){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.mainLayout, fragment);
		transaction.commit();
	}
	/**
	 * 界面跳转
	 * @param tab_id
	 */
	private void showPager(int tab_id){
		FragmentManager fm = getSupportFragmentManager();
		SearchParkingFragment homeFragment = (SearchParkingFragment) fm.findFragmentByTag(HOME_TAB_ID + "");
		SearchParkingFragment moreFragment = (SearchParkingFragment) fm.findFragmentByTag(MORE_TAB_ID + "");
		FragmentTransaction transaction = fm.beginTransaction();
		switch (tab_id) {
		case HOME_TAB_ID:
			if(homeFragment == null){
				transaction.add(new SearchParkingFragment(), HOME_TAB_ID + "");
			}else{
				transaction.show(homeFragment);
			}
			break;
		case MORE_TAB_ID:
			if(moreFragment == null){
				transaction.add(new SearchParkingFragment(), MORE_TAB_ID + "");
			}else{
				transaction.show(moreFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}
}
