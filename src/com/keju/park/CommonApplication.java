package com.keju.park;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
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
import com.keju.park.db.DataBaseAdapter;
/**
 * 应用全局变量
 * @author Zhoujun
 * 说明：	1、可以缓存一些应用全局变量。比如数据库操作对象
 */
public class CommonApplication extends Application {
	/**
	 * 定位的城市；
	 */
	private String province;
	private String city = "";
	
	private String userAddress;//用户当前位置
	/**
	 * Singleton pattern
	 */
	private static CommonApplication instance;
	public BMapManager mBMapManager = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public LocationClient mLocationClient = null;
	private BDLocation lastLocation;// 位置
	LocationData locData = null;
	
	private MKSearch mSearch; // 搜索定位
	private MKSearchListener mSearchListener;
	
	public boolean mIsEngineInitSuccess = false;
	
	/**
	 * 数据库操作类
	 * 
	 * @return
	 */
	private DataBaseAdapter dataBaseAdapter;

	
	public static CommonApplication getInstance() {
		return instance;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		initEngineManager(this);
		initBMapInfo();
		dataBaseAdapter = new DataBaseAdapter(this);
		dataBaseAdapter.open();
		
//		//捕获系统异常
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
		instance = this;
	}
	
	
	/**
	 * 初始化地图信息
	 */
	public void initBMapInfo() {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(this);
		}
		mLocationClient = new LocationClient(getApplicationContext());
		
		locData = new LocationData();
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setPriority(LocationClientOption.NetWorkFirst);// 网络优先
		option.setOpenGps(false);
		option.setScanSpan(500);// 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		option.disableCache(true);
		option.setCoorType("bd09ll"); // 设置坐标类型
		mLocationClient.setLocOption(option);

		mSearch = new MKSearch();
		mSearchListener = new MySearchListener();
		mSearch.init(mBMapManager, mSearchListener);

		mLocationClient.start();
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(CommonApplication.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_SHORT).show();
        }
	}
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(CommonApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_SHORT).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(CommonApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_SHORT).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(CommonApplication.getInstance().getApplicationContext(), 
                        "请在 CommonApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_SHORT).show();
            }
            else{
            	Toast.makeText(CommonApplication.getInstance().getApplicationContext(), 
                        "key认证成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			lastLocation = location;
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			mSearch.reverseGeocode(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
			mLocationClient.stop();

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}

		}
	}
	
	class MySearchListener implements MKSearchListener {
		/**
		 * 根据经纬度搜索地址信息结果
		 * 
		 * @param result搜索结果
		 * @param iError 错误号（0表示正确返回）
		 * 
		 */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int error) {
			MKGeocoderAddressComponent kk = result.addressComponents;
			userAddress = kk.district + kk.street + kk.streetNumber;
			setProvince(kk.province);
			setCity(kk.city);
			if(userAddress.equals("")){
				setUserAddress(null);
			}else{
				setUserAddress(userAddress);
			}

		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

		}

	}
	/**
	 * 获得数据库操作对象
	 * 
	 * @return
	 */
	public DataBaseAdapter getDbAdapter() {
		return this.dataBaseAdapter;
	}
	
	public BDLocation getLastLocation() {
		return lastLocation;
	}
	

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}



	/**
	 * 缓存activity对象索引
	 */
	public List<Activity> activities = new ArrayList<Activity>();;
	public List<Activity> getActivities(){
		return activities;
	}
	public void addActivity(Activity mActivity) {
		activities.add(mActivity);
	}
	public boolean ismIsEngineInitSuccess() {
		return mIsEngineInitSuccess;
	}
	public void setmIsEngineInitSuccess(boolean mIsEngineInitSuccess) {
		this.mIsEngineInitSuccess = mIsEngineInitSuccess;
	}
	
}
