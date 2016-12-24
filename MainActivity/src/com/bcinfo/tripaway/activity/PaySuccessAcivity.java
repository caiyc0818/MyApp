package com.bcinfo.tripaway.activity;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.view.date.MyDateFormat;

/**
 * @function
 * @author WeiCH
 * @version 1.0, 2015年07月25日 下午5:26:30
 */
public class PaySuccessAcivity extends BaseActivity implements OnClickListener
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success_activity);
        initpayView();
    }
    
    private void initpayView()
    {
        TextView PaySecondTitleText = (TextView)findViewById(R.id.pay_second_title_text);
        LinearLayout LayoutBackButton = (LinearLayout)findViewById(R.id.layout_back_button);
        LayoutBackButton.setOnClickListener(this);
        ((View)LayoutBackButton.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
        ((View)LayoutBackButton.getParent()).getBackground().setAlpha(255);
        TextView PadStateText = (TextView)findViewById(R.id.pad_state_text);
        TextView PaySuccessMoney = (TextView)findViewById(R.id.pay_success_money);
        TextView SetPadSuccessPaytime = (TextView)findViewById(R.id.set_pad_success_paytime);
        TextView SetPadSuccessNo = (TextView)findViewById(R.id.set_pad_success_no);
        TextView SetPadSuccessName = (TextView)findViewById(R.id.set_pad_success_name);
        TextView SetPadSuccessStarttime = (TextView)findViewById(R.id.set_pad_success_starttime);
        TextView SetPadSuccessNum = (TextView)findViewById(R.id.set_pad_success_num);
        TextView SetPadSuccessMessage = (TextView)findViewById(R.id.set_pad_success_message);
        String code = getIntent().getStringExtra("result");
        if (code.equals("9000"))
        {
            PaySecondTitleText.setText("支付成功");
            PadStateText.setText("支付成功");
            PaySuccessMoney.setText(getIntent().getStringExtra("total"));
        }
        else
        {
            PaySecondTitleText.setText("支付失败");
            PadStateText.setText("支付失败");
            PaySuccessMoney.setText("支付失败");
        }
        SetPadSuccessPaytime.setText(MyDateFormat.ConverToString(new Date(System.currentTimeMillis()),
            "yyyy-MM-dd HH:mm:ss"));
        SetPadSuccessNo.setText(getIntent().getStringExtra("orderNo"));
        SetPadSuccessName.setText(getIntent().getStringExtra("name"));
//        String BeginDate =
//            MyDateFormat.ConverToString(MyDateFormat.ConverToDate(getIntent().getStringExtra("beginDate"), "yyyyMMdd"),
//                "yyyy/MM/dd");
//        String EndDate =
//            MyDateFormat.ConverToString(MyDateFormat.ConverToDate(getIntent().getStringExtra("endDate"), "yyyyMMdd"),
//                "yyyy/MM/dd");
        
        String BeginDate =DateUtil.formateDateToTime(
        		getIntent().getStringExtra("beginDate"));
            String EndDate =DateUtil.formateDateToTime(
            		getIntent().getStringExtra("endDate"));
        
        
        SetPadSuccessStarttime.setText(BeginDate + "至" + EndDate);
        SetPadSuccessNum.setText(getIntent().getStringExtra("num"));
        SetPadSuccessMessage.setText(getIntent().getStringExtra("leaveEdt"));
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.layout_back_button:
                activityAnimationClose();
                finish();
                break;
            default:
                break;
        }
    }
}
