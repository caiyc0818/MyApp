package com.bcinfo.tripaway.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.DiscoveryProductResultAdapter;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 搜索默认页面
 * 
 * @function
 * @author weichanghang
 * @version 1.0 2015年4月18日 14:31:11
 * 
 */
public class DiscoveryDefaultsolresultActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
    
    private static final String DEFAULT_URL = "2015-05-30_09:52:04_rbBBYctZ.jpg";
    
    private static final String SOU_COUNT_TEXT = "  共"; // 共...
    
    private static final String SOU_PIECE_TEXT = "条"; // 条
    
    private final static String TAG = "DiscoveryDefaultsolresultActivity";
    
    /**
     * 搜索结果 listview
     */
    private ListView discoveryResultListView;
    
    private ImageView filterIv;// 筛选按钮
    
    /**
     * 搜索产品结果集合
     */
    private ArrayList<Map<String, Object>> resultList;
    
    private DiscoveryProductResultAdapter discoveryResultAdapter;
    
    // 筛选条件
    // private LinearLayout filterLayout;
    // 要搜索的文字
    private String finalkeyWords = "";
    
    private InputMethodManager inputManager;
    
    // 搜索文字
    private TextView CenterTitleTextUp;
    
    // 文字下的时间
    private TextView CenterTitleTextDown;
    
    // 加载旋转框
    private ProgressBar FooterBar;
    
    private TextView TextOfFooterBar;
    
    // 删选的参数
    private String[] filterpar = new String[5];
    
    // 是否删选过了
    private boolean isfilter = false;
    
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                default:
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.discovery_default_sol_result);
        finalkeyWords = this.getIntent().getStringExtra("keyWords");
        LayoutInflater inflator = LayoutInflater.from(this);
        discoveryResultListView = (ListView)findViewById(R.id.discovery_result_listview);
        filterIv = (ImageView)findViewById(R.id.discovery_filter_iv);
        filterIv.setOnClickListener(this);
        TextView SecondTitleText = (TextView)findViewById(R.id.second_title_text);
        SecondTitleText.setVisibility(View.GONE);
        LinearLayout CenterTitleText = (LinearLayout)findViewById(R.id.center_title_text);
        CenterTitleText.setVisibility(View.VISIBLE);
        CenterTitleTextUp = (TextView)findViewById(R.id.center_title_text_up);
        CenterTitleTextUp.setText(finalkeyWords);
        CenterTitleTextDown = (TextView)findViewById(R.id.center_title_text_down);
        CenterTitleTextDown.setText(testsolgettime());
        arrowBack = (LinearLayout)findViewById(R.id.layout_back_button);
        resultList = new ArrayList<Map<String, Object>>();
        // 搜索产品结果
        discoveryResultAdapter = new DiscoveryProductResultAdapter(resultList, this);
        discoveryResultListView.setAdapter(discoveryResultAdapter);// 设置适配器
        discoveryResultListView.setOnItemClickListener(this);
        arrowBack.setOnClickListener(mOnClickListener);
        FooterBar = (ProgressBar)findViewById(R.id.footerBar);
        TextOfFooterBar = (TextView)findViewById(R.id.text_of_footerBar);
        testSolrUrl(isfilter);
    }
    
    /**
     * 查询 搜索结果
     */
    private void QuerySouResultData(JSONObject response)
    {
        String totalNum = String.valueOf(response.optJSONObject("data").opt("totalNum"));
        // String souCountText="  共"+totalNum+"条";
        // String souHintText = finalkeyWords + "  共" + totalNum + "条";
        JSONArray itemsArray = response.optJSONObject("data").optJSONArray("items");
        if (resultList.size() != 0)
        {
            resultList.clear(); // 清空集合中所有的item
        }
        if (Integer.parseInt(totalNum) != 0)
        {
            for (int i = 0; i < itemsArray.length(); i++)
            {
                
                JSONObject itemsObject = itemsArray.optJSONObject(i);
                if ("destination".equals(itemsObject.optString("itemType"))) // 搜索结果为目的地
                {
                    JSONObject destItemObj = itemsObject.optJSONObject("item");
                    Map<String, Object> desMapItem = new HashMap<String, Object>();
                    if (!StringUtils.isEmpty(DEFAULT_URL))
                    {
                        desMapItem.put("destLogoUrl", DEFAULT_URL); // 目的地logo图片
                    }
                    else
                    {
                        desMapItem.put("destLogoUrl", "");
                    }
                    desMapItem.put("destNameStr", destItemObj.optString("destName"));// 目的地名字
                    desMapItem.put("destId", destItemObj.optString("id")); // 目的地标识id
                    desMapItem.put("destpNum", destItemObj.optString("pNum")); // 目的地产品数量
                    desMapItem.put("destsNum", destItemObj.optString("sNum")); // 目的地达人数量
                    desMapItem.put("itemType", "destination");
                    resultList.add(desMapItem);
                    
                }
                else if ("product".equals(itemsObject.optString("itemType"))) // 搜索结果为产品
                {
                    if ("base".equals(itemsObject.optJSONObject("item").optString("productType"))) // 标准产品
                    {
                        JSONObject baseProductItemObj = itemsObject.optJSONObject("item");
                        Map<String, Object> baseProMapItem = new HashMap<String, Object>();
                        baseProMapItem.put("productId", baseProductItemObj.optString("id")); // 标准产品标识
                        if (!StringUtils.isEmpty(DEFAULT_URL))
                        {
                            baseProMapItem.put("baseProductLogoUrl", DEFAULT_URL); // 标准产品logo
                        }
                        else
                        {
                            baseProMapItem.put("baseProductLogoUrl", ""); // 标准产品logo
                        }
                        
                        baseProMapItem.put("productNameStr", baseProductItemObj.optString("title")); // 标准产品标题
                        baseProMapItem.put("productDaysStr", baseProductItemObj.optString("days")); // 标准产品天数
                        baseProMapItem.put("productDistanceStr", baseProductItemObj.optString("distance"));// 标准产品里程
                        JSONArray topicArray = baseProductItemObj.optJSONArray("topics"); // 标准产品
                                                                                          // 主题
                        if (topicArray != null && topicArray.length() != 0)
                        {
                            StringBuilder sb = new StringBuilder("");
                            for (int j = 0; j < topicArray.length(); j++)
                            {
                                if (j == 4)
                                {
                                    break;
                                }
                                if (j != 0)
                                {
                                    sb.append(" · ");
                                }
                                
                                sb.append(topicArray.optString(j));
                                
                            }
                            baseProMapItem.put("productTopicStr", sb.toString());
                        }
                        baseProMapItem.put("itemType", "base"); // 产品的type 标准产品
                        resultList.add(baseProMapItem);
                    }
                    else if ("single".equals(itemsObject.optJSONObject("item").optString("productType"))) // 单产品
                    {
                        JSONObject singleProductItemObj = itemsObject.optJSONObject("item");
                        Map<String, Object> singleProMapItem = new HashMap<String, Object>();
                        singleProMapItem.put("productId", singleProductItemObj.optString("id")); // 单产品标识
                        singleProMapItem.put("singleProductLogoUrl", DEFAULT_URL); // 单产品
                                                                                   // logo
                        singleProMapItem.put("productNameStr", singleProductItemObj.optString("title")); // 单产品标题
                        singleProMapItem.put("productDaysStr", singleProductItemObj.optString("days")); // 单产品
                                                                                                        // 天数
                        singleProMapItem.put("productDistanceStr", singleProductItemObj.optString("distance"));// 单产品里程
                        JSONArray topicArray = singleProductItemObj.optJSONArray("topics"); // 单产品
                                                                                            // 主题
                        if (topicArray != null && topicArray.length() != 0)
                        {
                            StringBuilder sb = new StringBuilder("");
                            for (int j = 0; j < topicArray.length(); j++)
                            {
                                if (j == 4)
                                {
                                    break;
                                }
                                if (j != 0)
                                {
                                    sb.append(" · ");
                                }
                                sb.append(topicArray.optString(j));
                            }
                            singleProMapItem.put("productTopicStr", sb.toString());
                        }
                        singleProMapItem.put("itemType", "single"); // 产品类型 单产品
                        resultList.add(singleProMapItem);
                        
                    }
                    else if ("team".equals(itemsObject.optJSONObject("item").optString("productType")))
                    { // 组团产品
                        JSONObject teamProductItemObj = itemsObject.optJSONObject("item");
                        Map<String, Object> teamProMapItem = new HashMap<String, Object>();
                        teamProMapItem.put("productId", teamProductItemObj.optString("id")); // 组团产品
                                                                                             // 标识
                        teamProMapItem.put("teamProductLogoUrl", DEFAULT_URL); // 组团产品
                                                                               // logo
                        teamProMapItem.put("productNameStr", teamProductItemObj.optString("title")); // 组团产品
                                                                                                     // 名字
                        teamProMapItem.put("productDaysStr", teamProductItemObj.optString("days")); // 组团产品
                                                                                                    // 天数
                        teamProMapItem.put("productDistanceStr", teamProductItemObj.optString("distance")); // 组团产品
                                                                                                            // 里程
                        JSONArray topicArray = teamProductItemObj.optJSONArray("topics"); // 组团产品
                                                                                          // 主题
                        if (topicArray != null && topicArray.length() != 0)
                        {
                            StringBuilder sb = new StringBuilder("");
                            for (int j = 0; j < topicArray.length(); j++)
                            {
                                if (j == 4)
                                {
                                    break;
                                }
                                if (j != 0)
                                {
                                    sb.append(" · ");
                                }
                                sb.append(topicArray.optString(j));
                                
                            }
                            teamProMapItem.put("productTopicStr", sb.toString()); // 组团主题
                        }
                        teamProMapItem.put("itemType", "team");// 产品类型 组团
                        resultList.add(teamProMapItem);
                    }
                }
                else if ("user".equals(itemsObject.optString("itemType")))
                { // 服务者
                    JSONObject userItemObj = itemsObject.optJSONObject("item");
                    Map<String, Object> userMapItem = new HashMap<String, Object>();
                    userMapItem.put("userId", userItemObj.optString("userId")); // 服务者标识id
                    userMapItem.put("userNameStr", userItemObj.optString("nickName")); // 服务者昵称
                    userMapItem.put("userLogoUrl", userItemObj.optString("avatar")); // 服务者头像logo
                    JSONArray userTagArray = userItemObj.optJSONArray("tags");
                    if (userTagArray != null && userTagArray.length() != 0)
                    {
                        StringBuilder sb = new StringBuilder("");
                        for (int j = 0; j < userTagArray.length(); j++)
                        {
                            if (j == 4)
                            {
                                break;
                            }
                            if (j != 0)
                            {
                                sb.append(" · ");
                                
                            }
                            sb.append(userTagArray.optString(j));
                        }
                        userMapItem.put("userTagStr", sb.toString());
                    }
                    userMapItem.put("itemType", "user"); // 服务者用户
                    resultList.add(userMapItem);
                }
                
            }
            UIchange(1);
        }
        else
        {
            // 搜索结果为空的处理
            UIchange(2);
        }
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.discovery_filter_iv:// 筛选图标 按钮
                
                Intent intentForFilter = new Intent(this, TripFilterConditionActivity.class);
                intentForFilter.putExtra("isfilter", isfilter);
                if (isfilter)
                {
                    intentForFilter.putExtra("filterpar", filterpar);
                }
                startActivityForResult(intentForFilter, 100);
                activityAnimationOpen();
                break;
            default:
                break;
        }
        
    }
    
    /*
     * test 搜索接口
     */
    private void testSolrUrl(boolean filter)
    {
        
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pagenum", "0");
            jsonObject.put("pagesize", "20");
            jsonObject.put("keyword", finalkeyWords);
            if (filter)
            {// filter为真时，加入筛选条件
                jsonObject.put("uNum", filterpar[0]);
                jsonObject.put("priceRange", filterpar[1]);
                jsonObject.put("servType", filterpar[2]);
                jsonObject.put("startTime", filterpar[3]);
                jsonObject.put("endTime", filterpar[4]);
                LogUtil.i(TAG, "Destination_DiscoveryDefaultsolresultActivity_testSolrUrl", " uNum=" + filterpar[0]
                    + " priceRange=" + filterpar[1] + " servType=" + filterpar[2] + " startTime=" + filterpar[3]
                    + " endTime=" + filterpar[4]);
            }
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            HttpUtil.post(Urls.solrUrl, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    super.onSuccess(statusCode, headers, response);
                    if ("00000".equals(response.optString("code")))
                    {
                        // LogUtil.i(TAG,
                        // "Destination_DiscoveryDefaultsolresultActivity_testSolrUrl",
                        // "statusCode=" + statusCode);
                        // LogUtil.i(TAG,
                        // "Destination_DiscoveryDefaultsolresultActivity_testSolrUrl",
                        // "responseString=" + response);
                        QuerySouResultData(response);
                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    UIchange(3);
                    // LogUtil.i(TAG,
                    // "Destination_DiscoveryDefaultsolresultActivity_testSolrUrl",
                    // "statusCode=" + statusCode);
                    // LogUtil.i(TAG,
                    // "Destination_DiscoveryDefaultsolresultActivity_testSolrUrl",
                    // "responseString=" + responseString);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void UIchange(int num)
    {
        switch (num)
        {
            case 1:
                discoveryResultListView.setAdapter(discoveryResultAdapter);
                discoveryResultAdapter.notifyDataSetChanged();
                filterIv.setVisibility(View.VISIBLE);
                FooterBar.setVisibility(View.GONE);
                TextOfFooterBar.setVisibility(View.GONE);
                break;
            case 2:
                FooterBar.setVisibility(View.GONE);
                TextOfFooterBar.setText("抱歉，未找到相关数据!");
                break;
            case 3:
                FooterBar.setVisibility(View.GONE);
                TextOfFooterBar.setText("加载失败!");
                break;
            case 4:
                FooterBar.setVisibility(View.VISIBLE);
                TextOfFooterBar.setVisibility(View.VISIBLE);
                TextOfFooterBar.setText("正在加载中...");
                CenterTitleTextDown.setText(filterpar[3] + "-" + filterpar[4]);
                isfilter = true;
                testSolrUrl(isfilter);
                break;
        }
    }
    
    /*
     * testsolgettime 获取时间，
     */
    private String testsolgettime()
    {
        Date currentDate = new Date(System.currentTimeMillis());
        long endtime = currentDate.getTime() + 1000 * 60 * 60 * 24 * 7;// 结束时间是当前时间加7天，
        Date endDate = new Date(endtime);
        
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return sDateFormat.format(currentDate) + "-" + sDateFormat.format(endDate);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            Message msg = mHandler.obtainMessage();
            filterpar = intent.getStringArrayExtra("filterpar");
            msg.what = 0x1345;
            UIchange(4);
            mHandler.sendMessage(msg);
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        
        switch (parent.getId())
        {
            case R.id.discovery_result_listview: // 显示列表
                String gettheId = "";
                Intent intentForProductDetail = null;
                if (null != resultList.get(position).get("destId"))
                {// 搜索结果为目的地
                
                    // gettheId = resultList.get(position).get("destId").toString();
                    // intentForProductDetail = new
                    // Intent(DiscoveryDefaultsolresultActivity.this,
                    // LocationCountryDetailActivity.class);
                    // intentForProductDetail.putExtra("destId", gettheId);
                    
                }
                else if (null != resultList.get(position).get("productId"))
                { // 搜索结果为产品
                
                    // gettheId =
                    // resultList.get(position).get("productId").toString();
                    // intentForProductDetail = new
                    // Intent(DiscoveryDefaultsolresultActivity.this,
                    // ProductDetailNewActivity.class);
                    // intentForProductDetail.putExtra("productId", gettheId);
                    
                }
                else if (null != resultList.get(position).get("userId"))
                {// 搜索结果为服务者
                
                    // gettheId = resultList.get(position).get("userId").toString();
                    // intentForProductDetail = new
                    // Intent(DiscoveryDefaultsolresultActivity.this, *.class);
                    // intentForProductDetail.putExtra("userId", gettheId);
                    
                }
                if (null != intentForProductDetail)
                    startActivity(intentForProductDetail);
                break;
            default:
                break;
        }
        activityAnimationOpen(this);
    }
}
