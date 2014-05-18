package com.parkmecn.android.ui.tab;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.parkmecn.android.Constants;
import com.parkmecn.android.R;
import com.parkmecn.android.listener.ActivityClickListener;
import com.parkmecn.android.ui.base.BaseFragment;

/**
 * 底部菜单fragment
 * 
 * @author Zhoujun
 * @version 创建时间：2014-5-2 下午3:32:56
 */
public class FootFragment extends BaseFragment implements OnCheckedChangeListener {
	private View viewVoice;
	private RadioButton rbHome, rbMore;
	private ActivityClickListener activityClickListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_foot, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			activityClickListener = (ActivityClickListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		viewVoice = getActivity().findViewById(R.id.viewVoice);
		viewVoice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_VOICE);
			}
		});
		rbHome = (RadioButton) getActivity().findViewById(R.id.rb_home);
		rbMore = (RadioButton) getActivity().findViewById(R.id.rb_more);
		rbHome.setOnCheckedChangeListener(this);
		rbMore.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.rb_home:
			if (isChecked) {
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_HOME);
				rbHome.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
						.getDrawable(R.drawable.tab_home_sel), null, null);
				rbMore.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_more),
						null, null);
				rbMore.setChecked(false);
			}
			break;

		case R.id.rb_more:
			if (isChecked) {
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_MORE);
				rbHome.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_home),
						null, null);
				rbMore.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
						.getDrawable(R.drawable.tab_more_sel), null, null);
				rbHome.setChecked(false);
			}
			break;
		default:
			break;
		}
	}
}
