package com.bcinfo.tripaway.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.bcinfo.tripaway.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

/**
 * 时间日期合并按钮
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月15日 下午4:04:30
 */
public class DateTimeButton extends Button
{
    private final static String DATA_FORMAT_YMDHMS_EN = "yyyy-MM-dd HH:mm";
    public Calendar time = Calendar.getInstance(Locale.CHINA);
    public static final SimpleDateFormat format = new SimpleDateFormat(DATA_FORMAT_YMDHMS_EN);
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button dataView;
    private AlertDialog dialog;

    //  private Activity activity;
    public DateTimeButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public DateTimeButton(Context context)
    {
        super(context);
        init();
    }

    //增加构造器
    public DateTimeButton(Context context, Button dataView)
    {
        super(context);
        this.dataView = dataView;
    }

    public AlertDialog dateTimePickerDialog()
    {
        LinearLayout dateTimeLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.date_time_picker, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.DatePicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.TimePicker);
        if (dataView == null)
            init();
        timePicker.setIs24HourView(true);
        OnTimeChangedListener timeListener = new OnTimeChangedListener()
        {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
            {
                // TODO Auto-generated method stub
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                time.set(Calendar.SECOND, 0);
            }
        };
        timePicker.setOnTimeChangedListener(timeListener);
        OnDateChangedListener dateListener = new OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                // TODO Auto-generated method stub
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, monthOfYear);
                time.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
        datePicker.init(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH),
                dateListener);
        timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(time.get(Calendar.MINUTE));
        dialog = new AlertDialog.Builder(getContext()).setTitle("设置日期时间").setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                        datePicker.clearFocus();
                        timePicker.clearFocus();
                        time.set(Calendar.YEAR, datePicker.getYear());
                        time.set(Calendar.MONTH, datePicker.getMonth());
                        time.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        time.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                        time.set(Calendar.SECOND, 0);
                        DateTimeButton.this.setTextColor(getResources().getColor(R.color.dark_gray));
                        updateLabel();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                    }
                }).show();
        return dialog;
    }

    /**
     * 
     */
    private void init()
    {
        this.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                dateTimePickerDialog();
            }
        });
        //        updateLabel();
    }

    /**
     * 更新标签
     */
    public void updateLabel()
    {
        if (dataView != null)
        {
            dataView.setText(format.format(time.getTime()));
            dataView.setTextColor(getResources().getColor(R.color.dark_gray));
        }
        this.setText(format.format(time.getTime()));
    }

    /**
     * @return 获得时间字符串"yyyy-MM-dd HH:mm"
     */
    public String getDateString()
    {
        return format.format(time.getTime());
    }

    public Calendar getCalendar()
    {
        return time;
    }
    
    public void setCalendar(Calendar calendar)
    {
        time=calendar;
    }

    public void setDate(String datestr)
    {
        try
        {
            time.setTime(format.parse(datestr));
            updateLabel();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
