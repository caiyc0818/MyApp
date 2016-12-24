package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductPushDestinationsGridAdapter;
import com.bcinfo.tripaway.adapter.SubjectGridAdapter;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.NewSinaRefreshView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.bcinfo.tripaway.view.refreshandload.PullableGridView;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout.OnRefreshListener;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AllPersonActivity extends BaseActivity implements
        OnItemClickListener {

    // GridView 显示全部人物志
    private PullableGridView orgGridView;


    private ArrayList<PushResource> orgList = new ArrayList<PushResource>();
    private ArrayList<PushResource> orgListUser = new ArrayList<PushResource>();

    // 全屏显示的半透明背景
    private int pageNum = 1;

    private int pageSize = 10;

    private boolean isPullRefresh = false;

    private boolean isLoadMore = false;

    private TextView responsetv;

    private SubjectGridAdapter mOrgGridViewAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private NewSinaRefreshView headerView;


    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.location_all);
        this.discoverTitle = (TextView) findViewById(R.id.discovery_title_text);

        // 人物志
        discoverTitle.setText("全部人物志");
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
        orgGridView = (PullableGridView) findViewById(R.id.discovery_locations_gridView);
        orgUrl();
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.startRefresh();
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
                        orgListUser.clear();
                        isPullRefresh = true;
                        pageNum = 1;
                        orgUrl();
                    }
                }, 2000);
            }
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadMore = true;
                        orgUrl();
                    }
                }, 2000);
            }
        });
    }

    /*
     * 合伙人
     */
    private void orgUrl() {
        RequestParams params = new RequestParams();
        params.put("pagesize", pageSize);
        params.put("pagenum", pageNum);
        params.put("mcode", "sale.find.randomsubject");
        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                super.onSuccess(statusCode, headers, response);
                getHotOrgsList(response);
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
     * @param responseString 服务端俱乐部接口返回的数据
     * @return
     */

    private void getHotOrgsList(JSONObject jsonResponse) {
        String code = jsonResponse.optString("code");
        refreshLayout.finishRefreshing();
        orgList.clear();
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
                    pushResource.setKeywords(jsonObj.optString("keywords")
                            .replace("，", "·"));
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
                            pushResource.setObject(JsonUtil
                                    .getProductNewInfo(jSONObject));

                        } else if ("topic".equals(jsonObj
                                .optString("objectType"))) {
                            pushResource.setObject(JsonUtil
                                    .getTopicInfo(jSONObject));
                        } else if ("user".equals(jsonObj
                                .optString("objectType"))) {
                            pushResource.setObject(JsonUtil
                                    .getUserInfo(jSONObject));
                        } else if ("destination".equals(jsonObj
                                .optString("objectType"))) {
                            pushResource.setObject(JsonUtil
                                    .getDestinationInfo(jSONObject));

                        } else if ("softtext".equals(jsonObj
                                .optString("objectType"))) {
                            pushResource.setObject(JsonUtil
                                    .getArticleInfo(jSONObject));

                        } else if ("story".equals(jsonObj
                                .optString("objectType"))) {
                            pushResource.setObject(JsonUtil
                                    .getArticleInfo(jSONObject));
                        } else {
                            JSONArray relatedResourcesJson = jSONObject
                                    .optJSONArray("relatedResources");
                            String objectType = pushResource.getObjectType();
                            if (jSONObject.optString("cover") != null
                                    && "subject".equals(objectType))
                                pushResource.setTitleImage(jSONObject
                                        .optString("cover"));
                            UserInfo userInfo = new UserInfo();
                            if (relatedResourcesJson != null
                                    && relatedResourcesJson.length() > 0) {
                                for (int m = 0; m < relatedResourcesJson
                                        .length(); m++) {
                                    JSONObject relatedResourceJson = relatedResourcesJson
                                            .optJSONObject(m);
                                    if (relatedResourceJson
                                            .optString("objectType") != null
                                            && !"user"
                                            .equals(relatedResourceJson
                                                    .optString("objectType"))) {
                                        JSONObject productObj = relatedResourceJson
                                                .optJSONObject("object");
                                        if (productObj != null
                                                && userInfo.getReferPics()
                                                .size() < 3) {
                                            if (productObj
                                                    .optString("titleImg") != null)
                                                userInfo.getReferPics()
                                                        .add(productObj
                                                                .optString("titleImg"));
                                        }
                                        continue;
                                    }
                                    if (!StringUtils.isEmpty(userInfo
                                            .getUserId()))
                                        continue;
                                    JSONObject objectParamObj = relatedResourceJson
                                            .optJSONObject("objectParams");
                                    if (objectParamObj != null
                                            && objectParamObj.length() > 0) {
                                        pushResource.getObjectParam().put(
                                                "nickname",
                                                objectParamObj
                                                        .optString("nickname"));
                                        pushResource.getObjectParam().put(
                                                "tags",
                                                objectParamObj
                                                        .optString("tags"));
                                        pushResource.getObjectParam().put(
                                                "avartar",
                                                objectParamObj
                                                        .optString("avartar"));
                                    }
                                    JSONObject userObj = relatedResourceJson
                                            .optJSONObject("object");
                                    userInfo.setGender(userObj.optString("sex"));
                                    userInfo.setStatus(userObj
                                            .optString("status"));
                                    userInfo.setAvatar(userObj
                                            .optString("avatar"));
                                    userInfo.setEmail(userObj
                                            .optString("email"));
                                    userInfo.setNickname(userObj
                                            .optString("nickName"));
                                    userInfo.setUserId(userObj
                                            .optString("userId"));
                                    userInfo.setBackground(userObj
                                            .optString("background"));
                                    userInfo.setBrief(userObj
                                            .optString("brief"));
                                    userInfo.setIntroduction(userObj
                                            .optString("introduction"));
                                    userInfo.setMobile(userObj
                                            .optString("mobile"));
                                    JSONArray professionArray = userObj
                                            .optJSONArray("profession");
                                    JSONObject tagObj = userObj
                                            .optJSONObject("tags");
                                    if (null != tagObj) {
                                        Tags tag = new Tags();
                                        JSONArray interest = tagObj
                                                .optJSONArray("interest");
                                        if (interest != null) {
                                            List<String> list = new ArrayList<>();
                                            for (int j = 0; j < interest
                                                    .length(); j++) {
                                                list.add(interest.optString(j));
                                            }
                                            tag.setInterests(list);
                                            userInfo.setTag(tag);
                                        }
                                    }
                                    String profession = "";
                                    if (professionArray != null) {
                                        for (int j = 0; j < professionArray
                                                .length(); j++) {
                                            if (j == 0) {
                                                profession += professionArray
                                                        .optString(j);
                                            } else {
                                                profession += ","
                                                        + professionArray
                                                        .optString(j);
                                            }
                                        }
                                    }
                                    userInfo.setPermission(profession);
                                    JSONObject orgRoleObj = userObj
                                            .optJSONObject("orgRole");
                                    if (orgRoleObj != null) {
                                        OrgRole orgRole = new OrgRole();
                                        orgRole.setRoleType(orgRoleObj
                                                .optString("roleType"));
                                        orgRole.setPermission(orgRoleObj
                                                .optString("permission"));
                                        orgRole.setRoleCode(orgRoleObj
                                                .optString("roleCode"));
                                        if (orgRole.getRoleCode().equals(
                                                "driver")) {
                                            System.out.print(orgRole
                                                    .getRoleCode().equals(
                                                            "driver"));
                                        }
                                        orgRole.setRoleName(orgRoleObj
                                                .optString("roleName"));
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
//		// 发现页最多显示三个
//		for (int i = 0; i < orgList.size(); i++) {
//			if (i < 3) {
//				orgListUser.add(orgList.get(i));
//			}
//		}
        mOrgGridViewAdapter = new SubjectGridAdapter(AllPersonActivity.this,
                orgListUser);
        orgGridView.setAdapter(mOrgGridViewAdapter);
        orgGridView.setOnItemClickListener(this);

        // setGridViewRealHeight(mLocationsGridView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }


}
