package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.SquareHotThemeAdapter;
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SquareHotTheme extends BaseActivity implements IXListViewListener {
	private XListView allHotTheme;
	private View loginLoading;
	private AnimationDrawable loadingAnimation;
	private ArrayList<TopicOrTag> lists;
	private SquareHotThemeAdapter myAdapter;
	private boolean isPullRefresh = false;
	private int pageNum = 1;
	private int pagesize = 10;
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.square_theme2);
		setSecondTitle("热门话题");
		lists = new ArrayList<>();
		allHotTheme = (XListView) findViewById(R.id.all_theme_listview);
		allHotTheme.setPullLoadEnable(true);
		allHotTheme.setPullRefreshEnable(true);
		allHotTheme.setXListViewListener(this);
		myAdapter = new SquareHotThemeAdapter(SquareHotTheme.this, lists);
		allHotTheme.setAdapter(myAdapter);
		loginLoading = (View) findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		alltheme();
		allHotTheme.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SquareHotTheme.this, SquareThemeActivity.class);
				intent.putExtra("themeName", lists.get(position - 1).getName());
				startActivity(intent);
				activityAnimationOpen();
			}

		});

	}

	private void alltheme() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("mcode", "sale.square.theme");
		params.put("pagenum", pageNum);
		params.put("pagesize", pagesize);
		HttpUtil.get(Urls.get_bymcode, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (isLoadmore) {
					allHotTheme.stopLoadMore();
				}
				String code = response.optString("code");
				if ("00000".equals(code)) {
					if (isPullRefresh) {
						allHotTheme.successRefresh();
						if (lists.size() > 0) {
							lists.clear();
						}
					}
					JSONArray dataArray = response.optJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						for (int i = 0; i < dataArray.length(); i++) {

							JSONObject object = dataArray.optJSONObject(i);
							JSONObject tagobject = object.optJSONObject("object");
							TopicOrTag tag2 = new TopicOrTag();
							tag2.setId(tagobject.optString("themeId"));
							tag2.setName(tagobject.optString("themName"));
							tag2.setDiscuss(tagobject.optString("discuss"));
							tag2.setReads(tagobject.optString("reads"));
							tag2.setCover(tagobject.optString("cover"));
							lists.add(tag2);
						}
					}
					if (lists.size() >= 0) {
						loadingAnimation.stop();
						loginLoading.setVisibility(View.GONE);
					}
					if (lists.size() >= pagesize) {
						pageNum++;
						allHotTheme.setPullLoadEnable(true);
					} else {
						allHotTheme.setPullLoadEnable(false);
					}

					myAdapter.notifyDataSetChanged();
				} else {
					if (isPullRefresh) {
						// 下拉刷新接口返回的数据不正确
						allHotTheme.stopRefresh();
					}
					if (pageNum != 1) {
						pageNum--;
					}
				}
				// 上拉 下拉的初始状态置为false;
				isPullRefresh = false;
				isLoadmore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					allHotTheme.stopLoadMore();
				}
				if (isPullRefresh) {
					allHotTheme.stopRefresh();
				}
				if (pageNum != 1) {
					pageNum--;
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					allHotTheme.stopLoadMore();
				}
				if (isPullRefresh) {
					allHotTheme.stopRefresh();
				}
				if (pageNum != 1) {
					pageNum--;
				}
			}
		});

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (isPullRefresh) {
			return;
		}
		pageNum = 1;
		isPullRefresh = true;
		alltheme();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		alltheme();
	}

}
