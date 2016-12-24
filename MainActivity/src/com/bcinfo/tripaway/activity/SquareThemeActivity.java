package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.SquareFragmentAdapter;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.DataBean;
import com.bcinfo.tripaway.bean.Feed_Schema;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.SquareProductNewInfo;
import com.bcinfo.tripaway.bean.SquareTripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.objectParam;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SquareThemeActivity extends BaseActivity implements IXListViewListener {

	private XListView squareListView;
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
	private ArrayList<Feed_Schema> lists;
	private View loginLoading;
	private AnimationDrawable loadingAnimation;
	private SquareFragmentAdapter myAdapter;
	DataBean dataBean = new DataBean();
	private String themeName = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.square_theme);
		themeName = getIntent().getStringExtra("themeName");
		setSecondTitle(themeName);
		lists = new ArrayList<>();
		squareListView = (XListView) findViewById(R.id.all_theme_listview);
		squareListView.setPullLoadEnable(true);
		squareListView.setPullRefreshEnable(true);
		squareListView.setXListViewListener(this);
		loginLoading = (View) findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		myAdapter = new SquareFragmentAdapter(SquareThemeActivity.this, lists, false);
		squareListView.setAdapter(myAdapter);
		allSuqareList("", "", "", themeName, "0");

	}

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
					squareListView.stopLoadMore();
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
					squareListView.stopLoadMore();
				}
				if (isPullRefresh) {
					squareListView.stopRefresh();
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
					squareListView.stopLoadMore();
				}
				if (isPullRefresh) {
					squareListView.stopRefresh();
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
					squareListView.stopLoadMore();
				}
				if (isPullRefresh) {
					squareListView.stopRefresh();
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
				squareListView.successRefresh();
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
								if (!StringUtils.isEmpty(feedObj.optString("feedType"))
										&& "photo".equals(feedObj.optString("feedType"))) {
									feed_Schema.setDesc(feedObj.optString("desc"));
									feed_Schema.setPublishTime(feedObj.optString("publishTime"));
									feed_Schema.setFeedType(feedObj.optString("feedType"));
									feed_Schema.setIsFocus(feedObj.optString("isFocus"));
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
								} else if (!StringUtils.isEmpty(feedObj.optString("feedType"))
										&& "series".equals(feedObj.optString("feedType"))) {
									feed_Schema.setDesc(feedObj.optString("desc"));
									feed_Schema.setPublishTime(feedObj.optString("publishTime"));
									feed_Schema.setFeedType(feedObj.optString("feedType"));
									feed_Schema.setIsFocus(feedObj.optString("isFocus"));
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
									}
									feed_Schema.setRawData(squareRawData);
									feedList.add(feed_Schema);
								}

							}
						}
					}

					if ("1".equals(databean.optString("type"))) {
						JSONArray data = databean.optJSONArray("obj");
						if (data != null && data.length() != 0) {
							for (int j = 0; j < data.length(); j++) {
								JSONObject feedObj = data.optJSONObject(j);
								Feed_Schema feed_Schema = new Feed_Schema();
								if ("commproduct".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
										SquareProductNewInfo proNewInfo = new SquareProductNewInfo();
										if (productObj.optJSONObject("exts") != null) {
											HashMap<String, String> exts = new HashMap<String, String>();
											if (!StringUtils.isEmpty(
													productObj.optJSONObject("exts").optString("refer_tags"))) {
												exts.put("refer_tags",
														productObj.optJSONObject("exts").optString("refer_tags"));
											}
											if (!StringUtils.isEmpty(
													productObj.optJSONObject("exts").optString("big_refer_tags"))) {
												exts.put("big_refer_tags",
														productObj.optJSONObject("exts").optString("big_refer_tags"));
											}
											if (exts.size() > 0) {
												proNewInfo.setExts(exts);
											}
										}
										proNewInfo.setPv(productObj.optString("pv"));
										proNewInfo.setOriginalPrice(productObj.optString("originalPrice"));
										proNewInfo.setId(productObj.optString("id"));
										proNewInfo.setDistance(productObj.optString("distance"));
										JSONArray topicArray = (productObj.optJSONArray("topics"));
										if (topicArray != null) {
											for (int k = 0; k < topicArray.length(); k++) {
												proNewInfo.getTopics().add(topicArray.optString(k));
											}
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
										proNewInfo.setIsFav(productObj.optString("isFav"));
										proNewInfo.setServices(productObj.optString("serviceCodes"));
										proNewInfo.setPriceFrequency(productObj.optString("priceFrequency"));
										// add by lij 2015/09/25 start
										// 新增每个产品的参与人数
										proNewInfo.setMemberJoinCount(productObj.optString("servTimes"));
										// add by lij 2015/09/25 end
										// tags参数有问题 暂时不解析
										if (productObj.optJSONObject("creater") != null) {
											if (productObj.optJSONObject("creater").optJSONObject("orgRole") != null) {
												OrgRole orgRole = new OrgRole();
												orgRole.setPermission(productObj.optJSONObject("creater")
														.optJSONObject("orgRole").optString("permission"));
												orgRole.setRoleCode(productObj.optJSONObject("creater")
														.optJSONObject("orgRole").optString("roleCode"));
												orgRole.setRoleName(productObj.optJSONObject("creater")
														.optJSONObject("orgRole").optString("roleName"));
												orgRole.setRoleType(productObj.optJSONObject("creater")
														.optJSONObject("orgRole").optString("roleType"));
												proNewInfo.getUser().setOrgRole(orgRole);
											}
											proNewInfo.getUser()
													.setGender(productObj.optJSONObject("creater").optString("sex"));
											proNewInfo.getUser()
													.setStatus(productObj.optJSONObject("creater").optString("status"));
											proNewInfo.getUser()
													.setEmail(productObj.optJSONObject("creater").optString("email"));
											proNewInfo.getUser().setNickname(
													productObj.optJSONObject("creater").optString("nickName"));
											proNewInfo.getUser()
													.setUserId(productObj.optJSONObject("creater").optString("userId"));
											proNewInfo.getUser()
													.setAvatar(productObj.optJSONObject("creater").optString("avatar"));
											proNewInfo.getUser().setIntroduction(
													productObj.optJSONObject("creater").optString("introduction"));
											proNewInfo.getUser()
													.setMobile(productObj.optJSONObject("creater").optString("mobile"));
										}

										JSONArray professionArray = productObj.optJSONObject("creater")
												.optJSONArray("profession");
										if (professionArray != null) {
											String profession = "";
											if (professionArray != null) {
												for (int l = 0; l < professionArray.length(); l++) {
													if (l == 0) {
														profession += professionArray.optString(l);
													} else {
														profession += "," + professionArray.optString(l);
													}
												}
											}
											proNewInfo.getUser().setPermission(profession);
										}

										feed_Schema.setObject(proNewInfo);
									}
									feedList.add(feed_Schema);
								} else if ("commtopic".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
										SquareProductNewInfo toppro = new SquareProductNewInfo();
										toppro.setTopbackground(productObj.optString("background"));
										toppro.setTopid(productObj.optString("id"));
										toppro.setTopsubTitle(productObj.optString("subTitle"));
										toppro.setToptitle(productObj.optString("title"));
										feed_Schema.setObject(toppro);
									}
									feedList.add(feed_Schema);
								} else if ("commdestination".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
										SquareProductNewInfo toppro = new SquareProductNewInfo();
										toppro.setTopbackground(productObj.optString("background"));
										toppro.setTopid(productObj.optString("id"));
										toppro.setTopsubTitle(productObj.optString("subTitle"));
										toppro.setToptitle(productObj.optString("title"));
										feed_Schema.setObject(toppro);
									}
									feedList.add(feed_Schema);
								} else if ("commlink".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									feed_Schema.setObj(feedObj.optString("object"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
									}
									feedList.add(feed_Schema);
								} else if ("commsubject".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
									}
									feedList.add(feed_Schema);
								} else if ("commarticle".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
									}
									feedList.add(feed_Schema);
								} else if ("commactivity".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
									}
									feedList.add(feed_Schema);
								} else if ("commuser".equals("comm" + feedObj.optString("objectType"))) {
									feed_Schema.setFeedType("comm" + feedObj.optString("objectType"));
									feed_Schema.setDescription(feedObj.optString("description"));
									feed_Schema.setTitleImage(feedObj.optString("titleImage"));
									feed_Schema.setObj(feedObj.optString("objectId"));
									feed_Schema.setResTitle(feedObj.optString("resTitle"));
									feed_Schema.setId(feedObj.optString("id"));
									feed_Schema.setContent(feedObj.optString("content"));
									feed_Schema.setDefaltAvatar(feedObj.optString("defaltAvatar"));
									feed_Schema.setDefaltName(feedObj.optString("defaltName"));
									JSONObject objectParam = feedObj.optJSONObject("objectParam");
									if (objectParam != null) {
										objectParam objectParam2 = new objectParam();
										objectParam2.setProductType(objectParam.optString("productType"));
										feed_Schema.setObjectParam(objectParam2);
									}
									JSONObject productObj = feedObj.optJSONObject("object");
									if (productObj != null) {
										UserInfo userInfo = new UserInfo();
										userInfo.setCertifyCode(productObj.optString("certifyCode"));
										userInfo.setFansCount(productObj.optString("fansCount"));
										userInfo.setIsFocus(productObj.optString("isFocus"));
										userInfo.setFocus(productObj.optString("focus"));
										userInfo.setIsTalent(productObj.optString("isTalent"));
										userInfo.setMobile(productObj.optString("mobile"));
										userInfo.setNickname(productObj.optString("nickName"));
										OrgRole orgRole = new OrgRole();
										JSONObject orgObj = productObj.optJSONObject("orgRole");
										orgRole.setPermission(orgObj.optString("permission"));
										orgRole.setRoleCode(orgObj.optString("roleCode"));
										orgRole.setRoleName(orgObj.optString("roleName"));
										orgRole.setRoleType(orgObj.optString("roleType"));
										userInfo.setOrgRole(orgRole);
										userInfo.setPermission(productObj.optString("permission"));
										userInfo.setProductCount(productObj.optString("productCount"));
										userInfo.setRole(productObj.optString("role"));
										userInfo.setStatus(productObj.optString("status"));
										userInfo.setUserId(productObj.optString("userId"));
										userInfo.setUserType(productObj.optString("userType"));
										userInfo.setUserName(productObj.optString("userName"));
										feed_Schema.setUserInfo(userInfo);
									}
									feedList.add(feed_Schema);
								}
							}
						}
					}
				}
					loadingAnimation.stop();
					loginLoading.setVisibility(View.GONE);
				if (feedList.size() >= pagesize) {
					pageNum++;
					squareListView.setPullLoadEnable(true);
				} else {
					squareListView.setPullLoadEnable(false);
				}
				lists.addAll(feedList);
				myAdapter.notifyDataSetChanged();
			} else {
				squareListView.setPullLoadEnable(false);
				loadingAnimation.stop();
				loginLoading.setVisibility(View.GONE);
				myAdapter.notifyDataSetChanged();
			}
		} else {
			if (isPullRefresh) {
				// 下拉刷新接口返回的数据不正确
				squareListView.stopRefresh();
			}
			if (pageNum != 1) {
				pageNum--;
			}
		}
		// 上拉 下拉的初始状态置为false;
		isLoadmore = false;
		isPullRefresh = false;
	}

	// 下拉刷新
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (isPullRefresh) {
			return;
		}
		pageNum = 1;
		isPullRefresh = true;
		allSuqareList("", "", "", themeName, "0");
	}
	// 上拉加载
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isLoadmore = true;
		allSuqareList("", "", "", themeName, "0");
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
		SquareThemeActivity.this.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void unRegisterBoradcastReceiver() {
		SquareThemeActivity.this.unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 关注广播
			if ("com.bcinfo.refreshFocus".equals(action)) {
				String userId = intent.getStringExtra("userId");
				boolean isFocus = intent.getBooleanExtra("isFocus", false);
				if (userId != null) {
					if (lists.size() > 0) {
						for (int i = 0; i < lists.size(); i++) {
							if (lists.get(i).getPublisher() != null
									&& lists.get(i).getPublisher().getUserId().equals(userId)) {
								if (lists.get(i).getIsFocus().equals("yes") && !isFocus) {
									lists.get(i).setIsFocus("no");
									myAdapter.notifyDataSetChanged();
								}
								if (lists.get(i).getIsFocus().equals("no") && isFocus) {
									lists.get(i).setIsFocus("yes");
									myAdapter.notifyDataSetChanged();
								}
							}
						}
					} else {
						pageNum = 1;
						isPullRefresh = true;
						allSuqareList("", "", "", themeName, "0");
					}
				}
			}
			if ("com.bcinfo.delPhoto".equals(action)) {
				 lists.clear();
				 // isRefresh = 0;
				 isPullRefresh = true;
				 pageNum = 1;
				 allSuqareList("", "", "", themeName, "0");
			} else if ("com.bcinfo.publishBlog".equals(action)) {
				if (isPullRefresh) {
					return;
				}
				pageNum = 1;
				isPullRefresh = true;
				allSuqareList("", "", "", themeName, "0");
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
			} else if ("com.bcinfo.refreshLikesCount".equals(action)) {
				int like = intent.getIntExtra("like", 0);
				String microId = intent.getStringExtra("microId");
				if (microId != null) {
					if (lists.size() > 0) {
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
					} else {

						pageNum = 1;
						isPullRefresh = true;
						allSuqareList("", "", "", themeName, "0");
					}
				}

			} else if ("com.bcinfo.squre".equals(action)) {
				 lists.clear();
				 // isRefresh = 0;
				 isPullRefresh = true;
				 pageNum = 1;
				 allSuqareList("", "", "", themeName, "0");
			} else if ("com.bcinfo.haveLogin".equals(action)) {
//				 lists.clear();
//				 // isRefresh = 0;
//				 isPullRefresh = true;
//				 pageNum = 1;
//				 allSuqareList("", "", "", themeName, "0");
			}
		}
	};
}
