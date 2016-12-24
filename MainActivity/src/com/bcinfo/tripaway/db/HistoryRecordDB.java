package com.bcinfo.tripaway.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bcinfo.tripaway.bean.ScheduleEvent;
import com.bcinfo.tripaway.db.SqliteDBHelper;

/**
 * 历史记录的数据库操作函数
 * 
 * @function
 * 
 * @author weichanghang
 * 
 * @version 1.0 2015年6月24日 14:00:23
 * 
 * 
 */
public class HistoryRecordDB
{
    private static final String RECENT_TABLE_NAME = "historyRecord";
    
    private final static String TAG = "historyRecord";
    
    private SQLiteDatabase mSDB = null;
    
    private static HistoryRecordDB historyRecordDB;
    
    private SqliteDBHelper dbHelper;
    
    private SQLiteDatabase sqliteDatabase;
    
    public static HistoryRecordDB getInstance()
    {
        if (historyRecordDB == null)
        {
            historyRecordDB = new HistoryRecordDB();
        }
        return historyRecordDB;
    }
    
    /**
     * 获取db
     * 
     * @return
     */
    SQLiteDatabase getDataBase()
    {
        if (sqliteDatabase == null)
        {
            sqliteDatabase = SqliteDBHelper.getHelper().getWritableDatabase();
        }
        return sqliteDatabase;
    }
    
    /**
     * 保存历史记录
     */
    public void saveRHistoryRecord(String destination)
    {
        String NumberTime = isRecordExist(destination);
        String currenttime = String.valueOf((new Date(System.currentTimeMillis())).getTime());
        try
        {
            if (NumberTime.equals("-1"))
            {
                String[] historyitem = new String[5];
                historyitem[0] = currenttime;
                historyitem[1] = "1";
                historyitem[2] = destination;
                historyitem[3] = "";
                historyitem[4] = "";
                getDataBase().execSQL("insert into " + RECENT_TABLE_NAME
                    + " (last_time,number_times,destination,userId,Priority) values(?,?,?,?,?)",
                    historyitem);
            }
            else
            {
                NumberTime = String.valueOf(1 + (Integer.valueOf(NumberTime)));
                
                getDataBase().execSQL("update " + RECENT_TABLE_NAME + " set number_times='" + NumberTime
                    + "', last_time='" + currenttime + "' where destination='" + destination + "'");
                
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        }
    }
    
    /**
     * 是否存在为destinaion的记录
     */
    private String isRecordExist(String destination)
    {
        
        ScheduleEvent eventData = null;
        String sql = "select number_times from " + RECENT_TABLE_NAME + " where destination = '" + destination + "'";
        String number = "-1";
        try
        {
            Cursor c = getDataBase().rawQuery(sql, null);
            if (c != null)
            {
                c.moveToFirst();
                number = c.getString(c.getColumnIndex("number_times"));
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            ;
        }
        return number;
    }
    
    /**
     * 得到历史记录，num为最多得到几个数据
     */
    public ArrayList<Map<String, String>> readRecord(int num)
    {
        
        ScheduleEvent eventData = null;
        String sql = "select destination from " + RECENT_TABLE_NAME + " ORDER BY last_time desc";
        ArrayList<Map<String, String>> historyrecord = new ArrayList<Map<String, String>>();
        try
        {
            Cursor c = getDataBase().rawQuery(sql, null);
            if (c != null)
            {
                int num1 = 0;
                c.moveToFirst();
                do
                {
                    Map<String, String> historyItem = new HashMap<String, String>();
                    historyItem.put("itemName", c.getString(c.getColumnIndex("destination")));
                    historyrecord.add(historyItem);
                    if (num < ++num1)
                    {
                        break;
                    }
                } while (c.moveToNext());
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            ;
        }
        return historyrecord;
    }
    
    /**
     * 删除历史记录
     */
    public void deleteRecord(String userId)
    {
        try
        {
            getDataBase().execSQL("delete from " + RECENT_TABLE_NAME + " where userId='" + userId + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        }
    }
}
