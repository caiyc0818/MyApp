package com.bcinfo.tripaway.fragment;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.MainActivity;
import com.bcinfo.tripaway.activity.SettingAboutUsActivity;
import com.bcinfo.tripaway.activity.SettingHelpActivity;
import com.bcinfo.tripaway.activity.SettingUpdateVersionActivity;
import com.bcinfo.tripaway.activity.SettingUserInfoActivity;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.dialog.ApplyExitDialog;
import com.bcinfo.tripaway.view.dialog.SettingCacheDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 设置 fragment
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月11日 11:26:11
 */
public class SettingFragment extends BaseFragment
{
    /**
     * 自定义清除缓存对话框
     */
    private SettingCacheDialog setDialog;
    
    /**
     * 自定义退出对话框
     */
    private ApplyExitDialog exitDialog;
    
    /**
     * 图片上传条件
     */
    // private RelativeLayout imageUploadLayout;
    
    /**
     * 清除缓存
     */
    // private RelativeLayout cacheClsLayout;
    
    /**
     * 关于我们
     */
    private RelativeLayout aboutUsLayout;
    
    /**
     * 版本检查
     */
    private RelativeLayout versionCheckLayout;
    
    /**
     * 退出
     */
    private RelativeLayout exitLayout;
    
    /**
     * 帮助
     */
    private RelativeLayout helpLayout;
    
    /**
     * 显示 图片品质级别的文本框
     */
    // private TextView imgQualityTv;
    
    /**
     * 品质级别
     */
    private String[] qualityArray;
    
    /**
     * 对话框 确定
     */
    private TextView positiveTv;
    
    /**
     * 对话框 取消
     */
    private TextView negativeTv;
    
    /**
     * 记录点击次数
     */
    private int count;
    
    // private SharePreferenceUtil sharedPreferenceUtil;
    
    /**
     * 个人信息页面
     */
    private RelativeLayout userInfoLayout;
    
    private RoundImageView userPhoto;
    
    private TextView nikeNameTxt;
    
    private TextView accountTxt;
    
    private TextView updateNewTipTv;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View settingView = inflater.inflate(R.layout.setting, container, false);
        qualityArray = getResources().getStringArray(R.array.setting_quality);
        // imgQualityTv = (TextView)settingView.findViewById(R.id.imageQualityText);
        // imgQualityTv.setText(qualityArray[0]);
        // imageUploadLayout = (RelativeLayout)settingView.findViewById(R.id.setting_ImageUpload);// 图片上传条件
        // cacheClsLayout = (RelativeLayout)settingView.findViewById(R.id.setting_ClsCache);// 清除缓存
        aboutUsLayout = (RelativeLayout)settingView.findViewById(R.id.setting_AboutUs);// 关于我们
        versionCheckLayout = (RelativeLayout)settingView.findViewById(R.id.setting_CheckForUpdate);// 版本检查
        updateNewTipTv = (TextView)settingView.findViewById(R.id.updateNewTip);
        if (((MainActivity)getActivity()).isNewVersion)
        {
            updateNewTipTv.setVisibility(View.VISIBLE);
        }
        else
        {
            updateNewTipTv.setVisibility(View.GONE);
        }
        helpLayout = (RelativeLayout)settingView.findViewById(R.id.setting_Help);// 帮助
        exitLayout = (RelativeLayout)settingView.findViewById(R.id.exitLayout);// 退出账号
        userInfoLayout = (RelativeLayout)settingView.findViewById(R.id.user_info_layout);
        userPhoto = (RoundImageView)settingView.findViewById(R.id.user_photo);
        nikeNameTxt = (TextView)settingView.findViewById(R.id.nick_name);
        accountTxt = (TextView)settingView.findViewById(R.id.user_account);
        userInfoLayout.setOnClickListener(setFragmentClickListener);
        // imageUploadLayout.setOnClickListener(setFragmentClickListener);
        // cacheClsLayout.setOnClickListener(setFragmentClickListener);
        aboutUsLayout.setOnClickListener(setFragmentClickListener);
        versionCheckLayout.setOnClickListener(setFragmentClickListener);
        helpLayout.setOnClickListener(setFragmentClickListener);
        exitLayout.setOnClickListener(setFragmentClickListener);
        
       // exitLayout.setVisibility(View.GONE);
        
        registerBoradcastReceiver();
        return settingView;
    }
    
    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        initLoginUserInfo();
    }
    
    private void initLoginUserInfo()
    {
        if (!StringUtils.isEmpty(PreferenceUtil.getUserId()))
        {
            UserInfo userInfo = UserInfoDB.getInstance().queryUserInfoById(PreferenceUtil.getUserId());
            if (!StringUtils.isEmpty(userInfo.getAvatar()))
            {
                ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getAvatar(),
                    userPhoto,
                    AppConfig.options(R.drawable.user_defult_photo));
                
            }
            
            nikeNameTxt.setText(userInfo.getNickname());
            accountTxt.setText("远行账号：" + PreferenceUtil.getAccount());
            exitLayout.setVisibility(View.VISIBLE);
        }else {
        	exitLayout.setVisibility(View.GONE);
		}
    }
    
    private void clearLoginUi()
    {
        userPhoto.setImageResource(R.drawable.user_defult_photo);
        nikeNameTxt.setText("");
        accountTxt.setText("远行账号：" + "");
        
    }
    
    public void registerBoradcastReceiver()
    {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.bcinfo.clearLogin");
        myIntentFilter.addAction("com.bcinfo.haveLogin");
        // 注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }
    
    private void unRegisterBoradcastReceiver()
    {
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
    
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals("com.bcinfo.clearLogin"))
            {
                clearLoginUi();
            }
            else if (action.equals("com.bcinfo.haveLogin"))
            {
                initLoginUserInfo();
            }
        }
    };
    
    private OnClickListener setFragmentClickListener = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
            // case R.id.setting_ImageUpload: // 图片上传条件
            // ++count;
            //
            // if (count == 3)
            // {
            // count = 0;
            //
            // }
            //
            // imgQualityTv.setText(qualityArray[count]);
            // break;
            // case R.id.setting_ClsCache:// 清除缓存
            // setDialog =
            // SettingCacheDialog.createDialog(SettingFragment.this.getActivity(),
            // R.layout.setting_cache_dialog);
            // LinearLayout cacheConfirm = (LinearLayout)setDialog.findViewById(R.id.setting_cache_confirm_layout);
            // LinearLayout cacheCancel = (LinearLayout)setDialog.findViewById(R.id.setting_cache_cancel_layout);
            // cacheConfirm.setOnClickListener(setFragmentClickListener);
            // cacheCancel.setOnClickListener(setFragmentClickListener);
            // setDialog.show();
            // break;
                case R.id.user_info_layout:
                    if (!AppInfo.getIsLogin())
                    {
                        goLoginActivity();
                        return;
                    }
                    Intent infoIntent = new Intent(getActivity(), SettingUserInfoActivity.class);
                    startActivity(infoIntent);
                    animationOpen();
                    break;
                case R.id.setting_AboutUs:// 关于我们
                    startActivity(new Intent(SettingFragment.this.getActivity(), SettingAboutUsActivity.class));
                    animationOpen();
                    break;
                case R.id.setting_CheckForUpdate:// 版本检查
                    startActivity(new Intent(SettingFragment.this.getActivity(), SettingUpdateVersionActivity.class));
                    animationOpen();
                    break;
                case R.id.setting_Help:// 帮助
                    startActivity(new Intent(SettingFragment.this.getActivity(), SettingHelpActivity.class));
                    animationOpen();
                    break;
                case R.id.exitLayout:// 退出账号
                    exitDialog =
                        ApplyExitDialog.createDialog(SettingFragment.this.getActivity(), R.layout.setting_exit_dialog);
                    
                    LinearLayout exitCancelLayout =
                        (LinearLayout)exitDialog.findViewById(R.id.setting_exitDialog_cancel_layout);
                    exitCancelLayout.setOnClickListener(setFragmentClickListener);
                    LinearLayout exitConfirmLayout =
                        (LinearLayout)exitDialog.findViewById(R.id.setting_exitDialog_confirm_layout);
                    exitConfirmLayout.setOnClickListener(setFragmentClickListener);
                    // 显示自定义Dialog
                    exitDialog.show();
                    break;
                case R.id.setting_cache_confirm_layout:// 清除缓存-确定
                    Toast.makeText(SettingFragment.this.getActivity(), "确定", 0).show();
                    setDialog.dismiss();
                    break;
                case R.id.setting_cache_cancel_layout:// 清除缓存-取消
                    Toast.makeText(SettingFragment.this.getActivity(), "取消", 0).show();
                    setDialog.dismiss();
                    break;
                case R.id.setting_exitDialog_cancel_layout:// 退出-取消
                    exitDialog.dismiss();
                    return;
                case R.id.setting_exitDialog_confirm_layout:// 退出-确定
                    clearLoginUi();
                    testLogOutUrl();
                    Intent intent = new Intent("com.bcinfo.clearLogin");
                    getActivity().sendBroadcast(intent);
                    exitDialog.dismiss();
                    exitLayout.setVisibility(View.GONE);
                    goLoginActivity();
                    break;
                default:
                    break;
            }
            
        }
    };
    
    /*
     * test 退出登录接口
     */
    private void testLogOutUrl()
    {
        HttpUtil.get(Urls.logoutUrl, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                System.out.println("退出登录状态码=" + statusCode);
                System.out.println("返回的参数=" + response.toString());
                PreferenceUtil.delUserInfo();
                UserInfoDB.getInstance().deleteAll();
                if ("00000".equals(response.optString("code")))
                {
                    if (setDialog != null && setDialog.isShowing())
                    {
                        exitDialog.dismiss();
                        System.out.println("create....");
                    }
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                
                super.onFailure(statusCode, headers, responseString, throwable);
                PreferenceUtil.delUserInfo();
                UserInfoDB.getInstance().deleteAll();
                System.out.println(statusCode);
                System.out.println(responseString);
            }
        });
    }
    
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        unRegisterBoradcastReceiver();
    }
    
}
