package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.CarInfoGridViewAdapter;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ScenicDetailListAdapter;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.CarServCategory;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.Facility;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.bean.TripDetailInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView;
import com.bcinfo.tripaway.view.ScrollView.ProductDetailScrollView.PullListener;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 单产品汽车详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月11日 下午4:44:18
 */
public class CarProductDetailActivity extends BaseActivity implements PersonalScrollViewListener, OnClickListener
{
    private static final String TAG = "ProductDetailNewActivity";
    
    private ProductDetailScrollView mProductScrollView;
    
    private ImageView mProductHeadImg;
    
    private ImageView user_info_blur_bg;
    
    private LinearLayout layout_scenic_info;
    
    /*
     * 产品信息
     */
    // private ProductInfo mProductInfo;
    
    /*
     * 相似产品列表
     */
    private MyListView mSimillarListView;
    
    /*
     * 相似产品列表适配器
     */
    private ProductAdapter mSimillarListAdapter;
    
    /*
     * 相似产品列表数据
     */
    private ArrayList<ProductNewInfo> mSimillarArrayList = new ArrayList<ProductNewInfo>();// 产品集合
    
    private LinearLayout product_info_layout;
    
    private RelativeLayout layout_product_detail_title;
    
    /**
     * 标题
     */
    private TextView product_title;
    
    private LinearLayout layout_operation_button;
    
    private ArrayList<TripDetailInfo> tripDetailInfoList = new ArrayList<TripDetailInfo>();
    
    /**
     * 配置
     */
    private GridView car_equipment_gv;
    
    /**
     * 费用
     */
    private GridView car_fee_gv;
    
    /**
     * 汽车详情
     */
    private ListView car_detail_listview;
    
    /**
     * 产品id
     */
    private String productId;
    
    /**
     * 返回按钮
     */
    private LinearLayout backButton;
    
    /**
     * 服务种类
     */
    private LinearLayout layout_service_list;
    
    /**
     * 产品介绍
     */
    private TextView product_introduce;
    
    private ImageView car_logo1;
    
    private ImageView car_logo2;
    
    private ImageView car_logo3;
    
    private ImageView car_logo4;
    
    private ImageView car_logo5;
    
    private ImageView car_logo6;
    
    private ImageView car_logo7;
    
    /**
     * 作者头像
     */
    private RoundImageView product_author_photo;
    
    /**
     * 作者姓名
     */
    private TextView product_author_name;
    
    /**
     * 作者简介
     */
    private TextView product_author_description;
    
    /**
     * 产品标题
     */
    private TextView detail_product_title;
    
    /**
     * 每天价格
     */
    private TextView product_price;
    
    /**
     * 汽车详情
     */
    private LinearLayout layout_car_info;
    
    /**
     * 汽车描述
     */
    private TextView car_description;
    
    /**
     * 汽车价格
     */
    private TextView car_price;
    
    /**
     * 汽车图片
     */
    private ImageView car_photo;
    
    /**
     * 汽车品牌
     */
    private TextView car_brand;
    
    /**
     * 车牌号
     */
    private TextView car_number;
    
    /**
     * 行驶里程
     */
    private TextView car_kilometer;
    
    /**
     * 购买时间
     */
    private TextView car_buyTime;
    
    /**
     * 汽车座位数
     */
    private TextView car_seatNum;
    
    /**
     * 汽车类型
     */
    private TextView car_type;
    
    /**
     * 后备箱容量
     */
    private TextView car_room;
    
    private List<Facility> facilityCarInsideList;
    
    private List<Facility> facilityCarFeesList;
    
    private List<ImageInfo> imageList;
    
    private CarInfoGridViewAdapter insideAdapter;
    
    private CarInfoGridViewAdapter feeAdapter;
    
    private ScenicDetailListAdapter carImageAdapter;
    
    private LinearLayout layout_user_evaluate;
    
    private RoundImageView evaluate_user_photo;
    
    private TextView evaluate_user_name;
    
    private TextView evaluate_user_date;
    
    private TextView evaluate_user_content;
    
    private TextView evaluate_detail_button;
    
    private ProductNewInfo productInfo;
    
    private LinearLayout scenic_spots_table;
    
    private ShareSelectDialog ShareSelectDialog;
    
    private TextView levelTxt;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        // mProductInfo =
        // (ProductInfo)getIntent().getSerializableExtra("product");
        productId = getIntent().getStringExtra("productId");
        setContentView(R.layout.car_product_detail_activity);
        AppManager.getAppManager().addActivity(this);
        mProductScrollView = (ProductDetailScrollView)findViewById(R.id.product_detail_scroll_view);
        mProductHeadImg = (ImageView)findViewById(R.id.product_head_imageView);
        user_info_blur_bg = (ImageView)findViewById(R.id.user_info_blur_bg);
        layout_scenic_info = (LinearLayout)findViewById(R.id.layout_scenic_info);
        car_equipment_gv = (GridView)findViewById(R.id.car_equipment_gv);
        car_fee_gv = (GridView)findViewById(R.id.car_fee_gv);
        ImageView product_service_button = (ImageView)findViewById(R.id.product_service_button);
        product_service_button.setOnClickListener(this);
        car_detail_listview = (ListView)findViewById(R.id.car_detail_listview);
        mProductScrollView.setImageView(mProductHeadImg);
        mProductScrollView.setScrollListener(this);
        mProductScrollView.setPullListener(mPullListener);
        levelTxt = (TextView)findViewById(R.id.level);
        ImageView product_service_share_button = (ImageView)findViewById(R.id.product_service_share_button);
        product_service_share_button.setOnClickListener(this);
        int space = getResources().getDimensionPixelSize(R.dimen.detail_photo_bottom_space);
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, screenHeight - space - statusBarHeight);
        mProductHeadImg.setLayoutParams(p);
        mProductHeadImg.setOnClickListener(this);
        initProductView();
        initCarInfoView();
        initSimillarProductListView();
        initUserComment();
        initBottomButton();
        
        QueryProductDetail(productId);
        // QueryProductComment(1, productId);
        QuerySimillarProduct(1, productId);
    }
    
    private void initProductView()
    {
        product_info_layout = (LinearLayout)findViewById(R.id.product_info_layout);
        layout_product_detail_title = (RelativeLayout)findViewById(R.id.layout_product_detail_title);
        layout_operation_button = (LinearLayout)findViewById(R.id.layout_operation_button);
        backButton = (LinearLayout)findViewById(R.id.layout_back_button);
        product_title = (TextView)findViewById(R.id.product_title);
        layout_service_list = (LinearLayout)findViewById(R.id.layout_service_list);
        product_introduce = (TextView)findViewById(R.id.product_introduce);
        product_introduce.setAlpha(0);
        layout_service_list.setAlpha(0);
        product_author_photo = (RoundImageView)findViewById(R.id.product_author_photo);
        product_author_name = (TextView)findViewById(R.id.product_author_name);
        product_author_description = (TextView)findViewById(R.id.product_description);
        detail_product_title = (TextView)findViewById(R.id.detail_product_title);
        product_price = (TextView)findViewById(R.id.product_price);
        layout_product_detail_title.getBackground().setAlpha(0);
        product_title.setAlpha(0);
        backButton.setOnClickListener(this);
        product_author_photo.setOnClickListener(this);
        layout_product_detail_title.setFocusable(true);
        layout_product_detail_title.setFocusableInTouchMode(true);
    }
    
    private void initProductInfo(ProductNewInfo productInfo)
    {
        UserInfo author = productInfo.getUser();
        product_title.setText(productInfo.getTitle());
        if (!StringUtils.isEmpty(author.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + author.getAvatar(),
                product_author_photo,
                AppConfig.options(R.drawable.user_defult_photo)); // 用户默认的头像
        }
        
        product_author_name.setText(author.getNickname());
        product_author_description.setText(author.getIntroduction());
        detail_product_title.setText(productInfo.getTitle());
        product_introduce.setText(productInfo.getDescription());
        product_price.setText("￥" + productInfo.getPrice() + "/天");
        if (productInfo.getLevel().equals("super"))
        {
            levelTxt.setText("非常严格");
        }
        else if (productInfo.getLevel().equals("hight"))
        {
            levelTxt.setText("严格");
        }
        else if (productInfo.getLevel().equals("middle"))
        {
            levelTxt.setText("适中");
        }
        else if (productInfo.getLevel().equals("low"))
        {
            levelTxt.setText("灵活");
        }
        else
        {
            levelTxt.setText("没有c");
        }
        if (!StringUtils.isEmpty(productInfo.getTitleImg()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + productInfo.getTitleImg(), mProductHeadImg);
        }
        
        String[] services = productInfo.getServices().split(",");
        for (int i = 0; i < services.length; i++)
        {
            ImageView serviceImg = new ImageView(this);
            int imgSize = getResources().getDimensionPixelSize(R.dimen.product_service_img_size);
            LayoutParams parm = new LayoutParams(imgSize, imgSize);
            parm.leftMargin = 10;
            parm.rightMargin = 10;
            serviceImg.setLayoutParams(parm);
            String serviceStr = services[i];
            if (serviceStr.equals("attractions"))
            {
                serviceImg.setImageResource(R.drawable.service_trip);
            }
            else if (serviceStr.equals("stay"))
            {
                serviceImg.setImageResource(R.drawable.service_house);
            }
            else if (serviceStr.equals("traffic"))
            {
                serviceImg.setImageResource(R.drawable.service_car);
            }
            // serviceImg.setImageResource(R.drawable.service_order_ticket);
            layout_service_list.addView(serviceImg);
        }
        /******************* 产品简要信息 ********************/
        LinearLayout servicesLine =
            (LinearLayout)LayoutInflater.from(this).inflate(R.layout.product_detail_services_h, null);
        // servicesLine.addView(getServiceItem(productInfo.getPoiCount() + "景点",
        // R.drawable.flag_scenery));
        // servicesLine.addView(getServiceItem(productInfo.getDistance() + "KM",
        // R.drawable.icon_create_trip));
        servicesLine.addView(getServiceItem(productInfo.getDays() + "天", R.drawable.flag_calendar));
        layout_scenic_info.addView(servicesLine);
    }
    
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
    
    /**
     * 初始化相似产品列表
     */
    private void initSimillarProductListView()
    {
        mSimillarListView = (MyListView)findViewById(R.id.product_detail_similar_listview);
        mSimillarListAdapter = new ProductAdapter(this, mSimillarArrayList);
        mSimillarListView.setAdapter(mSimillarListAdapter);
        mSimillarListView.setOnItemClickListener(mSimillarListListener);
    }
    
    /**
     * 初始化用户评论
     */
    private void initUserComment()
    {
        layout_user_evaluate = (LinearLayout)findViewById(R.id.layout_user_evaluate);
        evaluate_user_photo = (RoundImageView)findViewById(R.id.evaluate_user_photo);
        evaluate_user_name = (TextView)findViewById(R.id.evaluate_user_name);
        evaluate_user_date = (TextView)findViewById(R.id.evaluate_user_date);
        evaluate_user_content = (TextView)findViewById(R.id.evaluate_user_content);
        evaluate_detail_button = (TextView)findViewById(R.id.product_evaluate_detail_button);
        evaluate_detail_button.setOnClickListener(CarProductDetailActivity.this);
        layout_user_evaluate.setVisibility(View.GONE);
    }
    
    /**
     * 初始化底部按钮
     */
    private void initBottomButton()
    {
        LinearLayout applyProductButton = (LinearLayout)findViewById(R.id.layout_apply_product_button);
        LinearLayout layout_contact_author_button = (LinearLayout)findViewById(R.id.layout_contact_author_button);
        applyProductButton.setOnClickListener(this);
        layout_contact_author_button.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.product_head_imageView:
                if (productTitleImgList.size() > 0)
                {
                    Intent intentForView = new Intent(CarProductDetailActivity.this, ImageViewerActivity.class);
                    intentForView.putExtra("image_index", 0);
                    intentForView.putParcelableArrayListExtra("image_urls", productTitleImgList);
                    startActivity(intentForView);
                }
                break;
            case R.id.product_service_button:
                if (productInfo == null)
                {
                    return;
                }
                intent = new Intent(this, ProductServiceActivity.class);
                intent.putExtra("bgUrl", productInfo.getTitleImg());
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.layout_back_button:
                if (isFromLoad)
                {
                    Intent intent1 = new Intent(CarProductDetailActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
                finish();
                activityAnimationClose();
                break;
            case R.id.product_evaluate_detail_button:
                intent = new Intent(this, UserCommentActivity.class);
                intent.putExtra("productInfo", productInfo);
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.product_service_share_button:
                ShareSelectDialog =
                    new ShareSelectDialog(CarProductDetailActivity.this, productInfo.getTitleImg(),
                        productInfo.getDescription(), productInfo.getTitle(), productInfo.getId(),Urls.ShareUrlOfProduct,productInfo.getTitle());
                ShareSelectDialog.show();
                break;
            case R.id.layout_apply_product_button:
                // intent = new Intent(this, ApplyProductActivity.class);
                if (!AppInfo.getIsLogin())
                {
                    goLoginActivity();
                    return;
                }
                if (productInfo == null)
                {
                    return;
                }
                intent = new Intent(this, ConfirmPayActivity3.class);
                intent.putExtra("productInfo", productInfo);
                startActivity(intent);
                activityAnimationOpen();
                break;
            case R.id.layout_contact_author_button:
                if (!AppInfo.getIsLogin())
                {
                    goLoginActivity();
                    return;
                }
                Intent talkIntent = new Intent(CarProductDetailActivity.this, ChatActivity.class);
                talkIntent.putExtra("receiveId", productInfo.getUser().getUserId());
                talkIntent.putExtra("title", productInfo.getUser().getNickname());
                startActivity(talkIntent);
                activityAnimationOpen();
                break;
            case R.id.product_author_photo:
                // ServicerPersonInfoDialog dialog = new
                // ServicerPersonInfoDialog(this);
                // dialog.show();
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        
        UMSsoHandler ssoHandler = ShareSelectDialog.mController.getConfig().getSsoHandler(resultCode);
        if (ssoHandler != null)
        {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY)
    {
        // TODO Auto-generated method stub
        if (view != null && view == mProductScrollView)
        {
            int alpha = positionY / 2;
            if (positionY < 50 || positionY == 50)
            {
                detail_product_title.setAlpha(1f);
            }
            else
            {
                detail_product_title.setAlpha(0);
            }
            if (positionY > 500)
            {
                alpha = 255;
            }
            layout_product_detail_title.getBackground().setAlpha(alpha);
            product_title.setAlpha(alpha / 255f);
        }
        else
        {
            return;
        }
    }
    
    OnItemClickListener mSimillarListListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // TODO Auto-generated method stub
            Intent intent = null;
            ProductNewInfo productNewInfo = mSimillarArrayList.get(position);
            LogUtil.i(TAG, "onItemClick", "position=" + position);
            if (productNewInfo.getProductType().equals("single"))
            {
                if (productNewInfo.getServices().equals("traffic"))
                {
                    intent = new Intent(CarProductDetailActivity.this, CarProductDetailActivity.class);
                    intent.putExtra("productId", productNewInfo.getId());
                }
                else if (productNewInfo.getServices().equals("stay"))
                {
                    intent = new Intent(CarProductDetailActivity.this, HouseProductDetailActivity.class);
                    intent.putExtra("productId", productNewInfo.getId());
                }
                else
                {
                    intent = new Intent(CarProductDetailActivity.this, GrouponProductNewDetailActivity.class);
                    intent.putExtra("productId", productNewInfo.getId());
                }
            }
            else if (productNewInfo.getProductType().equals("base")
                || productNewInfo.getProductType().equals("customization"))
            {
                intent = new Intent(CarProductDetailActivity.this, GrouponProductNewDetailActivity.class);
                intent.putExtra("productId", productNewInfo.getId());
            }
            else if (productNewInfo.getProductType().equals("team"))
            {
                intent = new Intent(CarProductDetailActivity.this, GrouponProductNewDetailActivity.class);
                intent.putExtra("productId", productNewInfo.getId());
            }
            startActivity(intent);
            activityAnimationOpen();
        }
    };
    
    PullListener mPullListener = new PullListener()
    {
        @Override
        public void onPull(int height)
        {
            // TODO Auto-generated method stub
            LogUtil.i(TAG, "mPullListener", "height=" + height);
            float alpha = height * 3 - 200;
            if (alpha > 255)
            {
                alpha = 255;
            }
            if (alpha < 50)
            {
                alpha = 0;
            }
            layout_service_list.setAlpha(alpha / 255f);
            product_introduce.setAlpha(alpha / 255f);
        }
    };
    
    private void initCarInfoView()
    {
        layout_car_info = (LinearLayout)findViewById(R.id.layout_car_info);
        car_photo = (ImageView)findViewById(R.id.car_photo);
        car_description = (TextView)findViewById(R.id.car_description);
        car_price = (TextView)findViewById(R.id.car_price);
        car_brand = (TextView)findViewById(R.id.car_brand);
        car_number = (TextView)findViewById(R.id.car_number);
        car_kilometer = (TextView)findViewById(R.id.car_kilometer);
        car_buyTime = (TextView)findViewById(R.id.car_buyTime);
        
        car_seatNum = (TextView)findViewById(R.id.car_seatNum);
        car_type = (TextView)findViewById(R.id.car_type);
        car_room = (TextView)findViewById(R.id.car_room);
        
        // 新增汽车图片liyuru
        
        car_logo1 = (ImageView)findViewById(R.id.car_logo1);
        car_logo2 = (ImageView)findViewById(R.id.car_logo2);
        car_logo3 = (ImageView)findViewById(R.id.car_logo3);
        car_logo4 = (ImageView)findViewById(R.id.car_logo4);
        car_logo5 = (ImageView)findViewById(R.id.car_logo5);
        car_logo6 = (ImageView)findViewById(R.id.car_logo6);
        
        // car_logo1.setImageResource(R.drawable.price);
        // car_logo2.setImageResource(R.drawable.pinpai);
        
        // car_logo3.setImageResource(R.drawable.licheng);
        // car_logo4.setImageResource(R.drawable.leibie);
        // car_logo5.setImageResource(R.drawable.zuowei);
        // car_logo6.setImageResource(R.drawable.leibie);
        
        initEquipmentGridView();
        initFeeGridView();
        initCarPictureListView();
    }
    
    private void initEquipmentGridView()
    {
        facilityCarInsideList = new ArrayList<Facility>();
        insideAdapter = new CarInfoGridViewAdapter(this, facilityCarInsideList);
        car_equipment_gv.setAdapter(insideAdapter);
    }
    
    private void initFeeGridView()
    {
        facilityCarFeesList = new ArrayList<Facility>();
        feeAdapter = new CarInfoGridViewAdapter(this, facilityCarFeesList);
        car_fee_gv.setAdapter(feeAdapter);
    }
    
    private void initCarPictureListView()
    {
        imageList = new ArrayList<ImageInfo>();
        carImageAdapter = new ScenicDetailListAdapter(this, imageList);
        car_detail_listview.setAdapter(carImageAdapter);
    }
    
    // 汽车详情 liyuru 修改于 2015年 8月12
    private void initCarInfo(ProductServiceResource resource)
    {
        if (!StringUtils.isEmpty(resource.getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(),
                car_photo,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
        // 李玉茹添加
        // ImageLoader.getInstance().displayImage(resource.getTitleImage(),
        // car_logo1, AppConfig.options(R.drawable.price));
        
        car_description.setText(resource.getServDesc());
        CarExt carInfo = JsonUtil.getCarInfo(resource.getResourceExt());
        if (StringUtils.isEmpty(carInfo.getCarName()))
        {
            car_brand.setText("—");
        }
        else
        {
            car_brand.setText(carInfo.getCarName());
        }
        if (StringUtils.isEmpty(carInfo.getCarPlace()))
        {
            car_number.setText("—");
        }
        else
        {
            car_number.setText(carInfo.getCarPlace());
        }
        if (StringUtils.isEmpty(carInfo.getDistance()))
        {
            car_kilometer.setText("—");
            
        }
        else
        {
            car_kilometer.setText(carInfo.getDistance());
        }
        if (StringUtils.isEmpty(carInfo.getShoppingTime()))
        {
            car_buyTime.setText("—");
        }
        else
        {
            car_buyTime.setText(carInfo.getShoppingTime());
        }
        if (StringUtils.isEmpty(carInfo.getSeatNum()))
        {
            car_seatNum.setText("—");
        }
        else
        {
            car_seatNum.setText(carInfo.getSeatNum());
        }
        if (StringUtils.isEmpty(carInfo.getCarType()))
        {
            car_type.setText("—");
        }
        else
        {
            car_type.setText(carInfo.getCarType());
        }
        if (StringUtils.isEmpty(carInfo.getCapacity()))
        {
            car_room.setText("—");
        }
        else
        {
            // car_room.setText(carInfo.getCapacity()+"-");
            car_room.setText(carInfo.getCapacity());
        }
        
        List<CarServCategory> categoryList = carInfo.getCarServers();
        for (int i = 0; i < categoryList.size(); i++)
        {
            if (categoryList.get(i).getCateCode().equals("facility_car_inside"))
            {
                facilityCarInsideList.addAll(categoryList.get(i).getFacilities());
            }
            else
            {
                facilityCarFeesList.addAll(categoryList.get(i).getFacilities());
            }
        }
        for (int i = 0; i < carInfo.getCarImages().size(); i++)
        {
            imageList.addAll(carInfo.getCarImages().get(i).getCarImageList());
        }
        insideAdapter.notifyDataSetChanged();
        feeAdapter.notifyDataSetChanged();
        carImageAdapter.notifyDataSetChanged();
    }
    
    private void QueryProductDetail(String productId)
    {
        // LogUtil.e(TAG, "QueryProductDetail", Urls.product_detail +
        // productId);
        HttpUtil.get(Urls.product_detail + productId, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // LogUtil.i(TAG, "QueryProductDetail", "statusCode=" +
                // statusCode);
                LogUtil.i(TAG, "QueryProductDetail", "response=" + response.toString());
                String code = response.optString("code");
                if (!code.equals("00000"))
                {
                    return;
                }
                String msg = response.optString("msg");
                JSONObject dataJson = response.optJSONObject("data");
                JSONObject resourceJson = dataJson.optJSONObject("resoure");
                ProductServiceResource resource = new ProductServiceResource();
                if (resourceJson != null && !resourceJson.equals(""))
                {
                    resource.setFee(resourceJson.optString("fee"));
                    JSONArray tagsArray = resourceJson.optJSONArray("tags");
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
                    resource.setId(resourceJson.optString("id"));
                    resource.setServName(resourceJson.optString("servName"));
                    resource.setRank(resourceJson.optString("rank"));
                    resource.setServAlias(resourceJson.optString("servAlias"));
                    resource.setTitleImage(resourceJson.optString("titleImage"));
                    resource.setFeeType(resourceJson.optString("feeType"));
                    resource.setResourceExt(resourceJson.optString("resourceExt"));
                    resource.setServDesc(resourceJson.optString("servDesc"));
                    JSONArray availableTimeJsonArray = resourceJson.optJSONArray("availableTime");
                    if (availableTimeJsonArray != null && availableTimeJsonArray.length() > 0)
                    {
                        List<AvailableTime> availableTimes = new ArrayList<AvailableTime>();
                        for (int k = 0; k < availableTimeJsonArray.length(); k++)
                        {
                            JSONObject availableTimeJsonObject;
                            try
                            {
                                availableTimeJsonObject = availableTimeJsonArray.getJSONObject(k);
                                AvailableTime availableTime = new AvailableTime();
                                availableTime.setBeginDate(availableTimeJsonObject.optString("beginDate"));
                                availableTime.setEndDate(availableTimeJsonObject.optString("endDate"));
                                availableTimes.add(availableTime);
                            }
                            catch (JSONException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        resource.setAvailableTimes(availableTimes);
                    }
                    resource.setServType(resourceJson.optString("servType"));
                }
                
                JSONObject product = dataJson.optJSONObject("product");
                productInfo = new ProductNewInfo();
                if (product != null && !product.equals(""))
                {
                    JSONArray topicJsonArray = product.optJSONArray("topics");
                    if (topicJsonArray != null && !topicJsonArray.equals(""))
                    {
                        ArrayList<String> topics = new ArrayList<String>();
                        for (int j = 0; j < topicJsonArray.length(); j++)
                        {
                            // LogUtil.i(TAG, "topicJsonArray",
                            // topicJsonArray.optString(j));
                            topics.add(topicJsonArray.optString(j));
                        }
                        productInfo.setTopics(topics);
                    }
                    productInfo.setExpStart(product.optString("expStart"));
                    productInfo.setExpend(product.optString("expend"));
                    JSONObject userJSON = product.optJSONObject("creater");
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
                        userInfo.setUserType(userJSON.optString("userType"));
                        userInfo.setMobile(userJSON.optString("mobile"));
                        JSONArray tagsJsonArray = userJSON.optJSONArray("tags");
                        if (tagsJsonArray != null && tagsJsonArray.length() > 0)
                        {
                            ArrayList<String> tags = new ArrayList<String>();
                            for (int j = 0; j < tagsJsonArray.length(); j++)
                            {
                                // LogUtil.i("ThemeProductListActivity",
                                // "tagsJsonArray",
                                // tagsJsonArray.optString(j));
                                tags.add(tagsJsonArray.optString(j));
                            }
                            userInfo.setTags(tags);
                        }
                        productInfo.setUser(userInfo);
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
                    productInfo.setServices(product.optString("serviceCodes"));
                    try
                    {
                        productInfo.setLevel(product.optJSONObject("policy").getString("type"));
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                // JSONArray imageArray =
                // dataJson.optJSONArray("images");
                // if (imageArray != null && imageArray.length() > 0)
                // {
                // List<ImageInfo> imageInfos = new
                // ArrayList<ImageInfo>();
                // for (int i = 0; i < imageArray.length(); i++)
                // {
                // JSONObject imageInfoJson =
                // imageArray.optJSONObject(i);
                // ImageInfo imageInfo = new ImageInfo();
                // imageInfo.setUrl(imageInfoJson.optString("url"));
                // imageInfo.setDesc(imageInfoJson.optString("desc"));
                // imageList.add(imageInfo);
                // }
                // }
                // JSONArray categoryArray =
                // dataJson.optJSONArray("category");
                // List<CarServCategory> categoryList = new
                // ArrayList<CarServCategory>();
                // if (categoryArray != null && categoryArray.length() >
                // 0)
                // {
                // for (int i = 0; i < categoryArray.length(); i++)
                // {
                // JSONObject categoryJson =
                // categoryArray.optJSONObject(i);
                // CarServCategory carServCategory = new
                // CarServCategory();
                // carServCategory.setCateCode(categoryJson.optString("cateCode"));
                // JSONArray facilityArray =
                // categoryJson.optJSONArray("facility");
                // if (facilityArray != null && facilityArray.length() >
                // 0)
                // {
                // List<Facility> facilities = new
                // ArrayList<Facility>();
                // for (int j = 0; j < facilityArray.length(); j++)
                // {
                // JSONObject facilityJson =
                // facilityArray.optJSONObject(j);
                // Facility facility = new Facility();
                // facility.setId(facilityJson.optString("id"));
                // facility.setFacilityCode(facilityJson.optString("facilityCode"));
                // facility.setFacilityName(facilityJson.optString("facilityName"));
                // facility.setStatus(facilityJson.optString("status"));
                // facilities.add(facility);
                // }
                // carServCategory.setFacilities(facilities);
                // }
                // categoryList.add(carServCategory);
                // }
                // }
                initProductInfo(productInfo);
                initCarInfo(resource);
                // initProductInfo(productInfo);
                // initScenicSpots(productInfo);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                LogUtil.i(TAG, "QueryProductDetail", "responseString=" + errorResponse);
            }
        });
    }
    
    private void QueryProductComment(int index, String id)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", "" + index);
        params.put("pagesize", "10");
        params.put("productId", id);
        HttpUtil.get(Urls.product_detail_comment, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                if (!response.optString("code").equals("00000"))
                {
                    return;
                }
                // 用户评论
                // LinearLayout layout_user_evaluate =
                // (LinearLayout)findViewById(R.id.layout_user_evaluate);
                // RoundImageView evaluate_user_photo =
                // (RoundImageView)findViewById(R.id.evaluate_user_photo);
                // TextView evaluate_user_name =
                // (TextView)findViewById(R.id.evaluate_user_name);
                // TextView evaluate_user_date =
                // (TextView)findViewById(R.id.evaluate_user_date);
                // TextView evaluate_user_content =
                // (TextView)findViewById(R.id.evaluate_user_content);
                // TextView evaluate_detail_button =
                // (TextView)findViewById(R.id.product_evaluate_detail_button);
                // evaluate_detail_button.setOnClickListener(CarProductDetailActivity.this);
                /***************************************************************/
                System.out.println("获取产品评论接口=" + response);
                // LogUtil.i(TAG, "QueryProductComment", "statusCode=" +
                // statusCode);
                // LogUtil.i(TAG, "QueryProductComment", "response=" +
                // response.toString());
                JSONObject dataJSON = response.optJSONObject("data");
                if (dataJSON != null && !dataJSON.equals("") && dataJSON.length() > 0)
                {
                    layout_user_evaluate.setVisibility(View.VISIBLE);
                    String totalComment = dataJSON.optString("commentsCount");// 评论数
                    String descScore = dataJSON.optString("descScore");// 产品描述相符评分
                    String serviceScore = dataJSON.optString("servScore");// 产品服务评分
                    String averScore = dataJSON.optString("avgScore");// 产品整体评分
                    JSONArray commentArray = dataJSON.optJSONArray("comments");
                    List<Comments> comments = new ArrayList<Comments>();
                    if (commentArray != null && commentArray.length() > 0)
                    {
                        for (int i = 0; i < commentArray.length(); i++)
                        {
                            JSONObject commentJsonObject = commentArray.optJSONObject(i);
                            Comments comment = new Comments();
                            comment.setAverScore(commentJsonObject.optString("avgScore"));
                            comment.setId(commentJsonObject.optString("id"));
                            comment.setCreateTime(commentJsonObject.optString("createTime"));
                            comment.setContent(commentJsonObject.optString("content"));
                            JSONObject userJsonObject = commentJsonObject.optJSONObject("sender");
                            if (userJsonObject != null && !userJsonObject.equals(""))
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
                                comment.setUser(userInfo);
                            }
                            comments.add(comment);
                        }
                    }
                    if (comments.size() > 0)
                    {
                        evaluate_user_content.setText(comments.get(0).getContent());
                        evaluate_user_date.setText(DateUtil.getFormateDate(comments.get(0).getCreateTime()));
                        evaluate_user_name.setText(comments.get(0).getUser().getNickname());
                        if (!StringUtils.isEmpty(comments.get(0).getUser().getAvatar()))
                        {
                            ImageLoader.getInstance()
                                .displayImage(Urls.imgHost + comments.get(0).getUser().getAvatar(), evaluate_user_photo);
                        }
                        
                    }
                    evaluate_detail_button.setText("查看全部评论" + "(" + totalComment + ")");
                    
                }
                else
                {
                    layout_user_evaluate.setVisibility(View.GONE);
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                LogUtil.i(TAG, "QueryProductComment", "responseString=" + errorResponse);
            }
        });
    }
    
    private void QuerySimillarProduct(int index, String id)
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", "" + index);
        params.put("pagesize", "10");
        params.put("productId", id);
        HttpUtil.get(Urls.simillar_product, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                LogUtil.i(TAG, "QuerySimillarProduct", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QuerySimillarProduct", "response=" + response.toString());
                JSONObject dataJSON = response.optJSONObject("data");
                ArrayList<ProductNewInfo> proList = new ArrayList<ProductNewInfo>();
                if (dataJSON != null)
                {
                    JSONArray productsJsonList = dataJSON.optJSONArray("products");
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
                        productNewInfo.setServices(productJson.optString("serviceCodes"));
                        productNewInfo.setQuickPayment(productJson.optString("quickPayment"));
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
                        mSimillarArrayList.add(productNewInfo);
                    }
                    mSimillarListAdapter.notifyDataSetChanged();
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                LogUtil.i(TAG, "QuerySimillarProduct", "statusCode=" + statusCode);
                LogUtil.i(TAG, "QuerySimillarProduct", "responseString=" + errorResponse);
            }
        });
    }
}
