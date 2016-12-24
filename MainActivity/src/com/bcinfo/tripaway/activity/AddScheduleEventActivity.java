package com.bcinfo.tripaway.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ScheduleEvent;
import com.bcinfo.tripaway.db.ScheduleEventDatabase;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.alarm.Alarm;
import com.bcinfo.tripaway.view.alarm.Alarms;
import com.bcinfo.tripaway.view.date.DateTimePicker;
import com.bcinfo.tripaway.view.date.DayPicker;
import com.bcinfo.tripaway.view.date.DateTimePicker.OnCalendarChangedListener;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加日程事件
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月8日 下午5:27:47
 */
public class AddScheduleEventActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "AddScheduleEventActivity";
    /**
     * 提交
     */
    private TextView commitButton;
    /**
     * 描述
     */
    private EditText eventContent;
    /**
     * 开始时间
     */
    private TextView eventDateBegin;
    /**
     * 结束时间
     */
    private TextView eventDateEnd;
    /**
     * 提醒时间
     */
    private TextView eventNotifyTime;
    /**
     * 事件颜色
     */
    private ImageView eventColor;
    /**
     * 备注
     */
    private EditText eventRemark;
    private int mBeginYear = 0;
    private int mBeginMonth = 0;
    private int mBeginDay = 0;
    private int mEndYear = 0;
    private int mEndMonth = 0;
    private int mEndDay = 0;
    private ScheduleEventDatabase mDatabase;
    /**
     * 是否为更新旧事件
     */
    private boolean isUpdate = false;
    /**
     * 编辑事件原事件
     */
    private ScheduleEvent scheduleEvent;
    /**
     * 闹钟日期时间选择对话框
     */
    private AlertDialog dateTimeDialog;
    /**
     * 闹钟日期时间
     */
    private DateTimePicker mDateTimePicker;
    
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.add_schedule_event_activity);
        setSecondTitle("添加事件");
        commitButton = (TextView) findViewById(R.id.event_commit_button);
        eventContent = (EditText) findViewById(R.id.event_des);
        eventDateBegin = (TextView) findViewById(R.id.event_date_begin);
        eventDateEnd = (TextView) findViewById(R.id.event_date_end);
        eventNotifyTime = (TextView) findViewById(R.id.event_notify_button);
        eventColor = (ImageView) findViewById(R.id.set_event_color);
        RelativeLayout layoutEventColor = (RelativeLayout) findViewById(R.id.layout_set_event_color);
        RelativeLayout layoutEventNotify = (RelativeLayout) findViewById(R.id.layout_event_notify);
        eventRemark = (EditText) findViewById(R.id.event_remark);
        LinearLayout backButton = (LinearLayout) findViewById(R.id.layout_back_button);
        backButton.setOnClickListener(mOnClickListener);
        commitButton.setOnClickListener(this);
        eventDateBegin.setOnClickListener(this);
        eventDateEnd.setOnClickListener(this);
        layoutEventColor.setOnClickListener(this);
        layoutEventNotify.setOnClickListener(this);
        eventColor.setTag("green");
        setDateTime();
        initData();
        mDatabase = ScheduleEventDatabase.getInstance(this);
    }

    private void initData()
    {
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        if (isUpdate)
        {
            scheduleEvent = (ScheduleEvent) getIntent().getSerializableExtra("scheduleEvent");
            eventContent.setText(scheduleEvent.getContent());
            eventDateBegin.setText(scheduleEvent.getBeginDate());
            eventDateEnd.setText(scheduleEvent.getEndDate());
            eventNotifyTime.setText(scheduleEvent.getNotifyTime());
            eventColor.setTag(scheduleEvent.getColor());
            eventRemark.setText(scheduleEvent.getRemark());
            if (scheduleEvent.getColor().equals("green"))
            {
                eventColor.setBackgroundColor(getResources().getColor(R.color.event_green));
            }
            else if (scheduleEvent.getColor().equals("gray"))
            {
                eventColor.setBackgroundColor(getResources().getColor(R.color.event_gray));
            }
            else if (scheduleEvent.getColor().equals("red"))
            {
                eventColor.setBackgroundColor(getResources().getColor(R.color.event_red));
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                Date beginDate = format.parse(scheduleEvent.getBeginDate());
                Date endDate = format.parse(scheduleEvent.getEndDate());
                Calendar beginCalendar = Calendar.getInstance();
                Calendar endCalendar = Calendar.getInstance();
                beginCalendar.setTime(beginDate);
                endCalendar.setTime(endDate);
                mBeginYear = beginCalendar.get(Calendar.YEAR);
                mBeginMonth = beginCalendar.get(Calendar.MONTH);
                mBeginDay = beginCalendar.get(Calendar.DAY_OF_MONTH);
                mEndYear = endCalendar.get(Calendar.YEAR);
                mEndMonth = endCalendar.get(Calendar.MONTH);
                mEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);
            }
            catch (ParseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            
            case R.id.event_commit_button:
                commitEvent();
                break;
            case R.id.event_date_begin:
                DatePickerDialog beginDatePickerDialog = new DatePickerDialog(this, mBeginDateSetListener, mBeginYear,
                        mBeginMonth, mBeginDay);
                beginDatePickerDialog.updateDate(mBeginYear, mBeginMonth, mBeginDay);
                beginDatePickerDialog.show();
                break;
            case R.id.event_date_end:
                DatePickerDialog endDatePickerDialog = new DatePickerDialog(this, mEndDateSetListener, mEndYear,
                        mEndMonth, mEndDay);
                endDatePickerDialog.updateDate(mEndYear, mEndMonth, mEndDay);
                endDatePickerDialog.show();
                break;
            case R.id.layout_set_event_color:
                if (eventColor.getTag().toString().equals("green"))
                {
                    eventColor.setTag("gray");
                    eventColor.setBackgroundColor(getResources().getColor(R.color.event_gray));
                }
                else if (eventColor.getTag().toString().equals("gray"))
                {
                    eventColor.setTag("red");
                    eventColor.setBackgroundColor(getResources().getColor(R.color.event_red));
                }
                else if (eventColor.getTag().toString().equals("red"))
                {
                    eventColor.setTag("green");
                    eventColor.setBackgroundColor(getResources().getColor(R.color.event_green));
                }
                break;
            case R.id.layout_event_notify:
                dateTimePickerDialog();
                break;
            case R.id.ok_button:
                Calendar calendar = mDateTimePicker.getCalendar();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);
                String notify=year+"年"+(month+1)+"月"+day+"日"+hour+"时"+minute+"分";
                eventNotifyTime.setText(notify);
                eventNotifyTime.setTextColor(getResources().getColor(R.color.dark_gray));
                dateTimeDialog.cancel();
                break;
            default:
                break;
        }
    }

    /**
     * 设置日期
     */
    private void setDateTime()
    {
        final Calendar c = Calendar.getInstance();
        mBeginYear = c.get(Calendar.YEAR);
        mBeginMonth = c.get(Calendar.MONTH);
        mBeginDay = c.get(Calendar.DAY_OF_MONTH);
        mEndYear = c.get(Calendar.YEAR);
        mEndMonth = c.get(Calendar.MONTH);
        mEndDay = c.get(Calendar.DAY_OF_MONTH);
        updateBeginDateDisplay();
        updateEndDateDisplay();
    }
    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mBeginDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            // TODO Auto-generated method stub
            mBeginYear = year;
            mBeginMonth = monthOfYear;
            mBeginDay = dayOfMonth;
            updateBeginDateDisplay();
        }
    };
    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mEndDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            // TODO Auto-generated method stub
            mEndYear = year;
            mEndMonth = monthOfYear;
            mEndDay = dayOfMonth;
            updateEndDateDisplay();
        }
    };

    /**
     * 更新开始日期显示
     */
    private void updateBeginDateDisplay()
    {
        StringBuilder beginBuilder = new StringBuilder();
        beginBuilder.append(mBeginYear);
        beginBuilder.append("-");
        beginBuilder.append((mBeginMonth + 1) < 10 ? "0" + (mBeginMonth + 1) : (mBeginMonth + 1));
        beginBuilder.append("-");
        beginBuilder.append((mBeginDay < 10) ? "0" + mBeginDay : mBeginDay);
        eventDateBegin.setText(beginBuilder);
    }

    /**
     * 更新结束日期显示
     */
    private void updateEndDateDisplay()
    {
        StringBuilder endBuilder = new StringBuilder();
        endBuilder.append(mEndYear);
        endBuilder.append("-");
        endBuilder.append((mEndMonth + 1) < 10 ? "0" + (mEndMonth + 1) : (mEndMonth + 1));
        endBuilder.append("-");
        endBuilder.append((mEndDay < 10) ? "0" + mEndDay : mEndDay);
        eventDateEnd.setText(endBuilder);
    }

    private void commitEvent()
    {
        Calendar today = Calendar.getInstance();
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        today.set(Calendar.MILLISECOND, 0);
        today.set(todayYear, todayMonth, todayDay, 0, 0, 0);
        begin.set(mBeginYear, mBeginMonth, mBeginDay, 0, 0, 0);
        end.set(mEndYear, mEndMonth, mEndDay, 0, 0, 0);
        if (begin.getTimeInMillis() < today.getTimeInMillis())
        {
            Toast.makeText(this, "开始时间不得小于今天", Toast.LENGTH_SHORT).show();
            return;
        }
        if (begin.getTimeInMillis() > end.getTimeInMillis())
        {
            Toast.makeText(this, "结束时间不得小于开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        String beginDate = eventDateBegin.getText().toString();
        String endDate = eventDateEnd.getText().toString();
        String content = eventContent.getText().toString();
        String color = eventColor.getTag().toString();
        String notifyTime = eventNotifyTime.getText().toString();
        String remark = eventRemark.getText().toString();
        String createTime = String.valueOf(System.currentTimeMillis());
        //        Calendar calendar = eventNotifyButton.getCalendar();
        //        if (!notifyTime.equals("无提醒"))
        //        {
        //            if (calendar.getTimeInMillis() < today.getTimeInMillis())
        //            {
        //                Toast.makeText(this, "您设置的提醒时间不正确", Toast.LENGTH_SHORT).show();
        //                return;
        //            }
        //        }
        ArrayList<ScheduleEvent> eventList = new ArrayList<ScheduleEvent>();
        ArrayList<String> dateList = DateUtil.getDaysBetweenDate(begin, end);
        if (dateList != null && dateList.size() > 0)
        {
            if (isUpdate)
            {
                for (int i = 0; i < dateList.size(); i++)
                {
                    ScheduleEvent event = new ScheduleEvent();
                    event.setId(dateList.get(i) + scheduleEvent.getCreateTime());
                    event.setCreateTime(scheduleEvent.getCreateTime());
                    event.setDate(dateList.get(i));
                    event.setBeginDate(beginDate);
                    event.setEndDate(endDate);
                    event.setColor(color);
                    event.setContent(content);
                    event.setIsFinish(scheduleEvent.getIsFinish());
                    event.setRemark(remark);
                    event.setNotifyTime(notifyTime);
                    eventList.add(event);
                    LogUtil.i(TAG, "commitEvent", i + "event.getId=" + event.getId());
                }
                mDatabase.delectEventByCreateTime(scheduleEvent.getCreateTime());
                mDatabase.insertEventList(eventList);
            }
            else
            {
                for (int i = 0; i < dateList.size(); i++)
                {
                    ScheduleEvent event = new ScheduleEvent();
                    event.setId(dateList.get(i) + createTime);
                    event.setCreateTime(createTime);
                    event.setDate(dateList.get(i));
                    event.setBeginDate(beginDate);
                    event.setEndDate(endDate);
                    event.setColor(color);
                    event.setContent(content);
                    event.setIsFinish("false");
                    event.setRemark(remark);
                    event.setNotifyTime(notifyTime);
                    eventList.add(event);
                }
                mDatabase.insertEventList(eventList);
            }
        }
        if (!notifyTime.equals("无提醒"))
        {
            if (isUpdate)
            {
                if (!notifyTime.equals(scheduleEvent.getNotifyTime()))
                {
                    ArrayList<Alarm> alarmList = Alarms.getAllAlarm(getContentResolver());
                    if (alarmList != null && alarmList.size() > 0)
                    {
                        for (int i = 0; i < alarmList.size(); i++)
                        {
                            Alarm alarm = alarmList.get(i);
                            if (alarm.label.equals(scheduleEvent.getCreateTime()))
                            {
                                //                                alarm.year = calendar.get(Calendar.YEAR);
                                //                                alarm.month = calendar.get(Calendar.MONTH);
                                //                                alarm.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                //                                alarm.hour = calendar.get(Calendar.HOUR_OF_DAY);
                                //                                alarm.minutes = calendar.get(Calendar.MINUTE);
                                //                                alarm.time = calendar.getTimeInMillis();
                                //                                Alarms.updateAlarm(this, alarm);
                                break;
                            }
                        }
                    }
                }
            }
            else
            {
                //                Alarms.createAlarm(this, createTime, calendar);
            }
        }
        finish();
        activityAnimationClose();
    }

    private void dateTimePickerDialog()
    {
        dateTimeDialog = new AlertDialog.Builder(this).create();
        dateTimeDialog.show();
        Window window = dateTimeDialog.getWindow();
        window.setContentView(R.layout.date_time_picker_dialog);
        mDateTimePicker= (DateTimePicker) window.findViewById(R.id.alarm_date_time_picker);
        Button ok = (Button) window.findViewById(R.id.ok_button);
        Button cancel = (Button) window.findViewById(R.id.cancel_button);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dateTimeDialog.cancel();
            }
        });
    }
}
