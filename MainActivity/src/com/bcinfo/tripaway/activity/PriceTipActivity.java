package com.bcinfo.tripaway.activity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TipsDetailInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PriceTipActivity extends BaseActivity implements OnClickListener {
	private TextView price_include;
	private TextView price_exclusive;
	private ImageView back;

	private String priceInclude;

	private String priceexclusive;

	private TipsDetailInfo tipInfoIncludPrice;

	private TipsDetailInfo tipInfoPriceExclusive;

	private String productId;

	private RelativeLayout titleBarLayout;

	private RelativeLayout layout_product_detail_title;
	/**
	 * 退款政策展开
	 */
	private TextView club_btn_more;
	private TextView refund_include;
	private boolean isClickMore = false;
	private boolean hasMesure = false;

	private int maxLines;
	private static final String TAG = "PriceTipActivity";
	/*
	 * <<<<<<< .mine
	 * 
	 * @Override protected void onCreate(Bundle arg0) { super.onCreate(arg0);
	 * setContentView(R.layout.price_tip_layout); price_include=(TextView)
	 * findViewById(R.id.price_include); price_exclusive=(TextView)
	 * findViewById(R.id.price_exclusive);
	 * layout_product_detail_title=(RelativeLayout)
	 * findViewById(R.id.layout_product_detail_title);
	 * layout_product_detail_title.getBackground().setAlpha(255);
	 * back=(ImageView) findViewById(R.id.back); back.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent intent =new
	 * Intent(PriceTipActivity.this,GrouponProductNewDetailActivity.class);
	 * startActivity(intent); activityAnimationClose();
	 * 
	 * } }); priceInclude=getIntent().getStringExtra("price_include");
	 * priceexclusive=getIntent().getStringExtra("price_exclusive");
	 * price_include.setText(priceInclude);
	 * price_exclusive.setText(priceexclusive); productId =
	 * getIntent().getStringExtra("productId"); QueryPriceInclud(productId);
	 * QueryTipPriceExclusive(productId);
	 * 
	 * 
	 * } ======= >>>>>>> .r1758
	 */

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.price_tip_layout);
		price_include = (TextView) findViewById(R.id.price_include);
		price_exclusive = (TextView) findViewById(R.id.price_exclusive);
		titleBarLayout = (RelativeLayout) findViewById(R.id.layout_product_detail_title);
		// titleBarLayout.setAlpha(255);
		refund_include = (TextView) findViewById(R.id.refund_include);
		ProductNewInfo productNewInfo = getIntent().getParcelableExtra("productInfo");
		if (productNewInfo != null) {
			String[] strText = productNewInfo.getPolicyContent().toString().split("\\r\n");
			String string = "";
			if (strText.length > 1) {
				for (int i = 1; i < strText.length; i++) {
					if (strText.length - 1 != i) {
						string = string + strText[i] + "\n";
					} else {
						string = string + strText[i];
					}
				}
				String ab=string;
				refund_include.setText(string);
			} else {

				refund_include.setText(productNewInfo.getPolicyContent().toString());
			}
		}
		// 计算退款政策与说明
		ViewTreeObserver viewTreeObserver = refund_include.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!hasMesure) {
					maxLines = refund_include.getLineCount();
					if (maxLines > 3) {
						refund_include.setMaxLines(3);
						refund_include.setEllipsize(TruncateAt.END);
					}
					if (maxLines > 3) {
						club_btn_more.setVisibility(View.VISIBLE);
					} else {
						club_btn_more.setVisibility(View.GONE);
					}
					hasMesure = true;
				}
				return true;
			}
		});
		club_btn_more = (TextView) findViewById(R.id.club_btn_more);
		club_btn_more.setOnClickListener(this);
		titleBarLayout.getBackground().setAlpha(255);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				activityAnimationClose();

			}
		});
		/*
		 * priceInclude=getIntent().getStringExtra("price_include");
		 * priceexclusive=getIntent().getStringExtra("price_exclusive");
		 * price_include.setText(priceInclude);
		 * price_exclusive.setText(priceexclusive);
		 */
		productId = getIntent().getStringExtra("productId");
		QueryPriceInclud(productId);
		QueryTipPriceExclusive(productId);

	}

	private void QueryPriceInclud(String productId) {
		RequestParams params = new RequestParams();
		params.put("objectId", productId);
		params.put("objectType", "product");
		params.put("tipsType", "price_include");

		HttpUtil.get(Urls.tips_detail, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				LogUtil.i(TAG, "QueryTipPriceExclusive", "response=" + response.toString());
				String code = response.optString("code");
				String msg = response.optString("msg");
				if (!code.equals("00000")) {
					ToastUtil.showErrorToast(PriceTipActivity.this, msg);
					return;
				}
				tipInfoIncludPrice = new TipsDetailInfo();
				JSONObject data = response.optJSONObject("data");
				JSONArray tips = data.optJSONArray("tips");
				if (tips != null) {
					try {
						JSONObject tip = (JSONObject) tips.get(0);
						tipInfoIncludPrice.setContent(tip.optString("content"));
						price_include.setText(tipInfoIncludPrice.getContent());
						LogUtil.i(TAG, "QueryTipDetail",
								"*******************" + tipInfoIncludPrice.getContent().toString());
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

	private void QueryTipPriceExclusive(String productId) {
		RequestParams params = new RequestParams();
		params.put("objectId", productId);
		params.put("objectType", "product");
		params.put("tipsType", "price_exclusive");

		HttpUtil.get(Urls.tips_detail, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				LogUtil.i(TAG, "QueryTipPriceExclusive", "response=" + response.toString());
				String code = response.optString("code");
				String msg = response.optString("msg");
				if (!code.equals("00000")) {
					ToastUtil.showErrorToast(PriceTipActivity.this, msg);
					return;
				}
				tipInfoPriceExclusive = new TipsDetailInfo();
				JSONObject data = response.optJSONObject("data");
				JSONArray tips = data.optJSONArray("tips");
				if (tips != null) {
					try {
						JSONObject tip = (JSONObject) tips.get(0);
						tipInfoPriceExclusive.setContent(tip.optString("content"));
						price_exclusive.setText(tipInfoPriceExclusive.getContent());
						LogUtil.i(TAG, "QueryTipDetail",
								"*******************" + tipInfoPriceExclusive.getContent().toString());
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.club_btn_more:
			isClickMore = !isClickMore;
			if (isClickMore) {
				club_btn_more.setText("收起");
				refund_include.setMaxLines(maxLines);
				// refund_include.postInvalidate();
			} else {
				club_btn_more.setText("展开");
				refund_include.setMaxLines(3);
				// refund_include.postInvalidate();
			}
			refund_include.requestLayout();
			break;

		default:
			break;
		}
	}

}
