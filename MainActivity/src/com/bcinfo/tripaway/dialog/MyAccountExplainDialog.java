package com.bcinfo.tripaway.dialog;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.dialog.MyAccountExplainDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

/**
 * 我的账户-问题解释对话框
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月23日 18:01:12
 */
public class MyAccountExplainDialog extends Dialog
{

    public MyAccountExplainDialog(Context context, int theme)
    {
        super(context, theme);

    }

    public MyAccountExplainDialog(Context context)
    {
        super(context);

    }

    public static MyAccountExplainDialog createDialog(Activity mActivity, int layoutId)
    {
    	MyAccountExplainDialog myAccountExplainDialog = new MyAccountExplainDialog(mActivity,
                R.style.Affirm_Dialog_undisappear_Style);
        myAccountExplainDialog.setContentView(layoutId);
        
        myAccountExplainDialog.getWindow().setGravity(Gravity.BOTTOM);// 对话框显示在底部
        // 使得对话框的宽度和整个手机屏幕的宽度一样大
        myAccountExplainDialog.getWindow().getAttributes().width = mActivity.getWindow().getWindowManager().getDefaultDisplay()
                .getWidth();
        return myAccountExplainDialog;

    }

}
