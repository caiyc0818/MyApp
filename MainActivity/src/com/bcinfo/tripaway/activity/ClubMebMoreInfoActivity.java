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

public class ClubMebMoreInfoActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout idVerifyLayout;
	private LinearLayout phoneVerifyLayout;
	private LinearLayout emailVerifyLayout;
	private TextView adress;
	private TextView language;
	private FlowLayout serviceArieaFlow;
	private FlowLayout serviceContentFlow;
//	private UserInfo user;
	private LinearLayout verifyLayout;
	private TextView second_title_text;
	
	   private LinearLayout backLayout;
	   
	   private ArrayList<String> languagesList;
	   private Tags tag;
	   private String certifyCode;
	   private String address;
	   private String realName;
	   

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		setContentView(R.layout.driver_more_info);
		// setSecondTitle("姓名");
		// getHelp("100","1");
		Intent intent=getIntent();
//		user = getIntent().getParcelableExtra("user");
		languagesList=intent.getStringArrayListExtra("languagesList");
		tag=intent.getParcelableExtra("tag");
		certifyCode=intent.getStringExtra("certifyCode");
		address=intent.getStringExtra("address");
		realName=intent.getStringExtra("realName");
		
//		setSecondTitle(user.getRealName());
		findView();
		setValue();
		second_title_text.setText(realName);
		   RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
	        titleLayout.setAlpha(1);
	        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
	        backLayout.setOnClickListener(this);
	}

	private void findView() {
		idVerifyLayout = (LinearLayout) findViewById(R.id.idVerify);
		phoneVerifyLayout = (LinearLayout) findViewById(R.id.idVerify);
		emailVerifyLayout = (LinearLayout) findViewById(R.id.emailVerify);
		verifyLayout = (LinearLayout) findViewById(R.id.verifyLayout);
	      backLayout = (LinearLayout)findViewById(R.id.layout_back_button);
		second_title_text=(TextView) findViewById(R.id.second_title_text);
		adress = (TextView) findViewById(R.id.adress);
		language = (TextView) findViewById(R.id.language);
		serviceArieaFlow = (FlowLayout) findViewById(R.id.serviceArieaFlow);
		serviceContentFlow = (FlowLayout) findViewById(R.id.serviceContentFlow);
	}

	private void setValue() {
//		String certifyCode = user.getCertifyCode();
		if (StringUtils.isEmpty(certifyCode)) {
			verifyLayout.setVisibility(View.GONE);
		} else {
			if (certifyCode.charAt(0) == '0') {
				idVerifyLayout.setVisibility(View.GONE);
			}
			if (certifyCode.charAt(1) == '0') {
				phoneVerifyLayout.setVisibility(View.GONE);
			}
			if (certifyCode.charAt(2) == '0') {
				emailVerifyLayout.setVisibility(View.GONE);
			}
		}
		if (languagesList!=null&&languagesList.size() > 0) {
			String languages = "";
			for (String str : languagesList) {
				languages += str + "、";
			}
			languages = languages.substring(0, languages.length() - 1);
			language.setText(languages);
		}
//		Tags tag=user.getTag();
		if(tag!=null&&tag.getServAreas()!=null&&tag.getServAreas().size()>0){
			addFlowView(tag.getServAreas(),serviceArieaFlow);
		}
		if(tag!=null&&tag.getClubTypes()!=null&&tag.getClubTypes().size()>0){
			addFlowView(tag.getClubTypes(),serviceContentFlow);
		}
		if(!StringUtils.isEmpty(address))
		adress.setText(address);
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

	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(this);
			newView.setBackgroundResource(R.drawable.shape_person_info);
			;
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			newView.setTextColor(Color.BLACK);
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(this, 2);
			params.bottomMargin = DensityUtil.dip2px(this, 5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}
}
