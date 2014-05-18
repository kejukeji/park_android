package com.parkmecn.android.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parkmecn.android.Constants;
import com.parkmecn.android.R;
import com.parkmecn.android.ui.base.BaseFragment;
import com.parkmecn.android.util.NetUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * 更多界面
 * 
 * @author zhouyong
 * @date 2014年5月15日 下午7:30:30
 * @version 1.0
 */
public class MoreFragment extends BaseFragment implements OnClickListener {
	private TextView tvLeft,tvTitle;
	private RelativeLayout rlaboutWe, rlcontactWe, rlversionUpdate, rlIdea, rlShare;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.more, container, false);
		findView(view);
		return view;
	}
	private void findView(View view) {
		tvLeft = (TextView) view.findViewById(R.id.tvLeft);
		tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		tvLeft.setVisibility(View.INVISIBLE);
		tvTitle.setText("更多");

		rlaboutWe = (RelativeLayout) view.findViewById(R.id.rlaboutWe);
		rlaboutWe.setOnClickListener(this);
		rlcontactWe = (RelativeLayout) view.findViewById(R.id.rlcontactWe);
		rlcontactWe.setOnClickListener(this);
		rlversionUpdate = (RelativeLayout) view.findViewById(R.id.rlversionUpdate);
		rlversionUpdate.setOnClickListener(this);
		rlIdea = (RelativeLayout) view.findViewById(R.id.rlIdea);
		rlIdea.setOnClickListener(this);
		rlShare = (RelativeLayout) view.findViewById(R.id.rlShare);
		rlShare.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlaboutWe:
			openActivity(AboutMeActivity.class);
			break;
		case R.id.rlcontactWe:
			 openActivity(ContactMeActivity.class);
			break;
		case R.id.rlversionUpdate:
			if (NetUtil.checkNet(getActivity())) {
				UmengUpdateAgent.update(getActivity());
				UmengUpdateAgent.setUpdateOnlyWifi(false);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						switch (updateStatus) {
						case 0: // has update
							UmengUpdateAgent.showUpdateDialog(
									getActivity(), updateInfo);
							break;
						case 1: // has no update
							showShortToast("已经是最新版本");
							break;
						case 2: // none wifi
							showShortToast("没有wifi连接， 只在wifi下更新");
							break;
						case 3: // time out
							showShortToast("连接服务器超时");
							break;
						}
					}
				});
			}else {
				showShortToast(R.string.NoSignalException);
			}
			break;
		case R.id.rlIdea:
			FeedbackAgent agent = new FeedbackAgent(getActivity());
			agent.startFeedbackActivity();
			break;
		case R.id.rlShare:
			recommandToYourFriend(Constants.APP_DOWNLOAD_URL, "想停车不再难么，猛戳这里，(微信用户，请复制链接在浏览器打开)");
			break;
		default:
			break;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		 MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
	}
}
