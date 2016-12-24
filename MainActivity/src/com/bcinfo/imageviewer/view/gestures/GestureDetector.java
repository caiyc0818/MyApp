package com.bcinfo.imageviewer.view.gestures;

import android.view.MotionEvent;

import com.bcinfo.imageviewer.listener.OnGestureListener;

public interface GestureDetector
{
    public boolean onTouchEvent(MotionEvent ev);

    public boolean isScaling();

    public void setOnGestureListener(OnGestureListener listener);
}
