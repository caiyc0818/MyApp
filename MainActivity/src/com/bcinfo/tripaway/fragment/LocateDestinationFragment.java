package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.DiscoverAllThemesActivity;
import com.bcinfo.tripaway.activity.LocationCountryDetailActivity;
import com.bcinfo.tripaway.activity.OrgListActivity;
import com.bcinfo.tripaway.activity.SubjectDetailActivity;
import com.bcinfo.tripaway.activity.ThemeDetailActivity;
import com.bcinfo.tripaway.activity.ThemeProductListActivity;
import com.bcinfo.tripaway.activity.TravelPeriodProductListActivity;
import com.bcinfo.tripaway.adapter.OrgGridViewAdapter;
import com.bcinfo.tripaway.adapter.ProductPushDestinationsGridAdapter;
import com.bcinfo.tripaway.adapter.ProductPushThemesGridAdapter;
import com.bcinfo.tripaway.adapter.SubjectClubPicAdapter;
import com.bcinfo.tripaway.adapter.SubjectGridAdapter;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bcinfo.tripaway.adapter.ProductPushThemesGridAdapter.ImageShowListener;

/**
 * 目的地 页面fragment
 * 
 * 显示"热门主题"和"热门目的地"
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月12日 10:20:27
 * 
 */
public class LocateDestinationFragment extends BaseFragment implements OnItemClickListener, ImageShowListener {
	private static final String TAG = "LocateDestinationFragment";

	/**
	 * 目的地 fragment 搜索框 线性布局
	 */
	// private LinearLayout discoveryLayout;

	/**
	 * 旅行时间 textview数组
	 */
	private TextView[] tripPeriodTv = new TextView[12];

	/**
	 * 热门目的地显示的GridView
	 */
	private MyGridView mLocationsGridView;

	private ProductPushDestinationsGridAdapter pLocationsAdapter;

	/**
	 * 热门目的地显示的GridView
	 */
	private MyGridView orgGridView;

	private ArrayList<PushResource> orgList = new ArrayList<PushResource>();
	private ArrayList<PushResource> orgListUser = new ArrayList<PushResource>();

	private SubjectGridAdapter mOrgGridViewAdapter;

	/**
	 * 热门主题显示的GridView
	 */
	private MyGridView mThemesGridView;

	private ProductPushThemesGridAdapter pThemesAdapter;

	/**
	 * 旅行时间-春
	 */
	private ImageView mSpringIv;

	/**
	 * 夏
	 */
	private ImageView mSummerIv;

	/**
	 * 秋
	 */
	private ImageView mAutumnIv;

	/**
	 * 冬
	 */
	private ImageView mWinterIv;

	/**
	 * 发现-全部目的地
	 */
	private LinearLayout mShowAllLocationTv;

	/**
	 * 发现-全部主题
	 */
	private LinearLayout mShowAllThemesTv;

	/**
	 * 定义搜索结果页面底部的 "筛选条件"按钮
	 */
	// private LinearLayout searchChoiceBtn;
	private TextView responsetv;

	/**
	 * 定义中间的分隔条
	 * 
	 */
	private View separateView;

	/**
	 * 定义listview底部View
	 */
	// private View footerView;

	/**
	 * 定义一个变量来存放搜索关键字
	 */
	// private String searchCode;

	/**
	 * 搜索结果数据适配器
	 * 
	 */
	// private DiscoveryProductResultAdapter resultAdapter;

	/**
	 * 定义搜索输入框
	 * 
	 */
	// private EditText searchKeyEdit;

	/**
	 * 产品目的地的集合
	 * 
	 */
	private List<PushResource> locationsList = new ArrayList<PushResource>();

	/**
	 * 推荐产品主题的集合
	 */
	private List<PushResource> topicsList = new ArrayList<PushResource>();

	/**
	 * 某一个旅行时段的产品列表集合
	 * 
	 */
	private List<PushResource> travelPeriodProductList = new ArrayList<PushResource>();

	private String pagesize = "10";

	private String pagenum = "1";

	/**
	 * 发现-更多远行合伙人
	 */
	private TextView more;

	private View loginLoading;

	private AnimationDrawable loadingAnimation;

	private ScrollView mainScroll;

	private TextView line1;

	private TextView line2;

	public TextView[] getTripPeriodTv() {
		return tripPeriodTv;
	}

	public void setTripPeriodTv(TextView[] tripPeriodTv) {
		this.tripPeriodTv = tripPeriodTv;
	}

	public GridView getmLocationsGridView() {
		return mLocationsGridView;
	}

	public void setmLocationsGridView(MyGridView mLocationsGridView) {
		this.mLocationsGridView = mLocationsGridView;
	}

	// public LinearLayout getSearchChoiceBtn()
	// {
	// return searchChoiceBtn;
	// }
	//
	// public void setSearchChoiceBtn(LinearLayout searchChoiceBtn)
	// {
	// this.searchChoiceBtn = searchChoiceBtn;
	// }

	public View getSeparateView() {
		return separateView;
	}

	public void setSeparateView(View separateView) {
		this.separateView = separateView;
	}

	// public String getSearchCode()
	// {
	// return searchCode;
	// }
	//
	// public void setSearchCode(String searchCode)
	// {
	// this.searchCode = searchCode;
	// }
	//
	// public DiscoveryProductResultAdapter getResultAdapter()
	// {
	// return resultAdapter;
	// }
	//
	// public void setResultAdapter(DiscoveryProductResultAdapter resultAdapter)
	// {
	//
	// this.resultAdapter = resultAdapter;
	// }
	//
	// public EditText getSearchKeyEdit()
	// {
	// return searchKeyEdit;
	// } 
	//
	// public void setSearchKeyEdit(EditText searchKeyEdit)
	// {
	// this.searchKeyEdit = searchKeyEdit;
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		statisticsTitle="发现";
	}

	/**
	 * 初始化 旅行时间 textview widget
	 * 
	 * @param fragmentView
	 */
	/*
	 * private void initPeriodWidgets(View fragmentView) { tripPeriodTv[0] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_January_text);
	 * tripPeriodTv[1] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_February_text);
	 * tripPeriodTv[2] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_March_text);
	 * tripPeriodTv[3] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_April_text);
	 * tripPeriodTv[4] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_May_text);
	 * tripPeriodTv[5] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_June_text);
	 * tripPeriodTv[6] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_July_text);
	 * tripPeriodTv[7] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_August_text);
	 * tripPeriodTv[8] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_September_text);
	 * tripPeriodTv[9] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_October_text);
	 * tripPeriodTv[10] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_November_text);
	 * tripPeriodTv[11] =
	 * (TextView)fragmentView.findViewById(R.id.trip_period_December_text);
	 * 
	 * }
	 */

	/*
	 * 测试 目的地资源推荐url
	 */
	private void testLocationTimeUrl() {

		HttpUtil.get(Urls.location_res_recommend_url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testLocationTimeUrl",
				// "statusCode=" + statusCode);
				LogUtil.i(TAG, "目的地", "responseString=" + response);
				getHotLocationsList(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testLocationTimeUrl",
				// "statusCode=" + statusCode);
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testLocationTimeUrl",
				// "responseString=" + responseString);

			}
		});
	}

	/*
	 * 测试 主题及时间栏目推荐url
	 */
	private void testTopicsAndTimeUrl() {
		HttpUtil.get(Urls.topics_time_recommend_url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);

				if (statusCode == 200) {
					getTopicsAndPeriodLists(response);
					// LogUtil.i(TAG,
					// "Destination_LocateDestinationFragment_testTopicsAndTimeUrl",
					// "statusCode=" + statusCode);
					LogUtil.i(TAG, "主题", "responseString=" + response);
					// responsetv.setText(response.toString());
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testTopicsAndTimeUrl",
				// "statusCode=" + statusCode);
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testTopicsAndTimeUrl",
				// "responseString=" + responseString);
				super.onFailure(statusCode, headers, responseString, throwable);

			}
		});
	}

	private void getTopicsAndPeriodLists(JSONObject response) {
		String respFlag = response.optString("msg");
		loadingAnimation.stop();
		loginLoading.setVisibility(View.GONE);
		mThemesGridView.setVisibility(View.VISIBLE);
		if ("success".equals(respFlag)) {

			JSONObject obj = response.optJSONObject("data");
			// JSONArray timeArray = obj.optJSONArray("pushTime"); // 旅行时间
			JSONArray topicArray = obj.optJSONArray("pushTopics"); // 主题
//			for (int i = 0; i < topicArray.length(); i++) {
//				JSONObject jsonTopicObj = topicArray.optJSONObject(i);
//				PushResource pushTopicResource = new PushResource();
//				pushTopicResource.setId(jsonTopicObj.optString("id"));
//				pushTopicResource.setSubTitle(jsonTopicObj.optString("subTitle"));
//				pushTopicResource.setTitleImage(jsonTopicObj.optString("titleImage"));
//				pushTopicResource.setObjectId(jsonTopicObj.optString("objectId"));
//				pushTopicResource.setObject(jsonTopicObj.optJSONObject("object"));
//				pushTopicResource.setObjectType(jsonTopicObj.optString("objectType"));
//				pushTopicResource.setResTitle(jsonTopicObj.optString("resTitle"));
//				topicsList.add(pushTopicResource);
//
//			}
			JsonUtil.getPushResources(topicArray, topicsList);
			/*
			 * if (null == timeArray) { return; }
			 * 
			 * for (int j = 0; j < timeArray.length() && j < 12; j++) { if
			 * (timeArray.optJSONObject(j).optString("objectType") == null) {
			 * break; } else if
			 * (timeArray.optJSONObject(j).optString("objectType"
			 * ).equals("product")) { continue; }
			 * tripPeriodTv[j].setTag(timeArray
			 * .optJSONObject(j).optString("objectId"));
			 * tripPeriodTv[j].setText(
			 * timeArray.optJSONObject(j).optString("resTitle")); // 设置旅行时间的月份
			 * tripPeriodTv[j].setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { TextView monthTv =
			 * (TextView)v; Intent intentForTravelPeriodProduct = new
			 * Intent(LocateDestinationFragment.this.getActivity(),
			 * TravelPeriodProductListActivity.class);
			 * intentForTravelPeriodProduct.putExtra("monthValue",
			 * monthTv.getText().toString());
			 * intentForTravelPeriodProduct.putExtra("monthId",
			 * (String)monthTv.getTag());
			 * startActivity(intentForTravelPeriodProduct);
			 * LocateDestinationFragment
			 * .this.getActivity().overridePendingTransition
			 * (R.anim.activity_new, R.anim.activity_out); } }); }
			 */

			pThemesAdapter = new ProductPushThemesGridAdapter(this.getActivity(), topicsList, this);
			mThemesGridView.setAdapter(pThemesAdapter);
			// setGridViewRealHeight(mThemesGridView);
			mThemesGridView.setOnItemClickListener(this);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

	}

	/**
	 * handler处理消息队列中的消息
	 * 
	 */
	// private Handler handler = new Handler()
	// {
	//
	// public void handleMessage(android.os.Message msg)
	// {
	// switch (msg.what)
	// {
	// case 0x1233:
	//
	// searchCode = (String)msg.obj;
	// // 清空搜索结果List集合
	//
	// resultAdapter.notifyDataSetChanged();
	//
	// // tripListView.setAdapter(resultAdapter);
	//
	// separateView.setVisibility(View.GONE);
	//
	// System.out.println(searchCode + "------");
	//
	// searchChoiceBtn.setVisibility(View.VISIBLE);
	//
	// break;
	// default:
	//
	// break;
	//
	// }
	//
	// };
	// };

	/**
	 * 加载推荐目的地的方法
	 * 
	 * @param responseString
	 *            服务端目的地接口返回的数据
	 * @return
	 */

	private void getHotLocationsList(JSONObject jsonResponse) {
		String code = jsonResponse.optString("code");
		if ("00000".equals(code)) {

			JSONArray jsonDestArray = jsonResponse.optJSONArray("data");
			for (int i = 0; i < jsonDestArray.length(); i++) {
				JSONObject jsonObj = jsonDestArray.optJSONObject(i);
				PushResource pushResource = new PushResource();
				pushResource.setId(jsonObj.optString("id"));
				pushResource.setResTitle(jsonObj.optString("resTitle"));
				pushResource.setObjectType(jsonObj.optString("objectType"));
				pushResource.setObjectId(jsonObj.optString("objectId"));
				if (jsonObj.optString("keywords").contains("，")) {
					pushResource.setKeywords(jsonObj.optString("keywords").replace("，", "·"));
				} else {
					pushResource.setKeywords(jsonObj.optString("keywords"));
				}
				pushResource.setTitleImage(jsonObj.optString("titleImage"));
				pushResource.setSubTitle(jsonObj.optString("subTitle"));
				JSONObject jsonDestObj = jsonObj.optJSONObject("object");
				if ("destination".equals(pushResource.getObjectType())) {
					if (pushResource.getObject() instanceof TripDestination) {
						TripDestination tripDestination = (TripDestination) pushResource.getObject();
						tripDestination.setDestProNum(jsonDestObj.optString("pNum"));
						tripDestination.setDestId(jsonDestObj.optString("id"));
						tripDestination.setDestSupNum(jsonDestObj.optString("sNum"));
						tripDestination.setDestLogoKey(jsonDestObj.optString("logo"));
						tripDestination.setDestName(jsonDestObj.optString("destName"));
						tripDestination.setDestName(jsonDestObj.optString("destNameEn"));
						tripDestination.setDestDescription(jsonDestObj.optString("description"));
					}

				}
				if ("user".equals(pushResource.getObjectType())) {
					UserInfo userInfo = new UserInfo();
					JSONObject userObj = jsonObj.optJSONObject("object");

					if (!StringUtils.isEmpty(userInfo.getUserId()))
						continue;
					userInfo.setGender(userObj.optString("sex"));
					userInfo.setStatus(userObj.optString("status"));
					userInfo.setAvatar(userObj.optString("avatar"));
					userInfo.setEmail(userObj.optString("email"));
					userInfo.setNickname(userObj.optString("nickName"));
					userInfo.setUserId(userObj.optString("userId"));
					userInfo.setBackground(userObj.optString("background"));
					userInfo.setBrief(userObj.optString("brief"));
					userInfo.setIntroduction(userObj.optString("introduction"));
					userInfo.setMobile(userObj.optString("mobile"));
					JSONArray professionArray = userObj.optJSONArray("profession");
					JSONObject tagObj = userObj.optJSONObject("tags");
					if (null != tagObj) {
						Tags tag = new Tags();
						JSONArray interest = tagObj.optJSONArray("interest");
						if (interest != null) {
							List<String> list = new ArrayList<>();
							for (int j = 0; j < interest.length(); j++) {
								list.add(interest.optString(j));
							}
							tag.setInterests(list);
							userInfo.setTag(tag);
						}
					}
					String profession = "";
					if (professionArray != null) {
						for (int j = 0; j < professionArray.length(); j++) {
							if (j == 0) {
								profession += professionArray.optString(j);
							} else {
								profession += "," + professionArray.optString(j);
							}
						}
					}
					userInfo.setPermission(profession);
					JSONObject orgRoleObj = userObj.optJSONObject("orgRole");
					if (orgRoleObj != null) {
						OrgRole orgRole = new OrgRole();
						orgRole.setRoleType(orgRoleObj.optString("roleType"));
						orgRole.setPermission(orgRoleObj.optString("permission"));
						orgRole.setRoleCode(orgRoleObj.optString("roleCode"));
						if (orgRole.getRoleCode().equals("driver")) {
							System.out.print(orgRole.getRoleCode().equals("driver"));
						}
						orgRole.setRoleName(orgRoleObj.optString("roleName"));
						userInfo.setOrgRole(orgRole);
					}
					pushResource.setObject(userInfo);
				}
				locationsList.add(pushResource);

			}

		}
		pLocationsAdapter = new ProductPushDestinationsGridAdapter(this.getActivity(), locationsList);
		mLocationsGridView.setAdapter(pLocationsAdapter);
		mLocationsGridView.setOnItemClickListener(this);

		// setGridViewRealHeight(mLocationsGridView);
	}

	/**
	 * 加载推荐俱乐部的方法
	 * 
	 * @param responseString
	 *            服务端俱乐部接口返回的数据
	 * @return
	 */

	private void getHotOrgsList(JSONObject jsonResponse) {
		String code = jsonResponse.optString("code");
		if ("00000".equals(code)) {
			JSONArray jsonDestArray = jsonResponse.optJSONArray("data");
			for (int i = 0; i < jsonDestArray.length(); i++) {
				PushResource pushResource = new PushResource();
				// if (i < 5) {
				JSONObject jsonObj = jsonDestArray.optJSONObject(i);

				pushResource.setId(jsonObj.optString("id"));
				pushResource.setResTitle(jsonObj.optString("resTitle"));
				pushResource.setObjectType(jsonObj.optString("objectType"));
				pushResource.setObjectId(jsonObj.optString("objectId"));
				if (jsonObj.optString("keywords").contains("，")) {
					pushResource.setKeywords(jsonObj.optString("keywords")
							.replace("，", "·"));
				} else {
					pushResource.setKeywords(jsonObj.optString("keywords"));
				}
				pushResource.setSubTitle(jsonObj.optString("subTitle"));
				pushResource.setTitleImage(jsonObj.optString("titleImage"));
				if ("link".equals(jsonObj.optString("objectType"))) {
					pushResource.setObject(jsonObj.optString("object"));

				} else {
					JSONObject jSONObject = jsonObj.optJSONObject("object");
					if (jSONObject != null && jSONObject.length() > 0) {
						if ("product".equals(jsonObj.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getProductNewInfo(jSONObject));

						} else if ("topic".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getTopicInfo(jSONObject));
						} else if ("user".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getUserInfo(jSONObject));
						} else if ("destination".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getDestinationInfo(jSONObject));

						} else if ("softtext".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getArticleInfo(jSONObject));

						} else if ("story".equals(jsonObj
								.optString("objectType"))) {
							pushResource.setObject(JsonUtil
									.getArticleInfo(jSONObject));
						} else {
							JSONArray relatedResourcesJson = jSONObject
									.optJSONArray("relatedResources");
							String objectType = pushResource.getObjectType();
							if (jSONObject.optString("cover") != null
									&& "subject".equals(objectType))
								pushResource.setTitleImage(jSONObject
										.optString("cover"));
							UserInfo userInfo = new UserInfo();
							if (relatedResourcesJson != null
									&& relatedResourcesJson.length() > 0) {
								for (int m = 0; m < relatedResourcesJson
										.length(); m++) {
									JSONObject relatedResourceJson = relatedResourcesJson
											.optJSONObject(m);
									if (relatedResourceJson
											.optString("objectType") != null
											&& !"user"
													.equals(relatedResourceJson
															.optString("objectType"))) {
										JSONObject productObj = relatedResourceJson
												.optJSONObject("object");
										if (productObj != null
												&& userInfo.getReferPics()
														.size() < 3) {
											if (productObj
													.optString("titleImg") != null)
												userInfo.getReferPics()
														.add(productObj
																.optString("titleImg"));
										}
										continue;
									}
									if (!StringUtils.isEmpty(userInfo
											.getUserId()))
										continue;
									JSONObject objectParamObj = relatedResourceJson
											.optJSONObject("objectParams");
									if (objectParamObj != null
											&& objectParamObj.length() > 0) {
										pushResource.getObjectParam().put(
												"nickname",
												objectParamObj
														.optString("nickname"));
										pushResource.getObjectParam().put(
												"tags",
												objectParamObj
														.optString("tags"));
										pushResource.getObjectParam().put(
												"avartar",
												objectParamObj
														.optString("avartar"));
									}
									JSONObject userObj = relatedResourceJson
											.optJSONObject("object");
									userInfo.setGender(userObj.optString("sex"));
									userInfo.setStatus(userObj
											.optString("status"));
									userInfo.setAvatar(userObj
											.optString("avatar"));
									userInfo.setEmail(userObj
											.optString("email"));
									userInfo.setNickname(userObj
											.optString("nickName"));
									userInfo.setUserId(userObj
											.optString("userId"));
									userInfo.setBackground(userObj
											.optString("background"));
									userInfo.setBrief(userObj
											.optString("brief"));
									userInfo.setIntroduction(userObj
											.optString("introduction"));
									userInfo.setMobile(userObj
											.optString("mobile"));
									JSONArray professionArray = userObj
											.optJSONArray("profession");
									JSONObject tagObj = userObj
											.optJSONObject("tags");
									if (null != tagObj) {
										Tags tag = new Tags();
										JSONArray interest = tagObj
												.optJSONArray("interest");
										if (interest != null) {
											List<String> list = new ArrayList<>();
											for (int j = 0; j < interest
													.length(); j++) {
												list.add(interest.optString(j));
											}
											tag.setInterests(list);
											userInfo.setTag(tag);
										}
									}
									String profession = "";
									if (professionArray != null) {
										for (int j = 0; j < professionArray
												.length(); j++) {
											if (j == 0) {
												profession += professionArray
														.optString(j);
											} else {
												profession += ","
														+ professionArray
																.optString(j);
											}
										}
									}
									userInfo.setPermission(profession);
									JSONObject orgRoleObj = userObj
											.optJSONObject("orgRole");
									if (orgRoleObj != null) {
										OrgRole orgRole = new OrgRole();
										orgRole.setRoleType(orgRoleObj
												.optString("roleType"));
										orgRole.setPermission(orgRoleObj
												.optString("permission"));
										orgRole.setRoleCode(orgRoleObj
												.optString("roleCode"));
										if (orgRole.getRoleCode().equals(
												"driver")) {
											System.out.print(orgRole
													.getRoleCode().equals(
															"driver"));
										}
										orgRole.setRoleName(orgRoleObj
												.optString("roleName"));
										userInfo.setOrgRole(orgRole);
									}
									pushResource.setObject(userInfo);
								}
							}
						}
					}
				}
				// } else {
				// if (i == 5) {
				// pushResource.setResTitle("更多");
				// orgList.add(pushResource);
				// break;
				// }
				// }
				orgList.add(pushResource);

			}
		}
		// 发现页最多显示三个
		for (int i = 0; i < orgList.size(); i++) {
			if (i < 3) {
				orgListUser.add(orgList.get(i));
			}
		}
		mOrgGridViewAdapter = new SubjectGridAdapter(this.getActivity(),
				orgListUser);
		orgGridView.setAdapter(mOrgGridViewAdapter);
		orgGridView.setOnItemClickListener(this);

		// setGridViewRealHeight(mLocationsGridView);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View discoveryFragment = (View) inflater.inflate(R.layout.discovery_default_main, container, false);

		loginLoading = (View) discoveryFragment.findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();

		mainScroll = (ScrollView) discoveryFragment.findViewById(R.id.mainScorll);

		line1 = (TextView) discoveryFragment.findViewById(R.id.line1);

		line2 = (TextView) discoveryFragment.findViewById(R.id.line2);
		// discoveryLayout =
		// (LinearLayout)discoveryFragment.findViewById(R.id.searchEditLayout);
		// discoveryLayout.setOnClickListener(onMfragmentClick);
		mLocationsGridView = (MyGridView) discoveryFragment.findViewById(R.id.discovery_locations_gridView);
		orgGridView = (MyGridView) discoveryFragment.findViewById(R.id.org_gridView);
		mThemesGridView = (MyGridView) discoveryFragment.findViewById(R.id.discovery_themes_gridView);
		mLocationsGridView.setVisibility(View.GONE);
		orgGridView.setVisibility(View.GONE);
		mThemesGridView.setVisibility(View.GONE);
		responsetv = (TextView) discoveryFragment.findViewById(R.id.response);
		more = (TextView) discoveryFragment.findViewById(R.id.more);
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), OrgListActivity.class);
//				intent.putParcelableArrayListExtra("orgList", orgList);
				startActivity(intent);
			}
		});
		// initPeriodWidgets(discoveryFragment);
		// mSpringIv =
		// (ImageView)discoveryFragment.findViewById(R.id.discovery_trip_spring_iv);
		// mSummerIv =
		// (ImageView)discoveryFragment.findViewById(R.id.discovery_trip_summer_iv);
		// mAutumnIv =
		// (ImageView)discoveryFragment.findViewById(R.id.discovery_trip_autumn_iv);
		// mWinterIv =
		// (ImageView)discoveryFragment.findViewById(R.id.discovery_trip_winter_iv);
		// ImageLoader.getInstance().displayImage("http://pic.58pic.com/58pic/12/39/07/80F58PICaRs.jpg",
		// mSpringIv,
		// AppConfig.options(R.drawable.ic_launcher));
		// ImageLoader.getInstance()
		// .displayImage("http://data1.act3.qq.com/2010-08-29/13/2dfa357fd3e6fad4a5c889eee7887878.jpg",
		// mSummerIv,
		// AppConfig.options(R.drawable.ic_launcher));
		// ImageLoader.getInstance().displayImage("http://pic16.nipic.com/20110914/7675135_081508761000_2.jpg",
		// mAutumnIv,
		// AppConfig.options(R.drawable.ic_launcher));
		// ImageLoader.getInstance()
		// .displayImage("http://www.chinapoesy.com/UploadFiles/Poesy/20110112_81b605ac-8c6c-41b7-904d-c6c0026a3501.jpg",
		// mWinterIv,
		// AppConfig.options(R.drawable.ic_launcher));
		/**
		 * 全部主题等界面修改好再放开两个更多查询和basefragment里的点击事件
		 */
		/*
		 * mShowAllLocationTv =
		 * (LinearLayout)discoveryFragment.findViewById(R.id
		 * .discovery_showAllLocations); mShowAllThemesTv =
		 * (LinearLayout)discoveryFragment
		 * .findViewById(R.id.discovery_showAllThemes);
		 * mShowAllLocationTv.setOnClickListener(onMfragmentClick);
		 * mShowAllThemesTv.setOnClickListener(onMfragmentClick);
		 */
		testTopicsAndTimeUrl();
		orgUrl();
		testLocationTimeUrl();
		// tripListView = (ListView)
		// searchFragment.findViewById(R.id.main_search_listview);
		// searchChoiceBtn = (LinearLayout)
		// searchFragment.findViewById(R.id.search_result_bottomBtn);
		// footerView = (View)inflater.inflate(R.layout.trip_product_footview,
		// null);
		// searchChoiceBtn.setOnClickListener(this);
		// tripListView.setAdapter(pAdapter);

		// 定义 搜索结果数据的适配器

		// resultAdapter = new SearchResultAdapter(resultList,
		// this.getActivity());

		// tripResultListView = (ListView)
		// searchFragment.findViewById(R.id.main_search_results_listview);
		//
		// tripResultListView.setAdapter(resultAdapter);

		return discoveryFragment;

	}

	/**
	 * 设置GridView的真实高度
	 */

	// private void setGridViewRealHeight(GridView gridView)
	// {
	//
	// ListAdapter gridAdapter = gridView.getAdapter();
	// int totalHeight = 0;
	// if (gridAdapter != null)
	// {
	// // for循环
	// for (int i = 0; i < gridAdapter.getCount(); i++)
	// {
	// View gridItem = gridAdapter.getView(i, null, gridView);
	// gridItem.measure(0, 0);
	//
	// totalHeight = totalHeight + (gridItem.getMeasuredHeight() / 2);
	//
	// }
	//
	// }
	// else
	// {
	//
	// return;
	// }
	// LayoutParams lps = gridView.getLayoutParams();
	// lps.height = totalHeight + (18 * (gridAdapter.getCount() / 2 - 1));
	//
	// gridView.setLayoutParams(lps);
	// }

	@Override
	public void onDestroy() {

		super.onDestroy();

	}

	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {

		switch (parentView.getId()) {
		case R.id.discovery_locations_gridView: // 目的地
			PushResource pushLocateResource = locationsList.get(position);
			if (pushLocateResource.getObjectType().equals("destination")) {
				String destId = pushLocateResource.getObjectId();// 目的地标识
				Intent intentForLocateCountry = new Intent(getActivity(), LocationCountryDetailActivity.class);
				intentForLocateCountry.putExtra("destId", destId);
				intentForLocateCountry.putExtra("destinationTitle", pushLocateResource.getResTitle());
				startActivity(intentForLocateCountry);
			} else if (pushLocateResource.getObjectType().equals("subject")) {
				String entityId = pushLocateResource.getObjectId();
				Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
				intent.putExtra("entityId", entityId);
				intent.putExtra("subjectTitle", pushLocateResource.getResTitle());
				getActivity().startActivity(intent);
			} else if (pushLocateResource.getObjectType().equals("user")) {
				UserInfo userInfo = (UserInfo) pushLocateResource.getObject();
				ActivityUtil.toPersonHomePage(userInfo, getActivity());
			}

			break;
		case R.id.discovery_themes_gridView: // 主题
			PushResource pushTopicResource = topicsList.get(position);
			if (position == 8 && topicsList.size() > 9) {
				Intent intent = new Intent(getActivity(), DiscoverAllThemesActivity.class);
				startActivity(intent);
			} else {
//				if (pushTopicResource.getObjectType().equals("topic")) {
//					String topicId = pushTopicResource.getObjectId();
//					Intent intentForTopicProductList = new Intent(getActivity(), ThemeDetailActivity.class);
//					intentForTopicProductList.putExtra("themeId", topicId);
//					intentForTopicProductList.putExtra("themeName", topicsList.get(position).getResTitle());
//					intentForTopicProductList.putExtra("description",
//							((JSONObject) topicsList.get(position).getObject()).optString("description"));
//
//					startActivity(intentForTopicProductList);
//				} else if (pushTopicResource.getObjectType().equals("subject")) {
//					String entityId = pushTopicResource.getObjectId();
//					Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
//					intent.putExtra("entityId", entityId);
//					getActivity().startActivity(intent);
//				}
				ActivityUtil.toDetailPage(pushTopicResource, getActivity(), false);
			}
			break;

		case R.id.org_gridView:
			// if(position==5){
			// Intent intent = new Intent(getActivity(), OrgListActivity.class);
			// startActivity(intent);
			// }else {
			// UserInfo userInfo=(UserInfo)orgList.get(position).getObject();
			// Intent intent = new Intent(getActivity(),
			// ClubFirstPageActivity.class);
			// intent.putExtra("userInfo",userInfo);
			// getActivity().startActivity(intent);
			// }
			break;
		default:
			break;
		}

		animationOpen();

	}

	/*
	 * 合伙人
	 */
	private void orgUrl() {
		RequestParams params = new RequestParams();
		params.put("pagesize", pagesize);
		params.put("pagenum", pagenum);
		params.put("mcode", "sale.find.randomsubject");
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testLocationTimeUrl",
				// "statusCode=" + statusCode);
				LogUtil.i(TAG, "俱乐部", "responseString=" + response);
				getHotOrgsList(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testLocationTimeUrl",
				// "statusCode=" + statusCode);
				// LogUtil.i(TAG,
				// "Destination_LocateDestinationFragment_testLocationTimeUrl",
				// "responseString=" + responseString);

			}
		});
	}

	@Override
	public void changView() {
		// TODO Auto-generated method stub
		if (line1.getVisibility() == View.VISIBLE)
			return;
		line1.setVisibility(View.VISIBLE);
		line2.setVisibility(View.VISIBLE);
		more.setVisibility(View.VISIBLE);
		mLocationsGridView.setVisibility(View.VISIBLE);
		orgGridView.setVisibility(View.VISIBLE);
	}

}
