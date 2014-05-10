package com.keju.park.bean;

import java.io.Serializable;

/**
 * 模糊查询 bean 
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-9  下午9:20:17
 */
public class FuzzyQueryBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String city;//城市的名字 或者是省份
	
	private String address;//详细地址
	
	private double Longitude;//经纬
	private double Latitude; //纬度
	
	private String cityFullName;// 城市地址全称

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public String getCityFullName() {
		return cityFullName;
	}

	public void setCityFullName(String cityFullName) {
		this.cityFullName = cityFullName;
	}
	
}
