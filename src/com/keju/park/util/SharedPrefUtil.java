package com.keju.park.util;

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

}
