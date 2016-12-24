package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.UserCommentListAdapter;
import com.bcinfo.tripaway.adapter.UserCommentListAdapter.UserReplyListener;
import com.bcinfo.tripaway.adapter.UserCommentSecListAdapter;
import com.bcinfo.tripaway.bean.Comments;
import com.bcinfo.tripaway.bean.Replys;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.ComListViewFooter;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 所有微游记评论
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月30日 下午3:30:01
 */
public class UserMicroCommentActivity extends BaseActivity implements OnClickListener, IXListViewListener {
	protected static final String TAG = "UserCommentActivity";

	private UserCommentListAdapter commentListAdapter;

	private ArrayList<Comments> commentListArr = new ArrayList<Comments>();

	private XListView commentListView;

	//
	// private boolean isLoadOver = false;
	//
	// private boolean isLoading = false;

	private String productId = "";

	private int pageNum = 1;

	private String pageSize = "10";

	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;

	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;

	private EditText mSendMsg;

	private Button mSendBtn;

	private RelativeLayout second_title;

	private String microId = "";

	private int count = 0;

	private LinearLayout mBackLay;

	int oldcount = 0;
	int type = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.micro_user_comment_activity);
		// productId = getIntent().getStringExtra("productId");
		statisticsTitle = "所有评论";
		microId = getIntent().getStringExtra("microId");
		count = getIntent().getIntExtra("count", 0);
		if (getIntent().getStringExtra("objectType") == null) {
			type = 0;
		} else {
			if (getIntent().getStringExtra("objectType").equals("tripStory")) {
				type = 0;
			} else {
				type = Integer.parseInt(getIntent().getStringExtra("objectType"));
			}
		}
		oldcount = count;
		second_title = (RelativeLayout) findViewById(R.id.second_title);
		second_title.getBackground().setAlpha(255);
		setSecondTitle("所有评论");
		mSendMsg = (EditText) findViewById(R.id.comment_user_reply);
		mSendBtn = (Button) findViewById(R.id.comment_reply_btn);
		mSendBtn.setOnClickListener(this);
		mBackLay = (LinearLayout) findViewById(R.id.layout_back_button);
		mBackLay.setOnClickListener(this);
		commentListView = (XListView) findViewById(R.id.user_comment_listview);
		commentListView.setPullRefreshEnable(true);
		commentListView.setPullLoadEnable(false);
		commentListView.setXListViewListener(this);
		commentListAdapter = new UserCommentListAdapter(this, commentListArr, new UserReplyListener() {
			@Override
			public void replyUserComment(String content, String commentId, String replyId) {
				replyProductComments(commentId, replyId, content);
			}

		});
		commentListView.setAdapter(commentListAdapter);
		queryAllComments(pageNum, microId, type);
		mSendMsg.setFocusable(true);
		mSendMsg.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) mSendMsg.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/****************** request *******************/
	private void queryAllComments(int index, String id, int type) {
		RequestParams params = new RequestParams();
		params.put("pagenum", "" + index);
		params.put("pagesize", "10");
		params.put("objectId", id);
		if (type == 0) {
			params.put("objectType", "tripstory");
		} else if (type == 1) {
			params.put("objectType", "series");
		} else if (type == 2) {
			params.put("objectType", "story");

		}
		HttpUtil.get(Urls.micro_comment, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				if (isPullRefresh) {
					commentListView.successRefresh();
				}

				if (isLoadmore) {
					commentListView.stopLoadMore();
				}

				if (response.optString("code").equals("00000")) {
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
								commentListView.setPullLoadEnable(false);
							} else {
								// isLoadmore = true;
								commentListView.setPullLoadEnable(true);
								pageNum++;
							}
						}
						isLoadmore = false;
						isPullRefresh = false;
						showPageComments(comments);
						// setSecondTitle(totalComment + "条评论");
					} else {
						commentListView.setPullLoadEnable(false);
						isLoadmore = false;
						isPullRefresh = false;
					}

				} else {
					// isLoadmore = false;
					if (isPullRefresh) {
						commentListView.stopRefresh();
					}
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
						commentListArr.clear();
						queryAllComments(pageNum, microId, type);
					} else if ("00099".equals(response.optString("code"))) {
						ToastUtil.showToast(UserMicroCommentActivity.this, "用户未登录");
					}

				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					LogUtil.i(TAG, "reply_responseString=", responseString);
					LogUtil.i(TAG, "reply_statusCode=", statusCode + "");
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
	 * 评论
	 */
	private void evaProductComments(int tag) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 评论内容
			String comment = mSendMsg.getText().toString();
			jsonObject.put("content", StringUtils.strConvertUnicode(comment));
			// 产品id
			jsonObject.put("objectId", microId);
			// 评论对象-产品-product
			if (tag == 0) {
				jsonObject.put("objectType", "tripstory");
			} else if (tag == 1) {
				jsonObject.put("objectType", "series");
			}else if (tag == 2) {
				jsonObject.put("objectType", "story");
			}
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpUtil.post(Urls.product_detail_comment, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					// mEditEvaEt.setClickable(false);
					if ("00000".equals(response.optString("code"))) {
						ToastUtil.showToast(UserMicroCommentActivity.this, "评论成功");
						mSendMsg.setText("");
						count++;
						// 重新刷新
						queryAllComments(pageNum, microId, type);
						hideSoftInput(mSendMsg.getWindowToken());
					} else if ("00099".equals(response.optString("code"))) {
						ToastUtil.showToast(UserMicroCommentActivity.this, "用户未登录");
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_reply_btn:
			if (AppInfo.getIsLogin()) {
				pageNum = 1;
				commentListArr.clear();
				evaProductComments(type);
			} else {
				ToastUtil.showToast(getApplication(), "用户未登录不能评价!");
			}
			break;
		case R.id.layout_back_button:
			Intent it = new Intent();
			it.setClass(this, BlogDetailActivity.class);
			it.putExtra("count", count + "");
			it.putParcelableArrayListExtra("comments", commentListArr);
			it.putExtra("microId", microId);
			setResult(1, it);
			finish();
			if (count != oldcount) {
				Intent intent = new Intent("com.bcinfo.refreshCommentsCount");
				intent.putExtra("count", count + "");
				intent.putParcelableArrayListExtra("comments", commentListArr);
				intent.putExtra("microId", microId);
				sendBroadcast(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent it = new Intent();
			it.setClass(this, BlogDetailActivity.class);
			it.putExtra("count", count + "");
			it.putParcelableArrayListExtra("comments", commentListArr);
			it.putExtra("microId", microId);
			setResult(1, it);
			finish();
			if (count != oldcount) {
				Intent intent = new Intent("com.bcinfo.refreshCommentsCount");
				intent.putExtra("count", count + "");
				intent.putParcelableArrayListExtra("comments", commentListArr);
				intent.putExtra("microId", microId);
				sendBroadcast(intent);
			}

		}

		return false;

	}

	@Override
	public void onRefresh() {
		if (isPullRefresh) {
			return;
		}
		if (commentListArr != null) {
			commentListArr.clear();
		}
		isPullRefresh = true;
		pageNum = 1;
		queryAllComments(pageNum, microId, type);
	}

	@Override
	public void onLoadMore() {
		isLoadmore = true;
		queryAllComments(pageNum, microId, type);
	}

	private void showPageComments(ArrayList<Comments> comments) {
		commentListArr.addAll(comments);
		commentListAdapter.notifyDataSetChanged();
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
