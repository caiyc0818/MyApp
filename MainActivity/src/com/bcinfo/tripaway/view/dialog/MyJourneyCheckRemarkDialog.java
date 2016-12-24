package com.bcinfo.tripaway.view.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.MyJourneyRemarkActivity;
import com.bcinfo.tripaway.utils.PreferenceUtil;

/**
 * 我的旅程查看备注对话框
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月16日 下午5:22:56
 */
public class MyJourneyCheckRemarkDialog extends BaseActivity implements OnClickListener
{
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.my_journey_check_remark_dialog);
        TextView remarkTV = (TextView)findViewById(R.id.my_journey_remak_text);
        Button changeRemark = (Button)findViewById(R.id.my_journey_change_remark);
        Button cancelRemark = (Button)findViewById(R.id.my_journey_cancel_remark);
        remarkTV.setText(PreferenceUtil.getString("my_journey_remark"));
        changeRemark.setOnClickListener(this);
        cancelRemark.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.my_journey_change_remark:
                Intent intent = new Intent(this, MyJourneyRemarkActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.my_journey_cancel_remark:
                finish();
                break;
            default:
                break;
        }
    }
}
