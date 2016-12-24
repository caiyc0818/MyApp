package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyAccountListAdapter;
import com.bcinfo.tripaway.bean.Account;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 我的账户
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月23日 11:26:11
 */
public class MyAccountActivity extends BaseActivity implements OnClickListener, OnItemClickListener, IXListViewListener
{
    private int pageNum = 1;

    private String pageSize = "10";

    /**
     * 下拉刷新
     */
    private boolean isPullRefresh = false;

    /**
     * 上拉加载更多
     */
    private boolean isLoadmore = false;
    private XListView myAccountListView;
    private ArrayList<Account> list;
    private MyAccountListAdapter adapter;
    private String amountStr;
    private int myposition = -1;
    private LinearLayout backbackTv;
    private RelativeLayout secondLayout;

    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);
        setContentView(R.layout.activity_my_account);
        setSecondTitle("添加账户");
        secondLayout = (RelativeLayout) findViewById(R.id.second_title);
        secondLayout.getBackground().setAlpha(255);
        AppManager.getAppManager().addActivity(this);
        backbackTv = (LinearLayout) findViewById(R.id.layout_back_button);
        backbackTv.setOnClickListener(this);
        myAccountListView = (XListView) findViewById(R.id.myaccount_listview);
        myAccountListView.setPullLoadEnable(false);
        myAccountListView.setPullRefreshEnable(true);
        myAccountListView.setXListViewListener(this);
        myAccountListView.setOnItemClickListener(this);
        list = new ArrayList<>();
        adapter = new MyAccountListAdapter(list, MyAccountActivity.this);
        myAccountListView.setAdapter(adapter);
        testAccountListUrl();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.layout_back_button:
            finish();
            activityAnimationClose();
            break;
        default:
            break;
        }

    }

    private void testAccountListUrl()
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);
        params.put("pagesize", pageSize);
        HttpUtil.get(Urls.payment_account_list_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                if (isLoadmore)
                {
                    // 上拉返回的结束加载更多布局
                    myAccountListView.stopLoadMore();
                }
                if ("00000".equals(response.optString("code")))
                {
                    if (isPullRefresh)
                    {
                        // 下拉刷新返回的
                        myAccountListView.successRefresh();
                    }
                    getAccountListInfo(response);
                }
                else if ("00099".equals(response.optString("code")))
                {
                    PreferenceUtil.delUserInfo();
                    UserInfoDB.getInstance().deleteAll();
                    goLoginActivity();
                }
                else
                {
                    if (isPullRefresh)
                    {
                        // 下拉刷新接口返回的数据不正确
                        myAccountListView.stopRefresh();
                    }
                    if (pageNum != 1)
                    {
                        pageNum--;
                    }
                }
                // 上拉 下拉的初始状态置为false;
                isLoadmore = false;
                isPullRefresh = false;

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {

                super.onFailure(statusCode, headers, responseString, throwable);
                isPullRefresh = false;
                isLoadmore = false;
                if (isLoadmore)
                {
                    myAccountListView.stopLoadMore();
                }
                if (isPullRefresh)
                {
                    myAccountListView.stopRefresh();
                }
                if (pageNum != 1)
                {
                    pageNum--;
                }
            }
        });

    }

    private void getAccountListInfo(JSONObject response)
    {

        JSONObject resultObj = response.optJSONObject("data");
        amountStr = resultObj.optString("amount"); // 账户金额
        JSONArray accountArray = resultObj.optJSONArray("accounts");
        ArrayList<Account> myaccount = new ArrayList<Account>();
        for (int i = 0; i < accountArray.length(); i++)
        {
            Account accountItem = new Account();
            accountItem.setId(accountArray.optJSONObject(i).optString("id")); // 账户标识
            accountItem.setNo(accountArray.optJSONObject(i).optString("no")); // 账户号
            accountItem.setIsdefault(accountArray.optJSONObject(i).optString("isdefault")); // 该账号是否默认
            accountItem.setType(accountArray.optJSONObject(i).optString("type")); // 账号的类型type
                                                                                  // '支付宝'
            myaccount.add(accountItem);

        }
        if (isPullRefresh)
        {
            list.clear();
        }
        if(isLoadmore)
        {
            list.remove(list.size()-1);
        }
        list.addAll(myaccount);
        list.add(new Account());
        if (myaccount.size() < 10)
        {
            myAccountListView.setPullLoadEnable(false);
        }
        else
        {
            pageNum++;
            myAccountListView.setPullLoadEnable(true);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        if (isPullRefresh)
        {
            return;
        }
        isPullRefresh = true;
        pageNum = 1;
        testAccountListUrl();
    }

    @Override
    public void onLoadMore()
    {
        // TODO Auto-generated method stub
        isLoadmore = true;
        testAccountListUrl();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 100 && resultCode == 200)
        {
            if (intent.getStringExtra("Default").equals("1") && myposition != -1)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    if (i == myposition - 1)
                    {
                        list.get(i).setIsdefault("1");
                    }
                    else
                    {
                        list.get(i).setIsdefault("0");
                    }
                }
                myposition = -1;
                adapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == 100 && resultCode == 300)
        {
            isPullRefresh = false;
            onRefresh();
        }else if (requestCode == 200 && resultCode == 500)
        {
            isPullRefresh = false;
            onRefresh();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (position == 0 || position + 2 > parent.getAdapter().getCount())
        {
            return;
        }
        if (list.size() > position)
        {
            Intent intent = new Intent(MyAccountActivity.this, AccountDefaultActivity.class);
            intent.putExtra("accountArg", list.get(position - 1)); // 账户
            myposition = position;
            startActivityForResult(intent, 100);// requestCode 100
        }
        else if (list.size() == position)
        {
            startActivityForResult(new Intent(this, AliPayBindedActivity.class),200);
        }
        activityAnimationOpen();
    }

}
