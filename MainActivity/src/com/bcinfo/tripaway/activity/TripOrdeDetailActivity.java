package com.bcinfo.tripaway.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AreaAdapter;
import com.bcinfo.tripaway.adapter.ClubInfoAdapter;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.textview.NormalTfTextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TripOrdeDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private TextView titleTv;
	private LinearLayout iv;
	private ImageView productBg;
	private TextView productCode;
	private TextView productPrice;
	private TextView productTitle;
	private TextView startDate;
	private TextView endDate;
	private TextView startWeek;
	private TextView endWeek;
	private TextView tripNum;
	private ListView clubMeb;
	private TextView routeDay;
	private TextView tripRoute;
	private WebView remark;
	protected String TAG = "TripOrdeDetailActivity";

	private String arrangements[];
	private ProductNewInfo productInfo;
	private ImageView status_imageview;
	private List<UserInfo> users = new ArrayList<UserInfo>();
	private TextView eventCommitButton;
	private TextView clubMebTip;
	private String id;

	private NormalTfTextView normalTextView;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.trip_order_detail);
		id = getIntent().getStringExtra("id");
		statisticsTitle="出行单详情";
		findView();
		setSecondTitle("出行单详情");
		if (id != null)
			getItineraryDetail(id);
	}

	private void findView() {
		normalTextView = (NormalTfTextView) findViewById(R.id.normalTextView);
		titleTv = (TextView) findViewById(R.id.second_title_text);
		iv = (LinearLayout) findViewById(R.id.layout_back_button);
		titleTv.setText(R.string.setting_aboutUs_title);
		((View) titleTv.getParent()).setBackgroundColor(getResources()
				.getColor(R.color.title_bg));
		((View) titleTv.getParent()).getBackground().setAlpha(255);
		iv.setOnClickListener(mOnClickListener);
		productBg = (ImageView) findViewById(R.id.product_bg);
		((View) productBg.getParent()).setOnClickListener(this);
		status_imageview = (ImageView) findViewById(R.id.status_imageview);
		productTitle = (TextView) findViewById(R.id.product_title);
		productCode = (TextView) findViewById(R.id.product_code);
		productPrice = (TextView) findViewById(R.id.product_price);
		startDate = (TextView) findViewById(R.id.start_date);
		endDate = (TextView) findViewById(R.id.end_date);
		startWeek = (TextView) findViewById(R.id.start_week);
		endWeek = (TextView) findViewById(R.id.end_week);
		tripNum = (TextView) findViewById(R.id.trip_num);
		clubMebTip = (TextView) findViewById(R.id.clubmeb_tip);
		clubMeb = (ListView) findViewById(R.id.clubmeb_listview);
		clubMeb.setOnItemClickListener(this);
		routeDay = (TextView) findViewById(R.id.route_day);
		tripRoute = (TextView) findViewById(R.id.trip_route);
		remark = (WebView) findViewById(R.id.remark);
		eventCommitButton = (TextView) findViewById(R.id.event_commit_button);
		eventCommitButton.setText("删除");
		eventCommitButton.setOnClickListener(this);
	}

	private void delItinerary() {
		RequestParams params = new RequestParams();
		params.put("itineraryId", id);
		HttpUtil.delete(Urls.get_itinerary, params,
				new JsonHttpResponseHandler() {
					// HttpUtil.get(Urls.push_flash_url, new
					// JsonHttpResponseHandler()
					// {
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if (code.equals("00000")) {
							ToastUtil.showToast(getBaseContext(), "删除成功");
							Intent it = new Intent();
							it.setAction("com.bcinfo.travelOrder");
							sendBroadcast(it);
							finish();
						}

					}
				});
	}

	private void getItineraryDetail(String id) {
		HttpUtil.get(Urls.get_itinerary_detail + id,
				new JsonHttpResponseHandler() {
					// HttpUtil.get(Urls.push_flash_url, new
					// JsonHttpResponseHandler()
					// {
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if (code.equals("00000")) {
							JSONObject data = response.optJSONObject("data");
							if (data != null && data.length() > 0) {
								JSONObject productJson = data
										.optJSONObject("product");
								productInfo = JsonUtil
										.getProductNewInfo(productJson);
								if (productInfo != null) {
									if (!StringUtils.isEmpty(productInfo
											.getTitleImg())) {
										ImageLoader
												.getInstance()
												.displayImage(
														Urls.imgHost
																+ productInfo
																		.getTitleImg(),
														productBg,
														AppConfig
																.options(R.drawable.ic_launcher));
									}
									productTitle.setText(productInfo.getTitle());
									if (!StringUtils.isEmpty(productInfo
											.getProductCode())) {
										productCode.setText("产品编号："
												+ productInfo.getProductCode());
									}
									productPrice.setText("￥"
											+ productInfo.getPrice() + "/人");
								}
								String start = data.optString("beginDate");
								if (!StringUtils.isEmpty(start)) {
									start = DateUtil.formateDateToTime1(start,
											"yyyy-MM-dd");
									startDate.setText(start);
									startWeek.setText(DateUtil.getWeek(start));
								}
								String end = data.optString("endDate");
								if (!StringUtils.isEmpty(end)) {
									end = DateUtil.formateDateToTime1(end,
											"yyyy-MM-dd");
									endDate.setText(end);
									endWeek.setText(DateUtil.getWeek(end));
								}
								String status = data.optString("status");
								if (!StringUtils.isEmpty(status)) {
									if (status.equals("init")) {
										// status_imageview
										// .setImageResource(R.drawable.notrip);
										clubMebTip.setText("接单人");
									} else if (status.equals("wait")) {
										status_imageview
												.setImageResource(R.drawable.notrip);
										clubMebTip.setText("接单人");
									} else if (status.equals("process")) {
										status_imageview
												.setImageResource(R.drawable.triping);
										clubMebTip.setText("接单人");
									} else if (status.equals("end")) {
										status_imageview
												.setImageResource(R.drawable.triped);
										eventCommitButton
												.setVisibility(View.VISIBLE);
										clubMebTip.setText("接单人");
									} else if (status.equals("delete")) {
										// status_imageview
										// .setImageResource(R.drawable.notrip);
										clubMebTip.setText("接单人");
									} else if (status.equals("cancel")) {
										// status_imageview
										// .setImageResource(R.drawable.notrip);
										clubMebTip.setText("接单人");
									}
								}
								if (!StringUtils.isEmpty(data
										.optString("partnerNum"))) {
									tripNum.setText("出行人数："
											+ data.optString("partnerNum")
											+ "人");
								}
								JSONArray servArray = data
										.optJSONArray("servs");
								if (servArray != null && servArray.length() > 0) {
									for (int i = 0; i < servArray.length(); i++) {
										JSONObject userObj = servArray
												.optJSONObject(i)
												.optJSONObject("user");
										int dispatchStatus = servArray
												.optJSONObject(i).optInt(
														"dispatchStatus");
										if (dispatchStatus == 1) {
											users.add(JsonUtil
													.getSimpleUserInfo(userObj));
										}
									}
									ClubInfoAdapter clubInfoAdapter = new ClubInfoAdapter(
											TripOrdeDetailActivity.this, users);
									clubMeb.setAdapter(clubInfoAdapter);
								}
								JSONArray arrangementArray = data
										.optJSONArray("arrangements");
								if (arrangementArray != null
										&& arrangementArray.length() > 0) {
									StringBuffer sb = new StringBuffer();
									for (int i = 0; i < arrangementArray
											.length(); i++) {
										sb.append(arrangementArray.optString(i)
												.trim());
										if (i != arrangementArray.length() - 1) {
											sb.append("\n");
										}
									}
									tripRoute.setText(sb.toString());
									routeDay.setText("行程路线（共"
											+ arrangementArray.length() + "天）");
								}
								if (!StringUtils.isEmpty(data
										.optString("remark"))) {
									remark.loadData(data.optString("remark"),
											"text/html; charset=UTF-8", null);
									normalTextView.setVisibility(View.VISIBLE);
								} else {
									normalTextView.setVisibility(View.GONE);
								}
							}
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.product_layout:
			ActivityUtil.toProductHomePage(productInfo, this);
			break;
		case R.id.event_commit_button:
			new AlertDialog.Builder(this)
					.setTitle("确认删除吗？")
					.setPositiveButton(
							"确认",
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									delItinerary();
									dialog.dismiss();
								}
							})
					.setNegativeButton(
							"取消",
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.clubmeb_listview:
			ActivityUtil.toPersonHomePage(users.get(position), this);
			break;
		}
	}
}
