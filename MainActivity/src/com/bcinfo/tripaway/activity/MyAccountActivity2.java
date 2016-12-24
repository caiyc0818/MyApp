package com.bcinfo.tripaway.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyAccountfasAdapter;
import com.bcinfo.tripaway.bean.MyAccFas;
import com.bcinfo.tripaway.bean.MyAccSubject;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.dialog.DialogReminderLayoutFas;
import com.bcinfo.tripaway.dialog.DialogReminderLayoutFas.AccountFasInterface;
import com.bcinfo.tripaway.dialog.DialogReminderLayoutNoFas;
import com.bcinfo.tripaway.dialog.DialogReminderLayoutNoFas.AccountNoFasInterface;
import com.bcinfo.tripaway.dialog.MyAccountExplainDialog;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 我的账户
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月23日 11:26:11
 */
public class MyAccountActivity2 extends BaseActivity
		implements OnClickListener, AccountFasInterface, AccountNoFasInterface {
	private DialogReminderLayoutFas myAccountUnbindDialog;
	private DialogReminderLayoutNoFas myAccountNoDialog;
	private ImageView secondTitleBarIv;// 标题栏右边的imageview
	private ImageView myAccountQuestionIv;// 问号 图标
	private TextView totalSalaryTv;
	private TextView availableBalance;
	private int pageNum = 1;
	private String pageSize = "10";
	private MyListView myAccountListView;
	private ArrayList<MyAccFas> list = new ArrayList<>();
	private MyAccountfasAdapter adapter;
	private LinearLayout backbackTv;
	private RelativeLayout secondLayout;
	private RelativeLayout textView;
	private ImageView noImageView;
	MyAccSubject myAccSubject = new MyAccSubject();
	private TextView fas;
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private View loginLoading;
	private AnimationDrawable loadingAnimation;

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
		setContentView(R.layout.activity_my_account3);
		setSecondTitle("我的账户");
		secondLayout = (RelativeLayout) findViewById(R.id.second_title);
		secondLayout.getBackground().setAlpha(255);
		AppManager.getAppManager().addActivity(this);
		totalSalaryTv = (TextView) findViewById(R.id.my_account_total_salary_number_tv);
		availableBalance = (TextView) findViewById(R.id.cash);
		textView = (RelativeLayout) findViewById(R.id.text);
		noImageView = (ImageView) findViewById(R.id.noimage);
		backbackTv = (LinearLayout) findViewById(R.id.layout_back_button);
		LinearLayout rightlineTv = (LinearLayout) findViewById(R.id.second_title_right_line);
		rightlineTv.setOnClickListener(this);
		backbackTv.setOnClickListener(this);
		secondTitleBarIv = (ImageView) findViewById(R.id.second_title_right_iv);
		myAccountQuestionIv = (ImageView) findViewById(R.id.my_account_question_iv);
		fas = (TextView) findViewById(R.id.fas);
		fas.setOnClickListener(this);
		secondTitleBarIv.setBackgroundResource(R.drawable.add_event_bg);
		secondTitleBarIv.setVisibility(View.VISIBLE);
		myAccountQuestionIv.setOnClickListener(this);
		myAccountListView = (MyListView) findViewById(R.id.myaccount_listview);
		textView.setOnClickListener(this);
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		testAccountListUrl();
		myAccountSubject();
		adapter = new MyAccountfasAdapter(list, MyAccountActivity2.this);
		myAccountListView.setAdapter(adapter);
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
					Intent intent = new Intent(MyAccountActivity2.this, MyOrderDetailActivity.class);
					intent.putExtra("orderId", myOrder.getOrderId());
					startActivity(intent);
					MyAccountActivity2.this.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}

			}
		});

	}

	private void testAccountListUrl() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("facId", "0001");
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		HttpUtil.get(Urls.my_account_fas, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					JSONArray jsonArray = response.optJSONArray("data");
					if (jsonArray.length() > 0 && jsonArray != null) {
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

							list.add(myAccFas);
							adapter.notifyDataSetChanged();

						}

					}
					if (list.size() == 0) {
						loadingAnimation.stop();
						loginLoading.setVisibility(View.GONE);
						noImageView.setVisibility(View.VISIBLE);
					} else {
						loadingAnimation.stop();
						loginLoading.setVisibility(View.GONE);
						textView.setVisibility(View.VISIBLE);
					}

				}

			}
		});

	}

	private void myAccountSubject() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("facId", "0001");
		HttpUtil.get(Urls.my_account_subject, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					JSONObject resultObj = response.optJSONObject("data");
					myAccSubject.setTotalBalance(resultObj.optString("totalBalance"));// 账户余额
					myAccSubject.setAvailableBalance(resultObj.optString("availableBalance"));// 可用余额（可提现，可消费）

				} else if ("00099".equals(response.optString("code"))) {
					PreferenceUtil.delUserInfo();
					UserInfoDB.getInstance().deleteAll();
					goLoginActivity();
				}
				if (!StringUtils.isEmpty(myAccSubject.getTotalBalance())) {
					totalSalaryTv
							.setText("¥ " + decimalFormat.format(Double.parseDouble(myAccSubject.getTotalBalance())));
				}
				if (!StringUtils.isEmpty(myAccSubject.getTotalBalance())) {
					availableBalance.setText(
							"¥ " + decimalFormat.format(Double.parseDouble(myAccSubject.getAvailableBalance())));
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_account_question_iv:// 问号
			final MyAccountExplainDialog myAccountExplainDialog = MyAccountExplainDialog.createDialog(this,
					R.layout.dialog_my_account_hint);

			myAccountExplainDialog.findViewById(R.id.layout_my_account_confirm_container)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							myAccountExplainDialog.dismiss();

						}
					});
			myAccountExplainDialog.show();
			break;
		case R.id.second_title_right_line:
			startActivity(new Intent(this, MyAccountActivity.class));
			activityAnimationOpen();
			break;
		case R.id.layout_back_button:
			finish();
			activityAnimationClose();
			break;
		case R.id.text:
			startActivity(new Intent(this, MyMoreFac.class));
			activityAnimationOpen();
			break;
		case R.id.fas:
			if (!StringUtils.isEmpty(myAccSubject.getAvailableBalance())) {
				float price = 0;
				price = Float.parseFloat(myAccSubject.getAvailableBalance());
				if (price > 0) {
					myAccountUnbindDialog = new DialogReminderLayoutFas(this, this);// 账户有钱提现对话框
					myAccountUnbindDialog.show();
					myAccountUnbindDialog.setDailogText(
							"¥ " + decimalFormat.format(Double.parseDouble(myAccSubject.getAvailableBalance())));
					myAccountUnbindDialog.showRotate(true);
				} else {
					myAccountNoDialog = new DialogReminderLayoutNoFas(this, this);// 账户无钱提现对话框
					myAccountNoDialog.show();
					myAccountNoDialog.showRotate(true);
				}
			}

			break;
		default:
			break;
		}

	}

	private void fas(String amount) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("facId", "0001");
		params.put("amount", amount);
		HttpUtil.get(Urls.my__fas, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					myAccountUnbindDialog.dismiss();
					ToastUtil.showToast(MyAccountActivity2.this, "提现申请成功！");
				}
				if ("10004".equals(response.optString("code"))) {
					myAccountUnbindDialog.dismiss();
					ToastUtil.showToast(MyAccountActivity2.this, "提现操作失败！");
				} else {
					myAccountUnbindDialog.dismiss();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				myAccountUnbindDialog.dismiss();
			}
		});

	}

	@Override
	public void unbindOperate(int operateCode) {
		// TODO Auto-generated method stub
		switch (operateCode) {
		case 0:// 取消
			break;
		case 1:// 确认
			fas(myAccSubject.getAvailableBalance()); // 调用删除账户的接口
			break;
		default:
			break;
		}
	}

	@Override
	public void noOperate(int operateCode) {
		// TODO Auto-generated method stub
		switch (operateCode) {
		case 0:// 取消
			myAccountNoDialog.dismiss();
			break;
		case 1:// 确认
			myAccountNoDialog.dismiss();
			break;
		default:
			break;
		}
	}

}
