package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.SubjectDetailActivity;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;


/**
 * 推荐专题 adapter适配器
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月14日 20:11:11
 */
public class SubjectGridAdapter extends BaseAdapter {
	private List<PushResource> subjectPushResourceList;
	public Context context;

	public SubjectGridAdapter(Context context,
			List<PushResource> subjectPushResourceList) {
		this.context = context;
		this.subjectPushResourceList = subjectPushResourceList;
		System.out.println("subjectPushResourceList=="
				+ subjectPushResourceList);
	}

	@Override
	public int getCount() {
		return subjectPushResourceList.size();
	}

	@Override
	public Object getItem(int position) {
		return subjectPushResourceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator = LayoutInflater.from(context);
		PushResource subjectPushResource = (PushResource) subjectPushResourceList
				.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.item_discovery_subject,
					null);
			holder = new ViewHolder();
			holder.cover = (ImageView) convertView.findViewById(R.id.cover);
			holder.subjectTitle = (TextView) convertView
					.findViewById(R.id.subject_title);
			holder.subjectSubtitle = (TextView) convertView
					.findViewById(R.id.subject_subtitle);
			holder.clubName = (TextView) convertView
					.findViewById(R.id.club_name);
			holder.tags = (TextView) convertView.findViewById(R.id.tags);
			holder.avartar = (ImageView) convertView.findViewById(R.id.avartar);
			holder.subjectIcon = (ImageView) convertView.findViewById(R.id.subject_icon);
			/*
			 * AssetManager mgr = context.getAssets();// 得到AssetManager Typeface
			 * tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");//
			 * 根据路径得到Typeface //holder.locationenNameTv.setTypeface(tf);// 设置字体
			 * holder.locationzhNameTv.setTypeface(tf);// 设置字体
			 */
			Log.e("SubjectGridAdapter,convertView为null", Integer.toString(position));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			Log.e("SubjectGridAdapter,convertView不为null", Integer.toString(position));
		}
		String objectType=subjectPushResource.getObjectType();
		if(!StringUtils.isEmpty(objectType)&&!"subject".equals(objectType)){
			((View)(holder.avartar.getParent().getParent())).setVisibility(View.GONE);
			holder.subjectIcon.setVisibility(View.GONE);
		}
		else{
			((View)(holder.avartar.getParent().getParent())).setVisibility(View.VISIBLE);
			holder.subjectIcon.setVisibility(View.VISIBLE);
		}
		UserInfo userInfo = null;
		if (subjectPushResource.getObject() != null&&subjectPushResource.getObject() instanceof UserInfo) {
			userInfo = (UserInfo) subjectPushResource.getObject();
		}
		;

		if (subjectPushResource.getTitleImage() != null) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + subjectPushResource.getTitleImage(),
					holder.cover, AppConfig.options(R.drawable.ic_launcher));
		}
		;
		Map<String, String> objectParam = subjectPushResource.getObjectParam();
		// if (userInfo!=null)
		// {
		//
		// }
		if (objectParam != null) {
			if (objectParam.get("nickname") != null) {
				holder.clubName.setText(objectParam.get("nickname"));
			}
			if (objectParam.get("tags") != null) {
				holder.tags.setText(objectParam.get("tags"));
			}
			if (objectParam.get("avartar") != null) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + objectParam.get("avartar"),
						holder.avartar, AppConfig.options(R.drawable.summer));
			}
		}
		
		if (!StringUtils.isEmpty(subjectPushResource.getResTitle())) {
			holder.subjectTitle.setText(subjectPushResource.getResTitle());
			holder.subjectTitle.getPaint().setFakeBoldText(true);
		}

		if (!StringUtils.isEmpty(subjectPushResource.getSubTitle())) {
			holder.subjectSubtitle.setText(subjectPushResource.getSubTitle());
		}
		holder.avartar.setTag(position);
		holder.avartar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position=(Integer)v.getTag();
				ActivityUtil.toDetailPage(subjectPushResourceList.get(position), context,true);
			}
		});
		View view =(View)holder.subjectTitle.getParent();
		view.setTag(position);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position=(Integer)v.getTag();
                ActivityUtil.toDetailPage(subjectPushResourceList.get(position), context,true);
			}
		});
		return convertView;
	}

	class ViewHolder {

		private ImageView cover;

		private TextView clubName;

		private TextView subjectTitle;

		private TextView subjectSubtitle;

		private ImageView avartar;
		
		private ImageView subjectIcon;

		private TextView tags;

	}

}
