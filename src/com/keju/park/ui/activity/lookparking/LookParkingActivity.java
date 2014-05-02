package com.keju.park.ui.activity.lookparking;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.keju.park.R;
import com.keju.park.ui.activity.base.BaseActivity;

/**
 * 找车位界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-1 下午10:59:30
 */
public class LookParkingActivity extends BaseActivity implements OnClickListener {
	private Button btnNearby, btnVoice;
	private EditText edSearch;

	// Tip
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_parking);
		initBar();

		findView();
		fillData();

	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		tvTitle.setText(R.string.look_parking);

		btnNearby = (Button) findViewById(R.id.btnLookNearby);
		btnNearby.setOnClickListener(this);

		btnVoice = (Button) findViewById(R.id.btnVoice);
		btnVoice.setOnClickListener(this);

		edSearch = (EditText) findViewById(R.id.edSearch);
		edSearch.setOnClickListener(this);
		edSearch.setInputType(InputType.TYPE_NULL);//强制关闭软键盘
	}

	/**
	 * 初始化数据
	 */
	private void fillData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLookNearby:

			break;

		case R.id.btnVoice:
			openActivity(VoiceSearchActivity.class);
			break;
		case R.id.edSearch:
			openActivity(HistorySearchParking.class);
			break;
		default:
			break;
		}
	}

}
