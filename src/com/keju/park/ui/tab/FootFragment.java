package com.keju.park.ui.tab;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.keju.park.Constants;
import com.keju.park.R;
import com.keju.park.listener.ActivityClickListener;
import com.keju.park.ui.base.BaseFragment;

/**
 * 底部菜单fragment
 * @author Zhoujun
 * @version 创建时间：2014-5-2 下午3:32:56
 */
public class FootFragment extends BaseFragment implements OnCheckedChangeListener {
	private View viewVoice;
	private RadioButton rbHome,rbCollection,rbStore,rbPersonalCenter;
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
	private void initView(){
		viewVoice = getActivity().findViewById(R.id.viewVoice);
		viewVoice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_VOICE);
			}
		});
		rbHome = (RadioButton) getActivity().findViewById(R.id.rb_home);
		rbCollection = (RadioButton) getActivity().findViewById(R.id.rb_collection);
		rbStore = (RadioButton) getActivity().findViewById(R.id.rb_store);
		rbPersonalCenter = (RadioButton) getActivity().findViewById(R.id.rb_personal_center);
		rbHome.setOnCheckedChangeListener(this);
		rbCollection.setOnCheckedChangeListener(this);
		rbStore.setOnCheckedChangeListener(this);
		rbPersonalCenter.setOnCheckedChangeListener(this);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.rb_home:
			if(isChecked){
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_HOME);
				rbCollection.setChecked(false);
				rbStore.setChecked(false);
				rbPersonalCenter.setChecked(false);
			}
			break;
		case R.id.rb_collection:
			if(isChecked){
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_COLLECTION);
				rbHome.setChecked(false);
				rbStore.setChecked(false);
				rbPersonalCenter.setChecked(false);
			}
			break;
		
		case R.id.rb_store:
			if(isChecked){
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_STORE);
				rbHome.setChecked(false);
				rbCollection.setChecked(false);
				rbPersonalCenter.setChecked(false);
			}
			break;
		case R.id.rb_personal_center:
			if(isChecked){
				activityClickListener.OnSetActivityListener(Constants.FRAGMENT_PERSONAL_CENTER);
				rbHome.setChecked(false);
				rbCollection.setChecked(false);
				rbStore.setChecked(false);
			}
			break;
		default:
			break;
		}
	}
}
