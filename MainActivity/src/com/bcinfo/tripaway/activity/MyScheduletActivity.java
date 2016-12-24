package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.MyScheduleAdapter;
import com.bcinfo.tripaway.bean.MySchedule;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 全部回应
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月28日 下午8:19:17
 */

public class MyScheduletActivity extends BaseActivity implements
		OnClickListener, IXListViewListener,OnItemClickListener {
	protected static final String TAG = "AllResponceActivity";
	private XListView responceListView;
	private List<MySchedule> lists = new ArrayList<MySchedule>();;
	private RelativeLayout rla;
	private LinearLayout backLaout;
	private int pageNum = 1;
	private boolean canContinue=false;
	/**
	 * 下拉刷新
	 */
	private boolean isPullRefresh = false;
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;
	private int pageSize = 3;
	private MyScheduleAdapter ad;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.my_schedulet_layout);
		statisticsTitle="我的出行单";
		lists = new ArrayList<MySchedule>();
		rla = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		rla.getBackground().setAlpha(255);

		backLaout = (LinearLayout) findViewById(R.id.layout_back_button);

		// lists.clear();

		responceListView = (XListView) findViewById(R.id.select_dialog_listview);

		ad = new MyScheduleAdapter(this, lists);

		backLaout.setOnClickListener(this);

		responceListView.setPullLoadEnable(false);
		responceListView.setPullRefreshEnable(true);
		responceListView.setXListViewListener(this);
		responceListView.setAdapter(ad);
		responceListView.setOnItemClickListener(this);
		  IntentFilter myIntentFilter = new IntentFilter();
	        myIntentFilter.addAction("com.bcinfo.travelOrder");
	        // 注册广播
	       registerReceiver(mBroadcastReceiver, myIntentFilter);
		getSchedulets();
	}

	 private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	    {
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	            String action = intent.getAction();
	            if (action.equals("com.bcinfo.travelOrder")){
	                lists.clear();
	                pageNum=1;
	                getSchedulets();
	            }
	            }
	    };
	    
	 protected void onDestroy() {
		 super.onDestroy();
		 unregisterReceiver(mBroadcastReceiver);
	 };   
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_back_button:
			setResult(8188);
			this.finish();
			activityAnimationClose();
			break;

		default:
			break;
		}

	}

	private void getSchedulets() {
		RequestParams params = new RequestParams();
		params.put("pagenum", pageNum);// 当前页码
		params.put("pagesize", pageSize);// 页码数

		HttpUtil.get(Urls.get_itinerary, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						canContinue=true;
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

								List<MySchedule> queuesInfos = new ArrayList<MySchedule>();
								String deleteStr = PreferenceUtil
										.getHaveDeleteStr();
								LogUtil.i(TAG, "已经删除了的列表id", deleteStr);

								for (int i = 0; i < dataJsonS.length(); i++) {
									JSONObject tripStoryObj = dataJsonS
											.optJSONObject(i);
									MySchedule mche = new MySchedule();
									mche.setId(tripStoryObj
											.optString("id"));
									mche.setTitle(tripStoryObj
											.optString("title"));
									mche.setType(tripStoryObj
											.optString("status"));
									mche.setBeginDate(tripStoryObj
											.optString("beginDate"));
									queuesInfos.add(mche);
									

								}
								
										
								lists.addAll(queuesInfos);
								
								ad.notifyDataSetChanged();
								
									pageNum++;
								

							} else {
							     isPullRefresh = false;
					            	
				            	  isLoadmore=false;
							}

						} else {

							if (pageNum != 1) {
								pageNum--;
							}
						}

						if (!isLoadmore) {
							// 上拉返回的结束加载更多布局
							responceListView.stopLoadMore();

						}
						if (!isPullRefresh) {
							responceListView.stopRefresh();
						}
						if (lists == null || lists.size() == 0) {
							responceListView.setVisibility(View.INVISIBLE);
							rla = (RelativeLayout) findViewById(R.id.zwcx);
							rla.setVisibility(View.VISIBLE);
						} else {
							rla = (RelativeLayout) findViewById(R.id.zwcx);
							rla.setVisibility(View.INVISIBLE);
						}

					}
				});
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

		
		//lists=getNewList(lists);
		isPullRefresh = true;
		if (!canContinue) {
			return;
		}
		canContinue = false;
		
			getSchedulets();	
		
		
		
	}

	@Override
	public void onLoadMore() {

		// TODO Auto-generated method stub

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
			Intent mainIntent = new Intent(getBaseContext(), TripOrdeDetailActivity.class);
			mainIntent.putExtra("id", lists.get(position-1).getId());
			startActivity(mainIntent);
		break;	
		}
		}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 ((View)backLaout.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
	        ((View)backLaout.getParent()).getBackground().setAlpha(255);
	}
}
