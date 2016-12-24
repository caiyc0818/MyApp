package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyAdsPagerAdapter;
import com.bcinfo.tripaway.adapter.MyCollectionAdapter;
import com.bcinfo.tripaway.adapter.PickListAdapter;
import com.bcinfo.tripaway.adapter.MyCollectionAdapter.OperationListener;
import com.bcinfo.tripaway.bean.MyOrder;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 旅游产品精选fragment
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2014年12月17日 14:11:22
 * 
 */
public class MyCollectionActivity extends BaseActivity implements IXListViewListener {

	/**
	 * An ExecutorService that can schedule commands to run after a given delay,
	 * or to execute periodically.
	 */
	// private ScheduledExecutorService scheduledExecutorService;

	/**
	 * 精选数据的适配器
	 */
	private MyCollectionAdapter myCollectionAdapter;

	/**
	 * 显示 精选结果数据的listview
	 */
	private XListView pickedListView;

	/**
	 * 定义 存放 精选数据的list集合
	 * 
	 */
	private List<ProductNewInfo> myCollectionItemList = new ArrayList<ProductNewInfo>();

	private int pageNum = 1;

	private String pageSize = "10";

	private RelativeLayout secondLayout;

	private LinearLayout backBtn;

	private LinearLayout no_trip;

	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_collection_activity);
		setSecondTitle("我的收藏");
		statisticsTitle = "我的收藏";
		secondLayout = (RelativeLayout) findViewById(R.id.my_collection_second_title);
		secondLayout.getBackground().setAlpha(255);
		backBtn = (LinearLayout) findViewById(R.id.layout_back_button);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				activityAnimationClose();
			}
		});
		// 获得listview
		pickedListView = (XListView) findViewById(R.id.my_collection_listview);
		no_trip = (LinearLayout) findViewById(R.id.no_trip);
		pickedListView.setPullRefreshEnable(true);
		pickedListView.setPullLoadEnable(false);
		pickedListView.setXListViewListener(this);
		myCollectionAdapter = new MyCollectionAdapter(myCollectionItemList, MyCollectionActivity.this,
				new OperationListener() {
					@Override
					public void addOrCancelStored(String productId, boolean flag, int position) {
						if (!AppInfo.getIsLogin()) {
							ToastUtil.showToast(getApplicationContext(), "未登录不能收藏");
							return;
						}
						// int firstPosition =
						// pickedListView.getFirstVisiblePosition();
						// int lastPosition =
						// pickedListView.getLastVisiblePosition();
						// if(position >=
						// firstPosition&&position<=lastPosition){
						// View view = pickedListView.getChildAt(position -
						// firstPosition);
						// ImageView img =(ImageView)
						// view.findViewById(R.id.if_stored);
						// if(flag){
						// img.setImageResource(R.drawable.no_store);
						// }else{
						// img.setImageResource(R.drawable.yes_store);
						// }
						// }
						if (myCollectionItemList != null && myCollectionItemList.size() > 0) {
							if (flag) {
								myCollectionItemList.remove(position);
							}
						}
						if (myCollectionItemList.size() == 0) {
							no_trip.setVisibility(View.VISIBLE);
							pickedListView.setVisibility(View.GONE);
						}
						storeProductByIsFaved(flag, productId);
						Intent intent = new Intent("com.bcinfo.pickListRefresh");
						intent.putExtra("flag", false);
						intent.putExtra("productId", productId);
						sendBroadcast(intent);
						myCollectionAdapter.notifyDataSetChanged();

					}
				});
		// 设置listview的适配器
		pickedListView.setAdapter(myCollectionAdapter);
		myCollectionAdapter.notifyDataSetChanged();
		no_trip.setVisibility(View.GONE);
		testPickedUrl();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		secondLayout.getBackground().setAlpha(255);
	}

	/*
	 * 测试 精选推荐 接口
	 */
	private void testPickedUrl() {
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);// 当前页码
		params.put("pagesize", pageSize);// 页码数
		HttpUtil.get(Urls.my_collection, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					pickedListView.stopLoadMore();
				}
				String code = response.optString("code");
				if (code.equals("00000")) {
					initPickedItemList(response);
				} else {
					if (isPullRefresh) {
						// 下拉刷新接口返回的数据不正确
						pickedListView.stopRefresh();
					}
					if (pageNum != 1) {
						pageNum--;
					}
				}
				isLoadmore = false;
				isPullRefresh = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (isLoadmore) {
					pickedListView.stopLoadMore();
				}
				if (isPullRefresh) {
					pickedListView.stopRefresh();
				}
				isPullRefresh = false;
				isLoadmore = false;
				if (pageNum != 1) {
					pageNum--;
				}
			}

		});
	}

	/**
	 * init 精选item
	 */
	private void initPickedItemList(JSONObject jsonObj) {
		if (isPullRefresh) {
			pickedListView.successRefresh();
			if (myCollectionItemList.size() > 0) {
				myCollectionItemList.clear();
			}
		}
		JSONObject jsonObject1 = jsonObj.optJSONObject("data");
		JSONArray arrayObj = jsonObject1.optJSONArray("favorite");
		ArrayList<ProductNewInfo> myOrders = new ArrayList<ProductNewInfo>();
		if (arrayObj != null && arrayObj.length() != 0) {
			for (int i = 0; i < arrayObj.length(); i++) {
				JSONObject obj = arrayObj.optJSONObject(i);
				String objectType = obj.optString("objectType");
				if ("product".equals(objectType)) {
					ProductNewInfo proNewInfo = new ProductNewInfo();
					JSONObject productObj = obj.optJSONObject("object");
					if (productObj.optJSONObject("exts") != null) {
						HashMap<String, String> exts = new HashMap<String, String>();
						if (!StringUtils.isEmpty(productObj.optJSONObject("exts").optString("refer_tags"))) {
							exts.put("refer_tags", productObj.optJSONObject("exts").optString("refer_tags"));
						}
						if (!StringUtils.isEmpty(productObj.optJSONObject("exts").optString("big_refer_tags"))) {
							exts.put("big_refer_tags", productObj.optJSONObject("exts").optString("big_refer_tags"));
						}
						if (exts.size() > 0) {
							proNewInfo.setExts(exts);
						}
					}
					proNewInfo.setPv(productObj.optString("pv"));
					proNewInfo.setOriginalPrice(productObj.optString("originalPrice"));
					proNewInfo.setId(productObj.optString("id"));
					proNewInfo.setDistance(productObj.optString("distance"));
					JSONArray topicArray = (productObj.optJSONArray("topics"));
					for (int j = 0; j < topicArray.length(); j++) {
						proNewInfo.getTopics().add(topicArray.optString(j));
					}
					proNewInfo.setTitle(productObj.optString("title"));
					proNewInfo.setPoiCount(productObj.optString("poiCount"));
					proNewInfo.setPrice(productObj.optString("price"));
					proNewInfo.setDays((productObj.optString("days")));
					proNewInfo.setDescription(productObj.optString("description"));
					proNewInfo.setPriceMax(productObj.optString("priceMax"));
					proNewInfo.setTitleImg(productObj.optString("titleImg"));
					proNewInfo.setMaxMember(productObj.optString("maxMember"));
					proNewInfo.setProductType(productObj.optString("productType"));
					proNewInfo.setIsFav("yes");
					proNewInfo.setServices(productObj.optString("serviceCodes"));
					proNewInfo.setPriceFrequency(productObj.optString("priceFrequency"));
					// add by lij 2015/09/25 start 新增每个产品的参与人数
					proNewInfo.setMemberJoinCount(productObj.optString("servTimes"));
					// add by lij 2015/09/25 end
					// tags参数有问题 暂时不解析
					if (productObj.optJSONObject("creater") != null) {
						proNewInfo.getUser().setGender(productObj.optJSONObject("creater").optString("sex"));
						proNewInfo.getUser().setStatus(productObj.optJSONObject("creater").optString("status"));
						proNewInfo.getUser().setEmail(productObj.optJSONObject("creater").optString("email"));
						proNewInfo.getUser().setNickname(productObj.optJSONObject("creater").optString("nickName"));
						proNewInfo.getUser().setUserId(productObj.optJSONObject("creater").optString("userId"));
						proNewInfo.getUser().setAvatar(productObj.optJSONObject("creater").optString("avatar"));
						proNewInfo.getUser()
								.setIntroduction(productObj.optJSONObject("creater").optString("introduction"));
						proNewInfo.getUser().setMobile(productObj.optJSONObject("creater").optString("mobile"));
					}
					myOrders.add(proNewInfo);
				}
			}
		}
		if (myOrders.size() < 10) {
			pickedListView.setPullLoadEnable(false);
		} else {
			pageNum++;
			pickedListView.setPullLoadEnable(true);
		}
		updateListAdapter(myOrders);
	}

	private void updateListAdapter(ArrayList<ProductNewInfo> myOrders) {

		myCollectionItemList.addAll(myOrders);
		myCollectionAdapter.notifyDataSetChanged();
		// 没有旅程订单显示的页面
		if (myCollectionItemList.size() <= 0) {
			no_trip.setVisibility(View.VISIBLE);
			pickedListView.setVisibility(View.GONE);
		} else {
			no_trip.setVisibility(View.GONE);
			pickedListView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isPullRefresh = true;
		pageNum = 1;
		testPickedUrl();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		testPickedUrl();
	}

	protected void onDestroy() {
		super.onDestroy();
		// unregisterReceiver(mBroadcastReceiver);
	};

	private void storeProductByIsFaved(boolean flag, String productId) {
		RequestParams params = new RequestParams();
		params.put("type", "product");
		params.put("objectId", productId);
		// 删除收藏
		HttpUtil.delete(Urls.cancel_store, params, new JsonHttpResponseHandler() {
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
				// mStorePicIv.setImageResource(R.drawable.store_pics);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}
}