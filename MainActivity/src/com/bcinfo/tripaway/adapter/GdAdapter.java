package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @ClassName: AppointmentsAdapter
 * @Description: TODO(发布商品的图片)
 * @author scene
 * @date 2015-4-17 上午11:36:50
 * 
 */
public class GdAdapter extends BaseAdapter {
	private Context mContext;
	private List<PhotoModel> mLists;

	public GdAdapter(Context mContext, List<PhotoModel> mLists) {
		this.mLists = mLists;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mLists == null ? 0 : mLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mLists == null ? null : mLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup group) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(
					R.layout.activity_slidingmenu_albums_item_item, null);
			holder.img = (ImageView) view.findViewById(R.id.img);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		PhotoModel info = mLists.get(position);

		if (info != null) {
			DensityUtil.setViewHeight2(
					holder.img,
					(DensityUtil.getScreen(mContext)[1] - DensityUtil.dip2px(mContext, 44)) / 4);

			if (info.getOriginalPath().equals("default")) {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.addphoto, holder.img);
			} else {
				ImageLoader.getInstance().displayImage(
						"file://" + info.getOriginalPath(), holder.img);
			}
			
		}
		return view;
	}

	class Holder {
		ImageView img;
	}

}
