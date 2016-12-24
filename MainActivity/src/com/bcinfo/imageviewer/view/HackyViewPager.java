package com.bcinfo.imageviewer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 *  ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月24日 下午8:45:28
 */
public class HackyViewPager extends ViewPager
{
    private static final String TAG = "HackyViewPager";

    public HackyViewPager(Context context)
    {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        try
        {
            return super.onInterceptTouchEvent(ev);
        }
        catch (IllegalArgumentException e)
        {
            // 不理会
            Log.e(TAG, "hacky viewpager error1");
            return false;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            // 不理会
            Log.e(TAG, "hacky viewpager error2");
            return false;
        }
    }
}
