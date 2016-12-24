package com.bcinfo.tripaway.getui.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;

/**
 * json消息整合
 * 
 * @author way
 * 
 */
public class JsonUtil
{
    public static final String USER_ID_KEY = "user_id";
    
    public static final String CHANNEL_ID_KEY = "channel_id";
    
    public static final String NICK_KEY = "nick";
    
    public static final String HEAD_ID_KEY = "head_id";
    
    public static final String TIME_SAMP_KEY = "time_samp";
    
    public static final String MESSAGE_KEY = "message";
    
    public static final String TAG_KEY = "tag";
    
    private static final String TAG = "JsonUtil";
    
    /**
     * 创建json消息
     * 
     * @param userId
     * @param channelId
     * @param nick
     * @param headId
     * @param timeSamp
     * @param message
     * @param tag
     * @return
     */
    public static String createJsonMsg(String userId, String channelId, String nick, String headId, String timeSamp,
        String message, String tag)
    {
        try
        {
            // 代码生成一个形如 {key1:value1,key2:value2,key3:value3}样式的json对象 在将它转换成json字符串
            String result =
                new JSONStringer().object()
                    .key(USER_ID_KEY)
                    .value(userId)
                    .key(CHANNEL_ID_KEY)
                    .value(channelId)
                    .key(NICK_KEY)
                    .value(nick)
                    .key(HEAD_ID_KEY)
                    .value(headId)
                    .key(TIME_SAMP_KEY)
                    .value(timeSamp)
                    .key(MESSAGE_KEY)
                    .value(message)
                    .key(TAG_KEY)
                    .value(tag)
                    .endObject()
                    .toString();
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * 创建json消息 (组装成Json数据)
     * 
     * @param timeSamp
     * @param message
     * @param tag
     * @return
     */
    public static String createJsonMsg(long timeSamp, String message, String tag)
    {
        // SharePreferenceUtil util = Constant.getInstance().getSpUtil();
        try
        {
            String result =
                new JSONStringer().object()
                    .key(USER_ID_KEY)
                    .value(PreferenceUtil.getUserId())
                    .key(CHANNEL_ID_KEY)
                    .value(PreferenceUtil.getChannelId())
                    .key(NICK_KEY)
                    .value(PreferenceUtil.getNick())
                    .key(HEAD_ID_KEY)
                    .value(PreferenceUtil.getHeadIcon())
                    .key(TIME_SAMP_KEY)
                    .value(timeSamp)
                    .key(MESSAGE_KEY)
                    .value(message)
                    .key(TAG_KEY)
                    .value(tag)
                    .endObject()
                    .toString();
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    
    private static JSONObject getJSONObject(String content)
    {
        JSONObject jsonContent = null;
        try
        {
            jsonContent = new JSONObject(content);
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
            LogUtil.e(TAG, "getJSONObject", "Parse bind json infos error: " + e);
        }
        return jsonContent;
    }
    
    /**
     * 是不是自己的消息
     * 
     * @param msg
     * @return
     */
    public static boolean isMe(String msg)
    {
        boolean isMe = false;
        try
        {
            JSONObject jsonContent = getJSONObject(msg);
            isMe = jsonContent.getString(USER_ID_KEY).equals(PreferenceUtil.getUserId());
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
            LogUtil.e(TAG, "isMe", "Parse bind json infos error: " + e);
        }
        return isMe;
    }
    
    /**
     * 从json消息中提取消息内容
     * 
     * @param msg
     * @return
     */
    public static String getMsgContent(String msg)
    {
        String message = "";
        try
        {
            JSONObject jsonContent = new JSONObject(msg);
            message = jsonContent.getString(MESSAGE_KEY);
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
            LogUtil.e(TAG, "getMsgContent", "Parse bind json infos error: " + e);
        }
        return message;
    }
    
    public static String getChannelId(String msg)
    {
        String channelId = "";
        try
        {
            JSONObject jsonContent = new JSONObject(msg);
            channelId = jsonContent.getString(CHANNEL_ID_KEY);
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
            LogUtil.e(TAG, "getChannelId", "Parse bind json infos error: " + e);
        }
        return channelId;
    }
    
    public static String getFromUserId(String msg)
    {
        String userId = "";
        try
        {
            JSONObject jsonContent = new JSONObject(msg);
            userId = jsonContent.getString(USER_ID_KEY);
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
            LogUtil.e(TAG, "getFromUserId", "Parse bind json infos error: " + e);
        }
        return userId;
    }
    
    public static String getFromUserHead(String msg)
    {
        String userHead = "";
        try
        {
            JSONObject jsonContent = new JSONObject(msg);
            userHead = jsonContent.getString(HEAD_ID_KEY);
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
            LogUtil.e(TAG, "getFromUserHead", "Parse bind json infos error: " + e);
        }
        return userHead;
    }
    
    public static String getFromUserNick(String msg)
    {
        String nick = "";
        try
        {
            JSONObject jsonContent = new JSONObject(msg);
            nick = jsonContent.getString(NICK_KEY);
        }
        catch (JSONException e)
        {
            LogUtil.e(TAG, "getFromUserNick", "Parse bind json infos error: " + e);
        }
        return nick;
    }
    
    public static String getTag(String msg)
    {
        String tag = "";
        try
        {
            JSONObject jsonContent = new JSONObject(msg);
            tag = jsonContent.getString(TAG_KEY);
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
            LogUtil.e(TAG, "getTag", "Parse bind json infos error: " + e);
        }
        return tag;
    }
}
