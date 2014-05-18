package com.parkmecn.android.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 附近停车场 实体类
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-7 下午9:39:28
 */

public class NearbyParkBean implements Serializable {

	private static final long serialVersionUID = -6117719353386596258L;

	private String address;
	private int last;// 剩余的车位
	private int total;// 总车位
	private int id;// 车库id
	private String name;
	private String type;// 类型
	private String dayPrice;// 白天价格
	private String nightPrice;// 晚上价格
	private String tel;// 电话

	private ArrayList<LocationBean> locationList;

	public NearbyParkBean(JSONObject obj) throws JSONException, JsonParseException, JsonMappingException, IOException {
		if (obj.has("address")) {
			this.address = obj.getString("address");
		}

		if (obj.has("cartEntrances") && !TextUtils.isEmpty(obj.getString("cartEntrances"))
				&& !obj.getString("cartEntrances").equals("null")) {
			ObjectMapper mapper = new ObjectMapper();
			this.locationList = mapper.readValue(obj.getString("cartEntrances"),
					mapper.getTypeFactory().constructParametricType(ArrayList.class, LocationBean.class));
			if (locationList == null) {
				locationList = new ArrayList<LocationBean>();
			}
		} else {
			this.locationList = new ArrayList<LocationBean>();
		}

		if (obj.has("dayPrice")) {
			this.dayPrice = obj.getString("dayPrice");
		}

		if (obj.has("id")) {
			this.id = obj.getInt("id");
		}
		if (obj.has("last")) {
			this.last = obj.getInt("last");
		}

		if (obj.has("name")) {
			this.name = obj.getString("name");
		}

		if (obj.has("nightPrice")) {
			this.nightPrice = obj.getString("nightPrice");
		}

		if (obj.has("tel")) {
			this.tel = obj.getString("tel");
		}

		if (obj.has("total")) {
			this.total = obj.getInt("total");
		}
		if (obj.has("type")) {
			this.type = obj.getString("type");
		}

	}

	/**
	 * 构建list
	 * 
	 * @param array
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static List<NearbyParkBean> constractList(JSONArray array) throws JsonParseException, JsonMappingException, JSONException,
			IOException {
		List<NearbyParkBean> list = new ArrayList<NearbyParkBean>();
		for (int i = 0; i < array.length(); i++) {
			NearbyParkBean bean = new NearbyParkBean(array.getJSONObject(i));
			list.add(bean);
		}
		return list;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getDayPrice() {
		return dayPrice;
	}

	public void setDayPrice(String dayPrice) {
		this.dayPrice = dayPrice;
	}

	public String getNightPrice() {
		return nightPrice;
	}

	public void setNightPrice(String nightPrice) {
		this.nightPrice = nightPrice;
	}

	public ArrayList<LocationBean> getLocationList() {
		return locationList;
	}

	public void setLocationList(ArrayList<LocationBean> locationList) {
		this.locationList = locationList;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
