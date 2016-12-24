package com.bcinfo.tripaway.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ServResrouce;

public class DelectChoiseTrafficDialog extends AlertDialog implements android.view.View.OnClickListener
{
    private ServResrouce mComfireInfo;
    
    private ServResrouce mFlightInfo;
    
    private int type = 0;
    
    private OperationListener mOperationListener;
    
    public DelectChoiseTrafficDialog(Context context, OperationListener listener)
    {
        super(context);
        mOperationListener = listener;
    }
    
    public void setComfireInfo(ServResrouce info)
    {
        type = 0;
        mComfireInfo = info;
    }
    
    public void setFlightInfo(ServResrouce info)
    {
        type = 1;
        mFlightInfo = info;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delect_choise_traffic_dialog);
        TextView delect_info_tv = (TextView)findViewById(R.id.delect_info_tv);
        TextView confirm = (TextView)findViewById(R.id.confirm);
        TextView cancel = (TextView)findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        String traffic = "";
        if (type == 0)
        {
            if (mComfireInfo.getServType().equals("car"))
            {
                traffic = "汽车";
            }
            else if (mComfireInfo.getServType().equals("boat"))
            {
                traffic = "游轮";
            }
            else if (mComfireInfo.getServType().equals("hotel"))
            {
                traffic = "酒店";
            }
            else if (mComfireInfo.getServType().equals("house"))
            {
                traffic = "民宿";
            }
        }
        else
        {
            traffic = "航班";
        }
        String show = "清除已选择的" + traffic + "信息？";
        delect_info_tv.setText(show);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.confirm:
                if (type == 0)
                {
                    mOperationListener.onDelectComfireInfo(mComfireInfo);
                }
                else
                {
                    mOperationListener.onDelectFlightInfo(mFlightInfo);
                }
                cancel();
                break;
            case R.id.cancel:
                cancel();
                break;
            default:
                break;
        }
    }
    
    public interface OperationListener
    {
        public void onDelectComfireInfo(ServResrouce info);
        
        public void onDelectFlightInfo(ServResrouce info);
    }
}
