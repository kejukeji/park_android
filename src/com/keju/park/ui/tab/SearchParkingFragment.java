package com.keju.park.ui.tab;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.db.DataBaseAdapter;
import com.keju.park.ui.base.BaseFragment;
import com.keju.park.ui.searchparking.HistorySearchParking;
import com.keju.park.ui.searchparking.ParkingListActivity;
import com.keju.park.ui.searchparking.VoiceSearchActivity;
import com.keju.park.util.NetUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 找车位界面(第一版：放在首页里)
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-1 下午10:59:30
 */
public class SearchParkingFragment extends BaseFragment implements
		OnClickListener{
	private TextView  tvTitle;
	private Button btnNearby, btnVoice;
	private TextView tvSearch;
	private CommonApplication app;
	private LinearLayout linearLayout;
	// 定位；
	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	private MKSearch mMKSearch = null;

	private DataBaseAdapter daAdapter;
	//语音合成；
	//合成对象.
	private SpeechSynthesizer mSpeechSynthesizer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_search_parking, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		UmengUpdateAgent.update(getActivity());
		//禁止默认的页面统计方式,这样将不会再自动统计Activity
		MobclickAgent.openActivityDurationTrack(false);
		app = (CommonApplication) getActivity().getApplication();
		daAdapter = app.getDbAdapter();
		findView();
		fillData();
	}
	/**
	 * 初始化控件
	 */
	private void findView() {
	
		app.initBMapInfo();

		tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
		tvTitle.setText("主页");

		btnNearby = (Button) getActivity().findViewById(R.id.btnLookNearby);
		btnNearby.setOnClickListener(this);
		btnVoice = (Button) getActivity().findViewById(R.id.btnVoice);
		btnVoice.setOnClickListener(this);

		tvSearch = (TextView) getActivity().findViewById(R.id.tvSearch);
		tvSearch.setOnClickListener(this);
		linearLayout = (LinearLayout) getActivity().findViewById(R.id.vo_Search);
		linearLayout.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void fillData() {
		mMKSearch = new MKSearch();
		mMKSearch.init(app.mBMapManager,
				new MySearchListener());
		initLocation();
		
		// 用户登录
		SpeechUser.getUser().login(getActivity(), null, null, "appid=" + getString(R.string.app_id),
						loginListener);
		// 初始化合成对象.
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(getActivity());
		// 获取合成文本.
		String source = "您好、我是您的停车小秘、";
		if(app.getUserAddress() != null){
			source = source + "您现在的位置是、" + app.getUserAddress().replace("-", "")+  "请告诉我您要去哪里";
		}else{
			source = source + "对不起，目前无法获取您当前的位置";
		}
		synthetizeInSilence(source);
	}
	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener loginListener = new SpeechListener() {

		@Override
		public void onData(byte[] arg0) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error != null) {
				showShortToast(R.string.text_login_fail);
			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}
	};
	/**
	 * 使用SpeechSynthesizer合成语音
	 * 
	 * @param
	 */
	private void synthetizeInSilence(String source) {
		if (null == mSpeechSynthesizer) {
			// 创建合成对象.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(getActivity());
		}
		// 设置合成发音人.
		String role = "xiaoyan";
		// 设置发音人
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, role);
		// 获取语速
		int speed = 50;
		// 设置语速
		mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "" + speed);
		// 获取音量.
		int volume = 100;
		// 设置音量
		mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "" + volume);
		// 获取语调
		int pitch = 49;
		// 设置语调
		mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "" + pitch);
		
		mSpeechSynthesizer.startSpeaking(source, synthesizerListener);
	}

	/**
	 * 语音合成播放；
	 */
	private SynthesizerListener synthesizerListener = new SynthesizerListener() {
		
		@Override
		public void onSpeakResumed() {
			
		}
		
		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {
			
		}
		
		@Override
		public void onSpeakPaused() {
			
		}
		
		@Override
		public void onSpeakBegin() {
			
		}
		
		@Override
		public void onCompleted(SpeechError arg0) {
			
		}
		
		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
			
		}
	};
	/**
	 * 初始化定位
	 */
	private void initLocation() {
		if (!NetUtil.checkNet(getActivity())) {
			showShortToast(R.string.NoSignalException);
			return;
		}
		mLocationClient = new LocationClient(getActivity());
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
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;
		case R.id.btnLookNearby:
			Bundle b = new Bundle();
			b.putDouble("Longitude", app.getLastLocation().getLongitude());
			b.putDouble("latitude", app.getLastLocation().getLatitude());
			openActivity(ParkingListActivity.class, b);
			break;

		case R.id.btnVoice:
			openActivity(VoiceSearchActivity.class);
//			openActivity(VoiceDialogueActivity.class);
			break;
		case R.id.tvSearch:
			// daAdapter.clearTableData("search_history");//清除表
			openActivity(HistorySearchParking.class);
			break;
		case R.id.vo_Search:
			openActivity(VoiceSearchActivity.class);
			break;
		default:
			break;
		}
	}




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
			
			mLocationClient.stop();
			mMKSearch.reverseGeocode(new GeoPoint(
					(int) (location.getLatitude() * 1e6), (int) (location
							.getLongitude() * 1e6)));
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
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			// 返回驾乘路线搜索结果
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// 返回poi搜索结果
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
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
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
			// 在此处理短串请求返回结果.
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}
	}
	@Override
	public void onResume() {
		super.onResume();
		 MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
	}

}
