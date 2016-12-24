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
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 旅行时间 (某个月份) 下的产品列表Activity
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年6月9日 16:36:11
 * 
 */
public class TravelPeriodProductListActivity extends BaseActivity
{
    private ListView travelPeriodMonthProductListView;
    
    private ProductAdapter proAdapter;
    
    private int pageNum = 1;
    
    private String pageSize = "10";
    
    private List<ProductNewInfo> productList = new ArrayList<ProductNewInfo>();
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.activity_travel_period_month_product_list);
        String monthValue = getIntent().getStringExtra("monthValue");
        String monthId = getIntent().getStringExtra("monthId");
        setSecondTitle(monthValue + "份的产品");
        travelPeriodMonthProductListView = (ListView)findViewById(R.id.travel_period_month_product_listview);
        proAdapter = new ProductAdapter(this, productList);
        travelPeriodMonthProductListView.setAdapter(proAdapter);
        testMonthProductPushResource(monthId);
    }
    
    private void testMonthProductPushResource(String monthId)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);
        params.put("pagesize", pageSize);
        HttpUtil.get(Urls.single_topic_product_url + monthId, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                if ("00000".equals(response.optString("code")))
                {
                    querySingletravelProduct(response);
                }
                // LogUtil.i("", "Destination_TravelPeriodProductListActivity_testMonthProductPushResource",
                // "statusCode=" + statusCode);
                // LogUtil.i("", "Destination_TravelPeriodProductListActivity_testMonthProductPushResource",
                // "responseString=" + response);
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // LogUtil.i("", "Destination_TravelPeriodProductListActivity_testMonthProductPushResource",
                // "statusCode=" + statusCode);
                // LogUtil.i("", "Destination_TravelPeriodProductListActivity_testMonthProductPushResource",
                // "responseString=" + responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
                
            }
        });
    }
    
    /*
     * 获得某一个旅行时段(月份)的产品列表
     */
    private void querySingletravelProduct(JSONObject response)
    {
        JSONArray topicProductDataArray = response.optJSONArray("data");
        if (topicProductDataArray != null && topicProductDataArray.length() != 0)
        {
            for (int i = 0; i < topicProductDataArray.length(); i++)
            {
                JSONObject productObj = topicProductDataArray.optJSONObject(i);
                ProductNewInfo productNewInfo = new ProductNewInfo();
                productNewInfo.setId(productObj.optString("id"));
                productNewInfo.setExpStart(productObj.optString("expStart"));
                productNewInfo.setExpend(productObj.optString("expend"));
                productNewInfo.setProductType(productObj.optString("productType"));
                productNewInfo.setDistance(productObj.optString("distance"));
                productNewInfo.setTitle(productObj.optString("title"));
                productNewInfo.setPoiCount(productObj.optString("poiCount"));
                productNewInfo.setPrice(productObj.optString("price"));
                productNewInfo.setDays(productObj.optString("days"));
                productNewInfo.setDescription(productObj.optString("description"));
                productNewInfo.setPriceMax(productObj.optString("priceMax"));
                productNewInfo.setTitleImg(productObj.optString("titleImg"));
                productNewInfo.setMaxMember(productObj.optString("maxMember"));
                ArrayList<String> topics = new ArrayList<String>();
                JSONArray topicJsonArray = productObj.optJSONArray("topics");
                if (topicJsonArray != null && topicJsonArray.length() > 0)
                {
                    for (int j = 0; j < topicJsonArray.length(); j++)
                    {
                        LogUtil.i("ThemeProductListActivity", "topicJsonArray", topicJsonArray.optString(j));
                        topics.add(topicJsonArray.optString(j));
                    }
                    productNewInfo.setTopics(topics);
                }
                productList.add(productNewInfo);
            }
            proAdapter.notifyDataSetChanged();
            travelPeriodMonthProductListView.setOnItemClickListener(new OnItemClickListener()
            {
                
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
                {
                    Intent intentForProductDetail =
                        new Intent(TravelPeriodProductListActivity.this, GrouponProductNewDetailActivity.class);
                    intentForProductDetail.putExtra("productId", (String)productList.get(position).getId());
                    startActivity(intentForProductDetail);
                    
                }
                
            });
        }
    }
    
}
