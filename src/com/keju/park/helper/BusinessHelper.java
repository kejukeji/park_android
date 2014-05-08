package com.keju.park.helper;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * 服务器请求
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-7 下午9:07:39
 */
public class BusinessHelper {
	public Context context;

	public static final String BASE_URL = "";

	public BusinessHelper(Context context) {
		this.context = context;
	}

	public void getHttpUrl(String url, final httpCallback callback) {
		RequestQueue mQueue = Volley.newRequestQueue(context);
		mQueue.add(new JsonObjectRequest(Method.GET, BASE_URL + url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jObject) {
				if (jObject != null && callback != null) {
					callback.returnJson(jObject);
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		}));
		mQueue.start();
	}

	public interface httpCallback {
		public void returnJson(JSONObject obj);
	}
}
