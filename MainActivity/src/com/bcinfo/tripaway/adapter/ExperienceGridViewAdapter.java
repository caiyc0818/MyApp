package com.bcinfo.tripaway.adapter;

import java.util.List;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ExperienceGridViewAdapter extends BaseAdapter{
	private Context mContext;
	private List<String> list;
	public ExperienceGridViewAdapter(Context mContext,List<String> list){
		this.mContext=mContext;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			String url  =(String) getItem(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.journey_adapter, null);
			holder.img=(ImageView)convertView.findViewById(R.id.journey);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		if(!StringUtils.isEmpty(url)){
			  ImageLoader.getInstance().displayImage(Urls.imgHost+url, holder.img, AppConfig.options(R.drawable.ic_launcher));
		}
		return convertView;
	}
	class ViewHolder{
		private ImageView img;
	}
}
