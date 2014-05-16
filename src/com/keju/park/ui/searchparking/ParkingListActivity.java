package com.keju.park.ui.searchparking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.keju.park.CommonApplication;
import com.keju.park.Constants;
import com.keju.park.R;
import com.keju.park.adapter.NearbyParkAdapter;
import com.keju.park.bean.NearbyParkBean;
import com.keju.park.ui.base.BaseActivity;

/**
 * 停车场列表
 * 
 * @author Zhoujun
 * @version 创建时间：2014-5-4 下午4:50:44
 */
public class ParkingListActivity extends BaseActivity implements OnClickListener {
	private final String TAG = "ParkingListActivity";

	private CommonApplication app;
	private TextView tvLocation;

	private ListView lvlocation;
	private NearbyParkAdapter adapter;
	private ArrayList<NearbyParkBean> nearbyList;

	private double Longitude;
	private double Latitude;

	private ProgressDialog pDialog;//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.park_list);
		app = (CommonApplication) getApplication();

		Longitude = getIntent().getExtras().getDouble("Longitude");
		Latitude = getIntent().getExtras().getDouble("Latitude");

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
		nearbyList = new ArrayList<NearbyParkBean>();

		lvlocation = (ListView) findViewById(R.id.lvlocationList);
		lvlocation.setOnItemClickListener(itemListener);

	}

	/**
	 * 初始化数据
	 */
	private void fillData() {

		if (Longitude != 0.0 && Latitude != 0.0) {
			getData(Longitude, Latitude);
		} else {
			getData(app.getLastLocation().getLongitude(), app.getLastLocation().getLatitude());// 用户当前的经纬度
		}
		// adapter = new NearbyParkAdapter();
		// lvlocation.setAdapter(adapter);
	}

	/**
	 * listView 点击事件
	 * */
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position >= nearbyList.size()) {
				return;
			}
			NearbyParkBean bean = nearbyList.get(position);
			Bundle b = new Bundle();
			b.putSerializable(Constants.EXTRA_DATA, bean);
			openActivity(ParkingDetailsActivity.class, b);
		}

	};

	/**
	 * 
	 */
	private void getData(Double longitude, Double Latitude) {
		RequestQueue mQueue = Volley.newRequestQueue(ParkingListActivity.this);
		mQueue.add(new StringRequest(Method.GET, "http://park.kejukeji.com/ssbusy/carbarn/latitude-longitude?latitude=" + Latitude
				+ "&longitude=" + longitude, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				try {
					JSONObject jsonObject = new JSONObject(arg0);
					Log.i(TAG, "argo = " + arg0);
					JSONArray array = jsonObject.getJSONArray("data");
					try {
						List<NearbyParkBean> list = NearbyParkBean.constractList(array);
						adapter = new NearbyParkAdapter(ParkingListActivity.this, list, app);
						lvlocation.setAdapter(adapter);
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
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

}
