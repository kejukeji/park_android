package com.parkmecn.android.ui.searchparking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.parkmecn.android.CommonApplication;
import com.parkmecn.android.Constants;
import com.parkmecn.android.R;
import com.parkmecn.android.adapter.NearbyParkAdapter;
import com.parkmecn.android.bean.NearbyParkBean;
import com.parkmecn.android.bean.ResponseBean;
import com.parkmecn.android.helper.BusinessHerlper;
import com.parkmecn.android.ui.base.BaseActivity;
import com.parkmecn.android.util.DateUtil;
import com.parkmecn.android.util.NetUtil;
import com.parkmecn.android.view.XListView;
import com.parkmecn.android.view.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 停车场列表
 * 
 * @author Zhoujun
 * @version 创建时间：2014-5-4 下午4:50:44
 */
public class ParkingListActivity extends BaseActivity implements OnClickListener, IXListViewListener {
	private final  String mPageName = "ParkingListActivity";
	private CommonApplication app;
	private TextView tvLocation;

	private XListView lvParkding;
	private NearbyParkAdapter adapter;
	private ArrayList<NearbyParkBean> parkingList;
	private int pageIndex = 1;
	private boolean isLoad = false;// 是否正在加载数据
	private boolean isComplete = false;// 是否加载完了；
	private boolean isRefresh = false;// 是否刷新
	private Handler mHandler = new Handler();

	private double longtitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.park_list);
		app = (CommonApplication) getApplication();

		longtitude = getIntent().getExtras().getDouble("Longitude");
		latitude = getIntent().getExtras().getDouble("latitude");

		initBar();
		findView();
		fillData();
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		tvLeft.setOnClickListener(this);
		tvTitle.setText("附近停车场");

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocation.setText("您当前的位置：" + app.getUserAddress());

		lvParkding = (XListView) findViewById(R.id.lvlocationList);
		parkingList = new ArrayList<NearbyParkBean>();

	}

	private void onLoad() {
		lvParkding.stopRefresh();
		lvParkding.stopLoadMore();
		lvParkding.setRefreshTime(DateUtil.dateToString("HH:mm", new Date()));
	}

	/**
	 * 初始化数据
	 */
	private void fillData() {
		refreshData();
		lvParkding.setPullLoadEnable(true);
		lvParkding.setXListViewListener(this);
		lvParkding.setOnItemClickListener(itemListener);
		adapter = new NearbyParkAdapter(ParkingListActivity.this, parkingList, app);
		lvParkding.setAdapter(adapter);
	}

	/**
	 * 更新数据
	 */
	private void refreshData() {
		if (NetUtil.checkNet(ParkingListActivity.this)) {
			new getParkListTask().execute();
		} else {
			showShortToast(R.string.NoSignalException);
		}
	}

	/**
	 * listview点击事件
	 */
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 - 1 >= parkingList.size()) {
				return;
			}
			NearbyParkBean bean = parkingList.get(arg2 - 1);
			Bundle b = new Bundle();
			b.putSerializable(Constants.EXTRA_DATA, bean);
			openActivity(ParkingDetailsActivity.class, b);
		}
	};

	/**
	 * 获取停车list接口
	 * 
	 * */
	private ProgressDialog pd;

	private class getParkListTask extends AsyncTask<Void, Void, ResponseBean<NearbyParkBean>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isRefresh && pageIndex == 1 && ParkingListActivity.this != null) {
				if (pd == null) {
					pd = new ProgressDialog(ParkingListActivity.this);
				}
				pd.setMessage(getString(R.string.xlistview_header_hint_loading));
				pd.show();
			}
		}

		@Override
		protected ResponseBean<NearbyParkBean> doInBackground(Void... params) {
			try {
				return new BusinessHerlper().getParkList(latitude, longtitude, pageIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ResponseBean<NearbyParkBean> result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if (isRefresh) {
				parkingList.clear();
			}
			if (result != null) {
				List<NearbyParkBean> tempList = result.getObjList();
				boolean isLastPage = false;
				if (tempList.size() > 0) {
					parkingList.addAll(tempList);
					pageIndex++;
				} else {
					isLastPage = true;
				}
				if (isLastPage) {
					isComplete = true;
				} else {
					if (tempList.size() > 0 && tempList.size() < Constants.PAGE_SIZE) {
						isComplete = true;
					}
				}
				if (isComplete) {
					lvParkding.setFooterViewInvisible(true);
				} else {
					lvParkding.setFooterViewInvisible(false);
				}
				if (pageIndex == 1 && tempList.size() == 0) {
					showShortToast("无数据");
				}
				if (pageIndex >= 2 && tempList.size() == 0) {
					showShortToast("无更多数据");
				}
				adapter.notifyDataSetChanged();
			}else{
				showShortToast("数据解析错误");
			}
			isLoad = false;
			isRefresh = false;

		}
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

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!NetUtil.checkNet(ParkingListActivity.this)) {
					showShortToast(R.string.NoSignalException);
					return;
				}
				if (isRefresh) {
					return;
				}
				isRefresh = true;
				pageIndex = 1;
				onLoad();
				refreshData();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		if (!NetUtil.checkNet(ParkingListActivity.this)) {
			showShortToast(R.string.NoSignalException);
			return;
		}
		if (!isLoad && !isComplete) {
			refreshData();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		MobclickAgent.onPause(this);
	}
}
