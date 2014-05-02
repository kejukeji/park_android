package com.keju.park.ui.activity.lookparking;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import com.keju.park.R;
import com.keju.park.ui.activity.base.BaseActivity;

/**
 * 历史搜索界面
 * 
 * @author zhouyong
 * @data 创建时间：2014-5-2 下午2:56:35
 */
public class HistorySearchParking extends BaseActivity implements OnClickListener {
	private AutoCompleteTextView edSearch;
	private ListView lvSearchHistory;
	private SearchHistoryAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_search_parking_list);
		initBar();

		findView();
		fillData();
	}

	/**
	 * 初始化控件
	 * 
	 */
	private void findView() {

		tvLeft.setOnClickListener(this);
		tvTitle.setText(R.string.look_parking);
		edSearch = (AutoCompleteTextView) findViewById(R.id.edSearch);

		lvSearchHistory = (ListView) findViewById(R.id.lvSearchHistory);
		adapter = new SearchHistoryAdapter();
		lvSearchHistory.setAdapter(adapter);
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
		case R.id.edSearch:

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
