package com.bcinfo.tripaway.adapter;

import java.util.List;
import java.util.Map;

import com.bcinfo.tripaway.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 微游记 照片适配器
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月22日 17:41:22
 * 
 */
public class SquarePhotoAdapter extends BaseAdapter {
	/**
	 * 存储值的list集合
	 */
	private List<Bitmap> photoList;
	private Context context;
	private LayoutInflater inflator;

	public SquarePhotoAdapter() {

	}

	public SquarePhotoAdapter(List<Bitmap> photoList, Context context) {
		this.photoList = photoList;

		this.context = context;
	}

	@Override
	public int getCount() {
		if(photoList.size()==5){
			return photoList.size();
		}
		return photoList.size() + 1;
	}

	@Override
	public Object getItem(int position) {

		return photoList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		holder = new ViewHolder();
		inflator = LayoutInflater.from(context);
		convertView = inflator.inflate(R.layout.item_trip_blog_photo, parent, false);
		holder.img = (ImageView) convertView.findViewById(R.id.item_blog_photo);
		convertView.setTag(holder);
		// 设置每个imageview的item
		if (position < photoList.size()) {
			holder.img.setImageBitmap(photoList.get(position));
		} else {
			holder.img.setBackgroundResource(R.drawable.addphoto);
		}

		return convertView;
	}

	/**
	 * 内部类ViewHolder
	 */
	class ViewHolder {

		private ImageView img;
	}
}
