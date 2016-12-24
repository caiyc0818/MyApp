package com.bcinfo.tripaway.adapter;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.r;
import com.bcinfo.photoselector.util.CommonUtils;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.activity.CustomerHomePageActivity;
import com.bcinfo.tripaway.activity.LocationCountryDetailActivity;
import com.bcinfo.tripaway.activity.LoginActivity;
import com.bcinfo.tripaway.activity.PersonalInfoNewActivity;
import com.bcinfo.tripaway.activity.SquarePicDetailActivity;
import com.bcinfo.tripaway.activity.SquareSerialDetailActivity;
import com.bcinfo.tripaway.activity.SubjectDetailActivity;
import com.bcinfo.tripaway.activity.ThemeDetailActivity;
import com.bcinfo.tripaway.activity.TouristHomepageActivity;
import com.bcinfo.tripaway.activity.UserMicroCommentActivity;
import com.bcinfo.tripaway.adapter.GdAdapter.Holder;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.Feed_Schema;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.SquareActivityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wefika.flowlayout.FlowLayout;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 广场主页连载晒图适配器
 * 
 */
public class SquareFragmentAdapter extends BaseAdapter {
	private ArrayList<Feed_Schema> mFeed_Schemas = new ArrayList<>();
	private Context mActivity;
	private boolean isComplete = true;
	private PopupWindow pw;
	// 公用一个适配器 判断是否为关注
	private Boolean isFocusTag;
	
	private int picWidth;
	
	private int picHight;
	
	private float picRatio;

	public SquareFragmentAdapter(Context mActivity, ArrayList<Feed_Schema> mFeed_Schemas, Boolean isFocusTag) {
		isComplete = true;
		this.mActivity = mActivity;
		this.mFeed_Schemas = mFeed_Schemas;
		this.isFocusTag = isFocusTag;
		 DisplayMetrics dm = new DisplayMetrics();
		 ((Activity) mActivity).getWindowManager().getDefaultDisplay().getMetrics(dm);
		picWidth=dm.widthPixels-DensityUtil.dip2px(mActivity, 10);
		picHight=picWidth/31*30;
		picRatio=30/31f;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFeed_Schemas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mFeed_Schemas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		String type = mFeed_Schemas.get(position).getFeedType();
		if ("photo".equals(type)) {
			return 0;
		}
		if ("series".equals(type)) {
			return 1;
		}
		if ("commlink".equals(type)) {
			return 2;
		}
		if ("commproduct".equals(type)) {
			return 3;
		}
		if ("commdestination".equals(type)) {
			return 4;
		}
		if ("commtopic".equals(type)) {
			return 5;
		}
		if ("commsubject".equals(type)) {
			return 6;
		}
		if ("commarticle".equals(type)) {
			return 7;
		}
		if ("commactivity".equals(type)) {
			return 8;
		}
		if ("commuser".equals(type)) {
			return 9;
		}
		return position;

	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		if (convertView == null) {
			holder = new ViewHolder();
			if (getItemViewType(position) == 0) {
				convertView = inflater.inflate(R.layout.squer_item_print, null);
				holder.product_servicer_icon_iv = (ImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
				holder.relative = (LinearLayout) convertView.findViewById(R.id.relative);
				holder.superStar = (ImageView) convertView.findViewById(R.id.starperson);
				holder.imageall = (ImageView) convertView.findViewById(R.id.imageall);
				holder.tvPost = (TextView) convertView.findViewById(R.id.tvPost);
				holder.noguanzhu = (TextView) convertView.findViewById(R.id.noguanzhu);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				holder.label = (TextView) convertView.findViewById(R.id.label);
				holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
				holder.tv_reviewnum = (TextView) convertView.findViewById(R.id.tv_reviewnum);
				holder.tv_pv = (TextView) convertView.findViewById(R.id.tv_pv);
				holder.review_listview = (MyListView) convertView.findViewById(R.id.review_listview);
				holder.tags = (FlowLayout) convertView.findViewById(R.id.tags);
				holder.line = (View) convertView.findViewById(R.id.line);
				holder.addressRelative = (LinearLayout) convertView.findViewById(R.id.addressRelative);
				holder.zanRelative = (RelativeLayout) convertView.findViewById(R.id.zanrelative);
				holder.reviewRelative = (RelativeLayout) convertView.findViewById(R.id.reviewRelative);
				holder.guanzhu_pic = (ImageView) convertView.findViewById(R.id.guanzhu_pic);
				holder.view = (View) convertView.findViewById(R.id.view);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 1) {
				convertView = inflater.inflate(R.layout.squer_item_serialize1, null);
				holder.product_servicer_icon_iv = (ImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
				holder.imageall = (ImageView) convertView.findViewById(R.id.imageall);
				holder.view = (View) convertView.findViewById(R.id.view);
				holder.tvPost = (TextView) convertView.findViewById(R.id.tvPost);
				holder.noguanzhu = (TextView) convertView.findViewById(R.id.noguanzhu);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				holder.label = (TextView) convertView.findViewById(R.id.label);
				holder.tv_reviewnum = (TextView) convertView.findViewById(R.id.tv_reviewnum);
				holder.tv_pv = (TextView) convertView.findViewById(R.id.tv_pv);
				holder.review_listview = (MyListView) convertView.findViewById(R.id.review_listview);
				holder.tags = (FlowLayout) convertView.findViewById(R.id.tags);
				holder.line = (View) convertView.findViewById(R.id.line);
				holder.superStar = (ImageView) convertView.findViewById(R.id.starperson);
				holder.story_title = (TextView) convertView.findViewById(R.id.story_title);
				holder.zanRelative = (RelativeLayout) convertView.findViewById(R.id.zanrelative);
				holder.reviewRelative = (RelativeLayout) convertView.findViewById(R.id.reviewRelative);
				holder.addressRelative = (LinearLayout) convertView.findViewById(R.id.addressRelative);
				holder.guanzhu_pic = (ImageView) convertView.findViewById(R.id.guanzhu_pic);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 2) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 3) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 4) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 5) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 6) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 7) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 8) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			} else if (getItemViewType(position) == 9) {
				convertView = init_commend(holder, inflater);
				convertView.setTag(holder);
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 统一图片尺寸
		WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
		float width = wm.getDefaultDisplay().getWidth();
		ViewGroup.LayoutParams lp = holder.imageall.getLayoutParams();
		lp.width = picWidth;
		lp.height = picHight;
		
		
		// 晒图详情
		if (getItemViewType(position) == 0) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			holder.tvPost.setText(StringUtils.isEmpty(feed_Schema.getDesc()) ? "" : feed_Schema.getDesc());
			// 达人图标
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getIsTalent())) {
				if ("1".equals(feed_Schema.getPublisher().getIsTalent())) {
					holder.superStar.setVisibility(View.VISIBLE);
				} else if ("0".equals(feed_Schema.getPublisher().getIsTalent())) {
					holder.superStar.setVisibility(View.GONE);
				}
			}
			// 评论列表点击
			holder.reviewRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(mActivity, LoginActivity.class);
						((Activity) mActivity).startActivity(it);
						return;
					}
					Intent reviewintent = new Intent(mActivity, UserMicroCommentActivity.class);
					reviewintent.putExtra("objectType", "0");
					reviewintent.putExtra("microId", feed_Schema.getRawData().getPhotoId());
					String counts = StringUtils.isEmpty(feed_Schema.getRawData().getComments()) ? "0"
							: feed_Schema.getRawData().getComments();
					if (counts.equals("0")) {
						reviewintent.putExtra("count", 0);
					} else {
						reviewintent.putExtra("count", Integer.parseInt(counts));
					}
					((Activity) mActivity).startActivityForResult(reviewintent, 3);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			// 设置晒图人头像
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getPublisher().getAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.user_defult_photo));
			} else {
				holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityUtil.squareToPersonHomePage(feed_Schema.getPublisher(), mActivity);
				}
			});
			// 设置发表的人
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getNickname())) {
				holder.tvName.setText(feed_Schema.getPublisher().getNickname());
			} else if (!StringUtils.isEmpty(feed_Schema.getPublisher().getRealName())) {
				holder.tvName.setText(feed_Schema.getPublisher().getRealName());
			} else {
				holder.tvName.setText("");
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getDesc())) {
				try {
					holder.tvDescription.setVisibility(View.VISIBLE);
					List<RichText> richTexts = StringUtils.xmlToRichText(feed_Schema.getRawData().getDesc());
					StringUtils.initRichTextView1(holder.tvDescription, richTexts);
				} catch (PatternSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				holder.tvDescription.setVisibility(View.GONE);
			}
			setTime(holder, feed_Schema);
			// 设置封面
			if (feed_Schema.getRawData().getPictureList().size() > 0) {
			ImageInfo imageInfo=feed_Schema.getRawData().getPictureList().get(0);
			try {
				float h=Float.parseFloat(imageInfo.getHeight());
				float w=Float.parseFloat(imageInfo.getWidth());
				if(h>0&&w>0){
					float ratio=h/w;
					if(ratio<picRatio){
						lp.height=(int) (picWidth/w*h);
						lp.width=picWidth;
					}
				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
			
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (feed_Schema.getRawData().getPictureList().size() > 0) {
				holder.relative.setVisibility(View.VISIBLE);
				if (!StringUtils.isEmpty(feed_Schema.getRawData().getPictureList().get(0).getUrl())) {
					ImageLoader.getInstance().displayImage(
							Urls.imgHost + feed_Schema.getRawData().getPictureList().get(0).getUrl(), holder.imageall,
							AppConfig.options(R.drawable.ic_launcher));
				}
				// 设置图片数量
				int num = feed_Schema.getRawData().getPictureList().size();
				if (num > 0) {
					((TextView) holder.relative.getChildAt(1)).setText(String.valueOf(num));
				}
			} else {
				holder.relative.setVisibility(View.GONE);
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity, SquarePicDetailActivity.class);
					intent.putExtra("id", feed_Schema.getRawData().getPhotoId());
					mActivity.startActivity(intent);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			// 发布地址
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLocation())
					&& !feed_Schema.getRawData().getLocation().equals("不显示")) {
				holder.addressRelative.setVisibility(View.VISIBLE);
				((TextView) holder.addressRelative.getChildAt(1)).setText(feed_Schema.getRawData().getLocation());
			} else {
				holder.addressRelative.setVisibility(View.GONE);
			}
			holder.zanRelative.getChildAt(0).setVisibility(View.VISIBLE);
			holder.zanRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(mActivity, LoginActivity.class);
						((Activity) mActivity).startActivity(it);
						isComplete = true;
						return;
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")
							|| StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "tripstory", 1, position);
					} else if ((feed_Schema.getRawData().getIsLike().equals("1"))) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "tripstory", 0, position);
					}
				}
			});

			// 赞的数目
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLikes())) {
				((TextView) holder.zanRelative.getChildAt(2)).setText(
						"0".equals(feed_Schema.getRawData().getLikes()) ? "" : feed_Schema.getRawData().getLikes());
			}
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
				if (feed_Schema.getRawData().getIsLike().equals("0")) {
					((ImageView) holder.zanRelative.getChildAt(0)).setBackgroundResource(R.drawable.nozan);
				} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
					((ImageView) holder.zanRelative.getChildAt(0)).setBackgroundResource(R.drawable.zan2x);
				}
			}
			// 评论数目
			holder.tv_reviewnum.setText(StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0") ? ""
							: feed_Schema.getRawData().getComments());
			if (StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0")) {
				holder.line.setVisibility(View.GONE);
				holder.review_listview.setVisibility(View.GONE);
			} else {
				holder.review_listview.setVisibility(View.VISIBLE);
				holder.line.setVisibility(View.VISIBLE);
			}
			// 浏览人数
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getViews())) {
				holder.tv_pv.setText("0".equals(feed_Schema.getRawData().getViews()) ? ""
						: "浏览" + feed_Schema.getRawData().getViews());
			}
			// 关注
			if (!StringUtils.isEmpty(feed_Schema.getIsFocus())) {
				if (PreferenceUtil.getUserId().equals(feed_Schema.getPublisher().getUserId())) {
					holder.noguanzhu.setVisibility(View.GONE);
					holder.guanzhu_pic.setVisibility(View.GONE);
				} else {
					if ("no".equals(feed_Schema.getIsFocus())) {
						holder.noguanzhu.setVisibility(View.VISIBLE);
						holder.noguanzhu.setText("+关注");
						holder.noguanzhu.setEnabled(true);
						holder.guanzhu_pic.setVisibility(View.GONE);
						holder.noguanzhu.setBackgroundResource(R.drawable.shape_button);
					} else if ("yes".equals(feed_Schema.getIsFocus())) {
						holder.noguanzhu.setVisibility(View.VISIBLE);
						holder.noguanzhu.setText("已关注");
						holder.noguanzhu.setEnabled(false);
						holder.noguanzhu.setBackgroundDrawable(new BitmapDrawable());
						holder.guanzhu_pic.setVisibility(View.VISIBLE);
					}
				}

			}
			// 评论列表
			showReview(holder, feed_Schema);
			// 标签
			if (feed_Schema.getRawData().getTagNames().size() > 0) {
				holder.tags.setVisibility(View.VISIBLE);
				addFlowView(feed_Schema.getRawData().getTagNames(), holder.tags);
			} else {
				holder.tags.setVisibility(View.GONE);

			}
			// 是否关注
			holder.noguanzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					addOrCancelFocus(false, feed_Schema.getPublisher().getUserId(), position);
				}
			});
			holder.guanzhu_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					showPopu(position, feed_Schema, v);

				}
			});

		}

		// 连载详情
		if (getItemViewType(position) == 1) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			holder.tvPost.setText(StringUtils.isEmpty(feed_Schema.getDesc()) ? "" : feed_Schema.getDesc());
			// 评论点击事件
			holder.reviewRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(mActivity, LoginActivity.class);
						((Activity) mActivity).startActivity(it);
						return;
					}
					Intent reviewintent = new Intent(mActivity, UserMicroCommentActivity.class);
					reviewintent.putExtra("objectType", "1");
					reviewintent.putExtra("microId", feed_Schema.getRawData().getPhotoId());
					String counts = StringUtils.isEmpty(feed_Schema.getRawData().getComments()) ? "0"
							: feed_Schema.getRawData().getComments();
					if (counts.equals("0")) {
						reviewintent.putExtra("count", 0);
					} else {
						reviewintent.putExtra("count", Integer.parseInt(counts));
					}
					((Activity) mActivity).startActivityForResult(reviewintent, 3);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});

			// 地点
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLocation())
					&& !feed_Schema.getRawData().getLocation().equals("不显示")) {
				holder.addressRelative.setVisibility(View.VISIBLE);
				((TextView) holder.addressRelative.getChildAt(1)).setText(feed_Schema.getRawData().getLocation());
			} else {
				holder.addressRelative.setVisibility(View.GONE);
			}
			// 标签
			if (feed_Schema.getRawData().getTagNames().size() > 0) {
				holder.tags.setVisibility(View.VISIBLE);
				addFlowView(feed_Schema.getRawData().getTagNames(), holder.tags);
			} else {
				holder.tags.setVisibility(View.GONE);
			}
			// 达人图标
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getIsTalent())) {
				if ("1".equals(feed_Schema.getPublisher().getIsTalent())) {
					holder.superStar.setVisibility(View.VISIBLE);
				} else if ("0".equals(feed_Schema.getPublisher().getIsTalent())) {
					holder.superStar.setVisibility(View.GONE);
				}
			}
			// 设置晒图人头像
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getPublisher().getAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.user_defult_photo));
			} else {
				holder.product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityUtil.squareToPersonHomePage(feed_Schema.getPublisher(), mActivity);
				}
			});
			// 设置发表的人
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getNickname())) {
				holder.tvName.setText(feed_Schema.getPublisher().getNickname());
			} else if (!StringUtils.isEmpty(feed_Schema.getPublisher().getRealName())) {
				holder.tvName.setText(feed_Schema.getPublisher().getRealName());
			} else {
				holder.tvName.setText("");
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getTitle())) {
				holder.story_title.setVisibility(View.VISIBLE);
				holder.story_title.setText(feed_Schema.getRawData().getTitle());
			} else {
				holder.story_title.setVisibility(View.GONE);
			}
			// 设置发送时间
			setTime(holder, feed_Schema);
			// 设置封面
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getCover())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getRawData().getCover(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher),new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								float h=loadedImage.getHeight();
								float w=loadedImage.getWidth();
								ViewGroup.LayoutParams lp =view.getLayoutParams();
								if(h>0&&w>0){
									float ratio=h/w;
									if(ratio<picRatio){
										lp.height=(int) (picWidth/w*h);
										lp.width=picWidth;
										view.setLayoutParams(lp);
									}
								}
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								
							}
						});
			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity, SquareSerialDetailActivity.class);
					intent.putExtra("id", feed_Schema.getRawData().getPhotoId());
					mActivity.startActivity(intent);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			holder.zanRelative.getChildAt(0).setVisibility(View.VISIBLE);
			holder.zanRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(mActivity, LoginActivity.class);
						((Activity) mActivity).startActivity(it);
						isComplete = true;
						return;
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")
							|| StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "series", 1, position);
					} else if ((feed_Schema.getRawData().getIsLike().equals("1"))) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "series", 0, position);
					}
				}
			});
			// 赞的数目
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLikes())) {
				((TextView) holder.zanRelative.getChildAt(2)).setText(
						"0".equals(feed_Schema.getRawData().getLikes()) ? "" : feed_Schema.getRawData().getLikes());
			}
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
				if (feed_Schema.getRawData().getIsLike().equals("0")) {
					((ImageView) holder.zanRelative.getChildAt(0)).setBackgroundResource(R.drawable.nozan);
				} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
					((ImageView) holder.zanRelative.getChildAt(0)).setBackgroundResource(R.drawable.zan2x);
				}
			}

			// 评论数目
			holder.tv_reviewnum.setText(StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0") ? ""
							: feed_Schema.getRawData().getComments());
			if (StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0")) {
				holder.line.setVisibility(View.GONE);
				holder.review_listview.setVisibility(View.GONE);
			} else {
				holder.review_listview.setVisibility(View.VISIBLE);
				holder.line.setVisibility(View.VISIBLE);

			}
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getViews())) {
				holder.tv_pv.setText("0".equals(feed_Schema.getRawData().getViews()) ? ""
						: "浏览" + feed_Schema.getRawData().getViews());
			}

			// 是否关注
			if (!StringUtils.isEmpty(feed_Schema.getIsFocus())) {
				if (PreferenceUtil.getUserId().equals(feed_Schema.getPublisher().getUserId())) {
					holder.noguanzhu.setVisibility(View.GONE);
					holder.guanzhu_pic.setVisibility(View.GONE);
				} else {
					if ("no".equals(feed_Schema.getIsFocus())) {
						holder.noguanzhu.setText("+关注");
						holder.noguanzhu.setVisibility(View.VISIBLE);
						holder.noguanzhu.setEnabled(true);
						holder.noguanzhu.setBackgroundResource(R.drawable.shape_button);
						holder.guanzhu_pic.setVisibility(View.GONE);
					} else if ("yes".equals(feed_Schema.getIsFocus())) {
						holder.noguanzhu.setVisibility(View.VISIBLE);
						holder.noguanzhu.setText("已关注");
						holder.noguanzhu.setEnabled(false);
						holder.noguanzhu.setBackgroundDrawable(new BitmapDrawable());
						holder.guanzhu_pic.setVisibility(View.VISIBLE);
					}
				}
			}
			holder.noguanzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addOrCancelFocus(false, feed_Schema.getPublisher().getUserId(), position);
				}
			});

			holder.guanzhu_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					showPopu(position, feed_Schema, v);
				}
			});
			// 评论列表
			showReview(holder, feed_Schema);
		}

		if (getItemViewType(position) == 2) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (!StringUtils.isEmpty(feed_Schema.getDefaltName())) {
				holder.tvName.setText(feed_Schema.getDefaltName());
			}
			if (!StringUtils.isEmpty(feed_Schema.getDefaltAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getDefaltAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String path = mFeed_Schemas.get(position).getObj();
					Intent intent = new Intent(mActivity, ContentActivity.class);
					intent.putExtra("title",mFeed_Schemas.get(position).getResTitle());
					intent.putExtra("path", path);
					mActivity.startActivity(intent);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getTitleImage(), holder.imageall,
						AppConfig.options(R.drawable.ic_launcher));
			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("");
				holder.tab.setBackgroundDrawable(new BitmapDrawable());
			}
			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}

		}
		// 推荐位
		if (getItemViewType(position) == 3) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (mFeed_Schemas.get(position).getResTitle() != null) {
				holder.tvName.setText(mFeed_Schemas.get(position).getResTitle());
			}
			if (mFeed_Schemas.get(position).getObject() != null) {
				if (mFeed_Schemas.get(position).getObject().getUser() != null) {
					if (mFeed_Schemas.get(position).getObject().getUser().getAvatar() != null) {
						ImageLoader.getInstance().displayImage(
								Urls.imgHost + mFeed_Schemas.get(position).getObject().getUser().getAvatar(),
								holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
						holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ActivityUtil.toPersonHomePage(mFeed_Schemas.get(position).getObject().getUser(),
										mActivity);
								// v.getContext().startActivity(blogIntentForPersonal);
								((Activity) mActivity).overridePendingTransition(R.anim.activity_new,
										R.anim.activity_out);
							}
						});

					}

				}
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher));

			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SquareActivityUtil.toProductHomePage(mFeed_Schemas.get(position).getObject(), mActivity);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("产品");

			}
			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}
		}
		// 推荐位
		if (getItemViewType(position) == 4) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (!StringUtils.isEmpty(feed_Schema.getDefaltName())) {
				holder.tvName.setText(feed_Schema.getDefaltName());
			}
			if (!StringUtils.isEmpty(feed_Schema.getDefaltAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getDefaltAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			}
			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher));

			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String destId2 = feed_Schema.getObj();// 目的地标识
					Intent intentForLocateCountry2 = new Intent(mActivity, LocationCountryDetailActivity.class);
					intentForLocateCountry2.putExtra("destId", destId2);
					mActivity.startActivity(intentForLocateCountry2);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});

			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("目的地");
			}
		}
		// 推荐位
		if (getItemViewType(position) == 5) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (!StringUtils.isEmpty(feed_Schema.getDefaltName())) {
				holder.tvName.setText(feed_Schema.getDefaltName());
			}
			if (!StringUtils.isEmpty(feed_Schema.getDefaltAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getDefaltAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			}

			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher));

			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String topicId = mFeed_Schemas.get(position).getObj();
					Intent intentForTopicProductList = new Intent(mActivity, ThemeDetailActivity.class);
					intentForTopicProductList.putExtra("themeId", topicId);
					intentForTopicProductList.putExtra("themeName", mFeed_Schemas.get(position).getObject().getTitle());
					// intentForTopicProductList.putExtra("description",
					// topicInfo.getDescription());
					mActivity.startActivity(intentForTopicProductList);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("主题");
			}
		}
		// 推荐位
		if (getItemViewType(position) == 6) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (!StringUtils.isEmpty(feed_Schema.getDefaltName())) {
				holder.tvName.setText(feed_Schema.getDefaltName());
			}
			if (!StringUtils.isEmpty(feed_Schema.getDefaltAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getDefaltAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			}

			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher));
			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String entityId = mFeed_Schemas.get(position).getObj();
					Intent intentSubjectDetail = new Intent(mActivity, SubjectDetailActivity.class);
					intentSubjectDetail.putExtra("entityId", entityId);
					intentSubjectDetail.putExtra("show", true);
					mActivity.startActivity(intentSubjectDetail);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("专题");

			}
		}
		// 推荐位
		if (getItemViewType(position) == 7) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (!StringUtils.isEmpty(feed_Schema.getDefaltName())) {
				holder.tvName.setText(feed_Schema.getDefaltName());
			}
			if (!StringUtils.isEmpty(feed_Schema.getDefaltAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getDefaltAvatar(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
			}
			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher));
			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String path = Urls.content_host + mFeed_Schemas.get(position).getObj();
					Intent intent = new Intent(mActivity, ContentActivity.class);
					intent.putExtra("path", path);
					mActivity.startActivity(intent);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});

			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("文章");
			}

		}
		if (getItemViewType(position) == 8) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (mFeed_Schemas.get(position).getResTitle() != null) {
				if (mFeed_Schemas.get(position).getResTitle() != null) {
					if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getResTitle())) {
						holder.tvName.setText(mFeed_Schemas.get(position).getResTitle());
					}
				}
			}
			if (mFeed_Schemas.get(position).getTitleImage() != null) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));

			}
			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher));
			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String path = Urls.huodong + mFeed_Schemas.get(position).getObj() + ".html";
					Intent intent = new Intent(mActivity, ContentActivity.class);
					intent.putExtra("title", "活动");
					intent.putExtra("path", path);
					mActivity.startActivity(intent);
					((Activity) mActivity).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("活动");
			}
		}
		if (getItemViewType(position) == 9) {
			final Feed_Schema feed_Schema = mFeed_Schemas.get(position);
			if (mFeed_Schemas.get(position).getResTitle() != null) {
				if (mFeed_Schemas.get(position).getResTitle() != null) {
					if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getResTitle())) {
						holder.tvName.setText(mFeed_Schemas.get(position).getResTitle());
					}
				}
			}
			if (mFeed_Schemas.get(position).getTitleImage() != null) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));

			}
			holder.product_servicer_icon_iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityUtil.toPersonHomePage(feed_Schema.getUserInfo(), mActivity);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getDescription())) {
				holder.commend_title.setVisibility(View.VISIBLE);
				holder.commend_title.setText(feed_Schema.getDescription());
			} else {
				holder.commend_title.setVisibility(View.GONE);
			}
			holder.imageall.setLayoutParams(lp);
			holder.view.setLayoutParams(lp);
			if (!StringUtils.isEmpty(mFeed_Schemas.get(position).getTitleImage())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + mFeed_Schemas.get(position).getTitleImage(),
						holder.imageall, AppConfig.options(R.drawable.ic_launcher));
			} else {
				holder.imageall.setImageResource(R.drawable.ic_launcher);
			}
			holder.imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityUtil.toPersonHomePage(feed_Schema.getUserInfo(), mActivity);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getFeedType())) {
				holder.tab.setText("服务者");
			}
		}
		return convertView;
	}

	/*
	 * 评论列表
	 */
	private void showReview(ViewHolder holder, final Feed_Schema feed_Schema) {
		MicroBlogsCommentsAdapter adapter1 = (MicroBlogsCommentsAdapter) holder.review_listview.getAdapter();
		int count = 0;
		if (adapter1 == null) {
			ArrayList<Comments> commentsList = new ArrayList<Comments>();
			for (Comments comments : feed_Schema.getRawData().getCommentList()) {
				count++;
				if (count > 2) {
					break;
				}
				commentsList.add(comments);
			}
			holder.review_listview.setAdapter(new MicroBlogsCommentsAdapter(mActivity, commentsList));
		} else {
			count = 0;
			MicroBlogsCommentsAdapter adapter = (MicroBlogsCommentsAdapter) adapter1;
			ArrayList<Comments> commentsList = adapter.getCommentsList();
			commentsList.clear();
			for (Comments comments : feed_Schema.getRawData().getCommentList()) {
				count++;
				if (count > 2) {
					break;
				}
				commentsList.add(comments);
			}
			adapter.notifyDataSetChanged();
		}
	}

	// 设置时间
	private void setTime(ViewHolder holder, final Feed_Schema feed_Schema) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			long time = (date.getTime() - format.parse(feed_Schema.getPublishTime()).getTime()) / 1000;
			long day1 = time / (24 * 3600);
			long hour1 = time % (24 * 3600) / 3600;
			long minute1 = time % 3600 / 60;
			if (day1 > 0) {
				holder.tvTime.setText(DateUtil.getFormateDate1(feed_Schema.getPublishTime()));
			} else if (hour1 > 0) {
				holder.tvTime.setText(hour1 + "小时前");
			} else if (minute1 > 0) {
				holder.tvTime.setText(minute1 + "分钟前");
			} else {
				if (time <= 0) {
					holder.tvTime.setText("刚刚");
				} else {
					holder.tvTime.setText(time + "秒前");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private View init_commend(ViewHolder holder, LayoutInflater inflater) {
		View convertView;
		convertView = inflater.inflate(R.layout.squer_item_commend, null);
		holder.product_servicer_icon_iv = (ImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
		holder.view = (View) convertView.findViewById(R.id.view);
		holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
		holder.tab = (TextView) convertView.findViewById(R.id.tab);
		holder.commend_title = (TextView) convertView.findViewById(R.id.commend_title);
		holder.imageall = (ImageView) convertView.findViewById(R.id.imageall);
		return convertView;
	}

	public class ViewHolder {
		public View view;
		public LinearLayout relative;
		public TextView tvPost;
		public ImageView guanzhu_pic;
		public TextView commend_title;
		public TextView tab;
		public TextView tv_zan;
		public TextView pickedProductNameTv;
		public TextView originalPrice;
		public TextView pickedProductPriceTv;
		public RelativeLayout reviewRelative;
		public TextView tvid;
		RelativeLayout zanRelative;
		ImageView product_servicer_icon_iv;// 头像
		TextView tvName;// 发表人
		TextView noguanzhu;// 是否关注
		TextView tvTime;// 发表时间
		TextView label;// 晒图 话题
		TextView tvDescription;// 晒图详情
		ImageView imageall;// 图片封面
		FlowLayout tags;// 标号
		TextView tv_reviewnum;// 评论
		TextView tv_pv;// 浏览
		MyListView review_listview;
		View line;
		LinearLayout addressRelative;
		// 连载
		ImageView superStar;
		TextView story_title;

	}

	@SuppressLint("ResourceAsColor")
	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		flowLayout.removeAllViews();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(mActivity);
			newView.setBackgroundResource(R.drawable.shape_club_tag2);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#FFFFFF"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(mActivity, 1);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	// 点赞
	private void setZanInfo(final String id, String objectType, final int like, final int position) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("objectId", id);
			jsonObject.put("objectType", objectType);
			jsonObject.put("opType", like);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.set_like, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					isComplete = true;
					SquareFragmentAdapter.this.notifyDataSetChanged();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					isComplete = true;
					SquareFragmentAdapter.this.notifyDataSetChanged();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					if (response.optString("code").equals("00000")) {
						mFeed_Schemas.get(position).getRawData().setIsLike(String.valueOf(like));
						String likes = mFeed_Schemas.get(position).getRawData().getLikes();
						int num = StringUtils.isEmpty(likes) ? 0 : Integer.parseInt(likes);
						if (like == 0 && num != 0) {
							mFeed_Schemas.get(position).getRawData().setLikes((num - 1) + "");
						} else if (like == 1) {
							mFeed_Schemas.get(position).getRawData().setLikes((num + 1) + "");
						}
						if (like == 1) {
							Toast.makeText(mActivity, "点赞 +1", Toast.LENGTH_SHORT).show();
						}
					}
					Intent it = new Intent("com.bcinfo.refreshLikesCount");
					it.putExtra("microId", id);
					it.putExtra("like", like);
					mActivity.sendBroadcast(it);
					isComplete = true;
					SquareFragmentAdapter.this.notifyDataSetChanged();
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 增加取消关注
	private void addOrCancelFocus(boolean flag, final String userId, final int position) {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(mActivity, LoginActivity.class);
			mActivity.startActivity(intent);
			return;
		}
		if (!flag) {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", userId);
				StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
				HttpUtil.post(Urls.friend_list_url, entity, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if ("00000".equals(code)) {
							for (int i = 0; i < mFeed_Schemas.size(); i++) {
								if (mFeed_Schemas.get(i).getPublisher() != null) {
									if (userId.equals(mFeed_Schemas.get(i).getPublisher().getUserId())) {
										mFeed_Schemas.get(i).setIsFocus("yes");
									}
								}
							}
							ToastUtil.showToast(mActivity, "关注成功");
							Intent intent = new Intent("com.bcinfo.refreshFocus");
							intent.putExtra("userId", userId);
							intent.putExtra("isFocus", true);
							mActivity.sendBroadcast(intent);
						}
						SquareFragmentAdapter.this.notifyDataSetChanged();
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			HttpUtil.delete(Urls.friend_list_url + "/" + userId, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if ("00000".equals(code)) {
						for (int i = 0; i < mFeed_Schemas.size(); i++) {
							if (mFeed_Schemas.get(i).getPublisher() != null) {
								if (userId.equals(mFeed_Schemas.get(i).getPublisher().getUserId())) {
									mFeed_Schemas.get(i).setIsFocus("no");
									if (isFocusTag == true) {
										mFeed_Schemas.remove(i);
										Log.i("删除的项目", i + "");
									}
								}
							}
						}
						if (isFocusTag == false) {
							ToastUtil.showToast(mActivity, "取消关注成功");
						}
						Intent intent = new Intent("com.bcinfo.refreshFocus");
						intent.putExtra("userId", userId);
						intent.putExtra("isFocus", false);
						mActivity.sendBroadcast(intent);
						SquareFragmentAdapter.this.notifyDataSetChanged();
					} else {
						ToastUtil.showToast(mActivity, "取消关注失败");
					}
				}
			});
		}
	}

	private void storeProductByIsFaved(boolean flag, String productId, final int position) {
		if (!flag) {
			JSONObject object = new JSONObject();
			try {
				object.put("objectId", productId);
				object.put("type", "product");
				StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
				// 添加收藏
				HttpUtil.post(Urls.add_store, entity, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if (!"00000".equals(code)) {
							return;
						} else if ("00000".equals(code)) {
							ArrayList<Feed_Schema> temp = new ArrayList<>();
							mFeed_Schemas.get(position).getObject().setIsFav("yes");
							temp.addAll(mFeed_Schemas);
							mFeed_Schemas.clear();
							mFeed_Schemas.addAll(temp);
						}
						SquareFragmentAdapter.this.notifyDataSetChanged();
						// mStorePicIv.setImageResource(R.drawable.stored_pics);
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			RequestParams params = new RequestParams();
			params.put("type", "product");
			params.put("objectId", productId);
			// 删除收藏
			HttpUtil.delete(Urls.cancel_store, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if (!"00000".equals(code)) {
						// Intent intent = new
						// Intent(GrouponProductNewDetailActivity.this,
						// LoginActivity.class);
						// startActivity(intent);
						return;
					} else if ("00000".equals(code)) {
						ArrayList<Feed_Schema> temp = new ArrayList<>();
						mFeed_Schemas.get(position).getObject().setIsFav("no");
						temp.addAll(mFeed_Schemas);
						mFeed_Schemas.clear();
						mFeed_Schemas.addAll(temp);
					}
					SquareFragmentAdapter.this.notifyDataSetChanged();
					// mStorePicIv.setImageResource(R.drawable.store_pics);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
			});
		}
	}

	/*
	 * 弹出取消关注
	 */
	private void showPopu(final int position, final Feed_Schema feed_Schema, final View v) {
		Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.hintpw);
		Animation anim2 = AnimationUtils.loadAnimation(mActivity, R.anim.hintpw2);
		anim.setFillAfter(true);
		v.startAnimation(anim);
		// 获取屏幕宽度
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mActivity).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenW = metrics.widthPixels;
		View convView = LayoutInflater.from(mActivity).inflate(R.layout.squarecannelguanzhupopuwindow, null);
		int gap = DensityUtil.dip2px(mActivity, 30);
		pw = new PopupWindow(convView, screenW - 2 * gap, LayoutParams.WRAP_CONTENT);
		// 设置pw中的控件可点击
		pw.setFocusable(true);
		// 调用展示方法，设置展示位置
		/*
		 * 在展示pw的同时，让窗口的其余部分显示半透明的颜色
		 */
		WindowManager.LayoutParams params = ((Activity) mActivity).getWindow().getAttributes();
		params.alpha = 0.6f;
		((Activity) mActivity).getWindow().setAttributes(params);

		// 设置pw可以在点击外部区域时关闭显示
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);

		// 设置pw弹出和隐藏时的动画效果
		/*
		 * 注意：此处的动画效果是通过style样式指定的
		 */
		// pw.setAnimationStyle(R.style.pw_anim_style);

		// 展示对话框，并设置展示位置

		// 监控pw何时被关闭，并且，在pw被关闭的时候，将窗口的背景色调节回来
		pw.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// TODO Auto-generated method stub
				// 将窗口颜色调回完全透明
				WindowManager.LayoutParams params = ((Activity) mActivity).getWindow().getAttributes();
				params.alpha = 1;
				((Activity) mActivity).getWindow().setAttributes(params);
				Animation anim2 = AnimationUtils.loadAnimation(mActivity, R.anim.hintpw2);
				v.startAnimation(anim2);
				anim2.setFillAfter(true);
			}
		});
		pw.showAtLocation(v, Gravity.CENTER | Gravity.CENTER, 0, 0);
		// 设置pw中button的点击事件
		TextView photo = (TextView) convView.findViewById(R.id.photo);
		photo.setOnClickListener(new OnClickListener() {
			Animation anim2 = AnimationUtils.loadAnimation(mActivity, R.anim.hintpw2);

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				addOrCancelFocus(true, feed_Schema.getPublisher().getUserId(), position);
			}
		});
	}

}
