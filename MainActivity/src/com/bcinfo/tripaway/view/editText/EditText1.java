package com.bcinfo.tripaway.view.editText;

import com.bcinfo.tripaway.view.text.LinkArrowKeyMovementMethod;

import android.content.Context;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

public class EditText1 extends EditText {

	 public EditText1(Context context, AttributeSet attrs, int defStyle)
	    {
	        super(context, attrs, defStyle);
	        // TODO Auto-generated constructor stub
	    }

	    public EditText1(Context context, AttributeSet attrs)
	    {
	        super(context, attrs);
	        // TODO Auto-generated constructor stub
	    }

	    public EditText1(Context context)
	    {
	        super(context);
	        // TODO Auto-generated constructor stub
	    }
	    
	   @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		   int action = event.getActionMasked();
		   if(action==MotionEvent.ACTION_UP){
			   super.onTouchEvent(event);
			   MovementMethod mMethod=getMovementMethod();
			   if(mMethod instanceof LinkArrowKeyMovementMethod){
				   ((LinkArrowKeyMovementMethod)mMethod).onClick(this, getText(), event);}
			   return true;
		   }
		return super.onTouchEvent(event);
	}
}
