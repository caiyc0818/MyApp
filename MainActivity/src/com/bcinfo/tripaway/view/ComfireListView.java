package com.bcinfo.tripaway.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
/**
 * Function：重写listview的onMeasure()，计算item中的高度
 * 注意item的XML只允许有线性布局
 * @author： tcyangpeng
 * @Time：2015年4月1日下午4:41:29
 * @Version： 1.0
 */
public class ComfireListView extends ListView
{
    public ComfireListView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ComfireListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ComfireListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
 
   
}
