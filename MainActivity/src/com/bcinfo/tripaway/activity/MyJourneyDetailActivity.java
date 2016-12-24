package com.bcinfo.tripaway.activity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductTripPlanListAdapter;
import com.bcinfo.tripaway.bean.FeatureInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.MyOrder;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.TripDetailInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.dialog.ServicerPersonInfoDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.pop.OrderPopWindow;
import com.bcinfo.tripaway.view.pop.OrderPopWindow.OperationListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的旅程订单详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年3月26日 上午11:45:26
 */
public class MyJourneyDetailActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "MyJourneyDetailActivity";
    
    private ScrollView mProductScrollView;
    
    private ImageView mProductHeadImg;
    
    private RelativeLayout layout_product_head;
    
    private ImageView user_info_blur_bg;
    
    private LinearLayout layout_scenic_info;
    
    private MyListView trip_detail_list;
    
    private ProductTripPlanListAdapter mTripPlanListAdapter;
    
    private final ArrayList<Journey> mJourneyList = new ArrayList<Journey>();
    
    /*
     * 产品信息
     */
    private MyOrder mMyOrder;
    
    private RelativeLayout layout_product_detail_title;
    
    private LinearLayout layout_operation_button;
    
    private final ArrayList<TripDetailInfo> tripDetailInfoList = new ArrayList<TripDetailInfo>();
    
    private TextView trip_state;
    
    private TextView order_leave_word;
    
    private RoundImageView product_author_photo;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mMyOrder = (MyOrder)getIntent().getSerializableExtra("myOrder");
        setContentView(R.layout.my_journey_detail_activity);
        LinearLayout backButton = (LinearLayout)findViewById(R.id.layout_back_button);
        backButton.setOnClickListener(this);
        mProductScrollView = (ScrollView)findViewById(R.id.product_detail_scroll_view);
        mProductHeadImg = (ImageView)findViewById(R.id.product_head_imageView);
        layout_product_head = (RelativeLayout)findViewById(R.id.layout_product_head);
        user_info_blur_bg = (ImageView)findViewById(R.id.user_info_blur_bg);
        layout_scenic_info = (LinearLayout)findViewById(R.id.layout_scenic_info);
        int space = getResources().getDimensionPixelSize(R.dimen.detail_photo_bottom_space);
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, screenHeight - space - statusBarHeight);
        // mProductHeadImg.setLayoutParams(p);
        layout_product_head.setLayoutParams(p);
        // ImageLoader.getInstance().displayImage("http://pic4.zhongsou.com/image/4804b5f2c51fd9b41da.jpg",
        // mProductHeadImg);
        trip_state = (TextView)findViewById(R.id.trip_state);
        order_leave_word = (TextView)findViewById(R.id.order_leave_word);
        product_author_photo = (RoundImageView)findViewById(R.id.product_author_photo);
        product_author_photo.setOnClickListener(this);
        ImageView product_service_button = (ImageView)findViewById(R.id.product_service_button);
        product_service_button.setOnClickListener(this);
        Button button_pay = (Button)findViewById(R.id.button_pay);
        button_pay.setOnClickListener(this);
        
        initTripDetail();
        queryMyTripPlanDetail();
        queryProductJourney();
        queryProductComment();
        QuerySimillarProduct(1, "1");
    }
    
    private void initProductInfo(ProductNewInfo productInfo)
    {
        UserInfo author = productInfo.getUser();
        if (!StringUtils.isEmpty(productInfo.getTitleImg()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + productInfo.getTitleImg(), mProductHeadImg);
        }
        
        TextView product_author_name = (TextView)findViewById(R.id.product_author_name);
        TextView product_author_description = (TextView)findViewById(R.id.product_author_description);
        TextView product_price = (TextView)findViewById(R.id.product_price);
        TextView detail_product_title = (TextView)findViewById(R.id.detail_product_title);
        RoundImageView order_user_photo = (RoundImageView)findViewById(R.id.order_user_photo);
        if (!StringUtils.isEmpty(author.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + author.getAvatar(),
                order_user_photo,
                AppConfig.options(R.drawable.user_defult_photo));
        }
        if (!StringUtils.isEmpty(author.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + author.getAvatar(),
                product_author_photo,
                AppConfig.options(R.drawable.user_defult_photo));
        }
        
        product_author_name.setText(author.getNickname());
        product_author_description.setText(author.getIntroduction());
        detail_product_title.setText(productInfo.getTitle());
        product_price.setText("￥" + productInfo.getPrice() + " - " + productInfo.getPriceMax());
        /******************* 产品简要信息 ********************/
        LinearLayout servicesLine =
            (LinearLayout)LayoutInflater.from(this).inflate(R.layout.product_detail_services_h, null);
        servicesLine.addView(getServiceItem(productInfo.getPoiCount() + "景点", R.drawable.flag_scenery));
        servicesLine.addView(getServiceItem(productInfo.getDistance() + "KM", R.drawable.distance));
        servicesLine.addView(getServiceItem(productInfo.getDays() + "天", R.drawable.flag_calendar));
        layout_scenic_info.addView(servicesLine);
    }
    
    /**
     * 初始化产品亮点,特色
     */
    private void initScenicSpots(ProductNewInfo productInfo)
    {
        ArrayList<FeatureInfo> infoList = productInfo.getFeatures();
        LinearLayout scenic_spots_table = (LinearLayout)findViewById(R.id.scenic_spots_layout);
        for (int i = 0; i < infoList.size(); i++)
        {
            FeatureInfo info = infoList.get(i);
            LinearLayout item =
                (LinearLayout)LayoutInflater.from(this).inflate(R.layout.scenic_spots_listview_item, null);
            TextView scenic_spots_name = (TextView)item.findViewById(R.id.scenic_spots_name);
            TextView scenic_spots_des = (TextView)item.findViewById(R.id.scenic_spots_des);
            LinearLayout photos_total_layout = (LinearLayout)item.findViewById(R.id.scenic_spots_photo_layout);
            scenic_spots_name.setText(info.getTitle());
            if(!StringUtils.isEmpty(info.getDesc())&&!info.getDesc().equals("null"))
            scenic_spots_des.setText(info.getDesc());
            ArrayList<ImageInfo> photos = (ArrayList<ImageInfo>) info.getImages();
            LinearLayout photo_hori_layout = new LinearLayout(this);
            for (int j = 0; j < photos.size(); j++)
            {
                LinearLayout photoItem =
                    (LinearLayout)LayoutInflater.from(this).inflate(R.layout.scenic_spots_gridview_item, null);
                photoItem.setTag(R.id.tag_photo_list, photos);// list集合
                photoItem.setTag(R.id.tag_photo_index, j);// 图片索引
                photoItem.setOnClickListener(mOnClickListener);
                ImageView scenic_spots_photo = (ImageView)photoItem.findViewById(R.id.scenic_spots_photo);
                if (!StringUtils.isEmpty(photos.get(j).getUrl()))
                {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + photos.get(j).getUrl(), scenic_spots_photo);
                }
                
                if (j % 5 == 0)// 判断j是否能被5整除
                {
                    photo_hori_layout = new LinearLayout(this);
                    photo_hori_layout.setOrientation(LinearLayout.HORIZONTAL);
                    photo_hori_layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                    photos_total_layout.addView(photo_hori_layout);
                }
                photo_hori_layout.addView(photoItem);
            }
            if (i != 0)
            {
                // 分隔条
                LinearLayout devider =
                    (LinearLayout)LayoutInflater.from(this).inflate(R.layout.scenic_spots_devider, null);
                scenic_spots_table.addView(devider);
            }
            scenic_spots_table.addView(item);
        }
    }
    
    OnClickListener mOnClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if (v.getTag(R.id.tag_photo_list) != null)
            {
                @SuppressWarnings("unchecked")
                ArrayList<String> photos = (ArrayList<String>)v.getTag(R.id.tag_photo_list);// 照片集合
                int index = (Integer)v.getTag(R.id.tag_photo_index);// 点击的index
                Intent intentForView = new Intent(MyJourneyDetailActivity.this, ImageViewerActivity.class);
                intentForView.putExtra("image_index", index);
                intentForView.putStringArrayListExtra("image_urls", photos);
                startActivity(intentForView);
            }
        }
    };
    
    /**
     * 初始化详细行程
     */
    private void initTripDetail()
    {
        for (int i = 0; i < 1; i++)
        {
            // 初始化数据，解决继续滑动时候如果未初始化数据导致第二页部分不显示问题
            Journey journey = new Journey();
            journey.setTitle("D1");
            mJourneyList.add(journey);
        }
        trip_detail_list = (MyListView)findViewById(R.id.trip_detail_list);
        mTripPlanListAdapter = new ProductTripPlanListAdapter(this, mJourneyList,productTitleImgList);
        trip_detail_list.setAdapter(mTripPlanListAdapter);
        trip_detail_list.setOnItemClickListener(mTripItemClick);
    }
    
    private final OnItemClickListener mTripItemClick = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // 亮点特色布局Item
            // TODO Auto-generated method stub
            Intent intent = new Intent(MyJourneyDetailActivity.this, TripPlanDetailNewActivity.class);
            intent.putExtra("journey", (Journey)parent.getAdapter().getItem(position));
            startActivity(intent);
        }
    };
    
    class TripDetailItemClick implements OnClickListener
    {
        private final TripDetailInfo info;
        
        public TripDetailItemClick(TripDetailInfo info)
        {
            this.info = info;
        }
        
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            Intent intent = new Intent(MyJourneyDetailActivity.this, TripPlanDetailNewActivity.class);
            intent.putExtra("tripDetailInfo", info);
            startActivity(intent);
        }
    }
    
    /**
     * 初始化相似产品列表
     */
    private void initSimillarProductListView(List<ProductNewInfo> proList)
    {
        MyListView simillarListView = (MyListView)findViewById(R.id.product_detail_similar_listview);
        ProductAdapter simillarListAdapter = new ProductAdapter(this, proList);
        simillarListView.setAdapter(simillarListAdapter);
        simillarListView.setOnItemClickListener(mSimillarListListener);
    }
    
    OnItemClickListener mSimillarListListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // TODO Auto-generated method stub
            Intent intent;
            // Map<String, Object> map = mSimillarArrayList.get(position);
            LogUtil.i(TAG, "onItemClick", "position=" + position);
            // if (!StringUtils.isEmpty(product.getType()))
            // {
            // if (product.getType().endsWith("1"))
            // {
            // intent = new Intent(ProductDetailNewActivity.this,
            // ProductDetailActivity.class);
            // }
            // else
            // {
            // intent = new Intent(ProductDetailNewActivity.this,
            // GrouponProductDetailActivity.class);
            // }
            // intent.putExtra("product", product);
            // startActivity(intent);
            // }
        }
    };
    
    /**
     * 初始化LinearLayout内部item
     * 
     * @param name
     * @param res
     * @return
     */
    private LinearLayout getServiceItem(String name, int res)
    {
        LinearLayout serviceItem =
            (LinearLayout)LayoutInflater.from(this).inflate(R.layout.product_services_item, null);
        ImageView serviceLogo = (ImageView)serviceItem.findViewById(R.id.product_service_logo);
        TextView serviceName = (TextView)serviceItem.findViewById(R.id.product_service_name);
        serviceLogo.setImageResource(res);
        serviceName.setText(name);
        return serviceItem;
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.layout_back_button:
                finish();
                activityAnimationClose();
                break;
            case R.id.product_service_button:
                intent = new Intent(this, ProductServiceActivity.class);
                intent.putExtra("bgUrl", "http://pic4.zhongsou.com/image/4804b5f2c51fd9b41da.jpg");
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.product_evaluate_detail_button:
                intent = new Intent(this, UserCommentActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.product_author_photo:
                ServicerPersonInfoDialog dialog = new ServicerPersonInfoDialog(this);
                dialog.show();
                break;
            case R.id.product_operation_button:
                OrderPopWindow pop = new OrderPopWindow(this, "取消预订", new OperationListener()
                {
                    @Override
                    public void refund(String string)
                    {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyJourneyDetailActivity.this, "点击了取消预订", Toast.LENGTH_SHORT).show();
                    }
                    
                    @Override
                    public void contact()
                    {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyJourneyDetailActivity.this, "点击了联系TA", Toast.LENGTH_SHORT).show();
                    }
                });
                pop.show(v);
                break;
            case R.id.button_pay:
                intent = new Intent(this, MyOrderDetailActivity.class);
                startActivity(intent);
                activityAnimationOpen();
                break;
            default:
                break;
        }
    }
    
    /****************** request *******************/
    private void queryMyTripPlanDetail()
    {
        HttpUtil.get(Urls.mytrip_detail_url + "1", null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i(TAG, "queryMyTripPlanDetail", "response=" + response.toString());
                JSONObject data = response.optJSONObject("data");
                if (data == null)
                {
                    return;
                }
                trip_state.setText(data.optString("status"));
                order_leave_word.setText(data.optString("leaveWord"));
                JSONObject product = data.optJSONObject("product");
                ProductNewInfo productInfo = new ProductNewInfo();
                JSONArray topics = product.optJSONArray("topics");
                for (int i = 0; i < topics.length(); i++)
                {
                    productInfo.getTopics().add(topics.opt(i).toString());
                }
                productInfo.setId(product.optString("id"));
                productInfo.setDistance(product.optString("distance"));
                productInfo.setTitle(product.optString("title"));
                productInfo.setPoiCount(product.optString("poiCount"));
                productInfo.setPrice(product.optString("price"));
                productInfo.setDays(product.optString("days"));
                productInfo.setDescription(product.optString("description"));
                productInfo.setPriceMax(product.optString("priceMax"));
                productInfo.setTitleImg(product.optString("titleImg"));
                productInfo.setMaxMember(product.optString("maxMember"));
                productInfo.setProductType(product.optString("productType"));
                productInfo.setCustomFor(data.optString("customFor"));
                productInfo.setServices(data.optString("serviceCodes"));
                JSONObject userJSON = product.optJSONObject("creater");
                if (userJSON != null)
                {
                    productInfo.getUser().setUserId(userJSON.optString("userId"));
                    productInfo.getUser().setNickname(userJSON.optString("nickName"));
                    productInfo.getUser().setAvatar(userJSON.optString("avatar"));
                    productInfo.getUser().setIntroduction(userJSON.optString("introduction"));
                }
                JSONArray featuresJSONList = data.optJSONArray("features");
                if (featuresJSONList != null)
                {
                    for (int i = 0; i < featuresJSONList.length(); i++)
                    {
                        JSONObject featureJSON = featuresJSONList.optJSONObject(i);
                        FeatureInfo feature = new FeatureInfo();
                        feature.setId(featureJSON.optString("id"));
                        feature.setTitle(featureJSON.optString("title"));
                        feature.setDesc(featureJSON.optString("desc"));
                        JSONArray imagesJSONList = featureJSON.optJSONArray("images");
                        if (imagesJSONList != null)
                        {
                            for (int j = 0; j < imagesJSONList.length(); j++)
                            {
                                JSONObject imagesJSON = imagesJSONList.optJSONObject(j);
                            	ImageInfo info = new ImageInfo();
								info.setUrl(imagesJSON.optString("url"));
								info.setDesc(imagesJSON.optString("desc"));
								feature.getImages().add(info);
                            }
                        }
                        productInfo.getFeatures().add(feature);
                    }
                }
                initProductInfo(productInfo);
                initScenicSpots(productInfo);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("failed statusCode=" + statusCode);
                System.out.println(responseString);
            }
        });
    }
    
    private void queryProductJourney()
    {
        HttpUtil.get(Urls.product_journey + "1", null, new JsonHttpResponseHandler()
        {
            @Override
            public void setRequestURI(URI requestURI)
            {
                // TODO Auto-generated method stub
                super.setRequestURI(requestURI);
                LogUtil.i(TAG, "QueryProductJourney", "requestURI=" + requestURI);
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "QueryProductJourney", "statusCode=" + statusCode);
                // LogUtil.i(TAG, "QueryProductJourney", "response=" + response.toString());
                String code = response.optString("code");
                if (!code.equals("00000"))
                {
                    return;
                }
                mJourneyList.clear();
                JSONArray journeyJSONList = response.optJSONArray("data");
                if (journeyJSONList == null)
                {
                    return;
                }
                for (int i = 0; i < journeyJSONList.length(); i++)
                {
                    JSONObject journeyJSON = journeyJSONList.optJSONObject(i);
                    Journey journey = new Journey();
                    journey.setId(journeyJSON.optString("id"));
                    journey.setTitle(journeyJSON.optString("title"));
                    journey.setDescription(journeyJSON.optString("description"));
                    JSONArray trafficJSONList = journeyJSON.optJSONArray("traffic");
                    if (trafficJSONList != null)
                        for (int j = 0; j < trafficJSONList.length(); j++)
                        {
                            JSONObject serviceJSON = trafficJSONList.optJSONObject(j);
                            ServResrouce service = new ServResrouce();
                            service.setServId(serviceJSON.optString("servId"));
                            service.setServName(serviceJSON.optString("servName"));
                            service.setServAlias(serviceJSON.optString("servAlias"));
                            service.setServType(serviceJSON.optString("servType"));
                            service.setRank(serviceJSON.optString("rank"));
                            journey.getTrafficList().add(service);
                        }
                    JSONArray stayJSONList = journeyJSON.optJSONArray("stay");
                    if (stayJSONList != null)
                        for (int j = 0; j < stayJSONList.length(); j++)
                        {
                            JSONObject serviceJSON = stayJSONList.optJSONObject(j);
                            ServResrouce service = new ServResrouce();
                            service.setServId(serviceJSON.optString("servId"));
                            service.setServName(serviceJSON.optString("servName"));
                            service.setServAlias(serviceJSON.optString("servAlias"));
                            service.setServType(serviceJSON.optString("servType"));
                            service.setRank(serviceJSON.optString("rank"));
                            journey.getStayList().add(service);
                        }
                    JSONArray attractionsJSONList = journeyJSON.optJSONArray("attractions");
                    if (attractionsJSONList != null)
                        for (int j = 0; j < attractionsJSONList.length(); j++)
                        {
                            JSONObject serviceJSON = attractionsJSONList.optJSONObject(j);
                            ServResrouce service = new ServResrouce();
                            service.setServId(serviceJSON.optString("servId"));
                            service.setServName(serviceJSON.optString("servName"));
                            service.setServAlias(serviceJSON.optString("servAlias"));
                            service.setServType(serviceJSON.optString("servType"));
                            service.setRank(serviceJSON.optString("rank"));
                            journey.getAttractionsList().add(service);
                        }
                    mJourneyList.add(journey);
                }
                mTripPlanListAdapter.notifyDataSetChanged();
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i(TAG, "QueryProductJourney", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QueryProductJourney", "responseString=" + responseString);
            }
        });
    }
    
    private void queryProductComment()
    {
        HttpUtil.get(Urls.product_comment + "1", null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // 用户评论
                LinearLayout layout_user_evaluate = (LinearLayout)findViewById(R.id.layout_user_evaluate);
                RoundImageView evaluate_user_photo = (RoundImageView)findViewById(R.id.evaluate_user_photo);
                TextView evaluate_user_name = (TextView)findViewById(R.id.evaluate_user_name);
                TextView evaluate_user_date = (TextView)findViewById(R.id.evaluate_user_date);
                TextView evaluate_user_content = (TextView)findViewById(R.id.evaluate_user_content);
                TextView evaluate_detail_button = (TextView)findViewById(R.id.product_evaluate_detail_button);
                evaluate_detail_button.setOnClickListener(MyJourneyDetailActivity.this);
                /***************************************************************/
                // LogUtil.i(TAG, "QueryProductComment", "statusCode=" + statusCode);
                // LogUtil.i(TAG, "QueryProductComment", "response=" + response.toString());
                JSONObject dataJSON = response.optJSONObject("data");
                if (dataJSON != null)
                {
                    layout_user_evaluate.setVisibility(View.VISIBLE);
                    JSONObject commentJSON = dataJSON.optJSONObject("comment");
                    if (commentJSON != null)
                    {
                        String content = commentJSON.optString("content");
                        String createTime = commentJSON.optString("createTime");
                        evaluate_user_content.setText(content);
                        evaluate_user_date.setText(createTime);
                    }
                    JSONObject userInfoJSON = dataJSON.optJSONObject("userInfo");
                    if (userInfoJSON != null)
                    {
                        String nickName = userInfoJSON.optString("nickName");
                        String avatar = userInfoJSON.optString("avatar");
                        evaluate_user_name.setText(nickName);
                        if (!StringUtils.isEmpty(avatar))
                        {
                            ImageLoader.getInstance().displayImage(Urls.imgHost + avatar, evaluate_user_photo);
                        }
                        
                    }
                    String totalComment = dataJSON.optString("commentsCount");
                    evaluate_detail_button.setText("查看全部评论" + "(" + totalComment + ")");
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i(TAG, "QueryProductComment", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QueryProductComment", "responseString=" + responseString);
            }
        });
    }
    
    private void QuerySimillarProduct(int index, String id)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", "" + index);
        params.put("pagesize", "10");
        params.put("productId", "1");
        HttpUtil.get(Urls.simillar_product, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "QuerySimillarProduct", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QuerySimillarProduct", "response=" + response.toString());
                JSONObject dataJSON = response.optJSONObject("data");
                if (dataJSON != null)
                {
                    JSONArray productsJsonList = dataJSON.optJSONArray("products");
                    List<ProductNewInfo> proList = new ArrayList<ProductNewInfo>();
                    if (productsJsonList == null)
                    {
                        return;
                    }
                    for (int i = 0; i < productsJsonList.length(); i++)
                    {
                        JSONObject productJson = productsJsonList.optJSONObject(i);
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
                                LogUtil.i(TAG, "topicJsonArray", topicJsonArray.optString(j));
                                topics.add(topicJsonArray.optString(j));
                            }
                            productNewInfo.setTopics(topics);
                        }
                        // JSONObject productJson = productsJsonList.optJSONObject(i);
                        // Map<String, Object> map = new HashMap<String, Object>();
                        // map.put("productLogoUrl", Urls.imgHost + productJson.opt("titleImg"));
                        // map.put("productTitle", productJson.opt("title"));
                        // map.put("productLocation", productJson.opt("location"));
                        // map.put("productTimeSchedual", productJson.opt("days"));
                        // map.put("productDistance", productJson.opt("distance"));
                        // map.put("productIntroduce", productJson.opt("description"));
                        // JSONArray topicsJSONList = productJson.optJSONArray("topics");
                        // StringBuilder topics = new StringBuilder("");
                        // if (topicsJSONList != null)
                        // {
                        // for (int k = 0; k < topicsJSONList.length(); k++)
                        // {
                        // if (k != 0)
                        // {
                        // topics.append("·");
                        // }
                        // topics.append(topicsJSONList.opt(k).toString());
                        // }
                        // map.put("productLabelLabel", topics.toString());
                        // }
                        proList.add(productNewInfo);
                    }
                    initSimillarProductListView(proList);
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i(TAG, "QuerySimillarProduct", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QuerySimillarProduct", "responseString=" + responseString);
            }
        });
    }
}
