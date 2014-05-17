package com.keju.park.ui.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.keju.park.Constants;
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
	private RelativeLayout rlaboutWe, rlcontactWe, rlversionUpdate, rlIdea, rlShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);

		initBar();
		findView();
		fillData();
	}

	private void findView() {
		tvLeft.setOnClickListener(this);
		tvTitle.setText(R.string.more);

		rlaboutWe = (RelativeLayout) findViewById(R.id.rlaboutWe);
		rlaboutWe.setOnClickListener(this);
		rlcontactWe = (RelativeLayout) findViewById(R.id.rlcontactWe);
		rlcontactWe.setOnClickListener(this);
		rlversionUpdate = (RelativeLayout) findViewById(R.id.rlversionUpdate);
		rlversionUpdate.setOnClickListener(this);
		rlIdea = (RelativeLayout) findViewById(R.id.rlIdea);
		rlIdea.setOnClickListener(this);
		rlShare = (RelativeLayout) findViewById(R.id.rlShare);
		rlShare.setOnClickListener(this);
	}

	private void fillData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;
		case R.id.rlaboutWe:
			openActivity(AboutMeActivity.class);
			break;
		case R.id.rlcontactWe:
			openActivity(ContactMeActivity.class);
			break;
		case R.id.rlversionUpdate:
			break;
		case R.id.rlIdea:
			openActivity(IdeaFeedBackActivity.class);
			break;
		case R.id.rlShare:
			recommandToYourFriend(Constants.APP_DOWNLOAD_URL, "我发现一个很好的 找创业园区的APP，下载地址：");
			break;
		default:
			break;
		}
	}

}
