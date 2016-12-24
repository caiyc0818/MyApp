package com.bcinfo.tripaway.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductThemesGridAdapter;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.LogUtil;
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

public class AllThemesActivity extends BaseActivity implements
        OnItemClickListener {
    // GridView 显示全部主题
    private PullableGridView allThemesGridView;


    // list集合
    private ArrayList<PushResource> topicsList = new ArrayList<PushResource>();

    // 全屏显示的半透明背景
    private int pageNum = 1;

    private int pageSize = 10;

    private boolean isPullRefresh = false;

    private boolean isLoadMore = false;

    private TextView responsetv;

    private ProductThemesGridAdapter pThemeAdapter;


    private TwinklingRefreshLayout refreshLayout;
    private NewSinaRefreshView headerView;

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.discovery_themes_all);
        this.discoverTitle = (TextView) findViewById(R.id.discovery_title_text);

        // 主题
        discoverTitle.setText(R.string.discover_title_themes);
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

        allThemesGridView = (PullableGridView) findViewById(R.id.discovery_allThemes_gridView);

        pThemeAdapter = new ProductThemesGridAdapter(AllThemesActivity.this,
                topicsList);

        allThemesGridView.setAdapter(pThemeAdapter);
        allThemesGridView.setOnItemClickListener(AllThemesActivity.this);

        testAllTopicsUrl();
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

        refreshLayout.setPureScrollModeOn(false);
        refreshLayout.setAutoLoadMore(true);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        topicsList.clear();
                        isPullRefresh = true;
                        pageNum = 1;
                        testAllTopicsUrl();
                    }
                }, 2000);
            }


            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加载操作
                        isLoadMore = true;
                        testAllTopicsUrl();
                    }
                }, 2000);
            }
        });

    }

    /*
     * 测试 全部主题接口
     */
    private void testAllTopicsUrl() {
        RequestParams params = new RequestParams();
        params.put("pagesize", 10);
        params.put("pagenum", pageNum);
        params.put("mcode", "app.dest.topic");
        HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {

                super.onSuccess(statusCode, headers, response);
                LogUtil.i("DiscoverAllThemesActivity", "全部主题接口=",
                        response.toString());
                refreshLayout.finishRefreshing();
                getAllTopicList(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // ToastUtil.showToast(DiscoverAllThemesActivity.this,
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

    private void getAllTopicList(JSONObject response) {
        String code = response.optString("code");
        if ("00000".equals(code)) {
            if (isLoadMore) {
                // 上拉返回的结束加载更多布局
                refreshLayout.finishLoadmore();
            }
            if (isPullRefresh) {
                // 下拉刷新返回的
                refreshLayout.finishRefreshing();
            }
            JSONArray topicArray = response.optJSONArray("data");// 主题
            ArrayList<PushResource> tempList = new ArrayList<PushResource>();
            // for (int i = 0; i < topicArray.length(); i++)
            // {
            // JSONObject jsonTopicObj = topicArray.optJSONObject(i);
            // PushResource pushTopicResource = new PushResource();
            // pushTopicResource.setId(jsonTopicObj.optString("id"));
            // pushTopicResource.setSubTitle(jsonTopicObj.optString("subTitle"));
            // pushTopicResource.setTitleImage(jsonTopicObj.optString("titleImage"));
            // pushTopicResource.setObjectId(jsonTopicObj.optString("objectId"));
            // pushTopicResource.setObject(jsonTopicObj.optJSONObject("object"));
            // pushTopicResource.setObjectType(jsonTopicObj.optString("objectType"));
            // pushTopicResource.setResTitle(jsonTopicObj.optString("resTitle"));
            // tempList.add(pushTopicResource);
            //
            // }
            JsonUtil.getPushResources(topicArray, tempList);
            if (tempList.size() < 10) {
                allThemesGridView.setPullLoadEnable(false);
                refreshLayout.setEnableLoadmore(false);
            } else {
                pageNum++;
                allThemesGridView.setPullLoadEnable(true);
                refreshLayout.setEnableLoadmore(true);
            }
            topicsList.addAll(tempList);
            pThemeAdapter.notifyDataSetChanged();
        } else {
            // ToastUtil.showToast(DiscoverAllThemesActivity.this, "errorCode="
            // + code);
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

    /**
     * 点击gridView的某一项后进入单一主题产品列表页面
     */
    @Override
    public void onItemClick(AdapterView<?> parentView, View view, int position,
                            long id) {
        // String topicId = topicsList.get(position).getId(); //
        // 获得指定position处的主题id
        // Intent intentForTopicProductList = new Intent(this,
        // ThemeProductListActivity.class);
        // intentForTopicProductList.putExtra("themeId", topicId);
        // intentForTopicProductList.putExtra("description",
        // topicsList.get(position).getDescription());
        // System.out.println("topicId=" + topicId);
        // startActivity(intentForTopicProductList);
        // this.activityAnimationOpen();
        PushResource pushTopicResource = topicsList.get(position);
        ActivityUtil.toDetailPage(pushTopicResource, this, false);
        // if (pushTopicResource.getObjectType().equals("topic")) {
        // String topicId = pushTopicResource.getObjectId();
        // Intent intentForTopicProductList = new Intent(
        // this, ThemeDetailActivity.class);
        // intentForTopicProductList.putExtra("themeId", topicId);
        // intentForTopicProductList.putExtra("themeName", topicsList
        // .get(position).getResTitle());
        // intentForTopicProductList.putExtra("description",
        // ((JSONObject) topicsList.get(position).getObject())
        // .optString("description"));
        //
        // startActivity(intentForTopicProductList);
        // }
        // else if (pushTopicResource.getObjectType().equals("subject")) {
        // String entityId = pushTopicResource.getObjectId();
        // Intent intent = new Intent(this,
        // SubjectDetailActivity.class);
        // intent.putExtra("entityId", entityId);
        // startActivity(intent);
        // this.activityAnimationOpen();
        // }

    }


}
