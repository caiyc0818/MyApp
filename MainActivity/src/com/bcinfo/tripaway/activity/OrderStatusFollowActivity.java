package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.OrderStatusFollowAdapter;
import com.bcinfo.tripaway.bean.DispatchInfo;
import com.bcinfo.tripaway.bean.TraceInfo;

public class OrderStatusFollowActivity extends BaseActivity {

	private ArrayList<TraceInfo> traceList;
	private ArrayList<DispatchInfo> dispatchInfos;
	
	private OrderStatusFollowAdapter  adapter;
	
	private ListView orderStatusListview;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.order_status_following);
		setSecondTitle("状态跟踪");
		orderStatusListview = (ListView) findViewById(R.id.order_status);
		traceList =getIntent().getParcelableArrayListExtra("trace");
		dispatchInfos =getIntent().getParcelableArrayListExtra("dispatch");
		adapter = new OrderStatusFollowAdapter(getApplication(), traceList,dispatchInfos);
		orderStatusListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
