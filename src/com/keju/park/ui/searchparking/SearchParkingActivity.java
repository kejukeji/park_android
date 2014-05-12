package com.keju.park.ui.searchparking;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.db.DataBaseAdapter;
import com.keju.park.ui.base.BaseActivity;
import com.keju.park.util.NetUtil;

/**
 * 找车位界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-1 下午10:59:30
 */
public class SearchParkingActivity extends BaseActivity implements OnClickListener {
	private TextView tvLeft, tvTitle;
	private Button btnNearby, btnVoice;
	private TextView tvSearch;
	private CommonApplication app;
	private LinearLayout linearLayout;
	// 定位；
	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	private MKSearch mMKSearch = null;
	
	private DataBaseAdapter daAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (CommonApplication) getApplication();
		
		daAdapter  = ((CommonApplication) getApplication()).getDbAdapter();
		setContentView(R.layout.activity_search_parking);
		findView();
		fillData();
		app.addActivity(this);
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		app.initBMapInfo();

//		tvLeft = (TextView) findViewById(R.id.tvLeft);
//		tvLeft.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(R.string.search_parking);

		btnNearby = (Button) findViewById(R.id.btnLookNearby);
		btnNearby.setOnClickListener(this);
		btnVoice = (Button) findViewById(R.id.btnVoice);
		btnVoice.setOnClickListener(this);

		tvSearch = (TextView) findViewById(R.id.tvSearch);
		tvSearch.setOnClickListener(this);
		linearLayout = (LinearLayout) findViewById(R.id.vo_Search);
		linearLayout.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void fillData() {
		mMKSearch = new MKSearch();
		mMKSearch.init(((CommonApplication) getApplication()).mBMapManager, new MySearchListener());
		initLocation();
	}

	/**
	 * 初始化定位
	 */
	private void initLocation() {
		if (!NetUtil.checkNet(this)) {
			showShortToast(R.string.NoSignalException);
			return;
		}
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setPriority(LocationClientOption.NetWorkFirst);// 网络优先
		option.setOpenGps(false);
		option.setScanSpan(500);// 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		option.disableCache(true);
		option.setCoorType("bd09ll"); // 设置坐标类型
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (pd == null) {
			pd = new ProgressDialog(this);
		}
		pd.setMessage("定位中...");
		pd.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;
		case R.id.btnLookNearby:
			Bundle b = new Bundle();
			b.putDouble("Longitude", 0.00);
			b.putDouble("Latitude", 0.00);
			openActivity(ParkingListActivity.class, b);
			break;

		case R.id.btnVoice:
//			openActivity(VoiceSearchActivity.class);
			break;
		case R.id.tvSearch:
//			daAdapter.clearTableData("search_history");//清除表
			openActivity(HistorySearchParking.class);
			break;
		case R.id.vo_Search:
			openActivity(VoiceSearchActivity.class);
			break;
		default:
			break;
		}
	}

	private ProgressDialog pd;

	/**
	 * 监听函数，有更新位置的时候
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				showShortToast("定位失败");
				return;
			}
			if (pd != null) {
				pd.dismiss();
			}
			mLocationClient.stop();
			mMKSearch.reverseGeocode(new GeoPoint((int) (location.getLatitude() * 1e6),
					(int) (location.getLongitude() * 1e6)));
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}

		}
	}

	/**
	 * 搜索监听类
	 * 
	 * @author Zhoujun
	 * 
	 */
	public class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			// 返回地址信息搜索结果
			MKGeocoderAddressComponent kk = result.addressComponents;
			app.setCity(kk.city);
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
			// 返回驾乘路线搜索结果
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// 返回poi搜索结果
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
			// 返回步行路线搜索结果
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			// 返回联想词信息搜索结果
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {
			// 在此处理短串请求返回结果.
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}
	}
}
