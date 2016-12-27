package com.bcinfo.tripaway.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.SquareFragmentAdapter;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.DataBean;
import com.bcinfo.tripaway.bean.Feed_Schema;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.SquareTripArticle;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 广场我的关注 fragment
 */
public class Fragment2 extends BaseFragment {
    private ListView squareListView;
    /**
     * 下拉刷新
     */
    private boolean isPullRefresh = false;
    /**
     * 上拉加载更多
     */
    private boolean isLoadmore = false;
    private int pageNum = 1;
    private int pagesize = 10;
    private SquareFragmentAdapter myAdapter;
    private ArrayList<Feed_Schema> lists;
    DataBean dataBean = new DataBean();
    private ImageView noimage;
    private TwinklingRefreshLayout refreshLayout;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.mysqurelayout_item, null);
        squareListView = (ListView) view.findViewById(R.id.all_square_listview);
        lists = new ArrayList<>();
        noimage = (ImageView) view.findViewById(R.id.noimage);
        myAdapter = new SquareFragmentAdapter(getActivity(), lists, true);
        squareListView.setAdapter(myAdapter);
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
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isPullRefresh) {
                            return;
                        }
                        pageNum = 1;
                        isPullRefresh = true;
                        setView();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadmore = true;
                        setView();
                    }
                }, 2000);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        statisticsTitle = "广场-我关注的";
    }

    private void setView() {
        allSuqareList("", "", "", "", "1");
    }



	/*
     * 话题列表数据
	 */

    private void allSuqareList(String userId, String commentRows, String tagId, String themeName, String focus) {

        // TODO Auto-generated method stub
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);
        params.put("pagesize", pagesize);
        params.put("userId", userId);
        params.put("commentRows", commentRows);
        params.put("tagId", tagId);
        params.put("themeName", themeName);
        params.put("focus", focus);
        HttpUtil.get(Urls.get_square_list, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                if (isLoadmore) {
                    refreshLayout.finishLoadmore();
                }

                allSquare(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                isPullRefresh = false;
                isLoadmore = false;
                if (isLoadmore) {
                    refreshLayout.finishLoadmore();
                }
                if (isPullRefresh) {
                    refreshLayout.finishRefreshing();
                }
                if (pageNum != 1) {
                    pageNum--;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                isPullRefresh = false;
                isLoadmore = false;
                if (isLoadmore) {
                    refreshLayout.finishLoadmore();
                }
                if (isPullRefresh) {
                    refreshLayout.finishRefreshing();
                }
                if (pageNum != 1) {
                    pageNum--;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                isPullRefresh = false;
                isLoadmore = false;
                if (isLoadmore) {
                    refreshLayout.finishLoadmore();
                }
                if (isPullRefresh) {
                    refreshLayout.finishRefreshing();
                }
                if (pageNum != 1) {
                    pageNum--;
                }
            }

        });

    }

    protected void allSquare(JSONObject response) {
        // TODO Auto-generated method stub
        if ("00000".equals(response.optString("code"))) {
            if (isPullRefresh) {
                refreshLayout.finishRefreshing();
                if (lists.size() > 0) {
                    lists.clear();
                }
            }
            JSONArray data1 = response.optJSONArray("data");
            ArrayList<Feed_Schema> feedList = new ArrayList<>();
            if (data1 != null && data1.length() != 0) {
                for (int i = 0; i < data1.length(); i++) {
                    JSONObject databean = data1.optJSONObject(i);
                    dataBean.setType(databean.optString("type"));
                    if ("0".equals(databean.optString("type"))) {
                        // 默认
                        JSONArray data = databean.optJSONArray("obj");
                        if (data != null && data.length() != 0) {
                            for (int j = 0; j < data.length(); j++) {
                                JSONObject feedObj = data.optJSONObject(j);
                                Feed_Schema feed_Schema = new Feed_Schema();
                                feed_Schema.setDesc(feedObj.optString("desc"));
                                feed_Schema.setPublishTime(feedObj.optString("publishTime"));
                                feed_Schema.setFeedType(feedObj.optString("feedType"));
                                feed_Schema.setIsFocus(feedObj.optString("isFocus"));
                                if ("yes".equals(feedObj.optString("isFocus"))) {
                                    JSONObject publisher = feedObj.optJSONObject("publisher");
                                    if (publisher != null) {
                                        feed_Schema.setPublisher(JsonUtil.getSquareUser(publisher));
                                    }
                                    JSONObject rawData = feedObj.optJSONObject("rawData");
                                    SquareTripArticle squareRawData = new SquareTripArticle();
                                    if (rawData != null) {
                                        squareRawData.setPhotoId(rawData.optString("id"));// id
                                        squareRawData.setDesc(rawData.optString("desc"));// 标题
                                        squareRawData.setIsLike(rawData.optString("isLike"));// 是否赞
                                        squareRawData.setLikes(rawData.optString("likes"));// 赞
                                        squareRawData.setLocation(rawData.optString("location"));// 位置
                                        squareRawData.setComments(rawData.optString("comments"));// 评论数
                                        squareRawData.setViews(rawData.optString("views"));// 浏览数
                                        squareRawData.setCover(rawData.optString("cover"));// 连载封面
                                        squareRawData.setTitle(rawData.optString("title"));// 连载封面
                                        squareRawData.setProductId(rawData.optString("productId"));// 产品Id
                                        squareRawData.setTitleImg(rawData.optString("titleImg"));// 产品标题图
                                        squareRawData.setProductTitle(rawData.optString("productTitle"));// 产品标题
                                        squareRawData.setAbstracts(rawData.optString("abstracts"));// 大游记的摘要
                                        JSONArray imageList = rawData.optJSONArray("images");
                                        ArrayList<ImageInfo> squareImageList = new ArrayList<>();
                                        if (imageList != null) {
                                            for (int k = 0; k < imageList.length(); k++) {
                                                ImageInfo imageInfo = new ImageInfo();
                                                imageInfo.setUrl(imageList.optJSONObject(k).optString("url"));
                                                imageInfo.setHeight(imageList.optJSONObject(k).optString("height"));
                                                imageInfo.setWidth(imageList.optJSONObject(k).optString("width"));
                                                squareImageList.add(imageInfo);
                                            }
                                            squareRawData.setPictureList(squareImageList);
                                        }
                                        JSONArray tagNames = rawData.optJSONArray("tagNames");
                                        String tagName = new String();
                                        ArrayList<String> tag = new ArrayList<>();
                                        for (int k = 0; k < tagNames.length(); k++) {
                                            String jsonObject = null;
                                            try {
                                                jsonObject = (String) tagNames.get(k);
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                            }
                                            tag.add(jsonObject);
                                        }
                                        squareRawData.setTagNames(tag);
                                        JSONArray commList = rawData.optJSONArray("commentList");
                                        ArrayList<Comments> comments = new ArrayList<>();
                                        if (commList != null) {
                                            for (int k = 0; k < commList.length(); k++) {
                                                JSONObject jsonObject = commList.optJSONObject(k);
                                                Comments comment = new Comments();
                                                comment.setContent(jsonObject.optString("content"));
                                                comment.setCreateTime(jsonObject.optString("createTime"));
                                                comment.setId(jsonObject.optString("id"));
                                                JSONObject jsonUserInfo = jsonObject.optJSONObject("publisher");
                                                if (jsonUserInfo != null) {
                                                    comment.setUser(JsonUtil.getUserInfo(jsonUserInfo));
                                                }
                                                comments.add(comment);
                                            }
                                        }
                                        squareRawData.setCommentList(comments);
                                        feed_Schema.setRawData(squareRawData);
                                        feedList.add(feed_Schema);
                                    }
                                }

                            }
                        }
                    }
                }
                if (feedList.size() == 0) {
                    noimage.setVisibility(View.VISIBLE);
                    squareListView.setVisibility(View.GONE);
                } else {
                    squareListView.setVisibility(View.VISIBLE);
                    noimage.setVisibility(View.GONE);
                }
                refreshLayout.finishRefreshing();
                if (feedList.size() >= pagesize) {
                    pageNum++;
                    refreshLayout.setEnableLoadmore(true);
                } else {
                    refreshLayout.setEnableLoadmore(false);
                }
                lists.addAll(feedList);
                myAdapter.notifyDataSetChanged();
            } else {
                refreshLayout.setEnableLoadmore(false);
                ToastUtil.showToast(getActivity(), "没有更多内容了！");
                myAdapter.notifyDataSetChanged();
            }
        } else {
            if (isPullRefresh) {
                // 下拉刷新接口返回的数据不正确
                refreshLayout.finishRefreshing();
            }
            if (pageNum != 1) {
                pageNum--;
            }
        }
        // 上拉 下拉的初始状态置为false;
        isLoadmore = false;
        isPullRefresh = false;
    }

    // 评论刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0 && data != null) {
            SquareTripArticle tripArticle = data.getParcelableExtra("tripArticle");
            for (Feed_Schema tripOld : lists) {
                if (tripOld.getRawData().getPhotoId() == tripArticle.getPhotoId()) {
                    tripOld.setPublisher(tripArticle.getPublisher());
                    break;
                }
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unRegisterBoradcastReceiver();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.bcinfo.delPhoto");
        myIntentFilter.addAction("com.bcinfo.refreshCommentsCount");
        myIntentFilter.addAction("com.bcinfo.refreshLikesCount");
        myIntentFilter.addAction("com.bcinfo.publishBlog");
        myIntentFilter.addAction("com.bcinfo.refreshFocus");
        myIntentFilter.addAction("com.bcinfo.squre");
        myIntentFilter.addAction("com.bcinfo.haveLogin");
        // 注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private void unRegisterBoradcastReceiver() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.bcinfo.refreshFocus".equals(action)) {
                // String userId = intent.getStringExtra("userId");
                // boolean isFocus = intent.getBooleanExtra("isFocus", false);
                // if (userId != null) {
                // for (int i = 0; i < lists.size(); i++) {
                // if (lists.get(i).getPublisher() != null
                // && lists.get(i).getPublisher().getUserId().equals(userId)) {
                // if (lists.get(i).getIsFocus().equals("yes") && !isFocus) {
                // lists.get(i).setIsFocus("no");
                // myAdapter.notifyDataSetChanged();
                // }
                // if (lists.get(i).getIsFocus().equals("no") && isFocus) {
                // lists.get(i).setIsFocus("yes");
                // myAdapter.notifyDataSetChanged();
                // }
                // }
                // }
                //
                // }

                lists.clear();
                isPullRefresh = true;
                pageNum = 1;
                setView();
            } else if ("com.bcinfo.squre".equals(action)) {
                lists.clear();
                isPullRefresh = true;
                pageNum = 1;
                setView();
            } else if ("com.bcinfo.haveLogin".equals(action)) {
                lists.clear();
                isPullRefresh = true;
                pageNum = 1;
                setView();
            }
            if ("com.bcinfo.delPhoto".equals(action)) {
                lists.clear();
                // isRefresh = 0;
                isPullRefresh = true;
                pageNum = 1;
                setView();
            } else if ("com.bcinfo.publishBlog".equals(action)) {
                // myListview.refreshDrawableState();
                // lists.clear();
                // // isRefresh = 0;
                // isPullRefresh = true;
                // pageNum = 1;
                // testTripStoryUrl();
            } else if ("com.bcinfo.refreshCommentsCount".equals(action)) {
                String count = intent.getStringExtra("count");
                String microId = intent.getStringExtra("microId");
                ArrayList<Comments> comments = intent.getParcelableArrayListExtra("comments");
                if (count != null && microId != null) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getRawData() != null
                                && !StringUtils.isEmpty(lists.get(i).getRawData().getPhotoId())) {
                            if (lists.get(i).getRawData().getPhotoId().equals(microId)) {
                                lists.get(i).getRawData().setComments(count);
                                lists.get(i).getRawData().setCommentList(comments);
                                myAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
                // isPullRefresh = true;
                // pageNum = 1;
                // testTripStoryUrl();
            } else if ("com.bcinfo.refreshLikesCount".equals(action)) {
                int like = intent.getIntExtra("like", 0);
                String microId = intent.getStringExtra("microId");
                if (microId != null) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getRawData() != null) {
                            if (!StringUtils.isEmpty(lists.get(i).getRawData().getPhotoId())) {
                                if (microId.equals(lists.get(i).getRawData().getPhotoId())) {
                                    String isLike = lists.get(i).getRawData().getIsLike();
                                    int likes = 0;
                                    if (!StringUtils.isEmpty(lists.get(i).getRawData().getLikes())) {
                                        likes = Integer.parseInt(lists.get(i).getRawData().getLikes());
                                    }
                                    if (StringUtils.isEmpty(isLike)) {
                                        if (like == 1) {
                                            lists.get(i).getRawData().setIsLike("1");
                                            lists.get(i).getRawData().setLikes(Integer.toString(likes + 1));
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        if (isLike.equals("0") && like == 1) {
                                            lists.get(i).getRawData().setIsLike("1");
                                            lists.get(i).getRawData().setLikes(Integer.toString(likes + 1));
                                            myAdapter.notifyDataSetChanged();
                                        } else if (isLike.equals("1") && like == 0) {
                                            lists.get(i).getRawData().setIsLike("0");
                                            lists.get(i).getRawData().setLikes(Integer.toString(likes - 1));
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    }
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
