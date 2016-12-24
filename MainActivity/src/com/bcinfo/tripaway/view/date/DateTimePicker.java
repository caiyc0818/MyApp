package com.bcinfo.tripaway.view.date;

import java.util.Calendar;

import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * 日期时间综合选择控件
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年3月5日 上午10:56:34
 */
public class DateTimePicker extends DrumPicker
{
    private final static String TAG = "DateTimePicker";
    private final static String[] YEAR =
    { "2030", "2029", "2028", "2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017",
            "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004",
            "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991",
            "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982", "1981", "1980", "1979", "1978",
            "1977", "1976", "1975", "1974", "1973", "1972", "1971", "1970", };
    private final static String[] MONTH =
    { "12", "11", "10", "09", "08", "07", "06", "05", "04", "03", "02", "01" };
    private final static String[] DAYS =
    { "31", "30", "29", "28", "27", "26", "25", "24", "23", "22", "21", "20", "19", "18", "17", "16", "15", "14", "13",
            "12", "11", "10", "09", "08", "07", "06", "05", "04", "03", "02", "01" };
    private final static String[] HOUR =
    { "23", "22", "21", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "10", "09", "08", "07", "06", "05",
            "04", "03", "02", "01", "00" };
    private final static String[] TIME =
    { "59", "58", "57", "56", "55", "54", "53", "52", "51", "50", "49", "48", "47", "46", "45", "44", "43", "42", "41",
            "40", "39", "38", "37", "36", "35", "34", "33", "32", "31", "30", "29", "28", "27", "26", "25", "24", "23",
            "22", "21", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "10", "09", "08", "07", "06", "05",
            "04", "03", "02", "01", "00" };
    public interface OnCalendarChangedListener
    {
        public void onCalendarChanged(Calendar calendar);
    }
    private OnCalendarChangedListener mCalendarListener = null;
    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    private int mHour = 0;
    private int mMinitue = 0;
    private Calendar mCalendar = null;

    public DateTimePicker(Context context)
    {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attr)
    {
        super(context, attr);
        init();
    }

    private void init()
    {
        mCalendar = Calendar.getInstance();
        addRow(YEAR, 80, "年");
        addRow(MONTH, 50, "月");
        addRow(DAYS, 50, "日");
        addRow(HOUR, 50, "时");
        addRow(TIME, 50, "分");
        String year = Integer.toString(mCalendar.get(Calendar.YEAR));
        for (int i = 0; i < YEAR.length; i++)
        {
            String y = YEAR[i];
            if (year.equals(y))
            {
                setPosition(0, i);
                break;
            }
        }
        setPosition(1, 11 - mCalendar.get(Calendar.MONTH));
        int oldy = mCalendar.get(Calendar.YEAR);
        int oldm = mCalendar.get(Calendar.MONTH);
        int oldd = mCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        resizeDay(oldy, oldm + 1);
        final int days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setPosition(2, days - oldd);
        setPosition(3, 23 - hour);
        setPosition(4, 59 - (minute + 1));
        /**日期时间变化监听**/
        setOnPostionChangedListener(new OnPositionChangedListener()
        {
            @Override
            public void onPositionChanged(int itemPos, int pos)
            {
                if (pos < 0)
                {
                    return;
                }
                switch (itemPos)
                {
                /********日期*********/
                    case 0:
                        mYear = Integer.parseInt(YEAR[pos]);
                        resizeDay(mYear, mMonth);
                        break;
                    case 1:
                        mMonth = Integer.parseInt(MONTH[pos]);
                        resizeDay(mYear, mMonth);
                        break;
                    case 2:
                        int adjust = monthDays(mYear, mMonth);
                        mDay = Integer.parseInt(DAYS[pos + (adjust > -1 ? 31 - adjust : 0)]);
                        break;
                    /********时间*********/
                    case 3:
                        mHour = Integer.parseInt(HOUR[pos]);
                        break;
                    case 4:
                        mMinitue = Integer.parseInt(TIME[pos]);
                        break;
                }
                mCalendar.set(mYear, mMonth-1, mDay, mHour, mMinitue, 0);
                if (mCalendarListener != null)
                {
                    mCalendarListener.onCalendarChanged(mCalendar);
                }
            }
        });
    }

    public void setOnCalendarChangedListener(OnCalendarChangedListener linstener)
    {
        mCalendarListener = linstener;
    }

    /**
     * 重置月份天数
     * @param year  年份
     * @param month 月份(从1开始)
     */
    private void resizeDay(int year, int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.add(Calendar.MONTH, -1);
        final int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(mDay>days)
        {
            mDay=days;
        }
        resize(2, new IsGoneListener()
        {
            @Override
            public boolean isGone(View item, int pos)
            {
                if (30 - pos < days)
                {
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int height = (int) (150 * AppInfo.getDisplayScale(getContext()));
        int h = MeasureSpec.makeMeasureSpec(MeasureSpec.getMode(heightMeasureSpec), height);
        super.onMeasure(widthMeasureSpec, h);
    }

    boolean isGone(View item)
    {
        return false;
    }

    static int monthDays(int year, int month)
    {
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (isLeapYear(year))
                {
                    return 29;
                }
                else
                {
                    return 28;
                }
        }
        return -1;
    }

    static boolean isLeapYear(int year)
    {
        if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Calendar getCalendar()
    {
        return mCalendar;
    }

    public void setYear(int year)
    {
        if (year >= 1970 && year < 2030)
        {
            setPosition(0, YEAR.length - 1 - (year - 1970));
        }
    }

    public void setMonth(int month)
    {
        if (month >= 1 && month < 13)
        {
            setPosition(1, MONTH.length - 1 - month);
        }
    }

    public void setDay(int day)
    {
        if (day >= 1 && day < 32)
        {
            int days = monthDays(mYear, mMonth);
            setPosition(2, days - 1 - day);
        }
    }

    public int getHour()
    {
        return mHour;
    }

    public void setHour(int hour)
    {
        if (hour >= 0 && hour < 24)
        {
            setPosition(0, 23 - hour);
        }
    }

    public int getMinitue()
    {
        return mMinitue;
    }

    public void setMinitue(int minitue)
    {
        if (minitue >= 0 && minitue < 60)
        {
            setPosition(1, 59 - minitue);
        }
    }
}
