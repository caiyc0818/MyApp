package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.FamousComment;
import com.bcinfo.tripaway.bean.Friend;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.fragment.ClubHomePageFragment;
import com.bcinfo.tripaway.fragment.ClubTravelsFragment;
import com.bcinfo.tripaway.fragment.ExpericeInfoFragment;
import com.bcinfo.tripaway.fragment.ProductInfoFragment;
import com.bcinfo.tripaway.fragment.SettingFragment1;
import com.bcinfo.tripaway.fragment.TripInfoFragment;
import com.bcinfo.tripaway.fragment.VehicleInfoFragment;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView;
import com.bcinfo.tripaway.view.ScrollView.PersonInfoScrollView.PullListener;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout.OnRefreshListener;
import com.bcinfo.tripaway.view.viewpager.IndexViewPager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ClubMebHomepageActivity extends BaseActivity implements
		PersonalScrollViewListener, OnClickListener {
//	private IndexViewPager vPager = null;

	/**
	 * 选项卡下划线长度
	 */
	private static int lineWidth = 0;

	/**
	 * 偏移量 （手机屏幕宽度/3-选项卡长度）/2
	 */
	private static int offset = 0;

	/**
	 * 选项卡总数
	 */
	private static final int TAB_COUNT = 3;
	/**
	 * 当前显示的选项卡位置
	 */
	private int current_index = 0;

	private int[] selectedImage = { R.drawable.vehicle_checked,
			R.drawable.trip_selected, R.drawable.product_checked };
	private int[] normalImage = { R.drawable.vehicle_normal,
			R.drawable.trip_normal, R.drawable.product_normal };

	/**
	 * 选项卡标题
	 */
	private RadioButton navIV_0,navIV_1, navIV_2, navIV_3;

	/**
	 * 选项卡点击
	 */
	private RadioButton[] titles = new RadioButton[4];
	private RadioButton[] titles1 = new RadioButton[4];
	private PersonInfoScrollView mProductScrollView;
	private ImageView mProductHeadImg;
	private RelativeLayout layout_product_detail_title;
	private LinearLayout layout_service_list;
	private List<Fragment> fragments = new ArrayList<Fragment>();

	private LinearLayout moreTextView;

	private String userId;
	private UserInfo user;
	private UserInfoEx infoEx;

	private TextView focusNumTextView;
	private TextView fansNumTextView;
	private TextView serviceNumTextView;
	private TextView driverYearTextView;
	private TextView nickNameTextView;
	private TextView groupNameTextView;
	private TextView jobTextView;
	private TextView introductionTextView;
	private TextView workYear;

	private ImageView sexImageView;
	private RoundImageView photoRoundImageView;

	private FlowLayout interestTagFlow;

	private LinearLayout workYearLayout;
	private CarExt carInfo;

	private VehicleInfoFragment vehicleInfoFragment;
	private TripInfoFragment tripInfoFragment;

	private boolean isLoadMore = false;
	private int isRefresh = 0;

	private boolean isFocused = false;

	// private List<Friend> friends=new ArrayList<Friend>();

	// 自定义上拉加载相对布局
//	private PullToRefreshLayout pullToRefreshLayout;

	private LinearLayout layout_back_button;

	private LinearLayout layout_contact_author_button;

	private ExpericeInfoFragment expericeInfoFragment;

	private boolean isDriverHomePage;

	private TextView mFocusTxt;
	private LinearLayout groupLayout;

	private LinearLayout product_service_share_button;

	private LinearLayout team_talk_layout;
	private LinearLayout tagLayout;
	private ImageView isCertified;
//	private View load_more;

	private ProductInfoFragment productInfoFragment;
	
	private View[] horizontalLines = new View[4];
	private View[] horizontalLines1 = new View[4];
	private View horizontalLine0;
	private View horizontalLine1;
	private View horizontalLine2;
	private View horizontalLine3;
	
	private LinearLayout verticalLine1;
	private RelativeLayout driverNavLayout;
	
	
	/**
	 * 游记Fragment
	 */
	private ClubTravelsFragment clubTravelsFragment;

	private LinearLayout homepageNav1;

	private LinearLayout homepageNav;

	private View horizontalLine01;

	private View horizontalLine11;

	private View horizontalLine21;

	private View horizontalLine31;

	private LinearLayout verticalLine11;

	private RelativeLayout driverNavLayout1;

	private RadioButton navIV_01;

	private RadioButton navIV_11;

	private RadioButton navIV_21;

	private RadioButton navIV_31;

//	private FrameLayout contentFrame2;
//
//	private FrameLayout contentFrame3;
//
//	private FrameLayout contentFrame4;

	private FrameLayout contentFrame1;

	private int alpha=-1;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_homepage);

		userId = getIntent().getStringExtra("identifyId");
	UserInfo userInfo = getIntent().getParcelableExtra("userInfo");
		if(userInfo!=null&&!StringUtils.isEmpty(userInfo.getNickname())){
			statisticsTitle=userInfo.getNickname();
		}
//		userId="263";
		isDriverHomePage = getIntent()
				.getBooleanExtra("isDriverHomePage", true);
		queryUserInfo(userId);
		// queryProductInfo(userId);
		findView();
		initRadioGroup();

		

	}

	private void findView() {
		mProductHeadImg = (ImageView) findViewById(R.id.personpic);

		mProductScrollView = (PersonInfoScrollView) findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setScrollListener(this);
		mProductScrollView.setPullListener(mPullListener);

		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(0);
		layout_product_detail_title.setFocusable(true);
		layout_product_detail_title.setFocusableInTouchMode(true);
		layout_product_detail_title.requestFocus();
		layout_service_list = (LinearLayout) findViewById(R.id.layout_service_list);
		moreTextView = (LinearLayout) findViewById(R.id.more_info);
//		load_more = this.findViewById(R.id.load_more);
		moreTextView.setOnClickListener(this);

		
		
		
		
		
		focusNumTextView = (TextView) this.findViewById(R.id.focus_num);
		jobTextView= (TextView) this.findViewById(R.id.job);
		fansNumTextView = (TextView) this.findViewById(R.id.fans_num);
		serviceNumTextView = (TextView) this.findViewById(R.id.service_num);
		driverYearTextView = (TextView) this.findViewById(R.id.driver_year);
		nickNameTextView = (TextView) this.findViewById(R.id.user_Nickname);
		workYear = (TextView) this.findViewById(R.id.workYear);
		mFocusTxt = (TextView) this.findViewById(R.id.add_focus);
		mFocusTxt.setOnClickListener(this);
		groupNameTextView = (TextView) this.findViewById(R.id.group_name);
		introductionTextView = (TextView) this.findViewById(R.id.introduction);
		layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		layout_back_button.setOnClickListener(this);
		team_talk_layout = (LinearLayout) findViewById(R.id.team_talk_layout1);
		tagLayout= (LinearLayout) findViewById(R.id.tagLayout);
		groupLayout = (LinearLayout) findViewById(R.id.groupLayout);
		isCertified = (ImageView) findViewById(R.id.personal_hp_v_icon_iv);
		team_talk_layout.setOnClickListener(this);
//		pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.allThemes_container);
		team_talk_layout.setBackgroundColor(Color.parseColor("#1cce6f"));
		product_service_share_button = (LinearLayout) findViewById(R.id.product_service_share_button);
//		pullToRefreshLayout.setOnRefreshListener(new MyListener());

		interestTagFlow = (FlowLayout) this
				.findViewById(R.id.interest_tag_flow);

		sexImageView = (ImageView) this.findViewById(R.id.sex);
		photoRoundImageView = (RoundImageView) this
				.findViewById(R.id.personal_icon);

		workYearLayout = (LinearLayout) this
				.findViewById(R.id.work_year_layout);

		layout_contact_author_button = (LinearLayout) findViewById(R.id.layout_contact_author_button);
		layout_contact_author_button.setOnClickListener(this);
		product_service_share_button.setOnClickListener(this);
		horizontalLine0=this.findViewById(R.id.horizontal_line0);
		horizontalLine1=this.findViewById(R.id.horizontal_line1);
		horizontalLine2=this.findViewById(R.id.horizontal_line2);
		horizontalLine3=this.findViewById(R.id.horizontal_line3);
		horizontalLines[0]=horizontalLine0;
		horizontalLine0.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLine1.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLine2.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLine3.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLines[1]=horizontalLine1;
		horizontalLines[2]=horizontalLine2;
		horizontalLines[3]=horizontalLine3;
		verticalLine1=(LinearLayout) this.findViewById(R.id.vertical_line1);
		driverNavLayout=(RelativeLayout) this.findViewById(R.id.driver_nav_layout);
		homepageNav=(LinearLayout) this.findViewById(R.id.driver_homepage_nav);
		homepageNav1=(LinearLayout) this.findViewById(R.id.driver_homepage_nav1);
		homepageNav1.setVisibility(View.GONE);
		horizontalLine01=this.findViewById(R.id.horizontal_line01);
		horizontalLine11=this.findViewById(R.id.horizontal_line11);
		horizontalLine21=this.findViewById(R.id.horizontal_line21);
		horizontalLine31=this.findViewById(R.id.horizontal_line31);
		horizontalLines1[0]=horizontalLine01;
		horizontalLine01.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLine11.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLine21.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLine31.setBackgroundColor(Color.parseColor("#1cce6f"));
		horizontalLines1[1]=horizontalLine11;
		horizontalLines1[2]=horizontalLine21;
		horizontalLines1[3]=horizontalLine31;
		verticalLine11=(LinearLayout) this.findViewById(R.id.vertical_line11);
		driverNavLayout1=(RelativeLayout) this.findViewById(R.id.driver_nav_layout1);
		contentFrame1=(FrameLayout) this.findViewById(R.id.content_frame1);
//		 contentFrame2=(FrameLayout) this.findViewById(R.id.content_frame2);
//		 contentFrame3=(FrameLayout) this.findViewById(R.id.content_frame3);
//		 contentFrame4=(FrameLayout) this.findViewById(R.id.content_frame4);
	}

	private void setValue() {
		if (user == null)
			return;
		focusNumTextView.setText(user.getFocus() == null ? "0" : user
				.getFocus());
		fansNumTextView.setText(user.getFansCount() == null ? "0" : user
				.getFansCount());
		serviceNumTextView.setText(user.getServTimes() == null ? "0人" : user
				.getServTimes() + "人");
		if(!StringUtils.isEmpty(user.getRealName()))
		nickNameTextView.setText(user.getRealName());
		else 
			nickNameTextView.setText(user.getNickname());
		if (user.getOrgInfo() == null || user.getOrgInfo().equals("")) {
			groupLayout.setVisibility(View.INVISIBLE);
		}
		if(!StringUtils.isEmpty(user.getOrgInfo()))
		groupNameTextView.setText(user.getOrgInfo());
		else {
			((View)groupNameTextView.getParent()).setVisibility(View.INVISIBLE);
		}
		introductionTextView.setText(StringUtils.isEmpty(user.getIntroduction())?"":user.getIntroduction());
		if (user.getTag().getInterests().size() == 0) {
			tagLayout.setVisibility(View.GONE);
		} else
		addFlowView(user.getTag().getInterests(), interestTagFlow);
		if (user.getGender().equals("1")) {
			sexImageView.setImageResource(R.drawable.man);
		} else if (user.getGender().equals("0")){
			sexImageView.setImageResource(R.drawable.woman);
		}else{
			sexImageView.setVisibility(View.GONE);
		}
		boolean check = false;
		String profession = user.getPermission();
		if (profession != null) {
			if (profession.contains("专业司机")) {
				jobTextView.setText("司");
				jobTextView.setVisibility(View.VISIBLE);
				check = true;
			} else if (profession.contains("资深领队")) {
				jobTextView.setText("领");
				jobTextView.setVisibility(View.VISIBLE);
				check = true;
			}
			if (profession.contains("达人导游")) {
				jobTextView.setText("导");
				jobTextView.setVisibility(View.VISIBLE);
				check = true;
			}
		}
		OrgRole orgRole = user.getOrgRole();
		if (!check && orgRole != null && orgRole.getRoleName() != null) {
			if ("driver".equals(orgRole.getRoleCode())) {
				jobTextView.setText("司");
				jobTextView.setVisibility(View.VISIBLE);
			} else if ("leader".equals(orgRole.getRoleCode())) {
				jobTextView.setText("领");
				jobTextView.setVisibility(View.VISIBLE);
			}
			if ("guide".equals(orgRole.getRoleCode())) {
				jobTextView.setText("导");
				jobTextView.setVisibility(View.VISIBLE);
			}
		}
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + user.getAvatar(), photoRoundImageView,
					AppConfig.options(R.drawable.user_defult_photo));
		}
		Map<String, String> exts = user.getExts();
		if (exts != null && !StringUtils.isEmpty(exts.get("work_time"))) {
			driverYearTextView.setText(exts.get("work_time"));
			if (!isDriverHomePage) {
				workYear.setText("从业");
			}
		} else {
			workYearLayout.setVisibility(View.GONE);
		}
//		if (isDriverHomePage)
//			vehicleInfoFragment.setValue(carInfo);
		if (StringUtils.isEmpty(user.getIsCertified())
				|| user.getIsCertified().equals("no")) {
			isCertified.setVisibility(View.INVISIBLE);
		}
//		if (exts != null && !StringUtils.isEmpty(exts.get("  "))) {
//			ImageLoader.getInstance().displayImage(
//					Urls.imgHost + exts.get("background"), mProductHeadImg,
//					AppConfig.options(R.drawable.winter));
//		}
		if ( !StringUtils.isEmpty(user.getBackground())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + user.getBackground(), mProductHeadImg,
					AppConfig.options(R.drawable.clubmeb_bg));
		}
		initFragment();
	}

	private void initFragment() {

		clubTravelsFragment = new ClubTravelsFragment(ClubMebHomepageActivity.this, user,false);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame1, clubTravelsFragment).commit();
//		vehicleInfoFragment = new  VehicleInfoFragment(carInfo);
//		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame2, vehicleInfoFragment).commit();
//		expericeInfoFragment = new ExpericeInfoFragment();
//		expericeInfoFragment.setData(user);
//		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame3, expericeInfoFragment).commit();
//		productInfoFragment = new ProductInfoFragment(userId,
//				layout_product_detail_title);
//		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame4, productInfoFragment).commit();
	}
	private void initRadioGroup() {
//		tripInfoFragment = new TripInfoFragment(userId);
//		getSupportFragmentManager()
//		.beginTransaction().replace(R.id.content_frame1, tripInfoFragment).commit();
		navIV_0 = (RadioButton) findViewById(R.id.radio_0_text);
		navIV_1 = (RadioButton) findViewById(R.id.radio_1_text);
		navIV_2 = (RadioButton) findViewById(R.id.radio_2_text);
		navIV_3 = (RadioButton) findViewById(R.id.radio_3_text);
		titles[0] = navIV_0;
		titles[1] = navIV_1;
		titles[2] = navIV_2;
		titles[3] = navIV_3;
		
		navIV_01 = (RadioButton) findViewById(R.id.radio_0_text1);
		navIV_11 = (RadioButton) findViewById(R.id.radio_1_text1);
		navIV_21 = (RadioButton) findViewById(R.id.radio_2_text1);
		navIV_31 = (RadioButton) findViewById(R.id.radio_3_text1);
		titles1[0] = navIV_01;
		titles1[1] = navIV_11;
		titles1[2] = navIV_21;
		titles1[3] = navIV_31;
		if (isDriverHomePage) {
			driverNavLayout.setVisibility(View.VISIBLE);
			verticalLine1.setVisibility(View.VISIBLE);
			driverNavLayout1.setVisibility(View.VISIBLE);
			verticalLine11.setVisibility(View.VISIBLE);
		} else {
			driverNavLayout.setVisibility(View.GONE);
			verticalLine1.setVisibility(View.GONE);
			driverNavLayout1.setVisibility(View.GONE);
			verticalLine11.setVisibility(View.GONE);

		}
		OnCheckedChangeListener onCheckedChangeListener0=new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					hideFragments(transaction);
//						if (tripInfoFragment == null) {
//							tripInfoFragment = new TripInfoFragment(userId);
//							transaction.add(R.id.content_frame1, tripInfoFragment);
//						} else {
//							transaction.show(tripInfoFragment);
//						}
					if (clubTravelsFragment == null) {
						clubTravelsFragment = new ClubTravelsFragment(ClubMebHomepageActivity.this, user,false);
						transaction.add(R.id.content_frame1, clubTravelsFragment);
					} else {
						transaction.show(clubTravelsFragment);
					}
//						load_more.setVisibility(View.VISIBLE);
					horizontalLines[0].setVisibility(View.VISIBLE);
					titles[0].setTextColor(getResources().getColor(R.color.dark_gray));
					horizontalLines1[0].setVisibility(View.VISIBLE);
					titles1[0].setTextColor(getResources().getColor(R.color.dark_gray));
					if(current_index!=0){
					horizontalLines[current_index].setVisibility(View.GONE);
					titles[current_index].setChecked(false);
					titles[current_index].setTextColor(getResources().getColor(R.color.gray_text));
					
					horizontalLines1[current_index].setVisibility(View.GONE);
					titles1[current_index].setChecked(false);
					titles1[current_index].setTextColor(getResources().getColor(R.color.gray_text));
					}
					current_index = 0;
//					clickChangeTab(0);
					 transaction.commitAllowingStateLoss();
				}
			}
		};
		OnCheckedChangeListener onCheckedChangeListener1=new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					hideFragments(transaction);
//					vPager.setCurrentItem(0);
//					if (isDriverHomePage) {
						if (vehicleInfoFragment == null) {
							vehicleInfoFragment = new  VehicleInfoFragment(carInfo);
							transaction.add(R.id.content_frame1, vehicleInfoFragment);
						} else {
							transaction.show(vehicleInfoFragment);
						}
//							load_more.setVisibility(View.GONE);
					horizontalLines[1].setVisibility(View.VISIBLE);
					horizontalLines[current_index].setVisibility(View.GONE);
					titles[1].setTextColor(getResources().getColor(R.color.dark_gray));
					titles[current_index].setTextColor(getResources().getColor(R.color.gray_text));
					titles[current_index].setChecked(false);
					
					horizontalLines1[1].setVisibility(View.VISIBLE);
					horizontalLines1[current_index].setVisibility(View.GONE);
					titles1[1].setTextColor(getResources().getColor(R.color.dark_gray));
					titles1[current_index].setTextColor(getResources().getColor(R.color.gray_text));
					titles1[current_index].setChecked(false);
					current_index = 1;
					 transaction.commitAllowingStateLoss();
//					clickChangeTab(1);
				}
			}
		};
		OnCheckedChangeListener onCheckedChangeListener2=new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if (isChecked) {
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					hideFragments(transaction);
//					load_more.setVisibility(View.VISIBLE);
//					if (!isDriverHomePage) {
						if (expericeInfoFragment == null) {
							expericeInfoFragment = new ExpericeInfoFragment();
							expericeInfoFragment.setData(user);
							transaction.add(R.id.content_frame1, expericeInfoFragment);
						} else {
							transaction.show(expericeInfoFragment);
						}
						horizontalLines[2].setVisibility(View.VISIBLE);
						horizontalLines[current_index].setVisibility(View.GONE);	
					titles[2].setTextColor(getResources().getColor(R.color.dark_gray));
					titles[current_index].setTextColor(getResources().getColor(R.color.gray_text));
					titles[current_index].setChecked(false);
					
					horizontalLines1[2].setVisibility(View.VISIBLE);
					horizontalLines1[current_index].setVisibility(View.GONE);	
				titles1[2].setTextColor(getResources().getColor(R.color.dark_gray));
				titles1[current_index].setTextColor(getResources().getColor(R.color.gray_text));
				titles1[current_index].setChecked(false);
					current_index = 2;
					 transaction.commitAllowingStateLoss();
//					clickChangeTab(2);
				}
			}
		};
		OnCheckedChangeListener onCheckedChangeListener3=new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					hideFragments(transaction);
//					load_more.setVisibility(View.VISIBLE);
//					vPager.setCurrentItem(2);
						if (productInfoFragment == null) {
							productInfoFragment = new ProductInfoFragment(userId,
									layout_product_detail_title);
							transaction.add(R.id.content_frame1, productInfoFragment);
						} else {
							transaction.show(productInfoFragment);
						}
						horizontalLines[3].setVisibility(View.VISIBLE);
						horizontalLines[current_index].setVisibility(View.GONE);
					titles[3].setTextColor(getResources().getColor(R.color.dark_gray));
					titles[current_index].setChecked(false);
					titles[current_index].setTextColor(getResources().getColor(R.color.gray_text));
					
					horizontalLines1[3].setVisibility(View.VISIBLE);
					horizontalLines1[current_index].setVisibility(View.GONE);
				titles1[3].setTextColor(getResources().getColor(R.color.dark_gray));
				titles1[current_index].setChecked(false);
				titles1[current_index].setTextColor(getResources().getColor(R.color.gray_text));
					current_index = 3;
					 transaction.commitAllowingStateLoss();
//					clickChangeTab(3);
				}
			}
		};
		navIV_0.setOnCheckedChangeListener(onCheckedChangeListener0);
		navIV_01.setOnCheckedChangeListener(onCheckedChangeListener0);
		navIV_1.setOnCheckedChangeListener(onCheckedChangeListener1);
		navIV_11.setOnCheckedChangeListener(onCheckedChangeListener1);
		navIV_2.setOnCheckedChangeListener(onCheckedChangeListener2);
		navIV_21.setOnCheckedChangeListener(onCheckedChangeListener2);
		navIV_3.setOnCheckedChangeListener(onCheckedChangeListener3);
		navIV_31.setOnCheckedChangeListener(onCheckedChangeListener3);
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (vehicleInfoFragment != null) {
			transaction.hide(vehicleInfoFragment);
		}
//		if (tripInfoFragment != null) {
//			transaction.hide(tripInfoFragment);
//		}
		if (clubTravelsFragment != null) {
			transaction.hide(clubTravelsFragment);
		}
		if (productInfoFragment != null) {
			transaction.hide(productInfoFragment);
		}
		if (expericeInfoFragment != null) {
			transaction.hide(expericeInfoFragment);
		}
	}
	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY,
			int prePositionX, int prePositionY) {
		// TODO Auto-generated method stub
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
			// product_title.setAlpha(alpha / 255f);
			
			
			int[] loactions = new int[2];
			int[] loactions1 = new int[2];
			homepageNav.getLocationOnScreen(loactions);
			homepageNav1.getLocationOnScreen(loactions1);
			if (loactions[1] < loactions1[1]) {
				homepageNav1.setVisibility(View.VISIBLE);}
			else{
				homepageNav1.setVisibility(View.GONE);	
			}
//			int[] loactionsContentFrame1 = new int[2];
//			contentFrame1.getLocationOnScreen(loactionsContentFrame1);
//			int[] loactionsContentFrame2 = new int[2];
//			contentFrame2.getLocationOnScreen(loactionsContentFrame2);
//			int[] loactionsContentFrame3 = new int[2];
//			contentFrame3.getLocationOnScreen(loactionsContentFrame3);
//			int[] loactionsContentFrame4 = new int[2];
//			contentFrame4.getLocationOnScreen(loactionsContentFrame4);
//			if (loactionsContentFrame1[1] <=loactions1[1]+DensityUtil.dip2px(this, 42)&&
//					loactionsContentFrame2[1] >loactions1[1]+DensityUtil.dip2px(this, 42)) {
//				changeTab(0);}
//			else{
//			}
//			if (loactionsContentFrame2[1] <=loactions1[1]+DensityUtil.dip2px(this, 42)&&
//					loactionsContentFrame3[1] >loactions1[1]+DensityUtil.dip2px(this, 42)) {
//				changeTab(1);}
//			else{
//			}
//			if (loactionsContentFrame3[1] <=loactions1[1]+DensityUtil.dip2px(this, 42)&&
//					loactionsContentFrame4[1] >loactions1[1]+DensityUtil.dip2px(this, 42)) {
//				changeTab(2);}
//			else{
//			}
//			if (loactionsContentFrame4[1] <=loactions1[1]+DensityUtil.dip2px(this, 42)) {
//				changeTab(3);}
//			else{
//			}
			View view1 = mProductScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = mProductScrollView.getScrollY();
			int scollHeight = mProductScrollView.getHeight();
			if (height <= scollY + scollHeight) {
				// 底部
				if (current_index == 0) {
					Intent it = new Intent("com.bcinfo.loadMoreTravels");
					sendBroadcast(it);
				} else if (current_index == 3) {
					Intent it = new Intent("com.bcinfo.loadMoreProducts1");
					sendBroadcast(it);
				}
			}
		} else {
			return;
		}
	}

//	
//	private void clickChangeTab(int index){
//		if(index==current_index)return;
//		changeTab(index);
//	
//		   View p1=(View)(contentFrame1.getParent());
//           View p2=(View)(p1.getParent());
//           View p3=(View)(p2.getParent());
//       	int top= -DensityUtil.dip2px(ClubMebHomepageActivity.this, 54)-contentFrame1.getTop()+p1.getTop()+p2.getTop()+p3.getTop();   
//		switch (index) {
//		case 0:
//                		int[] loactionsContentFrame1 = new int[2];
//                           mProductScrollView.smoothScrollTo(0,contentFrame1.getTop()+top);
//			break;
//		case 1:
//			
//			 loactionsContentFrame1 = new int[2];
//			contentFrame2.getLocationOnScreen(loactionsContentFrame1);
//			mProductScrollView.smoothScrollTo(0,contentFrame2.getTop()+top);
//			break;
//		case 2:
//			 loactionsContentFrame1 = new int[2];
//				contentFrame3.getLocationOnScreen(loactionsContentFrame1);
//				mProductScrollView.smoothScrollTo(0,contentFrame3.getTop()+top);
//			break;
//		case 3:
//			 loactionsContentFrame1 = new int[2];
//				contentFrame4.getLocationOnScreen(loactionsContentFrame1);
//				mProductScrollView.smoothScrollTo(0,contentFrame4.getTop()+top);
//			break;
//		default:
//			break;
//		}
//	}
	
//	private void changeTab(int index){
//		if(index==current_index)return;
//		horizontalLines[index].setVisibility(View.VISIBLE);
//		horizontalLines[current_index].setVisibility(View.GONE);
//		titles[index].setTextColor(getResources().getColor(R.color.dark_gray));
//		titles[current_index].setChecked(false);
//		titles[current_index].setTextColor(getResources().getColor(
//				R.color.gray_text));
//
//		horizontalLines1[index].setVisibility(View.VISIBLE);
//		horizontalLines1[current_index].setVisibility(View.GONE);
//		titles1[index].setTextColor(getResources().getColor(R.color.dark_gray));
//		titles1[current_index].setChecked(false);
//		titles1[current_index].setTextColor(getResources().getColor(
//				R.color.gray_text));
//		current_index = index;
//	}
	
	/**
	 * 重新设置viewPager高度
	 * 
	 * @param position
	 */
	public void resetViewPagerHeight(int position) {
//		View child = fragments.get(position).getView();
//		if (child != null && vPager.getCurrentItem() == position) {
//			child.measure(0, 0);
//			int h = child.getMeasuredHeight();
//			LinearLayout.LayoutParams params = (LayoutParams) vPager
//					.getLayoutParams();
//			// params.height = h + 50;
//			params.height = h;
//			vPager.setLayoutParams(params);
//		}
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
//			layout_service_list.setAlpha(alpha / 255f);
			// product_introduce.setAlpha(alpha / 255f);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more_info:
			if(user==null) return;
			Intent moreIntent = new Intent(ClubMebHomepageActivity.this,
					ShowQualificationInfoActivity.class);
			moreIntent.putExtra("certifyCode", user.getCertifyCode());
			startActivity(moreIntent);
			activityAnimationOpen();
			break;
		case R.id.layout_back_button:
			finish();
			break;

		case R.id.layout_contact_author_button:
			if (!AppInfo.getIsLogin()) {
				goLoginActivity();
				return;
			}
			joinPrivateChat();
//			Intent talkIntent = new Intent(ClubMebHomepageActivity.this,
//					ChatActivity.class);
//			talkIntent.putExtra("receiveId", user.getUserId());
//			talkIntent.putExtra("title", user.getRealName());
//			startActivity(talkIntent);
//			activityAnimationOpen();
			break;
		case R.id.add_focus:
			addOrCancelFocus(isFocused, userId);
			break;
		case R.id.product_service_share_button:
			String name = user.getNickname();
			if(StringUtils.verifyIsPhone(name)){
				name = StringUtils.getSecretStr(name);
			}
			ShareSelectDialog shareSelectDialog = new ShareSelectDialog(this,
					user.getAvatar(), user.getIntroduction(),
					name, user.getUserId(),Urls.ShareUrlOfUser,name);
			shareSelectDialog.show();
			break;

		case R.id.team_talk_layout1:
			joinTeamTalk(user.getUserId());
			break;

		}
	}

	/**
	 * 查询个人资料
	 * 
	 * @param userId
	 */
	private void queryUserInfo(String userId) {
		HttpUtil.get(Urls.personal_url + userId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONObject data = response.optJSONObject("data");
					infoEx = new UserInfoEx();
					if (data != null) {
						// 司机数 领队数 导游数 群聊人数
						infoEx.setGuideNum(data.optInt("guide", 0));
						infoEx.setDriverNum(data.optInt("driver", 0));
						infoEx.setLeaderNum(data.optInt("leader", 0));
						infoEx.setGroupChatCount(data.optInt("groupChatCount",
								0));
						JSONObject carObj = data.optJSONObject("carInfo");
						if (carObj != null) {
							carInfo = new CarExt();
							carInfo.setCarName(carObj.optString("car_name"));
							carInfo.setCapacityBig(carObj
									.optString("capacity_big"));
							carInfo.setCapacitySmall(carObj
									.optString("capacity_small"));
							carInfo.setDistance(carObj.optString("distance"));
							carInfo.setCarType(carObj.optString("car_type"));
							List<String> facilities = carInfo.getFacilities();
							JSONArray facilitiesArray = carObj
									.optJSONArray("facilities");
							if (facilitiesArray != null) {
								for (int i = 0; i < facilitiesArray.length(); i++) {
									facilities.add(facilitiesArray.optString(i));
								}
							}
							carInfo.setSeatNum(carObj.optString("seatnum"));
							carInfo.setDesc(carObj.optString("car_desc"));
							List<ImageInfo> pics = carInfo.getCarPics();
							JSONArray picsArray = carObj.optJSONArray("pics");
							if (picsArray != null) {
								for (int i = 0; i < picsArray.length(); i++) {
									ImageInfo pic = new ImageInfo();
									JSONObject picObj = picsArray
											.optJSONObject(i);
									pic.setDesc(picObj.optString("desc"));
									pic.setHeight(picObj.optString("height"));
									pic.setWidth(picObj.optString("width"));
									pic.setUrl(picObj.optString("url"));
									pics.add(pic);
								}
							}
						}

						JSONObject userObj = data.optJSONObject("userInfo");
						if (userObj != null) {
							user = new UserInfo();
							user.setUserId(userObj.optString("userId"));
							user.setUserType(userObj.optString("userType"));
							user.setAvatar(userObj.optString("avatar"));
							user.setNickname(userObj.optString("nickName"));
							user.setBackground(userObj.optString("background"));
							JSONArray professionArray = userObj
									.optJSONArray("profession");
							String profession = "";
							if (professionArray != null) {
								for (int i = 0; i < professionArray.length(); i++) {
									if (i == 0) {
										profession += professionArray
												.optString(i);
									} else {
										profession += ","
												+ professionArray.optString(i);
									}
								}
							}
							user.setPermission(profession);
							user.setIsCertified(userObj
									.optString("isCertified"));
							user.setLocation(userObj.optString("location"));
							// 认证编码 certifyCode
							// tags
							JSONObject tagObj = userObj.optJSONObject("tags");
							if (null != tagObj) {
								Tags tag = new Tags();
								JSONArray interest = tagObj
										.optJSONArray("interest");
								if (interest != null) {
									List<String> list = tag.getInterests();
									for (int i = 0; i < interest.length(); i++) {
										list.add(interest.optString(i));
									}
									tag.setInterests(list);
								}

								JSONArray sphere = tagObj
										.optJSONArray("sphere");
								if (sphere != null) {
									List<String> list = tag.getSpheres();
									for (int i = 0; i < sphere.length(); i++) {
										list.add(sphere.optString(i));
									}
									tag.setSpheres(list);
								}

								JSONArray footprint = tagObj
										.optJSONArray("footprint");
								if (footprint != null) {
									List<String> list = tag.getFootprints();
									for (int i = 0; i < footprint.length(); i++) {
										list.add(footprint.optString(i));
									}
									tag.setFootprints(list);
								}

								JSONArray clubtype = tagObj
										.optJSONArray("club_type");
								if (clubtype != null) {
									List<String> list = tag.getClubTypes();
									for (int i = 0; i < clubtype.length(); i++) {
										list.add(clubtype.optString(i));
									}
									tag.setClubTypes(list);
								}

								JSONArray servarea = tagObj
										.optJSONArray("serv_area");
								if (servarea != null) {
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < servarea.length(); i++) {
										list.add(servarea.optString(i));
									}
									tag.setServAreas(list);
								}
								user.setTag(tag);
							}

							user.setRealName(userObj.optString("realName"));
							user.setGender(userObj.optString("sex"));
							user.setBirthday(userObj.optString("birthday"));
							user.setAddress(userObj.optString("address"));
							user.setOrgInfo(userObj.optString("orgInfo"));
							JSONArray languageArray = userObj
									.optJSONArray("languages");
							if (languageArray != null) {
								ArrayList<String> laglist = user
										.getLanguagesList();
								for (int i = 0; i < languageArray.length(); i++) {
									laglist.add(languageArray.optString(i));
								}
							}

							user.setIntroduction(userObj
									.optString("introduction"));
							user.setUsersIdentity(userObj
									.optString("usersIdentity"));
							user.setStatus(userObj.optString("status"));
							user.setMobile(userObj.optString("mobile"));
							user.setEmail(userObj.optString("email"));
							user.setProductCount(userObj
									.optString("productCount"));
							user.setServTimes(userObj.optString("servTimes"));
							user.setMembers(userObj.optString("members"));
							JSONObject objs = userObj.optJSONObject("orgRole");

							if (null != objs) {
								OrgRole role = new OrgRole();
								role.setRoleName(objs.optString("roleName"));
								role.setRoleCode(objs.optString("roleCode"));
								role.setRoleType(objs.optString("roleType"));
								role.setPermission(objs.optString("permission"));
								user.setOrgRole(role);
							}
							user.setOrgCreator(userObj.optString("orgCreator"));
							user.setCertifyCode(userObj
									.optString("certifyCode"));
							JSONObject exts = userObj.optJSONObject("exts");
							HashMap<String, String> map = new HashMap<String,String>();
							if (exts != null && exts.length() > 0) {
								
								JSONArray famousCommentArray = exts
										.optJSONArray("famous_comment");
								if (famousCommentArray != null) {
									for (int i = 0; i < famousCommentArray.length(); i++) {
										JSONObject famousCommentObj = famousCommentArray
												.optJSONObject(i);
										FamousComment famousComment = new FamousComment();
										famousComment.setComment(famousCommentObj
												.optString("comment"));
										famousComment.setName(famousCommentObj
												.optString("name"));
										user.getFamousCommentsList().add(
												famousComment);
									}
								}
//								user.getFamousCommentsList().add(
//										new FamousComment("呵呵", "呵呵"));
//								user.getFamousCommentsList().add(
//										new FamousComment("呵呵", "呵呵"));
//								user.getFamousCommentsList().add(
//										new FamousComment("呵呵", "呵呵"));
								map.put("school",
										exts.optString("school"));
								map.put("education",
										exts.optString("education"));
								map.put("work_time",
										exts.optString("work_time"));
								map.put("background",
										exts.optString("background"));
								user.setExts(map);
							}

							JSONArray experiencesArr = userObj
									.optJSONArray("experiences");
							if (null != experiencesArr
									&& experiencesArr.length() > 0) {
								for (int i = 0; i < experiencesArr.length(); i++) {
									JSONObject exObj = experiencesArr
											.optJSONObject(i);
									Experiences experiences = new Experiences();
									experiences.setTravelTime(exObj
											.optString("travelTime"));
									experiences.setDescription(exObj
											.optString("description"));

									List<ImageInfo> list = experiences
											.getImages();
									JSONArray ImageArr = exObj
											.optJSONArray("images");
									if (null != ImageArr
											&& ImageArr.length() > 0) {
										for (int j = 0; j < ImageArr.length(); j++) {
											ImageInfo imageInfo = new ImageInfo();
											JSONObject exObj1 = ImageArr
													.optJSONObject(j);
											imageInfo.setUrl(exObj1
													.optString("url"));
											imageInfo.setDesc(exObj1
													.optString("desc"));
											list.add(imageInfo);
										}
									}
									user.getExperiencesList().add(experiences);

								}
								if (!isDriverHomePage) {
//									expericeInfoFragment.setData(user
//											.getExperiencesList());
//									expericeInfoFragment.refeshData();
								}
							}

							user.setServerPolicy(userObj
									.optString("serverPolicy"));
							user.setFocus(userObj.optString("focus"));
							user.setFansCount(userObj.optString("fansCount"));
							user.setIsFocus(userObj.optString("isFocus"));
							if (userObj.optString("isFocus").equals("yes")) {
								mFocusTxt.setText("取消关注");
								isFocused = true;
							}
						}

					}
				}
				setValue();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(this);
			newView.setBackgroundResource(R.drawable.shape_person_info);
			;
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#5EB48A"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(this, 4);
			params.bottomMargin = DensityUtil.dip2px(this, 4);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	/**
	 * 查询个人资料
	 * 
	 * @param userId
	 */
	private void queryProductInfo(String userId) {
		RequestParams params = new RequestParams();
		params.put("pagesize", 100);
		params.put("pagenum", 1);
		params.put("userId", userId);
		HttpUtil.get(Urls.get_plist, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/**
	 * 定义一个实现OnRefreshListener接口的实现类对象
	 * 
	 * @author caihelin
	 * 
	 */
	class MyListener implements OnRefreshListener {

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			isLoadMore = true;
			isRefresh = 1;
			int status;
				switch (current_index) {
				case 0:

//					int status=tripInfoFragment.isNeedLoad();
//					if (status== 0) {
//						pullToRefreshLayout
//								.loadmoreFinish(PullToRefreshLayout.DONE);
//						Toast.makeText(DriverHomepageActivity.this, "没有内容",
//								Toast.LENGTH_SHORT).show();
//					} else if (status == 2) {
//						pullToRefreshLayout
//								.loadmoreFinish(PullToRefreshLayout.DONE);
//						Toast.makeText(DriverHomepageActivity.this, "没有更多的内容",
//								Toast.LENGTH_SHORT).show();

//					}
					pullToRefreshLayout
					.loadmoreFinish(PullToRefreshLayout.DONE);
					break;
				case 1:
					isLoadMore = false;
					pullToRefreshLayout
							.loadmoreFinish(PullToRefreshLayout.DONE);
					break;
				case 2:
					isLoadMore = false; 
					status=expericeInfoFragment.isNeedLoad();
					pullToRefreshLayout
							.loadmoreFinish(PullToRefreshLayout.DONE);
					if (status== 0) {
						pullToRefreshLayout
						.loadmoreFinish(PullToRefreshLayout.DONE);
//						Toast.makeText(DriverHomepageActivity.this, "没有更多的内容",
//								Toast.LENGTH_SHORT).show();
					}else if (status == 2) {
						pullToRefreshLayout
						.loadmoreFinish(PullToRefreshLayout.DONE);
//						Toast.makeText(DriverHomepageActivity.this, "没有内容",
//								Toast.LENGTH_SHORT).show();
					}
					break;
				
			
				case 3:
					status=productInfoFragment.isNeedLoad() ;
					if (status == 0) {
						pullToRefreshLayout
								.loadmoreFinish(PullToRefreshLayout.DONE);
//						Toast.makeText(DriverHomepageActivity.this, "没有内容",
//								Toast.LENGTH_SHORT).show();
					} else if (status == 2) {
						pullToRefreshLayout
								.loadmoreFinish(PullToRefreshLayout.DONE);
//						Toast.makeText(DriverHomepageActivity.this, "没有更多的内容",
//								Toast.LENGTH_SHORT).show();

					}
					
					break;
				}

		}

		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			// TODO Auto-generated method stub
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.DONE);
		}

	}

	public interface OnLoadMoreListener {
		public int isNeedLoad();
	}

	public void loadmoreFinish() {
		if (isLoadMore) {
//			pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.DONE);
			Toast.makeText(ClubMebHomepageActivity.this, "加载完毕",
					Toast.LENGTH_SHORT).show();
		}
		isLoadMore = false;
//		int i = vPager.getCurrentItem();
//		resetViewPagerHeight(vPager.getCurrentItem());
	}

	public void noMoreFinish() {
		if (isLoadMore) {
//			pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.DONE);
//			Toast.makeText(DriverHomepageActivity.this, "没有更多的内容",
//					Toast.LENGTH_SHORT).show();
		}
		isLoadMore = false;
	}

	private void addOrCancelFocus(boolean flag, final String userId) {
		if (!AppInfo.getIsLogin()) {
			ToastUtil.showToast(ClubMebHomepageActivity.this, "请登录");
			return;
		}
		if (!flag) {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", userId);
				StringEntity entity = new StringEntity(params.toString(),
						HTTP.UTF_8);
				HttpUtil.post(Urls.friend_list_url, entity,
						new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								super.onSuccess(statusCode, headers, response);
								String code = response.optString("code");
								if ("00000".equals(code)) {
									mFocusTxt.setText("取消关注");
									Intent intent=new Intent("com.bcinfo.refreshFocus");
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
			HttpUtil.delete(Urls.friend_list_url + "/" + userId,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							String code = response.optString("code");
							if ("00000".equals(code)) {
								mFocusTxt.setText("+关注");
								Intent intent=new Intent("com.bcinfo.refreshFocus");
								intent.putExtra("userId", userId);
								intent.putExtra("isFocus", false);
								sendBroadcast(intent);
								isFocused = false;
							}
						}
					});

		}
	}

	private void joinTeamTalk(String productId) {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("referType", "homepage");
		params.put("referId", productId);
		HttpUtil.get(Urls.join_team_talk, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if (!"00000".equals(code)) {
							return;
						}
						String queueId = response.optString("data");
						if (!StringUtils.isEmpty(queueId)) {
							// 查询队列详情并跳转
							queryTeamTalkInfo(queueId);
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
				});
	}

	private void queryTeamTalkInfo(final String queueId) {
		RequestParams params = new RequestParams();
		params.put("queueId", queueId);
		params.put("pagenum", 1);// 当前页码
		params.put("pagesize", 10);// 页码数
		params.put("msgId", "");
		HttpUtil.get(Urls.message_queue_url, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if (!"00000".equals(code)) {
							return;
						}
						JSONObject data = response.optJSONObject("data");
						JSONObject product = data.optJSONObject("product");
						Intent intent = new Intent(ClubMebHomepageActivity.this,
								ChatActivity.class);
						intent.putExtra("queueId", queueId);
					intent.putExtra("chatTitle", user.getRealName());
				
						if (product != null) {
							intent.putExtra("title", product.optString("title"));
						} else {
							intent.putExtra("title", "");
						}
						intent.putExtra("pattern", "team");
						intent.putExtra("fromQueue", true);
						intent.putExtra("isTeam", true);
						intent.putExtra("isTeam2", true);
						startActivity(intent);
						overridePendingTransition(R.anim.activity_new,
								R.anim.activity_out);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}
				});
	}
	
	private void joinPrivateChat() {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("referType", "user");
		params.put("referId", user.getUserId());
		HttpUtil.get(Urls.join_private_chat, params, new JsonHttpResponseHandler() {
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
//				if (!StringUtils.isEmpty(queueId)) {
//					// 查询队列详情并跳转
//					queryPrivateChatInfo(queueId);
					Intent talkIntent = new Intent(ClubMebHomepageActivity.this,
							ChatActivity.class);
					talkIntent.putExtra("queueId", queueId);
					talkIntent.putExtra("receiveId", user.getUserId());
					talkIntent.putExtra("title", user.getNickname());
					talkIntent.putExtra("isTeam", false);
					talkIntent.putExtra("fromQueue", true);
					startActivity(talkIntent);
					activityAnimationOpen();
//				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}
	
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if(alpha!=-1){
				layout_product_detail_title.getBackground().setAlpha(alpha);
			}
		}

}
