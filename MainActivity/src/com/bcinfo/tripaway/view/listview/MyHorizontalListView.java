package com.bcinfo.tripaway.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.HorizontalScrollView;

public class MyHorizontalListView extends HorizontalScrollView {

	 public MyHorizontalListView(Context context, AttributeSet attrs, int defStyle)
	    {
	        super(context, attrs, defStyle);
	        // TODO Auto-generated constructor stub
	    }

	    public MyHorizontalListView(Context context, AttributeSet attrs)
	    {
	        super(context, attrs);
	        // TODO Auto-generated constructor stub
	    }

	    public MyHorizontalListView(Context context)
	    {
	        super(context);
	        // TODO Auto-generated constructor stub
	    }
	    
	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	// TODO Auto-generated method stub
	    	 int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	         super.onMeasure(widthMeasureSpec, expandSpec);
	    }
	    
	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent ev) {
	    	// TODO Auto-generated method stub
	    	return false;
	    }
}
