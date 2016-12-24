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
import com.bcinfo.tripaway.adapter.MicroBlogsCommentNewInfoAdapter;
import com.bcinfo.tripaway.adapter.MicroBlogsCommentNewInfoAdapter.UserReplyListener;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MicroBlogsNewInfoActivity extends BaseActivity implements OnClickListener, IXListViewListener {

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

	private RelativeLayout reviewRelative;
	private RelativeLayout zanrelative;
	private RelativeLayout shareRelative;

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

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.micro_blogs_travel_info);
		initTitleLayout("微游记", "");
		tripArticle = getIntent().getParcelableExtra("tripArticle");
		// commentsList = tripArticle.getCommentList();
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		initView();
		queryAllComments(1, tripArticle.getPhotoId());

	}

	protected void initView() {
		microBlogsCommentNewInfoAdapter = new MicroBlogsCommentNewInfoAdapter(this, commentsList,
				new UserReplyListener() {
					@Override
					public void replyUserComment(String content, String commentId, String replyId) {
						replyProductComments(commentId, replyId, content);
					}

				});
		reviewRelative = (RelativeLayout) findViewById(R.id.reviewRelative);
		zanrelative = (RelativeLayout) findViewById(R.id.zanrelative);
		if (tripArticle.getIsLike().equals("0")) {
			((ImageView) zanrelative.getChildAt(0)).setImageResource(R.drawable.nozan);
			((TextView) zanrelative.getChildAt(2))
					.setText(StringUtils.isEmpty(tripArticle.getLikes()) ? "0" : tripArticle.getLikes());
		} else if (tripArticle.getIsLike().equals("1")) {
			((ImageView) zanrelative.getChildAt(0)).setImageResource(R.drawable.zan2x);
			((TextView) zanrelative.getChildAt(2))
					.setText(StringUtils.isEmpty(tripArticle.getLikes()) ? "0" : tripArticle.getLikes());
		}
		shareRelative = (RelativeLayout) findViewById(R.id.shareRelative);
		review_listview = (XListView) findViewById(R.id.review_listview);
		reviewRelative.setOnClickListener(this);
		zanrelative.setOnClickListener(this);
		shareRelative.setOnClickListener(this);
		travel_head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.micro_blogs_travel_info_head, null);
		product_servicer_icon_iv = (RoundImageView) travel_head.findViewById(R.id.product_servicer_icon_iv);
		product_servicer_icon_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final UserInfo user = tripArticle.getPublisher();
				ActivityUtil.toPersonHomePage(user, MicroBlogsNewInfoActivity.this);
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
		review_listview.setPullLoadEnable(true);
		review_listview.setPullRefreshEnable(false);
		review_listview.setXListViewListener(this);
		review_listview.addHeaderView(travel_head);
		review_listview.setAdapter(microBlogsCommentNewInfoAdapter);
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
		if (!StringUtils.isEmpty(tripArticle.getPublisher().getOrgRole() + "")) {
			tvPost.setText(StringUtils.isEmpty(tripArticle.getPublisher().getOrgRole().getRoleName()) ? ""
					: tripArticle.getPublisher().getOrgRole().getRoleName());
			tvPost.setVisibility(View.VISIBLE);
		} else {
			tvPost.setVisibility(View.GONE);
		}
		String time = DateUtil.setTime(tripArticle.getPublishTime());
		tvTime.setText(StringUtils.isEmpty(time) ? "" : time);
		tvDescription.setText(StringUtils.isEmpty(tripArticle.getDescription()) ? "" : tripArticle.getDescription());
		if (!StringUtils.isEmpty(tripArticle.getLocation())&& !tripArticle.getLocation().equals("不显示")) {
			map_location_text.setText(tripArticle.getLocation());
			map_location.setVisibility(View.VISIBLE);
			map_location_text.setVisibility(View.VISIBLE);
		} else {
			map_location.setVisibility(View.GONE);
			map_location_text.setVisibility(View.GONE);
		}
		text2.setText(StringUtils.isEmpty(tripArticle.getComments()) ? "0" : tripArticle.getComments());
		addImage();
		microBlogsCommentNewInfoAdapter.notifyDataSetChanged();

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
					// LogUtil.i(TAG, "reply_statusCode=", statusCode + "");
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

	private void addImage() {
		for (int i = 0; i < tripArticle.getPictureList().size(); i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams ls = new LinearLayout.LayoutParams(dm.widthPixels - 14,
					(dm.widthPixels - 14) * Integer.parseInt(tripArticle.getPictureList().get(i).getHeight())
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

	/**
	 * 设置二级标题
	 * 
	 * @param title
	 *            旅行经历
	 * @param rightText
	 *            保存
	 */
	private void initTitleLayout(String title, String rightText) {
		TextView titleTV = (TextView) findViewById(R.id.second_title_text);
		LinearLayout backButton = (LinearLayout) findViewById(R.id.layout_back_button);
		// saveBtn = (TextView) findViewById(R.id.event_commit_button);
		releaseBtn = (ImageView) findViewById(R.id.release_button);
		// saveBtn.setVisibility(View.VISIBLE);
		releaseBtn.setVisibility(View.VISIBLE);
		titleTV.setText(title);
		// saveBtn.setText(rightText);
		// saveBtn.setOnClickListener(this);
		releaseBtn.setOnClickListener(this);
		backButton.setOnClickListener(this);
		((View) titleTV.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
		((View) titleTV.getParent()).getBackground().setAlpha(255);

	}

	private void queryAllComments(int index, String id) {
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);
		params.put("pagesize", pagesize);
		params.put("objectId", id);
		params.put("objectType", "tripstory");
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
				Intent it = new Intent(MicroBlogsNewInfoActivity.this, LoginActivity.class);
				startActivity(it);
				return;
			}
			Intent intent = new Intent(MicroBlogsNewInfoActivity.this, MicroBlogPublishedActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_back_button:
			Intent returnIntent = new Intent();
			returnIntent.putExtra("tripArticle", tripArticle);
			setResult(0, returnIntent);
			finish();
			activityAnimationClose();
			break;
		case R.id.reviewRelative:
			if (!AppInfo.getIsLogin()) {
				Intent it = new Intent(this, LoginActivity.class);
				startActivity(it);
				return;
			}
			Intent reviewintent = new Intent(this, UserMicroCommentActivity.class);
			reviewintent.putExtra("objectType", "0");
			reviewintent.putExtra("microId", tripArticle.getPhotoId());
			String counts = StringUtils.isEmpty(tripArticle.getComments()) ? "0" : tripArticle.getComments();
			if (counts.equals("0")) {
				reviewintent.putExtra("count", 0);
			} else {
				reviewintent.putExtra("count", Integer.parseInt(counts));
			}

			startActivityForResult(reviewintent, 1);
			break;
		case R.id.zanrelative:
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
		case R.id.shareRelative:
			if (tripArticle.getPictureList() != null && tripArticle.getPictureList().size() > 0) {
				ShareSelectDialog shareSelectDialog = new ShareSelectDialog(this,
						tripArticle.getPictureList().get(0).getUrl(), "发表微游记", tripArticle.getPictureList().get(0).getUrl(),
						tripArticle.getPhotoId(), Urls.ShareUrlOfPhoto,tripArticle.getPublisher().getNickname());
				shareSelectDialog.show();
			}
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
		if (commentsList != null) {
			commentsList.clear();
		}
		// isPullRefresh = true;
		pageNum = 1;
		queryAllComments(pageNum, tripArticle.getPhotoId());
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
		zanrelative.setEnabled(false);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("objectId", tripArticle.getPhotoId());
			jsonObject.put("objectType", "tripstory");
			jsonObject.put("opType", like);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.set_like, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					zanrelative.setEnabled(true);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					zanrelative.setEnabled(true);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					zanrelative.setEnabled(true);
					if (response.optString("code").equals("00000")) {
						int likes = 0;
						if (!StringUtils.isEmpty(tripArticle.getLikes())) {
							likes = Integer.parseInt(tripArticle.getLikes());
						}

						if (like == 0) {
							((ImageView) zanrelative.getChildAt(0)).setImageResource(R.drawable.nozan);
							if (--likes < 0)
								likes = 0;

						} else if (like == 1) {
							((ImageView) zanrelative.getChildAt(0)).setImageResource(R.drawable.zan2x);
							++likes;
							Toast.makeText(MicroBlogsNewInfoActivity.this, "点赞 +1", Toast.LENGTH_SHORT).show();
						}

						tripArticle.setLikes(Integer.toString(likes));
						tripArticle.setIsLike(Integer.toString(like));
						if (likes == 0) {
							((TextView) zanrelative.getChildAt(2)).setText("");
						} else {
							((TextView) zanrelative.getChildAt(2)).setText(Integer.toString(likes));
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
}
