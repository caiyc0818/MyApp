package com.bcinfo.tripaway.adapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.CustomerHomePageActivity;
import com.bcinfo.tripaway.adapter.ClubTrendsAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.ImageInfo;
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
import android.sax.StartElementListener;
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
import android.widget.Toast;

public class ClubTravelsAdapter extends BaseAdapter implements OnClickListener {

	/**
	 * 游记集合
	 */
	private List<Feed> travelList;

	private Context mContext;

	private Activity mActivity;

	/**
	 * 对图片进行管理的工具类
	 */
	private BlogImageLoader imageLoader;

	public ClubTravelsAdapter(Context mContext, List<Feed> travelList, Activity mActivity) {
		this.travelList = travelList;
		this.mContext = mContext;
		this.mActivity = mActivity;
		imageLoader = BlogImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return travelList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return travelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	TripArticle articles =null;
	Story storys=null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = null;
		ViewHolder holder = null;
		Feed feed = travelList.get(position);
		Object rawData = feed.getMap().get("rawData");
		if (null == convertView) {
			inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.club_travels_item, null);
			
			holder = new ViewHolder();
			holder.greate_travel_relativelayout = (RelativeLayout) convertView
					.findViewById(R.id.greate_travel_relativelayout);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvPost = (TextView) convertView.findViewById(R.id.tvPost);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);

			holder.product_servicer_icon_iv = (RoundImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
			
			holder.map_location = (ImageView) convertView.findViewById(R.id.map_location);
			holder.tvType = (TextView) convertView.findViewById(R.id.tvType);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			holder.tv_story_title = (TextView) convertView.findViewById(R.id.tv_story_title);
			holder.tv_story_content = (TextView) convertView.findViewById(R.id.tv_story_content);
			holder.map_location_text = (TextView) convertView.findViewById(R.id.map_location_text);
			holder.tvComments = (TextView) convertView.findViewById(R.id.tvComments);
			

			holder.micro_travel_relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.micro_travel_relativeLayout);
			holder.gridView = (MyGridView) convertView.findViewById(R.id.gridView);
			holder.gridView.setFocusable(false);
			// convertView.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (rawData instanceof TripArticle) {
			TripArticle article = (TripArticle) rawData;
			articles= (TripArticle) rawData;
holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Toast.makeText(mContext,articles.getPublisher().getRoleName(), 1).show();;
					
					Intent it=new Intent();
					
					
					
					
					
					it.putExtra("identifyId", articles.getPublisher().getUserId());
					it.setClass(mContext,ClubMebHomepageActivity.class);
					if("司机".equals(articles.getPublisher().getRoleName()))
					{
						it.putExtra("isDriverHomePage", true);
						
					}else if("管理员".equals(articles.getPublisher().getRoleName())){
						it.putExtra("userInfo", articles.getPublisher());
						it.setClass(mContext,ClubFirstPageActivity.class);
						
					}else{
						it.putExtra("userInfo", articles.getPublisher());

						it.setClass(mContext,CustomerHomePageActivity.class);
					}
					mContext.startActivity(it);
				}
			});
			holder.greate_travel_relativelayout.setVisibility(View.GONE);
			holder.micro_travel_relativeLayout.setVisibility(View.VISIBLE);
			if (StringUtils.isEmpty(article.getLocation())) {
				holder.map_location.setVisibility(View.GONE);
				holder.map_location_text.setVisibility(View.GONE);
			} else {
				holder.map_location_text.setText(article.getLocation());
				holder.map_location.setVisibility(View.VISIBLE);
				holder.map_location_text.setVisibility(View.VISIBLE);
			}
			if (article.getPublisher().getAvatar() != null) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + article.getPublisher().getAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			}
			holder.tvContent.setText(article.getDescription());
			if(!StringUtils.isEmpty(article.getPublisher().getRoleName()))
			holder.tvPost.setText("(" + article.getPublisher().getRoleName() + ")");
			else
				holder.tvPost.setText("");
			holder.tvComments.setText(article.getComments());
			holder.tvName.setText(article.getPublisher().getNickname());
			articles= (TripArticle) rawData;
			holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								//Toast.makeText(mContext,articles.getPublisher().getRoleName(), 1).show();;
								

								Intent it=new Intent();
								
								
				
								
							
								it.putExtra("identifyId", articles.getPublisher().getUserId());
								it.setClass(mContext,ClubMebHomepageActivity.class);
								if("司机".equals(articles.getPublisher().getRoleName()))
								{
									it.putExtra("isDriverHomePage", true);
									
								}else if("管理员".equals(articles.getPublisher().getRoleName())){
									it.putExtra("userInfo", articles.getPublisher());
									it.setClass(mContext,ClubFirstPageActivity.class);
									
								}else{
									it.putExtra("userInfo", articles.getPublisher());

									it.setClass(mContext,CustomerHomePageActivity.class);
								}
								mContext.startActivity(it);
							}
						});
			List<ImageInfo> list = new ArrayList<ImageInfo>();
			for (int i = 0; i < article.getPictureList().size() && i < 9; i++) {
				list.add(article.getPictureList().get(i));
			}
			ImageAdapter ipAdapter = new ImageAdapter(mContext, article, list, mActivity);
			// 添加并且显示
			holder.gridView.setAdapter(ipAdapter);
			holder.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			// ipAdapter.notifyDataSetChanged();
			// convertView.setTag(article);
		} else if (rawData instanceof Story) {
			Story story = (Story) rawData;
			holder.greate_travel_relativelayout.setVisibility(View.VISIBLE);
			holder.micro_travel_relativeLayout.setVisibility(View.GONE);
			holder.map_location.setVisibility(View.GONE);
			holder.map_location_text.setVisibility(View.GONE);
			if (!StringUtils.isEmpty(story.getCover())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + story.getCover(), holder.iv_image,
						AppConfig.options(R.drawable.ic_launcher));
			}
			if (story.getPublisher().getAvatar() != null) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + story.getPublisher().getAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			}
			holder.tv_story_content.setText(story.getAbstracts());
			holder.tv_story_title.setText(story.getTitle());
			if(!StringUtils.isEmpty(story.getPublisher().getRoleName()))
				holder.tvPost.setText("(" + story.getPublisher().getRoleName() + ")");
				else
					holder.tvPost.setText("");
			storys= (Story) rawData;
			holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								//Toast.makeText(mContext,articles.getPublisher().getRoleName(), 1).show();;
								
								Intent it=new Intent();
								
								
				
								
							
								it.putExtra("identifyId", articles.getPublisher().getUserId());
								it.setClass(mContext,ClubMebHomepageActivity.class);
								if("司机".equals(articles.getPublisher().getRoleName()))
								{
									it.putExtra("isDriverHomePage", true);
									
								}else if("管理员".equals(articles.getPublisher().getRoleName())){
									it.putExtra("userInfo", articles.getPublisher());
									it.setClass(mContext,ClubFirstPageActivity.class);
									
								}else{
									it.putExtra("userInfo", articles.getPublisher());

									it.setClass(mContext,CustomerHomePageActivity.class);
								}
								mContext.startActivity(it);
							}
						});
			holder.tvComments.setText(story.getComments());
			holder.tvName.setText(story.getPublisher().getNickname());
			// convertView.setTag(story);
		}
		holder.tvTime.setText(DateUtil.getFormateDateWithOutSencond(feed.getPublishTime()));
		holder.tvType.setText(feed.getDesc());
		return convertView;
	}

	class ViewHolder {
		/**
		 * 姓名
		 */
		private TextView tvName;
		/**
		 * 称号
		 */
		private TextView tvPost;
		/**
		 * 时间
		 */
		private TextView tvTime;
		/**
		 * 图片
		 */
		private RoundImageView product_servicer_icon_iv;
		/**
		 * 内容图片
		 */
		private ImageView iv_image;

		private ImageView map_location;

		private MyGridView gridView;
		private TextView tvType;
		private TextView tvContent;
		private TextView tv_story_title;
		private TextView tv_story_content;
		private TextView map_location_text;
		private TextView tvComments;
		private RelativeLayout greate_travel_relativelayout;
		private RelativeLayout micro_travel_relativeLayout;

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
