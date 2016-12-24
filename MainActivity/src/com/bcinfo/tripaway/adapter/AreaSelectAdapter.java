package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.LocationListActivity;
import com.bcinfo.tripaway.activity.LocationSelectAreaActivity;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.utils.StringUtils;

public class AreaSelectAdapter extends BaseAdapter {
	private List<HashMap<String, String>> areaList = new ArrayList<HashMap<String, String>>();

	private Context mContext;

	private LayoutInflater inflater;

	public AreaSelectAdapter(Context mContext, List<HashMap<String, String>> areaList) {
		this.mContext = mContext;
		this.areaList = areaList;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return areaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return areaList.get(position);
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.area_select_item, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name = (TextView) convertView.findViewById(R.id.name_txt);
		holder.name.setTag(position);
		holder.address = (TextView) convertView.findViewById(R.id.address_txt);
		Map<String, String> area = areaList.get(position);
		if (!StringUtils.isEmpty(area.get("name"))) {
			holder.name.setText(area.get("name"));
		}
		if (!StringUtils.isEmpty(area.get("address"))) {
			holder.address.setText(area.get("address"));
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position = (Integer) ((TextView)v.findViewById(R.id.name_txt)).getTag();
				HashMap<String, String> str = areaList.get(position);
				String address = str.get("name");
				Intent intent1 = new Intent();
				intent1.putExtra("address", address);
				((Activity)mContext).setResult(0, intent1);
				((Activity)mContext).overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
				((Activity)mContext).finish();
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView name;
		private TextView address;
	}

}
