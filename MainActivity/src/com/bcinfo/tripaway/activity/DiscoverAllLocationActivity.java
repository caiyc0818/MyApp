package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductLocationsGridAdapter;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout.OnRefreshListener;
import com.bcinfo.tripaway.view.refreshandload.PullableGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 发现-全部目的地
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月12日 10:32:22
 */
public class DiscoverAllLocationActivity extends BaseActivity implements OnItemClickListener
{
    // GridView 显示全部目的地
    private PullableGridView allLocationsGridView;
    
    private PullToRefreshLayout pullToRefreshLayout;
    
    // list集合
    private ArrayList<TripDestination> locationList = new ArrayList<TripDestination>();
    
    // 全屏显示的半透明背景
    private int pageNum = 1;
    
    private int pageSize = 10;
    
    private boolean isPullRefresh = false;
    
    private boolean isLoadMore = false;
    
    private ProductLocationsGridAdapter pLocationAdapter;
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.discovery_locations_all);
        this.discoverTitle = (TextView)findViewById(R.id.discovery_title_text);
        // 主题
        discoverTitle.setText(R.string.discover_title_locations);
        discoverLayout = (LinearLayout)findViewById(R.id.discovery_discover_container);
        pullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.allLocations_container);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
        // 标题栏右边的放大镜
        discoverThemeIcon = (ImageView)findViewById(R.id.discovery_discover_button);
        discoverThemeIcon.setVisibility(View.GONE);
        discoverLayout.setVisibility(View.GONE);
        navBack = (ImageView)findViewById(R.id.discovery_back_button);
        navBack.setOnClickListener(mOnClickListener);
        discoverThemeIcon.setOnClickListener(mOnClickListener);
        discoverThemeIcon = (ImageView)findViewById(R.id.discovery_discover_button);
        
        allLocationsGridView = (PullableGridView)findViewById(R.id.discovery_allLocations_gridView);
        
        pLocationAdapter = new ProductLocationsGridAdapter(this, locationList);
        
        allLocationsGridView.setAdapter(pLocationAdapter);
        testAllDestinationsUrl();
        
        allLocationsGridView.setOnItemClickListener(this);
    }
    
    private void testAllDestinationsUrl()
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);
        params.put("pagesize", pageSize);
        params.put("areaLevel", "1");
        HttpUtil.get(Urls.destinations_all_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("", "全部目的地接口=", "responseString=" + response);
                getLocationList(response);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(DiscoverAllLocationActivity.this, "statusCode=" + statusCode);
                if (isLoadMore)
                {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                if (isPullRefresh)
                {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
                isLoadMore = false;
                isPullRefresh = false;
            }
        });
    }
    
    private void getLocationList(JSONObject response)
    {
        String code = response.optString("code");
        if ("00000".equals(code))
        {
            if (isLoadMore)
            {
                // 上拉返回的结束加载更多布局
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
            if (isPullRefresh)
            {
                // 下拉刷新返回的
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
            JSONArray locationArray = response.optJSONArray("data");
            if (locationArray == null || locationArray.length() == 0)
            {
                return;
            }
            ArrayList<TripDestination> tempLocationList = new ArrayList<TripDestination>();
            for (int i = 0; i < locationArray.length(); i++)
            {
                JSONObject locationObj = locationArray.optJSONObject(i);
                TripDestination tripDestination = new TripDestination();
                tripDestination.setDestProNum(locationArray.optJSONObject(i).optString("pNum"));
                tripDestination.setDestId(locationArray.optJSONObject(i).optString("id"));
                tripDestination.setDestSupNum(locationArray.optJSONObject(i).optString("sNum"));
                if (!StringUtils.isEmpty(locationArray.optJSONObject(i).optString("logo")))
                {
                    tripDestination.setDestLogoKey(locationArray.optJSONObject(i).optString("logo"));
                }
                tripDestination.setDestName(locationArray.optJSONObject(i).optString("destName"));
                tripDestination.setDestDescription(locationArray.optJSONObject(i).optString("description"));
                tripDestination.setDestNameEn(locationArray.optJSONObject(i).optString("destNameEn"));
                tempLocationList.add(tripDestination);
                JSONArray subDestArray = locationObj.optJSONArray("subDest");
                if (subDestArray != null)
                {
                    for (int j = 0; j < subDestArray.length(); j++)
                    {
                        TripDestination subDestionation = new TripDestination(); // 子目的地
                        subDestionation.setDestProNum(subDestArray.optJSONObject(j).optString("pNum"));
                        subDestionation.setDestId(subDestArray.optJSONObject(j).optString("id"));
                        subDestionation.setDestSupNum(subDestArray.optJSONObject(j).optString("sNum"));
                        if (!StringUtils.isEmpty(subDestArray.optJSONObject(j).optString("logo")))
                        {
                            subDestionation.setDestLogoKey(subDestArray.optJSONObject(j).optString("logo"));
                        }
                        
                        subDestionation.setDestName(subDestArray.optJSONObject(j).optString("destName"));
                        subDestionation.setDestDescription(subDestArray.optJSONObject(j).optString("description"));
                        subDestionation.setDestNameEn(subDestArray.optJSONObject(j).optString("destNameEn"));
                        tempLocationList.add(subDestionation); // 添加子目的地
                    }
                }
            }
            if (tempLocationList.size() < 10)
            {
                allLocationsGridView.setPullLoadEnable(false);
            }
            else
            {
                pageNum++;
                allLocationsGridView.setPullLoadEnable(true);
            }
            locationList.addAll(tempLocationList);
            pLocationAdapter.notifyDataSetChanged();
        }
        else
        {
//            ToastUtil.showToast(DiscoverAllLocationActivity.this, "errorCode=" + code);
            if (isLoadMore)
            {
                // 上拉返回的结束加载更多布局
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            }
            if (isPullRefresh)
            {
                // 下拉刷新返回的
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            }
            if (pageNum != 1)
            {
                pageNum--;
            }
        }
        isLoadMore = false;
        isPullRefresh = false;
    }
    
    /**
     * 点击gridView的某一项后进入单一主题产品列表页面
     */
    @Override
    public void onItemClick(AdapterView<?> parentView, View view, int position, long id)
    {
        
        // 将点击的item的主题id赋给intent
        /*
         * Intent intentForThemeList=new Intent(this,ThemeProductListActivity.class);
         * intentForThemeList.putExtra("themeId", ((ProductTheme)view.getTag(R.id.tag_theme)).getThemeId());
         * intentForThemeList.putExtra("themeName", ((ProductTheme)view.getTag(R.id.tag_theme)).getThemeName());
         * startActivity(intentForThemeList); activityAnimationOpen();
         */
        String destId = locationList.get(position).getDestId();// 目的地标识
        Intent intentForLocateCountry =
            new Intent(DiscoverAllLocationActivity.this, LocationCountryDetailActivity.class);
        intentForLocateCountry.putExtra("destId", destId);
        startActivity(intentForLocateCountry);
    }
    
    /**
     * 定义一个实现OnRefreshListener接口的实现类对象
     * 
     * @author caihelin
     * 
     */
    class MyListener implements OnRefreshListener
    {
        
        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
        {
            isLoadMore = true;
            testAllDestinationsUrl();
        }
        
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout)
        {
            // TODO Auto-generated method stub
            locationList.clear();
            isPullRefresh = true;
            pageNum = 1;
            testAllDestinationsUrl();
        }
        
    }
    
}
