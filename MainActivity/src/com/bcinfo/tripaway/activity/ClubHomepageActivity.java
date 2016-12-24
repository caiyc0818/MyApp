package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ClubFragmentAdapter;
import com.bcinfo.tripaway.bean.Cats;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.fragment.ActionFragment;
import com.bcinfo.tripaway.fragment.BaseFragment;
import com.bcinfo.tripaway.fragment.ClubHomePageFragment;
import com.bcinfo.tripaway.fragment.ClubProductsFragment;
import com.bcinfo.tripaway.fragment.ClubTravelsFragment;
import com.bcinfo.tripaway.fragment.ClubTrendsFragment;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView.PullListener;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ClubHomepageActivity extends BaseActivity implements OnClickListener, PersonalScrollViewListener {

	private ImageView mBackgroundLy;

	private RoundImageView mTitleIm;

	// private ViewPager mViewPager;

	private FrameLayout mFrameLayout;

	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

	private ImageView cursor = null;

	private ClubHomePageFragment clubHomePageFragment;
	/**
	 * 游记Fragment
	 */
	private ClubTravelsFragment clubTravelsFragment;

	private ClubTrendsFragment clubTrendsFragment;;

	private ClubProductsFragment clubProductsFragment;

	private ActionFragment actionFragment;
	private TextView mHomePage;

	/**
	 * 游记
	 */
	private TextView mTravels;

	private TextView mTrends;

	private TextView mProducts;

	private TextView mTitle;
	private TextView mAtion;

	private ClubFragmentAdapter clubFragmentAdapter;

	private UserInfo userInfo = new UserInfo();

	private UserInfoEx infoEx = new UserInfoEx();

	private String backgroundPath;

	private TextView mUserName;

	private TextView mBuserCount;

	private TextView mGuiderCount;

	private TextView mFocusCount;

	private TextView mFansCount;
	private RelativeLayout layout_product_detail_title;
	private ProductDetailScrollView mProductScrollView;
	private ImageView mProductHeadImg;
	private LinearLayout mBackBtn;
	private LinearLayout mAddFocusBtn;
	private boolean isFocused = false;
	private LinearLayout mComplainBtn;
	private LinearLayout mShareBtn;
	private LinearLayout mTeamTalkBtn;
	private LinearLayout mSimpleTalkBtn;
	private TextView mFocusTxt;
	private int currentIndex = 0;
	private TextView mTxt;
	private TextView mTxt3;
	private TextView mTxt4;
	/**
	 * 关注数
	 */
	private int count;
	/**
	 * 游记
	 */
	private TextView mTxt1;

	private int alpha = 0;
	/**
	 * 选项卡下划线长度
	 */
	private static int lineWidth = 0;

	/**
	 * 偏移量 （手机屏幕宽度/3-选项卡长度）/2
	 */
	private static int offset = 0;
	private int current_index = 0;
	private TextView[] titles = new TextView[5];
	private RelativeLayout radioBtnLayout;
	private LinearLayout radionBtnLayout1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.club_homepage);
		userInfo = getIntent().getParcelableExtra("userInfo");
		if (!StringUtils.isEmpty(userInfo.getNickname())) {
			statisticsTitle = userInfo.getNickname();
		}
		// backgroundPath = getIntent().getStringExtra("background");
		initView();
		// initImageView();
		queryUserInfo(userInfo.getUserId());
		// new myAsyncTask().execute();
		registerBoradcastReceiver();
		// initFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		layout_product_detail_title.getBackground().setAlpha(alpha);
	}

	@Override
	protected void initView() {
		super.initView();
		radioBtnLayout = (RelativeLayout) findViewById(R.id.main_radiogroup_root);
		radionBtnLayout1 = (LinearLayout) findViewById(R.id.main_radiogroup_root1);
		mTxt = (TextView) findViewById(R.id.club_homepage1);
		mTxt3 = (TextView) findViewById(R.id.club_products1);
		mTxt1 = (TextView) findViewById(R.id.club_travels1);
		mTxt4 = (TextView) findViewById(R.id.club_action1);

		mTxt4.setOnClickListener(transactionListener);
		mTxt.setOnClickListener(transactionListener);
		mTxt3.setOnClickListener(transactionListener);
		mTxt1.setOnClickListener(transactionListener);
		mTitle = (TextView) findViewById(R.id.club_name_title);
		mTitle.setText(userInfo.getNickname());
		mTitle.setAlpha(0);
		mTeamTalkBtn = (LinearLayout) findViewById(R.id.teamtalk_btn);
		mSimpleTalkBtn = (LinearLayout) findViewById(R.id.simpletalk_btn);
		mTeamTalkBtn.setOnClickListener(this);
		mSimpleTalkBtn.setOnClickListener(this);
		mShareBtn = (LinearLayout) findViewById(R.id.share_btn);
		mShareBtn.setOnClickListener(this);
		mComplainBtn = (LinearLayout) findViewById(R.id.complain_btn);
		mComplainBtn.setOnClickListener(this);
		mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
		mFocusTxt = (TextView) findViewById(R.id.focus_txt);
		mBackBtn = (LinearLayout) findViewById(R.id.layout_back_button);
		mBackBtn.setOnClickListener(this);
		mAddFocusBtn = (LinearLayout) findViewById(R.id.add_focus);
		mAddFocusBtn.setOnClickListener(this);
		mProductScrollView = (ProductDetailScrollView) findViewById(R.id.product_detail_scroll_view);
		mProductScrollView.setImageView(mProductHeadImg);
		mProductScrollView.setScrollListener(this);
		mProductScrollView.setPullListener(mPullListener);
		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(0);
		layout_product_detail_title.setFocusable(true);
		layout_product_detail_title.setFocusableInTouchMode(true);
		layout_product_detail_title.requestFocus();
		mTitleIm = (RoundImageView) findViewById(R.id.club_author_photo);
		mUserName = (TextView) findViewById(R.id.club_name);
		mBuserCount = (TextView) findViewById(R.id.club_buser_count);
		mGuiderCount = (TextView) findViewById(R.id.club_guider_count);
		mFocusCount = (TextView) findViewById(R.id.club_focus_count);
		mFansCount = (TextView) findViewById(R.id.club_fans_count);
		mBackgroundLy = (ImageView) findViewById(R.id.page_background);
		backgroundPath = userInfo.getBackground();
		if (!StringUtils.isEmpty(backgroundPath)) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + backgroundPath, mBackgroundLy,
					AppConfig.options(R.drawable.summer));
		}

		mUserName.setText(userInfo.getNickname());
		// mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mHomePage = (TextView) findViewById(R.id.club_homepage);
		mTrends = (TextView) findViewById(R.id.club_trends);
		mTravels = (TextView) findViewById(R.id.club_travels);
		mProducts = (TextView) findViewById(R.id.club_products);
		mAtion = (TextView) findViewById(R.id.club_action);
		titles[0] = mHomePage;
		titles[1] = mTravels;
		titles[2] = mTrends;
		titles[3] = mProducts;
		titles[4] = mAtion;
		mAtion.setOnClickListener(transactionListener);
		mHomePage.setOnClickListener(transactionListener);
		mTravels.setOnClickListener(transactionListener);
		mTrends.setOnClickListener(transactionListener);
		mProducts.setOnClickListener(transactionListener);
	}

	private void initFragment(UserInfoEx infoEx) {

		clubHomePageFragment = new ClubHomePageFragment();
		clubHomePageFragment.setContext(ClubHomepageActivity.this);
		clubHomePageFragment.setUserInfo(infoEx.getUserInfo());
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame1, clubHomePageFragment).commit();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (clubHomePageFragment != null) {
			transaction.hide(clubHomePageFragment);
		}
		if (clubTravelsFragment != null) {
			transaction.hide(clubTravelsFragment);
		}
		if (clubTrendsFragment != null) {
			transaction.hide(clubTrendsFragment);
		}
		if (clubProductsFragment != null) {
			transaction.hide(clubProductsFragment);
		}
		if (actionFragment != null) {
			transaction.hide(actionFragment);
		}

	}

	private OnClickListener transactionListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			hideFragments(transaction);
			switch (v.getId()) {
			case R.id.club_homepage1:
				mTxt.setTextColor(getResources().getColor(R.color.dark_gray));
				mTxt1.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt3.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt4.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt1.setBackgroundColor(Color.WHITE);
				mTxt3.setBackgroundColor(Color.WHITE);
				mTxt4.setBackgroundColor(Color.WHITE);
				mTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
			case R.id.club_homepage:
				currentIndex = 0;
				mHomePage.setTextColor(getResources().getColor(R.color.dark_gray));
				mTravels.setTextColor(getResources().getColor(R.color.gray_text));
				mAtion.setTextColor(getResources().getColor(R.color.gray_text));
				mTrends.setTextColor(getResources().getColor(R.color.gray_text));
				mProducts.setTextColor(getResources().getColor(R.color.gray_text));
				mTrends.setBackgroundColor(Color.WHITE);
				mProducts.setBackgroundColor(Color.WHITE);
				mTravels.setBackgroundColor(Color.WHITE);
				mAtion.setBackgroundColor(Color.WHITE);
				mHomePage.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				// mViewPager.setCurrentItem(0);
				if (clubHomePageFragment == null) {
					clubHomePageFragment = new ClubHomePageFragment();
					clubHomePageFragment.setContext(ClubHomepageActivity.this);
					clubHomePageFragment.setUserInfo(infoEx.getUserInfo());
					transaction.add(R.id.content_frame1, clubHomePageFragment);
				} else {
					transaction.show(clubHomePageFragment);
				}
				break;
			case R.id.club_travels1:
				mTxt1.setTextColor(getResources().getColor(R.color.dark_gray));
				mTxt.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt3.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt4.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt.setBackgroundColor(Color.WHITE);
				mTxt3.setBackgroundColor(Color.WHITE);
				mTxt4.setBackgroundColor(Color.WHITE);
				mTxt1.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
			case R.id.club_travels:// 游记
				currentIndex = 1;
				mTravels.setTextColor(getResources().getColor(R.color.dark_gray));
				mProducts.setTextColor(getResources().getColor(R.color.gray_text));
				mTrends.setTextColor(getResources().getColor(R.color.gray_text));
				mAtion.setTextColor(getResources().getColor(R.color.gray_text));
				mHomePage.setTextColor(getResources().getColor(R.color.gray_text));
				mProducts.setBackgroundColor(Color.WHITE);
				mTrends.setBackgroundColor(Color.WHITE);
				mHomePage.setBackgroundColor(Color.WHITE);
				mAtion.setBackgroundColor(Color.WHITE);
				mTravels.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				if (clubTravelsFragment == null) {
					clubTravelsFragment = new ClubTravelsFragment(ClubHomepageActivity.this, infoEx.getUserInfo(),
							true);
					transaction.add(R.id.content_frame1, clubTravelsFragment);
				} else {
					transaction.show(clubTravelsFragment);
				}
				break;
			case R.id.club_trends:
				currentIndex = 2;
				mTrends.setTextColor(getResources().getColor(R.color.dark_gray));
				mTravels.setTextColor(getResources().getColor(R.color.gray_text));
				mProducts.setTextColor(getResources().getColor(R.color.gray_text));
				mAtion.setTextColor(getResources().getColor(R.color.gray_text));
				mHomePage.setTextColor(getResources().getColor(R.color.gray_text));
				mProducts.setBackgroundColor(Color.WHITE);
				mTravels.setBackgroundColor(Color.WHITE);
				mHomePage.setBackgroundColor(Color.WHITE);
				mAtion.setBackgroundColor(Color.WHITE);
				mTrends.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				if (clubTrendsFragment == null) {
					clubTrendsFragment = new ClubTrendsFragment(ClubHomepageActivity.this, infoEx.getUserInfo());
					transaction.add(R.id.content_frame1, clubTrendsFragment);
				} else {
					transaction.show(clubTrendsFragment);
				}
				break;
			case R.id.club_products1:
				mTxt3.setTextColor(getResources().getColor(R.color.dark_gray));
				mTxt1.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt4.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt1.setBackgroundColor(Color.WHITE);
				mTxt.setBackgroundColor(Color.WHITE);
				mTxt4.setBackgroundColor(Color.WHITE);
				mTxt3.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
			case R.id.club_products:
				currentIndex = 3;
				mProducts.setTextColor(getResources().getColor(R.color.dark_gray));
				mTravels.setTextColor(getResources().getColor(R.color.gray_text));
				mTrends.setTextColor(getResources().getColor(R.color.gray_text));
				mHomePage.setTextColor(getResources().getColor(R.color.gray_text));
				mAtion.setTextColor(getResources().getColor(R.color.gray_text));
				mTravels.setBackgroundColor(Color.WHITE);
				mTrends.setBackgroundColor(Color.WHITE);
				mHomePage.setBackgroundColor(Color.WHITE);
				mAtion.setBackgroundColor(Color.WHITE);
				mProducts.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				if (clubProductsFragment == null) {
					clubProductsFragment = new ClubProductsFragment(ClubHomepageActivity.this, infoEx);
					transaction.add(R.id.content_frame1, clubProductsFragment);
				} else {
					transaction.show(clubProductsFragment);
				}
				break;

			case R.id.club_action1:
				mTxt3.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt4.setTextColor(getResources().getColor(R.color.dark_gray));
				mTxt1.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt.setTextColor(getResources().getColor(R.color.gray_text));
				mTxt1.setBackgroundColor(Color.WHITE);
				mTxt.setBackgroundColor(Color.WHITE);
				mTxt3.setBackgroundColor(Color.WHITE);
				mTxt4.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));

			case R.id.club_action:
				currentIndex = 4;
				mAtion.setTextColor(getResources().getColor(R.color.dark_gray));
				mProducts.setTextColor(getResources().getColor(R.color.gray_text));
				mTravels.setTextColor(getResources().getColor(R.color.gray_text));
				mTrends.setTextColor(getResources().getColor(R.color.gray_text));
				mHomePage.setTextColor(getResources().getColor(R.color.gray_text));
				mProducts.setBackgroundColor(Color.WHITE);
				mTravels.setBackgroundColor(Color.WHITE);
				mTrends.setBackgroundColor(Color.WHITE);
				mHomePage.setBackgroundColor(Color.WHITE);
				mAtion.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				if (actionFragment == null) {
					actionFragment = new ActionFragment(ClubHomepageActivity.this, infoEx.getUserInfo());
					transaction.add(R.id.content_frame1, actionFragment);
				} else {
					transaction.show(actionFragment);
				}
				break;
			default:
				break;
			}

			transaction.commitAllowingStateLoss();

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			break;
		case R.id.add_focus:
			addOrCancelFocus(isFocused, userInfo.getUserId());
			// isFocused = !isFocused;
			break;
		case R.id.complain_btn:
			if (!AppInfo.getIsLogin()) {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				return;
			}
			Intent intent1 = new Intent(ClubHomepageActivity.this, ConsultOrComplaintActivity.class);
			intent1.putExtra("type", "complaint");
			startActivity(intent1);
			break;
		case R.id.share_btn:
			ShareSelectDialog shareSelectDialog = new ShareSelectDialog(this, userInfo.getAvatar(),
					userInfo.getIntroduction(), userInfo.getNickname(), userInfo.getUserId(), Urls.ShareUrlOfUser,
					userInfo.getNickname());
			shareSelectDialog.show();
			break;
		case R.id.teamtalk_btn:
			joinTeamTalk(userInfo.getUserId());
			break;
		case R.id.simpletalk_btn:
			if (!AppInfo.getIsLogin()) {
				goLoginActivity();
				return;
			}
			// Intent talkIntent = new Intent(this, ChatActivity.class);
			// talkIntent.putExtra("receiveId", userInfo.getUserId());
			// talkIntent.putExtra("title", userInfo.getNickname());
			// startActivity(talkIntent);
			// activityAnimationOpen();
			joinPrivateChat();
			break;
		default:
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
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONObject data = response.optJSONObject("data");
					if (data != null) {
						// 司机数 领队数 导游数 群聊人数
						infoEx.setGuideNum(data.optInt("guide", 0));
						infoEx.setDriverNum(data.optInt("driver", 0));
						infoEx.setLeaderNum(data.optInt("leader", 0));
						infoEx.setGroupChatCount(data.optInt("groupChatCount", 0));
						JSONArray catsArray = data.optJSONArray("cats");
						ArrayList<Cats> catsList = new ArrayList<Cats>();
						if (catsArray != null) {
							for (int i = 0; i < catsArray.length(); i++) {
								JSONObject obj = catsArray.optJSONObject(i);
								Cats cats = new Cats();
								cats.setCatId(obj.optString("catId"));
								cats.setCover(obj.optString("cover"));
								cats.setCustTitle(obj.optString("custTitle"));
								catsList.add(cats);
							}
						}
						infoEx.setCats(catsList);
						JSONObject userObj = data.optJSONObject("userInfo");
						if (userObj != null) {
							UserInfo user = new UserInfo();
							user.setUserId(userObj.optString("userId"));
							user.setUserType(userObj.optString("userType"));
							user.setAvatar(userObj.optString("avatar"));
							user.setNickname(userObj.optString("nickname"));
							JSONArray professionArray = userObj.optJSONArray("profession");
							String profession = "";
							if (professionArray != null) {
								for (int i = 0; i < professionArray.length(); i++) {
									if (i == 0) {
										profession += professionArray.optString(i);
									} else {
										profession += "," + professionArray.optString(i);
									}
								}
							}
							user.setPermission(profession);
							user.setIsCertified(userObj.optString("isCertified"));
							user.setLocation(userObj.optString("location"));
							// 认证编码 certifyCode
							// tags
							JSONObject tagObj = userObj.optJSONObject("tags");
							if (null != tagObj) {
								Tags tag = new Tags();
								JSONArray interest = tagObj.optJSONArray("interest");
								if (interest != null) {
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < interest.length(); i++) {
										list.add(interest.optString(i));
									}
									tag.setInterests(list);
								}

								JSONArray sphere = tagObj.optJSONArray("sphere");
								if (sphere != null) {
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < sphere.length(); i++) {
										list.add(sphere.optString(i));
									}
									tag.setSpheres(list);
								}

								JSONArray footprint = tagObj.optJSONArray("footprint");
								if (footprint != null) {
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < footprint.length(); i++) {
										list.add(footprint.optString(i));
									}
									tag.setFootprints(list);
								}

								JSONArray clubtype = tagObj.optJSONArray("club_type");
								if (clubtype != null) {
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < clubtype.length(); i++) {
										list.add(clubtype.optString(i));
									}
									tag.setClubTypes(list);
								}

								JSONArray servarea = tagObj.optJSONArray("serv_area");
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
							JSONArray languageArray = userObj.optJSONArray("languages");
							if (languageArray != null) {
								ArrayList<String> laglist = new ArrayList<String>();
								for (int i = 0; i < languageArray.length(); i++) {
									laglist.add(languageArray.optString(i));
								}
								user.setLanguagesList(laglist);
							}

							user.setIntroduction(userObj.optString("introduction"));
							user.setUsersIdentity(userObj.optString("usersIdentity"));
							user.setStatus(userObj.optString("status"));
							user.setMobile(userObj.optString("mobile"));
							user.setEmail(userObj.optString("email"));
							user.setProductCount(userObj.optString("productCount"));
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
							user.setCertifyCode(userObj.optString("certifyCode"));
							JSONObject exts = userObj.optJSONObject("exts");
							HashMap<String, String> map = new HashMap<String, String>();
							if (exts != null) {
								map.put("referContent", exts.optString("refer_content"));
								map.put("referScore", exts.optString("refer_score"));
							}
							user.setExts(map);

							// JSONArray experiencesArr =
							// userObj.optJSONArray("experiences");
							// if(null != experiencesArr){
							// for (int i = 0; i < experiencesArr.length(); i++)
							// {
							// JSONObject exObj =
							// experiencesArr.optJSONObject(i);
							// Experiences experiences = new Experiences();
							// experiences.setDescription(exObj.optString("description"));
							// experiences.setDescription(exObj.optString("description"));
							// }
							// }

							user.setServerPolicy(userObj.optString("serverPolicy"));
							user.setFocus(userObj.optString("focus"));
							user.setFansCount(userObj.optString("fansCount"));
							user.setIsFocus(userObj.optString("isFocus"));
							infoEx.setUserInfo(user);
						}

					}
					initUserInfo(infoEx);
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

	private void initUserInfo(UserInfoEx infoEx) {
		UserInfo info = infoEx.getUserInfo();
		if (!StringUtils.isEmpty(infoEx.getUserInfo().getAvatar())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + infoEx.getUserInfo().getAvatar(), mTitleIm,
					AppConfig.options(R.drawable.user_defult_photo));
		}
		mFansCount.setText("粉丝 " + info.getFansCount());
		mFocusCount.setText("关注 " + info.getFocus());
		count = Integer.parseInt(info.getFocus());
		if (0 == infoEx.getGuideNum()) {
			mGuiderCount.setVisibility(View.GONE);
		} else {
			mGuiderCount.setVisibility(View.VISIBLE);
			mGuiderCount.setText("导游 " + infoEx.getGuideNum());
		}

		if (infoEx.getDriverNum() == 0) {
			mBuserCount.setVisibility(View.GONE);
		} else {
			mBuserCount.setVisibility(View.VISIBLE);
			mBuserCount.setText("司机 " + infoEx.getDriverNum());
		}

		if ("yes".equals(info.getIsFocus())) {
			isFocused = true;
			mFocusTxt.setText("已关注");
		} else {
			isFocused = false;
			mFocusTxt.setText("+关注");
		}
		initFragment(infoEx);
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

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY) {
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
			mTitle.setAlpha(alpha / 255f);
			// product_title.setAlpha(alpha / 255f);
			int[] loactions = new int[2];
			int[] loactions1 = new int[2];
			radioBtnLayout.getLocationOnScreen(loactions);
			radionBtnLayout1.getLocationOnScreen(loactions1);
			if (loactions[1] < loactions1[1]) {
				radionBtnLayout1.setVisibility(View.VISIBLE);
				if (currentIndex == 0) {
					mTxt.setTextColor(getResources().getColor(R.color.dark_gray));
					mTxt1.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt3.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt4.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt1.setBackgroundColor(Color.WHITE);
					mTxt3.setBackgroundColor(Color.WHITE);
					mTxt4.setBackgroundColor(Color.WHITE);
					mTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				} else if (currentIndex == 1) {
					mTxt1.setTextColor(getResources().getColor(R.color.dark_gray));
					mTxt.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt3.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt4.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt.setBackgroundColor(Color.WHITE);
					mTxt3.setBackgroundColor(Color.WHITE);
					mTxt4.setBackgroundColor(Color.WHITE);
					mTxt1.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				} else if (currentIndex == 3) {
					mTxt3.setTextColor(getResources().getColor(R.color.dark_gray));
					mTxt.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt1.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt4.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt.setBackgroundColor(Color.WHITE);
					mTxt1.setBackgroundColor(Color.WHITE);
					mTxt4.setBackgroundColor(Color.WHITE);
					mTxt3.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				} else if (currentIndex == 4) {
					mTxt4.setTextColor(getResources().getColor(R.color.dark_gray));
					mTxt1.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt3.setTextColor(getResources().getColor(R.color.gray_text));
					mTxt.setBackgroundColor(Color.WHITE);
					mTxt1.setBackgroundColor(Color.WHITE);
					mTxt3.setBackgroundColor(Color.WHITE);
					mTxt4.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bottom_border_bg));
				}
			} else {
				radionBtnLayout1.setVisibility(View.GONE);
			}

			View view1 = mProductScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = mProductScrollView.getScrollY();
			int scollHeight = mProductScrollView.getHeight();
			if (height <= scollY + scollHeight) {
				// 底部
				if (currentIndex == 1) {
					Intent it = new Intent("com.bcinfo.loadMoreTravels");
					sendBroadcast(it);
				} else if (currentIndex == 2) {
					Intent it = new Intent("com.bcinfo.loadMoreTrends");
					sendBroadcast(it);
				} else if (currentIndex == 3) {
					Intent it = new Intent("com.bcinfo.loadMoreProducts");
					sendBroadcast(it);
				} else if (currentIndex == 4) {
					Intent it = new Intent("com.bcinfo.loadMoreActions");
					sendBroadcast(it);
				}
			} else if (scollY == 0) {
				// 顶部
			}
		} else {
			return;
		}

	}

	// public void resetViewPagerHeight(int position) {
	// if(fragmentList.size() <= position){
	// return;
	// }
	// View child = fragmentList.get(position).getView();
	// if (child != null) {
	// child.measure(0, 0);
	// int h = child.getMeasuredHeight();
	// LinearLayout.LayoutParams params = (LayoutParams) mViewPager
	// .getLayoutParams();
	// params.height = h + 50;
	// mViewPager.setLayoutParams(params);
	// }
	// }

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
			// resetViewPagerHeight(0);
		}
	}

	private void initImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		// 获取图片宽度
		lineWidth = dip2px(this, 65);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 获取屏幕宽度
		offset = (int) cursor.getX();
		// (int) ((screenWidth/(float)3 - lineWidth)/2);
		// 设置初始位置
		MarginLayoutParams margin = new MarginLayoutParams(cursor.getLayoutParams());
		margin.setMargins(offset, 0, offset + margin.width, 0 + margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		cursor.setLayoutParams(layoutParams);
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private void addOrCancelFocus(boolean flag, String userId) {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
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
							isFocused = true;
							// mFocusCount.setText("关注 " + (count+1));
							Intent intent = new Intent("com.bcinfo.clubToFragmentRefreshFocus");
							intent.putExtra("userId", infoEx.getUserInfo().getUserId());
							intent.putExtra("isFocus", true);
							ClubHomepageActivity.this.sendBroadcast(intent);
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
						isFocused = false;
						Intent intent = new Intent("com.bcinfo.clubToFragmentRefreshFocus");
						intent.putExtra("userId", infoEx.getUserInfo().getUserId());
						intent.putExtra("isFocus", false);
						ClubHomepageActivity.this.sendBroadcast(intent);
						// mFocusCount.setText("关注 " + (count-1));
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
		HttpUtil.get(Urls.join_team_talk, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
					return;
				}
				JSONObject data = response.optJSONObject("data");
				JSONObject product = data.optJSONObject("product");
				Intent intent = new Intent(ClubHomepageActivity.this, ChatActivity.class);
				intent.putExtra("queueId", queueId);
				intent.putExtra("chatTitle", userInfo.getNickname());
				if (product != null) {
					intent.putExtra("title", product.optString("title"));
				} else {
					intent.putExtra("title", userInfo.getNickname());
				}
				intent.putExtra("pattern", "team");
				intent.putExtra("fromQueue", true);
				intent.putExtra("isTeam", true);
				intent.putExtra("isTeam1", true);

				startActivity(intent);
				overridePendingTransition(R.anim.activity_new, R.anim.activity_out);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.setAlpha");
		myIntentFilter.addAction("com.bcinfo.clubRefreshFocus");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.bcinfo.setAlpha".equals(action)) {
				layout_product_detail_title.getBackground().setAlpha(alpha);
			} else if ("com.bcinfo.clubRefreshFocus".equals(action)) {
				String userId = intent.getStringExtra("userId");
				if (userId.equals(infoEx.getUserInfo().getUserId())) {
					if (intent.getBooleanExtra("isFocus", false) == true) {
						isFocused = true;
						mFocusTxt.setText("已关注");
					} else if (intent.getBooleanExtra("isFocus", false) == false) {
						isFocused = false;
						mFocusTxt.setText("+关注");
					}
				}
			}
		}

	};

	private void joinPrivateChat() {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("referType", "user");
		params.put("referId", userInfo.getUserId());
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
				// if (!StringUtils.isEmpty(queueId)) {
				// // 查询队列详情并跳转
				// queryPrivateChatInfo(queueId);
				Intent talkIntent = new Intent(ClubHomepageActivity.this, ChatActivity.class);
				talkIntent.putExtra("queueId", queueId);
				talkIntent.putExtra("receiveId", userInfo.getUserId());
				talkIntent.putExtra("title", userInfo.getNickname());
				talkIntent.putExtra("isTeam", false);
				talkIntent.putExtra("fromQueue", true);
				startActivity(talkIntent);
				activityAnimationOpen();
				// }

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
}
