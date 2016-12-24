package com.bcinfo.tripaway.fragment;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.activity.LoginActivity;
import com.bcinfo.tripaway.activity.SquarePicDetailActivity;
import com.bcinfo.tripaway.activity.SquareSerialDetailActivity;
import com.bcinfo.tripaway.activity.UserMicroCommentActivity;
import com.bcinfo.tripaway.adapter.MicroBlogsCommentsAdapter;
import com.bcinfo.tripaway.adapter.SquareFragmentAdapter;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.DataBean;
import com.bcinfo.tripaway.bean.Feed;
import com.bcinfo.tripaway.bean.Feed_Schema;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.SquareTripArticle;
import com.bcinfo.tripaway.bean.Story;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wefika.flowlayout.FlowLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * isClub 表示是否为俱乐部    true 是  false 不是为俱乐部的成员
 * 
 * 本类包括：1.俱乐部主体跳转 ，标识 isClub.
 * 			2.俱乐部成员界面                      false
 *
 * 界面包含 1.游记的晒图，连载，大游记
 * 
 */
public class ClubTravelsFragment extends BaseFragment implements OnItemClickListener {

	private ArrayList<Feed_Schema> travelList = new ArrayList<Feed_Schema>();

	private Context mContext;

	private UserInfo userInfo;

	private int pageNum = 1;

	private int pageSize = 10;

	private View back;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	// private boolean isMore = true;

	// private boolean isFirstGetData =false;
	private ImageView noimage;

	private LinearLayout serialListview;
	private LinearLayout mTravelListViewFooter;

	private int screenWidth;
	private int picWidth;
	private float picRatio;
	private int picHight;
	private boolean isNeedLoad = false;

	private boolean isSelf = false;
	private boolean isClub = false;

	public ClubTravelsFragment(Context mContext, UserInfo userInfo, Boolean isClub) {
		this.mContext = mContext;
		this.userInfo = userInfo;
		this.isClub = isClub;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.club_travels_fragment, null);
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		if (userInfo.getUserId() == null || StringUtils.isEmpty(PreferenceUtil.getUserId())) {
		} else {
			if (userInfo.getUserId().equals(PreferenceUtil.getUserId())) {
				isSelf = true;
			}
		}
		screenWidth = wm.getDefaultDisplay().getWidth();
		initView(view);
		isLoadmore = true;
		isNeedLoad = true;
		travelList.clear();
		queryMyTrends(userInfo.getUserId(), "", "", "0");
		registerBoradcastReceiver();
		 picWidth = screenWidth - DensityUtil.dip2px(getActivity(), 10);
//		picWidth = screenWidth;
		picHight = picWidth / 31 * 30;
		picRatio = 30 / 31f;
		return view;
	}

	private void initView(View view) {
		noimage = (ImageView) view.findViewById(R.id.noimage);
		serialListview = (LinearLayout) view.findViewById(R.id.pic_and_serial_layout);
		mTravelListViewFooter = (LinearLayout) view.findViewById(R.id.xlistview_footer);
		mTravelListViewFooter.setVisibility(View.VISIBLE);
	}

	DataBean dataBean = new DataBean();
	List<Feed_Schema> List = new ArrayList<>();
	/**
	 * 显示游记列表
	 */
	List<Feed_Schema> feedList = new ArrayList<Feed_Schema>();

	private void queryMyTrends(String userId, String commentRows, String themeName, String focus) {
		if (!isNeedLoad) {
			return;
		}
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		params.put("userId", userId);
		params.put("commentRows", commentRows);
		params.put("tagId", "");
		params.put("themeName", themeName);
		params.put("focus", focus);
		params.put("feedType", "all");
		params.put("time", 0);
		params.put("pFlag", "true");
		if (isClub == true) {
			params.put("isProvider", "true");
		} else {
			params.put("isProvider", "false");
		}
		HttpUtil.get(Urls.get_square_list, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if ("00000".equals(code)) {
					JSONArray data1 = response.optJSONArray("data");
					feedList.clear();
					if (data1 != null) {
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
														imageInfo.setHeight(
																imageList.optJSONObject(k).optString("height"));
														imageInfo.setWidth(
																imageList.optJSONObject(k).optString("width"));
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
														// TODO Auto-generated
														// catch
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
														imageInfo.setHeight(
																imageList.optJSONObject(k).optString("height"));
														imageInfo.setWidth(
																imageList.optJSONObject(k).optString("width"));
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
														// TODO Auto-generated
														// catch
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
										} else

										if (!StringUtils.isEmpty(feedObj.optString("feedType"))
												&& "story".equals(feedObj.optString("feedType"))) {
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
												squareRawData.setPhotoId(rawData.optString("articleId"));// id
												squareRawData.setTitle(rawData.optString("title"));// 连载封面
												squareRawData.setIsLike(rawData.optString("isLike"));// 是否赞
												squareRawData.setLikes(rawData.optString("likes"));// 赞
												squareRawData.setComments(rawData.optString("comments"));// 评论数
												squareRawData.setViews(rawData.optString("views"));// 浏览数
												squareRawData.setCover(rawData.optString("cover"));// 连载封面
												squareRawData.setAbstracts(rawData.optString("abstracts"));// 大游记的摘要
												ArrayList<ImageInfo> squareImageList = new ArrayList<>();
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
						}

						if (feedList.size() == 0) {
							noimage.setVisibility(View.VISIBLE);
						} else {
							noimage.setVisibility(View.GONE);
						}
						for (int i = 0; i < feedList.size(); i++) {
							if ("photo".equals(feedList.get(i).getFeedType())
									|| "series".equals(feedList.get(i).getFeedType())
									|| "story".equals(feedList.get(i).getFeedType())) {
								List.add(feedList.get(i));
							}
						}
						if (List.size() > 0 && List.size() % pageSize == 0) {
							isNeedLoad = true;
							++pageNum;
						} else {
							mTravelListViewFooter.setVisibility(View.GONE);
							isNeedLoad = false;
						}
						for (int i = 0; i < feedList.size(); i++) {
							addItem(i, serialListview);
						}
						mTravelListViewFooter.setVisibility(View.GONE);
					} else {
						// if (pageNum != 1) {
						// pageNum--;
						// }
						mTravelListViewFooter.setVisibility(View.GONE);
					}
				}
				isLoadmore = false;

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				isLoadmore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				isLoadmore = false;
				if (pageNum != 1) {
					pageNum--;
				}
			}

		});
	}

	private void addItem(final int position, LinearLayout serialListview) {
		final Feed_Schema feed_Schema = feedList.get(position);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.club_my_squer_item, null);
		ImageView product_servicer_icon_iv = (ImageView) view.findViewById(R.id.product_servicer_icon_iv);
		ImageView imageall = (ImageView) view.findViewById(R.id.imageall);
		TextView noguanzhu = (TextView) view.findViewById(R.id.noguanzhu);
		TextView tvPost = (TextView) view.findViewById(R.id.tvPost);
		TextView tvName = (TextView) view.findViewById(R.id.tvName);
		TextView story_desc = (TextView) view.findViewById(R.id.story_desc);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		TextView access = (TextView) view.findViewById(R.id.access);
		TextView map_location_text = (TextView) view.findViewById(R.id.map_location_text);
		final TextView tv_zannum = (TextView) view.findViewById(R.id.tv_zannum);
		TextView tv_reviewnum = (TextView) view.findViewById(R.id.tv_reviewnum);
		TextView tv_pv = (TextView) view.findViewById(R.id.tv_pv);
		MyListView review_listview = (MyListView) view.findViewById(R.id.review_listview);
		FlowLayout tags = (FlowLayout) view.findViewById(R.id.tags);
		ImageView image = (ImageView) view.findViewById(R.id.image);
		final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
		ImageView guanzhu_pic = (ImageView) view.findViewById(R.id.guanzhu_pic);
		View line = (View) view.findViewById(R.id.line);
		View view1 = (View) view.findViewById(R.id.view);
		ImageView superStar = (ImageView) view.findViewById(R.id.starperson);
		TextView story_title = (TextView) view.findViewById(R.id.story_title);
		TextView tv_picnum = (TextView) view.findViewById(R.id.tv_picnum);
		LinearLayout relative = (LinearLayout) view.findViewById(R.id.relative);
		RelativeLayout reviewRelative = (RelativeLayout) view.findViewById(R.id.reviewRelative);
		RelativeLayout zanRelative = (RelativeLayout) view.findViewById(R.id.zanrelative);
		RelativeLayout imageRelative = (RelativeLayout) view.findViewById(R.id.imageRelative);
		LinearLayout addressRelative = (LinearLayout) view.findViewById(R.id.addressRelative);
		// 统一图片尺寸
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		float width = wm.getDefaultDisplay().getWidth();
		final ViewGroup.LayoutParams lp = imageRelative.getLayoutParams();
		lp.width = picWidth;
		lp.height = picHight;
		/*
		 * isClub 表示是否为俱乐部 true 是 false 不是为俱乐部的成员
		 * 
		 * 关注的控件显示 1. 只有在是俱乐部的
		 *
		 * 界面包含 1.游记的晒图，连载，大游记
		 * 
		 */
		if ("photo".equals(feed_Schema.getFeedType())) {
			if (isClub == true) {
				product_servicer_icon_iv.setEnabled(true);
				image.setVisibility(View.GONE);
				tags.setVisibility(View.GONE);
				addressRelative.setVisibility(View.GONE);
				guanzhu_pic.setVisibility(View.VISIBLE);
				noguanzhu.setVisibility(View.VISIBLE);
			} else {
				product_servicer_icon_iv.setEnabled(false);
				image.setVisibility(View.VISIBLE);
				tags.setVisibility(View.VISIBLE);
				addressRelative.setVisibility(View.VISIBLE);
				guanzhu_pic.setVisibility(View.GONE);
				noguanzhu.setVisibility(View.GONE);
			}
			/*
			 * 晒图和连载对应的隐藏的控件
			 * 
			 */
			story_desc.setVisibility(View.GONE);
			image.setVisibility(View.GONE);
			relative.setVisibility(View.VISIBLE);
			/*
			 * isSelf 表示自己 隐藏 游记的公开，自己可见状态
			 * 
			 */
			if (isSelf == true) {
				access.setVisibility(View.VISIBLE);
			} else {
				access.setVisibility(View.GONE);
			}
			guanzhu_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					showPopu(position, feed_Schema, v);
				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getAccess())) {
				if ("all".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("公开");
				} else if ("friends".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("好友可见");
				} else if ("private".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("自己可见");
				}
			}

			// 点赞的点击
			zanRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(getActivity(), LoginActivity.class);
						((Activity) getActivity()).startActivity(it);
						return;
					}
					// if
					// (!feed_Schema.getPublisher().getUserId().equals(PreferenceUtil.getUserId()))
					// {
					if (feed_Schema.getRawData().getIsLike().equals("0")
							|| StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "tripstory", 1, position);
						feed_Schema.getRawData().setIsLike("1");
						feed_Schema.getRawData()
								.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) + 1));
					} else if ((feed_Schema.getRawData().getIsLike().equals("1"))) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "tripstory", 0, position);
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							feed_Schema.getRawData()
									.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) - 1));
						}

						feed_Schema.getRawData().setIsLike("0");
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						tv_zannum.setText(feed_Schema.getRawData().getLikes());
						image1.setBackgroundResource(R.drawable.nozan);
					} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						image1.setBackgroundResource(R.drawable.zan2x);
						tv_zannum.setText(feed_Schema.getRawData().getLikes());

					}
				}
				// }
			});

			if (!StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
				if (feed_Schema.getRawData().getIsLike().equals("0")) {
					image1.setBackgroundResource(R.drawable.nozan);
				} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
					image1.setBackgroundResource(R.drawable.zan2x);
				}
			}
			if (!StringUtils.isEmpty(feed_Schema.getDesc())) {
				tvPost.setText(feed_Schema.getDesc());
			}
			// 达人图标
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getIsTalent())) {
				if ("1".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.VISIBLE);
				} else if ("0".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.GONE);
				}
			}
			// 评论列表点击
			reviewRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(getActivity(), LoginActivity.class);
						((Activity) getActivity()).startActivity(it);
						return;
					}
					Intent reviewintent = new Intent(getActivity(), UserMicroCommentActivity.class);
					reviewintent.putExtra("objectType", "0");
					reviewintent.putExtra("microId", feed_Schema.getRawData().getPhotoId());
					String counts = StringUtils.isEmpty(feed_Schema.getRawData().getComments()) ? "0"
							: feed_Schema.getRawData().getComments();
					if (counts.equals("0")) {
						reviewintent.putExtra("count", 0);
					} else {
						reviewintent.putExtra("count", Integer.parseInt(counts));
					}
					((Activity) getActivity()).startActivityForResult(reviewintent, 3);
					((Activity) getActivity()).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			// 设置晒图人头像
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getPublisher().getAvatar(),
						product_servicer_icon_iv, AppConfig.options(R.drawable.user_defult_photo));
			} else {
				product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			// 设置发表的人
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getNickname())) {
				tvName.setText(feed_Schema.getPublisher().getNickname());
			} else if (!StringUtils.isEmpty(feed_Schema.getPublisher().getRealName())) {
				tvName.setText(feed_Schema.getPublisher().getRealName());
			} else {
				tvName.setText("");
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getDesc())) {
				story_title.setVisibility(View.VISIBLE);
				List<RichText> richTexts = StringUtils.xmlToRichText(feed_Schema.getRawData().getDesc());
				StringUtils.initRichTextView1(story_title, richTexts);
			} else {
				story_title.setVisibility(View.GONE);
			}
			setTime(tvTime, feed_Schema);
			// 设置封面
			// imageall.setLayoutParams(lp);
			// view1.setLayoutParams(lp);
			if (feed_Schema.getRawData().getPictureList().size() > 0) {
				ImageInfo imageInfo = feed_Schema.getRawData().getPictureList().get(0);
				try {
					float h = Float.parseFloat(imageInfo.getHeight());
					float w = Float.parseFloat(imageInfo.getWidth());
					if (h > 0 && w > 0) {
						float ratio = h / w;
						if (ratio < picRatio) {
							lp.height = (int) (picWidth / w * h);
							lp.width = picWidth;
						}
					}
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}

			}
			imageRelative.setLayoutParams(lp);
			if (feed_Schema.getRawData().getPictureList().size() > 0) {
				if (!StringUtils.isEmpty(feed_Schema.getRawData().getPictureList().get(0).getUrl())) {
					ImageLoader.getInstance().displayImage(
							Urls.imgHost + feed_Schema.getRawData().getPictureList().get(0).getUrl(), imageall,
							AppConfig.options(R.drawable.ic_launcher));
				}
				// 设置图片数量
				int num = feed_Schema.getRawData().getPictureList().size();
				if (num > 0) {
					tv_picnum.setText(String.valueOf(num));
				}
			} else {
				imageall.setImageResource(R.drawable.ic_launcher);
			}
			imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SquarePicDetailActivity.class);
					intent.putExtra("id", feed_Schema.getRawData().getPhotoId());
					getActivity().startActivity(intent);
					((Activity) getActivity()).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			// 发布地址
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLocation())
					&& !feed_Schema.getRawData().getLocation().equals("不显示")) {
				addressRelative.setVisibility(View.VISIBLE);
				map_location_text.setText(feed_Schema.getRawData().getLocation());
			} else {
				addressRelative.setVisibility(View.GONE);
			}
			String userId = PreferenceUtil.getUserId();

			// 赞的数目
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLikes())) {
				if ("0".equals(feed_Schema.getRawData().getLikes())) {
					tv_zannum.setVisibility(View.GONE);
				} else {
					tv_zannum.setVisibility(View.VISIBLE);
					tv_zannum.setText(feed_Schema.getRawData().getLikes());
				}
			}
			// 评论数目
			tv_reviewnum.setText(StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0") ? ""
							: feed_Schema.getRawData().getComments());
			if (StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0")) {
				line.setVisibility(View.GONE);
				review_listview.setVisibility(View.GONE);
			} else {
				review_listview.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
			}
			// 浏览人数
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getViews())) {
				if ("0".equals(feed_Schema.getRawData().getViews())) {
					tv_pv.setText("");
				} else {
					tv_pv.setText("浏览" + feed_Schema.getRawData().getViews());
				}
			}
			// 评论列表
			showReview(review_listview, feed_Schema);
			// 标签
			if (feed_Schema.getRawData().getTagNames().size() > 0) {
				addFlowView(feed_Schema.getRawData().getTagNames(), tags);
			}
			noguanzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addOrCancelFocus(false, feed_Schema.getPublisher().getUserId(), position);
				}
			});
			product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityUtil.squareToPersonHomePage(feed_Schema.getPublisher(), getActivity());
				}
			});
			if (isClub) {
				// 关注
				if (!StringUtils.isEmpty(feed_Schema.getIsFocus())) {
					if (PreferenceUtil.getUserId().equals(feed_Schema.getPublisher().getUserId())) {
						noguanzhu.setVisibility(View.GONE);
						guanzhu_pic.setVisibility(View.GONE);
					} else {
						if ("no".equals(feed_Schema.getIsFocus())) {
							noguanzhu.setVisibility(View.VISIBLE);
							noguanzhu.setText("+关注");
							noguanzhu.setEnabled(true);
							guanzhu_pic.setVisibility(View.GONE);
							noguanzhu.setBackgroundResource(R.drawable.shape_button);
						} else if ("yes".equals(feed_Schema.getIsFocus())) {
							noguanzhu.setVisibility(View.VISIBLE);
							noguanzhu.setText("已关注");
							noguanzhu.setEnabled(false);
							noguanzhu.setBackgroundDrawable(new BitmapDrawable());
							guanzhu_pic.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			serialListview.addView(view);
			return;
		}

		if ("series".equals(feed_Schema.getFeedType())) {
			if (isClub == true) {
				product_servicer_icon_iv.setEnabled(true);
				relative.setVisibility(View.GONE);
				image.setVisibility(View.GONE);
				tags.setVisibility(View.GONE);
				addressRelative.setVisibility(View.GONE);
				guanzhu_pic.setVisibility(View.VISIBLE);
				noguanzhu.setVisibility(View.VISIBLE);
			} else {
				product_servicer_icon_iv.setEnabled(false);
				relative.setVisibility(View.VISIBLE);
				image.setVisibility(View.VISIBLE);
				tags.setVisibility(View.VISIBLE);
				addressRelative.setVisibility(View.VISIBLE);
				guanzhu_pic.setVisibility(View.GONE);
				noguanzhu.setVisibility(View.GONE);
			}
			/*
			 * isSelf 表示自己 隐藏 游记的公开，自己可见状态
			 * 
			 */
			if (isSelf == true) {
				access.setVisibility(View.VISIBLE);
			} else {
				access.setVisibility(View.GONE);
			}
			/*
			 * 晒图和连载对应的隐藏的控件
			 * 
			 */
			story_desc.setVisibility(View.GONE);
			guanzhu_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					showPopu(position, feed_Schema, v);

				}
			});
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getAccess())) {
				if ("all".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("公开");
				} else if ("friends".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("好友可见");
				} else if ("private".equals(feed_Schema.getRawData().getAccess())) {
					access.setText("自己可见");
				}
			}
			relative.setVisibility(View.GONE);
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
				if (feed_Schema.getRawData().getIsLike().equals("0")) {
					image1.setBackgroundResource(R.drawable.nozan);
				} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
					image1.setBackgroundResource(R.drawable.zan2x);
				}
			}

			// 点赞的点击
			zanRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(getActivity(), LoginActivity.class);
						((Activity) getActivity()).startActivity(it);
						return;
					}
					// if
					// (!feed_Schema.getPublisher().getUserId().equals(PreferenceUtil.getUserId()))
					// {
					if (feed_Schema.getRawData().getIsLike().equals("0")
							|| StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "series", 1, position);
						feed_Schema.getRawData().setIsLike("1");
						feed_Schema.getRawData()
								.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) + 1));
					} else if ((feed_Schema.getRawData().getIsLike().equals("1"))) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "series", 0, position);
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							feed_Schema.getRawData()
									.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) - 1));
						}

						feed_Schema.getRawData().setIsLike("0");
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						tv_zannum.setText(feed_Schema.getRawData().getLikes());
						image1.setBackgroundResource(R.drawable.nozan);
					} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						image1.setBackgroundResource(R.drawable.zan2x);
						tv_zannum.setText(feed_Schema.getRawData().getLikes());

					}
					// }
				}
			});

			if (!StringUtils.isEmpty(feed_Schema.getDesc())) {
				tvPost.setText(feed_Schema.getDesc());
			}
			// 评论点击事件
			reviewRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(getActivity(), LoginActivity.class);
						((Activity) getActivity()).startActivity(it);
						return;
					}
					Intent reviewintent = new Intent(getActivity(), UserMicroCommentActivity.class);
					reviewintent.putExtra("objectType", "1");
					reviewintent.putExtra("microId", feed_Schema.getRawData().getPhotoId());
					String counts = StringUtils.isEmpty(feed_Schema.getRawData().getComments()) ? "0"
							: feed_Schema.getRawData().getComments();
					if (counts.equals("0")) {
						reviewintent.putExtra("count", 0);
					} else {
						reviewintent.putExtra("count", Integer.parseInt(counts));
					}
					((Activity) getActivity()).startActivityForResult(reviewintent, 3);
					((Activity) getActivity()).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});

			// 地点
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLocation())
					&& !feed_Schema.getRawData().getLocation().equals("不显示")) {
				addressRelative.setVisibility(View.VISIBLE);
				map_location_text.setText(feed_Schema.getRawData().getLocation());
			} else {
				addressRelative.setVisibility(View.GONE);
			}

			// 达人图标
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getIsTalent())) {
				if ("1".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.VISIBLE);
				} else if ("0".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.GONE);
				}
			}
			// 设置晒图人头像
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getPublisher().getAvatar(),
						product_servicer_icon_iv, AppConfig.options(R.drawable.user_defult_photo));
			} else {
				product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			// 设置发表的人
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getNickname())) {
				tvName.setText(feed_Schema.getPublisher().getNickname());
			} else if (!StringUtils.isEmpty(feed_Schema.getPublisher().getRealName())) {
				tvName.setText(feed_Schema.getPublisher().getRealName());
			} else {
				tvName.setText("");
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getTitle())) {
				story_title.setVisibility(View.VISIBLE);
				story_title.setText(feed_Schema.getRawData().getTitle());
			} else {
				story_title.setVisibility(View.GONE);
			}
			// 设置发送时间
			setTime(tvTime, feed_Schema);
			// 设置封面

			imageRelative.setLayoutParams(lp);
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getCover())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getRawData().getCover(),
						imageall, AppConfig.options(R.drawable.ic_launcher),new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								float h=loadedImage.getHeight();
								float w=loadedImage.getWidth();
//								ViewGroup.LayoutParams lp =view.getLayoutParams();
								if(h>0&&w>0){
									float ratio=h/w;
									if(ratio<picRatio){
										lp.height=(int) (picWidth/w*h);
										lp.width=picWidth;
										
									}
								}
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								
							}
						});
			} else {
				imageall.setImageResource(R.drawable.ic_launcher);
			}
			imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SquareSerialDetailActivity.class);
					intent.putExtra("id", feed_Schema.getRawData().getPhotoId());
					getActivity().startActivity(intent);
					((Activity) getActivity()).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});

			// 赞的数目
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLikes())) {
				if ("0".equals(feed_Schema.getRawData().getLikes())) {
					tv_zannum.setVisibility(View.GONE);
				} else {
					tv_zannum.setVisibility(View.VISIBLE);
					tv_zannum.setText(feed_Schema.getRawData().getLikes());
				}
			}

			// 评论数目
			tv_reviewnum.setText(StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0") ? ""
							: feed_Schema.getRawData().getComments());
			if (StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0")) {
				line.setVisibility(View.GONE);
				review_listview.setVisibility(View.GONE);
			} else {
				review_listview.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
			}
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getViews())) {
				if ("0".equals(feed_Schema.getRawData().getViews())) {
					tv_pv.setText("");
				} else {
					tv_pv.setText("浏览" + feed_Schema.getRawData().getViews());
				}
			}
			// 评论列表
			showReview(review_listview, feed_Schema);
			if (feed_Schema.getRawData().getTagNames().size() > 0) {
				addFlowView(feed_Schema.getRawData().getTagNames(), tags);
			}
			noguanzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addOrCancelFocus(false, feed_Schema.getPublisher().getUserId(), position);
				}
			});
			product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// toClub(feed_Schema);
					ActivityUtil.squareToPersonHomePage(feed_Schema.getPublisher(), getActivity());
				}
			});
			if (isClub) {
				// 关注
				if (!StringUtils.isEmpty(feed_Schema.getIsFocus())) {
					if (PreferenceUtil.getUserId().equals(feed_Schema.getPublisher().getUserId())) {
						noguanzhu.setVisibility(View.GONE);
						guanzhu_pic.setVisibility(View.GONE);
					} else {
						if ("no".equals(feed_Schema.getIsFocus())) {
							noguanzhu.setVisibility(View.VISIBLE);
							noguanzhu.setText("+关注");
							noguanzhu.setEnabled(true);
							guanzhu_pic.setVisibility(View.GONE);
							noguanzhu.setBackgroundResource(R.drawable.shape_button);
						} else if ("yes".equals(feed_Schema.getIsFocus())) {
							noguanzhu.setVisibility(View.VISIBLE);
							noguanzhu.setText("已关注");
							noguanzhu.setEnabled(false);
							noguanzhu.setBackgroundDrawable(new BitmapDrawable());
							guanzhu_pic.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			serialListview.addView(view);
			return;
		}

		if ("story".equals(feed_Schema.getFeedType())) {
			if (isClub == true) {
				product_servicer_icon_iv.setEnabled(true);
				relative.setVisibility(View.GONE);
				image.setVisibility(View.GONE);
				tags.setVisibility(View.GONE);
				addressRelative.setVisibility(View.GONE);
				guanzhu_pic.setVisibility(View.VISIBLE);
				noguanzhu.setVisibility(View.VISIBLE);
			} else {
				product_servicer_icon_iv.setEnabled(false);
				relative.setVisibility(View.VISIBLE);
				image.setVisibility(View.VISIBLE);
				tags.setVisibility(View.VISIBLE);
				addressRelative.setVisibility(View.VISIBLE);
				guanzhu_pic.setVisibility(View.GONE);
				noguanzhu.setVisibility(View.GONE);
			} /*
				 * 晒图和连载对应的隐藏的控件
				 * 
				 */
			story_desc.setVisibility(View.VISIBLE);
			image.setVisibility(View.GONE);
			/*
			 * isSelf 表示自己
			 * 
			 */
			if (isSelf == true) {
				access.setVisibility(View.VISIBLE);
			} else {
				access.setVisibility(View.GONE);
			}
			guanzhu_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					showPopu(position, feed_Schema, v);
				}
			});
			// 点赞的点击
			zanRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(getActivity(), LoginActivity.class);
						((Activity) getActivity()).startActivity(it);
						return;
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")
							|| StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "story", 1, position);
						feed_Schema.getRawData().setIsLike("1");
						feed_Schema.getRawData()
								.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) + 1));
					} else if ((feed_Schema.getRawData().getIsLike().equals("1"))) {
						setZanInfo(feed_Schema.getRawData().getPhotoId(), "story", 0, position);
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							feed_Schema.getRawData()
									.setLikes("" + (Integer.parseInt(feed_Schema.getRawData().getLikes()) - 1));
						}

						feed_Schema.getRawData().setIsLike("0");
					}
					if (feed_Schema.getRawData().getIsLike().equals("0")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						tv_zannum.setText(feed_Schema.getRawData().getLikes());
						image1.setBackgroundResource(R.drawable.nozan);
					} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
						if (!"0".equals(feed_Schema.getRawData().getLikes())) {
							tv_zannum.setVisibility(View.VISIBLE);
						} else {
							tv_zannum.setVisibility(View.GONE);
						}
						image1.setBackgroundResource(R.drawable.zan2x);
						tv_zannum.setText(feed_Schema.getRawData().getLikes());

					}
				}
				// }
			});

			if (!StringUtils.isEmpty(feed_Schema.getRawData().getIsLike())) {
				if (feed_Schema.getRawData().getIsLike().equals("0")) {
					image1.setBackgroundResource(R.drawable.nozan);
				} else if (feed_Schema.getRawData().getIsLike().equals("1")) {
					image1.setBackgroundResource(R.drawable.zan2x);
				}
			}
			if (!StringUtils.isEmpty(feed_Schema.getDesc())) {
				tvPost.setText(feed_Schema.getDesc());
			}
			// 达人图标
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getIsTalent())) {
				if ("1".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.VISIBLE);
				} else if ("0".equals(feed_Schema.getPublisher().getIsTalent())) {
					superStar.setVisibility(View.GONE);
				}
			}
			// 评论列表点击
			reviewRelative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!AppInfo.getIsLogin()) {
						Intent it = new Intent(getActivity(), LoginActivity.class);
						((Activity) getActivity()).startActivity(it);
						return;
					}
					Intent reviewintent = new Intent(getActivity(), UserMicroCommentActivity.class);
					reviewintent.putExtra("objectType", "2");
					reviewintent.putExtra("microId", feed_Schema.getRawData().getPhotoId());
					String counts = StringUtils.isEmpty(feed_Schema.getRawData().getComments()) ? "0"
							: feed_Schema.getRawData().getComments();
					if (counts.equals("0")) {
						reviewintent.putExtra("count", 0);
					} else {
						reviewintent.putExtra("count", Integer.parseInt(counts));
					}
					((Activity) getActivity()).startActivityForResult(reviewintent, 3);
					((Activity) getActivity()).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			// 设置晒图人头像
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getAvatar())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getPublisher().getAvatar(),
						product_servicer_icon_iv, AppConfig.options(R.drawable.user_defult_photo));
			} else {
				product_servicer_icon_iv.setImageResource(R.drawable.ic_launcher);
			}
			product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityUtil.squareToPersonHomePage(feed_Schema.getPublisher(), getActivity());
				}
			});
			// 设置发表的人
			if (!StringUtils.isEmpty(feed_Schema.getPublisher().getNickname())) {
				tvName.setText(feed_Schema.getPublisher().getNickname());
			} else if (!StringUtils.isEmpty(feed_Schema.getPublisher().getRealName())) {
				tvName.setText(feed_Schema.getPublisher().getRealName());
			} else {
				tvName.setText("");
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getAbstracts())) {
				story_title.setVisibility(View.VISIBLE);
				story_title.setText(feed_Schema.getRawData().getAbstracts());
			} else {
				story_title.setVisibility(View.GONE);
			}
			// 设置标题
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getTitle())) {
				story_desc.setVisibility(View.VISIBLE);
				story_desc.setText(feed_Schema.getRawData().getTitle());
			} else {
				story_desc.setVisibility(View.GONE);
			}
			setTime(tvTime, feed_Schema);
			// 设置封面
			imageRelative.setLayoutParams(lp);
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getCover())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + feed_Schema.getRawData().getCover(), imageall,
						AppConfig.options(R.drawable.ic_launcher));
			} else {
				imageall.setImageResource(R.drawable.ic_launcher);
			}
			imageall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String path = Urls.content_host + feed_Schema.getRawData().getPhotoId();
					Intent intent = new Intent(getActivity(), ContentActivity.class);
					intent.putExtra("path", path);
					getActivity().startActivity(intent);
					((Activity) getActivity()).overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
				}
			});
			// 赞的数目
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getLikes())) {
				if ("0".equals(feed_Schema.getRawData().getLikes())) {
					tv_zannum.setVisibility(View.GONE);
				} else {
					tv_zannum.setVisibility(View.VISIBLE);
					tv_zannum.setText(feed_Schema.getRawData().getLikes());
				}
			}
			// 评论数目
			tv_reviewnum.setText(StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0") ? ""
							: feed_Schema.getRawData().getComments());
			if (StringUtils.isEmpty(feed_Schema.getRawData().getComments())
					|| feed_Schema.getRawData().getComments().equals("0")) {
				line.setVisibility(View.GONE);
				review_listview.setVisibility(View.GONE);
			} else {
				review_listview.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
			}
			// 浏览人数
			if (!StringUtils.isEmpty(feed_Schema.getRawData().getViews())) {
				if ("0".equals(feed_Schema.getRawData().getViews())) {
					tv_pv.setText("");
				} else {
					tv_pv.setText("浏览" + feed_Schema.getRawData().getViews());
				}
			}
			// 评论列表
			showReview(review_listview, feed_Schema);
			noguanzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addOrCancelFocus(false, feed_Schema.getPublisher().getUserId(), position);
				}
			});
			// 关注
			if (!StringUtils.isEmpty(feed_Schema.getIsFocus())) {
				if (PreferenceUtil.getUserId().equals(feed_Schema.getPublisher().getUserId())) {
					noguanzhu.setVisibility(View.GONE);
					guanzhu_pic.setVisibility(View.GONE);
				} else {
					if ("no".equals(feed_Schema.getIsFocus())) {
						noguanzhu.setVisibility(View.VISIBLE);
						noguanzhu.setText("+关注");
						noguanzhu.setEnabled(true);
						guanzhu_pic.setVisibility(View.GONE);
						noguanzhu.setBackgroundResource(R.drawable.shape_button);
					} else if ("yes".equals(feed_Schema.getIsFocus())) {
						noguanzhu.setVisibility(View.VISIBLE);
						noguanzhu.setText("已关注");
						noguanzhu.setEnabled(false);
						noguanzhu.setBackgroundDrawable(new BitmapDrawable());
						guanzhu_pic.setVisibility(View.VISIBLE);
					}
				}
			}
			serialListview.addView(view);
			return;
		}

	}

	// 设置时间
	private void setTime(TextView tvTime, final Feed_Schema feed_Schema) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			long time = (date.getTime() - format.parse(feed_Schema.getPublishTime()).getTime()) / 1000;
			long day1 = time / (24 * 3600);
			long hour1 = time % (24 * 3600) / 3600;
			long minute1 = time % 3600 / 60;
			if (day1 > 0) {
				tvTime.setText(DateUtil.getFormateDate1(feed_Schema.getPublishTime()));
			} else if (hour1 > 0) {
				tvTime.setText(hour1 + "小时前");
			} else if (minute1 > 0) {
				tvTime.setText(minute1 + "分钟前");
			} else {
				if (time <= 0) {
					tvTime.setText("刚刚");
				} else {
					tvTime.setText(time + "秒前");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 评论列表
	 */
	private void showReview(ListView review_listview, final Feed_Schema feed_Schema) {
		MicroBlogsCommentsAdapter adapter1 = (MicroBlogsCommentsAdapter) review_listview.getAdapter();
		int count = 0;
		if (adapter1 == null) {
			ArrayList<Comments> commentsList = new ArrayList<Comments>();
			for (Comments comments : feed_Schema.getRawData().getCommentList()) {
				count++;
				if (count > 2) {
					break;
				}
				commentsList.add(comments);
			}
			review_listview.setAdapter(new MicroBlogsCommentsAdapter(getActivity(), commentsList));
		} else {
			count = 0;
			MicroBlogsCommentsAdapter adapter = (MicroBlogsCommentsAdapter) adapter1;
			ArrayList<Comments> commentsList = adapter.getCommentsList();
			commentsList.clear();
			for (Comments comments : feed_Schema.getRawData().getCommentList()) {
				count++;
				if (count > 2) {
					break;
				}
				commentsList.add(comments);
			}
			adapter.notifyDataSetChanged();
		}
	}

	@SuppressLint("ResourceAsColor")
	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		flowLayout.removeAllViews();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(getActivity());
			newView.setBackgroundResource(R.drawable.shape_club_tag2);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#FFFFFF"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(getActivity(), 1);
			params.bottomMargin = DensityUtil.dip2px(getActivity(), 1);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	// 点赞
	private void setZanInfo(final String id, String objectType, final int like, final int position) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("objectId", id);
			jsonObject.put("objectType", objectType);
			jsonObject.put("opType", like);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.set_like, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					if (response.optString("code").equals("00000")) {
						if (like == 1) {
							Toast.makeText(getActivity(), "点赞 +1", Toast.LENGTH_SHORT).show();
						}
						Intent it = new Intent("com.bcinfo.refreshLikesCount");
						it.putExtra("microId", id);
						it.putExtra("like", like);
						getActivity().sendBroadcast(it);
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		myIntentFilter.addAction("com.bcinfo.publishBlog2");
		myIntentFilter.addAction("com.bcinfo.refreshFocus");
		myIntentFilter.addAction("com.bcinfo.squre");
		myIntentFilter.addAction("com.bcinfo.haveLogin");
		myIntentFilter.addAction("com.bcinfo.loadMoreTravels");
		myIntentFilter.addAction("com.bcinfo.clubToFragmentRefreshFocus");

		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void unRegisterBoradcastReceiver() {
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.bcinfo.refreshCommentsCount")) {
				String count = intent.getStringExtra("count");
				String microId = intent.getStringExtra("microId");
				ArrayList<Comments> comments = intent.getParcelableArrayListExtra("comments");
				if (count != null && microId != null) {
					for (int i = 0; i < List.size(); i++) {
						if (List.get(i).getRawData() != null
								&& !StringUtils.isEmpty(List.get(i).getRawData().getPhotoId())) {
							if (List.get(i).getRawData().getPhotoId().equals(microId)) {
								List.get(i).getRawData().setComments(count);
								List.get(i).getRawData().setCommentList(comments);
								// 评论数目
								((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_reviewnum))
										.setText(StringUtils.isEmpty(List.get(i).getRawData().getComments())
												|| List.get(i).getRawData().getComments().equals("0") ? ""
														: List.get(i).getRawData().getComments());
								if (StringUtils.isEmpty(List.get(i).getRawData().getComments())
										|| List.get(i).getRawData().getComments().equals("0")) {
									((View) serialListview.getChildAt(i).findViewById(R.id.line))
											.setVisibility(View.GONE);
									((MyListView) serialListview.getChildAt(i).findViewById(R.id.review_listview))
											.setVisibility(View.GONE);
								} else {
									((MyListView) serialListview.getChildAt(i).findViewById(R.id.review_listview))
											.setVisibility(View.VISIBLE);
									((View) serialListview.getChildAt(i).findViewById(R.id.line))
											.setVisibility(View.VISIBLE);
								}

								// 评论列表
								showReview(
										((MyListView) serialListview.getChildAt(i).findViewById(R.id.review_listview)),
										List.get(i));
								break;
							}
						}
					}
				}
			} else if ("com.bcinfo.refreshLikesCount".equals(action)) {
				int like = intent.getIntExtra("like", 0);
				String microId = intent.getStringExtra("microId");
				if (microId != null) {
					for (int i = 0; i < List.size(); i++) {
						if (List.get(i).getRawData() != null) {
							if (!StringUtils.isEmpty(List.get(i).getRawData().getPhotoId())) {
								if (microId.equals(List.get(i).getRawData().getPhotoId())) {
									String isLike = List.get(i).getRawData().getIsLike();
									int likes = 0;
									if (!StringUtils.isEmpty(List.get(i).getRawData().getLikes())) {
										likes = Integer.parseInt(List.get(i).getRawData().getLikes());
									}
									if (isLike.equals("0") && like == 1) {
										List.get(i).getRawData().setIsLike("1");
										List.get(i).getRawData().setLikes(Integer.toString(likes + 1));
										if ("0".equals(List.get(i).getRawData().getLikes())) {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.GONE);
										} else {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.VISIBLE);
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setText(List.get(i).getRawData().getLikes());
										}
										serialListview.getChildAt(i).findViewById(R.id.image1)
												.setBackgroundResource(R.drawable.zan2x);
									} else if (isLike.equals("1") && like == 0) {
										List.get(i).getRawData().setIsLike("0");
										List.get(i).getRawData().setLikes(Integer.toString(likes - 1));
										if ("0".equals(List.get(i).getRawData().getLikes())) {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.GONE);
										} else {
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setVisibility(View.VISIBLE);
											((TextView) serialListview.getChildAt(i).findViewById(R.id.tv_zannum))
													.setText(List.get(i).getRawData().getLikes());
										}
										serialListview.getChildAt(i).findViewById(R.id.image1)
												.setBackgroundResource(R.drawable.nozan);
									}

									break;
								}
							}
						}
					}
				}

			} else if ("com.bcinfo.publishBlog2".equals(action)) {
				pageNum = 1;
				List.clear();
				isNeedLoad = true;
				mTravelListViewFooter.setVisibility(View.VISIBLE);
				serialListview.removeAllViews();
				queryMyTrends(userInfo.getUserId(), "", "", "0");
			} else if ("com.bcinfo.loadMoreTravels".equals(action)) {
				queryMyTrends(userInfo.getUserId(), "", "", "0");
			} else if ("com.bcinfo.clubToFragmentRefreshFocus".equals(action)) {
				String userId = intent.getStringExtra("userId");
				for (int i = 0; i < List.size(); i++) {
					if (userId.equals(List.get(i).getPublisher().getUserId())) {
						if (intent.getBooleanExtra("isFocus", false) == true) {
							List.get(i).setIsFocus("yes");
						}
						if (intent.getBooleanExtra("isFocus", false) == false) {
							List.get(i).setIsFocus("no");
						}
						if (isClub) {
							// 关注
							if (!StringUtils.isEmpty(List.get(i).getIsFocus())) {
								if (PreferenceUtil.getUserId().equals(List.get(i).getPublisher().getUserId())) {
									serialListview.getChildAt(i).findViewById(R.id.noguanzhu).setVisibility(View.GONE);
									serialListview.getChildAt(i).findViewById(R.id.guanzhu_pic)
											.setVisibility(View.GONE);
								} else {
									if ("no".equals(List.get(i).getIsFocus())) {
										serialListview.getChildAt(i).findViewById(R.id.noguanzhu)
												.setVisibility(View.VISIBLE);
										((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
												.setText("+关注");
										serialListview.getChildAt(i).findViewById(R.id.noguanzhu).setEnabled(true);
										serialListview.getChildAt(i).findViewById(R.id.guanzhu_pic)
												.setVisibility(View.GONE);
										serialListview.getChildAt(i).findViewById(R.id.noguanzhu)
												.setBackgroundResource(R.drawable.shape_button);
									} else if ("yes".equals(List.get(i).getIsFocus())) {
										serialListview.getChildAt(i).findViewById(R.id.noguanzhu)
												.setVisibility(View.VISIBLE);
										((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
												.setText("已关注");
										serialListview.getChildAt(i).findViewById(R.id.noguanzhu).setEnabled(false);
										serialListview.getChildAt(i).findViewById(R.id.noguanzhu)
												.setBackgroundDrawable(new BitmapDrawable());
										serialListview.getChildAt(i).findViewById(R.id.guanzhu_pic)
												.setVisibility(View.VISIBLE);
									}
								}
							}
						}

					}

				}
			}

		};
	};

	private PopupWindow pw;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == R.id.my_travels_listview) {
			Feed feed = (Feed) parent.getItemAtPosition(position);
			Object rawData = feed.getMap().get("rawData");
			if (feed.getFeedType().equals("tripstory")) {
				TripArticle article = (TripArticle) rawData;
				Intent blogIntentForDetail = new Intent(mContext, BlogDetailActivity.class);
				// TripArticle tripArticle = (TripArticle) v.getTag();
				// 显示第几张图片
				blogIntentForDetail.putExtra("blog_image_index", 0);
				// 标示ID
				blogIntentForDetail.putExtra("blog_article_id", article.getPhotoId());
				// 图片集
				blogIntentForDetail.putExtra("blog_article_pic_list", article.getPictureList());
				// 描述
				blogIntentForDetail.putExtra("blog_article_description", article.getDescription());
				// 评论条数
				blogIntentForDetail.putExtra("blog_article_comments", article.getComments());
				// 发布时间
				blogIntentForDetail.putExtra("blog_article_current_date", article.getPublishTime());
				// 用户头像
				blogIntentForDetail.putExtra("blog_article_author_avatar", article.getPublisher().getAvatar());
				// 用户信息
				blogIntentForDetail.putExtra("blog_article_author_id", article.getPublisher().getUserId());
				// 昵称
				blogIntentForDetail.putExtra("blog_article_author_nickName", article.getPublisher().getNickname());

				mContext.startActivity(blogIntentForDetail);
				getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
			} else if (feed.getFeedType().equals("story")) {
				Story story = (Story) rawData;
				String path = Urls.content_host + story.getArticleId();
				Intent intent = new Intent(mContext, ContentActivity.class);
				intent.putExtra("path", path);
				mContext.startActivity(intent);
			}
		}
	}

	/*
	 * 弹出取消关注
	 */
	private void showPopu(final int position, final Feed_Schema feed_Schema, final View v) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.hintpw);
		Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.hintpw2);
		anim.setFillAfter(true);

		v.startAnimation(anim);
		// 获取屏幕宽度
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenW = metrics.widthPixels;
		View convView = LayoutInflater.from(getActivity()).inflate(R.layout.squarecannelguanzhupopuwindow, null);
		int gap = DensityUtil.dip2px(getActivity(), 30);
		pw = new PopupWindow(convView, screenW - 2 * gap, LayoutParams.WRAP_CONTENT);
		// 设置pw中的控件可点击
		pw.setFocusable(true);
		// 调用展示方法，设置展示位置
		/*
		 * 在展示pw的同时，让窗口的其余部分显示半透明的颜色
		 */
		WindowManager.LayoutParams params = ((Activity) getActivity()).getWindow().getAttributes();
		params.alpha = 0.6f;
		((Activity) getActivity()).getWindow().setAttributes(params);

		// 设置pw可以在点击外部区域时关闭显示
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);

		// 设置pw弹出和隐藏时的动画效果
		/*
		 * 注意：此处的动画效果是通过style样式指定的
		 */
		// pw.setAnimationStyle(R.style.pw_anim_style);

		// 展示对话框，并设置展示位置

		// 监控pw何时被关闭，并且，在pw被关闭的时候，将窗口的背景色调节回来
		pw.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// TODO Auto-generated method stub
				// 将窗口颜色调回完全透明
				WindowManager.LayoutParams params = ((Activity) getActivity()).getWindow().getAttributes();
				params.alpha = 1;
				((Activity) getActivity()).getWindow().setAttributes(params);
				Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.hintpw2);
				v.startAnimation(anim2);
				anim2.setFillAfter(true);
			}
		});
		pw.showAtLocation(v, Gravity.CENTER | Gravity.CENTER, 0, 0);
		// 设置pw中button的点击事件
		TextView photo = (TextView) convView.findViewById(R.id.photo);
		photo.setOnClickListener(new OnClickListener() {
			Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.hintpw2);

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				addOrCancelFocus(true, feed_Schema.getPublisher().getUserId(), position);
			}
		});
	}

	// 增加取消关注
	private void addOrCancelFocus(boolean flag, final String userId, final int position) {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			return;
		}
		if (!flag) {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", userId);
				StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
				HttpUtil.post(Urls.friend_list_url, entity, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String code = response.optString("code");
						if ("00000".equals(code)) {
							for (int i = 0; i < List.size(); i++) {
								if (List.get(i).getPublisher() != null) {
									if (userId.equals(List.get(i).getPublisher().getUserId())) {
										List.get(i).setIsFocus("yes");
										((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
												.setVisibility(View.VISIBLE);
										((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
												.setText("已关注");
										((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
												.setEnabled(false);
										((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
												.setBackgroundDrawable(new BitmapDrawable());
										((ImageView) serialListview.getChildAt(i).findViewById(R.id.guanzhu_pic))
												.setVisibility(View.VISIBLE);
									}
								}
							}
							ToastUtil.showToast(getActivity(), "关注成功");
							Intent intent = new Intent("com.bcinfo.clubRefreshFocus");
							intent.putExtra("userId", userId);
							intent.putExtra("isFocus", true);
							getActivity().sendBroadcast(intent);
						}
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
			HttpUtil.delete(Urls.friend_list_url + "/" + userId, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if ("00000".equals(code)) {
						for (int i = 0; i < List.size(); i++) {
							if (List.get(i).getPublisher() != null) {
								if (userId.equals(List.get(i).getPublisher().getUserId())) {
									List.get(i).setIsFocus("no");
									((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
											.setVisibility(View.VISIBLE);
									((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
											.setText("+关注");
									((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
											.setEnabled(true);
									((ImageView) serialListview.getChildAt(i).findViewById(R.id.guanzhu_pic))
											.setVisibility(View.GONE);
									((TextView) serialListview.getChildAt(i).findViewById(R.id.noguanzhu))
											.setBackgroundResource(R.drawable.shape_button);
								}
							}
						}
						Intent intent = new Intent("com.bcinfo.clubRefreshFocus");
						intent.putExtra("userId", userId);
						intent.putExtra("isFocus", false);
						getActivity().sendBroadcast(intent);
					} else {
						ToastUtil.showToast(getActivity(), "取消关注失败");
					}
				}
			});
		}
	}
}
