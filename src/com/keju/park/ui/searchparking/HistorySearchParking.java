package com.keju.park.ui.searchparking;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.keju.park.CommonApplication;
import com.keju.park.R;
import com.keju.park.ui.base.BaseActivity;

/**
 * 历史搜索界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-2 下午2:56:35
 */
public class HistorySearchParking extends BaseActivity implements OnClickListener {
	private EditText etSearch;
	private ListView lvSearch;
	private SearchHistoryAdapter adapter;

	private CommonApplication app;
	private MKSearch mMKSearch;
	
	private String voiceSearchStr;//用户语音
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (CommonApplication) getApplication();
		voiceSearchStr = getIntent().getStringExtra("voiceSearchStr");
		setContentView(R.layout.history_search_parking_list);
		initBar();
		findView();
		fillData();
		app.addActivity(this);
	}

	/**
	 * 初始化控件
	 * 
	 */
	private void findView() {

		tvLeft.setOnClickListener(this);
		tvTitle.setText(R.string.search_parking);
		etSearch = (EditText) findViewById(R.id.etSearch);
		etSearch.setText(voiceSearchStr);
		etSearch.addTextChangedListener(textWatcher);
		lvSearch = (ListView) findViewById(R.id.lvSearch);
		adapter = new SearchHistoryAdapter();
		lvSearch.setAdapter(adapter);
		
		mMKSearch = new MKSearch();
		mMKSearch.init(((CommonApplication)getApplication()).mBMapManager, new MySearchListener());//注意，MKSearchListener只支持一个，以最后一次设置为准
	    if(!TextUtils.isEmpty(voiceSearchStr)){
	    	mMKSearch.suggestionSearch(voiceSearchStr, app.getCity());
	    }
		
	
	}
	
	/**
	 * 输入框监听；
	 */
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(TextUtils.isEmpty(s.toString())){
				ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(HistorySearchParking.this, android.R.layout.simple_list_item_1,new ArrayList<String>());
	            lvSearch.setAdapter(suggestionString);
				return;
			}
		   
			mMKSearch.suggestionSearch(s.toString(), app.getCity());
		}
	};
	/**
	 * 搜索监听类
	 * @author Zhoujun
	 *
	 */
	public class MySearchListener implements MKSearchListener {  
        @Override  
        public void onGetAddrResult(MKAddrInfo result, int iError) {  
             
        }  
        @Override  
        public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {  
                //返回驾乘路线搜索结果  
        }  
        @Override  
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {  
                //返回poi搜索结果  
        }  
        @Override  
        public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {  
                //返回公交搜索结果  
        }  
        @Override  
        public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {  
                //返回步行路线搜索结果  
        }  
        @Override      
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {  
                //返回公交车详情信息搜索结果  
        }  
        @Override  
        public void onGetSuggestionResult(MKSuggestionResult result, int iError) {  
                //返回联想词信息搜索结果  
        	 if (iError!= 0 || result == null) {
                 Toast.makeText(HistorySearchParking.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show(); 
                 return;
             }
             int nSize = result.getSuggestionNum();
             String[] mStrSuggestions = new String[nSize];
             for (int i = 0; i <nSize; i++){
                mStrSuggestions[i] = result.getSuggestion(i).city + result.getSuggestion(i).key;
             }
             ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(HistorySearchParking.this, android.R.layout.simple_list_item_1,mStrSuggestions);
             lvSearch.setAdapter(suggestionString);
        }

         @Override 
         public void onGetShareUrlResult(MKShareUrlResult result , int type, int error) {
               //在此处理短串请求返回结果. 
        }
		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			
		} 
}
	/**
	 * 初始化数据
	 */
	private void fillData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;
		case R.id.etSearch:

			break;
		default:
			break;
		}
	}

	/**
	 * 搜索历史适配器
	 * 
	 */
	private class SearchHistoryAdapter extends BaseAdapter implements Filterable {
		private AutoMailFilter mFilter;// 自定义过滤器

		@Override
		public int getCount() {

			return 0;
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return null;
		}

		@Override
		public Filter getFilter() {
			if (mFilter == null) {
				mFilter = new AutoMailFilter();
			}
			return mFilter;
		}
	}

	/**
	 * 自定义过滤器
	 * 
	 */
	private class AutoMailFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			// 没有输入信息
			if (prefix == null || prefix.length() == 0) {
//				synchronized (mLock) {
//					ArrayList<String> list = new ArrayList<String>(mOriginalValues);
//					results.values = list;
//					results.count = list.size();
//					return results;
//				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
//
//				final int count = mOriginalValues.size();
//
//				final ArrayList<String> newValues = new ArrayList<String>(count);
//				for (int i = 0; i < count; i++) {
//					final String value = mOriginalValues.get(i);
//					// final String valueText = value.toLowerCase();
//					if (value.startsWith(prefixString)) { // 源码 ,匹配开头
//						newValues.add(i + "");// 存放的是他在原数组的id值
//					}
//					if (maxMatch > 0) {// 有数量限制
//						if (newValues.size() > maxMatch - 1) {// 不要太多
//							break;
//						}
//					}
//				}
//				results.values = newValues;
//				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
//			// 刷新信息
//			mObjects = (List<String>) results.values;
//			if (results.count > 0) {
//				notifyDataSetChanged();
//			} else {
//				notifyDataSetInvalidated();
//			}
		}

	}
	
	
	
	
	//静态数据
	public List<String> getData (){
		List<String> data = new ArrayList<String>();
		data.add("人民广场");
		data.add("中山公园");
		data.add("上海体育馆");
		data.add("上海火车站");
		data.add("延安西路");
		data.add("上海南站");
		data.add("人民公园");
		return data;
	}
	
}
