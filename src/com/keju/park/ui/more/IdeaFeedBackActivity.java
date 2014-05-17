package com.keju.park.ui.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.park.R;
import com.keju.park.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 意见反馈
 * 
 * @author zhouyong
 * @date 2014年5月16日 上午11:15:12
 * @version 1.0
 */
public class IdeaFeedBackActivity extends BaseActivity implements OnClickListener {
	private final  String mPageName = "IdeaFeedBackActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_feedback);
		initBar();
		findView();
		fillData();
	}

	private void findView() {
		tvTitle.setText("意见反馈");
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
