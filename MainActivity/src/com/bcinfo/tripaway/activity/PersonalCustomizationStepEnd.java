package com.bcinfo.tripaway.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AreaAdapter;
import com.bcinfo.tripaway.adapter.PartnerMemberAdapter.OnDelPartnerItemListener;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.fragment.CustomizationFragment;
import com.bcinfo.tripaway.fragment.CustomizationFragment.OnDelLessoner;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.HanziToPinyin;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.SideBar;
import com.bcinfo.tripaway.view.SideBar.OnTouchingLetterChangedListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.utils.Log;

/**
 * @author
 * @date
 */
public class PersonalCustomizationStepEnd extends BaseActivity implements
		OnDelLessoner {

	private List<Customization> customizationList = new ArrayList<Customization>();
	private List<Customization> customizationListTmp = new ArrayList<Customization>();

	private static final String TAG = "PersonalCustomizationStepEnd";

	List<Fragment> allFragments;

	private ViewPager mPager;
	private TextView pageNumTextView;
	private TextView pageCountTextView;
	private TextView addTextView;
	
	private LinearLayout showLayout;

	private LinearLayout layout_back_button;
	private LinearLayout tipLayout;
	private Button nextStepButton;
	private RelativeLayout titleLayout;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.personal_customization_step_end);
		// initLocation();
		statisticsTitle="我的定制";
		initView();
		getCustomization("100", "1");

	}

	protected void initView() {
		mPager = (ViewPager) this.findViewById(R.id.viewPage);
		pageNumTextView = (TextView) this.findViewById(R.id.pageNum);
		addTextView = (TextView) this.findViewById(R.id.addTextView);
		pageCountTextView = (TextView) this.findViewById(R.id.pageCount);
		showLayout=(LinearLayout) this.findViewById(R.id.showLayout);
		layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		nextStepButton = (Button) findViewById(R.id.nextStep);
		titleLayout = (RelativeLayout) findViewById(R.id.second_title);
		titleLayout.setAlpha(1);
		titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
		nextStepButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						PersonalCustomizationStepEnd.this, PersonalCustomizationStepOne.class);
				

//				intentForMain.setAction("com.bcinfo.CustomizationFragment");
//				intentForMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
//				Intent intent = new Intent("com.bcinfo.CustomizationFragment");
//				sendBroadcast(intent);
//				finish();
			}
		});
		tipLayout = (LinearLayout) findViewById(R.id.tipLayout);
		layout_back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				activityAnimationClose();
				Intent intentForMain = new Intent(
						PersonalCustomizationStepEnd.this, MainActivity.class);
				

				intentForMain.setAction("com.bcinfo.haveLogin");
				intentForMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentForMain);
			}
		});

		addTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						PersonalCustomizationStepEnd.this, PersonalCustomizationStepOne.class);
				

//				intentForMain.setAction("com.bcinfo.CustomizationFragment");
//				intentForMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
//				Intent intent = new Intent("com.bcinfo.CustomizationFragment");
//				sendBroadcast(intent);
//				finish();
			}
		});

	}

	private void initFragmentsAndPageCount() {

		if (allFragments == null) {
			allFragments = new ArrayList<Fragment>();
		} else {
			allFragments.clear();
		}
		for (int i = 0; i < customizationList.size(); i++) {
			allFragments.add(new CustomizationFragment(
					customizationList.get(i), this));
		}
		// if(customizationList.size()>0){
		pageCountTextView.setText(Integer.toString(customizationList.size()));
		if (customizationList.size() > 0) {
			pageNumTextView.setText("1");
		} else
			pageNumTextView.setText("0");
		// }else
		// {pageCountTextView.setText(Integer.toString(customizationList.size()));}
	}

	private void initViewPager() {
		if(customizationList.size()==0){
			showLayout.setVisibility(View.GONE);
			mPager.setVisibility(View.GONE);
		tipLayout.setVisibility(View.VISIBLE);
		
		}else 
		{
			mPager.setVisibility(View.VISIBLE);
			tipLayout.setVisibility(View.GONE);
			showLayout.setVisibility(View.VISIBLE);
		
		}
		mPager.setOnPageChangeListener(mPagerListener);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
		mPager.setPageMargin(30);
		// 1.设置幕后item的缓存数目
		mPager.setOffscreenPageLimit(customizationList.size());
		// mPager.setIsCanScroll(false);
	}

	private ViewPager.SimpleOnPageChangeListener mPagerListener = new ViewPager.SimpleOnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int state) {
			super.onPageScrollStateChanged(state);
		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		};

		@Override
		public void onPageSelected(int position) {
			pageNumTextView.setText(Integer.toString(position + 1));

		}
	};

	private void getCustomization(String pagesize, String pagenum) {
		try {
			RequestParams params = new RequestParams();
			params.put("pagesize", pagesize);
			params.put("pagenum", "pagenum");
			HttpUtil.get(Urls.get_customization, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							LogUtil.d("PersonalCustomizationStepEnd",
									"testClientCustomizationUrl",
									response.toString());
							super.onSuccess(statusCode, headers, response);
							JSONArray datas = response.optJSONArray("data");
							if (datas!=null&&datas.length() > 0) {

								for (int i = 0; i < datas.length(); i++) {
									JSONObject dataJson = datas
											.optJSONObject(i);
									Customization customization = new Customization();
									customization.setAdultNum(dataJson
											.optString("adultNum"));
									customization.setArrange(dataJson
											.optString("arrange"));
									customization.setBudget(dataJson
											.optString("budget"));
									customization.setChildrenNum(dataJson
											.optString("childrenNum"));
									customization.setCreateTime(dataJson
											.optString("createTime"));
									customization.setDays(dataJson
											.optString("days"));
									customization.setDesireId(dataJson
											.optString("desireId"));
									customization.setEmail(dataJson
											.optString("email"));
									customization.setFrom(dataJson
											.optString("from"));
									customization.setMobile(dataJson
											.optString("mobile"));
									customization.setRealName(dataJson
											.optString("realName"));
									customization.setReply(dataJson
											.optString("reply"));
									customization.setStartDate(dataJson
											.optString("startDate"));
									customization.setStatus(dataJson
											.optString("status"));
									customization.setTo(dataJson
											.optString("to"));
									List<TraceLogs> traceLogs = customization
											.getTraceLogs();
									JSONArray traceLogsJson = dataJson
											.optJSONArray("traceLogs");
									for (int j = 0; j < traceLogsJson.length(); j++) {
										TraceLogs traceLog = new TraceLogs();
										JSONObject traceLogJson = traceLogsJson
												.optJSONObject(j);
										traceLog.setCreateTime(traceLogJson
												.optString("createTime"));
										traceLog.setStatus(traceLogJson
												.optString("status"));
										traceLog.setTitle(traceLogJson
												.optString("title"));
										traceLog.setDesc(traceLogJson
												.optString("description"));
										traceLogs.add(traceLog);
									}
									JSONArray productArray = dataJson.optJSONArray("recommendProducts");
									if (productArray!=null&&productArray.length() > 0) {
										for (int m = 0; m < productArray.length(); m++) {
											JSONObject product = productArray.optJSONObject(m);
											ProductNewInfo productInfo = new ProductNewInfo();
											JSONArray expPeriodArray = product
													.optJSONArray("expPeriod");
											if (expPeriodArray != null) {
												// =====行程的开始日期
												ArrayList<AvailableTime> experiodList = new ArrayList<AvailableTime>();
												for (int j = 0; j < expPeriodArray.length(); j++) {
													JSONObject experiodJson = expPeriodArray
															.optJSONObject(j);
													AvailableTime availableTime = new AvailableTime();
													availableTime.setBeginDate(experiodJson
															.optString("beginDate"));
													availableTime.setEndDate(experiodJson
															.optString("endDate"));
													experiodList.add(availableTime);
													// //////////////////////////////
													String begin_time = availableTime
															.getBeginDate();
												}
												productInfo.setExpPeriodList(experiodList);
											}
											JSONArray topics = product.optJSONArray("topics");
											if (topics != null) {
												for (int j = 0; j < topics.length(); j++) {
													productInfo.getTopics().add(
															topics.opt(j).toString());
												}
											}
											productInfo.setId(product.optString("id"));
											productInfo.setDistance(product
													.optString("distance"));
											productInfo.setTitle(product.optString("title"));
											productInfo.setPoiCount(product
													.optString("poiCount"));
											productInfo.setPrice(product.optString("price"));
											productInfo.setDays(product.optString("days"));

											productInfo.setDescription(product
													.optString("description")); // 内容
											productInfo.setPriceMax(product
													.optString("priceMax"));
											productInfo.setTitleImg(product
													.optString("titleImg")); // 标题图片
											productInfo.setMaxMember(product
													.optString("maxMember"));
											productInfo.setProductType(product
													.optString("productType"));
											productInfo.setTotal(product.optString("servTimes"));
											;
											productInfo.setServices(product
													.optString("serviceCodes"));
											try {
												productInfo.setLevel(product.optJSONObject(
														"policy").getString("type"));
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											JSONObject userJSON = product
													.optJSONObject("creater");
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
											customization.getRecommendProducts().add(productInfo);
										}
									}
									customizationList.add(customization);
									
								}
								if(customizationList.size()>0){
								customizationListTmp.addAll(customizationList);
								for(int j=customizationList.size()-1;j>=0;j--){
									customizationList.set(j, customizationListTmp.get(customizationList.size()-1-j));
								}}
							} else {

							}
							initFragmentsAndPageCount();
							initViewPager();

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {

							super.onFailure(statusCode, headers,
									responseString, throwable);
							ToastUtil.showErrorToast(
									PersonalCustomizationStepEnd.this,
									getString(R.string.register_fail) + ":"
											+ statusCode);
							throwable.printStackTrace();
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(
			getSupportFragmentManager()) {

		private int mChildCount;

		/** 仅执行一次 */
		@Override
		public Fragment getItem(int position) {
			Fragment result = allFragments.get(position);
			return result;
		}

		@Override
		public int getCount() {
			return allFragments.size();
		}

//		 @Override
//		 public void notifyDataSetChanged() {
//		 mChildCount = getCount();
//		 super.notifyDataSetChanged();
//		 }
		
		  @Override
		  public int getItemPosition(Object object) {
		   return POSITION_NONE;
		  }

	};

	@Override
	public void OnDel(CustomizationFragment fragment) {
		// TODO Auto-generated method stub
		allFragments.remove(fragment);

		fragment.onDestroy();
		mAdapter.notifyDataSetChanged();
		pageCountTextView.setText(Integer.toString(allFragments.size()));
		if(allFragments.size()==0){pageNumTextView.setText("0");
		showLayout.setVisibility(View.GONE);
		mPager.setVisibility(View.GONE);
	tipLayout.setVisibility(View.VISIBLE);
	}
		
		// mPager.setCurrentItem(allFragments.size());
	}
}
