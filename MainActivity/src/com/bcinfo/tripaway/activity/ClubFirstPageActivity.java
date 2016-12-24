package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Cats;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

public class ClubFirstPageActivity extends BaseActivity{

	private ImageView goBtn;
	
	private TextView mBuserCount;

	private TextView mGuiderCount;

	private TextView mFocusCount;

	private TextView mFansCount;
	
	private ImageView mBackgroundLy;

	private RoundImageView mTitleIm;
	
	private TextView mName;
	
	private UserInfo userInfo;
	
	private UserInfoEx infoEx = new UserInfoEx();
	
	private TextView mBrief;

	private FlowLayout mClubFootFlowLayout;

	private FlowLayout mAreaLayout;

	private FlowLayout mClubFlowLayout;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.club_firstpage);
		userInfo = getIntent().getParcelableExtra("userInfo");
		initView();
		 runnable = new Runnable()
		    {
		        
		        @Override
		        public void run()
		        {
		            // TODO Auto-generated method stub
		            Intent it = new Intent(ClubFirstPageActivity.this,ClubHomepageActivity.class);
		            it.putExtra("userInfo", userInfo);
		            startActivity(it);
		            finish();
		        }
		    };
		handler.postDelayed(runnable,  3000);
	}
	
	   Handler handler = new Handler();
	    
	    Runnable runnable ;

		private TextView starText;

		private LinearLayout mStarLayout;

		private TextView mReferTxt;
	    
	@Override
	protected void initView() {
		super.initView();
		goBtn = (ImageView) findViewById(R.id.go_btn);
		goBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.removeCallbacks(runnable);
				Intent it = new Intent(ClubFirstPageActivity.this,ClubHomepageActivity.class);
				it.putExtra("userInfo", userInfo);
				startActivity(it);
				finish();
			}
		});
		mTitleIm = (RoundImageView) findViewById(R.id.club_author_photo);
		mBuserCount = (TextView) findViewById(R.id.club_buser_count);
		mGuiderCount = (TextView) findViewById(R.id.club_guider_count);
		mFocusCount = (TextView) findViewById(R.id.club_focus_count);
		mFansCount = (TextView) findViewById(R.id.club_fans_count);
		starText = (TextView) findViewById(R.id.star_text);
		mStarLayout = (LinearLayout) findViewById(R.id.star_layout);
		mReferTxt = (TextView) findViewById(R.id.refer_content);
		
		mFansCount = (TextView) findViewById(R.id.club_fans_count);
		mFansCount = (TextView) findViewById(R.id.club_fans_count);
		mBackgroundLy = (ImageView) findViewById(R.id.page_background);
		mName = (TextView) findViewById(R.id.club_name);
		mName.setText(userInfo.getNickname());
		if(!StringUtils.isEmpty(userInfo.getBackground())){
			ImageLoader.getInstance().displayImage(Urls.imgHost+userInfo.getBackground(), mBackgroundLy,AppConfig.options(R.drawable.club_bg));
		}
		mBrief = (TextView) findViewById(R.id.brief);
		queryUserInfo(userInfo.getUserId());
	}
	
	private void queryUserInfo(String userId) {
		HttpUtil.get(Urls.personal_url + userId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONObject data = response.optJSONObject("data");
					if (data != null) {
						// 司机数 领队数 导游数 群聊人数
						infoEx.setGuideNum(data.optInt("guide", 0));
						infoEx.setDriverNum(data.optInt("driver", 0));
						infoEx.setLeaderNum(data.optInt("leader", 0));
						infoEx.setGroupChatCount(data.optInt("groupChatCount",
								0));
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
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < interest.length(); i++) {
										list.add(interest.optString(i));
									}
									tag.setInterests(list);
								}

								JSONArray sphere = tagObj
										.optJSONArray("sphere");
								if (sphere != null) {
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < sphere.length(); i++) {
										list.add(sphere.optString(i));
									}
									tag.setSpheres(list);
								}

								JSONArray footprint = tagObj
										.optJSONArray("footprint");
								if (footprint != null) {
									List<String> list = new ArrayList<String>();
									for (int i = 0; i < footprint.length(); i++) {
										list.add(footprint.optString(i));
									}
									tag.setFootprints(list);
								}

								JSONArray clubtype = tagObj
										.optJSONArray("club_type");
								if (clubtype != null) {
									List<String> list = new ArrayList<String>();
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
							user.setBackground(userObj.optString("background"));
							user.setOrgInfo(userObj.optString("orgInfo"));
							user.setBrief(userObj.optString("brief"));
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
							if(exts!=null){
							map.put("referContent",
									exts.optString("refer_content"));
							map.put("referScore", exts.optString("refer_score"));}
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

							user.setServerPolicy(userObj
									.optString("serverPolicy"));
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
	
	private void initUserInfo(UserInfoEx infoEx) {
		UserInfo info = infoEx.getUserInfo();
		if(!StringUtils.isEmpty(infoEx.getUserInfo().getAvatar())){
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + infoEx.getUserInfo().getAvatar(), mTitleIm,
					AppConfig.options(R.drawable.user_defult_photo));
		}
		mFansCount.setText("粉丝 " + info.getFansCount());
		mFocusCount.setText("关注 " + info.getFocus());
		if(0 == infoEx.getGuideNum()){
			mGuiderCount.setVisibility(View.GONE);
		}else{
			mGuiderCount.setVisibility(View.VISIBLE);
			mGuiderCount.setText("导游 " + infoEx.getGuideNum());
		}
		if(0 ==  infoEx.getDriverNum()){
			mBuserCount.setVisibility(View.GONE);
		}else{
			mBuserCount.setVisibility(View.VISIBLE);
			mBuserCount.setText("司机 " + infoEx.getDriverNum());
		}
		
		mBrief.setText(StringUtils.isEmpty(info.getBrief())?"":"TA说："+info.getBrief());
		
		
		mClubFootFlowLayout = (FlowLayout) findViewById(R.id.clubarea_flowlayout);
		//服务区域
		if(info.getTag().getFootprints() != null){
			List<String> list = info.getTag().getFootprints();
			for (int i = 0; i < list.size(); i++) {
				if(i==3)break;
				TextView tv = new TextView(this);
				tv.setPadding(DensityUtil.dip2px(this, 13), 2,DensityUtil.dip2px(this, 13), 2);
				tv.setGravity(Gravity.CENTER);
				tv.setText(list.get(i));
				tv.setTextColor(Color.parseColor("#5EB48A"));
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(params);
				params.setMargins(10, 5, 10, 5);
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.green_circle_corner_lit_bg));
				mClubFootFlowLayout.addView(tv);
			}
		}
		mAreaLayout = (FlowLayout) findViewById(R.id.area_flowlayout);
		//服务内容
		if (info.getTag().getServAreas() != null) {
			List<String> list = info.getTag().getServAreas();
			for (int i = 0; i < list.size(); i++) {
				if(i==3)break;
				TextView tv = new TextView(this);
				tv.setPadding(DensityUtil.dip2px(this, 13), 2,DensityUtil.dip2px(this, 13), 2);
				tv.setGravity(Gravity.CENTER);
				tv.setText(list.get(i));
				tv.setTextColor(Color.parseColor("#5EB48A"));
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(params);
				params.setMargins(10, 5, 10, 5);
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.green_circle_corner_lit_bg));
				mAreaLayout.addView(tv);
			}
		}
		// 服务类型
		mClubFlowLayout = (FlowLayout) 
				findViewById(R.id.clubtype_flowlayout);
		if (null != info.getTag().getClubTypes()) {
			List<String> clubtypeList = info.getTag().getClubTypes();
			for (int i = 0; i < clubtypeList.size(); i++) {
				if(i==3)break;
				TextView tv = new TextView(this);
				tv.setPadding(DensityUtil.dip2px(this, 13), 2,DensityUtil.dip2px(this, 13), 2);
				tv.setGravity(Gravity.CENTER);
				tv.setText(clubtypeList.get(i));
				tv.setTextColor(Color.parseColor("#5EB48A"));
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(params);
				params.setMargins(10, 5, 10, 0);
				tv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.green_circle_corner_lit_bg));
				mClubFlowLayout.addView(tv);
			}
		}
		HashMap<String, String> map = info.getExts();
		if(map!=null){
		if (map.containsKey("referScore")) {
			String stars = map.get("referScore");
			if (!StringUtils.isEmpty(stars)) {
				LayoutParams params = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(10, 0, 0, 0);
				if (Double.parseDouble(stars) >= 1
						&& Double.parseDouble(stars) < 2) {
					for (int i = 0; i < 1; i++) {
						ImageView img = new ImageView(this);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 2
						&& Double.parseDouble(stars) < 3) {
					for (int i = 0; i < 2; i++) {
						ImageView img = new ImageView(this);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 3
						&& Double.parseDouble(stars) < 4) {
					for (int i = 0; i < 3; i++) {
						starText.setText("三星推荐");
						ImageView img = new ImageView(this);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 4
						&& Double.parseDouble(stars) < 5) {
					for (int i = 0; i < 4; i++) {
						starText.setText("四星推荐");
						ImageView img = new ImageView(this);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				} else if (Double.parseDouble(stars) >= 5) {
					starText.setText("五星推荐");
					for (int i = 0; i < 5; i++) {
						ImageView img = new ImageView(this);
						img.setImageResource(R.drawable.red_star);
						img.setLayoutParams(params);
						mStarLayout.addView(img);
					}
				}
			}else {
				
			}
		}else {
		}
		if (map.containsKey("referContent")&&!StringUtils.isEmpty(map.get("referContent"))) {
			mReferTxt.setText(map.get("referContent"));
			((View)mReferTxt.getParent()).setVisibility(View.VISIBLE);
		}else {
			((View)mReferTxt.getParent()).setVisibility(View.GONE);
		}
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(runnable!=null){
			handler.removeCallbacks(runnable);
		}
	}
}
