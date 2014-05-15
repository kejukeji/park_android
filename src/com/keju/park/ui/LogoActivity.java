package com.keju.park.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.keju.park.R;
import com.keju.park.ui.searchparking.SearchParkingActivity;

/**
 * 启动页
 * @author Zhoujun
 * @version 创建时间：2014年5月15日 下午3:00:15
 */
public class LogoActivity extends Activity {
	private View viewLogo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		viewLogo = findViewById(R.id.viewLogo);
		animation();
	}
	
    /**
	 * 跳转；
	 */
	private void animation() {
		AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
		aa.setDuration(2500);
		viewLogo.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation arg0) {
				startActivity(new Intent(LogoActivity.this, SearchParkingActivity.class));
				finish();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
	}

}

