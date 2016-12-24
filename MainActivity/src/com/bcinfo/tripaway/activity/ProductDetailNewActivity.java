package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductTripPlanListAdapter;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.FeatureInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.TipsDetailInfo;
import com.bcinfo.tripaway.bean.TripDetailInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView.PullListener;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 团购产品详情 不可删除，与GrouponProductNewDetailActivity内容相同，但各有不同对象调用，不可删除
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年3月26日 上午11:45:26
 */
public class ProductDetailNewActivity extends BaseActivity implements
		PersonalScrollViewListener, OnClickListener {

	private static final String TAG = "GrouponProductNewDetailActivity";

	private ProductDetailScrollView mProductScrollView;

	private ImageView mProductHeadImg;

	private ImageView user_info_blur_bg;

	private ImageView personal_v_icon_iv;

	private ImageView company_v_icon_iv;

	private LinearLayout layout_scenic_info;

	private TextView scenicspots_name;

	private TipsDetailInfo tipInfoIncludPrice;

	private TipsDetailInfo tipInfoExtrePrice;

	private TipsDetailInfo tipInfoWarmPrompt;

	public ArrayList<ImageInfo> productAllImgList = new ArrayList<ImageInfo>();

	/*
	 * 产品信息
	 */
	// private ProductInfo mProductInfo;

	/*
	 * 相似产品列表
	 */
	private MyListView mSimillarListView;

	/*
	 * 相似产品列表适配器
	 */
	private ProductAdapter mSimillarListAdapter;

	private ArrayList<Comments> comments;

	/*
	 * 相似产品列表数据
	 */
	private final ArrayList<ProductNewInfo> mSimillarArrayList = new ArrayList<ProductNewInfo>();// 产品集合

	private LinearLayout product_info_layout;

	private RelativeLayout layout_product_detail_title;

	private TextView product_title;

	private LinearLayout layout_operation_button;

	/*
	 * 产品信息id
	 */
	private String productId;

	private final ArrayList<TripDetailInfo> tripDetailInfoList = new ArrayList<TripDetailInfo>();

	private RoundImageView product_author_photo;

	private final ArrayList<Journey> mJourneyList = new ArrayList<Journey>();

	private MyListView trip_detail_list;

	private ProductTripPlanListAdapter mTripPlanListAdapter;

	private ProductNewInfo productInfo;

	// private TextView moreTimeBtn;

	private LinearLayout layout_service_list;

	private LinearLayout warmprompt_lyatyout;

	private TextView product_introduce;

	private TextView detail_product_title;

	private TextView title;

	private TextView levelTxt;

	private TextView scenic_spots_des;

	private final String hint = "hint";

	private final String product = "product";

	private final String price_include = "price_include";

	private final String price_exclusive = "price_exclusive";

	private RelativeLayout price_tip;

	// add by lij 2015/09/26 start 参与产品订购头像显示适配器
	private LinearLayout mProductJoinMebLayout;

	private TextView mProductJoinMebCountTv;

	// add by lij 2015/09/26 end

	/**
	 * 可参团的时间按钮
	 */
	private LinearLayout inGroupData;

	/**
	 * 行程日期
	 */
	private TextView tour_dates;

	private String tour_dates_text;

	private String begin_time;

	private List<AvailableTime> experiodList = new ArrayList<AvailableTime>();

	List<String> endTime;

	private List<String> startTime;

	// add by lij 2015/09/29 start 产品评价
	// 产品评价满意度
	private String score;

	private LinearLayout mEvaluteProductLayout;

	private LinearLayout mNotLoginLayout;

	// 登录
	private TextView mLoginTv;

	// 注册
	private TextView mRegisterTv;

	// 评价框
	private EditText mEditEvaEt;

	// 评价按钮
	private Button mEvaBtn;

	// 评论总数
	private TextView mComCounts;

	// add by lij 2015/09/29 end

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.groupon_product_detail_activity);
		LinearLayout backButton = (LinearLayout) findViewById(R.id.layout_back_button);
		backButton.setOnClickListener(this);
		AppManager.getAppManager().addActivity(this);

		// add by lij 2015/09/27 start
		mProductJoinMebLayout = (LinearLayout) findViewById(R.id.product_joinmem_user_photo_layout);

		// add by lij 2015/09/27 start
		mProductJoinMebLayout = (LinearLayout) findViewById(R.id.product_joinmem_user_photo_layout);

		mProductJoinMebCountTv = (TextView) findViewById(R.id.product_joinmem_user_count);
		mEvaluteProductLayout = (LinearLayout) findViewById(R.id.evalute_stars);
		mLoginTv = (TextView) findViewById(R.id.eva_login);
		mRegisterTv = (TextView) findViewById(R.id.eva_register);
		mLoginTv.setOnClickListener(this);
		mRegisterTv.setOnClickListener(this);
		mNotLoginLayout = (LinearLayout) findViewById(R.id.eva_not_login);
		mEditEvaEt = (EditText) findViewById(R.id.eva_edit_logined);
		mEvaBtn = (Button) findViewById(R.id.eva_btn);
		mComCounts = (TextView) findViewById(R.id.user_comment_count);
		if (StringUtils.isEmpty(PreferenceUtil.getUserId())) {
			// 未登录则显示登录
			mNotLoginLayout.setVisibility(View.VISIBLE);
		} else {
			// 登录则显示评论(区分订购与未订购)
			mEditEvaEt.setVisibility(View.VISIBLE);
			mEvaBtn.setOnClickListener(this);
		}

		// add by lij 2015/09/27 end

		tour_dates = (TextView) findViewById(R.id.tour_dates);

		inGroupData = (LinearLayout) findViewById(R.id.inGroupData);
		inGroupData.setOnClickListener(new OnClickListener() {
			// 点击查看出发日期
			@Override
			public void onClick(View v) {
				startTime = new ArrayList<String>();
				endTime = new ArrayList<String>();
				Intent intent_groupData = new Intent(
						ProductDetailNewActivity.this, InGroupDataDetail.class);
				if (experiodList.size() == 0) {
					startTime.add("没有开始时间数据");
					intent_groupData.putStringArrayListExtra("tour_dates",
							(ArrayList<String>) startTime);

				} else {

					for (int i = 0; i < experiodList.size(); i++) {
						startTime.add(experiodList.get(i).getBeginDate()
								.toString());
						endTime.add(experiodList.get(i).getEndDate().toString());
					}
					intent_groupData.putStringArrayListExtra("tour_dates",
							(ArrayList<String>) startTime);
					intent_groupData.putStringArrayListExtra("tour_dates2",
							(ArrayList<String>) endTime);
				}
				startActivity(intent_groupData);
				activityAnimationOpen();
			}
		});

		mProductScrollView = (ProductDetailScrollView) findViewById(R.id.product_detail_scroll_view);
		// 亮点特色
		scenicspots_name = (TextView) findViewById(R.id.scenic_spots_name);
		scenic_spots_des = (TextView) findViewById(R.id.scenic_spots_des);
		// 个人认证v
		personal_v_icon_iv = (ImageView) findViewById(R.id.personal_v_icon_iv);
		// 企业认证v
		company_v_icon_iv = (ImageView) findViewById(R.id.company_v_icon_iv);

		mProductHeadImg = (ImageView) findViewById(R.id.product_head_imageView);
		user_info_blur_bg = (ImageView) findViewById(R.id.user_info_blur_bg);
		layout_scenic_info = (LinearLayout) findViewById(R.id.layout_scenic_info);
		warmprompt_lyatyout = (LinearLayout) findViewById(R.id.warmprompt_lyatyout);
		price_tip = (RelativeLayout) findViewById(R.id.price_tip);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setScrollListener(this);
		mProductHeadImg.setOnClickListener(this);
		price_tip.setOnClickListener(this);
		warmprompt_lyatyout.setOnClickListener(this);
		mProductScrollView.setPullListener(mPullListener);
		levelTxt = (TextView) findViewById(R.id.level);
		int space = getResources().getDimensionPixelSize(
				R.dimen.detail_photo_bottom_space);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT,
				screenHeight - space - statusBarHeight);
		mProductHeadImg.setLayoutParams(p);

		ImageLoader.getInstance().displayImage(
				"http://pic25.nipic.com/20121117/6240454_094125531000_2.jpg",
				mProductHeadImg);
		mProductHeadImg.setBackgroundResource(R.color.half_translate_black);
		
		layout_operation_button = (LinearLayout) findViewById(R.id.layout_operation_button);
		product_info_layout = (LinearLayout) findViewById(R.id.product_info_layout);
		product_info_layout.setFocusable(true);
		product_info_layout.setFocusableInTouchMode(true);
		// product_info_layout.setAlpha(0);
		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(0);
		detail_product_title = (TextView) findViewById(R.id.detail_product_title);
		layout_service_list = (LinearLayout) findViewById(R.id.layout_service_list);
		// product_introduce = (TextView) findViewById(R.id.product_introduce);
		layout_service_list.setAlpha(0);
		// product_introduce.setAlpha(0);
		product_title = (TextView) findViewById(R.id.product_title);
		product_title.setAlpha(0);
		ImageView product_service_button = (ImageView) findViewById(R.id.product_service_button);
		ImageView product_service_share_button = (ImageView) findViewById(R.id.product_service_share_button);
		product_author_photo = (RoundImageView) findViewById(R.id.product_author_photo);
		// moreTimeBtn = (TextView)findViewById(R.id.more_btn);
		title = (TextView) findViewById(R.id.title);
		product_service_button.setOnClickListener(this);
		product_service_share_button.setOnClickListener(this);
		product_author_photo.setOnClickListener(this/*
													 * new OnClickListener() {
													 * 
													 * @Override public void
													 * onClick(View arg0) {
													 * 
													 * 
													 * } }
													 */);
		// moreTimeBtn.setOnClickListener(this);

		initTripDetail();

		productId = getIntent().getStringExtra("productId");
		// add by lij 2015/09/27 start
		// 初始化参与人数头像
		initJoinMebPhoto();
		// add by lij 2015/09/27 end

		initBottomButton();
		queryPicList(productId); // 查询产品所有图片
		QueryProductDetail(productId); // 查询产品细节
		QueryProductJourney(productId); // 查询产品之旅
		queryProductComment(1, productId);
		QuerySimillarProduct(1, productId); // 查询similla制品
		// String objectType=productInfo.getProductType();

		/*
		 * if(productInfo.getUser().getUserType().equals("organization")){
		 * personal_v_icon_iv.setVisibility(View.GONE);
		 * company_v_icon_iv.setVisibility(View.VISIBLE); }else
		 * if(productInfo.getUser().getUserType().equals("person")){
		 * personal_v_icon_iv.setVisibility(View.VISIBLE);
		 * company_v_icon_iv.setVisibility(View.GONE); }else{
		 * personal_v_icon_iv.setVisibility(View.GONE);
		 * company_v_icon_iv.setVisibility(View.GONE); }
		 */

		// 获得焦点
		/*
		 * inGroupData.setFocusable(true);
		 * inGroupData.setFocusableInTouchMode(true);
		 */

	}

	private final ArrayList<AvailableTime> timeList = new ArrayList<AvailableTime>();

	private void initProductInfo(ProductNewInfo productInfo) {
		UserInfo author = productInfo.getUser();
		ImageView product_service_button = (ImageView) findViewById(R.id.product_service_button);
		TextView product_author_name = (TextView) findViewById(R.id.product_author_name);
		TextView product_author_description = (TextView) findViewById(R.id.product_description);
		TextView product_price = (TextView) findViewById(R.id.product_price);
		Button product_orgname = (Button) findViewById(R.id.product_orgName);
		// TextView recruit_time = (TextView)findViewById(R.id.recruit_time);//
		// 招募期开始结束时间
		// TextView experience_time =
		// (TextView)findViewById(R.id.experience_time);// 体验期开始结束时间
		TextView tripDays = (TextView) findViewById(R.id.detail_trip_days);
		tripDays.setText(productInfo.getDays());
		product_title.setText(productInfo.getTitle());
		scenic_spots_des.setText(productInfo.getDescription());
		// add by lij 2015/10/08 start
		if (!StringUtils.isEmpty(author.getOrgInfo())) {
			product_orgname.setText("隶属" + author.getOrgInfo());
			product_orgname.setVisibility(View.VISIBLE);
		} else {
			product_orgname.setVisibility(View.GONE);
		}
		// add by lij 2015/10/08 end
		product_service_button.setOnClickListener(this);
		// product_author_photo.setOnClickListener(this);
		if (!StringUtils.isEmpty(author.getAvatar())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + author.getAvatar(), product_author_photo,
					AppConfig.options(R.drawable.user_defult_photo));
		}

		product_author_name.setText(author.getNickname());
		if (!StringUtils.isEmpty(author.getIntroduction())) {
			product_author_description.setText(author.getIntroduction());
		}
		detail_product_title.setText(productInfo.getTitle());
		// product_introduce.setText(productInfo.getDescription());

		// ===============行程日期的显示==========
		 String str="天";
	        if(productInfo.getTimeUnit().equals("week")){
	        	str="周";
	        }else 
	        	if(productInfo.getTimeUnit().equals("month")){
	            	str="月";
	            }
	        tour_dates.setText("行程" + productInfo.getDays() + str);
	        
		product_price.setText("￥" + productInfo.getPrice());
		if(!StringUtils.isEmpty(productInfo.getTitleImg())){
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + productInfo.getTitleImg(), mProductHeadImg);
		}

		mProductHeadImg.setBackgroundResource(R.color.half_translate_black);
		/*
		 * if(productInfo.getUser().getUserType().equals("organization")){
		 * personal_v_icon_iv.setVisibility(View.GONE);
		 * company_v_icon_iv.setVisibility(View.VISIBLE); }else
		 * if(productInfo.getUser().getUserType().equals("person")){
		 * personal_v_icon_iv.setVisibility(View.VISIBLE);
		 * company_v_icon_iv.setVisibility(View.GONE); }else{
		 * personal_v_icon_iv.setVisibility(View.GONE);
		 * company_v_icon_iv.setVisibility(View.GONE); }
		 */
		// ****************************给详情页添加下拉显示主题标签*********************************

		List<String> topicNameTvArray = productInfo.getTopics();
		if (topicNameTvArray != null && topicNameTvArray.size() != 0) {

			for (int i = 0; i < topicNameTvArray.size(); i++) {
				TextView labelTv = new TextView(this);
				View view = new View(this);
				view.setLayoutParams(new LayoutParams(5, 5));
				labelTv.setText(productInfo.getTopics().get(i));// 设置内容
				labelTv.setTextSize(12);// 设置字体大小
				labelTv.setSingleLine(true);
				labelTv.setTextColor(this.getResources()
						.getColor(R.color.white));// 设置字体颜色
				// labelTv.setPadding(5,5,5,5);//设置四周留白

				labelTv.setGravity(Gravity.CENTER);
				labelTv.setBackgroundResource(R.drawable.white_shap);
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
		// MyListView timeLst = (MyListView)findViewById(R.id.time_lst);

		// AvailableTime availableTime = new AvailableTime();
		// availableTime.setBeginDate("20150122");
		// availableTime.setEndDate("20150131");
		// timeList.add(availableTime);
		// AvailableTime availableTime1 = new AvailableTime();
		// availableTime1.setBeginDate("20150202");
		// availableTime1.setEndDate("20150211");
		// timeList.add(availableTime1);
		// AvailableTime availableTime2 = new AvailableTime();
		// availableTime2.setBeginDate("20150212");
		// availableTime2.setEndDate("20150221");
		// timeList.add(availableTime2);
		// AvailableTime availableTime3 = new AvailableTime();
		// availableTime3.setBeginDate("20150404");
		// availableTime3.setEndDate("20150415");
		// timeList.add(availableTime3);
		// AvailableTime availableTime4 = new AvailableTime();
		// availableTime4.setBeginDate("20150511");
		// availableTime4.setEndDate("20150521");
		// timeList.add(availableTime4);
		// AvailableTime availableTime5 = new AvailableTime();
		// availableTime5.setBeginDate("20150522");
		// availableTime5.setEndDate("20150531");
		// timeList.add(availableTime5);
		// AvailableTime availableTime6 = new AvailableTime();
		// availableTime6.setBeginDate("20150710");
		// availableTime6.setEndDate("20150722");

		// 日期 8.20注释
		// timeList.addAll(productInfo.getExpPeriodList());
		// if (timeList.size() > 2)
		// {
		// moreTimeBtn.setVisibility(View.VISIBLE);
		// }
		// ExpPeriodAdapter adapter = new ExpPeriodAdapter(this, /*
		// productInfo.getExpPeriodList() */timeList);
		// timeLst.setAdapter(adapter);

		// **********用来增加下拉图片显示（trip，house，car）********************************
		/*
		 * String[] services = productInfo.getServices().split(","); for (int i
		 * = 0; i < services.length; i++) { ImageView serviceImg = new
		 * ImageView(this); int imgSize = getResources().getDimensionPixelSize(
		 * R.dimen.product_service_img_size); LayoutParams parm = new
		 * LayoutParams(imgSize, imgSize); parm.leftMargin = 10;
		 * parm.rightMargin = 10; serviceImg.setLayoutParams(parm); String
		 * serviceStr = services[i]; if (serviceStr.equals("attractions")) {
		 * serviceImg.setImageResource(R.drawable.service_trip); } else if
		 * (serviceStr.equals("stay")) {
		 * serviceImg.setImageResource(R.drawable.service_house); } else if
		 * (serviceStr.equals("traffic")) {
		 * serviceImg.setImageResource(R.drawable.service_car); } //
		 * serviceImg.setImageResource(R.drawable.service_order_ticket);
		 * layout_service_list.addView(serviceImg); }
		 */
	}

	/******************* 产品简要信息 ********************/
	// LinearLayout servicesLine =
	// (LinearLayout)LayoutInflater.from(this).inflate(R.layout.product_detail_services_h,
	// null);
	// servicesLine.addView(getServiceItem(productInfo.getPoiCount() + "景点",
	// R.drawable.flag_scenery));
	// servicesLine.addView(getServiceItem(productInfo.getDistance() + "KM",
	// R.drawable.distance));
	// servicesLine.addView(getServiceItem(productInfo.getDays() + "天",
	// R.drawable.flag_calendar));
	// layout_scenic_info.addView(servicesLine);

	/**
	 * 初始化底部按钮
	 */
	private void initBottomButton() {
		LinearLayout applyProductButton = (LinearLayout) findViewById(R.id.layout_apply_product_button);
		LinearLayout layout_contact_author_button = (LinearLayout) findViewById(R.id.layout_contact_author_button);
		applyProductButton.setOnClickListener(this);
		layout_contact_author_button.setOnClickListener(this);
	}

	/**
	 * 初始化景点信息 亮点特色
	 */

	// private void initScenicSpots(ProductNewInfo productInfo){
	// ArrayList<FeatureInfo> infoList = productInfo.getFeatures();
	//
	// for (int i = 0; i < infoList.size(); i++) {
	// FeatureInfo info = infoList.get(i);
	// scenicspots_name.setText(info.getTitle());
	// scenic_spots_des.setText(info.getDesc());
	// }
	//
	//
	//
	//
	// }

	// private void initScenicSpots(ProductNewInfo productInfo)
	// {
	// ArrayList<FeatureInfo> infoList = productInfo.getFeatures();
	// LinearLayout scenic_spots_table =
	// (LinearLayout)findViewById(R.id.scenic_spots_layout);
	// if (infoList.size() == 0)
	// {
	// title.setVisibility(View.GONE);
	// scenic_spots_table.setVisibility(View.GONE);
	// }
	// for (int i = 0; i < infoList.size(); i++)
	// {
	// FeatureInfo info = infoList.get(i);
	// LinearLayout item =
	// (LinearLayout)LayoutInflater.from(this).inflate(R.layout.scenic_spots_listview_item,
	// null);
	// TextView scenic_spots_name =
	// (TextView)item.findViewById(R.id.scenic_spots_name);
	// TextView scenic_spots_des =
	// (TextView)item.findViewById(R.id.scenic_spots_des);
	// LinearLayout photos_total_layout =
	// (LinearLayout)item.findViewById(R.id.scenic_spots_photo_layout);
	// scenic_spots_name.setText(info.getTitle());
	// scenic_spots_des.setText(info.getDesc());
	// ArrayList<String> photos = info.getImageUrls();
	// LinearLayout photo_hori_layout = new LinearLayout(this);
	// for (int j = 0; j < photos.size(); j++)
	// {
	// LinearLayout photoItem =
	// (LinearLayout)LayoutInflater.from(this).inflate(R.layout.scenic_spots_gridview_item,
	// null);
	// photoItem.setTag(R.id.tag_photo_list, photos);// list集合
	// photoItem.setTag(R.id.tag_photo_index, j);// 图片索引
	// photoItem.setOnClickListener(mOnClickListener);
	// ImageView scenic_spots_photo =
	// (ImageView)photoItem.findViewById(R.id.scenic_spots_photo);
	// ImageLoader.getInstance().displayImage(photos.get(j),
	// scenic_spots_photo);
	// if (j == 5)// 判断j是否能被5整除
	// {
	// photo_hori_layout = new LinearLayout(this);
	// photo_hori_layout.setOrientation(LinearLayout.HORIZONTAL);
	// photo_hori_layout.setLayoutParams(new
	// LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// photos_total_layout.addView(photo_hori_layout);
	// }
	// photo_hori_layout.addView(photoItem);
	// }
	// if (i != 0)
	// {
	// // 分隔条
	// LinearLayout devider =
	// (LinearLayout)LayoutInflater.from(this).inflate(R.layout.scenic_spots_devider,
	// null);
	// scenic_spots_table.addView(devider);
	// }
	// scenic_spots_table.addView(item);
	// }
	// }

	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getTag(R.id.tag_photo_list) != null) {
				@SuppressWarnings("unchecked")
				ArrayList<String> photos = (ArrayList<String>) v
						.getTag(R.id.tag_photo_list);// 照片集合
				int index = (Integer) v.getTag(R.id.tag_photo_index);// 点击的index
				Intent intentForView = new Intent(
						ProductDetailNewActivity.this,
						ImageViewerActivity.class);
				intentForView.putExtra("image_index", index);
				intentForView.putStringArrayListExtra("image_urls", photos);
				startActivity(intentForView);
				activityAnimationOpen();
			}
		}
	};

	/**
	 * 初始化详细行程
	 */
	private void initTripDetail() {
		// for (int i = 0; i < 1; i++)
		// {
		// // 初始化数据，解决继续滑动时候如果未初始化数据导致第二页部分不显示问题
		// Journey journey = new Journey();
		// journey.setTitle("D1");
		// mJourneyList.add(journey);
		// }
		trip_detail_list = (MyListView) findViewById(R.id.trip_detail_list);
		mTripPlanListAdapter = new ProductTripPlanListAdapter(this,
				mJourneyList, productAllImgList);
		trip_detail_list.setAdapter(mTripPlanListAdapter);
		trip_detail_list.setDividerHeight(0);
		trip_detail_list.setOnItemClickListener(mTripItemClick);
	}

	private final OnItemClickListener mTripItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if ((Journey) parent.getAdapter().getItem(position) == null) {
				return;
			}
			Intent intent = new Intent(ProductDetailNewActivity.this,
					TripPlanDetailNewActivity.class);
			intent.putExtra("journey",
					(Journey) parent.getAdapter().getItem(position));
			intent.putExtra("position", position);
			intent.putExtra("productId", productId);
			intent.putParcelableArrayListExtra("list", mJourneyList);
			startActivity(intent);
		}
	};

	// /**
	// * 初始化用户评论
	// */
	// private void initUserComment()
	// {
	// RoundImageView evaluate_user_photo =
	// (RoundImageView)findViewById(R.id.evaluate_user_photo);
	// TextView evaluate_user_name =
	// (TextView)findViewById(R.id.evaluate_user_name);
	// TextView evaluate_user_date =
	// (TextView)findViewById(R.id.evaluate_user_date);
	// TextView evaluate_user_content =
	// (TextView)findViewById(R.id.evaluate_user_content);
	// TextView evaluate_detail_button =
	// (TextView)findViewById(R.id.product_evaluate_detail_button);
	// evaluate_detail_button.setOnClickListener(this);
	// String userPhoto =
	// "http://h.hiphotos.baidu.com/image/pic/item/a5c27d1ed21b0ef4cc365758dec451da80cb3ede.jpg";
	// ImageLoader.getInstance().displayImage(userPhoto, evaluate_user_photo);
	// }

	/**
	 * 初始化相似产品列表
	 */
	private void initSimillarProductListView(List<ProductNewInfo> proList) {
		mSimillarListView = (MyListView) findViewById(R.id.product_detail_similar_listview);
		mSimillarArrayList.addAll(proList);
		mSimillarListAdapter = new ProductAdapter(this, mSimillarArrayList);
		mSimillarListView.setAdapter(mSimillarListAdapter);
		mSimillarListView.setOnItemClickListener(mSimillarListListener);
	}

	OnItemClickListener mSimillarListListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = null;
			ProductNewInfo productNewInfo = mSimillarArrayList.get(position);
			LogUtil.i(TAG, "onItemClick", "position=" + position);
			if (productNewInfo.getProductType().equals("single")) {
				if (productNewInfo.getServices().equals("traffic")) {
					intent = new Intent(ProductDetailNewActivity.this,
							CarProductDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				} else if (productNewInfo.getServices().equals("stay")) {
					intent = new Intent(ProductDetailNewActivity.this,
							HouseProductDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				} else {
					intent = new Intent(ProductDetailNewActivity.this,
							GrouponProductNewDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				}
			} else if (productNewInfo.getProductType().equals("base")
					|| productNewInfo.getProductType().equals("customization")) {
				intent = new Intent(ProductDetailNewActivity.this,
						GrouponProductNewDetailActivity.class);
				intent.putExtra("productId", productNewInfo.getId());
			} else if (productNewInfo.getProductType().equals("team")) {
				intent = new Intent(ProductDetailNewActivity.this,
						GrouponProductNewDetailActivity.class);
				intent.putExtra("productId", productNewInfo.getId());
			}
			startActivity(intent);
			activityAnimationOpen();
		}
	};

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY,
			int prePositionX, int prePositionY) {

		// TODO Auto-generated method stub
		if (view != null && view == mProductScrollView) {
			int alpha = positionY / 3;
			if (positionY < 50 || positionY == 50) {
				detail_product_title.setAlpha(1f);
			} else {
				detail_product_title.setAlpha(0);
			}

			if (positionY > 220)

			{
				alpha = 255;
			}
			layout_product_detail_title.getBackground().setAlpha(alpha);
			product_title.setAlpha(alpha / 255f);
		} else {
			return;
		}
	}

	PullListener mPullListener = new PullListener() {
		@Override
		public void onPull(int height) {
			// TODO Auto-generated method stub
			LogUtil.i(TAG, "mPullListener", "height=" + height);
			float alpha = height * 3 - 200;
			if (alpha > 255) {
				alpha = 255;
			}
			if (alpha < 50) {
				alpha = 0;
			}
			layout_service_list.setAlpha(alpha / 255f);
			// product_introduce.setAlpha(alpha / 255f);
		}
	};

	private ShareSelectDialog ShareSelectDialog;

	/**
	 * 初始化LinearLayout内部item
	 * 
	 * @param name
	 * @param res
	 * @return
	 */
	private LinearLayout getServiceItem(String name, int res) {
		LinearLayout serviceItem = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.product_services_item, null);
		ImageView serviceLogo = (ImageView) serviceItem
				.findViewById(R.id.product_service_logo);
		TextView serviceName = (TextView) serviceItem
				.findViewById(R.id.product_service_name);
		serviceLogo.setImageResource(res);
		serviceName.setText(name);
		return serviceItem;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.product_head_imageView:
			if (productTitleImgList.size() > 0) {
				Intent intentForView = new Intent(
						ProductDetailNewActivity.this,
						ImageViewerActivity.class);
				intentForView.putExtra("image_index", 0);
				intentForView.putParcelableArrayListExtra("image_urls",
						productTitleImgList);
				startActivity(intentForView);
			}
			break;
		case R.id.warmprompt_lyatyout:
			intent = new Intent(this, WarmpromptTipActivity.class);
			// QueryTipDetail(productId, product, hint);
			intent.putExtra("productId", productId);
			startActivity(intent);
			activityAnimationOpen();
			break;
		case R.id.price_tip:
			intent = new Intent(this, PriceTipActivity.class);
			intent.putExtra("productId", productId);
			intent.putExtra("productInfo", productInfo);
			// QueryPriceInclud(productId, product, price_include);
			// intent.putExtra("price_include",tipInfoIncludPrice.getContent()
			// );
			// QueryTipPriceExtre(productId, product, price_exclusive);
			// intent.putExtra("price_exclusive",tipInfoExtrePrice.getContent()
			// );
			startActivity(intent);
			activityAnimationOpen();
			break;
		// case R.id.more_btn:
		// Intent intent2 = new Intent(GrouponProductNewDetailActivity.this,
		// ExpPeriodTimeDialog.class);
		// intent2.putParcelableArrayListExtra("timeList", timeList);
		// startActivity(intent2);
		// activityAnimationOpen();
		// break;
		case R.id.layout_back_button:
			if (isFromLoad) {
				Intent intent1 = new Intent(ProductDetailNewActivity.this,
						MainActivity.class);
				startActivity(intent1);
			}
			finish();
			activityAnimationClose();
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
		case R.id.product_service_share_button:
			ShareSelectDialog = new ShareSelectDialog(
					ProductDetailNewActivity.this, productInfo.getTitleImg(),
					productInfo.getDescription(), productInfo.getTitle(),
					productInfo.getId(),Urls.ShareUrlOfProduct,productInfo.getTitle());
			ShareSelectDialog.show();
			break;
		case R.id.product_evaluate_detail_button:
			intent = new Intent(this, UserCommentActivity.class);
			intent.putExtra("productId", productId);
			startActivity(intent);
			activityAnimationOpen();
			break;

		// ====订购界面========================================================
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

			startTime = new ArrayList<String>();
			endTime = new ArrayList<String>();
			if (experiodList.size() == 0) {
				startTime.add("没有开始时间数据");
				intent.putStringArrayListExtra("tour_dates",
						(ArrayList<String>) startTime);

			} else {

				for (int i = 0; i < experiodList.size(); i++) {
					startTime
							.add(experiodList.get(i).getBeginDate().toString());
					endTime.add(experiodList.get(i).getEndDate().toString());
				}
				intent.putStringArrayListExtra("tour_dates",
						(ArrayList<String>) startTime);
				intent.putStringArrayListExtra("tour_dates2",
						(ArrayList<String>) endTime);
			}

			startActivity(intent);
			activityAnimationOpen();
			break;

		case R.id.layout_contact_author_button:
			if (!AppInfo.getIsLogin()) {
				goLoginActivity();
				return;
			}
			Intent talkIntent = new Intent(ProductDetailNewActivity.this,
					ChatActivity.class);
			talkIntent.putExtra("receiveId", productInfo.getUser().getUserId());
			talkIntent.putExtra("title", productInfo.getUser().getNickname());
			startActivity(talkIntent);
			activityAnimationOpen();
			break;
		case R.id.product_author_photo:
			intent = new Intent(this, PersonalHomepageActivity.class);
			intent.putExtra("productId", productId);
			intent.putExtra("userId", productInfo.getUser().getUserId());
			startActivity(intent);
			activityAnimationOpen();

			// ServicerPersonInfoDialog dialog = new
			// ServicerPersonInfoDialog(this);
			// dialog.show();
			break;
		// add by lij 2015/09/29 start
		// 产品 评论 模块
		case R.id.eva_login:
			// 登录
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent, 100);
			this.overridePendingTransition(R.anim.activity_new,
					R.anim.activity_out);
			finish();
			break;
		case R.id.eva_register:
			Intent registerIntent = new Intent(this, RegisterActivity.class);
			startActivity(registerIntent);
			this.overridePendingTransition(R.anim.activity_new,
					R.anim.activity_out);
			// 注册
			break;
		case R.id.eva_btn:
			// 评论
			evaProductComments();
			break;
		// add by lij 2015/09/29 end
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		UMSsoHandler ssoHandler = ShareSelectDialog.mController.getConfig()
				.getSsoHandler(resultCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	/***************************** 查询图片 ******************************/
	@Override
	public void queryPicList(String productId) {
		HttpUtil.get(Urls.product_pic_url + productId,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
//						ToastUtil.showToast(ProductDetailNewActivity.this,
//								"error=" + throwable.getMessage());
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						LogUtil.i("queryPicList", "获取产品图片接口",
								response.toString());
						if (response.optString("code").equals("00000")) {
							JSONArray dataArray = response.optJSONArray("data");
							if (dataArray != null && dataArray.length() > 0) {
								for (int i = 0; i < dataArray.length(); i++) {
									JSONObject picJson = dataArray
											.optJSONObject(i);
									ImageInfo imageInfo = new ImageInfo();
									if (!StringUtils.isEmpty(picJson
											.optString("url"))) {
										imageInfo.setUrl(picJson
												.optString("url"));
										// productTitleImgList.add(picJson.optString("url"));
									} else {
										imageInfo.setUrl("");
									}
									if (!StringUtils.isEmpty(picJson
											.optString("desc"))) {
										imageInfo.setDesc(picJson
												.optString("desc"));
										// productTitleImgList.add(picJson.optString("desc"));
									} else {
										imageInfo.setDesc("");
									}
									// LogUtil.i("imageInfo", "获取产品图片详情",
									// imageInfo.getDesc().toString());
									productAllImgList.add(imageInfo);
									mTripPlanListAdapter.notifyDataSetChanged();
								}
							}
						}

					}

				});
	}

	/****************** request *******************/
	private void QueryProductDetail(String productId) {
		// LogUtil.e(TAG, "QueryProductDetail", Urls.product_detail +
		// productId);
		HttpUtil.get(Urls.product_detail + productId,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// LogUtil.i(TAG, "QueryProductDetail", "statusCode=" +
						// statusCode);
						LogUtil.i(TAG, "QueryProductDetail", "response="
								+ response.toString());
						productInfo = new ProductNewInfo();
						String code = response.optString("code");
						String msg = response.optString("msg");
//						if (!code.equals("00000")) {
//							
//
//							// ToastUtil.showErrorToast(ProductDetailNewActivity.this,
//							// msg);
//
//							return;
//						}
						JSONObject data = response.optJSONObject("data");
						// 产品星级
						score = data.optString("score");
						// add by lij 2015/09/29 start
						if (score != null) {
							if (score.contains(".")) {
								score = score.substring(0, score.indexOf("."));
							}
							int stars = Integer.parseInt(score);
							for (int i = 0; i < stars; i++) {
								ImageView view = new ImageView(
										ProductDetailNewActivity.this);
								LayoutParams params = new LayoutParams(20, 20);
								params.setMargins(5, 0, 0, 0);
								view.setLayoutParams(params);
								view.setImageResource(R.drawable.yellow_star);
								mEvaluteProductLayout.addView(view);
							}
						}
						TextView textview = new TextView(
								ProductDetailNewActivity.this);
						LayoutParams params = new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						;
						params.setMargins(5, 0, 0, 0);
						textview.setLayoutParams(params);
						textview.setText(score + "分");
						textview.setTextColor(Color.RED);
						mEvaluteProductLayout.addView(textview);
						// add by lij 2015/09/29 end
						JSONObject product = data.optJSONObject("product");
						JSONArray expPeriodArray = product
								.optJSONArray("expPeriod");
						if (expPeriodArray != null) {
							// =====行程的开始日期
							experiodList = new ArrayList<AvailableTime>();
							for (int i = 0; i < expPeriodArray.length(); i++) {
								JSONObject experiodJson = expPeriodArray
										.optJSONObject(i);
								AvailableTime availableTime = new AvailableTime();
								availableTime.setBeginDate(experiodJson
										.optString("beginDate"));
								availableTime.setEndDate(experiodJson
										.optString("endDate"));
								experiodList.add(availableTime);
								// //////////////////////////////
								begin_time = availableTime.getBeginDate();
							}
							productInfo.setExpPeriodList(experiodList);
						}
						JSONArray topics = product.optJSONArray("topics");
						if (topics != null) {
							for (int i = 0; i < topics.length(); i++) {
								productInfo.getTopics().add(
										topics.opt(i).toString());
							}
						}
						productInfo.setId(product.optString("id"));
						productInfo.setDistance(product.optString("distance"));
						productInfo.setTitle(product.optString("title"));
						productInfo.setPoiCount(product.optString("poiCount"));
						productInfo.setPrice(product.optString("price"));
						productInfo.setDays(product.optString("days"));
					     productInfo.setTimeUnit(product.optString("timeUnit"));
			                if(productInfo.getTimeUnit()!=null){
			                    if(productInfo.getTimeUnit().equals("week")){
			                    	 mTripPlanListAdapter.setTimeUnit("周");
			                    }
			                    else
			                    	if(productInfo.getTimeUnit().equals("month")){
			                    		 mTripPlanListAdapter.setTimeUnit("月");
			                    	}
			                }
			                
						productInfo.setDescription(product
								.optString("description")); // 内容
						productInfo.setPriceMax(product.optString("priceMax"));
						productInfo.setTitleImg(product.optString("titleImg")); // 标题图片
						productInfo.setMaxMember(product.optString("maxMember"));
						productInfo.setProductType(product
								.optString("productType"));
						productInfo.setCustomFor(data.optString("customFor"));
						productInfo.setServices(product
								.optString("serviceCodes"));
						try {
							productInfo.setLevel(product
									.optJSONObject("policy").getString("type"));
							productInfo.setPolicyContent(product.optJSONObject("policy").getString("content"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						JSONObject userJSON = product.optJSONObject("creater");
						if (userJSON != null) {
							productInfo.getUser().setUserId(
									userJSON.optString("userId"));
							productInfo.getUser().setNickname(
									userJSON.optString("nickName"));
							productInfo.getUser().setAvatar(
									userJSON.optString("avatar"));
							productInfo.getUser().setIntroduction(
									userJSON.optString("introduction"));
							productInfo.getUser().setUserType(
									userJSON.optString("userType"));
						}
						JSONArray featuresJSONList = data
								.optJSONArray("features");
						if (featuresJSONList != null) {
							for (int i = 0; i < featuresJSONList.length(); i++) {
								JSONObject featureJSON = featuresJSONList
										.optJSONObject(i);
								FeatureInfo feature = new FeatureInfo();
								feature.setId(featureJSON.optString("id"));
								feature.setTitle(featureJSON.optString("title"));
								feature.setDesc(featureJSON.optString("desc"));
								JSONArray imagesJSONList = featureJSON
										.optJSONArray("images");
								if (imagesJSONList != null) {
									for (int j = 0; j < imagesJSONList.length(); j++) {
										JSONObject imagesJSON = imagesJSONList
												.optJSONObject(j);
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
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "QueryProductDetail", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryProductDetail", "responseString="
								+ responseString);
						for (int i = 0; i < headers.length; i++) {
							LogUtil.i(TAG, "QueryProductDetail", "key-->"
									+ headers[i].getName() + "--getValue-->"
									+ headers[i].getValue());
						}
					}

				});
	}

	private void QueryProductJourney(String productId) {
		HttpUtil.get(Urls.product_journey + productId,
				new JsonHttpResponseHandler() {
					@Override
					public void setRequestURI(URI requestURI) {
						// TODO Auto-generated method stub
						super.setRequestURI(requestURI);
						LogUtil.i(TAG, "QueryProductJourney", "requestURI="
								+ requestURI);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogUtil.i(TAG, "QueryProductJourney", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryProductJourney", "response="
								+ response.toString());
						// scenic_spots_des.setText(response.toString());

						String code = response.optString("code");
						if (!code.equals("00000")) {
							return;
						}
						mJourneyList.clear();
						JSONObject dataJsonObject = response
								.optJSONObject("data");
						if (dataJsonObject == null
								|| dataJsonObject.toString().equals("")
								|| dataJsonObject.toString().equals("null")) {
							return;
						}
						JSONArray journeyJSONList = dataJsonObject
								.optJSONArray("journeys");
						if (journeyJSONList == null) {
							return;
						}
						for (int i = 0; i < journeyJSONList.length(); i++) {
							JSONObject journeyJSON = journeyJSONList
									.optJSONObject(i);
							Journey journey = new Journey();
							journey.setId(journeyJSON.optString("id"));
							journey.setTitle(journeyJSON.optString("title"));
							journey.setDescription(journeyJSON
									.optString("description"));
							JSONArray trafficJSONList = journeyJSON
									.optJSONArray("traffic");
							if (trafficJSONList != null)
								for (int j = 0; j < trafficJSONList.length(); j++) {
									JSONObject serviceJSON = trafficJSONList
											.optJSONObject(j);
									ServResrouce service = new ServResrouce();
									service.setServId(serviceJSON
											.optString("servId"));
									service.setServName(serviceJSON
											.optString("servName"));
									service.setServAlias(serviceJSON
											.optString("servAlias"));
									service.setServType(serviceJSON
											.optString("servType"));
									service.setRank(serviceJSON
											.optString("rank"));
									journey.getTrafficList().add(service);
								}
							JSONArray stayJSONList = journeyJSON
									.optJSONArray("stay");
							if (stayJSONList != null)
								for (int j = 0; j < stayJSONList.length(); j++) {
									JSONObject serviceJSON = stayJSONList
											.optJSONObject(j);
									ServResrouce service = new ServResrouce();
									service.setServId(serviceJSON
											.optString("servId"));
									service.setServName(serviceJSON
											.optString("servName"));
									service.setServAlias(serviceJSON
											.optString("servAlias"));
									service.setServType(serviceJSON
											.optString("servType"));
									service.setRank(serviceJSON
											.optString("rank"));
									journey.getStayList().add(service);
								}
							JSONArray attractionsJSONList = journeyJSON
									.optJSONArray("attractions");
							if (attractionsJSONList != null)
								for (int j = 0; j < attractionsJSONList
										.length(); j++) {
									JSONObject serviceJSON = attractionsJSONList
											.optJSONObject(j);
									ServResrouce service = new ServResrouce();
									service.setServId(serviceJSON
											.optString("servId"));
									service.setServName(serviceJSON
											.optString("servName"));
									service.setServAlias(serviceJSON
											.optString("servAlias"));
									service.setServType(serviceJSON
											.optString("servType"));
									service.setTitleImage(serviceJSON
											.optString("titleImage"));
									service.setRank(serviceJSON
											.optString("rank"));
									journey.getAttractionsList().add(service);
								}
							mJourneyList.add(journey);
						}
						mTripPlanListAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "QueryProductJourney", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryProductJourney", "responseString="
								+ responseString);
					}
				});
	}

	private void queryProductComment(int index, String id) {
		RequestParams params = new RequestParams();
		params.put("pagenum", "" + index);
		params.put("pagesize", "10");
		params.put("productId", id);
		HttpUtil.get(Urls.product_detail_starcomment, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if (!response.optString("code").equals("00000")) {
							return;
						}
						// 用户评论
						RelativeLayout user_comment = (RelativeLayout) findViewById(R.id.user_comment);
						LinearLayout layout_user_evaluate = (LinearLayout) findViewById(R.id.layout_user_evaluate);
						RoundImageView evaluate_user_photo = (RoundImageView) findViewById(R.id.evaluate_user_photo);
						TextView evaluate_user_name = (TextView) findViewById(R.id.evaluate_user_name);
						TextView evaluate_user_date = (TextView) findViewById(R.id.evaluate_user_date);
						TextView evaluate_user_content = (TextView) findViewById(R.id.evaluate_user_content);
						TextView evaluate_detail_button = (TextView) findViewById(R.id.product_evaluate_detail_button);
						evaluate_detail_button
								.setOnClickListener(ProductDetailNewActivity.this);
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
							}
							// layout_user_evaluate.setVisibility(View.VISIBLE);
							// String descScore =
							// dataJSON.optString("descScore");// 产品描述相符评分
							// String serviceScore =
							// dataJSON.optString("servScore");// 产品服务评分
							// String averScore =
							// dataJSON.optString("avgScore");// 产品整体评分
							JSONArray commentArray = dataJSON
									.optJSONArray("comments");
							comments = new ArrayList<Comments>();
							if (commentArray != null
									&& commentArray.length() > 0) {
								for (int i = 0; i < commentArray.length(); i++) {
									JSONObject commentJsonObject = commentArray
											.optJSONObject(i);
									Comments comment = new Comments();
									// 内容
									comment.setContent(commentJsonObject
											.optString("content"));
									// 评分精度为小数点后1位
									comment.setAverScore(commentJsonObject
											.optString("score"));
									// comment.setId(commentJsonObject.optString("id"));
									// 发表时间,yyyyMMddHHmmss
									comment.setCreateTime(commentJsonObject
											.optString("createTime"));
									JSONObject userJsonObject = commentJsonObject
											.optJSONObject("publisher");
									if (userJsonObject != null
											&& !userJsonObject.equals("")) {
										UserInfo userInfo = new UserInfo();
										userInfo.setUserId(userJsonObject
												.optString("userId"));
										userInfo.setNickname(userJsonObject
												.optString("nickName"));
										userInfo.setAvatar(userJsonObject
												.optString("avatar"));
										userInfo.setUserType(userJsonObject
												.optString("userType"));
										// userInfo.setGender(userJsonObject.optString("sex"));
										// userInfo.setAddress(userJsonObject.optString("address"));
										// userInfo.setStatus(userJsonObject.optString("status"));
										// userInfo.setEmail(userJsonObject.optString("email"));
										// userInfo.setRole(userJsonObject.optString("role"));
										// userInfo.setPermission(userJsonObject.optString("permission"));
										//
										// userInfo.setIntroduction(userJsonObject.optString("introduction"));
										// userInfo.setMobile(userJsonObject.optString("mobile"));
										comment.setUser(userInfo);
									}
									// 评论回复内容
									JSONArray replyArray = commentJsonObject
											.optJSONArray("replies");
									if (replyArray != null) {
										List<Replys> replysList = new ArrayList<Replys>();
										for (int j = 0; j < replyArray.length(); j++) {
											JSONObject replyobject = replyArray
													.optJSONObject(j);
											Replys replys = new Replys();
											replys.setId(replyobject
													.optString("id"));
											replys.setContent(replyobject
													.optString("content"));
											replys.setCreateTime(replyobject
													.optString("createTime"));

											// 回复人
											JSONObject replyUser = replyobject
													.optJSONObject("publisher");
											if (replyUser != null
													&& !replyUser.equals("")) {
												UserInfo userInfo = new UserInfo();
												userInfo.setUserId(replyUser
														.optString("userId"));
												userInfo.setNickname(replyUser
														.optString("nickName"));
												userInfo.setAvatar(replyUser
														.optString("avatar"));
												userInfo.setUserType(replyUser
														.optString("userType"));
												replys.setPublisher(userInfo);
											}
											// 回复对象
											JSONObject replyToUser = replyobject
													.optJSONObject("replyTo");
											if (replyToUser != null
													&& !replyToUser.equals("")) {
												UserInfo userInfo = new UserInfo();
												userInfo.setUserId(replyToUser
														.optString("userId"));
												userInfo.setNickname(replyToUser
														.optString("nickName"));
												userInfo.setAvatar(replyToUser
														.optString("avatar"));
												userInfo.setUserType(replyToUser
														.optString("userType"));
												replys.setPublisher(userInfo);
											}
											replysList.add(replys);
										}
										// 回复列表
										comment.setReplys(replysList);
									}
									comments.add(comment);
								}
							}

							if (comments.size() > 0) {
								evaluate_user_content.setText(comments.get(0)
										.getContent());
								Date date = new Date();
								DateFormat format = new SimpleDateFormat(
										"yyyyMMddHHmmss");
								try {
									long time = (date.getTime() - format.parse(
											comments.get(0).getCreateTime())
											.getTime()) / 1000;
									long day1 = time / (24 * 3600);
									long hour1 = time % (24 * 3600) / 3600;
									long minute1 = time % 3600 / 60;
									if (day1 > 0) {
										evaluate_user_date.setText(day1 + "天前");
									} else if (hour1 > 0) {
										evaluate_user_date.setText(hour1
												+ "小时前");
									} else if (minute1 > 0) {
										evaluate_user_date
												.setText(hour1 + "分前");
									} else {
										if(time<=0){
											evaluate_user_date.setText("刚刚");
										}else{
										evaluate_user_date.setText(time+"秒前");
									}
										}
									// evaluate_user_date.setText(DateUtil.getFormateDate(comments.get(0).getCreateTime()));
								} catch (ParseException e) {
									e.printStackTrace();
								}
								evaluate_user_name.setText(comments.get(0)
										.getUser().getNickname());
								ImageLoader.getInstance().displayImage(
										Urls.imgHost
												+ comments.get(0).getUser()
														.getAvatar(),
										evaluate_user_photo);

							}
							evaluate_detail_button.setText("查看全部评论" + "("
									+ totalComment + ")");
						} else {
							user_comment.setVisibility(View.GONE);
							// layout_user_evaluate.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "QueryProductComment", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryProductComment", "responseString="
								+ responseString);
					}
				});
	}

	// TODO 设置页数与每页显示条数的查询
	private void QuerySimillarProduct(int index, String id) {
		RequestParams params = new RequestParams();
		params.put("pagenum", "" + index);
		params.put("pagesize", "10");
		params.put("productId", id);
		HttpUtil.get(Urls.simillar_product, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogUtil.i(TAG, "QuerySimillarProduct", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QuerySimillarProduct", "response="
								+ response.toString());
						JSONObject dataJSON = response.optJSONObject("data");
						ArrayList<ProductNewInfo> proList = new ArrayList<ProductNewInfo>();
						if (dataJSON != null) {
							JSONArray productsJsonList = dataJSON
									.optJSONArray("products");
							if (productsJsonList == null) {
								return;
							}
							for (int i = 0; i < productsJsonList.length(); i++) {
								JSONObject productJson = productsJsonList
										.optJSONObject(i);
								ProductNewInfo productNewInfo = new ProductNewInfo();
								productNewInfo.setId(productJson
										.optString("id"));
								productNewInfo.setExpStart(productJson
										.optString("expStart"));
								productNewInfo.setExpend(productJson
										.optString("expend"));
								productNewInfo.setProductType(productJson
										.optString("productType"));
								productNewInfo.setDistance(productJson
										.optString("distance"));
								productNewInfo.setTitle(productJson
										.optString("title"));
								productNewInfo.setPoiCount(productJson
										.optString("poiCount"));
								productNewInfo.setPrice(productJson
										.optString("price"));
								productNewInfo.setDays(productJson
										.optString("days"));
								productNewInfo.setDescription(productJson
										.optString("description"));
								productNewInfo.setPriceMax(productJson
										.optString("priceMax"));
								productNewInfo.setTitleImg(productJson
										.optString("titleImg"));
								productNewInfo.setMaxMember(productJson
										.optString("maxMember"));
								productNewInfo.setServices(productJson
										.optString("serviceCodes"));
								ArrayList<String> topics = new ArrayList<String>();
								JSONArray topicJsonArray = productJson
										.optJSONArray("topics");
								if (topicJsonArray != null
										&& topicJsonArray.length() > 0) {
									for (int j = 0; j < topicJsonArray.length(); j++) {
										LogUtil.i(TAG, "topicJsonArray",
												topicJsonArray.optString(j));
										topics.add(topicJsonArray.optString(j));
									}
									productNewInfo.setTopics(topics);
								}
								proList.add(productNewInfo);
							}
							initSimillarProductListView(proList);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "QuerySimillarProduct", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QuerySimillarProduct",
								"responseString=" + responseString);
					}
				});
	}

	private void initJoinMebPhoto() {
		// 1.http协议调用接口获取订购人数

		RequestParams requestParams = new RequestParams();
		requestParams.put("pagenum", "1");
		requestParams.put("pagesize", "20");
		requestParams.put("productId", productId);
		HttpUtil.get(Urls.product_buyers, requestParams,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						if (mProductJoinMebLayout != null) {
							mProductJoinMebLayout.removeAllViews();
						}
						LogUtil.i(TAG, "GrouponProductNewDetailActivity",
								"statusCode=" + statusCode);
						LogUtil.i(TAG, "GrouponProductNewDetailActivity",
								"response=" + response.toString());
						String code = response.optString("code");
						String msg = response.optString("msg");
						if (!code.equals("00000")) {
							ToastUtil.showErrorToast(
									ProductDetailNewActivity.this, msg);
							return;
						}
						JSONObject dataJSON = response.optJSONObject("data");
						final String total = dataJSON.optString("total");
						mProductJoinMebCountTv.setText("当前已有" + total
								+ "人预订此产品");
						JSONArray productArray = dataJSON
								.optJSONArray("buyers");
						// 屏幕 宽度
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						int width = dm.widthPixels;
						LayoutParams params = new LayoutParams(60, 60);
						params.setMargins(10, 10, 0, 0);
						if (productArray != null) {
							for (int i = 0; i < productArray.length(); i++) {
								JSONObject productObject = productArray
										.optJSONObject(i);
								RoundImageView imageView = new RoundImageView(
										ProductDetailNewActivity.this);
								if ((i + 2) * 70 > width) {
									imageView
											.setImageResource(R.drawable.all_buyers_more);
									imageView.setLayoutParams(params);
									imageView
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													Intent it = new Intent(
															ProductDetailNewActivity.this,
															ProductJoinMebActivity.class);
													it.putExtra("productId",
															productId);
													it.putExtra("total", total);
													startActivity(it);
													finish();
												}
											});
									mProductJoinMebLayout.removeView(imageView);
									mProductJoinMebLayout.addView(imageView);
									break;
								} else if (i == (productArray.length() - 1)) {
									if ((i + 2) * 70 > width) {
										imageView
												.setImageResource(R.drawable.all_buyers_more);
										imageView.setLayoutParams(params);
										imageView
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														Intent it = new Intent(
																ProductDetailNewActivity.this,
																ProductJoinMebActivity.class);
														it.putExtra(
																"productId",
																productId);
														it.putExtra("total",
																total);
														startActivity(it);
														finish();
													}
												});
										mProductJoinMebLayout
												.addView(imageView);
									} else {
										if(!StringUtils.isEmpty(productObject
																		.optString("avatar"))){
											ImageLoader
											.getInstance()
											.displayImage(
													Urls.imgHost
													+ productObject
													.optString("avatar"),
													imageView,
													AppConfig
													.options(R.drawable.user_defult_photo));
											
										}
										imageView.setLayoutParams(params);
										mProductJoinMebLayout
												.addView(imageView);
										RoundImageView imageView1 = new RoundImageView(
												ProductDetailNewActivity.this);
										imageView1
												.setImageResource(R.drawable.all_buyers_more);
										imageView1.setLayoutParams(params);
										imageView1
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														Intent it = new Intent(
																ProductDetailNewActivity.this,
																ProductJoinMebActivity.class);
														it.putExtra(
																"productId",
																productId);
														it.putExtra("total",
																total);
														startActivity(it);
														finish();
													}
												});
										mProductJoinMebLayout
												.addView(imageView1);
									}
								} else {
									// 2.给头像设置参数属性
									if( !StringUtils.isEmpty(productObject.optString("avatar"))){
										ImageLoader
										.getInstance()
										.displayImage(
												Urls.imgHost
												+ productObject
												.optString("avatar"),
												imageView,
												AppConfig
												.options(R.drawable.user_defult_photo));
									}
									imageView.setLayoutParams(params);
									mProductJoinMebLayout.addView(imageView);
								}

							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "GrouponProductNewDetailActivity",
								"statusCode=" + statusCode);
						LogUtil.i(TAG, "GrouponProductNewDetailActivity",
								"responseString=" + responseString);
					}

				});

	}

	/**
	 * 评论
	 */
	private void evaProductComments() {
		JSONObject jsonObject = new JSONObject();
		try {
			// 评论内容
			String comment = mEditEvaEt.getText().toString();
			jsonObject.put("content", comment);
			// 产品id
			jsonObject.put("objectId", productId);
			// 评论对象-产品-product
			jsonObject.put("objectType", "product");
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpUtil.post(Urls.product_detail_comment, stringEntity,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							mEditEvaEt.setClickable(false);
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

}
