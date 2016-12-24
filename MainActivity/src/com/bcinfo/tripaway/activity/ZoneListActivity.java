package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ZoneListAdapter;
import com.bcinfo.tripaway.bean.GoWithNew;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.TripZone;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.view.ComListViewFooter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 圈子
 * 
 * @function
 * @author caihelin
 * @version 1.0
 */
public class ZoneListActivity extends BaseActivity implements OnItemClickListener
{
    private List<TripZone> zoneList = new ArrayList<TripZone>();
    
    private ListView zoneListView;
    
    private ZoneListAdapter zoneListAdapter;
    
    private ComListViewFooter mComListViewFooter;
    
    private int pageNum = 1;
    
    private String pageSize = "10";
    
    /**
     * 请求时否发送中
     */
    private boolean isSend = false;
    
    /**
     * 判断是否是最后一项
     */
    private boolean isLastRow = false;
    
    private boolean isLoadmore = false;
    
    private LinearLayout footerParent;
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.activity_trip_zone_layout);
        setSecondTitle("圈子");
        initView();
        testZoneUrl();
    }
    
    protected void initView()
    {
        zoneListView = (ListView)findViewById(R.id.trip_zone_item_listview);
        mComListViewFooter = new ComListViewFooter(this);
        footerParent = new LinearLayout(this);
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        footerParent.addView(mComListViewFooter, params);
        footerParent.setVisibility(View.GONE);
        zoneListView.addFooterView(footerParent);
        zoneListAdapter = new ZoneListAdapter(zoneList, this);
        zoneListView.setAdapter(zoneListAdapter);
        zoneListView.setOnItemClickListener(this);
        zoneListView.setOnScrollListener(new OnScrollListener()
        {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                // TODO Auto-generated method stub
                // 当滚到最后一行且停止滚动时，执行加载
                if (isLastRow && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
                {
                    // 加载元素
                    if (isLoadmore && !isSend)
                    {
                        footerParent.setVisibility(View.VISIBLE);
                        isSend = true;
                    }
                    isLastRow = false;
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                // TODO Auto-generated method stub
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0)
                {
                    isLastRow = true;
                }
            }
        });
    }
    
    /*
     * 测试圈子
     */
    private void testZoneUrl()
    {
        
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);// 当前页码
        params.put("pagesize", pageSize);// 页条数
        
        HttpUtil.get(Urls.zone_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                System.out.println("圈子接口=" + response.toString());
                footerParent.setVisibility(View.GONE);
                String code = response.optString("code");
                if ("00000".equals(code))
                {
                    initZoneItemList(response);
                }
                else
                {
                    footerParent.setVisibility(View.GONE);
                    isLoadmore = false;
                    isSend = false;
                    if (pageNum != 1)
                    {
                        pageNum--;
                    }
                }
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(responseString);
                footerParent.setVisibility(View.GONE);
                isSend = false;
                isLoadmore = false;
                if (pageNum != 1)
                {
                    pageNum--;
                }
                
            }
        });
    }
    
    /*
     * 获取服务端返回的圈子数据
     */
    private void initZoneItemList(JSONObject response)
    {
        JSONArray zoneArray = response.optJSONArray("data");
        if (zoneArray != null)
        {
            List<TripZone> tripZones = new ArrayList<TripZone>();
            for (int i = 0; i < zoneArray.length(); i++)
            {
                JSONObject jsonObj = zoneArray.optJSONObject(i);
                TripZone tripZone = new TripZone();
                tripZone.setPublishTime(jsonObj.optString("publishTime")); // 圈子item 发布的时间
                tripZone.setLocation(jsonObj.optString("location")); // 圈子 item 发布的地点
                tripZone.setIsLike(jsonObj.optString("isLike")); // 圈子 item 被赞
                tripZone.setObjectType(jsonObj.optString("objectType")); // 圈子 item 类型
                tripZone.setLikes(jsonObj.optString("likes")); // 圈子 item 赞数
                JSONObject publisherJson = jsonObj.optJSONObject("publisher");
                JSONArray tagsArray = publisherJson.optJSONArray("tags");
                if (tagsArray != null)
                {
                    for (int j = 0; j < tagsArray.length(); j++)
                    {
                        tripZone.getPublisher().getTags().add(tagsArray.optString(j));
                    }
                }
                tripZone.getPublisher().setGender(publisherJson.optString("sex"));
                tripZone.getPublisher().setStatus(publisherJson.optString("status"));
                tripZone.getPublisher().setEmail(publisherJson.optString("email"));
                tripZone.getPublisher().setNickname(publisherJson.optString("nickName"));
                tripZone.getPublisher().setUserId(publisherJson.optString("userId"));
                tripZone.getPublisher().setAvatar(publisherJson.optString("avatar"));
                // System.out.println("图片  url=" + (publisherJson.optString("avatar")));
                tripZone.getPublisher().setIntroduction(publisherJson.optString("introduction"));
                tripZone.getPublisher().setMobile(publisherJson.optString("mobile"));
                if ("tripstory".equals(tripZone.getObjectType())) // 微游记
                {
                    JSONObject tripArticleObj = jsonObj.optJSONObject("object");
                    TripArticle tripArticle = new TripArticle();
                    JSONArray pictureArray = tripArticleObj.optJSONArray("pictures");
                    if (pictureArray != null)
                    {
                        for (int j = 0; j < pictureArray.length(); j++)
                        {
                            JSONObject pictureObj = pictureArray.optJSONObject(j);
                            ImageInfo resPicture = new ImageInfo();
                            resPicture.setDesc(pictureObj.optString("desc"));
                            resPicture.setUrl(pictureObj.optString("url"));
                            tripArticle.getPictureList().add(resPicture);
                        }
                    }
                    tripArticle.setPublishTime(tripArticleObj.optString("publishTime"));
                    tripArticle.setLocation(tripArticleObj.optString("location"));
                    tripArticle.setIsLike(tripArticleObj.optString("isLike"));
                    tripArticle.setPhotoId(tripArticleObj.optString("photoId"));
                    tripArticle.setDescription(tripArticleObj.optString("description"));
                    JSONObject tripStoryUserObj = tripArticleObj.optJSONObject("publisher");
                }
                else if ("product".equals(tripZone.getObjectType()))
                { // 新产品
                    JSONObject productObj = jsonObj.optJSONObject("object");
                    if (productObj == null || productObj.equals(""))
                    {
                        return;
                    }
                    ProductNewInfo productInfo = new ProductNewInfo();
                    JSONArray topicJsonArray = productObj.optJSONArray("topics");
                    if (topicJsonArray != null && !topicJsonArray.equals(""))
                    {
                        ArrayList<String> topics = new ArrayList<String>();
                        for (int j = 0; j < topicJsonArray.length(); j++)
                        {
                            // LogUtil.i(TAG, "topicJsonArray", topicJsonArray.optString(j));
                            topics.add(topicJsonArray.optString(j));
                        }
                        productInfo.setTopics(topics);
                    }
                    productInfo.setExpStart(productObj.optString("expStart"));
                    productInfo.setExpend(productObj.optString("expend"));
                    JSONObject userJSON = productObj.optJSONObject("creater");
                    if (userJSON != null && !userJSON.toString().equals(""))
                    {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setGender(userJSON.optString("sex"));
                        userInfo.setAddress(userJSON.optString("address"));
                        userInfo.setStatus(userJSON.optString("status"));
                        userInfo.setEmail(userJSON.optString("email"));
                        userInfo.setNickname(userJSON.optString("nickName"));
                        userInfo.setUserId(userJSON.optString("userId"));
                        userInfo.setRole(userJSON.optString("role"));
                        userInfo.setPermission(userJSON.optString("permission"));
                        userInfo.setAvatar(userJSON.optString("avatar"));
                        userInfo.setIntroduction(userJSON.optString("introduction"));
                        userInfo.setMobile(userJSON.optString("mobile"));
                        JSONArray tagsJsonArray = userJSON.optJSONArray("tags");
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
                        productInfo.setUser(userInfo);
                    }
                    productInfo.setId(productObj.optString("id"));
                    productInfo.setDistance(productObj.optString("distance"));
                    productInfo.setTitle(productObj.optString("title"));
                    productInfo.setPoiCount(productObj.optString("poiCount"));
                    productInfo.setPrice(productObj.optString("price"));
                    productInfo.setDays(productObj.optString("days"));
                    productInfo.setDescription(productObj.optString("description"));
                    productInfo.setPriceMax(productObj.optString("priceMax"));
                    productInfo.setTitleImg(productObj.optString("titleImg"));
                    productInfo.setMaxMember(productObj.optString("maxMember"));
                    productInfo.setProductType(productObj.optString("productType"));
                    productInfo.setServices(productObj.optString("serviceCodes"));
                    tripZone.setObject(productInfo);
                }
                else if ("together".equals(tripZone.getObjectType()))
                { // 结伴
                    JSONObject togetherObj = jsonObj.optJSONObject("object");
                    GoWithNew goWithTogether = new GoWithNew();
                    goWithTogether.setId(togetherObj.optString("id"));
                    goWithTogether.setTitle(togetherObj.optString("title"));
                    goWithTogether.setDescription(togetherObj.optString("description"));
                    JSONObject goWithSponsorObj = togetherObj.optJSONObject("sponsor");
                    goWithTogether.getUser().setUserId(goWithSponsorObj.optString("userId"));
                    goWithTogether.getUser().setNickname(goWithSponsorObj.optString("nickName"));
                    goWithTogether.getUser().setAvatar(goWithSponsorObj.optString("avatar"));
                    tripZone.setObject(goWithTogether);
                }
                tripZones.add(tripZone);
            }
            if (tripZones.size() < 10)
            {
                isLoadmore = false;
            }
            else
            {
                isLoadmore = true;
                pageNum++;
            }
            zoneList.addAll(tripZones);
            zoneListAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        
    }
}
