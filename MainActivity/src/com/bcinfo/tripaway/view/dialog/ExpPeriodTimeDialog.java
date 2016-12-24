package com.bcinfo.tripaway.view.dialog;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.adapter.MoreExperiodTimeAdapter;
import com.bcinfo.tripaway.bean.AvailableTime;

/**
 * @author hanweipeng
 * @date 2015-7-10 下午7:00:04
 */
public class ExpPeriodTimeDialog extends BaseActivity implements OnClickListener
{
    private LayoutInflater inflater;
    
    private LayoutParams lp;
    
    private ListView timeLst;
    
    private ImageView closeBtn;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.experiod_list_dialog);
       // timeLst = (ListView)findViewById(R.id.time_lst);
        closeBtn = (ImageView)findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(this);
        ArrayList<AvailableTime> timeList = getIntent().getParcelableArrayListExtra("timeList");
        MoreExperiodTimeAdapter adapter = new MoreExperiodTimeAdapter(this, timeList);
        timeLst.setAdapter(adapter);
    }
    
    // public ExpPeriodTimeDialog(Context context, List<AvailableTime> timeList)
    // {
    // super.on
    // // TODO Auto-generated constructor stub
    // inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    // View layout = inflater.inflate(R.layout.experiod_list_dialog, null);
    // setContentView(R.layout.experiod_list_dialog);
    //
    // // 设置window属性
    // lp = getWindow().getAttributes();
    // lp.dimAmount = 0; // 去背景遮盖
    // lp.alpha = 0.8f;
    // getWindow().setAttributes(lp);
    // getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    // }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.close_btn:
                finish();
                activityAnimationClose();
                break;
            
            default:
                break;
        }
    }
    
}
