package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyOrdersListAdapter;
import com.bcinfo.tripaway.adapter.MyOrdersListAdapter.OnOrderListener;
import com.bcinfo.tripaway.bean.MyOrder;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.ComListViewFooter;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 我的旅程
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月9日 下午3:37:09
 */
public class MyJourneyActivity extends BaseActivity implements OnItemClickListener, IXListViewListener {
	private static final String TAG = "MyJourneyFragment";

	/**
	 * 我的订单列表
	 */
	private XListView myOrdersListview;

	/**
	 * 我的订单列表数据
	 */
	private ArrayList<MyOrder> myOrdersArrayList = new ArrayList<MyOrder>();

	private MyOrdersListAdapter mMyOrdersListAdapter;

	private ComListViewFooter mComListViewFooter;

	private int pageNum = 1;

	private String pageSize = "10";

	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private LinearLayout no_trip;

	// 旅程状态
	private String state;

	private RelativeLayout secondLayout;

	private View loginLoading;

	private AnimationDrawable loadingAnimation;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.my_journey_fragment);
		String title = getIntent().getStringExtra("title");
		if (title == null)
			setSecondTitle("我的旅程");
		else {
			setSecondTitle(title);
		}
		statisticsTitle = "我的旅程";
		loginLoading = (View) findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		myOrdersListview = (XListView) findViewById(R.id.my_orders_listview);
		secondLayout = (RelativeLayout) findViewById(R.id.second_title);
		secondLayout.getBackground().setAlpha(255);
		no_trip = (LinearLayout) findViewById(R.id.no_trip);
		no_trip.setVisibility(View.GONE);
		myOrdersListview.setPullLoadEnable(false);
		myOrdersListview.setPullRefreshEnable(true);
		myOrdersListview.setXListViewListener(this);
		state = getIntent().getStringExtra("state");
		initListView();
		// queryMyJourney(state);
		registerBroadReceiver();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pageNum = 1;
		myOrdersArrayList.clear();
		mMyOrdersListAdapter.notifyDataSetChanged();
		queryMyJourney(state);
	}

	/*
	 * 我的旅程
	 */
	private void queryMyJourney(String state) {
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);// 当前页码
		params.put("pagesize", pageSize);// 页码数
		params.put("state", state);
		HttpUtil.get(Urls.mytrip_url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				loadingAnimation.stop();
				loginLoading.setVisibility(View.GONE);
				LogUtil.i(TAG, "queryMyJourney", "response=" + response.toString());
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					myOrdersListview.stopLoadMore();
				}
				if (response.optString("code").equals("00000")) {
					if (isPullRefresh) {
						// 下拉刷新返回的
						myOrdersListview.successRefresh();
					}
					JSONArray dataList = response.optJSONArray("data");
					if (dataList == null || dataList.length() == 0) {
						myOrdersArrayList.clear();
						updateListAdapter(new ArrayList<MyOrder>());
						return;
					}
					ArrayList<MyOrder> myOrders = new ArrayList<MyOrder>();
					// myOrdersArrayList.clear();
					for (int i = 0; i < dataList.length(); i++) {
						JSONObject myOrderJson = dataList.optJSONObject(i);
						MyOrder myOrder = new MyOrder();
						myOrder.setId(myOrderJson.optString("id"));
						// myOrder.setRefundPolicy(myOrderJson
						// .optString("refundPolicy"));
						myOrder.setPrice(myOrderJson.optString("price"));
						myOrder.setOriginalPrice(myOrderJson.optString("originalTotal"));
						myOrder.setTotalPrice(myOrderJson.optString("totalPrice"));
						myOrder.setStatus(myOrderJson.optString("status"));
						myOrder.setNo(myOrderJson.optString("no"));
						myOrder.setEndDate(myOrderJson.optString("endDate"));
						myOrder.setBeginDate(myOrderJson.optString("beginDate"));
						myOrder.setLeaveword(myOrderJson.optString("leaveword"));
						myOrder.setAmount(myOrderJson.optString("amount"));
						myOrder.setStatements(myOrderJson.optString("statements"));
						myOrder.setFinalPayment(myOrderJson.optString("finalPayment"));
						// add by lij 2015/10/13 start
						myOrder.setAppraiseBtn(myOrderJson.optString("appraiseButton"));
						myOrder.setDeleteBtn(myOrderJson.optString("deleteButton"));
						myOrder.setCreateTime(myOrderJson.optString("createTime"));
						myOrder.setIs_instal(myOrderJson.optString("is_instal"));
						// add by lij 2015/10/13 end
						JSONObject productJson = myOrderJson.optJSONObject("product");
						if (productJson != null && !productJson.equals("")) {
							ProductNewInfo productNewInfo = new ProductNewInfo();
							JSONArray topicJsonArray = productJson.optJSONArray("topics");
							if (topicJsonArray != null && !topicJsonArray.equals("")) {
								ArrayList<String> topics = new ArrayList<String>();
								for (int j = 0; j < topicJsonArray.length(); j++) {
									topics.add(topicJsonArray.optString(j));
								}
								productNewInfo.setTopics(topics);
							}
							productNewInfo.setExpStart(productJson.optString("expStart"));
							productNewInfo.setExpend(productJson.optString("expend"));
							JSONObject userJSON = productJson.optJSONObject("creater");
							if (userJSON != null && !userJSON.toString().equals("")) {
								UserInfo userInfo = new UserInfo();
								userInfo.setGender(userJSON.optString("sex"));
								userInfo.setAddress(userJSON.optString("address"));
								userInfo.setStatus(userJSON.optString("status"));
								userInfo.setEmail(userJSON.optString("email"));
								userInfo.setNickname(userJSON.optString("nickName"));
								userInfo.setUserId(userJSON.optString("userId"));
								userInfo.setRole(userJSON.optString("role"));
								userInfo.setPermission(userJSON.optString("permission"));
								userInfo.setAvatar(userJSON.optString("avatar"));
								userInfo.setIntroduction(userJSON.optString("introduction"));
								userInfo.setMobile(userJSON.optString("mobile"));
								JSONArray tagsJsonArray = userJSON.optJSONArray("tags");
								if (tagsJsonArray != null && tagsJsonArray.length() > 0) {
									ArrayList<String> tags = new ArrayList<String>();
									for (int j = 0; j < tagsJsonArray.length(); j++) {
										tags.add(tagsJsonArray.optString(j));
									}
									userInfo.setTags(tags);
								}
								productNewInfo.setUser(userInfo);
							}
							productNewInfo.setId(productJson.optString("id"));
							productNewInfo.setDistance(productJson.optString("distance"));
							productNewInfo.setTitle(productJson.optString("title"));
							productNewInfo.setPoiCount(productJson.optString("poiCount"));
							productNewInfo.setPrice(productJson.optString("price"));
							productNewInfo.setDays(productJson.optString("days"));
							productNewInfo.setDescription(productJson.optString("description"));
							productNewInfo.setPriceMax(productJson.optString("priceMax"));
							productNewInfo.setTitleImg(productJson.optString("titleImg"));
							productNewInfo.setMaxMember(productJson.optString("maxMember"));
							productNewInfo.setProductType(productJson.optString("productType"));
							productNewInfo.setServices(productJson.optString("serviceCodes"));
							productNewInfo.setProductCode(productJson.optString("productCode"));
							JSONObject policy = productJson.optJSONObject("policy");
							if (null != policy) {
								myOrder.setRefundPolicy(policy.optString("type"));
							}
							myOrder.setProduct(productNewInfo);
						}
						myOrders.add(myOrder);
					}
					if (myOrders.size() < 10) {
						myOrdersListview.setPullLoadEnable(false);
					} else {
						pageNum++;
						myOrdersListview.setPullLoadEnable(true);
					}
					updateListAdapter(myOrders);
					// myOrdersArrayList.addAll(myOrders);
					// mMyOrdersListAdapter.notifyDataSetChanged();
				} else {
					// ToastUtil.showToast(getApplicationContext(), "errorCode="
					// + response.optString("code"));
					if (isPullRefresh) {
						// 下拉刷新接口返回的数据不正确
						myOrdersListview.stopRefresh();
					}
					if (pageNum != 1) {
						pageNum--;
					}
					no_trip.setVisibility(View.VISIBLE);
					myOrdersListview.setVisibility(View.GONE);
				}
				// 上拉 下拉的初始状态置为false;
				isLoadmore = false;
				isPullRefresh = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				System.out.println(errorResponse);
				// ToastUtil.showToast(getApplicationContext(), "statusCode=" +
				// statusCode);
				if (isLoadmore) {
					myOrdersListview.stopLoadMore();
				}
				if (isPullRefresh) {
					myOrdersListview.stopRefresh();
				}
				isPullRefresh = false;
				isLoadmore = false;
				if (pageNum != 1) {
					pageNum--;
				}
				no_trip.setVisibility(View.VISIBLE);
				myOrdersListview.setVisibility(View.GONE);
			}
		});
	}

	private void updateListAdapter(ArrayList<MyOrder> myOrders) {

		myOrdersArrayList.addAll(myOrders);
		mMyOrdersListAdapter.notifyDataSetChanged();
		// 没有旅程订单显示的页面
		if (myOrdersArrayList.size() <= 0) {
			no_trip.setVisibility(View.VISIBLE);
			myOrdersListview.setVisibility(View.GONE);
		} else {
			no_trip.setVisibility(View.GONE);
			myOrdersListview.setVisibility(View.VISIBLE);
		}
	}

	private void initListView() {
		if (!AppInfo.getIsLogin()) {
			goLoginActivity();
			return;
		}
		mMyOrdersListAdapter = new MyOrdersListAdapter(MyJourneyActivity.this, myOrdersArrayList,
				new OnOrderListener() {

					@Override
					public void appariseOrder(String content, int descScore, int serviceScore, int satisScore,
							String productId) {
						evaProductComments(content, descScore, serviceScore, satisScore, productId);
					}

					@Override
					public void delOrder(String orderId) {
						final String id = orderId;
						new AlertDialog.Builder(MyJourneyActivity.this).setMessage("确认删除吗？")
								.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								delOrderById(id);
							}
						}).setNegativeButton("取消", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).create().show();
					}
				});
		myOrdersListview.setAdapter(mMyOrdersListAdapter);

		myOrdersListview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == parent.getAdapter().getCount() - 1) {
			return;
		}
		LogUtil.d(TAG, "onItemClick", "position=" + position);
		// TODO Auto-generated method stub
		MyOrder myOrder = (MyOrder) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this, MyOrderDetailActivity.class);
		intent.putExtra("orderId", myOrder.getId());
		startActivity(intent);
		this.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isPullRefresh = true;
		pageNum = 1;
		myOrdersArrayList.clear();
		queryMyJourney(state);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		queryMyJourney(state);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		queryMyJourney(state);
	}

	private void evaProductComments(String content, int descScore, int serviceScore, int satisScore, String productId) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 评论内容
			jsonObject.put("content", content);
			// 产品id
			jsonObject.put("orderId", productId);
			// 评分
			jsonObject.put("descScore", descScore);
			jsonObject.put("serviceScore", serviceScore);
			jsonObject.put("satisfactionScore", satisScore);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.product_detail_starcomment, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if (code.equals("00000")) {
						pageNum = 1;
						myOrdersArrayList.clear();
						queryMyJourney(state);
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
	}

	private void delOrderById(String orderId) {
		// 评论内容
		HttpUtil.delete(Urls.order_delete + orderId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				pageNum = 1;
				myOrdersArrayList.clear();
				queryMyJourney(state);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i(TAG, "delete_order_responseString", responseString);
				LogUtil.i(TAG, "delete_order_statusCode", statusCode + "");
			}
		});
	}

	private void registerBroadReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.delMyOrder");
		myIntentFilter.addAction("com.bcinfo.cancelOrder");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if ("com.bcinfo.delMyOrder".equals(action) || "com.bcinfo.cancelOrder".equals(action)) {
				pageNum = 1;
				myOrdersArrayList.clear();
				queryMyJourney(state);
			}
		}
	};
}
