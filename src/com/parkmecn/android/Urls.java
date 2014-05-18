package com.parkmecn.android;

/**
 * url地址
 * @author Zhoujun
 * @version 创建时间：2014年5月16日 下午2:29:49
 */
public class Urls {
	public static final String BASE_URL = "http://park.kejukeji.com";//测试服务器
	public static final String URL_PARK_LIST = BASE_URL + "/ssbusy/carbarn/latitude-longitude?";
	
//    public static final String BASE_URL1 = "http://192.168.1.125/park/v1/carbarn/"; //生产服务器
  
	  public static final String BASE_URL1 = "http://192.168.1.125:8080/v1/carbarn/";
	public static final String URL_PARK_LIST1 = BASE_URL1 + "latitude-longitude?";//停车列表url
}

