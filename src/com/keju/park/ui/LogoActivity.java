
package com.keju.park.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.util.SharedPrefUtil;

/**
 * 启动页
 * 
 * @author Zhoujun
 * @version 创建时间：2014年5月15日 下午3:00:15
 */
public class LogoActivity extends Activity {
	private View viewLogo;
	private CommonApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		app = (CommonApplication) getApplication();
		viewLogo = findViewById(R.id.viewLogo);
		// 初始化导航引擎
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener, "ZOatwHcx3iSNWy4VfQ0NhwxT", null);
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
				if(SharedPrefUtil.isReadGuider(LogoActivity.this)){
					startActivity(new Intent(LogoActivity.this, MainActivity.class));
				}else{
					startActivity(new Intent(LogoActivity.this, HelperActivity.class));
					SharedPrefUtil.setGuiderRead(LogoActivity.this, true);
				}
				finish();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
	}
	/**
	 * 导航初始化
	 */
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			// 导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航
			app.setmIsEngineInitSuccess(true);
		}

		public void engineInitStart() {
		}

		public void engineInitFail() {
		}
	};

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
}
