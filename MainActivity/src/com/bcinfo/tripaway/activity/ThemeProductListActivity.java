package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 单一主题产品列表
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年2月5日 9:52:22
 */
public class ThemeProductListActivity extends BaseActivity implements IXListViewListener
{
    
    private String themeId; // 特定的主题id
    
    private List<ProductNewInfo> productList;
    
    private XListView productListView;
    
    private ProductAdapter productAdapter;
    
    private int pageNum = 1;
    
    private String pageSize = "10";
    
    private String themeName;
    
    /**
     * 下拉刷新
     */
    private boolean isPullRefresh = false;
    
    /**
     * 上拉加载更多
     */
    private boolean isLoadmore = false;
    
    private WebView webView;
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        this.setContentView(R.layout.discovery_themes_products_list);
        String description = this.getIntent().getStringExtra("description");
        themeName = this.getIntent().getStringExtra("themeName");
        themeId = this.getIntent().getStringExtra("themeId");
        productListView = (XListView)findViewById(R.id.single_theme_product_listview);
        productListView.setPullLoadEnable(false);
        productListView.setPullRefreshEnable(true);
        productListView.setXListViewListener(this);
        discoverThemeIcon = (ImageView)findViewById(R.id.discovery_discover_button);
        discoverLayout = (LinearLayout)findViewById(R.id.discovery_discover_container);
        testSingleTopicProductUrl(pageNum, themeId);
        // ThemeProductGridAdapter gridAdapter = new
        // ThemeProductGridAdapter(this, productList);
        
        discoverTitle = (TextView)findViewById(R.id.discovery_title_text);
        discoverTitle.setText(themeName);
        navBack = (ImageView)findViewById(R.id.discovery_back_button);
        discoverThemeIcon.setOnClickListener(mOnClickListener);
        navBack.setOnClickListener(mOnClickListener);
        
        productList = new ArrayList<ProductNewInfo>();
        addHeaderView(description);
        productAdapter = new ProductAdapter(this, productList);
        productListView.setAdapter(productAdapter);
        productListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Intent intent = null;
               
                ProductNewInfo productNewInfo =  (ProductNewInfo) adapterView.getAdapter().getItem(position);
                LogUtil.i("ThemeProductListActivity", "onItemClick", "position=" + position);
                if (productNewInfo.getProductType().equals("single"))
                {
                    if (productNewInfo.getServices().equals("traffic"))
                    {
                        intent = new Intent(ThemeProductListActivity.this, CarProductDetailActivity.class);
                        intent.putExtra("productId", productNewInfo.getId());
                    }
                    else if (productNewInfo.getServices().equals("stay"))
                    {
                        intent = new Intent(ThemeProductListActivity.this, HouseProductDetailActivity.class);
                        intent.putExtra("productId", productNewInfo.getId());
                    }
                    else
                    {
                        intent = new Intent(ThemeProductListActivity.this, GrouponProductNewDetailActivity.class);
                        intent.putExtra("productId", productNewInfo.getId());
                    }
                }
                else if (productNewInfo.getProductType().equals("base")
                    || productNewInfo.getProductType().equals("customization"))
                {
                    intent = new Intent(ThemeProductListActivity.this, GrouponProductNewDetailActivity.class);
                    intent.putExtra("productId", productNewInfo.getId());
                }
                else if (productNewInfo.getProductType().equals("team"))
                {
                    intent = new Intent(ThemeProductListActivity.this, GrouponProductNewDetailActivity.class);
                    intent.putExtra("productId", productNewInfo.getId());
                }
                startActivity(intent);
                activityAnimationOpen();
            }
            
        });
        discoverLayout.setVisibility(View.GONE);
        
    }
    
    private void testSingleTopicProductUrl(int page, String themeId)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", page);
        params.put("pagesize", pageSize);
        HttpUtil.get(Urls.single_topic_product_url + themeId, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                System.out.println("主题产品列表接口=" + response);
                if (isLoadmore)
                {
                    // 上拉返回的结束加载更多布局
                    productListView.stopLoadMore();
                }
                querySingleTopicProduct(response);
                // System.out.println("主题产品列表接口=" + response);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println(errorResponse);
//                ToastUtil.showToast(ThemeProductListActivity.this, "statusCode=" + statusCode);
                isPullRefresh = false;
                isLoadmore = false;
                if (isLoadmore)
                {
                    productListView.stopLoadMore();
                }
                if (isPullRefresh)
                {
                    productListView.stopRefresh();
                }
                if (pageNum != 1)
                {
                    pageNum--;
                }
            }
        });
    }
    
    private void querySingleTopicProduct(JSONObject response)
    {
        if (response.optString("code").equals("00000"))
        {
            if (isPullRefresh)
            {
                // 下拉刷新返回的
                productListView.successRefresh();
            }
            JSONArray topicProductDataArray = response.optJSONArray("data");
            List<ProductNewInfo> productNewInfos = new ArrayList<ProductNewInfo>();
            if (topicProductDataArray != null && topicProductDataArray.length() != 0)
            {
                for (int i = 0; i < topicProductDataArray.length(); i++)
                {
                    JSONObject productObj = topicProductDataArray.optJSONObject(i);
                    ProductNewInfo productNewInfo = new ProductNewInfo();
                    if (productObj.optJSONObject("exts") != null) {
    					HashMap<String, String> exts=new HashMap<String, String>();
    					if(!StringUtils.isEmpty(productObj.optJSONObject("exts").optString("refer_tags"))){
    						exts.put("refer_tags", productObj.optJSONObject("exts").optString("refer_tags"));
    					}
    					if(!StringUtils.isEmpty(productObj.optJSONObject("exts").optString("big_refer_tags"))){
    						exts.put("big_refer_tags", productObj.optJSONObject("exts").optString("big_refer_tags"));
    					}
    					if(exts.size()>0){
    						productNewInfo.setExts(exts);	
    					}
    				}
                    productNewInfo.setId(productObj.optString("id"));
                    productNewInfo.setPv(productObj.optString("pv"));
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
                            // LogUtil.i("ThemeProductListActivity", "topicJsonArray", topicJsonArray.optString(j));
                            topics.add(topicJsonArray.optString(j));
                        }
                        productNewInfo.setTopics(topics);
                    }
                    JSONObject userJsonObject = productObj.optJSONObject("creater");
                    if (userJsonObject != null && !userJsonObject.toString().equals(""))
                    {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setGender(userJsonObject.optString("sex"));
                        userInfo.setAddress(userJsonObject.optString("address"));
                        userInfo.setStatus(userJsonObject.optString("status"));
                        userInfo.setEmail(userJsonObject.optString("email"));
                        userInfo.setNickname(userJsonObject.optString("nickName"));
                        userInfo.setUserId(userJsonObject.optString("userId"));
                        userInfo.setRole(userJsonObject.optString("role"));
                        userInfo.setPermission(userJsonObject.optString("permission"));
                        userInfo.setAvatar(userJsonObject.optString("avatar"));
                        userInfo.setIntroduction(userJsonObject.optString("introduction"));
                        userInfo.setMobile(userJsonObject.optString("mobile"));
                        JSONArray tagsJsonArray = userJsonObject.optJSONArray("tags");
                        if (tagsJsonArray != null && tagsJsonArray.length() > 0)
                        {
                            ArrayList<String> tags = new ArrayList<String>();
                            for (int j = 0; j < tagsJsonArray.length(); j++)
                            {
                                // LogUtil.i("ThemeProductListActivity", "tagsJsonArray", tagsJsonArray.optString(j));
                                tags.add(tagsJsonArray.optString(j));
                            }
                            userInfo.setTags(tags);
                        }
                        productNewInfo.setUser(userInfo);
                    }
                    productNewInfos.add(productNewInfo);
                }
                if (productNewInfos.size() < 10)
                {
                    productListView.setPullLoadEnable(false);
                }
                else
                {
                    pageNum++;
                    productListView.setPullLoadEnable(true);
                }
                productList.addAll(productNewInfos);
                Log.d("", "------- productAdapter.notifyDataSetChanged()");
                productAdapter.notifyDataSetChanged();
            }
            // 上拉 下拉的初始状态置为false;
            isLoadmore = false;
            isPullRefresh = false;
        }
        else
        {
//            ToastUtil.showToast(ThemeProductListActivity.this, "errorCode=" + response.optString("code"));
            if (isPullRefresh)
            {
                // 下拉刷新接口返回的数据不正确
                productListView.stopRefresh();
            }
            if (pageNum != 1)
            {
                pageNum--;
            }
        }
    }
    
    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        isPullRefresh = true;
        pageNum = 1;
        testSingleTopicProductUrl(pageNum, themeId);
    }
    
    @Override
    public void onLoadMore()
    {
        // TODO Auto-generated method stub
        isLoadmore = true;
        testSingleTopicProductUrl(pageNum, themeId);
    }
    
    private void addHeaderView(String description)
    {
        // 将xml布局文件生成view对象通过LayoutInflater
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 将view对象挂载到那个父元素上，这里没有就为null
        View headerView = inflater.inflate(R.layout.theme_list_hearder, null);
        webView = (WebView)headerView.findViewById(R.id.theme_common);
        webView.loadUrl("http://www.tripaway.cn/site/wap/topicinfo.do?token=" + themeId);
        productListView.addHeaderView(headerView);
    }
}
