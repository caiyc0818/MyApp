package com.bcinfo.tripaway.activity;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.UpdateCilent;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 设置-版本升级
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月11日 14:54:22
 */

public class SettingUpdateVersionActivity extends BaseActivity implements OnClickListener
{
    private TextView usageTv;// 使用条款
    
    private TextView privateTv;// 隐私策略
    
    private LinearLayout versionUpdateBtn; // 检查新版本
    
    private TextView versionTxt;
    
    /**
     * 版本号
     */
    private int versionCode;
    
    /**
     * 版本名
     */
    private String versionName;
    
    /**
     * 包名
     */
    private String packageName;
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        setContentView(R.layout.setting_version_update_activity);
        statisticsTitle="版本检查";
        setSecondTitle("版本升级");
        RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.setting_second_titlebar);
        titleLayout.setAlpha(1);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
        usageTv = (TextView)findViewById(R.id.setting_usage_introduce_tv);
        privateTv = (TextView)findViewById(R.id.setting_private_policy_tv);
        versionUpdateBtn = (LinearLayout)findViewById(R.id.layout_update_version_container);
        versionTxt = (TextView)findViewById(R.id.setting_app_version_text);
        versionUpdateBtn.setOnClickListener(this);
        usageTv.setOnClickListener(this);
        privateTv.setOnClickListener(this);
        initData();
    }
    
    private void initData()
    {
        getVersionInfo();
        versionTxt.setText("版本：" + versionName);
    }
    
    /**
     * 获取当前客户端版本信息
     */
    private void getVersionInfo()
    {
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
            packageName = info.packageName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.setting_usage_introduce_tv:
                Toast.makeText(this, "使用条款", 0).show();
                break;
            case R.id.setting_private_policy_tv:
                Toast.makeText(this, "隐私策略", 0).show();
                break;
            case R.id.layout_update_version_container:
                ApplicationInfo appInfo = null;
                try
                {
                    appInfo =
                        this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                }
                catch (NameNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String channel = appInfo.metaData.getString("BaiduMobAd_CHANNEL", "official");
                checkUpdateVersionUrl(channel);
                break;
            default:
                break;
        }
        
    }
    
    /*
     * 测试 查询最新版本
     */
    private void checkUpdateVersionUrl(String channel)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appid", packageName);
            jsonObject.put("channel", channel);
            jsonObject.put("ver", versionCode);
            System.out.println("jsonObject=" + jsonObject.toString());
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            HttpUtil.post(Urls.version_update_url, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
//                    ToastUtil.showToast(SettingUpdateVersionActivity.this, throwable.getMessage());
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                    ToastUtil.showToast(SettingUpdateVersionActivity.this, throwable.getMessage());
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.e("SettingUpdateVersionActivity", "checkUpdateVersionUrl", response.toString());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("code", response.optString("code"));
                    JSONObject dataJson = response.optJSONObject("data");
                    if (dataJson != null && dataJson.length() > 0)
                    {
                        map.put("versionCode", dataJson.optString("versionCode"));
                        map.put("versionName", dataJson.optString("versionName"));
                        map.put("url", dataJson.optString("url"));
                        map.put("info", dataJson.optString("info"));
                    }
                    UpdateCilent.getUpdateClient().showDialog(SettingUpdateVersionActivity.this, map, true);
                }
                
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
