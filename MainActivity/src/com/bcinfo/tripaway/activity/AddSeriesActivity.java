package com.bcinfo.tripaway.activity;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.GoWithResponceListAdapter;
import com.bcinfo.tripaway.adapter.MyScheduleAdapter;
import com.bcinfo.tripaway.adapter.OldPicAdapter;
import com.bcinfo.tripaway.adapter.SeriesAdapter;
import com.bcinfo.tripaway.adapter.SeriesAdapter.OnSelectSerialListener;
import com.bcinfo.tripaway.bean.Intention;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.MySchedule;
import com.bcinfo.tripaway.bean.OldPic;
import com.bcinfo.tripaway.bean.QueuesInfo;
import com.bcinfo.tripaway.bean.Series;
import com.bcinfo.tripaway.bean.TripStorySchema;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.QueuesListDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.TextStyleUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.emoji.EmojiconSpan;
import com.bcinfo.tripaway.view.pop.EditPopWindow;
import com.bcinfo.tripaway.view.pop.EditPopWindow.OperationListener;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.bcinfo.tripaway.view.span.TextClickSpan;
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

public class AddSeriesActivity extends BaseActivity implements OnClickListener,
		IXListViewListener, OnItemClickListener, OnSelectSerialListener {
	
	protected static final String TAG = "AddSeriesActivity";
	private final static int PIC_TO_SERIAL= 1004;
	private XListView responceListView;
	private List<Series> lists = new ArrayList<Series>();;
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
	private SeriesAdapter ad;
	private TextView confirm;

	private String seriesId;
	private View loginLoading;
	private AnimationDrawable loadingAnimation;
	private LinearLayout noContent;
	private LinearLayout submitBtn;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.addserieslist);
		lists = new ArrayList<Series>();
		OldPic old = new OldPic();
		old.setOldTime("");
		backLaout = (LinearLayout) findViewById(R.id.layout_back_button);

		// lists.clear();
		// TripStorySchema sche=new TripStorySchema();
		// sche.setDescription("jibberJabber");
		// sche.setPublishTime("20160318142047");
		// lists.add(sche);
		responceListView = (XListView) findViewById(R.id.select_dialog_listview);

		ad = new SeriesAdapter(this, lists, this);

		backLaout.setOnClickListener(this);

		responceListView.setPullLoadEnable(true);
		responceListView.setPullRefreshEnable(true);
		responceListView.setXListViewListener(this);
		responceListView.setAdapter(ad);
		responceListView.setOnItemClickListener(this);
		confirm = (TextView) findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
		loginLoading = findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		noContent = (LinearLayout) findViewById(R.id.default_layout);
		submitBtn = (LinearLayout) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
		getSchedulets();
	}

	protected void onDestroy() {
		super.onDestroy();

	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			break;
		case R.id.confirm:
			if (seriesId == null)
				return;
			Intent data = new Intent();
			data.putExtra("seriesId", seriesId);
			for (int i = 0; i < lists.size(); i++)
				if (seriesId.equals(lists.get(i).getId())) {
					data.putExtra("title", lists.get(i).getTitle());
					break;
				}
			setResult(Activity.RESULT_OK, data);
			finish();
			break;
		case R.id.submitBtn:
			Intent intent3 = new Intent(AddSeriesActivity.this, SquareSerialPublishedActivity.class);
			intent3.putExtra("from", "pic");
			startActivityForResult(intent3,PIC_TO_SERIAL);
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

			HttpUtil.get(Urls.series, params, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {

					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString,
							throwable);

					ToastUtil.showToast(AddSeriesActivity.this, "throwable="
							+ throwable.getMessage());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONArray errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					loadingAnimation.stop();
					loginLoading.setVisibility(View.GONE);
					canContinue = true;
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					if (isLoadmore) {
						// 上拉返回的结束加载更多布局
						responceListView.stopLoadMore();
						responceListView.stopRefresh();
					}

					LogUtil.i(TAG, "查询消息列表接口1", response.toString());
					String code = response.optString("code");

					if (code.equals("00000")) {

						JSONArray dataJsonS = response.optJSONArray("data");
						if (dataJsonS != null && dataJsonS.length() > 0) {
							if (isPullRefresh) {
								responceListView.successRefresh();

							}

							List<Series> queuesInfos = new ArrayList<Series>();
							String deleteStr = PreferenceUtil
									.getHaveDeleteStr();
							LogUtil.i(TAG, "已经删除了的列表id", deleteStr);

							for (int i = 0; i < dataJsonS.length(); i++) {
								JSONObject tripStoryObj = dataJsonS
										.optJSONObject(i);
								Series mche = new Series();
								mche.setId(tripStoryObj.optString("id"));
								mche.setTitle(tripStoryObj.optString("title"));
								queuesInfos.add(mche);

							}

							lists.addAll(queuesInfos);

							ad.notifyDataSetChanged();

							pageNum++;

						} else {
							isPullRefresh = false;

							isLoadmore = false;
						}

					} else {

						if (pageNum != 1) {
							pageNum--;
						}
					}

					if(lists.size()==0){
						noContent.setVisibility(View.VISIBLE);
						confirm.setVisibility(View.GONE);
					}
					if (!isLoadmore) {
						// 上拉返回的结束加载更多布局
						responceListView.stopLoadMore();

					}
					if (!isPullRefresh) {
						responceListView.stopRefresh();
					}

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
		lists.clear();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.select_dialog_listview:
			Intent mainIntent = new Intent(getBaseContext(),
					TripOrdeDetailActivity.class);
			// mainIntent.putExtra("id", lists.get(position-1).getId());
			startActivity(mainIntent);
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((View) backLaout.getParent()).setBackgroundColor(getResources()
				.getColor(R.color.title_bg));
		((View) backLaout.getParent()).getBackground().setAlpha(255);
	}

	@Override
	public void setSerial(String id) {
		// TODO Auto-generated method stub
		seriesId = id;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PIC_TO_SERIAL:
				if(data!=null){
					setResult(Activity.RESULT_OK, data);
					finish();
				}
				break;
			default:
				break;
			}
		}
	}
}
