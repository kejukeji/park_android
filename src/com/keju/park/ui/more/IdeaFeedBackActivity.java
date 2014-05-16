package com.keju.park.ui.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.park.R;
import com.keju.park.ui.base.BaseActivity;

/**
 * 意见反馈
 * 
 * @author zhouyong
 * @date 2014年5月16日 上午11:15:12
 * @version 1.0
 */
public class IdeaFeedBackActivity extends BaseActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
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

}
