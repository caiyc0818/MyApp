package com.bcinfo.tripaway.activity;

import im.yixin.sdk.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.r;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.R.raw;
import com.bcinfo.tripaway.adapter.CommendClubAdapter;
import com.bcinfo.tripaway.adapter.CommendDestopicsubjectAdapter;
import com.bcinfo.tripaway.adapter.CommendactilandmosrcAdapter2;
import com.bcinfo.tripaway.adapter.LocationCountryAdapter;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductLocationsGridAdapter;
import com.bcinfo.tripaway.adapter.SubjectClubAdapter;
import com.bcinfo.tripaway.adapter.SubjectClubAdapter2;
import com.bcinfo.tripaway.adapter.SubjectDestAdapter;
import com.bcinfo.tripaway.adapter.SubjectProductAdapter;
import com.bcinfo.tripaway.adapter.SubjectProductAdapter2;
import com.bcinfo.tripaway.adapter.SubjectStoryAdapter;
import com.bcinfo.tripaway.adapter.SubjectTopicOrSofttextAdapter;
import com.bcinfo.tripaway.adapter.ThemePicListAdapter;
import com.bcinfo.tripaway.adapter.TravelAchiveAdapter;
import com.bcinfo.tripaway.adapter.commendProductAdapter;
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.TextView1;
import com.bcinfo.tripaway.view.ScrollView.LocationCountryDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.LocationCountryDetailScrollView.onTurnListener;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;

/**
 * 目的地-国家详情
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月16日 9:40:02
 */
public class SubjectDetailActivity extends BaseActivity
		implements OnClickListener, PersonalScrollViewListener, OnItemClickListener

{

	private LocationCountryDetailScrollView subjectDetailScrollView;

	private List<UserInfo> achiveList = new ArrayList<UserInfo>();

	private TextView subjectTitleTv;// 国家 中文名

	private TextView country_name;

	private TextView subjectSubTitleTv;// 国家 英文名

	private ImageView SubjectHeadIv;

	private ImageLoader imgLoader;

	private String entityId;

	private TextView subjectDesc;

	private TextView showmore_orpackup;

	private TextView responseTv;

	private int textLineCount;

	private LinearLayout show_more_layout;

	private LinearLayout country_name_layout;

	private RelativeLayout warm_tip;

	private RelativeLayout country_detail_layout_title;

	private static final int SHRINK_UP_STATE = 1;// 收起状态

	private static final int SPREAD_STATE = 2;// 展开状态

	private static int mState = SHRINK_UP_STATE;// 默认收起状态

	private ListView picListview;

	List<ImageInfo> pics = new ArrayList<ImageInfo>();// 图片列表

	ThemePicListAdapter themePicListAdapter;

	List<HashMap<String, String>> tips = new ArrayList<HashMap<String, String>>();// 图片列表

	private List<PushResource> allList = new ArrayList<PushResource>();
	private List<PushResource> productPushResourceList = new ArrayList<PushResource>();

	private List<PushResource> topicPushResourceList = new ArrayList<PushResource>();

	private List<PushResource> userPushResourceList = new ArrayList<PushResource>();

	private List<PushResource> destPushResourceList = new ArrayList<PushResource>();

	private List<PushResource> softtextPushResourceList = new ArrayList<PushResource>();

	private List<PushResource> storyPushResourceList = new ArrayList<PushResource>();

	private List<PushResource> subjectPushResourceList = new ArrayList<PushResource>();

	private List<PushResource> productPushResourceAllList = new ArrayList<PushResource>();

	private List<PushResource> topicPushResourceAllList = new ArrayList<PushResource>();

	private List<PushResource> userPushResourceAllList = new ArrayList<PushResource>();

	private List<PushResource> destPushResourceAllList = new ArrayList<PushResource>();

	private List<PushResource> softtextPushResourceAllList = new ArrayList<PushResource>();

	private List<PushResource> storyPushResourceAllList = new ArrayList<PushResource>();

	private List<PushResource> subjectPushResourceAllList = new ArrayList<PushResource>();

	private List<PushResource> topiclist = new ArrayList<PushResource>();
	private List<PushResource> subjectlist = new ArrayList<PushResource>();
	private List<PushResource> productlist = new ArrayList<PushResource>();
	private List<PushResource> deslist = new ArrayList<PushResource>();
	private List<PushResource> storylist = new ArrayList<PushResource>();
	private List<PushResource> articlist = new ArrayList<PushResource>();
	private List<PushResource> userlist = new ArrayList<PushResource>();

	private ListView subjectProductListView;

	private SubjectProductAdapter2 subjectProductAdapter;

	private GridView subjectClubGridView;

	private SubjectClubAdapter2 subjectClubAdapter;

	private GridView subjectTopiGridView;

	private SubjectTopicOrSofttextAdapter subjectTopicAdapter;

	private GridView subjectDesGridView;

	private SubjectDestAdapter subjectDestAdapter;

	private ListView subjectSofttextListView;

	private SubjectStoryAdapter subjectSofttextAdapter;

	private ListView subjectStoryListView;

	private SubjectStoryAdapter subjectStoryAdapter;

	private GridView subjectSubjectGridView;

	private SubjectTopicOrSofttextAdapter subjectSubjectAdapter;

	private TextView showmore_club;

	private TextView showmore_dest;

	private TextView showmore_product;

	private TextView showmore_softtext;

	private TextView showmore_story;

	private TextView showmore_subject;

	private TextView showmore_topic;

	private int alpha = -1;

	private Drawable downDrawable;

	private Drawable upDrawable;

	private boolean isCheck = false;

	private LinearLayout product_layout_;

	private LinearLayout club_layout;

	private LinearLayout dest_layout;

	private LinearLayout softtext_layout;

	private LinearLayout story_layout;

	private LinearLayout subject_layout;

	private LinearLayout topic_layout;

	// 推荐的LinearLayout
	// Gridkuandu
	int GridWidth;
	private LinearLayout commend_club;
	private LinearLayout commend_subject;
	private LinearLayout commend_des;
	private LinearLayout commend_mcrio;
	private LinearLayout commend_topic;
	private LinearLayout commend_actil;
	private LinearLayout commend_product;

	// 推荐位的ListView

	private ListView commend_club_listView;
	private ListView commend_subject_listView;
	private ListView commend_topic_listView;
	private ListView commend_des_listView;
	private ListView commend_mcrio_listView;
	private ListView commend_actil_listView;
	private ListView commend_product_listview;
	// private ListView commend_club_listView;
	// 推荐位的adapter
	// 推荐位 目的地 、专题、主题 的适配器、
	private CommendDestopicsubjectAdapter desAdapter;
	private CommendDestopicsubjectAdapter topicAdapter;
	private CommendDestopicsubjectAdapter subjectAdapter;
	// 推荐位 文章 游记的适配器
	private CommendactilandmosrcAdapter2 articAdapter2;
	private CommendactilandmosrcAdapter2 mosrcAdapter2;
	// 推荐位 club的适配器
	private CommendClubAdapter ClubAdapter;
	private commendProductAdapter commendProductAdapter;

	// 推荐的参数类型字符串
	String commendtype;

	private LinearLayout text;

	private ImageLoader imageLoader;

	private View loginLoading;

	private AnimationDrawable loadingAnimation;

	private Map<String, LinearLayout> viewMap = new HashMap<String, LinearLayout>();

	private RelativeLayout relateReLayout;
	
	private boolean show=false;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.subject_detail_activity);
		if(getIntent().getStringExtra("subjectTitle")!=null){
        	statisticsTitle=getIntent().getStringExtra("subjectTitle");
        }
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		imageLoader = imageLoader.getInstance();
		// 获取屏幕宽度
		WindowManager wm1 = this.getWindowManager();
		int width1 = wm1.getDefaultDisplay().getWidth();
		GridWidth = (DensityUtil.px2dip(this, width1 - DensityUtil.dip2px(this, 30))) / 2;
		entityId = getIntent().getStringExtra("entityId"); // 目的地标识id
		show = getIntent().getBooleanExtra("show",false); // 目的地标识id
		commend_actil = (LinearLayout) findViewById(R.id.commend_actil);
		commend_subject = (LinearLayout) findViewById(R.id.commend_subject);
		commend_club = (LinearLayout) findViewById(R.id.commend_club);
		commend_des = (LinearLayout) findViewById(R.id.commend_des);
		commend_mcrio = (LinearLayout) findViewById(R.id.commend_mcrio);
		commend_topic = (LinearLayout) findViewById(R.id.commend_topic);
		commend_product = (LinearLayout) findViewById(R.id.commend_product);
		// 设置全部隐藏
		commend_actil.setVisibility(View.GONE);
		commend_subject.setVisibility(View.GONE);
		commend_club.setVisibility(View.GONE);
		commend_des.setVisibility(View.GONE);
		commend_mcrio.setVisibility(View.GONE);
		commend_topic.setVisibility(View.GONE);
		commend_product.setVisibility(View.GONE);

		commend_club_listView = (ListView) findViewById(R.id.commend_club_listview);
		commend_club_listView.setOnItemClickListener(this);
		commend_subject_listView = (ListView) findViewById(R.id.commend_subject_listview);
		commend_subject_listView.setOnItemClickListener(this);
		commend_topic_listView = (ListView) findViewById(R.id.commend_topic_listview);
		commend_topic_listView.setOnItemClickListener(this);
		commend_des_listView = (ListView) findViewById(R.id.commend_des_listview);
		commend_des_listView.setOnItemClickListener(this);
		commend_mcrio_listView = (ListView) findViewById(R.id.commend_mcrio_listview);
		commend_mcrio_listView.setOnItemClickListener(this);
		commend_actil_listView = (ListView) findViewById(R.id.commend_actil_listview);
		commend_actil_listView.setOnItemClickListener(this);
		commend_product_listview = (ListView) findViewById(R.id.commend_product_listview);
		commend_product_listview.setOnItemClickListener(this);
		subjectTitleTv = (TextView) findViewById(R.id.subject_detail_title_tv);
		subjectSubTitleTv = (TextView) findViewById(R.id.subject_detail_subtitle_tv);
		country_name = (TextView) findViewById(R.id.country_name);
		country_name_layout = (LinearLayout) findViewById(R.id.country_name_layout);
		relateReLayout = (RelativeLayout) findViewById(R.id.relateReLayout);
		// responseTv= (TextView)findViewById(R.id.responsetv);
		subjectDesc = (TextView) findViewById(R.id.subject_desc);
		country_detail_layout_title = (RelativeLayout) findViewById(R.id.country_detail_layout_title);
		country_detail_layout_title.getBackground().setAlpha(0);
		warm_tip = (RelativeLayout) findViewById(R.id.warm_tip);
		country_name.setAlpha(0);
		getSubjectUrl(entityId);
		textLineCount = subjectDesc.getLineCount();
		show_more_layout = (LinearLayout) findViewById(R.id.show_more_layout);
		showmore_orpackup = (TextView) findViewById(R.id.showmore_orpackup);
		if(!show){
		ViewTreeObserver viewTreeObserver = subjectDesc.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				int maxLines = subjectDesc.getLineCount();
				// if ((Boolean) description.getTag()) {
				// return true;
				// }
				if (isCheck)
					return true;
				if (maxLines <= 5 && pics.size() == 0) {
					showmore_orpackup.setVisibility(View.GONE);
				} else {
					subjectDesc.setMaxLines(5);
					showmore_orpackup.setVisibility(View.VISIBLE);
					mState = SHRINK_UP_STATE;
					isCheck = true;
				}

				return true;
			}
		});
		}
		showmore_orpackup.setCompoundDrawablePadding(5);
		// show_more_layout.setVisibility(View.VISIBLE);
		show_more_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mState == SHRINK_UP_STATE) {
					subjectDesc.setMaxLines(Integer.MAX_VALUE);
					subjectDesc.requestLayout();
					showmore_orpackup.setText("收起");
					mState = SPREAD_STATE;
					picListview.setVisibility(View.VISIBLE);
					showmore_orpackup.setCompoundDrawables(null, null, upDrawable, null);
				} else if (mState == SPREAD_STATE) {
					subjectDesc.setMaxLines(5);
					subjectDesc.requestLayout();
					showmore_orpackup.setText("展开全部");
					picListview.setVisibility(View.GONE);
					subjectDetailScrollView.scrollTo(0, 0);
					mState = SHRINK_UP_STATE;
					showmore_orpackup.setCompoundDrawables(null, null, downDrawable, null);
				}

			}
		});

		// initProductList();
		subjectDetailScrollView = (LocationCountryDetailScrollView) findViewById(
				R.id.subject_detail_scrollview_container);
		((LinearLayout) findViewById(R.id.layout_back_button)).setOnClickListener(mOnClickListener);
		// ((ImageView)findViewById(R.id.layout_location_country_detail_container)).setOnClickListener(this);
		subjectProductListView = (ListView) findViewById(R.id.subject_product_listview);
		subjectProductListView.setOnItemClickListener(this);
		subjectClubGridView = (GridView) findViewById(R.id.gridview_club);
		subjectClubGridView.setOnItemClickListener(this);
		subjectTopiGridView = (GridView) findViewById(R.id.subject_topic_Gridview);
		subjectTopiGridView.setOnItemClickListener(this);
		// 获取listview的布局参数
		ViewGroup.LayoutParams params1 = subjectTopiGridView.getLayoutParams();
		// 设置高度
		params1.height = GridWidth;
		// 设置参数
		subjectTopiGridView.setLayoutParams(params1);
		subjectDesGridView = (GridView) findViewById(R.id.subject_dest_gridview);
		subjectDesGridView.setOnItemClickListener(this);
		// 获取listview的布局参数
		ViewGroup.LayoutParams params2 = subjectDesGridView.getLayoutParams();
		// 设置高度
		params2.height = GridWidth;
		// 设置参数
		subjectTopiGridView.setLayoutParams(params2);
		subjectSofttextListView = (ListView) findViewById(R.id.subject_softtext_listview);
		subjectSofttextListView.setOnItemClickListener(this);
		subjectStoryListView = (ListView) findViewById(R.id.subject_story_listview);
		subjectStoryListView.setOnItemClickListener(this);
		subjectSubjectGridView = (GridView) findViewById(R.id.subject_subject_gridview);
		subjectSubjectGridView.setOnItemClickListener(this);
		// 获取listview的布局参数
		ViewGroup.LayoutParams params3 = subjectDesGridView.getLayoutParams();
		// 设置高度
		params3.height = GridWidth;
		subjectTopiGridView.setLayoutParams(params3);
		showmore_club = (TextView) findViewById(R.id.showmore_club);
		showmore_dest = (TextView) findViewById(R.id.showmore_dest);
		showmore_product = (TextView) findViewById(R.id.showmore_product);
		showmore_softtext = (TextView) findViewById(R.id.showmore_softtext);
		showmore_story = (TextView) findViewById(R.id.showmore_story);
		showmore_subject = (TextView) findViewById(R.id.showmore_subject);
		showmore_topic = (TextView) findViewById(R.id.showmore_topic);
		showmore_club.setTag(false);
		showmore_dest.setTag(false);
		showmore_product.setTag(false);
		showmore_softtext.setTag(false);
		showmore_story.setTag(false);
		showmore_subject.setTag(false);
		showmore_topic.setTag(false);
		showmore_club.setOnClickListener(this);
		showmore_dest.setOnClickListener(this);
		showmore_product.setOnClickListener(this);
		showmore_softtext.setOnClickListener(this);
		showmore_story.setOnClickListener(this);
		showmore_subject.setOnClickListener(this);
		showmore_topic.setOnClickListener(this);

		club_layout = (LinearLayout) findViewById(R.id.club_layout);
		dest_layout = (LinearLayout) findViewById(R.id.dest_layout);
		softtext_layout = (LinearLayout) findViewById(R.id.softtext_layout);
		story_layout = (LinearLayout) findViewById(R.id.story_layout);
		subject_layout = (LinearLayout) findViewById(R.id.subject_layout);
		topic_layout = (LinearLayout) findViewById(R.id.topic_layout);
		product_layout_ = (LinearLayout) findViewById(R.id.product_layout);
		viewMap.put("product", product_layout_);
		viewMap.put("topic", topic_layout);
		viewMap.put("user", club_layout);
		viewMap.put("destination", dest_layout);
		viewMap.put("softtext", softtext_layout);
		viewMap.put("story", story_layout);
		viewMap.put("subject", subject_layout);

		SubjectHeadIv = (ImageView) findViewById(R.id.subject_detail_iv);

		subjectDetailScrollView.setImageView(SubjectHeadIv);
		subjectDetailScrollView.setScrollListener(this);

		subjectDetailScrollView.setTurnListener(new onTurnListener() {
			@Override
			public void onTurn(boolean isShowing) {
			}
		});
		// subjectDetailScrollView.setOnTouchListener(new TouchListenerImpl());

		picListview = (ListView) findViewById(R.id.pic__listview);
if(show)
	picListview.setVisibility(View.VISIBLE);
		downDrawable = getResources().getDrawable(R.drawable.subject_down_arrow);
		downDrawable.setBounds(0, 0, downDrawable.getMinimumWidth(), downDrawable.getMinimumHeight());
		upDrawable = getResources().getDrawable(R.drawable.subject_up_arrow);
		upDrawable.setBounds(0, 0, upDrawable.getMinimumWidth(), upDrawable.getMinimumHeight());
		ImageView subject_service_share_button = (ImageView) findViewById(R.id.subject_service_share_button);
		subject_service_share_button.setVisibility(View.GONE);
		subject_service_share_button.setOnClickListener(this);

	}

	private LinearLayout setVisible(int gone) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 专题详情url
	 */
	private void getSubjectUrl(String entityId) {

		RequestParams params = new RequestParams();
		params.put("entityId", entityId);
		params.put("entityType", "subject");
		HttpUtil.get(Urls.get_entity, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					getSubjectDetail(response);

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
	 * 获取专题 详情参数
	 */
	private void getSubjectDetail(JSONObject response) {
		String code = response.optString("code");
		if ("00000".equals(code)) {
			loadingAnimation.stop();
			loginLoading.setVisibility(View.GONE);
			JSONObject dataObj = response.optJSONObject("data");
			LogUtil.i("data", dataObj.optString("data").toString(), dataObj.optString("data").toString());
			JSONObject entityObj = dataObj.optJSONObject("entity");
			if (entityObj != null) {
				if (!StringUtils.isEmpty(entityObj.optString("title"))) {
					subjectTitleTv.setText(entityObj.optString("title"));
					country_name.setText(entityObj.optString("title"));
				}
				if (!StringUtils.isEmpty(entityObj.optString("subTitle"))) {
					subjectSubTitleTv.setText(entityObj.optString("subTitle"));
				}
				if (!StringUtils.isEmpty(entityObj.optString("cover"))) {
					ImageLoader.getInstance().displayImage(Urls.imgHost + entityObj.optString("cover"), SubjectHeadIv,
							AppConfig.options(R.drawable.ic_launcher));
				}
				if (!StringUtils.isEmpty(entityObj.optString("description"))) {
					subjectDesc.setText(entityObj.optString("description"));
				}
			}
			JSONArray picAarray = dataObj.optJSONArray("pics");
			if (picAarray != null && picAarray.length() > 0) {
				for (int m = 0; m < picAarray.length(); m++) {
					ImageInfo pic = new ImageInfo();
					pic.setDesc(picAarray.optJSONObject(m).optString("desc"));
					pic.setWidth(picAarray.optJSONObject(m).optString("width"));
					pic.setHeight(picAarray.optJSONObject(m).optString("height"));
					pic.setUrl(picAarray.optJSONObject(m).optString("url"));
					pics.add(pic);
				}
				themePicListAdapter = new ThemePicListAdapter(SubjectDetailActivity.this, pics);
				picListview.setAdapter(themePicListAdapter);
			}

			JSONArray relatedResourceArray = dataObj.optJSONArray("relatedResources");
			if (relatedResourceArray != null && relatedResourceArray.length() != 0) {
				for (int i = 0; i < relatedResourceArray.length(); i++) {
					JSONArray relatedResourcesJson = relatedResourceArray.optJSONArray(i);
					if (relatedResourcesJson != null && relatedResourcesJson.length() != 0) {
						for (int j = 0; j < relatedResourcesJson.length(); j++) {
							JSONObject relatedResourceJson = relatedResourcesJson.optJSONObject(j);
							if (relatedResourceJson.optString("objectType") != null) {
								getPushResource(relatedResourceJson, i);
							}
						}
					}
				}
			}

		}
		setData();
		// 设置推荐位显示的数据
		setcommendadapter();
	}

	/*
	 * 设置推荐位显示的数据
	 */
	private void setcommendadapter() {

		// TODO Auto-generated method stub
		// 判断推荐位显示类型的数据
		if (!(allList == null)) {
			// String commend_type = allList.get(0).getObjectType();

			List<String> commend_type1 = new ArrayList<>();
			for (int i = 0; i < allList.size(); i++) {
				commend_type1.add(allList.get(i).getObjectType());
			}
//			for (int i = 0; i < commend_type1.size(); i++) { // 测试时
//				if (productlist.size() >= 1 && commend_type1.get(i).equals(productlist.get(0).getObjectType())) {
//					// product_layout_.setVisibility(View.GONE);
//					commend_product.setVisibility(View.VISIBLE);
//					commendProductAdapter = new commendProductAdapter(productlist, this);
//					commend_product_listview.setAdapter(commendProductAdapter);
//				}
//				if (userlist.size() >= 1 && commend_type1.get(i).equals(userlist.get(0).getObjectType())) {
//					// club_layout.setVisibility(View.GONE);
//					commend_club.setVisibility(View.VISIBLE);
//					ClubAdapter = new CommendClubAdapter(this, userlist);
//					commend_club_listView.setAdapter(ClubAdapter);
//
//				}
//				if (topiclist.size() >= 1 && commend_type1.get(i).equals(topiclist.get(0).getObjectType())) {
//					// topic_layout.setVisibility(View.GONE);
//					commend_topic.setVisibility(View.VISIBLE);
//					topicAdapter = new CommendDestopicsubjectAdapter(SubjectDetailActivity.this, topiclist);
//					commend_topic_listView.setAdapter(topicAdapter);
//				}
//				if (deslist.size() >= 1 && commend_type1.get(i).equals(deslist.get(0).getObjectType())) {
////					 dest_layout.setVisibility(View.GONE);
//					commend_des.setVisibility(View.VISIBLE);
//					desAdapter = new CommendDestopicsubjectAdapter(this, deslist);
//					commend_des_listView.setAdapter(desAdapter);
//					LogUtil.i("deslist的数量", "deslist.size()", deslist.toString());
//				}
//				if (articlist.size() >= 1 && commend_type1.get(i).equals(articlist.get(0).getObjectType())) {
//					// softtext_layout.setVisibility(View.GONE);
//					commend_actil.setVisibility(View.VISIBLE);
//					articAdapter2 = new CommendactilandmosrcAdapter2(this, articlist);
//					commend_actil_listView.setAdapter(articAdapter2);
//				}
//				if (storylist.size() >= 1 && commend_type1.get(i).equals(storylist.get(0).getObjectType())) {
//					// story_layout.setVisibility(View.GONE);
//					commend_mcrio.setVisibility(View.VISIBLE);
//					mosrcAdapter2 = new CommendactilandmosrcAdapter2(this, storylist);
//					commend_mcrio_listView.setAdapter(mosrcAdapter2);
//				}
//				if (subjectlist.size() >= 1 && commend_type1.get(i).equals(subjectlist.get(0).getObjectType())) {
//					// subject_layout.setVisibility(View.GONE);
//					commend_subject.setVisibility(View.VISIBLE);
//					subjectAdapter = new CommendDestopicsubjectAdapter(SubjectDetailActivity.this, subjectlist);
//					commend_subject_listView.setAdapter(subjectAdapter);
//				}
//
//			}
			 for (int i = 0; i <1; i++) {//正式
			 if (productlist.size() >= 1
			 &&commend_type1.get(i).equals(productlist.get(0).getObjectType()))
			 {
			 product_layout_.setVisibility(View.GONE);
			 commend_product.setVisibility(View.VISIBLE);
			 commendProductAdapter = new commendProductAdapter(productlist,
			 this);
			 commend_product_listview.setAdapter(commendProductAdapter);
			 break;
			 }
			 if (userlist.size() >= 1 &&
			 commend_type1.get(i).equals(userlist.get(0).getObjectType())) {
			 club_layout.setVisibility(View.GONE);
			 commend_club.setVisibility(View.VISIBLE);
			 ClubAdapter = new CommendClubAdapter(this, userlist);
			 commend_club_listView.setAdapter(ClubAdapter);
			 break;
			 }
			 if (topiclist.size() >= 1 &&
			 commend_type1.get(i).equals(topiclist.get(0).getObjectType())) {
			 topic_layout.setVisibility(View.GONE);
			 commend_topic.setVisibility(View.VISIBLE);
			 topicAdapter = new
			 CommendDestopicsubjectAdapter(SubjectDetailActivity.this,
			 topiclist);
			 commend_topic_listView.setAdapter(topicAdapter);
			 break;
			 }
			 if (deslist.size() >= 1 &&
			 commend_type1.get(i).equals(deslist.get(0).getObjectType())) {
			 dest_layout.setVisibility(View.GONE);
			 commend_des.setVisibility(View.VISIBLE);
			 desAdapter = new CommendDestopicsubjectAdapter(this, deslist);
			 commend_des_listView.setAdapter(desAdapter);
			 break;
			 }
			 if (articlist.size() >= 1 &&
			 commend_type1.get(i).equals(articlist.get(0).getObjectType())) {
			 softtext_layout.setVisibility(View.GONE);
			 commend_actil.setVisibility(View.VISIBLE);
			 articAdapter2 = new CommendactilandmosrcAdapter2(this,
			 articlist);
			 commend_actil_listView.setAdapter(articAdapter2);
			 break;
			 }
			 if (storylist.size() >= 1 &&
			 commend_type1.get(i).equals(storylist.get(0).getObjectType())) {
			 story_layout.setVisibility(View.GONE);
			 commend_mcrio.setVisibility(View.VISIBLE);
			 mosrcAdapter2 = new CommendactilandmosrcAdapter2(this,
			 storylist);
			 commend_mcrio_listView.setAdapter(mosrcAdapter2);
			 break;
			 }
			 if (subjectlist.size() >= 1 &&
			 commend_type1.get(i).equals(subjectlist.get(0).getObjectType()))
			 {
			 subject_layout.setVisibility(View.GONE);
			 commend_subject.setVisibility(View.VISIBLE);
			 subjectAdapter = new
			 CommendDestopicsubjectAdapter(SubjectDetailActivity.this,
			 subjectlist);
			 commend_subject_listView.setAdapter(subjectAdapter);
			 }
			
			 }
		
		}

	}

	private void setData() {
		RelativeLayout.LayoutParams params;
		LinearLayout linearLayout;
		for (String key : viewMap.keySet()) {
			if (map.get(key) != null) {
				int id = map.get(key);
				linearLayout = viewMap.get(key);
				if (id > 0) {
					params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
					params.addRule(RelativeLayout.BELOW, id - 1); // add a new
																	// one
																	// 'BELOW'
																	// rule,below
																	// control
																	// NO. 11
					linearLayout.setLayoutParams(params);
				}
			}
		}
		initListView(productPushResourceList, productPushResourceAllList, showmore_product);
		initListView3(topicPushResourceList, topicPushResourceAllList, showmore_topic);
		initListView2(userPushResourceList, userPushResourceAllList, showmore_club);
		initListView3(destPushResourceList, destPushResourceAllList, showmore_dest);
		initListView(softtextPushResourceList, softtextPushResourceAllList, showmore_softtext);
		initListView(storyPushResourceList, storyPushResourceAllList, showmore_story);
		initListView3(subjectPushResourceList, subjectPushResourceAllList, showmore_subject);
		if (productPushResourceList.size() > 0&&map.get("product")!=0) {
			subjectProductAdapter = new SubjectProductAdapter2(productPushResourceList, this);
			subjectProductListView.setAdapter(subjectProductAdapter);
		}else{
			product_layout_.setVisibility(View.GONE);
		}
		if (userPushResourceList.size() > 0&&map.get("user")!=0) {
			subjectClubAdapter = new SubjectClubAdapter2(this, userPushResourceList);
			subjectClubGridView.setAdapter(subjectClubAdapter);
		}else{
			club_layout.setVisibility(View.GONE);
		}
		if (topicPushResourceList.size() > 0&&map.get("topic")!=0) {
			subjectTopicAdapter = new SubjectTopicOrSofttextAdapter(this, topicPushResourceList);
			subjectTopiGridView.setAdapter(subjectTopicAdapter);
		}else{
			topic_layout.setVisibility(View.GONE);
		}
		if (destPushResourceList.size() > 0&&map.get("destination")!=0) {
			subjectDestAdapter = new SubjectDestAdapter(this, destPushResourceList);
			subjectDesGridView.setAdapter(subjectDestAdapter);
		}else{
			 dest_layout.setVisibility(View.GONE);
		}
		if (softtextPushResourceList.size() > 0&&map.get("softtext")!=0) {
			subjectSofttextAdapter = new SubjectStoryAdapter(this, softtextPushResourceList);
			subjectSofttextListView.setAdapter(subjectSofttextAdapter);
		}else{
			 softtext_layout.setVisibility(View.GONE);
		}
		
		if (storyPushResourceList.size() > 0&&map.get("story")!=0) {
			subjectStoryAdapter = new SubjectStoryAdapter(this, storyPushResourceList);
			subjectStoryListView.setAdapter(subjectStoryAdapter);
		}else{
			 story_layout.setVisibility(View.GONE);
		}
		if (subjectPushResourceList.size() > 0&&map.get("subject")!=0) {
			subjectSubjectAdapter = new SubjectTopicOrSofttextAdapter(this, subjectPushResourceList);
			subjectSubjectGridView.setAdapter(subjectSubjectAdapter);
		}else{
			 subject_layout.setVisibility(View.GONE);
		}

	}

	private List<PushResource> destPushResourceAllListpush() {
		// TODO Auto-generated method stub
		return null;
	}

	Map<String, Integer> map = new HashMap<String, Integer>();

	private void getPushResource(JSONObject relatedResourceJson, int i) {

		PushResource pushResource = new PushResource();
		pushResource.setRelationId(relatedResourceJson.optString("relationId"));
		pushResource.setReason(relatedResourceJson.optString("reason"));
		pushResource.setObjectType(relatedResourceJson.optString("objectType"));
		pushResource.setObjectId(relatedResourceJson.optString("objectId"));
		pushResource.setDescription(relatedResourceJson.optString("description"));
		pushResource.setResTitle(relatedResourceJson.optString("resTitle"));
		pushResource.setTitleImage(relatedResourceJson.optString("titleImage"));
		JSONObject objectParamObj = relatedResourceJson.optJSONObject("objectParams");
		if (objectParamObj != null && objectParamObj.length() > 0) {
			pushResource.getObjectParam().put("nickname", objectParamObj.optString("nickname"));
			pushResource.getObjectParam().put("tags", objectParamObj.optString("tags"));
			pushResource.getObjectParam().put("avartar", objectParamObj.optString("avartar"));
		}
		JSONObject jSONObject = relatedResourceJson.optJSONObject("object");
		// 获取所有的pushResource对象。通过遍历来取得第一个推荐的数据
		allList.add(pushResource);

		if (jSONObject != null && jSONObject.length() > 0) {
			if ("product".equals(relatedResourceJson.optString("objectType"))) {
				pushResource.setObject(JsonUtil.getProductNewInfo(jSONObject));
//				ProductNewInfo proNewInfo = new ProductNewInfo();
//	            	if (jSONObject.optJSONObject("exts") != null) {
//						HashMap<String, String> exts=new HashMap<String, String>();
//						if(!StringUtils.isEmpty(jSONObject.optJSONObject("exts").optString("refer_tags"))){
//							exts.put("refer_tags", jSONObject.optJSONObject("exts").optString("refer_tags"));
//						}
//						if(!StringUtils.isEmpty(jSONObject.optJSONObject("exts").optString("big_refer_tags"))){
//							exts.put("big_refer_tags", jSONObject.optJSONObject("exts").optString("big_refer_tags"));
//						}
//						if(exts.size()>0){
//							proNewInfo.setExts(exts);	
//						}
//					}
//	            	proNewInfo.setPv(jSONObject.optString("pv"));
//	            	proNewInfo.setOriginalPrice(jSONObject.optString("originalPrice"));
//	                proNewInfo.setId(jSONObject.optString("id"));
//	                proNewInfo.setDistance(jSONObject.optString("distance"));
//	                JSONArray topicArray = (jSONObject.optJSONArray("topics"));
//	                for (int j = 0; j < topicArray.length(); j++)
//	                {
//	                    proNewInfo.getTopics().add(topicArray.optString(j));
//	                }
//	                proNewInfo.setTitle(jSONObject.optString("title"));
//	                proNewInfo.setPoiCount(jSONObject.optString("poiCount"));
//	                proNewInfo.setPrice(jSONObject.optString("price"));
//	                proNewInfo.setDays((jSONObject.optString("days")));
//	                proNewInfo.setDescription(jSONObject.optString("description"));
//	                proNewInfo.setPriceMax(jSONObject.optString("priceMax"));
//	                proNewInfo.setTitleImg(jSONObject.optString("titleImg"));
//	                proNewInfo.setMaxMember(jSONObject.optString("maxMember"));
//	                proNewInfo.setProductType(jSONObject.optString("productType"));
//	                proNewInfo.setIsFav("yes");
//	                proNewInfo.setServices(jSONObject.optString("serviceCodes"));
//	                proNewInfo.setPriceFrequency(jSONObject.optString("priceFrequency"));
//	                //add by lij 2015/09/25 start 新增每个产品的参与人数 
//	                proNewInfo.setMemberJoinCount(jSONObject.optString("servTimes"));
//	                //add by lij 2015/09/25 end
//	                // tags参数有问题 暂时不解析
//	                if (jSONObject.optJSONObject("creater") != null)
//	                {
//	                    proNewInfo.getUser().setGender(jSONObject.optJSONObject("creater").optString("sex"));
//	                    proNewInfo.getUser().setStatus(jSONObject.optJSONObject("creater").optString("status"));
//	                    proNewInfo.getUser().setEmail(jSONObject.optJSONObject("creater").optString("email"));
//	                    proNewInfo.getUser().setNickname(jSONObject.optJSONObject("creater").optString("nickName"));
//	                    proNewInfo.getUser().setUserId(jSONObject.optJSONObject("creater").optString("userId"));
//	                    proNewInfo.getUser().setAvatar(jSONObject.optJSONObject("creater").optString("avatar"));
//	                    proNewInfo.getUser().setIntroduction(jSONObject.optJSONObject("creater").optString("introduction"));
//	                    proNewInfo.getUser().setMobile(jSONObject.optJSONObject("creater").optString("mobile"));
//	                }
//				pushResource.setObject(proNewInfo);
				productPushResourceAllList.add(pushResource);
				productlist.add(pushResource);
				if (map.get("product") == null) {
					map.put("product", i);
					viewMap.get("product").setId(i);
				}

			} else if ("topic".equals(relatedResourceJson.optString("objectType"))) {
				pushResource.setObject(JsonUtil.getTopicInfo(jSONObject));
				topicPushResourceAllList.add(pushResource);
				topiclist.add(pushResource);
				if (map.get("topic") == null) {
					map.put("topic", i);
					viewMap.get("topic").setId(i);
				}
			} else if ("user".equals(relatedResourceJson.optString("objectType"))) {
				pushResource.setObject(JsonUtil.getUserInfo(jSONObject));
				userPushResourceAllList.add(pushResource);
				userlist.add(pushResource);
				if (map.get("user") == null) {
					map.put("user", i);
					viewMap.get("user").setId(i);
				}
			} else if ("destination".equals(relatedResourceJson.optString("objectType"))) {
				pushResource.setObject(JsonUtil.getDestinationInfo(jSONObject));
				destPushResourceAllList.add(pushResource);
				deslist.add(pushResource);
				if (map.get("destination") == null) {
					map.put("destination", i);
					viewMap.get("destination").setId(i);
				}

			} else if ("softtext".equals(relatedResourceJson.optString("objectType"))) {
				pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));
				softtextPushResourceAllList.add(pushResource);
				articlist.add(pushResource);
				if (map.get("softtext") == null) {
					map.put("softtext", i);
					viewMap.get("softtext").setId(i);
				}

			} else if ("story".equals(relatedResourceJson.optString("objectType"))) {
				pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));
				storyPushResourceAllList.add(pushResource);
				storylist.add(pushResource);
				if (map.get("story") == null) {
					map.put("story", i);
					viewMap.get("story").setId(i);
				}
			} else if ("subject".equals(relatedResourceJson.optString("objectType"))) {
				pushResource.setObject(JsonUtil.getSubjectInfo(jSONObject));
				subjectPushResourceAllList.add(pushResource);
				subjectlist.add(pushResource);
				if (map.get("subject") == null) {
					map.put("subject", i);
					viewMap.get("subject").setId(i);
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.showmore_club:
			refreshListView2(subjectClubAdapter, userPushResourceList, userPushResourceAllList, showmore_club);
			break;
		case R.id.showmore_dest:
			refreshListView3(subjectDestAdapter, destPushResourceList, destPushResourceAllList, showmore_dest);
			break;
		case R.id.showmore_product:
			refreshListView(subjectProductAdapter, productPushResourceList, productPushResourceAllList,
					showmore_product);
			break;
		case R.id.showmore_softtext:
			refreshListView(subjectSofttextAdapter, softtextPushResourceList, softtextPushResourceAllList,
					showmore_softtext);
			break;
		case R.id.showmore_story:
			refreshListView(subjectStoryAdapter, storyPushResourceList, storyPushResourceAllList, showmore_story);
			break;
		case R.id.showmore_subject:
			refreshListView3(subjectSubjectAdapter, subjectPushResourceList, subjectPushResourceAllList,
					showmore_subject);
			break;
		case R.id.showmore_topic:
			refreshListView3(subjectTopicAdapter, topicPushResourceList, topicPushResourceAllList, showmore_topic);
			break;
		case R.id.subject_service_share_button:
			// ShareSelectDialog shareSelectDialog = new ShareSelectDialog(
			// GrouponProductNewDetailActivity.this,
			// productInfo.getTitleImg(), productInfo.getFeature(),
			// productInfo.getTitle(), productInfo.getId());
			// shareSelectDialog.show();
			break;
		default:
			break;
		}

	}

	private void initListView(List<PushResource> pushResources, List<PushResource> pushResourcesAll,
			TextView showMoreTextView) {
		if (pushResourcesAll.size() <= 2) {
			showMoreTextView.setVisibility(View.GONE);
			pushResources.addAll(pushResourcesAll);
			if (pushResourcesAll.size() == 0 && pushResourcesAll != productPushResourceAllList) {
				((View) showMoreTextView.getParent()).setVisibility(View.GONE);
			} else {
				((View) showMoreTextView.getParent()).setVisibility(View.VISIBLE);
			}
		} else {
			((View) showMoreTextView.getParent()).setVisibility(View.VISIBLE);
			showMoreTextView.setVisibility(View.VISIBLE);
			showMoreTextView.setCompoundDrawables(null, null, downDrawable, null);
			showMoreTextView.setCompoundDrawablePadding(5);
			int i = 0;
			for (PushResource pushResource : pushResourcesAll) {
				if (i == 2)
					break;
				pushResources.add(pushResource);
				++i;
			}
		}

	}

	private void initListView2(List<PushResource> pushResources, List<PushResource> pushResourcesAll,
			TextView showMoreTextView) {
		if (pushResourcesAll.size() <= 3) {
			showMoreTextView.setVisibility(View.GONE);
			pushResources.addAll(pushResourcesAll);
			if (pushResourcesAll.size() == 0 && pushResourcesAll != productPushResourceAllList) {
				((View) showMoreTextView.getParent()).setVisibility(View.GONE);
			} else {
				((View) showMoreTextView.getParent()).setVisibility(View.VISIBLE);
			}

		} else {
			((View) showMoreTextView.getParent()).setVisibility(View.VISIBLE);
			showMoreTextView.setVisibility(View.VISIBLE);
			showMoreTextView.setCompoundDrawables(null, null, downDrawable, null);
			showMoreTextView.setCompoundDrawablePadding(5);
			int i = 0;
			for (PushResource pushResource : pushResourcesAll) {
				if (i == 3)
					break;
				pushResources.add(pushResource);
				++i;
			}
		}

	}

	private void initListView3(List<PushResource> pushResources, List<PushResource> pushResourcesAll,
			TextView showMoreTextView) {
		if (pushResourcesAll.size() <= 4) {
			showMoreTextView.setVisibility(View.GONE);
			pushResources.addAll(pushResourcesAll);
			if (pushResourcesAll.size() == 0 && pushResourcesAll != productPushResourceAllList) {
				((View) showMoreTextView.getParent()).setVisibility(View.GONE);
			} else {
				((View) showMoreTextView.getParent()).setVisibility(View.VISIBLE);
			}

		} else {
			((View) showMoreTextView.getParent()).setVisibility(View.VISIBLE);
			showMoreTextView.setVisibility(View.VISIBLE);
			showMoreTextView.setCompoundDrawables(null, null, downDrawable, null);
			showMoreTextView.setCompoundDrawablePadding(5);
			int i = 0;
			for (PushResource pushResource : pushResourcesAll) {
				if (i == 4)
					break;
				pushResources.add(pushResource);
				++i;
			}
		}

	}

	private void refreshListView(BaseAdapter adapter, List<PushResource> pushResources,
			List<PushResource> pushResourcesAll, TextView showMoreTextView) {
		boolean tag = (Boolean) showMoreTextView.getTag();
		tag = !tag;
		pushResources.clear();
		if (tag) {
			pushResources.addAll(pushResourcesAll);
			showMoreTextView.setText("收起");
			showMoreTextView.setCompoundDrawables(null, null, upDrawable, null);
		} else {
			int i = 0;
			for (PushResource pushResource : pushResourcesAll) {
				if (i == 2)
					break;
				pushResources.add(pushResource);
				++i;
			}
			showMoreTextView.setText("展开全部");
			showMoreTextView.setCompoundDrawables(null, null, downDrawable, null);
		}
		showMoreTextView.setTag(tag);
		adapter.notifyDataSetChanged();
	}

	private void refreshListView2(BaseAdapter adapter, List<PushResource> pushResources,
			List<PushResource> pushResourcesAll, TextView showMoreTextView) {
		boolean tag = (Boolean) showMoreTextView.getTag();
		tag = !tag;
		pushResources.clear();
		if (tag) {
			pushResources.addAll(pushResourcesAll);
			showMoreTextView.setText("收起");
			showMoreTextView.setCompoundDrawables(null, null, upDrawable, null);
		} else {
			int i = 0;
			for (PushResource pushResource : pushResourcesAll) {
				if (i == 3)
					break;
				pushResources.add(pushResource);
				++i;
			}
			showMoreTextView.setText("展开全部");
			showMoreTextView.setCompoundDrawables(null, null, downDrawable, null);
		}
		showMoreTextView.setTag(tag);
		adapter.notifyDataSetChanged();
	}

	private void refreshListView3(BaseAdapter adapter, List<PushResource> pushResources,
			List<PushResource> pushResourcesAll, TextView showMoreTextView) {
		boolean tag = (Boolean) showMoreTextView.getTag();
		tag = !tag;
		pushResources.clear();
		if (tag) {
			pushResources.addAll(pushResourcesAll);
			showMoreTextView.setText("收起");
			showMoreTextView.setCompoundDrawables(null, null, upDrawable, null);
		} else {
			int i = 0;
			for (PushResource pushResource : pushResourcesAll) {
				if (i == 4)
					break;
				pushResources.add(pushResource);
				++i;
			}
			showMoreTextView.setText("展开全部");
			showMoreTextView.setCompoundDrawables(null, null, downDrawable, null);
		}
		showMoreTextView.setTag(tag);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY) {
		// TODO Auto-generated method stub
		if (view != null && view == subjectDetailScrollView) {

			alpha = positionY / 2;
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

		} else {
			return;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (alpha != -1) {
			country_detail_layout_title.getBackground().setAlpha(alpha);
			country_name.setAlpha(alpha / 255f);
		}
	}
	// 推荐位的监听事件

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.commend_product_listview:
			ProductNewInfo productNewInfo2 = (ProductNewInfo) productlist.get(position).getObject();
			Intent intentForProductDetail2;
			if (productNewInfo2.getProductType().equals("base")
					|| productNewInfo2.getProductType().equals("customization")) {
				intentForProductDetail2 = new Intent(this, GrouponProductNewDetailActivity.class);
				intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			} else if (productNewInfo2.getProductType().equals("single")
					&& (productNewInfo2.getServices().equals("traffic"))) {
				intentForProductDetail2 = new Intent(this, CarProductDetailActivity.class);
				intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			} else if (productNewInfo2.getProductType().equals("single")
					&& (productNewInfo2.getServices().equals("stay"))) {
				intentForProductDetail2 = new Intent(this, HouseProductDetailActivity.class);
				intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
			}
			// 团产品
			else if (productNewInfo2.getProductType().equals("team")) {

				intentForProductDetail2 = new Intent(this, GrouponProductNewDetailActivity.class);
				intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
				intentForProductDetail2.putExtra("productTitle", productNewInfo2.getTitle());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			} else {
				intentForProductDetail2 = new Intent(this, CarProductDetailActivity.class);
				intentForProductDetail2.putExtra("productId", productNewInfo2.getId());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			}
			if (intentForProductDetail2 != null)
				this.startActivity(intentForProductDetail2);
			break;
		case R.id.commend_des_listview:
			PushResource pushLocateResource = deslist.get(position);
			String destId = pushLocateResource.getObjectId();// 目的地标识
			Intent intentForLocateCountry = new Intent(this, LocationCountryDetailActivity.class);
			intentForLocateCountry.putExtra("destId", destId);
			startActivity(intentForLocateCountry);
			break;
		case R.id.subject_product_listview:

			ProductNewInfo productNewInfo = (ProductNewInfo) productPushResourceList.get(position).getObject();
			Intent intentForProductDetail;
			if (productNewInfo.getProductType().equals("base")
					|| productNewInfo.getProductType().equals("customization")) {
				intentForProductDetail = new Intent(this, GrouponProductNewDetailActivity.class);
				intentForProductDetail.putExtra("productId", productNewInfo.getId());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			} else if (productNewInfo.getProductType().equals("single")
					&& (productNewInfo.getServices().equals("traffic"))) {
				intentForProductDetail = new Intent(this, CarProductDetailActivity.class);
				intentForProductDetail.putExtra("productId", productNewInfo.getId());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			} else if (productNewInfo.getProductType().equals("single")
					&& (productNewInfo.getServices().equals("stay"))) {
				intentForProductDetail = new Intent(this, HouseProductDetailActivity.class);
				intentForProductDetail.putExtra("productId", productNewInfo.getId());
			}
			// 团产品
			else if (productNewInfo.getProductType().equals("team")) {

				intentForProductDetail = new Intent(this, GrouponProductNewDetailActivity.class);
				intentForProductDetail.putExtra("productId", productNewInfo.getId());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			} else {
				intentForProductDetail = new Intent(this, CarProductDetailActivity.class);
				intentForProductDetail.putExtra("productId", productNewInfo.getId());
				// intentForProductDetail.putExtra("product",
				// productNewInfo);
			}
			if (intentForProductDetail != null)
				this.startActivity(intentForProductDetail);
			break;
		case R.id.subject_dest_gridview:

			PushResource pushLocateResource2 = destPushResourceList.get(position);
			String destId2 = pushLocateResource2.getObjectId();// 目的地标识
			Intent intentForLocateCountry2 = new Intent(this, LocationCountryDetailActivity.class);
			intentForLocateCountry2.putExtra("destId", destId2);
			startActivity(intentForLocateCountry2);
			break;
		case R.id.subject_softtext_listview:
			ArticleInfo articleInfo = (ArticleInfo) softtextPushResourceList.get(position).getObject();
			String path = Urls.content_host + articleInfo.getArticleId();
			Intent intent = new Intent(this, ContentActivity.class);
			intent.putExtra("path", path);
			startActivity(intent);
			break;
		case R.id.commend_actil_listview:
			ArticleInfo articleInfo2 = (ArticleInfo) articlist.get(position).getObject();
			String path2 = Urls.content_host + articleInfo2.getArticleId();
			Intent intent2 = new Intent(this, ContentActivity.class);
			intent2.putExtra("path", path2);
			startActivity(intent2);
			break;

		case R.id.subject_story_listview:

			articleInfo = (ArticleInfo) storyPushResourceList.get(position).getObject();
			path = Urls.content_host + articleInfo.getArticleId();
			intent = new Intent(this, ContentActivity.class);
			intent.putExtra("path", path);
			startActivity(intent);
			break;
		case R.id.commend_mcrio_listview:
			articleInfo = (ArticleInfo) storylist.get(position).getObject();
			path = Urls.content_host + articleInfo.getArticleId();
			intent = new Intent(this, ContentActivity.class);
			intent.putExtra("path", path);
			startActivity(intent);
			break;
		case R.id.subject_topic_Gridview:

			TopicInfo topicInfo = (TopicInfo) topicPushResourceList.get(position).getObject();
			String topicId = topicPushResourceList.get(position).getObjectId();
			Intent intentForTopicProductList = new Intent(this, ThemeDetailActivity.class);
			intentForTopicProductList.putExtra("themeId", topicId);
			intentForTopicProductList.putExtra("themeName", topicInfo.getTitle());
			intentForTopicProductList.putExtra("description", topicInfo.getDescription());
			startActivity(intentForTopicProductList);
			break;
		case R.id.commend_topic_listview:

			TopicInfo topicInfo2 = (TopicInfo) topiclist.get(position).getObject();
			String topicId2 = topicPushResourceList.get(position).getObjectId();
			Intent intentForTopicProductList2 = new Intent(this, ThemeDetailActivity.class);
			intentForTopicProductList2.putExtra("themeId", topicId2);
			intentForTopicProductList2.putExtra("themeName", topicInfo2.getTitle());
			intentForTopicProductList2.putExtra("description", topicInfo2.getDescription());
			startActivity(intentForTopicProductList2);
			break;
		case R.id.subject_subject_gridview:

			String entityId = subjectPushResourceList.get(position).getObjectId();
			Intent intentSubjectDetail = new Intent(this, SubjectDetailActivity.class);
			intentSubjectDetail.putExtra("entityId", entityId);
			startActivity(intentSubjectDetail);
			break;

		case R.id.commend_subject_listview:

			String entityId2 = subjectlist.get(position).getObjectId();
			Intent intentSubjectDetail2 = new Intent(this, SubjectDetailActivity.class);
			intentSubjectDetail2.putExtra("entityId", entityId2);
			startActivity(intentSubjectDetail2);
			break;
		case R.id.gridview_club:

			UserInfo userInfo = (UserInfo) userPushResourceList.get(position).getObject();
//			Intent intentClubFirstPage = new Intent(this, ClubFirstPageActivity.class);
//			intentClubFirstPage.putExtra("userInfo", userInfo);
//			startActivity(intentClubFirstPage);
			ActivityUtil.toPersonHomePage(userInfo, this);
			break;
		case R.id.commend_club_listview:
			UserInfo userInfo2 = (UserInfo) userlist.get(position).getObject();
//			Intent intentClubFirstPage2 = new Intent(this, ClubFirstPageActivity.class);
//			intentClubFirstPage2.putExtra("userInfo", userInfo2);
//			startActivity(intentClubFirstPage2);
			ActivityUtil.toPersonHomePage(userInfo2, this);
			break;
		default:

		}
	}

}
