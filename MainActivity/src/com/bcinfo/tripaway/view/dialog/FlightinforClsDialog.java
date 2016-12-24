package com.bcinfo.tripaway.view.dialog;

import com.bcinfo.tripaway.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

/**
 * 自定义 清除航班信息对话框
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月31日 17:40:33
 * 
 */
public class FlightinforClsDialog extends Dialog
{

    public FlightinforClsDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);

    }

    public FlightinforClsDialog(Context context, int theme)
    {
        super(context, theme);

    }

    public FlightinforClsDialog(Context context)
    {
        super(context);

    }

    /**
     * 新建一个对话框
     * 
     * @param context
     * @param layoutId
     * @return
     */
    public static FlightinforClsDialog createDialog(Context context, int layoutId)
    {
        FlightinforClsDialog flightInforClsDialog = new FlightinforClsDialog(context, R.style.style_setting_dialog);
        flightInforClsDialog.setContentView(layoutId);
        flightInforClsDialog.getWindow().setGravity(Gravity.CENTER);
        return flightInforClsDialog;
    }
}
