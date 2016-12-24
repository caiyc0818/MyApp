package com.bcinfo.tripaway.activity;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.GoWithResponceListAdapter;
import com.bcinfo.tripaway.bean.Intention;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.pop.EditPopWindow;
import com.bcinfo.tripaway.view.pop.EditPopWindow.OperationListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 全部回应
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月28日 下午8:19:17
 */

public class AllResponceActivity extends BaseActivity implements
		OnClickListener {
	protected static final String TAG = "AllResponceActivity";
	private ListView responceListView;
	private ArrayList<Intention> responceList = new ArrayList<Intention>();
	private GoWithResponceListAdapter mResponceAdapter;
	private String STATUS = "all";
	// 加载旋转框
	private ProgressBar FooterBar;
	private TextView TextOfFooterBar;                    

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.all_responce_activity);
		LinearLayout layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		ImageView button_operation = (ImageView) findViewById(R.id.button_operation);
		layout_back_button.setOnClickListener(this);
		button_operation.setOnClickListener(this);
		FooterBar = (ProgressBar) findViewById(R.id.footerBar);
		TextOfFooterBar = (TextView) findViewById(R.id.text_of_footerBar);
		responceListView = (ListView) findViewById(R.id.all_responce_listview);
		initListView();
		QueryAllResponce(getIntent().getStringExtra("togetherId"));
	}

            

	private void initListView() {
		mResponceAdapter = new GoWithResponceListAdapter(this, responceList);
		responceListView.setAdapter(mResponceAdapter);
	}

            
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			AllResponceActivity.this.activityAnimationClose();
			break;
		case R.id.button_operation:
			EditPopWindow pop = new EditPopWindow(this, 0, "邀请已发出", 0, "邀请成功");
			pop.setOperationListener(new OperationListener() {
				@Override
				public void clickItem2() {
					Toast.makeText(AllResponceActivity.this, "点击了clickItem2",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void clickItem1() {
					Toast.makeText(AllResponceActivity.this, "点击了clickItem1",
							Toast.LENGTH_SHORT).show();
				}
			});
			pop.show(v);
			break;
		default:
			break;
		}
	}

	/****************** request *******************/
	private void QueryAllResponce(String id) {
		RequestParams params = new RequestParams();
		params.put("status", STATUS);
		params.put("pagesize", "10");
		params.put("pagenum", "1");

		HttpUtil.get(Urls.go_with + "/" + id, params,
				new JsonHttpResponseHandler() {
					@Override
					public void setRequestURI(URI requestURI) {
						// TODO Auto-generated method stub
						super.setRequestURI(requestURI);
						LogUtil.i(TAG, "QueryAllResponce", "requestURI="
								+ requestURI);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogUtil.i(TAG, "QueryAllResponce", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryAllResponce", "response="
								+ response.toString());
						JSONObject data = response.optJSONObject("data");
						if (data == null) {
							UIchange(2);
							return;
						}
						JSONArray intentionJsonArr = data
								.optJSONArray("intention");
						if (intentionJsonArr != null) {
							if (intentionJsonArr.length() == 0) {
								UIchange(2);
							} else {
								UIchange(1);
							}
							for (int i = 0; i < intentionJsonArr.length(); i++) {
								JSONObject intentionJson = intentionJsonArr
										.optJSONObject(i);
								Intention intention = new Intention();
								intention.setContent(intentionJson
										.optString("content"));
								intention.setStatus(intentionJson
										.optString("status"));
								intention.setCreateTime(intentionJson
										.optString("createTime"));
								JSONObject userJson = intentionJson
										.optJSONObject("user");
		                        if (userJson != null)
		                        {
		                            intention.getUser().setUserId(userJson.optString("userId"));
		                            intention.getUser().setGender(userJson.optString("sex"));
		                            intention.getUser().setNickname(userJson.optString("nickName"));
		                            intention.getUser().setAvatar(userJson.optString("avatar"));
		                            intention.getUser().setIntroduction(userJson.optString("introduction"));
		                            intention.getUser().setMobile(userJson.optString("mobile"));
		                            intention.getUser().setStatus(userJson.optString("status"));
		                            intention.getUser().setEmail(userJson.optString("email"));
		                            JSONArray tagsJSONList = userJson.optJSONArray("tags");
		                            if (tagsJSONList != null)
		                                for (int k = 0; k < tagsJSONList.length(); k++)
		                                {
		                                    intention.getUser().getTags().add(tagsJSONList.opt(k).toString());
		                                }
		                        }
								responceList.add(intention);
							}
							mResponceAdapter.notifyDataSetChanged();
						} else {
							UIchange(2);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, "onFailure", "statusCode=" + statusCode);
						UIchange(3);
					}
				});
	}

	private void UIchange(int num) {
		switch (num) {
		case 1:
			FooterBar.setVisibility(View.GONE);
			TextOfFooterBar.setVisibility(View.GONE);
			break;
		case 2:
			FooterBar.setVisibility(View.GONE);
			TextOfFooterBar.setText("抱歉，未找到相关数据!");
			break;
		case 3:
			FooterBar.setVisibility(View.GONE);
			TextOfFooterBar.setText("加载失败!");
			break;
		}
	}
}
