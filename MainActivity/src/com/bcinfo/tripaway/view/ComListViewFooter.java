package com.bcinfo.tripaway.view;

import com.bcinfo.tripaway.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ListView's footer
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年11月29日 上午10:05:33
 */
public class ComListViewFooter extends LinearLayout
{
    public final static int STATE_FINISHED = 1;
    public final static int STATE_LOADING = 2;
    private Context mContext;
    private View mProgressBar;
    private TextView mHintView;

    public ComListViewFooter(Context context)
    {
        super(context);
        initView(context);
    }

    public ComListViewFooter(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context)
    {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.com_listview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        mProgressBar = moreView.findViewById(R.id.listview_footer_progressbar);
        mHintView = (TextView) moreView.findViewById(R.id.listview_footer_hint_textview);
    }

    public void setState(int state)
    {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        if (state == STATE_FINISHED)
        {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText("加载完毕");
        }
        else
        {
            mProgressBar.setVisibility(View.VISIBLE);
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText("正在加载");
        }
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide()
    {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }
}
