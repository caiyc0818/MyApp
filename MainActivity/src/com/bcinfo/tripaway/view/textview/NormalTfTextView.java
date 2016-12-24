package com.bcinfo.tripaway.view.textview;

import com.bcinfo.tripaway.TripawayApplication;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class NormalTfTextView extends TextView {
	 
	public NormalTfTextView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    init();
	}
	 
	public NormalTfTextView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init();
	}
	 
	public NormalTfTextView(Context context) {
	    super(context);
	    init();
	}
	 
	private void init() {
	    if (!isInEditMode()) {
	        setTypeface(TripawayApplication.normalTf);
	    }
	}
	
}