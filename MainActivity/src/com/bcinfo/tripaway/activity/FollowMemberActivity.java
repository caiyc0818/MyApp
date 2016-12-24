package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.FollowAdapter;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author hanweipeng
 * @date 2015-6-30 上午10:50:29
 */
public class FollowMemberActivity extends BaseActivity implements OnItemClickListener, OnClickListener
{
    private GridView followGridView;
    
    private FollowAdapter adapter;
    
    private List<UserInfo> userInfos;
    
    private String userId;
    
    private LinearLayout backLayout;// 新个人资料-后退按钮
    
    private LinearLayout moreLayout;// 新个人资料-更多
    
    private TextView titleText;// 标题
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.follow_member_activity);
        userId = getIntent().getStringExtra("userId");
        initView();
        queryFollowMemberInfo();
        
    }
    
    protected void initView()
    {
        backLayout = (LinearLayout)findViewById(R.id.layout_back_button);
        moreLayout = (LinearLayout)findViewById(R.id.layout_more_button);
        titleText = (TextView)findViewById(R.id.title_text);
        followGridView = (GridView)findViewById(R.id.follow_gridview);
        userInfos = new ArrayList<UserInfo>();
        adapter = new FollowAdapter(FollowMemberActivity.this, userInfos);
        followGridView.setAdapter(adapter);
        followGridView.setOnItemClickListener(this);
        backLayout.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
        titleText.setText("关注");
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.layout_back_button:
                finish();
                activityAnimationClose();
                break;
            case R.id.layout_more_button:
                Intent intentForMore = new Intent(this, PersonalInfoMoreActivity.class);
                intentForMore.putExtra("userInfo_id_more_value", userId);
                startActivity(intentForMore);
                activityAnimationOpen();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        
    }
    
    private void queryFollowMemberInfo()
    {
        try
        {
            RequestParams params = new RequestParams();
            params.put("userId", "" + userId);
            params.put("pagenum", "1");
            params.put("pagesize", "100");
            HttpUtil.get(Urls.personal_focus_url, params, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    System.out.println("获取关注接口=" + response);
                    if (response.optString("code").equals("00000"))
                    {
                        JSONObject dataJson = response.optJSONObject("data");
                        if (dataJson != null && !dataJson.equals(""))
                        {
                            JSONArray userArray = dataJson.optJSONArray("user");
                            userInfos.addAll(initFocusList(userArray));
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        ToastUtil.showErrorToast(FollowMemberActivity.this, response.optString("msg"));
                    }
                }
                
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    /**
     * 初始化关注或粉丝数据
     */
    protected List<UserInfo> initFocusList(JSONArray focusArray)
    {
        
        List<UserInfo> focusList = new ArrayList<UserInfo>();
        if (focusArray != null)
        {
            for (int i = 0; i < focusArray.length(); i++)
            {
                UserInfo focusUserInfo = new UserInfo();
                JSONObject userObj = focusArray.optJSONObject(i);
                JSONArray tagsArray = userObj.optJSONArray("tags");
                if (tagsArray != null)
                {
                    for (int j = 0; j < tagsArray.length(); j++)
                    {
                        focusUserInfo.getTags().add(tagsArray.optString(j));
                    }
                }
                focusUserInfo.setGender(userObj.optString("sex")); // 设置性别
                focusUserInfo.setStatus(userObj.optString("status")); // 设置 状态
                focusUserInfo.setEmail(userObj.optString("email")); // 设置 电子邮件
                focusUserInfo.setNickname(userObj.optString("nickName"));// 设置
                // 昵称
                focusUserInfo.setUserId(userObj.optString("userId")); // 设置userId
                if (!StringUtils.isEmpty(userObj.optString("avatar")))
                {
                    focusUserInfo.setAvatar(userObj.optString("avatar")); // 设置
                }
                // 头像
                focusUserInfo.setIntroduction(userObj.optString("introduction"));// 设置
                // 简介
                focusUserInfo.setMobile(userObj.optString("mobile"));
                focusList.add(focusUserInfo);
            }
        }
        
        return focusList;
        
    }
}
