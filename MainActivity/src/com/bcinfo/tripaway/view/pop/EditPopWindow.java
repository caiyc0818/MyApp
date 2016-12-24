package com.bcinfo.tripaway.view.pop;

import com.bcinfo.tripaway.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 编辑弹出框
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年4月25日 上午10:33:15
 */
public class EditPopWindow extends PopupWindow implements OnClickListener
{
    private static final String TAG = "EditPopWindow";
    private Context mContext;
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    private OperationListener mOperationListener;

    @SuppressWarnings("deprecation")
    public EditPopWindow(Context context, int item1Logo, String item1Fun, int item2Logo, String item2Fun)
    {
        this.mContext = context;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.edit_pop_window, null);
        setContentView(view);
        LinearLayout layout_item1 = (LinearLayout) view.findViewById(R.id.layout_item1);
        LinearLayout layout_item2 = (LinearLayout) view.findViewById(R.id.layout_item2);
        ImageView item1_logo = (ImageView) view.findViewById(R.id.item1_logo);
        TextView item1_function = (TextView) view.findViewById(R.id.item1_function);
        ImageView item2_logo = (ImageView) view.findViewById(R.id.item2_logo);
        TextView item2_function = (TextView) view.findViewById(R.id.item2_function);
        if(item1Logo==0)
        {
            item1_logo.setVisibility(View.GONE);
        }
        if(item2Logo==0)
        {
            item2_logo.setVisibility(View.GONE);
        }
        item1_logo.setImageResource(item1Logo);
        item2_logo.setImageResource(item2Logo);
        item1_function.setText(item1Fun);
        item2_function.setText(item2Fun);
        layout_item1.setOnClickListener(this);
        layout_item2.setOnClickListener(this);
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
            case R.id.layout_item1:
                if (mOperationListener != null)
                {
                    mOperationListener.clickItem1();
                }
                dismiss();
                break;
            case R.id.layout_item2:
                if (mOperationListener != null)
                {
                    mOperationListener.clickItem2();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setOperationListener(OperationListener listener)
    {
        mOperationListener = listener;
    }
    public interface OperationListener
    {
        public void clickItem1();

        public void clickItem2();
    }
}
