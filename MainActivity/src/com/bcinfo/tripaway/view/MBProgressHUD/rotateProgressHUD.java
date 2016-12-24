
package com.bcinfo.tripaway.view.MBProgressHUD;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
/**
 * @file MBProgressHUD.java
 * @create July 15, 2015 15:22:27 PM
 * @author WeiChangHang
 * @description This is a MBProgressHUD
 */
public class rotateProgressHUD extends LinearLayout
{


    private ImageView rotateLoadingIcon;
    
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    private LinearLayout mContainer;
    private boolean isplay=false;
    
    public rotateProgressHUD(Context context)
    {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public rotateProgressHUD(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context)
    {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
         mContainer = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.trip_product_rotate, null);
        addView(mContainer, lp);

        rotateLoadingIcon = (ImageView) mContainer.findViewById(R.id.rotate_loading_icon);
        refreshingAnimation = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        mContainer.setVisibility(View.GONE);
    }

public void startAnimation(){
    mContainer.setVisibility(View.VISIBLE);
    rotateLoadingIcon.startAnimation(refreshingAnimation);
    isplay=true;
}
public void endAnimation()
{
    mContainer.setVisibility(View.GONE);
    rotateLoadingIcon.clearAnimation();
    isplay=false;
}
public boolean getstate()
{
    return isplay;
}
}
