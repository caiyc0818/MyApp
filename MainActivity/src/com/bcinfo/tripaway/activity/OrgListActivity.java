package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ClubAdapter;
import com.bcinfo.tripaway.adapter.ClubListAdapter;
import com.bcinfo.tripaway.adapter.HelpAndConsultAdapter;
import com.bcinfo.tripaway.adapter.MyAdsPagerAdapter;
import com.bcinfo.tripaway.adapter.SubjectGridAdapter;
import com.bcinfo.tripaway.adapter.SubjectProductAdapter;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Club;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.HelpInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;
import com.bcinfo.tripaway.listener.GridScrollListener;
import com.bcinfo.tripaway.listener.GridScrollListener.AutoLoadCallBack;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.FilterLayout;
import com.bcinfo.tripaway.view.MyGridView;
import com.bcinfo.tripaway.view.MBProgressHUD.MyMBProgressHUD;
import com.bcinfo.tripaway.view.FilterLayout.OnDaysListener;
import com.bcinfo.tripaway.view.FilterLayout.OnDestinationListener;
import com.bcinfo.tripaway.view.FilterLayout.OnPriceListener;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设置-帮助
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月15日 14:49:22
 */
public class OrgListActivity extends BaseActivity implements IXListViewListener, OnClickListener, OnItemClickListener {

	private ViewPager viewPager;
	private XListView pickedListView;
	private View headerView;
	private LinearLayout dotsLayout;
	private LinearLayout startFilter;
	private ArrayList<View> imageViews;
	private TextView all;
	private ArrayList<View> dots;
	private int count;

	private int pagesize = 10;

	private int pagenum = 1;
	/**
	 * 当前图片的索引号
	 */
	private int currentItem = 0;

	// private List<Club> clubList=new ArrayList<Club>();

	private List<UserInfo> clubList = new ArrayList<UserInfo>();

	private ClubAdapter clubAdapter;
	private TextView second_title_text;
	private LinearLayout backLayout;

	private String priceStrsMatch[] = { "lt2000", "2001-3000", "3001-5000", "5000-1W", "1W-5W", "gt5W" };
	private String daysStrsMatch[] = { "lt5", "5-10", "10-15", "gt15" };
	private List<String> servAreaId = new ArrayList<String>();
	private List<String> servAreaName = new ArrayList<String>();
	private List<String> clubTypeID = new ArrayList<String>();
	private List<String> clubTypeName = new ArrayList<String>();
	private List<String> footprintId = new ArrayList<String>();
	private List<String> footprintName = new ArrayList<String>();

	private List<String> tagIds = new ArrayList<String>();

	/**
	 * 加载数据是否结束
	 */
	private boolean isLoadDataEnd = true;

	private boolean isCanLoadData = true;

	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private int isFilterChange[][] = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	private int isFilterChangeTemp[][] = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	private ImageView orgButton;
	/**
	 * 专题adapter
	 */
	private SubjectGridAdapter mOrgGridViewAdapter;
	/**
	 * 专题对象集合
	 */
	private ArrayList<PushResource> orgList = new ArrayList<PushResource>();
	/**
	 * 热门目的地显示的GridView
	 */
	private MyGridView orgGridView;
	/**
	 * 俱乐部列表
	 */
	private ArrayList<Club> clubLists = new ArrayList<>();
	/**
	 * 俱乐部显示控件
	 */
	private MyGridView club_griv_view;
	/**
	 * 俱乐部Adapter
	 */
	private ClubListAdapter clubListAdapter;

	private ScrollView sv;

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		setContentView(R.layout.org_list);
		statisticsTitle="发现-更多";
		// getHelp("100","1");
		findView();
		second_title_text.setText("更多");
		clubAdapter = new ClubAdapter(this, clubList);
//		getPushCarousel();
		// getClub();
//		getClubTags();
		getPushResourceList();
		pickedListView.setAdapter(clubAdapter);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
			}
		});

		pickedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				// queryUserInfo(clubList.get(position-2).getUserId());
				UserInfo userInfo = clubList.get(position - 2);
				ActivityUtil.toPersonHomePage(userInfo, OrgListActivity.this);
			}
		});
		RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.second_title);
		titleLayout.setAlpha(1);
		titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
		orgButton = (ImageView) findViewById(R.id.org_button);
		orgButton.setOnClickListener(this);
		club_griv_view = (MyGridView) findViewById(R.id.club_griv_view);
		clubListAdapter = new ClubListAdapter(OrgListActivity.this, clubLists);
		club_griv_view.setAdapter(clubListAdapter);
		clubLists.clear();
		
		clubListAdapter.notifyDataSetChanged();
		sv = (ScrollView) findViewById(R.id.sv);
		GridScrollListener autoLoadListener = new GridScrollListener(callBack);
		// sv.setOnScrollListener(autoLoadListener);
		sv.setOnTouchListener(new TouchListenerImpl());
	}

	private class TouchListenerImpl implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = sv.getChildAt(0).getMeasuredHeight();
				if (scrollY == 0) {
					System.out.println("滑动到了顶端 view.getScrollY()=" + scrollY);
				}
				if ((scrollY + height) == scrollViewMeasuredHeight) {
					System.out.println("滑动到了底部 scrollY=" + scrollY);
					System.out.println("滑动到了底部 height=" + height);
					System.out.println("滑动到了底部 scrollViewMeasuredHeight=" + scrollViewMeasuredHeight);
					if (!isLoadDataEnd && isCanLoadData) {
						getClubInfo();
					}
				}
				break;

			default:
				break;
			}
			return false;
		}

	};

	/**
	 * 回调函数
	 */
	AutoLoadCallBack callBack = new AutoLoadCallBack() {

		public void execute() {
			Toast.makeText(OrgListActivity.this, "滚动至底部", 500).show();
			if (!isLoadDataEnd && isCanLoadData) {
				getClubInfo();
			}
		}

	};
	private View loginLoading;
	private AnimationDrawable loadingAnimation;

	private void getClubInfo() {
		RequestParams params = new RequestParams();
		params.put("pagesize", 12);
		params.put("pagenum", pagenum);
		isCanLoadData = false;
		HttpUtil.get(Urls.get_location_club, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					JSONArray datas = response.optJSONArray("data");
					if (datas != null && datas.length() > 0) {
						ArrayList<Club> tempList = new ArrayList<>();
						for (int i = 0; i < datas.length(); i++) {
							JSONObject dataJson = datas.optJSONObject(i);
							Club club = new Club();
							club.setDrivers(dataJson.optString("drivers"));
							club.setFans(dataJson.optString("fans"));
							club.setGuides(dataJson.optString("guides"));
							club.setIntroduce(dataJson.optString("introduce"));
							club.setIsFocus(dataJson.optString("isFocus"));
							club.setLeaders(dataJson.optString("leaders"));
							club.setOrgId(dataJson.optString("orgId"));
							club.setOrgLogo(dataJson.optString("orgLogo"));
							club.setOrgName(dataJson.optString("orgName"));
							club.setProducts(dataJson.optString("products"));
							club.setUserId(dataJson.optString("userId"));
							tempList.add(club);
						}
						if (tempList.size() < pagesize) {
							isLoadDataEnd = true;
						} else {
							isLoadDataEnd = false;
							pagenum++;
						}
						notifyDataClubList(tempList);
					} else {
						isLoadDataEnd = true;
					}
				} else {
					isLoadDataEnd = true;
				}
				isPullRefresh = false;
				isLoadmore = false;
			}
		});
	}

	private void notifyDataClubList(ArrayList<Club> clubs) {
		clubLists.addAll(clubs);
		isCanLoadData = true;
		clubListAdapter.notifyDataSetChanged();
	}

	private void findView() {
		pickedListView = (XListView) findViewById(R.id.main_org_listview);
		second_title_text = (TextView) findViewById(R.id.second_title_text);
		backLayout = (LinearLayout) findViewById(R.id.layout_back_button);
		pickedListView.setPullRefreshEnable(true);
		pickedListView.setPullLoadEnable(false);
		pickedListView.setXListViewListener(this);
		viewPager = (ViewPager) findViewById(R.id.carousel_vp);
		 loginLoading = (View)findViewById(R.id.login_loading);
		  loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
         loadingAnimation.start();

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isPullRefresh = true;
		pagenum = 1;
		// getClub();
		getClubInfo();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		// getClub();
		getClubInfo();
	}

	private void addHeaderView() {
		// 将xml布局文件生成view对象通过LayoutInflater
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 将view对象挂载到那个父元素上，这里没有就为null
		if (headerView == null) {
			headerView = inflater.inflate(R.layout.org_carousel_view, null);
		}
		viewPager = (ViewPager) headerView.findViewById(R.id.carousel_vp);
		dotsLayout = (LinearLayout) headerView.findViewById(R.id.carousel_dots);
		startFilter = (LinearLayout) headerView.findViewById(R.id.startFilter);
		all = (TextView) headerView.findViewById(R.id.all);
		all.setOnClickListener(this);
		startFilter.setOnClickListener(this);
		pickedListView.addHeaderView(headerView);
	}

	private void addCarousel(final List<PushResource> pushResourceList) {
		// count = tempList.size();
		imageViews = new ArrayList<View>();
		dots = new ArrayList<View>();
		if (dotsLayout != null) {
			dotsLayout.removeAllViews();
		}
		count = pushResourceList.size();
		for (int i = 0; i < count; i++) {
			PushResource resource = pushResourceList.get(i);
			// 初始化图片资源
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			// WindowManager wm =
			// (WindowManager)getActivity().getSystemService(WINDOW_SERVICE);
			// int width = wm.getDefaultDisplay().getWidth();
			if (!StringUtils.isEmpty(resource.getTitleImage())) {
				// TODO 如果主题有的话就放在oncreat里面
				// addHeaderView();
				ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(), imageView,
						AppConfig.options(R.drawable.default_photo));
			}
			imageViews.add(imageView);
			// TODO 添加标题下面的点********当滚动主题有时再放开下面代码******
			ImageView dotsView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 0, 5, 5);
			dotsView.setLayoutParams(params);
			dotsView.setBackgroundResource(R.drawable.banner);
			if (0 == i) {
				dotsView.setBackgroundResource(R.drawable.banner_icon);
			}
			dotsLayout.addView(dotsView);
			dots.add(dotsView);
		}

		// TODO 设置填充ViewPager页面的适配器********当滚动主题有时再放开下面代码******
		viewPager.setAdapter(new MyAdsPagerAdapter(this, imageViews, pushResourceList));
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		if (count > 0) {
			handler.postDelayed(new ScrollTask(), 3000);

		}
	}

	private void getPushCarousel() {
		RequestParams params = new RequestParams();
		params.put("pagesize", pagesize);
		params.put("pagenum", pagenum);
		params.put("mcode", "app.choice.org.pics");
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i("PickedFragment", "getPushCarousel", throwable.getMessage());

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				LogUtil.i("PickedFragment", "getPushCarousel", throwable.getMessage());

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				LogUtil.i("PickedFragment", "精选轮播图接口=", response.toString());
				String code = response.optString("code");
				if (code.equals("00000")) {
					JSONArray dataArray = response.optJSONArray("data");
					List<PushResource> pushResourceList = new ArrayList<PushResource>();
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject dataJson = dataArray.optJSONObject(i);
							PushResource pushResource = new PushResource();
							pushResource.setId(dataJson.optString("id"));
							pushResource.setResTitle(dataJson.optString("resTitle"));
							pushResource.setSubTitle(dataJson.optString("subTitle"));
							pushResource.setTitleImage(dataJson.optString("titleImage"));
							pushResource.setObjectType(dataJson.optString("objectType"));
							pushResource.setObjectId(dataJson.optString("objectId"));
							pushResource.setObject(dataJson.optString("object"));
							pushResourceList.add(pushResource);
							JSONObject userJson = dataJson.optJSONObject("object");
							if ("user".equals(dataJson.optString("objectType")) && userJson != null
									&& userJson.length() > 0) {
								pushResource.setObject(JsonUtil.getUserInfo(userJson));
							}

						}

						if (pushResourceList.size() > 0) {
							addHeaderView();
							addCarousel(pushResourceList);
						}
					}
				}
			}
		});
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author zhangbingkang
	 * @version [2013-6-18]
	 * @see [相关类/方法]
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.banner);
			dots.get(position).setBackgroundResource(R.drawable.banner_icon);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	// 定时器
	Handler handler = new Handler();
	private FilterLayout filterLayout;
	private TextView cancel;
	private TextView ok;
	private PopupWindow popupWindow;

	/**
	 * 换行切换任务
	 * 
	 * @author zhangbingkang
	 * @version [2013-6-18]
	 * @see [相关类/方法]
	 */
	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				viewPager.setCurrentItem(currentItem);
				// 通过Handler切换图片
				handler.postDelayed(this, 3000);
			}
		}
	}

	private void getClub() {
		RequestParams params = new RequestParams();
		params.put("pagesize", pagesize);
		params.put("pagenum", pagenum);
		// StringBuffer str=new StringBuffer();
		//// if(isFilterChange[0][0]!=0){
		//// str.append(clubTypeID.get(isFilterChange[0][1])).append(",");
		//// }
		//// if(isFilterChange[1][0]!=0){
		//// str.append(servAreaId.get(isFilterChange[1][1])).append(",");
		//// }
		//// if(isFilterChange[2][0]!=0){
		//// str.append(footprintId.get(isFilterChange[2][1])).append(",");
		//// }
		// if(tagIds.size()>0){
		// for(String tagId:tagIds){
		// str.append(tagId).append(",");
		// }
		// }
		// if(str.length()>0){
		// str.deleteCharAt(str.length()-1);
		// params.put("tagIds", str.toString());
		// }
		params.put("mcode", "app.choice.org");
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					pickedListView.stopLoadMore();
				}
				if (isPullRefresh) {
					pickedListView.stopRefresh();
				}
				if (pagenum != 1) {
					pagenum--;
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					pickedListView.stopLoadMore();
				}
				if (isPullRefresh) {
					pickedListView.stopRefresh();
				}
				if (pagenum != 1) {
					pagenum--;
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (!isLoadmore && !isPullRefresh) {
					clubList.clear();
				}
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					pickedListView.stopLoadMore();
				}
				String code = response.optString("code");
				if (code.equals("00000")) {

					if (isPullRefresh) {
						// 下拉刷新返回的
						pickedListView.successRefresh();
						clubList.clear();
					}
					JSONArray dataArray = response.optJSONArray("data");
					List<PushResource> pushResourceList = new ArrayList<PushResource>();
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject dataJson = dataArray.optJSONObject(i);
							// Club club = new Club();
							// club.setDrivers(dataJson.optString("drivers"));
							// club.setFans(dataJson.optString("fans"));
							// club.setGuides(dataJson.optString("guides"));
							// club.setIntroduce(dataJson.optString("introduce"));
							// club.setIsFocus(dataJson.optString("isFocus"));
							// club.setLeaders(dataJson.optString("leaders"));
							// club.setOrgId(dataJson.optString("orgId"));
							// club.setOrgLogo(dataJson.optString("orgLogo"));
							// club.setOrgName(dataJson.optString("orgName"));
							// club.setProducts(dataJson.optString("products"));
							// club.setUserId(dataJson.optString("userId"));
							String objectType = dataJson.optString("objectType");
							JSONObject userJson = dataJson.optJSONObject("object");
							if (objectType != null && objectType.equals("user") && userJson != null) {
								clubList.add(JsonUtil.getUserInfo(userJson));
							} else {

							}

						}

						if (dataArray.length() < 10) {
							pickedListView.setPullLoadEnable(false);
						} else {
							pagenum++;
							pickedListView.setPullLoadEnable(true);
						}
						if (clubList.size() > 0) {
							clubAdapter.notifyDataSetChanged();
						}

					}
				} else {
					if (isPullRefresh) {
						// 下拉刷新接口返回的数据不正确
						pickedListView.stopRefresh();
					}
					if (pagenum != 1) {
						pagenum--;
					}
				}
				isLoadmore = false;
				isPullRefresh = false;
			}
		});
	}

	private void getClubTags() {
		HttpUtil.get(Urls.get_club_tags, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if (code.equals("00000")) {
					JSONObject data = response.optJSONObject("data");
					JSONArray clubTypeArray = data.optJSONArray("club_type");
					if (clubTypeArray != null && clubTypeArray.length() > 0) {
						for (int i = 0; i < clubTypeArray.length(); i++) {
							JSONObject clubTypeJson = clubTypeArray.optJSONObject(i);
							clubTypeID.add(clubTypeJson.optString("tagId"));
							clubTypeName.add(clubTypeJson.optString("tagName"));
						}

					}
					JSONArray servAreaArray = data.optJSONArray("serv_area");
					if (servAreaArray != null && servAreaArray.length() > 0) {
						for (int i = 0; i < servAreaArray.length(); i++) {
							JSONObject servAreaJson = servAreaArray.optJSONObject(i);
							servAreaId.add(servAreaJson.optString("tagId"));
							servAreaName.add(servAreaJson.optString("tagName"));
						}

					}
					JSONArray footprintArray = data.optJSONArray("footprint");
					if (footprintArray != null && footprintArray.length() > 0) {
						for (int i = 0; i < footprintArray.length(); i++) {
							JSONObject footprintJson = footprintArray.optJSONObject(i);
							footprintId.add(footprintJson.optString("tagId"));
							footprintName.add(footprintJson.optString("tagName"));
						}

					}
					initPop();
				}
			}
		});
	}

	private void initPop() {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(R.layout.filter_pop_window, null);
		// 设置按钮的点击事件
		filterLayout = (FilterLayout) contentView.findViewById(R.id.filterLayout);
		filterLayout.setMutiChoice(true, false, false);
		cancel = (TextView) contentView.findViewById(R.id.cancel);
		ok = (TextView) contentView.findViewById(R.id.ok);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		if (clubTypeID.size() > 0) {
			filterLayout.setText1("服务类型");
			filterLayout.setDestinationValue(clubTypeName);
		} else {
			filterLayout.setDestinationLayoutGone();
		}
		if (servAreaId.size() > 0) {
			filterLayout.setText2("服务范围");
			filterLayout.setPriceValue(servAreaName);
		} else {
			filterLayout.setPriceLayoutGone();
		}
		if (footprintId.size() > 0) {
			filterLayout.setText3("服务区域");
			filterLayout.setDaysValue(footprintName);
		} else {
			filterLayout.setDaysLayoutGone();
		}

		// filterLayout.setOnDaysListener(new OnDaysListener() {
		//
		// @Override
		// public void OnCheck(String Value, int position,boolean state) {
		// // TODO Auto-generated method stub
		//// isFilterChange[2][0]=1;
		//// isFilterChange[2][1]=position;
		// if(state)
		// tagIds.add(footprintId.get(position));
		// else {
		// tagIds.remove(footprintId.get(position));
		// }
		// }
		// });
		// filterLayout.setOnDestinationListener(new OnDestinationListener() {
		//
		// @Override
		// public void OnCheck(String Value, int position,boolean state) {
		// // TODO Auto-generated method stub
		//// isFilterChange[0][0]=1;
		//// isFilterChange[0][1]=position;
		//
		// if(state)
		// tagIds.add(clubTypeID.get(position));
		// else {
		// tagIds.remove(clubTypeID.get(position));
		// }
		// }
		// });
		//
		// filterLayout.setonPriceListener(new OnPriceListener() {
		//
		// @Override
		// public void OnCheck(String Value, int position,boolean state) {
		// // TODO Auto-generated method stub
		//// isFilterChange[1][0]=1;
		//// isFilterChange[1][1]=position;
		//
		//
		// if(state)
		// tagIds.add(servAreaId.get(position));
		// else {
		// tagIds.remove(servAreaId.get(position));
		// }
		// }
		// });

		// button.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(mContext, "button is pressed",
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				backgroundAlpha(1f);
			}
		});
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}

		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		// popupWindow.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.transparent));
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startFilter:
			// 设置好参数之后再show
			int[] location = new int[2];
			backgroundAlpha(0.5f);
			popupWindow.showAtLocation(startFilter, Gravity.RIGHT, 0, 0);
			break;
		case R.id.ok:
			// if(isFilterChange[0][0]!=0||isFilterChange[1][0]!=0||isFilterChange[2][0]!=0){
			tagIds.clear();
			filterLayout.addTextValue(tagIds, clubTypeID, servAreaId, footprintId);
			if (tagIds.size() > 0) {
				clubList.clear();
				pagenum = 1;
				getClub();
			}
			popupWindow.dismiss();
			break;
		case R.id.cancel:

			popupWindow.dismiss();
			break;
		case R.id.all:
			clubList.clear();
			pagenum = 1;
			// isFilterChange[0][0]=0;
			// isFilterChange[1][0]=0;
			// isFilterChange[2][0]=0;
			tagIds.clear();
			getClub();
			break;
		case R.id.org_button:
			Intent it = new Intent(this, ContentActivity.class);
			it.putExtra("title", "发现-俱乐部聚合");
			it.putExtra("path", Urls.club);
			startActivity(it);
			break;
		}
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
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

						JSONObject userObj = data.optJSONObject("userInfo");
						if (userObj != null && userObj.length() > 0) {
							UserInfo userInfo = new UserInfo();
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
							Intent intent = new Intent(OrgListActivity.this, ClubFirstPageActivity.class);
							intent.putExtra("userInfo", userInfo);
							startActivity(intent);
						}

					}
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
	
	
	private void getPushResourceList() {
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
				JsonUtil.getPushResourceList(response,orgList);
				loadingAnimation.stop();
				loginLoading.setVisibility(View.GONE);
				if (orgList != null) {
					orgGridView = (MyGridView) findViewById(R.id.org_gridView);
					mOrgGridViewAdapter = new SubjectGridAdapter(OrgListActivity.this, orgList);
					orgGridView.setAdapter(mOrgGridViewAdapter);
					orgGridView.setOnItemClickListener(OrgListActivity.this);
					mOrgGridViewAdapter.notifyDataSetChanged();
				}
				getClubInfo();
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
}
