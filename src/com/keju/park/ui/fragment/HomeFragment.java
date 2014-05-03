package com.keju.park.ui.fragment;

import com.keju.park.R;
import com.keju.park.ui.activity.LookParkingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 首页
 * @author Zhoujun
 * @version 创建时间：2014-5-2 下午3:32:30
 */
public class HomeFragment extends Fragment implements OnClickListener{
	private View viewSearchPark;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	private void initView(){
		viewSearchPark = getActivity().findViewById(R.id.viewSearchPark);
		viewSearchPark.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.viewSearchPark:
			intent = new Intent(getActivity(), LookParkingActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
