package com.keju.park.ui.activity;

import java.text.DecimalFormat;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.keju.park.R;
import com.keju.park.util.NetUtil;

public class MainActivity extends FragmentActivity {
	private TextView tvTest;

	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initLocation();
	}

	private void init() {
		tvTest = (TextView) findViewById(R.id.tvTest);

		// VolleyTest
		RequestQueue mQueue = Volley.newRequestQueue(this);
		mQueue.add(new JsonObjectRequest(Method.GET,
				"http://neituidev.sinaapp.com/?version=1.0.3&name=devapi&handle=getconfig&type=city&json=1", null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						Toast.makeText(MainActivity.this, arg0.toString(), Toast.LENGTH_LONG).show();
						Log.d("MainActivity", arg0.toString());
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.d("MainActivity", arg0.toString());
					}
				}));
		mQueue.start();
	}

	/**
	 * 初始化定位
	 */
	private void initLocation() {
		if (!NetUtil.checkNet(this)) {
			Toast.makeText(MainActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
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

	private ProgressDialog pd;

	/**
	 * 监听函数，有更新位置的时候
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
				return;
			}
			if (pd != null) {
				pd.dismiss();
			}
			mLocationClient.stop();
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ncity : ");
				sb.append(location.getCity());
			}
			tvTest.setText(sb.toString());
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}

		}
	}
}
