package com.bcinfo.tripaway.view.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Settings;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;

import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;

/**
 * 闹钟管理
 *   The Alarms provider supplies info about Alarm Clock settings
 * 核心类，对Clock设置提供支持信息
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月16日 上午11:15:53
 */
public class Alarms
{
    private final static String TAG = "Alarms";
    // This action triggers the AlarmReceiver as well as the AlarmKlaxon. It
    // is a public action used in the manifest for receiving Alarm broadcasts
    // from the alarm manager.
    public static final String ALARM_ALERT_ACTION = "com.bcinfo.tripaway.ALARM_ALERT";
    // A public action sent by AlarmKlaxon when the alarm has stopped sounding
    // for any reason (e.g. because it has been dismissed from AlarmAlertFullScreen,
    // or killed due to an incoming phone call, etc).
    public static final String ALARM_DONE_ACTION = "com.bcinfo.tripaway.ALARM_DONE";
    // AlarmAlertFullScreen listens for this broadcast intent, so that other applications
    // can snooze the alarm (after ALARM_ALERT_ACTION and before ALARM_DONE_ACTION).
    public static final String ALARM_SNOOZE_ACTION = "com.bcinfo.tripaway.ALARM_SNOOZE";
    // AlarmAlertFullScreen listens for this broadcast intent, so that other applications
    // can dismiss the alarm (after ALARM_ALERT_ACTION and before ALARM_DONE_ACTION).
    public static final String ALARM_DISMISS_ACTION = "com.bcinfo.tripaway.ALARM_DISMISS";
    // This is a private action used by the AlarmKlaxon to update the UI to
    // show the alarm has been killed.
    public static final String ALARM_KILLED = "alarm_killed";
    // Extra in the ALARM_KILLED intent to indicate to the user how long the
    // alarm played before being killed.
    public static final String ALARM_KILLED_TIMEOUT = "alarm_killed_timeout";
    // This string is used to indicate a silent alarm in the db.
    public static final String ALARM_ALERT_SILENT = "silent";
    // This intent is sent from the notification when the user cancels the
    // snooze alert.
    public static final String CANCEL_SNOOZE = "cancel_snooze";
    // This string is used when passing an Alarm object through an intent.
    public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";
    // This extra is the raw Alarm object data. It is used in the
    // AlarmManagerService to avoid a ClassNotFoundException when filling in
    // the Intent extras.
    public static final String ALARM_RAW_DATA = "intent.extra.alarm_raw";
    // This string is used to identify the alarm id passed to SetAlarm from the
    // list of alarms.
    public static final String ALARM_ID = "alarm_id";
    final static String PREF_SNOOZE_ID = "snooze_id";
    final static String PREF_SNOOZE_TIME = "snooze_time";
    //    private final static String DM12 = "E h:mm aa";
    //    private final static String DM24 = "E k:mm";
    private final static String M12 = "h:mm aa";
    // Shared with DigitalClock
    public final static String M24 = "kk:mm";

    /**
     * Removes an existing Alarm.  If this alarm is snoozing, disables
     * snooze.  Sets next alert.
     */
    public static void deleteAlarm(Context context, int alarmId)
    {
        if (alarmId == -1)
            return;
        ContentResolver contentResolver = context.getContentResolver();
        /* If alarm is snoozing, lose it */
        disableSnoozeAlert(context, alarmId);
        Uri uri = ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarmId);
        contentResolver.delete(uri, "", null);
        //        setNextAlert(context);
    }

    /**
     * Queries all alarms
     * @return cursor over all alarms
     */
    public static Cursor getAlarmsCursor(ContentResolver contentResolver)
    {
        return contentResolver.query(Alarm.Columns.CONTENT_URI, Alarm.Columns.ALARM_QUERY_COLUMNS, null, null,
                Alarm.Columns.DEFAULT_SORT_ORDER);
    }

    // Private method to get a more limited set of alarms from the database.
    private static Cursor getFilteredAlarmsCursor(ContentResolver contentResolver)
    {
        return contentResolver.query(Alarm.Columns.CONTENT_URI, Alarm.Columns.ALARM_QUERY_COLUMNS,
                Alarm.Columns.WHERE_ENABLED, null, null);
    }

    private static ContentValues createContentValues(Alarm alarm)
    {
        ContentValues values = new ContentValues(10);
        // Set the alarm_time value if this alarm does not repeat. This will be
        // used later to disable expire alarms.
        values.put(Alarm.Columns.ENABLED, alarm.enabled ? 1 : 0);
        values.put(Alarm.Columns.YEAR, alarm.year);
        values.put(Alarm.Columns.MONTH, alarm.month);
        values.put(Alarm.Columns.DAY_OF_MONTH, alarm.dayOfMonth);
        values.put(Alarm.Columns.HOUR, alarm.hour);
        values.put(Alarm.Columns.MINUTES, alarm.minutes);
        values.put(Alarm.Columns.ALARM_TIME, alarm.time);
        values.put(Alarm.Columns.VIBRATE, alarm.vibrate);
        values.put(Alarm.Columns.MESSAGE, alarm.label);
        // A null alert Uri indicates a silent alarm.
        values.put(Alarm.Columns.ALERT, alarm.alert == null ? ALARM_ALERT_SILENT : alarm.alert.toString());
        return values;
    }

    public static Uri getDefaultRingtoneUri(Context context, int type)
    {
        return RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM);
    }

    public static void createAlarm(Context context, String alarmName, Calendar cal)
    {
        Alarm alarm = new Alarm();
        alarm.id = -1;
        alarm.year = cal.get(Calendar.YEAR);
        alarm.month = cal.get(Calendar.MONTH);
        alarm.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        alarm.hour = cal.get(Calendar.HOUR_OF_DAY);
        alarm.minutes = cal.get(Calendar.MINUTE);
        //        alarm.daysOfWeek = new Alarm.DaysOfWeek(0);
        alarm.vibrate = false;
        alarm.label = alarmName;
        alarm.alert = getDefaultRingtoneUri(context, 0);
        alarm.time = cal.getTimeInMillis();
        if (cal.getTimeInMillis() < System.currentTimeMillis())
        {
            alarm.enabled = false;
        }
        else
        {
            alarm.enabled = true;
        }
        Alarms.addAlarm(context, alarm);
    }

    /**
     * Return an Alarm object representing the alarm id in the database.
     * Returns null if no alarm exists.
     */
    public static Alarm getAlarm(ContentResolver contentResolver, int alarmId)
    {
        Cursor cursor = contentResolver.query(ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarmId),
                Alarm.Columns.ALARM_QUERY_COLUMNS, null, null, null);
        Alarm alarm = null;
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                alarm = new Alarm(cursor);
            }
            cursor.close();
        }
        return alarm;
    }

    public static ArrayList<Alarm> getAllAlarm(ContentResolver contentResolver)
    {
        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
        Cursor cursor = getAlarmsCursor(contentResolver);
        LogUtil.d("AlarmManage", "getAllAlarm", "mCursor=" + cursor);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    Alarm alarm = new Alarm(cursor);
                    alarmList.add(alarm);
                    LogUtil.i("AlarmManage", "getAllAlarm", "alarm.enabled/alarm.label=" + alarm.enabled + "/"
                            + alarm.label);
                }
                while (cursor.moveToNext());
            }
        }
        return alarmList;
    }

    /**
     * Creates a new Alarm and fills in the given alarm's id.
     */
    public static void addAlarm(Context context, Alarm alarm)
    {
        if (alarm.time < System.currentTimeMillis())
        {
            LogUtil.i(TAG, "addAlarm", "闹钟过期");
            return;
        }
        ContentValues values = createContentValues(alarm);
        Uri uri = context.getContentResolver().insert(Alarm.Columns.CONTENT_URI, values);
        alarm.id = (int) ContentUris.parseId(uri);
        enableAlert(context, alarm);
    }

    /**
     * A convenience method to set an alarm in the Alarms
     * content provider.
     * @return Time when the alarm will fire.
     */
    public static void updateAlarm(Context context, Alarm alarm)
    {
        if (alarm.time < System.currentTimeMillis())
        {
            LogUtil.i(TAG, "setAlarm", "闹钟过期");
            return;
        }
        LogUtil.i(TAG, "updateAlarm", "alarm.dayOfMonth=" + alarm.dayOfMonth);
        LogUtil.i(TAG, "updateAlarm", " alarm.hour=" + alarm.hour);
        LogUtil.i(TAG, "updateAlarm", " alarm.minutes=" + alarm.minutes);
        ContentValues values = createContentValues(alarm);
        ContentResolver resolver = context.getContentResolver();
        resolver.update(ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarm.id), values, null, null);
        enableAlert(context, alarm);
    }

    private static void enableAlarmInternal(final Context context, final Alarm alarm, boolean enabled)
    {
        if (alarm == null)
        {
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues(2);
        values.put(Alarm.Columns.ENABLED, enabled ? 1 : 0);
        // If we are enabling the alarm, calculate alarm time since the time
        // value in Alarm may be old.
        if (enabled)
        {
            long time = 0;
            time = calculateAlarm(alarm);
            values.put(Alarm.Columns.ALARM_TIME, time);
        }
        else
        {
            // Clear the snooze if the id matches.
            disableSnoozeAlert(context, alarm.id);
        }
        resolver.update(ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarm.id), values, null, null);
    }

    /**
     * Disables non-repeating alarms that have passed.  Called at
     * boot.
     */
    public static void disableExpiredAlarms(final Context context)
    {
        Cursor cur = getFilteredAlarmsCursor(context.getContentResolver());
        long now = System.currentTimeMillis();
        if (cur.moveToFirst())
        {
            do
            {
                Alarm alarm = new Alarm(cur);
                // A time of 0 means this alarm repeats. If the time is
                // non-zero, check if the time is before now.
                if (alarm.time != 0 && alarm.time < now)
                {
                    enableAlarmInternal(context, alarm, false);
                }
            }
            while (cur.moveToNext());
        }
        cur.close();
    }

    /**
     * Sets alert in AlarmManger and StatusBar.  This is what will
     * actually launch the alert when the alarm triggers.
     *
     * @param alarm Alarm.
     * @param atTimeInMillis milliseconds since epoch
     */
    private static void enableAlert(Context context, final Alarm alarm)
    {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ALERT_ACTION);
        // To avoid this, we marshall the data ourselves and then parcel a plain
        // byte[] array. The AlarmReceiver class knows to build the Alarm
        // object from the byte[] array.
        Parcel out = Parcel.obtain();
        alarm.writeToParcel(out, 0);
        out.setDataPosition(0);
        intent.putExtra(ALARM_RAW_DATA, out.marshall());
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, alarm.time, sender);
        //        setStatusBarIcon(context, true);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(alarm.time);
        LogUtil.i(TAG, "enableAlert", "alarm.hour=" + alarm.hour);
        LogUtil.i(TAG, "enableAlert", "alarm.minutes=" + alarm.minutes);
    }

    /**
     * Disables alert in AlarmManger and StatusBar.
     *
     * @param id Alarm ID.
     */
    static void disableAlert(Context context)
    {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_ALERT_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(sender);
        setStatusBarIcon(context, false);
        saveNextAlarm(context, "");
    }

    /**
     * Disable the snooze alert if the given id matches the snooze id.
     */
    public static void disableSnoozeAlert(final Context context, final int id)
    {
        SharedPreferences prefs = context.getSharedPreferences(PreferenceUtil.ALARM_CLOCK, 0);
        int snoozeId = prefs.getInt(PREF_SNOOZE_ID, -1);
        if (snoozeId == -1)
        {
            // No snooze set, do nothing.
            return;
        }
        else if (snoozeId == id)
        {
            // This is the same id so clear the shared prefs.
            clearSnoozePreference(context, prefs);
        }
    }

    // Helper to remove the snooze preference. Do not use clear because that
    // will erase the clock preferences. Also clear the snooze notification in
    // the window shade.
    private static void clearSnoozePreference(final Context context, final SharedPreferences prefs)
    {
        final int alarmId = prefs.getInt(PREF_SNOOZE_ID, -1);
        if (alarmId != -1)
        {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(alarmId);
        }
        final SharedPreferences.Editor ed = prefs.edit();
        ed.remove(PREF_SNOOZE_ID);
        ed.remove(PREF_SNOOZE_TIME);
        ed.apply();
    };

    /**
     * Tells the StatusBar whether the alarm is enabled or disabled
     */
    private static void setStatusBarIcon(Context context, boolean enabled)
    {
        Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
        alarmChanged.putExtra("alarmSet", enabled);
        context.sendBroadcast(alarmChanged);
    }

    private static long calculateAlarm(Alarm alarm)
    {
        return calculateAlarm(alarm.year, alarm.month, alarm.dayOfMonth, alarm.hour, alarm.minutes).getTimeInMillis();
    }

    /**
     * Given an alarm in hours and minutes, return a time suitable for
     * setting in AlarmManager.
     */
    static Calendar calculateAlarm(int year, int month, int day, int hour, int minute)
    {
        // start with now
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    static String formatTime(final Context context, int year, int month, int day, int hour, int minute)
    {
        Calendar c = calculateAlarm(year, month, day, hour, minute);
        return formatTime(context, c);
    }

    /* used by AlarmAlert */
    public static String formatTime(final Context context, Calendar c)
    {
        String format = get24HourMode(context) ? M24 : M12;
        return (c == null) ? "" : (String) DateFormat.format(format, c);
    }

    /**
     * Save time of the next alarm, as a formatted string, into the system
     * settings so those who care can make use of it.
     */
    static void saveNextAlarm(final Context context, String timeString)
    {
        Settings.System.putString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED, timeString);
    }

    /**
     * @return true if clock is set to 24-hour mode
     */
    public static boolean get24HourMode(final Context context)
    {
        return android.text.format.DateFormat.is24HourFormat(context);
    }
}
