package com.parkmecn.android.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parkmecn.android.R;
import com.parkmecn.android.ui.searchparking.VoiceDialogueActivity;

/**
 * @ClassName: VoiceDialogueAdapter
 * @Description: 语音对话适配器
 * 
 * @author zhouyong
 * @date 2014年5月15日 下午1:47:24
 * @version 1.0
 */
public class VoiceDialogueAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<String> voiceList;

	public VoiceDialogueAdapter(VoiceDialogueActivity voiceDialogueActivity, ArrayList<String> voiceList) {
		this.activity = voiceDialogueActivity;
		this.voiceList = voiceList;
	}

	@Override
	public int getCount() {
		return voiceList.size();
	}

	@Override
	public Object getItem(int position) {
		return voiceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int id = 1, ids = 2;
		ViewHolder viewHolder = null;
		if (id == ids) {
			convertView = activity.getLayoutInflater().inflate(R.layout.voice_dialogue_left_litem, null);
		} else {
			convertView = activity.getLayoutInflater().inflate(R.layout.voice_dialogue_right_litem, null);
		}
		if (convertView.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.tvVoice = (TextView) convertView.findViewById(R.id.tvVoice);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (id == ids) {
			viewHolder.tvVoice.setText("你好");
		} else {
			viewHolder.tvVoice.setText("不好");
		}

		return convertView;
	}

	class ViewHolder {
		private TextView tvVoice;
	}

}
