package com.bcinfo.tripaway.service;

/*
 * Copyright (C) 2008 The Android Open Source Project
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
import com.bcinfo.tripaway.view.alarm.Alarm;
import com.bcinfo.tripaway.view.alarm.AlarmAlertWakeLock;
import com.bcinfo.tripaway.view.alarm.Alarms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.media.AudioManager.OnAudioFocusChangeListener;

/**
 * Manages alarms and vibe. Runs as a service so that it can continue to play
 * if another activity overrides the AlarmAlert dialog.
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月16日 下午7:43:21
 */
public class AlarmService extends Service
{
    /** Play alarm up to 10 minutes before silencing */
    private static final int ALARM_TIMEOUT_SECONDS = 10 * 60;
    private boolean mPlaying = false;
    private MediaPlayer mMediaPlayer;
    private Alarm mCurrentAlarm;
    private long mStartTime;
    private TelephonyManager mTelephonyManager;
    private int mInitialCallState;
    private AudioManager mAudioManager = null;
    private boolean mCurrentStates = true;
    private static final int KILLER = 1;
    private static final int FOCUSCHANGE = 2;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case KILLER:
                    sendKillBroadcast((Alarm) msg.obj);
                    stopSelf();
                    break;
                case FOCUSCHANGE:
                    switch (msg.arg1)
                    {
                        case AudioManager.AUDIOFOCUS_LOSS:
                            if (!mPlaying && mMediaPlayer != null)
                            {
                                stop();
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            if (!mPlaying && mMediaPlayer != null)
                            {
                                mMediaPlayer.pause();
                                mCurrentStates = false;
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (mPlaying && !mCurrentStates)
                            {
                                play(mCurrentAlarm);
                            }
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }
        }
    };
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener()
    {
        @Override
        public void onCallStateChanged(int state, String ignored)
        {
            // which kills the alarm. Check against the initial call state so
            // we don't kill the alarm during a call.
            if (state != TelephonyManager.CALL_STATE_IDLE && state != mInitialCallState)
            {
                sendKillBroadcast(mCurrentAlarm);
                stopSelf();
            }
        }
    };

    @Override
    public void onCreate()
    {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        AlarmAlertWakeLock.acquireCpuWakeLock(this);
    }

    @Override
    public void onDestroy()
    {
        stop();
        // Stop listening for incoming calls.
        mTelephonyManager.listen(mPhoneStateListener, 0);
        AlarmAlertWakeLock.releaseCpuLock();
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent == null)
        {
            stopSelf();
            return START_NOT_STICKY;
        }
        final Alarm alarm = intent.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);
        if (alarm == null)
        {
            stopSelf();
            return START_NOT_STICKY;
        }
        if (mCurrentAlarm != null)
        {
            sendKillBroadcast(mCurrentAlarm);
        }
        play(alarm);
        mCurrentAlarm = alarm;
        // Record the initial call state here so that the new alarm has the
        mInitialCallState = mTelephonyManager.getCallState();
        return START_STICKY;
    }

    private void sendKillBroadcast(Alarm alarm)
    {
        long millis = System.currentTimeMillis() - mStartTime;
        int minutes = (int) Math.round(millis / 60000.0);
        Intent alarmKilled = new Intent(Alarms.ALARM_KILLED);
        alarmKilled.putExtra(Alarms.ALARM_INTENT_EXTRA, alarm);
        alarmKilled.putExtra(Alarms.ALARM_KILLED_TIMEOUT, minutes);
        sendBroadcast(alarmKilled);
    }
    // Volume suggested by media team for in-call alarms.
    //    private static final float IN_CALL_VOLUME = 0.125f;
    private OnAudioFocusChangeListener mAudioFocusListener = new OnAudioFocusChangeListener()
    {
        public void onAudioFocusChange(int focusChange)
        {
            mHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };

    private void play(final Alarm alarm)
    {
        // stop() checks to see if we are already playing.
        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_ALARM,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        stop();
        if (!alarm.silent)
        {
            Uri alert = alarm.alert;
            if (alert == null)
            {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new OnErrorListener()
            {
                public boolean onError(MediaPlayer mp, int what, int extra)
                {
                    mp.stop();
                    mp.release();
                    mMediaPlayer = null;
                    return true;
                }
            });
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mediaplayer)
                {
                    // TODO Auto-generated method stub
                    mHandler.sendMessage(mHandler.obtainMessage(KILLER, alarm));
                }
            });
            try
            {
                if (mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE)
                {
                    return;
                }
                else
                {
                    mMediaPlayer.setDataSource(this, alert);
                }
                startAlarm(mMediaPlayer);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        /* Start the vibrator after everything is ok with the media player */
        enableKiller(alarm);
        mPlaying = true;
        mStartTime = System.currentTimeMillis();
    }

    // Do the common stuff when starting the alarm.
    private void startAlarm(MediaPlayer player) throws java.io.IOException, IllegalArgumentException,
            IllegalStateException
    {
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // do not play alarms if stream volume is 0
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0)
        {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(false);
            player.prepare();
            player.start();
        }
    }

    /**
     * Stops alarm audio and disables alarm if it not snoozed and not
     * repeating
     */
    public void stop()
    {
        if (mPlaying)
        {
            mPlaying = false;
            Intent alarmDone = new Intent(Alarms.ALARM_DONE_ACTION);
            sendBroadcast(alarmDone);
            // Stop audio playing
            if (mMediaPlayer != null)
            {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            // Stop vibrator
            //            mVibrator.cancel();
        }
        disableKiller();
    }

    /**
     * Kills alarm audio after ALARM_TIMEOUT_SECONDS, so the alarm
     * won't run all day.
     *
     * This just cancels the audio, but leaves the notification
     * popped, so the user will know that the alarm tripped.
     */
    private void enableKiller(Alarm alarm)
    {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(KILLER, alarm), 1000 * ALARM_TIMEOUT_SECONDS);
    }

    private void disableKiller()
    {
        mHandler.removeMessages(KILLER);
    }
}
