package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CertifaceAdapter extends BaseAdapter {

	private List<ImageInfo> list;
	
	private Context context;
	
	public CertifaceAdapter(){
		
	}
	
	public CertifaceAdapter(List<ImageInfo> list,Context context){
		this.list = list;
		this.context = context;
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
		ViewHolder viewHolder = null;
		ImageInfo info = list.get(position);
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.image_item,null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
		if(!StringUtils.isEmpty(info.getUrl())){
			ImageLoader.getInstance().displayImage(Urls.imgHost+info.getUrl(), viewHolder.imageView);
		}
		return convertView;
	}

	class ViewHolder{
		
		private ImageView imageView;
	}
}
