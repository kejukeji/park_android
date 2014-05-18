package com.keju.park.ui.searchparking;

import android.os.Bundle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.keju.park.CommonApplication;
import com.keju.park.Constants;
import com.keju.park.R;
import com.keju.park.bean.NearbyParkBean;
import com.keju.park.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 显示路线导航；
 * 
 * @author Zhoujun
 * @version 创建时间：2014-4-27 下午9:58:58
 */
public class ShowRouteActivity extends BaseActivity {
	private final  String mPageName = "ShowRouteActivity";
	MapView mMapView = null; // 地图View
	// 搜索相关
	MKSearch mMKSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private NearbyParkBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonApplication app = (CommonApplication) getApplication();
		bean = (NearbyParkBean) getIntent().getExtras().getSerializable(Constants.EXTRA_DATA);
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(new CommonApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_show_route);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.getController().setCenter(new GeoPoint((int) (app.getLastLocation().getLatitude() * 1E6), (int) (app.getLastLocation().getLongitude() * 1E6)));

		// 初始化搜索模块，注册事件监听
		mMKSearch = new MKSearch();
		mMKSearch.init(((CommonApplication) getApplication()).mBMapManager, new MySearchListener());
		MKPlanNode start = new MKPlanNode();
		// start.pt = new GeoPoint((int) (31.217899 * 1E6), (int) (121.53427 *
		// 1E6));
		start.pt = new GeoPoint((int) (app.getLastLocation().getLatitude() * 1E6), (int) (app.getLastLocation()
				.getLongitude() * 1E6));
		MKPlanNode end = new MKPlanNode();
		end.pt = new GeoPoint((int) (bean.getLocationList().get(0).getLatitude() * 1E6), (int) (bean.getLocationList()
				.get(0).getLongitude() * 1E6));
		mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);// 设置驾车路线搜索策略，时间优先、费用最少或距离最短
		mMKSearch.drivingSearch(null, start, null, end);

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

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
			// 返回驾乘路线搜索结果
			if (result == null) {
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(ShowRouteActivity.this, mMapView); // 此处仅展示一个方案作为示例
			routeOverlay.setData(result.getPlan(0).getRoute(0));
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();
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

		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {
			// 在此处理短串请求返回结果.
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

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
