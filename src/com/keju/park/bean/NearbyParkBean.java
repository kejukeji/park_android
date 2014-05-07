package com.keju.park.bean;

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
	private int carbarnLast;// 剩余的车位
	private int carbarnTotal;// 总车位
	private int id;// 车库id
	private String name;
	private int price;// 价格

	private ArrayList<LocationBean> locationList;

	public NearbyParkBean(JSONObject obj) throws JSONException, JsonParseException, JsonMappingException, IOException {
		if (obj.has("address")) {
			this.address = obj.getString("address");
		}

		if (obj.has("carbarnLast")) {
			this.carbarnLast = obj.getInt("carbarnLast");
		}

		if (obj.has("carbarnTotal")) {
			this.carbarnTotal = obj.getInt("carbarnTotal");
		}

		if (obj.has("id")) {
			this.id = obj.getInt("id");
		}

		if (obj.has("price")) {
			this.price = obj.getInt("price");
		}

		if (obj.has("name")) {
			this.name = obj.getString("name");
		}

		if (obj.has("cartEntrances") && !TextUtils.isEmpty(obj.getString("cartEntrances"))
				&& !obj.getString("cartEntrances").equals("null")) {
			ObjectMapper mapper = new ObjectMapper();
			this.locationList = mapper.readValue(obj.getString("cartEntrances"), mapper.getTypeFactory()
					.constructParametricType(ArrayList.class, LocationBean.class));
			if (locationList == null) {
				locationList = new ArrayList<LocationBean>();
			}
		} else {
			this.locationList = new ArrayList<LocationBean>();
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
	public static List<NearbyParkBean> constractList(JSONArray array) throws JsonParseException, JsonMappingException,
			JSONException, IOException {
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

	public int getCarbarnLast() {
		return carbarnLast;
	}

	public void setCarbarnLast(int carbarnLast) {
		this.carbarnLast = carbarnLast;
	}

	public int getCarbarnTotal() {
		return carbarnTotal;
	}

	public void setCarbarnTotal(int carbarnTotal) {
		this.carbarnTotal = carbarnTotal;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ArrayList<LocationBean> getLocationList() {
		return locationList;
	}

	public void setLocationList(ArrayList<LocationBean> locationList) {
		this.locationList = locationList;
	}
}
