package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R.layout;
import android.app.Activity;
import android.app.ApplicationErrorReport.CrashInfo;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.provider.Settings.SettingNotFoundException;
import android.util.TypedValue;
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
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Story;
import com.bcinfo.tripaway.bean.SubjectInfo;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.CacheUtils;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.Triangle;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 推荐位的 主题、目的地、专题 的 adapter适配器
 * 
 */
public class CommendDestopicsubjectAdapter extends BaseAdapter {
	private List<PushResource> locationList;
	public Context context;
	CacheUtils utils;

	public CommendDestopicsubjectAdapter(Context context, List<PushResource> locationList) {
		this.context = context;
		utils = CacheUtils.getInstance();
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
			convertView = inflator.inflate(R.layout.item_recomend_notitle_list, null);
			holder = new ViewHolder();
			holder.commndIv = (ImageView) convertView.findViewById(R.id.commend_image_iv);
			holder.commendReson = (TextView) convertView.findViewById(R.id.commend_reson);
			holder.commend_main = (TextView) convertView.findViewById(R.id.commend_maintitle);
			holder.commend_title = (TextView) convertView.findViewById(R.id.text1);
			holder.commend_enName = (TextView) convertView.findViewById(R.id.text2);
			holder.commend_introduce = (TextView) convertView.findViewById(R.id.text3);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image);
			holder.triangle = convertView.findViewById(R.id.triangle);
			holder.reason_layout = (LinearLayout) convertView.findViewById(R.id.reason_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!(pushResource == null)) {
			// if ("destination".equals(pushResource.getObjectType())) {
			// holder.commendReson.setText(pushResource.getReason());
			// TripDestination tripDestination = (TripDestination)
			// pushResource.getObject();
			// ImageLoader.getInstance().displayImage(Urls.imgHost +
			// tripDestination.getDestLogoKey(), holder.commndIv,
			// AppConfig.options(R.drawable.default_photo));
			// holder.commend_title.setText(tripDestination.getDestName());
			// holder.commend_title.setTextSize(30);
			// holder.commend_enName.setText(tripDestination.getDestNameEn());
			// holder.commend_introduce.setText(tripDestination.getDestKeyWords());
			// } else if ("subject".equals(pushResource.getObjectType())) {
			// holder.commendReson.setText(pushResource.getReason());
			// SubjectInfo subjectInfo = (SubjectInfo) pushResource.getObject();
			// ImageLoader.getInstance().displayImage(Urls.imgHost +
			// subjectInfo.getCover(), holder.commndIv,
			// AppConfig.options(R.drawable.default_photo));
			// holder.commend_main.setVisibility(View.VISIBLE);
			// holder.commend_main.setText(subjectInfo.getTitle());
			// holder.commend_title.setVisibility(View.GONE);
			// holder.commend_enName.setVisibility(View.GONE);
			// holder.commend_title.setVisibility(View.GONE);
			// } else if ("topic".equals(pushResource.getObjectType())) {
			// TopicInfo topicInfo = (TopicInfo) pushResource.getObject();
			// holder.commendReson.setText(pushResource.getReason());
			// holder.commend_main.setVisibility(View.VISIBLE);
			// holder.commend_main.setText(topicInfo.getTitle());
			// holder.commend_title.setVisibility(View.GONE);
			// holder.commend_enName.setVisibility(View.GONE);
			// holder.commend_title.setVisibility(View.GONE);
			// }
			//

			if ("destination".equals(pushResource.getObjectType())) {
				// holder.subjectIcon.setVisibility(View.GONE);
				if (!(StringUtils.isEmpty(pushResource.getReason()))) {
					holder.reason_layout.setVisibility(View.VISIBLE);
					holder.triangle.setVisibility(View.VISIBLE);
					holder.commendReson.setText(pushResource.getReason());
				}
				TripDestination tripDestination = (TripDestination) pushResource.getObject();
				if (tripDestination.getDestKeyWords() != null && !tripDestination.getDestKeyWords().equals("null")
						&& !tripDestination.getDestKeyWords().equals("")) {
					String keywords = tripDestination.getDestKeyWords().toString();
					holder.commend_introduce.setVisibility(View.VISIBLE);
					if (keywords.contains(",")) {
						keywords = keywords.replace(",", "·");
					} else if (keywords.contains("，")) {
						keywords = keywords.replace("，", "·");
					} else if (keywords.contains("·")) {
						keywords = keywords.replace("·", " · ");
					}
					holder.commend_introduce.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
					holder.commend_introduce.setText(keywords);
				}
				ImageLoader.getInstance().displayImage(Urls.imgHost + tripDestination.getDestLogoKey(), holder.commndIv,
						AppConfig.options(R.drawable.ic_launcher));
				// holder.commend_main.setTextSize(TypedValue.COMPLEX_UNIT_SP,
				// 20);
				holder.commend_main.setVisibility(View.GONE);
				holder.commend_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
				// holder.commend_main.setText(tripDestination.getDestName());
				holder.commend_title.setText(tripDestination.getDestName());
				holder.commend_enName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				holder.commend_enName.setText(tripDestination.getDestNameEn());
			} else if ("subject".equals(pushResource.getObjectType())) {
				holder.commend_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
				holder.commend_main.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
				// holder.locationKeywordsTv.setVisibility(View.GONE);
				// holder.commend_enName.setTextSize(TypedValue.COMPLEX_UNIT_SP,
				// 14);
				// holder.subjectIcon.setVisibility(View.VISIBLE);
				if (!StringUtils.isEmpty(pushResource.getReason())) {
					holder.reason_layout.setVisibility(View.VISIBLE);
					holder.triangle.setVisibility(View.VISIBLE);
					holder.commendReson.setText(pushResource.getReason());
				}
				SubjectInfo subjectInfo = (SubjectInfo) pushResource.getObject();

				ImageLoader.getInstance().displayImage(Urls.imgHost + subjectInfo.getCover(), holder.commndIv,
						AppConfig.options(R.drawable.ic_launcher));
				holder.commend_main.setVisibility(View.VISIBLE);
				holder.commend_main.setText(subjectInfo.getTitle());
				holder.commend_title.setVisibility(View.GONE);
				holder.commend_enName.setVisibility(View.GONE);
				holder.commend_title.setVisibility(View.GONE);

			} else if ("topic".equals(pushResource.getObjectType())) {
				if (!StringUtils.isEmpty(pushResource.getReason())) {
					holder.reason_layout.setVisibility(View.VISIBLE);
					holder.triangle.setVisibility(View.VISIBLE);
					holder.commendReson.setText(pushResource.getReason());
				}
				TopicInfo topicInfo = (TopicInfo) pushResource.getObject();
				ImageLoader.getInstance().displayImage(Urls.imgHost + topicInfo.getBackground(), holder.commndIv,
						AppConfig.options(R.drawable.ic_launcher));
				holder.commend_main.setVisibility(View.VISIBLE);
				holder.commend_main.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
				holder.commend_main.setText(topicInfo.getTitle());
				holder.commend_title.setVisibility(View.GONE);
				holder.commend_enName.setVisibility(View.GONE);
				holder.commend_title.setVisibility(View.GONE);

			}

		}

		return convertView;
	}

	class ViewHolder {

		LinearLayout reason_layout;
		View triangle;
		ImageView imageView;
		private ImageView commndIv;// 推荐图标
		private TextView commendReson;// 推荐原因
		private TextView commend_main;// 主标题
		private TextView commend_title;// 推荐标题
		private TextView commend_enName;// 推荐简介
		private TextView commend_introduce;// 推荐简介
	}
}
