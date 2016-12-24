package com.bcinfo.tripaway.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bcinfo.tripaway.bean.MessageInfo;

/**
 * @author hanweipeng
 * @date 2015-7-21 下午7:54:13
 */
public class MessageInfoDB
{
    private final static String TAG = "MessageInfoDB";
    
    private static MessageInfoDB messageInfoDB;
    
    private SqliteDBHelper dbHelper;
    
    private SQLiteDatabase sqliteDatabase = SqliteDBHelper.getHelper().getWritableDatabase();
    
    private String MESSAGE_TAB_NAME = "message";
    
    public static MessageInfoDB getInstance()
    {
        if (messageInfoDB == null)
        {
            messageInfoDB = new MessageInfoDB();
        }
        return messageInfoDB;
    }
    
    public synchronized void insertData(MessageInfo messageInfo)
    {
        MessageInfo oldMessageInfo = queryMessageById(messageInfo.getId());
        if (oldMessageInfo != null)
        {
            updateMessageInfo(messageInfo);
        }
        else
        {
            ContentValues contentValues = valuesFrom(messageInfo);
            try
            {
                long result = sqliteDatabase.insert(MESSAGE_TAB_NAME, null, contentValues);
                Log.e("insertData", "result-->" + result);
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
    
    public synchronized void updateMessageInfo(MessageInfo messageInfo)
    {
        if (null == messageInfo.getId() || 0 == messageInfo.getId().length())
        {
            return;
        }
        ContentValues values = valuesFrom(messageInfo);
        long result =
            sqliteDatabase.update(MESSAGE_TAB_NAME, values, "id" + " = ?", new String[] {messageInfo.getId()});
        Log.e("updateMessageInfo", "result-->" + result);
    }
    
    public synchronized MessageInfo queryMessageById(String id)
    {
        Cursor cursor =
            sqliteDatabase.rawQuery("select * from " + MESSAGE_TAB_NAME + " where " + "id" + " = ?", new String[] {id});
        MessageInfo messageInfo = null;
        if (null != cursor && cursor.moveToFirst())
        {
            messageInfo = readFrom(cursor);
        }
        cursor.close();
        // sqliteDatabase.close();
        return messageInfo;
    }
    
    public synchronized List<MessageInfo> queryMessageListByQueueId(String queueId)
    {
        List<MessageInfo> messageList = new ArrayList<MessageInfo>();
        Cursor cursor =
            sqliteDatabase.rawQuery("select * from " + MESSAGE_TAB_NAME + " where " + "queueId" + " = ?" + " order by "
                + "createTime" + " asc", new String[] {queueId});
        if (cursor.getCount() == 0)
        {
            return messageList;
        }
        if ((null != cursor) && cursor.moveToFirst())
        {
            do
            {
                MessageInfo messageInfo = readFrom(cursor);
                messageList.add(messageInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return messageList;
    }
    
    private ContentValues valuesFrom(MessageInfo messageInfo)
    {
        ContentValues values = new ContentValues();
        values.put("queueId", messageInfo.getQueueId());
        values.put("id", messageInfo.getId());
        values.put("title", messageInfo.getTitle());
        values.put("content", messageInfo.getContent());
        values.put("image", messageInfo.getImage());
        values.put("pattern", messageInfo.getPattern());
        values.put("createtime", messageInfo.getCreatetime());
        values.put("senderId", messageInfo.getSender().getUserId());
        values.put("senderAvatar", messageInfo.getSender().getAvatar());
        values.put("productTile", messageInfo.getProductInfo().getTitle());
        values.put("productId", messageInfo.getProductInfo().getId());
        values.put("productDays", messageInfo.getProductInfo().getDays());
        values.put("productDistance", messageInfo.getProductInfo().getDistance());
        values.put("productType", messageInfo.getProductInfo().getProductType());
        values.put("productServices", messageInfo.getProductInfo().getServices());
        if (messageInfo.getProductInfo().getTopics().size() > 0)
        {
            values.put("productTags",
                messageInfo.getProductInfo()
                    .getTopics()
                    .toString()
                    .substring(1, messageInfo.getProductInfo().getTopics().toString().length() - 1));
        }
        else
        {
            values.put("productTags", "");
        }
        return values;
    }
    
    private MessageInfo readFrom(Cursor cursor)
    {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
        messageInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        messageInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
        messageInfo.setImage(cursor.getString(cursor.getColumnIndex("image")));
        messageInfo.setPattern(cursor.getString(cursor.getColumnIndex("pattern")));
        messageInfo.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
        messageInfo.getSender().setUserId(cursor.getString(cursor.getColumnIndex("senderId")));
        messageInfo.getSender().setAvatar(cursor.getString(cursor.getColumnIndex("senderAvatar")));
        messageInfo.getProductInfo().setTitle(cursor.getString(cursor.getColumnIndex("productTile")));
        messageInfo.getProductInfo().setId(cursor.getString(cursor.getColumnIndex("productId")));
        messageInfo.getProductInfo().setDays(cursor.getString(cursor.getColumnIndex("productDays")));
        String productTagStr = cursor.getString(cursor.getColumnIndex("productTags"));
        if (productTagStr != null && productTagStr.length() > 0)
        {
            String[] productTag = productTagStr.split(",");
            List<String> productTagList = Arrays.asList(productTagStr);
            messageInfo.getProductInfo().setTopics(productTagList);
        }
        messageInfo.getProductInfo().setDistance(cursor.getString(cursor.getColumnIndex("productDistance")));
        messageInfo.getProductInfo().setProductType(cursor.getString(cursor.getColumnIndex("productType")));
        messageInfo.getProductInfo().setServices(cursor.getString(cursor.getColumnIndex("productServices")));
        return messageInfo;
    }
}
