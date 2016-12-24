package com.bcinfo.tripaway.view.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager {

	private OnSingleTouchListener onSingleTouchListener;
	PointF downP = new PointF();

	PointF curP = new PointF();

	public ChildViewPager(Context context) {
		super(context);
	}

	public ChildViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		curP.x = arg0.getX();
		curP.y = arg0.getY();
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = arg0.getX();
			downP.y = arg0.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			// 在up时判断是否按下和松手的坐标为一个点
			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
			if (downP.x == curP.x && downP.y == curP.y) {
//				onSingleTouch();
				getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		}
		return super.onTouchEvent(arg0);
	}

	
	/**
	 * 　　 * 单击 　　
	 */
	public void onSingleTouch() {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch();
		}
	}

	public interface OnSingleTouchListener {
		public void onSingleTouch();
	}

	public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
			this.onSingleTouchListener = onSingleTouchListener;
	}
}
