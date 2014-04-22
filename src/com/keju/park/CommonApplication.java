package com.keju.park;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 * 应用全局变量
 * @author Zhoujun
 * 说明：	1、可以缓存一些应用全局变量。比如数据库操作对象
 */
public class CommonApplication extends Application {
	/**
	 * Singleton pattern
	 */
	private static CommonApplication instance;
	
	public static CommonApplication getInstance() {
		return instance;
	}
	//账号是否被冻结
	private boolean isFreeze = false;
	@Override
	public void onCreate() {
		super.onCreate();
//		//捕获系统异常
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
		instance = this;
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

	public boolean isFreeze() {
		return isFreeze;
	}

	public void setFreeze(boolean isFreeze) {
		this.isFreeze = isFreeze;
	}
}
