package com.keju.park;

import android.os.Environment;

/**
 * 常量类
 * 
 * @author Zhoujun 说明： 1、一些应用常量在此定义 2、常量包括：一些类型的定义，在其他程序中不能够出现1 2 3之类的值。
 */
public class Constants {

	/**
	 * 应用文件存放目录
	 */
	public static final String APP_DIR_NAME = "park";
	// 图片目录
	public static final String IMAGE_DIR = Environment.getExternalStorageDirectory() + "/" + APP_DIR_NAME + "/image/";
	// 日志目录
	public static final String LOG_DIR = Environment.getExternalStorageDirectory() + "/" + APP_DIR_NAME + "/log/";
	// 文件目录
	public static final String FILE_DIR = Environment.getExternalStorageDirectory() + "/" + APP_DIR_NAME + "/file/";
	// 分享的Url
	public static final String APP_DOWNLOAD_URL = "http://www.i-chuangye.com/download/app/ichuangye.apk";

	/**
	 * activity 定义的id,fragment间切换;
	 */
	public static final int FRAGMENT_HOME = 1;// 主页
	public static final int FRAGMENT_COLLECTION = 2;// 收藏界面
	public static final int FRAGMENT_VOICE = 3;// 语音界面
	public static final int FRAGMENT_STORE = 4;// 商店界面
	public static final int FRAGMENT_PERSONAL_CENTER = 5;// 个人中心
	/**
	 * 网络请求状态
	 */
	public static final int REQUEST_SUCCESS = 0;// 表示成功
	public static final int REQUEST_FAILED = 400;// 表示失败
	public static final String REQUEST_TOKEN_INVALID = "auth is no exist";
	public static final String CONNECT_SERVER_FAILED = "服务器连接失败";
	public static final String JSON_EXCEPTION = "json解析错误";
	/**
	 * intent extra
	 */
	public static final String EXTRA_DATA = "extra_data";
	/**
	 * 分页数据
	 */
	public static final int PAGE_SIZE = 10;
}
