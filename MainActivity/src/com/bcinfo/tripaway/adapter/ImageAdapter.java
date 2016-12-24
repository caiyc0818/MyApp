package com.bcinfo.tripaway.adapter;

import java.util.List;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.adapter.ClubTravelsAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class ImageAdapter extends BaseAdapter {

	List<ImageInfo> imageViews;
	Context mContext;
	TripArticle article;
	Activity mActivity;

	public ImageAdapter(Context mContext, TripArticle article,  List<ImageInfo> imageViews,Activity mActivity) {
		this.imageViews = imageViews;
		this.mContext = mContext;
		this.article = article;
		this.mActivity = mActivity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageViews.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageViews.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		LayoutInflater inflater = null;
//		ViewHolder holder = null;
		ImageInfo imageInfo = imageViews.get(position);
		ImageView imageView;
		if (null == convertView) {
			imageView = new ImageView(mContext);
//			LayoutParams params = new LayoutParams(
//					(int) TypedValue
//							.applyDimension(
//									TypedValue.COMPLEX_UNIT_DIP, 80,
//									mContext.getResources()
//											.getDisplayMetrics()),
//					(int) TypedValue
//							.applyDimension(
//									TypedValue.COMPLEX_UNIT_DIP, 80,
//									mContext.getResources()
//											.getDisplayMetrics()));
//			params.setMargins(5, 0, 0, 0);
			imageView.setLayoutParams(new GridView.LayoutParams(DensityUtil.dip2px(mContext, 80), DensityUtil.dip2px(mContext, 80)));  
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent blogIntentForDetail = new Intent(mContext, BlogDetailActivity.class);
					// TripArticle tripArticle = (TripArticle) v.getTag();
					// 显示第几张图片
					blogIntentForDetail.putExtra("blog_image_index", 0);

	                blogIntentForDetail.putExtra("blog_article", article);					

	             // 标示ID
					blogIntentForDetail.putExtra("blog_article_id", article.getPhotoId());
					// 图片集
					blogIntentForDetail.putExtra("blog_article_pic_list", article.getPictureList());
					// 描述
					blogIntentForDetail.putExtra("blog_article_description", article.getDescription());
					// 评论条数
					blogIntentForDetail.putExtra("blog_article_comments", article.getComments());
					// 发布时间
					blogIntentForDetail.putExtra("blog_article_current_date", article.getPublishTime());
					// 用户头像
					blogIntentForDetail.putExtra("blog_article_author_avatar", article.getPublisher().getAvatar());
					// 用户信息
					blogIntentForDetail.putExtra("blog_article_author_id", article.getPublisher().getUserId());
					// 昵称
					blogIntentForDetail.putExtra("blog_article_author_nickName", article.getPublisher().getNickname());
	                
					mContext.startActivity(blogIntentForDetail);
					mActivity.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
		} else {
			imageView = (ImageView) convertView;
		}

		if (!StringUtils.isEmpty(imageInfo.getUrl())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + imageInfo.getUrl(), imageView,
					AppConfig.options(R.drawable.ic_launcher));
		}
		return imageView;
	}

	class ViewHolder {
		ImageView imageView;
	}
}
