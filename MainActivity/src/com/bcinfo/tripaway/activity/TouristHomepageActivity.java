package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.s;
import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.photoselector.util.CommonUtils;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.adapter.MicroBlogsCommentsAdapter;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.DataBean;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.FamousComment;
import com.bcinfo.tripaway.bean.Feed_Schema;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.SquareTripArticle;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil;
import com.bcinfo.tripaway.utils.UploadPicUtil.UploadFinishListener;
import com.bcinfo.tripaway.view.FilterLayout;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.FilterLayout.OnDaysListener;
import com.bcinfo.tripaway.view.FilterLayout.OnDestinationListener;
import com.bcinfo.tripaway.view.FilterLayout.OnPriceListener;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView.PullListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.controller.impl.InitializeController;
import com.umeng.socialize.view.wigets.SectionListView;
import com.wefika.flowlayout.FlowLayout;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class TouristHomepageActivity extends BaseActivity implements OnClickListener, PersonalScrollViewListener {

	private int tagGap;

	private FlowLayout tagsFlow;

	private List<TopicOrTag> topicOrTags = new ArrayList<TopicOrTag>();

	private LinearLayout maintitle;

	private LinearLayout startFilter;

	private TextView all;

	private FilterLayout filterLayout;

	private TextView cancel;

	private TextView ok;

	private PopupWindow popupWindow;

	List<String> priceStrs = new ArrayList<String>();
	List<String> daysStrs = new ArrayList<String>();

	private ImageView mProductHeadImg;

	private PersonInfoScrollView mProductScrollView;

	private LinearLayout layout_back_button;

	private TextView mFocusTxt;

	private boolean isFocused = false;

	private UserInfo user;

	private boolean isSelf = false;

	private boolean isFootPrintEdit = false;

	private ImageView takePhoto;

	private TextView footprintOk;

	private LinearLayout footprintLayout;

	private TextView footprintNum;

	private static final int FOOTPRINT = 1;

	private List<TopicOrTag> tags = new ArrayList<TopicOrTag>();

	private TextView nickNameTextView;

	private TextView focusNumTextView;

	private TextView fansNumTextView;

	private RoundImageView photoRoundImageView;

	private TextView descTextView;

	private LinearLayout serialListview;

	private ImageView descSave;

	private String feedType = "all";

	private int time = 0;

	private String tagId = "";

	private String feedTypeTemp = "all";

	private int timeTemp = 0;

	private String tagIdTemp = "";

	private static String[] feedTypes = { "all", "photo", "series" };

	private static int[] times = { 1, 2, 3, 4, 5 };

	private View mTravelListViewFooter;

	private boolean isNeedLoad = true;

	private int alpha = -1;

	private RelativeLayout layout_product_detail_title;

	private boolean isLoadmore = false;

	private UploadPicUtil uploadPicUtil;

	private final int ADD_BACKGROUND_CODE = 1002;
	
private int picWidth;
	
	private int picHight;
	
	private float picRatio;

	private LinearLayout fans;
	private LinearLayout follow;
	
	private ImageView picNoContentLv;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.tourist_homepage);
		statisticsTitle="个人主页";
		user = getIntent().getParcelableExtra("userInfo");
		String selfId = PreferenceUtil.getUserId();
		if (user == null || StringUtils.isEmpty(selfId)) {
		} else {
			if (user.getUserId().equals(selfId)) {
				isSelf = true;
			}
		}
		initViews();
		tagGap = DensityUtil.dip2px(this, 5);
		if (user != null) {
			getFootPrint(user.getUserId());
			queryUserInfo(user.getUserId());
		}
		querySquareTags();
		allSuqareList(user.getUserId(), "", "", "0");
		uploadPicUtil = new UploadPicUtil(uploadFinishListener);
		picWidth=screenWidth-DensityUtil.dip2px(this, 10);
		picHight=picWidth/31*30;
		picRatio=30/31f;
	}

	private UploadFinishListener uploadFinishListener = new UploadFinishListener() {

		@Override
		public void onUploadFinishListener() {
			// TODO Auto-generated method stub
			List<String> keyList = uploadPicUtil.getPicKeyList();
			if (keyList.size() == 1) {
				userInfoEdit(null, keyList.get(0));
			}
		}
	};

	private LinearLayout picContentLl;

	private View tagsFlowLine;

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY) {
		// TODO Auto-generated method stub
		if (view != null && view == mProductScrollView) {

			alpha = positionY / 3;
			if (positionY < 50 || positionY == 50) {
				// detail_product_title.setAlpha(1f);
			} else {
				// detail_product_title.setAlpha(0);
			}

			if (positionY > 220)

			{
				alpha = 255;
			}
			layout_product_detail_title.getBackground().setAlpha(alpha);
			View view1 = mProductScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = mProductScrollView.getScrollY();
			int scollHeight = mProductScrollView.getHeight();
			if (height == scollY + scollHeight) {
				// 底部
				allSuqareList(user.getUserId(), "", "", "0");
			}
		} else {
			return;
		}
	}

	private void initViews() {
		
		fans = (LinearLayout) findViewById(R.id.fans);
		follow = (LinearLayout) findViewById(R.id.follow);
		fans.setOnClickListener(this);
		follow.setOnClickListener(this);
		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(0);
		layout_product_detail_title.setFocusable(true);
		layout_product_detail_title.setFocusableInTouchMode(true);
		layout_product_detail_title.requestFocus();
		tagsFlow = (FlowLayout) findViewById(R.id.tags_flow);
		maintitle = (LinearLayout) findViewById(R.id.maintitle);
		startFilter = (LinearLayout) findViewById(R.id.startFilter);
		all = (TextView) findViewById(R.id.all);
		all.setOnClickListener(this);
		startFilter.setOnClickListener(this);
		mProductHeadImg = (ImageView) findViewById(R.id.personpic);
		mProductScrollView = (PersonInfoScrollView) findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setScrollListener(this);
		mProductScrollView.setPullListener(mPullListener);
		layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		layout_back_button.setOnClickListener(this);
		mFocusTxt = (TextView) this.findViewById(R.id.add_focus);
		footprintOk = (TextView) this.findViewById(R.id.footprint_ok);
		takePhoto = (ImageView) findViewById(R.id.take_photo);
		footprintLayout = (LinearLayout) findViewById(R.id.footprint_Layout);
		footprintNum = (TextView) this.findViewById(R.id.footprint_num);
		mFocusTxt.setOnClickListener(this);
		takePhoto.setOnClickListener(this);
		footprintOk.setOnClickListener(this);
		nickNameTextView = (TextView) this.findViewById(R.id.username);
		focusNumTextView = (TextView) this.findViewById(R.id.focus_num);
		fansNumTextView = (TextView) this.findViewById(R.id.fans_num);
		photoRoundImageView = (RoundImageView) this.findViewById(R.id.personal_icon);
		descTextView = (TextView) this.findViewById(R.id.desc);
		descTextView.setTypeface(TripawayApplication.normalTf);
		serialListview = (LinearLayout) findViewById(R.id.pic_and_serial_layout);
		descSave = (ImageView) findViewById(R.id.desc_save);
		mTravelListViewFooter = findViewById(R.id.xlistview_footer_content);
		descSave.setOnClickListener(this);
		if (isSelf) {
			takePhoto.setVisibility(View.VISIBLE);
			mFocusTxt.setVisibility(View.INVISIBLE);
			footprintLayout.setVisibility(View.VISIBLE);
//			mProductHeadImg.setOnClickListener(this);
		} else {
			footprintOk.setVisibility(View.GONE);
			descTextView.setEnabled(false);
			descSave.setVisibility(View.GONE);
		}
		picNoContentLv = (ImageView) findViewById(R.id.pic_no_content_lv);
		picContentLl = (LinearLayout) findViewById(R.id.pic_content_ll);
		tagsFlowLine =  findViewById(R.id.tags_flow_line);
		
	}

	private void setData() {
		if (!StringUtils.isEmpty(user.getNickname()))
			nickNameTextView.setText(user.getNickname());
		else
			nickNameTextView.setText(user.getRealName());
		focusNumTextView.setText(user.getFocus() == null ? "0" : user.getFocus());
		fansNumTextView.setText(user.getFansCount() == null ? "0" : user.getFansCount());
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + user.getAvatar(), photoRoundImageView,
					AppConfig.options(R.drawable.user_defult_photo));
		}
		if (!StringUtils.isEmpty(user.getBackground())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + user.getBackground(), mProductHeadImg,
					AppConfig.options(R.drawable.ic_launcher));
		}
		if (!StringUtils.isEmpty(user.getIntroduction()))
			descTextView.setText(user.getIntroduction());
		else
			descTextView.setText("这个人很懒，什么都没留下！");
		if (!StringUtils.isEmpty(user.getIsFocus()) && user.getIsFocus().equals("yes")) {
			mFocusTxt.setText("取消关注");
			isFocused = true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startFilter:
			// 设置好参数之后再show
			if (popupWindow != null) {
				backgroundAlpha(0.5f);
				popupWindow.showAtLocation(startFilter, Gravity.RIGHT, 0, 0);
			}
			break;
		case R.id.layout_back_button:
			finish();
			activityAnimationClose();
			break;
		case R.id.add_focus:
			addOrCancelFocus(isFocused, user.getUserId());
			break;
		case R.id.ok:
			if (feedTypeTemp.equals(feedType) && timeTemp == time && tagIdTemp.equals(tagId)) {
				popupWindow.dismiss();
				return;
			}
			pageNum = 1;
			List.clear();
			mTravelListViewFooter.setVisibility(View.VISIBLE);
			serialListview.removeAllViews();
			isNeedLoad = true;
			allSuqareList(user.getUserId(), "", "", "0");
			popupWindow.dismiss();
			feedTypeTemp = feedType;
			timeTemp = time;
			tagIdTemp = tagId;
			break;
		case R.id.cancel:
			filterLayout.resetState();
			initCondition();
			break;
		case R.id.all:
			filterLayout.resetState();
			initCondition();
			if (feedTypeTemp.equals(feedType) && timeTemp == time && tagIdTemp.equals(tagId)) {
				return;
			}
			pageNum = 1;
			List.clear();
			mTravelListViewFooter.setVisibility(View.VISIBLE);
			serialListview.removeAllViews();
			isNeedLoad = true;
			allSuqareList(user.getUserId(), "", "", "0");
			feedTypeTemp = feedType;
			timeTemp = time;
			tagIdTemp = tagId;
			break;
		case R.id.footprint_ok:
//			int size = tagsFlow.getChildCount();
//			isFootPrintEdit = !isFootPrintEdit;
//			if (isFootPrintEdit) {
//				footprintOk.setText("完成");
//				for (int i = 0; i < size; i++) {
//					View view = tagsFlow.getChildAt(i);
//					if (view instanceof TextView)
//						continue;
//					ImageView footprintDel = (ImageView) view.findViewById(R.id.footprint_del);
//					footprintDel.setVisibility(View.VISIBLE);
//				}
//			} else {
//				footprintOk.setText("编辑");
//				for (int i = 0; i < size; i++) {
//					View view = tagsFlow.getChildAt(i);
//					if (view instanceof TextView)
//						continue;
//					ImageView footprintDel = (ImageView) view.findViewById(R.id.footprint_del);
//					footprintDel.setVisibility(View.INVISIBLE);
//				}
//				List<String> strs = new ArrayList<String>();
//				for (TopicOrTag mTag : topicOrTags) {
//					strs.add(mTag.getName());
//				}
//				addFootprint(strs);
//			}
			Intent it = new Intent(TouristHomepageActivity.this, FootprintActivity.class);
			it.putParcelableArrayListExtra("topicOrTags", (ArrayList<TopicOrTag>) topicOrTags);
			startActivityForResult(it, FOOTPRINT);
			break;
		case R.id.footprint_del:
			TopicOrTag tag = (TopicOrTag) v.getTag();
			if (topicOrTags.contains(tag)) {
				topicOrTags.remove(tag);
				tagsFlow.removeView((View) v.getParent());
			}
			footprintNum.setText(Integer.toString(topicOrTags.size()));
			// if (topicOrTags.size() == 0) {
			// tagsFlow.setVisibility(View.GONE);
			// }
			break;
		case R.id.desc_save:
			String desc = descTextView.getText().toString();
			if (StringUtils.isEmpty(desc)) {
				ToastUtil.showToast(getApplicationContext(), "描述不能为空");
				return;
			}
			userInfoEdit(desc, null);
			break;
		case R.id.take_photo:
			// 获取屏幕宽度
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int screenW = metrics.widthPixels;
			View convView = LayoutInflater.from(this).inflate(R.layout.shaitupopuwindow, null);
			int gap = DensityUtil.dip2px(this, 30);
			pw = new PopupWindow(convView, screenW - 2 * gap, LayoutParams.WRAP_CONTENT);
			// 设置pw中的控件可点击
			pw.setFocusable(true);
			// 调用展示方法，设置展示位置
			/*
			 * 在展示pw的同时，让窗口的其余部分显示半透明的颜色
			 */
			WindowManager.LayoutParams params = this.getWindow().getAttributes();
			params.alpha = 0.6f;
			this.getWindow().setAttributes(params);

			// 设置pw可以在点击外部区域时关闭显示
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.setOutsideTouchable(true);

			// 设置pw弹出和隐藏时的动画效果
			/*
			 * 注意：此处的动画效果是通过style样式指定的
			 */
			// pw.setAnimationStyle(R.style.pw_anim_style);

			// 展示对话框，并设置展示位置
			pw.showAtLocation(v, Gravity.CENTER | Gravity.CENTER, 0, 0);
			// 监控pw何时被关闭，并且，在pw被关闭的时候，将窗口的背景色调节回来
			pw.setOnDismissListener(new OnDismissListener() {
				public void onDismiss() {
					// TODO Auto-generated method stub
					// 将窗口颜色调回完全透明
					WindowManager.LayoutParams params = TouristHomepageActivity.this.getWindow().getAttributes();
					params.alpha = 1;
					TouristHomepageActivity.this.getWindow().setAttributes(params);
				}
			});

			// 设置pw中button的点击事件
			TextView photo = (TextView) convView.findViewById(R.id.photo);
			photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent3 = new Intent(TouristHomepageActivity.this, SquarePicPublishedActivity.class);
					intent3.putExtra("tag", "home");
					startActivity(intent3);
					pw.dismiss();
				}
			});
			LinearLayout series = (LinearLayout) convView.findViewById(R.id.series);
			series.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					Intent intent3 = new Intent(TouristHomepageActivity.this, SquareSerialPublishedActivity.class);
					intent3.putExtra("tag", "home");
					startActivity(intent3);
					pw.dismiss();
				}
			});

			break;
		case R.id.personpic:
			CommonUtils.launchPhotoSelectorActivity(this, ADD_BACKGROUND_CODE);
			break;
		case R.id.fans:
			Intent intentFans = new Intent(TouristHomepageActivity.this,FollowOrFansActivity.class);
			intentFans.putExtra("title", "粉丝");
			intentFans.putExtra("userId", user.getUserId());
			startActivity(intentFans);
			activityAnimationOpen();
			break;
		case R.id.follow:
			Intent intentFollow = new Intent(TouristHomepageActivity.this,FollowOrFansActivity.class);
			intentFollow.putExtra("title", "关注");
			intentFollow.putExtra("userId", user.getUserId());
			startActivity(intentFollow);
			activityAnimationOpen();
			break;
		default:
			break;
		}
	}

	private void initCondition() {
		feedType = "all";
		time = 0;
		tagId = "";
	}

	private void addTagFlow(List<TopicOrTag> tagList, FlowLayout flowLayout) {
		if (tagList.size() > 0)
			{
			footprintLayout.setVisibility(View.VISIBLE);
			tagsFlow.setVisibility(View.VISIBLE);
			tagsFlowLine.setVisibility(View.VISIBLE);
			}
		footprintNum.setText(Integer.toString(tagList.size()));
		for (TopicOrTag tag : tagList) {
			LayoutInflater inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.footprint_item, null);

			TextView name = (TextView) view.findViewById(R.id.footprint_name);
			ImageView footprintDel = (ImageView) view.findViewById(R.id.footprint_del);
			footprintDel.setVisibility(View.INVISIBLE);
			name.setText(tag.getName());
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = tagGap;
			params.topMargin = DensityUtil.dip2px(this, 5);
			view.setLayoutParams(params);
			flowLayout.addView(view);
			view.setTag(tag);
			footprintDel.setTag(tag);
			footprintDel.setOnClickListener(this);
		}
	}

	private void initPop() {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(R.layout.filter_pop_window, null);
		// 设置按钮的点击事件
		filterLayout = (FilterLayout) contentView.findViewById(R.id.filterLayout);
		filterLayout.setText1("按类型");
		filterLayout.setText2("按时间");
		filterLayout.setText3("按标签");
		cancel = (TextView) contentView.findViewById(R.id.cancel);
		cancel.setText("重置");
		ok = (TextView) contentView.findViewById(R.id.ok);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		List<String> typeStrs = new ArrayList<String>();
		typeStrs.add("全部");
		typeStrs.add("晒图");
		typeStrs.add("连载");
		filterLayout.setDestinationValue(typeStrs);
		priceStrs.add("三天前");
		priceStrs.add("一个月前");
		priceStrs.add("三个月前");
		priceStrs.add("一年前");
		priceStrs.add("更早");
		filterLayout.setPriceValue(priceStrs);
		for (TopicOrTag tag : tags) {
			daysStrs.add(tag.getName());
		}
		filterLayout.setMutiChoice(false, false, true);
		filterLayout.setDaysValue(daysStrs);
		filterLayout.setOnDaysListener(new OnDaysListener() {

			@Override
			public void OnCheck(String Value, int position, boolean state) {
				// TODO Auto-generated method stub
				// isFilterChange[2][0]=1;
				// isFilterChange[2][1]=position;
				String id = tags.get(position).getId();
				if (state) {
					if (tagId.equals("")) {
						tagId = tags.get(position).getId();
					} else {
						tagId = tagId + "," + id;
					}
				} else {
					int start = tagId.indexOf(id);
					if (start != -1) {
						if (!tagId.startsWith(id)) {
							id = "," + id;
						}
						tagId = tagId.replace(id, "");
					}
				}
			}
		});
		filterLayout.setOnDestinationListener(new OnDestinationListener() {

			@Override
			public void OnCheck(String Value, int position, boolean state) {

				// TODO Auto-generated method stub
				// isFilterChange[0][0]=1;
				// isFilterChange[0][1]=position;
				feedType = feedTypes[position];
			}
		});

		filterLayout.setonPriceListener(new OnPriceListener() {

			@Override
			public void OnCheck(String Value, int position, boolean state) {
				// TODO Auto-generated method stub
				// isFilterChange[1][0]=1;
				// isFilterChange[1][1]=position;
				time = times[position];
			}
		});

		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				backgroundAlpha(1f);
			}
		});
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}

		});
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	private void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	PullListener mPullListener = new PullListener() {
		@Override
		public void onPull(int height) {
			// TODO Auto-generated method stub
			// LogUtil.i(TAG, "mPullListener", "height=" + height);
			float alpha = height * 3 - 200;
			if (alpha > 255) {
				alpha = 255;
			}
			if (alpha < 50) {
				alpha = 0;
			}
			// layout_service_list.setAlpha(alpha / 255f);
			// product_introduce.setAlpha(alpha / 255f);
		}
	};

	private void addOrCancelFocus(boolean flag, final String userId) {
		if (!AppInfo.getIsLogin()) {
			ToastUtil.showToast(getApplicationContext(), "请登录");
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
							mFocusTxt.setText("取消关注");
							Intent intent = new Intent("com.bcinfo.refreshFocus");
							intent.putExtra("userId", userId);
							intent.putExtra("isFocus", true);
							sendBroadcast(intent);
							isFocused = true;
						}
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
						mFocusTxt.setText("+关注");
						Intent intent = new Intent("com.bcinfo.refreshFocus");
						intent.putExtra("userId", userId);
						intent.putExtra("isFocus", false);
						sendBroadcast(intent);
						isFocused = false;
					}
				}
			});

		}
	}

	private void addFootprint(List<String> tagNames) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("tagNames", new JSONArray(tagNames));
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
			HttpUtil.post(Urls.add_footprint, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {

						ToastUtil.showToast(getApplicationContext(), "保存成功");
					} else {
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					super.onFailure(statusCode, headers, responseString, throwable);
					throwable.printStackTrace();
				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 查询足迹
	 */
	private void getFootPrint(String userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pagenum", 1);
		params.put("pagesize", 100);
		HttpUtil.get(Urls.footprint, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				// ToastUtil.showToast(ChatActivity.this, "throwable=" +
				// throwable.getMessage());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				// ToastUtil.showToast(ChatActivity.this, "throwable=" +
				// throwable.getMessage());
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if (code.equals("00000")) {
					JSONArray dataArray = response.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject object = dataArray.optJSONObject(i);
							TopicOrTag tag = new TopicOrTag();
							tag.setId(object.optString("tagId"));
							tag.setName(object.optString("tagName"));
							topicOrTags.add(tag);
						}
						addTagFlow(topicOrTags, tagsFlow);
					}else{
						tagsFlowLine.setVisibility(View.GONE);
						tagsFlow.setVisibility(View.GONE);
					}
					
				}
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case FOOTPRINT:
				tagsFlow.removeAllViews();
				topicOrTags = data.getParcelableArrayListExtra("topicOrTags");
				if (topicOrTags.size() > 0) {
					tagsFlow.setVisibility(View.VISIBLE);
					addTagFlow(topicOrTags, tagsFlow);
					int size = topicOrTags.size();
					if (isFootPrintEdit) {
						for (int i = 0; i < size; i++) {
							View view = tagsFlow.getChildAt(i);
							if (view instanceof TextView)
								continue;
							ImageView footprintDel = (ImageView) view.findViewById(R.id.footprint_del);
							footprintDel.setVisibility(View.VISIBLE);
						}
					} else {
						for (int i = 0; i < size; i++) {
							View view = tagsFlow.getChildAt(i);
							if (view instanceof TextView)
								continue;
							ImageView footprintDel = (ImageView) view.findViewById(R.id.footprint_del);
							footprintDel.setVisibility(View.INVISIBLE);
						}
					}
				}
				break;
			case ADD_BACKGROUND_CODE:
				List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
				if (photos.size() > 0) {
					PhotoModel info = photos.get(0);
					ImageLoader.getInstance().displayImage("file://" + info.getOriginalPath(), mProductHeadImg);
					List<String> uriList = new ArrayList<String>(1);
					uriList.add(info.getOriginalPath());
					uploadPicUtil.uploadPicData(uriList);
				}
				break;
			case 1001:
				String id =data.getStringExtra("id");
				String cover = data.getStringExtra("cover");
				String title = data.getStringExtra("title");
				for (int i = 0; i < List.size(); i++) {
					if (id.equals(List.get(i).getRawData().getPhotoId())) {
						List.get(i).getRawData().setTitle(title);
						// 设置标题
						if (!StringUtils.isEmpty(List.get(i).getRawData().getTitle())) {
							((TextView)serialListview.getChildAt(i).findViewById(R.id.story_title)).setVisibility(View.VISIBLE);
							((TextView)serialListview.getChildAt(i).findViewById(R.id.story_title)).setText(List.get(i).getRawData().getTitle());
						} else {
							((TextView)serialListview.getChildAt(i).findViewById(R.id.story_title)).setVisibility(View.GONE);
						}
						// 设置封面
						if (!StringUtils.isEmpty(cover)) {
							ImageLoader.getInstance().displayImage(Urls.imgHost + cover, (ImageView)serialListview.getChildAt(i).findViewById(R.id.imageall),
									AppConfig.options(R.drawable.ic_launcher));
						} else {
//							((ImageView)serialListview.getChildAt(i).findViewById(R.id.imageall)).setImageResource(R.drawable.ic_launcher);
						}
						return;
					}
				}

				break;
			default:
				break;
			}
		}
	}

	private void querySquareTags() {
		RequestParams params = new RequestParams();
		params.put("mcode", "sale.square.tag");
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONArray dataArray = response.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject object = dataArray.optJSONObject(i).optJSONObject("object");
							;
							TopicOrTag tag = new TopicOrTag();
							tag.setId(object.optString("tagsId"));
							tag.setName(object.optString("tagsName"));
							// tag.setName("#"+object.optString("tagsName")+"#");
							tags.add(tag);
						}
						initPop();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}

	// start
	DataBean dataBean = new DataBean();
	List<Feed_Schema> List = new ArrayList<>();

	protected boolean isComplete;
	private int pageNum = 1;
	private int pageSize = 10;

	private void allSuqareList(String userId, String commentRows, String themeName, String focus) {

		// TODO Auto-generated method stub
		if (!isNeedLoad)
			return;
		if (isLoadmore)
			return;
		isLoadmore = true;
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		params.put("userId", userId);
		params.put("commentRows", commentRows);
		params.put("tagId", tagId);
		params.put("themeName", themeName);
		params.put("focus", focus);
		params.put("feedType", feedType);
		params.put("time", time);
		params.put("pFlag", "true");
		HttpUtil.get(Urls.get_square_list, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				isLoadmore = false;
				allSquare(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				isLoadmore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isLoadmore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);

			}

		});

	}

	protected void allSquare(JSONObject response) {
		// TODO Auto-generated method stub
		if ("00000".equals(response.optString("code"))) {

			JSONArray data1 = response.optJSONArray("data");
			ArrayList<Feed_Schema> feedList = new ArrayList<>();
			if (data1 != null && data1.length() != 0) {
				for (int i = 0; i < data1.length(); i++) {
					JSONObject databean = data1.optJSONObject(i);
					dataBean.setType(databean.optString("type"));
					if ("0".equals(databean.optString("type"))) {
						// 默认
						JSONArray data = databean.optJSONArray("obj");
						if (data != null && data.length() != 0) {
							for (int j = 0; j < data.length(); j++) {
								JSONObject feedObj = data.optJSONObject(j);
								Feed_Schema feed_Schema = new Feed_Schema();
								if (!StringUtils.isEmpty(feedObj.optString("feedType"))
										&& "photo".equals(feedObj.optString("feedType"))) {
									feed_Schema.setDesc(feedObj.optString("desc"));
									feed_Schema.setPublishTime(feedObj.optString("publishTime"));
									feed_Schema.setFeedType(feedObj.optString("feedType"));
									feed_Schema.setIsFocus(feedObj.optString("isFocus"));
									JSONObject publisher = feedObj.optJSONObject("publisher");
									if (publisher != null) {
										feed_Schema.setPublisher(JsonUtil.getSquareUser(publisher));
									}
									JSONObject rawData = feedObj.optJSONObject("rawData");
									SquareTripArticle squareRawData = new SquareTripArticle();
									if (rawData != null) {
										squareRawData.setPhotoId(rawData.optString("id"));// id
										squareRawData.setAccess(rawData.optString("access"));
										squareRawData.setDesc(rawData.optString("desc"));// 标题
										squareRawData.setIsLike(rawData.optString("isLike"));// 是否赞
										squareRawData.setLikes(rawData.optString("likes"));// 赞
										squareRawData.setLocation(rawData.optString("location"));// 位置
										squareRawData.setComments(rawData.optString("comments"));// 评论数
										squareRawData.setViews(rawData.optString("views"));// 浏览数
										squareRawData.setCover(rawData.optString("cover"));// 连载封面
										squareRawData.setTitle(rawData.optString("title"));// 连载封面
										squareRawData.setProductId(rawData.optString("productId"));// 产品Id
										squareRawData.setTitleImg(rawData.optString("titleImg"));// 产品标题图
										squareRawData.setProductTitle(rawData.optString("productTitle"));// 产品标题
										squareRawData.setAbstracts(rawData.optString("abstracts"));// 大游记的摘要
										JSONArray imageList = rawData.optJSONArray("images");
										ArrayList<ImageInfo> squareImageList = new ArrayList<>();
										if (imageList != null) {
											for (int k = 0; k < imageList.length(); k++) {
												ImageInfo imageInfo = new ImageInfo();
												imageInfo.setUrl(imageList.optJSONObject(k).optString("url"));
												imageInfo.setHeight(imageList.optJSONObject(k).optString("height"));
												imageInfo.setWidth(imageList.optJSONObject(k).optString("width"));
												squareImageList.add(imageInfo);
											}
											squareRawData.setPictureList(squareImageList);
										}
										JSONArray tagNames = rawData.optJSONArray("tagNames");
										String tagName = new String();
										ArrayList<String> tag = new ArrayList<>();
										for (int k = 0; k < tagNames.length(); k++) {
											String jsonObject = null;
											try {
												jsonObject = (String) tagNames.get(k);
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											tag.add(jsonObject);
										}
										squareRawData.setTagNames(tag);
										JSONArray commList = rawData.optJSONArray("commentList");
										ArrayList<Comments> comments = new ArrayList<>();
										if (commList != null) {
											for (int k = 0; k < commList.length(); k++) {
												JSONObject jsonObject = commList.optJSONObject(k);
												Comments comment = new Comments();
												comment.setContent(jsonObject.optString("content"));
												comment.setCreateTime(jsonObject.optString("createTime"));
												comment.setId(jsonObject.optString("id"));
												JSONObject jsonUserInfo = jsonObject.optJSONObject("publisher");
												if (jsonUserInfo != null) {
													comment.setUser(JsonUtil.getUserInfo(jsonUserInfo));
												}
												comments.add(comment);
											}
										}
										squareRawData.setCommentList(comments);
										feed_Schema.setRawData(squareRawData);
										feedList.add(feed_Schema);
									}
								} else if (!StringUtils.isEmpty(feedObj.optString("feedType"))
										&& "series".equals(feedObj.optString("feedType"))) {
									feed_Schema.setDesc(feedObj.optString("desc"));
									feed_Schema.setPublishTime(feedObj.optString("publishTime"));
									feed_Schema.setFeedType(feedObj.optString("feedType"));
									feed_Schema.setIsFocus(feedObj.optString("isFocus"));
									JSONObject publisher = feedObj.optJSONObject("publisher");
									if (publisher != null) {
										feed_Schema.setPublisher(JsonUtil.getSquareUser(publisher));
									}
									JSONObject rawData = feedObj.optJSONObject("rawData");
									SquareTripArticle squareRawData = new SquareTripArticle();
									if (rawData != null) {
										squareRawData.setPhotoId(rawData.optString("id"));// id
										squareRawData.setAccess(rawData.optString("access"));// id
										squareRawData.setDesc(rawData.optString("desc"));// 标题
										squareRawData.setIsLike(rawData.optString("isLike"));// 是否赞
										squareRawData.setLikes(rawData.optString("likes"));// 赞
										squareRawData.setLocation(rawData.optString("location"));// 位置
										squareRawData.setComments(rawData.optString("comments"));// 评论数
										squareRawData.setViews(rawData.optString("views"));// 浏览数
										squareRawData.setCover(rawData.optString("cover"));// 连载封面
										squareRawData.setTitle(rawData.optString("title"));// 连载封面
										squareRawData.setProductId(rawData.optString("productId"));// 产品Id
										squareRawData.setTitleImg(rawData.optString("titleImg"));// 产品标题图
										squareRawData.setProductTitle(rawData.optString("productTitle"));// 产品标题
										squareRawData.setAbstracts(rawData.optString("abstracts"));// 大游记的摘要
										JSONArray imageList = rawData.optJSONArray("images");
										ArrayList<ImageInfo> squareImageList = new ArrayList<>();
										if (imageList != null) {
											for (int k = 0; k < imageList.length(); k++) {
												ImageInfo imageInfo = new ImageInfo();
												imageInfo.setUrl(imageList.optJSONObject(k).optString("url"));
												imageInfo.setHeight(imageList.optJSONObject(k).optString("height"));
												imageInfo.setWidth(imageList.optJSONObject(k).optString("width"));
												squareImageList.add(imageInfo);
											}
											squareRawData.setPictureList(squareImageList);
										}
										JSONArray tagNames = rawData.optJSONArray("tagNames");
										String tagName = new String();
										ArrayList<String> tag = new ArrayList<>();
										for (int k = 0; k < tagNames.length(); k++) {
											String jsonObject = null;
											try {
												jsonObject = (String) tagNames.get(k);
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											tag.add(jsonObject);
										}
										squareRawData.setTagNames(tag);
										JSONArray commList = rawData.optJSONArray("commentList");
										ArrayList<Comments> comments = new ArrayList<>();
										if (commList != null) {
											for (int k = 0; k < commList.length(); k++) {
												JSONObject jsonObject = commList.optJSONObject(k);
												Comments comment = new Comments();
												comment.setContent(jsonObject.optString("content"));
												comment.setCreateTime(jsonObject.optString("createTime"));
												comment.setId(jsonObject.optString("id"));
												JSONObject jsonUserInfo = jsonObject.optJSONObject("publisher");
												if (jsonUserInfo != null) {
													comment.setUser(JsonUtil.getUserInfo(jsonUserInfo));
												}
												comments.add(comment);
											}
										}
										squareRawData.setCommentList(comments);
									}
									feed_Schema.setRawData(squareRawData);
									feedList.add(feed_Schema);
								}

							}
						}
					}

				}

				for (int i = 0; i < feedList.size(); i++) {
					if ("photo".equals(feedList.get(i).getFeedType())
							|| "series".equals(feedList.get(i).getFeedType())) {

						List.add(feedList.get(i));

					}
				}
				if (List.size() > 0 && List.size() % pageSize == 0) {
					mTravelListViewFooter.setVisibility(View.VISIBLE);
					isNeedLoad = true;
					++pageNum;
				} else {
					mTravelListViewFooter.setVisibility(View.GONE);
					isNeedLoad = false;
				}
				if(List.size()==0){
					picNoContentLv.setVisibility(View.VISIBLE);
					picContentLl.setVisibility(View.GONE);
				}
				for (int i = List.size()-feedList.size(); i < List.size(); i++) {
					addItem(i, serialListview);
				}
			} else {

			}
		} else {

		}
		// 上拉 下拉的初始状态置为false;

	}

	private void addItem(final int position, LinearLayout serialListview) {
		final Feed_Schema feed_Schema = List.get(position);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.my_squer_item, null);
		ImageView product_servicer_icon_iv = (ImageView) view.findViewById(R.id.product_servicer_icon_iv);
		ImageView imageall = (ImageView) view.findViewById(R.id.imageall);
		TextView tvPost = (TextView) view.findViewById(R.id.tvPost);
		TextView tvName = (TextView) view.findViewById(R.id.tvName);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		TextView access = (TextView) view.findViewById(R.id.access);
		TextView map_location_text = (TextView) view.findViewById(R.id.map_location_text);
		final TextView tv_zannum = (TextView) view.findViewById(R.id.tv_zannum);
		TextView tv_reviewnum = (TextView) view.findViewById(R.id.tv_reviewnum);
		TextView tv_pv = (TextView) view.findViewById(R.id.tv_pv);
		MyListView review_listview = (MyListView) view.findViewById(R.id.review_listview);
		FlowLayout tags = (FlowLayout) view.findViewById(R.id.tags);
		ImageView image = (ImageView) view.findViewById(R.id.image);
		final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
		ImageView guanzhu_pic = (ImageView) view.findViewById(R.id.guanzhu_pic);
		View line = (View) view.findViewById(R.id.line);
		View view1 = (View) view.findViewById(R.id.view);
		ImageView superStar = (ImageView) view.findViewById(R.id.starperson);
		TextView story_title = (TextView) view.findViewById(R.id.story_title);
		TextView tv_picnum = (TextView) view.findViewById(R.id.tv_picnum);
		LinearLayout relative = (LinearLayout) view.findViewById(R.id.relative);
		RelativeLayout reviewRelative = (RelativeLayout) view.findViewById(R.id.reviewRelative);
		RelativeLayout zanRelative = (RelativeLayout) view.findViewById(R.id.zanrelative);
		RelativeLayout imageRelative = (RelativeLayout) view.findViewById(R.id.imageRelative);
		LinearLayout addressRelative = (LinearLayout) view.findViewById(R.id.addressRelative);
		// 统一图片尺寸
		WindowManager wm = (WindowManager) TouristHomepageActivity.this.getSystemService(Context.WINDOW_SERVICE);
		float width = wm.getDefaultDisplay().getWidth();
		final ViewGroup.LayoutParams lp = imageRelative.getLayoutParams();
		lp.width = picWidth;
		lp.height = picHight;
		if ("photo".equals(feed_Schema.getFeedType())) {
			image.setVisibility(View.GONE);
			relative.setVisibility(View.VISIBLE);
			if (isSelf == true) {
				guanzhu_pic.setVisibility(View.VISIBLE);
				access.setVisibility(View.VISIBLE);
			} else {
				guanzhu_pic.setVisibility(View.GONE);
				access.setVisibility(View.GONE);
			}
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getAccess())) {
				if ("all".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("公开");
				} else if ("friends".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("好友可见");
				} else if ("private".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("自己可见");
				}
			}
			((View) guanzhu_pic.getParent()).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					showPopu(position, feed_Schema, v, view, 0);
				}
			});
			// 点赞的点击
			zanRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(TouristHomepageActivity.this, LoginActivity.class);
						((Activity) TouristHomepageActivity.this).startActivity(it);
						return;
					}
					// if
					// (!feed_Schema.getPublisher().getUserId().equals(PreferenceUtil.getUserId()))
					// {
					if (feed_Schema.getRawData().getIsLike().equals("0")
							|| StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "tripstory", 1, position);
						feed_Schema.getRawData().setIsLike("1");
						feed_Schema.getRawData()
								.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) + 1));
					} else if ((feed_Schema.getRawData().getIsLike().equals("1"))) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "tripstory", 0, position);
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							feed_Schema.getRawData()
									.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) - 1));
						}

						feed_Schema.getRawData().setIsLike("0");
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						tv_zannum.setText(feed_Schema.getRawData().getLikes());
						image1.setBackgroundResource(R.drawable.nozan);
					} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						image1.setBackgroundResource(R.drawable.zan2x);
						tv_zannum.setText(feed_Schema.getRawData().getLikes());

					}
				}
				// }
			});

			if (!StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
				if (feed_Schema.getRawData().getIsLike().equals("0")) {
					image1.setBackgroundResource(R.drawable.nozan);
				} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
					image1.setBackgroundResource(R.drawable.zan2x);
				}
			}
			if (!StringUtils.isEmpty(feed_Schema.getDesc())) {
				tvPost.setText(feed_Schema.getDesc());
			}
			// 达人图标
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getIsTalent())) {
				if ("1".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.VISIBLE);
				} else if ("0".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.GONE);
				}
			}
			// 评论列表点击
			reviewRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(TouristHomepageActivity.this, LoginActivity.class);
						((Activity) TouristHomepageActivity.this).startActivity(it);
						return;
					}
					Intent reviewintent = new Intent(TouristHomepageActivity.this, UserMicroCommentActivity.class);
					reviewintent.putExtra("objectType", "0");
					reviewintent.putExtra("microId", feed_Schema.getRawData().getPhotoId());
					String counts = StringUtils.isEmpty(feed_Schema.getRawData().getComments()) ? "0"
							: feed_Schema.getRawData().getComments();
					if (counts.equals("0")) {
						reviewintent.putExtra("count", 0);
					} else {
						reviewintent.putExtra("count", Integer.parseInt(counts));
					}
					((Activity) TouristHomepageActivity.this).startActivityForResult(reviewintent, 3);
					((Activity) TouristHomepageActivity.this).overridePendingTransition(R.anim.activity_new,
							R.anim.activity_out);
				}
			});
			// 设置晒图人头像
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getPublisher().getAvatar(),
						product_servicer_icon_iv, AppConfig.options(R.drawable.user_defult_photo));
			} else {
				product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// toClub(feed_Schema);
				}
			});
			// 设置发表的人
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getNickname())) {
				tvName.setText(feed_Schema.getPublisher().getNickname());
			} else if (!StringUtils.isEmpty(feed_Schema.getPublisher().getRealName())) {
				tvName.setText(feed_Schema.getPublisher().getRealName());
			} else {
				tvName.setText("");
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getDesc())) {
				story_title.setVisibility(View.VISIBLE);
				List<RichText> richTexts = StringUtils.xmlToRichText(feed_Schema.getRawData().getDesc());
				StringUtils.initRichTextView1(story_title, richTexts);
			} else {
				story_title.setVisibility(View.GONE);
			}
			setTime(tvTime, feed_Schema);
			// 设置封面
			// imageall.setLayoutParams(lp);
			// view1.setLayoutParams(lp);
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
			imageRelative.setLayoutParams(lp);
			if (feed_Schema.getRawData().getPictureList().size() > 0) {
				if (!StringUtils.isEmpty(feed_Schema.getRawData().getPictureList().get(0).getUrl())) {
					ImageLoader.getInstance().displayImage(
							Urls.imgHost + feed_Schema.getRawData().getPictureList().get(0).getUrl(), imageall,
							AppConfig.options(R.drawable.ic_launcher));
				}
				// 设置图片数量
				int num = feed_Schema.getRawData().getPictureList().size();
				if (num > 0) {
					tv_picnum.setText(String.valueOf(num));
				}
			} else {
				imageall.setImageResource(R.drawable.ic_launcher);
			}
			imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(TouristHomepageActivity.this, SquarePicDetailActivity.class);
					intent.putExtra("id", feed_Schema.getRawData().getPhotoId());
					TouristHomepageActivity.this.startActivity(intent);
					((Activity) TouristHomepageActivity.this).overridePendingTransition(R.anim.activity_new,
							R.anim.activity_out);
				}
			});
			// 发布地址
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLocation())
					&& !feed_Schema.getRawData().getLocation().equals("不显示")) {
				addressRelative.setVisibility(View.VISIBLE);
				map_location_text.setText(feed_Schema.getRawData().getLocation());
			} else {
				addressRelative.setVisibility(View.GONE);
			}
			String userId = PreferenceUtil.getUserId();

			// 赞的数目
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLikes())) {
				if ("0".equals(feed_Schema.getRawData().getLikes())) {
					tv_zannum.setVisibility(View.GONE);
				} else {
					tv_zannum.setVisibility(View.VISIBLE);
					tv_zannum.setText(feed_Schema.getRawData().getLikes());
				}
			}
			// 评论数目
			tv_reviewnum.setText(StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0") ? ""
							: feed_Schema.getRawData().getComments());
			if (StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0")) {
				line.setVisibility(View.GONE);
				review_listview.setVisibility(View.GONE);
			} else {
				review_listview.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
			}
			// 浏览人数
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getViews())) {
				if ("0".equals(feed_Schema.getRawData().getViews())) {
					tv_pv.setText("");
				} else {
					tv_pv.setText("浏览" + feed_Schema.getRawData().getViews());
				}
			}
			// 评论列表
			showReview(review_listview, feed_Schema);
			// 标签
			if (feed_Schema.getRawData().getTagNames().size() > 0) {
				addFlowView(feed_Schema.getRawData().getTagNames(), tags);
			}
			serialListview.addView(view);
			return;
		}

		if ("series".equals(feed_Schema.getFeedType())) {
			if (isSelf == true) {
				access.setVisibility(View.VISIBLE);
				guanzhu_pic.setVisibility(View.VISIBLE);
			} else {
				access.setVisibility(View.GONE);
				guanzhu_pic.setVisibility(View.GONE);
			}
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getAccess())) {
				if ("all".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("公开");
				} else if ("friends".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("好友可见");
				} else if ("private".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("自己可见");
				}
			}
			((View) guanzhu_pic.getParent()).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					showPopu(position, feed_Schema, v, view, 1);

				}
			});
			relative.setVisibility(View.GONE);
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
				if (feed_Schema.getRawData().getIsLike().equals("0")) {
					image1.setBackgroundResource(R.drawable.nozan);
				} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
					image1.setBackgroundResource(R.drawable.zan2x);
				}
			}

			// 点赞的点击
			zanRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(TouristHomepageActivity.this, LoginActivity.class);
						((Activity) TouristHomepageActivity.this).startActivity(it);
						return;
					}
					// if
					// (!feed_Schema.getPublisher().getUserId().equals(PreferenceUtil.getUserId()))
					// {
					if (feed_Schema.getRawData().getIsLike().equals("0")
							|| StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "series", 1, position);
						feed_Schema.getRawData().setIsLike("1");
						feed_Schema.getRawData()
								.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) + 1));
					} else if ((feed_Schema.getRawData().getIsLike().equals("1"))) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "series", 0, position);
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							feed_Schema.getRawData()
									.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) - 1));
						}

						feed_Schema.getRawData().setIsLike("0");
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						tv_zannum.setText(feed_Schema.getRawData().getLikes());
						image1.setBackgroundResource(R.drawable.nozan);
					} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						image1.setBackgroundResource(R.drawable.zan2x);
						tv_zannum.setText(feed_Schema.getRawData().getLikes());

					}
					// }
				}
			});

			if (!StringUtils.isEmpty(feed_Schema.getDesc())) {
				tvPost.setText(feed_Schema.getDesc());
			}
			// 评论点击事件
			reviewRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(TouristHomepageActivity.this, LoginActivity.class);
						((Activity) TouristHomepageActivity.this).startActivity(it);
						return;
					}
					Intent reviewintent = new Intent(TouristHomepageActivity.this, UserMicroCommentActivity.class);
					reviewintent.putExtra("objectType", "1");
					reviewintent.putExtra("microId", feed_Schema.getRawData().getPhotoId());
					String counts = StringUtils.isEmpty(feed_Schema.getRawData().getComments()) ? "0"
							: feed_Schema.getRawData().getComments();
					if (counts.equals("0")) {
						reviewintent.putExtra("count", 0);
					} else {
						reviewintent.putExtra("count", Integer.parseInt(counts));
					}
					((Activity) TouristHomepageActivity.this).startActivityForResult(reviewintent, 3);
					((Activity) TouristHomepageActivity.this).overridePendingTransition(R.anim.activity_new,
							R.anim.activity_out);
				}
			});

			// 地点
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLocation())
					&& !feed_Schema.getRawData().getLocation().equals("不显示")) {
				addressRelative.setVisibility(View.VISIBLE);
				map_location_text.setText(feed_Schema.getRawData().getLocation());
			} else {
				addressRelative.setVisibility(View.GONE);
			}

			// 达人图标
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getIsTalent())) {
				if ("1".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.VISIBLE);
				} else if ("0".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.GONE);
				}
			}
			// 设置晒图人头像
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getPublisher().getAvatar(),
						product_servicer_icon_iv, AppConfig.options(R.drawable.user_defult_photo));
			} else {
				product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// toClub(feed_Schema);
				}
			});
			// 设置发表的人
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getNickname())) {
				tvName.setText(feed_Schema.getPublisher().getNickname());
			} else if (!StringUtils.isEmpty(feed_Schema.getPublisher().getRealName())) {
				tvName.setText(feed_Schema.getPublisher().getRealName());
			} else {
				tvName.setText("");
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getTitle())) {
				story_title.setVisibility(View.VISIBLE);
				story_title.setText(feed_Schema.getRawData().getTitle());
			} else {
				story_title.setVisibility(View.GONE);
			}
			// 设置发送时间
			setTime(tvTime, feed_Schema);
			// 设置封面

			imageRelative.setLayoutParams(lp);
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getCover())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getRawData().getCover(),
						imageall, AppConfig.options(R.drawable.ic_launcher),new ImageLoadingListener() {
							
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
//								ViewGroup.LayoutParams lp =view.getLayoutParams();
								if(h>0&&w>0){
									float ratio=h/w;
									if(ratio<picRatio){
										lp.height=(int) (picWidth/w*h);
										lp.width=picWidth;
										
									}
								}
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								
							}
						});
			} else {
				imageall.setImageResource(R.drawable.ic_launcher);
			}
			imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(TouristHomepageActivity.this, SquareSerialDetailActivity.class);
					intent.putExtra("id", feed_Schema.getRawData().getPhotoId());
					TouristHomepageActivity.this.startActivity(intent);
					((Activity) TouristHomepageActivity.this).overridePendingTransition(R.anim.activity_new,
							R.anim.activity_out);
				}
			});

			// 赞的数目
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLikes())) {
				if ("0".equals(feed_Schema.getRawData().getLikes())) {
					tv_zannum.setVisibility(View.GONE);
				} else {
					tv_zannum.setVisibility(View.VISIBLE);
					tv_zannum.setText(feed_Schema.getRawData().getLikes());
				}
			}

			// 评论数目
			tv_reviewnum.setText(StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0") ? ""
							: feed_Schema.getRawData().getComments());
			if (StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0")) {
				line.setVisibility(View.GONE);
				review_listview.setVisibility(View.GONE);
			} else {
				review_listview.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
			}
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getViews())) {
				if ("0".equals(feed_Schema.getRawData().getViews())) {
					tv_pv.setText("");
				} else {
					tv_pv.setText("浏览" + feed_Schema.getRawData().getViews());
				}
			}
			// 评论列表
			showReview(review_listview, feed_Schema);
			if (feed_Schema.getRawData().getTagNames().size() > 0) {
				addFlowView(feed_Schema.getRawData().getTagNames(), tags);
			}
			serialListview.addView(view);
			return;
		}

	}

	// 设置时间
	private void setTime(TextView tvTime, final Feed_Schema feed_Schema) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			long time = (date.getTime() - format.parse(feed_Schema.getPublishTime()).getTime()) / 1000;
			long day1 = time / (24 * 3600);
			long hour1 = time % (24 * 3600) / 3600;
			long minute1 = time % 3600 / 60;
			if (day1 > 0) {
				tvTime.setText(DateUtil.getFormateDate1(feed_Schema.getPublishTime()));
			} else if (hour1 > 0) {
				tvTime.setText(hour1 + "小时前");
			} else if (minute1 > 0) {
				tvTime.setText(minute1 + "分钟前");
			} else {
				if (time <= 0) {
					tvTime.setText("刚刚");
				} else {
					tvTime.setText(time + "秒前");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 评论列表
	 */
	private void showReview(ListView review_listview, final Feed_Schema feed_Schema) {
		MicroBlogsCommentsAdapter adapter1 = (MicroBlogsCommentsAdapter) review_listview.getAdapter();
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
			review_listview.setAdapter(new MicroBlogsCommentsAdapter(TouristHomepageActivity.this, commentsList));
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

	@SuppressLint("ResourceAsColor")
	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		flowLayout.removeAllViews();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(TouristHomepageActivity.this);
			newView.setBackgroundResource(R.drawable.shape_club_tag2);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#FFFFFF"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(TouristHomepageActivity.this, 1);
			params.bottomMargin = DensityUtil.dip2px(TouristHomepageActivity.this, 1);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unRegisterBoradcastReceiver();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.delPhoto");
		myIntentFilter.addAction("com.bcinfo.refreshCommentsCount");
		myIntentFilter.addAction("com.bcinfo.refreshLikesCount");
		myIntentFilter.addAction("com.bcinfo.publishBlog2");
		myIntentFilter.addAction("com.bcinfo.refreshFocus");
		myIntentFilter.addAction("com.bcinfo.squre");
		myIntentFilter.addAction("com.bcinfo.haveLogin");

		// 注册广播
		TouristHomepageActivity.this.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void unRegisterBoradcastReceiver() {
		TouristHomepageActivity.this.unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.bcinfo.refreshCommentsCount")) {
				String count = intent.getStringExtra("count");
				String microId = intent.getStringExtra("microId");
				ArrayList<Comments> comments = intent.getParcelableArrayListExtra("comments");
				if (count != null && microId != null) {
					for (int i = 0; i < List.size(); i++) {
						if (List.get(i).getRawData() != null
								&& !StringUtils.isEmpty(List.get(i).getRawData().getPhotoId())) {
							if (List.get(i).getRawData().getPhotoId().equals(microId)) {
								List.get(i).getRawData().setComments(count);
								List.get(i).getRawData().setCommentList(comments);
								// 评论数目
								((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_reviewnum))
										.setText(StringUtils.isEmpty(List.get(i).getRawData().getComments())
												|| List.get(i).getRawData().getComments().equals("0") ? ""
														: List.get(i).getRawData().getComments());
								if (StringUtils.isEmpty(List.get(i).getRawData().getComments())
										|| List.get(i).getRawData().getComments().equals("0")) {
									((View) serialListview.getChildAt(i).findViewById(R.id.line))
											.setVisibility(View.GONE);
									((MyListView) serialListview.getChildAt(i).findViewById(R.id.review_listview))
											.setVisibility(View.GONE);
								} else {
									((MyListView) serialListview.getChildAt(i).findViewById(R.id.review_listview))
											.setVisibility(View.VISIBLE);
									((View) serialListview.getChildAt(i).findViewById(R.id.line))
											.setVisibility(View.VISIBLE);
								}

								// 评论列表
								showReview(
										((MyListView) serialListview.getChildAt(i).findViewById(R.id.review_listview)),
										List.get(i));
								break;
							}
						}
					}
				}
			} else if ("com.bcinfo.refreshLikesCount".equals(action)) {
				int like = intent.getIntExtra("like", 0);
				String microId = intent.getStringExtra("microId");
				if (microId != null) {
					for (int i = 0; i < List.size(); i++) {
						if (List.get(i).getRawData() != null) {
							if (!StringUtils.isEmpty(List.get(i).getRawData().getPhotoId())) {
								if (microId.equals(List.get(i).getRawData().getPhotoId())) {
									String isLike = List.get(i).getRawData().getIsLike();
									int likes = 0;
									if (!StringUtils.isEmpty(List.get(i).getRawData().getLikes())) {
										likes = Integer.parseInt(List.get(i).getRawData().getLikes());
									}
									if (isLike.equals("0") && like == 1) {
										List.get(i).getRawData().setIsLike("1");
										List.get(i).getRawData().setLikes(Integer.toString(likes + 1));
										if ("0".equals(List.get(i).getRawData().getLikes())) {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.GONE);
										} else {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.VISIBLE);
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setText(List.get(i).getRawData().getLikes());
										}
										serialListview.getChildAt(i).findViewById(R.id.image1)
												.setBackgroundResource(R.drawable.zan2x);
									} else if (isLike.equals("1") && like == 0) {
										List.get(i).getRawData().setIsLike("0");
										List.get(i).getRawData().setLikes(Integer.toString(likes - 1));
										if ("0".equals(List.get(i).getRawData().getLikes())) {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.GONE);
										} else {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.VISIBLE);
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setText(List.get(i).getRawData().getLikes());
										}
										serialListview.getChildAt(i).findViewById(R.id.image1)
												.setBackgroundResource(R.drawable.nozan);
									}

									break;
								}
							}
						}
					}
				}

			} else if ("com.bcinfo.refreshFocus".equals(action)) {
				String userId = intent.getStringExtra("userId");
				boolean isFocus = intent.getBooleanExtra("isFocus", false);
				if (userId != null) {
					if (isFocus) {
						mFocusTxt.setText("取消关注");
						isFocused = true;
					}
					if (!isFocus) {
						mFocusTxt.setText("+关注");
						isFocused = false;
					}
				}

			}else if ("com.bcinfo.publishBlog2".equals(action)) {
				pageNum = 1;
				List.clear();
				mTravelListViewFooter.setVisibility(View.VISIBLE);
				serialListview.removeAllViews();
				isNeedLoad = true;
				allSuqareList(user.getUserId(), "", "", "0");
			}

		};
	};
	private PopupWindow pw;

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
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					isComplete = true;
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					if (response.optString("code").equals("00000")) {
						if (like == 1) {
							Toast.makeText(TouristHomepageActivity.this, "点赞 +1", Toast.LENGTH_SHORT).show();
						}
						Intent it = new Intent("com.bcinfo.refreshLikesCount");
						it.putExtra("microId", id);
						it.putExtra("like", like);
						TouristHomepageActivity.this.sendBroadcast(it);
						isComplete = true;
					}
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

	/*
	 * 弹出取消关注
	 */
	private void showPopu(final int position, final Feed_Schema feed_Schema, final View view, final View Layview,
			final int tag) {
		Animation anim = AnimationUtils.loadAnimation(TouristHomepageActivity.this, R.anim.hintpw);
		Animation anim2 = AnimationUtils.loadAnimation(TouristHomepageActivity.this, R.anim.hintpw2);
		anim.setFillAfter(true);
		view.startAnimation(anim);
		// 获取屏幕宽度
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) TouristHomepageActivity.this).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenW = metrics.widthPixels;
		View convView = LayoutInflater.from(TouristHomepageActivity.this)
				.inflate(R.layout.squarecannelguanzhupopuwindow2, null);
		pw = new PopupWindow(convView, screenW, LayoutParams.WRAP_CONTENT);
		// 设置pw中的控件可点击
		pw.setFocusable(true);
		// 调用展示方法，设置展示位置
		/*
		 * 在展示pw的同时，让窗口的其余部分显示半透明的颜色
		 */
		WindowManager.LayoutParams params = ((Activity) TouristHomepageActivity.this).getWindow().getAttributes();
		params.alpha = 0.6f;
		((Activity) TouristHomepageActivity.this).getWindow().setAttributes(params);

		// 设置pw可以在点击外部区域时关闭显示
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);

		// 设置pw弹出和隐藏时的动画效果
		/*
		 * 注意：此处的动画效果是通过style样式指定的
		 */
		pw.setAnimationStyle(R.style.pw_anim_style2);

		// 展示对话框，并设置展示位置

		// 监控pw何时被关闭，并且，在pw被关闭的时候，将窗口的背景色调节回来
		pw.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// TODO Auto-generated method stub
				// 将窗口颜色调回完全透明
				WindowManager.LayoutParams params = ((Activity) TouristHomepageActivity.this).getWindow()
						.getAttributes();
				params.alpha = 1;
				((Activity) TouristHomepageActivity.this).getWindow().setAttributes(params);
				Animation anim2 = AnimationUtils.loadAnimation(TouristHomepageActivity.this, R.anim.hintpw2);
				view.startAnimation(anim2);
				anim2.setFillAfter(true);
			}
		});
		pw.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		// 设置pw中button的点击事件

		TextView dele = (TextView) convView.findViewById(R.id.dele);
		TextView edit = (TextView) convView.findViewById(R.id.edit);
		TextView cannel = (TextView) convView.findViewById(R.id.cannel);
		View line = (View) convView.findViewById(R.id.view);
		/*
		 * tag==0为晒图 tag==1为连载
		 * 
		 */
		if (tag == 0) {
			edit.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		} else {
			line.setVisibility(View.VISIBLE);
			edit.setVisibility(View.VISIBLE);
		}
		dele.setOnClickListener(new OnClickListener() {
			Animation anim2 = AnimationUtils.loadAnimation(TouristHomepageActivity.this, R.anim.hintpw2);

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				if (tag == 0) {
					delPic(feed_Schema.getRawData().getPhotoId(), Layview);
				} else {
					delSeries(feed_Schema.getRawData().getPhotoId(), Layview);
				}
			
			}
		});
		edit.setOnClickListener(new OnClickListener() {
			Animation anim2 = AnimationUtils.loadAnimation(TouristHomepageActivity.this, R.anim.hintpw2);

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				Intent it = new Intent();
				it.putExtra("id", feed_Schema.getRawData().getPhotoId());
				it.putExtra("tag", "edit");
				it.putExtra("tag2", "old");
				it.setClass(TouristHomepageActivity.this, SquareSerialPreview1Activity.class);
				TouristHomepageActivity.this.startActivityForResult(it, 1001);
			}
		});
		cannel.setOnClickListener(new OnClickListener() {
			Animation anim2 = AnimationUtils.loadAnimation(TouristHomepageActivity.this, R.anim.hintpw2);

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();

			}
		});
	}

	/*
	 * 删除晒图
	 */
	private void delPic(final String picId, final View layview) {
		HttpUtil.delete(Urls.tripStory_delete_url + picId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					Toast.makeText(getApplicationContext(), "删除成功", 100).show();
					serialListview.removeView(layview);
					Intent intent = new Intent("com.bcinfo.delPhoto");
					sendBroadcast(intent);
					if(serialListview.getChildCount()==0){
						pageNum = 1;
						List.clear();
						mTravelListViewFooter.setVisibility(View.VISIBLE);
						serialListview.removeAllViews();
						isNeedLoad = true;
						allSuqareList(user.getUserId(), "", "", "0");
					}
				} else {
					Toast.makeText(getApplicationContext(), "删除失败", 100).show();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Toast.makeText(getApplicationContext(), "删除失败", 100).show();
			}
		});
	}

	/*
	 * 删除连载
	 */
	private void delSeries(final String picId, final View layview) {
		HttpUtil.delete(Urls.del_series + picId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					Toast.makeText(getApplicationContext(), "删除成功", 100).show();
					serialListview.removeView(layview);
					Intent intent = new Intent("com.bcinfo.delPhoto");
					sendBroadcast(intent);
					if(serialListview.getChildCount()==0){
						pageNum = 1;
						List.clear();
						mTravelListViewFooter.setVisibility(View.VISIBLE);
						serialListview.removeAllViews();
						isNeedLoad = true;
						allSuqareList(user.getUserId(), "", "", "0");
					}

				} else {
					Toast.makeText(getApplicationContext(), "删除失败", 100).show();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Toast.makeText(getApplicationContext(), "删除失败", 100).show();
			}
		});
	}
	// end

	/**
	 * 查询个人资料
	 * 
	 * @param userId
	 */
	private void queryUserInfo(String userId) {
		HttpUtil.get(Urls.personal_url + userId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONObject data = response.optJSONObject("data");
					if (data != null) {
						JSONObject userObj = data.optJSONObject("userInfo");
						if (userObj != null && userObj.length() > 0) {
							user = JsonUtil.getUserInfo(userObj);
							setData();
						}

					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/**
	 * 修改个人信息接口
	 */
	private void userInfoEdit(String brief, String background) {
		try {
			JSONObject jsonObject = new JSONObject();
			if (brief != null)
				jsonObject.put("brief", brief);
			if (background != null)
				jsonObject.put("background", background);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.userinfo_edit_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					// ToastUtil.showToast(ModifyInfoActivity.this, "修改失败
					// errorMessage=" + throwable.getMessage());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					if (response.optString("code").equals("00000")) {
						ToastUtil.showToast(TouristHomepageActivity.this, "修改成功");
						// UserInfo
						// userInfo=UserInfoDB.getInstance().queryUserInfoById(user.getUserId());
						// userInfo.setIntroduction(introduction);
						// UserInfoDB.getInstance().insertData(userInfo);
						// Intent intent = new
						// Intent("com.bcinfo.modifyUserInfo");
						// sendBroadcast(intent);
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (alpha != -1) {
			layout_product_detail_title.getBackground().setAlpha(alpha);
		}
		registerBoradcastReceiver();
	}

}
