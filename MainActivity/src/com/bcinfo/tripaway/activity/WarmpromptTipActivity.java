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
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WarmpromptTipActivity extends BaseActivity {
	private static final String TAG = "WarmpromptTipActivity";
	private String content;
	private TextView warmprompt_text;
	private TipsDetailInfo tipInfoWarmPrompt;
	private String productId;
	private RelativeLayout layout_product_detail_title;
	private View loginLoading;
	private AnimationDrawable loadingAnimation;
	private TextView product_title;
	StringBuffer price_include = new StringBuffer();
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.warmprompt_tip_layout);
		statisticsTitle="温馨提示";
		loginLoading = (View) findViewById(R.id.login_loading);
		loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		loadingAnimation.start();
		product_title = (TextView) findViewById(R.id.product_title);
		String type = getIntent().getStringExtra("tipsType");
		warmprompt_text = (TextView) findViewById(R.id.warmprompt_text);
		productId = getIntent().getStringExtra("productId");
		layout_product_detail_title = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		layout_product_detail_title.getBackground().setAlpha(255);
		ImageView backButton = (ImageView) findViewById(R.id.back);
		if ("hint".equals(type)) {
			QueryTipDetail(productId, "hint");
			product_title.setText("温馨提示");    
		} else if ("price_include".equals(type)) {
			product_title.setText("费用包含/不包含");             
			QueryTipDetail(productId, "price_include");
			QueryTipDetail(productId, "price_exclusive");
			warmprompt_text.setText(price_include);
		}else if ("visi_matierial".equals(type)) {
			product_title.setText("签证材料");
			QueryTipDetail(productId, "visi_matierial");
		} else if ("order_instruction".equals(type)) {
			product_title.setText("预定须知");
			QueryTipDetail(productId, "order_instruction");
		}else if ("how_to".equals(type)) {
			product_title.setText("怎么去");
			QueryTipDetail(productId, "how_to");
		}else if ("refund_instruction".equals(type)) {
			product_title.setText("退款须知");
			QueryTipDetail(productId, "refund_instruction");
		}
			
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				activityAnimationClose();
			}
		});

	}

	private void QueryTipDetail(String productId, final String type) {
		RequestParams params = new RequestParams();
		params.put("objectId", productId);
		params.put("objectType", "product");
		params.put("tipsType", type);
		HttpUtil.get(Urls.tips_detail, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				LogUtil.i(TAG, "QueryTipDetail", "response=" + response.toString());
				loadingAnimation.stop();
				loginLoading.setVisibility(View.GONE);
				String code = response.optString("code");
				String msg = response.optString("msg");
				if (!code.equals("00000")) {
					ToastUtil.showErrorToast(WarmpromptTipActivity.this, msg);
					return;
				}
				tipInfoWarmPrompt = new TipsDetailInfo();
				JSONObject data = response.optJSONObject("data");
				JSONArray tips = data.optJSONArray("tips");
				if (tips != null) {
					try {
						JSONObject tip = (JSONObject) tips.get(0);
						tipInfoWarmPrompt.setContent(tip.optString("content"));
						if (type.equals("price_include")) {
							price_include.append("费用包含"+"\n");
							price_include.append(tipInfoWarmPrompt.getContent());
							price_include.append("\n");
						} else if(type.equals("price_exclusive")){
							price_include.append("费用不包含\n");
							price_include.append(tipInfoWarmPrompt.getContent());
							warmprompt_text.setText(price_include);
						}else{
							warmprompt_text.setText(tipInfoWarmPrompt.getContent());
						}
						LogUtil.i(TAG, "QueryTipDetail",
								"*******************" + tipInfoWarmPrompt.getContent().toString());
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
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i(TAG, "QueryTipDetail", "statusCode=" + statusCode);

			}

		});
	}

}
