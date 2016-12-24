package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.bean.HelpInfo;

public class HelpAndConsultAdapter extends BaseAdapter {
	private List<HelpInfo> helpInfoList;

	private Context mContext;

	private LayoutInflater inflater;

	public HelpAndConsultAdapter(Context mContext, List<HelpInfo> helpInfoList) {
		this.mContext = mContext;
		this.helpInfoList = helpInfoList;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return helpInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return helpInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		holder = new ViewHolder();
		if (helpInfoList.get(position).getLevel() == 0)
			convertView = inflater.inflate(
					R.layout.help_and_consult_header_item, null);
		else
		{convertView = inflater
		.inflate(R.layout.help_and_consult_item, null);
View line= convertView.findViewById(R.id.line);
if(position+1<helpInfoList.size()&&helpInfoList.get(position+1).getLevel() == 0)
    line.setVisibility(View.GONE);
else line.setVisibility(View.VISIBLE);
}
		TextView title=(TextView) convertView.findViewById(R.id.item_title);
		title.setText(helpInfoList.get(position).getTitle());
		return convertView;
	}

	private class ViewHolder {
		private TextView title;
	}

}
