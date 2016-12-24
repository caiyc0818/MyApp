package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.GrouponMemberGridViewAdapter;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.QueueMeb;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 团购产品人员列表
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年3月12日 下午5:13:56
 */
public class GrouponMemberActivity extends BaseActivity implements OnClickListener
{
    // GridView 显示全部主题
    private MyGridView myGridView;
    
    private MyGridView myGridView1;
    
    private RelativeLayout second_title;
    
    // 自定义上拉加载相对布局
    // private PullToRefreshLayout pullToRefreshLayout;
    
    // 全屏显示的半透明背景
    private int pageNum = 1;
    
    private int pageSize = 10;
    
    private boolean isPullRefresh = false;
    
    private boolean isLoadMore = false;
    
    // private GridView memberGridView;
    
    private Button signOutBtn;
    
    private String queueId;
    
    private GrouponMemberGridViewAdapter memberAdapter;
    
    private GrouponMemberGridViewAdapter memberAdapter1;
    
    private ArrayList<QueueMeb> memberList = new ArrayList<QueueMeb>();
    
    
    private ArrayList<QueueMeb> memberList1 = new ArrayList<QueueMeb>();
    
    private TextView mTextView;
    
    private TextView mTextView1;
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.groupon_member_activity);
        queueId = getIntent().getStringExtra("queueId");
        String title = getIntent().getStringExtra("title");
        setSecondTitle(title);
        second_title = (RelativeLayout) findViewById(R.id.second_title);
        second_title.getBackground().setAlpha(255);
        myGridView = (MyGridView)findViewById(R.id.members_gridView);
        myGridView1 = (MyGridView)findViewById(R.id.not_members_gridView);
        mTextView = (TextView) findViewById(R.id.pro_meb);
        mTextView1 =(TextView) findViewById(R.id.not_pro_meb);
        // pullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.members_container);
        // pullToRefreshLayout.setOnRefreshListener(new MyListener());
        signOutBtn = (Button)findViewById(R.id.sign_out_btn);
        signOutBtn.setOnClickListener(this);
        initServiceGridView();
        queryMemebers();
    }
    
    private void initServiceGridView()
    {
        // for (int i = 0; i < 8; i++)
        // {
        // UserInfo user = new UserInfo();
        // user.setAvatar("http://dh2.kimg.cn/static/images/public/xiaohua/20150118/CgAAhlS4y8qAK-kbAAC-3x4rIYs685.jpg");
        // user.setNickname("行程规划");
        // memberList.add(user);
        // }
        memberAdapter = new GrouponMemberGridViewAdapter(this, memberList);
        myGridView.setAdapter(memberAdapter);
        memberAdapter1 = new GrouponMemberGridViewAdapter(this,memberList1);
        myGridView1.setAdapter(memberAdapter1);
    }
    
    /**
     * 定义一个实现OnRefreshListener接口的实现类对象
     * 
     * @author caihelin
     * 
     */
    // class MyListener implements OnRefreshListener
    // {
    //
    // @Override
    // public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
    // {
    // // 加载操作
    // isLoadMore = true;
    // // testAllTopicsUrl();
    // }
    //
    // @Override
    // public void onRefresh(PullToRefreshLayout pullToRefreshLayout)
    // {
    // // TODO Auto-generated method stub
    // // topicsList.clear();
    // isPullRefresh = true;
    // pageNum = 1;
    // // testAllTopicsUrl();
    // }
    //
    // }
    
    private void queryMemebers()
    {
        RequestParams params = new RequestParams();
        params.put("queueId", queueId);
        params.put("pagenum", pageNum);
        params.put("pagesize", 100);
        HttpUtil.get(Urls.message_member_url, params, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastUtil.showToast(GrouponMemberActivity.this, "statusCode=" + statusCode);
                // if (isLoadMore)
                // {
                // pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                // }
                // if (isPullRefresh)
                // {
                // pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                // }
                // isLoadMore = false;
                // isPullRefresh = false;
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(GrouponMemberActivity.this, "statusCode=" + statusCode);
                // if (isLoadMore)
                // {
                // pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                // }
                // if (isPullRefresh)
                // {
                // pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                // }
                // isLoadMore = false;
                // isPullRefresh = false;
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("GrouponMemberActivity", "查询群聊成员接口=", response.toString());
                String code = response.optString("code");
                if (code.equals("00000"))
                {
                    JSONObject dataJson = response.optJSONObject("data");
                    if (dataJson != null && dataJson.length() > 0)
                    {
                        JSONArray memberArray = dataJson.optJSONArray("senders");
                        if (memberArray != null && memberArray.length() > 0)
                        {
                            ArrayList<QueueMeb> tempList = new ArrayList<QueueMeb>();
                            ArrayList<QueueMeb> tempList1 = new ArrayList<QueueMeb>();
                            for (int i = 0; i < memberArray.length(); i++)
                            {
                                JSONObject user1 = memberArray.optJSONObject(i);
                                QueueMeb meb = new QueueMeb();
                                UserInfo senderInfo = new UserInfo();
                                meb.setMemberType(user1.optString("memberType"));
                                meb.setInfo(senderInfo);
                                JSONObject userJson = user1.optJSONObject("userInfo");
                                senderInfo.setGender(userJson.optString("sex"));
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
                                if("serv".equals(meb.getMemberType())){
                                	tempList.add(meb);
                                }else{
                                	tempList1.add(meb);
                                }
                            }
                            memberList.addAll(tempList);
                            memberAdapter.notifyDataSetChanged();
                            if(memberList.size()>0){
                            	mTextView.setVisibility(View.VISIBLE);
                            	mTextView.setText("导游、司机领队（"+memberList.size()+"）");
                            }else{
                            	mTextView.setVisibility(View.GONE);
                            }
                            memberList1.addAll(tempList1);
                            memberAdapter1.notifyDataSetChanged();
                            if(memberList1.size()>0){
                            	mTextView1.setVisibility(View.VISIBLE);
                            	mTextView1.setText("群成员（"+memberList1.size()+"）");
                            }else{
                            	mTextView1.setVisibility(View.GONE);
                            }
                        }
                    }
                    
                }
                else
                {
//                    ToastUtil.showToast(GrouponMemberActivity.this, "errorCode=" + code);
                }
            }
            
        });
    }
    
    private void signOutChat()
    {
        HttpUtil.delete(Urls.drop_group_url + queueId, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastUtil.showToast(GrouponMemberActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(GrouponMemberActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("GrouponMemberActivity", "退出群聊接口=", response.toString());
                String code = response.optString("code");
                if (code.equals("00000"))
                {
//                    ToastUtil.showToast(GrouponMemberActivity.this, "退出成功");
                	Intent it = new Intent("com.bcinfo.tripaway.exitteamTalk");
                	sendBroadcast(it);
                	  Intent intent = new Intent("com.bcinfo.refreshUnread");
                      sendBroadcast(intent);
                	finish();
                }
                else
                {
//                    ToastUtil.showToast(GrouponMemberActivity.this, "errorCode=" + code);
                }
            }
            
        });
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.sign_out_btn:
                signOutChat();
                break;
            
            default:
                break;
        }
    }
}
