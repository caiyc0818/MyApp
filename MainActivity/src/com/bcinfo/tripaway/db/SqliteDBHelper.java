/**
 * 文 件 名:  DBHelper.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  s00223583
 * 修改时间:  2013-7-23
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.bcinfo.tripaway.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.AreaInfo;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author s00223583
 * @version [版本号, 2013-7-23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SqliteDBHelper extends SQLiteOpenHelper
{
    /**
     * 日志标签
     */
    private static final String TAG = "SqliteDBHelper";
    
    private static final String DATABASE_NAME = "tripaway";
    
    private static final int DB_VERSION = 1;
    
    private SQLiteDatabase database = null;
    
    private String MESSAGE_TAB_NAME = "message";
    
    private String SCHEDULEEVENT_TAB_NAME = "scheduleEvent";
    
    private String USERINFO_TAB_NAME = "userinfo";
    
    private String HISTORYRECORD_TAB_NAME = "historyRecord";
    
    private String UPLOADPICTURESRECORD_TAB_NAME = "uploadPicturesRecord";
    
    private String QUEUES_TAB_NAME = "queues";
    
    private final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
        + "/area";
    
    private final String AREA_FILENAME = "area.db";
    
    private String databaseFilename = DATABASE_PATH + "/" + AREA_FILENAME;
    
    private String PUSHFLASH_TAB_NAME = "pushResource";
    
    /**
     * 用来操作的数据库的实例
     */
    private static SqliteDBHelper instance = null;
    
    private String createPushFlashDB =
        "create table "
            + PUSHFLASH_TAB_NAME
            + " (id text primary key, resTitle text, subTitle text, titleImage text, objectType text, objectId text, object text)";
    
    private String createMessageDB =
        "create table "
            + MESSAGE_TAB_NAME
            + " (id text primary key, queueId text, title text, content text, image text, pattern text, createtime text, productId text, productTile text, productTags text, productDays text, productDistance text, productType text, productServices text, senderId text, senderAvatar text)";
    
    private String createScheduleEventDB = "create table " + SCHEDULEEVENT_TAB_NAME
        + " (id text not null ,createTime text not null , date text not null ,beginDate text, endDate text,"
        + "color text not null,content text, isFinish text not null, remark text, notifyTime text)";
    
    private String createUserInfoDB =
        "create table "
            + USERINFO_TAB_NAME
            + " (id text not null, sex text, avatar text, nickName text, introduction text, status text, mobile text, email text, tags text, address text, realName text, usersIdentity text)";
    
    private String createHistoryRecordDB =
        "create table "
            + HISTORYRECORD_TAB_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, last_time TEXT, number_times TEXT, destination TEXT, userId TEXT, Priority TEXT)";
    
    private String createUploadPicturesRecordDB =
        "create table "
            + UPLOADPICTURESRECORD_TAB_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, last_time TEXT, number_times TEXT, image_old_uri TEXT,image_uri TEXT, image_name TEXT, userId TEXT)";
    
    private String createQueuesListDB =
        "create table "
            + QUEUES_TAB_NAME
            + " (userId text not null, queueId text not null, avatars text, senderName text, content text, createTime text, unread text, type text, image text, title text, queueLogo text)";
    
    public SqliteDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }
    
    /**
     * 数据库操作
     * 
     * @return instance
     */
    public static SqliteDBHelper getHelper()
    {
        if (instance == null)
        {
            instance = new SqliteDBHelper(TripawayApplication.application);
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(createMessageDB);
        // db.execSQL(createScheduleEventDB);
        db.execSQL(createUserInfoDB);
        db.execSQL(createQueuesListDB);
        db.execSQL(createPushFlashDB);
        createAreaDB();
        // db.execSQL(createHistoryRecordDB);
        // db.execSQL(createUploadPicturesRecordDB);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion < newVersion)
        {
            db.execSQL("drop table if exists " + MESSAGE_TAB_NAME);
            db.execSQL("drop table if exists " + USERINFO_TAB_NAME);
            db.execSQL("drop table if exists " + QUEUES_TAB_NAME);
            db.execSQL("drop table if exists " + PUSHFLASH_TAB_NAME);
            // db.execSQL("drop table if exists " + SCHEDULEEVENT_TAB_NAME);
            // db.execSQL(createMessageDB);
            createAreaDB();
            db.execSQL(createUserInfoDB);
            db.execSQL(createQueuesListDB);
            db.execSQL(createMessageDB);
            db.execSQL(createPushFlashDB);
        }
    }
    
    public ArrayList<AreaInfo> getAreaListByPid(String areaPid)
    {
        ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();
        // 打开/sdcard/dictionary目录中的dictionary.db文件
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        if (database == null)
        {
            createAreaDB();
            database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        }
        Cursor cursor =
            database.rawQuery("select * from " + "area" + " where " + "pid" + " = ?", new String[] {areaPid});
        if (cursor.getCount() == 0)
        {
            return areaList;
        }
        if ((null != cursor) && cursor.moveToFirst())
        {
            do
            {
                AreaInfo areaInfo = readFrom(cursor);
                areaList.add(areaInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return areaList;
    }
    
    public ArrayList<AreaInfo> getAreaList()
    {
        ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();
        // 打开/sdcard/dictionary目录中的dictionary.db文件
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        if (database == null)
        {
            createAreaDB();
            database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        }
        Cursor cursor =
            database.rawQuery("select * from " + "area where 1=", new String[] {"1"});
        if (cursor.getCount() == 0)
        {
            return areaList;
        }
        if ((null != cursor) && cursor.moveToFirst())
        {
            do
            {
                AreaInfo areaInfo = readFrom(cursor);
                areaList.add(areaInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return areaList;
    }
    
    public ArrayList<AreaInfo> getAreaListByPid(String areaPid,String exAreapid)
    {
        ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();
        // 打开/sdcard/dictionary目录中的dictionary.db文件
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        if (database == null)
        {
            createAreaDB();
            database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        }
        Cursor cursor =
            database.rawQuery("select * from " + "area" + " where " + "pid" + " = ? and pid <> ?", new String[] {areaPid,exAreapid});
        if (cursor.getCount() == 0)
        {
            return areaList;
        }
        if ((null != cursor) && cursor.moveToFirst())
        {
            do
            {
                AreaInfo areaInfo = readFrom(cursor);
                areaList.add(areaInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return areaList;
    }
    
    public AreaInfo getAreaInfoByName(String areaName)
    {
        AreaInfo areaInfo = new AreaInfo();
        // 打开/sdcard/dictionary目录中的dictionary.db文件
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        if (database == null)
        {
            createAreaDB();
            database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        }
        Cursor cursor =
            database.rawQuery("select * from " + "area" + " where " + "areaName" + " = ?", new String[] {areaName});
        if (null != cursor && cursor.moveToFirst())
        {
            areaInfo = readFrom(cursor);
        }
        cursor.close();
        return areaInfo;
    }
    
    private AreaInfo readFrom(Cursor cursor)
    {
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setAreaId(cursor.getString(cursor.getColumnIndex("areaId")));
        areaInfo.setAreaLevel(cursor.getString(cursor.getColumnIndex("level")));
        areaInfo.setAreaName(cursor.getString(cursor.getColumnIndex("areaName")));
        areaInfo.setAreaPid(cursor.getString(cursor.getColumnIndex("pid")));
        return areaInfo;
    }
    
    private void createAreaDB()
    {
        try
        {
            File dir = new File(DATABASE_PATH);
            // 如果/sdcard目录中存在，创建这个目录
            if (!dir.exists())
                dir.mkdir();
            // 如果在/sdcard目录中不存在
            // area.db文件，则从res\assest目录中复制这个文件到
            // SD卡的目录（/sdcard/）
            if (!(new File(databaseFilename)).exists())
            {
                // 获得封装dictionary.db文件的InputStream对象
                InputStream is = TripawayApplication.application.getAssets().open("area.db");
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                // 开始复制dictionary.db文件
                while ((count = is.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, count);
                }
                
                fos.close();
                is.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 模糊查询
     * @param areaNm
     * @return
     */
    public ArrayList<AreaInfo> getAreaListByName(String areaNm)
    {
        ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();
        // 打开/sdcard/dictionary目录中的dictionary.db文件
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        if (database == null)
        {
            createAreaDB();
            database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        }
        Cursor cursor =
            database.rawQuery("select * from " + "area" + " where " + "areaName" + " like '%"+areaNm+"%'", null);
        if (cursor.getCount() == 0)
        {
            return areaList;
        }
        if ((null != cursor) && cursor.moveToFirst())
        {
            do
            {
                AreaInfo areaInfo = readFrom(cursor);
                areaList.add(areaInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return areaList;
    }
}
