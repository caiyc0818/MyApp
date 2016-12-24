package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyGoWithListAdapter;
import com.bcinfo.tripaway.bean.GoWithNew;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.ComListViewFooter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 我的结伴
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月28日 下午5:00:16
 */
public class MyGoWithActivity extends BaseActivity implements OnClickListener
{
    protected static final String TAG = "MyGoWithActivity";
    
    private ImageView addGoWithButton;
    
    private ListView mGoWithListview;
    
    private ArrayList<GoWithNew> mGoWithList = new ArrayList<GoWithNew>();
    
    private MyGoWithListAdapter latestAdapter;
    
    private boolean isLatestLoadOver = false;
    
    private boolean isLatestLoading = false;
    
    private ComListViewFooter mListViewFooter;
    
    private int pagenum = 1;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_go_with_activity);
        setSecondTitle("我的结伴");
        addGoWithButton = (ImageView)findViewById(R.id.add_go_with_button);
        addGoWithButton.setOnClickListener(this);
        mGoWithListview = (ListView)findViewById(R.id.go_with_listview);
        initGoWithListView();
        queryMyGoWith(pagenum);
    }
    
    private void initGoWithListView()
    {
        mListViewFooter = new ComListViewFooter(this);
        mGoWithListview.addFooterView(mListViewFooter);
        latestAdapter = new MyGoWithListAdapter(this, mGoWithList);
        mGoWithListview.setAdapter(latestAdapter);
        mGoWithListview.setOnItemClickListener(onItemClickListener);
        mGoWithListview.setOnScrollListener(mOnScrollListener);
    }
    
    private OnItemClickListener onItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // TODO Auto-generated method stub
            GoWithNew info = (GoWithNew)parent.getAdapter().getItem(position);
            if (info != null)
            {
                if (info.getComeFrom() != null && !info.getComeFrom().isEmpty())
                {
                    Intent intent = new Intent(MyGoWithActivity.this, InviteGoWithDetailActivity.class);
                    GoWithNew goWith = (GoWithNew)parent.getAdapter().getItem(position);
                    intent.putExtra("goWithNew", goWith);
                    startActivity(intent);
                    activityAnimationOpen();
                }
                else
                {
                    Intent intent = new Intent(MyGoWithActivity.this, MyGoWithDetailActivity.class);
                    GoWithNew goWith = (GoWithNew)parent.getAdapter().getItem(position);
                    intent.putExtra("goWithNew", goWith);
                    startActivity(intent);
                    activityAnimationOpen();
                }
            }
        }
    };
    
    private OnScrollListener mOnScrollListener = new OnScrollListener()
    {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {
            // TODO Auto-generated method stub
        }
        
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {
            // TODO Auto-generated method stub
            if (firstVisibleItem + visibleItemCount > latestAdapter.getCount())
            {
                if (firstVisibleItem + visibleItemCount >= 10)
                {
                    if (!isLatestLoadOver && !isLatestLoading)
                    {
                        isLatestLoading = true;
                        queryMyGoWith(pagenum);
                    }
                }
            }
        }
    };
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.add_go_with_button:
                intent = new Intent(this, AddGoWithActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                break;
            default:
                break;
        }
    }
    
    /****************** request *******************/
    private void queryMyGoWith(int index)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", "" + index);
        params.put("pagesize", "10");
        HttpUtil.get(Urls.my_go_with, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "queryMyGoWith", "statusCode=" + statusCode);
                LogUtil.i(TAG, "queryMyGoWith", "response=" + response.toString());
                if (!response.optString("code").equals("00000"))
                {
                    pagenum--;
                    ToastUtil.showErrorToast(MyGoWithActivity.this,
                        response.optString("msg") );
                    return;
                }
                JSONArray dataJSONList = response.optJSONArray("data");
                if (dataJSONList == null || dataJSONList.length() == 0)
                {
                    isLatestLoadOver = true;
                    mListViewFooter.setState(ComListViewFooter.STATE_FINISHED);
                    return;
                }
                else if (dataJSONList.length() < 10)
                {
                    isLatestLoadOver = true;
                    mListViewFooter.setState(ComListViewFooter.STATE_FINISHED);
                }
                else
                {
                    pagenum++;
                }
                if (dataJSONList != null && dataJSONList.length() > 0)
                {
                    for (int i = 0; i < dataJSONList.length(); i++)
                    {
                        GoWithNew goWith = new GoWithNew();
                        JSONObject goWithJSON1 = dataJSONList.optJSONObject(i);
                        JSONObject togetherJSONList = goWithJSON1.optJSONObject("together");
                        goWith.setId(togetherJSONList.optString("id"));
                        goWith.setTitle(togetherJSONList.optString("title"));
                        goWith.setCreateTime(togetherJSONList.optString("createTime"));
                        goWith.setDescription(togetherJSONList.optString("description"));
                        goWith.setApplyNum(togetherJSONList.optString("applyNum"));
                        goWith.setJoinNum(togetherJSONList.optString("joinNum"));
                        goWith.setStartPoint(togetherJSONList.optString("startPoint"));
                        goWith.setEndPoint(togetherJSONList.optString("endPoint"));
                        goWith.setStatus(togetherJSONList.optString("status"));
                        goWith.setPlanTime(togetherJSONList.optString("planTime"));
                        JSONObject userJson = togetherJSONList.optJSONObject("sponsor");
                        if (userJson != null)
                        {
                            goWith.getUser().setUserId(userJson.optString("userId"));
                            goWith.getUser().setGender(userJson.optString("sex"));
                            goWith.getUser().setNickname(userJson.optString("nickName"));
                            goWith.getUser().setAvatar(userJson.optString("avatar"));
                            goWith.getUser().setIntroduction(userJson.optString("introduction"));
                            goWith.getUser().setMobile(userJson.optString("mobile"));
                            goWith.getUser().setStatus(userJson.optString("status"));
                            goWith.getUser().setEmail(userJson.optString("email"));
                            JSONArray tagsJSONList = userJson.optJSONArray("tags");
                            if (tagsJSONList != null)
                                for (int k = 0; k < tagsJSONList.length(); k++)
                                {
                                    goWith.getUser().getTags().add(tagsJSONList.opt(k).toString());
                                }
                        }
                        mGoWithList.add(goWith);
                    }
                    isLatestLoading = false;
                    latestAdapter.notifyDataSetChanged();
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                LogUtil.i(TAG, "queryMyGoWith", "statusCode=" + statusCode);
                LogUtil.i(TAG, "queryMyGoWith", "responseString=" + responseString);
                pagenum--;
            }
        });
    }
}
