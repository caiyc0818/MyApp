package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.Triangle;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 推荐目的地 adapter适配器
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月14日 20:11:11
 */
public class SubjectDestAdapter extends BaseAdapter {
	private List<PushResource> locationList;
	public Context context;

	public SubjectDestAdapter(Context context, List<PushResource> locationList) {
		this.context = context;
		this.locationList = locationList;
		System.out.println("locationList==" + locationList);
	}

	@Override
	public int getCount() {
		return locationList.size();
	}

	@Override
	public Object getItem(int position) {
		return locationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator = LayoutInflater.from(context);
		PushResource pushResource = (PushResource) locationList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.item_subject_topic_layout, null);
			holder = new ViewHolder();

			holder.locationFlagIv = (ImageView) convertView.findViewById(R.id.topic_icon_iv);

			holder.locationzhNameTv = (TextView) convertView.findViewById(R.id.topic_title);
			// holder.nameEn = (TextView) convertView.findViewById(R.id.text2);
			/*
			 * AssetManager mgr = context.getAssets();// 得到AssetManager Typeface
			 * tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");//
			 * 根据路径得到Typeface //holder.locationenNameTv.setTypeface(tf);// 设置字体
			 * holder.locationzhNameTv.setTypeface(tf);// 设置字体
			 */
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TripDestination tripDestination = (TripDestination) pushResource.getObject();
		if (tripDestination != null) {
			if (!StringUtils.isEmpty(tripDestination.getDestLogoKey())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + tripDestination.getDestLogoKey(),
						holder.locationFlagIv, AppConfig.options(R.drawable.ic_launcher));
			}
			if (!StringUtils.isEmpty(tripDestination.getDestName())) {
				holder.locationzhNameTv.setText(tripDestination.getDestName());
				// holder.nameEn .setText(tripDestination.getDestNameEn());
			}

		}
		return convertView;
	}

	class ViewHolder {
		private ImageView locationFlagIv;// 目的地国旗iv
		private TextView locationzhNameTv;// 目的地中文名tv
		// private TextView nameEn;
	}
}
