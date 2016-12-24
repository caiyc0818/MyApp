package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;

import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ClubTrendsAdapter extends BaseAdapter {

	private List<Feed> feedList;

	private Context mContext;

	public ClubTrendsAdapter() {

	}
	
	private OnperationListener listener;

	public ClubTrendsAdapter(Context mContext, List<Feed> feedList,OnperationListener listener) {
		this.mContext = mContext;
		this.feedList = feedList;
		this.listener =listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feedList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return feedList.get(position);
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
		Feed feed = feedList.get(position);
		if (null == convertView) {
			inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.club_trends_item, null);
			holder = new ViewHolder();
			// 产品
			holder.productContainer = (LinearLayout) convertView
					.findViewById(R.id.club_trend_product_container);
			holder.productTime = (TextView) convertView
					.findViewById(R.id.club_trend_pro_time);
			holder.productTitle = (TextView) convertView
					.findViewById(R.id.club_trend_pro_title);
			holder.productImg = (ImageView) convertView
					.findViewById(R.id.club_trend_pro_img);

			// 关注
			holder.userContainer = (LinearLayout) convertView
					.findViewById(R.id.club_trend_user_container);
			holder.userTime = (TextView) convertView
					.findViewById(R.id.club_trend_focus_time);
			holder.userName = (TextView) convertView
					.findViewById(R.id.club_trend_focus_name);
			holder.userTag = (TextView) convertView
					.findViewById(R.id.club_trend_focus_tag);
			holder.userFocusAdd = (TextView) convertView
					.findViewById(R.id.club_trend_focus_add);
			holder.userPhoto = (RoundImageView) convertView
					.findViewById(R.id.club_trend_focus_photo);

			// 微游记
			holder.storyContainer = (LinearLayout) convertView
					.findViewById(R.id.club_trend_story_container);
			holder.storyTime = (TextView) convertView
					.findViewById(R.id.club_trend_story_time);
			holder.storyContent = (TextView) convertView
					.findViewById(R.id.club_trend_story_content);
			holder.storyImgs = (LinearLayout) convertView
					.findViewById(R.id.club_trend_story_imgs);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Object rawData = feed.getMap().get("rawData");

		if (rawData instanceof ProductNewInfo) {
			ProductNewInfo info = (ProductNewInfo) rawData;
			// 如果 对象是产品
			holder.productContainer.setVisibility(View.VISIBLE);
			holder.userContainer.setVisibility(View.GONE);
			holder.storyContainer.setVisibility(View.GONE);
			holder.productTime.setText(DateUtil.getFormateDate(feed
					.getPublishTime()));
			holder.productTitle.setText(info.getTitle());
			holder.productImg.setImageResource(R.drawable.ic_launcher);
			if (!StringUtils.isEmpty(info.getTitleImg())) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + info.getTitleImg(), holder.productImg,
						AppConfig.options(R.drawable.ic_launcher));
			}
			holder.productImg.setTag(info.getId());
			holder.productImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it = new Intent(mContext,GrouponProductNewDetailActivity.class);
					it.putExtra("productId", (String) v.getTag());
					it.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(it);
				}
			});
		} else if (rawData instanceof UserInfo) {
			UserInfo info = (UserInfo) rawData;
			// 如果 对象是用户
			holder.productContainer.setVisibility(View.GONE);
			holder.userContainer.setVisibility(View.VISIBLE);
			holder.storyContainer.setVisibility(View.GONE);
			holder.userTime.setText(DateUtil.getFormateDate(feed
					.getPublishTime()));
			holder.userName.setText(info.getNickname());
			holder.userPhoto.setImageResource(R.drawable.user_defult_photo);
			if (!StringUtils.isEmpty(info.getAvatar())) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + info.getAvatar(), holder.userPhoto,
						AppConfig.options(R.drawable.user_defult_photo));
			}
			ArrayList<String> tags = info.getTags();
			String tagName ="";
			if(tags != null&&tags.size()>0){
				for(int i=0;i<tags.size();i++){
					if(i == 0){
						tagName += tags.get(i);
					}else{
						tagName += " · "+tags.get(i);
					}
				}
			}
			holder.userTag.setText(tagName);
			// holder.userTag
			if ("no".equals(info.getIsFocus())) {
				holder.userFocusAdd.setVisibility(View.VISIBLE);
			} else {
				holder.userFocusAdd.setVisibility(View.GONE);
			}
			holder.userFocusAdd.setTag(R.id.tag_first,info.getUserId());
			holder.userFocusAdd.setTag(R.id.tag_second,position);
			holder.userFocusAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String userId = (String) v.getTag(R.id.tag_first);
					int id = (Integer) v.getTag(R.id.tag_second);
					listener.addFoucsById(userId,id);
				}
			});
		} else if (rawData instanceof TripArticle) {
			TripArticle info = (TripArticle) rawData;
			// 如果 对象是微游记
			holder.productContainer.setVisibility(View.GONE);
			holder.userContainer.setVisibility(View.GONE);
			holder.storyContainer.setVisibility(View.VISIBLE);
			holder.storyTime.setText(DateUtil.getFormateDate(feed
					.getPublishTime()));
			holder.storyContent.setText(info.getDescription());
			ArrayList<ImageInfo> list = info.getPictureList();
		
			if (list != null && list.size() > 0) {
				int nums = 0;
				if(list.size()>3){
					nums = 3;
				}else{
					nums = list.size();
				}
				final ArrayList<ImageInfo> listss = list;
				for (int i =0;i<nums;i++) {
					ImageInfo imageInfo = list.get(i);
					ImageView imageView = new ImageView(mContext);
					LayoutParams params = new LayoutParams(
							(int) TypedValue
									.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 80,
											mContext.getResources()
													.getDisplayMetrics()),
							(int) TypedValue
									.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 80,
											mContext.getResources()
													.getDisplayMetrics()));
					params.setMargins(5, 0, 0, 0);
					imageView.setLayoutParams(params);
					imageView.setScaleType(ScaleType.FIT_XY);
					if(!StringUtils.isEmpty(imageInfo.getUrl())){
						ImageLoader.getInstance().displayImage(Urls.imgHost+imageInfo.getUrl(),imageView);
					}
					imageView.setTag(i);
					imageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int m = (Integer) v.getTag();
							Intent intentForView = new Intent(mContext, ImageViewerActivity.class);
				            intentForView.putExtra("image_index", 0);
				            intentForView.putExtra("STATE_POSITION", m);
				            intentForView.putParcelableArrayListExtra("images", listss);
				            mContext.startActivity(intentForView);
						}
					});
					holder.storyImgs.addView(imageView);
				}
			}
		}

		return convertView;
	}

	class ViewHolder {
		// 产品
		private LinearLayout productContainer;

		private TextView productTime;

		private TextView productTitle;

		private ImageView productImg;

		// 关注
		private LinearLayout userContainer;

		private TextView userTime;

		private TextView userName;

		private TextView userTag;

		private RoundImageView userPhoto;

		private TextView userFocusAdd;

		// 微游记
		private LinearLayout storyContainer;

		private TextView storyTime;

		private TextView storyContent;

		private LinearLayout storyImgs;
	}

	public interface OnperationListener {
		void addFoucsById(String userId,int id);
	}
}
