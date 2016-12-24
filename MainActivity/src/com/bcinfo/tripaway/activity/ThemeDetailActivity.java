package com.bcinfo.tripaway.activity;

import im.yixin.sdk.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.LocationCountryAdapter;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductLocationsGridAdapter;
import com.bcinfo.tripaway.adapter.ThemePicListAdapter;
import com.bcinfo.tripaway.adapter.TravelAchiveAdapter;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.ScrollView.LocationCountryDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.LocationCountryDetailScrollView.onTurnListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 目的地-国家详情
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月16日 9:40:02
 */
public class ThemeDetailActivity extends BaseActivity implements OnClickListener,PersonalScrollViewListener

{
    
    
    
    private LocationCountryDetailScrollView locationCountryDetailScrollView;
    
    private LocationCountryAdapter productAdapter;// 旅游产品 adapter
    
    
    private ListView productListView;// 旅游产品 列表
    
    private List<UserInfo> achiveList = new ArrayList<UserInfo>();
    
    private TextView countryNameTv;// 国家 中文名
    
    private TextView country_name;
    
    private View line;
    
    private TextView countryNameEnTv;// 国家 英文名
    
    
    private List<ProductNewInfo> productList = new ArrayList<ProductNewInfo>();// 旅游产品集合
    
    private ImageView locationCountryHeadIv;
    
    private ImageLoader imgLoader;
    
    private String topicId;
    
    private TripDestination tripDestination = new TripDestination();
    
    private TextView location_country_desc;
    
    private TextView showmore_orpackup;
    
    private ImageView arrow_down;
    
    private ImageView arrow_up;
    
    private TextView responseTv;
    
    private int textLineCount;
    
    private LinearLayout show_more_layout;
    
    private LinearLayout layout_location_country_detail_hotcity_container;
    
    private LinearLayout travel_product_container;
    
    private LinearLayout country_name_layout;
    
    
    private RelativeLayout warm_tip;
    
    private RelativeLayout country_detail_layout_title;
    
    private static final int SHRINK_UP_STATE = 1;// 收起状态  
    
    private static final int SPREAD_STATE = 2;// 展开状态  
    
    private static int mState = SHRINK_UP_STATE;//默认收起状态  
    
    
    private ListView picListview;
    
    List<ImageInfo> pics=new ArrayList<ImageInfo>();//图片列表
    
    ThemePicListAdapter themePicListAdapter;
    private LinearLayout warm_tip_layout;
    
    List<HashMap<String,String>> tips=new ArrayList<HashMap<String,String>>();//图片列表
    
    
    private int pageSize=10;
	private int pageNum=1;

	private LinearLayout mTravelListViewFooter;
	
	private boolean isNeedLoad=true;
	
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;
	private Drawable downDrawable;

	private Drawable upDrawable;

	private View loginLoading;

	private AnimationDrawable loadingAnimation;
	
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.theme_detail_activity);
        loginLoading = findViewById(R.id.login_loading);
  	  loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
        loadingAnimation.start();
        topicId = getIntent().getStringExtra("themeId"); // 目的地标识id
        if(getIntent().getStringExtra("themeName")!=null){
        	statisticsTitle=getIntent().getStringExtra("themeName");
        }
        countryNameTv = (TextView)findViewById(R.id.location_country_detail_country_tv);
        countryNameEnTv = (TextView)findViewById(R.id.location_city_detail_city_enUSName_tv);
        country_name = (TextView)findViewById(R.id.country_name);
        country_name_layout= (LinearLayout)findViewById(R.id.country_name_layout);
        //responseTv= (TextView)findViewById(R.id.responsetv);
        location_country_desc = (TextView)findViewById(R.id.location_country_desc);
        layout_location_country_detail_hotcity_container= (LinearLayout)findViewById(R.id.layout_location_country_detail_hotcity_container);
        travel_product_container= (LinearLayout)findViewById(R.id.travel_product_container);
        country_detail_layout_title= (RelativeLayout)findViewById(R.id.country_detail_layout_title);
        country_detail_layout_title.getBackground().setAlpha(0);
        warm_tip= (RelativeLayout)findViewById(R.id.warm_tip);
        country_name.setAlpha(0);
        testDestinationDetailUrl(topicId);
        testProductDetailUrl(topicId);
//        warm_tip.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent=new Intent(ThemeDetailActivity.this,LocationCountryTipActivity.class);
//				intent.putExtra("countryName", tripDestination.getDestName());
//				intent.putExtra("destinationId", topicId);
//				startActivity(intent);
//				activityAnimationOpen();
//			}
//		});
        textLineCount=location_country_desc.getLineCount();
        show_more_layout= (LinearLayout)findViewById(R.id.show_more_layout);
        showmore_orpackup= (TextView)findViewById(R.id.showmore_orpackup);
    	ViewTreeObserver viewTreeObserver = location_country_desc.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			private boolean isCheck=false;

			@Override
			public boolean onPreDraw() {
				int maxLines = location_country_desc.getLineCount();
//				if ((Boolean) description.getTag()) {
//					return true;
//				}
				if(isCheck)return true;
					if (maxLines <= 5&&pics.size()==0) {
						showmore_orpackup.setVisibility(View.GONE);
					} else {
						location_country_desc.setMaxLines(5);
						showmore_orpackup.setVisibility(View.VISIBLE);
						mState = SHRINK_UP_STATE;
						isCheck=true;
					}
					
				return true;
			}
		});
        	//show_more_layout.setVisibility(View.VISIBLE);
        	show_more_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mState == SHRINK_UP_STATE) {  
						location_country_desc.setMaxLines(Integer.MAX_VALUE);  
						location_country_desc.requestLayout();  
						showmore_orpackup.setText("收起更多");
						mState = SPREAD_STATE;  
						warm_tip_layout.setVisibility(View.VISIBLE);
						if(pics.size()>0)
						{picListview.setVisibility(View.VISIBLE);
							line.setVisibility(View.VISIBLE);}
						showmore_orpackup.setCompoundDrawables(null,null,upDrawable,null);
						} else if (mState == SPREAD_STATE) {  
							location_country_desc.setMaxLines(5);  
							location_country_desc.requestLayout();  
							showmore_orpackup.setText("显示更多");
							warm_tip_layout.setVisibility(View.GONE);
							picListview.setVisibility(View.GONE);
							line.setVisibility(View.GONE);
							showmore_orpackup.setCompoundDrawables(null,null,downDrawable,null);
						mState = SHRINK_UP_STATE;  
						}  
					
					
				}
			});
        	
       
        // initProductList();
        locationCountryDetailScrollView =
            (LocationCountryDetailScrollView)findViewById(R.id.location_country_detail_scrollview_container);
        ((LinearLayout)findViewById(R.id.layout_back_button)).setOnClickListener(mOnClickListener);
        //((ImageView)findViewById(R.id.layout_location_country_detail_container)).setOnClickListener(this);
        productListView = (ListView)findViewById(R.id.location_country_detail_trip_product_listview);
        mTravelListViewFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.xlistview_footer1, null);
		  productAdapter = new LocationCountryAdapter(ThemeDetailActivity.this, productList);
          productListView.setAdapter(productAdapter);
          productListView.addFooterView(mTravelListViewFooter);
  		mTravelListViewFooter.setVisibility(View.GONE);
          productListView.setOnItemClickListener(new OnItemClickListener()
          {
              
              @Override
              public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
              {
                  LogUtil.i("LocationCountryDetailActivity", "onItemClick", "position=" + position);
                  Intent intent = null;
                  ProductNewInfo productNewInfo = productList.get(position);
                  if (productNewInfo.getProductType().equals("single"))
                  {
                      if (productNewInfo.getServices().equals("traffic"))
                      {
                          intent = new Intent(ThemeDetailActivity.this, CarProductDetailActivity.class);
                          intent.putExtra("productId", productNewInfo.getId());
                      }
                      else if (productNewInfo.getServices().equals("stay"))
                      {
                          intent =
                              new Intent(ThemeDetailActivity.this, HouseProductDetailActivity.class);
                          intent.putExtra("productId", productNewInfo.getId());
                      }
                      else
                      {
                          intent = new Intent(ThemeDetailActivity.this, GrouponProductNewDetailActivity.class);
                          intent.putExtra("productId", productNewInfo.getId());
                      }
                  }
                  else if (productNewInfo.getProductType().equals("base")
                      || productNewInfo.getProductType().equals("customization"))
                  {
                      intent = new Intent(ThemeDetailActivity.this, GrouponProductNewDetailActivity.class);
                      intent.putExtra("productId", productNewInfo.getId());
                  }
                  else if (productNewInfo.getProductType().equals("team"))
                  {
                      intent =
                          new Intent(ThemeDetailActivity.this, GrouponProductNewDetailActivity.class);
                      intent.putExtra("productId", productNewInfo.getId());
                  }
  				intent.putExtra("productTitle", productNewInfo.getTitle());
                  ThemeDetailActivity.this.startActivity(intent);
                  activityAnimationOpen();
                  // Intent intentForProductDetail =
                  // new Intent(LocationCountryDetailActivity.this, ProductDetailNewActivity.class);
                  // intentForProductDetail.putExtra("productId", (String)productList.get(position).getId());
                  // startActivity(intentForProductDetail);
                  // LocationCountryDetailActivity.this.activityAnimationOpen();
              }
              
          });
        locationCountryHeadIv = (ImageView)findViewById(R.id.location_country_detail_iv);
        
        
        locationCountryDetailScrollView.setImageView(locationCountryHeadIv);
        locationCountryDetailScrollView.setScrollListener(this);
        locationCountryDetailScrollView.setTurnListener(new onTurnListener()
        {
            @Override
            public void onTurn(boolean isShowing)
            {
            }
        });
        
        picListview=(ListView)findViewById(R.id.pic__listview);
        warm_tip_layout=(LinearLayout)findViewById(R.id.warm_tip_layout);
        line=findViewById(R.id.line);
   	 downDrawable= getResources().getDrawable(R.drawable.subject_down_arrow);
   		downDrawable.setBounds(0, 0, downDrawable.getMinimumWidth(), downDrawable.getMinimumHeight());
   		upDrawable= getResources().getDrawable(R.drawable.subject_up_arrow);
   		upDrawable.setBounds(0, 0, upDrawable.getMinimumWidth(), upDrawable.getMinimumHeight());
   		showmore_orpackup.setCompoundDrawables(null,null,downDrawable,null);
   		showmore_orpackup.setCompoundDrawablePadding(5);
   	 
    }
    
    /*
     * test 目的地 -国家详情url
     */
    private void testDestinationDetailUrl(String destId)
    {
        HttpUtil.get(Urls.topics_all_url + destId, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200)
                {
                    // LogUtil.i(
                    // "",
                    // "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
                    // "statusCode=" + statusCode);
                     LogUtil.i(
                     "",
                    "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
                     "responseString=" + response.toString());
                     //responseTv.setText(response.toString());
                    getThemeDetail(response);
                }
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // LogUtil.i(
                // "",
                // "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
                // "statusCode=" + statusCode);
                // LogUtil.i(
                // "",
                // "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
                // "responseString=" + responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
                
            }
        });
    }
    
   
    
    /**
     * 获取目的地-国家 详情参数
     */
    private void getThemeDetail(JSONObject response)
    {
        String code = response.optString("code");
        
        if ("00000".equals(code))
        {
        	loadingAnimation.stop();
			loginLoading.setVisibility(View.GONE);
            JSONObject dataObj = response.optJSONObject("data");
            JSONObject topicObj = dataObj.optJSONObject("topic");
            if (topicObj != null)
            {
            	if(!StringUtils.isEmpty(topicObj.optString("title"))){
                	countryNameTv.setText(topicObj.optString("title"));
                	country_name.setText(topicObj.optString("title"));
                	StatService.onResume( topicObj.optString("title"));
                }
            	if(!StringUtils.isEmpty(topicObj.optString("subTitle"))){
                	countryNameEnTv.setText(topicObj.optString("subTitle"));
                }
                if(!StringUtils.isEmpty(topicObj.optString("background"))){
                ImageLoader.getInstance().displayImage(Urls.imgHost +topicObj.optString("background"),locationCountryHeadIv,
                    AppConfig.options(R.drawable.ic_launcher));
                }
                
            }
            JSONArray detailArray = dataObj.optJSONArray("details"); // 当地旅游达人
            if (detailArray != null && detailArray.length() != 0)
            {
                for (int i = 0; i < detailArray.length(); i++)
                {
                    JSONObject detailObj = detailArray.optJSONObject(i);
                    if(!StringUtils.isEmpty(detailObj.optString("desc"))){
                    	location_country_desc.setText(detailObj.optString("desc"));
                    }
                    JSONArray picAarray = detailObj.optJSONArray("pics");
                    if (picAarray != null&&picAarray.length()>0) {
						for (int m = 0; m < picAarray.length(); m++) {
							ImageInfo pic=new ImageInfo();
							pic.setDesc(picAarray.optJSONObject(m).optString("desc"));
							pic.setWidth(picAarray.optJSONObject(m).optString("width"));
							pic.setHeight(picAarray.optJSONObject(m).optString("height"));
							pic.setUrl(picAarray.optJSONObject(m).optString("url"));
							pics.add(pic);
							}
						themePicListAdapter=new ThemePicListAdapter(ThemeDetailActivity.this, pics);
						picListview.setAdapter(themePicListAdapter);
						}
                    break;
                }
            }else{
//            	layout_location_country_detail_achivement_container.setVisibility(View.GONE);
            }
            
            JSONArray tipArray = dataObj.optJSONArray("tips"); // 当地旅游达人
            if (tipArray != null && tipArray.length() != 0)
            {
                for (int i = 0; i < tipArray.length(); i++)
                {
                    JSONObject tipObj = tipArray.optJSONObject(i);
                    if(StringUtils.isEmpty(tipObj.optString("title")))continue;
                    HashMap<String,String> map=new HashMap<String,String>();
                    map.put("id", tipObj.optString("id"));
                    map.put("content", tipObj.optString("content"));
                    map.put("title", tipObj.optString("title"));
                    tips.add(map);
                    View tipView=LayoutInflater.from(ThemeDetailActivity.this).inflate(R.layout.item_warm_tip, null);
                    RelativeLayout warm_tip =(RelativeLayout) tipView.findViewById(R.id.warm_tip);
                    TextView warm_tip_title=(TextView) tipView.findViewById(R.id.warm_tip_title);
                    warm_tip_title.setText(tipObj.optString("title"));
                    warm_tip_layout.addView(tipView); 
                    warm_tip.setTag(i);
                    warm_tip.setOnClickListener(new OnClickListener() {
            			
            			@Override
            			public void onClick(View v) {
            				int i=(Integer)v.getTag();
            				Intent intent=new Intent(ThemeDetailActivity.this,LocationCountryTipActivity.class);
            				intent.putExtra("title", tips.get(i).get("title"));
            				intent.putExtra("content", tips.get(i).get("content"));
            				intent.putExtra("from", "theme");
            				startActivity(intent);
            				activityAnimationOpen();
            			}
            		});
                }
            }else{
//            	layout_location_country_detail_achivement_container.setVisibility(View.GONE);
            }
            
        }
    }
    
    /*
    * test 产品详情url
    */
   private void testProductDetailUrl(String destId)
   {
	   
	   if(!isNeedLoad)return;
	   if(isLoadmore)return;
	   isLoadmore=true;
	   RequestParams params = new RequestParams();
		params.put("pagesize", pageSize);
		params.put("pagenum", pageNum);
//		params.put("topicId", destId);
       HttpUtil.get(Urls.get_theme_product+destId,params, new JsonHttpResponseHandler()
       {
           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONObject response)
           {
               
               super.onSuccess(statusCode, headers, response);
               if (statusCode == 200)
               {
                   // LogUtil.i(
                   // "",
                   // "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
                   // "statusCode=" + statusCode);
                    LogUtil.i(
                    "",
                   "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
                    "responseString=" + response.toString());
                    //responseTv.setText(response.toString());
                    String code = response.optString("code");
                    
                    if ("00000".equals(code))
                    {
                    	List<ProductNewInfo> list=new ArrayList<ProductNewInfo>();
                    JSONArray productArray = response.optJSONArray("data");
                    if (productArray != null && productArray.length() != 0)
                    {
                    	((LinearLayout)productListView.getParent()).getChildAt(0).setVisibility(View.VISIBLE);
                        for (int i = 0; i < productArray.length(); i++)
                        {
                            JSONObject productJson = productArray.optJSONObject(i);
                            ProductNewInfo productNewInfo = new ProductNewInfo();
                    		if (productJson.optJSONObject("exts") != null) {
								HashMap<String, String> exts = new HashMap<String, String>();
								if (!StringUtils
										.isEmpty(productJson.optJSONObject("exts").optString("refer_tags"))) {
									exts.put("refer_tags",
											productJson.optJSONObject("exts").optString("refer_tags"));
								}
								if (!StringUtils
										.isEmpty(productJson.optJSONObject("exts").optString("big_refer_tags"))) {
									exts.put("big_refer_tags",
											productJson.optJSONObject("exts").optString("big_refer_tags"));
								}
								if (exts.size() > 0) {
									productNewInfo.setExts(exts);
								}
							}
            				productNewInfo.setOriginalPrice(productJson.optString("originalPrice"));
            				productNewInfo.setPv(productJson.optString("pv"));
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
                            productNewInfo.setServices(productJson.optString("serviceCodes"));
                            productNewInfo.setQuickPayment(productJson.optString("quickPayment"));
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
                            list.add(productNewInfo);
                        }
                        
                        notifySetChanged(list);
//                        setProductViewInitialHeight(productListView);
                    }else{
//                    	travel_product_container.setVisibility(View.GONE);
                    	mTravelListViewFooter.setVisibility(View.GONE);
                    	isNeedLoad=false;
                    }
               }
               }
               
           }
           
           @Override
           public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
           {
               // LogUtil.i(
               // "",
               // "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
               // "statusCode=" + statusCode);
               // LogUtil.i(
               // "",
               // "Destination_LocationCountryDetailActivity_testDestinationDetailUrl",
               // "responseString=" + responseString);
        	   isLoadmore=false;
               super.onFailure(statusCode, headers, responseString, throwable);
               
           }
       });
   }
    
   private void notifySetChanged(List<ProductNewInfo> list) {
	   productList.addAll(list);
	   productAdapter.notifyDataSetChanged();
		int size = productList.size() % pageSize;
		if (size == 0&&list.size()!=0) {
			mTravelListViewFooter.setVisibility(View.VISIBLE);
			isNeedLoad=true;
			++pageNum;
		} else {
			mTravelListViewFooter.setVisibility(View.GONE);
//			productListView.removeFooterView(mTravelListViewFooter);
			isNeedLoad=false;
		}
		isLoadmore=false;
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
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
           /* case R.id.titlebar_right_tip:
                // Intent intent = new Intent(this, TipsDetailActivity.class);
                // intent.putExtra("objectId", destId);
                // intent.putExtra("objectType", "destination");
                // startActivity(intent);
                // activityAnimationOpen();
                break;*/
            
            default:
                break;
        }
        
    }

	@Override
	public void onScrollChanged(ScrollView view, int positionX, int positionY,
			int prePositionX, int prePositionY) {
        // TODO Auto-generated method stub
        if (view != null && view == locationCountryDetailScrollView)
        {
        	
            int alpha = positionY / 2;
            if (positionY < 50 || positionY == 50)
            {
            	country_name_layout.setAlpha(1f);
            }
            else
            {
            	country_name_layout.setAlpha(0);
            }
            if (positionY > 220)
            {
                alpha = 255;
            }
            country_detail_layout_title.getBackground().setAlpha(alpha);
            country_name.setAlpha(alpha / 255f);
            
            View view1 = locationCountryDetailScrollView.getChildAt(0);
			int height = view1.getMeasuredHeight();
			int scollY = locationCountryDetailScrollView.getScrollY();
			int scollHeight = locationCountryDetailScrollView.getHeight();
			if (height == scollY + scollHeight) {
				// 底部
				  testProductDetailUrl(topicId);
			}
        }
        else
        {
            return;
        }
    }
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "主题详情";
	}
}
