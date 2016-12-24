package com.bcinfo.tripaway.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bcinfo.tripaway.TripawayApplication;

/**
 * 保存数据至preference
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月15日 下午5:02:57
 */
public class PreferenceUtil
{
    /**
     * 存入文件中的工程名
     */
    public static final String PREFERENCES_NAME = "com.bcinfo.tripaway.preferences";
    
    /**
     * 闹钟
     */
    public static final String ALARM_CLOCK = "AlarmClock";
    
    public static final String MESSAGE_NOTIFY_KEY = "message_notify";
    
    public static final String MESSAGE_SOUND_KEY = "message_sound";
    
    public static final String SHOW_HEAD_KEY = "show_head";
    
    // 登录成功后生成的token 永久令牌
    public static void setToken(String tokenValue)
    {
        setString("token", tokenValue);
    }
    
    public static String getToken()
    {
        return getString("token");
        
    }
    
    public static void setAccount(String account)
    {
        setString("account", account);
    }
    
    public static String getAccount()
    {
        return getString("account");
    }
    
    // 登录成功后生成的session 临时会话
    public static void setSession(String sessionId)
    {
        setString("session", sessionId);
    }
    
    public static String getSession()
    {
        return getString("session");
        
    }
    
    // appid
    public static void setAppId(String appid)
    {
        // TODO Auto-generated method stub
        setString("appid", appid);
    }
    
    public static String getAppId()
    {
        return getString("appid");
    }
    
    // user_id
    public static void setUserId(String userId)
    {
        setString("userId", userId);
    }
    
    public static String getUserId()
    {
        return getString("userId");
    }
    
    public static void delUserInfo()
    {
        delString("userId");
        delString("session");
        delString("token");
        delString("account");
        delString("avatar");
    }
    
    // clientId
    public static void setClientId(String clientid)
    {
        setString("clientid", clientid);
    }
    
    public static String getClientId()
    {
        
        return getString("clientid");
    }
    
    // channel_id
    public static void setChannelId(String ChannelId)
    {
        setString("ChannelId", ChannelId);
    }
    
    public static String getChannelId()
    {
        return getString("ChannelId");
    }
    
    // nick
    public static void setNick(String nick)
    {
        setString("nick", nick);
    }
    
    public static String getNick()
    {
        return getString("nick");
    }
    
    // 头像图标
    public static int getHeadIcon()
    {
        return getInt("headIcon");
    }
    
    public static void setHeadIcon(int icon)
    {
        setInt("headIcon", icon);
    }
    
    // 设置Tag
    public static void setTag(String tag)
    {
        setString("tag", tag);
    }
    
    public static String getTag()
    {
        return getString("tag");
    }
    
    // 是否通知
    public static boolean getMsgNotify()
    {
        return getBoolean(MESSAGE_NOTIFY_KEY);
    }
    
    public static void setMsgNotify(boolean isChecked)
    {
        setBoolean(MESSAGE_NOTIFY_KEY, isChecked);
    }
    
    // 是否有声音
    public static boolean getMsgSound()
    {
        return getBoolean(MESSAGE_SOUND_KEY);
    }
    
    public static void setMsgSound(boolean isChecked)
    {
        setBoolean(MESSAGE_SOUND_KEY, isChecked);
    }
    
    // 是否显示自己头像
    public static boolean getShowHead()
    {
        return getBoolean(SHOW_HEAD_KEY);
    }
    
    public static void setShowHead(boolean isChecked)
    {
        setBoolean(SHOW_HEAD_KEY, isChecked);
    }
    
    // 表情翻页效果
    public static int getFaceEffect()
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt("face_effects", 3);
    }
    
    public static void setFaceEffect(int effect)
    {
        if (effect < 0 || effect > 11)
            effect = 3;
        setInt("face_effects", effect);
    }
    
    /**
     * 保存至preferences中
     * 
     * @param key
     * @return
     */
    public static void setString(String key, String value)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     * @param value
     */
    public static String getString(String key)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
    
    /**
     * 保存至preferences中
     * 
     * @param key
     * @return
     */
    public static void setInt(String key, int value)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     * @param value
     */
    public static int getInt(String key)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     * @param value
     */
    public static String getStringWithDefault(String key)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "0");
    }
    
    /**
     * 删除preferences中的数据
     * 
     * @param key
     * @return
     */
    public static void delString(String key)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
    
    /**
     * 保存至preferences中
     * 
     * @param key
     * @return
     */
    public static void setLong(String key, Long value)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     * @param value
     */
    public static Long getLong(String key, long def)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, def);
    }
    
    public static void setBoolean(String key, boolean value)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    public static boolean getBoolean(String key)
    {
        SharedPreferences sp =
            TripawayApplication.application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
    
    public static void setHaveDeleteId(String id)
    {
        String[] deleteArray = getHaveDeleteIdArray();
        StringBuffer deleteStr = new StringBuffer();
        if (deleteArray != null && deleteArray.length > 0)
        {
            for (int i = 0; i < deleteArray.length; i++)
            {
                if (i != deleteArray.length - 1)
                {
                    deleteStr.append(deleteArray[i]).append(",");
                }
                else
                {
                    deleteStr.append(deleteArray[i]);
                }
            }
            deleteStr.append(",").append(id);
            setString("queueId", deleteStr.toString());
        }
        else
        {
            setString("queueId", id);
        }
        
    }
    
    public static String getHaveDeleteStr()
    {
        return getString("queueId");
    }
    
    public static String[] getHaveDeleteIdArray()
    {
        String deleteIdStr = getString("queueId");
        if (StringUtils.isEmpty(deleteIdStr))
        {
            return null;
        }
        else
        {
            String[] deleteArray = deleteIdStr.split(",");
            return deleteArray;
        }
    }
}
