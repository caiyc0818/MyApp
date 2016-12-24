package com.bcinfo.tripaway.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.LogUtil;

/**
 * @author hanweipeng
 * @date 2015-7-29 下午2:12:41
 */
public class LinkDetailActivity extends BaseActivity
{
    private final static String TAG = "LinkDetailActivity";
    
    private WebView webview;
    
    private String title;
    
    private String linkUrl;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.link_activity);
        setSecondTitle("专题活动");
        linkUrl = getIntent().getStringExtra("link");
        webview = ((WebView)findViewById(R.id.link_webview));
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClientDemo());
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.loadUrl(linkUrl);
    }
    
    private class WebViewClientDemo extends WebViewClient
    {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            if (LinkDetailActivity.this != null)
            {
                // showDialog(CommonWebActivity.this.dafult);
                webview.setVisibility(View.GONE);
            }
            else
            {
                return;
            }
            super.onReceivedError(view, errorCode, "", "");
        }
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            LogUtil.e(TAG, "url", url);
            if (url.contains("@"))
            {
                String params = url.substring(url.indexOf("@") + 1);
                String productId = params.substring(0, params.indexOf("@"));
                String productType =
                    params.substring(params.indexOf("@") + 1).substring(0,
                        params.substring(params.indexOf("@") + 1).indexOf("@"));
                String singleType =
                    params.substring(params.indexOf("@") + 1)
                        .substring(params.substring(params.indexOf("@") + 1).indexOf("@") + 1)
                        .substring(0,
                            params.substring(params.indexOf("@") + 1)
                                .substring(params.substring(params.indexOf("@") + 1).indexOf("@") + 1)
                                .indexOf("@"));
                Intent intent = null;
                if (productType.equals("single"))
                {
                    if (singleType.equals("traffic"))
                    {
                        intent = new Intent(LinkDetailActivity.this, CarProductDetailActivity.class);
                        intent.putExtra("productId", productId);
                    }
                    else if (singleType.equals("stay"))
                    {
                        intent = new Intent(LinkDetailActivity.this, HouseProductDetailActivity.class);
                        intent.putExtra("productId", productId);
                    }
                    else
                    {
                        intent = new Intent(LinkDetailActivity.this, GrouponProductNewDetailActivity.class);
                        intent.putExtra("productId", productId);
                    }
                }
                else if (productType.equals("base") || productType.equals("customization"))
                {
                    intent = new Intent(LinkDetailActivity.this, GrouponProductNewDetailActivity.class);
                    intent.putExtra("productId", productId);
                }
                else if (productType.equals("team"))
                {
                    intent = new Intent(LinkDetailActivity.this, GrouponProductNewDetailActivity.class);
                    intent.putExtra("productId", productId);
                }
                startActivity(intent);
                activityAnimationOpen();
            }
            else
            {
                
            }
            return true;
        }
    }
    
    public class MyWebChromeClient extends WebChromeClient
    {
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg)
        {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
        
        @Override
        public void onCloseWindow(WebView window)
        {
            super.onCloseWindow(window);
        }
        
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("提示：").setMessage(message).setPositiveButton("确定", null);
            builder.setOnKeyListener(new DialogInterface.OnKeyListener()
            {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                {
                    return true;
                }
            });
            builder.setCancelable(false);
            builder.create().show();
            result.confirm();
            return true;
        }
        
        public void onProgressChanged(WebView paramWebView, int paramInt)
        {
            super.onProgressChanged(paramWebView, paramInt);
        }
        
        public void onReceivedTitle(WebView paramWebView, String paramString)
        {
            super.onReceivedTitle(paramWebView, paramString);
        }
    }
    
}
