package com.bcinfo.tripaway.view.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.bean.ScheduleEvent;
import com.bcinfo.tripaway.db.ScheduleEventDatabase;
import com.bcinfo.tripaway.utils.DateUtil;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 日历
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月5日 上午10:15:36
 */
public class CalendarTableCellProvider
{
    private long firstDayMillis = 0;
    private int solarTerm1 = 0;
    private int solarTerm2 = 0;
    private DateFormatter fomatter;
    private Resources mResources;

    public CalendarTableCellProvider(Resources resources, int monthIndex)
    {
        mResources=resources;
        int year = LunarCalendar.getMinYear() + (monthIndex / 12);
        int month = monthIndex % 12;
        Calendar date = new GregorianCalendar(year, month, 1);
        int offset = 1 - date.get(Calendar.DAY_OF_WEEK);
        date.add(Calendar.DAY_OF_MONTH, offset);
        firstDayMillis = date.getTimeInMillis();
        solarTerm1 = LunarCalendar.getSolarTerm(year, month * 2 + 1);
        solarTerm2 = LunarCalendar.getSolarTerm(year, month * 2 + 2);
        fomatter = new DateFormatter(resources);
    }

    public View getView(int position, LayoutInflater inflater, ViewGroup container, ScheduleEventDatabase database)
    {
        //是否节日
        boolean isFestival = false;
        //是否节气
        boolean isSolarTerm = false;
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.view_calendar_day_cell, container, false);
        TextView setItemWidth = (TextView) rootView.findViewById(R.id.set_item_width);
        LinearLayout markToday = (LinearLayout) rootView.findViewById(R.id.mark_today);
        ImageView eventMark = (ImageView) rootView.findViewById(R.id.event_remark);
        TextView txtCellGregorian = (TextView) rootView.findViewById(R.id.txtCellGregorian);
        TextView txtCellLunar = (TextView) rootView.findViewById(R.id.txtCellLunar);
        setItemWidth.setLayoutParams(new LayoutParams(BaseActivity.screenWidth / 7, 6));
        markToday.setLayoutParams(new LayoutParams(BaseActivity.screenWidth / 7 - 2, BaseActivity.screenWidth / 7 - 2));
        // 开始日期处理
        LunarCalendar date = new LunarCalendar(firstDayMillis + position * LunarCalendar.DAY_MILLIS);
        int gregorianDay = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        // 判断是否为本月日期
        boolean isOutOfRange = ((position < 7 && gregorianDay > 7) || (position > 7 && gregorianDay < position - 7 - 6));
        txtCellGregorian.setText(String.valueOf(gregorianDay));
        // 农历节日 > 公历节日 > 农历月份 > 二十四节气 > 农历日
        int index = date.getLunarFestival();
        if (index >= 0)
        {
            // 农历节日
            txtCellLunar.setTag("isFestival");
            txtCellLunar.setText(fomatter.getLunarFestivalName(index));
            isFestival = true;
        }
        else
        {
            //            index = date.getGregorianFestival();
            //            if (index >= 0)
            //            {
            //                // 公历节日
            //                txtCellLunar.setText(fomatter.getGregorianFestivalName(index));
            //                isFestival = true;
            //            }else
            if (date.getLunar(LunarCalendar.LUNAR_DAY) == 1)
            {
                // 初一,显示月份
                txtCellLunar.setText(fomatter.getMonthName(date));
            }
            else if (!isOutOfRange && gregorianDay == solarTerm1)
            {
                // 节气1
                txtCellLunar.setTag("isSolarTerm");
                txtCellLunar.setText(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2));
                isSolarTerm = true;
            }
            else if (!isOutOfRange && gregorianDay == solarTerm2)
            {
                // 节气2
                txtCellLunar.setTag("isSolarTerm");
                txtCellLunar.setText(fomatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2 + 1));
                isSolarTerm = true;
            }
            else
            {
                txtCellLunar.setText(fomatter.getDayName(date));
            }
        }
        // set style
        Resources resources = container.getResources();
        if (isOutOfRange)
        {
            txtCellGregorian.setTag(isOutOfRange);
            txtCellGregorian.setTextColor(resources.getColor(R.color.gray_little_d));
            txtCellLunar.setTextColor(resources.getColor(R.color.gray_little_d));
        }
        else if (isFestival)
        {
            txtCellLunar.setTextColor(resources.getColor(R.color.color_calendar_festival));
        }
        else if (isSolarTerm)
        {
            txtCellLunar.setTextColor(resources.getColor(R.color.color_calendar_solarterm));
        }
        if (date.isToday())
        {
            markToday.setBackgroundResource(R.drawable.img_hint_today);
            markToday.setTag("today");
            txtCellGregorian.setTextColor(resources.getColor(R.color.white));
            txtCellLunar.setTextColor(resources.getColor(R.color.white));
        }
        // store date into tag
        rootView.setTag(date);
        int year = date.getGregorianDate(Calendar.YEAR);
        int month = date.getGregorianDate(Calendar.MONTH) + 1;
        int dayOfMonth = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        String dateStr = DateUtil.getFormateDate(year, month, dayOfMonth);
        ArrayList<ScheduleEvent> eventList=   database.queryEventByDate(dateStr);
        setEventColor(eventMark, eventList);
        return rootView;
    }
    
    private void setEventColor( ImageView eventMark,ArrayList<ScheduleEvent> eventList)
    {
        if(eventList!=null&&eventList.size()>0)
        {
            for (int i = 0; i < eventList.size(); i++)
            {
                ScheduleEvent event=eventList.get(i);
               if(event.getColor().equals("green")) 
               {
                   eventMark.setBackgroundColor(mResources.getColor(R.color.event_green));
                   eventMark.setVisibility(View.VISIBLE);
                   return;
               }
            }
            for (int i = 0; i < eventList.size(); i++)
            {
                ScheduleEvent event=eventList.get(i);
                if(event.getColor().equals("gray")) 
               {
                   eventMark.setBackgroundColor(mResources.getColor(R.color.event_gray));
                   eventMark.setVisibility(View.VISIBLE);
                   return;
               }
            }
            eventMark.setBackgroundColor(mResources.getColor(R.color.event_red));
            eventMark.setVisibility(View.VISIBLE);
        }
    }
}
