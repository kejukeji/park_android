package com.keju.park.ui.activity;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import com.keju.park.util.NetUtil;

public class MainActivity extends FragmentActivity {
	private TextView tvTest;

	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	
	private MKSearch mMKSearch = null ;
	private ListView mSuggestionList ;
	private EditText etSearch;
	private String city = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initLocation();
	}

	private void init() {
		tvTest = (TextView) findViewById(R.id.tvTest);
		mSuggestionList = (ListView) findViewById(R.id.listView1);
		etSearch = (EditText) findViewById(R.id.etSearch);
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				mMKSearch.suggestionSearch(etSearch.getText().toString(), city);
			}
		});
		etSearch.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					Intent intent = new Intent(MainActivity.this, ShowRouteActivity.class);
					startActivity(intent);
				}
				return false;
			}
		});
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
		
		mMKSearch = new MKSearch();
		mMKSearch.init(((CommonApplication)getApplication()).mBMapManager, new MySearchListener());//注意，MKSearchListener只支持一个，以最后一次设置为准

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
	/**
	 * 搜索监听类
	 * @author Zhoujun
	 *
	 */
	public class MySearchListener implements MKSearchListener {  
        @Override  
        public void onGetAddrResult(MKAddrInfo result, int iError) {  
               //返回地址信息搜索结果  
        	MKGeocoderAddressComponent kk=result.addressComponents; 
        	city=kk.city;
        	Toast.makeText(MainActivity.this, city, Toast.LENGTH_SHORT).show();
        }  
        @Override  
        public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {  
                //返回驾乘路线搜索结果  
        }  
        @Override  
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {  
                //返回poi搜索结果  
        }  
        @Override  
        public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {  
                //返回公交搜索结果  
        }  
        @Override  
        public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {  
                //返回步行路线搜索结果  
        }  
        @Override      
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {  
                //返回公交车详情信息搜索结果  
        }  
        @Override  
        public void onGetSuggestionResult(MKSuggestionResult result, int iError) {  
                //返回联想词信息搜索结果  
        	 if (iError!= 0 || result == null) {
                 Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show(); 
                 return;
             }
             int nSize = result.getSuggestionNum();
             String[] mStrSuggestions = new String[nSize];
             for (int i = 0; i <nSize; i++){
                mStrSuggestions[i] = result.getSuggestion(i).city + result.getSuggestion(i).key;
             }
             ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,mStrSuggestions);
             mSuggestionList.setAdapter(suggestionString);
        }

         @Override 
         public void onGetShareUrlResult(MKShareUrlResult result , int type, int error) {
               //在此处理短串请求返回结果. 
        }
		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			
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
			mMKSearch.reverseGeocode(new GeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6)));
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}

		}
	}
}
