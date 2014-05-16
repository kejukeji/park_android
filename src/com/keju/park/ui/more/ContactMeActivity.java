package com.keju.park.ui.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.park.R;
import com.keju.park.ui.base.BaseActivity;

/**
 * 联系我们
 * 
 * @author zhouyong
 * @date 2014年5月16日 上午11:16:36
 * @version 1.0
 */
public class ContactMeActivity extends BaseActivity implements OnClickListener {
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
}
