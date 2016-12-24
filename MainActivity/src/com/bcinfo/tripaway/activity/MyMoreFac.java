package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyAccountfasAdapter;
import com.bcinfo.tripaway.bean.MyAccFas;
import com.bcinfo.tripaway.bean.MyOrder;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 我的账户
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月23日 11:26:11
 */
public class MyMoreFac extends BaseActivity implements OnClickListener, IXListViewListener {

	private int pageNum = 1;

	private int pageSize = 10;

	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;
	private XListView myAccountListView;
	private ArrayList<MyAccFas> list;
	private MyAccountfasAdapter adapter;
	private LinearLayout backbackTv;
	private RelativeLayout secondLayout;
	private ImageView noimage;
	private View loginLoading;
	private AnimationDrawable loadingAnimation;

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
		setContentView(R.layout.activity_my_account2);
		setSecondTitle("账户明细");
		secondLayout = (RelativeLayout) findViewById(R.id.second_title);
		secondLayout.getBackground().setAlpha(255);
		AppManager.getAppManager().addActivity(this);
		noimage = (ImageView) findViewById(R.id.noimage);
		backbackTv = (LinearLayout) findViewById(R.id.layout_back_button);
		backbackTv.setOnClickListener(this);
		myAccountListView = (XListView) findViewById(R.id.myaccount_listview);
		myAccountListView.setPullLoadEnable(true);
		myAccountListView.setPullRefreshEnable(true);
		myAccountListView.setXListViewListener(this);
		list = new ArrayList<>();
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		adapter = new MyAccountfasAdapter(list, MyMoreFac.this);
		myAccountListView.setAdapter(adapter);
		testAccountListUrl();
//		如果是订单 有订单id就跳转
		myAccountListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				MyAccFas myOrder = (MyAccFas) parent.getAdapter().getItem(position);
				if (!StringUtils.isEmpty(myOrder.getOrderId())&&!"0".equals(myOrder.getOrderId())) {
					if (position == parent.getAdapter().getCount() - 1) {
						return;
					}
					// TODO Auto-generated method stub
					Intent intent = new Intent(MyMoreFac.this, MyOrderDetailActivity.class);
					intent.putExtra("orderId", myOrder.getOrderId());
					startActivity(intent);
					MyMoreFac.this.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			activityAnimationClose();
			break;
		default:
			break;
		}

	}

	private void testAccountListUrl() {
		RequestParams params = new RequestParams();
		params.put("facId", "0001");
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		HttpUtil.get(Urls.my_account_fas, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					myAccountListView.stopLoadMore();
				}
				if ("00000".equals(response.optString("code"))) {
					if (isPullRefresh) {
						// 下拉刷新返回的
						myAccountListView.successRefresh();
					}
					getAccountListInfo(response);
				} else if ("00099".equals(response.optString("code"))) {
					PreferenceUtil.delUserInfo();
					UserInfoDB.getInstance().deleteAll();
					goLoginActivity();
				} else {
					if (isPullRefresh) {
						// 下拉刷新接口返回的数据不正确
						myAccountListView.stopRefresh();
					}
					if (pageNum != 1) {
						pageNum--;
					}
				}
				// 上拉 下拉的初始状态置为false;
				isLoadmore = false;
				isPullRefresh = false;

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					myAccountListView.stopLoadMore();
				}
				if (isPullRefresh) {
					myAccountListView.stopRefresh();
				}
				if (pageNum != 1) {
					pageNum--;
				}
			}
		});

	}

	private void getAccountListInfo(JSONObject response) {

		JSONArray jsonArray = response.optJSONArray("data");

		ArrayList<MyAccFas> myaccount = new ArrayList<MyAccFas>();
		for (int i = 0; i < jsonArray.length(); i++) {
			MyAccFas myAccFas = new MyAccFas();
			myAccFas.setBuyer(jsonArray.optJSONObject(i).optString("buyer"));// 购买人
			myAccFas.setOrderId(jsonArray.optJSONObject(i).optString("orderId"));// 订单id
			myAccFas.setActionType(jsonArray.optJSONObject(i).optString("actionType"));// 购买类型
			myAccFas.setAmount(jsonArray.optJSONObject(i).optString("amount"));// 金额
			myAccFas.setDirection((jsonArray.optJSONObject(i).optString("direction")));// 判断正负
			myAccFas.setOrderNo((jsonArray.optJSONObject(i).optString("orderNo")));// 订单号
			myAccFas.setRecordTime((jsonArray.optJSONObject(i).optString("recordTime")));// 订单号
			myAccFas.setStatus((jsonArray.optJSONObject(i).optString("status")));// 订单号
			myAccFas.setTitle((jsonArray.optJSONObject(i).optString("title")));// 订单号
			myaccount.add(myAccFas);
		}
		if (isPullRefresh) {
			if (list.size() >= 0) {
				list.clear();
			}
		}
		if (myaccount.size() >= pageSize) {
			pageNum++;
			myAccountListView.setPullLoadEnable(true);
		} else {
			myAccountListView.setPullLoadEnable(false);
		}

		list.addAll(myaccount);
		if (list.size() > 0) {
			loadingAnimation.stop();
			loginLoading.setVisibility(View.GONE);
			myAccountListView.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
		if (list.size() <= 0) {
			loadingAnimation.stop();
			loginLoading.setVisibility(View.GONE);
			noimage.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (isPullRefresh) {
			return;
		}
		isPullRefresh = true;
		pageNum = 1;
		testAccountListUrl();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		testAccountListUrl();
	}

}
