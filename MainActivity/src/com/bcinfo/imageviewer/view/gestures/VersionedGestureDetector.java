package com.bcinfo.imageviewer.view.gestures;

import android.content.Context;
import android.os.Build;

import com.bcinfo.imageviewer.listener.OnGestureListener;

public final class VersionedGestureDetector
{
    public static GestureDetector newInstance(Context context, OnGestureListener listener)
    {
        final int sdkVersion = Build.VERSION.SDK_INT;
        GestureDetector detector;
        if (sdkVersion < Build.VERSION_CODES.ECLAIR)
        {
            detector = new CupcakeGestureDetector(context);
        }
        else if (sdkVersion < Build.VERSION_CODES.FROYO)
        {
            detector = new EclairGestureDetector(context);
        }
        else
        {
            detector = new FroyoGestureDetector(context);
        }
        detector.setOnGestureListener(listener);
        return detector;
    }
}