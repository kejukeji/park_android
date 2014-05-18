package com.parkmecn.android.ui.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.parkmecn.android.R;
import com.parkmecn.android.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们
 * 
 * @author zhouyong
 * @date 2014年5月16日 上午9:59:33
 * @version 1.0
 */
public class AboutMeActivity extends BaseActivity implements OnClickListener {
	private final  String mPageName = "AboutMeActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
		initBar();
		findView();
		fillData();
	}

	private void findView() {
		tvTitle.setText("关于我们");
		tvLeft.setOnClickListener(this);
	}

	private void fillData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		MobclickAgent.onPause(this);
	}
}
