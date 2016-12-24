package com.bcinfo.tripaway.getui.receiver;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.activity.ChatActivity;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.MyOrderDetailActivity;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

public class PushDemoReceiver extends BroadcastReceiver
{
    private static final String TAG = "PushDemoReceiver";
    
    public static final String RESPONSE = "response";
    
    public static final int NOTIFY_ID = 0x000;
    
    public static int mNewMessageNum = 0;// 通知栏新消息条目，我只是用了一个全局变量，
    
    public static ArrayList<EventHandler> ehList = new ArrayList<EventHandler>();
    
    public static abstract interface EventHandler
    {
        public abstract void onMessage(String message);
        
        public abstract void onNotify(String title, String content);
        
        public abstract void onNetChange(boolean isNetConnected);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        LogUtil.d(TAG, "onReceive", "action=" + bundle.getInt("action"));
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        
        switch (bundle.getInt(PushConsts.CMD_ACTION))
        {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                LogUtil.d(TAG, "onReceive", "第三方回执接口调用" + (result ? "成功" : "失败"));
                if (payload != null)
                {
                    String data = new String(payload);
                    Parse(data);// 预处理，过滤一些消息，比如说新人问候或自己发送的
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID) 第三方应用需要将CID上传到第三方服务器，以便日后通过用户帐号查找CID进行消息推送
                String clientid = bundle.getString("clientid");
                PreferenceUtil.setClientId(clientid);// 将ClientId保存到本地sharedPreferences.xml文件中
                LogUtil.d(TAG, "onReceive", "clientid=" + clientid);
                break;
            default:
                break;
        }
    }
    
    private void Parse(String message)
    {
        LogUtil.d(TAG, "message", message);
        JSONObject messageJson;
        try
        {
            messageJson = new JSONObject(message);
            if (messageJson != null && messageJson.length() > 0)
            {
                String msgId = messageJson.optString("msgId");
                String sender = messageJson.optString("sender");
                String msgType = messageJson.optString("msgType");
                String title = messageJson.optString("title");
                if(title==null||title.equals("null")||title.equals("NULL")){
                	title="远行";	
                }
                String content = messageJson.optString("content");
                if(StringUtils.isEmpty(content)){
                	content="";	
                }
                String target = messageJson.optString("target");
                String objectId = messageJson.optString("objectId");
                // 判断程序是否在前台运行
                if (AppInfo.isRunningForeground(TripawayApplication.application))
                {
                    // 在前台运行消息通知不提示 订单通知提示
                    if (!target.equals("0"))
                    {
                        // 我的旅程 通知栏提醒 ,跳转到订单详情页
                        showNotifyAppRun(msgId, msgType, title, content, target, objectId);
                    }
                    else
                    {
                        // 消息不提示 直接刷新界面
                        if (ehList.size() > 0)
                        {// 有监听的时候，传递下去
                            for (int i = 0; i < ehList.size(); i++)
                            {
                                ((EventHandler)ehList.get(i)).onMessage(message);
                            }
                        }
                    }
                }
                else
                // 不在前台运行 都在通知栏提示
                {
                    // 判断程序是否在运行
                    if (AppInfo.isAppRunning(TripawayApplication.application))
                    {
                        showNotifyAppRun(msgId, msgType, title, content, target, objectId);
                    }
                    else
                    {
                        showNotifyAppNoRun(msgId, msgType, title, content, target, objectId);
                    }
                }
                
            }
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private void showNotifyAppRun(String mesgId, String msgType, String title, String content, String target,
        String objectId)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(TripawayApplication.application);
        mBuilder.setTicker(title);
        mBuilder.setSmallIcon(R.drawable.logoapp);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        // 设置点击一次后消失（如果没有点击事件，则该方法无效。）
        mBuilder.setAutoCancel(true);
        // 使用TaskStackBuilder为“通知页面”设置返回关系
        // 点击通知之后需要跳转的页面
        Intent resultIntent = null;
        if (target.equals("10"))
        {
            resultIntent = new Intent(TripawayApplication.application, ContentActivity.class);
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("path", objectId);
            // 为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
        }else
        if (target.equals("2"))
        {
            resultIntent = new Intent(TripawayApplication.application, MyOrderDetailActivity.class);
            resultIntent.putExtra("orderId", objectId);
            // 为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
        }
        else
        if (target.equals("3"))
        {
            resultIntent = new Intent(TripawayApplication.application, GrouponProductNewDetailActivity.class);
            resultIntent.putExtra("productId", objectId);
            // 为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
        }
        else
        {
            resultIntent = new Intent(TripawayApplication.application, ChatActivity.class);
            resultIntent.putExtra("fromQueue", true);
            resultIntent.putExtra("queueId", objectId);
            resultIntent.putExtra("title", title);
            if (msgType.equals("01"))
            {
                resultIntent.putExtra("isTeam", true);
             
            }
            else
            {
                resultIntent.putExtra("isTeam", false);
            }
        }
        
        PendingIntent pIntent =
            PendingIntent.getActivity(TripawayApplication.application,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
        // mId allows you to update the notification later on.
        TripawayApplication.application.getNotificationManager().notify(0, mBuilder.build());
    }
    
    private void showNotifyAppNoRun(String mesgId, String msgType, String title, String content, String target,
        String objectId)
    {
        // TODO Auto-generated method stub
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(TripawayApplication.application);
        mBuilder.setTicker(title);
        mBuilder.setSmallIcon(R.drawable.logoapp);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        // 设置点击一次后消失（如果没有点击事件，则该方法无效。）
        mBuilder.setAutoCancel(true);
        // 使用TaskStackBuilder为“通知页面”设置返回关系
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(TripawayApplication.application);
        // 点击通知之后需要跳转的页面
        Intent resultIntent = null;
        if (target.equals("10"))
        {
            resultIntent = new Intent(TripawayApplication.application, ContentActivity.class);
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("path", objectId);
            resultIntent.putExtra("formLoading", true);
            stackBuilder.addParentStack(ContentActivity.class);
            // 为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
        }else
        if (target.equals("2"))
        {
            resultIntent = new Intent(TripawayApplication.application, MyOrderDetailActivity.class);
            // resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra("orderId", objectId);
            // 为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
            stackBuilder.addParentStack(MyOrderDetailActivity.class);
        }
        if (target.equals("3"))
        {
            resultIntent = new Intent(TripawayApplication.application, GrouponProductNewDetailActivity.class);
            resultIntent.putExtra("productId", objectId);
            resultIntent.putExtra("formLoading", true);
            // 为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
            stackBuilder.addParentStack(GrouponProductNewDetailActivity.class);
        }
        else
        {
            resultIntent = new Intent(TripawayApplication.application, ChatActivity.class);
            // resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra("fromQueue", true);
            resultIntent.putExtra("queueId", objectId);
            resultIntent.putExtra("title", title);
            if (msgType.equals("01"))
            {
                resultIntent.putExtra("isTeam", true);
            
            }
            else
            {
                resultIntent.putExtra("isTeam", false);
            }
            // 为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
            stackBuilder.addParentStack(ChatActivity.class);
        }
        
        stackBuilder.addNextIntent(resultIntent);
        
        PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
        // mId allows you to update the notification later on.
        TripawayApplication.application.getNotificationManager().notify(0, mBuilder.build());
    }
}
