package com.keju.park.ui.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.db.DataBaseAdapter;
import com.keju.park.ui.base.BaseFragment;
import com.keju.park.ui.searchparking.SearchParkingActivity;

/**
 * 首页
 * @author Zhoujun
 * @version 创建时间：2014-5-2 下午3:32:30
 */
public class HomeFragment extends BaseFragment implements OnClickListener{
	private View viewSearchPark;
	
	/**
	 * 数据库操作对象
	 */
	private DataBaseAdapter dba;
	
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
		dba = ((CommonApplication) getActivity().getApplicationContext()).getDbAdapter();
		
		viewSearchPark = getActivity().findViewById(R.id.viewSearchPark);
		viewSearchPark.setOnClickListener(this);
		
		
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.viewSearchPark:
			openActivity(SearchParkingActivity.class);
			break;

		default:
			break;
		}
	}
}
