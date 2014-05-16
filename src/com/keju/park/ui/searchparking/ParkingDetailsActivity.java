package com.keju.park.ui.searchparking;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.keju.park.CommonApplication;
import com.keju.park.Constants;
import com.keju.park.R;
import com.keju.park.Urls;
import com.keju.park.bean.NearbyParkBean;
import com.keju.park.ui.base.BaseActivity;
import com.keju.park.util.StringUtil;

/**
 * 停车列表详情界面
 * 
 * @author zhouyong
 * @date 2014年5月16日 下午1:43:32
 * @version 1.0
 */
public class ParkingDetailsActivity extends BaseActivity implements OnClickListener {
	private final String TAG = "ParkingDetailsActivity";

	private CommonApplication app;
	private TextView tvLocation, tvLocationPark, tvAddress, tvDistance;
	private LinearLayout viewNavigation;
	private TextView tvDayTimePrice, tvNightPrice;
	private NearbyParkBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.park_details);
		app = (CommonApplication) getApplication();
		bean = (NearbyParkBean) getIntent().getExtras().getSerializable(Constants.EXTRA_DATA);

		initBar();

		findView();
		fillData();

	}

	private void findView() {
		tvLeft.setOnClickListener(this);
		tvTitle.setText("停车详情");

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocationPark = (TextView) findViewById(R.id.tvLocationPark);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvDayTimePrice = (TextView) findViewById(R.id.tvDayTimePrice);
		tvNightPrice = (TextView) findViewById(R.id.tvNightPrice);

		viewNavigation = (LinearLayout) findViewById(R.id.viewNavigation);
		viewNavigation.setOnClickListener(this);
	}

	private void fillData() {
		tvLocation.setText("您当前的位置：" + app.getUserAddress());
		getData(bean.getId());

	}

	/**
	 * 
	 */
	private void getData(int parkId) {
		RequestQueue mQueue = Volley.newRequestQueue(ParkingDetailsActivity.this);
		mQueue.add(new StringRequest(Method.GET, Urls.BASE_URL1 + parkId, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				Log.i(TAG, "argo = " + arg0);
				try {
					JSONObject jsonObject = new JSONObject(arg0);
					JSONObject job = jsonObject.getJSONObject("data");
					setDetailData(job);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		}));

		mQueue.start();
	}

	protected void setDetailData(JSONObject job) {
		try {
			tvLocationPark.setText(job.getString("address"));
			tvAddress.setText(job.getString("name"));
			if (app.getLastLocation() != null) {
				double distance = StringUtil.getDistance(app.getLastLocation().getLatitude(), app.getLastLocation().getLongitude(), bean
						.getLocationList().get(0).getLatitude(), bean.getLocationList().get(0).getLongitude());
				String distanceStr = null;
				if (distance > 1000) {
					distance = distance / 1000;
					distanceStr = String.format("%.0f", distance) + "km";
				} else {
					distanceStr = String.format("%.0f", distance) + "m";
				}
				tvDistance.setText(Html.fromHtml("<a>距离<font color='#fe0000'>" + distanceStr + "</a>    空车位" + "<a><font color='#fe0000'>"
						+ bean.getLast() + "</a>个"));
			} else {
				tvDistance.setText("");
			}
			tvDayTimePrice.setText(job.getString("dayPrice"));
			tvNightPrice.setText(job.getString("nightPrice"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;
		case R.id.viewNavigation:
			BaiduNaviManager.getInstance().launchNavigator(ParkingDetailsActivity.this, app.getLastLocation().getLatitude(),
					app.getLastLocation().getLongitude(), app.getUserAddress(), bean.getLocationList().get(0).getLatitude(),
					bean.getLocationList().get(0).getLongitude(), bean.getAddress(), NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
					true, // 真实导航
					BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
					new OnStartNavigationListener() { // 跳转监听

						@Override
						public void onJumpToNavigator(Bundle configParams) {
							Intent intent = new Intent(ParkingDetailsActivity.this, BNavigatorActivity.class);
							intent.putExtras(configParams);
							startActivity(intent);
						}

						@Override
						public void onJumpToDownloader() {
						}
					});
			break;
		default:
			break;
		}
	}
}
