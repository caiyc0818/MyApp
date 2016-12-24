package com.bcinfo.tripaway.view.textview;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BoldTfTextView extends TextView {
	 
	public BoldTfTextView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    init();
	}
	 
	public BoldTfTextView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init();
	}
	 
	public BoldTfTextView(Context context) {
	    super(context);
	    init();
	}
	 
	private void init() {
	    if (!isInEditMode()) {
	    	
	        setTypeface(TripawayApplication.boldTf);
	    }
	}
	
}