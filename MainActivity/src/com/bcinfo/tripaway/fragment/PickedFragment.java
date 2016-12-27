package com.bcinfo.tripaway.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyAdsPagerAdapter;
import com.bcinfo.tripaway.adapter.PickListAdapter;
import com.bcinfo.tripaway.adapter.PickListAdapter.OperationListener;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
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

/**
 * 旅游产品精选fragment
 *
 * @author caihelin
 * @version 1.0 2014年12月17日 14:11:22
 * @function
 */
public class PickedFragment extends BaseFragment {
    /**
     * android-support-v4中的滑动组件
     */
    private ViewPager viewPager;

    /**
     * 滑动的图片集合
     */
    private List<View> imageViews;

    /**
     * 图片数量
     */
    private int count;

    /**
     * 点的父布局
     */
    private LinearLayout dotsLayout;

    /**
     * 图片标题正文的那些点
     */
    private List<View> dots;

    private View headerView;

    /**
     * 当前图片的索引号
     */
    private int currentItem = 0;

    /**
     * An ExecutorService that can schedule commands to run after a given delay,
     * or to execute periodically.
     */
    // private ScheduledExecutorService scheduledExecutorService;

    /**
     * 精选数据的适配器
     */
    private PickListAdapter pickedAdapter;

    /**
     * 显示 精选结果数据的listview
     */
    private XListView pickedListView;

    /**
     * 定义 存放 精选数据的list集合
     */
    private List<Map<String, Object>> pickedItemList = new ArrayList<Map<String, Object>>();

    private TwinklingRefreshLayout refreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        statisticsTitle = "精选";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tripPickedView = inflater.inflate(R.layout.trip_items_picked, container, false);
        // 获得listview
        pickedListView = (XListView) tripPickedView.findViewById(R.id.main_picked_listview);

        pickedListView.setPullRefreshEnable(true);
        pickedListView.setPullLoadEnable(false);
        pickedAdapter = new PickListAdapter(pickedItemList, this.getActivity(), new OperationListener() {
            @Override
            public void addOrCancelStored(String productId, boolean flag, int position) {
                if (!AppInfo.getIsLogin()) {
                    ToastUtil.showToast(getActivity(), "未登录不能收藏");
                    return;
                }
                // int firstPosition = pickedListView.getFirstVisiblePosition();
                // int lastPosition = pickedListView.getLastVisiblePosition();
                // if(position >= firstPosition&&position<=lastPosition){
                // View view = pickedListView.getChildAt(position -
                // firstPosition);
                // ImageView img =(ImageView) view.findViewById(R.id.if_stored);
                // if(flag){
                // img.setImageResource(R.drawable.no_store);
                // }else{
                // img.setImageResource(R.drawable.yes_store);
                // }
                // }
                if (pickedItemList != null && pickedItemList.size() > 0) {
                    if (flag) {
                        ((ProductNewInfo) pickedItemList.get(position).get("objectValue")).setIsFav("no");
                    } else {
                        ((ProductNewInfo) pickedItemList.get(position).get("objectValue")).setIsFav("yes");
                    }
                }
                storeProductByIsFaved(flag, productId);
                pickedAdapter.notifyDataSetChanged();

            }
        });
        // 设置listview的适配器
        pickedListView.setAdapter(pickedAdapter);
        testPickedUrl();
//		隐藏头部的轮播图
//		getPushCarousel();
        registerBoradcastReceiver();
        refreshLayout = (TwinklingRefreshLayout) tripPickedView.findViewById(R.id.refreshLayout);
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
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pickedListView != null) {
                            pickedListView.removeHeaderView(headerView);
                        }
                        testPickedUrl();
//		getPushCarousel();
                    }
                }, 2000);
            }


            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加载操作
                    }
                }, 2000);
            }
        });
        return tripPickedView;
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
                    refreshLayout.finishRefreshing();
                    System.out.println("精选推荐接口=" + response);
                    String code = response.optString("code");
                    if (code.equals("00000")) {
                        pickedItemList.clear();
                        initPickedItemList(response);
                        pickedListView.successRefresh();
                    } else {
                        pickedListView.stopRefresh();
                        // ToastUtil.showToast(getActivity(), "errorCode=" +
                        // code);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                pickedListView.stopRefresh();
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
                // JSONObject topicAnotherObj = arrayObj.optJSONObject(++i);
                // if(null != topicAnotherObj){
                // TopicInfo topicItem2 = new TopicInfo();
                //
                // topicItem2.setId(topicAnotherObj.optString("objectId"));
                // topicItem2.setTitle(topicAnotherObj.optString("resTitle"));
                //
                // topicItem2.setBackground(topicAnotherObj.optString("titleImage"));
                // mapItem.put("pickedThemeItem2", topicItem2);
                // }else{
                // mapItem.put("pickedThemeItem2",null);
                // }

                pickedItemList.add(mapItem);

            }
        }
        pickedAdapter.notifyDataSetChanged();

    }

    private void addHeaderView() {
        // 将xml布局文件生成view对象通过LayoutInflater
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 将view对象挂载到那个父元素上，这里没有就为null
        if (headerView == null) {
            headerView = inflater.inflate(R.layout.pick_carousel_view, null);
        }
        viewPager = (ViewPager) headerView.findViewById(R.id.carousel_vp);
        dotsLayout = (LinearLayout) headerView.findViewById(R.id.carousel_dots);
        pickedListView.addHeaderView(headerView);
    }

    private void addCarousel(List<PushResource> pushResourceList) {
        // count = tempList.size();
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
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
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
                if (code.equals("00000")) {
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
                            addHeaderView();
                            addCarousel(pushResourceList);
                        }
                    }
                }
            }
        });
    }

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

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.bcinfo.pickListRefresh");
        // 注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.bcinfo.pickListRefresh")) {
                boolean flag = intent.getBooleanExtra("flag", false);
                String productId = intent.getStringExtra("productId");
                if (productId != null) {
                    if (flag) {
                        for (Map<String, Object> mapItem : pickedItemList) {
                            if (((String) mapItem.get("pickedItemType"))
                                    .equals("product")) {
                                ProductNewInfo productNewInfo = (ProductNewInfo) mapItem
                                        .get("objectValue");
                                if (productNewInfo.getId().equals(productId)
                                        && productNewInfo.getIsFav().equals(
                                        "no")) {
                                    productNewInfo.setIsFav("yes");
                                    pickedAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Map<String, Object> mapItem : pickedItemList) {
                            if (((String) mapItem.get("pickedItemType"))
                                    .equals("product")) {
                                ProductNewInfo productNewInfo = (ProductNewInfo) mapItem
                                        .get("objectValue");
                                if (productNewInfo.getId().equals(productId)
                                        && productNewInfo.getIsFav().equals(
                                        "yes")) {
                                    productNewInfo.setIsFav("no");
                                    pickedAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        }
    };

}
