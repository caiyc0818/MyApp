package com.bcinfo.tripaway.activity;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AliyrTripPlanListAdapter;
import com.bcinfo.tripaway.adapter.CarInfoGridViewAdapter;
import com.bcinfo.tripaway.adapter.MyTripPlanDetailParentAdapter;
import com.bcinfo.tripaway.adapter.ScenicDetailListAdapter;
import com.bcinfo.tripaway.adapter.TripPlanDetailNewListAdapter;
import com.bcinfo.tripaway.adapter.TripPlanDetailNewListParentAdapter;
import com.bcinfo.tripaway.bean.AirplaneInfo;
import com.bcinfo.tripaway.bean.AttractionAllInfo;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.BoatInfo;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.CarServCategory;
import com.bcinfo.tripaway.bean.Facility;
import com.bcinfo.tripaway.bean.HotelInfo;
import com.bcinfo.tripaway.bean.HouseInfo;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.MetroInfo;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.TrainInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 行程规划详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月27日 下午4:45:35
 */
public class TripPlanDetailNewActivity extends BaseActivity
{
    protected static final String TAG = "TripPlanDetailNewActivity";
    private ListView tripDetailListView;
    private ListView mDrawerList; 
    private LinearLayout mDrawerLy;
    private LinearLayout sssss;
//    private ScrollView sc;
    private TripPlanDetailNewListAdapter mTripPlanAdapter;
    private TripPlanDetailNewListParentAdapter mTripPlanParentAdapter;
    private MyTripPlanDetailParentAdapter myTripPlanParentAdapter;
    private AliyrTripPlanListAdapter mydapter;
    // private ArrayList<ServResrouce> mServiceArrList = new ArrayList<ServResrouce>();
    private ImageView backButton;
    private Journey mJourney;
    HashMap<String, Vector<ProductServiceResource>> map = new HashMap<String, Vector<ProductServiceResource>>();
    private ArrayList<Journey> mJourneyList = new ArrayList<Journey>();
    private int z;
    private Vector<ProductServiceResource> attractionsList = new Vector<ProductServiceResource>();//景点列表
    
    private List<ProductServiceResource> trafficList = new ArrayList<ProductServiceResource>();  //通报表
    
    private List<ProductServiceResource> stayList = new ArrayList<ProductServiceResource>();  //其他
    
    //private ArrayList<Object> allAttractionsList=new ArrayList<Object>();
    ArrayList<HashMap<String, Vector<ProductServiceResource>>> allAttractionsList=new  ArrayList<HashMap<String, Vector<ProductServiceResource>>>();
	private String productId;
	private DrawerLayout drawerLayout;//侧边栏
	private LinearLayout trip_detail_title;
	private View loginLoading;
	private AnimationDrawable loadingAnimation;
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.trip_plan_detail_new_activity);
        loginLoading = (View)findViewById(R.id.login_loading);
		  loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
       loadingAnimation.start();
        trip_detail_title= (LinearLayout)findViewById(R.id.trip_detail_title);
        trip_detail_title.getBackground().setAlpha(255);
        tripDetailListView = (ListView)findViewById(R.id.trip_plan_detail_listview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       // mJourney = getIntent().getParcelableExtra("journey");
        backButton=(ImageView) findViewById(R.id.trip_back_button);
        backButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				activityAnimationClose();
				finish();
			}
		});
        productId=getIntent().getStringExtra("productId");
       // mJourneyList= getIntent().getParcelableArrayListExtra("list");
        //setSecondTitle(mJourney.getTitle());
        RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);//抽屉里的view  
        mDrawerList.setDividerHeight(0);
        LinearLayout  hearderViewLayout = (LinearLayout) LayoutInflater.from(TripPlanDetailNewActivity.this).inflate(R.layout.drawer_listview_headview, null);
        mDrawerList.addHeaderView(hearderViewLayout);
        mydapter=new AliyrTripPlanListAdapter(this, mJourneyList);
        //mDrawerLy = (LinearLayout) findViewById(R.id.left);
        
       // sssss = (LinearLayout) findViewById(R.id.ly_ssss); //旁边的布局
//        sc=  (ScrollView) findViewById(R.id.sc_view); //旁边的布局
        initPlanDetailList();  //chushihua 
        //mDrawerLy.setVisibility(View.GONE);
        
		
       //mDrawerLayout.setDrawerShadow(R.drawable.pay_price_gray, GravityCompat.START);//设置shadow
        Button  btnxuanfu = (Button) findViewById(R.id.btn_xuanfu);
        ImageView  closeimg = (ImageView) findViewById(R.id.sta2);
        
        
        
//        sc.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				//mDrawerLayout.openDrawer(GravityCompat.END);
//				mDrawerLy.setVisibility(View.GONE);
//				 mDrawerList.setVisibility(View.GONE);
//				// mDrawerList.setAdapter(mTripPlanAdapter); //悬浮 列表  
//			}
//		});
        
        closeimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (drawerLayout.isDrawerOpen(Gravity.END))
                {
                    drawerLayout.closeDrawer(Gravity.END);
                    tripDetailListView.setFocusable(true);
    				tripDetailListView.setFocusableInTouchMode(true);
    				
    				drawerLayout.setFocusable(false);
    				drawerLayout.setFocusableInTouchMode(false);
                }
                else
                {
                    drawerLayout.openDrawer(Gravity.END);
                    drawerLayout.setFocusable(true);
                    drawerLayout.setFocusableInTouchMode(true);
                    tripDetailListView.setFocusable(false);
    				tripDetailListView.setFocusableInTouchMode(false);
                }
				
				//mDrawerLayout.openDrawer(GravityCompat.END);
				//mDrawerLy.setVisibility(View.GONE);
				 //mDrawerList.setVisibility(View.GONE);
				// mDrawerList.setAdapter(mTripPlanAdapter); //悬浮 列表  
			}
		});
        
        btnxuanfu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				
				//mDrawerLayout.closeDrawer(mDrawerList); 
				// mDrawerLayout.openDrawer(GravityCompat.END);//打开抽屉 
				// 按钮按下，将抽屉打开
				//mDrawerLayout.openDrawer(GravityCompat.START);
				/*mDrawerLy.setVisibility(View.VISIBLE);
				 mDrawerList.setAdapter(mydapter); //悬浮 列表  
				 mDrawerList.setVisibility(View.VISIBLE);*/

                if (drawerLayout.isDrawerOpen(Gravity.END))
                {
                    drawerLayout.closeDrawer(Gravity.END);
                    tripDetailListView.setFocusable(true);
    				tripDetailListView.setFocusableInTouchMode(true);
    				
    				drawerLayout.setFocusable(false);
    				drawerLayout.setFocusableInTouchMode(false);
                }
                else
                {
                    drawerLayout.openDrawer(Gravity.END);
                    
                    mDrawerList.setAdapter(mydapter);
                    drawerLayout.setFocusable(true);
                    drawerLayout.setFocusableInTouchMode(true);
                    tripDetailListView.setFocusable(false);
    				tripDetailListView.setFocusableInTouchMode(false);
                }
            
				 
			}
		});
        
        
        
        QueryAttractionAllInfo();
		
    }
    
    
    
    
    public void onDrawerOpened(View drawerView) {//抽屉打开后  
        //getActionBar().setTitle(mTitle);  
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()  
    }  
    
    public void onDrawerClosed(View view) {//抽屉关闭后  
        //getActionBar().setTitle(mDrawerTitle);  
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()  
    }
    private void initPlanDetailList()
    {
        
        myTripPlanParentAdapter = new MyTripPlanDetailParentAdapter(this, mJourneyList);
        
        tripDetailListView.setAdapter(myTripPlanParentAdapter);
        
        //mDrawerList.setVisibility(View.GONE);
        tripDetailListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				//mDrawerLy.setVisibility(View.GONE);
				 //mDrawerList.setVisibility(View.GONE);
				//Toast.makeText(this, "a 被点击了", 100)
				
			}
		});
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				
				tripDetailListView.setSelection(position-1);
				drawerLayout.closeDrawer(Gravity.END);
				//mDrawerLy.setVisibility(View.GONE);
				 //mDrawerList.setVisibility(View.GONE);
			}
		}); 
        //
        
        //mDrawerLayout.closeDrawer(GravityCompat.END);//关闭抽屉
    }
    
   /* private void initTrafficInfo(List<ProductServiceResource> trafficList)
    {
        TextView titleTxt = (TextView)findViewById(R.id.traffic_title);
        LinearLayout trafficLayout = (LinearLayout)findViewById(R.id.traffic_layout);
        if (trafficList == null || trafficList.size() == 0)
        {
            titleTxt.setVisibility(View.GONE);
            return;
        }
        for (int i = 0; i < trafficList.size(); i++)
        {
            ProductServiceResource resource = trafficList.get(i);
            if (resource.getServType().equals("car") || resource.getServType().equals("boat")
                || resource.getServType().equals("metro") || resource.getServType().equals("train"))
            {
            	//交通  汽车
                View trafficView = getPartView(R.layout.traffic_info_layout);
                LinearLayout flagLayout = (LinearLayout)trafficView.findViewById(R.id.tag_layout);
                TextView trafficName = (TextView)trafficView.findViewById(R.id.traffic_name);   //标题
                TextView trafficServalia = (TextView)trafficView.findViewById(R.id.traffic_servalia);
                ImageView trafficPhoto = (ImageView)trafficView.findViewById(R.id.traffic_photo);   //图片  
                TextView trafficDescription = (TextView)trafficView.findViewById(R.id.traffic_description);
                LinearLayout layout1 = (LinearLayout)trafficView.findViewById(R.id.layout1);   //Relativilayout
                LinearLayout layout2 = (LinearLayout)trafficView.findViewById(R.id.layout2);
                LinearLayout layout3 = (LinearLayout)trafficView.findViewById(R.id.layout3);
                LinearLayout layout4 = (LinearLayout)trafficView.findViewById(R.id.layout4);
                LinearLayout layout5 = (LinearLayout)trafficView.findViewById(R.id.layout5);
                LinearLayout layout6 = (LinearLayout)trafficView.findViewById(R.id.layout6);
                LinearLayout layout7 = (LinearLayout)trafficView.findViewById(R.id.layout7);
                LinearLayout layout8 = (LinearLayout)trafficView.findViewById(R.id.layout8);
                
                TextView title1 = (TextView)trafficView.findViewById(R.id.title1);
                TextView title2 = (TextView)trafficView.findViewById(R.id.title2);
                TextView title3 = (TextView)trafficView.findViewById(R.id.title3);
                TextView title4 = (TextView)trafficView.findViewById(R.id.title4);
                TextView title5 = (TextView)trafficView.findViewById(R.id.title5);
                TextView title6 = (TextView)trafficView.findViewById(R.id.title6);
                TextView title7 = (TextView)trafficView.findViewById(R.id.title7);
                TextView title8 = (TextView)trafficView.findViewById(R.id.title8);
                
                
              *//***
               * 新增logo liyuru
               * *//*  
                
                ImageView logo1 = (ImageView)trafficView.findViewById(R.id.logo1);
                ImageView logo2 = (ImageView)trafficView.findViewById(R.id.logo2);
                ImageView logo3 = (ImageView)trafficView.findViewById(R.id.logo3);
                ImageView logo4 = (ImageView)trafficView.findViewById(R.id.logo4);
                ImageView logo5 = (ImageView)trafficView.findViewById(R.id.logo5);
                ImageView logo6 = (ImageView)trafficView.findViewById(R.id.logo6);
                ImageView logo7 = (ImageView)trafficView.findViewById(R.id.logo7);
                ImageView logo8 = (ImageView)trafficView.findViewById(R.id.logo8);
                
                
                TextView text1 = (TextView)trafficView.findViewById(R.id.text1);
                TextView text2 = (TextView)trafficView.findViewById(R.id.text2);
                TextView text3 = (TextView)trafficView.findViewById(R.id.text3);
                TextView text4 = (TextView)trafficView.findViewById(R.id.text4);
                TextView text5 = (TextView)trafficView.findViewById(R.id.text5);
                TextView text6 = (TextView)trafficView.findViewById(R.id.text6);
                TextView text7 = (TextView)trafficView.findViewById(R.id.text7);
                TextView text8 = (TextView)trafficView.findViewById(R.id.text8);
                
                TextView gridTitle1 = (TextView)trafficView.findViewById(R.id.grid_title1);
                TextView gridTitle2 = (TextView)trafficView.findViewById(R.id.grid_title2);
                GridView gridView1 = (GridView)trafficView.findViewById(R.id.grid1);
                GridView gridView2 = (GridView)trafficView.findViewById(R.id.grid2);
                ListView imageLst = (ListView)trafficView.findViewById(R.id.image_detail_listview);
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
                List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
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
                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.ic_launcher));  //默认的图片
                    trafficDescription.setText(resource.getServDesc());
                    // carPrice.setText("￥" + resource.getFee());
                    title1.setText("汽车品牌");
                    text1.setText(info.getCarName());
                    title2.setText("汽车出产国");
                    text2.setText(info.getCarPlace());
                    title3.setText("行驶里程");
                    text3.setText(info.getDistance());
                    title4.setText("购车时间");
                    text4.setText(info.getShoppingTime());
                    title5.setText("座位数");
                    text5.setText(info.getSeatNum());
                    title6.setText("汽车类型");
                    text6.setText(info.getCarType());
                    title7.setText("后备箱容量");
                    logo1.setImageResource(R.drawable.pinpai);
                    logo2.setImageResource(R.drawable.chuchangguo);
                    logo3.setImageResource(R.drawable.licheng);
                    logo4.setImageResource(R.drawable.gouchrshijian);
                    logo5.setImageResource(R.drawable.zuowei);
                    logo6.setImageResource(R.drawable.leibie);
                    logo7.setImageResource(R.drawable.houbeixiang);
                  //新增liyuru动态设置图片  
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo1,
//                            AppConfig.options(R.drawable.pinpai)); 
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo2,
//                            AppConfig.options(R.drawable.chuchangguo)); 
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo3,
//                            AppConfig.options(R.drawable.licheng)); 
//                    
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo4,
//                            AppConfig.options(R.drawable.gouchrshijian)); 
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo5,
//                            AppConfig.options(R.drawable.zuowei)); 
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo6,
//                            AppConfig.options(R.drawable.leibie)); 
//                    
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo7,
//                            AppConfig.options(R.drawable.houbeixiang)); 
//                    
//                    
                    text7.setText(info.getCapacity());
                    List<CarServCategory> categoryList = info.getCarServers();
                    for (int j = 0; j < categoryList.size(); j++)
                    {
                        if (categoryList.get(j).getCateCode().equals("facility_car_inside"))
                        {
                            gridTitle1.setText("车内配套");
                            List<Facility> facilityCarInsideList = new ArrayList<Facility>();
                            facilityCarInsideList.addAll(categoryList.get(j).getFacilities());
                            CarInfoGridViewAdapter insideAdapter =
                                new CarInfoGridViewAdapter(this, facilityCarInsideList);
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
                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.ic_launcher));
                    trafficDescription.setText(resource.getServDesc());
                    layout3.setVisibility(View.VISIBLE);
                    layout4.setVisibility(View.VISIBLE);
                    title1.setText("轮船类型");
                    title2.setText("始发港");
                    title3.setText("目的港");
                    title4.setText("运行时间");
                    
                   //liyuru
                    
                    //新增liyuru动态设置图片  
                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
                            logo1,
                            AppConfig.options(R.drawable.leibie)); 
                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo2,
//                            AppConfig.options(R.drawable.chuchangguo)); 
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo3,
//                            AppConfig.options(R.drawable.licheng)); 
//                    
//                    
//                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
//                            logo4,
//                            AppConfig.options(R.drawable.gouchrshijian)); 
                    
                    
                    
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
                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.ic_launcher));
                    trafficDescription.setText(resource.getServDesc());
                    layout3.setVisibility(View.VISIBLE);
                    title1.setText("始发站");
                    title2.setText("目的站");
                    title3.setText("运行时间");
                    text1.setText(metroInfo.getStartingPoint());
                    text2.setText(metroInfo.getDestPoint());
                    text3.setText(metroInfo.getElapsedTime());
                }
                else if (resource.getServType().equals("train"))
                {
                    TrainInfo trainInfo = JsonUtil.getTrainInfo(resource.getResourceExt());
                    trafficName.setText(resource.getServName());
                    ImageLoader.getInstance().displayImage(resource.getTitleImage(),
                        trafficPhoto,
                        AppConfig.options(R.drawable.ic_launcher));
                    trafficDescription.setText(resource.getServDesc());
                    layout3.setVisibility(View.VISIBLE);
                    layout4.setVisibility(View.VISIBLE);
                    layout5.setVisibility(View.VISIBLE);
                    title1.setText("类型");
                    title2.setText("始发站");
                    title3.setText("目的站");
                    title4.setText("行驶里程");
                    title5.setText("运行时间");
                    text1.setText(trainInfo.getTrainType());
                    text2.setText(trainInfo.getStartingPoint());
                    text3.setText(trainInfo.getDestPoint());
                    text4.setText(trainInfo.getDistance());
                    text5.setText(trainInfo.getElapsedTime());
                }
                trafficLayout.addView(trafficView);
            }
            else if (resource.getServType().equals("airplane") || resource.getServType().equals("Iairplane"))
            {
            	//飞机
                AirplaneInfo info = JsonUtil.getAirplaneInfo(resource.getResourceExt());
                View airPlaneView = getPartView(R.layout.airplane_detail_layout);
                TextView planeName = (TextView)airPlaneView.findViewById(R.id.plane_name);
                TextView planeDescription = (TextView)airPlaneView.findViewById(R.id.plane_description);
                TextView flyTime = (TextView)airPlaneView.findViewById(R.id.time);
                TextView starPoint = (TextView)airPlaneView.findViewById(R.id.start_point);
                TextView startAlias = (TextView)airPlaneView.findViewById(R.id.start_alias);
                TextView arrivePoint = (TextView)airPlaneView.findViewById(R.id.arrive_point);
                TextView arriveAlias = (TextView)airPlaneView.findViewById(R.id.arrive_alias);
                TextView companyName = (TextView)airPlaneView.findViewById(R.id.company_name);
                TextView planePrice = (TextView)airPlaneView.findViewById(R.id.plane_price);
                TextView planeStartPoint = (TextView)airPlaneView.findViewById(R.id.plane_start_point);
                TextView planeArrivePoint = (TextView)airPlaneView.findViewById(R.id.plane_arrive_point);
                TextView planeStartTime = (TextView)airPlaneView.findViewById(R.id.plane_start_time);
                TextView planeArriveTime = (TextView)airPlaneView.findViewById(R.id.plane_arrive_time);
                TextView errorTime = (TextView)airPlaneView.findViewById(R.id.error_time);
                ImageView airLogo = (ImageView)airPlaneView.findViewById(R.id.air_logo);
                planeName.setText(resource.getServName());
                planeDescription.setText(resource.getServDesc());
                flyTime.setText(info.getElapsedTime());
                starPoint.setText(info.getStartingPoint());
                startAlias.setText(info.getStartingPointAlias());
                arrivePoint.setText(info.getDescPoint());
                arriveAlias.setText(info.getDescPointAlias());
                planePrice.setText("￥" + resource.getFee());
                planeStartPoint.setText(info.getStartingPoint());
                planeArrivePoint.setText(info.getDescPoint());
                planeStartTime.setText(info.getDepartureTime());
                planeArriveTime.setText(info.getArriveTime());
                trafficLayout.addView(airPlaneView);
            }
        }
    }*/
    
    /*private void initStayInfo(List<ProductServiceResource> stayList)
    {
        TextView titleTxt = (TextView)findViewById(R.id.stay_title);
        LinearLayout stayLayout = (LinearLayout)findViewById(R.id.stay_layout);
        if (stayList == null || stayList.size() == 0)
        {
            titleTxt.setVisibility(View.GONE);
            stayLayout.setVisibility(View.GONE);
            return;
        }
        for (int i = 0; i < stayList.size(); i++)
        {
        	//房间的布局的初始化   
            View stayView = getPartView(R.layout.stay_info_layout);
            LinearLayout flagLayout = (LinearLayout)stayView.findViewById(R.id.scenic_flag_layout);
            ImageView housePhoto = (ImageView)stayView.findViewById(R.id.house_photo);
            TextView houseDescription = (TextView)stayView.findViewById(R.id.house_description);
            LinearLayout layout1 = (LinearLayout)stayView.findViewById(R.id.layout1);
            LinearLayout layout2 = (LinearLayout)stayView.findViewById(R.id.layout2);
            LinearLayout layout3 = (LinearLayout)stayView.findViewById(R.id.layout3);
            LinearLayout layout4 = (LinearLayout)stayView.findViewById(R.id.layout4);
            LinearLayout layout5 = (LinearLayout)stayView.findViewById(R.id.layout5);
            
            
            *//**
             * 新增加  liyuru 
             * *//*
            ImageView imageview1 = (ImageView)stayView.findViewById(R.id.img1);
            ImageView imageview2 = (ImageView)stayView.findViewById(R.id.img2);
            ImageView imageview3 = (ImageView)stayView.findViewById(R.id.img3);
            ImageView imageview4 = (ImageView)stayView.findViewById(R.id.img4);
            ImageView imageview5 = (ImageView)stayView.findViewById(R.id.img5);
      
            
            
            TextView title1 = (TextView)stayView.findViewById(R.id.title1);
            TextView title2 = (TextView)stayView.findViewById(R.id.title2);
            TextView title3 = (TextView)stayView.findViewById(R.id.title3);
            TextView title4 = (TextView)stayView.findViewById(R.id.title4);
            TextView title5 = (TextView)stayView.findViewById(R.id.title5);
            TextView text1 = (TextView)stayView.findViewById(R.id.text1);
            TextView text2 = (TextView)stayView.findViewById(R.id.text2);
            TextView text3 = (TextView)stayView.findViewById(R.id.text3);
            TextView text4 = (TextView)stayView.findViewById(R.id.text4);
            TextView text5 = (TextView)stayView.findViewById(R.id.text5);
            TextView gridTitle1 = (TextView)stayView.findViewById(R.id.grid_title1);
            TextView gridTitle2 = (TextView)stayView.findViewById(R.id.grid_title2);
            GridView gridView1 = (GridView)stayView.findViewById(R.id.grid1);
            GridView gridView2 = (GridView)stayView.findViewById(R.id.grid2);
            ListView imageLst = (ListView)stayView.findViewById(R.id.house_detail_listview);
            layout5.setVisibility(View.GONE);
            gridTitle1.setVisibility(View.GONE);
            gridTitle2.setVisibility(View.GONE);
            gridView1.setVisibility(View.GONE);
            gridView2.setVisibility(View.GONE);
            List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
            for (int k = 0; k < stayList.get(i).getTags().size(); k++)
            {
                flagLayout.addView(getLable(stayList.get(i).getTags().get(k)));
            }
            ImageLoader.getInstance().displayImage(stayList.get(i).getTitleImage(),
                housePhoto,
                AppConfig.options(R.drawable.ic_launcher));
            houseDescription.setText(stayList.get(i).getServDesc());
            if (stayList.get(i).getServType().equals("house"))
            {
                layout5.setVisibility(View.VISIBLE);
                gridTitle1.setVisibility(View.VISIBLE);
                gridTitle2.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.VISIBLE);
                gridView2.setVisibility(View.VISIBLE);
                title1.setText("地址");
                title2.setText("房间类型");
                title3.setText("入住退房时间");
                title4.setText("可住人数");
                title5.setText("卫生间数");
                HouseInfo info = JsonUtil.getHouseInfo(stayList.get(i).getResourceExt());
                
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
                            new CarInfoGridViewAdapter(TripPlanDetailNewActivity.this, facilityRoomsList);
                        gridView1.setAdapter(insideAdapter);
                    }
                    else
                    {
                        List<Facility> facilityOtherList = new ArrayList<Facility>();
                        facilityOtherList.addAll(info.getHouseServs().get(j).getFacilities());
                        CarInfoGridViewAdapter feeAdapter =
                            new CarInfoGridViewAdapter(TripPlanDetailNewActivity.this, facilityOtherList);
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
                title2.setText("房间类型");
                title3.setText("地址");
                title4.setText("入住退房时间");
                HotelInfo info = JsonUtil.getHotelInfo(stayList.get(i).getResourceExt());
                
                text1.setText(info.getStarLevel());
                text2.setText(info.getBedType());
                text3.setText(info.getAddress());
                text4.setText(info.getCheckTime());
                imageInfos.addAll(info.getImages());
            }
            ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(TripPlanDetailNewActivity.this, imageInfos);//详细listadapter风景
            imageLst.setAdapter(adapter);
            stayLayout.addView(stayView);
        }
    }*/
    //************************************查询景点所有信息************************************//
    private void QueryAttractionAllInfo(){

		HttpUtil.get(Urls.product_journey + productId,
				new JsonHttpResponseHandler() {
					@Override
					public void setRequestURI(URI requestURI) {
						// TODO Auto-generated method stub
						super.setRequestURI(requestURI);
						LogUtil.i(TAG, "QueryProductJourney", "requestURI="
								+ requestURI);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						loadingAnimation.stop();
						loginLoading.setVisibility(View.GONE);
						LogUtil.i(TAG, "QueryProductJourney", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryProductJourney", "response="
								+ response.toString());
						
						
						String code = response.optString("code");
						if (!code.equals("00000")) {
							return;
						}
						mJourneyList.clear();
						JSONObject dataJsonObject = response
								.optJSONObject("data");
						if (dataJsonObject == null
								|| dataJsonObject.toString().equals("")
								|| dataJsonObject.toString().equals("null")) {
							return;
						}
						JSONArray journeyJSONList = dataJsonObject
								.optJSONArray("journeys");
						if (journeyJSONList == null) {
							return;
						}
						for (int i = 0; i < journeyJSONList.length(); i++) {
							JSONObject journeyJSON = journeyJSONList
									.optJSONObject(i);
							Journey journey = new Journey();
							journey.setId(journeyJSON.optString("id"));
							journey.setTitle(journeyJSON.optString("title"));
							journey.setDescription(journeyJSON
									.optString("description"));
							JSONArray trafficJSONList = journeyJSON
									.optJSONArray("traffic");
							if (trafficJSONList != null)
								for (int j = 0; j < trafficJSONList.length(); j++) {
									JSONObject serviceJSON = trafficJSONList
											.optJSONObject(j);
									ServResrouce service = new ServResrouce();
									service.setServId(serviceJSON
											.optString("servId"));
									service.setServName(serviceJSON
											.optString("servName"));
									service.setServAlias(serviceJSON
											.optString("servAlias"));
									service.setServType(serviceJSON
											.optString("servType"));
									service.setRank(serviceJSON
											.optString("rank"));
									journey.getTrafficList().add(service);
								}
							JSONArray stayJSONList = journeyJSON
									.optJSONArray("stay");
							if (stayJSONList != null)
								for (int j = 0; j < stayJSONList.length(); j++) {
									JSONObject serviceJSON = stayJSONList
											.optJSONObject(j);
									ServResrouce service = new ServResrouce();
									service.setServId(serviceJSON
											.optString("servId"));
									service.setServName(serviceJSON
											.optString("servName"));
									service.setServAlias(serviceJSON
											.optString("servAlias"));
									service.setServType(serviceJSON
											.optString("servType"));
									service.setRank(serviceJSON
											.optString("rank"));
									journey.getStayList().add(service);
								}
							JSONArray attractionAllInfoJSONList = journeyJSON
									.optJSONArray("attractions");
							if (attractionAllInfoJSONList != null)
								for (int j = 0; j < attractionAllInfoJSONList
										.length(); j++) {
									JSONObject attractionAllInfoJSON = attractionAllInfoJSONList
											.optJSONObject(j);
									AttractionAllInfo attractionAllInfo = new AttractionAllInfo();
									attractionAllInfo.setFee(attractionAllInfoJSON.optString("fee"));
									attractionAllInfo.setId(attractionAllInfoJSON.optString("id"));
									attractionAllInfo.setServName(attractionAllInfoJSON.optString("servName"));
									attractionAllInfo.setRank(attractionAllInfoJSON.optString("rank"));
									attractionAllInfo.setServAlias(attractionAllInfoJSON.optString("servAlias"));
									attractionAllInfo.setTitleImage(attractionAllInfoJSON.optString("titleImage"));
									attractionAllInfo.setFeeType(attractionAllInfoJSON.optString("feeType"));
									attractionAllInfo.setTicket(attractionAllInfoJSON.optString("ticket"));
									attractionAllInfo.setBusiness_hours(attractionAllInfoJSON.optString("business_hours"));
									attractionAllInfo.setServDesc(attractionAllInfoJSON.optString("servDesc"));
									attractionAllInfo.setAvailableTime(attractionAllInfoJSON.optString("availableTime"));
									attractionAllInfo.setServType(attractionAllInfoJSON.optString("servType"));
									attractionAllInfo.setResourceExt(attractionAllInfoJSON.optString("resourceExt"));
									JSONArray tagJSONList=attractionAllInfoJSON.optJSONArray("tags");
									if(tagJSONList!=null&&tagJSONList.length()>0){
										ArrayList<String> tagsList=new ArrayList<String>();
										for(int k=0;k<tagJSONList.length();k++){
											tagsList.add(tagJSONList.optString(k));
										}
										attractionAllInfo.setTags(tagsList);
									}
									JSONArray imageJSONList=attractionAllInfoJSON.optJSONArray("image");
									if(imageJSONList!=null&&imageJSONList.length()>0){
										ArrayList<ImageInfo> imageList=new ArrayList<ImageInfo>();
										
										for(int x=0;z<imageJSONList.length();x++){
											ImageInfo imageInfo=new ImageInfo();
											JSONObject imageJSON=imageJSONList.optJSONObject(x);
											imageInfo.setUrl(imageJSON.optString("url"));
											imageInfo.setDesc(imageJSON.optString("desc"));
											imageInfo.setHeight(imageJSON.optString("height"));
											imageInfo.setWidth(imageJSON.optString("width"));
											//imageList.add(imageInfo);
											attractionAllInfo.getImages().add(imageInfo);
										}
										
									}
									journey.getAttractionAllInfoList().add(attractionAllInfo);
								}
							mJourneyList.add(journey);
						}
						myTripPlanParentAdapter.notifyDataSetChanged();
						int position=getIntent().getIntExtra("position", 5);
						tripDetailListView.setSelection(position);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "QueryProductJourney", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryProductJourney", "responseString="
								+ responseString);
					}
				});
		
    }
    
    
    
    private LinearLayout getLable(String lable)
    {
        LinearLayout lableLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.lable_layout, null);
        TextView lableText = (TextView)lableLayout.findViewById(R.id.lable_text);
        lableText.setText(lable);
        return lableLayout;
    }
}
