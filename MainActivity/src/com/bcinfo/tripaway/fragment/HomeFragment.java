package com.bcinfo.tripaway.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.AllLocationActivity;
import com.bcinfo.tripaway.activity.AllPersonActivity;
import com.bcinfo.tripaway.activity.AllThemesActivity;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.LocationCountryDetailActivity;
import com.bcinfo.tripaway.activity.MainActivity.PickInterface;
import com.bcinfo.tripaway.activity.PersonalInfoNewActivity;
import com.bcinfo.tripaway.activity.SubjectDetailActivity;
import com.bcinfo.tripaway.activity.ThemeProductListActivity;
import com.bcinfo.tripaway.adapter.MyAdsPagerAdapter;
import com.bcinfo.tripaway.adapter.NewPickListAdapter.OperationListener;
import com.bcinfo.tripaway.adapter.NewProductPushThemesGridAdapter.ImageShowListener;
import com.bcinfo.tripaway.adapter.SubjectGridAdapter;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.NewSinaRefreshView;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragment implements OnItemClickListener, OnClickListener, ImageShowListener {

    private View login_loading;
    private AnimationDrawable loadingAnimation;

    LinearLayout pickcarouselview;
    /**
     * android-support-v4中的滑动组件
     */
    private ViewPager viewPager;
    /**
     * 点的父布局
     */
    private LinearLayout dotsLayout;
    /**
     * 滑动的图片集合
     */
    private List<View> imageViews;
    /**
     * 图片标题正文的那些点
     */
    private List<View> dots;
    /**
     * 图片数量
     */
    private int count;
    /**
     * 当前图片的索引号
     */
    private int currentItem = 0;
    /**
     * 本周推荐
     */
    List<PushResource> pushResourceList;
    /**
     * 热门主题显示的GridView
     */
    private LinearLayout mThemesGridView;

    // private NewProductPushThemesGridAdapter pThemesAdapter;
    /**
     * 推荐产品主题的集合
     */
    private List<PushResource> topicsList = new ArrayList<PushResource>();
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;

    LinearLayout image1Layout;
    LinearLayout image2Layout;
    LinearLayout image3Layout;

    TextView more_title;
    TextView more_location_title;
    TextView more_pick;
    TextView find_more;
    TextView past_title;

    /**
     * 产品目的地的集合
     */
    private List<PushResource> locationsList = new ArrayList<PushResource>();

    // private ProductPushDestinationsGridAdapter pLocationsAdapter;
    /**
     * 热门目的地显示的GridView
     */
    private LinearLayout mLocationsGridView;
    /**
     * 显示 精选结果数据的listview
     */
    private LinearLayout pickedListView;

    /**
     * 定义 存放 精选数据的list集合
     */
    private List<Map<String, Object>> pickedItemList = new ArrayList<Map<String, Object>>();

    LinearLayout grid;
    private SubjectGridAdapter mOrgGridViewAdapter;
    private ArrayList<PushResource> orgList = new ArrayList<PushResource>();
    private ArrayList<PushResource> orgListUser = new ArrayList<PushResource>();
    LinearLayout linearlayout1;
    LinearLayout linearlayout2;
    LinearLayout linearlayout3;
    LinearLayout linearlayout4;
    LinearLayout linearlayout5;
    private ScrollView mainScorll;

    int themeOrDestinationImageW;

    private JSONObject pushCarouselJsonObject;
    private JSONObject findWeekJsonObject;
    private JSONObject pickTitleJsonObject;
    private JSONObject locationJsonObject;
    private JSONObject pickedJsonObject;
    private JSONObject orgJsonObject;
    private TwinklingRefreshLayout refreshLayout;
    private boolean isRefresh = false;
    private NewSinaRefreshView headerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
//        login_loading = view.findViewById(R.id.login_loading);
//        loadingAnimation = (AnimationDrawable) login_loading.getBackground();
//        loadingAnimation.start();
        initView(view);
        pickcarouselview = (LinearLayout) view.findViewById(R.id.pickcarouselview);
        mainScorll = (ScrollView) view.findViewById(R.id.mainScorll);
        themeOrDestinationImageW = (screenWidth - DensityUtil.dip2px(getActivity(), 14)) / 3;
        // 轮播图
//        getPushCarousel();

        mainScorll.setVisibility(View.GONE);
        statisticsTitle = "首页";
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.startRefresh();
        ProgressLayout header = new ProgressLayout(getActivity());
        refreshLayout.setHeaderView(header);
        refreshLayout.setFloatRefresh(true);
        refreshLayout.setOverScrollRefreshShow(false);
        refreshLayout.setHeaderHeight(140);
        refreshLayout.setWaveHeight(240);
        refreshLayout.setOverScrollHeight(200);
        refreshLayout.setPureScrollModeOn(false);
        refreshLayout.setAutoLoadMore(true);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isRefresh = true;
                        if (isRefresh) {
                            topicsList.clear();
                            mThemesGridView.removeAllViews();
                            grid.removeAllViews();
                            orgListUser.clear();
                            orgList.clear();
                            mLocationsGridView.removeAllViews();
                            locationsList.clear();
                            pickedItemList.clear();
                            pickedListView.removeAllViews();
//                            mainScorll.setVisibility(View.GONE);
                        }

                        getPushCarousel();
                    }
                }, 2000);
            }
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {

            }
        });
        return view;
    }

    private void initView(View view) {
        linearlayout1 = (LinearLayout) view.findViewById(R.id.linearlayout1);
        linearlayout2 = (LinearLayout) view.findViewById(R.id.linearlayout2);
        linearlayout3 = (LinearLayout) view.findViewById(R.id.linearlayout3);
        linearlayout4 = (LinearLayout) view.findViewById(R.id.linearlayout4);
        linearlayout5 = (LinearLayout) view.findViewById(R.id.linearlayout5);

        linearlayout1.setOnClickListener(this);
        linearlayout2.setOnClickListener(this);
        linearlayout3.setOnClickListener(this);
        linearlayout4.setOnClickListener(this);
        linearlayout5.setOnClickListener(this);

        image1 = (ImageView) view.findViewById(R.id.image1);
        image2 = (ImageView) view.findViewById(R.id.image2);
        image3 = (ImageView) view.findViewById(R.id.image3);
        image4 = (ImageView) view.findViewById(R.id.image4);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);

        image1Layout = (LinearLayout) view.findViewById(R.id.image1Layout);
        image2Layout = (LinearLayout) view.findViewById(R.id.image2Layout);
        image3Layout = (LinearLayout) view.findViewById(R.id.image3Layout);

        more_title = (TextView) view.findViewById(R.id.more_title);
        more_title.setOnClickListener(this);
        more_location_title = (TextView) view.findViewById(R.id.more_location_title);
        more_location_title.setOnClickListener(this);
        more_pick = (TextView) view.findViewById(R.id.more_pick);
        more_pick.setOnClickListener(this);
        find_more = (TextView) view.findViewById(R.id.find_more);
        find_more.setOnClickListener(this);
        past_title = (TextView) view.findViewById(R.id.past_title);
        past_title.setOnClickListener(this);

        mThemesGridView = (LinearLayout) view.findViewById(R.id.discovery_themes_gridView);
        mLocationsGridView = (LinearLayout) view.findViewById(R.id.discovery_locations_gridView);

        grid = (LinearLayout) view.findViewById(R.id.grid_ll);

        pickedListView = (LinearLayout) view.findViewById(R.id.main_picked_listview);

    }

    private OperationListener listener = new OperationListener() {
        @Override
        public void addOrCancelStored(String productId, boolean flag, int position) {
            if (!AppInfo.getIsLogin()) {
                ToastUtil.showToast(getActivity(), "未登录不能收藏");
                return;
            }
            if (pickedItemList != null && pickedItemList.size() > 0) {
                if (flag) {
                    ((ProductNewInfo) pickedItemList.get(position).get("objectValue")).setIsFav("no");
                } else {
                    ((ProductNewInfo) pickedItemList.get(position).get("objectValue")).setIsFav("yes");
                }
            }
            storeProductByIsFaved(flag, productId);

        }
    };

    private void storeProductByIsFaved(boolean flag, String productId) {

        if (!flag) {
            JSONObject object = new JSONObject();
            try {
                object.put("objectId", productId);
                object.put("type", "product");
                StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
                // 添加收藏
                HttpUtil.post(Urls.add_store, entity, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        String code = response.optString("code");
                        if (!"00000".equals(code)) {
                            return;
                        }
                        // mStorePicIv.setImageResource(R.drawable.stored_pics);
                    }
                });
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            RequestParams params = new RequestParams();
            params.put("type", "product");
            params.put("objectId", productId);
            // 删除收藏
            HttpUtil.delete(Urls.cancel_store, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    String code = response.optString("code");
                    if (!"00000".equals(code)) {
                        // Intent intent = new
                        // Intent(GrouponProductNewDetailActivity.this,
                        // LoginActivity.class);
                        // startActivity(intent);
                        return;
                    }
                    // mStorePicIv.setImageResource(R.drawable.store_pics);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    private void getPushCarousel() {
        HttpUtil.get(Urls.push_pics_url, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i("PickedFragment", "getPushCarousel", throwable.getMessage());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                LogUtil.i("PickedFragment", "getPushCarousel", throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("PickedFragment", "精选轮播图接口=", response.toString());
                String code = response.optString("code");
                // 本周推荐
                getFindWeek();
                if (code.equals("00000")) {
                    pushCarouselJsonObject = response;

                }
            }
        });
    }

    private void addCarousel(List<PushResource> pushResourceList) {
        imageViews = new ArrayList<View>();
        dots = new ArrayList<View>();
        if (dotsLayout != null) {
            dotsLayout.removeAllViews();
        }
        count = pushResourceList.size();
        for (int i = 0; i < count; i++) {
            PushResource resource = pushResourceList.get(i);
            // 初始化图片资源
            ImageView imageView = new ImageView(getActivity());
            int h = DensityUtil.dip2px(getActivity(), 180);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            // WindowManager wm =
            // (WindowManager)getActivity().getSystemService(WINDOW_SERVICE);
            // int width = wm.getDefaultDisplay().getWidth();
            if (!StringUtils.isEmpty(resource.getTitleImage())) {
                // TODO 如果主题有的话就放在oncreat里面
                // addHeaderView();
                ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(), imageView,
                        AppConfig.options(R.drawable.default_photo));
            }
            imageViews.add(imageView);
            // TODO 添加标题下面的点********当滚动主题有时再放开下面代码******
            ImageView dotsView = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 5);
            dotsView.setLayoutParams(params);
            dotsView.setBackgroundResource(R.drawable.banner);
            if (0 == i) {
                dotsView.setBackgroundResource(R.drawable.banner_icon);
            }
            dotsLayout.addView(dotsView);
            dots.add(dotsView);
        }

        // TODO 设置填充ViewPager页面的适配器********当滚动主题有时再放开下面代码******
        viewPager.setAdapter(new MyAdsPagerAdapter(getActivity(), imageViews, pushResourceList));
        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        if (count > 0) {
            handler.postDelayed(new ScrollTask(), 3000);

        }
    }

    // 定时器
    Handler handler = new Handler();

    /**
     * 换行切换任务
     *
     * @author zhangbingkang
     * @version [2013-6-18]
     * @see [相关类/方法]
     */
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                viewPager.setCurrentItem(currentItem);
                // 通过Handler切换图片
                handler.postDelayed(this, 3000);
            }
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author zhangbingkang
     * @version [2013-6-18]
     * @see [相关类/方法]
     */
    private class MyPageChangeListener implements OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            dots.get(oldPosition).setBackgroundResource(R.drawable.banner);
            dots.get(position).setBackgroundResource(R.drawable.banner_icon);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    private void getFindWeek() {
        RequestParams params = new RequestParams();
        params.put("pagesize", 10);
        params.put("pagenum", 1);
        params.put("mcode", "sale.find.week");
        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            // HttpUtil.get(Urls.push_flash_url, new JsonHttpResponseHandler()
            // {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                // LogUtil.i(TAG, "getPushCarousel", throwable.getMessage());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // LogUtil.i(TAG, "getPushCarousel", throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                // LogUtil.i(TAG, "闪屏接口=", response.toString());
                String code = response.optString("code");
                // 精选主题
                getPickTitle();
                if (code.equals("00000")) {
                    findWeekJsonObject = response;
                }
            }
        });
    }

    @Override
    public void changView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {

        switch (parentView.getId()) {
            case R.id.discovery_locations_gridView: // 目的地
                PushResource pushLocateResource = locationsList.get(position);
                if (pushLocateResource.getObjectType().equals("destination")) {
                    String destId = pushLocateResource.getObjectId();// 目的地标识
                    Intent intentForLocateCountry = new Intent(getActivity(), LocationCountryDetailActivity.class);
                    intentForLocateCountry.putExtra("destId", destId);
                    intentForLocateCountry.putExtra("destinationTitle", pushLocateResource.getResTitle());
                    startActivity(intentForLocateCountry);
                } else if (pushLocateResource.getObjectType().equals("subject")) {
                    String entityId = pushLocateResource.getObjectId();
                    Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("subjectTitle", pushLocateResource.getResTitle());
                    getActivity().startActivity(intent);
                } else if (pushLocateResource.getObjectType().equals("user")) {
                    UserInfo userInfo = (UserInfo) pushLocateResource.getObject();
                    ActivityUtil.toPersonHomePage(userInfo, getActivity());
                }

                break;
            case R.id.discovery_themes_gridView: // 主题
                PushResource pushTopicResource = topicsList.get(position);
                ActivityUtil.toDetailPage(pushTopicResource, getActivity(), false);
                break;

            case R.id.org_gridView:
                // if(position==5){
                // Intent intent = new Intent(getActivity(), OrgListActivity.class);
                // startActivity(intent);
                // }else {
                // UserInfo userInfo=(UserInfo)orgList.get(position).getObject();
                // Intent intent = new Intent(getActivity(),
                // ClubFirstPageActivity.class);
                // intent.putExtra("userInfo",userInfo);
                // getActivity().startActivity(intent);
                // }
                break;
            default:
                break;
        }

        animationOpen();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        PushResource temp = new PushResource();
        switch (v.getId()) {
            case R.id.linearlayout1:
            case R.id.more_title:// 主题
                Intent intent = new Intent(getActivity(), AllThemesActivity.class);
                startActivity(intent);
                break;
            case R.id.linearlayout3:
            case R.id.more_location_title:// 目的地
                Intent intentlocation = new Intent(getActivity(), AllLocationActivity.class);
                startActivity(intentlocation);
                break;
            case R.id.linearlayout4:// 精选
            case R.id.more_pick:
            case R.id.find_more:
                pickInterface.setTabSelection1(1);
                break;
            case R.id.past_title:
            case R.id.linearlayout2:// 人物志
                Intent intentperson = new Intent(getActivity(), AllPersonActivity.class);
                startActivity(intentperson);
                break;
            case R.id.linearlayout5:// 活动
                Intent intent2 = new Intent(getActivity(), ContentActivity.class);
                intent2.putExtra("path", "http://m.tripaway.cn/activitiesall.html");
                intent2.putExtra("title", "专题活动");
                getActivity().startActivity(intent2);
                break;
            case R.id.image1:
            case R.id.image2:
            case R.id.image3:
            case R.id.image4:
                if (v.getTag() != null)
                    temp = (PushResource) v.getTag();
                if (temp.getObjectType() != null) {
                    String type = temp.getObjectType();
                    if (type.equals("topic")) {// 主题
                        ActivityUtil.toDetailPage(temp, getActivity(), false);
                    } else if (type.equals("subject")) {// 人物志
                        ActivityUtil.toDetailPage(temp, getActivity(), true);
                    } else if (type.equals("destination")) {// 目的地
                        ActivityUtil.toDetailPage(temp, getActivity(), false);
                    } else if (type.equals("activity")) {// 活动
                        String path = Urls.huodong + temp.getObject() + ".html";
                        Intent intent3 = new Intent(getActivity(), ContentActivity.class);
                        intent3.putExtra("title", "活动");
                        intent3.putExtra("path", path);
                        getActivity().startActivity(intent3);
                        ((Activity) getActivity()).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
                    } else if (type.equals("user")) {// 达人
                        ActivityUtil.toDetailPage(temp, getActivity(), false);
                    } else if (type.equals("product")) {// 产品
                        ActivityUtil.toDetailPage(temp, getActivity(), false);
                    } else if (type.equals("story")) {// 游记
                        ActivityUtil.toDetailPage(temp, getActivity(), false);
                    } else if (type.equals("column")) {// 子栏目

                    } else if (type.equals("link")) {// 外链接
                        ActivityUtil.toDetailPage(temp, getActivity(), false);
                    }
                }
                break;
            default:
                break;
        }
    }

    /*
     * 测试 主题及时间栏目推荐url
     */
    private void getPickTitle() {
        RequestParams params = new RequestParams();
        params.put("pagesize", 10);
        params.put("pagenum", 1);
        params.put("mcode", "app.dest.topic");
        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                super.onSuccess(statusCode, headers, response);
                // 人物志
                orgUrl();
                if (statusCode == 200) {
                    pickTitleJsonObject = response;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }
        });
    }

    private void getTopicsAndPeriodLists(JSONObject response) {

        String respFlag = response.optString("msg");
        refreshLayout.finishRefreshing();
//        loadingAnimation.stop();
//        login_loading.setVisibility(View.GONE);
        if ("success".equals(respFlag)) {

            JSONArray topicArray = response.optJSONArray("data"); // 主题
            JsonUtil.getPushResources(topicArray, topicsList);
            for (int i = 0; i < topicsList.size(); i++) {
                if (!topicsList.get(i).getObjectType().equals("topic")) {
                    topicsList.remove(i);
                    i--;
                }
            }
            // pThemesAdapter = new NewProductPushThemesGridAdapter(
            // this.getActivity(), topicsList, this);
            // mThemesGridView.setAdapter(pThemesAdapter);
            // mThemesGridView.setOnItemClickListener(this);
            int size = topicsList.size() > 6 ? 6 : topicsList.size();
            LinearLayout ll = null;
            int h = DensityUtil.dip2px(getActivity(), 100);
            for (int i = 0; i < size; i++) {
                if (i % 3 == 0) {
                    ll = new LinearLayout(getActivity());
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    LayoutParams llLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    if (i < 3)
                        llLp.bottomMargin = DensityUtil.dip2px(getActivity(), 2);
                    ll.setLayoutParams(llLp);
                    mThemesGridView.addView(ll);
                }
                LayoutParams lp = new LayoutParams(themeOrDestinationImageW, h);
                if (i + 1 % 3 != 0) {
                    lp.rightMargin = DensityUtil.dip2px(getActivity(), 2);
                }
                if (ll != null)
                    ll.addView(getThemeView(i, topicsList), lp);
            }
        }
    }

    /*
     * 测试 目的地资源推荐url
     */
    private void getLocation() {

        RequestParams params = new RequestParams();
        params.put("pagesize", 10);
        params.put("pagenum", 1);
        params.put("mcode", "app.dest.dest");
        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                super.onSuccess(statusCode, headers, response);
                // LogUtil.i(TAG,
                // "Destination_LocateDestinationFragment_testLocationTimeUrl",
                // "statusCode=" + statusCode);
                // LogUtil.i(TAG, "目的地", "responseString=" + response);
                // 远行精选
                testPickedUrl();
                locationJsonObject = response;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
                // LogUtil.i(TAG,
                // "Destination_LocateDestinationFragment_testLocationTimeUrl",
                // "statusCode=" + statusCode);
                // LogUtil.i(TAG,
                // "Destination_LocateDestinationFragment_testLocationTimeUrl",
                // "responseString=" + responseString);

            }
        });
    }

    /**
     * 加载推荐目的地的方法
     * <p/>
     * 服务端目的地接口返回的数据
     *
     * @return
     */

    private void getHotLocationsList(JSONObject jsonResponse) {
        String code = jsonResponse.optString("code");
        if ("00000".equals(code)) {

            JSONArray jsonDestArray = jsonResponse.optJSONArray("data");
            for (int i = 0; i < jsonDestArray.length(); i++) {
                JSONObject jsonObj = jsonDestArray.optJSONObject(i);
                PushResource pushResource = new PushResource();
                pushResource.setId(jsonObj.optString("id"));
                pushResource.setResTitle(jsonObj.optString("resTitle"));
                pushResource.setObjectType(jsonObj.optString("objectType"));
                pushResource.setObjectId(jsonObj.optString("objectId"));
                if (jsonObj.optString("keywords").contains("，")) {
                    pushResource.setKeywords(jsonObj.optString("keywords").replace("，", "·"));
                } else {
                    pushResource.setKeywords(jsonObj.optString("keywords"));
                }
                pushResource.setTitleImage(jsonObj.optString("titleImage"));
                pushResource.setSubTitle(jsonObj.optString("subTitle"));
                JSONObject jsonDestObj = jsonObj.optJSONObject("object");
                if ("destination".equals(pushResource.getObjectType())) {
                    if (pushResource.getObject() instanceof TripDestination) {
                        TripDestination tripDestination = (TripDestination) pushResource.getObject();
                        tripDestination.setDestProNum(jsonDestObj.optString("pNum"));
                        tripDestination.setDestId(jsonDestObj.optString("id"));
                        tripDestination.setDestSupNum(jsonDestObj.optString("sNum"));
                        tripDestination.setDestLogoKey(jsonDestObj.optString("logo"));
                        tripDestination.setDestName(jsonDestObj.optString("destName"));
                        tripDestination.setDestName(jsonDestObj.optString("destNameEn"));
                        tripDestination.setDestDescription(jsonDestObj.optString("description"));
                    }

                }
                if ("user".equals(pushResource.getObjectType())) {
                    UserInfo userInfo = new UserInfo();
                    JSONObject userObj = jsonObj.optJSONObject("object");

                    if (!StringUtils.isEmpty(userInfo.getUserId()))
                        continue;
                    userInfo.setGender(userObj.optString("sex"));
                    userInfo.setStatus(userObj.optString("status"));
                    userInfo.setAvatar(userObj.optString("avatar"));
                    userInfo.setEmail(userObj.optString("email"));
                    userInfo.setNickname(userObj.optString("nickName"));
                    userInfo.setUserId(userObj.optString("userId"));
                    userInfo.setBackground(userObj.optString("background"));
                    userInfo.setBrief(userObj.optString("brief"));
                    userInfo.setIntroduction(userObj.optString("introduction"));
                    userInfo.setMobile(userObj.optString("mobile"));
                    JSONArray professionArray = userObj.optJSONArray("profession");
                    JSONObject tagObj = userObj.optJSONObject("tags");
                    if (null != tagObj) {
                        Tags tag = new Tags();
                        JSONArray interest = tagObj.optJSONArray("interest");
                        if (interest != null) {
                            List<String> list = new ArrayList<>();
                            for (int j = 0; j < interest.length(); j++) {
                                list.add(interest.optString(j));
                            }
                            tag.setInterests(list);
                            userInfo.setTag(tag);
                        }
                    }
                    String profession = "";
                    if (professionArray != null) {
                        for (int j = 0; j < professionArray.length(); j++) {
                            if (j == 0) {
                                profession += professionArray.optString(j);
                            } else {
                                profession += "," + professionArray.optString(j);
                            }
                        }
                    }
                    userInfo.setPermission(profession);
                    JSONObject orgRoleObj = userObj.optJSONObject("orgRole");
                    if (orgRoleObj != null) {
                        OrgRole orgRole = new OrgRole();
                        orgRole.setRoleType(orgRoleObj.optString("roleType"));
                        orgRole.setPermission(orgRoleObj.optString("permission"));
                        orgRole.setRoleCode(orgRoleObj.optString("roleCode"));
                        if (orgRole.getRoleCode().equals("driver")) {
                            System.out.print(orgRole.getRoleCode().equals("driver"));
                        }
                        orgRole.setRoleName(orgRoleObj.optString("roleName"));
                        userInfo.setOrgRole(orgRole);
                    }
                    pushResource.setObject(userInfo);
                }
                locationsList.add(pushResource);

            }

        }
        // pLocationsAdapter = new ProductPushDestinationsGridAdapter(
        // this.getActivity(), locationsList);
        // mLocationsGridView.setAdapter(pLocationsAdapter);
        // mLocationsGridView.setOnItemClickListener(this);
        int size = locationsList.size() > 6 ? 6 : locationsList.size();
        LinearLayout ll = null;
        int h = DensityUtil.dip2px(getActivity(), 160);
        for (int i = 0; i < size; i++) {
            if (i % 3 == 0) {
                ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                LayoutParams llLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                if (i < 3)
                    llLp.bottomMargin = DensityUtil.dip2px(getActivity(), 2);
                ll.setLayoutParams(llLp);
                mLocationsGridView.addView(ll);
            }
            LayoutParams lp = new LayoutParams(themeOrDestinationImageW, h);
            if (i + 1 % 3 != 0) {
                lp.rightMargin = DensityUtil.dip2px(getActivity(), 2);
            }
            if (ll != null)
                ll.addView(getDestinationsView(i, locationsList), lp);
        }
        // setGridViewRealHeight(mLocationsGridView);
    }

    /*
     * 测试 精选推荐 接口
     */
    private void testPickedUrl() {
        HttpUtil.get(Urls.picked_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200) {
//                    loadingAnimation.stop();
                    // ((View)login_loading.ge[tParent()).setBackgroundColor(Color.BLACK);
//                    login_loading.setVisibility(View.GONE);
                    System.out.println("精选推荐接口=" + response);
                    String code = response.optString("code");
                    if (code.equals("00000")) {
                        pickedItemList.clear();
                        pickedJsonObject = response;

                        // pickedListView.successRefresh();
                    } else {
                        // pickedListView.stopRefresh();
                        // ToastUtil.showToast(getActivity(), "errorCode=" +
                        // code);
                    }
                    mainScorll.setVisibility(View.VISIBLE);
                    refreshLayout.finishRefreshing();
                    setPushCarouselData(pushCarouselJsonObject);
                    setFindWeekData(findWeekJsonObject);
                    setPickTitleData(pickTitleJsonObject);
                    setOrgUrlData(orgJsonObject);
                    setLocationData(locationJsonObject);
                    setPickedData(pickedJsonObject);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // pickedListView.stopRefresh();
                // ToastUtil.showToast(getActivity(), "errorCode=" +
                // statusCode);
                System.out.println(errorResponse);
            }

        });
    }

    /**
     * init 精选item
     */
    private void initPickedItemList(JSONObject jsonObj) {
        JSONArray arrayObj = jsonObj.optJSONArray("data");

        for (int i = 0; i < arrayObj.length(); i++) {
            JSONObject jsonObject = arrayObj.optJSONObject(i);

            if ("product".equals(jsonObject.optString("objectType"))) {
                Map<String, Object> mapItem = new HashMap<String, Object>();
                mapItem.put("pickedItemTitle", jsonObject.optString("resTitle"));
                mapItem.put("pickedItemSubTitle", jsonObject.optString("subTitle"));
                mapItem.put("pickedItemContent", jsonObject.optString("content"));
                mapItem.put("pickedItemId", jsonObject.optString("objectId"));
                mapItem.put("pickedItemType", jsonObject.optString("objectType"));
                mapItem.put("pickedItemImg", jsonObject.optString("titleImage"));

                JSONObject productObj = jsonObject.optJSONObject("object");
                // System.out.println("product=" + productObj);
                ProductNewInfo proNewInfo = new ProductNewInfo();
                proNewInfo.setPv(productObj.optString("pv"));
                proNewInfo.setOriginalPrice(productObj.optString("originalPrice"));
                proNewInfo.setId(productObj.optString("id"));
                proNewInfo.setDistance(productObj.optString("distance"));
                JSONArray topicArray = (productObj.optJSONArray("topics"));
                for (int j = 0; j < topicArray.length(); j++) {
                    if (j == 6) {
                        break;
                    }
                    proNewInfo.getTopics().add(topicArray.optString(j));
                }
                proNewInfo.setTitle(productObj.optString("title"));
                proNewInfo.setPoiCount(productObj.optString("poiCount"));
                proNewInfo.setPrice(productObj.optString("price"));
                proNewInfo.setDays((productObj.optString("days")));
                proNewInfo.setDescription(productObj.optString("description"));
                proNewInfo.setPriceMax(productObj.optString("priceMax"));
                proNewInfo.setTitleImg(productObj.optString("titleImg"));
                proNewInfo.setMaxMember(productObj.optString("maxMember"));
                proNewInfo.setProductType(productObj.optString("productType"));
                proNewInfo.setServices(productObj.optString("serviceCodes"));
                proNewInfo.setIsFav(productObj.optString("isFav"));
                // add by lij 2015/09/25 start 新增每个产品的参与人数
                proNewInfo.setMemberJoinCount(productObj.optString("servTimes"));

                // add by lij 2015/09/25 end
                // tags参数有问题 暂时不解析
                if (productObj.optJSONObject("creater") != null) {
                    proNewInfo.getUser().setGender(productObj.optJSONObject("creater").optString("sex"));
                    proNewInfo.getUser().setStatus(productObj.optJSONObject("creater").optString("status"));
                    proNewInfo.getUser().setEmail(productObj.optJSONObject("creater").optString("email"));
                    proNewInfo.getUser().setNickname(productObj.optJSONObject("creater").optString("nickName"));
                    proNewInfo.getUser().setUserId(productObj.optJSONObject("creater").optString("userId"));
                    proNewInfo.getUser().setAvatar(productObj.optJSONObject("creater").optString("avatar"));
                    proNewInfo.getUser().setIntroduction(productObj.optJSONObject("creater").optString("introduction"));
                    proNewInfo.getUser().setMobile(productObj.optJSONObject("creater").optString("mobile"));
                }
                if (productObj.optJSONObject("exts") != null) {
                    HashMap<String, String> exts = new HashMap<String, String>();
                    if (!StringUtils.isEmpty(productObj.optJSONObject("exts").optString("refer_tags"))) {
                        exts.put("refer_tags", productObj.optJSONObject("exts").optString("refer_tags"));
                    }
                    if (!StringUtils.isEmpty(productObj.optJSONObject("exts").optString("big_refer_tags"))) {
                        exts.put("big_refer_tags", productObj.optJSONObject("exts").optString("big_refer_tags"));
                    }
                    if (exts.size() > 0) {
                        proNewInfo.setExts(exts);
                    }
                }
                mapItem.put("objectValue", proNewInfo);
                pickedItemList.add(mapItem);
            } else if ("user".equals(jsonObject.optString("objectType"))) {
                Map<String, Object> mapItem = new HashMap<String, Object>();
                mapItem.put("pickedItemId", jsonObject.optString("objectId"));
                mapItem.put("pickedItemType", jsonObject.optString("objectType"));
                mapItem.put("pickedItemImg", jsonObject.optString("titleImage"));
                mapItem.put("pickedItemTitle", jsonObject.optString("resTitle"));
                mapItem.put("pickedItemSubTitle", jsonObject.optString("subTitle"));
                mapItem.put("pickedItemContent", jsonObject.optString("content"));

                JSONObject userObj = jsonObject.optJSONObject("object");

                UserInfo userInfo = new UserInfo();
                userInfo.setGender(userObj.optString("sex"));
                userInfo.setStatus(userObj.optString("status"));
                userInfo.setAvatar(userObj.optString("avatar"));
                userInfo.setEmail(userObj.optString("email"));
                userInfo.setNickname(userObj.optString("nickName"));
                userInfo.setUserId(userObj.optString("userId"));
                userInfo.setBackground(userObj.optString("background"));
                userInfo.setBrief(userObj.optString("brief"));
                userInfo.setIntroduction(userObj.optString("introduction"));
                userInfo.setMobile(userObj.optString("mobile"));
                JSONArray professionArray = userObj.optJSONArray("profession");
                String profession = "";
                if (professionArray != null) {
                    for (int j = 0; j < professionArray.length(); j++) {
                        if (j == 0) {
                            profession += professionArray.optString(j);
                        } else {
                            profession += "," + professionArray.optString(j);
                        }
                    }
                }
                userInfo.setPermission(profession);
                mapItem.put("objectValue", userInfo);
                JSONObject orgRoleObj = userObj.optJSONObject("orgRole");
                if (orgRoleObj != null) {
                    OrgRole orgRole = new OrgRole();
                    orgRole.setRoleType(orgRoleObj.optString("roleType"));
                    orgRole.setPermission(orgRoleObj.optString("permission"));
                    orgRole.setRoleCode(orgRoleObj.optString("roleCode"));
                    if (orgRole.getRoleCode().equals("driver")) {
                        System.out.print(orgRole.getRoleCode().equals("driver"));
                    }
                    orgRole.setRoleName(orgRoleObj.optString("roleName"));
                    userInfo.setOrgRole(orgRole);
                }
                JSONObject tagsArray = userObj.optJSONObject("tags");
                if (tagsArray != null) {
                    Tags tags = new Tags();
                    JSONArray interest = tagsArray.optJSONArray("interest");
                    if (interest != null) {
                        for (int j = 0; j < interest.length(); j++) {
                            tags.getInterests().add(interest.optString(j));
                        }
                    }
                    JSONArray sphere = tagsArray.optJSONArray("sphere");
                    if (sphere != null) {
                        for (int j = 0; j < sphere.length(); j++) {
                            tags.getSpheres().add(sphere.optString(j));
                        }
                    }
                    JSONArray footprint = tagsArray.optJSONArray("footprint");
                    if (footprint != null) {
                        for (int j = 0; j < footprint.length(); j++) {
                            tags.getFootprints().add(footprint.optString(j));
                        }
                    }
                    JSONArray club_type = tagsArray.optJSONArray("club_type");
                    if (club_type != null) {
                        for (int j = 0; j < club_type.length(); j++) {
                            tags.getClubTypes().add(club_type.optString(j));
                        }
                    }
                    JSONArray serv_area = tagsArray.optJSONArray("serv_area");
                    if (serv_area != null) {
                        for (int j = 0; j < serv_area.length(); j++) {
                            tags.getServAreas().add(serv_area.optString(j));
                        }
                    }
                    userInfo.setTag(tags);
                }
                pickedItemList.add(mapItem);
            } else if ("column".equals(jsonObject.optString("objectType"))
                    || "topic".equals(jsonObject.optString("objectType"))) {
                Map<String, Object> mapItem = new HashMap<String, Object>();
                mapItem.put("pickedItemId", jsonObject.optString("objectId"));
                mapItem.put("pickedItemType", jsonObject.optString("objectType"));

                mapItem.put("pickedItemSubTitle", jsonObject.optString("subTitle"));
                mapItem.put("pickedItemContent", jsonObject.optString("content"));

                TopicInfo topicItem1 = new TopicInfo();

                topicItem1.setId(jsonObject.optString("objectId"));
                topicItem1.setTitle(jsonObject.optString("resTitle"));
                topicItem1.setBackground(jsonObject.optString("titleImage"));
                mapItem.put("pickedThemeItem1", topicItem1);

                pickedItemList.add(mapItem);

            }

        }
        if (pickedItemList != null && pickedItemList.size() > 0) {
            find_more.setVisibility(View.VISIBLE);
            for (int j = 0; j < pickedItemList.size(); j++) {
                if (j == 5)
                    break;
                pickedListView.addView(getPickView(j, pickedItemList));
            }
        } else {
            find_more.setVisibility(View.GONE);
        }
    }

    /*
     * 合伙人
     */
    private void orgUrl() {
        RequestParams params = new RequestParams();
        params.put("pagesize", 10);
        params.put("pagenum", 1);
        params.put("mcode", "sale.find.randomsubject");
        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                super.onSuccess(statusCode, headers, response);
                // LogUtil.i(TAG,
                // "Destination_LocateDestinationFragment_testLocationTimeUrl",
                // "statusCode=" + statusCode);
                // LogUtil.i(TAG, "俱乐部", "responseString=" + response);
                // 目的地
                getLocation();
                orgJsonObject = response;

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
                // LogUtil.i(TAG,
                // "Destination_LocateDestinationFragment_testLocationTimeUrl",
                // "statusCode=" + statusCode);
                // LogUtil.i(TAG,
                // "Destination_LocateDestinationFragment_testLocationTimeUrl",
                // "responseString=" + responseString);

            }
        });
    }

    /**
     * 加载推荐俱乐部的方法
     *
     * @return
     */

    private void getHotOrgsList(JSONObject jsonResponse) {
        String code = jsonResponse.optString("code");
        if ("00000".equals(code)) {
            JSONArray jsonDestArray = jsonResponse.optJSONArray("data");
            for (int i = 0; i < jsonDestArray.length(); i++) {
                PushResource pushResource = new PushResource();
                // if (i < 5) {
                JSONObject jsonObj = jsonDestArray.optJSONObject(i);

                pushResource.setId(jsonObj.optString("id"));
                pushResource.setResTitle(jsonObj.optString("resTitle"));
                pushResource.setObjectType(jsonObj.optString("objectType"));
                pushResource.setObjectId(jsonObj.optString("objectId"));
                if (jsonObj.optString("keywords").contains("，")) {
                    pushResource.setKeywords(jsonObj.optString("keywords").replace("，", "·"));
                } else {
                    pushResource.setKeywords(jsonObj.optString("keywords"));
                }
                pushResource.setSubTitle(jsonObj.optString("subTitle"));
                pushResource.setTitleImage(jsonObj.optString("titleImage"));
                if ("link".equals(jsonObj.optString("objectType"))) {
                    pushResource.setObject(jsonObj.optString("object"));

                } else {
                    JSONObject jSONObject = jsonObj.optJSONObject("object");
                    if (jSONObject != null && jSONObject.length() > 0) {
                        if ("product".equals(jsonObj.optString("objectType"))) {
                            pushResource.setObject(JsonUtil.getProductNewInfo(jSONObject));

                        } else if ("topic".equals(jsonObj.optString("objectType"))) {
                            pushResource.setObject(JsonUtil.getTopicInfo(jSONObject));
                        } else if ("user".equals(jsonObj.optString("objectType"))) {
                            pushResource.setObject(JsonUtil.getUserInfo(jSONObject));
                        } else if ("destination".equals(jsonObj.optString("objectType"))) {
                            pushResource.setObject(JsonUtil.getDestinationInfo(jSONObject));

                        } else if ("softtext".equals(jsonObj.optString("objectType"))) {
                            pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));

                        } else if ("story".equals(jsonObj.optString("objectType"))) {
                            pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));
                        } else {
                            JSONArray relatedResourcesJson = jSONObject.optJSONArray("relatedResources");
                            String objectType = pushResource.getObjectType();
                            if (jSONObject.optString("cover") != null && "subject".equals(objectType))
                                pushResource.setTitleImage(jSONObject.optString("cover"));
                            UserInfo userInfo = new UserInfo();
                            if (relatedResourcesJson != null && relatedResourcesJson.length() > 0) {
                                for (int m = 0; m < relatedResourcesJson.length(); m++) {
                                    JSONObject relatedResourceJson = relatedResourcesJson.optJSONObject(m);
                                    if (relatedResourceJson.optString("objectType") != null
                                            && !"user".equals(relatedResourceJson.optString("objectType"))) {
                                        JSONObject productObj = relatedResourceJson.optJSONObject("object");
                                        if (productObj != null && userInfo.getReferPics().size() < 3) {
                                            if (productObj.optString("titleImg") != null)
                                                userInfo.getReferPics().add(productObj.optString("titleImg"));
                                        }
                                        continue;
                                    }
                                    if (!StringUtils.isEmpty(userInfo.getUserId()))
                                        continue;
                                    JSONObject objectParamObj = relatedResourceJson.optJSONObject("objectParams");
                                    if (objectParamObj != null && objectParamObj.length() > 0) {
                                        pushResource.getObjectParam().put("nickname",
                                                objectParamObj.optString("nickname"));
                                        pushResource.getObjectParam().put("tags", objectParamObj.optString("tags"));
                                        pushResource.getObjectParam().put("avartar",
                                                objectParamObj.optString("avartar"));
                                    }
                                    JSONObject userObj = relatedResourceJson.optJSONObject("object");
                                    userInfo.setGender(userObj.optString("sex"));
                                    userInfo.setStatus(userObj.optString("status"));
                                    userInfo.setAvatar(userObj.optString("avatar"));
                                    userInfo.setEmail(userObj.optString("email"));
                                    userInfo.setNickname(userObj.optString("nickName"));
                                    userInfo.setUserId(userObj.optString("userId"));
                                    userInfo.setBackground(userObj.optString("background"));
                                    userInfo.setBrief(userObj.optString("brief"));
                                    userInfo.setIntroduction(userObj.optString("introduction"));
                                    userInfo.setMobile(userObj.optString("mobile"));
                                    JSONArray professionArray = userObj.optJSONArray("profession");
                                    JSONObject tagObj = userObj.optJSONObject("tags");
                                    if (null != tagObj) {
                                        Tags tag = new Tags();
                                        JSONArray interest = tagObj.optJSONArray("interest");
                                        if (interest != null) {
                                            List<String> list = new ArrayList<>();
                                            for (int j = 0; j < interest.length(); j++) {
                                                list.add(interest.optString(j));
                                            }
                                            tag.setInterests(list);
                                            userInfo.setTag(tag);
                                        }
                                    }
                                    String profession = "";
                                    if (professionArray != null) {
                                        for (int j = 0; j < professionArray.length(); j++) {
                                            if (j == 0) {
                                                profession += professionArray.optString(j);
                                            } else {
                                                profession += "," + professionArray.optString(j);
                                            }
                                        }
                                    }
                                    userInfo.setPermission(profession);
                                    JSONObject orgRoleObj = userObj.optJSONObject("orgRole");
                                    if (orgRoleObj != null) {
                                        OrgRole orgRole = new OrgRole();
                                        orgRole.setRoleType(orgRoleObj.optString("roleType"));
                                        orgRole.setPermission(orgRoleObj.optString("permission"));
                                        orgRole.setRoleCode(orgRoleObj.optString("roleCode"));
                                        if (orgRole.getRoleCode().equals("driver")) {
                                            System.out.print(orgRole.getRoleCode().equals("driver"));
                                        }
                                        orgRole.setRoleName(orgRoleObj.optString("roleName"));
                                        userInfo.setOrgRole(orgRole);
                                    }
                                    pushResource.setObject(userInfo);
                                }
                            }
                        }
                    }
                }
                // } else {
                // if (i == 5) {
                // pushResource.setResTitle("更多");
                // orgList.add(pushResource);
                // break;
                // }
                // }
                orgList.add(pushResource);

            }
        }
        orgListUser.addAll(orgList);
        // // 发现页最多显示三个
        // for (int i = 0; i < orgList.size(); i++) {
        // if (i < 3) {
        // orgListUser.add(orgList.get(i));
        // }
        // }
        setGridView();

        // setGridViewRealHeight(mLocationsGridView);
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        int size = orgListUser.size();

        for (int i = 0; i < size; i++) {
            int w = screenWidth - DensityUtil.dip2px(getActivity(), 55);
            LinearLayout.LayoutParams lp = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
            lp.leftMargin = DensityUtil.dip2px(getActivity(), 5);
            if (i == size - 1) {
                lp.rightMargin = DensityUtil.dip2px(getActivity(), 5);
            }
            grid.addView(getSubjectGridView(i, orgListUser), lp);
        }

    }

    public PickInterface pickInterface;

    public void setPickInterface(PickInterface pickInterface) {
        this.pickInterface = pickInterface;
    }

    private View getSubjectGridView(int position, final List<PushResource> subjectPushResourceList) {
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        PushResource subjectPushResource = (PushResource) subjectPushResourceList.get(position);
        View convertView = inflator.inflate(R.layout.item_discovery_subject, null);
        int w = screenWidth - DensityUtil.dip2px(getActivity(), 55);
        LinearLayout.LayoutParams lp = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        // lp.rightMargin=DensityUtil.dip2px(getActivity(), 10);
        // convertView.setLayoutParams(lp);
        ImageView cover = (ImageView) convertView.findViewById(R.id.cover);
        TextView subjectTitle = (TextView) convertView.findViewById(R.id.subject_title);
        TextView subjectSubtitle = (TextView) convertView.findViewById(R.id.subject_subtitle);
        TextView clubName = (TextView) convertView.findViewById(R.id.club_name);
        TextView tags = (TextView) convertView.findViewById(R.id.tags);
        ImageView avartar = (ImageView) convertView.findViewById(R.id.avartar);
        ImageView subjectIcon = (ImageView) convertView.findViewById(R.id.subject_icon);
        /*
         * AssetManager mgr = context.getAssets();// 得到AssetManager Typeface tf
		 * = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");// 根据路径得到Typeface
		 * //locationenNameTv.setTypeface(tf);// 设置字体
		 * locationzhNameTv.setTypeface(tf);// 设置字体
		 */
        cover.getLayoutParams().width = w;
        String objectType = subjectPushResource.getObjectType();
        if (!StringUtils.isEmpty(objectType) && !"subject".equals(objectType)) {
            ((View) (avartar.getParent().getParent())).setVisibility(View.GONE);
            subjectIcon.setVisibility(View.GONE);
        } else {
            ((View) (avartar.getParent().getParent())).setVisibility(View.VISIBLE);
            subjectIcon.setVisibility(View.VISIBLE);
        }
        UserInfo userInfo = null;
        if (subjectPushResource.getObject() != null && subjectPushResource.getObject() instanceof UserInfo) {
            userInfo = (UserInfo) subjectPushResource.getObject();
        }
        ;

        if (subjectPushResource.getTitleImage() != null) {
            ImageLoader.getInstance().displayImage(Urls.imgHost + subjectPushResource.getTitleImage(), cover,
                    AppConfig.options(R.drawable.ic_launcher));
        }
        ;
        Map<String, String> objectParam = subjectPushResource.getObjectParam();
        // if (userInfo!=null)
        // {
        //
        // }
        if (objectParam != null) {
            if (objectParam.get("nickname") != null) {
                clubName.setText(objectParam.get("nickname"));
            }
            if (objectParam.get("tags") != null) {
                tags.setText(objectParam.get("tags"));
            }
            if (objectParam.get("avartar") != null) {
                ImageLoader.getInstance().displayImage(Urls.imgHost + objectParam.get("avartar"), avartar,
                        AppConfig.options(R.drawable.ic_launcher));
            }
        }

        if (!StringUtils.isEmpty(subjectPushResource.getResTitle())) {
            subjectTitle.setText(subjectPushResource.getResTitle());
            subjectTitle.getPaint().setFakeBoldText(true);
        }

        if (!StringUtils.isEmpty(subjectPushResource.getSubTitle())) {
            subjectSubtitle.setText(subjectPushResource.getSubTitle());
        }
        avartar.setTag(position);
        avartar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int position = (Integer) v.getTag();
                ActivityUtil.toDetailPage(subjectPushResourceList.get(position), getActivity(), true);
            }
        });
        View view = (View) subjectTitle.getParent();
        view.setTag(position);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int position = (Integer) v.getTag();
                ActivityUtil.toDetailPage(subjectPushResourceList.get(position), getActivity(), true);
            }
        });
        return convertView;
    }

    private View getPickView(int position, final List<Map<String, Object>> resultList) {
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        Log.e("getPickView", Integer.toString(position));
        Map<String, Object> mapItem = resultList.get(position);
        View convertView = (View) inflator.inflate(R.layout.new_trip_picked_item, null);
        // 精选-普通产品
        RelativeLayout pickedProductLayoutContainer = (RelativeLayout) convertView
                .findViewById(R.id.layout_trip_picked_product_container);
        TextView pickedProductUnit = (TextView) convertView.findViewById(R.id.trip_picked_product_info_unit_tv);
        LinearLayout pickedProductGroupLayout = (LinearLayout) convertView
                .findViewById(R.id.trip_picked_product_group_layout);
        ImageView pickedProductIv = (ImageView) convertView.findViewById(R.id.trip_picked_product_iv);
        TextView pickedProductPriceTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_price_tv);
        TextView pickedProductNameTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_name_tv);
        ImageView storeImg = (ImageView) convertView.findViewById(R.id.if_stored);
        TextView pickedProductTimeSchedualTv = (TextView) convertView
                .findViewById(R.id.trip_picked_product_timeSchedual_tv);
        TextView pickedProductDistanceTv = (TextView) convertView.findViewById(R.id.trip_picked_product_distance_tv);
        TextView pickedProductLableTv = (TextView) convertView.findViewById(R.id.trip_picked_product_lable);
        ImageView pickedProductPeoIv = (ImageView) convertView.findViewById(R.id.trip_picked_product_peo_iv); // 参与人数图片
        FrameLayout pickedServicerLayoutContainer = (FrameLayout) convertView
                .findViewById(R.id.layout_trip_picked_product_servicer_container);
        ImageView pickedServicerIv = (ImageView) convertView.findViewById(R.id.trip_picked_product_servicer_iv);
        RoundImageView pickedServicerHeadIconIv = (RoundImageView) convertView
                .findViewById(R.id.trip_picked_product_servicer_headIcon_iv);
        TextView pickedServicerNameTv = (TextView) convertView.findViewById(R.id.trip_picked_product_servicer_name_tv);
        TextView pickedServicerBriefTv = (TextView) convertView
                .findViewById(R.id.trip_picked_product_servicer_brief_tv);
        // 精选-主题
        LinearLayout pickedProductThemeLayoutContainer = (LinearLayout) convertView
                .findViewById(R.id.layout_trip_picked_product_theme_container);
        FrameLayout themeProductLayout1 = (FrameLayout) convertView.findViewById(R.id.theme_product1);
        // themeProductLayout2 =
        // (FrameLayout)convertView.findViewById(R.id.theme_product2);
        ImageView pickedProductThemeIv1 = (ImageView) convertView.findViewById(R.id.trip_picked_product_theme_item1_iv);
        // pickedProductThemeIv2 =
        // (ImageView)convertView.findViewById(R.id.trip_picked_product_theme_item2_iv);
        TextView pickedProductThemeNameTv1 = (TextView) convertView
                .findViewById(R.id.trip_picked_product_theme_item_name_tv1);
        // pickedProductThemeNameTv2 =
        // (TextView)convertView.findViewById(R.id.trip_picked_product_theme_item_name_tv2);
        TextView pickedTag1 = (TextView) convertView.findViewById(R.id.trip_picked_tag1_lable);
        TextView pickedTag2 = (TextView) convertView.findViewById(R.id.trip_picked_tag2_lable);
        TextView pickedTag3 = (TextView) convertView.findViewById(R.id.trip_picked_tag3_lable);
        TextView pickedTag4 = (TextView) convertView.findViewById(R.id.trip_picked_tag4_lable);
        RelativeLayout layout_originalPrice = (RelativeLayout) convertView.findViewById(R.id.layout_originalPrice);
        TextView originalPrice = (TextView) convertView.findViewById(R.id.originalPrice);
        TextView pv = (TextView) convertView.findViewById(R.id.trip_picked_product_peo_join_tv);
        // if判断 mapItem的type属性 其中 1 表示精选普通产品 2 表示精选服务者 3 表示 精选 主题
        if (((String) mapItem.get("pickedItemType")).equals("product")) // 精选 产品
        {

            pickedProductIv.getLayoutParams().width = screenWidth;
            final ProductNewInfo product = (ProductNewInfo) mapItem.get("objectValue");
            // 设置原价

            float price = 0;
            if (!StringUtils.isEmpty(product.getPrice())) {
                price = Float.parseFloat(product.getPrice());
            }
            float price2 = 0;
            if (!StringUtils.isEmpty(product.getOriginalPrice())) {
                price2 = Float.parseFloat(product.getOriginalPrice());
            }
            if (!StringUtils.isEmpty(product.getOriginalPrice()) && "0".equals(product.getOriginalPrice()) == false
                    && (price < price2)) {
                layout_originalPrice.setVisibility(View.VISIBLE);
                originalPrice.setText("¥" + product.getOriginalPrice());
            } else {
                layout_originalPrice.setVisibility(View.GONE);
            }

            // 设置浏览人数
            if (!StringUtils.isEmpty(product.getPv())) {
                pv.setText(product.getPv());
            }
            HashMap<String, String> exts = product.getExts();
            String tag = null;
            if (exts != null && exts.size() > 0) {
                tag = exts.get("refer_tags");
                String bigTag = exts.get("big_refer_tags");
                if (bigTag != null) {
                    if (bigTag.contains("小团游")) {
                        pickedTag1.setVisibility(View.VISIBLE);
                    } else {
                        pickedTag1.setVisibility(View.GONE);
                    }
                    if (bigTag.contains("深度游")) {
                        pickedTag2.setVisibility(View.VISIBLE);
                    } else {
                        pickedTag2.setVisibility(View.GONE);
                    }
                    if (bigTag.contains("可定制")) {
                        pickedTag3.setVisibility(View.VISIBLE);
                    } else {
                        pickedTag3.setVisibility(View.GONE);
                    }
                    if (bigTag.contains("跟团游")) {
                        pickedTag4.setVisibility(View.VISIBLE);
                    } else {
                        pickedTag4.setVisibility(View.GONE);
                    }
                } else {
                    pickedTag4.setVisibility(View.GONE);
                    pickedTag1.setVisibility(View.GONE);
                    pickedTag2.setVisibility(View.GONE);
                    pickedTag3.setVisibility(View.GONE);
                }
            } else {
                pickedTag4.setVisibility(View.GONE);
                pickedTag1.setVisibility(View.GONE);
                pickedTag2.setVisibility(View.GONE);
                pickedTag3.setVisibility(View.GONE);
            }

            pickedProductLayoutContainer.setVisibility(View.VISIBLE);
            pickedServicerLayoutContainer.setVisibility(View.GONE);
            pickedProductThemeLayoutContainer.setVisibility(View.GONE);

            if ((((ProductNewInfo) mapItem.get("objectValue")).getProductType()).equals("team")) {
                pickedProductUnit.setVisibility(View.VISIBLE);
                // pickedProductGroupLayout.setVisibility(View.VISIBLE);
            } else {
                pickedProductUnit.setVisibility(View.GONE);
                pickedProductGroupLayout.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty((String) mapItem.get("pickedItemImg"))) {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String) mapItem.get("pickedItemImg"),
                        pickedProductIv, AppConfig.options(R.drawable.ic_launcher));
            }

            if ((String) (((ProductNewInfo) mapItem.get("objectValue")).getPrice()) != null) {
                pickedProductPriceTv.setText("￥" + (String) (((ProductNewInfo) mapItem.get("objectValue")).getPrice()));
                /*
                 * if ((boolean) (((ProductNewInfo) mapItem.get("objectValue"))
				 * .getProductType()).equals("team")) {
				 * pickedProductUnit.setVisibility(View.VISIBLE);
				 * pickedProductGroupLayout.setVisibility(View.VISIBLE); }
				 */
                // 价格字体加粗
                pickedProductNameTv.getPaint().setFakeBoldText(true);
            } else {
                pickedProductPriceTv.setText("¥888");

                // 价格字体加粗
                pickedProductNameTv.getPaint().setFakeBoldText(true);

            }
            String productName = (String) mapItem.get("pickedItemTitle");

            // 标题字体加粗
            pickedProductNameTv.getPaint().setFakeBoldText(true);
            pickedProductTimeSchedualTv
                    .setText((String) (((ProductNewInfo) mapItem.get("objectValue")).getDays()) + "天");
            pickedProductDistanceTv
                    .setText((String) (((ProductNewInfo) mapItem.get("objectValue")).getDistance()) + "km");
            if (!StringUtils.isEmpty((String) mapItem.get("pickedItemImg"))) {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String) mapItem.get("pickedItemImg"),
                        pickedProductIv, AppConfig.options(R.drawable.default_photo));
            }
            pickedProductNameTv.setText(productName);
            if ((String) (((ProductNewInfo) mapItem.get("objectValue")).getPrice()) != null) {
                pickedProductPriceTv.setText("￥" + (String) (((ProductNewInfo) mapItem.get("objectValue")).getPrice()));

                // 价格字体加粗
                pickedProductNameTv.getPaint().setFakeBoldText(true);
            } else {
                pickedProductPriceTv.setText("¥888");

                // 价格字体加粗
                pickedProductNameTv.getPaint().setFakeBoldText(true);
            }
            // 标题字体加粗
            pickedProductNameTv.getPaint().setFakeBoldText(true);
            pickedProductTimeSchedualTv
                    .setText((String) (((ProductNewInfo) mapItem.get("objectValue")).getDays()) + "天");
            pickedProductDistanceTv
                    .setText((String) (((ProductNewInfo) mapItem.get("objectValue")).getDistance()) + "km");
            // add by lij 2015/09/25 start
            // if ("0".equals(((ProductNewInfo)
            // mapItem.get("objectValue")).getMemberJoinCount())) {
            // } else {
            // pickedProductPeoTv
            // .setText(((ProductNewInfo)
            // mapItem.get("objectValue")).getMemberJoinCount() + "人浏览");
            // }

            if (!StringUtils.isEmpty(tag)) {
                pickedProductLableTv.setText(tag.replaceAll(";", " · "));
            } else {
                // add by lij 2015/09/25 end
                List<String> topicNameTvArray = ((ProductNewInfo) mapItem.get("objectValue")).getTopics();
                if (topicNameTvArray != null && topicNameTvArray.size() != 0) {
                    String lable = "";
                    for (int i = 0; i < topicNameTvArray.size(); i++) {
                        if (i != topicNameTvArray.size() - 1) {
                            lable = lable + topicNameTvArray.get(i) + " · ";
                        } else {
                            lable = lable + topicNameTvArray.get(i);
                        }
                    }
                    pickedProductLableTv.setText(lable);
                }
            }
            if ("yes".equals(product.getIsFav())) {
                storeImg.setImageResource(R.drawable.yes_store);
            } else {
                storeImg.setImageResource(R.drawable.no_store);
            }
            storeImg.setTag(R.id.tag_first, product.getId());
            storeImg.setTag(R.id.tag_third, position);
            storeImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String productId = (String) v.getTag(R.id.tag_first);
                    boolean falg;
                    if ("yes".equals(product.getIsFav())) {
                        falg = true;
                    } else {
                        falg = false;

                    }
                    int position = (Integer) v.getTag(R.id.tag_third);

                    if (falg) {
                        ToastUtil.showToast(getActivity(), "收藏已取消");
                        ((ImageView) v).setImageResource(R.drawable.no_store);
                    } else {
                        ToastUtil.showToast(getActivity(), "已收藏");
                        ((ImageView) v).setImageResource(R.drawable.yes_store);
                    }
                    listener.addOrCancelStored(productId, falg, position);
                }
            });
            pickedProductLayoutContainer.setTag(mapItem);
            pickedProductLayoutContainer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Map<String, Object> mapItem = (Map<String, Object>) v.getTag();
                    Intent intentForProductDetail = null;
                    ProductNewInfo productNewInfo = (ProductNewInfo) mapItem.get("objectValue");
                    // 单产品
                    if (productNewInfo.getProductType().equals("base")
                            || productNewInfo.getProductType().equals("customization")) {
                        intentForProductDetail = new Intent(getActivity(), GrouponProductNewDetailActivity.class);
                        intentForProductDetail.putExtra("productId", productNewInfo.getId());
                        // intentForProductDetail.putExtra("product",
                        // productNewInfo);
                    } else if (productNewInfo.getProductType().equals("single")
                            && (((ProductNewInfo) mapItem.get("objectValue")).getServices().equals("traffic"))) {
                        intentForProductDetail = new Intent(getActivity(), CarProductDetailActivity.class);
                        intentForProductDetail.putExtra("productId", productNewInfo.getId());
                        // intentForProductDetail.putExtra("product",
                        // productNewInfo);
                    } else if (productNewInfo.getProductType().equals("single")
                            && (((ProductNewInfo) mapItem.get("objectValue")).getServices().equals("stay"))) {
                        intentForProductDetail = new Intent(getActivity(), HouseProductDetailActivity.class);
                        intentForProductDetail.putExtra("productId", productNewInfo.getId());
                    }
                    // 团产品
                    else if (productNewInfo.getProductType().equals("team")) {

                        intentForProductDetail = new Intent(getActivity(), GrouponProductNewDetailActivity.class);
                        intentForProductDetail.putExtra("productId", productNewInfo.getId());
                        // intentForProductDetail.putExtra("product",
                        // productNewInfo);
                    } else {
                        intentForProductDetail = new Intent(getActivity(), CarProductDetailActivity.class);
                        intentForProductDetail.putExtra("productId", productNewInfo.getId());
                        // intentForProductDetail.putExtra("product",
                        // productNewInfo);
                    }
                    intentForProductDetail.putExtra("productTitle", productNewInfo.getTitle());
                    getActivity().startActivity(intentForProductDetail);
                }
            });

        } else if (((String) mapItem.get("pickedItemType")).equals("user")) // 服务者
        {
            pickedTag1.setVisibility(View.GONE);
            pickedTag2.setVisibility(View.GONE);
            pickedTag3.setVisibility(View.GONE);
            pickedProductLayoutContainer.setVisibility(View.GONE);
            pickedServicerLayoutContainer.setVisibility(View.VISIBLE);
            pickedProductThemeLayoutContainer.setVisibility(View.GONE);
            if (!StringUtils.isEmpty((String) mapItem.get("pickedItemImg"))) {
                ImageLoader.getInstance().displayImage(Urls.imgHost + (String) mapItem.get("pickedItemImg"),
                        pickedServicerIv, AppConfig.options(R.drawable.ic_launcher));// 服务者
            }

            pickedServicerHeadIconIv.setImageResource(R.drawable.user_defult_photo);
            if (!StringUtils.isEmpty(((UserInfo) mapItem.get("objectValue")).getAvatar())) {
                ImageLoader.getInstance().displayImage(
                        Urls.imgHost + (String) (((UserInfo) mapItem.get("objectValue")).getAvatar()),
                        pickedServicerHeadIconIv, AppConfig.options(R.drawable.user_defult_photo));// 服务者圆形头像
            }

            pickedServicerNameTv.setText((String) mapItem.get("pickedItemTitle"));// 服务者姓名
            // pickedServicerBriefTv.setText(((UserInfo)mapItem.get("objectValue")).getIntroduction());
            // 改成显示服务标签
            UserInfo userInfo = (UserInfo) mapItem.get("objectValue");
            Tags tagsArray = userInfo.getTag();
            if (tagsArray != null) {
                List<String> list = new ArrayList<String>();
                if (tagsArray.getInterests() != null && tagsArray.getInterests().size() > 0) {
                    for (int i = 0; i < tagsArray.getInterests().size(); i++) {
                        if (list.contains(tagsArray.getInterests().get(i))) {
                            continue;
                        } else {
                            list.add(tagsArray.getInterests().get(i));
                        }
                    }
                }
                if (tagsArray.getSpheres() != null) {
                    for (int i = 0; i < tagsArray.getSpheres().size(); i++) {
                        if (list.contains(tagsArray.getSpheres().get(i))) {
                            continue;
                        } else {
                            list.add(tagsArray.getSpheres().get(i));
                        }
                    }
                }
                if (tagsArray.getFootprints() != null) {
                    for (int i = 0; i < 2; i++) {

                        try {
                            if (list.contains(tagsArray.getFootprints().get(i))) {
                                continue;
                            } else {
                                list.add(tagsArray.getFootprints().get(i));
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                if (tagsArray.getClubTypes() != null) {
                    for (int i = 0; i < 2; i++) {
                        try {
                            if (list.contains(tagsArray.getClubTypes().get(i))) {
                                continue;
                            } else {
                                list.add(tagsArray.getClubTypes().get(i));
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                if (tagsArray.getServAreas() != null) {
                    for (int i = 0; i < 2; i++) {
                        try {
                            if (list.contains(tagsArray.getServAreas().get(i))) {
                                continue;
                            } else {
                                list.add(tagsArray.getServAreas().get(i));
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                String lable = "";
                for (int i = 0; i < list.size(); i++) {
                    if (i != list.size() - 1) {
                        lable += list.get(i) + " · ";
                    } else {
                        lable += list.get(i);
                    }
                }
                pickedServicerBriefTv.setText(lable);
            }
            pickedServicerLayoutContainer.setTag(((UserInfo) mapItem.get("objectValue")));

            pickedServicerLayoutContainer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    UserInfo userInfo = (UserInfo) v.getTag();
                    OrgRole orgRole = userInfo.getOrgRole();

                    if (orgRole != null) {
                        if ("admin".equals(orgRole.getRoleCode())) {
                            Intent intent = new Intent(getActivity(), ClubFirstPageActivity.class);
                            intent.putExtra("userInfo", userInfo);
                            getActivity().startActivity(intent);
                            return;
                        }
                        if ("leader".equals(orgRole.getRoleCode()) || "guide".equals(orgRole.getRoleCode())) {
                            Intent intentForUserInfo = new Intent(getActivity(), ClubMebHomepageActivity.class);
                            intentForUserInfo.putExtra("isDriverHomePage", false);
                            intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
                            intentForUserInfo.putExtra("userInfo", userInfo);
                            getActivity().startActivity(intentForUserInfo);
                            return;

                        } else if ("driver".equals(orgRole.getRoleCode())) {
                            Intent intentForUserInfo = new Intent(getActivity(), ClubMebHomepageActivity.class);
                            intentForUserInfo.putExtra("isDriverHomePage", true);
                            intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
                            intentForUserInfo.putExtra("userInfo", userInfo);
                            getActivity().startActivity(intentForUserInfo);
                            return;
                        }
                    }
                    // if (orgRole != null) {
                    // if (orgRole.getRoleCode().equals("driver"))
                    String profession = userInfo.getPermission();
                    Intent intentForUserInfo = new Intent(getActivity(), PersonalInfoNewActivity.class);
                    if (profession != null && profession.contains("专业司机")) {
                        intentForUserInfo = new Intent(getActivity(), ClubMebHomepageActivity.class);
                        intentForUserInfo.putExtra("isDriverHomePage", true);
                    } else if (profession != null && (profession.contains("资深领队") || profession.contains("达人导游")))

                    {
                        intentForUserInfo = new Intent(getActivity(), ClubMebHomepageActivity.class);
                        intentForUserInfo.putExtra("isDriverHomePage", false);
                    }

                    // }

                    intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
                    intentForUserInfo.putExtra("userInfo", userInfo);
                    getActivity().startActivity(intentForUserInfo);
                }
            });

        } else if (((String) mapItem.get("pickedItemType")).equals("column")
                || ((String) mapItem.get("pickedItemType")).equals("topic")) // 精选主题
        {
            pickedTag1.setVisibility(View.GONE);
            pickedTag2.setVisibility(View.GONE);
            pickedTag3.setVisibility(View.GONE);
            pickedProductLayoutContainer.setVisibility(View.GONE);
            pickedServicerLayoutContainer.setVisibility(View.GONE);
            pickedProductThemeLayoutContainer.setVisibility(View.VISIBLE);
            if (!StringUtils.isEmpty(((TopicInfo) mapItem.get("pickedThemeItem1")).getBackground())) {
                ImageLoader.getInstance().displayImage(
                        Urls.imgHost + ((TopicInfo) mapItem.get("pickedThemeItem1")).getBackground(),
                        pickedProductThemeIv1);
            }
            // if (null != (TopicInfo)mapItem.get("pickedThemeItem2")
            // &&!StringUtils.isEmpty(((TopicInfo)mapItem.get("pickedThemeItem2")).getBackground()))
            // {
            // ImageLoader.getInstance().displayImage(Urls.imgHost
            // + ((TopicInfo)mapItem.get("pickedThemeItem2")).getBackground(),
            // pickedProductThemeIv2);
            // }

            themeProductLayout1.setTag(((TopicInfo) mapItem.get("pickedThemeItem1")).getId());
            // if(null != (TopicInfo)mapItem.get("pickedThemeItem2")){
            // themeProductLayout2.setTag(((TopicInfo)mapItem.get("pickedThemeItem2")).getId());
            // themeProductLayout2.setOnClickListener(new
            // OnClickListener()
            // {
            //
            // @Override
            // public void onClick(View v)
            // {
            // // TODO Auto-generated method stub
            // String themeId = (String)v.getTag();
            // Intent intentForTopicProductList = new Intent(getActivity(),
            // ThemeProductListActivity.class);
            // intentForTopicProductList.putExtra("themeId", themeId);
            // getActivity().startActivity(intentForTopicProductList);
            // }
            // });
            // }
            pickedProductThemeNameTv1.setText(((TopicInfo) mapItem.get("pickedThemeItem1")).getTitle());
            // if((TopicInfo)mapItem.get("pickedThemeItem2")!=
            // null&&!StringUtils.isEmpty(((TopicInfo)mapItem.get("pickedThemeItem2")).getTitle())){
            // pickedProductThemeNameTv2.setText(((TopicInfo)mapItem.get("pickedThemeItem2")).getTitle());
            // }else{
            // pickedProductThemeNameTv2.setText("");
            // }
            themeProductLayout1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String themeId = (String) v.getTag();
                    Intent intentForTopicProductList = new Intent(getActivity(), ThemeProductListActivity.class);
                    intentForTopicProductList.putExtra("themeId", themeId);
                    getActivity().startActivity(intentForTopicProductList);
                }
            });
        } /*
             * else if (((String) mapItem.get("pickedItemType")).equals(
			 * "team")){ pickedProductUnit.setVisibility(View.VISIBLE);
			 * pickedProductGroupLayout.setVisibility(View.VISIBLE); }
			 */

        // convertView.setTag(R.id.tag_picked_first, holder);
        // convertView.setTag(R.id.tag_picked_second,
        // (String) mapItem.get("pickedItemType"));
        // convertView.setTag(R.id.tag_picked_thrid, mapItem);
        return convertView;
    }

    private View getThemeView(int position, List<PushResource> themeList) {
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        PushResource themeItem = themeList.get(position);
        View convertView = inflator.inflate(R.layout.new_item_discovery_theme_list, null);
        ImageView themeIconIv = (ImageView) convertView.findViewById(R.id.item_discovery_theme_iv);
        TextView themeNameTv = (TextView) convertView.findViewById(R.id.item_discovery_theme_tv);
        themeIconIv.setImageResource(R.drawable.default_photo);
        themeIconIv.getLayoutParams().width = themeOrDestinationImageW;
        if (!StringUtils.isEmpty(themeItem.getTitleImage())) {
            // BitmapUtils bit = new BitmapUtils(context);
            // bit.configDefaultLoadingImage(R.drawable.default_photo);
            // bit.display(themeIconIv, Urls.imgHost +
            // themeItem.getTitleImage());
            ImageLoader.getInstance().displayImage(Urls.imgHost + themeItem.getTitleImage(), themeIconIv,
                    AppConfig.options(R.drawable.default_photo));
            // ,new ImageLoadingListener() {
            //
            // @Override
            // public void onLoadingStarted(String imageUri, View view) {
            // // TODO Auto-generated method stub
            //
            // }
            //
            // @Override
            // public void onLoadingFailed(String imageUri, View view,
            // FailReason failReason) {
            // // TODO Auto-generated method stub
            //
            // }
            //
            // @Override
            // public void onLoadingComplete(String imageUri, View view, Bitmap
            // loadedImage) {
            // // TODO Auto-generated method stub
            // float
            // picRatio=loadedImage.getHeight()/(float)loadedImage.getWidth();
            // if(Math.abs(picRatio-imageViewRatio)<0.2){
            // ((ImageView)view).setScaleType(ScaleType.FIT_XY);
            // }else {
            // ((ImageView)view).setScaleType(ScaleType.CENTER_CROP);
            // }
            //// ((ImageView)view).setImageBitmap(loadedImage);
            // }
            //
            // @Override
            // public void onLoadingCancelled(String imageUri, View view) {
            // // TODO Auto-generated method stub
            //
            // }
            // });
        }
        if (themeItem.getObjectType().equals("topic")) {
            themeNameTv.setText(themeItem.getResTitle());
            //// subjectIcon.setVisibility(View.GONE);
            // themeNameTv.setGravity(Gravity.LEFT);
            //// themeDescTv.setGravity(Gravity.LEFT);
        } else if (themeItem.getObjectType().equals("subject")) {
            themeNameTv.setText(themeItem.getResTitle());
            // themeNameTv.setGravity(Gravity.CENTER);
            // themeDescTv.setGravity(Gravity.CENTER);
            // subjectIcon.setVisibility(View.VISIBLE);
        }
        convertView.setTag(position);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int position = (Integer) v.getTag();
                PushResource pushTopicResource = topicsList.get(position);
                ActivityUtil.toDetailPage(pushTopicResource, getActivity(), false);
            }
        });
        return convertView;
    }

    private View getDestinationsView(int position, List<PushResource> locationList) {
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        PushResource locationItem = (PushResource) locationList.get(position);
        View convertView = inflator.inflate(R.layout.item_discovery_locations, null);
        ImageView locationFlagIv = (ImageView) convertView.findViewById(R.id.item_location_flag_iv);
        TextView locationenNameTv = (TextView) convertView.findViewById(R.id.item_location_enUSname_tv);
        TextView locationzhNameTv = (TextView) convertView.findViewById(R.id.item_location_zhCNname_tv);
        TextView locationKeywordsTv = (TextView) convertView.findViewById(R.id.item_location_keywords_tv);
        ImageView subjectIcon = (ImageView) convertView.findViewById(R.id.subject_icon);
        locationFlagIv.getLayoutParams().width = themeOrDestinationImageW;
        if (!locationItem.getSubTitle().equals("null") || locationItem.getSubTitle() != null
                || locationItem.getSubTitle().equals("")) {
            if (!StringUtils.isEmpty(locationItem.getTitleImage())) {
                ImageLoader.getInstance().displayImage(Urls.imgHost + locationItem.getTitleImage(), locationFlagIv,
                        AppConfig.options(R.drawable.default_photo));
            }

            locationenNameTv.setText(locationItem.getSubTitle());
        }
        locationzhNameTv.setText(locationItem.getResTitle());
        if (locationItem.getKeywords() != null && !locationItem.getKeywords().equals("null")
                && !locationItem.getKeywords().equals("")) {
            String keywords = locationItem.getKeywords().toString();
            if (keywords.contains(",")) {
                keywords = keywords.replace(",", "·");

            } else if (keywords.contains("，")) {
                keywords = keywords.replace("，", "·");
            } else if (keywords.contains("·")) {
                keywords = keywords.replace("·", " · ");
            }
            locationKeywordsTv.setText(keywords);
        }
        if (locationItem.getObjectType().equals("destination")) {
            locationzhNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            subjectIcon.setVisibility(View.GONE);
            locationenNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            locationKeywordsTv.setVisibility(View.VISIBLE);
            locationKeywordsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        } else if (locationItem.getObjectType().equals("subject")) {
            locationzhNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            locationKeywordsTv.setVisibility(View.GONE);
            locationenNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            subjectIcon.setVisibility(View.VISIBLE);
            locationKeywordsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        }
        convertView.setTag(position);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int position = (Integer) v.getTag();
                PushResource pushLocateResource = locationsList.get(position);
                if (pushLocateResource.getObjectType().equals("destination")) {
                    String destId = pushLocateResource.getObjectId();// 目的地标识
                    Intent intentForLocateCountry = new Intent(getActivity(), LocationCountryDetailActivity.class);
                    intentForLocateCountry.putExtra("destId", destId);
                    intentForLocateCountry.putExtra("destinationTitle", pushLocateResource.getResTitle());
                    startActivity(intentForLocateCountry);
                } else if (pushLocateResource.getObjectType().equals("subject")) {
                    String entityId = pushLocateResource.getObjectId();
                    Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("subjectTitle", pushLocateResource.getResTitle());
                    getActivity().startActivity(intent);
                } else if (pushLocateResource.getObjectType().equals("user")) {
                    UserInfo userInfo = (UserInfo) pushLocateResource.getObject();
                    ActivityUtil.toPersonHomePage(userInfo, getActivity());
                }
            }
        });
        return convertView;

    }

    private void setPushCarouselData(JSONObject response) {
        if (response == null || response.length() <= 0)
            return;
        JSONArray dataArray = response.optJSONArray("data");
        List<PushResource> pushResourceList = new ArrayList<PushResource>();
        if (dataArray != null && dataArray.length() > 0) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataJson = dataArray.optJSONObject(i);
                PushResource pushResource = new PushResource();
                pushResource.setId(dataJson.optString("id"));
                pushResource.setResTitle(dataJson.optString("resTitle"));
                pushResource.setSubTitle(dataJson.optString("subTitle"));
                pushResource.setTitleImage(dataJson.optString("titleImage"));
                pushResource.setObjectType(dataJson.optString("objectType"));
                pushResource.setObjectId(dataJson.optString("objectId"));
                pushResource.setObject(dataJson.optString("object"));
                pushResource.setDescription(dataJson.optString("description"));
                pushResourceList.add(pushResource);
            }

            if (pushResourceList.size() > 0) {
                viewPager = (ViewPager) pickcarouselview.findViewById(R.id.carousel_vp);
                dotsLayout = (LinearLayout) pickcarouselview.findViewById(R.id.carousel_dots);
                addCarousel(pushResourceList);
            }
        }
    }

    // 本周推荐
    private void setFindWeekData(JSONObject response) {
        if (response == null || response.length() <= 0)
            return;
        JSONArray dataArray = response.optJSONArray("data");
        pushResourceList = new ArrayList<PushResource>();
        if (dataArray != null && dataArray.length() > 0) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataJson = dataArray.optJSONObject(i);
                PushResource pushResource = new PushResource();
                pushResource.setId(dataJson.optString("id"));
                pushResource.setDescription(dataJson.optString("description"));
                pushResource.setResTitle(dataJson.optString("resTitle"));
                pushResource.setSubTitle(dataJson.optString("subTitle"));
                pushResource.setTitleImage(dataJson.optString("titleImage"));
                if (i == 0) {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + dataJson.optString("titleImage"), image1,
                            AppConfig.options(R.drawable.ic_launcher));
                    image1.setTag(pushResource);
                } else if (i == 1) {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + dataJson.optString("titleImage"), image2,
                            AppConfig.options(R.drawable.ic_launcher));
                    image2.setTag(pushResource);
                } else if (i == 2) {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + dataJson.optString("titleImage"), image3,
                            AppConfig.options(R.drawable.ic_launcher));
                    image3.setTag(pushResource);
                } else if (i == 3) {
                    ImageLoader.getInstance().displayImage(Urls.imgHost + dataJson.optString("titleImage"), image4,
                            AppConfig.options(R.drawable.ic_launcher));
                    image4.setTag(pushResource);
                }
                pushResource.setObjectType(dataJson.optString("objectType"));
                pushResource.setObjectId(dataJson.optString("objectId"));
                pushResource.setObject(dataJson.optString("object"));
                JSONObject jSONObject = dataJson.optJSONObject("object");
                if (jSONObject != null && jSONObject.length() > 0) {
                    if ("product".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(JsonUtil.getProductNewInfo(jSONObject));
                    } else if ("topic".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(JsonUtil.getTopicInfo(jSONObject));
                    } else if ("user".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(JsonUtil.getUserInfo(jSONObject));
                    } else if ("destination".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(JsonUtil.getDestinationInfo(jSONObject));
                    } else if ("softtext".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));
                    } else if ("story".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(JsonUtil.getArticleInfo(jSONObject));
                    } else if ("subject".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(JsonUtil.getSubjectInfo(jSONObject));
                    } else if ("link".equals(dataJson.optString("objectType"))) {
                        pushResource.setObject(jSONObject.toString());
                    }
                }
                pushResourceList.add(pushResource);
            }
            if (pushResourceList != null) {
                if (pushResourceList.size() == 0) {
                    image1Layout.setVisibility(View.GONE);
                } else if (pushResourceList.size() == 1) {
                    image1Layout.setVisibility(View.VISIBLE);
                    image2Layout.setVisibility(View.GONE);
                } else if (pushResourceList.size() == 2) {
                    image1Layout.setVisibility(View.VISIBLE);
                    image2Layout.setVisibility(View.VISIBLE);
                    image3Layout.setVisibility(View.GONE);
                } else if (pushResourceList.size() == 3) {
                    image1Layout.setVisibility(View.VISIBLE);
                    image2Layout.setVisibility(View.VISIBLE);
                    image3Layout.setVisibility(View.VISIBLE);
                    image4.setVisibility(View.GONE);
                } else if (pushResourceList.size() == 4) {
                    image1Layout.setVisibility(View.VISIBLE);
                    image2Layout.setVisibility(View.VISIBLE);
                    image3Layout.setVisibility(View.VISIBLE);
                    image4.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    // 精选主题
    private void setPickTitleData(JSONObject response) {
        if (response == null || response.length() <= 0)
            return;
        getTopicsAndPeriodLists(response);
    }

    // 远行精选
    private void setPickedData(JSONObject response) {
        if (response == null || response.length() <= 0)
            return;
        initPickedItemList(response);
    }

    // 目的地
    private void setLocationData(JSONObject response) {
        if (response == null || response.length() <= 0)
            return;
        getHotLocationsList(response);
    }

    // 人物志
    private void setOrgUrlData(JSONObject response) {
        if (response == null || response.length() <= 0)
            return;
        getHotOrgsList(response);
    }
}
