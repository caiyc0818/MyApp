/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.bcinfo.tripaway.view.refreshandload;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;

public class XListViewHeader extends LinearLayout
{
    private LinearLayout mContainer;
    
    private ImageView mArrowImageView;
    
    // private ProgressBar mProgressBar;
    private ImageView loadingIcon;
    
    private TextView mHintTextView;
    
    private int mState = STATE_NORMAL;
    
    private Animation mRotateUpAnim;
    
    private Animation mRotateDownAnim;
    
    private final int ROTATE_ANIM_DURATION = 180;
    
    public final static int STATE_NORMAL = 0;
    
    public final static int STATE_READY = 1;
    
    public final static int STATE_REFRESHING = 2;
    
    public final static int STATE_SUCCEED = 3;
    
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;
    
    private ImageView stateImg;
    
    public XListViewHeader(Context context)
    {
        super(context);
        initView(context);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context)
    {
        // 初始情况，设置下拉刷新view高度为0
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0);
        mContainer = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        
        mArrowImageView = (ImageView)findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView)findViewById(R.id.xlistview_header_hint_textview);
        // mProgressBar = (ProgressBar)findViewById(R.id.xlistview_header_progressbar);
        loadingIcon = (ImageView)findViewById(R.id.loading_icon);
        stateImg = (ImageView)findViewById(R.id.state_iv);
        mRotateUpAnim =
            new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim =
            new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
        
        refreshingAnimation = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
    }
    
    public void setState(int state)
    {
        if (state == mState)
            return;
        
        if (state == STATE_REFRESHING)
        { // 显示进度
            stateImg.setVisibility(View.GONE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            loadingIcon.startAnimation(refreshingAnimation);
            loadingIcon.setVisibility(View.VISIBLE);
        }
        else
        { // 显示箭头图片
            stateImg.setVisibility(View.GONE);
            mArrowImageView.setVisibility(View.VISIBLE);
            loadingIcon.clearAnimation();
            loadingIcon.setVisibility(View.INVISIBLE);
        }
        
        switch (state)
        {
            case STATE_NORMAL:
                if (mState == STATE_READY)
                {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING)
                {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText(R.string.xlistview_header_hint_normal);
                break;
            case STATE_READY:
                if (mState != STATE_READY)
                {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(R.string.xlistview_header_hint_ready);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.xlistview_header_hint_loading);
                break;
            case STATE_SUCCEED:
                stateImg.setBackgroundResource(R.drawable.refresh_succeed);
                stateImg.setVisibility(View.VISIBLE);
                mArrowImageView.setVisibility(View.GONE);
                loadingIcon.clearAnimation();
                mArrowImageView.clearAnimation();
                loadingIcon.setVisibility(View.GONE);
                mHintTextView.setText(R.string.xlistview_header_hint_success);
                break;
            default:
        }
        
        mState = state;
    }
    
    public void setVisiableHeight(int height)
    {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }
    
    public int getVisiableHeight()
    {
        return mContainer.getHeight();
    }
    
}
