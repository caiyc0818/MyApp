package com.bcinfo.tripaway.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.CashCardAdapter;
import com.bcinfo.tripaway.adapter.OldPicAdapter.OnSelectPicListener;
import com.bcinfo.tripaway.bean.Cash;
import com.bcinfo.tripaway.bean.MySchedule;
import com.bcinfo.tripaway.bean.OldPic;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.bcinfo.tripaway.view.textview.NormalTfTextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 全部回应
 * 
 * @function
 * @author JiangCSS东西啊
 * @version 1.0, 2015年4月28日 下午8:19:17
 */

@SuppressLint("ResourceAsColor")
public class CashCardActivity extends BaseActivity
		implements OnClickListener, IXListViewListener, OnItemClickListener, OnSelectPicListener {
	protected static final String TAG = "AllResponceActivity";
	private XListView responceListView;
	private List<Cash> lists = new ArrayList<Cash>();;
	private RelativeLayout rla;
	private LinearLayout backLaout;
	RelativeLayout od;
	RelativeLayout td;
	RelativeLayout pd;
	private NormalTfTextView ysy;
	private NormalTfTextView wsy;
	private NormalTfTextView ygq;
	private View xj1;
	private View xj2;
	private View xj3;
	private int pageNum = 1;
	private boolean canContinue = false;
	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;
	private int pageSize = 10;
	private CashCardAdapter ad;

	private List<String> selectIdList = new ArrayList<String>();
	private TextView confirm;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;

	private String seriesId;
	/**
	 * 无现金券
	 */
	private LinearLayout no_cashcoupon;

	private RelativeLayout relative;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.cashcardlist);
		statisticsTitle="我的现金券";
		seriesId = getIntent().getStringExtra("seriesId");
		lists = new ArrayList<Cash>();
		OldPic old = new OldPic();
		old.setOldTime("");
		backLaout = (LinearLayout) findViewById(R.id.layout_back_button);
		ysy = (NormalTfTextView) findViewById(R.id.ys);
		wsy = (NormalTfTextView) findViewById(R.id.ws);
		ygq = (NormalTfTextView) findViewById(R.id.gq);
		xj1 = findViewById(R.id.xj1);
		xj2 = findViewById(R.id.xj2);
		xj3 = findViewById(R.id.xj3);

		od = (RelativeLayout) findViewById(R.id.od);
		td = (RelativeLayout) findViewById(R.id.td);
		pd = (RelativeLayout) findViewById(R.id.pd);
		// lists.clear();
		// TripStorySchema sche=new TripStorySchema();
		// sche.setDescription("jibberJabber");
		// sche.setPublishTime("20160318142047");
		// lists.add(sche);
		responceListView = (XListView) findViewById(R.id.select_dialog_listview);
		relative = (RelativeLayout) findViewById(R.id.relative);
		no_cashcoupon = (LinearLayout) findViewById(R.id.no_cashcoupon);
		no_cashcoupon.setVisibility(View.GONE);
		ad = new CashCardAdapter(this, lists);

		// ad.setOnSelectPicListener(this);
		//
		backLaout.setOnClickListener(this);
		pd.setOnClickListener(this);
		od.setOnClickListener(this);
		td.setOnClickListener(this);

		responceListView.setPullLoadEnable(true);
		responceListView.setPullRefreshEnable(false);
		responceListView.setXListViewListener(this);
		responceListView.setAdapter(ad);

		// responceListView.setOnItemClickListener(this);
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		getSchedulets();
	}

	protected void onDestroy() {
		super.onDestroy();

	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_back_button:
			this.finish();
			activityAnimationClose();
			break;
		case R.id.confirm:
			picToSerial(seriesId, selectIdList);
			break;
		case R.id.pd:
			if (!canContinue) {
				return;
			}
			canContinue = false;
			couponTab = "used";
			pageNum = 1;
			ysy.setTextColor(getResources().getColor(R.color.title_bar_bg));
			wsy.setTextColor(getResources().getColor(R.color.black));
			ygq.setTextColor(getResources().getColor(R.color.black));
			xj1.setVisibility(View.INVISIBLE);
			xj2.setVisibility(View.INVISIBLE);
			xj3.setVisibility(View.VISIBLE);
			lists.clear();
			ad.notifyDataSetChanged();
			getSchedulets();

			break;

		case R.id.od:
			if (!canContinue) {
				return;
			}
			canContinue = false;
			couponTab = "valid";
			pageNum = 1;
			wsy.setTextColor(getResources().getColor(R.color.title_bar_bg));
			ygq.setTextColor(getResources().getColor(R.color.black));
			ysy.setTextColor(getResources().getColor(R.color.black));
			xj2.setVisibility(View.INVISIBLE);
			xj1.setVisibility(View.VISIBLE);
			xj3.setVisibility(View.INVISIBLE);
			lists.clear();
			ad.notifyDataSetChanged();
			getSchedulets();
			break;
		case R.id.td:
			if (!canContinue) {
				return;
			}
			canContinue = false;
			couponTab = "expired";
			xj3.setVisibility(View.INVISIBLE);
			xj1.setVisibility(View.INVISIBLE);
			pageNum = 1;
			xj2.setVisibility(View.VISIBLE);
			ygq.setTextColor(getResources().getColor(R.color.title_bar_bg));
			ysy.setTextColor(getResources().getColor(R.color.black));
			wsy.setTextColor(getResources().getColor(R.color.black));
			lists.clear();
			ad.notifyDataSetChanged();
			getSchedulets();
			break;
		default:

			break;
		}

	}

	// 默认未使用
	private String couponTab = "valid";

	private void getSchedulets() {
		try {
			RequestParams params = new RequestParams();
			params.put("pagenum", pageNum);// 当前页码
			params.put("pagesize", pageSize);// 页码数
			params.put("couponTab", couponTab);// 券类型

			HttpUtil.get(Urls.cashs, params, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String responseString) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, responseString);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);

					ToastUtil.showToast(CashCardActivity.this, "throwable=" + throwable.getMessage());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					canContinue = true;
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					loadingAnimation.stop();
					loginLoading.setVisibility(View.GONE);
					if (isLoadmore) {
						// 上拉返回的结束加载更多布局
						responceListView.stopLoadMore();
						responceListView.stopRefresh();
					}

					LogUtil.i(TAG, "查询消息列表接口1", response.toString());
					String code = response.optString("code");

					if (code.equals("00000")) {

						JSONObject dataJsonSB = response.optJSONObject("data");

						String us = dataJsonSB.optString("used");
						String unUs = dataJsonSB.optString("unUsed");
						String expired = dataJsonSB.optString("expired");
						if ("0".equals(us)) {
							ysy.setText("已使用 ");
						} else {
							ysy.setText("已使用 (" + us + ")");
						}
						if ("0".equals(unUs)) {
							wsy.setText("未使用");
						} else {
							wsy.setText("未使用 (" + unUs + ")");
						}
						if ("0".equals(expired)) {
							ygq.setText("已过期");
						} else {
							ygq.setText("已过期 (" + expired + ")");
						}
						JSONArray dataJsonS = dataJsonSB.optJSONArray("coupons");
						if (dataJsonS != null && dataJsonS.length() > 0) {
							if (isPullRefresh) {
								responceListView.successRefresh();

							}

							List<Cash> queuesInfos = new ArrayList<Cash>();
							String deleteStr = PreferenceUtil.getHaveDeleteStr();
							LogUtil.i(TAG, "已经删除了的列表id", deleteStr);

							for (int i = 0; i < dataJsonS.length(); i++) {
								JSONObject tripStoryObj = dataJsonS.optJSONObject(i);
								// if
								// ("现金抵用券".equals(tripStoryObj.optString("couponTypeName")))
								// {
								Cash mche = new Cash();
								mche.setCouponCode(tripStoryObj.optString("couponCode"));
								mche.setCashMoney(tripStoryObj.optString("faceValue"));
								mche.setCashType(tripStoryObj.optString("couponTypeName"));
								mche.setCashDate(tripStoryObj.optString("expireDate"));
								mche.setCashClub(tripStoryObj.optString("couponName"));
								mche.setCashCon(tripStoryObj.optString("description"));
								mche.setDiscount(tripStoryObj.optString("discount"));
								mche.setCashStatus(couponTab);
								queuesInfos.add(mche);
								// }

							}
							lists.addAll(queuesInfos);
							if (lists.size() >= pageSize) {
								responceListView.setPullLoadEnable(true);
							} else {
								responceListView.setPullLoadEnable(false);
							}
							ad.notifyDataSetChanged();
							pageNum++;

						} else {
							isPullRefresh = false;
							responceListView.setPullLoadEnable(false);
							isLoadmore = false;
						}
						if (lists == null || lists.size() == 0) {
							no_cashcoupon.setVisibility(View.VISIBLE);
							relative.setVisibility(View.GONE);
						} else {
							no_cashcoupon.setVisibility(View.GONE);
							relative.setVisibility(View.VISIBLE);
						}

					} else {

						if (pageNum != 1) {
							pageNum--;
						}
					}

					if (!isLoadmore) {
						// 上拉返回的结束加载更多布局
						responceListView.stopLoadMore();

					}
					if (!isPullRefresh) {
						responceListView.stopRefresh();
					}

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

		// lists=getNewList(lists);
		isPullRefresh = true;
		if (!canContinue) {
			return;
		}
		canContinue = false;
		pageNum = 1;
		getSchedulets();

	}

	@Override
	public void onLoadMore() {

		if (!canContinue) {
			return;
		}
		canContinue = false;
		isLoadmore = true;
		getSchedulets();

	}

	public static List<MySchedule> removeDuplicate(List list) {
		HashSet<MySchedule> h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.select_dialog_listview:
			Intent mainIntent = new Intent(getBaseContext(), TripOrdeDetailActivity.class);
			// mainIntent.putExtra("id", lists.get(position-1).getId());
			startActivity(mainIntent);
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((View) backLaout.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
		((View) backLaout.getParent()).getBackground().setAlpha(255);
	}

	@Override
	public void addPic(String id) {
		// TODO Auto-generated method stub
		selectIdList.add(id);
	}

	@Override
	public void removePic(String id) {
		// TODO Auto-generated method stub
		selectIdList.remove(id);
	}

	private void picToSerial(String seriesId, List<String> photoIdList) {
		if (photoIdList.size() == 0)
			return;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("seriesId", seriesId);
			jsonObject.put("photoId", new JSONArray(photoIdList));
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpUtil.post(Urls.square_pic_to_serial, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {

						// testUnReadMsgUrl(myInfo);
						ToastUtil.showToast(getApplicationContext(), "添加成功");
						finish();
						activityAnimationClose();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					super.onFailure(statusCode, headers, responseString, throwable);
				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
