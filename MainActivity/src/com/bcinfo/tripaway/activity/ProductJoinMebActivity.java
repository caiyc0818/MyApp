package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.GrouponJoinMemGridViewAdapter;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ProductJoinMebActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG = "ProductJoinMebActivity";
	// 返回按钮
	private LinearLayout callBackLayout;
	// 订购人员列表
	private GridView mGridView;
	// 自定义人员列表adapter
	private GrouponJoinMemGridViewAdapter mGroupGvAdapter;
	// 产品Id
	private String productId;
	// 每页条数
	private String pageSize;

	private List<UserInfo> mMemberList = new ArrayList<UserInfo>();
	
	private RelativeLayout mRelativeLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.groupon_joinmeb_info);
		statisticsTitle="所有订购游客";
		// 初始化所有控件
		initParams();
		// 初始化gridview列表
		initGridParams();
	}

	private void initParams() {
		// 初始化产品Id
		productId = getIntent().getStringExtra("productId");
		pageSize = getIntent().getStringExtra("total");
		callBackLayout = (LinearLayout) findViewById(R.id.layout_back_buttons);
		mGridView = (GridView) findViewById(R.id.join_user_gv_list);
		callBackLayout.setOnClickListener(this);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		mRelativeLayout.getBackground().setAlpha(255);
	}

	private void setMebListAdapter() {
		mGroupGvAdapter = new GrouponJoinMemGridViewAdapter(this, mMemberList);
		mGridView.setAdapter(mGroupGvAdapter);
	}

	private void initGridParams() {
		RequestParams params = new RequestParams();
		params.put("pagenum", "1");
		params.put("pagesize", pageSize);
		params.put("productId", productId);
//		for(int i= 0;i<100;i++){
//			UserInfo info = new UserInfo();
//			info.setAvatar("");
//			info.setAge(24+"");
//			info.setNickname("测试"+i);
//			if(i%2 == 0){
//				info.setGender("0");
//			}else{
//				info.setGender("1");
//			}
//			mMemberList.add(info);
//		}
//		setMebListAdapter();
		HttpUtil.get(Urls.product_buyers, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogUtil.i(TAG, "ProductJoinMebActivity", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "ProductJoinMebActivity", "response="
								+ response.toString());
						String code = response.optString("code");
						String msg = response.optString("msg");
						if (!code.equals("00000")) {
							ToastUtil.showErrorToast(
									ProductJoinMebActivity.this, msg);
							return;
						}
						JSONObject dataJSON = response.optJSONObject("data");
						JSONArray productArray = dataJSON
								.optJSONArray("buyers");
						if(productArray != null ){
							for (int i = 0; i < productArray.length(); i++) {
								UserInfo senderInfo = new UserInfo();
								JSONObject userJson = productArray
										.optJSONObject(i);
                                senderInfo.setGender(userJson.optString("sex"));
                                senderInfo.setAge(userJson.optString("age"));
                                senderInfo.setAddress(userJson.optString("address"));
                                senderInfo.setStatus(userJson.optString("status"));
                                senderInfo.setEmail(userJson.optString("email"));
                                senderInfo.setNickname(userJson.optString("nickName"));
                                senderInfo.setUserId(userJson.optString("userId"));
                                senderInfo.setUserType(userJson.optString("userType"));
                                senderInfo.setBrief(userJson.optString("brief"));
                                senderInfo.setRole(userJson.optString("role"));
                                senderInfo.setPermission(userJson.optString("permission"));
                                senderInfo.setAvatar(userJson.optString("avatar"));
                                senderInfo.setIntroduction(userJson.optString("introduction"));
                                senderInfo.setMobile(userJson.optString("mobile"));
                                senderInfo.setBackground(userJson.optString("background"));
                                senderInfo.setUsersIdentity(userJson.optString("usersIdentity"));
                                JSONArray tagsJsonArray = userJson.optJSONArray("tags");
                                if (tagsJsonArray != null && tagsJsonArray.length() > 0)
                                {
                                    ArrayList<String> tags = new ArrayList<String>();
                                    for (int j = 0; j < tagsJsonArray.length(); j++)
                                    {
                                        tags.add(tagsJsonArray.optString(j));
                                    }
                                    senderInfo.setTags(tags);
                                }
                                
                                JSONObject orgRole = userJson.optJSONObject("orgRole");
                                OrgRole orle = new OrgRole();
                                if(orgRole != null){
                                	orle.setRoleName(orgRole.optString("roleName"));
                                	orle.setRoleType(orgRole.optString("roleType"));
                                	orle.setRoleCode(orgRole.optString("roleCode"));
                                }
                                senderInfo.setOrgRole(orle);
                                JSONArray  proArray = userJson.optJSONArray("profession");
                                ArrayList<String> list = new ArrayList<String>();
                                String profession = "";
                                if(proArray != null){
                                	for(int j=0;j<proArray.length();j++){
                                		list.add(proArray.optString(j));
                                		if(j ==0){
                                			profession += proArray
                									.optString(j);
                                		}else{
                                			profession += ","
                									+ proArray.optString(j);
                                		}
                                	}
                                }
                                senderInfo.setPermission(profession);
                                senderInfo.setProfession(list);
								mMemberList.add(senderInfo);
							}
							// 添加列表数据
							setMebListAdapter();
						}
						
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "ProductJoinMebActivity", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "ProductJoinMebActivity", "responseString="
								+ responseString);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back_buttons:
//			Intent it = new Intent(this, GrouponProductNewDetailActivity.class);
//			it.putExtra("productId", productId);
//			startActivity(it);
			finish();
			break;
		default:
			break;
		}
	}

}
