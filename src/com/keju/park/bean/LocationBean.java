package com.keju.park.bean;

import java.io.Serializable;

/**
 * 停车场 所有进口的 经纬度实体类
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-7  下午9:45:48
 */
public class LocationBean implements Serializable {
	
	private static final long serialVersionUID = -6117719353386596258L;
	
	private int id ;
	private double latitude;
	private double longitude;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	

}
