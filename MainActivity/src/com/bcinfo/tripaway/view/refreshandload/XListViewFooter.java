/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.bcinfo.tripaway.view.refreshandload;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;

public class XListViewFooter extends LinearLayout
{
    public final static int STATE_NORMAL = 0;
    
    public final static int STATE_READY = 1;
    
    public final static int STATE_LOADING = 2;
    
    public final static int STATE_NOMORE = 3;
    
    private Context mContext;
    
    private View mContentView;
    
    // private View mProgressBar;
    private ImageView loadingIcon;
    
    private TextView mHintView;
    
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;
    
    private int endState = -1;
    
    public XListViewFooter(Context context)
    {
        super(context);
        initView(context);
    }
    
    public XListViewFooter(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }
    
    public void setState(int state)
    {
        mHintView.setVisibility(View.INVISIBLE);
        loadingIcon.setVisibility(View.INVISIBLE);
        mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY)
        {
            if (endState != STATE_NOMORE)
            {
                show();
            }
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_ready);
        }
        else if (state == STATE_LOADING)
        {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_header_hint_loading);
            loadingIcon.startAnimation(refreshingAnimation);
            loadingIcon.setVisibility(View.VISIBLE);
        }
        else if (state == STATE_NORMAL)
        {
            if (endState != STATE_NOMORE)
            {
                hide();
            }
        }
        else
        {
            endState = STATE_NOMORE;
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_no_more);
        }
    }
    
    public void setBottomMargin(int height)
    {
        if (height < 0)
            return;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }
    
    public int getBottomMargin()
    {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
        return lp.bottomMargin;
    }
    
    /**
     * normal status
     */
    public void normal()
    {
        mHintView.setVisibility(View.VISIBLE);
        loadingIcon.clearAnimation();
        loadingIcon.setVisibility(View.GONE);
    }
    
    /**
     * loading status
     */
    public void loading()
    {
        mHintView.setVisibility(View.GONE);
        loadingIcon.setVisibility(View.VISIBLE);
    }
    
    /**
     * hide footer when disable pull load more
     */
    public void hide()
    {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }
    
    /**
     * show footer
     */
    public void show()
    {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }
    
    private void initView(Context context)
    {
        mContext = context;
        LinearLayout moreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        // mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
        loadingIcon = (ImageView)moreView.findViewById(R.id.loading_icon);
        mHintView = (TextView)moreView.findViewById(R.id.xlistview_footer_hint_textview);
        
        refreshingAnimation = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
    }
    
}
