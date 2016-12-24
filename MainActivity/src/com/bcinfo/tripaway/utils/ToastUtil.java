package com.bcinfo.tripaway.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;

/**
 * @author hanweipeng
 * @date 2015-6-16 上午10:13:30
 */
public class ToastUtil
{
    public static void showErrorToast(Context context, String str)
    {
        LinearLayout errorLayout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.error_toast, null);
        
        TextView hintInfoText = (TextView)errorLayout.findViewById(R.id.hintInfo);
        
        hintInfoText.setText(str);
        
        Toast toast = new Toast(context);
        toast.setView(errorLayout);
        
        toast.setDuration(Toast.LENGTH_LONG); // 设置toast显示的时长
        
        toast.setGravity(Gravity.TOP,
            (int)context.getResources().getDimension(R.dimen.login_toast_positionX),
            (int)context.getResources().getDimension(R.dimen.login_toast_positionY)); // 50
        // 190
        
        toast.show();
    }
    
    public static void showToast(Context context, String str)
    {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
    public static void showToast1(Context context, String str)
    {
    	Toast  toast= new Toast(context);
    	toast.makeText(context, str, 100).show();
    	toast.cancel();
    }
}
