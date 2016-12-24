package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyInvoiceAdapter;
import com.bcinfo.tripaway.adapter.MyInvoiceAdapter.OperationListener;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyInvoiceActivity extends BaseActivity
		implements IXListViewListener, OnClickListener, OnItemClickListener {

	private XListView mXlistView;

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

	private MyInvoiceAdapter mInvoiceAdapter;

	private LinearLayout no_trip;

	private ArrayList<PartnerInfo> partnerInfoList = new ArrayList<PartnerInfo>();

	private TextView mDelBtn;

	private TextView mModifyBtn;

	private RelativeLayout secondTitles;

	private ImageView mAddBtn;

	private LinearLayout backBtn;

	private boolean isOrderFrom = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_invoice);
		mXlistView = (XListView) findViewById(R.id.partner_listview);
		mXlistView.setPullRefreshEnable(true);
		mXlistView.setPullLoadEnable(false);
		mXlistView.setXListViewListener(this);

		initView();
		queryMyParteners(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isOrderFrom = getIntent().getBooleanExtra("isOrderFrom", false);
	}

	@Override
	protected void initView() {
		super.initView();
		no_trip = (LinearLayout) findViewById(R.id.no_trip);
		no_trip.setVisibility(View.GONE);
		mInvoiceAdapter = new MyInvoiceAdapter(getApplication(), partnerInfoList, new OperationListener() {
			@Override
			public void modifyInfo(PartnerInfo info, int position) {
				Intent it = new Intent(MyInvoiceActivity.this, MyPartenerAddActivity.class);
				it.putExtra("partner", info);
				it.putExtra("flag", true);
				it.putExtra("position", position);
				startActivityForResult(it, 0);
			}

			@Override
			public void delInfoById(String id, int position) {
				final String ids = id;
				final int pos = position;
				new AlertDialog.Builder(MyInvoiceActivity.this)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						delMyPartener(ids, pos);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				}).setTitle("是否删除?").show();
			}
		});
		mXlistView.setAdapter(mInvoiceAdapter);
		mInvoiceAdapter.notifyDataSetChanged();
		mDelBtn = (TextView) findViewById(R.id.button_delete);
		mModifyBtn = (TextView) findViewById(R.id.button_modify);
		mAddBtn = (ImageView) findViewById(R.id.add_partener);
		mAddBtn.setOnClickListener(this);
		secondTitles = (RelativeLayout) findViewById(R.id.second_titles);
		secondTitles.getBackground().setAlpha(255);
		backBtn = (LinearLayout) findViewById(R.id.layout_back_button);
		backBtn.setOnClickListener(this);
		mXlistView.setOnItemClickListener(this);
	}

	private void queryMyParteners(final boolean isAddNewData) {
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);// 当前页码
		params.put("pagesize", pageSize);// 页码数
		HttpUtil.get(Urls.my_partner, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					mXlistView.stopLoadMore();
				}
				if (response.optString("code").equals("00000")) {
					if (isPullRefresh) {
						// 下拉刷新返回的
						mXlistView.successRefresh();
					}
					JSONObject partnerData = response.optJSONObject("data");
					JSONArray dataList = partnerData.optJSONArray("partners");

					if (dataList == null || dataList.length() == 0) {
						updateListAdapter(new ArrayList<PartnerInfo>(), isAddNewData);
						return;
					}
					ArrayList<PartnerInfo> myPartner = new ArrayList<PartnerInfo>();
					// myOrdersArrayList.clear();
					for (int i = 0; i < dataList.length(); i++) {
						JSONObject myOrderJson = dataList.optJSONObject(i);
						PartnerInfo info = new PartnerInfo();
						info.setId(myOrderJson.optString("id"));

						// 是否添加
						boolean isAdd = false;
						for (PartnerInfo pinfo : partnerInfoList) {
							if (pinfo.getId().toString().equals(info.getId().toString())) {
								isAdd = true;
								break;
							}
						}
						// 已存在，则不添加
						if (isAdd) {
							continue;
						}

						info.setRealName(myOrderJson.optString("realName"));
						info.setPassportNo(myOrderJson.optString("passportNo"));
						info.setIdNo(myOrderJson.optString("idNo"));
						info.setEmail(myOrderJson.optString("email"));
						info.setTel(myOrderJson.optString("tel"));
						myPartner.add(info);
					}
					if (myPartner.size() < 10) {
						mXlistView.setPullLoadEnable(false);
					} else {
						pageNum++;
						mXlistView.setPullLoadEnable(true);
					}
					updateListAdapter(myPartner, isAddNewData);
					// myOrdersArrayList.addAll(myOrders);
					// mMyOrdersListAdapter.notifyDataSetChanged();
				} else {
					// ToastUtil.showToast(getApplicationContext(), "errorCode="
					// + response.optString("code"));
					if (isPullRefresh) {
						// 下拉刷新接口返回的数据不正确
						mXlistView.stopRefresh();
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
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				System.out.println(errorResponse);
				// ToastUtil.showToast(getApplicationContext(), "statusCode=" +
				// statusCode);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					mXlistView.stopLoadMore();
				}
				if (isPullRefresh) {
					mXlistView.stopRefresh();
				}
				if (pageNum != 1) {
					pageNum--;
				}
			}
		});
	}

	private void updateListAdapter(ArrayList<PartnerInfo> myOrders, final boolean isAddNewData) {

		partnerInfoList.addAll(myOrders);
		mInvoiceAdapter.notifyDataSetChanged();
		//定位到最新数据
		if (!isLoadmore && !isPullRefresh && isAddNewData) {
			mXlistView.setSelection(partnerInfoList.size() - 1);
		}
		// 没有旅程订单显示的页面
		if (partnerInfoList.size() <= 0) {
			no_trip.setVisibility(View.VISIBLE);
			mXlistView.setVisibility(View.GONE);
		} else {
			no_trip.setVisibility(View.GONE);
			mXlistView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRefresh() {
		isPullRefresh = true;
		pageNum = 1;
		partnerInfoList.clear();
		queryMyParteners(false);
	}

	@Override
	public void onLoadMore() {
		isLoadmore = true;
		queryMyParteners(false);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.add_partener:
			Intent it = new Intent(this, MyPartenerAddActivity.class);
			it.putExtra("flag", false);
			startActivityForResult(it, 0);
			break;
		case R.id.layout_back_button:
			Intent intent = new Intent(MyInvoiceActivity.this, ConfirmPayActivity3.class);
//			intent.putExtra("partner", info);
			intent.putParcelableArrayListExtra("partnerInfoList",partnerInfoList);
			setResult(1, intent);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requsetCode, int responseCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requsetCode, responseCode, intent);

		if (requsetCode == 0 && responseCode == 1) {
			queryMyParteners(true);
		} else if (requsetCode == 0 && responseCode == 2) {
			int position = intent.getIntExtra("position", -1);
			PartnerInfo info = intent.getParcelableExtra("partner");
			if (-1 != position) {
				partnerInfoList.get(position).setRealName(info.getRealName());
				partnerInfoList.get(position).setIdNo(info.getIdNo());
				partnerInfoList.get(position).setPassportNo(info.getPassportNo());
				mInvoiceAdapter.notifyDataSetChanged();
			}
		}
	}

	private void delMyPartener(final String id, final int position) {
		HttpUtil.delete(Urls.my_partner + "/" + id, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					ToastUtil.showToast(getApplication(), "删除成功");
					// queryMyParteners();
					partnerInfoList.remove(position);
					mInvoiceAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}

		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.partner_listview:
			PartnerInfo info = (PartnerInfo) parent.getItemAtPosition(position);
			if (isOrderFrom) {
				Intent it = new Intent(MyInvoiceActivity.this, ConfirmPayActivity3.class);
				it.putExtra("partner", info);
				it.putParcelableArrayListExtra("partnerInfoList",partnerInfoList);
				setResult(1, it);
				finish();
			}
			break;
		default:
			break;
		}
	}

}