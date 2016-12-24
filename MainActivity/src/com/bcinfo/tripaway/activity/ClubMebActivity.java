package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ClubMebAdapter;
import com.bcinfo.tripaway.adapter.ClubMebAdapter.OperationListen;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.fragment.BaseFragment;
import com.bcinfo.tripaway.fragment.GoldMedalFragment;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.GoldFilterDialog;
import com.bcinfo.tripaway.view.dialog.GoldFilterDialog.OperationListener;
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ClubMebActivity extends BaseActivity implements OnClickListener,
		IXListViewListener {

	private RelativeLayout titleLayout;

	private LinearLayout backBtn;

	private XListView listView;
	private int pageNum = 1;

	private int pageSize = 10;

	private String userId;

	private String type = "all";

	private String isGold = "no";

	private String keyword = "";

	private DeletedEditText queryText;
	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private ClubMebAdapter adapter;

	private List<UserInfo> users = new ArrayList<UserInfo>();

	private ImageView filterImage;
	
	private ImageView noPeoImage;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.item_orgpeo_layout);
		userId = getIntent().getStringExtra("userId");

		initView();
		registerBoradcastReceiver();
	}

	@Override
	protected void initView() {
		super.initView();
		filterImage = (ImageView) findViewById(R.id.fliter_btn);
		filterImage.setOnClickListener(this);
		noPeoImage = (ImageView) findViewById(R.id.no_peo);
		queryText = (DeletedEditText) findViewById(R.id.friendship_query_tv);
		queryText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				keyword = queryText.getText().toString();
				pageNum = 1;
				if (users != null) {
					users.clear();
				}
				queryGoldMebs(type, keyword, userId, isGold);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		titleLayout = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		titleLayout.getBackground().setAlpha(255);
		backBtn = (LinearLayout) findViewById(R.id.layout_back_button);
		backBtn.setOnClickListener(this);
		listView = (XListView) findViewById(R.id.club_meb_list);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		adapter = new ClubMebAdapter(ClubMebActivity.this, users,
				new OperationListen() {

					@Override
					public void addFocusByUserId(String userId,int position,boolean flag) {
						addOrCancelFocus(userId,position,flag);
					}
				});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		queryGoldMebs(type, keyword, userId, isGold);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			break;
		case R.id.fliter_btn:
			GoldFilterDialog dialog = new GoldFilterDialog(
					ClubMebActivity.this, new OperationListener() {

						@Override
						public void queryMeb(String types, String isGolds) {
							pageNum = 1;
							if (users != null) {
								users.clear();
							}
							type = types;
							isGold = isGolds;
							queryGoldMebs(type, keyword, userId, isGold);
						}
					}, isGold, type);
			dialog.show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		isPullRefresh = true;
		pageNum = 1;
		users.clear();
		queryGoldMebs(type, keyword, userId, isGold);
	}

	@Override
	public void onLoadMore() {
		isLoadmore = true;
		++pageNum;
		queryGoldMebs(type, keyword, userId, isGold);
	}

	private void queryGoldMebs(String type, String keyword, String userId,
			String isGold) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("type", type);
			obj.put("keyword", keyword);
			obj.put("userId", userId);
			obj.put("isGold", isGold);
			obj.put("pagenum", pageNum);
			obj.put("pagesize", pageSize);
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);

			HttpUtil.post(Urls.goldmedal_list, entity,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							if (isLoadmore) {
								// 上拉返回的结束加载更多布局
								listView.stopLoadMore();
							}
							String code = response.optString("code");
							if (!"00000".equals(code)) {
								notifyList(new ArrayList<UserInfo>());
								if (isPullRefresh) {
									// 下拉刷新接口返回的数据不正确
									listView.stopRefresh();
								}
								if (pageNum != 1) {
									pageNum--;
								}
								isLoadmore = false;
								isPullRefresh = false;
								return;
							}
							if (isPullRefresh) {
								// 下拉刷新返回的
								listView.successRefresh();
							}
							List<UserInfo> userList = new ArrayList<UserInfo>();
							JSONArray data = response.optJSONArray("data");
							if (data != null) {
								for (int i = 0; i < data.length(); i++) {
									JSONObject obj = data.optJSONObject(i);
									UserInfo info = new UserInfo();
									info.setUserId(obj.optString("userId"));
									info.setAvatar(obj.optString("avatar"));
									info.setNickname(obj.optString("nickName"));
									info.setFansCount(obj
											.optString("fansCount"));
									info.setIsFocus(obj.optString("isFocus"));
									info.setIsGold(obj.optString("isGold"));
									JSONObject orgRole = obj
											.optJSONObject("orgRole");
									JSONObject tags = obj.optJSONObject("tags");
									Tags tag = new Tags();
									if (null != tags) {
										JSONArray interest = tags
												.optJSONArray("interest");
										List<String> interList = new ArrayList<String>();
										if (null != interest) {
											for (int j = 0; j < interest
													.length(); j++) {
												interList.add(interest
														.optString(j));
											}
										}
										tag.setInterests(interList);
										info.setTag(tag);
									}
									if (null != orgRole) {
										OrgRole role = new OrgRole();
										role.setRoleName(orgRole
												.optString("roleName"));
										role.setRoleCode(orgRole
												.optString("roleCode"));
										info.setOrgRole(role);
									}
									userList.add(info);
								}
							}

							notifyList(userList);

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							super.onFailure(statusCode, headers,
									responseString, throwable);
							if (isLoadmore) {
								listView.stopLoadMore();
							}
							if (isPullRefresh) {
								listView.stopRefresh();
							}
							isPullRefresh = false;
							isLoadmore = false;
							if (pageNum != 1) {
								pageNum--;
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

	private void notifyList(List<UserInfo> list) {
		users.addAll(list);
		adapter.notifyDataSetChanged();
		if(users.size() == 0){
			listView.setVisibility(View.GONE);
			noPeoImage.setVisibility(View.VISIBLE);
		}else{
			listView.setVisibility(View.VISIBLE);
			noPeoImage.setVisibility(View.GONE);
		}
	}

	private void addOrCancelFocus(String userId,final int position,boolean flag) {
		if (!AppInfo.getIsLogin()) {
			ToastUtil.showToast(ClubMebActivity.this, "请登录");
			return;
		}
		if (!flag) {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", userId);
				StringEntity entity = new StringEntity(params.toString(),
						HTTP.UTF_8);
				HttpUtil.post(Urls.friend_list_url, entity,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode, Header[] headers,
									JSONObject response) {
								super.onSuccess(statusCode, headers, response);
								String code = response.optString("code");
								if ("00000".equals(code)) {
									if(users.size() >position){
										users.get(position).setIsFocus("yes");
										adapter.notifyDataSetChanged();
									}
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
		}else{
			HttpUtil.delete(Urls.friend_list_url + "/" + userId,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							String code = response.optString("code");
							if ("00000".equals(code)) {
								if(users.size() >position){
									users.get(position).setIsFocus("no");
									adapter.notifyDataSetChanged();
								}
							}
						}
					});
		}
		
	}
	
	   public void registerBoradcastReceiver()
	    {
	        IntentFilter myIntentFilter = new IntentFilter();
	        myIntentFilter.addAction("com.bcinfo.refreshFocus");
	        // 注册广播
	       registerReceiver(mBroadcastReceiver, myIntentFilter);
	    }
	   
	   private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	    {
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	            String action = intent.getAction();
	            if("com.bcinfo.refreshFocus".equals(action)){
	            	String userId=intent.getStringExtra("userId");
	            	boolean isFocus=intent.getBooleanExtra("isFocus",false);
	            	if(userId!=null){
	            		for(UserInfo userInfo:users){
	            			if(userInfo!=null&&userInfo.getUserId().equals(userId)){
							if (userInfo.getIsFocus().equals("yes") && !isFocus) {
								userInfo.setIsFocus("no");
								adapter.notifyDataSetChanged();
							}
							if (userInfo.getIsFocus().equals("no") && isFocus) {
								userInfo.setIsFocus("yes");
								adapter.notifyDataSetChanged();
							}
	            				break;
	            			}
	            		}
	            	}
	            }
	        }
	    };
	    
	    protected void onDestroy() {
	    	super.onDestroy();
	    	unregisterReceiver(mBroadcastReceiver);
	    };
}
