package com.bcinfo.tripaway.view.dialog;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.bean.ScheduleEvent;
import com.bcinfo.tripaway.db.ScheduleEventDatabase;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.alarm.Alarm;
import com.bcinfo.tripaway.view.alarm.Alarms;
/**
 * 闹钟提醒对话框
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月23日 下午7:06:03
 */
public class AlarmDialog extends BaseActivity implements OnClickListener
{
    private Button cancelButton;
    private Button delayButton;
    private Alarm mAlarm = null;
    private TextView alarmContent;
    private ScheduleEventDatabase mDatabase;

    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.alarm_dialog);
        cancelButton = (Button) findViewById(R.id.alarm_cancel_button);
        delayButton = (Button) findViewById(R.id.alarm_delay_button);
        alarmContent = (TextView) findViewById(R.id.alarm_content);
        cancelButton.setOnClickListener(this);
        delayButton.setOnClickListener(this);
        initData();
    }

    private void initData()
    {
        mDatabase = ScheduleEventDatabase.getInstance(this);
        mAlarm = getIntent().getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);
        LogUtil.i("AlarmDialog", "initData", "mAlarm=" + mAlarm);
        if (mAlarm == null)
        {
            return;
        }
        ArrayList<ScheduleEvent> eventList = mDatabase.queryEventByCreateTime(mAlarm.label);
        if (eventList.size() > 0)
        {
            ScheduleEvent event = eventList.get(0);
            alarmContent.setText(event.getContent());
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.alarm_cancel_button:
                stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
                finish();
                break;
            case R.id.alarm_delay_button:
                stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
                if (mAlarm != null)
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(mAlarm.time);
                    calendar.add(Calendar.MINUTE, 10);
                    mAlarm.year = calendar.get(Calendar.YEAR);
                    mAlarm.month = calendar.get(Calendar.MONTH);
                    mAlarm.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    mAlarm.hour = calendar.get(Calendar.HOUR_OF_DAY);
                    mAlarm.minutes = calendar.get(Calendar.MINUTE);
                    mAlarm.time = calendar.getTimeInMillis();
                    Alarms.updateAlarm(this, mAlarm);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
