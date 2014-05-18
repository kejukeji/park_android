package com.keju.park.ui.searchparking;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.bean.FuzzyQueryBean;
import com.keju.park.ui.base.BaseActivity;
import com.keju.park.util.JsonParser;
import com.umeng.analytics.MobclickAgent;

/**
 * 语音对话界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-15 下午10:25:56
 */
public class VoiceDialogueActivity extends BaseActivity implements OnClickListener, SynthesizerListener {
	private final  String mPageName = "VoiceDialogueActivity";
	private ListView vdList;
	private Button btn;
	// Tip
	private Toast mToast;
	private StringBuffer sb = new StringBuffer();
	// 合成对象.
	private SpeechSynthesizer mSpeechSynthesizer;
	// 识别对象
	private SpeechRecognizer iatRecognizer;
	
	private CommonApplication app;
	// 地图搜索；
	private MKSearch mMKSearch;

	private static final int STEP_SPEAK_ADDRESS = 1;// 第一步输出地点；
	private static final int STEP_SELECT_ADDRESS = 2;// 第一步输出地点；
	private static final int STEP_SELECT_PARKING = 3;// 第一步输出地点；
	private int step = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_dialogue_list);
		app = (CommonApplication) getApplication();
		initBar();
		initBMapSearch();
		findView();
		fillData();
	}

	/**
	 * 初始化地图搜索；
	 */
	private void initBMapSearch() {
		mMKSearch = new MKSearch();
		mMKSearch.init(((CommonApplication) getApplication()).mBMapManager, new MySearchListener());
	}

	private void findView() {
		vdList = (ListView) findViewById(R.id.vdList);
		tvLeft.setOnClickListener(this);
		tvTitle.setText("找停车您说");
		btn = (Button) findViewById(R.id.btn);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
	}

	private void showTip(String str) {
		if (!TextUtils.isEmpty(str)) {
			mToast.setText(str);
			mToast.show();
		}
	}

	private void fillData() {
		// 用户登录
		SpeechUser.getUser().login(VoiceDialogueActivity.this, null, null, "appid=" + getString(R.string.app_id),
				listener);

		// 创建听写对象,如果只使用无UI听写功能,不需要创建RecognizerDialog
		iatRecognizer = SpeechRecognizer.createRecognizer(this);
		// 初始化合成对象.
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
		synthetizeInSilence();

	}

	/**
	 * 使用SpeechSynthesizer合成语音
	 * 
	 * @param
	 */
	private void synthetizeInSilence() {
		if (null == mSpeechSynthesizer) {
			// 创建合成对象.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
		}
		// 设置合成发音人.
		String role = "xiaoyan";

		// 设置发音人
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, role);
		// 获取语速
		int speed = 15;
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
		// 获取合成文本.
		String source = "欢迎使用停车宝，停车宝小颖为您服务!请说出您要去的地方";
		startSpeaking(source);
	}

	/**
	 * 语音合成
	 * 
	 * @param source
	 */
	private void startSpeaking(String source) {
		// 进行语音合成.
		mSpeechSynthesizer.startSpeaking(source, this);
	}

	@Override
	protected void onStop() {
		mToast.cancel();
		if (null != iatRecognizer) {
			iatRecognizer.cancel();
		}
		super.onStop();
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
	 * SynthesizerListener的"开始播放"回调接口.
	 * 
	 * @param
	 */
	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {

	}

	@Override
	public void onCompleted(SpeechError arg0) {
		if(step == 0){
			step = STEP_SPEAK_ADDRESS;
			initVoice();
//			iatRecognizer.startListening(recognizerListener);
		}
	}

	@Override
	public void onSpeakBegin() {

	}

	@Override
	public void onSpeakPaused() {

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {

	}

	@Override
	public void onSpeakResumed() {

	}

	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener listener = new SpeechListener() {

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
	 * 初始化声音；
	 */
	public void initVoice() {
		
		// 获取引擎参数
		String engine = getString(R.string.preference_key_poi_engine);

		// 清空Grammar_ID，防止识别后进行听写时Grammar_ID的干扰
		iatRecognizer.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
		// 设置听写引擎
		iatRecognizer.setParameter(SpeechConstant.DOMAIN, engine);
		// 设置采样率参数，支持8K和16K
		iatRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
		String province = "全选";
		String city = "全选";
		if (!TextUtils.isEmpty(app.getProvince())) {
			province = app.getProvince();
		}
		if (!TextUtils.isEmpty(app.getCity())) {
			city = app.getCity();
		}
		iatRecognizer.setParameter(SpeechConstant.SEARCH_AREA, province + city);
		
		iatRecognizer.startListening(recognizerListener);
		showShortToast(R.string.text_iat_begin);
	}

	/**
	 * 语音识别监听
	 */
	RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			sb.setLength(0);
		}

		@Override
		public void onError(SpeechError err) {
			startSpeaking("对不起，您说的不清楚，请再说一遍");
		}

		@Override
		public void onEndOfSpeech() {
			showTip(sb.toString());
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, String msg) {

		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			sb.append(text);
		}

		@Override
		public void onVolumeChanged(int volume) {

		}

	};


	/**
	 * 搜索监听类
	 * 
	 * @author Zhoujun
	 * 
	 */
	public class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {

			if (result.type == MKAddrInfo.MK_GEOCODE) {
				// 地理编码：通过地址检索坐标点
				double Longitude = result.geoPt.getLongitudeE6() / 1e6;
				double Latitude = result.geoPt.getLatitudeE6() / 1e6;

			}
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
			if (iError != 0 || result == null) {
				Toast.makeText(VoiceDialogueActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
				return;
			}

			int nSize = result.getSuggestionNum();
			String[] mStrSuggestions = new String[nSize];

			for (int i = 0; i < nSize; i++) {
				mStrSuggestions[i] = result.getSuggestion(i).city + result.getSuggestion(i).key;
				FuzzyQueryBean bean = new FuzzyQueryBean();
				bean.setCity(result.getSuggestion(i).city.toString());
				bean.setAddress(result.getSuggestion(i).key.toString());
			}

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
