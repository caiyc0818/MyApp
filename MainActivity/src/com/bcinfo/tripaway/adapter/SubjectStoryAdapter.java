package com.bcinfo.tripaway.adapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.adapter.ClubTrendsAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Story;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.camera.ImageBucketAdapter;
import com.bcinfo.tripaway.camera.ImageGridAdapter;
import com.bcinfo.tripaway.camera.TestPicActivity;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.loadIMG.BlogImageLoader;
import com.bcinfo.tripaway.view.MyGridView;

import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SubjectStoryAdapter extends BaseAdapter implements OnClickListener {

	/**
	 * 游记集合
	 */
	private List<PushResource> pushResources;

	private Context mContext;

	public SubjectStoryAdapter(Context mContext, List<PushResource> pushResources) {
		this.pushResources = pushResources;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pushResources.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pushResources.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = null;
		ViewHolder holder = null;
		PushResource pushResource = pushResources.get(position);
		if (null == convertView) {
			inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_article_micro, null);
			holder = new ViewHolder();
			// holder.greate_travel_relativelayout = (RelativeLayout)
			// convertView
			// .findViewById(R.id.greate_travel_relativelayout);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			// holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			// holder.tvPost = (TextView) convertView.findViewById(R.id.tvPost);
			// holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			// holder.product_servicer_icon_iv = (RoundImageView)
			// convertView.findViewById(R.id.product_servicer_icon_iv);
			// holder.map_location = (ImageView)
			// convertView.findViewById(R.id.map_location);
			// holder.tvType = (TextView) convertView.findViewById(R.id.tvType);
			// holder.tvContent = (TextView)
			// convertView.findViewById(R.id.tvContent);
			holder.tv_story_title = (TextView) convertView.findViewById(R.id.tvName);
			holder.tv_story_content = (TextView) convertView.findViewById(R.id.content);
			// holder.map_location_text = (TextView)
			// convertView.findViewById(R.id.map_location_text);
			// holder.tvComments = (TextView)
			// convertView.findViewById(R.id.tvComments);
			// holder.micro_travel_relativeLayout = (RelativeLayout) convertView
			// .findViewById(R.id.micro_travel_relativeLayout);
			// holder.gridView = (MyGridView)
			// convertView.findViewById(R.id.gridView);
			// holder.gridView.setFocusable(false);
			// convertView.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Object object = pushResource.getObject();
		if (object instanceof ArticleInfo) {
			ArticleInfo articleInfo = (ArticleInfo) object;
			if (!StringUtils.isEmpty(articleInfo.getTitle())) {
				holder.tv_story_title.setText(articleInfo.getTitle());
			}
			if (!StringUtils.isEmpty(articleInfo.getCover())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + articleInfo.getCover(), holder.iv_image,
						AppConfig.options(R.drawable.ic_launcher));
			}
			if (!StringUtils.isEmpty(articleInfo.getAbstracts())) {
				holder.tv_story_content.setText(articleInfo.getAbstracts());
			}

			ArticleInfo story = (ArticleInfo) pushResource.getObject();
			// holder.greate_travel_relativelayout.setVisibility(View.VISIBLE);
			// holder.micro_travel_relativeLayout.setVisibility(View.GONE);
			// holder.map_location.setVisibility(View.GONE);
			// holder.map_location_text.setVisibility(View.GONE);
			if (!StringUtils.isEmpty(story.getCover())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + story.getCover(), holder.iv_image,
						AppConfig.options(R.drawable.ic_launcher));
			}
			// if (story.getPublisher().getAvatar() != null) {
			// ImageLoader.getInstance().displayImage(Urls.imgHost +
			// story.getPublisher().getAvatar(),
			// holder.product_servicer_icon_iv,
			// AppConfig.options(R.drawable.ic_launcher));
			// }
			if (!StringUtils.isEmpty(story.getAbstracts())) {
				holder.tv_story_content.setText(story.getAbstracts());
				// holder.tv_story_content.setText(story.getAbstracts());
			}else {
				holder.tv_story_content.setText(story.getContent().replace("<p>", "").replace("</p>", "").replace("<br/>", ""));
			}
			holder.tv_story_title.setText(story.getTitle());
			// if(story.getPublisher().getOrgRole()!=null&&story.getPublisher().getOrgRole().getRoleName()!=null)
			// holder.tvPost.setText("(" +
			// story.getPublisher().getOrgRole().getRoleName() + ")");
			// else {
			// holder.tvPost.setVisibility(View.GONE);
			// }
			// holder.tvComments.setText(story.getComments());
			// holder.tvName.setText(story.getPublisher().getNickname());
			// if (!StringUtils.isEmpty(story.getPublishTime())) {
			// Date date = new Date();
			// DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			// try {
			// long time = (date.getTime()
			// -format.parse(story.getPublishTime()).getTime())/1000;
			// long day1=time/(24*3600);
			// long hour1=time%(24*3600)/3600;
			// long minute1=time%3600/60;
			// if(day1 >0){
			// holder.tvTime.setText(DateUtil.getFormateDate1(story.getPublishTime()));
			// }else if(hour1 > 0){
			// holder.tvTime.setText(hour1+"小时前");
			// }else if(minute1 > 0){
			// holder.tvTime.setText(minute1+"分钟前");
			// }else{
			// holder.tvTime.setText("刚刚");
			// }
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
			// }
			// holder.tvType.setText("发表大游记");
		}

		return convertView;
	}

	class ViewHolder {
		// /**
		// * 姓名
		// */
		// private TextView tvName;
		// /**
		// * 称号
		// */
		// private TextView tvPost;
		// /**
		// * 时间
		// */
		// private TextView tvTime;
		// /**
		// * 图片
		// */
		// private RoundImageView product_servicer_icon_iv;
		/**
		 * 内容图片
		 */
		private ImageView iv_image;

		// private ImageView map_location;

		// private MyGridView gridView;
		private TextView tvType;
		// private TextView tvContent;
		private TextView tv_story_title;
		private TextView tv_story_content;
		// private TextView map_location_text;
		// private TextView tvComments;
		// private RelativeLayout greate_travel_relativelayout;
		// private RelativeLayout micro_travel_relativeLayout;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.club_travel_item:
			if ((TripArticle) v.getTag() != null) {

			} else if ((Story) v.getTag() != null) {

			}
			break;

		default:
			break;
		}
	}
}
