package com.bcinfo.tripaway.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextView1 extends TextView{

	public TextView1(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public TextView1(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public TextView1(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
	
	 private int getAvailableWidth()
	    {
	        return getWidth() - getPaddingLeft() - getPaddingRight();
	    }
	    public  boolean isOverFlowed()
	    {
	        Paint paint = getPaint();
	        int i=2*getAvailableWidth();
	        float width = paint.measureText(getText().toString());
	        if (width > 2*getAvailableWidth()) return true;
	        return false;
	    }

}
