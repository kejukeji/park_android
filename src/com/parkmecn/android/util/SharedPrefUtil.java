package com.parkmecn.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * SharedPreferences工具类
 * 
 * @author Zhoujun 说明：SharedPreferences的操作工具类，需要缓存到SharedPreferences中的数据在此设置。
 */
public class SharedPrefUtil {

	public static final String UID = "uid";// 用户id
	private static final String USER_GUIDER = "help1";//这个如果新版本换引导页，把数字改成对应的版本号

	/**
	 * 判断用户是否登录
	 */
	public static boolean isLogin(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return (sp.getInt(UID, 0) > 0);
	}

	/**
	 * 保存uid
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setUid(Context context, int uid) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putInt(UID, uid);
		e.commit();
	}

	public static int getUid(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(UID, 0);
	}
	//设置引导页是否看过；
	public static void setGuiderRead(Context context, boolean isRead) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(USER_GUIDER, isRead);
		e.commit();
	}
	
	public static boolean isReadGuider(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(USER_GUIDER,false);
	}

}
