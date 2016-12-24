package com.bcinfo.tripaway.view.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DensityUtil;

/**
 * 订单操作弹出框
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月25日 上午10:33:15
 */
public class ComplainPopWindow extends PopupWindow implements OnClickListener
{
    private static final String TAG = "OrderPopWindow.java";
    
    private Context mContext;
    
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    
    private OperationListener mOperationListener;
    
    private String orderId;
    
    @SuppressWarnings("deprecation")
    public ComplainPopWindow(Context context, OperationListener listener,String orderId)
    {
        this.mContext = context;
        this.orderId = orderId;
        mOperationListener = listener;
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        // 设置弹窗的宽度和高度
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(mContext).inflate(R.layout.complain_pop_view, null);
        setContentView(view);
        TextView function = (TextView)view.findViewById(R.id.my_complain);
        function.setTag(orderId);
        function.setOnClickListener(this);
    }
    
    /**
     * 显示弹窗列表界面
     */
    public void show(final View view)
    {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);
        int x = mLocation[0]-5;
        int y = mLocation[1] +DensityUtil.dip2px(mContext, 54) ;
        // 显示弹窗的位置
        showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.my_complain:
            	String orderId = v.getTag().toString();
                mOperationListener.complain(orderId);
                dismiss();
                break;
            default:
                break;
        }
    }
    
    public interface OperationListener
    {
        public void complain(String orderId);
        
    }
}
