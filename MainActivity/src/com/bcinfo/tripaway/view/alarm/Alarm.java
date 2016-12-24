package com.bcinfo.tripaway.view.alarm;

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.Calendar;

import com.bcinfo.tripaway.R;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;
/**
 * 闹钟类
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月16日 上午11:15:12
 */
public final class Alarm implements Parcelable
{
    //序列化的Parcelable接口
    /*
     * by wangxianming 
     * in 2012-04-09
     * by start
     */
    public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>()
    {
        public Alarm createFromParcel(Parcel p)
        {
            return new Alarm(p);
        }

        public Alarm[] newArray(int size)
        {
            return new Alarm[size];
        }
    };

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags)
    {
        p.writeInt(id);
        p.writeInt(enabled ? 1 : 0);
        p.writeInt(year);
        p.writeInt(month);
        p.writeInt(dayOfMonth);
        p.writeInt(hour);
        p.writeInt(minutes);
        p.writeLong(time);
        p.writeInt(vibrate ? 1 : 0);
        p.writeString(label);
        p.writeParcelable(alert, flags);
        p.writeInt(silent ? 1 : 0);
    }
    // 定义列
    public static class Columns implements BaseColumns
    {
        /**
         * The content:// 为这个表定义一个共享的Url
         */
        public static final Uri CONTENT_URI = Uri.parse("content://com.bcinfo.tripaway/alarm");
        /**
         * Hour in 24-hour localtime 0 - 23.
         * <P>Type: INTEGER</P>
         */
        public static final String YEAR = "year";
        /**
         * Hour in 24-hour localtime 0 - 23.
         * <P>Type: INTEGER</P>
         */
        public static final String MONTH = "month";
        /**
         * Hour in 24-hour localtime 0 - 23.
         * <P>Type: INTEGER</P>
         */
        public static final String DAY_OF_MONTH = "dayofmonth";
        /**
         * Hour in 24-hour localtime 0 - 23.
         * <P>Type: INTEGER</P>
         */
        public static final String HOUR = "hour";
        /**
         * Minutes in localtime 0 - 59
         * <P>Type: INTEGER</P>
         */
        public static final String MINUTES = "minutes";
        /**
         * Days of week coded as integer
         * <P>Type: INTEGER</P>
         */
        //        public static final String DAYS_OF_WEEK = "daysofweek";
        /**
         * Alarm time in UTC milliseconds from the epoch.
         * <P>Type: INTEGER</P>
         */
        public static final String ALARM_TIME = "alarmtime";
        /**
         * True if alarm is active
         * <P>Type: BOOLEAN</P>
         */
        public static final String ENABLED = "enabled";
        /**
         * True if alarm should vibrate
         * <P>Type: BOOLEAN</P>
         */
        public static final String VIBRATE = "vibrate";
        /**
         * Message to show when alarm triggers
         * Note: not currently used
         * <P>Type: STRING</P>
         */
        public static final String MESSAGE = "message";
        /**
         * Audio alert to play when alarm triggers
         * <P>Type: STRING</P>
         */
        public static final String ALERT = "alert";
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = HOUR + ", " + MINUTES + " ASC";
        // Used when filtering enabled alarms.
        public static final String WHERE_ENABLED = ENABLED + "=1";
        static final String[] ALARM_QUERY_COLUMNS =
        { _ID, YEAR, MONTH, DAY_OF_MONTH, HOUR, MINUTES, ALARM_TIME, ENABLED, VIBRATE, MESSAGE, ALERT };
        /**
         * These save calls to cursor.getColumnIndexOrThrow()
         * THEY MUST BE KEPT IN SYNC WITH ABOVE QUERY COLUMNS
         */
        public static final int ALARM_ID_INDEX = 0;
        public static final int ALARM_YEAR_INDEX = 1;
        public static final int ALARM_MONTH_INDEX = 2;
        public static final int ALARM_DAYS_OF_MONTH_INDEX = 3;
        public static final int ALARM_HOUR_INDEX = 4;
        public static final int ALARM_MINUTES_INDEX = 5;
        //        public static final int ALARM_DAYS_OF_WEEK_INDEX = 3;
        public static final int ALARM_TIME_INDEX = 6;
        public static final int ALARM_ENABLED_INDEX = 7;
        public static final int ALARM_VIBRATE_INDEX = 8;
        public static final int ALARM_MESSAGE_INDEX = 9;
        public static final int ALARM_ALERT_INDEX = 10;
        // End 每一列定义结束
    }
    // 对应的公共的每一列的映射
    public int id;
    public boolean enabled;
    public int year;
    public int month;
    public int dayOfMonth;
    public int hour;
    public int minutes;
    //    public DaysOfWeek daysOfWeek;
    public long time;
    public boolean vibrate;
    public String label;
    public Uri alert;
    public boolean silent;

    public Alarm(Cursor c)
    {
        id = c.getInt(Columns.ALARM_ID_INDEX);
        enabled = c.getInt(Columns.ALARM_ENABLED_INDEX) == 1;
        year = c.getInt(Columns.ALARM_YEAR_INDEX);
        month = c.getInt(Columns.ALARM_MONTH_INDEX);
        dayOfMonth = c.getInt(Columns.ALARM_DAYS_OF_MONTH_INDEX);
        hour = c.getInt(Columns.ALARM_HOUR_INDEX);
        minutes = c.getInt(Columns.ALARM_MINUTES_INDEX);
        time = c.getLong(Columns.ALARM_TIME_INDEX);
        vibrate = c.getInt(Columns.ALARM_VIBRATE_INDEX) == 1;
        label = c.getString(Columns.ALARM_MESSAGE_INDEX);
        String alertString = c.getString(Columns.ALARM_ALERT_INDEX);
        if (Alarms.ALARM_ALERT_SILENT.equals(alertString))
        {
            if (true)
            {
                Log.v("wangxianming", "Alarm is marked as silent");
            }
            silent = true;
        }
        else
        {
            if (alertString != null && alertString.length() != 0)
            {
                alert = Uri.parse(alertString);
            }
            // If the database alert is null or it failed to parse, use the
            // default alert.
            if (alert == null)
            {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }
    }

    public Alarm(Parcel p)
    {
        id = p.readInt();
        enabled = p.readInt() == 1;
        year = p.readInt();
        month = p.readInt();
        dayOfMonth = p.readInt();
        hour = p.readInt();
        minutes = p.readInt();
        time = p.readLong();
        vibrate = p.readInt() == 1;
        label = p.readString();
        alert = (Uri) p.readParcelable(null);
        silent = p.readInt() == 1;
    }

    // Creates a default alarm at the current time.
    //创建一个默认当前时间的闹钟
    public Alarm()
    {
        id = -1;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        vibrate = true;
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    public String getLabelOrDefault(Context context)
    {
        if (label == null || label.length() == 0)
        {
            return context.getString(R.string.default_alarm_label);
        }
        return label;
    }
}
