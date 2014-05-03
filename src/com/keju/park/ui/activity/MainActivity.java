package com.keju.park.ui.activity;

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
import com.keju.park.ui.fragment.FootFragment;
import com.keju.park.ui.fragment.HomeFragment;

public class MainActivity extends FragmentActivity implements ActivityClickListener{
	private View footLayout;
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
		transaction.replace(R.id.mainLayout, new HomeFragment());
		transaction.replace(R.id.footLayout, new FootFragment());
		transaction.commit();
	}
	@Override
	public void OnSetActivityListener(int activityId) {
		switch (activityId) {
		case Constants.FRAGMENT_VOICE:
//			hideFootLayout();
			Intent intent = new Intent(this, VoiceLookCarport.class);
			startActivity(intent);
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
}
