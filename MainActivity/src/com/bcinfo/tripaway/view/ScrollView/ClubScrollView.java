package com.bcinfo.tripaway.view.ScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ClubScrollView extends ScrollView {

	private float xDistance;
	private float yDistance;
	private float xLast;
	private float yLast;

	public ClubScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ClubScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClubScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0.0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);

			if (xDistance > yDistance)
				return false;

			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
