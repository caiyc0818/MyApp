package com.bcinfo.tripaway.view.dialog;

import com.bcinfo.tripaway.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

/**
 * 继承Dialog的自定义清除缓存对话框
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月26日14:52:45
 * 
 */
public class SettingCacheDialog extends Dialog
{

    /**
     * 定义构造方法1
     * 
     * @param context
     */
    public SettingCacheDialog(Context context)
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
    public SettingCacheDialog(Context context, int theme)
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
    public static SettingCacheDialog createDialog(Context context, int layoutId)
    {

        SettingCacheDialog customDialog1 = new SettingCacheDialog(context, R.style.style_setting_dialog);

        // 设置自定义对话框的布局内容
        customDialog1.setContentView(layoutId);

        // 设置对话框居中显示
        customDialog1.getWindow().setGravity(Gravity.CENTER);

        return customDialog1;

    }

}
