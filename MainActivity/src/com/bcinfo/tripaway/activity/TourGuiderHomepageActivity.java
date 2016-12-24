package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.fragment.ProductInfoFragment;
import com.bcinfo.tripaway.fragment.TourGuideExperienceFragment;
import com.bcinfo.tripaway.fragment.TripInfoFragment;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView.PullListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

public class TourGuiderHomepageActivity extends BaseActivity implements
		PersonalScrollViewListener, OnClickListener {
	private ViewPager vPager = null;

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

	private int[] selectedImage = { 
			R.drawable.trip_selected,R.drawable.experience_selected, R.drawable.product_checked };
	private int[] normalImage = { 
			R.drawable.trip_normal, R.drawable.experience,R.drawable.product_normal };

	/**
	 * 选项卡标题
	 */
	private RadioButton navIV_1, navIV_2, navIV_3;

	/**
	 * 选项卡点击
	 */
	private RadioButton[] titles = new RadioButton[3];
	private ProductDetailScrollView mProductScrollView;
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
	private TextView introductionTextView;

	private ImageView sexImageView;
	private RoundImageView photoRoundImageView;

	private FlowLayout interestTagFlow;

	private LinearLayout workYearLayout;

	
	private CarExt carInfo;
	private List<Experiences> experiencesList=new ArrayList<Experiences>();
	private TripInfoFragment tripInfoFragment;
	private TourGuideExperienceFragment tourGuideExperienceFragment;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_guider_homepage);
		userId = getIntent().getStringExtra("identifyId");
		queryUserInfo(userId);
		tripInfoFragment =new TripInfoFragment(userId);
		
		initRadioGroup();
		initPagerView();
		new myAsyncTask().execute();
		mProductHeadImg = (ImageView) findViewById(R.id.personpic);

		mProductScrollView = (ProductDetailScrollView) findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setScrollListener(this);
		mProductScrollView.setPullListener(mPullListener);
		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(0);
		layout_service_list = (LinearLayout) findViewById(R.id.layout_service_list);
		moreTextView = (LinearLayout) findViewById(R.id.more_info);
		moreTextView.setOnClickListener(this);
		
		findView();
		
	}

	private void findView() {
		focusNumTextView = (TextView) this.findViewById(R.id.focus_num);
		fansNumTextView = (TextView) this.findViewById(R.id.fans_num);
		serviceNumTextView = (TextView) this.findViewById(R.id.service_num);
		driverYearTextView = (TextView) this.findViewById(R.id.driver_year);
		nickNameTextView = (TextView) this.findViewById(R.id.user_nickname);
		groupNameTextView = (TextView) this.findViewById(R.id.group_name);
		introductionTextView = (TextView) this.findViewById(R.id.introduction);

		interestTagFlow = (FlowLayout) this
				.findViewById(R.id.interest_tag_flow);

		sexImageView = (ImageView) this.findViewById(R.id.sex);
		photoRoundImageView = (RoundImageView) this
				.findViewById(R.id.personal_icon);

		workYearLayout = (LinearLayout) this
				.findViewById(R.id.work_year_layout);
	}

	private void setValue() {
		focusNumTextView.setText(user.getFocus() == null ? "0" : user
				.getFocus());
		fansNumTextView.setText(user.getFansCount() == null ? "0" : user
				.getFansCount());
		serviceNumTextView.setText(user.getServTimes() == null ? "0人" : user
				.getServTimes() + "人");
		nickNameTextView.setText(user.getRealName());
		groupNameTextView.setText(user.getOrgInfo());
		introductionTextView.setText(user.getIntroduction());
		addFlowView(user.getTag().getInterests(), interestTagFlow);
		if (user.getGender().equals("1")) {
			sexImageView.setImageResource(R.drawable.man);
		} else {
			sexImageView.setImageResource(R.drawable.woman);
		}
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + user.getAvatar(), photoRoundImageView,
					AppConfig.options(R.drawable.user_defult_photo));
		}
		Map<String, String> exts = user.getExts();
		if (!StringUtils.isEmpty(exts.get("work_time"))) {
			driverYearTextView.setText(exts.get("work_time"));
		} else {
			workYearLayout.setVisibility(View.GONE);
		}
		
	}

	private void initRadioGroup() {

		navIV_1 = (RadioButton) findViewById(R.id.radio_1_text);
		navIV_2 = (RadioButton) findViewById(R.id.radio_2_text);
		navIV_3 = (RadioButton) findViewById(R.id.radio_3_text);
		titles[0] = navIV_1;
		titles[1] = navIV_2;
		titles[2] = navIV_3;
		navIV_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					vPager.setCurrentItem(0);
					navIV_2.setChecked(false);
					navIV_3.setChecked(false);
					// navTextView[]
				}
			}
		});
		navIV_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					vPager.setCurrentItem(1);
					navIV_1.setChecked(false);
					navIV_3.setChecked(false);
				}
			}
		});
		navIV_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					vPager.setCurrentItem(2);
					navIV_1.setChecked(false);
					navIV_2.setChecked(false);
				}
			}
		});
	}

	private void initPagerView() {
		vPager = (ViewPager) findViewById(R.id.vPager);
		fragments.add(tripInfoFragment);

		fragments.add(new TourGuideExperienceFragment(experiencesList));
		fragments.add(new ProductInfoFragment(userId,layout_product_detail_title));
		vPager.setOffscreenPageLimit(fragments.size());

		vPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return TAB_COUNT;
			}

			@Override
			public Fragment getItem(int index)// 直接创建fragment对象并返回
			{
				return fragments.get(index);
			}
		});
		vPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index)// 设置标题的颜色以及下划线的移动效果
			{
				resetViewPagerHeight(index);

				Drawable selectedDrawable = getResources().getDrawable(
						selectedImage[index]);
				Drawable normalDrawable = getResources().getDrawable(
						normalImage[current_index]);
				// / 这一步必须要做,否则不会显示.
				selectedDrawable.setBounds(0, 0,
						selectedDrawable.getMinimumWidth(),
						selectedDrawable.getMinimumHeight());
				normalDrawable.setBounds(0, 0,
						normalDrawable.getMinimumWidth(),
						normalDrawable.getMinimumHeight());
				titles[current_index].setCompoundDrawables(normalDrawable,
						null, null, null);
				titles[index].setCompoundDrawables(selectedDrawable, null,
						null, null);
				titles[index].setTextColor(Color.BLACK);
				titles[current_index].setTextColor(Color.parseColor("#888888"));
				current_index = index;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int index) {
			}
		});

	}

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY,
			int prePositionX, int prePositionY) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (view != null && view == mProductScrollView) {
			int alpha = positionY / 3;
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
		} else {
			return;
		}
	}

	/**
	 * 重新设置viewPager高度
	 * 
	 * @param position
	 */
	public void resetViewPagerHeight(int position) {
		View child = fragments.get(position).getView();
		if (child != null) {
			child.measure(0, 0);
			int h = child.getMeasuredHeight();
			LinearLayout.LayoutParams params = (LayoutParams) vPager
					.getLayoutParams();
			params.height = h + 50;
			vPager.setLayoutParams(params);
		}
	}

	public class myAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			resetViewPagerHeight(0);
		}
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
			layout_service_list.setAlpha(alpha / 255f);
			// product_introduce.setAlpha(alpha / 255f);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more_info:
			Intent moreIntent = new Intent(TourGuiderHomepageActivity.this,
					ClubMebMoreInfoActivity.class);
			moreIntent.putExtra("user", user);
			startActivity(moreIntent);
			activityAnimationOpen();
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
							List<ImageInfo> pics=carInfo.getCarPics();
							JSONArray picsArray = carObj
									.optJSONArray("pics");
							if (picsArray != null) {
								for (int i = 0; i < picsArray.length(); i++) {
									ImageInfo pic=new ImageInfo();
									JSONObject picObj=picsArray.optJSONObject(i);
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
							user.setNickname(userObj.optString("nickname"));
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
								ArrayList<String> laglist = new ArrayList<String>();
								for (int i = 0; i < languageArray.length(); i++) {
									laglist.add(languageArray.optString(i));
								}
								user.setLanguagesList(laglist);
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
							map.put("education", exts.optString("education"));
							map.put("work_time", exts.optString("work_time"));
							user.setExts(map);

							 JSONArray experiencesArr =
							 userObj.optJSONArray("experiences");
							 if(null != experiencesArr){
							 for (int i = 0; i < experiencesArr.length(); i++)
							 {
							 JSONObject exObj =
							 experiencesArr.optJSONObject(i);
							 Experiences experiences = new Experiences();
							 experiences.setDescription(exObj.optString("description"));
							 JSONArray imgAry=exObj.optJSONArray("images");
							 if(imgAry!=null&&imgAry.length()>0){
								 for (int j = 0; j < imgAry.length(); j++) {
									 JSONObject imgObj=imgAry.optJSONObject(j);
									 
									 experiences.getImagesUrls().add(imgObj.optString("url"));
									 
								 }
							 }
//							 experiencesList=new ArrayList<>();
							 experiencesList.add(experiences);
							 }
							 }
							 
							user.setServerPolicy(userObj
									.optString("serverPolicy"));
							user.setFocus(userObj.optString("focus"));
							user.setFansCount(userObj.optString("fansCount"));
							user.setIsFocus(userObj.optString("isFocus"));
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
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			newView.setTextColor(Color.BLACK);
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(this, 2);
			params.bottomMargin = DensityUtil.dip2px(this, 5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}
}
