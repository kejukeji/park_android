package com.keju.park.ui.searchparking;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.keju.park.bean.FuzzyQueryBean;
import com.keju.park.bean.LocationBean;
import com.keju.park.db.DataBaseAdapter;
import com.keju.park.ui.base.BaseActivity;

/**
 * 历史搜索界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-2 下午2:56:35
 */
public class HistorySearchParking extends BaseActivity implements OnClickListener {
	private EditText etSearch;
	private LinearLayout linearLayout;	//语音搜索相关组件
	private ListView lvSearch;
	private SearchHistoryAdapter adapter;

	private CommonApplication app;
	private MKSearch mMKSearch;

	private String voiceSearchStr;// 用户语音

	private List<FuzzyQueryBean> list;

	private DataBaseAdapter dba;

	private int clickItemPositn;// 点击的itmeid

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (CommonApplication) getApplication();
		dba = ((CommonApplication) getApplicationContext()).getDbAdapter();

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
		etSearch = (EditText) findViewById(R.id.tvSearch);
		if (!TextUtils.isEmpty(voiceSearchStr)){
			etSearch.setText(voiceSearchStr);
		}
		linearLayout = (LinearLayout) findViewById(R.id.vo_Search);
		linearLayout.setOnClickListener(this);
		etSearch.addTextChangedListener(textWatcher);
		lvSearch = (ListView) findViewById(R.id.lvSearch);
		lvSearch.setOnItemClickListener(listener);
		// adapter = new SearchHistoryAdapter();
		// lvSearch.setAdapter(adapter);
		list = new ArrayList<FuzzyQueryBean>();

		mMKSearch = new MKSearch();
		mMKSearch.init(((CommonApplication) getApplication()).mBMapManager, new MySearchListener());// 注意，MKSearchListener只支持一个，以最后一次设置为准
		if (!TextUtils.isEmpty(voiceSearchStr)) {
			mMKSearch.suggestionSearch(voiceSearchStr, app.getCity());
		} else {
			ArrayList<FuzzyQueryBean> fuzzyList = dba.queryHistoryRecode();
			list.clear();
			list = fuzzyList;
			adapter = new SearchHistoryAdapter(fuzzyList, true); // turn
			lvSearch.setAdapter(adapter);
		}

	}

	/**
	 * listView 点击事件
	 * 
	 * */
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			clickItemPositn = position;
			FuzzyQueryBean itemBean = list.get(position);
			getSearchAreaLongitudeAndlatitude(itemBean);

		}

	};
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
			list.clear();
			if (TextUtils.isEmpty(s.toString())) {
				ArrayList<FuzzyQueryBean> fuzzyList = dba.queryHistoryRecode();
				list = fuzzyList;
				adapter = new SearchHistoryAdapter(fuzzyList, true); // turn
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

			if (result.type == MKAddrInfo.MK_GEOCODE) {
				// 地理编码：通过地址检索坐标点
				double Longitude = result.geoPt.getLongitudeE6() / 1e6;
				double Latitude = result.geoPt.getLatitudeE6() / 1e6;
				FuzzyQueryBean fuzzyQueryBean = list.get(clickItemPositn);
				fuzzyQueryBean.setLongitude(Longitude);
				fuzzyQueryBean.setLatitude(Latitude);
				Bundle b = new Bundle();
				b.putDouble("Longitude", fuzzyQueryBean.getLongitude());
				b.putDouble("Latitude", fuzzyQueryBean.getLatitude());
                boolean isAlikeFullName = dba.isAlikeData(fuzzyQueryBean.getCity()+fuzzyQueryBean.getAddress());
				
                Log.i("isAlikeFullName", "isAlikeFullName= "+ isAlikeFullName);
				
                if(!isAlikeFullName){
					 dba.inserData(fuzzyQueryBean.getCity(), fuzzyQueryBean.getAddress(), fuzzyQueryBean.getCity()
								+ fuzzyQueryBean.getAddress(), Longitude + "", Latitude + "");
				 }
                
				openActivity(ParkingListActivity.class, b);

			}
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
				FuzzyQueryBean bean = new FuzzyQueryBean();
				bean.setCity(result.getSuggestion(i).city.toString());
				bean.setAddress(result.getSuggestion(i).key.toString());
				list.add(bean);
			}
			adapter = new SearchHistoryAdapter(list, false);

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

	/**
	 * 获取用户搜地区的经纬度
	 * 
	 * @param itemBean
	 */
	protected void getSearchAreaLongitudeAndlatitude(FuzzyQueryBean itemBean) {
		mMKSearch.geocode(itemBean.getAddress(), itemBean.getCity());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLeft:
			finish();
			break;
		case R.id.etSearch:

			break;
		case R.id.vo_Search:
			Intent intent = new Intent(this,VoiceSearchActivity.class);
			startActivity(intent);
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
		private List<FuzzyQueryBean> lists;
		private boolean isDbQuery;

		/**
		 * @param list
		 */
		public SearchHistoryAdapter(List<FuzzyQueryBean> list, boolean isDbQuery) {
			this.lists = list;
			this.isDbQuery = isDbQuery;

		}

		@Override
		public int getCount() {

			return lists.size();
		}

		@Override
		public Object getItem(int position) {

			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			final FuzzyQueryBean bean = lists.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.history_search_parking_item, null);
				holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
				holder.ivHisotoySerarch = (ImageView) convertView.findViewById(R.id.ivHisotoySerarch);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (isDbQuery) {
				holder.tvAddress.setText(bean.getCityFullName());
				holder.ivHisotoySerarch.setVisibility(View.VISIBLE);
			} else {
				holder.tvAddress.setText(bean.getCity() + bean.getAddress());
			}

			holder.ivHisotoySerarch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dba.delOneHistoryRecode(bean.getCityFullName());
					lists.remove(position);
					adapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}

	}

	class ViewHolder {
		private TextView tvAddress;
		private ImageView ivHisotoySerarch;
	}

}
