package com.keju.park.ui.searchparking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

	private String voiceSearchStr;// 用户语音

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
//		adapter = new SearchHistoryAdapter();
//		lvSearch.setAdapter(adapter);

		mMKSearch = new MKSearch();
		mMKSearch.init(((CommonApplication) getApplication()).mBMapManager, new MySearchListener());// 注意，MKSearchListener只支持一个，以最后一次设置为准
		if (!TextUtils.isEmpty(voiceSearchStr)) {
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
			if (TextUtils.isEmpty(s.toString())) {
//				ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(HistorySearchParking.this,
//						android.R.layout.simple_list_item_1, new ArrayList<String>());
//				
//				adapter = new SearchHistoryAdapter(new String[]);
				lvSearch.setAdapter(adapter);
				return;
			}

			mMKSearch.suggestionSearch(s.toString(), app.getCity());
		}
	};

	/**
	 * 搜索监听类
	 * 
	 * @author Zhoujun
	 * 
	 */
	public class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
			// 返回驾乘路线搜索结果
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// 返回poi搜索结果
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
			// 返回步行路线搜索结果
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			// 返回联想词信息搜索结果
			if (iError != 0 || result == null) {
				Toast.makeText(HistorySearchParking.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
				return;
			}
			
	
			int nSize = result.getSuggestionNum();
			String[] mStrSuggestions = new String[nSize];
			for (int i = 0; i < nSize; i++) {
				mStrSuggestions[i] = result.getSuggestion(i).city + result.getSuggestion(i).key;
			}
			adapter = new SearchHistoryAdapter(mStrSuggestions);
//			ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(HistorySearchParking.this,
//					android.R.layout.simple_list_item_1, mStrSuggestions);
			
			lvSearch.setAdapter(adapter);
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {
			// 在此处理短串请求返回结果.
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
	private class SearchHistoryAdapter extends BaseAdapter {
		String mStrSuggestions[] = null;

		/**
		 * @param mStrSuggestions
		 */
		public SearchHistoryAdapter(String[] mStrSuggestions) {
			this.mStrSuggestions  = mStrSuggestions ;
			
		}

		@Override
		public int getCount() {

			return mStrSuggestions.length;
		}

		@Override
		public Object getItem(int position) {

			return mStrSuggestions;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.history_search_parking_item, null);
				holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
            holder.tvAddress.setText(mStrSuggestions[position]);
			return convertView;
		}

	}

	class ViewHolder {
		private TextView  tvAddress;
		private ImageView  ivHisotoySerarch;
	}

}
