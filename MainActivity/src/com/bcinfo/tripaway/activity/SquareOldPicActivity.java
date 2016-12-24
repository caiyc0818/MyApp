package com.bcinfo.tripaway.activity;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.GoWithResponceListAdapter;
import com.bcinfo.tripaway.adapter.MyScheduleAdapter;
import com.bcinfo.tripaway.adapter.OldPicAdapter;
import com.bcinfo.tripaway.adapter.OldPicAdapter.OnSelectPicListener;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Intention;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.MySchedule;
import com.bcinfo.tripaway.bean.OldPic;
import com.bcinfo.tripaway.bean.QueuesInfo;
import com.bcinfo.tripaway.bean.TripStorySchema;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.QueuesListDB;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.pop.EditPopWindow;
import com.bcinfo.tripaway.view.pop.EditPopWindow.OperationListener;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.igexin.sdk.aidl.UserServiceListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.open.utils.HttpUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 全部回应
 * 
 * @function
 * @author JiangCSS东西啊
 * @version 1.0, 2015年4月28日 下午8:19:17
 */

public class SquareOldPicActivity extends BaseActivity
		implements OnClickListener, IXListViewListener, OnItemClickListener, OnSelectPicListener {
	protected static final String TAG = "AllResponceActivity";
	private XListView responceListView;
	private List<TripStorySchema> lists = new ArrayList<TripStorySchema>();;
	private RelativeLayout rla;
	private LinearLayout backLaout;
	private int pageNum = 1;
	private boolean canContinue = false;
	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;
	private int pageSize = 10;
	private OldPicAdapter ad;

	private List<String> selectIdList = new ArrayList<String>();
	private TextView confirm;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
private ImageView noimage;
	private String seriesId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.squeroldshaitulist);
		seriesId = getIntent().getStringExtra("seriesId");
		lists = new ArrayList<TripStorySchema>();
		OldPic old = new OldPic();
		old.setOldTime("");
		backLaout = (LinearLayout) findViewById(R.id.layout_back_button);
		// lists.clear();
		// TripStorySchema sche=new TripStorySchema();
		// sche.setDescription("jibberJabber");
		// sche.setPublishTime("20160318142047");
		// lists.add(sche);
		responceListView = (XListView) findViewById(R.id.select_dialog_listview);
		confirm = (TextView) findViewById(R.id.confirm);
		noimage = (ImageView) findViewById(R.id.noimage);
		confirm.setOnClickListener(this);

		ad = new OldPicAdapter(this, lists);
		ad.setOnSelectPicListener(this);

		backLaout.setOnClickListener(this);

		responceListView.setPullLoadEnable(true);
		responceListView.setPullRefreshEnable(true);
		responceListView.setXListViewListener(this);
		responceListView.setAdapter(ad);
		responceListView.setOnItemClickListener(this);
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		getSchedulets();
	}

	protected void onDestroy() {
		super.onDestroy();

	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_back_button:
			this.finish();
			break;
		case R.id.confirm:
			picToSerial(seriesId, selectIdList);
			break;
		default:
			break;
		}

	}

	private void getSchedulets() {
		try {
			RequestParams params = new RequestParams();
			params.put("pagenum", pageNum);// 当前页码
			params.put("pagesize", pageSize);// 页码数

			HttpUtil.get(Urls.oldPic, params, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);

					ToastUtil.showToast(SquareOldPicActivity.this, "throwable=" + throwable.getMessage());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					canContinue = true;
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					loadingAnimation.stop();
					loginLoading.setVisibility(View.GONE);
					if (isLoadmore) {
						responceListView.stopLoadMore();
					}

					LogUtil.i(TAG, "查询消息列表接口1", response.toString());
					String code = response.optString("code");

					if (code.equals("00000")) {
						if (isPullRefresh) {
							responceListView.successRefresh();
							if (lists.size() > 0) {
								lists.clear();
							}
						}
						JSONObject dataJsonSB = response.optJSONObject("data");
						JSONArray dataJsonS = dataJsonSB.optJSONArray("tripstory");
						if (dataJsonS != null && dataJsonS.length() > 0) {
							List<TripStorySchema> queuesInfos = new ArrayList<TripStorySchema>();
							String deleteStr = PreferenceUtil.getHaveDeleteStr();
							LogUtil.i(TAG, "已经删除了的列表id", deleteStr);

							for (int i = 0; i < dataJsonS.length(); i++) {
								JSONObject tripStoryObj = dataJsonS.optJSONObject(i);
								TripStorySchema mche = new TripStorySchema();
								mche.setPhotoId(tripStoryObj.optString("id"));
								mche.setDescription(tripStoryObj.optString("desc"));
								mche.setPublishTime(tripStoryObj.optString("time"));
								if (!StringUtils.isEmpty(tripStoryObj.optString("count"))) {
									int count = Integer.parseInt(tripStoryObj.optString("count"));
									mche.setPicCount(count);
								}
								if (!StringUtils.isEmpty(tripStoryObj.optString("cover"))) {
									List<ImageInfo> picture = new ArrayList<ImageInfo>();
									ImageInfo imageInfo = new ImageInfo();
									imageInfo.setUrl(tripStoryObj.optString("cover"));
									picture.add(imageInfo);
									mche.setPicture(picture);
								}
								queuesInfos.add(mche);
							}
							if (queuesInfos.size() >= pageSize) {
								pageNum++;
								responceListView.setPullLoadEnable(true);
							} else {
								responceListView.setPullLoadEnable(false);
							}
							lists.addAll(queuesInfos);
							ad.notifyDataSetChanged();
							// lists.addAll(queuesInfos);
							//
							// ad.notifyDataSetChanged();
							//
							// pageNum++;

						} else {
							responceListView.setPullLoadEnable(false);
							ad.notifyDataSetChanged();
						}

					} else {
						if (isPullRefresh) {
							// 下拉刷新接口返回的数据不正确
							responceListView.stopRefresh();
						}
						if (pageNum != 1) {
							pageNum--;
						}
					}
					if (lists.size() == 0) {
						noimage.setVisibility(View.VISIBLE);
						responceListView.setPullRefreshEnable(false);
					}
					// 上拉 下拉的初始状态置为false;
					isLoadmore = false;
					isPullRefresh = false;

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

		// lists=getNewList(lists);
		isPullRefresh = true;
		if (!canContinue) {
			return;
		}
		canContinue = false;
		pageNum = 1;
		getSchedulets();

	}

	@Override
	public void onLoadMore() {

		// TODO Auto-generated method stub
		if (!canContinue) {
			return;
		}
		canContinue = false;
		isLoadmore = true;
		getSchedulets();
	}

	public static List<MySchedule> removeDuplicate(List list) {
		HashSet<MySchedule> h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.select_dialog_listview:
			Intent mainIntent = new Intent(getBaseContext(), TripOrdeDetailActivity.class);
			// mainIntent.putExtra("id", lists.get(position-1).getId());
			startActivity(mainIntent);
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((View) backLaout.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
		((View) backLaout.getParent()).getBackground().setAlpha(255);
	}

	@Override
	public void addPic(String id) {
		// TODO Auto-generated method stub
		selectIdList.add(id);
	}

	@Override
	public void removePic(String id) {
		// TODO Auto-generated method stub
		selectIdList.remove(id);
	}

	private void picToSerial(String seriesId, List<String> photoIdList) {
		if (photoIdList.size() == 0)
			return;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("seriesId", seriesId);
			jsonObject.put("photoId", new JSONArray(photoIdList));
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpUtil.post(Urls.square_pic_to_serial, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					if ("00000".equals(response.optString("code"))) {
						setResult(Activity.RESULT_OK);
						ToastUtil.showToast(getApplicationContext(), "添加成功");
						finish();
						activityAnimationClose();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

					super.onFailure(statusCode, headers, responseString, throwable);
				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
