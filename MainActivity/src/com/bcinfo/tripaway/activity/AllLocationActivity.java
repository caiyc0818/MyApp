package com.bcinfo.tripaway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.NewProductPushDestinationsGridAdapter;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.NewSinaRefreshView;
import com.bcinfo.tripaway.view.refreshandload.PullableGridView;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllLocationActivity extends BaseActivity implements
        OnItemClickListener {

    // GridView 显示全部目的地
    private PullableGridView allGridView;


    // list集合
    private ArrayList<PushResource> locationsList = new ArrayList<PushResource>();

    // 全屏显示的半透明背景
    private int pageNum = 1;

    private int pageSize = 10;

    private boolean isPullRefresh = false;

    private boolean isLoadMore = false;

    private TextView responsetv;

    private NewProductPushDestinationsGridAdapter pLocationsAdapter;


    private TwinklingRefreshLayout refreshLayout;
    private NewSinaRefreshView headerView;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.location_all);
        this.discoverTitle = (TextView) findViewById(R.id.discovery_title_text);

        // 目的地
        discoverTitle.setText("全部目的地");
        // responsetv=(TextView) findViewById(R.id.responsetv);
        discoverLayout = (LinearLayout) findViewById(R.id.discovery_discover_container);
        // 标题栏右边的放大镜
        discoverThemeIcon = (ImageView) findViewById(R.id.discovery_discover_button);
        discoverLayout.setVisibility(View.GONE);
        navBack = (ImageView) findViewById(R.id.discovery_back_button);
        navBack.setOnClickListener(mOnClickListener);
        ((View) (navBack.getParent())).getBackground().setAlpha(255);
        discoverThemeIcon.setOnClickListener(mOnClickListener);
        discoverThemeIcon = (ImageView) findViewById(R.id.discovery_discover_button);

        allGridView = (PullableGridView) findViewById(R.id.discovery_locations_gridView);

        pLocationsAdapter = new NewProductPushDestinationsGridAdapter(
                AllLocationActivity.this, locationsList);
        allGridView.setAdapter(pLocationsAdapter);
        allGridView.setOnItemClickListener(AllLocationActivity.this);
        getAllLocationUrl();
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.startRefresh();
//        ProgressLayout headerView = new ProgressLayout(this);
////        BezierLayout headerView = new BezierLayout(this);
//        refreshLayout.setHeaderView(headerView);
        headerView = new NewSinaRefreshView(this);
        headerView.setArrowResource(R.drawable.arrow);
        headerView.setTextColor(0xff745D5C);
//        TextHeaderView headerView = (TextHeaderView) View.inflate(this,R.layout.header_tv,null);
        refreshLayout.setHeaderView(headerView);

        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);

//        refreshLayout.setFloatRefresh(false);
        refreshLayout.setPureScrollModeOn(false);
//        refreshLayout.setEnableOverlayRefreshView(false);
        refreshLayout.setAutoLoadMore(true);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        locationsList.clear();
                        isPullRefresh = true;
                        pageNum = 1;
                        getAllLocationUrl();
                    }
                }, 2000);
            }



            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadMore = true;
                        getAllLocationUrl();
                    }
                }, 2000);
            }
        });
    }

    private void getAllLocationUrl() {
        RequestParams params = new RequestParams();
        params.put("pagesize", 10);
        params.put("pagenum", pageNum);
        params.put("mcode", "app.dest.dest");
        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("", "全部目的地接口=", "responseString=" + response);
                getLocationList(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable,
                        errorResponse);
                // ToastUtil.showToast(DiscoverAllLocationActivity.this,
                // "statusCode=" + statusCode);
                if (isLoadMore) {
                    refreshLayout.finishLoadmore();
                }
                if (isPullRefresh) {
                    refreshLayout.finishRefreshing();
                }
                isLoadMore = false;
                isPullRefresh = false;
            }
        });
    }

    private void getLocationList(JSONObject response) {
        String code = response.optString("code");
        refreshLayout.finishRefreshing();
        if ("00000".equals(code)) {
            if (isLoadMore) {
                // 上拉返回的结束加载更多布局
                refreshLayout.finishLoadmore();
            }
            if (isPullRefresh) {
                // 下拉刷新返回的
                refreshLayout.finishRefreshing();
            }
            JSONArray jsonDestArray = response.optJSONArray("data");
            if (jsonDestArray == null || jsonDestArray.length() == 0) {
                return;
            }
            ArrayList<PushResource> tempLocationList = new ArrayList<>();
            for (int i = 0; i < jsonDestArray.length(); i++) {
                JSONObject jsonObj = jsonDestArray.optJSONObject(i);
                PushResource pushResource = new PushResource();
                pushResource.setId(jsonObj.optString("id"));
                pushResource.setResTitle(jsonObj.optString("resTitle"));
                pushResource.setObjectType(jsonObj.optString("objectType"));
                pushResource.setObjectId(jsonObj.optString("objectId"));
                if (jsonObj.optString("keywords").contains("，")) {
                    pushResource.setKeywords(jsonObj.optString("keywords")
                            .replace("，", "·"));
                } else {
                    pushResource.setKeywords(jsonObj.optString("keywords"));
                }
                pushResource.setTitleImage(jsonObj.optString("titleImage"));
                pushResource.setSubTitle(jsonObj.optString("subTitle"));
                JSONObject jsonDestObj = jsonObj.optJSONObject("object");
                if ("destination".equals(pushResource.getObjectType())) {
                    if (pushResource.getObject() instanceof TripDestination) {
                        TripDestination tripDestination = (TripDestination) pushResource
                                .getObject();
                        tripDestination.setDestProNum(jsonDestObj
                                .optString("pNum"));
                        tripDestination.setDestId(jsonDestObj.optString("id"));
                        tripDestination.setDestSupNum(jsonDestObj
                                .optString("sNum"));
                        tripDestination.setDestLogoKey(jsonDestObj
                                .optString("logo"));
                        tripDestination.setDestName(jsonDestObj
                                .optString("destName"));
                        tripDestination.setDestName(jsonDestObj
                                .optString("destNameEn"));
                        tripDestination.setDestDescription(jsonDestObj
                                .optString("description"));
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
                    JSONArray professionArray = userObj
                            .optJSONArray("profession");
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
                                profession += ","
                                        + professionArray.optString(j);
                            }
                        }
                    }
                    userInfo.setPermission(profession);
                    JSONObject orgRoleObj = userObj.optJSONObject("orgRole");
                    if (orgRoleObj != null) {
                        OrgRole orgRole = new OrgRole();
                        orgRole.setRoleType(orgRoleObj.optString("roleType"));
                        orgRole.setPermission(orgRoleObj
                                .optString("permission"));
                        orgRole.setRoleCode(orgRoleObj.optString("roleCode"));
                        if (orgRole.getRoleCode().equals("driver")) {
                            System.out.print(orgRole.getRoleCode().equals(
                                    "driver"));
                        }
                        orgRole.setRoleName(orgRoleObj.optString("roleName"));
                        userInfo.setOrgRole(orgRole);
                    }
                    pushResource.setObject(userInfo);
                }
                tempLocationList.add(pushResource);
            }
            if (tempLocationList.size() < 10) {
                allGridView.setPullLoadEnable(false);
                refreshLayout.setEnableLoadmore(false);
            } else {
                pageNum++;
                refreshLayout.setEnableLoadmore(true);
                allGridView.setPullLoadEnable(true);
            }
            locationsList.addAll(tempLocationList);
            pLocationsAdapter.notifyDataSetChanged();
        } else {
            // ToastUtil.showToast(DiscoverAllLocationActivity.this,
            // "errorCode=" + code);
            if (isLoadMore) {
                // 上拉返回的结束加载更多布局
                refreshLayout.finishLoadmore();
            }
            if (isPullRefresh) {
                // 下拉刷新返回的
                refreshLayout.finishRefreshing();
            }
            if (pageNum != 1) {
                pageNum--;
            }
        }
        isLoadMore = false;
        isPullRefresh = false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (parent.getId()) {
            case R.id.discovery_locations_gridView: // 目的地
                PushResource pushLocateResource = locationsList.get(position);
                if (pushLocateResource.getObjectType().equals("destination")) {
                    String destId = pushLocateResource.getObjectId();// 目的地标识
                    Intent intentForLocateCountry = new Intent(
                            AllLocationActivity.this,
                            LocationCountryDetailActivity.class);
                    intentForLocateCountry.putExtra("destId", destId);
                    intentForLocateCountry.putExtra("destinationTitle",
                            pushLocateResource.getResTitle());
                    startActivity(intentForLocateCountry);
                } else if (pushLocateResource.getObjectType().equals("subject")) {
                    String entityId = pushLocateResource.getObjectId();
                    Intent intent = new Intent(AllLocationActivity.this,
                            SubjectDetailActivity.class);
                    intent.putExtra("entityId", entityId);
                    intent.putExtra("subjectTitle",
                            pushLocateResource.getResTitle());
                    AllLocationActivity.this.startActivity(intent);
                } else if (pushLocateResource.getObjectType().equals("user")) {
                    UserInfo userInfo = (UserInfo) pushLocateResource.getObject();
                    ActivityUtil.toPersonHomePage(userInfo,
                            AllLocationActivity.this);
                }
                break;
            default:
                break;
        }
        activityAnimationOpen();
    }


}
