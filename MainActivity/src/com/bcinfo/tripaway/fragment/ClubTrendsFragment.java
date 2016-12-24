package com.bcinfo.tripaway.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ClubTrendsAdapter;
import com.bcinfo.tripaway.adapter.ClubTrendsAdapter.OnperationListener;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ClubTrendsFragment extends BaseFragment {

	private List<Feed> feedList = new ArrayList<Feed>();

	private ClubTrendsAdapter trendsAdapter;

	private Context mContext;

	private UserInfo userInfo;

	private MyListView mTrendsListView;

	private int pageNum = 1;

	private int pageSize = 10;

	private LinearLayout mTrendListViewFooter;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;


	private boolean isMore = true;

	public ClubTrendsFragment() {

	}

	public ClubTrendsFragment(Context mContext, UserInfo userInfo) {
		this.mContext = mContext;
		this.userInfo = userInfo;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.club_trends_fragment, null);
		initView(view);
		queryMyTrends(userInfo.getUserId());
		registerBoradcastReceiver();
		return view;
	}

	private void initView(View view) {
		trendsAdapter = new ClubTrendsAdapter(mContext, feedList,
				new OnperationListener() {
					@Override
					public void addFoucsById(String userId,int id) {
//						View views =mTrendsListView.getChildAt(id);
//						TextView txt = (TextView)views.findViewById(R.id.club_trend_focus_add);
//						txt.setVisibility(View.GONE);
						addOrCancelFocus(userId);
						trendsAdapter.notifyDataSetChanged();
					}
				});
		mTrendsListView = (MyListView) view
				.findViewById(R.id.my_trends_listview);
//		mTrendListViewFooter = (LinearLayout) LayoutInflater.from(mContext)
//				.inflate(R.layout.xlistview_footer, null);
//		mTrendsListView.addFooterView(mTrendListViewFooter);
//		mTrendListViewFooter.setVisibility(View.GONE);
		mTrendsListView.setAdapter(trendsAdapter);
		trendsAdapter.notifyDataSetChanged();
	}

	private void queryMyTrends(String userId) {
		if (!isMore||isLoadmore) {
			return;
		}
		RequestParams params = new RequestParams();
		isLoadmore = true;
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		params.put("userId", userId);
		HttpUtil.get(Urls.trends_list, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
			
				String code = response.optString("code");

				if ("00000".equals(code)) {
					JSONArray data = response.optJSONArray("data");

					if (data != null) {
//						if (isLoadmore) {
//							mTrendListViewFooter.setVisibility(View.VISIBLE);
//						}
						List<Feed> list = new ArrayList<>();
						for (int i = 0; i < data.length(); i++) {
							JSONObject jsonObject = data.optJSONObject(i);
							if (null != jsonObject) {
								Feed feed = new Feed();
								feed.setPublisherId(jsonObject
										.optString("publisherId"));
								feed.setPublisher(jsonObject
										.optString("publisher"));
								feed.setPublishTime(jsonObject
										.optString("publishTime"));
								feed.setDesc(jsonObject.optString("desc"));
								JSONObject rawData = jsonObject
										.optJSONObject("rawData");
								if (rawData != null) {
									HashMap<String, Object> map = new HashMap<>();
									if (rawData.has("productId")) {
										// 产品动态
										ProductNewInfo product = new ProductNewInfo();
										product.setId(rawData
												.optString("productId"));
										product.setTitle(rawData
												.optString("productTitle"));
										product.setTitleImg(rawData
												.optString("titleImg"));
										map.put("rawData", product);
									} else if (rawData.has("id")) {
										// 游记
										TripArticle article = new TripArticle();
										article.setPhotoId(rawData
												.optString("id"));
										article.setDescription(rawData
												.optString("desc"));
										JSONArray array = rawData
												.optJSONArray("images");
										if (array != null) {
											ArrayList<ImageInfo> listImg = new ArrayList<>();
											for (int j = 0; j < array.length(); j++) {
												JSONObject obj = array
														.optJSONObject(j);
												ImageInfo info = new ImageInfo();
												info.setUrl(obj
														.optString("url"));
												info.setWidth(obj
														.optString("width"));
												info.setHeight(obj
														.optString("height"));
												info.setDesc(obj
														.optString("desc"));
												listImg.add(info);
											}
											article.setPictureList(listImg);
										}
										map.put("rawData", article);
									} else if (rawData.has("userId")) {
										UserInfo userInfo = new UserInfo();
										JSONArray tagArray = rawData
												.optJSONArray("tags");
										ArrayList<String> tag = new ArrayList<>();
										if (tagArray != null) {
											for (int j = 0; j < tagArray
													.length(); j++) {
												String tagObj = tagArray
														.optString(j);
												tag.add(tagObj);
											}
										}
										userInfo.setTags(tag);
										userInfo.setIsFocus(rawData
												.optString("isFocus"));
										userInfo.setAvatar(rawData
												.optString("avatar"));
										userInfo.setNickname(rawData
												.optString("nickname"));
										userInfo.setUserId(rawData
												.optString("userId"));
										map.put("rawData", userInfo);
									}
									feed.setMap(map);
									list.add(feed);
								}
							}
						}
						if (list.size() >= pageSize) {
							pageNum++;
						} else {
							isMore = false;
						}
						notifySetChanged(list);
					} else {
						if (pageNum != 1) {
							pageNum--;
						}
					}
				}
				isLoadmore = false;

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isLoadmore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				isLoadmore = false;
				if (pageNum != 1) {
					pageNum--;
				}
			}
		});

	}

	private void notifySetChanged(List<Feed> list) {
		feedList.addAll(list);
		trendsAdapter.notifyDataSetChanged();
//		if (isLoadmore) {
//			mTrendListViewFooter.setVisibility(View.GONE);
//		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.bcinfo.loadMoreTrends".equals(action)) {
//				isLoadmore = false;
				queryMyTrends(userInfo.getUserId());
			}
		}
	};

	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.loadMoreTrends");
		// 注册广播
		mContext.registerReceiver(broadcastReceiver, myIntentFilter);
	}

	private void addOrCancelFocus(String userId) {
		if (!AppInfo.getIsLogin()) {
			ToastUtil.showToast(mContext, "请登录");
			return;
		}
		try {
			JSONObject params = new JSONObject();
			params.put("userId", userId);
			StringEntity entity = new StringEntity(params.toString(),
					HTTP.UTF_8);
			HttpUtil.post(Urls.friend_list_url, entity,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							String code = response.optString("code");
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
