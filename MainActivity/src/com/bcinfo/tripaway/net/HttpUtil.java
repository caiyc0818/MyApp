package com.bcinfo.tripaway.net;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil
{
    
    private static final String TAG = "HttpUtil";
    
    private static String appUserAgent = null;
    
    // private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
    
    // private static SharePreferenceUtil sharedPreferenceUtil = Constant.getInstance().getSpUtil();
    
    // static
    // {
    // client.setTimeout(11000); // 设置链接超时，如果不设置，默认为10s
    // }
    
    private static AsyncHttpClient getHttpClient(String urlString)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);
        client.addHeader("Accept-Encoding", "gzip,deflate");
        Map<String, String> header;
        try
        {
            header = getHeadersMap(urlString);
            for (Map.Entry<String, String> entry : header.entrySet())
            {
                // LogUtil.e(TAG, "addHeader", entry.getKey() + "-->" + entry.getValue());
                client.addHeader(entry.getKey(), entry.getValue());
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return client;
    }
    
    // private static void addHeader(String urlString)
    // {
    // try
    // {
    // Map<String, String> header = getHeadersMap(urlString);
    // for (Map.Entry<String, String> entry : header.entrySet())
    // {
    // // LogUtil.e(TAG, "addHeader", entry.getKey() + "-->" + entry.getValue());
    // client.addHeader(entry.getKey(), entry.getValue());
    // }
    // }
    // catch (Exception e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    
    public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
    {
        AsyncHttpClient client = getHttpClient(urlString);
        if (!urlString.startsWith(Urls.loginUrl))
        {
            addHeaderFlag(client, "", "");
        }
        // addHeader(urlString);
        
        client.get(urlString, res);
    }
    
    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res) // url里面带参数
    {
        AsyncHttpClient client = getHttpClient(urlString);
        // addHeader(urlString);
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        client.get(urlString, params, res);
    }
    
    public static void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        // client = new AsyncHttpClient();
        AsyncHttpClient client = getHttpClient(urlString);
        // addHeader(urlString);
        
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        client.get(urlString, res);
    }
    
    public static void addHeaderFlag(AsyncHttpClient client, String tokenValue, String sessionValue)
    {
        client.addHeader("token", tokenValue);
        client.addHeader("session", sessionValue);
    }
    
    /*
     * get 请求 返回json响应数据
     */
    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
    {
        AsyncHttpClient client = getHttpClient(urlString);
        if (urlString.startsWith(Urls.logoutUrl))
        {
            String deviceId = getTelManager(TripawayApplication.application).getDeviceId();
            client.addHeader("session", PreferenceUtil.getSession());
            client.addHeader("deviceID", deviceId);
            
        }
        else
        {
            addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        }
        
        // addHeader(urlString);
        client.get(urlString, params, res);
    }
    
    public static void get(String urlString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
    {
        AsyncHttpClient client = getHttpClient(urlString);
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        // addHeader(urlString);
        client.get(urlString, bHandler);
    }
    
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        AsyncHttpClient client = getHttpClient(url);
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        // addHeader(url);
        client.post(url, params, responseHandler);
    }
    
    public static void post(String url, StringEntity params, AsyncHttpResponseHandler responseHandler)
    {
        AsyncHttpClient client = getHttpClient(url);
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        // addHeader(url);
        client.post(TripawayApplication.application, url, params, "application/json;charset=UTF-8", responseHandler);
    }
    
    public static void delete(String url, AsyncHttpResponseHandler responseHandler)
    {
        AsyncHttpClient client = getHttpClient(url);
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        client.delete(url, responseHandler);
    }
    
    public static void delete(String url,RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        AsyncHttpClient client = getHttpClient(url);
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        client.delete(url,params, responseHandler);
    }
    
    public static void patch(String url, StringEntity params, AsyncHttpResponseHandler responseHandler)
    {
        AsyncHttpClient client = getHttpClient(url);
        addHeaderFlag(client, PreferenceUtil.getToken(), PreferenceUtil.getSession());
        client.patch(TripawayApplication.application, url, params, "application/json;charset=UTF-8", responseHandler);
    }
    
    // public static AsyncHttpClient getClient()
    // {
    // return client;
    // }
    
    /**
     * 获取Http头域
     * 
     * @param application
     * @param url
     * @param userId
     * @return
     * @throws Exception
     */
    public static Map<String, String> getHeadersMap(String url)
        throws Exception
    {
        String deviceId = getTelManager(TripawayApplication.application).getDeviceId();// IMEI
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = s.format(new Date());
        
        String partUrl = url.substring(Urls.host.length() - 1);
        // Log.e("getHeadersMap", "partUrl-->" + partUrl);
        String mUserId = PreferenceUtil.getAccount();
        String securityRes = partUrl + deviceId + MCryptUtil.APPCLIENT_KEY + time + mUserId;
        
        Log.e("getHeadersMap", "securityRes-->" + securityRes);
        MCryptUtil mCryptUtil = new MCryptUtil();
        String security = mCryptUtil.Encrypt(securityRes);
        
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("deviceID", deviceId);
        headers.put("x-bci-user-id", mUserId);
        headers.put("security", security);
        headers.put("timestamp", time);
        headers.put("x-bci-platform", "10");
        headers.put("x-bci-user-agent", getUserAgent(TripawayApplication.application));
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        // headers.put("charset", HTTP.UTF_8);
        
        return headers;
    }
    
    public static TelephonyManager getTelManager(Context context)
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        
        return tm;
    }
    
    /**
     * 用户手机信息 appUserAgent=Tripaway/versionName_versionCode/Android/手机系统版本/手机型号/ 客户端唯一标识
     * 
     * @param application
     * @return
     */
    public static String getUserAgent(TripawayApplication application)
    {
        if (appUserAgent == null || appUserAgent == "")
        {
            StringBuilder ua = new StringBuilder("Tripaway");
            ua.append('/' + application.getPackageInfo().versionName + '_' + application.getPackageInfo().versionCode);// App版本
            ua.append("/Android");// 手机系统平台
            ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
            ua.append("/" + android.os.Build.MODEL); // 手机型号
            ua.append("/" + application.getAppId());// 客户端唯一标识
            appUserAgent = ua.toString();
            // appUserAgent=Tripaway/versionName_versionCode/Android/手机系统版本/手机型号/客户端唯一标识
        }
        return appUserAgent;
    }
}
