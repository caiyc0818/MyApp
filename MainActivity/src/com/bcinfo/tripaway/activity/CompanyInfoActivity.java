package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.CertifaceAdapter;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wefika.flowlayout.FlowLayout;

public class CompanyInfoActivity extends BaseActivity {

	private FlowLayout mAreaLayout;

	private FlowLayout mServLayout;

	private FlowLayout mFootLayout;

	private TextView mMebCount;

	private TextView mCompanyName;

	private TextView mCompanyPass;

	private TextView mCompanyTel;

	private MyListView mListView;

	private String userId;
	
	private RelativeLayout secondTitle;
	
	private LinearLayout backBtn;
	
	private List<ImageInfo> imageList = new ArrayList<ImageInfo>();
	
	private CertifaceAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.company_info_layout);
		userId = getIntent().getStringExtra("userId");
		initView();
		queryUserInfo(userId);
	}

	@Override
	protected void initView() {
		super.initView();
		mAreaLayout = (FlowLayout) findViewById(R.id.clubarea_flowlayout);
		mServLayout = (FlowLayout) findViewById(R.id.area_flowlayout);
		mFootLayout = (FlowLayout) findViewById(R.id.clubtype_flowlayout);
		mMebCount = (TextView) findViewById(R.id.meb_count);
		mCompanyName = (TextView) findViewById(R.id.company_name);
		mCompanyPass = (TextView) findViewById(R.id.company_pass);
		mCompanyTel = (TextView) findViewById(R.id.company_tel);
		secondTitle =(RelativeLayout) findViewById(R.id.layout_product_detail_title);
		secondTitle.getBackground().setAlpha(255);
		backBtn  = (LinearLayout) findViewById(R.id.layout_back_button);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mListView = (MyListView) findViewById(R.id.identiy_list);
		adapter = new CertifaceAdapter(imageList, CompanyInfoActivity.this);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void queryUserInfo(String userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		HttpUtil.get(Urls.personal_url + userId, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if ("00000".equals(code)) {
							UserInfoEx info = new UserInfoEx();
							JSONObject data = response.optJSONObject("data");
							if (null != data) {
								JSONArray certificate = data
										.optJSONArray("certificate");
								List<ImageInfo> list = new ArrayList<ImageInfo>();
								// 证件照
								if (null != certificate) {
									for(int j=0;j<certificate.length();j++){
										JSONObject obj = certificate.optJSONObject(j);
										ImageInfo imageInfo = new ImageInfo();
										imageInfo.setUrl(obj.optString("url"));
										list.add(imageInfo);
									}
								}
								info.setImages(list);
								UserInfo userInfo = new UserInfo();
								// 会员数
								JSONObject userObj = data
										.optJSONObject("userInfo");
								if (null != userObj) {
									userInfo.setMembers(userObj
											.optString("members"));
									// tags
									JSONObject tagObj = userObj
											.optJSONObject("tags");
									Tags tags = new Tags();
									if (null != tagObj) {
										JSONArray servArray = tagObj
												.optJSONArray("serv_area");
										List<String> servList = new ArrayList<String>();
										if (servArray != null) {
											for (int i = 0; i < servArray
													.length(); i++) {
												servList.add(servArray
														.optString(i));
											}
										}
										tags.setServAreas(servList);
										JSONArray footprintArray = tagObj
												.optJSONArray("footprint");
										List<String> footprintList = new ArrayList<String>();
										if (footprintArray != null) {
											for (int i = 0; i < footprintArray
													.length(); i++) {
												footprintList
														.add(footprintArray
																.optString(i));
											}
										}
										tags.setFootprints(footprintList);
										JSONArray clubTypeArray = tagObj
												.optJSONArray("club_type");
										List<String> clubTypeList = new ArrayList<String>();
										if (clubTypeArray != null) {
											for (int i = 0; i < clubTypeArray
													.length(); i++) {
												clubTypeList.add(clubTypeArray
														.optString(i));
											}
										}
										tags.setClubTypes(clubTypeList);
									}
									userInfo.setTag(tags);

									// 企业名称
									userInfo.setRealName(userObj
											.optString("realName"));
									// 经营许可证
									userInfo.setOcc(userObj.optString("occ"));
									// 电话号码
									userInfo.setMobile(userObj
											.optString("mobile"));
								}
								info.setUserInfo(userInfo);
								setUserInfo(info);
							}
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

	private void setUserInfo(UserInfoEx infoEx) {
		if (infoEx.getUserInfo().getTag().getFootprints() != null) {
			List<String> list = infoEx.getUserInfo().getTag().getFootprints();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					TextView tv = new TextView(this);
					tv.setPadding(DensityUtil.dip2px(this, 13), 2,
							DensityUtil.dip2px(this, 13), 2);
					tv.setGravity(Gravity.CENTER);
					tv.setText(list.get(i));
					tv.setTextColor(Color.parseColor("#5EB48A"));
					FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					tv.setLayoutParams(params);
					params.setMargins(10, 5, 10, 0);
					tv.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.green_circle_corner_lit_bg));
					mFootLayout.addView(tv);
				}
			}
		}

		if (infoEx.getUserInfo().getTag().getClubTypes() != null) {
			List<String> list = infoEx.getUserInfo().getTag().getClubTypes();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					TextView tv = new TextView(this);
					tv.setPadding(DensityUtil.dip2px(this, 13), 2,
							DensityUtil.dip2px(this, 13), 2);
					tv.setGravity(Gravity.CENTER);
					tv.setText(list.get(i));
					tv.setTextColor(Color.parseColor("#5EB48A"));
					FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					tv.setLayoutParams(params);
					params.setMargins(10, 5, 10, 0);
					tv.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.green_circle_corner_lit_bg));
					mServLayout.addView(tv);
				}
			}
		}
		
		if (infoEx.getUserInfo().getTag().getFootprints() != null) {
			List<String> list = infoEx.getUserInfo().getTag().getFootprints();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					TextView tv = new TextView(this);
					tv.setPadding(DensityUtil.dip2px(this, 13), 2,
							DensityUtil.dip2px(this, 13), 2);
					tv.setGravity(Gravity.CENTER);
					tv.setText(list.get(i));
					tv.setTextColor(Color.parseColor("#5EB48A"));
					FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					tv.setLayoutParams(params);
					params.setMargins(10, 5, 10, 0);
					tv.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.green_circle_corner_lit_bg));
					mAreaLayout.addView(tv);
				}
			}
		}
		
		if(StringUtils.isEmpty(infoEx.getUserInfo().getMembers())||infoEx.getUserInfo().getMembers().equals("0"))
			((View)mMebCount.getParent()).setVisibility(View.GONE);
		//会员数
		else
		mMebCount.setText(infoEx.getUserInfo().getMembers());
		
		//企业名称
		mCompanyName.setText(infoEx.getUserInfo().getRealName());
		
		//许可证
		
		if(infoEx.getUserInfo().getOcc().equals("null")){
			mCompanyPass.setText("");
		}else{
			mCompanyPass.setText(infoEx.getUserInfo().getOcc());
		}
		
		
		//电话
		mCompanyTel.setText(StringUtils.isEmpty(infoEx.getUserInfo().getMobile())?"":infoEx.getUserInfo().getMobile());
	
		imageList.clear();
		if(infoEx.getImages() != null&&infoEx.getImages().size()>0){
			imageList.addAll(infoEx.getImages());
			adapter.notifyDataSetChanged();
		}
	}
}
