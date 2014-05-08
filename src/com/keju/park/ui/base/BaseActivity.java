package com.keju.park.ui.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.util.LogUtil;

/**O
 * 基础父类activity 说明：大部分的activity需要继承该类，提供一些常用的方法；
 * 
 * @author Zhoujun
 * @version 创建时间：2013-6-17 上午10:26:56
 */
public class BaseActivity extends Activity {
	//公用的头标题栏  控件
	protected TextView tvLeft,tvTitle;
    
	private static final String TAG = "BaseActivity";  
     
	protected AlertDialog mAlertDialog;
	@SuppressWarnings("rawtypes")
	protected AsyncTask mRunningTask;

	/******************************** 【Activity LifeCycle For Debug】 *******************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onCreate() invoked!!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		MobclickAgent.onError(this);
		((CommonApplication) getApplication()).addActivity(this);
	}

	protected void initBar () {
		
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		
	}
	@Override
	protected void onStart() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStart() invoked!!");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
//			MobclickAgent.onPause(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStop() invoked!!");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onDestroy() invoked!!");
		super.onDestroy();

		if (mRunningTask != null && mRunningTask.isCancelled() == false) {
			mRunningTask.cancel(false);
			mRunningTask = null;
		}
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
	}


	/******************************** 【Activity LifeCycle For Debug】 *******************************************/

	/**
	 * 显示toast（时间短）
	 * 
	 * @param pResId
	 */
	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	/**
	 * 显示toast（时间长）
	 * 
	 * @param pResId
	 */
	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示toast（时间短）
	 * 
	 * @param pMsg
	 */
	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 判断
	 * 
	 * @param pExtraKey
	 * @return
	 */
	protected boolean hasExtra(String pExtraKey) {
		if (getIntent() != null) {
			return getIntent().hasExtra(pExtraKey);
		}
		return false;
	}

	/**
	 * 跳转activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 跳转activity ，绑定数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 打开activity（打开某种action）
	 * 
	 * @param pAction
	 *            activity动作
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 打开activity（打开某种action）
	 * 
	 * @param pAction
	 *            activity动作
	 * @param pBundle
	 *            数据
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 显示提醒dialog
	 * 
	 * @param TitleID
	 * @param Message
	 * @return
	 */
	protected AlertDialog showAlertDialog(String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(TitleID).setMessage(Message).show();
		return mAlertDialog;
	}

	/**
	 * 显示提醒dialog
	 * 
	 * @param pTitelResID
	 * @param pMessage
	 * @param pOkClickListener
	 * @return
	 */
	protected AlertDialog showAlertDialog(int pTitelResID, String pMessage,
			DialogInterface.OnClickListener pOkClickListener) {
		String title = getResources().getString(pTitelResID);
		return showAlertDialog(title, pMessage, pOkClickListener, null, null);
	}

	/**
	 * 显示提醒dialog
	 * 
	 * @param pTitle
	 * @param pMessage
	 * @param pOkClickListener
	 * @param pCancelClickListener
	 * @param pDismissListener
	 * @return
	 */
	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			DialogInterface.OnClickListener pOkClickListener, DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	/**
	 * 显示提醒dialog
	 * 
	 * @param pTitle
	 * @param pMessage
	 * @param pOkClickListener
	 * @param pCancelClickListener
	 * @param pDismissListener
	 * @return
	 */
	protected AlertDialog showAlertDialog(int pTitle, int pMessage, DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener, DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	/**
	 * 显示提醒dialog
	 * 
	 * @param pTitle
	 * @param pMessage
	 * @param pPositiveButtonLabel
	 * @param pNegativeButtonLabel
	 * @param pOkClickListener
	 * @param pCancelClickListener
	 * @param pDismissListener
	 * @return
	 */
	protected AlertDialog showAlertDialog(String pTitle, String pMessage, String pPositiveButtonLabel,
			String pNegativeButtonLabel, DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener, DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	/**
	 * 显示进度dialog
	 * 
	 * @param pTitelResID
	 * @param pMessage
	 * @param pCancelClickListener
	 * @return
	 */
	protected ProgressDialog showProgressDialog(int pTitelResID, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = getResources().getString(pTitelResID);
		return showProgressDialog(title, pMessage, pCancelClickListener);
	}

	/**
	 * 显示进度dialog
	 * 
	 * @param pTitle
	 * @param pMessage
	 * @param pCancelClickListener
	 * @return
	 */
	protected ProgressDialog showProgressDialog(String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(this, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		return (ProgressDialog) mAlertDialog;
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param view
	 */
	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 显示键盘；
	 */
	protected void showKeyborad() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 关闭activity加入动画
	 */
//	public void finish() {
//		super.finish();
//		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//	}

	/**
	 * 关闭activity
	 */
	public void defaultFinish() {
		super.finish();
	}

	private ProgressDialog pd;

	/**
	 * 显示progressDialog
	 */
	protected void showPd(String message) {
		if (pd == null) {
			pd = new ProgressDialog(this);
		}
		pd.setMessage(message);
		pd.show();
	}

	/**
	 * 显示progressDialog
	 */
	protected void showPd(int msgId) {
		if (pd == null) {
			pd = new ProgressDialog(this);
		}
		pd.setMessage(getString(msgId));
		pd.show();
	}

	/**
	 * 关闭progressDialog
	 */
	protected void dismissPd() {
		if (pd != null) {
			pd.dismiss();
		}
	}

//	// 处理back键无动画效果问题
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//			this.finish(); // finish当前activity
//			overridePendingTransition(0, R.anim.roll_down);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
