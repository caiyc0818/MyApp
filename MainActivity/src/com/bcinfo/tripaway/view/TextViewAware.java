package com.bcinfo.tripaway.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class TextViewAware implements ImageAware{


	private int w;
	private int h;
	public TextViewAware(int w,int h) {
		// TODO Auto-generated constructor stub
		this.w=w;
		this.h=h;
	}
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return w;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return h;
	}

	@Override
	public ViewScaleType getScaleType() {
		// TODO Auto-generated method stub
		return ViewScaleType.CROP;
	}

	@Override
	public View getWrappedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCollected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return  hashCode();
	}

	@Override
	public boolean setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setImageBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		return false;
	}

}
