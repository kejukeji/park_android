/**
 * 
 */
package com.keju.park.helper;

import org.json.JSONObject;

import com.keju.park.SystemException;
import com.keju.park.internet.HttpClient;
import com.keju.park.internet.PostParameter;



/**
 * 网络访问操作
 * 
 * @author zhouyong 说明： 1、一些网络操作方法 2、访问系统业务方法，转换成json数据对象，或者业务对象。
 * 
 * 
 */
public class BusinessHerlper {

	/**
	 * 网络访问路径
	 */
	private static final String BASE_URL1 = "http://api.maserati.eeelephant.com/api/";
	
	public static final String BASE_URL = "http://www.i-chuangye.com/";
	HttpClient httpClient = new HttpClient();
	
	/**
	 * 登录接口
	 * 
	 * @param loginName
	 * @param password
	 * @return
	 * @throws SystemException
	 */
	public JSONObject login(String loginName, String password)
			throws SystemException {
		return httpClient.get(
				BASE_URL + "user/login",
				new PostParameter[] {
						new PostParameter("name", loginName),
						new PostParameter("passwd", password) })
				.asJSONObject();
	}

	/**
	 * 获得首页图片
	 * @param type
	 * @return
	 * @throws SystemException
	 */
	public JSONObject getHomeImages(int type) throws SystemException{
		
		return httpClient.get(
				BASE_URL1 + "enter/search.json",
				new PostParameter[] {
						new PostParameter("type", type) })
				.asJSONObject();
	}
	

}
