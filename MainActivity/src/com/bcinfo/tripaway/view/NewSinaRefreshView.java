package com.bcinfo.tripaway.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

/**
 * Created by Administrator on 2016/12/25.
 */
public class NewSinaRefreshView extends FrameLayout implements IHeaderView {

    private ImageView refreshArrow;
    private ImageView loadingView;
    private TextView refreshTextView;
    private boolean isFirst=true;

    public NewSinaRefreshView(Context context) {
        this(context, null);
    }

    public NewSinaRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewSinaRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), com.lcodecore.tkrefreshlayout.R.layout.view_sinaheader, null);
        refreshArrow = (ImageView) rootView.findViewById(com.lcodecore.tkrefreshlayout.R.id.iv_arrow);
        refreshTextView = (TextView) rootView.findViewById(com.lcodecore.tkrefreshlayout.R.id.tv);
        loadingView = (ImageView) rootView.findViewById(com.lcodecore.tkrefreshlayout.R.id.iv_loading);
        addView(rootView);
    }

    public void setArrowResource(@DrawableRes int resId) {
        refreshArrow.setImageResource(resId);
    }

    public void setTextColor(@ColorInt int color) {
        refreshTextView.setTextColor(color);
    }

    public void setPullDownStr(String pullDownStr1) {
        pullDownStr = pullDownStr1;
    }

    public void setReleaseRefreshStr(String releaseRefreshStr1) {
        releaseRefreshStr = releaseRefreshStr1;
    }

    public void setRefreshingStr(String refreshingStr1) {
        refreshingStr = refreshingStr1;
    }

    private String pullDownStr = "下拉刷新";
    private String releaseRefreshStr = "释放刷新";
    private String refreshingStr = "正在刷新";

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        isFirst = true;
       if(refreshTextView.getVisibility()==GONE) refreshTextView.setVisibility(VISIBLE);
       if(refreshArrow.getVisibility()==GONE) refreshArrow.setVisibility(VISIBLE);
        if (fraction < 1f) refreshTextView.setText(pullDownStr);
        if (fraction > 1f) refreshTextView.setText(releaseRefreshStr);
        refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
       if(isFirst){
           if (fraction < 1f) {
               refreshTextView.setText(pullDownStr);
               if (refreshArrow.getVisibility() == GONE) {
                   refreshArrow.setVisibility(VISIBLE);
                   loadingView.setVisibility(GONE);
               }
               refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
           }
       }

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText(refreshingStr);
        refreshArrow.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        ((AnimationDrawable) loadingView.getDrawable()).start();
    }

    @Override
    public void onFinish(final OnAnimEndListener listener) {
        isFirst=false;
        refreshTextView.setText("刷新成功");
        ((AnimationDrawable) loadingView.getDrawable()).stop();
        loadingView.setVisibility(GONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                refreshTextView.setVisibility(GONE);
                refreshArrow.setVisibility(GONE);
                listener.onAnimEnd();
            }
        }, 5000);

    }

    @Override
    public void reset() {

    }

//    @Override
//    public void reset() {
//        refreshArrow.setVisibility(VISIBLE);
//        loadingView.setVisibility(GONE);
//        refreshTextView.setText(pullDownStr);
//    }


}
