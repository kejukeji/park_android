package com.keju.park.ui.searchparking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.keju.park.ui.base.BaseActivity;
import com.keju.park.util.JsonParser;
import com.umeng.analytics.MobclickAgent;

/**
 * 语音搜索界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-2 下午12:46:56
 */
public class VoiceSearchActivity extends BaseActivity implements OnClickListener {
	private final  String mPageName = "VoiceSearchActivity";
	private Button btnVoiceSearch;
	private TextView tvSpeak, tvPosition;
	private RelativeLayout rlErroeOrRight;
	private Button btnError, btnRight;
	// 识别对象
	private SpeechRecognizer iatRecognizer;
	// 识别窗口
	private RecognizerDialog iatDialog;
	// 缓存，保存当前的引擎参数到下一次启动应用程序使用.
	private SharedPreferences mSharedPreferences;
	// 合成对象.
	private SpeechSynthesizer mSpeechSynthesizer;
	private String voiceStr;

	private CommonApplication app;

	// private int count =0; //记录用户点击的语音按钮的次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_search);
		app = (CommonApplication) getApplication();
		initBar();

		findView();
		fillData();

	}

	/**
	 * 控件初始化
	 */
	private void findView() {
		tvLeft.setOnClickListener(this);
		tvTitle.setText("语音搜索");

		btnVoiceSearch = (Button) findViewById(R.id.btnVoiceSearch);
		btnVoiceSearch.setOnClickListener(this);

		tvSpeak = (TextView) findViewById(R.id.tvSpeak);
		tvPosition = (TextView) findViewById(R.id.tvPosition);
		rlErroeOrRight = (RelativeLayout) findViewById(R.id.rlErroeOrRight);

		btnError = (Button) findViewById(R.id.btnError);
		btnError.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight1);
		btnRight.setOnClickListener(this);

	}

	/**
	 * 数据初始化
	 */
	private void fillData() {
		// 用户登录
		SpeechUser.getUser().login(VoiceSearchActivity.this, null, null, "appid=" + getString(R.string.app_id), listener);
		// 初始化缓存对象.
		mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		// 创建听写对象,如果只使用无UI听写功能,不需要创建RecognizerDialog
		iatRecognizer = SpeechRecognizer.createRecognizer(this);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(this);
		// 初始化合成对象.
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this);

	}

	/**
	 * 使用SpeechSynthesizer合成语音
	 * 
	 * @param
	 */
	private void synthetizeInSilence(String source) {
		if (null == mSpeechSynthesizer) {
			// 创建合成对象.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
		}
		// 设置合成发音人.
		String role = "xiaoyan";
		// 设置发音人
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, role);
		// 获取语速
		int speed = 20;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;
		case R.id.btnVoiceSearch:
			// count ++;
			rlErroeOrRight.setVisibility(View.VISIBLE);
			tvSpeak.setText("你是否说的是");
			showIatDialog();
			break;
		case R.id.btnError:
			tvSpeak.setText(R.string.speak);
			tvPosition.setText(R.string.position);
			rlErroeOrRight.setVisibility(View.GONE);
			break;
		case R.id.btnRight1:
			Bundle b = new Bundle();
			b.putString("voiceSearchStr", voiceStr.replace("。", ""));
			openActivity(HistorySearchParking.class, b);
			tvSpeak.setText(R.string.speak);
			tvPosition.setText(R.string.position);
			rlErroeOrRight.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 显示听写对话框.
	 * 
	 * @param
	 */
	public void showIatDialog() {
		if (null == iatDialog) {
			// 初始化听写Dialog
			iatDialog = new RecognizerDialog(this);
		}
		// 获取引擎参数
		String engine = "poi_engine";

		// 清空Grammar_ID，防止识别后进行听写时Grammar_ID的干扰
		iatDialog.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
		// 设置听写Dialog的引擎
		iatDialog.setParameter(SpeechConstant.DOMAIN, engine);
		// 设置采样率参数，支持8K和16K
		String rate = mSharedPreferences.getString(getString(R.string.preference_key_iat_rate),
				getString(R.string.preference_default_iat_rate));
		if (rate.equals("rate8k")) {
			iatDialog.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
		} else {
			iatDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
		}

		String province = "全选";
		String city = "全选";
		if (!TextUtils.isEmpty(app.getProvince())) {
			province = app.getProvince();
		}
		if (!TextUtils.isEmpty(app.getCity())) {
			city = app.getCity();
		}
		iatRecognizer.setParameter(SpeechConstant.SEARCH_AREA, province + city);

		// 清空结果显示框.
		tvPosition.setText(null);
		// 显示听写对话框
		iatDialog.setListener(recognizerDialogListener);
		iatDialog.show();
		showShortToast(getString(R.string.text_iat_begin));
	}

	/**
	 * 识别回调监听器
	 */
	RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			// if(count==2){
			// voiceYseOrOn =
			// JsonParser.parseIatResult(results.getResultString());
			// }else{
			// voiceStr = JsonParser.parseIatResult(results.getResultString());
			// tvPosition.append(" " + voiceStr + " ");
			// }
			//
			// if(count==2){
			// if(voiceYseOrOn.replace("。", "").equals("是")){
			// Bundle b = new Bundle();
			// b.putString("voiceSearchStr", voiceStr.replace("。", ""));
			// openActivity(HistorySearchParking.class, b);
			// tvPosition.append("");
			// count = 0;
			// tvSpeak.setText(R.string.speak);
			// tvPosition.setText(R.string.position);
			// rlErroeOrRight.setVisibility(View.GONE);
			// }else if (voiceYseOrOn.equals("不是")){
			// tvPosition.append("");
			// tvSpeak.setText(R.string.speak);
			// tvPosition.setText(R.string.position);
			// rlErroeOrRight.setVisibility(View.GONE);
			// } else{
			// count = 0;
			// tvPosition.append("");
			// tvSpeak.setText(R.string.speak);
			// tvPosition.setText(R.string.position);
			// rlErroeOrRight.setVisibility(View.GONE);
			// }
			voiceStr = JsonParser.parseIatResult(results.getResultString());
			tvPosition.append(" " + voiceStr.replace("。", "") + " ");
			// tvPosition.setSelection(tvPosition.length());
			if (isLast) {
				synthetizeInSilence("您是要去" + tvPosition.getText().toString().replace("。", "") + "吗");
			}
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {

		}

	};
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
