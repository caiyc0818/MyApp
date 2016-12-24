package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.adapter.MyaccAdapter3;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.activity;
import com.bcinfo.tripaway.bean.articleExtInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.view.MBProgressHUD.MyMBProgressHUD;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 已经结束活动 fragment
 * 
 */
public class OverActionsFragment extends BaseFragment {
	private ListView all__listview;

	// list数据
	private List<activity> lists = new ArrayList<>();;
	private MyaccAdapter3 myAdapter;
	private int pageNum = 1;
	private int pageSize = 10;

	private ImageView nopic;
	/**
	 * 上拉加载更多
	 */
	private boolean isLoadmore = false;
	private LinearLayout mTravelListViewFooter;

	private Context mContext;

	private UserInfo userInfo = new UserInfo();

	public OverActionsFragment(Context mContext, UserInfo userInfo) {
		this.mContext = mContext;
		this.userInfo = userInfo;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.traitemfragment, null);

		all__listview = (ListView) view.findViewById(R.id.all_listview);
		lists = new ArrayList<>();

		mTravelListViewFooter = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		all__listview.addFooterView(mTravelListViewFooter);

		mTravelListViewFooter.setVisibility(View.GONE);
		// 动态更新
		myAdapter = new MyaccAdapter3(getActivity(), lists);
		all__listview.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();
		all__listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ContentActivity.class);
				String path = Urls.huodong + lists.get(position).getArticleId() + ".html";
				intent.putExtra("path", path);
				intent.putExtra("title", "活动");
				startActivity(intent);
			}
		});
		isLoadmore = true;
		lists.clear();
		searchAct();
		registerBoradcastReceiver();
		return view;
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.bcinfo.loadMoreActions".equals(action)) {
				isLoadmore = false;
				searchAct();
			}
		}
	};

	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.bcinfo.loadMoreActions");
		// 注册广播
		mContext.registerReceiver(broadcastReceiver, myIntentFilter);
	}

	private void searchAct() {
		// TODO Auto-generated method stub
		if (!isLoadmore && mTravelListViewFooter.getVisibility() == View.GONE) {
			return;
		}
		RequestParams params = new RequestParams();

		params.put("type", "activity");
		params.put("userId", userInfo.getUserId());
		params.put("pagenum", pageNum);
		params.put("pagesize", pageSize);
		HttpUtil.get(Urls.search_act, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);

				if ("00000".equals(response.optString("code"))) {

					getdata(response);

				}
				isLoadmore = false;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

		});
	}

	private void getdata(JSONObject response) {
		// TODO Auto-generated method stub
		JSONArray data = response.optJSONArray("data");

		List<activity> dataList = new ArrayList<>();
		for (int i = 0; i < data.length(); i++) {
			JSONObject activityObj = data.optJSONObject(i);
			activity activity = new activity();
			activity.setTitle(activityObj.optString("title"));
			activity.setStatus(activityObj.optString("status"));
			activity.setCover(activityObj.optString("cover"));
			activity.setArticleId(activityObj.optString("articleId"));
			JSONArray topicJsonArray = activityObj.optJSONArray("articleExt");
			if (topicJsonArray != null && topicJsonArray.length() > 0) {
				ArrayList<articleExtInfo> topics = new ArrayList<articleExtInfo>();
				for (int j = 0; j < topicJsonArray.length(); j++) {
					articleExtInfo articleExtInfo = new articleExtInfo();
					if (topicJsonArray.optJSONObject(j).optString("type").equals("poi")) {
						articleExtInfo.setInfo(topicJsonArray.optJSONObject(j).optString("info"));
						topics.add(articleExtInfo);
					}
				}
				activity.setArticleExt(topics);
			}
			dataList.add(activity);
		}
		if (dataList.size() >= pageSize * pageNum) {

			pageNum++;
			mTravelListViewFooter.setVisibility(View.VISIBLE);
		} else {
			mTravelListViewFooter.setVisibility(View.GONE);
		}
		notifySetChanged(dataList);

	}

	private void notifySetChanged(List<activity> list) {
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if ("invalid".equals(list.get(i).getStatus())) {
					lists.add(list.get(i));
				}
			}
		}
		if (lists.size() == 0) {
			all__listview.setVisibility(View.GONE);
			// noimage.setVisibility(View.VISIBLE);
		}
		myAdapter.notifyDataSetChanged();
		int size = list.size() % pageSize;
		if (!isLoadmore) {
			// mTravelsListView.setSelection(list.size()-size-1);
		}
		if (size == 0 && list.size() > 0) {
			mTravelListViewFooter.setVisibility(View.VISIBLE);

		} else {

			mTravelListViewFooter.setVisibility(View.GONE);
			all__listview.removeFooterView(mTravelListViewFooter);
		}
	}

}