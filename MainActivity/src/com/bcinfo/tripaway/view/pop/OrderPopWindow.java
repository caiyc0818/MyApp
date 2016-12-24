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

/**
 * 订单操作弹出框
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月25日 上午10:33:15
 */
public class OrderPopWindow extends PopupWindow implements OnClickListener
{
    private static final String TAG = "OrderPopWindow";
    
    private Context mContext;
    
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    
    private OperationListener mOperationListener;
    
    @SuppressWarnings("deprecation")
    public OrderPopWindow(Context context, String functionStr, OperationListener listener)
    {
        this.mContext = context;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_popwindow, null);
        setContentView(view);
        LinearLayout layout_contact_author = (LinearLayout)view.findViewById(R.id.layout_contact_author);
        LinearLayout layout_refund = (LinearLayout)view.findViewById(R.id.layout_refund);
        TextView function = (TextView)view.findViewById(R.id.function);
        function.setText(functionStr);
        layout_contact_author.setOnClickListener(this);
        layout_refund.setTag(functionStr);
        layout_refund.setOnClickListener(this);
    }
    
    /**
     * 显示弹窗列表界面
     */
    public void show(final View view)
    {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);
        int popWidth = mContext.getResources().getDimensionPixelSize(R.dimen.order_pop_width);
        int x = mLocation[0] + view.getWidth() * 2 / 3 - popWidth;
        int y = mLocation[1] + view.getHeight() - 10;
        // 显示弹窗的位置
        showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.layout_contact_author:
                mOperationListener.contact();
                dismiss();
                break;
            case R.id.layout_refund:
                String str = (String)v.getTag();
                mOperationListener.refund(str);
                dismiss();
                break;
            default:
                break;
        }
    }
    
    public interface OperationListener
    {
        public void contact();
        
        public void refund(String string);
    }
}
