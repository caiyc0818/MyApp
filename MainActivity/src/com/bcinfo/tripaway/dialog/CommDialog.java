package com.bcinfo.tripaway.dialog;




import com.bcinfo.tripaway.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 继承Dialog的自定义手机找回密码result 对话框
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月17日   15:45:22
 * 
 */
public class CommDialog extends Dialog
{

    /**
     * 定义构造方法1
     * 
     * @param context
     */
    public CommDialog(Context context)
    {
        super(context);

    }

    /**
     * 定义 构造方法2
     * 
     * @param context
     * @param theme
     *            表示该对话框所应用的样式
     * 
     */
    public CommDialog(Context context, int theme)
    {
        super(context, theme);

    }

    /**
     * 定义创建对话框实例的方法
     * 
     * @param context
     * @param layoutId
     *            要显示的layout的xml文件的id
     * @param positionX
     *            显示对话框的具体x方向的位置
     * @param positionY
     *            显示对话框的具体y方向的位置
     * @return 自定义对话框实例
     */
    public static   CommDialog createDialog(final Context context, View view)
    {

        final CommDialog customDialog1 = new CommDialog(context,
                R.style.style_setting_dialog);

        // 设置自定义对话框的布局内容
        customDialog1.setContentView(view);


        // 设置对话框居中显示
        customDialog1.getWindow().setGravity(Gravity.CENTER);

        return customDialog1;
    }
}
