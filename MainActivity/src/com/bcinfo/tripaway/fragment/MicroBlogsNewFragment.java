package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MicroBlogsAdapter;
import com.bcinfo.tripaway.bean.Comment;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MBProgressHUD.MyMBProgressHUD;
import com.bcinfo.tripaway.view.refreshandload.PullToRefreshLayout;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MicroBlogsNewFragment extends BaseFragment implements IXListViewListener {

	private XListView myListview;
	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private MyMBProgressHUD myFooterBar;

	private MicroBlogsAdapter microBlogsAdapter;

	private ArrayList<TripArticle> tripArticleList = new ArrayList<>();
	private int pageNum = 1;
	private int pagesize = 10;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.micro_blogs_fragment, null);
		myListview = (XListView) view.findViewById(R.id.my_travels_listview);
		myFooterBar = (MyMBProgressHUD) view.findViewById(R.id.myfooterBar);
		tripArticleList.clear();
		myListview.setPullLoadEnable(false);
		myListview.setPullRefreshEnable(true);
		myListview.setXListViewListener(this);
		microBlogsAdapter = new MicroBlogsAdapter(getActivity(), tripArticleList);
		myListview.setAdapter(microBlogsAdapter);
		testTripStoryUrl();
		return view;
	}

	/*
	 * test 微游记-照片列表
	 */
	private void testTripStoryUrl() {
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);
		params.put("pagesize", pagesize);
		HttpUtil.get(Urls.tripStory_url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				if (isLoadmore) {
					// 上拉返回的结束加载更多布局
					myListview.stopLoadMore();
				}
				// System.out.println(statusCode);
				System.out.println(response.toString());
				getTripStoryList(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
				myFooterBar.setLoadFailed();
				isPullRefresh = false;
				isLoadmore = false;
				if (isLoadmore) {
					myListview.stopLoadMore();
				}
				if (isPullRefresh) {
					myListview.stopRefresh();
				}
				if (pageNum != 1) {
					pageNum--;
				}
			}
		});
	}

	/*
	 * 解析从服务端获取的微游记参数
	 */
	private void getTripStoryList(JSONObject response) {
		if ("00000".equals(response.optString("code"))) {
			if (isPullRefresh) {
				myListview.successRefresh();
			}

			ArrayList<TripArticle> list = new ArrayList<>();
			// responsetv.setText(response.toString());
			JSONObject dataObj = response.optJSONObject("data");
			JSONArray dataArray = dataObj.optJSONArray("tripstory");

			if (dataArray != null && dataArray.length() != 0) {
				for (int i = 0; i < dataArray.length(); i++) {
					JSONObject tripStoryObj = dataArray.optJSONObject(i);
					if (null == tripStoryObj) {
						return;
					}
					TripArticle articleObj = new TripArticle();
					// 微游记 发布时间
					articleObj.setPublishTime(tripStoryObj.optString("publishTime"));
					// 位置
					articleObj.setLocation(tripStoryObj.optString("location"));
					// 是否已赞
					articleObj.setIsLike(tripStoryObj.optString("isLike"));
					// 点赞数
					articleObj.setLikes(tripStoryObj.optString("likes"));
					// 微游记 标识id
					articleObj.setPhotoId(tripStoryObj.optString("photoId"));
					// 微游记 评论数
					articleObj.setComments(tripStoryObj.optString("comments"));
					// 微游记 描述
					articleObj.setDescription(tripStoryObj.optString("description"));
					JSONArray picArray = tripStoryObj.optJSONArray("pictures");
					if (null != picArray) {
						for (int j = 0; j < picArray.length(); j++) {
							JSONObject picObj = picArray.optJSONObject(j);
							ImageInfo picRes = new ImageInfo();
							if (null == picObj) {
								return;
							}
							if(StringUtils.isEmpty(picObj.optString("url")))
								continue;
							picRes.setDesc(picObj.optString("desc"));
							picRes.setUrl(picObj.optString("url"));
							picRes.setHeight(picObj.optString("height"));
							picRes.setWidth(picObj.optString("width"));
							articleObj.getPictureList().add(picRes);

						}
					}
					JSONObject userObj = tripStoryObj.optJSONObject("publisher");
					if (null != userObj&&userObj.length()>0) {

//						articleObj.getPublisher().setGender(userObj.optString("sex"));
//						articleObj.getPublisher().setStatus(userObj.optString("status"));
//						articleObj.getPublisher().setEmail(userObj.optString("email"));
//						articleObj.getPublisher().setNickname(userObj.optString("nickName"));
//						articleObj.getPublisher().setUserId(userObj.optString("userId"));
//						articleObj.getPublisher().setAvatar(userObj.optString("avatar"));
//						System.out.println("头像的url" + userObj.optString("avatar"));
//						articleObj.getPublisher().setIntroduction(userObj.optString("introduction"));
//						articleObj.getPublisher().setMobile(userObj.optString("mobile"));
//						JSONArray tagsArray = userObj.optJSONArray("tags");
//						if (null != tagsArray) {
//							for (int k = 0; k < tagsArray.length(); k++) {
//								articleObj.getPublisher().getTags().add(tagsArray.optString(k));
//
//							}
//						}
						articleObj.setPublisher(JsonUtil.getUserInfo(userObj));
					}
					JSONArray jsonArray = tripStoryObj.optJSONArray("commentList");
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int j = 0; j < jsonArray.length(); j++) {
							JSONObject jsonObject = jsonArray.optJSONObject(j);
							Comments comment = new Comments();
							comment.setContent(jsonObject.optString("content"));
							comment.setCreateTime(jsonObject.optString("createTime"));
							comment.setId(jsonObject.optString("id"));
							JSONObject jsonUserInfo = jsonObject.optJSONObject("publisher");
							if (jsonUserInfo != null) {
								UserInfo userInfo = new UserInfo();
								userInfo.setAvatar(jsonUserInfo.optString("avatar"));
								userInfo.setNickname(jsonUserInfo.optString("nickName"));
								comment.setUser(userInfo);
							}
							articleObj.getCommentList().add(comment);
						}
					}
					list.add(articleObj);
				}
				if (list.size() >= pagesize) {
					pageNum++;
					myListview.setPullLoadEnable(true);
				} else {
					myListview.setPullLoadEnable(false);
				}
				notifyMoreData(list);
			} else {
				myFooterBar.setLoadEmptyInfo();
				tripArticleList.clear();
				microBlogsAdapter.notifyDataSetChanged();
			}
		} else {
			myFooterBar.setLoadEmptyInfo();
			if (isPullRefresh) {
				// 下拉刷新接口返回的数据不正确
				myListview.stopRefresh();
			}
			if (pageNum != 1) {
				pageNum--;
			}
		}
		// 上拉 下拉的初始状态置为false;
		isLoadmore = false;
		isPullRefresh = false;
	}

	private void notifyMoreData(ArrayList<TripArticle> list) {
		for (TripArticle tripnew : list) {
			boolean flag = false;
			for (TripArticle tripold : tripArticleList) {
				if (tripnew.getPhotoId().equals(tripold.getPhotoId())) {
					flag = true;
					// experience.setExpDetail(newExp.getExpDetail());
					break;
				}
			}
			if (flag) {
				continue;
			}
			tripArticleList.add(tripnew);
		}
		// tripArticleList.addAll(list);
		// experienceslist.addAll(list);
		myFooterBar.setLoadSuccess();
		microBlogsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (isPullRefresh) {
			return;
		}
		if (tripArticleList != null) {
			tripArticleList.clear();
		}
		pageNum = 1;
		isPullRefresh = true;
		testTripStoryUrl();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		testTripStoryUrl();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 0 && data != null) {
			TripArticle tripArticle = data.getParcelableExtra("tripArticle");
			for (TripArticle tripOld : tripArticleList) {
				if (tripOld.getPhotoId() == tripArticle.getPhotoId()) {
					tripOld.setPublisher(tripArticle.getPublisher());
					break;
				}
			}
			microBlogsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerBoradcastReceiver();
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		unRegisterBoradcastReceiver();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.delPhoto");
		myIntentFilter.addAction("com.bcinfo.refreshCommentsCount");
		myIntentFilter.addAction("com.bcinfo.refreshLikesCount");
		myIntentFilter.addAction("com.bcinfo.publishBlog");
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
			if ("com.bcinfo.delPhoto".equals(action)) {
				tripArticleList.clear();
				// isRefresh = 0;
				isPullRefresh = true;
				pageNum = 1;
				testTripStoryUrl();
			} else if ("com.bcinfo.publishBlog".equals(action)) {
				myListview.refreshDrawableState();
				tripArticleList.clear();
				// isRefresh = 0;
				isPullRefresh = true;
				pageNum = 1;
				testTripStoryUrl();
			} else if ("com.bcinfo.refreshCommentsCount".equals(action)) {
				String count = intent.getStringExtra("count");
				String microId = intent.getStringExtra("microId");
				ArrayList<Comments> comments = intent.getParcelableArrayListExtra("comments");
				if (count != null && microId != null) {
					for (TripArticle article : tripArticleList) {
						if (article.getPhotoId().equals(microId)) {
							article.setComments(count);
							article.setCommentList(comments);
							microBlogsAdapter.notifyDataSetChanged();
							break;
						}
					}
				}
//				isPullRefresh = true;
//				pageNum = 1;
//				testTripStoryUrl();
			} else if ("com.bcinfo.refreshLikesCount".equals(action)) {
				int like = intent.getIntExtra("like", 0);
				String microId = intent.getStringExtra("microId");
				if (microId != null) {
					for (TripArticle article : tripArticleList) {
						if (article.getPhotoId().equals(microId)) {
							String isLike = article.getIsLike();
							int likes = 0;
							if (!StringUtils.isEmpty(article.getLikes())) {
								likes = Integer.parseInt(article.getLikes());
							}
							if (StringUtils.isEmpty(isLike)) {
								if (like == 1) {
									article.setIsLike("1");
									article.setLikes(Integer.toString(likes + 1));
									microBlogsAdapter.notifyDataSetChanged();
								}
							} else {
								if (isLike.equals("0") && like == 1) {
									article.setIsLike("1");
									article.setLikes(Integer.toString(likes + 1));
									microBlogsAdapter.notifyDataSetChanged();
								} else if (isLike.equals("1") && like == 0) {
									article.setIsLike("0");
									article.setLikes(Integer.toString(likes - 1));
									microBlogsAdapter.notifyDataSetChanged();
								}
							}
							break;
						}
					}
				}

			}
		}
	};
}
