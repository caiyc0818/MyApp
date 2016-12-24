package com.bcinfo.tripaway.activity;

import im.yixin.sdk.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.LocationCountryAdapter;
import com.bcinfo.tripaway.adapter.LocationStoryAdapter;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductLocationsGridAdapter;
import com.bcinfo.tripaway.adapter.TravelAchiveAdapter;
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.OrgInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.ScrollView.LocationCountryDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.LocationCountryDetailScrollView.onTurnListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 目的地-国家详情
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月16日 9:40:02
 */
public class LocationCountryDetailActivity extends BaseActivity implements OnClickListener, PersonalScrollViewListener

{
	List<ArticleInfo> articleInfos = new ArrayList<>();

	private GridView travelAchiveMentGridView;// 旅游达人 列表

	private TravelAchiveAdapter achiveAdapter;// 旅游达人 adapter

	private ProductLocationsGridAdapter locationsGridAdapter;// 热门城市 adapter

	private LocationCountryDetailScrollView locationCountryDetailScrollView;

	private LocationCountryAdapter productAdapter;// 旅游产品 adapter

	private GridView locationHotCityGridView;// 热门城市 列表

	private ListView productListView;// 旅游产品 列表

	private List<UserInfo> achiveList = new ArrayList<UserInfo>();

	private TextView countryNameTv;// 国家 中文名

	private TextView country_name;

	private TextView countryNameEnTv;// 国家 英文名

	private List<TripDestination> locationList = new ArrayList<TripDestination>(); // 热门城市

	private List<ProductNewInfo> productList = new ArrayList<ProductNewInfo>();// 旅游产品集合

	private ImageView locationCountryHeadIv;

	private ImageLoader imgLoader;

	private String destId;

	private TripDestination tripDestination = new TripDestination();

	private ArticleInfo articleInfo = new ArticleInfo();

	private TextView location_country_desc;

	// private TextView location_keyword_tv;

	private TextView showmore_orpackup;

	private TextView responseTv;

	private int textLineCount;

	private LinearLayout show_more_layout;

	private LinearLayout layout_location_country_detail_hotcity_container;

	private LinearLayout travel_product_container;

	private LinearLayout country_name_layout;

	private LinearLayout layout_location_country_detail_achivement_container;

	private RelativeLayout warm_tip;

	private RelativeLayout country_detail_layout_title;

	private Drawable downDrawable;

	private Drawable upDrawable;

	// 载入图片动画

	private LinearLayout layout_location_country_detail_container;

	private View loginLoading;

	private AnimationDrawable loadingAnimation;

	private static final int SHRINK_UP_STATE = 1;// 收起状态

	private static final int SPREAD_STATE = 2;// 展开状态

	private static int mState = SHRINK_UP_STATE;// 默认收起状态

	private LinearLayout travel_art_container;

	private ListView location_country_detail_trip_art_listview;
	private LocationStoryAdapter lacationStoryAdapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(getIntent().getStringExtra("destinationTitle")!=null){
        	statisticsTitle=getIntent().getStringExtra("destinationTitle");
        }
		setContentView(R.layout.location_country_detail_activity);
		
		layout_location_country_detail_container = (LinearLayout) findViewById(
				R.id.layout_location_country_detail_container);
		layout_location_country_detail_container = (LinearLayout) findViewById(
				R.id.layout_location_country_detail_container);
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();

		travel_art_container = (LinearLayout) findViewById(R.id.travel_art_container);
		location_country_detail_trip_art_listview = (ListView) findViewById(
				R.id.location_country_detail_trip_art_listview);

		destId = getIntent().getStringExtra("destId"); // 目的地标识id
		countryNameTv = (TextView) findViewById(R.id.location_country_detail_country_tv);
		countryNameEnTv = (TextView) findViewById(R.id.location_city_detail_city_enUSName_tv);
		country_name = (TextView) findViewById(R.id.country_name);
		// location_keyword_tv=
		// (TextView)findViewById(R.id.location_keyword_tv);
		country_name_layout = (LinearLayout) findViewById(R.id.country_name_layout);
		// responseTv= (TextView)findViewById(R.id.responsetv);
		location_country_desc = (TextView) findViewById(R.id.location_country_desc);
		layout_location_country_detail_hotcity_container = (LinearLayout) findViewById(
				R.id.layout_location_country_detail_hotcity_container);
		travel_product_container = (LinearLayout) findViewById(R.id.travel_product_container);
		layout_location_country_detail_achivement_container = (LinearLayout) findViewById(
				R.id.layout_location_country_detail_achivement_container);
		country_detail_layout_title = (RelativeLayout) findViewById(R.id.country_detail_layout_title);
		country_detail_layout_title.getBackground().setAlpha(0);
		warm_tip = (RelativeLayout) findViewById(R.id.warm_tip);
		country_name.setAlpha(0);
		testDestinationDetailUrl(destId);
		testProductDetailUrl(destId);
		warm_tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationCountryDetailActivity.this, LocationCountryTipActivity.class);
				intent.putExtra("countryName", tripDestination.getDestName());
				intent.putExtra("destinationId", destId);
				startActivity(intent);
				activityAnimationOpen();
			}
		});
		textLineCount = location_country_desc.getLineCount();
		show_more_layout = (LinearLayout) findViewById(R.id.show_more_layout);
		showmore_orpackup = (TextView) findViewById(R.id.showmore_orpackup);
		ViewTreeObserver viewTreeObserver = location_country_desc.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			private boolean isCheck = false;

			@Override
			public boolean onPreDraw() {
				int maxLines = location_country_desc.getLineCount();
				// if ((Boolean) description.getTag()) {
				// return true;
				// }
				if (isCheck)
					return true;
				if (maxLines <= 5) {
					showmore_orpackup.setVisibility(View.GONE);
				} else {
					location_country_desc.setMaxLines(5);
					showmore_orpackup.setVisibility(View.VISIBLE);
					mState = SHRINK_UP_STATE;
					isCheck = true;
				}

				return true;
			}
		});
		// show_more_layout.setVisibility(View.VISIBLE);
		show_more_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mState == SHRINK_UP_STATE) {
					location_country_desc.setMaxLines(Integer.MAX_VALUE);
					location_country_desc.requestLayout();
					showmore_orpackup.setText("收起更多");
					mState = SPREAD_STATE;
					showmore_orpackup.setCompoundDrawables(null, null, upDrawable, null);
				} else if (mState == SPREAD_STATE) {
					location_country_desc.setMaxLines(5);
					location_country_desc.requestLayout();
					showmore_orpackup.setText("显示更多");
					mState = SHRINK_UP_STATE;
					showmore_orpackup.setCompoundDrawables(null, null, downDrawable, null);
				}

			}
		});

		// initProductList();
		locationCountryDetailScrollView = (LocationCountryDetailScrollView) findViewById(
				R.id.location_country_detail_scrollview_container);
		((LinearLayout) findViewById(R.id.layout_back_button)).setOnClickListener(mOnClickListener);
		// ((ImageView)findViewById(R.id.layout_location_country_detail_container)).setOnClickListener(this);
		travelAchiveMentGridView = (GridView) findViewById(R.id.location_country_detail_travel_achivement_gridview);
		locationHotCityGridView = (GridView) findViewById(R.id.location_country_detail_hotcity_gridview);
		productListView = (ListView) findViewById(R.id.location_country_detail_trip_product_listview);
		
		
		mTravelListViewFooter = findViewById(R.id.xlistview_footer_content);
		locationCountryHeadIv = (ImageView) findViewById(R.id.location_country_detail_iv);

		locationCountryDetailScrollView.setImageView(locationCountryHeadIv);
		locationCountryDetailScrollView.setScrollListener(this);
		locationCountryDetailScrollView.setTurnListener(new onTurnListener() {
			@Override
			public void onTurn(boolean isShowing) {
			}
		});
		downDrawable = getResources().getDrawable(R.drawable.subject_down_arrow);
		downDrawable.setBounds(0, 0, downDrawable.getMinimumWidth(), downDrawable.getMinimumHeight());
		upDrawable = getResources().getDrawable(R.drawable.subject_up_arrow);
		upDrawable.setBounds(0, 0, upDrawable.getMinimumWidth(), upDrawable.getMinimumHeight());
		showmore_orpackup.setCompoundDrawables(null, null, downDrawable, null);
		showmore_orpackup.setCompoundDrawablePadding(5);
		productAdapter = new LocationCountryAdapter(this, productList);
		productListView.setAdapter(productAdapter);
		mTravelListViewFooter.setVisibility(View.GONE);
		productListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				LogUtil.i("LocationCountryDetailActivity", "onItemClick", "position=" + position);
				Intent intent = null;
				ProductNewInfo productNewInfo = productList.get(position);
				if (productNewInfo.getProductType().equals("single")) {
					if (productNewInfo.getServices().equals("traffic")) {
						intent = new Intent(LocationCountryDetailActivity.this, CarProductDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					} else if (productNewInfo.getServices().equals("stay")) {
						intent = new Intent(LocationCountryDetailActivity.this, HouseProductDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					} else {
						intent = new Intent(LocationCountryDetailActivity.this, GrouponProductNewDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					}
				} else if (productNewInfo.getProductType().equals("base")
						|| productNewInfo.getProductType().equals("customization")) {
					intent = new Intent(LocationCountryDetailActivity.this, GrouponProductNewDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				} else if (productNewInfo.getProductType().equals("team")) {
					intent = new Intent(LocationCountryDetailActivity.this, GrouponProductNewDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				}
				intent.putExtra("productTitle", productNewInfo.getTitle());
				LocationCountryDetailActivity.this.startActivity(intent);
				activityAnimationOpen();
				// Intent intentForProductDetail =
				// new Intent(LocationCountryDetailActivity.this,
				// ProductDetailNewActivity.class);
				// intentForProductDetail.putExtra("productId",
				// (String)productList.get(position).getId());
				// startActivity(intentForProductDetail);
				// LocationCountryDetailActivity.this.activityAnimationOpen();
			}

		});
	}

	/*
	 * test 目的地 -国家详情url
	 */
	private void testDestinationDetailUrl(String destId) {
		HttpUtil.get(Urls.destinations_detail_url + destId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {

					// LogUtil.i(
					// "",
					// "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
					// "statusCode=" + statusCode);
					LogUtil.i("", "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
							"responseString=" + response.toString());
					// responseTv.setText(response.toString());
					getLocationCountryDetail(response);
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// LogUtil.i(
				// "",
				// "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
				// "statusCode=" + statusCode);
				// LogUtil.i(
				// "",
				// "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
				// "responseString=" + responseString);
				super.onFailure(statusCode, headers, responseString, throwable);

			}
		});
	}

	/**
	 * 获取目的地-国家 详情参数
	 */
	private void getLocationCountryDetail(JSONObject response) {
		String code = response.optString("code");

		if ("00000".equals(code)) {
			JSONObject dataObj = response.optJSONObject("data");

			JSONObject story = dataObj.optJSONObject("story");
			if (story != null) {
				articleInfo.setAbstracts(story.optString("abstracts"));
				articleInfo.setContent(story.optString("content"));
				articleInfo.setArticleId(story.optString("articleId"));
				articleInfo.setCover(story.optString("cover"));
				articleInfo.setTitle(story.optString("title"));
				articleInfos.addAll(articleInfos);
			}

			JSONObject destinationObj = dataObj.optJSONObject("destination");
			if (destinationObj != null) {
				tripDestination.setDestId(destinationObj.optString("id"));
				tripDestination.setDestName(destinationObj.optString("destName"));

				// if(!StringUtils.isEmpty(destinationObj.optString("keywords"))){
				// String keywords=destinationObj.optString("keywords");
				// if(keywords.contains(",")){
				// keywords=keywords.replace(",","·" );
				//
				// }else if(keywords.contains("，")){
				// keywords=keywords.replace("，","·" );
				// }
				// location_keyword_tv.setText(keywords);
				// }
				tripDestination.setDestNameEn(destinationObj.optString("destNameEn"));
				tripDestination.setDestDescription(destinationObj.optString("description"));
				tripDestination.setDestLogoKey(destinationObj.optString("logo"));
				countryNameTv.setText(tripDestination.getDestName());
				country_name.setText(tripDestination.getDestName());
				countryNameEnTv.setText(tripDestination.getDestNameEn());
				if (tripDestination.getDestLogoKey() != null) {
					ImageLoader.getInstance().displayImage(Urls.imgHost + tripDestination.getDestLogoKey(),
							locationCountryHeadIv, AppConfig.options(R.drawable.ic_launcher));
				}
				location_country_desc.setText(tripDestination.getDestDescription());

			}
			// 隐藏当地旅游达人
			// JSONArray servicerTravelArray = dataObj.optJSONArray("user"); //
			// 当地旅游达人
			// if (servicerTravelArray != null && servicerTravelArray.length()
			// != 0) {
			// //
			// layout_location_country_detail_achivement_container.setVisibility(View.VISIBLE);
			// for (int i = 0; i < servicerTravelArray.length(); i++) {
			// JSONObject userObj = servicerTravelArray.optJSONObject(i);
			// UserInfo userInfo = new UserInfo();
			// userInfo.setUserId(userObj.optString("userId"));
			// userInfo.setNickname(userObj.optString("nickName"));
			// userInfo.setAvatar(userObj.optString("avatar"));
			// userInfo.setIntroduction(userObj.optString("introduction"));
			// userInfo.setMobile(userObj.optString("mobile"));
			// userInfo.setEmail(userObj.optString("email"));
			// userInfo.setStatus(userObj.optString("status"));
			// userInfo.setGender(userObj.optString("sex"));
			// JSONArray professionArray = userObj.optJSONArray("profession");
			// String profession = "";
			// if (professionArray != null) {
			// for (int j = 0; j < professionArray.length(); j++) {
			// if (j == 0) {
			// profession += professionArray.optString(j);
			// } else {
			// profession += "," + professionArray.optString(j);
			// }
			// }
			// }
			// userInfo.setPermission(profession);
			// JSONObject orgRoleObj = userObj.optJSONObject("orgRole");
			// if (orgRoleObj != null) {
			// OrgRole orgRole = new OrgRole();
			// orgRole.setRoleType(orgRoleObj.optString("roleType"));
			// orgRole.setPermission(orgRoleObj.optString("permission"));
			// orgRole.setRoleCode(orgRoleObj.optString("roleCode"));
			// if (orgRole.getRoleCode().equals("driver")) {
			// System.out.print(orgRole.getRoleCode().equals("driver"));
			// }
			// orgRole.setRoleName(orgRoleObj.optString("roleName"));
			// userInfo.setOrgRole(orgRole);
			// }
			// JSONObject tagsArray = userObj.optJSONObject("tags");
			// if (tagsArray != null) {
			// Tags tags = new Tags();
			// JSONArray interest = tagsArray.optJSONArray("interest");
			// if (interest != null) {
			// for (int j = 0; j < interest.length(); j++) {
			// tags.getInterests().add(interest.optString(j));
			// }
			// }
			// JSONArray sphere = tagsArray.optJSONArray("sphere");
			// if (sphere != null) {
			// for (int j = 0; j < sphere.length(); j++) {
			// tags.getSpheres().add(sphere.optString(j));
			// }
			// }
			// JSONArray footprint = tagsArray.optJSONArray("footprint");
			// if (footprint != null) {
			// for (int j = 0; j < footprint.length(); j++) {
			// tags.getFootprints().add(footprint.optString(j));
			// }
			// }
			// JSONArray club_type = tagsArray.optJSONArray("club_type");
			// if (club_type != null) {
			// for (int j = 0; j < club_type.length(); j++) {
			// tags.getClubTypes().add(club_type.optString(j));
			// }
			// }
			// JSONArray serv_area = tagsArray.optJSONArray("serv_area");
			// if (serv_area != null) {
			// for (int j = 0; j < serv_area.length(); j++) {
			// tags.getServAreas().add(serv_area.optString(j));
			// }
			// }
			// userInfo.setTag(tags);
			// }
			// achiveList.add(userInfo);
			// }
			// achiveAdapter = new TravelAchiveAdapter(this, achiveList);
			// travelAchiveMentGridView.setAdapter(achiveAdapter);
			// travelAchiveMentGridView.setOnItemClickListener(new
			// OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> adapterView, View view,
			// int position, long id) {
			//
			// // Intent intentForPersonal =
			// // new Intent(LocationCountryDetailActivity.this,
			// // PersonalInfo.class);
			// // intentForPersonal.putExtra("userId",
			// // achiveList.get(position).getUserId());
			// //
			// // startActivity(intentForPersonal);
			// // activityAnimationOpen();
			//
			// UserInfo userInfo = achiveList.get(position);
			// ActivityUtil.toPersonHomePage(userInfo,
			// LocationCountryDetailActivity.this);
			// }
			//
			// });
			// setInitialHeight(travelAchiveMentGridView);
			// } else {
			// layout_location_country_detail_achivement_container.setVisibility(View.GONE);
			// }
			// 隐藏热门城市
			// JSONArray hotCityArray = dataObj.optJSONArray("hotDestination");
			// // 热门城市
			// if (hotCityArray != null && hotCityArray.length() != 0) {
			//
			//// layout_location_country_detail_hotcity_container.setVisibility(View.VISIBLE);
			// for (int k = 0; k < hotCityArray.length(); k++) {
			// JSONObject hotDestinationObj = hotCityArray.optJSONObject(k);
			// TripDestination tripHotDestination = new TripDestination();
			// tripHotDestination.setDestProNum(hotDestinationObj.optString("pNum"));
			// tripHotDestination.setDestId(hotDestinationObj.optString("id"));
			// tripHotDestination.setDestSupNum(hotDestinationObj.optString("sNum"));
			// tripHotDestination.setDestLogoKey(hotDestinationObj.optString("logo"));
			// tripHotDestination.setDestName(hotDestinationObj.optString("destName"));
			// tripHotDestination.setDestNameEn(hotDestinationObj.optString("destNameEn"));
			// tripHotDestination.setDestDescription(hotDestinationObj.optString("description"));
			//
			// locationList.add(tripHotDestination);
			// }
			// locationsGridAdapter = new ProductLocationsGridAdapter(this,
			// locationList);
			// locationHotCityGridView.setAdapter(locationsGridAdapter);
			// locationHotCityGridView.setOnItemClickListener(new
			// OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> adapterView, View
			// convertView, int position, long id) {
			// Intent intentForPopular = new
			// Intent(LocationCountryDetailActivity.this,
			// LocationCountryDetailActivity.class);
			// intentForPopular.putExtra("destId",
			// locationList.get(position).getDestId());
			// startActivity(intentForPopular);
			// activityAnimationOpen();
			// }
			//
			// });
			// setInitialHeight2(locationHotCityGridView);
			// } else {
			// layout_location_country_detail_hotcity_container.setVisibility(View.GONE);
			// }
			// JSONArray productArray = dataObj.optJSONArray("product");
			// if (productArray != null && productArray.length() != 0)
			// {
			// travel_product_container.setVisibility(View.VISIBLE);
			// for (int i = 0; i < productArray.length(); i++)
			// {
			// JSONObject productJson = productArray.optJSONObject(i);
			// ProductNewInfo productNewInfo = new ProductNewInfo();
			// productNewInfo.setId(productJson.optString("id"));
			// productNewInfo.setExpStart(productJson.optString("expStart"));
			// productNewInfo.setExpend(productJson.optString("expend"));
			// productNewInfo.setProductType(productJson.optString("productType"));
			// productNewInfo.setDistance(productJson.optString("distance"));
			// productNewInfo.setTitle(productJson.optString("title"));
			// productNewInfo.setPoiCount(productJson.optString("poiCount"));
			// productNewInfo.setPrice(productJson.optString("price"));
			// productNewInfo.setDays(productJson.optString("days"));
			// productNewInfo.setDescription(productJson.optString("description"));
			// productNewInfo.setPriceMax(productJson.optString("priceMax"));
			// productNewInfo.setTitleImg(productJson.optString("titleImg"));
			// productNewInfo.setMaxMember(productJson.optString("maxMember"));
			// productNewInfo.setServices(productJson.optString("serviceCodes"));
			// productNewInfo.setQuickPayment(productJson.optString("quickPayment"));
			// ArrayList<String> topics = new ArrayList<String>();
			// JSONArray topicJsonArray = productJson.optJSONArray("topics");
			// if (topicJsonArray != null && topicJsonArray.length() > 0)
			// {
			// for (int j = 0; j < topicJsonArray.length(); j++)
			// {
			// LogUtil.i("LocationCityDetailActivity", "topicJsonArray",
			// topicJsonArray.optString(j));
			// topics.add(topicJsonArray.optString(j));
			// }
			// productNewInfo.setTopics(topics);
			// }
			// productList.add(productNewInfo);
			// }
			//
			// productAdapter = new LocationCountryAdapter(this, productList);
			// productListView.setAdapter(productAdapter);
			// productListView.addFooterView(mTravelListViewFooter);
			// mTravelListViewFooter.setVisibility(View.GONE);
			// productListView.setOnItemClickListener(new OnItemClickListener()
			// {
			//
			// @Override
			// public void onItemClick(AdapterView<?> adapterView, View view,
			// int position, long id)
			// {
			// LogUtil.i("LocationCountryDetailActivity", "onItemClick",
			// "position=" + position);
			// Intent intent = null;
			// ProductNewInfo productNewInfo = productList.get(position);
			// if (productNewInfo.getProductType().equals("single"))
			// {
			// if (productNewInfo.getServices().equals("traffic"))
			// {
			// intent = new Intent(LocationCountryDetailActivity.this,
			// CarProductDetailActivity.class);
			// intent.putExtra("productId", productNewInfo.getId());
			// }
			// else if (productNewInfo.getServices().equals("stay"))
			// {
			// intent =
			// new Intent(LocationCountryDetailActivity.this,
			// HouseProductDetailActivity.class);
			// intent.putExtra("productId", productNewInfo.getId());
			// }
			// else
			// {
			// intent = new Intent(LocationCountryDetailActivity.this,
			// GrouponProductNewDetailActivity.class);
			// intent.putExtra("productId", productNewInfo.getId());
			// }
			// }
			// else if (productNewInfo.getProductType().equals("base")
			// || productNewInfo.getProductType().equals("customization"))
			// {
			// intent = new Intent(LocationCountryDetailActivity.this,
			// GrouponProductNewDetailActivity.class);
			// intent.putExtra("productId", productNewInfo.getId());
			// }
			// else if (productNewInfo.getProductType().equals("team"))
			// {
			// intent =
			// new Intent(LocationCountryDetailActivity.this,
			// GrouponProductNewDetailActivity.class);
			// intent.putExtra("productId", productNewInfo.getId());
			// }
			// LocationCountryDetailActivity.this.startActivity(intent);
			// activityAnimationOpen();
			// // Intent intentForProductDetail =
			// // new Intent(LocationCountryDetailActivity.this,
			// ProductDetailNewActivity.class);
			// // intentForProductDetail.putExtra("productId",
			// (String)productList.get(position).getId());
			// // startActivity(intentForProductDetail);
			// // LocationCountryDetailActivity.this.activityAnimationOpen();
			// }
			//
			// });
			// setProductViewInitialHeight(productListView);
			// }else{
			// travel_product_container.setVisibility(View.GONE);
			// }

		}
	}

	/**
	 * 设置 旅游达人 gridView真实的高度
	 */
	private void setInitialHeight(GridView gridView) {
		ListAdapter listAdapter = gridView.getAdapter();
		int count = listAdapter.getCount();// item的总数
		int totalHeight;
		int rowCount = 0;// 行数
		if (count % 5 != 0) {
			rowCount = (count / 5) + 1;
		} else {
			rowCount = count / 5;
		}
		View viewItem = listAdapter.getView(0, null, gridView);
		viewItem.measure(0, 0);
		totalHeight = viewItem.getMeasuredHeight() * rowCount + 30 * rowCount;
		gridView.getLayoutParams().height = totalHeight;
	}

	/**
	 * 设置 热门城市 gridView真实的高度
	 */
	private void setInitialHeight2(GridView gridView) {
		ListAdapter listAdapter = gridView.getAdapter();
		int count = listAdapter.getCount();// item的总数
		int totalHeight;
		int rowCount = 0;// 行数
		if (count % 2 != 0) {
			rowCount = (count / 2) + 1;
		} else {
			rowCount = count / 2;
		}
		View viewItem = listAdapter.getView(0, null, gridView);
		viewItem.measure(0, 0);
		totalHeight = viewItem.getMeasuredHeight() * rowCount + 30 * rowCount;
		gridView.getLayoutParams().height = totalHeight;
	}

	/**
	 * 设置产品 adapterView真实的高度
	 */
	private void setProductViewInitialHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		int count = listAdapter.getCount();// item的总数
		int totalHeight = 0;
		// int rowCount = 0;// 行数
		for (int i = 0; i < count; i++) {
			View viewItem = listAdapter.getView(0, null, listView);
			viewItem.measure(0, 0);
			// totalHeight = viewItem.getMeasuredHeight() * rowCount;
			totalHeight = totalHeight + viewItem.getMeasuredHeight();
		}
		totalHeight = totalHeight + listView.getDividerHeight() * (count - 1);
		listView.getLayoutParams().height = totalHeight;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.titlebar_right_tip: // Intent intent = new Intent(this,
		 * TipsDetailActivity.class); // intent.putExtra("objectId", destId); //
		 * intent.putExtra("objectType", "destination"); //
		 * startActivity(intent); // activityAnimationOpen(); break;
		 */

		default:
			break;
		}

	}

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY) {
		// TODO Auto-generated method stub
		if (view != null && view == locationCountryDetailScrollView) {

			int alpha = positionY / 2;
			if (positionY < 50 || positionY == 50) {
				country_name_layout.setAlpha(1f);
			} else {
				country_name_layout.setAlpha(0);
			}
			if (positionY > 220) {
				alpha = 255;
			}
			country_detail_layout_title.getBackground().setAlpha(alpha);
			country_name.setAlpha(alpha / 255f);
			View view1 = locationCountryDetailScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = locationCountryDetailScrollView.getScrollY();
			int scollHeight = locationCountryDetailScrollView.getHeight();
			if (height == scollY + scollHeight) {
				// 底部
				testProductDetailUrl(destId);
			}
		} else {
			return;
		}
	}

	/*
	 * test 产品详情url
	 */
	private void testProductDetailUrl(String destId) {

		if (!isNeedLoad)
			return;
		if (isLoadmore)
			return;
		isLoadmore = true;
		RequestParams params = new RequestParams();
		params.put("pagesize", pageSize);
		params.put("pagenum", pageNum);
		params.put("destId", destId);
		HttpUtil.get(Urls.get_location_product, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					// LogUtil.i(
					// "",
					// "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
					// "statusCode=" + statusCode);
					LogUtil.i("", "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
							"responseString=" + response.toString());
					// responseTv.setText(response.toString());
					String code = response.optString("code");

					if ("00000".equals(code)) {
						loadingAnimation.stop();
						loginLoading.setVisibility(View.GONE);
						layout_location_country_detail_container.setVisibility(View.VISIBLE);
						List<ProductNewInfo> list = new ArrayList<ProductNewInfo>();
						JSONArray productArray = response.optJSONArray("data");
						if (productArray != null && productArray.length() != 0) {
							travel_product_container.setVisibility(View.VISIBLE);
							((LinearLayout) productListView.getParent()).getChildAt(0).setVisibility(View.VISIBLE);
							for (int i = 0; i < productArray.length(); i++) {
								JSONObject productJson = productArray.optJSONObject(i);
								ProductNewInfo productNewInfo = new ProductNewInfo();
								if (productJson.optJSONObject("exts") != null) {
									HashMap<String, String> exts = new HashMap<String, String>();
									if (!StringUtils
											.isEmpty(productJson.optJSONObject("exts").optString("refer_tags"))) {
										exts.put("refer_tags",
												productJson.optJSONObject("exts").optString("refer_tags"));
									}
									if (!StringUtils
											.isEmpty(productJson.optJSONObject("exts").optString("big_refer_tags"))) {
										exts.put("big_refer_tags",
												productJson.optJSONObject("exts").optString("big_refer_tags"));
									}
									if (exts.size() > 0) {
										productNewInfo.setExts(exts);
									}
								}
								productNewInfo.setOriginalPrice(productJson.optString("originalPrice"));
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
								productNewInfo.setQuickPayment(productJson.optString("quickPayment"));
								productNewInfo.setPv(productJson.optString("pv"));
								ArrayList<String> topics = new ArrayList<String>();
								JSONArray topicJsonArray = productJson.optJSONArray("topics");
								if (topicJsonArray != null && topicJsonArray.length() > 0) {
									for (int j = 0; j < topicJsonArray.length(); j++) {
										LogUtil.i("LocationCityDetailActivity", "topicJsonArray",
												topicJsonArray.optString(j));
										topics.add(topicJsonArray.optString(j));
									}
									productNewInfo.setTopics(topics);
								}
								list.add(productNewInfo);
							}

							notifySetChanged(list);
							// setProductViewInitialHeight(productListView);
						} else {
							// travel_product_container.setVisibility(View.GONE);
						}
					}
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// LogUtil.i(
				// "",
				// "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
				// "statusCode=" + statusCode);
				// LogUtil.i(
				// "",
				// "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
				// "responseString=" + responseString);
				isLoadmore = false;
				super.onFailure(statusCode, headers, responseString, throwable);

			}
		});
	}

	private void notifySetChanged(List<ProductNewInfo> list) {
		 LayoutParams ls=	productListView.getLayoutParams(); 
		 int h=DensityUtil.dip2px(this, 150+0.5f);
		 ls.height=(list.size()+productList.size())*h;
		 productListView.setLayoutParams(ls);
		productList.addAll(list);
	
		productAdapter.notifyDataSetChanged();
		int size = productList.size() % pageSize;
		if (size == 0 && list.size() != 0) {
			mTravelListViewFooter.setVisibility(View.VISIBLE);
			isNeedLoad = true;
			++pageNum;
		} else {
			// 相关文章
			if (articleInfos.size() > 0) {
				travel_art_container.setVisibility(View.VISIBLE);
				lacationStoryAdapter = new LocationStoryAdapter(LocationCountryDetailActivity.this, articleInfos);
				location_country_detail_trip_art_listview.setAdapter(lacationStoryAdapter);
				location_country_detail_trip_art_listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						ArticleInfo articleInfo = (ArticleInfo) articleInfos.get(position);
						String path = Urls.content_host + articleInfo.getArticleId();
						Intent intent = new Intent(LocationCountryDetailActivity.this, ContentActivity.class);
						intent.putExtra("path", path);
						startActivity(intent);
					}
				});
			}
			mTravelListViewFooter.setVisibility(View.GONE);
			// productListView.removeFooterView(mTravelListViewFooter);
			isNeedLoad = false;
		}
		isLoadmore = false;
	}

	private boolean isNeedLoad = true;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private View mTravelListViewFooter;
	private int pageSize = 10;
	private int pageNum = 1;
}
