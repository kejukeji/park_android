package com.keju.park.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 基类fragment
 * @author Zhoujun
 * @version 创建时间：2014-5-2 下午3:31:52
 */
public class BaseFragment extends Fragment {
	
	protected AlertDialog mAlertDialog;
	/**
	 * 显示下一个fragment
	 * 
	 * @param fragment
	 * @param id
	 */
	protected void showNext(Fragment fragment, int id) {
		showNext(fragment, id, null);
	}

	/**
	 * 显示下一个fragment
	 * 
	 * @param fragment
	 * @param id
	 * @param b
	 */
	protected void showNext(Fragment fragment, int id, Bundle b) {
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		fragmentTransaction.replace(id, fragment);
		if (b != null) {
			fragment.setArguments(b);
		}
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}

	/**
	 * 返回上一个界面
	 */
	protected void back() {
		getActivity().getSupportFragmentManager().popBackStack();
	}
	/**
	 * 结束activity；
	 */
	protected void finish(){
		getActivity().finish();
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
		Intent intent = new Intent(getActivity(), pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 打开activity
	 * 
	 * @param pAction
	 *            activity动作
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 打开activity
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
	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	protected void showShortToastInCenter(int pResId) {
		showShortToastInCenter(getString(pResId));
	}
	protected void showShortToast(String pMsg) {
		if(getActivity() == null){
			return;
		}
		Toast.makeText(getActivity(), pMsg, Toast.LENGTH_SHORT).show();
	}

	protected void showShortToastInCenter(String pMsg) {
		if(getActivity() == null){
			return;
		}
		Toast toast = Toast.makeText(getActivity(), pMsg,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	/**
	 * 隐藏界面
	 */
	protected void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try {
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			//这里可能会报空指针错误
		}
	}
	/**
	 * 显示提醒dialog
	 * 
	 * @param TitleID
	 * @param Message
	 * @return
	 */
	protected AlertDialog showAlertDialog(String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(TitleID).setMessage(Message).show();
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
		mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(pTitle).setMessage(pMessage)
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
		mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(pTitle).setMessage(pMessage)
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
		mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}
}
