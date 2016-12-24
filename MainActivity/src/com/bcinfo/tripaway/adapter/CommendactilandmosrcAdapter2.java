package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.provider.Settings.SettingNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.r;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Article;
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.ArticlleInfo1;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Story;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.subjectsoftyouji;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.CacheUtils;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.Triangle;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 推荐 userclub adapter适配器
 * 
 */
public class CommendactilandmosrcAdapter2 extends BaseAdapter {
	private List<PushResource> locationList;
	public Context context;
	CacheUtils utils;

	public CommendactilandmosrcAdapter2(Context context, List<PushResource> locationList) {
		utils = CacheUtils.getInstance();
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
			convertView = inflator.inflate(R.layout.item_recomend_list, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.commndIv = (ImageView) convertView.findViewById(R.id.commend_image_iv);
			holder.commend_title = (TextView) convertView.findViewById(R.id.commend_title);
			holder.commend_introduce = (TextView) convertView.findViewById(R.id.commend_introduce);
			holder.commendReson = (TextView) convertView.findViewById(R.id.commend_reson);
			holder.triangle = convertView.findViewById(R.id.triangle);
			holder.reason_layout = (LinearLayout) convertView.findViewById(R.id.reason_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if ("softtext".equals(pushResource.getObjectType())) {
			ArticleInfo soft = (ArticleInfo) pushResource.getObject();

			if (!StringUtils.isEmpty(pushResource.getReason())) {
				holder.reason_layout.setVisibility(View.VISIBLE);
				holder.commendReson.setText(pushResource.getReason());
			}

			ImageLoader.getInstance().displayImage(Urls.imgHost + soft.getCover(), holder.commndIv,
					AppConfig.options(R.drawable.default_photo));
			holder.commend_title.setText(soft.getTitle());
			if (!StringUtils.isEmpty(soft.getAbstracts())) {
				holder.commend_introduce.setText(soft.getAbstracts());
			} else {
				holder.commend_introduce.setText(soft.getContent().replaceAll("<p>", "").replaceAll("</p>", "").replace("<br/>", "\n"));
			}

		}
		if ("story".equals(pushResource.getObjectType())) {
			ArticleInfo story = (ArticleInfo) pushResource.getObject();
			if (!StringUtils.isEmpty(pushResource.getReason())) {
				holder.reason_layout.setVisibility(View.VISIBLE);
				holder.triangle.setVisibility(View.VISIBLE);
				holder.commendReson.setText(pushResource.getReason());
			}
			ImageLoader.getInstance().displayImage(Urls.imgHost + story.getCover(), holder.commndIv,
					AppConfig.options(R.drawable.default_photo));
			holder.commend_title.setText(story.getTitle());
			if (!StringUtils.isEmpty(story.getAbstracts())) {
				holder.commend_introduce.setText(story.getAbstracts());
			} else {
				holder.commend_introduce.setText(story.getContent().replaceAll("<p>", "").replaceAll("</p>", "").replace("<br/>", "\n"));
			}
		}
		return convertView;
	}
	class ViewHolder {
		View triangle;
		ImageView image;
		LinearLayout reason_layout;
		private ImageView commndIv;// 推荐图标
		private TextView commendReson;// 推荐原因
		private TextView commend_title;// 推荐标题
		private TextView commend_introduce;// 推荐简介

	}
}
