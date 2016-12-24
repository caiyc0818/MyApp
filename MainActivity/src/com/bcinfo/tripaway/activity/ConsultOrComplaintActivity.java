package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.HelpAndConsultAdapter;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.HelpInfo;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
public class ConsultOrComplaintActivity extends BaseActivity implements
		OnClickListener {

	private Button submitButton;
	private TextView myTextView;
	private EditText contentEditText;

	private String type;
	private String content;
	private String referId;

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		setContentView(R.layout.to_consult_or_complaint);
		setSecondTitle("投诉建议");
		// getHelp("100","1");
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		referId=intent.getStringExtra("referId");
		if(referId==null) referId="";

		findView();
		if (type.equals("complaint")) {
			setSecondTitle("投诉建议");
			statisticsTitle="投诉建议";
			contentEditText.setHint("请详细描述投诉理由，有利于我们快速跟进（内容不得少于5个字）");
		} else{
			setSecondTitle("我要咨询");
			statisticsTitle="我要咨询";
			}
		myTextView.setVisibility(View.VISIBLE);
		myTextView.setText("我的");
		myTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConsultOrComplaintActivity.this,
						ConsultOrComplaintDetailActivity.class);
				intent.putExtra("type", type);
				startActivity(intent);
				activityAnimationOpen();
			}
		});
	
	}

	private void findView() {
		submitButton = (Button) this.findViewById(R.id.submit);
		myTextView = (TextView) this.findViewById(R.id.event_commit_button);
		contentEditText = (EditText) this.findViewById(R.id.content);
		submitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			this.overridePendingTransition(R.anim.activity_back,
					R.anim.activity_finish);
			break;
		case R.id.submit:
			content = contentEditText.getText().toString();
			if (!AppInfo.getIsLogin()) {
				ToastUtil.showToast(this, "请登录");
				return;
			}
			if (TextUtils.isEmpty(content) || content.length() < 5) {
				ToastUtil.showToast(this, "内容少于5个字");
			} else
				sendFeedback();
			break;
		default:
			break;
		}

	}

	private void sendFeedback() {
		try {

			JSONObject feedbackJson = new JSONObject();

			feedbackJson.put("content", content);
			feedbackJson.put("type", type);
			feedbackJson.put("referId", referId);
			

			StringEntity stringEntity = new StringEntity(
					feedbackJson.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.get_consultation, stringEntity,
					new JsonHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers,
									responseString, throwable);
							// ToastUtil.showToast(ReplayActivity.this,
							// "throwable=" + throwable.getMessage());
							// for (int i = 0; i < messageList.size(); i++)
							// {
							// if
							// (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
							// {
							// messageList.get(i).setSendStatus("1");
							// break;
							// }
							// }
							ToastUtil.showToast(
									ConsultOrComplaintActivity.this, "提交失败");

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							// ToastUtil.showToast(ReplayActivity.this,
							// "throwable=" + throwable.getMessage());
							// for (int i = 0; i < messageList.size(); i++)
							// {
							// if
							// (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
							// {
							// messageList.get(i).setSendStatus("1");
							// break;
							// }
							// }
							ToastUtil.showToast(
									ConsultOrComplaintActivity.this, "提交失败");

						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, headers, response);
							LogUtil.i("ConsultOrComplaintActivity",
									"发送帮助或投诉接口=", response.toString());
							if (response.optString("code").equals("00000")) {
								Intent intent = new Intent(
										ConsultOrComplaintActivity.this,
										ConsultOrComplaintDetailActivity.class);
								intent.putExtra("type", type);
								startActivity(intent);
								activityAnimationOpen();
							} else {
								// ToastUtil.showToast(ReplayActivity.this,
								// "errorCode=" + response.optString("code"));
								System.out.println("errorCode="
										+ response.optString("code"));
							}
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
