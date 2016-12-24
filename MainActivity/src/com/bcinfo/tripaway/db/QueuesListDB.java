package com.bcinfo.tripaway.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bcinfo.tripaway.bean.QueuesInfo;
import com.bcinfo.tripaway.utils.PreferenceUtil;

/**
 * @author hanweipeng
 * @date 2015-7-17 上午10:03:47
 */
public class QueuesListDB
{
    private final static String TAG = "QueuesListDB";
    
    private static QueuesListDB queuesListDB;
    
    private SqliteDBHelper dbHelper;
    
    private SQLiteDatabase sqliteDatabase = SqliteDBHelper.getHelper().getWritableDatabase();
    
    private String QUEUES_TAB_NAME = "queues";
    
    public static QueuesListDB getInstance()
    {
        if (queuesListDB == null)
        {
            queuesListDB = new QueuesListDB();
        }
        return queuesListDB;
    }
    
    public synchronized void insertData(QueuesInfo queuesInfo)
    {
        QueuesInfo oldQueuesInfo = queryQueuesInfoById(queuesInfo.getQueueId());
        if (oldQueuesInfo != null)
        {
            updateQueuesInfo(queuesInfo);
        }
        else
        {
            ContentValues contentValues = valuesFrom(queuesInfo);
            try
            {
                long result = sqliteDatabase.insert(QUEUES_TAB_NAME, null, contentValues);
            }
            catch (Exception e)
            {
                Log.e(TAG, "insert fail,please try again");
            }
            finally
            {
                if (null != sqliteDatabase && sqliteDatabase.isOpen())
                {
                    // sqliteDatabase.close();
                }
            }
        }
    }
    
    public synchronized void updateQueuesInfo(QueuesInfo queuesInfo)
    {
        if (null == queuesInfo.getQueueId() || 0 == queuesInfo.getQueueId().length())
        {
            return;
        }
        ContentValues values = valuesFrom(queuesInfo);
        long result =
            sqliteDatabase.update(QUEUES_TAB_NAME, values, "queueId" + " = ?", new String[] {queuesInfo.getQueueId()});
    }
    
    public synchronized QueuesInfo queryQueuesInfoById(String queueId)
    {
        Cursor cursor =
            sqliteDatabase.rawQuery("select * from " + QUEUES_TAB_NAME + " where " + "queueId" + " = ?",
                new String[] {queueId});
        QueuesInfo queuesInfo = null;
        if (null != cursor && cursor.moveToFirst())
        {
            queuesInfo = readFrom(cursor);
        }
        cursor.close();
        // sqliteDatabase.close();
        return queuesInfo;
    }
    
    public synchronized List<QueuesInfo> queryAllQueuesInfoByUserId(String userId)
    {
        List<QueuesInfo> queuesInfoList = new ArrayList<QueuesInfo>();
        Cursor cursor =
            sqliteDatabase.rawQuery("select * from " + QUEUES_TAB_NAME + " where " + "userId" + " = ?" + " order by "
                + "createTime" + " desc", new String[] {userId});
        if (cursor.getCount() == 0)
        {
            return queuesInfoList;
        }
        if ((null != cursor) && cursor.moveToFirst())
        {
            do
            {
                QueuesInfo queuesInfo = readFrom(cursor);
                queuesInfoList.add(queuesInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return queuesInfoList;
    }
    
    private ContentValues valuesFrom(QueuesInfo queuesInfo)
    {
        ContentValues values = new ContentValues();
        values.put("userId", PreferenceUtil.getUserId());
        values.put("queueId", queuesInfo.getQueueId());
        if (queuesInfo.getAvatarsList().size() > 0)
        {
            values.put("avatars",
                queuesInfo.getAvatarsList()
                    .toString()
                    .substring(1, queuesInfo.getAvatarsList().toString().length() - 1));
        }
        else
        {
            values.put("avatars", "");
        }
        values.put("senderName", queuesInfo.getMessageInfo().getSender().getNickname());
        values.put("content", queuesInfo.getMessageInfo().getContent());
        values.put("createTime", queuesInfo.getMessageInfo().getCreatetime());
        values.put("unread", queuesInfo.getUnread());
        values.put("type", queuesInfo.getType());
        values.put("image", queuesInfo.getMessageInfo().getImage());
        values.put("title", queuesInfo.getQueueTitle());
        values.put("queueLogo", queuesInfo.getQueueLogo());
        return values;
    }
    
    private QueuesInfo readFrom(Cursor cursor)
    {
        QueuesInfo queuesInfo = new QueuesInfo();
        queuesInfo.setQueueId(cursor.getString(cursor.getColumnIndex("queueId")));
        String avatarStr = cursor.getString(cursor.getColumnIndex("avatars"));
        if (avatarStr != null && avatarStr.length() > 0)
        {
            String[] avatars = avatarStr.split(",");
            List<String> avatarsList = Arrays.asList(avatars);
            queuesInfo.setAvatarsList(avatarsList);
        }
        queuesInfo.getMessageInfo().getSender().setNickname(cursor.getString(cursor.getColumnIndex("senderName")));
        queuesInfo.getMessageInfo().setContent(cursor.getString(cursor.getColumnIndex("content")));
        queuesInfo.getMessageInfo().setCreatetime(cursor.getString(cursor.getColumnIndex("createTime")));
        queuesInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
        queuesInfo.setUnread(cursor.getString(cursor.getColumnIndex("unread")));
        queuesInfo.getMessageInfo().setImage(cursor.getString(cursor.getColumnIndex("image")));
        queuesInfo.setQueueTitle(cursor.getString(cursor.getColumnIndex("title")));
        queuesInfo.setQueueLogo(cursor.getString(cursor.getColumnIndex("queueLogo")));
        return queuesInfo;
    }
    
    public void deleteQueuesById(String queueId)
    {
        sqliteDatabase.delete(QUEUES_TAB_NAME, "queueId = ?", new String[] {"" + queueId});
    }
    
    public void deleteAll()
    {
        sqliteDatabase.execSQL("drop table if exists " + QUEUES_TAB_NAME);
        // db.execSQL("drop table if exists " + SCHEDULEEVENT_TAB_NAME);
        // db.execSQL(createMessageDB);
        sqliteDatabase.execSQL("create table "
            + QUEUES_TAB_NAME
            + " (queueId text not null, avatars text, senderName text, content text, createTime text, unread text, type text)");
    }
}
