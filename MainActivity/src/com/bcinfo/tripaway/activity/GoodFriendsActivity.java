package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.FriendShipListAdapter;
import com.bcinfo.tripaway.bean.Friend;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 好友
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月24日 17:52:22
 */
public class GoodFriendsActivity extends BaseActivity
{
    private MyListView friendShipListView;
    
    private List<Friend> friendList;
    
    private FriendShipListAdapter friendShipAdapter;
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        
        setContentView(R.layout.activity_my_friendships_layout);
        setSecondTitle("好友");
        // initFriendList();
        friendShipListView = (MyListView)findViewById(R.id.my_friendship_listview);
        friendList = new ArrayList<Friend>();
        friendShipAdapter = new FriendShipListAdapter(this, friendList);
        friendShipListView.setAdapter(friendShipAdapter);
        friendShipListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // TODO Auto-generated method stub
                Intent intentForIndividual = new Intent(GoodFriendsActivity.this, PersonalInfoNewActivity.class);
                intentForIndividual.putExtra("identifyId", friendList.get(position).getUserId());
                startActivity(intentForIndividual);
                activityAnimationOpen();
            }
        });
        testFriendsListUrl();
    }
    
    private void testFriendsListUrl()
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", "1");
        params.put("pagesize", "100");
        params.put("keyword", "");
        HttpUtil.get(Urls.friend_list_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                System.out.println("好友列表=" + response);
                if (response.optString("code").equals("00000"))
                {
                    JSONArray dataArray = response.optJSONArray("data");
                    if (dataArray != null && dataArray.length() > 0)
                    {
                        for (int i = 0; i < dataArray.length(); i++)
                        {
                            JSONObject dataJson = dataArray.optJSONObject(i);
                            Friend friend = new Friend();
                            friend.setUserId(dataJson.optString("userId"));
                            friend.setStatus(dataJson.optString("status"));
                            friend.setNickName(dataJson.optString("nickName"));
                            friend.setAvatar(dataJson.optString("avatar"));
                            friend.setArea(dataJson.optString("area"));
                            friend.setGender(dataJson.optString("sex"));
                            friendList.add(friend);
                        }
                        friendShipAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    ToastUtil.showErrorToast(GoodFriendsActivity.this, response.optString("msg"));
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(responseString);
            }
        });
    }
}
