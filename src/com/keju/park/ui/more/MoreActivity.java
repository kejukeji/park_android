package com.keju.park.ui.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.keju.park.R;
import com.keju.park.ui.base.BaseActivity;

/**
 * 更多界面
 * 
 * @author zhouyong
 * @date 2014年5月15日 下午7:30:30
 * @version 1.0
 */
public class MoreActivity extends BaseActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		
		initBar();
	}

	@Override
	public void onClick(View v) {

	}

}
