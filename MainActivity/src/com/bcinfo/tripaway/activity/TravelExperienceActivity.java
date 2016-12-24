package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductTripPlanListAdapter;
import com.bcinfo.tripaway.adapter.TravelExperienceAdapter;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TravelExperienceActivity extends BaseActivity {
	protected static final String TAG = "TravelExperienceActivity";
	private String nickName;
	private String productId;
	private String userId;
	private ImageView backButton;
	private MyListView personal_experience_listview;
	private TextView serverExperienceTitle;
	private TextView responseTv;
	private TravelExperienceAdapter travelExperienceAdapter;
	private ArrayList<Experiences> experiences = new ArrayList<Experiences>();
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.travel_experience);
		nickName=getIntent().getStringExtra("nickName");
		productId=getIntent().getStringExtra("productId");
		serverExperienceTitle=(TextView) findViewById(R.id.server_experience);
		backButton=(ImageView) findViewById(R.id.bact_btn);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
				activityAnimationClose();
			}
		});
		//personal_experience_listview=(MyListView) findViewById(R.id.personal_experience_listview);
		serverExperienceTitle.setText(nickName+"的旅行经历");
		//experiences=getIntent().getParcelableArrayListExtra("experiences");
		//LogUtil.i(TAG, "experiences", "********************" + experiences.size()+"**************"+experiences.toString());
		userId = getIntent().getStringExtra("userId");
		QueryUserInfo(userId);
		initTravelExperience();
		System.out.println("************initTravelExperience");
		//responseTv.setText("***********************"+userId);
	}
	private void initTravelExperience() {
		
		personal_experience_listview = (MyListView) findViewById(R.id.personal_experience_listview);
		travelExperienceAdapter = new TravelExperienceAdapter(this,
				experiences);
		personal_experience_listview.setAdapter(travelExperienceAdapter);
		System.out.println("****************travelExperienceAdapter");
		personal_experience_listview.setDividerHeight(0);
		//personal_experience_listview.setOnItemClickListener(mTripItemClick);
	}
	private void QueryUserInfo(final String userId){

		// LogUtil.e(TAG, "QueryProductDetail", Urls.product_detail +
		// productId);
		HttpUtil.get(Urls.personal_url + userId,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogUtil.i(TAG, "QueryUserInfo", "response="
								+ response.toString());
						//responseTv.setText("*******"+userId+"******"+response.toString());
						String code = response.optString("code");
						String msg = response.optString("msg");
						if (!code.equals("00000")) {
							ToastUtil.showErrorToast(
									TravelExperienceActivity.this, msg);
							return;
						}
						JSONObject data = response.optJSONObject("data");
						if (data == null || data.toString().equals("")
								|| data.toString().equals("null")) {
							return;
						}
						JSONObject userInfoObject = data
								.optJSONObject("userInfo");
						JSONArray experiencesJSONList = userInfoObject
								.optJSONArray("experiences");
						if (experiencesJSONList == null
								|| experiencesJSONList.toString().equals("")
								|| experiencesJSONList.toString()
										.equals("null")) {
							System.out.println("experiencesJSONList是空"+experiencesJSONList.toString());
							return;
						}
						for (int j = 0; j < experiencesJSONList.length(); j++) {
							JSONObject experienceJSON = experiencesJSONList
									.optJSONObject(j);
							Experiences experience = new Experiences();
							experience.setDescription(experienceJSON
									.optString("description"));
							experience.setTravelTime(experienceJSON
									.optString("travelTime"));
							JSONArray imageJSONList = experienceJSON
									.optJSONArray("images");
							if (imageJSONList == null
									|| imageJSONList.toString().equals("")
									|| imageJSONList.toString().equals("null")) {
								return;
							}
							for (int m = 0; m < imageJSONList.length(); m++) {
								ImageInfo imageInfo = new ImageInfo();
								JSONObject imageJSON = imageJSONList
										.optJSONObject(m);
								imageInfo.setUrl(imageJSON.optString("url"));
								if(imageJSON.optString("description")!=null||!imageJSON
										.optString("description").equals("null")||!imageJSON
										.optString("description").equals("")){
								imageInfo.setDesc(imageJSON
										.optString("description"));
								}
								experience.getImages().add(imageInfo);

							}
							experiences.add(experience);
							travelExperienceAdapter.notifyDataSetChanged();
							System.out.println(experiences.size()+"*******"+experiences.toString());
							
						}
						
						
						
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
						LogUtil.i(TAG, "QueryProductDetail", "statusCode="
								+ statusCode);
						LogUtil.i(TAG, "QueryProductDetail", "responseString="
								+ responseString);
						for (int i = 0; i < headers.length; i++) {
							LogUtil.i(TAG, "QueryProductDetail", "key-->"
									+ headers[i].getName() + "--getValue-->"
									+ headers[i].getValue());
						}
					}

				});

	
		
		
	}

}
