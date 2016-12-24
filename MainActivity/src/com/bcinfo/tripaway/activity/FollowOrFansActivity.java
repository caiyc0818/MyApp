package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.FollowOrFansAdapter;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class FollowOrFansActivity extends BaseActivity implements
		IXListViewListener {

	private FollowOrFansAdapter followOrFansadapter;
	private XListView xListView;
	private ArrayList<UserInfo> userInfoList = new ArrayList<>();
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;
	private int pageNum = 1;
	private int pagesize = 10;
	String title = "";
	String userId = "";
	private LinearLayout imageviewFocus;
	private LinearLayout imageviewFans;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.follow_fans_activity);
		title = getIntent().getStringExtra("title");
		userId = getIntent().getStringExtra("userId");
		setSecondTitle(title);
		userInfoList.clear();
		initView();
		if (title.equals("粉丝")) {
			// showData(1);
			queryFans(pageNum, userId);
		} else if (title.equals("关注")) {
			// showData(1);
			queryFocus(pageNum, userId);
		}

	}

	protected void initView() {
		imageviewFocus = (LinearLayout) findViewById(R.id.imageViewFocus);
		imageviewFans = (LinearLayout) findViewById(R.id.imageViewFans);
		followOrFansadapter = new FollowOrFansAdapter(
				FollowOrFansActivity.this, userInfoList);
		xListView = (XListView) findViewById(R.id.review_listview);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(false);
		xListView.setXListViewListener(this);
		xListView.setAdapter(followOrFansadapter);
		followOrFansadapter.notifyDataSetChanged();
	}

	private void showData(int index) {
		// if (isLoadmore) {
		// xListView.stopLoadMore();
		// }
		// for(int i = 0;i<10;i++){
		// list.add(String.valueOf(i));
		// }
		// followOrFansadapter.notifyDataSetChanged();
		// isLoadmore = false;
	}

	private void queryFans(int index, String userId) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("pagenum", pageNum);
			params.put("pagesize", pagesize);
			HttpUtil.get(Urls.fans, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if (code.equals("00000")) {
						JSONObject json = response.optJSONObject("data");
						JSONArray data = json.optJSONArray("user");
						if (data != null && data.length() > 0) {
							xListView.setVisibility(View.VISIBLE);
							imageviewFocus.setVisibility(View.GONE);
							ArrayList<UserInfo> tempUserInfo = new ArrayList<>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject jsonObject = data.optJSONObject(i);
//								UserInfo user = new UserInfo();
//								user.setAvatar(jsonObject.optString("avatar"));
//								user.setUserId(jsonObject.optString("userId"));
//								user.setIntroduction(jsonObject
//										.optString("introduction"));
//								user.setUserName(jsonObject
//										.optString("userName"));
//								user.setRealName(jsonObject
//										.optString("realName"));
//								user.setNickname(jsonObject
//										.optString("nickName"));
//								user.setMobile(jsonObject.optString("mobile"));
//								user.setEmail(jsonObject.optString("email"));
//								user.setStatus(jsonObject.optString("status"));
//								user.setGender(jsonObject.optString("sex"));
//								user.setRole(jsonObject.optString("role"));
//								user.setPermission(jsonObject
//										.optString("permission"));
//								user.setUserType(jsonObject
//										.optString("userType"));
//								user.setFocus(jsonObject.optString("focus"));
//								user.setFansCount(jsonObject
//										.optString("fansCount"));
//								user.setIsFocus(jsonObject.optString("isFocus"));
//								user.setIsTalent(jsonObjects
//										.optString("isTalent"));
//								user.setBrief(jsonObject.optString("brief"));
								tempUserInfo.add(JsonUtil.getUserInfo(jsonObject));
								if (tempUserInfo.size() >= pagesize) {
									pageNum++;
									imageviewFocus.setVisibility(View.GONE);
									xListView.setPullLoadEnable(true);
								} else {
									xListView.setPullLoadEnable(false);
								}
							}
							refreshData(tempUserInfo);
						} else {
							imageviewFans.setVisibility(View.VISIBLE);
						}
					} else {
						imageviewFans.setVisibility(View.VISIBLE);
					}
					isLoadmore = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONArray errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					isLoadmore = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString,
							throwable);
					isLoadmore = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					isLoadmore = false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void queryFocus(int index, String userId) {
		try {
			RequestParams params = new RequestParams();
			params.put("userId", userId);
			params.put("pagenum", pageNum);
			params.put("pagesize", pagesize);
			HttpUtil.get(Urls.focus, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if (code.equals("00000")) {
						JSONObject json = response.optJSONObject("data");
						JSONArray data = json.optJSONArray("user");
						if (data != null && data.length() > 0) {
							xListView.setVisibility(View.VISIBLE);
							imageviewFocus.setVisibility(View.GONE);
							ArrayList<UserInfo> tempUserInfo = new ArrayList<>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject jsonObject = data.optJSONObject(i);
//								UserInfo user = new UserInfo();
//								user.setAvatar(jsonObject.optString("avatar"));
//								user.setUserId(jsonObject.optString("userId"));
//								user.setIntroduction(jsonObject
//										.optString("introduction"));
//								user.setUserName(jsonObject
//										.optString("userName"));
//								user.setRealName(jsonObject
//										.optString("realName"));
//								user.setNickname(jsonObject
//										.optString("nickName"));
//								user.setMobile(jsonObject.optString("mobile"));
//								user.setEmail(jsonObject.optString("email"));
//								user.setStatus(jsonObject.optString("status"));
//								user.setGender(jsonObject.optString("sex"));
//								user.setRole(jsonObject.optString("role"));
//								user.setPermission(jsonObject
//										.optString("permission"));
//								user.setUserType(jsonObject
//										.optString("userType"));
//								user.setFocus(jsonObject.optString("focus"));
//								user.setFansCount(jsonObject
//										.optString("fansCount"));
//								user.setIsFocus(jsonObject.optString("isFocus"));
//								user.setIsTalent(jsonObject
//										.optString("isTalent"));
//								user.setBrief(jsonObject.optString("brief"));
								tempUserInfo.add(JsonUtil.getUserInfo(jsonObject));
								if (tempUserInfo.size() >= pagesize) {
									pageNum++;
									imageviewFocus.setVisibility(View.GONE);
									xListView.setPullLoadEnable(true);
								} else {
									xListView.setPullLoadEnable(false);
								}
							}
							refreshData(tempUserInfo);
						} else {
							imageviewFocus.setVisibility(View.VISIBLE);
						}
					} else {
						imageviewFocus.setVisibility(View.VISIBLE);
					}
					isLoadmore = false;
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONArray response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					isLoadmore = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					isLoadmore = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString,
							throwable);
					isLoadmore = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONArray errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					isLoadmore = false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void refreshData(ArrayList<UserInfo> list) {
		userInfoList.addAll(list);
		followOrFansadapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		if (title.equals("粉丝")) {
			queryFans(pageNum, userId);
		} else {
			queryFocus(pageNum, userId);
		}
		// showData(pageNum);
	}
}
