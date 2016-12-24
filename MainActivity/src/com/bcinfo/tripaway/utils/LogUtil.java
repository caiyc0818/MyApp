package com.bcinfo.tripaway.utils;

import android.util.Log;
/**
 * Log助手
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月16日 上午11:32:22
 */
public class LogUtil
{
    public static void i(String TAG,String methodName,String msg)
    {
        Log.i(TAG+"-->", methodName+"-->"+msg);
    }
    
    public static void d(String TAG,String methodName,String msg)
    {
        Log.d(TAG+"-->", methodName+"-->"+msg);
    }
    
    public static void w(String TAG,String methodName,String msg)
    {
        Log.w(TAG+"-->", methodName+"-->"+msg);
    }
    
    public static void e(String TAG,String methodName,String msg)
    {
        Log.e(TAG+"-->", methodName+"-->"+msg);
    }
    
    public static void v(String TAG,String methodName,String msg)
    {
        Log.v(TAG+"-->", methodName+"-->"+msg);
    }
}

