package com.bcinfo.tripaway.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import im.yixin.sdk.util.StringUtil;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.ShareSelectDialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class ContentActivity extends BaseActivity {

	private WebView webview;
	private ProgressDialog dialog;
	private String path;
	private String title;
	private String titleImage;
	private String resTitle;
	private String desc;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.content_activity);
		webview = (WebView) findViewById(R.id.webview);
		title = getIntent().getStringExtra("title");
		if (title == null) {
			setSecondTitle("文章内容");
			statisticsTitle = "文章内容";
		} else {
			setSecondTitle(title);
			statisticsTitle = title;
		}
		path = getIntent().getStringExtra("path");
		titleImage = getIntent().getStringExtra("titleImage");
		resTitle = getIntent().getStringExtra("resTitle");
		if (StringUtils.isEmpty(resTitle))
			resTitle = "";
		desc = getIntent().getStringExtra("desc");
		if (StringUtils.isEmpty(desc))
			desc = "";
		ImageView product_service_share_button = (ImageView) findViewById(R.id.product_service_share_button);
		if ("首付游".equals(title)) {
			product_service_share_button.setVisibility(View.GONE);
		} else {
			product_service_share_button.setVisibility(View.VISIBLE);
		}
		product_service_share_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(path)) {
					if (!StringUtils.isEmpty(titleImage)) {
						ShareSelectDialog shareSelectDialog = new ShareSelectDialog(ContentActivity.this, titleImage,
								desc, resTitle, "", path, "", true);
						shareSelectDialog.show();
					} else {
						ShareSelectDialog shareSelectDialog = new ShareSelectDialog(ContentActivity.this, "", title, "",
								"", path, "", true);
						shareSelectDialog.show();
					}
				}
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				dialog.dismiss();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 如果是打开的是百度URL,则进行跳转Activity
				if (url.trim().contains(Urls.ShareUrlOfProduct)) {
					// 活动跳转的方法
					int start = url.indexOf("-");
					int end = url.indexOf(".html");
					String id = null;
					if (start > 0 && end > 0 && start + 1 < url.trim().length()) {
						id = url.trim().substring(start + 1, end);
					}
					if (id != null)
						JumpActvity(id);
				}
				if (url.trim().contains(Urls.UrlOfOrderDetail)) {
					// 订单详情跳转的方法
					JumpOrderDetalActvity(url);
				}
				if (url.trim().contains(Urls.e_UrlOfOrderDetail)) {
					finish();
				} 
				else {
					loadUrl(view, url);
				}

				return true;
			}
		});
		loadUrl(webview, path);
		dialog = ProgressDialog.show(this, null, "页面加载中，请稍后..");
		dialog.setCancelable(true);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// User settings

		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setUseWideViewPort(true);// 关键点

		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		webSettings.setDisplayZoomControls(false);
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
		webSettings.setAllowFileAccess(true); // 允许访问文件
		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		webSettings.setSupportZoom(true); // 支持缩放

		webSettings.setLoadWithOverviewMode(true);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 240) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == 160) {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 120) {
			webSettings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == DisplayMetrics.DENSITY_TV) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		}
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				AlertDialog.Builder b2 = new AlertDialog.Builder(
						ContentActivity.this)
						.setTitle("提示")
						.setMessage(message)
						.setPositiveButton("ok",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										result.confirm(); // MyWebView.this.finish();
															// } });
									}
								});
				b2.setCancelable(false);
				b2.create();
				b2.show();
				return true;
			}
		});

	}

	private void JumpActvity(String id) {
		ProductNewInfo productNewInfo2 = new ProductNewInfo();
		productNewInfo2.setProductType("team");
		productNewInfo2.setId(id);
		ActivityUtil.toProductHomePage(productNewInfo2, this);
	}

	private void JumpOrderDetalActvity(String url) {
		String strs[] = url.split("-");
		if (strs != null && strs.length == 3) {
			int end = strs[2].indexOf(".html");
			String id = null;
			if (end > 0) {
				id = strs[2].substring(0, end);
			}
			if (id != null) {
				System.out.print(id);
				Intent it = new Intent(this, MyOrderDetailActivity.class);
				it.putExtra("orderId", id);
				startActivity(it);
				activityAnimationOpen();
			}
		}

	}

	private void loadUrl(WebView v, String url) {
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = s.format(new Date());
		Map<String, String> map = new HashMap<String, String>(2);
		map.put("timestamp", time);
		if (!StringUtils.isEmpty(PreferenceUtil.getSession())) {
			MCryptUtil mCryptUtil = new MCryptUtil();
			try {
				map.put("secretStr", mCryptUtil.Encrypt(PreferenceUtil.getSession() + time));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				map.put("secretStr", null);
				e.printStackTrace();
			}
		} else {
			map.put("secretStr", null);
		}
		v.loadUrl(url, map);
	}
}
