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
 * 上传图片的数据库操作函数
 * 
 * @function
 * 
 * @author weichanghang
 * 
 * @version 1.0 2015年6月24日 14:00:23
 * 
 * 
 */
public class UploadPicturesRecordDB
{
    private static final String RECENT_TABLE_NAME = "uploadPicturesRecord";
    
    private final static String TAG = "uploadPicturesRecord";
    
    private SQLiteDatabase mSDB = null;
    
    private static UploadPicturesRecordDB uploadPicturesRecord;
    
    private SqliteDBHelper dbHelper;
    
    private SQLiteDatabase sqliteDatabase;
    
    public static UploadPicturesRecordDB getInstance()
    {
        if (uploadPicturesRecord == null)
        {
            uploadPicturesRecord = new UploadPicturesRecordDB();
        }
        return uploadPicturesRecord;
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
     * 保存上传记录
     * 
     * @imageOldUri 原图片的uri
     * @imageUri 新图片的uri
     * @isFirstTime 若能知道是否上传过，则true，不知道就传false
     */
    public boolean saveloadPicturesRecord(String imageOldUri, String imageUri, boolean isFirstTime)
    {
        String NumberTime = "-1";
        if (!isFirstTime)
        {
            NumberTime = isRecordExist(imageOldUri);
        }
        String currenttime = String.valueOf((new Date(System.currentTimeMillis())).getTime());
        try
        {
            if (NumberTime.equals("-1"))
            {
                String[] historyitem = new String[6];
                historyitem[0] = currenttime;
                historyitem[1] = "1";
                historyitem[2] = imageOldUri;
                historyitem[3] = imageUri;
                historyitem[4] = "";
                historyitem[5] = "";
                getDataBase().execSQL("insert into " + RECENT_TABLE_NAME
                    + " (last_time,number_times,image_old_uri,image_uri,image_name,userId) values(?,?,?,?,?,?)",
                    historyitem);
                return true;
            }
            else
            {
                NumberTime = String.valueOf(1 + (Integer.valueOf(NumberTime)));
                
                getDataBase().execSQL("update " + RECENT_TABLE_NAME + " set number_times='" + NumberTime
                    + "', last_time='" + currenttime + "' where image_old_uri='" + imageOldUri + "'");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        }
        return false;
    }
    
    /**
     * 是否存在为imageUri的记录
     */
    public String isRecordExist(String imageOldUri)
    {
        
        String sql = "select number_times from " + RECENT_TABLE_NAME + " where image_old_uri = '" + imageOldUri + "'";
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
     * 通过userid得到记录，num为最多得到几个数据
     */
    public ArrayList<Map<String, String>> readRecord(String userid, int num)
    {
        
        ScheduleEvent eventData = null;
        String sql =
            "select image_uri from " + RECENT_TABLE_NAME + "where userId = '" + userid + "'ORDER BY last_time desc";
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
                    historyItem.put("itemName", c.getString(c.getColumnIndex("image_uri")));
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
     * 通过imageOldUri得到imageUri
     */
    public String readimageuriRecord(String imageOldUri)
    {
        
        String sql = "select image_uri from " + RECENT_TABLE_NAME + " where image_old_uri = '" + imageOldUri + "'";
        String imageuri = null;
        try
        {
            Cursor c = getDataBase().rawQuery(sql, null);
            if (c != null)
            {
                int num1 = 0;
                c.moveToFirst();
                do
                {
                    imageuri = c.getString(c.getColumnIndex("image_uri"));
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
        return imageuri;
    }
    
    /**
     * 删除上传记录
     */
    public void deleteRecord(String imageUri)
    {
        try
        {
            getDataBase().execSQL("delete from " + RECENT_TABLE_NAME + " where image_uri='" + imageUri + "'");
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
