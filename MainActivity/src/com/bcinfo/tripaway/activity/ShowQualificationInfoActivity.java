package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.HelpAndConsultAdapter;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.HelpInfo;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wefika.flowlayout.FlowLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowQualificationInfoActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout idVerifyLayout;
	private LinearLayout phoneVerifyLayout;
	private LinearLayout emailVerifyLayout;
	private LinearLayout idVerifyLayout1;
	private LinearLayout phoneVerifyLayout1;
	private LinearLayout emailVerifyLayout1;
	private LinearLayout driverCardVerifyLayout;
	private LinearLayout guideCardVerifyLayout;
	private LinearLayout driverCardVerifyLayout1;
	private LinearLayout guideCardVerifyLayout1;
	
	   private LinearLayout backLayout;
	   
	   private String certifyCode;
	private TextView second_title_text;
	   

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		setContentView(R.layout.qualification_more_info);
		Intent intent=getIntent();
		certifyCode=intent.getStringExtra("certifyCode");
		
		findView();
		setValue();
		second_title_text.setText("查看资质");
		   RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
	        titleLayout.setAlpha(1);
	        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
	        backLayout.setOnClickListener(this);
	}

	private void findView() {
		second_title_text= (TextView) findViewById(R.id.second_title_text);
		idVerifyLayout = (LinearLayout) findViewById(R.id.idVerifyLayout);
		phoneVerifyLayout = (LinearLayout) findViewById(R.id.phoneVerifyLayout);
		emailVerifyLayout = (LinearLayout) findViewById(R.id.emailVerifyLayout);
		idVerifyLayout1 = (LinearLayout) findViewById(R.id.idVerifyLayout1);
		phoneVerifyLayout1 = (LinearLayout) findViewById(R.id.phoneVerifyLayout1);
		emailVerifyLayout1 = (LinearLayout) findViewById(R.id.emailVerifyLayout1);
		driverCardVerifyLayout= (LinearLayout) findViewById(R.id.driverCardVerifyLayout);
		guideCardVerifyLayout= (LinearLayout) findViewById(R.id.guideCardVerifyLayout);
		driverCardVerifyLayout1= (LinearLayout) findViewById(R.id.driverCardVerifyLayout1);
		guideCardVerifyLayout1= (LinearLayout) findViewById(R.id.guideCardVerifyLayout1);
		backLayout=(LinearLayout) findViewById(R.id.layout_back_button);
	}

	private void setValue() {
		if (StringUtils.isEmpty(certifyCode)) {
		} else {
			if (certifyCode.charAt(0) == '0') {
				idVerifyLayout.setVisibility(View.GONE);
				idVerifyLayout1.setVisibility(View.VISIBLE);
			}else {
				idVerifyLayout.setVisibility(View.VISIBLE);
				idVerifyLayout1.setVisibility(View.GONE);
			}
			if (certifyCode.charAt(2) == '0') {
				phoneVerifyLayout.setVisibility(View.GONE);
				phoneVerifyLayout1.setVisibility(View.VISIBLE);
			}else {
				phoneVerifyLayout1.setVisibility(View.GONE);
				phoneVerifyLayout.setVisibility(View.VISIBLE);
			}
			if (certifyCode.charAt(1) == '0') {
				emailVerifyLayout.setVisibility(View.GONE);
				emailVerifyLayout1.setVisibility(View.VISIBLE);
			}else{
				emailVerifyLayout1.setVisibility(View.GONE);
				emailVerifyLayout.setVisibility(View.VISIBLE);
			}
			if (certifyCode.charAt(3) == '0') {
				driverCardVerifyLayout.setVisibility(View.GONE);
				driverCardVerifyLayout1.setVisibility(View.VISIBLE);
			}else{
				driverCardVerifyLayout1.setVisibility(View.GONE);
				driverCardVerifyLayout.setVisibility(View.VISIBLE);
			}
			if (certifyCode.charAt(4) == '0') {
				guideCardVerifyLayout.setVisibility(View.GONE);
				guideCardVerifyLayout1.setVisibility(View.VISIBLE);
			}else{
				guideCardVerifyLayout1.setVisibility(View.GONE);
				guideCardVerifyLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			this.overridePendingTransition(R.anim.activity_back,
					R.anim.activity_finish);
			break;

		default:
			break;
		}

	}

}
