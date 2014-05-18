/**
 * 
 */
package com.keju.park.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.keju.park.Constants;
import com.keju.park.SystemException;
import com.keju.park.bean.NearbyParkBean;
import com.keju.park.bean.ResponseBean;
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

	public static final String BASE_URL = "http://121.199.251.166//park/v1/carbarn/";// 生产服务器
	HttpClient httpClient = new HttpClient();

	/**
	 * 获得停车数据
	 * 
	 * @param latitude
	 * @param longtitude
	 *            pageIndex
	 * @param pageIndex
	 * @return
	 * @throws SystemException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public ResponseBean<NearbyParkBean> getParkList(double latitude, double longtitude, int pageIndex) throws SystemException,
			JsonParseException, JsonMappingException, IOException {
		List<PostParameter> p = new ArrayList<PostParameter>();
		if (latitude != 0.0) {
			p.add(new PostParameter("latitude", latitude));
		}
		if (longtitude != 0.0) {
			p.add(new PostParameter("longitude", longtitude));
		}
		p.add(new PostParameter("page_show", pageIndex));
		p.add(new PostParameter("sortBy", "sortBy"));
		ResponseBean<NearbyParkBean> response = null;
		try {
			JSONObject obj = null;
			obj = httpClient.get(BASE_URL + "latitude-longitude", p.toArray(new PostParameter[p.size()])).asJSONObject();
			int status = obj.getInt("status");
			if (status == Constants.REQUEST_SUCCESS) {
				response = new ResponseBean<NearbyParkBean>(obj);
				List<NearbyParkBean> list = null;
				if (!TextUtils.isEmpty(obj.getString("data"))) {
					JSONArray array =  obj.getJSONArray("data");
					list = NearbyParkBean.constractList(array);// 停车列表
				} else {
					list = new ArrayList<NearbyParkBean>();
				}
				response.setObjList(list);
			} else {
				response = new ResponseBean<NearbyParkBean>(Constants.REQUEST_SUCCESS, obj.getString("message"));
			}
		} catch (SystemException e1) {
			e1.printStackTrace();
			response = new ResponseBean<NearbyParkBean>(Constants.REQUEST_FAILED, "服务器连接失败");
		} catch (JSONException e) {
			e.printStackTrace();
			response = new ResponseBean<NearbyParkBean>(Constants.REQUEST_FAILED, "json解析错误");
		}
		return response;
	}

	/**
	 * 获得停车详情数据
	 * 
	 * @param int
	 * 
	 */
	public JSONObject getParkDetailsTask(int id) throws SystemException {
		return httpClient.get(BASE_URL + "get/" + id).asJSONObject();
	}

}
