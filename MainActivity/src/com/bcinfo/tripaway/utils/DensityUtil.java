package com.bcinfo.tripaway.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
/**
 * dp和px相互转换的工具类
 * @function
 * @author caihelin
 * @version 1.0 2015年1月20日  16:16:22
 */
public class DensityUtil {
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) { 
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
    
	/**
	 * 设置View的高度
	 * 
	 * @param v
	 * @param height
	 */
	public static void setViewHeight2(View v, int height) {
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) v
				.getLayoutParams(); // 取控件mGrid当前的布局参数
		linearParams.height = height;// 当控件的高强制设成height
		v.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件myGrid
	}
	
	/**
	 * 获取屏幕高度和宽带
	 * 
	 * @param mContext
	 * @return int[高，宽]
	 */
	public static int[] getScreen(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		// 窗口的宽度
		int screenWidth = dm.widthPixels;

		// 窗口高度
		int screenHeight = dm.heightPixels;
		int screen[] = { screenHeight, screenWidth };
		return screen;

	}
}
