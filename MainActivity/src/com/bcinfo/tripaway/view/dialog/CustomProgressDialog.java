package com.bcinfo.tripaway.view.dialog;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DensityUtil;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 继承Dialog的 等待性质的对话框子类
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月9日17:52:45
 * 
 */
public class CustomProgressDialog extends Dialog
{
    private Context context = null;

    private static CustomProgressDialog customDialog = null;

    /**
     * 定义构造方法1
     * 
     * @param context
     */
    public CustomProgressDialog(Context context)
    {
        super(context);
        this.context = context;

    }

    /**
     * 定义 构造方法2
     * 
     * @param context
     * @param theme
     *            表示该对话框所应用的样式
     * 
     */
    public CustomProgressDialog(Context context, int theme)
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
    public static CustomProgressDialog createDialog(Context context, int layoutId, int positionX, int positionY)
    {
        // 创建自定义对话框实例
        customDialog = new CustomProgressDialog(context, R.style.loginWaitDialogStyle);

        customDialog.setContentView(layoutId);
        customDialog.getWindow().setGravity(Gravity.TOP|Gravity.LEFT);
        // 设置对话框所处的具体位置
       
        customDialog.getWindow().getAttributes().x = positionX;

        customDialog.getWindow().getAttributes().y = positionY;

        return customDialog;
    }

    /*
     * 
     * 当对话框的focus焦点发生变化时，会回调 onWindowFocusChanged()方法
     * 
     * public void onWindowFocusChanged(boolean hasFocus){
     * 
     * if (customDialog == null){ return; }
     * 
     * ImageView imageView = (ImageView)
     * customProgressDialog.findViewById(R.id.loadingImageView);
     * AnimationDrawable animationDrawable = (AnimationDrawable)
     * imageView.getBackground(); animationDrawable.start(); }
     */

    /**
     * 定义 设置标题的方法
     * 
     * @param strTitle
     * @return
     */
    public CustomProgressDialog setTitile(String strTitle)
    {

        return customDialog;
    }

    /**
     * 定义 设置对话框显示内容的方法
     * 
     * @param strMessage
     * @return 对话框实例
     */
    public CustomProgressDialog setMessage(int strMessage)
    {

        TextView tvMsg = (TextView) customDialog.findViewById(R.id.showingText);

        if (tvMsg != null)
        {

            // 设置对话框界面上文本框中的内容

            tvMsg.setText(strMessage);

        }

        return customDialog;

    }

}
