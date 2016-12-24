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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductLocationsGridAdapter;
import com.bcinfo.tripaway.adapter.TravelAchiveAdapter;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.ScrollView.LocationCityDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.LocationCityDetailScrollView.onTurnListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 目的地-城市详情
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月15日 14:26:22
 * 
 */
public class LocationCityDetailActivity extends BaseActivity implements OnItemClickListener
{
    private GridView travelAchiveMentGridView;
    
    private List<UserInfo> achiveList = new ArrayList<UserInfo>();
    
    private List<TripDestination> locationList = new ArrayList<TripDestination>();
    
    private String destId;
    
    private TravelAchiveAdapter achiveAdapter;
    
    private ImageView locationHeadIv;
    
    private ListView locationProductListView;// 当地 城市的旅游产品
    
    private LocationCityDetailScrollView locationCityDetailScrollView;// 自定义目的地-城市详情
                                                                      // 滚动条
    
    private ProductAdapter productAdapter;// 旅游产品 adapter
    
    private GridView locationHotCityGridView;// 热门城市 列表
    
    private ListView productListView;// 旅游产品 列表
    
    private ProductLocationsGridAdapter locationsGridAdapter;
    
    private TextView countryNameTv;// 国家 中文名
    
    private TextView countryNameEnTv;// 国家 英文名
    
    private List<ProductNewInfo> productList = new ArrayList<ProductNewInfo>();// 旅游产品集合
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.location_city_detail_activity);
        destId = getIntent().getStringExtra("destId");
        
        locationHeadIv = (ImageView)findViewById(R.id.location_city_detail_iv);
        countryNameTv = (TextView)findViewById(R.id.location_city_detail_country_tv);
        countryNameEnTv = (TextView)findViewById(R.id.location_city_detail_city_enUSName_tv);
        locationHotCityGridView = (GridView)findViewById(R.id.location_country_detail_hotcity_gridview);
        locationCityDetailScrollView =
            (LocationCityDetailScrollView)findViewById(R.id.location_city_detail_scrollview_container);
        initAchiveList();
        initProductList();
        travelAchiveMentGridView = (GridView)findViewById(R.id.location_city_detail_travel_achivement_gridview);
        locationProductListView = (ListView)findViewById(R.id.location_city_detail_trip_product_listview);
        productListView = (ListView)findViewById(R.id.location_city_detail_trip_product_listview);
        ImageLoader.getInstance()
            .displayImage("http://www.jinmalvyou.com/kindeditor/attached/image/20130617/20130617145351_60341.jpg",
                locationHeadIv);
        
        locationCityDetailScrollView.setImageView(locationHeadIv);
        ((LinearLayout)findViewById(R.id.layout_back_button)).setOnClickListener(mOnClickListener);
        testLocationCityDetailUrl(destId);
        locationCityDetailScrollView.setTurnListener(new onTurnListener()
        {
            
            @Override
            public void onTurn(boolean isShowing)
            {
                
            }
        });
    }
    
    /*
     * 测试 目的地-城市详情接口
     */
    private void testLocationCityDetailUrl(String destId)
    {
        HttpUtil.get(Urls.destinations_detail_url + destId, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200)
                {
                    getLocationCountryDetail(response);
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
    
    private void getLocationCountryDetail(JSONObject response)
    {
        String code = response.optString("code");
        
        if ("00000".equals(code))
        {
            JSONObject dataObj = response.optJSONObject("data");
            JSONObject destinationObj = dataObj.optJSONObject("destination");
            if (destinationObj != null)
            {
                TripDestination tripDestination = new TripDestination();
                tripDestination.setDestId(destinationObj.optString("id"));
                tripDestination.setDestName(destinationObj.optString("destName"));
                tripDestination.setDestNameEn(destinationObj.optString("destNameEn"));
                tripDestination.setDestLogoKey(destinationObj.optString("logo"));
                countryNameTv.setText(tripDestination.getDestName());
                countryNameEnTv.setText(tripDestination.getDestNameEn());
                
            }
            JSONArray servicerTravelArray = dataObj.optJSONArray("user"); // 当地旅游达人
            if (servicerTravelArray != null && servicerTravelArray.length() != 0)
            {
                for (int i = 0; i < servicerTravelArray.length(); i++)
                {
                    JSONObject userObj = servicerTravelArray.optJSONObject(i);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(userObj.optString("userId"));
                    userInfo.setNickname(userObj.optString("nickName"));
                    userInfo.setAvatar(userObj.optString("avatar"));
                    userInfo.setIntroduction(userObj.optString("introduction"));
                    userInfo.setMobile(userObj.optString("mobile"));
                    userInfo.setEmail(userObj.optString("email"));
                    userInfo.setStatus(userObj.optString("status"));
                    userInfo.setGender(userObj.optString("sex"));
                    JSONArray tagsArray = userObj.optJSONArray("tags");
                    if (tagsArray != null)
                    {
                        for (int j = 0; j < tagsArray.length(); j++)
                        {
                            
                            userInfo.getTags().add(tagsArray.optString(j));
                            
                        }
                    }
                    achiveList.add(userInfo);
                }
                achiveAdapter = new TravelAchiveAdapter(this, achiveList);
                travelAchiveMentGridView.setAdapter(achiveAdapter);
                travelAchiveMentGridView.setOnItemClickListener(new OnItemClickListener()
                {
                    
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
                    {
                        
                        Intent intentForPersonal =
                            new Intent(LocationCityDetailActivity.this, PersonalInfoNewActivity.class);
                        intentForPersonal.putExtra("identifyId", achiveList.get(position).getUserId());
                        startActivity(intentForPersonal);
                        activityAnimationOpen();
                    }
                    
                });
                setInitialHeight(travelAchiveMentGridView);
            }
            
            JSONArray hotCityArray = dataObj.optJSONArray("hotDestination"); // 热门城市
            if (hotCityArray != null && hotCityArray.length() != 0)
            {
                for (int k = 0; k < hotCityArray.length(); k++)
                {
                    JSONObject hotDestinationObj = hotCityArray.optJSONObject(k);
                    TripDestination tripHotDestination = new TripDestination();
                    tripHotDestination.setDestProNum(hotDestinationObj.optString("pNum"));
                    tripHotDestination.setDestId(hotDestinationObj.optString("id"));
                    tripHotDestination.setDestSupNum(hotDestinationObj.optString("sNum"));
                    tripHotDestination.setDestLogoKey(hotDestinationObj.optString("logo"));
                    tripHotDestination.setDestName(hotDestinationObj.optString("destName"));
                    tripHotDestination.setDestNameEn(hotDestinationObj.optString("destNameEn"));
                    tripHotDestination.setDestDescription(hotDestinationObj.optString("description"));
                    
                    locationList.add(tripHotDestination);
                }
                locationsGridAdapter = new ProductLocationsGridAdapter(this, locationList);
                locationHotCityGridView.setAdapter(locationsGridAdapter);
                locationHotCityGridView.setOnItemClickListener(new OnItemClickListener()
                {
                    
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View convertView, int position, long id)
                    {
                        Intent intentForPopular =
                            new Intent(LocationCityDetailActivity.this, LocationCityDetailActivity.class);
                        intentForPopular.putExtra("destId", locationList.get(position).getDestId());
                        startActivity(intentForPopular);
                        activityAnimationOpen();
                    }
                    
                });
                setInitialHeight2(locationHotCityGridView);
            }
            JSONArray productArray = dataObj.optJSONArray("product");
            if (productArray != null && productArray.length() != 0)
            {
                for (int l = 0; l < productArray.length(); l++)
                {
                    JSONObject productJson = productArray.optJSONObject(l);
                    ProductNewInfo productNewInfo = new ProductNewInfo();
                    productNewInfo.setId(productJson.optString("id"));
                    productNewInfo.setExpStart(productJson.optString("expStart"));
                    productNewInfo.setExpend(productJson.optString("expend"));
                    productNewInfo.setProductType(productJson.optString("productType"));
                    productNewInfo.setDistance(productJson.optString("distance"));
                    productNewInfo.setTitle(productJson.optString("title"));
                    productNewInfo.setPoiCount(productJson.optString("poiCount"));
                    productNewInfo.setPrice(productJson.optString("price"));
                    productNewInfo.setDays(productJson.optString("days"));
                    productNewInfo.setDescription(productJson.optString("description"));
                    productNewInfo.setPriceMax(productJson.optString("priceMax"));
                    productNewInfo.setTitleImg(productJson.optString("titleImg"));
                    productNewInfo.setMaxMember(productJson.optString("maxMember"));
                    ArrayList<String> topics = new ArrayList<String>();
                    JSONArray topicJsonArray = productJson.optJSONArray("topics");
                    if (topicJsonArray != null && topicJsonArray.length() > 0)
                    {
                        for (int j = 0; j < topicJsonArray.length(); j++)
                        {
                            LogUtil.i("LocationCityDetailActivity", "topicJsonArray", topicJsonArray.optString(j));
                            topics.add(topicJsonArray.optString(j));
                        }
                        productNewInfo.setTopics(topics);
                    }
                    // Map<String, Object> mapItem = new HashMap<String, Object>();
                    // JSONObject productObj = productArray.optJSONObject(l); // 旅游产品
                    // mapItem.put("productId", productObj.optString("id"));// 旅游产品标识
                    // mapItem.put("productType", productObj.optString("productType")); // 旅游产品类型
                    // // type
                    // mapItem.put("productLogoUrl", productObj.optString("titleImg"));
                    // mapItem.put("productTitle", productObj.optString("title"));
                    // mapItem.put("productTimeSchedual", productObj.optString("days"));
                    // mapItem.put("productDistance", productObj.optString("distance"));
                    // mapItem.put("productIntroduce", productObj.optString("description"));
                    // JSONArray productTopicArray = productObj.optJSONArray("topics");
                    // if (productTopicArray != null && productTopicArray.length() != 0)
                    // {
                    // StringBuffer sb = new StringBuffer("");
                    // for (int m = 0; m < productTopicArray.length(); m++)
                    // {
                    // String topicName = productTopicArray.optString(m);
                    // sb.append(topicName);
                    // sb.append(" · ");
                    // }
                    // sb.deleteCharAt(sb.length() - 2);
                    // mapItem.put("productTopics", sb.toString());
                    // }
                    productList.add(productNewInfo);
                    
                }
                
                productAdapter = new ProductAdapter(this, productList);
                productListView.setAdapter(productAdapter);
                productListView.setOnItemClickListener(new OnItemClickListener()
                {
                    
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
                    {
                        Intent intentForProductDetail =
                            new Intent(LocationCityDetailActivity.this, GrouponProductNewDetailActivity.class);
                        intentForProductDetail.putExtra("productId", (String)productList.get(position).getId());
                        startActivity(intentForProductDetail);
                    }
                    
                });
                setProductViewInitialHeight(productListView);
            }
            
        }
    }
    
    /**
     * 初始化 旅游达人
     */
    private void initAchiveList()
    {
        // achiveList = new ArrayList<>();
        // Map<String, Object> mapItem1 = new HashMap<>();
        // mapItem1.put("achiveUrl",
        // "http://img04.store.sogou.com/app/a/10010016/b4ca7bdb9d00f8ee25b365b62baf6389");
        // mapItem1.put("achiveName", "李静");
        // Map<String, Object> mapItem2 = new HashMap<>();
        // mapItem2.put("achiveUrl",
        // "http://img03.store.sogou.com/app/a/10010016/a9b8ec78dec159796caf8c7a037ec9cb");
        // mapItem2.put("achiveName", "AllKee");
        // Map<String, Object> mapItem3 = new HashMap<>();
        // mapItem3.put("achiveUrl",
        // "http://img02.store.sogou.com/app/a/10010016/35da3977880315e31a191d49ccef978a");
        // mapItem3.put("achiveName", "王阿瑾");
        // Map<String, Object> mapItem4 = new HashMap<>();
        // mapItem4.put("achiveUrl",
        // "http://img02.store.sogou.com/app/a/10010016/debcfa5fbc02ae098fce38de1550769a");
        // mapItem4.put("achiveName", "温文娜");
        // Map<String, Object> mapItem5 = new HashMap<>();
        // mapItem5.put("achiveUrl",
        // "http://img01.store.sogou.com/app/a/10010016/7bd8e79bf9cc1e2af09a8f6f258240fc");
        // mapItem5.put("achiveName", "张涛");
        // mapItem3.put("achiveUrl",
        // "http://img04.store.sogou.com/app/a/10010016/4e9411249a0293d9190bc89d39496311");
        // mapItem3.put("achiveName", "王阿瑾");
        // Map<String, Object> mapItem6 = new HashMap<>();
        // mapItem6.put("achiveUrl",
        // "http://img01.store.sogou.com/app/a/10010016/cfa144ceec96bd1c402d2e20745f88ca");
        // mapItem6.put("achiveName", "王磊");
        // Map<String, Object> mapItem7 = new HashMap<>();
        // mapItem7.put("achiveUrl",
        // "http://img02.store.sogou.com/app/a/10010016/97250008486871d73d99839d91b2075b");
        // mapItem7.put("achiveName", "戴俊");
        // Map<String, Object> mapItem8 = new HashMap<>();
        // mapItem8.put("achiveUrl",
        // "http://img04.store.sogou.com/app/a/10010016/bb30d53454911d403437e6e691b63a8a");
        // mapItem8.put("achiveName", "图文");
        // achiveList.add(mapItem1);
        // achiveList.add(mapItem2);
        // achiveList.add(mapItem3);
        // achiveList.add(mapItem4);
        // achiveList.add(mapItem5);
        // achiveList.add(mapItem6);
        // achiveList.add(mapItem7);
        // achiveList.add(mapItem8);
        
    }
    
    /**
     * 初始化 当地城市 旅游产品
     */
    private void initProductList()
    {
        // productList = new ArrayList<>();
        // Map<String, Object> mapItem1 = new HashMap<>();
        // mapItem1.put("productLogoUrl",
        // "http://www.potchinese.com/uploadpic/20130413/20130413143028_7465.jpg");
        // mapItem1.put("productTitle", "泰国曼谷大相寺十日游");
        // mapItem1.put("productLocation", "泰国-曼谷");
        // mapItem1.put("productLabelLabel", "宗教-文化");
        // mapItem1.put("productTimeSchedual", "10天");
        // mapItem1.put("productDistance", "2000km");
        // mapItem1.put("productIntroduce", "位于泰国的曼谷有一座大象寺庙,这座寺庙是由象牙建成的。");
        //
        // Map<String, Object> mapItem2 = new HashMap<>();
        // mapItem2.put("productLogoUrl",
        // "http://www.microfotos.com/pic/1/159/15953/1595326preview4.jpg");
        // mapItem2.put("productTitle", "泰国曼谷象牙塔二日游");
        // mapItem2.put("productLocation", "泰国-曼谷");
        // mapItem2.put("productLabelLabel", "宗教-娱乐");
        // mapItem2.put("productTimeSchedual", "2天");
        // mapItem2.put("productDistance", "1784km");
        // mapItem2.put("productIntroduce",
        // "地处曼谷的象牙塔是前世释迦摩力诞生的地方,塔内象牙密布,黄金尘推,被称为金色的银行。");
        //
        // Map<String, Object> mapItem3 = new HashMap<>();
        // mapItem3.put("productLogoUrl",
        // "http://file20.mafengwo.net/M00/A2/F6/wKgB3FE2cEeAOZzGADMgZnZVV9A19.jpeg");
        // mapItem3.put("productTitle", "泰国曼谷总统府一日游");
        // mapItem3.put("productLocation", "泰国-曼谷");
        // mapItem3.put("productLabelLabel", "政治-文化");
        // mapItem3.put("productTimeSchedual", "1天");
        // mapItem3.put("productDistance", "9967km");
        // mapItem3.put("productIntroduce",
        // "泰国的总统府在曼谷这个地方是一个特别显著的建筑物,历代帝王和将士,乃至今天的总统都居住在此。");
        //
        // Map<String, Object> mapItem4 = new HashMap<>();
        // mapItem4.put("productLogoUrl",
        // "http://m2.quanjing.com/2m/danita_rm003/as36-pso0053.jpg");
        // mapItem4.put("productTitle", "泰国曼谷千寻塔之旅");
        // mapItem4.put("productLocation", "泰国-曼谷");
        // mapItem4.put("productLabelLabel", "宗教-文化");
        // mapItem4.put("productTimeSchedual", "3天");
        // mapItem4.put("productDistance", "3321km");
        // mapItem4.put("productIntroduce",
        // "千寻塔在曼谷乃至泰国都是十分重要的宗教朝拜之地,被誉为\"东方的耶路沙冷\",去过此处的游客纷纷称赞。");
        //
        // productList.add(mapItem1);
        // productList.add(mapItem2);
        // productList.add(mapItem3);
        // productList.add(mapItem4);
        
    }
    
    /**
     * 设置gridView真实的高度
     */
    
    private void setInitialHeight(GridView gridView)
    {
        
        ListAdapter listAdapter = gridView.getAdapter();
        int count = listAdapter.getCount();// item的总数
        int totalHeight;
        int rowCount = 0;// 行数
        if (count % 5 != 0)
        {
            rowCount = (count / 5) + 1;
        }
        else
        {
            rowCount = count / 5;
        }
        View viewItem = listAdapter.getView(0, null, gridView);
        viewItem.measure(0, 0);
        totalHeight = viewItem.getMeasuredHeight() * rowCount + 30 * rowCount;
        
        gridView.getLayoutParams().height = totalHeight;
        
    }
    
    /**
     * 设置产品 adapterView真实的高度
     */
    private void setProductViewInitialHeight(ListView listView)
    {
        
        ListAdapter listAdapter = listView.getAdapter();
        int count = listAdapter.getCount();// item的总数
        int totalHeight = 0;
        // int rowCount = 0;// 行数
        for (int i = 0; i < count; i++)
        {
            View viewItem = listAdapter.getView(0, null, listView);
            viewItem.measure(0, 0);
            // totalHeight = viewItem.getMeasuredHeight() * rowCount;
            totalHeight = totalHeight + viewItem.getMeasuredHeight();
        }
        totalHeight = totalHeight + listView.getDividerHeight() * (count - 1);
        listView.getLayoutParams().height = totalHeight;
    }
    
    /**
     * 设置 热门城市 gridView真实的高度
     */
    private void setInitialHeight2(GridView gridView)
    {
        ListAdapter listAdapter = gridView.getAdapter();
        int count = listAdapter.getCount();// item的总数
        int totalHeight;
        int rowCount = 0;// 行数
        if (count % 2 != 0)
        {
            rowCount = (count / 2) + 1;
        }
        else
        {
            rowCount = count / 2;
        }
        View viewItem = listAdapter.getView(0, null, gridView);
        viewItem.measure(0, 0);
        totalHeight = viewItem.getMeasuredHeight() * rowCount + 30 * rowCount;
        gridView.getLayoutParams().height = totalHeight;
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        Toast.makeText(this, "" + position, 0).show();
        
    }
    
}
