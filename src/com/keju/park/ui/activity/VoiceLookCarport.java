package com.keju.park.ui.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.keju.park.R;
import com.keju.park.ui.activity.base.BaseActivity;
import com.keju.park.util.JsonParser;

/**
 * 语言找车位界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-4-29 下午9:48:43
 */
public class VoiceLookCarport extends BaseActivity implements OnClickListener {
	private EditText tvVoice;
	private Button btnStart;
	// 识别对象
	private SpeechRecognizer iatRecognizer;
	// 识别窗口
	private RecognizerDialog iatDialog;
	// 缓存，保存当前的引擎参数到下一次启动应用程序使用.
	private SharedPreferences mSharedPreferences;
	// Tip
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voicel_look_carport);
       
		initBar();
		findView();
		fillData();
	}

	@SuppressLint("ShowToast")
	private void findView() {
		//用户登录
		 SpeechUser.getUser().login(VoiceLookCarport.this, null, null
						, "appid=" + getString(R.string.app_id), listener);
		// 初始化缓存对象.
		mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		// 创建听写对象,如果只使用无UI听写功能,不需要创建RecognizerDialog
		iatRecognizer = SpeechRecognizer.createRecognizer(this);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(this);

		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);

		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(this);
		tvVoice = (EditText) findViewById(R.id.tvVoice);

	}

	private void fillData() {
		tvLeft.setOnClickListener(this);
		tvTitle.setText(R.string.look_parking);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:
			boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.preference_key_iat_show), true);
			if (isShowDialog) {
				// 显示语音听写Dialog.
				showIatDialog();
			} else {
				if (null == iatRecognizer) {
					iatRecognizer = SpeechRecognizer.createRecognizer(this);
				}
				if (iatRecognizer.isListening()) {
					iatRecognizer.stopListening();
					showTip("停止录音");
					((Button) findViewById(android.R.id.button1)).setEnabled(false);
				} else {
					// 不显示Dialog.
					showIatInvisble();
					((Button) findViewById(android.R.id.button1)).setText("停止");
				}
			}
			break;
		case R.id.tvLeft:
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
		String engine = "poi";

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

		// 当DOMAIN设置为POI搜索时,获取area参数

		String province = mSharedPreferences.getString(getString(R.string.preference_key_poi_province),
				getString(R.string.preference_default_poi_province));
		String city = mSharedPreferences.getString(getString(R.string.preference_key_poi_city),
				getString(R.string.preference_default_poi_city));

		iatDialog.setParameter(SpeechConstant.SEARCH_AREA, province + city);

		// 清空结果显示框.
		tvVoice.setText(null);
		// 显示听写对话框
		iatDialog.setListener(recognizerDialogListener);
		iatDialog.show();
		showTip(getString(R.string.text_iat_begin));
	}

	/**
	 * 不显示听写对话框
	 */
	public void showIatInvisble() {
		if (null == iatRecognizer) {
			iatRecognizer = SpeechRecognizer.createRecognizer(this);
		}
		// 获取引擎参数
		String engine = mSharedPreferences.getString(getString(R.string.preference_key_iat_engine),
				getString(R.string.preference_default_iat_engine));

		// 清空Grammar_ID，防止识别后进行听写时Grammar_ID的干扰
		iatRecognizer.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
		// 设置听写引擎
		iatRecognizer.setParameter(SpeechConstant.DOMAIN, engine);
		// 设置采样率参数，支持8K和16K
		String rate = mSharedPreferences.getString(getString(R.string.preference_key_iat_rate),
				getString(R.string.preference_default_iat_rate));
		if (rate.equals("rate8k")) {
			iatRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
		} else {
			iatRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
		}

		// 当DOMAIN设置为POI搜索时,获取area参数
		String province = mSharedPreferences.getString(getString(R.string.preference_key_poi_province),
				getString(R.string.preference_default_poi_province));
		String city = mSharedPreferences.getString(getString(R.string.preference_key_poi_city),
				getString(R.string.preference_default_poi_city));

		iatRecognizer.setParameter(SpeechConstant.SEARCH_AREA, province + city);

		// 清空结果显示框.
		tvVoice.setText(null);
		// 显示听写对话框
		iatRecognizer.startListening(recognizerListener);
		showTip(getString(R.string.text_iat_begin));
	}

	RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError err) {
			showTip(err.getPlainDescription(true));
			((Button) findViewById(android.R.id.button1)).setText(VoiceLookCarport.this.getString(R.string.text_iat));
			((Button) findViewById(android.R.id.button1)).setEnabled(true);
		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, String msg) {

		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			tvVoice.append(text);
			tvVoice.setSelection(tvVoice.length());
			if (isLast) {
				((Button) findViewById(android.R.id.button1)).setText(VoiceLookCarport.this
						.getString(R.string.text_iat));
				((Button) findViewById(android.R.id.button1)).setEnabled(true);
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);
		}

	};
	/**
	 * 识别回调监听器
	 */
	RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			tvVoice.append(text);
			tvVoice.setSelection(tvVoice.length());
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {

		}

	};

	private void showTip(String str) {
		if (!TextUtils.isEmpty(str)) {
			mToast.setText(str);
			mToast.show();
		}
	}
	
	
	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener listener = new SpeechListener()
	{

		@Override
		public void onData(byte[] arg0) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if(error != null) {
				Toast.makeText(VoiceLookCarport.this, getString(R.string.text_login_fail)
						, Toast.LENGTH_SHORT).show();
				
			}			
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}		
	};
}
