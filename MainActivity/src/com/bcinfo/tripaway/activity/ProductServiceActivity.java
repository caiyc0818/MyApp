package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.CarInfoGridViewAdapter;
import com.bcinfo.tripaway.adapter.MyPagerAdapter;
import com.bcinfo.tripaway.adapter.ScenicDetailListAdapter;
import com.bcinfo.tripaway.adapter.ServiceContentListAdapter;
import com.bcinfo.tripaway.adapter.TipsAdapter;
import com.bcinfo.tripaway.animation.ServiceRotate3dAnimation;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.BoatInfo;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.CarServCategory;
import com.bcinfo.tripaway.bean.Cate;
import com.bcinfo.tripaway.bean.Facility;
import com.bcinfo.tripaway.bean.HotelInfo;
import com.bcinfo.tripaway.bean.HouseInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.MetroInfo;
import com.bcinfo.tripaway.bean.PoiInfo;
import com.bcinfo.tripaway.bean.ProductService;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.bean.ShoppingInfo;
import com.bcinfo.tripaway.bean.TipsInfo;
import com.bcinfo.tripaway.bean.TrainInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 产品提供服务
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月7日 上午9:41:54
 */
public class ProductServiceActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "ProductServiceActivity";
    
    private ViewPager mViewPager;
    
    private ArrayList<View> mListViews;
    
    private String productId;
    
    private List<ProductService> productServices;
    
    private ServiceContentListAdapter trafficAdapter;
    
    private TipsAdapter tipsAdapter;
    
    private List<TipsInfo> tipContentInfos;
    
    // private Gallery mGallery;
    
    // private ImageAdapter adapter;
    private LinearLayout titleLayout;
    
    private ArrayList<TextView> textViews;
    
    private List<String> commonTips = new ArrayList<String>();;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.product_service_activity);
        ImageView background = (ImageView)findViewById(R.id.back_ground);
        String bgUrl = getIntent().getStringExtra("bgUrl");
        productId = getIntent().getStringExtra("productId");
        if (!StringUtils.isEmpty(bgUrl))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + bgUrl, background);
        }
        
        ImageView back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnClickListener(this);
        titleLayout = (LinearLayout)findViewById(R.id.title_layout);
        // mGallery = (Gallery)findViewById(R.id.title_layout);
        // adapter = new ImageAdapter(this);
        // mGallery.setAdapter(adapter);
        // tipsInfo = new TipsInfo();
        productServices = new ArrayList<ProductService>();
        tipContentInfos = new ArrayList<TipsInfo>();
        queryProductService(productId);
    }
    
    private void initViewPager(List<TipsInfo> tipContentInfos, List<ProductService> productServices)
    {
        mViewPager = (ViewPager)findViewById(R.id.service_viewpage);
        mListViews = new ArrayList<View>();
        if (productServices != null)
        {
            textViews = new ArrayList<TextView>();
            int count = 0;
            if (tipContentInfos != null && tipContentInfos.size() > 0)
            {
                count = productServices.size() + 1;
            }
            else
            {
                count = productServices.size();
            }
            for (int i = 0; i < productServices.size(); i++)
            {
                View view = LayoutInflater.from(this).inflate(R.layout.service_page_list_item, null);
                initPageView(view, productServices.get(i));
                mListViews.add(view);
                View titleView = LayoutInflater.from(this).inflate(R.layout.gallery_title_layout, null);
                TextView textView = (TextView)titleView.findViewById(R.id.title_txt);
                textView.setText(productServices.get(i).getName());
                textViews.add(textView);
                titleLayout.addView(titleView, 120, LinearLayout.LayoutParams.MATCH_PARENT);
            }
        }
        if (tipContentInfos != null && tipContentInfos.size() > 0)
        {
            View pageTips = LayoutInflater.from(this).inflate(R.layout.service_detail_tips_info, null);
            initTipsPage(pageTips);
            mListViews.add(pageTips);
            View titleView = LayoutInflater.from(this).inflate(R.layout.gallery_title_layout, null);
            TextView textView = (TextView)titleView.findViewById(R.id.title_txt);
            textView.setText("贴士");
            textViews.add(textView);
            titleLayout.addView(titleView, 120, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        mViewPager.setAdapter(new MyPagerAdapter(mListViews));
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(0);
        Animation mAnimationRight = AnimationUtils.loadAnimation(ProductServiceActivity.this, R.anim.scale_animation);
        if (textViews != null && textViews.size() > 0)
        {
            textViews.get(0).setAlpha(1f);
            textViews.get(0).startAnimation(mAnimationRight);
        }
    }
    
    private void initTipsPage(View view)
    {
        final ListView listview = (ListView)view.findViewById(R.id.tip_content_listview);
        tipsAdapter = new TipsAdapter(this, tipContentInfos);
        listview.setAdapter(tipsAdapter);
        
    }
    
    private void initPageView(final View pageView, ProductService productService)
    {
        final ListView listview = (ListView)pageView.findViewById(R.id.service_content_listview);
        final RelativeLayout item_detail = (RelativeLayout)pageView.findViewById(R.id.item_detail_layout);
        ImageView back_btutton = (ImageView)pageView.findViewById(R.id.service_back_btutton);
        TextView pageTitle = (TextView)pageView.findViewById(R.id.service_description);
        pageTitle.setText(productService.getName());
        LinearLayout car_info = (LinearLayout)pageView.findViewById(R.id.layout_detail_car_info);
        car_info.setVisibility(View.VISIBLE);
        trafficAdapter = new ServiceContentListAdapter(this, productService.getProductServiceResources());
        listview.setAdapter(trafficAdapter);
        listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // TODO Auto-generated method stub
                ProductServiceResource info = (ProductServiceResource)parent.getAdapter().getItem(position);
                initDetailCarInfo(pageView, info);
                final int mCenterX = listview.getWidth() / 2;
                final int mCenterY = listview.getHeight() / 2;
                ServiceRotate3dAnimation leftAnimation = new ServiceRotate3dAnimation(0, -90, mCenterX, mCenterY);
                ServiceRotate3dAnimation rightAnimation = new ServiceRotate3dAnimation(90, 0, mCenterX, mCenterY);
                leftAnimation.setFillAfter(true);
                leftAnimation.setDuration(500);
                rightAnimation.setFillAfter(true);
                rightAnimation.setDuration(500);
                item_detail.startAnimation(rightAnimation);
                listview.startAnimation(leftAnimation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        item_detail.bringToFront();
                    }
                }, 250);
            }
        });
        back_btutton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int mCenterX = item_detail.getWidth() / 2;
                final int mCenterY = item_detail.getHeight() / 2;
                ServiceRotate3dAnimation leftAnimation = new ServiceRotate3dAnimation(0, -90, mCenterX, mCenterY);
                ServiceRotate3dAnimation rightAnimation = new ServiceRotate3dAnimation(90, 0, mCenterX, mCenterY);
                leftAnimation.setFillAfter(true);
                leftAnimation.setDuration(500);
                rightAnimation.setFillAfter(true);
                rightAnimation.setDuration(500);
                item_detail.startAnimation(leftAnimation);
                listview.startAnimation(rightAnimation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listview.bringToFront();
                        // TODO Auto-generated method stub
                    }
                }, 250);
            }
        });
    }
    
    private void initDetailCarInfo(View view, ProductServiceResource resource)
    {
        LinearLayout flagLayout = (LinearLayout)view.findViewById(R.id.tag_layout);
        LinearLayout servNameLayout = (LinearLayout)view.findViewById(R.id.layout_car_info);
        TextView trafficName = (TextView)view.findViewById(R.id.traffic_name);
        TextView trafficServalia = (TextView)view.findViewById(R.id.traffic_servalia);
        ImageView trafficPhoto = (ImageView)view.findViewById(R.id.traffic_photo);
        TextView trafficDescription = (TextView)view.findViewById(R.id.traffic_description);
        LinearLayout layout1 = (LinearLayout)view.findViewById(R.id.layout1);
        LinearLayout layout2 = (LinearLayout)view.findViewById(R.id.layout2);
        LinearLayout layout3 = (LinearLayout)view.findViewById(R.id.layout3);
        LinearLayout layout4 = (LinearLayout)view.findViewById(R.id.layout4);
        LinearLayout layout5 = (LinearLayout)view.findViewById(R.id.layout5);
        LinearLayout layout6 = (LinearLayout)view.findViewById(R.id.layout6);
        LinearLayout layout7 = (LinearLayout)view.findViewById(R.id.layout7);
        LinearLayout layout8 = (LinearLayout)view.findViewById(R.id.layout8);
        ImageView logo1 = (ImageView)view.findViewById(R.id.logo1);
        ImageView logo2 = (ImageView)view.findViewById(R.id.logo2);
        ImageView logo3 = (ImageView)view.findViewById(R.id.logo3);
        ImageView logo4 = (ImageView)view.findViewById(R.id.logo4);
        ImageView logo5 = (ImageView)view.findViewById(R.id.logo5);
        ImageView logo6 = (ImageView)view.findViewById(R.id.logo6);
        ImageView logo7 = (ImageView)view.findViewById(R.id.logo7);
        ImageView logo8 = (ImageView)view.findViewById(R.id.logo8);
        TextView title1 = (TextView)view.findViewById(R.id.title1);
        TextView title2 = (TextView)view.findViewById(R.id.title2);
        TextView title3 = (TextView)view.findViewById(R.id.title3);
        TextView title4 = (TextView)view.findViewById(R.id.title4);
        TextView title5 = (TextView)view.findViewById(R.id.title5);
        TextView title6 = (TextView)view.findViewById(R.id.title6);
        TextView title7 = (TextView)view.findViewById(R.id.title7);
        TextView title8 = (TextView)view.findViewById(R.id.title8);
        
        TextView text1 = (TextView)view.findViewById(R.id.text1);
        TextView text2 = (TextView)view.findViewById(R.id.text2);
        TextView text3 = (TextView)view.findViewById(R.id.text3);
        TextView text4 = (TextView)view.findViewById(R.id.text4);
        TextView text5 = (TextView)view.findViewById(R.id.text5);
        TextView text6 = (TextView)view.findViewById(R.id.text6);
        TextView text7 = (TextView)view.findViewById(R.id.text7);
        TextView text8 = (TextView)view.findViewById(R.id.text8);
        
        TextView gridTitle1 = (TextView)view.findViewById(R.id.grid_title1);
        TextView gridTitle2 = (TextView)view.findViewById(R.id.grid_title2);
        GridView gridView1 = (GridView)view.findViewById(R.id.grid1);
        GridView gridView2 = (GridView)view.findViewById(R.id.grid2);
        ListView imageLst = (ListView)view.findViewById(R.id.image_detail_listview);
        flagLayout.setVisibility(View.GONE);
        flagLayout.removeAllViews();
        servNameLayout.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);
        layout5.setVisibility(View.GONE);
        layout6.setVisibility(View.GONE);
        layout7.setVisibility(View.GONE);
        layout8.setVisibility(View.GONE);
        gridTitle1.setVisibility(View.GONE);
        gridTitle2.setVisibility(View.GONE);
        gridView1.setVisibility(View.GONE);
        gridView2.setVisibility(View.GONE);
        if (resource.getServType().equals("car") || resource.getServType().equals("boat")
            || resource.getServType().equals("metro") || resource.getServType().equals("train"))
        {
            if (resource.getServType().equals("car"))
            {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.VISIBLE);
                layout6.setVisibility(View.VISIBLE);
                layout7.setVisibility(View.VISIBLE);
                gridTitle1.setVisibility(View.VISIBLE);
                gridTitle2.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.VISIBLE);
                gridView2.setVisibility(View.VISIBLE);
                CarExt info = JsonUtil.getCarInfo(resource.getResourceExt());
                trafficName.setText(info.getCarName());
                if (!StringUtils.isEmpty(resource.getTitleImage()))
                {
                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.default_photo));
                }
                trafficDescription.setText(resource.getServDesc());
                // carPrice.setText("￥" + resource.getFee());
                title1.setText("汽车品牌");
                logo1.setImageResource(R.drawable.brand);
                text1.setText(info.getCarName());
                title2.setText("汽车出产国");
                logo2.setImageResource(R.drawable.car_place);
                text2.setText(info.getCarPlace());
                title3.setText("行驶里程");
                logo3.setImageResource(R.drawable.distance);
                text3.setText(info.getDistance());
                title4.setText("购车时间");
                logo4.setImageResource(R.drawable.shopping_year);
                text4.setText(info.getShoppingTime());
                title5.setText("座位数");
                logo5.setImageResource(R.drawable.seat_count);
                text5.setText(info.getSeatNum());
                title6.setText("汽车类型");
                logo6.setImageResource(R.drawable.house_type);
                text6.setText(info.getCarType());
                title7.setText("后备箱容量");
                logo7.setImageResource(R.drawable.capacity);
                text7.setText(info.getCapacity());
                List<CarServCategory> categoryList = info.getCarServers();
                for (int j = 0; j < categoryList.size(); j++)
                {
                    if (categoryList.get(j).getCateCode().equals("facility_car_inside"))
                    {
                        gridTitle1.setText("车内配套");
                        List<Facility> facilityCarInsideList = new ArrayList<Facility>();
                        facilityCarInsideList.addAll(categoryList.get(j).getFacilities());
                        CarInfoGridViewAdapter insideAdapter = new CarInfoGridViewAdapter(this, facilityCarInsideList);
                        gridView1.setAdapter(insideAdapter);
                    }
                    else
                    {
                        gridTitle2.setText("费用范围");
                        List<Facility> facilityCarFeesList = new ArrayList<Facility>();
                        facilityCarFeesList.addAll(categoryList.get(j).getFacilities());
                        CarInfoGridViewAdapter feeAdapter = new CarInfoGridViewAdapter(this, facilityCarFeesList);
                        gridView2.setAdapter(feeAdapter);
                    }
                }
                List<ImageInfo> imageList = new ArrayList<ImageInfo>();
                for (int k = 0; k < info.getCarImages().size(); k++)
                {
                    imageList.addAll(info.getCarImages().get(k).getCarImageList());
                }
                ScenicDetailListAdapter carImageAdapter = new ScenicDetailListAdapter(this, imageList);
                imageLst.setAdapter(carImageAdapter);
            }
            else if (resource.getServType().equals("boat"))
            {
                BoatInfo info = JsonUtil.getBoatInfo(resource.getResourceExt());
                trafficName.setText(resource.getServName());
                if (!StringUtils.isEmpty(resource.getTitleImage()))
                {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.ic_launcher));
                }
                
                trafficDescription.setText(resource.getServDesc());
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                title1.setText("轮船类型");
                logo1.setImageResource(R.drawable.house_type);
                title2.setText("始发港");
                logo2.setImageResource(R.drawable.address);
                title3.setText("目的港");
                logo3.setImageResource(R.drawable.address);
                title4.setText("运行时间");
                logo4.setImageResource(R.drawable.time);
                text1.setText(info.getBoatType());
                text2.setText(info.getStartingPoint());
                text3.setText(info.getDestPoint());
                text4.setText(info.getElapsedTime());
                
                List<ImageInfo> imageList = new ArrayList<ImageInfo>();
                imageList.addAll(info.getImageInfoList());
                ScenicDetailListAdapter boatImageAdapter = new ScenicDetailListAdapter(this, imageList);
                imageLst.setAdapter(boatImageAdapter);
            }
            else if (resource.getServType().equals("metro"))
            {
                MetroInfo metroInfo = JsonUtil.getMetroInfo(resource.getResourceExt());
                trafficName.setText(resource.getServName());
                if (StringUtils.isEmpty(resource.getTitleImage()))
                {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.ic_launcher));
                }
                
                trafficDescription.setText(resource.getServDesc());
                layout3.setVisibility(View.VISIBLE);
                title1.setText("始发站");
                logo1.setImageResource(R.drawable.address);
                title2.setText("目的站");
                logo2.setImageResource(R.drawable.address);
                title3.setText("运行时间");
                logo3.setImageResource(R.drawable.time);
                text1.setText(metroInfo.getStartingPoint());
                text2.setText(metroInfo.getDestPoint());
                text3.setText(metroInfo.getElapsedTime());
            }
            else if (resource.getServType().equals("train"))
            {
                TrainInfo trainInfo = JsonUtil.getTrainInfo(resource.getResourceExt());
                trafficName.setText(resource.getServName());
                if (!StringUtils.isEmpty(resource.getTitleImage()))
                {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.ic_launcher));
                }
                
                trafficDescription.setText(resource.getServDesc());
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.VISIBLE);
                title1.setText("类型");
                logo1.setImageResource(R.drawable.house_type);
                title2.setText("始发站");
                logo2.setImageResource(R.drawable.address);
                title3.setText("目的站");
                logo3.setImageResource(R.drawable.address);
                title4.setText("行驶里程");
                logo4.setImageResource(R.drawable.distance);
                title5.setText("运行时间");
                logo5.setImageResource(R.drawable.time);
                text1.setText(trainInfo.getTrainType());
                text2.setText(trainInfo.getStartingPoint());
                text3.setText(trainInfo.getDestPoint());
                text4.setText(trainInfo.getDistance());
                text5.setText(trainInfo.getElapsedTime());
            }
        }
        else if (resource.getServType().equals("house") || resource.getServType().equals("hotel"))
        {
            List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
            if (resource.getTags().size() > 0)
            {
                flagLayout.addView(getLabelLogo());
            }
            for (int k = 0; k < resource.getTags().size(); k++)
            {
                flagLayout.setVisibility(View.VISIBLE);
                flagLayout.addView(getLable(resource.getTags().get(k)));
            }
            if (!StringUtils.isEmpty(resource.getTitleImage()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                    trafficPhoto,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            trafficDescription.setText(resource.getServDesc());
            if (resource.getServType().equals("house"))
            {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.VISIBLE);
                gridTitle1.setVisibility(View.VISIBLE);
                gridTitle2.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.VISIBLE);
                gridView2.setVisibility(View.VISIBLE);
                title1.setText("地址");
                logo1.setImageResource(R.drawable.address);
                title2.setText("房间类型");
                logo2.setImageResource(R.drawable.house_type);
                title3.setText("入住退房时间");
                logo3.setImageResource(R.drawable.in_out_time);
                title4.setText("可住人数");
                logo4.setImageResource(R.drawable.occupancy);
                title5.setText("卫生间数");
                logo5.setImageResource(R.drawable.toilet);
                HouseInfo info = JsonUtil.getHouseInfo(resource.getResourceExt());
                
                text1.setText(info.getAddress());
                text2.setText(info.getBedType());
                text3.setText(info.getCheckTime());
                text4.setText(info.getHoldnum());
                text5.setText(info.getToilet());
                
                gridTitle1.setText("房内配套");
                gridTitle2.setText("其他");
                for (int j = 0; j < info.getHouseServs().size(); j++)
                {
                    if (info.getHouseServs().get(j).getCateCode().equals("facility_rooms"))
                    {
                        List<Facility> facilityRoomsList = new ArrayList<Facility>();
                        facilityRoomsList.addAll(info.getHouseServs().get(j).getFacilities());
                        CarInfoGridViewAdapter insideAdapter =
                            new CarInfoGridViewAdapter(ProductServiceActivity.this, facilityRoomsList);
                        gridView1.setAdapter(insideAdapter);
                    }
                    else
                    {
                        List<Facility> facilityOtherList = new ArrayList<Facility>();
                        facilityOtherList.addAll(info.getHouseServs().get(j).getFacilities());
                        CarInfoGridViewAdapter feeAdapter =
                            new CarInfoGridViewAdapter(ProductServiceActivity.this, facilityOtherList);
                        gridView2.setAdapter(feeAdapter);
                    }
                }
                imageInfos.addAll(info.getImages());
            }
            else
            {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                title1.setText("酒店星级");
                logo1.setImageResource(R.drawable.house_level);
                title2.setText("房间类型");
                logo2.setImageResource(R.drawable.house_type);
                title3.setText("地址");
                logo3.setImageResource(R.drawable.address);
                title4.setText("入住退房时间");
                logo4.setImageResource(R.drawable.in_out_time);
                HotelInfo info = JsonUtil.getHotelInfo(resource.getResourceExt());
                
                text1.setText(info.getStarLevel());
                text2.setText(info.getBedType());
                text3.setText(info.getAddress());
                text4.setText(info.getCheckTime());
                imageInfos.addAll(info.getImages());
            }
            ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(ProductServiceActivity.this, imageInfos);
            imageLst.setAdapter(adapter);
        }
        if (resource.getServType().equals("cate"))
        {
            if (resource.getTags().size() > 0)
            {
                flagLayout.addView(getLabelLogo());
            }
            for (int k = 0; k < resource.getTags().size(); k++)
            {
                flagLayout.setVisibility(View.VISIBLE);
                flagLayout.addView(getLable(resource.getTags().get(k)));
            }
            if (!StringUtils.isEmpty(resource.getTitleImage()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                    trafficPhoto,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            trafficDescription.setText(resource.getServDesc());
            List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
            Cate cate = JsonUtil.getCate(resource.getResourceExt());
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            title1.setText("人均消费");
            logo1.setImageResource(R.drawable.price);
            title2.setText("地址");
            logo2.setImageResource(R.drawable.address);
            title3.setText("时间");
            logo3.setImageResource(R.drawable.time);
            text1.setText("￥" + resource.getFee());
            text2.setText(cate.getAddress());
            text3.setText(cate.getBusinessHours());
            imageInfos.addAll(cate.getImageInfoList());
            ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(ProductServiceActivity.this, imageInfos);
            imageLst.setAdapter(adapter);
        }
        else if (resource.getServType().equals("shopping"))
        {
            if (resource.getTags().size() > 0)
            {
                flagLayout.addView(getLabelLogo());
            }
            for (int k = 0; k < resource.getTags().size(); k++)
            {
                flagLayout.setVisibility(View.VISIBLE);
                flagLayout.addView(getLable(resource.getTags().get(k)));
            }
            if (!StringUtils.isEmpty(resource.getTitleImage()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                    trafficPhoto,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            trafficDescription.setText(resource.getServDesc());
            List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            
            title1.setText("地址");
            logo1.setImageResource(R.drawable.address);
            title2.setText("营业时间");
            logo2.setImageResource(R.drawable.time);
            ShoppingInfo info = JsonUtil.getShoppingInfo(resource.getResourceExt());
            text1.setText(info.getAddress());
            text2.setText(info.getBusinessHours());
            imageInfos.addAll(info.getImageInfoList());
            ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(ProductServiceActivity.this, imageInfos);
            imageLst.setAdapter(adapter);
        }
        else if (resource.getServType().equals("poi"))
        {
            if (resource.getTags().size() > 0)
            {
                flagLayout.addView(getLabelLogo());
            }
            for (int k = 0; k < resource.getTags().size(); k++)
            {
                flagLayout.setVisibility(View.VISIBLE);
                flagLayout.addView(getLable(resource.getTags().get(k)));
            }
            if (!StringUtils.isEmpty(resource.getTitleImage()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                    trafficPhoto,
                    AppConfig.options(R.drawable.ic_launcher));
            }
            
            trafficDescription.setText(resource.getServDesc());
            List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            title1.setText("门票");
            logo1.setImageResource(R.drawable.price);
            title2.setText("地址");
            logo2.setImageResource(R.drawable.address);
            title3.setText("时间");
            logo3.setImageResource(R.drawable.time);
            PoiInfo info = JsonUtil.getPoiInfo(resource.getResourceExt());
            text1.setText("￥" + info.getTicket());
            text2.setText(info.getAddress());
            text3.setText(info.getBusinessHours());
            imageInfos.addAll(info.getImageInfoList());
            ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(ProductServiceActivity.this, imageInfos);
            imageLst.setAdapter(adapter);
        }
    }
    
    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageSelected(int position)
        {
            Animation mAnimationRight =
                AnimationUtils.loadAnimation(ProductServiceActivity.this, R.anim.scale_animation);
            for (int i = 0; i < textViews.size(); i++)
            {
                if (i == position)
                {
                    LogUtil.e(TAG, "onPageSelected", i + "--position-->" + position);
                    textViews.get(i).setAlpha(1f);
                    textViews.get(i).startAnimation(mAnimationRight);
                }
                else
                {
                    LogUtil.e(TAG, "onPageSelected111", i + "--position-->" + position);
                    textViews.get(i).clearAnimation();
                    textViews.get(i).setAlpha(0.5f);
                }
            }
        }
        
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }
        
        @Override
        public void onPageScrollStateChanged(int arg0)
        {
        }
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            default:
                break;
        }
    }
    
    /****************** request *******************/
    private void queryProductService(String id)
    {
        LogUtil.i(TAG, "queryProductService", "url=" + Urls.product_service + id);
        HttpUtil.get(Urls.product_service + id, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "queryProductService", "response=" + response.toString());
                JSONObject dataJSON = response.optJSONObject("data");
                // TipsInfo tipsInfo = null;
                if (!StringUtils.isEmpty(dataJSON.toString()))
                {
                    // 解析tip
                    JSONArray tipArray = dataJSON.optJSONArray("tips");
                    if (tipArray != null && tipArray.length() > 0)
                    {
                        for (int i = 0; i < tipArray.length(); i++)
                        {
                            TipsInfo tipsInfo = new TipsInfo();
                            JSONObject tipJsonObject = tipArray.optJSONObject(i);
                            tipsInfo.setId(tipJsonObject.optString("id"));
                            tipsInfo.setTitle(tipJsonObject.optString("title"));
                            tipsInfo.setType(tipJsonObject.optString("type"));
                            tipsInfo.setContent(tipJsonObject.optString("content"));
                            tipContentInfos.add(tipsInfo);
                        }
                    }
                    // 解析service
                    JSONArray serviceJsonArray = dataJSON.optJSONArray("service");
                    if (serviceJsonArray != null && serviceJsonArray.length() > 0)
                    {
                        // LogUtil.d(TAG, "serviceJsonArray.length()", serviceJsonArray.length() + "-->"
                        // + serviceJsonArray.toString());
                        for (int i = 0; i < serviceJsonArray.length(); i++)
                        {
                            JSONObject serviceJsonObject;
                            try
                            {
                                serviceJsonObject = serviceJsonArray.getJSONObject(i);
                                // LogUtil.d(TAG, "serviceJsonObject", serviceJsonObject.toString());
                                ProductService productService = new ProductService();
                                productService.setCode(serviceJsonObject.optString("code"));
                                productService.setName(serviceJsonObject.optString("name"));
                                JSONArray resourceJsonArray = serviceJsonObject.optJSONArray("resource");
                                if (resourceJsonArray != null && resourceJsonArray.length() > 0)
                                {
                                    List<ProductServiceResource> productServiceResources =
                                        new ArrayList<ProductServiceResource>();
                                    // LogUtil.d(TAG, "resourceJsonArray.length()", resourceJsonArray.length() + "-->"
                                    // + resourceJsonArray.toString());
                                    for (int j = 0; j < resourceJsonArray.length(); j++)
                                    {
                                        JSONObject resourceJsonObject = resourceJsonArray.getJSONObject(j);
                                        // LogUtil.d(TAG, "resourceJsonObject", resourceJsonObject.toString());
                                        ProductServiceResource resource = new ProductServiceResource();
                                        resource.setId(resourceJsonObject.optString("id"));
                                        resource.setServType(resourceJsonObject.optString("servType"));
                                        // LogUtil.e(TAG, "servType", resourceJsonObject.optString("servType"));
                                        resource.setServName(resourceJsonObject.optString("servName"));
                                        resource.setServAlias(resourceJsonObject.optString("servAlias"));
                                        resource.setServDesc(resourceJsonObject.optString("servDesc"));
                                        JSONArray tagsArray = resourceJsonObject.optJSONArray("tags");
                                        if (tagsArray != null && tagsArray.length() > 0)
                                        {
                                            ArrayList<String> tagList = new ArrayList<String>();
                                            for (int k = 0; k < tagsArray.length(); k++)
                                            {
                                                String tag = tagsArray.optString(k);
                                                tagList.add(tag);
                                            }
                                            resource.setTags(tagList);
                                        }
                                        resource.setFee(resourceJsonObject.optString("fee"));
                                        resource.setFeeType(resourceJsonObject.optString("feeType"));
                                        resource.setTitleImage(resourceJsonObject.optString("titleImage"));
                                        resource.setInventory(resourceJsonObject.optString("inventory"));
                                        JSONArray availableTimeJsonArray =
                                            resourceJsonObject.optJSONArray("availableTime");
                                        if (availableTimeJsonArray != null && availableTimeJsonArray.length() > 0)
                                        {
                                            List<AvailableTime> availableTimes = new ArrayList<AvailableTime>();
                                            // LogUtil.d(TAG,
                                            // "availableTimeJsonArray.length()",
                                            // availableTimeJsonArray.length() + "-->"
                                            // + availableTimeJsonArray.toString());
                                            for (int k = 0; k < availableTimeJsonArray.length(); k++)
                                            {
                                                JSONObject availableTimeJsonObject =
                                                    availableTimeJsonArray.getJSONObject(k);
                                                // LogUtil.d(TAG,
                                                // "availableTimeJsonObject",
                                                // availableTimeJsonObject.toString());
                                                AvailableTime availableTime = new AvailableTime();
                                                availableTime.setBeginDate(availableTimeJsonObject.optString("beginDate"));
                                                availableTime.setEndDate(availableTimeJsonObject.optString("endDate"));
                                                availableTimes.add(availableTime);
                                            }
                                            resource.setAvailableTimes(availableTimes);
                                        }
                                        resource.setResourceExt(resourceJsonObject.optString("resourceExt"));
                                        productServiceResources.add(resource);
                                    }
                                    productService.setProductServiceResources(productServiceResources);
                                    productServices.add(productService);
                                }
                            }
                            catch (JSONException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
                initViewPager(tipContentInfos, productServices);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                LogUtil.i(TAG, "queryProductService", "statusCode=" + statusCode);
            }
        });
    }
    
    // public class ImageAdapter extends BaseAdapter
    // {
    // private Context mContext;
    //
    // private String[] title = {"交通", "住宿", "美食", "购物", "贴士"};
    //
    // private int selectPosition = 0;
    //
    // public ImageAdapter(Context context)
    // {
    // mContext = context;
    // }
    //
    // public void setSelectPosition(int selectPosition)
    // {
    // this.selectPosition = selectPosition;
    // notifyDataSetChanged();
    // }
    //
    // public int getCount()
    // {
    // return title.length;
    // }
    //
    // public Object getItem(int position)
    // {
    // return position;
    // }
    //
    // public long getItemId(int position)
    // {
    // return position;
    // }
    //
    // public View getView(int position, View convertView, ViewGroup parent)
    // {
    // // ViewHolder holder = null;
    // // LayoutInflater inflator;
    // // if (convertView == null)
    // // {
    // LayoutInflater inflator = LayoutInflater.from(mContext);
    // convertView = inflator.inflate(R.layout.gallery_title_layout, null);
    // TextView textView = (TextView)convertView.findViewById(R.id.title_txt);
    // // convertView.setTag(holder);
    // // }
    // // else
    // // {
    // // holder = (ViewHolder)convertView.getTag();
    // // }
    // textView.setText(title[position]);
    // // TextView text = new TextView(mContext);
    // // text.setText(title[position]);
    // // text.setTextSize(16);
    // // text.setTextColor(getResources().getColor(R.color.white));
    // // text.setLayoutParams(new Gallery.LayoutParams(80, LayoutParams.WRAP_CONTENT));
    // textView.setAlpha(0.6f);
    // if (selectPosition == position)
    // {
    // Animation mAnimationRight =
    // AnimationUtils.loadAnimation(ProductServiceActivity.this, R.anim.scale_animation);
    // textView.setAnimation(mAnimationRight);
    // textView.setAlpha(1f);
    // }
    // else
    // {
    // textView.clearAnimation();
    // if (position < selectPosition)
    // {
    // textView.setAlpha(1 - (selectPosition - position) * 0.4f);
    //
    // }
    // else if (position > selectPosition)
    // {
    // textView.setAlpha(1 - (position - selectPosition) * 0.4f);
    // }
    // }
    // return convertView;
    // }
    // }
    private ImageView getLabelLogo()
    {
        ImageView img = new ImageView(this);
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;
        p.rightMargin = 4;
        img.setLayoutParams(p);
        img.setImageResource(R.drawable.tag);
        return img;
    }
    
    private LinearLayout getLable(String lable)
    {
        LinearLayout lableLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.lable_layout, null);
        TextView lableText = (TextView)lableLayout.findViewById(R.id.lable_text);
        lableText.setTextSize(12);
        lableText.setText(lable);
        return lableLayout;
    }
}
