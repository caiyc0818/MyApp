package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;

import com.bcinfo.tripaway.view.dialog.AffirmDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;


public class TravelAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<TripArticle> articleList;
	private int width;

	public TravelAdapter(Context c, ArrayList<TripArticle> articleList) {
		mContext = c;
		this.articleList = articleList;
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = (dm.widthPixels - DensityUtil.dip2px(mContext, 30)) / 3;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return articleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return articleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ImageView imageview;
		if (convertView == null) {
			imageview = new ImageView(mContext);

			imageview.setLayoutParams(new GridView.LayoutParams(width, width));
			imageview.setScaleType(ImageView.ScaleType.FIT_XY);
			// imageview.setPadding(8,8,8,8);
		} else {
			imageview = (ImageView) convertView;
		}
		imageview.setTag(articleList.get(position));
		imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent blogIntentForDetail = new Intent(mContext, BlogDetailActivity.class);
				blogIntentForDetail.putExtra("blog_article_author_avatar",
						((TripArticle) v.getTag()).getPublisher().getAvatar());
				blogIntentForDetail.putExtra("blog_article_comments", ((TripArticle) v.getTag()).getComments());
				blogIntentForDetail.putExtra("blog_article_id", ((TripArticle) v.getTag()).getPhotoId());
				blogIntentForDetail.putExtra("blog_article_author_id",
						((TripArticle) v.getTag()).getPublisher().getUserId()); // 获取游记发布者的id

				blogIntentForDetail.putExtra("blog_article_pic_list", ((TripArticle) v.getTag()).getPictureList());
				blogIntentForDetail.putExtra("blog_article_current_date", ((TripArticle) v.getTag()).getPublishTime());
				blogIntentForDetail.putExtra("blog_article_author_nickName",
						((TripArticle) v.getTag()).getPublisher().getNickname());
				blogIntentForDetail.putExtra("blog_article_description", ((TripArticle) v.getTag()).getDescription());
				blogIntentForDetail.putExtra("blog_image_index", 0);
				v.getContext().startActivity(blogIntentForDetail);
				((ClubMebHomepageActivity) mContext).overridePendingTransition(R.anim.activity_new,
						R.anim.activity_out);
			}
		});
		ImageLoader.getInstance().displayImage( Urls.imgHost + articleList.get(position).getPictureList().get(0).getUrl(),imageview);
		return imageview;
	}

}
