package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.view.MyGridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ExperienceAdapter extends BaseAdapter{
	private Context mContext;
	private List<Experiences> infoList;
	private ExperienceGridViewAdapter adapter;
	private List<String> list=new ArrayList<String>();
	public ExperienceAdapter(Context mContext,List<Experiences> infoList){
		this.mContext=mContext;
		this.infoList=infoList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			ViewHolder holder =null;
			Experiences info=(Experiences) getItem(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.experience_adapter, null);
			holder.grid=(MyGridView) convertView.findViewById(R.id.journey_grid_view);
			
			holder.text=(TextView)convertView.findViewById(R.id.experience_text);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.text.setText(info.getDescription());
		list=info.getImagesUrls();
		adapter=new ExperienceGridViewAdapter(mContext, list);
		holder.grid.setAdapter(adapter);
		
		return convertView;
	}
	class ViewHolder{
		public MyGridView grid;
		public TextView text;
	}
}
