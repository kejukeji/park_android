package com.keju.park.ui.searchparking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.keju.park.R;
import com.keju.park.bean.NearbyParkBean;
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

	private ListView lvlocation;
	private NearbyParkAdapter adapter;
	private ArrayList<NearbyParkBean> nearbyList;

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

		lvlocation = (ListView) findViewById(R.id.lvlocationList);
		nearbyList = new ArrayList<NearbyParkBean>();

	}

	/**
	 * 初始化数据
	 */
	private void fillData() {

		getData(app.getLastLocation().getLongitude(), app.getLastLocation().getLatitude());

		// adapter = new NearbyParkAdapter();
		// lvlocation.setAdapter(adapter);

	}

	/**
	 * 
	 */
	private void getData(Double longitude, Double Latitude) {
		RequestQueue mQueue = Volley.newRequestQueue(ParkingListActivity.this);
		mQueue.add(new StringRequest(Method.GET, "http://park.kejukeji.com/ssbusy/carbarn/latitude-longitude?latitude="
				+ longitude + "&longitude=" + Latitude, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				try {
					JSONObject jsonObject = new JSONObject(arg0);
					
					JSONArray array =jsonObject.getJSONArray("data");
					try {
						List<NearbyParkBean> list = NearbyParkBean.constractList(array);
						// nearbyList.addAll(list);
						adapter = new NearbyParkAdapter(list);
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

	/**
	 * 附近停车场适配器
	 * 
	 * */

	private class NearbyParkAdapter extends BaseAdapter {
		private List<NearbyParkBean> nearbyLists;

		/**
		 * @param list
		 */
		public NearbyParkAdapter(List<NearbyParkBean> list) {
			this.nearbyLists = list;
		}

		@Override
		public int getCount() {

			return nearbyLists.size();
		}

		@Override
		public Object getItem(int position) {

			return nearbyLists.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			NearbyParkBean bean = nearbyLists.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.park_item, null);
				holder.tvLocationPark = (TextView) convertView.findViewById(R.id.tvLocationPark);
				holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
				holder.tvParkCost = (TextView) convertView.findViewById(R.id.tvParkCost);
				holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
				holder.btnNavigation = (Button) convertView.findViewById(R.id.btnNavigation);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvLocationPark.setText(bean.getName());
			holder.tvAddress.setText(bean.getAddress());
			holder.tvParkCost.setText(bean.getPrice() + "/" + "小时");
			holder.tvDistance.setText("空车位" + bean.getCarbarnLast() + "个");

			holder.btnNavigation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			return convertView;
		}

	}

	class ViewHolder {
		private TextView tvLocationPark, tvAddress, tvParkCost, tvDistance;
		private Button btnNavigation;
	}

}
