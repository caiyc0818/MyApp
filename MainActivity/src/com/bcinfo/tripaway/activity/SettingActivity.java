package com.bcinfo.tripaway.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
















import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.fragment.SettingFragment;
import com.bcinfo.tripaway.fragment.SettingFragment1;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.view.UpdateCilent;
import com.bcinfo.tripaway.view.dialog.ApplyExitDialog;
import com.bcinfo.tripaway.view.dialog.SettingCacheDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

/**
 * 设置
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月22日 19:07:12
 */
public class SettingActivity extends BaseActivity 
{
    protected static final String TAG = "SettingActivity";
    public boolean isNewVersion = false;

private SettingFragment1 settingFragment;
    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);
        setContentView(R.layout.setting_main);
        setSecondTitle("设置");
        settingFragment=new SettingFragment1();
        getSupportFragmentManager().beginTransaction().add(R.id.container,  settingFragment).commit();
        
    }

//    /*
//     * 测试 查询最新版本
//     */
//    private void checkUpdateVersionUrl(String channel)
//    {
//        try
//        {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("appid", packageName);
//            jsonObject.put("channel", channel);
//            jsonObject.put("ver", versionCode);
//            System.out.println("jsonObject=" + jsonObject.toString());
//            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
//            HttpUtil.post(Urls.version_update_url, stringEntity, new JsonHttpResponseHandler()
//            {
//                
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
//                {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, responseString, throwable);
////                    ToastUtil.showToast(MainActivity.this, throwable.getMessage());
//                }
//                
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
//                {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, throwable, errorResponse);
////                    ToastUtil.showToast(MainActivity.this, throwable.getMessage());
//                }
//                
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
//                {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, response);
//                    LogUtil.e("SettingUpdateVersionActivity", "checkUpdateVersionUrl", response.toString());
//                    if (response.optString("code").equals("00080") || response.optString("code").equals("00083"))
//                    {
//                        isNewVersion = false;
//                    }
//                    else
//                    {
//                        isNewVersion = true;
//                    }
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("code", response.optString("code"));
//                    JSONObject dataJson = response.optJSONObject("data");
//                    if (dataJson != null && dataJson.length() > 0)
//                    {
//                        map.put("versionCode", dataJson.optString("versionCode"));
//                        map.put("versionName", dataJson.optString("versionName"));
//                        map.put("url", dataJson.optString("url"));
//                        map.put("info", dataJson.optString("info"));
//                    }
//                    UpdateCilent.getUpdateClient().showDialog(MainActivity.this, map, false, handler);
//                }
//                
//            });
//        }
//        catch (Exception e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

   
}
