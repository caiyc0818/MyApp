package com.bcinfo.tripaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.activity.AddGoWithActivity;
import com.bcinfo.tripaway.activity.DiscoverAllLocationActivity;
import com.bcinfo.tripaway.activity.DiscoverAllThemesActivity;
import com.bcinfo.tripaway.activity.DiscoveryDefaultActivity;
import com.bcinfo.tripaway.activity.LoginActivity;
import com.bcinfo.tripaway.utils.StringUtils;

/**
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月3日 下午5:26:20
 */
public class BaseFragment extends Fragment
{
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        getScreenData();
    }
    
    /**
     * 定义一个显示自定义toast的方法
     * 
     * @param view 要显示的view
     * 
     */
    public Toast showToast(View view)
    {
        
        Toast toast = new Toast(this.getActivity());
        
        toast.setView(view);
        
        return toast;
        
    }
    
    protected OnClickListener onMfragmentClick = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                /*case R.id.discovery_showAllThemes:
                    Intent intentForAllThemes =
                        new Intent(BaseFragment.this.getActivity(), DiscoverAllThemesActivity.class);
                    startActivity(intentForAllThemes);
                    animationOpen();
                    break;
                case R.id.discovery_showAllLocations:
                    Intent intentForAllLocations =
                        new Intent(BaseFragment.this.getActivity(), DiscoverAllLocationActivity.class);
                    startActivity(intentForAllLocations);
                    animationOpen();
                    break;*/
                case R.id.add_go_with_button:
                    Intent intentForGo = new Intent(getActivity(), AddGoWithActivity.class);
                    startActivity(intentForGo);
                    animationOpen();
                    break;
                case R.id.searchEditLayout:
                    Intent intentForDiscovery = new Intent(getActivity(), DiscoveryDefaultActivity.class);
                    startActivity(intentForDiscovery);
                    animationOpen();
                    break;
                default:
                    break;
            }
            
        }
    };
    
    /**
     * 页面启动动画
     */
    protected void animationOpen()
    {
        getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }
    
    /**
     * 页面关闭动画
     */
    public void animationClose()
    {
        getActivity().overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
    }
    
    public void goLoginActivity()
    {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        animationOpen();
        return;
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	 public void onResume() {
	        super.onResume();

	        /**
	         * 百度统计
	         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
	         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
	         */
	        if(!StringUtils.isEmpty(statisticsTitle)&&!TripawayApplication.isDebug)
	        StatService.onPageStart(getActivity(), statisticsTitle);;
	    }

	    public void onPause() {
	        super.onPause();

	        /**
	         * 百度统计
	         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
	         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
	         */
	        if(!StringUtils.isEmpty(statisticsTitle)&&!TripawayApplication.isDebug)
	        StatService.onPageEnd(getActivity(), statisticsTitle);
	    }
	
	protected String statisticsTitle;
	
	 /**
     * 获取屏幕像素数据
     */
    public void getScreenData()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        screenDensity = dm.density;
    }
    
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
    
}
