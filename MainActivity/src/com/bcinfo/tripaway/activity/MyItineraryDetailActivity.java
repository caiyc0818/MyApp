package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.DayButtonListAdapter;
import com.bcinfo.tripaway.adapter.ItineraryListAdapter;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 我的行程规划详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月30日 上午9:32:46
 */
public class MyItineraryDetailActivity extends BaseActivity implements OnClickListener
{
    protected static final String TAG = "MyItineraryDetailActivity";
    
    private DayButtonListAdapter mDayButtonListAdapter;
    
    private ArrayList<HashMap<String, String>> mDayButtonDataList = new ArrayList<HashMap<String, String>>();
    
    private ItineraryListAdapter mItineraryListAdapter;
    
    private ArrayList<String> mItineraryaDatList = new ArrayList<String>();
    
    private ListView itinerary_listview;
    
    private boolean isScroll = false;
    
    private String id = "";
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.my_itinerary_detail_activity);
        ImageView button_operation = (ImageView)findViewById(R.id.button_operation);
        LinearLayout layout_back_button = (LinearLayout)findViewById(R.id.layout_back_button);
        TextView title_text = (TextView)findViewById(R.id.title_text);
        button_operation.setOnClickListener(this);
        layout_back_button.setOnClickListener(this);
        id = getIntent().getStringExtra("id");
        initItineraryLV();
        initDayButtonLV();
        queryItineraryDetail();
    }
    
    /*
     * 测试 行程单详情(游客)
     */
    private void queryItineraryDetail()
    {
        RequestParams params = new RequestParams();
        params.put("itineraryId", id);
        HttpUtil.get(Urls.myItinerary_detail_url + id, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "queryItineraryDetail", "response=" + response.toString());
                JSONArray dataList = response.optJSONArray("data");
                // FileUtils.SetTxt(response.toString(), "queryItineraryDetail.txt", true);
                if (dataList == null)
                {
                    return;
                }
                for (int i = 0; i < dataList.length(); i++)
                {
                    
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                System.out.println(statusCode);
                System.out.println(responseString.toString());
            }
        });
    }
    
    /**
     * 初始化行程规划列表
     */
    private void initItineraryLV()
    {
        for (int i = 0; i < 4; i++)
        {
            String day = "D" + (i + 1);
            mItineraryaDatList.add(day);
        }
        mItineraryListAdapter = new ItineraryListAdapter(this, mItineraryaDatList);
        itinerary_listview = (ListView)findViewById(R.id.itinerary_listview);
        itinerary_listview.setAdapter(mItineraryListAdapter);
        itinerary_listview.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                // TODO Auto-generated method stub
                LogUtil.i(TAG, "onScrollStateChanged", "scrollState=" + scrollState);
                if (scrollState == 1)
                {
                    isScroll = true;
                }
                else
                {
                    isScroll = false;
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                // TODO Auto-generated method stub
                if (isScroll)
                {
                    // LogUtil.i(TAG, "onScroll", "firstVisibleItem=" +
                    // firstVisibleItem);
                    for (int i = 0; i < mDayButtonDataList.size(); i++)
                    {
                        HashMap<String, String> map = mDayButtonDataList.get(i);
                        if (i == firstVisibleItem)
                        {
                            map.put("isChecked", "true");
                        }
                        else
                        {
                            map.put("isChecked", "false");
                        }
                    }
                    if (mDayButtonListAdapter != null)
                        mDayButtonListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    
    /**
     * 初始化日期跳转按钮列表
     */
    private void initDayButtonLV()
    {
        ListView day_button_lv = (ListView)findViewById(R.id.day_button_lv);
        for (int i = 0; i < mItineraryaDatList.size(); i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("day", "D" + (i + 1));
            if (i == 0)
            {
                map.put("isChecked", "true");
            }
            else
            {
                map.put("isChecked", "false");
            }
            mDayButtonDataList.add(map);
        }
        mDayButtonListAdapter = new DayButtonListAdapter(this, mDayButtonDataList);
        day_button_lv.setAdapter(mDayButtonListAdapter);
        day_button_lv.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // TODO Auto-generated method stub
                for (int i = 0; i < mDayButtonDataList.size(); i++)
                {
                    HashMap<String, String> map = mDayButtonDataList.get(i);
                    if (i == position)
                    {
                        map.put("isChecked", "true");
                    }
                    else
                    {
                        map.put("isChecked", "false");
                    }
                }
                mDayButtonListAdapter.notifyDataSetChanged();
                itinerary_listview.setSelection(position);
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.button_operation:
                intent = new Intent(this, ProductAuthorBriefActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_back_button:
                finish();
                break;
            default:
                break;
        }
    }
}
