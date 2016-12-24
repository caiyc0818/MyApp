package com.bcinfo.tripaway.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.PreferenceUtil;

/**
 * 我的旅程备注
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月13日 上午9:32:47
 */
public class MyJourneyRemarkActivity extends BaseActivity implements OnClickListener
{
    private EditText remarkET;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.my_journey_remark_activity);
        initTitle();
    }
    
    /**
     * 设置二级界面标题
     */
    private void initTitle()
    {
        TextView titleTV = (TextView)findViewById(R.id.title_text);
        LinearLayout backButton = (LinearLayout)findViewById(R.id.layout_back_button);
        TextView eventCommitButton = (TextView)findViewById(R.id.remark_commit_button);
        remarkET = (EditText)findViewById(R.id.my_journey_remark_text);
        remarkET.setText(PreferenceUtil.getString("my_journey_remark"));
        titleTV.setText("行程备注");
        backButton.setOnClickListener(mOnClickListener);
        eventCommitButton.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
        
            case R.id.remark_commit_button:
                
                String remarkString = remarkET.getText().toString();
                PreferenceUtil.setString("my_journey_remark", remarkString);
                break;
            default:
                break;
        }
    }
}
