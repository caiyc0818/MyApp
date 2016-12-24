package com.bcinfo.tripaway.view.dialog;

import com.bcinfo.tripaway.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

/**
 * 自定义退出对话框
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月26日 14:30:22
 * 
 */
public class ApplyExitDialog extends Dialog
{

    public ApplyExitDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);

    }

    public ApplyExitDialog(Context context, int theme)
    {
        super(context, theme);

    }

    public ApplyExitDialog(Context context)
    {
        super(context);

    }

    /**
     * 创建对话框
     * 
     * @param context
     * @param layoutId
     * @return
     */
    public static ApplyExitDialog createDialog(Context context, int layoutId)
    {

        ApplyExitDialog applyExitDialog = new ApplyExitDialog(context, R.style.style_setting_dialog);

        applyExitDialog.setContentView(layoutId);
        // 居中显示
        applyExitDialog.getWindow().setGravity(Gravity.CENTER);
//        applyExitDialog.getWindow().getAttributes().y = -110;

        return applyExitDialog;

    }

}
