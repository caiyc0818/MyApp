package com.bcinfo.tripaway.view.date;

import com.bcinfo.tripaway.utils.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class DrumPickerScrollView extends ScrollView
{
    private final static String TAG = DrumPickerScrollView.class.getSimpleName();
    interface OnPositionChangedListener
    {
        public void onPositionChenged(int pos);
    }
    private int mItemHeight = -1;
    private IDrumPickerScroller mDrumPickerScroller = null;
    private boolean isFling = false;
    private OnPositionChangedListener mListener = null;

    public DrumPickerScrollView(Context context, int itemHeight)
    {
        super(context);
        mItemHeight = itemHeight;
        mDrumPickerScroller = ScrollerFactory.create(this, context);
    }

    public void setOnPositionChangedListener(OnPositionChangedListener listener)
    {
        mListener = listener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy)
    {
        if (mDrumPickerScroller.getFinalY() == y)
        {
            isFling = false;
            adjust(y);
        }
        super.onScrollChanged(x, y, oldx, oldy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        boolean b = super.onTouchEvent(ev);
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                isFling = false;
                break;
            case MotionEvent.ACTION_UP:
                if (!isFling)
                {
                    //Log.d(TAG, "onUp and adjust:" + getScrollY());
                    adjust(getScrollY());
                }
                break;
        }
        return b;
    }

    @Override
    public void fling(int velocityY)
    {
        super.fling(velocityY);
        isFling = true;
    }

    private void adjust(int y)
    {
        int position = y / mItemHeight;
        int d = y % mItemHeight;
        if (d > mItemHeight / 2)
        {
            position++;
        }
        int pos = mItemHeight * position;
        int move = pos - y;
        //Log.d(TAG, "move:" + move);
        smoothScrollBy(0, move);
        mPos = position;
        if (mListener != null)
        {
            mListener.onPositionChenged(position);
        }
    }
    boolean isScroll = false;
    int mScrollPos = 0;
    int mPos = 0;

    public void setPosition(int pos)
    {
        setPosition(pos, true);
    }

    public void setPosition(int pos, boolean isAnimation)
    {
        int position = mItemHeight * pos;
        mScrollPos = position;
        mPos = pos;
        if (!isAnimation)
        {
            LogUtil.i(TAG, "setPosition", "mScrollPos=" + mScrollPos);
            scrollTo(0, mScrollPos);
            if (mListener != null)
            {
                mListener.onPositionChenged(mScrollPos / mItemHeight);
            }
        }
        else
        {
            isScroll = true;
        }
    }

    public int getPosition()
    {
        return mPos;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (isScroll)
        {
            isScroll = false;
            smoothScrollTo(0, mScrollPos);
            if (mListener != null)
            {
                mListener.onPositionChenged(mScrollPos / mItemHeight);
            }
            //mScrollPos = 0;
        }
        super.onDraw(canvas);
    }
}
