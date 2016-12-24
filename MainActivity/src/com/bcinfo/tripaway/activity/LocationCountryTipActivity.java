package com.bcinfo.tripaway.activity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.TipsDetailInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LocationCountryTipActivity extends BaseActivity {
	
	private static final String TAG = "WarmpromptTipActivity";
	
	private TextView countryName;
	
	private TextView countryTip;
	
	private String destId;
	
	private String destName;
	
	private ImageView backiv;
	
	private RelativeLayout layout_product_detail_title;
	
	private TipsDetailInfo tipInfoWarmPrompt;

	private String from;

	private TextView productTitle;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.location_country_tip_layout);
		countryName=(TextView) findViewById(R.id.country_name);
		productTitle=(TextView) findViewById(R.id.product_title);
		countryTip=(TextView) findViewById(R.id.country_tip);
		layout_product_detail_title=(RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(255);
		backiv=(ImageView) findViewById(R.id.backiv);
		backiv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent=new Intent(LocationCountryTipActivity.this,LocationCountryDetailActivity.class);
				startActivity(intent);*/
				finish();
				activityAnimationClose();
				
			}
		});
		destId=getIntent().getStringExtra("destinationId");
		destName=getIntent().getStringExtra("countryName");
		if(!StringUtils.isEmpty(destName)){
			countryName.setText(destName+":");
			QueryTipDetail(destId);
		}
		from =getIntent().getStringExtra("from");
		if(!StringUtils.isEmpty(from)&&from.equals("theme")){
			productTitle.setText(getIntent().getStringExtra("title"));;
			countryName.setText(getIntent().getStringExtra("content"));
		}
	}
	private void QueryTipDetail(String destId) {
		RequestParams params = new RequestParams();
		params.put("objectId", destId);
		params.put("objectType", "destination");
		params.put("tipsType", "common");

		HttpUtil.get(Urls.tips_detail, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				LogUtil.i(TAG, "QueryTipDetail",
						"response=" + response.toString());
				String code = response.optString("code");
				String msg = response.optString("msg");
				if (!code.equals("00000")) {
					ToastUtil.showErrorToast(LocationCountryTipActivity.this, msg);
					return;
				}
				tipInfoWarmPrompt = new TipsDetailInfo();
				JSONObject data = response.optJSONObject("data");
				JSONArray tips = data.optJSONArray("tips");
				if (tips != null) {
					try {
						JSONObject tip = (JSONObject) tips.get(0);
						tipInfoWarmPrompt.setContent(tip.optString("content"));
						countryTip.setText(tipInfoWarmPrompt.getContent());
						LogUtil.i(TAG, "QueryTipDetail", "*******************"
								+ tipInfoWarmPrompt.getContent().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					/*
					 * try { tipInfoWarmPrompt.setTitle(((JSONObject)
					 * tips.get(0)).optString("title")); } catch (JSONException
					 * e1) { // TODO Auto-generated catch block
					 * e1.printStackTrace(); }
					 * tipInfoWarmPrompt.setContent(tips.optString(0,
					 * "content"));
					 */
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i(TAG, "QueryTipDetail", "statusCode=" + statusCode);

			}

		});
	}
	
	

}
