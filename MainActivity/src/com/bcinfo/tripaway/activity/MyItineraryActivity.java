package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyItineraryGridAdapter;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 我的行程单
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月29日 上午10:46:36
 */
public class MyItineraryActivity extends BaseActivity
{
    protected static final String TAG = "MyItineraryActivity";
    
    private GridView mMyItineraryGV;
    
    private MyItineraryGridAdapter mMyItineraryAdapter;
    
    private ArrayList<HashMap<String, String>> mItineraryList = new ArrayList<HashMap<String, String>>();
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.my_itinerary_activity);
        setSecondTitle("我的行程单");
        mMyItineraryGV = (GridView)findViewById(R.id.my_itinerary_lv);
        initMyItineraryGV();
        queryMyItinerary();
    }
    
    /*
     * 测试 行程单列表(游客)url
     */
    private void queryMyItinerary()
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", "1");// 当前页码
        params.put("pagesize", "10");// 总页数
        HttpUtil.get(Urls.myItinerary_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println("行程单接口=" + response);
                JSONArray dataList = response.optJSONArray("data");
                if (dataList == null)
                {
                    return;
                }
                mItineraryList.clear();
                for (int i = 0; i < dataList.length(); i++)
                {
                    JSONObject subJson = dataList.optJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", subJson.optString("id"));
                    map.put("leave_time", subJson.optString("beginDate"));
                    map.put("product_title", subJson.optString("title"));
                    JSONObject productJson = subJson.optJSONObject("product");
                    if (productJson != null)
                    {
                        if (!StringUtils.isEmpty(productJson.optString("titleImg")))
                        {
                            map.put("product_photo", productJson.optString("titleImg"));
                        }
                        else
                        {
                            map.put("product_photo", "");
                        }
                        
                    }
                    mItineraryList.add(map);
                }
                mMyItineraryAdapter.notifyDataSetChanged();
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                LogUtil.i(TAG, "onFailure", "statusCode=" + statusCode + "--responseString=" + responseString);
            }
        });
    }
    
    private void initMyItineraryGV()
    {
        mMyItineraryAdapter = new MyItineraryGridAdapter(this, mItineraryList);
        mMyItineraryGV.setAdapter(mMyItineraryAdapter);
        mMyItineraryGV.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // TODO Auto-generated method stub
                HashMap<String, String> map = (HashMap<String, String>)parent.getAdapter().getItem(position);
                Intent intent = new Intent(MyItineraryActivity.this, MyItineraryDetailActivity.class);
                intent.putExtra("id", map.get("id"));
                startActivity(intent);
            }
        });
    }
}
