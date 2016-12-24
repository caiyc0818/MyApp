package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.adapter.MicroBlogsCommentNewInfoAdapter;
import com.bcinfo.tripaway.adapter.MicroBlogsCommentNewInfoAdapter.UserReplyListener;
import com.bcinfo.tripaway.adapter.SerialAdapter;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.TopicOrTag;
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
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SquareSerialDetailActivity extends BaseActivity implements OnClickListener, IXListViewListener {

	TripArticle tripArticle;

	RoundImageView product_servicer_icon_iv;
	TextView tvName;
	TextView tvPost;
	TextView tvTime;
	TextView tvDescription;
	TextView map_location_text;
	TextView text2;
	ImageView map_location;
	ImageView releaseBtn;
	LinearLayout imageall;
	private DisplayMetrics dm;

	private LinearLayout reviewLinear;
	private LinearLayout zanLinear;
	private ImageView share;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private XListView review_listview;

	// private MyMBProgressHUD myFooterBar;

	private MicroBlogsCommentNewInfoAdapter microBlogsCommentNewInfoAdapter;

	private ArrayList<Comments> commentsList = new ArrayList<>();
	private LinearLayout travel_head;
	private int pageNum = 1;
	private int pagesize = 10;

	private FlowLayout tagsFlow;

	private TextView commentNum;

	private TextView viewNum;

	private ImageView pic;

	private TextView title;

	private List<TripArticle> tripArticles = new ArrayList<TripArticle>();

	private LinearLayout serialListview;

	private String id;

	private ImageView isTalent;

	private TextView mFocusTxt;

	private boolean isFocused = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.square_pic_or_serial_detail);
		statisticsTitle="连载详情";
		initTitleLayout("连载详情");
		id = getIntent().getStringExtra("id");
		// commentsList = tripArticle.getCommentList();
		dm = new DisplayMetrics();
		if (id == null) {
			id = "11";
		}
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		initView();
		//
		testTripStorysUrl(id);
	}

	protected void initView() {
		microBlogsCommentNewInfoAdapter = new MicroBlogsCommentNewInfoAdapter(this, commentsList,
				new UserReplyListener() {
					@Override
					public void replyUserComment(String content, String commentId, String replyId) {
						replyProductComments(commentId, replyId, content);
					}

				});
		reviewLinear = (LinearLayout) findViewById(R.id.reviewLinear);
		zanLinear = (LinearLayout) findViewById(R.id.zanLinear);
		review_listview = (XListView) findViewById(R.id.comment_listview);
		reviewLinear.setOnClickListener(this);
		travel_head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.square_serial_header, null);
		product_servicer_icon_iv = (RoundImageView) travel_head.findViewById(R.id.product_servicer_icon_iv);
		product_servicer_icon_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final UserInfo user = tripArticle.getPublisher();
				ActivityUtil.squareToPersonHomePage(user, SquareSerialDetailActivity.this);
				activityAnimationOpen();
			}
		});
		tvName = (TextView) travel_head.findViewById(R.id.tvName);
		tvPost = (TextView) travel_head.findViewById(R.id.tvPost);
		tvTime = (TextView) travel_head.findViewById(R.id.tvTime);
		tvDescription = (TextView) travel_head.findViewById(R.id.tvDescription);
		map_location_text = (TextView) travel_head.findViewById(R.id.map_location_text);
		map_location = (ImageView) travel_head.findViewById(R.id.map_location);
		imageall = (LinearLayout) travel_head.findViewById(R.id.imageall);
		// myFooterBar = (MyMBProgressHUD) findViewById(R.id.myfooterBar);
		text2 = (TextView) travel_head.findViewById(R.id.text2);
		commentNum = (TextView) findViewById(R.id.comment_num);
		review_listview.setPullLoadEnable(true);
		review_listview.setPullRefreshEnable(false);
		review_listview.setXListViewListener(this);
		review_listview.addHeaderView(travel_head);
		review_listview.setAdapter(microBlogsCommentNewInfoAdapter);

		microBlogsCommentNewInfoAdapter.notifyDataSetChanged();
		tagsFlow = (FlowLayout) findViewById(R.id.tags_flow);
		viewNum = (TextView) travel_head.findViewById(R.id.viewNum);
		pic = (ImageView) findViewById(R.id.pic);
		title = (TextView) travel_head.findViewById(R.id.title);
		serialListview = (LinearLayout) travel_head.findViewById(R.id.serial_listview);
		isTalent = (ImageView) travel_head.findViewById(R.id.isTalent);
		mFocusTxt = (TextView) this.findViewById(R.id.add_focus);
		mFocusTxt.setOnClickListener(this);

	}

	boolean check = false;

	/**
	 * 发表回复
	 */
	private void replyProductComments(String commentId, String replyId, String content) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("commentId", commentId);
			jsonObject.put("content", StringUtils.strConvertUnicode(content));
			jsonObject.put("replyId", replyId);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpUtil.post(Urls.product_detail_reply, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {
						ToastUtil.showToast(getApplication(), "回复成功");
						// 回复成功刷新页面

						pageNum = 1;
						commentsList.clear();
						queryAllComments(pageNum, tripArticle.getPhotoId());
						Intent intent = new Intent("com.bcinfo.refreshComment");
						sendBroadcast(intent);
					} else if ("00099".equals(response.optString("code"))) {
						// ToastUtil.showToast(this, "用户未登录");
					}

				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					// LogUtil.i(TAG, "reply_responseString=",
					// responseString);
					// LogUtil.i(TAG, "reply_statusCode=", statusCode +
					// "");
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

	/**
	 * 设置二级标题
	 * 
	 * @param title
	 *            旅行经历
	 * @param rightText
	 *            保存
	 */
	private void initTitleLayout(String title) {
		TextView titleTV = (TextView) findViewById(R.id.second_title_text);
		titleTV.setText(title);
		LinearLayout backButton = (LinearLayout) findViewById(R.id.layout_back_button);
		share = (ImageView) findViewById(R.id.product_service_share_button);
		share.setVisibility(View.INVISIBLE);
		share.setOnClickListener(this);
		backButton.setOnClickListener(this);
		((View) titleTV.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
		((View) titleTV.getParent()).getBackground().setAlpha(255);

	}

	private void queryAllComments(int index, String id) {
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);
		params.put("pagesize", pagesize);
		params.put("objectId", id);
		params.put("objectType", "series");
		HttpUtil.get(Urls.micro_comment, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// if(isPullRefresh){
				// commentListView.successRefresh();
				// }

				if (isLoadmore) {
					review_listview.stopLoadMore();
				}

				if (response.optString("code").equals("00000")) {

					String opens = response.toString();
					JSONObject dataJSON = response.optJSONObject("data");
					if (dataJSON != null) {
						JSONArray commentArray = dataJSON.optJSONArray("comments");
						ArrayList<Comments> comments = new ArrayList<Comments>();
						// commentListArr.clear();
						if (commentArray != null && commentArray.length() > 0) {
							for (int i = 0; i < commentArray.length(); i++) {
								JSONObject commentJsonObject = commentArray.optJSONObject(i);
								Comments comment = new Comments();
								comment.setAverScore(commentJsonObject.optString("avgScore"));
								comment.setId(commentJsonObject.optString("id"));
								comment.setCreateTime(commentJsonObject.optString("createTime"));
								comment.setContent(commentJsonObject.optString("content"));
								JSONObject userJsonObject = commentJsonObject.optJSONObject("publisher");
								if (userJsonObject != null && !userJsonObject.equals("")) {
									UserInfo userInfo = new UserInfo();
									JSONObject roleJson = userJsonObject.optJSONObject("role");
									if (roleJson != null) {
										OrgRole rol = new OrgRole();

										rol.setRoleCode(StringUtils.isEmptyReturn(roleJson.optString("roleCode")));
										rol.setRoleName(StringUtils.isEmptyReturn(roleJson.optString("roleName")));
										userInfo.setOrgRole(rol);
									}
									userInfo.setUserId(userJsonObject.optString("userId"));
									userInfo.setNickname(userJsonObject.optString("nickName"));
									userInfo.setAvatar(userJsonObject.optString("avatar"));
									userInfo.setUserType(userJsonObject.optString("userType"));
									comment.setUser(userInfo);
								}
								JSONArray replyArray = commentJsonObject.optJSONArray("replies");
								if (replyArray != null) {
									List<Replys> replysList = new ArrayList<Replys>();
									for (int j = 0; j < replyArray.length(); j++) {
										JSONObject replyobject = replyArray.optJSONObject(j);
										Replys replys = new Replys();
										replys.setId(replyobject.optString("id"));
										replys.setContent(replyobject.optString("content"));
										replys.setCreateTime(replyobject.optString("createTime"));

										// 回复人
										JSONObject replyUser = replyobject.optJSONObject("publisher");
										if (replyUser != null && !replyUser.equals("")) {
											UserInfo userInfo = new UserInfo();
											userInfo.setUserId(replyUser.optString("userId"));
											userInfo.setNickname(replyUser.optString("nickName"));
											userInfo.setAvatar(replyUser.optString("avatar"));
											userInfo.setUserType(replyUser.optString("userType"));
											replys.setPublisher(userInfo);
										}
										// 回复对象
										JSONObject replyToUser = replyobject.optJSONObject("replyTo");
										if (replyToUser != null && !replyToUser.equals("")) {
											UserInfo userInfo = new UserInfo();
											userInfo.setUserId(replyToUser.optString("userId"));
											userInfo.setNickname(replyToUser.optString("nickName"));
											userInfo.setAvatar(replyToUser.optString("avatar"));
											userInfo.setUserType(replyToUser.optString("userType"));
											replys.setReplyTo(userInfo);
										}
										replysList.add(replys);
									}
									// 回复列表
									comment.setReplys(replysList);
								}

								comments.add(comment);
							}
							if (comments.size() < 10) {
								// isLoadmore = false;
								// review_listview.setPullLoadEnable(false);
							} else {
								// isLoadmore = true;
								// review_listview.setPullLoadEnable(true);
								pageNum++;
							}
						}
						isLoadmore = false;
						// isPullRefresh = false;
						showPageComments(comments);
						// setSecondTitle(totalComment + "条评论");
					} else {
						// review_listview.setPullLoadEnable(false);
						isLoadmore = false;
						// isPullRefresh = false;
					}

				} else {
					isLoadmore = false;
					// if(isPullRefresh){
					// commentListView.stopRefresh();
					// }
					if (pageNum != 1) {
						pageNum--;
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				if (pageNum != 1) {
					pageNum--;
				}
			}
		});
	}

	private void showPageComments(ArrayList<Comments> comments) {
		for (Comments comnew : comments) {
			boolean flag = false;
			for (Comments comold : commentsList) {
				if (comnew.getId().equals(comold.getId())) {
					flag = true;
					// experience.setExpDetail(newExp.getExpDetail());
					break;
				}
			}
			if (flag) {
				continue;
			}
			commentsList.add(comnew);
		}
		// commentsList.addAll(comments);
		// myFooterBar.setLoadSuccess();
//		if (comments.size() == 0 && !check) {
//			travel_head.getViewTreeObserver().addOnGlobalLayoutListener(
//					new OnGlobalLayoutListener() {
//						@Override
//						public void onGlobalLayout() {
//							if (!check) {
//								int listViewHeight = review_listview
//										.getHeight();
//								travel_headHeight = travel_head.getHeight();
//								ls = travel_head.getLayoutParams();
//								if (travel_headHeight < listViewHeight) {
//									ls.height = listViewHeight;
//									travel_head.setLayoutParams(ls);
//									check = true;
//								}
//							}
//						}
//					});
//		} else {
//			if (comments.size() > 0 && check) {
//				if (ls != null) {
//					ls.height = travel_headHeight;
//				}
//			}
//		}
		if (comments.size() == 0 && !check) {
			travel_head.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (!check) {
						int listViewHeight = review_listview.getHeight();
						travel_headHeight = travel_head.getHeight();
						ls = travel_head.getLayoutParams();
						if (travel_headHeight < listViewHeight) {
							ls.height = listViewHeight;
							travel_head.setLayoutParams(ls);
							check = true;
						}
					}
				}
			});
		} else {
			if (comments.size() > 0 && check) {
				if (ls != null) {
					ls.height = travel_headHeight;
				}
			}
		}
		microBlogsCommentNewInfoAdapter.notifyDataSetChanged();
	}

	LayoutParams ls;
	int travel_headHeight;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.release_button:
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(SquareSerialDetailActivity.this, LoginActivity.class);
				startActivity(it);
				return;
			}
			Intent intent = new Intent(SquareSerialDetailActivity.this, MicroBlogPublishedActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_back_button:
			Intent returnIntent = new Intent();
			returnIntent.putExtra("tripArticle", tripArticle);
			setResult(0, returnIntent);
			finish();
			activityAnimationClose();
			break;
		case R.id.reviewLinear:
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(this, LoginActivity.class);
				startActivity(it);
				return;
			}
			Intent reviewintent = new Intent(this, UserMicroCommentActivity.class);
			reviewintent.putExtra("objectType", "1");
			reviewintent.putExtra("microId", tripArticle.getPhotoId());
			String counts = StringUtils.isEmpty(tripArticle.getComments()) ? "0" : tripArticle.getComments();
			if (counts.equals("0")) {
				reviewintent.putExtra("count", 0);
			} else {
				reviewintent.putExtra("count", Integer.parseInt(counts));
			}

			startActivityForResult(reviewintent, 1);
			break;
		case R.id.zanLinear:
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(this, LoginActivity.class);
				startActivity(it);
				return;
			}
			if (StringUtils.isEmpty(tripArticle.getIsLike()) || tripArticle.getIsLike().equals("0"))
				setZanInfo(1);
			else
				setZanInfo(0);
			break;
		case R.id.product_service_share_button:
			if (!StringUtils.isEmpty(tripArticle.getCover())) {
				ShareSelectDialog shareSelectDialog = new ShareSelectDialog(this, tripArticle.getCover(), "", "发表连载",
						tripArticle.getPhotoId(), Urls.ShareUrlOfPhoto, tripArticle.getPublisher().getNickname());
				shareSelectDialog.show();
			}
			break;
		case R.id.add_focus:
			addOrCancelFocus(isFocused, tripArticle.getPublisher().getUserId());
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		// if (isPullRefresh)
		// {
		// return;
		// }
		return;
		// if (commentsList != null) {
		// commentsList.clear();
		// }
		// // isPullRefresh = true;
		// pageNum = 1;
		// queryAllComments(pageNum, tripArticle.getPhotoId());
	}

	@Override
	public void onLoadMore() {
		isLoadmore = true;
		queryAllComments(pageNum, tripArticle.getPhotoId());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			Bundle b = intent.getExtras();
			String count = b.getString("count");
			text2.setText(count);
			commentNum.setText(count);
			commentsList.clear();
			ArrayList<Comments> temp = intent.getParcelableArrayListExtra("comments");
			for (Comments c : temp) {
				commentsList.add(c);
			}
			microBlogsCommentNewInfoAdapter.notifyDataSetChanged();
			// pageNum =1;
			// queryAllComments(1, tripArticle.getPhotoId());
			// Intent intent1=new Intent("com.bcinfo.refreshCommentsCount");
			// intent1.putExtra("count", count);
			// intent1.putExtra("microId", intent.getStringExtra("microId"));
			// intent1.putParcelableArrayListExtra("comments",
			// intent.getParcelableArrayListExtra("comments"));
			// intent1.putExtra("microId", tripArticle.getPhotoId());
			// sendBroadcast(intent1);
		}
	}

	private void setZanInfo(final int like) {
		zanLinear.setEnabled(false);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("objectId", tripArticle.getPhotoId());
			jsonObject.put("objectType", "series");
			jsonObject.put("opType", like);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.set_like, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					zanLinear.setEnabled(true);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					zanLinear.setEnabled(true);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					zanLinear.setEnabled(true);
					if (response.optString("code").equals("00000")) {
						int likes = 0;
						if (!StringUtils.isEmpty(tripArticle.getLikes())) {
							likes = Integer.parseInt(tripArticle.getLikes());
						}

						if (like == 0) {
							((ImageView) zanLinear.getChildAt(0)).setImageResource(R.drawable.nozan);
							if (--likes < 0)
								likes = 0;

						} else if (like == 1) {
							((ImageView) zanLinear.getChildAt(0)).setImageResource(R.drawable.zan2x);
							++likes;
							Toast.makeText(SquareSerialDetailActivity.this, "点赞 +1", Toast.LENGTH_SHORT).show();
						}

						tripArticle.setLikes(Integer.toString(likes));
						tripArticle.setIsLike(Integer.toString(like));
						if (likes == 0) {
							((TextView) zanLinear.getChildAt(2)).setText("");
						} else {
							((TextView) zanLinear.getChildAt(2)).setText(Integer.toString(likes));
						}
						Intent it = new Intent("com.bcinfo.refreshLikesCount");
						it.putExtra("microId", tripArticle.getPhotoId());
						it.putExtra("like", like);
						sendBroadcast(it);
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

	private void testTripStorysUrl(String seriesId) {
		HttpUtil.get(Urls.square_serial + seriesId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				super.onSuccess(statusCode, headers, response);
				// System.out.println(statusCode);
				System.out.println(response.toString());
				String id = PreferenceUtil.getSession();
				getTripStoryList(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/*
	 * 解析从服务端获取的微游记参数
	 */
	private void getTripStoryList(JSONObject response) {
		if ("00000".equals(response.optString("code"))) {

			// responsetv.setText(response.toString());
			JSONObject dataObj = response.optJSONObject("data").optJSONObject("series");
			JSONArray dataArray = response.optJSONObject("data").optJSONArray("tripstory");
			TripArticle tripArticle;
			if (dataObj != null && dataObj.length() != 0) {
				tripArticle = JsonUtil.getSerialTripArticle(dataObj);
				this.tripArticle = tripArticle;
				if (tripArticle != null) {
					// tripArticles.add(tripArticle);
				}

			}
			if (dataArray != null && dataArray.length() > 0) {
				for (int i = 0; i < dataArray.length(); i++) {
					JSONObject dataObj1 = dataArray.optJSONObject(i);
					tripArticle = JsonUtil.getTripArticle(dataObj1);
					if (tripArticle != null && !StringUtils.isEmpty(tripArticle.getDescription())
							&& !tripArticle.getDescription().equals("<p></p>")) {
						tripArticles.add(tripArticle);
						// break;
					}
				}
			}
			setData();
		}
	}

	private void setData() {
		String userId = PreferenceUtil.getUserId();
		// if (!StringUtils.isEmpty(userId)
		// && !tripArticle.getPublisher().getUserId().equals(userId)){
		((ImageView) zanLinear.getChildAt(0)).setVisibility(View.VISIBLE);
		zanLinear.setOnClickListener(this);
		// }
		if (!StringUtils.isEmpty(tripArticle.getPublisher().getIsTalent())) {
			if (tripArticle.getPublisher().getIsTalent().equals("1"))
				isTalent.setVisibility(View.VISIBLE);
			else
				isTalent.setVisibility(View.GONE);
		}
		if (StringUtils.isEmpty(tripArticle.getIsLike()) || tripArticle.getIsLike().equals("0")) {
			((ImageView) zanLinear.getChildAt(0)).setImageResource(R.drawable.nozan);
			if (!StringUtils.isEmpty(tripArticle.getLikes())) {
				((TextView) zanLinear.getChildAt(2))
				.setText("0".equals(tripArticle.getLikes()) ? "" : tripArticle.getLikes());
			}
		} else if (tripArticle.getIsLike().equals("1")) {
			((ImageView) zanLinear.getChildAt(0)).setImageResource(R.drawable.zan2x);
			if (!StringUtils.isEmpty(tripArticle.getLikes())) {
				((TextView) zanLinear.getChildAt(2))
				.setText("0".equals(tripArticle.getLikes()) ? "" : tripArticle.getLikes());
			}
		}
		if (!StringUtils.isEmpty(tripArticle.getPublisher().getAvatar())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getPublisher().getAvatar(),
					product_servicer_icon_iv, AppConfig.options(R.drawable.ic_launcher));
		}
		String name = "";
		if (tripArticle.getPublisher().getNickname().equals(tripArticle.getPublisher().getMobile())) {
			name = StringUtils.getSecretStr(tripArticle.getPublisher().getNickname());
			tvName.setText(name);
		} else {
			tvName.setText(tripArticle.getPublisher().getNickname());
		}
		// tvName.setText(StringUtils.isEmpty(tripArticle.getPublisher().getNickname())
		// ? "": tripArticle.getPublisher().getNickname());
		// if (!StringUtils.isEmpty(tripArticle.getPublisher().getOrgRole() +
		// "")) {
		// tvPost.setText(StringUtils.isEmpty(tripArticle.getPublisher().getOrgRole().getRoleName())
		// ? ""
		// : tripArticle.getPublisher().getOrgRole().getRoleName());
		// tvPost.setVisibility(View.VISIBLE);
		// } else {
		// tvPost.setVisibility(View.GONE);
		// }
		// String time = DateUtil.setTime(tripArticle.getPublishTime());
		// tvTime.setText(StringUtils.isEmpty(time) ? "" : time);
		// if (StringUtils.isEmpty(tripArticle.getDescription())) {
		// tvDescription.setText("");
		// } else {
		// List<RichText> richTexts = StringUtils.xmlToRichText(tripArticle
		// .getDescription());
		// StringUtils.initRichTextView1(tvDescription, richTexts);
		//
		// }
		// if (!StringUtils.isEmpty(tripArticle.getLocation())
		// && !tripArticle.getLocation().equals("不显示")) {
		// map_location_text.setText(tripArticle.getLocation());
		// map_location.setVisibility(View.VISIBLE);
		// map_location_text.setVisibility(View.VISIBLE);
		// } else {
		// map_location.setVisibility(View.GONE);
		// map_location_text.setVisibility(View.GONE);
		// }
		text2.setText(StringUtils.isEmpty(tripArticle.getComments()) ? "0" : tripArticle.getComments());
		if (!StringUtils.isEmpty(tripArticle.getComments())) {
			commentNum.setText("0".equals(tripArticle.getComments()) ? "" : tripArticle.getComments());
		}
		// if(tripArticle.getTagList().size()>0){
		// addTagFlow(tripArticle.getTagList(), tagsFlow);
		// }
		// if (tripArticle.getViewNum() != 0)
		viewNum.setText("阅读" + tripArticle.getViewNum());
		// else
		// viewNum.setVisibility(View.GONE);
		if (!StringUtils.isEmpty(tripArticle.getCover())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getCover(), pic,
					AppConfig.options(R.drawable.ic_launcher));
		}
		if (!StringUtils.isEmpty(tripArticle.getTitle())) {
			title.setText(tripArticle.getTitle());
		}

		if (!StringUtils.isEmpty(userId) && !tripArticle.getPublisher().getUserId().equals(userId)) {
			mFocusTxt.setVisibility(View.VISIBLE);

			if (tripArticle.getIsFocus().equals("yes")) {
				mFocusTxt.setText("取消关注");
				mFocusTxt.setBackgroundResource(R.drawable.shape_gray_rectangle);
				isFocused = true;

			}
		}
		queryAllComments(1, tripArticle.getPhotoId());
		for (int i = 0; i < tripArticles.size(); i++) {
			addItem(i);
		}
	}

	private void addTagFlow(List<String> tagList, FlowLayout flowLayout) {
		for (String tag : tagList) {
			TextView newView = new TextView(this);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setText(tag);
			newView.setTypeface(TripawayApplication.normalTf);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			newView.setBackgroundResource(R.drawable.shape_tag_bg);
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(this, 8);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	private void addItem(int position) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.square_serial_item, null);
		ImageView map_location = (ImageView) view.findViewById(R.id.map_location);
		TextView map_location_text = (TextView) view.findViewById(R.id.map_location_text);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
		LinearLayout imageall = (LinearLayout) view.findViewById(R.id.imageall);
		FlowLayout tagsFlow = (FlowLayout) view.findViewById(R.id.tags_flow);

		TripArticle tripArticle = tripArticles.get(position);

		if (tripArticle != null) {

			if (!StringUtils.isEmpty(tripArticle.getLocation()) && !tripArticle.getLocation().equals("不显示")) {
				map_location_text.setText(tripArticle.getLocation());
				map_location.setVisibility(View.VISIBLE);
				map_location_text.setVisibility(View.VISIBLE);
			} else {
				map_location.setVisibility(View.GONE);
				map_location_text.setVisibility(View.GONE);
			}
			if (tripArticle.getType() == 0) {
				imageall.setVisibility(View.VISIBLE);
				imageall.removeAllViews();
				addImage(tripArticle, imageall);
			} else {
				imageall.setVisibility(View.GONE);
			}
			if (StringUtils.isEmpty(tripArticle.getDescription())) {
				tvDescription.setText("");
			} else {
				List<RichText> richTexts = StringUtils.xmlToRichText(tripArticle.getDescription());
				StringUtils.initRichTextView1(tvDescription, richTexts);

			}
			if (tripArticle.getTagList().size() > 0) {
				tagsFlow.setVisibility(View.VISIBLE);
				addTagFlow(tripArticle.getTagList(), tagsFlow);
			} else {
				tagsFlow.setVisibility(View.GONE);
			}
			String time = DateUtil.setTime(tripArticle.getPublishTime());
			if (tripArticle.getType() == 0)
				tvTime.setText(StringUtils.isEmpty(time) ? "" : time + "晒图");
			else if (tripArticle.getType() == 1)
				tvTime.setText(StringUtils.isEmpty(time) ? "" : time + "连载");
			serialListview.addView(view);
		}
	}

	private void addImage(TripArticle tripArticle, LinearLayout imageall) {
		int gap = 2 * DensityUtil.dip2px(this, 11);
		for (int i = 0; i < tripArticle.getPictureList().size(); i++) {
			ImageView imageView = new ImageView(this);
			if(StringUtils.isEmpty(tripArticle.getPictureList().get(i).getHeight())||
					StringUtils.isEmpty(tripArticle.getPictureList().get(i).getWidth())){
				continue;
			}
			LinearLayout.LayoutParams ls = new LinearLayout.LayoutParams(dm.widthPixels - gap,
					(dm.widthPixels - gap) * Integer.parseInt(tripArticle.getPictureList().get(i).getHeight())
							/ Integer.parseInt(tripArticle.getPictureList().get(i).getWidth()));

			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(ls);
			imageView.setImageResource(R.drawable.ic_launcher);
			ImageLoader.getInstance().displayImage(Urls.imgHost + tripArticle.getPictureList().get(i).getUrl(),
					imageView, AppConfig.options(R.drawable.ic_launcher));
			imageall.addView(imageView);
			if (i != tripArticle.getPictureList().size() - 1) {
				View view = new View(this);
				LinearLayout.LayoutParams lsview = new LinearLayout.LayoutParams(dm.widthPixels - 14, 6);
				view.setLayoutParams(lsview);
				imageall.addView(view);
			}
		}
	}

	private void addOrCancelFocus(boolean flag, final String userId) {
		if (!AppInfo.getIsLogin()) {
			ToastUtil.showToast(this, "请登录");
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
							mFocusTxt.setText("取消关注");
							mFocusTxt.setBackgroundResource(R.drawable.shape_gray_rectangle);
							Intent intent = new Intent("com.bcinfo.refreshFocus");
							intent.putExtra("userId", userId);
							intent.putExtra("isFocus", true);
							sendBroadcast(intent);
							isFocused = true;
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
						mFocusTxt.setText("+关注");
						mFocusTxt.setBackgroundResource(R.drawable.shape_green_rectangle);
						Intent intent = new Intent("com.bcinfo.refreshFocus");
						intent.putExtra("userId", userId);
						intent.putExtra("isFocus", false);
						sendBroadcast(intent);
						isFocused = false;
					}
				}
			});

		}
	}
}
