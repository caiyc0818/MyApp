package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

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

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyInvoice2Adapter;
import com.bcinfo.tripaway.adapter.MyInvoice2Adapter.OperationListener;
import com.bcinfo.tripaway.bean.Invoice;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyInvoice2Activity extends BaseActivity implements IXListViewListener,OnClickListener,OnItemClickListener{
	
	
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
	
	private MyInvoice2Adapter mInvoiceAdapter;
	
	private LinearLayout no_trip;
	
	private ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
	
	private TextView mDelBtn;
	
	private TextView mModifyBtn;
	
	private RelativeLayout secondTitles; 
	
	private ImageView mAddBtn;
	
	private LinearLayout backBtn;
	
	private boolean isOrderFrom = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_invoice2);
		mXlistView =  (XListView) findViewById(R.id.invoice_listview);
		mXlistView.setPullRefreshEnable(true);
		mXlistView.setPullLoadEnable(false);
		mXlistView.setXListViewListener(this);
		
		initView();
		queryMyInvoice();
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
		mInvoiceAdapter = new MyInvoice2Adapter(getApplication(),invoiceList,new OperationListener() {
			@Override
			public void modifyInfo(Invoice info,int position) {
				Intent it = new Intent(MyInvoice2Activity.this,MyInvoiceAddActivity.class);
				it.putExtra("invoice",info);
				it.putExtra("flag", true);
				it.putExtra("position", position);
				startActivityForResult(it, 0);
			}
			@Override
			public void delInfoById(String id,int position) {
				final String ids = id;
				final int pos = position;
				new AlertDialog.Builder(MyInvoice2Activity.this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						delMyInvoice(ids, pos);
					}
				}).setNegativeButton("取消",  new DialogInterface.OnClickListener(){

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
		mModifyBtn =(TextView) findViewById(R.id.button_modify);
		mAddBtn = (ImageView) findViewById(R.id.add_partener);
		mAddBtn.setOnClickListener(this);
		secondTitles = (RelativeLayout) findViewById(R.id.second_titles);
		secondTitles.getBackground().setAlpha(255);
		backBtn = (LinearLayout )findViewById(R.id.layout_back_button);
		backBtn.setOnClickListener(this);
		mXlistView.setOnItemClickListener(this);
	}
	
	private void queryMyInvoice(){
		RequestParams params = new RequestParams();
		HttpUtil.get(Urls.my_invoice, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
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
					JSONArray dataList = response.optJSONArray("data");
					invoiceList.clear();
					if (dataList == null || dataList.length() == 0) {
						updateListAdapter(new ArrayList<Invoice>());
						return;
					}
					ArrayList<Invoice> myPartner = new ArrayList<Invoice>();
					// myOrdersArrayList.clear();
					for (int i = 0; i < dataList.length(); i++) {
						JSONObject myOrderJson = dataList.optJSONObject(i);
						Invoice info = new Invoice();
						info.setInvoiceId(myOrderJson.optString("invoiceId"));
						info.setInvoiceTitle(myOrderJson.optString("invoiceTitle"));
						info.setInvoiceType(myOrderJson.optString("invoiceType"));
						info.setAddress(myOrderJson.optString("address"));
						info.setArea(myOrderJson.optString("area"));
						info.setUserName(myOrderJson.optString("userName"));
						info.setTel(myOrderJson.optString("tel"));
						info.setEmail(myOrderJson.optString("email"));
						info.setAlias(myOrderJson.optString("alias"));
						info.setIsDefault(myOrderJson.optString("isDefault"));
						info.setPostCode(myOrderJson.optString("postCode"));
						myPartner.add(info);
					}
//					if (myPartner.size() < 10) {
//						mXlistView.setPullLoadEnable(false);
//					} else {
////						pageNum++;
////						mXlistView.setPullLoadEnable(true);
//					}
					updateListAdapter(myPartner);
					// myOrdersArrayList.addAll(myOrders);
					// mMyOrdersListAdapter.notifyDataSetChanged();
				} else {
//					ToastUtil.showToast(getApplicationContext(), "errorCode="
//							+ response.optString("code"));
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
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				System.out.println(errorResponse);
//				ToastUtil.showToast(getApplicationContext(), "statusCode=" + statusCode);
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
	
	private void updateListAdapter(ArrayList<Invoice> myOrders) {

		invoiceList.addAll(myOrders);
		mInvoiceAdapter.notifyDataSetChanged();
		// 没有旅程订单显示的页面
		if (invoiceList.size() <= 0) {
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
		invoiceList.clear();
		queryMyInvoice();
	}
	@Override
	public void onLoadMore() {
		isLoadmore = true;
		queryMyInvoice();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.add_partener:
			Intent it = new Intent(this,MyInvoiceAddActivity.class);
			it.putExtra("flag",false);
			startActivityForResult(it, 0);
			break;
		case R.id.layout_back_button:
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
		
		if(requsetCode == 0&&responseCode ==1){
			queryMyInvoice();
		}else if(requsetCode == 0&&responseCode ==2){
			int position = intent.getIntExtra("position", -1);
			Invoice info = intent.getParcelableExtra("invoice");
			if(-1 != position){
				invoiceList.get(position).setAddress(info.getAddress());
				invoiceList.get(position).setInvoiceId(info.getInvoiceId());
				invoiceList.get(position).setInvoiceTitle(info.getInvoiceTitle());
				invoiceList.get(position).setInvoiceType(info.getInvoiceType());
				invoiceList.get(position).setArea(info.getArea());
				invoiceList.get(position).setAlias(info.getAlias());
				invoiceList.get(position).setEmail(info.getEmail());
				invoiceList.get(position).setIsDefault(info.getIsDefault());
				invoiceList.get(position).setUserName(info.getUserName());
				invoiceList.get(position).setTel(info.getTel());
				mInvoiceAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private void delMyInvoice(final String id,final int position){
		HttpUtil.delete(Urls.my_invoice+"/"+id, new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if("00000".equals(code)){
					ToastUtil.showToast(getApplication(), "删除成功");
//					queryMyParteners();
					invoiceList.remove(position);
					mInvoiceAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.invoice_listview:
			Invoice info = (Invoice)parent.getItemAtPosition(position);
			if(isOrderFrom){
				Intent it = new Intent(MyInvoice2Activity.this,ConfirmPayActivity3.class);
				it.putExtra("invoice", info);
				setResult(2,  it );
				finish();
			}
			break;
		default:
			break;
		}
	}
	
}