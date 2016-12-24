package com.bcinfo.tripaway.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ServResrouce;

/**
 * 产品-申请预订-选择汽车
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月2日 14:18:11
 */
public class TrafficToolsPickedDialog extends AlertDialog implements android.view.View.OnClickListener
{
    private Context mContext;
    
    private ServResrouce mComfireInfo;
    
    private TextView product_car_picked_start;
    
    private TextView product_car_picked_end;
    
    private ChoseTimeListener mChoseTimeListener;
    
    public TrafficToolsPickedDialog(Context context, ServResrouce info, ChoseTimeListener listener)
    {
        super(context);
        mContext = context;
        mComfireInfo = info;
        mChoseTimeListener = listener;
    }
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_layout_car_picked);
        float dialogHeight = mContext.getResources().getDimension(R.dimen.dialog_product_car_picked_window_height);
        this.getWindow().getAttributes().height = (int)dialogHeight;// 设置对话框样式的高度为指定的高度 480dp
        initView();
    }
    
    private void initView()
    {
        TextView picked_car_Name_tv = (TextView)findViewById(R.id.picked_car_Name_tv);
        TextView picked_car_introduce_tv = (TextView)findViewById(R.id.picked_car_introduce_tv);
        TextView layout_car_picked_tip_container = (TextView)findViewById(R.id.layout_car_picked_tip_container);
        product_car_picked_start = (TextView)findViewById(R.id.product_car_picked_start);
        product_car_picked_end = (TextView)findViewById(R.id.product_car_picked_end);
        TextView product_car_picked_confirm_tv = (TextView)findViewById(R.id.product_car_picked_confirm_tv);
        TextView product_car_picked_cancel_tv = (TextView)findViewById(R.id.product_car_picked_cancel_tv);
        picked_car_Name_tv.setText(mComfireInfo.getServName());
        picked_car_introduce_tv.setText(mComfireInfo.getServDesc());
        if (mComfireInfo.getServType().equals("car"))
        {
            layout_car_picked_tip_container.setText("选择使用该汽车的日期");
        }
        else if (mComfireInfo.getServType().equals("boat"))
        {
            layout_car_picked_tip_container.setText("选择使用该游轮的日期");
        }
        else if (mComfireInfo.getServType().equals("hotel"))
        {
            layout_car_picked_tip_container.setText("选择使用该酒店的日期");
        }
        else if (mComfireInfo.getServType().equals("house"))
        {
            layout_car_picked_tip_container.setText("选择使用该民宿的日期");
        }
        product_car_picked_confirm_tv.setOnClickListener(this);
        product_car_picked_cancel_tv.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.product_car_picked_confirm_tv:
                mComfireInfo.setChecked(true);
                String startTime = product_car_picked_start.getText().toString();
                String endTime = product_car_picked_end.getText().toString();
                mChoseTimeListener.onResult(startTime, endTime);
                cancel();
                break;
            case R.id.product_car_picked_cancel_tv:
                cancel();
                break;
            default:
                break;
        }
    }
    
    public interface ChoseTimeListener
    {
        public void onResult(String startTime, String endTime);
    }
}
