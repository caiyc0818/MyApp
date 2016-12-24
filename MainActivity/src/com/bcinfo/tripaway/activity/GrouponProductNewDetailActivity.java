package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductTripPlanListAdapter;
import com.bcinfo.tripaway.adapter.UserCommentSecListAdapter;
import com.bcinfo.tripaway.adapter.UserCommentSecListAdapter.ReplyInterface;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.FeatureInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.TipsDetailInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView.PullListener;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView1;
import com.bcinfo.tripaway.view.dialog.ReplyDialog;
import com.bcinfo.tripaway.view.dialog.ReplyDialog.OnSendReplyListener;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.slidebuttompanel.SlideBottomPanel;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wefika.flowlayout.FlowLayout;

public class GrouponProductNewDetailActivity extends BaseActivity
		implements PersonalScrollViewListener, OnClickListener {
	private SlideBottomPanel mPanel;

	private String productId;

	private String TAG = "GroupProductNewestActivity";

	private ProductNewInfo productInfo;

	private RelativeLayout featLayout;

	private String score;

	private TextView levelTxt;

	private String total;

	private List<AvailableTime> experiodList = new ArrayList<AvailableTime>();

	private LinearLayout mEvaluteProductLayout;

	private LinearLayout mJoinMebLayout;

	private ProductTripPlanListAdapter mTripPlanListAdapter;

	private TextView tour_dates;

	private ListView trip_detail_list;

	public ArrayList<ImageInfo> productAllImgList = new ArrayList<ImageInfo>();

	private final ArrayList<Journey> mJourneyList = new ArrayList<Journey>();

	/**
	 * 是否收藏
	 */
	private boolean isFavedFlag = false;
	private ImageView mStorePicIv;
	private TextView mStoreTxt;

	private LinearLayout mReferLayout;

	private RoundImageView mReferAuthorPhoto;

	private TextView mReferNameTv;

	private TextView mReferContentTv;

	private TextView mReplyAutor;

	private LinearLayout mProductJoinMebLayout;

	private TextView mProductJoinMebCountTv;

	private ImageView mebMore;

	private LinearLayout inGroupData;

	private List<String> endTime;

	private List<String> startTime;

	// private LinearLayout warmprompt_lyatyout;

	private RelativeLayout productDetailLayout;

	// private RelativeLayout price_tip;
	// 评价按钮
	private Button mEvaBtn;

	private String commentId;

	// 评论总数
	private TextView mComCounts;

	private ListView evalute_reply_lv;

	private ArrayList<Replys> replys = new ArrayList<Replys>();

	private UserCommentSecListAdapter mUserCommentSecListAdapter;

	/*
	 * 相似产品列表
	 */
	private ListView mSimillarListView;

	/*
	 * 相似产品列表适配器
	 */
	private ProductAdapter mSimillarListAdapter;

	private final ArrayList<ProductNewInfo> mSimillarArrayList = new ArrayList<ProductNewInfo>();// 产品集合

	private FlowLayout layout_service_list;

	private TextView detail_product_title;

	private RoundImageView product_author_photo;

	private ProductDetailScrollView mProductScrollView;

	private ImageView imgCccc;

	private LinearLayout mFeatureLists;

	private ImageView mProductBg;

	private TextView product_title;

	private LinearLayout mOperationLayout;

	private ProductDetailScrollView1 mScrollView;

	private LinearLayout mStoreProLy;

	private LinearLayout mTeamTalkLy;

	private RelativeLayout mUpLayout;

	private TextView mFeatInfo;

	private ImageView storeImg;

	private View loginLoading;

	private AnimationDrawable loadingAnimation;

	private LinearLayout product_layout_container;

	private TextView productCode;
	/**
	 * 市场价
	 */
	private TextView market_price;
	// 浏览人数
	private TextView number;
	// 加载更多
	private TextView more;
	// 加载更多
	private TextView market_price_text;

	private boolean check = false;

	private TextView beTime;
	private StringBuffer beTime1 = new StringBuffer();
	private String allBeTime;
	private LinearLayout tipe1;
	private LinearLayout tipe2;
	private LinearLayout tipe3;
	private LinearLayout tipe4;
	private LinearLayout tipe5;
	private LinearLayout tipe6;
	List<LinearLayout> TipLaylist = new ArrayList<>();
	private LinearLayout join_view;

	private TextView startPoint;

	private TextView endPoint;
	private View TipLine2;
	private View TipLine1;

	// 判断tip
	List<TipsDetailInfo> TipsDetailInfoList = new ArrayList<>();

	private LinearLayout llAll;

	private static final int tipImgs[] = { R.drawable.procash, R.drawable.protip, R.drawable.tips3, R.drawable.tips4,
			R.drawable.tips5, R.drawable.tips6 };
	private static final String tipTexts[] = { "费用包含/不包含", "温馨提示", "签证材料", "预定须知", "怎么去", "退款说明" };
	private static final String tipType[] = { "price_include", "hint", "visi_matierial", "order_instruction", "how_to",
			"refund_instruction" };

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.group_product_newest_activity);
		if (getIntent().getStringExtra("productTitle") != null) {
			statisticsTitle = getIntent().getStringExtra("productTitle");
		}
		productId = getIntent().getStringExtra("productId");
		mPanel = (SlideBottomPanel) findViewById(R.id.sbv);
		WindowManager winManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mPanel.setPanelHeight(
				winManager.getDefaultDisplay().getHeight() - DensityUtil.dip2px(this, 52) - statusBarHeight);
		initBroadcastReceiver();
		initViews();
	}

	private void initViews() {
		tipe1 = (LinearLayout) findViewById(R.id.ll1);
		tipe2 = (LinearLayout) findViewById(R.id.ll2);
		tipe3 = (LinearLayout) findViewById(R.id.ll3);
		tipe4 = (LinearLayout) findViewById(R.id.ll4);
		tipe5 = (LinearLayout) findViewById(R.id.ll5);
		tipe6 = (LinearLayout) findViewById(R.id.ll6);
		llAll = (LinearLayout) findViewById(R.id.ll_all);
		TipLine1 = (View) findViewById(R.id.tiplin1);
		TipLine2 = (View) findViewById(R.id.tiplin2);

		TipLaylist.add(tipe1);
		TipLaylist.add(tipe2);
		TipLaylist.add(tipe3);
		TipLaylist.add(tipe4);
		TipLaylist.add(tipe5);
		TipLaylist.add(tipe6);

		tipe1.setOnClickListener(this);
		tipe2.setOnClickListener(this);
		tipe3.setOnClickListener(this);
		tipe4.setOnClickListener(this);
		tipe5.setOnClickListener(this);
		tipe6.setOnClickListener(this);
		tipe1.setOnClickListener(this);
		market_price_text = (TextView) findViewById(R.id.market_price_text);
		storeImg = (ImageView) findViewById(R.id.if_stored);
		storeImg.setOnClickListener(this);
		featLayout = (RelativeLayout) findViewById(R.id.feat_layout);
		mFeatInfo = (TextView) findViewById(R.id.feat_info);
		mUpLayout = (RelativeLayout) findViewById(R.id.up_layout);
		imgCccc = (ImageView) findViewById(R.id.cccc);
		mTeamTalkLy = (LinearLayout) findViewById(R.id.team_talk_layout);
		mTeamTalkLy.setOnClickListener(this);
		mStoreProLy = (LinearLayout) findViewById(R.id.store_pro_layout);
		mStoreProLy.setOnClickListener(this);
		mStorePicIv = (ImageView) findViewById(R.id.store_pic_image);
		mScrollView = (ProductDetailScrollView1) findViewById(R.id.front_scollview);
		mScrollView.setScrollListener(this);
		mOperationLayout = (LinearLayout) findViewById(R.id.layout_operation_button);
		product_layout_container = (LinearLayout) findViewById(R.id.product_layout_container1);
		product_title = (TextView) findViewById(R.id.product_title);
		product_title.setAlpha(0);
		market_price = (TextView) findViewById(R.id.market_price);
		number = (TextView) findViewById(R.id.number);
		ImageView product_service_share_button = (ImageView) findViewById(R.id.product_service_share_button);
		product_service_share_button.setOnClickListener(this);
		mProductBg = (ImageView) findViewById(R.id.product_bg_img);
		mFeatureLists = (LinearLayout) findViewById(R.id.feature_list);
		mProductScrollView = (ProductDetailScrollView) findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setScrollListener(this);
		mProductScrollView.setImageView(mProductBg);
		mProductScrollView.setPullListener(mPullListener);
		// int space = getResources().getDimensionPixelSize(
		// R.dimen.detail_photo_bottom_space);
		// LayoutParams p = new LayoutParams(
		// LayoutParams.MATCH_PARENT, screenHeight - space
		// - statusBarHeight);
		// mProductBg.setLayoutParams(p);
		productDetailLayout = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		productDetailLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		productDetailLayout.getBackground().setAlpha(0);
		mJoinMebLayout = (LinearLayout) findViewById(R.id.join_meb_parent_layout);
		join_view = (LinearLayout) findViewById(R.id.join_view);
		mProductJoinMebCountTv = (TextView) findViewById(R.id.product_joinmem_user_count);
		mProductJoinMebLayout = (LinearLayout) findViewById(R.id.product_joinmem_user_photo_layout);
		levelTxt = (TextView) findViewById(R.id.level);
		tour_dates = (TextView) findViewById(R.id.tour_dates);
		mEvaluteProductLayout = (LinearLayout) findViewById(R.id.evalute_stars);
		mStorePicIv = (ImageView) findViewById(R.id.store_pic_image);
		mStoreTxt = (TextView) findViewById(R.id.store_pro_btn);
		mReplyAutor = (TextView) findViewById(R.id.evaluate_user_reply);
		mReplyAutor.setOnClickListener(this);
		mReferLayout = (LinearLayout) findViewById(R.id.referLayout);
		mReferAuthorPhoto = (RoundImageView) findViewById(R.id.refer_author_photo);
		mReferNameTv = (TextView) findViewById(R.id.refer_author_name);
		mReferContentTv = (TextView) findViewById(R.id.refer_content);
		mebMore = (ImageView) findViewById(R.id.more_meb);
		mebMore.setOnClickListener(this);
		productDetailLayout.setFocusable(true);
		productDetailLayout.setFocusableInTouchMode(true);
		inGroupData = (LinearLayout) findViewById(R.id.inGroupData);
		inGroupData.setOnClickListener(new OnClickListener() {
			// 点击查看出发日期
			@Override
			public void onClick(View v) {
				startTime = new ArrayList<String>();
				endTime = new ArrayList<String>();
				ArrayList<String> priceList = new ArrayList<String>();
				Intent intent_groupData = new Intent(GrouponProductNewDetailActivity.this, InGroupDataDetail.class);
				if (experiodList.size() == 0) {
					startTime.add("没有开始时间数据");
					intent_groupData.putStringArrayListExtra("tour_dates", (ArrayList<String>) startTime);

				} else {

					for (int i = 0; i < experiodList.size(); i++) {
						startTime.add(experiodList.get(i).getBeginDate().toString());
						endTime.add(experiodList.get(i).getEndDate().toString());
						priceList.add(experiodList.get(i).getPrice());
					}
					intent_groupData.putStringArrayListExtra("tour_dates", (ArrayList<String>) startTime);
					intent_groupData.putStringArrayListExtra("tour_dates2", (ArrayList<String>) endTime);
					intent_groupData.putStringArrayListExtra("priceList", priceList);
				}
				startActivity(intent_groupData);
				activityAnimationOpen();
			}
		});
		// warmprompt_lyatyout = (LinearLayout)
		// findViewById(R.id.warmprompt_lyatyout);
		// warmprompt_lyatyout.setOnClickListener(this);
		// price_tip = (RelativeLayout) findViewById(R.id.price_tip);
		// price_tip.setOnClickListener(this);
		mEvaBtn = (Button) findViewById(R.id.eva_btn);
		mEvaBtn.setOnClickListener(this);
		mComCounts = (TextView) findViewById(R.id.user_comment_count);
		layout_service_list = (FlowLayout) findViewById(R.id.layout_service_list);
		detail_product_title = (TextView) findViewById(R.id.detail_product_title);
		product_author_photo = (RoundImageView) findViewById(R.id.product_author_photo);
		LinearLayout backButton = (LinearLayout) findViewById(R.id.layout_back_button);
		backButton.setOnClickListener(this);
		AppManager.getAppManager().addActivity(this);
		ImageView product_service_button = (ImageView) findViewById(R.id.product_service_button);
		product_service_button.setOnClickListener(this);
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		productCode = (TextView) findViewById(R.id.productCode);
		more = (TextView) findViewById(R.id.more);
		beTime = (TextView) findViewById(R.id.beTime);
		startPoint = (TextView) findViewById(R.id.startPoint);
		endPoint = (TextView) findViewById(R.id.endPoint);
		initTripDetail();
		// 查询详情
		QueryProductDetail(productId);
		// 初始化订购人员
		initJoinMebPhoto();
		// 行程路线
		QueryProductJourney(productId);
		// 初始化图片
		queryPicList(productId);
		// 查询评论数
		queryProductComment(1, productId);
		// 查询相似产品
		QuerySimillarProduct(1, productId);

	}

	@Override
	public void onBackPressed() {
		if (mPanel.isPanelShowing()) {
			mPanel.hide();
			return;
		}
		super.onBackPressed();
	}

	private void QueryProductDetail(String productId) {
		// LogUtil.e(TAG, "QueryProductDetail", Urls.product_detail +
		// productId);

		HttpUtil.get(Urls.product_detail + productId, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// LogUtil.i(TAG, "QueryProductDetail", "statusCode=" +
				// statusCode);
				LogUtil.i(TAG, "QueryProductDetail", "response=" + response.toString());
				productInfo = new ProductNewInfo();
				String sb = response.toString();
				String code = response.optString("code");
				String msg = response.optString("msg");
				if (!code.equals("00000")) {
					initBottomButton(false);
					ToastUtil.showToast(GrouponProductNewDetailActivity.this, msg);
					finish();
					return;
				}

				JSONObject data = response.optJSONObject("data");
				// 产品星级
				score = data.optString("score");
				String score1 = score;
				// add by lij 2015/09/29 start
				if (score != null) {
					if (score.contains(".")) {
						score = score.substring(0, score.indexOf("."));
					}
					int stars = Integer.parseInt(score);
					for (int i = 0; i < stars; i++) {
						ImageView view = new ImageView(GrouponProductNewDetailActivity.this);
						LayoutParams params = new LayoutParams(20, 20);
						params.setMargins(5, 0, 0, 0);
						view.setLayoutParams(params);
						view.setImageResource(R.drawable.yellow_star);
						mEvaluteProductLayout.addView(view);
					}
				}
				TextView textview = new TextView(GrouponProductNewDetailActivity.this);
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				;
				params.setMargins(5, 0, 0, 0);
				textview.setLayoutParams(params);
				if ("0".equals(score)) {
					textview.setText("暂无评分");
					textview.setTextColor(Color.parseColor("#999999"));
				} else {
					textview.setText(score1 + "分");
					textview.setTextColor(Color.RED);
				}
				mEvaluteProductLayout.addView(textview);
				// add by lij 2015/09/29 end
				productInfo.setShowButton(data.optString("showButton"));
				initBottomButton(true);
				// if("2".equals(productInfo.getShowButton())){
				// }else{
				// initBottomButton(false);
				// }
				JSONArray tips = data.optJSONArray("tips");
				if (tips != null) {
					for (int i = 0; i < tips.length(); i++) {
						JSONObject tip = tips.optJSONObject(i);
						String content = tip.optString("content");
						if (StringUtils.isEmpty(content))
							continue;
						TipsDetailInfo tipInfoWarmPrompt = new TipsDetailInfo();
						tipInfoWarmPrompt.setType(tip.optString("tipsType"));
						tipInfoWarmPrompt.setTitle(tip.optString("title"));
						if ("price_include".equals(tip.optString("tipsType"))) {
							tipInfoWarmPrompt.setId(0);
							TipsDetailInfoList.add(tipInfoWarmPrompt);
						}
						if ("hint".equals(tip.optString("tipsType"))) {
							tipInfoWarmPrompt.setId(1);
							TipsDetailInfoList.add(tipInfoWarmPrompt);
						}
						if ("visi_matierial".equals(tip.optString("tipsType"))) {
							tipInfoWarmPrompt.setId(2);
							TipsDetailInfoList.add(tipInfoWarmPrompt);
						}
						if ("order_instruction".equals(tip.optString("tipsType"))) {
							tipInfoWarmPrompt.setId(3);
							TipsDetailInfoList.add(tipInfoWarmPrompt);
						}
						if ("how_to".equals(tip.optString("tipsType"))) {
							tipInfoWarmPrompt.setId(4);
							TipsDetailInfoList.add(tipInfoWarmPrompt);
						}
						if ("refund_instruction".equals(tip.optString("tipsType"))) {
							tipInfoWarmPrompt.setId(5);
							TipsDetailInfoList.add(tipInfoWarmPrompt);
						}
					}
				}
				setLlValue();
				JSONObject product = data.optJSONObject("product");
				JSONArray expPeriodArray = product.optJSONArray("expPeriod");
				if (expPeriodArray != null) {
					// =====行程的开始日期
					experiodList = new ArrayList<AvailableTime>();
					for (int i = 0; i < expPeriodArray.length(); i++) {
						JSONObject experiodJson = expPeriodArray.optJSONObject(i);
						AvailableTime availableTime = new AvailableTime();
						availableTime.setBeginDate(experiodJson.optString("beginDate"));
						availableTime.setEndDate(experiodJson.optString("endDate"));
						availableTime.setPrice(experiodJson.optString("price"));
						experiodList.add(availableTime);
						// //////////////////////////////
						// begin_time = availableTime.getBeginDate();
					}
					productInfo.setExpPeriodList(experiodList);

					for (int i = 0; i < experiodList.size(); i++) {
						if (i < 8) {
							String bT = experiodList.get(i).getBeginDate().substring(4, 8);
							String bt1 = bT.substring(0, 2);
							String bt2 = bT.substring(2, 4);
							String over = bt1 + "-" + bt2 + "、";
							beTime1.append(over);
						}
					}
					if (beTime1.length() > 1) {
						allBeTime = beTime1.substring(0, beTime1.length() - 1);
						beTime.setText(allBeTime);
					}
				}
				JSONArray topics = product.optJSONArray("topics");
				if (topics != null) {
					for (int i = 0; i < topics.length(); i++) {
						productInfo.getTopics().add(topics.opt(i).toString());
					}
				}
				String isFav = product.optString("isFav", "no");
				if ("yes".equals(isFav)) {
					isFavedFlag = true;
					mStorePicIv.setImageResource(R.drawable.stored3);
					storeImg.setImageResource(R.drawable.yes_store);

					mStoreTxt.setText("已收藏");
				} else {
					isFavedFlag = false;
					mStorePicIv.setImageResource(R.drawable.store3);
					storeImg.setImageResource(R.drawable.no_store);
					mStoreTxt.setText("收藏");
				}
				productInfo.setId(product.optString("id"));
				productInfo.setEndPoint(product.optString("endPoint"));
				if (!StringUtils.isEmpty(productInfo.getEndPoint())) {
					String end = productInfo.getEndPoint().replace(",", "、");
					endPoint.setText(end);
				}
				productInfo.setStartPoint(product.optString("startPoint"));
				if (!StringUtils.isEmpty(productInfo.getStartPoint())) {
					String star = productInfo.getStartPoint().replace(",", "、");
					startPoint.setText(star);
				}
				productInfo.setId(product.optString("id"));
				productInfo.setDistance(product.optString("distance"));
				productInfo.setTitle(product.optString("title"));
				productInfo.setPoiCount(product.optString("poiCount"));
				productInfo.setPrice(product.optString("price"));
				productInfo.setDays(product.optString("days"));
				productInfo.setTimeUnit(product.optString("timeUnit"));
				productInfo.setProductCode(product.optString("productCode"));
				productInfo.setHasDeposit(product.optString("hasDeposit"));
				productInfo.setOriginalPrice(product.optString("originalPrice"));
				productInfo.setPv(product.optString("pv"));
				if (productInfo.getTimeUnit() != null) {
					if (productInfo.getTimeUnit().equals("week")) {
						mTripPlanListAdapter.setTimeUnit("周");
					} else if (productInfo.getTimeUnit().equals("month")) {
						mTripPlanListAdapter.setTimeUnit("月");
					}
				}

				productInfo.setDescription(product.optString("description")); // 内容
				productInfo.setPriceMax(product.optString("priceMax"));
				productInfo.setTitleImg(product.optString("titleImg")); // 标题图片
				productInfo.setMaxMember(product.optString("maxMember"));
				productInfo.setProductType(product.optString("productType"));
				productInfo.setCustomFor(data.optString("customFor"));
				productInfo.setServices(product.optString("serviceCodes"));
				productInfo.setServTime(product.optString("servTimes"));
				productInfo.setFeature(product.optString("feature"));
				try {
					productInfo.setLevel(product.optJSONObject("policy").getString("type"));
					productInfo.setPolicyContent(product.optJSONObject("policy").getString("content"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject userJSON = product.optJSONObject("creater");
				// add by lij 2015/11/03 start
				// 小编推荐
				if (userJSON != null) {
					JSONObject exts = userJSON.optJSONObject("exts");
					if (exts != null) {
						JSONObject referer = exts.optJSONObject("referer");
						if (referer != null) {
							String imgRes = referer.optString("img");
							mReferAuthorPhoto.setBackgroundResource(R.drawable.user_defult_photo);
							if (!StringUtils.isEmpty(imgRes)) {
								ImageLoader.getInstance().displayImage(Urls.imgHost + imgRes, mReferAuthorPhoto,
										AppConfig.options(R.drawable.user_defult_photo));
							}
							// 推荐人昵称
							mReferNameTv.setText(referer.optString("nickname"));
							// 推荐人layout显示
							mReferLayout.setVisibility(View.VISIBLE);
						} else {
							mReferLayout.setVisibility(View.GONE);
						}
						// 推荐语
						mReferContentTv.setText(exts.optString("refer_content"));

					} else {
						mReferLayout.setVisibility(View.GONE);
					}
				}
				// add by lij 2015/11/03 end
				if (userJSON != null) {
					productInfo.getUser().setUserId(userJSON.optString("userId"));
					productInfo.getUser().setPermission(userJSON.optString("profession"));
					productInfo.getUser().setNickname(userJSON.optString("nickName"));
					productInfo.getUser().setAvatar(userJSON.optString("avatar"));
					productInfo.getUser().setIntroduction(userJSON.optString("introduction"));
					productInfo.getUser().setUserType(userJSON.optString("userType"));
					productInfo.getUser().setServerPolicy(userJSON.optString("serverPolicy"));
					productInfo.getUser().setBackground(userJSON.optString("background"));
					productInfo.getUser().setBrief(userJSON.optString("brief"));
					JSONObject obj = userJSON.optJSONObject("orgRole");

					if (null != obj) {
						OrgRole role = new OrgRole();
						role.setRoleName(obj.optString("roleName"));
						role.setRoleCode(obj.optString("roleCode"));
						role.setRoleType(obj.optString("roleType"));
						role.setPermission(obj.optString("permission"));
						productInfo.getUser().setOrgRole(role);
					}
				}
				JSONArray featuresJSONList = data.optJSONArray("features");
				if (featuresJSONList != null) {
					for (int i = 0; i < featuresJSONList.length(); i++) {
						JSONObject featureJSON = featuresJSONList.optJSONObject(i);
						FeatureInfo feature = new FeatureInfo();
						feature.setId(featureJSON.optString("id"));
						feature.setTitle(featureJSON.optString("title"));
						feature.setDesc(featureJSON.optString("desc"));
						JSONArray imagesJSONList = featureJSON.optJSONArray("images");
						if (imagesJSONList != null) {
							for (int j = 0; j < imagesJSONList.length(); j++) {
								JSONObject imagesJSON = imagesJSONList.optJSONObject(j);
								ImageInfo info = new ImageInfo();
								info.setUrl(imagesJSON.optString("url"));
								info.setDesc(imagesJSON.optString("desc"));
								feature.getImages().add(info);
							}
						}
						productInfo.getFeatures().add(feature);

					}
				}

				initProductInfo(productInfo);
				// initScenicSpots(productInfo);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i(TAG, "QueryProductDetail", "statusCode=" + statusCode);
				LogUtil.i(TAG, "QueryProductDetail", "responseString=" + responseString);
				for (int i = 0; i < headers.length; i++) {
					LogUtil.i(TAG, "QueryProductDetail",
							"key-->" + headers[i].getName() + "--getValue-->" + headers[i].getValue());
				}
			}

		});

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.layout_back_button:
			if (isFromLoad) {
				Intent intent1 = new Intent(GrouponProductNewDetailActivity.this, MainActivity.class);
				startActivity(intent1);
			}
			finish();
			activityAnimationClose();
			break;
		case R.id.more_meb:
			Intent it = new Intent(GrouponProductNewDetailActivity.this, ProductJoinMebActivity.class);
			it.putExtra("productId", productId);
			it.putExtra("total", total);
			startActivity(it);
			break;
		// case R.id.warmprompt_lyatyout:
		// intent = new Intent(this, WarmpromptTipActivity.class);
		// intent.putExtra("productId", productId);
		// intent.putExtra("tipsType", "hint");
		// startActivity(intent);
		// activityAnimationOpen();
		// break;

		// 贴士点击时间
		case R.id.ll1:
		case R.id.ll2:
		case R.id.ll3:
		case R.id.ll4:
		case R.id.ll5:
		case R.id.ll6:
			TipsDetailInfo tipInfoWarmPrompt = (TipsDetailInfo) v.getTag();
			intent = new Intent(this, WarmpromptTipActivity.class);
			intent.putExtra("productId", productId);
			intent.putExtra("tipsType", tipType[tipInfoWarmPrompt.getId()]);
			startActivity(intent);
			activityAnimationOpen();
			break;
		case R.id.eva_btn:
			// 评论
			if (StringUtils.isEmpty(PreferenceUtil.getUserId())) {
				ToastUtil.showToast(GrouponProductNewDetailActivity.this, "用户还未登录不能评价");
			} else {
				ReplyDialog dialog = new ReplyDialog(this, new OnSendReplyListener() {
					@Override
					public void sendReply(String comment) {
						evaProductComments(comment);
					}
				});
				dialog.show();

			}
			break;
		case R.id.evaluate_user_reply:
			// 回复
			if (!AppInfo.getIsLogin()) {
				ToastUtil.showToast(GrouponProductNewDetailActivity.this, "用户还未登录不能回复");
			} else {
				// replyProductComments();
				ReplyDialog dialog = new ReplyDialog(this, new OnSendReplyListener() {
					@Override
					public void sendReply(String comment) {
						replyProductComments(commentId, "", comment);
					}
				});
				dialog.show();
				// dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				// LayoutParams.WRAP_CONTENT);
			}
			break;
		// add by lij 2015/09/29 end
		// add by lij 2015/11/03 start
		case R.id.product_evaluate_detail_button:
			intent = new Intent(this, UserCommentActivity.class);
			intent.putExtra("productId", productId);
			startActivity(intent);
			activityAnimationOpen();
			break;
		case R.id.product_service_share_button:
			ShareSelectDialog shareSelectDialog = new ShareSelectDialog(GrouponProductNewDetailActivity.this,
					productInfo.getTitleImg(), productInfo.getFeature(), productInfo.getTitle(), productInfo.getId(),
					Urls.ShareUrlOfProduct, productInfo.getTitle());
			shareSelectDialog.show();
			break;
		case R.id.layout_apply_product_button:
			// intent = new Intent(this, ApplyProductActivity.class);
			if (!AppInfo.getIsLogin()) {
				goLoginActivity();
				return;
			}
			if (productInfo == null) {
				return;
			}
			intent = new Intent(this, ConfirmPayActivity3.class);
			intent.putExtra("productInfo", productInfo);

			ArrayList<String> priceList = new ArrayList<String>();
			startTime = new ArrayList<String>();
			endTime = new ArrayList<String>();
			if (experiodList.size() == 0) {
				startTime.add("没有开始时间数据");
				intent.putStringArrayListExtra("tour_dates", (ArrayList<String>) startTime);

			} else {

				for (int i = 0; i < experiodList.size(); i++) {
					startTime.add(experiodList.get(i).getBeginDate().toString());
					endTime.add(experiodList.get(i).getEndDate().toString());
					priceList.add(experiodList.get(i).getPrice());
				}
				intent.putStringArrayListExtra("tour_dates", (ArrayList<String>) startTime);
				intent.putStringArrayListExtra("tour_dates2", (ArrayList<String>) endTime);
				intent.putStringArrayListExtra("priceList", priceList);
			}

			startActivity(intent);
			activityAnimationOpen();
			break;

		case R.id.layout_contact_author_button:
			if (!AppInfo.getIsLogin()) {
				goLoginActivity();
				return;
			}
			Intent talkIntent = new Intent(GrouponProductNewDetailActivity.this, ChatActivity.class);
			talkIntent.putExtra("receiveId", productInfo.getUser().getUserId());
			talkIntent.putExtra("title", productInfo.getUser().getNickname());
			startActivity(talkIntent);
			activityAnimationOpen();
			break;
		case R.id.product_author_photo:
			// 取消服务者界面跳转2016.6.3徐总要求
			// if (productInfo != null && productInfo.getUser() != null) {
			// boolean ischeck = true;
			// OrgRole role = productInfo.getUser().getOrgRole();
			// if (role != null) {
			//
			// if ("admin".equals(role.getRoleCode())) {
			// intent = new Intent(this, ClubFirstPageActivity.class);
			// // intent.putExtra("productId", productId);
			// intent.putExtra("userInfo", productInfo.getUser());
			// // intent.putExtra("background",productInfo.getTitleImg());
			// startActivity(intent);
			// activityAnimationOpen();
			// }
			// Intent intentForUserInfo = new Intent(this,
			// PersonalInfoNewActivity.class);
			// if ("leader".equals(role.getRoleCode()) ||
			// "guide".equals(role.getRoleCode())) {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", false);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// ischeck = false;
			// } else if ("driver".equals(role.getRoleCode())) {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", true);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// ischeck = false;
			// }
			//
			// }
			// if (ischeck && productInfo.getUser().getPermission() != null) {
			// String profession = productInfo.getUser().getPermission();
			// Intent intentForUserInfo = new Intent(this,
			// PersonalInfoNewActivity.class);
			// if (profession != null && profession.contains("专业司机")) {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", true);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// } else if (profession != null && (profession.contains("资深领队") ||
			// profession.contains("达人导游")))
			//
			// {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", false);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// }
			// }
			// }
			break;
		case R.id.product_author_name:
			// 取消服务者界面跳转2016.6.3徐总要求
			// if (productInfo != null && productInfo.getUser() != null) {
			// boolean ischeck = true;
			// OrgRole role = productInfo.getUser().getOrgRole();
			// if (role != null) {
			//
			// if ("admin".equals(role.getRoleCode())) {
			// intent = new Intent(this, ClubFirstPageActivity.class);
			// // intent.putExtra("productId", productId);
			// intent.putExtra("userInfo", productInfo.getUser());
			// // intent.putExtra("background",productInfo.getTitleImg());
			// startActivity(intent);
			// activityAnimationOpen();
			// }
			// Intent intentForUserInfo = new Intent(this,
			// PersonalInfoNewActivity.class);
			// if ("leader".equals(role.getRoleCode()) ||
			// "guide".equals(role.getRoleCode())) {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", false);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// ischeck = false;
			// } else if ("driver".equals(role.getRoleCode())) {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", true);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// ischeck = false;
			// }
			//
			// }
			// if (ischeck && productInfo.getUser().getPermission() != null) {
			// String profession = productInfo.getUser().getPermission();
			// Intent intentForUserInfo = new Intent(this,
			// PersonalInfoNewActivity.class);
			// if (profession != null && profession.contains("专业司机")) {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", true);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// } else if (profession != null && (profession.contains("资深领队") ||
			// profession.contains("达人导游")))
			//
			// {
			// intentForUserInfo = new Intent(this,
			// ClubMebHomepageActivity.class);
			// intentForUserInfo.putExtra("isDriverHomePage", false);
			// intentForUserInfo.putExtra("identifyId",
			// productInfo.getUser().getUserId());
			// intentForUserInfo.putExtra("userInfo", productInfo.getUser());
			// startActivity(intentForUserInfo);
			// activityAnimationOpen();
			// }
			// }
			// }
			break;
		case R.id.product_service_button:
			if (productInfo == null) {
				return;
			}
			intent = new Intent(this, ProductServiceActivity.class);
			intent.putExtra("bgUrl", productInfo.getTitleImg());
			intent.putExtra("productId", productId);
			startActivity(intent);
			activityAnimationOpen();
			break;
		case R.id.team_talk_layout:
			intent = new Intent(this, ChatActivity.class);
			intent.putExtra("ok", productInfo);
			System.out.println(productInfo);
			joinTeamTalk(productId);
			break;
		case R.id.store_pro_layout:
		case R.id.if_stored:
			if (!AppInfo.getIsLogin()) {
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				return;
			}
			isFavedFlag = !isFavedFlag;
			storeProductByIsFaved(isFavedFlag);
			break;
		default:
			break;
		}
	}

	private void joinTeamTalk(String productId) {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("referType", "product");
		params.put("referId", productId);
		HttpUtil.get(Urls.join_team_talk, params, new JsonHttpResponseHandler() {
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
				}
				String queueId = response.optString("data");
				if (!StringUtils.isEmpty(queueId)) {
					// 查询队列详情并跳转
					queryTeamTalkInfo(queueId);
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	private void queryTeamTalkInfo(final String queueId) {
		RequestParams params = new RequestParams();
		params.put("queueId", queueId);
		params.put("pagenum", 1);// 当前页码
		params.put("pagesize", 10);// 页码数
		params.put("msgId", "");
		HttpUtil.get(Urls.message_queue_url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if (!"00000".equals(code)) {
					Intent intent = new Intent(GrouponProductNewDetailActivity.this, LoginActivity.class);
					startActivity(intent);
					return;
				}
				JSONObject data = response.optJSONObject("data");
				JSONObject product = data.optJSONObject("product");
				Intent intent = new Intent(GrouponProductNewDetailActivity.this, ChatActivity.class);
				intent.putExtra("queueId", queueId);
				intent.putExtra("title", product.optString("title"));
				intent.putExtra("pattern", "team");
				intent.putExtra("fromQueue", true);
				intent.putExtra("isTeam", true);
				intent.putExtra("ok", productInfo);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_new, R.anim.activity_out);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	private void storeProductByIsFaved(boolean flag) {

		if (flag) {
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
							Intent intent = new Intent(GrouponProductNewDetailActivity.this, LoginActivity.class);
							startActivity(intent);
							return;
						}
						ToastUtil.showToast(GrouponProductNewDetailActivity.this, "已收藏");
						mStorePicIv.setImageResource(R.drawable.stored3);
						storeImg.setImageResource(R.drawable.yes_store);
						mStoreTxt.setText("已收藏");
						Intent intent = new Intent("com.bcinfo.pickListRefresh");
						intent.putExtra("flag", true);
						intent.putExtra("productId", productId);
						sendBroadcast(intent);
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
					}
					ToastUtil.showToast(GrouponProductNewDetailActivity.this, "收藏已取消");
					mStorePicIv.setImageResource(R.drawable.store3);
					storeImg.setImageResource(R.drawable.no_store);
					mStoreTxt.setText("收藏");
					Intent intent = new Intent("com.bcinfo.pickListRefresh");
					intent.putExtra("flag", false);
					intent.putExtra("productId", productId);
					sendBroadcast(intent);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}
			});
		}
	}

	private void initProductInfo(ProductNewInfo productInfo) {
		UserInfo author = productInfo.getUser();
		// ImageView product_service_button = (ImageView)
		// findViewById(R.id.product_service_button);
		TextView product_author_name = (TextView) findViewById(R.id.product_author_name);
		product_author_name.setOnClickListener(this);
		// TextView product_author_description = (TextView)
		// findViewById(R.id.product_description);
		TextView product_price = (TextView) findViewById(R.id.product_price);
		// TextView recruit_time = (TextView)findViewById(R.id.recruit_time);//
		// 招募期开始结束时间
		// TextView experience_time =
		// (TextView)findViewById(R.id.experience_time);// 体验期开始结束时间
		TextView tripDays = (TextView) findViewById(R.id.detail_trip_days);
		tripDays.setText(productInfo.getDays());
		// product_title.setText(productInfo.getTitle());
		// scenic_spots_des.setText(productInfo.getDescription());
		// add by lij 2015/10/08 start
		/* if(!StringUtils.isEmpty(productInfo.getServTime())){ */

		// totals = productAllImgList.size()+"";
		// product_ser_time.setOnClickListener(this);
		/*
		 * }else{ product_ser_time.setText("0P"); }
		 */
		// add by lij 2015/10/08 end
		// product_service_button.setOnClickListener(this);
		product_author_name.setText("由" + author.getNickname() + "提供");
		number.setText(productInfo.getPv());
		// if (!StringUtils.isEmpty(author.getIntroduction())) {
		// product_author_description.setText(author.getIntroduction());
		// }
		product_title.setText(productInfo.getTitle());
		detail_product_title.setText(productInfo.getTitle());

		mProductBg.setImageResource(R.drawable.ic_launcher);
		if (!StringUtils.isEmpty(productInfo.getTitleImg())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + productInfo.getTitleImg(), mProductBg,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							// TODO Auto-generated method stub
							loadingAnimation.stop();
							((View) loginLoading.getParent()).setVisibility(View.GONE);

							product_author_photo.setVisibility(View.VISIBLE);
							product_layout_container.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							loadingAnimation.stop();
							((View) loginLoading.getParent()).setVisibility(View.GONE);
							product_author_photo.setVisibility(View.VISIBLE);
							product_layout_container.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub

						}
					});
		}
		product_author_photo.setOnClickListener(this);
		if (!StringUtils.isEmpty(author.getAvatar())) {

			ImageLoader.getInstance().displayImage(Urls.imgHost + author.getAvatar(), product_author_photo,
					AppConfig.options(R.drawable.user_defult_photo));
		}
		if (StringUtils.isEmpty(productInfo.getFeature())) {
			featLayout.setVisibility(View.GONE);
		} else {
			featLayout.setVisibility(View.VISIBLE);
			mFeatInfo.setText(productInfo.getFeature());
		}
		// product_introduce.setText(productInfo.getDescription());

		// ===============行程日期的显示==========
		String str = "天";
		if (productInfo.getTimeUnit().equals("week")) {
			str = "周";
		} else if (productInfo.getTimeUnit().equals("month")) {
			str = "月";
		}
		tour_dates.setText("行程" + productInfo.getDays() + str);

		product_price.setText("￥" + productInfo.getPrice());
		// ****************************给详情页添加下拉显示主题标签*********************************

		List<String> topicNameTvArray = productInfo.getTopics();
		if (topicNameTvArray != null && topicNameTvArray.size() != 0) {

			for (int i = 0; i < topicNameTvArray.size(); i++) {
				TextView labelTv = new TextView(this);
				View view = new View(this);
				view.setLayoutParams(new FlowLayout.LayoutParams(5, 5));
				labelTv.setText(productInfo.getTopics().get(i));// 设置内容
				labelTv.setTextSize(12);// 设置字体大小
				labelTv.setSingleLine(true);
				FlowLayout.LayoutParams ls = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
						FlowLayout.LayoutParams.WRAP_CONTENT);
				ls.bottomMargin = 10;
				labelTv.setLayoutParams(ls);
				labelTv.setTextColor(this.getResources().getColor(R.color.gray_text));// 设置字体颜色
				// labelTv.setPadding(5,5,5,5);//设置四周留白

				labelTv.setGravity(Gravity.CENTER);
				labelTv.setBackgroundResource(R.drawable.gray_shap);
				if (i != topicNameTvArray.size()) {
					layout_service_list.addView(view);
				}
				layout_service_list.addView(labelTv);
			}

		}

		if (productInfo.getLevel() != null)

		{
			if (productInfo.getLevel().equals("super")) {
				levelTxt.setText("非常严格");
			} else if (productInfo.getLevel().equals("hight")) {
				levelTxt.setText("严格");
			} else if (productInfo.getLevel().equals("middle")) {
				levelTxt.setText("适中");
			} else if (productInfo.getLevel().equals("low")) {
				levelTxt.setText("灵活");
			} else {
				levelTxt.setText(productInfo.getLevel());
			}
		}
		if (StringUtils.isEmpty(productInfo.getFeature())
				&& (productInfo.getFeatures() == null || productInfo.getFeatures().size() == 0)) {
			// mUpLayout.setVisibility(View.VISIBLE);
			// productDetailLayout.getBackground().setAlpha(255);
			// product_title.setAlpha(255);
		}
		// if (!StringUtils.isEmpty(productInfo.getFeature())){
		// more.setVisibility(View.VISIBLE);
		// }
		ViewTreeObserver viewTreeObserver = mProductScrollView.getChildAt(0).getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (check)
					return true;
				int h = mProductScrollView.getChildAt(0).getHeight();
				if (initH == 0) {
					initH = h;
					return true;
				}
				if (initH == h)
					return true;
				int h1 = mProductScrollView.getHeight();
				if (h1 != 0) {
					if (h > h1) {
						more.setVisibility(View.VISIBLE);
						check = true;
					} else {
						mUpLayout.setVisibility(View.VISIBLE);
						productDetailLayout.getBackground().setAlpha(255);
						product_title.setAlpha(255);
						check = true;
					}
				}
				return true;
			}
		});
		if (productInfo.getFeatures() != null && productInfo.getFeatures().size() > 0) {
			ArrayList<FeatureInfo> featureLists = productInfo.getFeatures();
			// featureListAdapter = new
			// FeatureListAdapter(GrouponProductNewDetailActivity.this,
			// featureLists);
			// mFeatureLists.setAdapter(featureListAdapter);
			// featureListAdapter.notifyDataSetChanged();
			addFeatureItem(featureLists, mFeatureLists);
		}
		if (!StringUtils.isEmpty(productInfo.getProductCode())) {
			productCode.setVisibility(View.VISIBLE);
			productCode.setText("产品编号：" + productInfo.getProductCode());
		}
		float price = 0;
		if (!StringUtils.isEmpty(productInfo.getPrice())) {
			price = Float.parseFloat(productInfo.getPrice());
		}
		float price2 = 0;
		if (!StringUtils.isEmpty(productInfo.getOriginalPrice())) {
			price2 = Float.parseFloat(productInfo.getOriginalPrice());
		}
		if (!StringUtils.isEmpty(productInfo.getOriginalPrice()) && "0".equals(productInfo.getOriginalPrice()) == false
				&& price < price2) {
			market_price_text.setVisibility(View.VISIBLE);
			market_price.setVisibility(View.VISIBLE);
			market_price.setText(productInfo.getOriginalPrice() + "/人");
			market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		}
	}

	private int initH = 0;

	private void initJoinMebPhoto() {
		// 1.http协议调用接口获取订购人数

		RequestParams requestParams = new RequestParams();
		requestParams.put("pagenum", "1");
		requestParams.put("pagesize", "20");
		requestParams.put("productId", productId);
		HttpUtil.get(Urls.product_buyers, requestParams, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (mProductJoinMebLayout != null) {
					mProductJoinMebLayout.removeAllViews();
				}
				LogUtil.i(TAG, "GrouponProductNewDetailActivity", "statusCode=" + statusCode);
				LogUtil.i(TAG, "GrouponProductNewDetailActivity", "response=" + response.toString());
				String code = response.optString("code");
				String msg = response.optString("msg");
				if (!code.equals("00000")) {
					// ToastUtil.showErrorToast(
					// GrouponProductNewDetailActivity.this, msg);
					return;
				}
				JSONObject dataJSON = response.optJSONObject("data");
				total = dataJSON.optString("total");
				mProductJoinMebCountTv.setText("当前已有" + total + "人预订此产品");
				if (!StringUtils.isEmpty(total) && Integer.parseInt(total) > 0) {
					mJoinMebLayout.setVisibility(View.VISIBLE);
					join_view.setVisibility(View.VISIBLE);
				} else {
					join_view.setVisibility(View.GONE);
					mJoinMebLayout.setVisibility(View.GONE);
				}
				JSONArray productArray = dataJSON.optJSONArray("buyers");
				// 屏幕 宽度
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = dm.widthPixels - 60;
				LayoutParams params = new LayoutParams(
						(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
								getResources().getDisplayMetrics()),
						(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
								getResources().getDisplayMetrics()));
				params.setMargins(10, 10, 0, 0);
				if (productArray != null) {
					for (int i = 0; i < productArray.length(); i++) {
						JSONObject userJson = productArray.optJSONObject(i);
						UserInfo senderInfo = new UserInfo();
						senderInfo.setGender(userJson.optString("sex"));
						senderInfo.setAddress(userJson.optString("address"));
						senderInfo.setStatus(userJson.optString("status"));
						senderInfo.setEmail(userJson.optString("email"));
						senderInfo.setNickname(userJson.optString("nickName"));
						senderInfo.setUserId(userJson.optString("userId"));
						senderInfo.setUserType(userJson.optString("userType"));
						senderInfo.setBrief(userJson.optString("brief"));
						senderInfo.setRole(userJson.optString("role"));
						senderInfo.setPermission(userJson.optString("permission"));
						senderInfo.setAvatar(userJson.optString("avatar"));
						senderInfo.setIntroduction(userJson.optString("introduction"));
						senderInfo.setMobile(userJson.optString("mobile"));
						senderInfo.setBackground(userJson.optString("background"));
						senderInfo.setUsersIdentity(userJson.optString("usersIdentity"));
						JSONArray tagsJsonArray = userJson.optJSONArray("tags");
						if (tagsJsonArray != null && tagsJsonArray.length() > 0) {
							ArrayList<String> tags = new ArrayList<String>();
							for (int j = 0; j < tagsJsonArray.length(); j++) {
								tags.add(tagsJsonArray.optString(j));
							}
							senderInfo.setTags(tags);
						}

						JSONObject orgRole = userJson.optJSONObject("orgRole");
						OrgRole orle = new OrgRole();
						if (orgRole != null) {
							orle.setRoleName(orgRole.optString("roleName"));
							orle.setRoleType(orgRole.optString("roleType"));
							orle.setRoleCode(orgRole.optString("roleCode"));
						}
						senderInfo.setOrgRole(orle);
						JSONArray proArray = userJson.optJSONArray("profession");
						ArrayList<String> list = new ArrayList<String>();
						String profession = "";
						if (proArray != null) {
							for (int j = 0; j < proArray.length(); j++) {
								list.add(proArray.optString(j));
								if (j == 0) {
									profession += proArray.optString(j);
								} else {
									profession += "," + proArray.optString(j);
								}
							}
						}
						senderInfo.setPermission(profession);
						senderInfo.setProfession(list);
						RoundImageView imageView = new RoundImageView(GrouponProductNewDetailActivity.this);
						if ((i + 2) * 70 > width) {
							// imageView.setImageResource(R.drawable.all_buyers_more);
							// imageView.setLayoutParams(params);
							// imageView.setOnClickListener(new
							// OnClickListener() {
							// @Override
							// public void onClick(View v) {
							// Intent it = new
							// Intent(GrouponProductNewDetailActivity.this,ProductJoinMebActivity.class);
							// it.putExtra("productId", productId);
							// it.putExtra("total", total);
							// startActivity(it);
							// // finish();
							// }
							// });
							// mProductJoinMebLayout.removeView(imageView);
							// mProductJoinMebLayout.addView(imageView);
							break;
						} else if (i == (productArray.length() - 1)) {
							if ((i + 2) * 70 > width) {
								// imageView.setImageResource(R.drawable.all_buyers_more);
								// imageView.setLayoutParams(params);
								// imageView.setOnClickListener(new
								// OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// Intent it = new
								// Intent(GrouponProductNewDetailActivity.this,ProductJoinMebActivity.class);
								// it.putExtra("productId", productId);
								// it.putExtra("total", total);
								// startActivity(it);
								// // finish();
								// }
								// });
								// mProductJoinMebLayout.addView(imageView);
							} else {
								imageView.setImageResource(R.drawable.user_defult_photo);
								if (!StringUtils.isEmpty(userJson.optString("avatar"))) {
									ImageLoader.getInstance().displayImage(Urls.imgHost + userJson.optString("avatar"),
											imageView, AppConfig.options(R.drawable.user_defult_photo));
								}
								imageView.setLayoutParams(params);
								imageView.setTag(senderInfo);
								imageView.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										UserInfo userInfo = (UserInfo) v.getTag();
										Intent intentForUserInfo;
										if ("customer".equals(userInfo.getUserType())) {
											intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
													CustomerHomePageActivity.class);
											intentForUserInfo.putExtra("userInfo", userInfo);
											startActivity(intentForUserInfo);
											return;
										}
										OrgRole orgRole = userInfo.getOrgRole();
										if (orgRole != null) {
											if ("admin".equals(orgRole.getRoleCode())) {
												intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
														ClubFirstPageActivity.class);
												intentForUserInfo.putExtra("userInfo", userInfo);
												startActivity(intentForUserInfo);
												return;
											}
											if ("leader".equals(orgRole.getRoleCode())
													|| "guide".equals(orgRole.getRoleCode())) {
												intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
														ClubMebHomepageActivity.class);
												intentForUserInfo.putExtra("isDriverHomePage", false);
												intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
												intentForUserInfo.putExtra("userInfo", userInfo);
												startActivity(intentForUserInfo);
												return;

											} else if ("driver".equals(orgRole.getRoleCode())) {
												intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
														ClubMebHomepageActivity.class);
												intentForUserInfo.putExtra("isDriverHomePage", true);
												intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
												intentForUserInfo.putExtra("userInfo", userInfo);
												startActivity(intentForUserInfo);
												return;
											}
										}

										String profession = userInfo.getPermission();
										intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
												PersonalInfoNewActivity.class);
										if (profession != null && profession.contains("专业司机")) {
											intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
													ClubMebHomepageActivity.class);
											intentForUserInfo.putExtra("isDriverHomePage", true);
											intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
											intentForUserInfo.putExtra("userInfo", userInfo);
											startActivity(intentForUserInfo);
										} else if (profession != null
												&& (profession.contains("资深领队") || profession.contains("达人导游")))

										{
											intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
													ClubMebHomepageActivity.class);
											intentForUserInfo.putExtra("isDriverHomePage", false);
											intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
											intentForUserInfo.putExtra("userInfo", userInfo);
											startActivity(intentForUserInfo);
										}

									}
								});
								mProductJoinMebLayout.addView(imageView);
								// RoundImageView imageView1 = new
								// RoundImageView(GrouponProductNewDetailActivity.this);
								// imageView1.setImageResource(R.drawable.all_buyers_more);
								// imageView1.setLayoutParams(params);
								// imageView1.setOnClickListener(new
								// OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// Intent it = new
								// Intent(GrouponProductNewDetailActivity.this,ProductJoinMebActivity.class);
								// it.putExtra("productId", productId);
								// it.putExtra("total", total);
								// startActivity(it);
								// // finish();
								// }
								// });
								// mProductJoinMebLayout.addView(imageView1);
							}
						} else {
							// 2.给头像设置参数属性
							imageView.setImageResource(R.drawable.user_defult_photo);
							if (!StringUtils.isEmpty(userJson.optString("avatar"))) {
								ImageLoader.getInstance().displayImage(Urls.imgHost + userJson.optString("avatar"),
										imageView, AppConfig.options(R.drawable.user_defult_photo));
							}
							imageView.setLayoutParams(params);
							imageView.setTag(senderInfo);
							imageView.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									UserInfo userInfo = (UserInfo) v.getTag();
									Intent intentForUserInfo;
									if ("customer".equals(userInfo.getUserType())) {
										intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
												CustomerHomePageActivity.class);
										intentForUserInfo.putExtra("userInfo", userInfo);
										startActivity(intentForUserInfo);
										return;
									}
									OrgRole orgRole = userInfo.getOrgRole();
									if (orgRole != null) {
										if ("admin".equals(orgRole.getRoleCode())) {
											intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
													ClubFirstPageActivity.class);
											intentForUserInfo.putExtra("userInfo", userInfo);
											startActivity(intentForUserInfo);
											return;
										}
										if ("leader".equals(orgRole.getRoleCode())
												|| "guide".equals(orgRole.getRoleCode())) {
											intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
													ClubMebHomepageActivity.class);
											intentForUserInfo.putExtra("isDriverHomePage", false);
											intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
											intentForUserInfo.putExtra("userInfo", userInfo);
											startActivity(intentForUserInfo);
											return;

										} else if ("driver".equals(orgRole.getRoleCode())) {
											intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
													ClubMebHomepageActivity.class);
											intentForUserInfo.putExtra("isDriverHomePage", true);
											intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
											intentForUserInfo.putExtra("userInfo", userInfo);
											startActivity(intentForUserInfo);
											return;
										}
									}

									String profession = userInfo.getPermission();
									intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
											PersonalInfoNewActivity.class);
									if (profession != null && profession.contains("专业司机")) {
										intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
												ClubMebHomepageActivity.class);
										intentForUserInfo.putExtra("isDriverHomePage", true);
										intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
										intentForUserInfo.putExtra("userInfo", userInfo);
										startActivity(intentForUserInfo);
									} else if (profession != null
											&& (profession.contains("资深领队") || profession.contains("达人导游")))

									{
										intentForUserInfo = new Intent(GrouponProductNewDetailActivity.this,
												ClubMebHomepageActivity.class);
										intentForUserInfo.putExtra("isDriverHomePage", false);
										intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
										intentForUserInfo.putExtra("userInfo", userInfo);
										startActivity(intentForUserInfo);
									}

								}
							});
							mProductJoinMebLayout.addView(imageView);
						}

					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

		});
	}

	private void initTripDetail() {
		// for (int i = 0; i < 1; i++)
		// {
		// // 初始化数据，解决继续滑动时候如果未初始化数据导致第二页部分不显示问题
		// Journey journey = new Journey();
		// journey.setTitle("D1");
		// mJourneyList.add(journey);
		// }
		trip_detail_list = (ListView) findViewById(R.id.trip_detail_list);
		mTripPlanListAdapter = new ProductTripPlanListAdapter(this, mJourneyList, productAllImgList);
		trip_detail_list.setAdapter(mTripPlanListAdapter);
		trip_detail_list.setDividerHeight(0);
		trip_detail_list.setOnItemClickListener(mTripItemClick);
	}

	private final OnItemClickListener mTripItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			if ((Journey) parent.getAdapter().getItem(position) == null) {
				return;
			}
			Intent intent = new Intent(GrouponProductNewDetailActivity.this, TripPlanDetailNewActivity.class);
			intent.putExtra("journey", (Journey) parent.getAdapter().getItem(position));
			intent.putExtra("position", position);
			intent.putExtra("productId", productId);
			intent.putParcelableArrayListExtra("list", mJourneyList);
			startActivity(intent);
		}
	};

	private void QueryProductJourney(String productId) {
		HttpUtil.get(Urls.product_journey + productId, new JsonHttpResponseHandler() {
			@Override
			public void setRequestURI(URI requestURI) {
				// TODO Auto-generated method stub
				super.setRequestURI(requestURI);
				LogUtil.i(TAG, "QueryProductJourney", "requestURI=" + requestURI);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				LogUtil.i(TAG, "QueryProductJourney", "statusCode=" + statusCode);
				LogUtil.i(TAG, "QueryProductJourney", "response=" + response.toString());
				// scenic_spots_des.setText(response.toString());

				String code = response.optString("code");
				if (!code.equals("00000")) {
					return;
				}
				mJourneyList.clear();
				JSONObject dataJsonObject = response.optJSONObject("data");
				if (dataJsonObject == null || dataJsonObject.toString().equals("")
						|| dataJsonObject.toString().equals("null")) {
					return;
				}
				JSONArray journeyJSONList = dataJsonObject.optJSONArray("journeys");
				if (journeyJSONList == null) {
					return;
				}
				for (int i = 0; i < journeyJSONList.length(); i++) {
					JSONObject journeyJSON = journeyJSONList.optJSONObject(i);
					Journey journey = new Journey();
					journey.setId(journeyJSON.optString("id"));
					journey.setTitle(journeyJSON.optString("title"));
					journey.setDescription(journeyJSON.optString("description"));
					JSONArray trafficJSONList = journeyJSON.optJSONArray("traffic");
					if (trafficJSONList != null)
						for (int j = 0; j < trafficJSONList.length(); j++) {
							JSONObject serviceJSON = trafficJSONList.optJSONObject(j);
							ServResrouce service = new ServResrouce();
							service.setServId(serviceJSON.optString("servId"));
							service.setServName(serviceJSON.optString("servName"));
							service.setServAlias(serviceJSON.optString("servAlias"));
							service.setServType(serviceJSON.optString("servType"));
							service.setRank(serviceJSON.optString("rank"));
							journey.getTrafficList().add(service);
						}
					JSONArray stayJSONList = journeyJSON.optJSONArray("stay");
					if (stayJSONList != null)
						for (int j = 0; j < stayJSONList.length(); j++) {
							JSONObject serviceJSON = stayJSONList.optJSONObject(j);
							ServResrouce service = new ServResrouce();
							service.setServId(serviceJSON.optString("servId"));
							service.setServName(serviceJSON.optString("servName"));
							service.setServAlias(serviceJSON.optString("servAlias"));
							service.setServType(serviceJSON.optString("servType"));
							service.setRank(serviceJSON.optString("rank"));
							journey.getStayList().add(service);
						}
					JSONArray attractionsJSONList = journeyJSON.optJSONArray("attractions");
					if (attractionsJSONList != null)
						for (int j = 0; j < attractionsJSONList.length(); j++) {
							JSONObject serviceJSON = attractionsJSONList.optJSONObject(j);
							ServResrouce service = new ServResrouce();
							service.setServId(serviceJSON.optString("servId"));
							service.setServName(serviceJSON.optString("servName"));
							service.setServAlias(serviceJSON.optString("servAlias"));
							service.setServType(serviceJSON.optString("servType"));
							service.setTitleImage(serviceJSON.optString("titleImage"));
							service.setRank(serviceJSON.optString("rank"));
							journey.getAttractionsList().add(service);
						}
					mJourneyList.add(journey);
				}
				int hh = DensityUtil.dip2px(getApplicationContext(), 108);
				int hhh = (screenWidth - DensityUtil.dip2px(getApplicationContext(), 80)) / 3;
				int h = 0;
				for (Journey journey : mJourneyList) {
					h = h + hh;
					if (journey.getAttractionsList() != null && journey.getAttractionsList().size() > 0) {
						h = h + hhh;
					}
				}
				LayoutParams ls = (LayoutParams) trip_detail_list.getLayoutParams();
				ls.height = h;
				mTripPlanListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	@Override
	public void queryPicList(String productId) {
		HttpUtil.get(Urls.product_pic_url + productId, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				// ToastUtil.showToast(GrouponProductNewDetailActivity.this,
				// "error=" + throwable.getMessage());
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				LogUtil.i("queryPicList", "获取产品图片接口", response.toString());
				if (response.optString("code").equals("00000")) {
					JSONArray dataArray = response.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject picJson = dataArray.optJSONObject(i);
							ImageInfo imageInfo = new ImageInfo();
							if (!StringUtils.isEmpty(picJson.optString("url"))) {
								imageInfo.setUrl(picJson.optString("url"));
								// productTitleImgList.add(picJson.optString("url"));
							} else {
								imageInfo.setUrl("");
							}
							if (!StringUtils.isEmpty(picJson.optString("desc"))) {
								imageInfo.setDesc(picJson.optString("desc"));
								// productTitleImgList.add(picJson.optString("desc"));
							} else {
								imageInfo.setDesc("");
							}
							// LogUtil.i("imageInfo", "获取产品图片详情",
							// imageInfo.getDesc().toString());
							productAllImgList.add(imageInfo);

							mTripPlanListAdapter.notifyDataSetChanged();
						}
						// product_ser_time.setText(productAllImgList.size()+"P");
					}
				}

			}

		});
	}

	private void queryProductComment(int index, String id) {
		RequestParams params = new RequestParams();
		params.put("pagenum", "" + index);
		params.put("pagesize", "10");
		params.put("productId", id);
		HttpUtil.get(Urls.product_detail_starcomment, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				if (!response.optString("code").equals("00000")) {
					return;
				}
				// 用户评论
				RelativeLayout user_comment = (RelativeLayout) findViewById(R.id.user_comment);
				// 用户星星
				LinearLayout user_evalute_stars = (LinearLayout) findViewById(R.id.evaluate_user_star);
				// LinearLayout layout_user_evaluate =
				// (LinearLayout)findViewById(R.id.layout_user_evaluate);
				RoundImageView evaluate_user_photo = (RoundImageView) findViewById(R.id.evaluate_user_photo);
				TextView evaluate_user_name = (TextView) findViewById(R.id.evaluate_user_name);
				TextView evaluate_user_date = (TextView) findViewById(R.id.evaluate_user_date);
				TextView evaluate_user_content = (TextView) findViewById(R.id.evaluate_user_content);
				TextView evaluate_detail_button = (TextView) findViewById(R.id.product_evaluate_detail_button);
				evaluate_detail_button.setOnClickListener(GrouponProductNewDetailActivity.this);

				/***************************************************************/
				System.out.println("获取产品评论接口=" + response);
				// LogUtil.i(TAG, "QueryProductComment", "statusCode=" +
				// statusCode);
				// LogUtil.i(TAG, "QueryProductComment", "response=" +
				// response.toString());
				JSONObject dataJSON = response.optJSONObject("data");

				if (dataJSON != null && dataJSON.length() > 0) {
					String totalComment = dataJSON.optString("total");// 评论数
					mComCounts.setText("评论（" + totalComment + "条）");
					if (Integer.parseInt(totalComment) > 0) {
						// 如果有评论则可以看见评论内容
						user_comment.setVisibility(View.VISIBLE);
						evaluate_detail_button.setVisibility(View.VISIBLE);
					}
					// layout_user_evaluate.setVisibility(View.VISIBLE);
					// String descScore =
					// dataJSON.optString("descScore");// 产品描述相符评分
					// String serviceScore =
					// dataJSON.optString("servScore");// 产品服务评分
					// String averScore =
					// dataJSON.optString("avgScore");// 产品整体评分
					JSONArray commentArray = dataJSON.optJSONArray("comments");
					// comments = new ArrayList<Comments>();
					List<Comments> listss = new ArrayList<Comments>();
					if (commentArray != null && commentArray.length() > 0) {
						for (int i = 0; i < commentArray.length(); i++) {
							JSONObject commentJsonObject = commentArray.optJSONObject(i);
							Comments comment = new Comments();
							// 内容
							comment.setContent(StringUtils.unicodeRevertString(commentJsonObject.optString("content")));
							// 评分精度为小数点后1位
							comment.setAverScore(commentJsonObject.optString("score"));
							comment.setId(commentJsonObject.optString("id"));
							// 发表时间,yyyyMMddHHmmss
							comment.setCreateTime(commentJsonObject.optString("createTime"));
							JSONObject userJsonObject = commentJsonObject.optJSONObject("publisher");
							if (userJsonObject != null && !userJsonObject.equals("")) {
								UserInfo userInfo = new UserInfo();
								userInfo.setUserId(userJsonObject.optString("userId"));
								userInfo.setNickname(userJsonObject.optString("nickName"));
								userInfo.setAvatar(userJsonObject.optString("avatar"));
								userInfo.setUserType(userJsonObject.optString("userType"));
								comment.setUser(userInfo);
							}
							// 评论回复内容
							JSONArray replyArray = commentJsonObject.optJSONArray("replies");
							if (replyArray != null) {
								List<Replys> replysList = new ArrayList<Replys>();
								for (int j = 0; j < replyArray.length(); j++) {
									JSONObject replyobject = replyArray.optJSONObject(j);
									Replys replys = new Replys();
									replys.setId(replyobject.optString("id"));
									replys.setContent(
											StringUtils.unicodeRevertString(replyobject.optString("content")));
									replys.setCreateTime(replyobject.optString("createTime"));

									// 回复人
									JSONObject replyUser = replyobject.optJSONObject("publisher");
									if (replyUser != null && !replyUser.equals("")) {
										UserInfo userInfo = new UserInfo();
										userInfo.setUserId(replyUser.optString("userId"));
										userInfo.setNickname(replyUser.optString("nickName"));
										userInfo.setAvatar(replyUser.optString("avatar"));
										userInfo.setUserType(replyUser.optString("userType"));
										replys.setPublisher(userInfo);
									}
									// 回复对象
									JSONObject replyToUser = replyobject.optJSONObject("replyTo");
									if (replyToUser != null && !replyToUser.equals("")) {
										UserInfo userInfo = new UserInfo();
										userInfo.setUserId(replyToUser.optString("userId"));
										userInfo.setNickname(replyToUser.optString("nickName"));
										userInfo.setAvatar(replyToUser.optString("avatar"));
										userInfo.setUserType(replyToUser.optString("userType"));
										replys.setReplyTo(userInfo);
									}
									replysList.add(replys);
								}
								// 回复列表
								comment.setReplys(replysList);
							}
							listss.add(comment);
						}
					}
					if (listss.size() > 0) {
						commentId = listss.get(0).getId();
						String starscore = listss.get(0).getAverScore();
						// 是否有评分
						if (starscore != null && !StringUtils.isEmpty(starscore)) {
							if (starscore.contains(".")) {
								starscore = starscore.substring(0, starscore.indexOf("."));
							}
							int stars = Integer.parseInt(starscore);
							if (stars != 0) {
								if (user_evalute_stars != null) {
									user_evalute_stars.removeAllViews();
								}
								for (int i = 0; i < stars; i++) {
									ImageView view = new ImageView(GrouponProductNewDetailActivity.this);
									LayoutParams params = new LayoutParams(20, 20);
									params.setMargins(5, 0, 0, 0);
									view.setLayoutParams(params);
									view.setImageResource(R.drawable.yellow_star);
									user_evalute_stars.addView(view);
								}
							} else {

							}

						}
						evaluate_user_content.setText(listss.get(0).getContent());
						Date date = new Date();
						DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
						try {
							long time = (date.getTime() - format.parse(listss.get(0).getCreateTime()).getTime()) / 1000;
							long day1 = time / (24 * 3600);
							long hour1 = time % (24 * 3600) / 3600;
							long minute1 = time % 3600 / 60;
							if (day1 > 0) {
								evaluate_user_date.setText(day1 + "天前");
							} else if (hour1 > 0) {
								evaluate_user_date.setText(hour1 + "小时前");
							} else if (minute1 > 0) {
								evaluate_user_date.setText(minute1 + "分前");
							} else {
								if (time <= 0) {
									evaluate_user_date.setText("刚刚");
								} else {
									evaluate_user_date.setText(time + "秒前");
								}
							}
							// evaluate_user_date.setText(DateUtil.getFormateDate(comments.get(0).getCreateTime()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						evaluate_user_name.setText(listss.get(0).getUser().getNickname());
						if (!StringUtils.isEmpty(listss.get(0).getUser().getAvatar())) {
							ImageLoader.getInstance().displayImage(Urls.imgHost + listss.get(0).getUser().getAvatar(),
									evaluate_user_photo);
						} else {
							evaluate_user_photo.setImageResource(R.drawable.user_defult_photo);
						}

						initCommentReply(listss.get(0).getReplys());
					}
					evaluate_detail_button.setText("查看全部评论" + "(" + totalComment + ")");
				} else {
					user_comment.setVisibility(View.GONE);
					evaluate_detail_button.setVisibility(View.GONE);
					// layout_user_evaluate.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	private void evaProductComments(String comments) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 评论内容
			// String comment = mEditEvaEt.getText().toString();
			jsonObject.put("content", StringUtils.strConvertUnicode(comments));
			// 产品id
			jsonObject.put("objectId", productId);
			// 评论对象-产品-product
			jsonObject.put("objectType", "product");
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpUtil.post(Urls.product_detail_comment, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					// mEditEvaEt.setClickable(false);
					if ("00000".equals(response.optString("code"))) {
						ToastUtil.showToast(GrouponProductNewDetailActivity.this, "评论成功");
						// mEditEvaEt.setText("");
						// 重新刷新
						queryProductComment(1, productId);
					} else if ("00099".equals(response.optString("code"))) {
						ToastUtil.showToast(GrouponProductNewDetailActivity.this, "用户未登录");
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
	}

	private void initCommentReply(List<Replys> list) {
		// 回复列表
		evalute_reply_lv = (ListView) findViewById(R.id.evaluate_replys_all);
		replys.clear();
		replys.addAll(list);
		mUserCommentSecListAdapter = new UserCommentSecListAdapter(GrouponProductNewDetailActivity.this, replys,
				new ReplyInterface() {
					@Override
					public void replyToPublisher(String replyId, String content) {
						// ToastUtil.showToast(getApplication(),
						// "replyId="+replyId+"|content="+content);
						replyProductComments(commentId, replyId, content);
					}
				});
		evalute_reply_lv.setAdapter(mUserCommentSecListAdapter);
		mUserCommentSecListAdapter.notifyDataSetChanged();
	}

	private void replyProductComments(String commentId, String replyId, String content) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("commentId", commentId);
			jsonObject.put("content", StringUtils.strConvertUnicode(content));
			jsonObject.put("replyId", replyId);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpUtil.post(Urls.product_detail_reply, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {
						ToastUtil.showToast(getApplication(), "回复成功");
						// 回复成功刷新页面
						queryProductComment(1, productId);
					} else if ("00099".equals(response.optString("code"))) {
						ToastUtil.showToast(GrouponProductNewDetailActivity.this, "用户未登录");
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					LogUtil.i(TAG, "reply_responseString=", responseString);
					LogUtil.i(TAG, "reply_statusCode=", statusCode + "");
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initBroadcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.refreshComment");
		registerReceiver(broadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.bcinfo.refreshComment".equals(action)) {
				queryProductComment(1, productId);
			}
		}
	};

	private void initSimillarProductListView(List<ProductNewInfo> proList) {
		mSimillarListView = (ListView) findViewById(R.id.product_detail_similar_listview);
		mSimillarArrayList.addAll(proList);
		LayoutParams ls = (LayoutParams) mSimillarListView.getLayoutParams();
		int h = DensityUtil.dip2px(getApplicationContext(), 150) + 1;
		ls.height = mSimillarArrayList.size() * h;
		mSimillarListAdapter = new ProductAdapter(this, mSimillarArrayList);
		mSimillarListView.setAdapter(mSimillarListAdapter);
		mSimillarListView.setOnItemClickListener(mSimillarListListener);
	}

	OnItemClickListener mSimillarListListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Intent intent = null;
			ProductNewInfo productNewInfo = mSimillarArrayList.get(position);
			LogUtil.i(TAG, "onItemClick", "position=" + position);
			if (productNewInfo.getProductType().equals("single")) {
				if (productNewInfo.getServices().equals("traffic")) {
					intent = new Intent(GrouponProductNewDetailActivity.this, CarProductDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				} else if (productNewInfo.getServices().equals("stay")) {
					intent = new Intent(GrouponProductNewDetailActivity.this, HouseProductDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				} else {
					intent = new Intent(GrouponProductNewDetailActivity.this, GrouponProductNewDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				}
			} else if (productNewInfo.getProductType().equals("base")
					|| productNewInfo.getProductType().equals("customization")) {
				intent = new Intent(GrouponProductNewDetailActivity.this, GrouponProductNewDetailActivity.class);
				intent.putExtra("productId", productNewInfo.getId());
			} else if (productNewInfo.getProductType().equals("team")) {
				intent = new Intent(GrouponProductNewDetailActivity.this, GrouponProductNewDetailActivity.class);
				intent.putExtra("productId", productNewInfo.getId());
			}
			startActivity(intent);
			activityAnimationOpen();
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	};

	private void QuerySimillarProduct(int index, String id) {
		RequestParams params = new RequestParams();
		params.put("pagenum", "" + index);
		params.put("pagesize", "2");
		params.put("productId", id);
		HttpUtil.get(Urls.simillar_product, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				LogUtil.i(TAG, "QuerySimillarProduct", "statusCode=" + statusCode);
				LogUtil.i(TAG, "QuerySimillarProduct", "response=" + response.toString());
				JSONObject dataJSON = response.optJSONObject("data");
				ArrayList<ProductNewInfo> proList = new ArrayList<ProductNewInfo>();
				if (dataJSON != null) {
					JSONArray productsJsonList = dataJSON.optJSONArray("products");
					if (productsJsonList == null) {
						return;
					}
					for (int i = 0; i < productsJsonList.length(); i++) {
						JSONObject productJson = productsJsonList.optJSONObject(i);
						ProductNewInfo productNewInfo = new ProductNewInfo();
						productNewInfo.setId(productJson.optString("id"));
						productNewInfo.setExpStart(productJson.optString("expStart"));
						productNewInfo.setExpend(productJson.optString("expend"));
						productNewInfo.setProductType(productJson.optString("productType"));
						productNewInfo.setDistance(productJson.optString("distance"));
						productNewInfo.setTitle(productJson.optString("title"));
						productNewInfo.setPoiCount(productJson.optString("poiCount"));
						productNewInfo.setPrice(productJson.optString("price"));
						productNewInfo.setDays(productJson.optString("days"));
						productNewInfo.setDescription(productJson.optString("description"));
						productNewInfo.setPriceMax(productJson.optString("priceMax"));
						productNewInfo.setTitleImg(productJson.optString("titleImg"));
						productNewInfo.setMaxMember(productJson.optString("maxMember"));
						productNewInfo.setServices(productJson.optString("serviceCodes"));
						productNewInfo.setOriginalPrice(productJson.optString("originalPrice"));
						productNewInfo.setPv(productJson.optString("pv"));
						ArrayList<String> topics = new ArrayList<String>();
						JSONArray topicJsonArray = productJson.optJSONArray("topics");
						if (topicJsonArray != null && topicJsonArray.length() > 0) {
							for (int j = 0; j < topicJsonArray.length(); j++) {
								LogUtil.i(TAG, "topicJsonArray", topicJsonArray.optString(j));
								topics.add(topicJsonArray.optString(j));
							}
							productNewInfo.setTopics(topics);
						}
						if (productJson.optJSONObject("exts") != null) {
							HashMap<String, String> exts = new HashMap<String, String>();
							if (!StringUtils.isEmpty(productJson.optJSONObject("exts").optString("refer_tags"))) {
								exts.put("refer_tags", productJson.optJSONObject("exts").optString("refer_tags"));
							}
							if (!StringUtils.isEmpty(productJson.optJSONObject("exts").optString("big_refer_tags"))) {
								exts.put("big_refer_tags",
										productJson.optJSONObject("exts").optString("big_refer_tags"));
							}
							if (exts.size() > 0) {
								productNewInfo.setExts(exts);
							}
						}
						proList.add(productNewInfo);
					}
					initSimillarProductListView(proList);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i(TAG, "QuerySimillarProduct", "statusCode=" + statusCode);
				LogUtil.i(TAG, "QuerySimillarProduct", "responseString=" + responseString);
			}
		});
	}

	private void initBottomButton(boolean flag) {
		LinearLayout applyProductButton = (LinearLayout) findViewById(R.id.layout_apply_product_button);
		LinearLayout layout_contact_author_button = (LinearLayout) findViewById(R.id.layout_contact_author_button);
		layout_contact_author_button.setOnClickListener(this);
		if (flag) {
			applyProductButton.setOnClickListener(this);
		} else {
			applyProductButton.setBackgroundColor(getResources().getColor(R.color.gray_border));
		}
	}

	private int alpha = -1;

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY) {
		if (view != null && view == mProductScrollView) {
			alpha = positionY / 3;
			if (positionY > 220) {
				alpha = 255;
			}
			productDetailLayout.getBackground().setAlpha(alpha);
			product_title.setAlpha(alpha / 255f);

			View view1 = mProductScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = mProductScrollView.getScrollY();

			float scollY1 = ((View) mUpLayout.getParent()).getY();
			int scollHeight = mProductScrollView.getHeight();
			if (height <= scollY + scollHeight && positionY < 220) {
				mUpLayout.setVisibility(View.VISIBLE);
			} else {
				if (scollY1 == mPanel.getDownChildMaxY()) {
					if (positionY > 220) {
						mUpLayout.setVisibility(View.VISIBLE);

					} else {
						mUpLayout.setVisibility(View.INVISIBLE);
					}
				}
			}
			if (scollY1 == mPanel.getDownChildMinY())
				mUpLayout.setVisibility(View.GONE);

			View view2 = mProductScrollView.getChildAt(0);
			int height2 = view2.getMeasuredHeight();
			int scollY2 = mProductScrollView.getScrollY();
			int scollHeight2 = mProductScrollView.getHeight();
			if (height2 == scollY2 + scollHeight2) {
				// 底部
				// testProductDetailUrl(destId);
				mPanel.displayPanel();
				mUpLayout.setVisibility(View.GONE);
			}
		} else if (view != null && mScrollView == view) {
			if (positionY > 300) {
				mOperationLayout.setVisibility(View.VISIBLE);
			} else {
				mOperationLayout.setVisibility(View.GONE);
			}
		}
	}

	PullListener mPullListener = new PullListener() {
		@Override
		public void onPull(int height) {

		}
	};

	protected void onResume() {
		super.onResume();
		if (alpha != -1) {
			productDetailLayout.getBackground().setAlpha(alpha);
			product_title.setAlpha(alpha / 255f);
		}
	};

	private void addFeatureItem(ArrayList<FeatureInfo> featureLists, LinearLayout ls) {
		LayoutInflater inflater = LayoutInflater.from(this);
		for (FeatureInfo info : featureLists) {
			View convertView = inflater.inflate(R.layout.feature_list_item1, null);
			TextView mTitle = (TextView) convertView.findViewById(R.id.feature_title);
			TextView mDesc = (TextView) convertView.findViewById(R.id.feature_desc);
			LinearLayout mImgListView = (LinearLayout) convertView.findViewById(R.id.feature_secondlist);
			mTitle.setText(info.getTitle());
			mDesc.setText(info.getDesc());
			addFeatureInnerItem(info.getImages(), mImgListView);
			ls.addView(convertView);
		}
	}

	private void addFeatureInnerItem(List<ImageInfo> infos, LinearLayout ls) {
		LayoutInflater inflater = LayoutInflater.from(this);
		for (ImageInfo info : infos) {
			View convertView = inflater.inflate(R.layout.feature_list_item2, null);
			ImageView mImgUrl = (ImageView) convertView.findViewById(R.id.feature_img_url);
			TextView mImgDesc = (TextView) convertView.findViewById(R.id.feature_img_desc);

			float width = screenWidth;
			ViewGroup.LayoutParams lp = mImgUrl.getLayoutParams();
			lp.width = (int) width;
			lp.height = (int) (width / 620 * 348);
			mImgUrl.setLayoutParams(lp);
			if (!StringUtils.isEmpty(info.getUrl())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + info.getUrl(), mImgUrl);
			}
			if (!StringUtils.isEmpty(info.getDesc()) && !info.getDesc().equals("null"))
			// holder.mImgDesc.setText("\u3000\u3000"+info.getDesc());
			{
				mImgDesc.setText(info.getDesc());
				mImgDesc.setVisibility(View.VISIBLE);
			} else {
				mImgDesc.setVisibility(View.GONE);
			}
			ls.addView(convertView);
		}
	}

	private void setLlValue() {
		if (TipsDetailInfoList.size() == 0) {
			llAll.setVisibility(View.GONE);
		} else {
			setlLlChildValue(tipe1, TipsDetailInfoList.get(0));
			if (TipsDetailInfoList.size() == 1) {
				tipe2.setVisibility(View.INVISIBLE);
				TipLine1.setVisibility(View.GONE);
				TipLine2.setVisibility(View.GONE);
				((View) tipe3.getParent()).setVisibility(View.GONE);
				((View) tipe5.getParent()).setVisibility(View.GONE);
			} else {
				setlLlChildValue(tipe2, TipsDetailInfoList.get(1));
				if (TipsDetailInfoList.size() == 2) {
					TipLine1.setVisibility(View.GONE);
					TipLine2.setVisibility(View.GONE);
					((View) tipe3.getParent()).setVisibility(View.GONE);
					((View) tipe5.getParent()).setVisibility(View.GONE);
				} else {
					setlLlChildValue(tipe3, TipsDetailInfoList.get(2));
					if (TipsDetailInfoList.size() == 3) {
						TipLine2.setVisibility(View.GONE);
						tipe4.setVisibility(View.INVISIBLE);
						((View) tipe5.getParent()).setVisibility(View.GONE);
					} else {
						setlLlChildValue(tipe4, TipsDetailInfoList.get(3));
						if (TipsDetailInfoList.size() == 4) {
							TipLine2.setVisibility(View.GONE);
							((View) tipe5.getParent()).setVisibility(View.GONE);
						} else {
							setlLlChildValue(tipe5, TipsDetailInfoList.get(4));
							if (TipsDetailInfoList.size() == 5) {
								tipe6.setVisibility(View.INVISIBLE);
							} else {
								setlLlChildValue(tipe6, TipsDetailInfoList.get(5));
							}
						}
					}
				}
			}
		}

	}

	private void setlLlChildValue(LinearLayout ll, TipsDetailInfo tipInfoWarmPrompt) {
		ImageView imageView = (ImageView) ll.getChildAt(0);
		TextView textView = (TextView) ll.getChildAt(1);
		imageView.setImageResource(tipImgs[tipInfoWarmPrompt.getId()]);
		textView.setText(tipTexts[tipInfoWarmPrompt.getId()]);
		ll.setTag(tipInfoWarmPrompt);
	}

}
