package com.keju.park.ui.searchparking;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.ui.base.BaseActivity;

/**
 * 停车场列表
 * 
 * @author Zhoujun
 * @version 创建时间：2014-5-4 下午4:50:44
 */
public class ParkingListActivity extends BaseActivity implements OnClickListener {
	private CommonApplication app;
	private TextView tvLocation;

	private ListView lvlocationList;
	private NearbyParkAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.park_list);
		app = (CommonApplication) getApplication();
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
		tvLocation.setText(app.getCity());

		lvlocationList = (ListView) findViewById(R.id.lvlocationList);

		getData(app.getLastLocation().getLongitude(), app.getLastLocation().getLatitude());
	}

	/**
	 * 
	 */
	private void getData(Double longitude, Double Latitude) {
		RequestQueue mQueue = Volley.newRequestQueue(this);
		mQueue.add(new JsonObjectRequest(Method.GET,
				"http://park.kejukeji.com/ssbusy/carbarn/latitude-longitude?latitude=" + longitude + "&longitude="
						+ Latitude, null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
                    
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				}));
		mQueue.start();
	}

	/**
	 * 初始化数据
	 */
	private void fillData() {

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

	/**
	 * 附近停车场适配器
	 * 
	 * */

	private class NearbyParkAdapter extends BaseAdapter {
		@Override
		public int getCount() {

			return 0;
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return null;
		}

	}
}
