package com.parkmecn.android.ui.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.parkmecn.android.R;
import com.parkmecn.android.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 联系我们
 * 
 * @author zhouyong
 * @date 2014年5月16日 上午11:16:36
 * @version 1.0
 */
public class ContactMeActivity extends BaseActivity implements OnClickListener {
	private final  String mPageName = "ContactMeActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_me);
		initBar();
		findView();
		fillData();
	}

	private void findView() {
		tvTitle.setText("联系我们");
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
