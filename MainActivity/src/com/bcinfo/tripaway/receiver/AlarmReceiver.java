package com.bcinfo.tripaway.receiver;

import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.alarm.Alarm;
import com.bcinfo.tripaway.view.alarm.Alarms;
import com.bcinfo.tripaway.view.dialog.AlarmDialog;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Parcel;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 闹钟广播接收
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月16日 下午7:43:37
 */
public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        LogUtil.i("AlarmReceiver", "onReceive", "intent.getAction()=" + intent.getAction());
        Toast.makeText(context, "你设置的闹钟时间到了", Toast.LENGTH_LONG).show();
        final byte[] data = intent.getByteArrayExtra(Alarms.ALARM_RAW_DATA);
        Alarm alarm = null;
        if (data != null)
        {
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            alarm = Alarm.CREATOR.createFromParcel(in);
        }
        Intent playAlarm = new Intent(Alarms.ALARM_ALERT_ACTION);
        playAlarm.putExtra(Alarms.ALARM_INTENT_EXTRA, alarm);
        context.startService(playAlarm);
        Intent alarmIntent = new Intent(context, AlarmDialog.class);
        alarmIntent.putExtra(Alarms.ALARM_INTENT_EXTRA, alarm);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(alarmIntent);
    }
}
