package com.bcinfo.tripaway.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月3日 下午5:26:30
 */
public class BaseActivity extends FragmentActivity
{
    /**
     * 定义标题栏上的返回箭头图标
     * 
     */
    protected LinearLayout arrowBack;
    
    protected RelativeLayout second_title;
    
    /**
     * 定义标题栏上的"登录"字样的textview
     * 
     */
    protected TextView loginTextView;
    
    protected LinearLayout discoverLayout;
    
    /**
     * 定义标题栏上的"注册"字样的textview
     * 
     */
    protected TextView regTextView;
    
    /**
     * 定义 标题栏上的 显示 "找回密码" "重置密码"的textview
     * 
     */
    protected TextView findOrResetLabel;
    
    /**
     * 二级标题栏上的 返回 图标
     * 
     * 发现 标题栏的 "返回"图标
     * 
     */
    protected ImageView navBack;
    
    /**
     * 返回 图标右边的文本框
     */
    protected TextView discoverTitle;
    
    /**
     * 精选首页-标题栏右边的放大镜图标(新增)
     */
    protected ImageView pickedTitleIcon;
    
    /**
     * 搜索-标题栏右边的 放大镜 图标
     */
    protected ImageView discoverThemeIcon;
    
    // protected TextView discoverCancelTv;
    /*
     * 屏幕高度
     */
    public static int screenHeight = 0;
    
    /*
     * 屏幕宽度
     */
    public static int screenWidth = 0;
    
    /*
     * 屏幕密度
     */
    public static float screenDensity = 0;
    
    /*
     * 状态栏高度
     */
    public static int statusBarHeight = 0;
    
    public ArrayList<ImageInfo> productTitleImgList = new ArrayList<ImageInfo>();
    
    public boolean isFromLoad = false;
    
    /*
     * 等待加载
     */
    private View loginLoading;

	private AnimationDrawable loadingAnimation;
	
	public void startLoading(){
		 loginLoading = (View)findViewById(R.id.login_loading);
		  loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
         loadingAnimation.start();
	}
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        getScreenData();
        isFromLoad = getIntent().getBooleanExtra("formLoading", false);
    }
    
    /**
     * 设置一级界面标题
     * 
     * @author caihelin
     * @param position 传递过来的position数值
     * 
     */
    protected void setMainTitle(int position)
    {
        TextView mainTitleTv = (TextView)this.findViewById(R.id.main_title_text);
        String[] functions = getResources().getStringArray(R.array.sidebar_function);
        ImageView addEventButton = (ImageView)findViewById(R.id.add_event_button);
        
        // ImageView writeRemarkButton = (ImageView) findViewById(R.id.write_remark_button);
        // 精选页面标题右边 放大镜图标
        ImageView pickedTitleIcon = (ImageView)findViewById(R.id.discover_icon);
        RadioGroup community_rg = (RadioGroup)findViewById(R.id.community_rg);
        mainTitleTv.setText(functions[position]);
        mainTitleTv.setVisibility(View.VISIBLE);
        addEventButton.setVisibility(View.GONE);
        
        // writeRemarkButton.setVisibility(View.GONE);
        pickedTitleIcon.setVisibility(View.GONE);
        community_rg.setVisibility(View.GONE);
        switch (position)
        {
            case 0: // 0表示点击了 精选模块
                
                pickedTitleIcon.setVisibility(View.VISIBLE);
                pickedTitleIcon.setOnClickListener(mOnClickListener);
                break;
            case 1: // 1表示点击了 目的地
                break;
            // case 3: // 3表示点击了 我的行程
            // writeRemarkButton.setVisibility(View.VISIBLE);
            // break;
            // case 4: // 4表示点击了 我的产品
            // break;
            // case 5: // 5表示点击了 日程
            // addEventButton.setVisibility(View.VISIBLE);
            // break;
            case 2:// 2表示点击了 社区
                mainTitleTv.setVisibility(View.GONE);
                community_rg.setVisibility(View.VISIBLE);
                break;
            case 3: // 3表示点击了 设置
                break;
            default:
                break;
        }
    }
    
    /**
     * 设置二级界面标题
     */
    protected void setSecondTitle(String title)
    {
    	
        TextView titleTV = (TextView)findViewById(R.id.second_title_text);
        LinearLayout backButton = (LinearLayout)findViewById(R.id.layout_back_button);
        TextView eventCommitButton = (TextView)findViewById(R.id.event_commit_button);
        titleTV.setText(title);
        backButton.setOnClickListener(mOnClickListener);
        if (title.equals("添加事件"))
        {
            eventCommitButton.setVisibility(View.VISIBLE);
        }
        else
        {
            eventCommitButton.setVisibility(View.INVISIBLE);
        }
        ((View)titleTV.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
        ((View)titleTV.getParent()).getBackground().setAlpha(255);
    }
    
    protected void setSecondMoreTitle(String title)
    {
        LinearLayout backLayout = (LinearLayout)findViewById(R.id.layout_back_button);
        LinearLayout moreLayout = (LinearLayout)findViewById(R.id.layout_more_button);
        TextView userNameTv = (TextView)findViewById(R.id.userNameIv);
    }
    
    /**
     * 设置 登录-注册 二级界面标题栏
     * 
     * @param isLoginRegShow 是否显示"登录" "注册"字样
     * 
     * 
     * @param isResetFindShow 是否显示"重置密码"，"找回密码"字样
     * 
     */
    protected void initFirstTitle(boolean isLoginShow, boolean isResetFindShow, boolean isRegShow)
    {
        initView();
        if (isLoginShow && !isResetFindShow && isRegShow)
        {
            loginTextView.setVisibility(View.VISIBLE);
            regTextView.setVisibility(View.VISIBLE);
            findOrResetLabel.setVisibility(View.GONE);
            loginTextView.setText(R.string.login);
            regTextView.setText(R.string.register);
        }
        else if (!isLoginShow && isResetFindShow && !isRegShow)
        {
            loginTextView.setVisibility(View.GONE);
            regTextView.setVisibility(View.VISIBLE);
            findOrResetLabel.setVisibility(View.VISIBLE);
        }
        else if (!isLoginShow && !isResetFindShow && isRegShow)
        {
            loginTextView.setVisibility(View.VISIBLE);
            regTextView.setVisibility(View.INVISIBLE);
            findOrResetLabel.setVisibility(View.GONE);
            loginTextView.setText(R.string.register);
        }
        // 设置登录-注册标题栏上 返回 图标的点击事件监听器
        arrowBack.setOnClickListener(mOnClickListener);
    }
    
    /**
     * 定义获取标题栏上控件的方法
     */
    protected void initView()
    {
        loginTextView = (TextView)this.findViewById(R.id.login);
        regTextView = (TextView)this.findViewById(R.id.register);
        findOrResetLabel = (TextView)this.findViewById(R.id.findOrResetLabel);
        arrowBack = (LinearLayout)this.findViewById(R.id.layout_back_button);
    }
    
    OnClickListener mOnClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.layout_back_button:
                    if (isFromLoad)
                    {
                        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    activityAnimationClose();
                    break;
                case R.id.register:
                    Intent intentForRegister = new Intent(v.getContext(), RegisterActivity.class);
                    startActivity(intentForRegister);
                    activityAnimationOpen();
                    break;
                case R.id.discovery_back_button:
                    finish();
                    activityAnimationClose();
                    break;
                case R.id.discovery_discover_button:
                    Intent intentForDiscovery1 = new Intent(v.getContext(), DiscoveryDefaultActivity.class);
                    startActivity(intentForDiscovery1);
                    activityAnimationOpen();
                    break;
                case R.id.discover_icon: // 精选页面标题栏放大镜图标
                    Intent intentForDiscovery2 = new Intent(v.getContext(), DiscoveryDefaultActivity.class);
                    startActivity(intentForDiscovery2);
                    activityAnimationOpen();
                    break;
                case R.id.picked_back_button: // 精选-目的地产品结果
                    finish();
                    activityAnimationClose();
                    break;
                // case R.id.discovery_container: // 搜索框整体布局
                // Intent intentForDefaultDiscovery = new Intent(v.getContext(), DiscoveryDefaultActivity.class);
                // startActivity(intentForDefaultDiscovery);
                // activityAnimationOpen();
                
                case R.id.add_event_button:// 添加日程事件
                    Intent intent = new Intent(v.getContext(), AddScheduleEventActivity.class);
                    startActivity(intent);
                    activityAnimationOpen();
                    break;
                case R.id.detail_back_icon:
                    finish();
                    activityAnimationClose();
                    break;
                case R.id.blog_navBack:
                    finish();
                    activityAnimationClose();
                    break;
                default:
                    break;
            }
        }
    };
    
    /**
     * 获取屏幕像素数据
     */
    public void getScreenData()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        screenDensity = dm.density;
        statusBarHeight = getStatusBarHeight();
    }
    
    /**
     * 获取手机状态栏高度
     */
    public int getStatusBarHeight()
    {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try
        {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
    
    /**
     * 界面启动的动画
     */
    public void activityAnimationOpen()
    {
        overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }
    
    protected void activityAnimationOpenUpDown()
    {
        overridePendingTransition(R.anim.activity_new1, R.anim.activity_out1);
    }
    
    /**
     * 界面关闭的动画
     */
    protected void activityAnimationClose()
    {
        overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
    }
    
    protected void activityAnimationCloseUpDown()
    {
        overridePendingTransition(R.anim.activity_back1, R.anim.activity_finish1);
    }
    
    /**
     * 界面启动的动画
     */
    public void activityAnimationOpen(Activity activity)
    {
        activity.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }
    
    /**
     * 界面关闭的动画
     */
    public void activityAnimationClose(Activity activity)
    {
        activity.overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
    }
    
    /**
     * 手机按键被按下时的回调方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (isFromLoad)
            {
                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
            }
//            我的主页 出行单小红点消失
            setResult(8188);
            finish();
            activityAnimationClose();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public View getPartView(int layoutId)
    {
        // 将xml布局文件生成view对象通过LayoutInflater
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 将view对象挂载到那个父元素上，这里没有就为null
        return inflater.inflate(layoutId, null);
    }
    
    public void goLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        activityAnimationOpen();
        return;
    }
    
    // 隐藏软键盘
    public void hideInputManager(Context ct)
    {
        try
        {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity)ct).getCurrentFocus()
                .getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
        catch (Exception e)
        {
            Log.e("hideInputManager", "hideInputManager Catch error,skip it!", e);
        }
    }
    
    // 隐藏软键盘
    public void hideInputManager1(View mcontext)
    {
        InputMethodManager inputManager = (InputMethodManager) mcontext.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputManager == null || !inputManager.isActive())
            inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mcontext.getWindowToken(), 0);// 隐藏软键盘
    }

  
    
    
    
    public void backspace(EditText editText)
    {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }
    
    public void queryPicList(String productId)
    {
        HttpUtil.get(Urls.product_pic_url + productId, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastUtil.showToast(BaseActivity.this, "error=" + throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(BaseActivity.this, "error=" + throwable.getMessage());
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("queryPicList", "获取产品图片接口", response.toString());
                if (response.optString("code").equals("00000"))
                {
                    JSONArray dataArray = response.optJSONArray("data");
                    if (dataArray != null && dataArray.length() > 0)
                    {
                        for (int i = 0; i < dataArray.length(); i++)
                        {
                            JSONObject picJson = dataArray.optJSONObject(i);
                            ImageInfo imageInfo=new ImageInfo();
                            if (!StringUtils.isEmpty(picJson.optString("url")))
                            {
                            	imageInfo.setUrl(picJson.optString("url"));
                                //productTitleImgList.add(picJson.optString("url"));
                            }
                            /*else
                            {
                                productTitleImgList.add("");
                            }*/
                            if (!StringUtils.isEmpty(picJson.optString("desc"))){
                            	imageInfo.setDesc(picJson.optString("desc"));
                            	//productTitleImgList.add(picJson.optString("desc"));
                            }/*else
                            {
                                productTitleImgList.add("");
                            }*/
                            productTitleImgList.add(imageInfo);
                        }
                    }
                }
                else
                {
//                    ToastUtil.showToast(BaseActivity.this, "errorCode=" + response.optString("code"));
                }
            }
            
        });
    }
    
    protected void onResume() {
        super.onResume();

        /**
         * 百度统计
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        if(!StringUtils.isEmpty(statisticsTitle)&&!TripawayApplication.isDebug)
        StatService.onPageStart(this, statisticsTitle);;
    }

    protected void onPause() {
        super.onPause();

        /**
         * 百度统计
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        if(!StringUtils.isEmpty(statisticsTitle)&&!TripawayApplication.isDebug)
        StatService.onPageEnd(this, statisticsTitle);
    }
    
    protected String statisticsTitle;
}
