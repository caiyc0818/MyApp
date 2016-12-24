package com.bcinfo.tripaway.db;

import java.util.ArrayList;

import com.bcinfo.tripaway.bean.ScheduleEvent;
import com.bcinfo.tripaway.utils.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * 日程添加事件数据库
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月10日 下午4:13:17
 */
public class ScheduleEventDatabase extends SQLiteOpenHelper
{
    private final static String TAG = "ScheduleEventDatabase";
    public static final String DATABASE_NAME = "EventDatabase";// 数据库name
    private static int version = 1;// database 版本号
    private final String TABLE_NAME = "scheduleEvent";// 数据库表名
    private SQLiteDatabase mSDB = getReadableDatabase();//获得可以读取的数据库对象
    
    private static ScheduleEventDatabase mScheduleEventDatabase;// SqliteOpenHelper子类
    
    public static ScheduleEventDatabase getInstance(Context context)
    {
        if (mScheduleEventDatabase == null)
        {
            mScheduleEventDatabase = new ScheduleEventDatabase(context);// 创建sqliteOpenHelper子类对象
           
        }
        return mScheduleEventDatabase;
    }

    ScheduleEventDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, version);
    }
    /*
     *  当数据库第一次被创建的时候  会回调oncreate()方法
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String carFriendDtcSql = "create table " + TABLE_NAME
                + " (id text not null ,createTime text not null , date text not null ,beginDate text, endDate text,"
                + "color text not null,content text, isFinish text not null, remark text, notifyTime text)";
        db.execSQL(carFriendDtcSql);// 执行create语句
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String sql = "drop table if exits " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);// 重新调用创建新表
    }

    /**
     * 插入一条事件
     * 
     * @param event
     */
    public void insertEvent(ScheduleEvent event)
    {
        // insert or replace into 语句表示 如果指定的数据表中存在指定主键的记录  那么就修改已经存在的行记录 相等于 update;如果不存在指定主键的行记录   那么久insert 插入新的行记录
        String sql = "INSERT OR REPLACE into " + TABLE_NAME
                + "(id ,createTime,date, beginDate,endDate,color, content,isFinish,remark,notifyTime)"
                + " values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = new Object[]
        { event.getId(), event.getCreateTime(), event.getDate(), event.getBeginDate(), event.getEndDate(), event.getColor(),
                event.getContent(), event.getIsFinish(), event.getRemark(), event.getNotifyTime() };
        mSDB.execSQL(sql, params);// 执行insert 插入表数据的命令
    }

    /**
     * 插入一系列事件
     * 
     * @param event
     */
    public void insertEventList(ArrayList<ScheduleEvent> eventList)
    {
        if (eventList == null)
        {
            return;
        }
        LogUtil.i(TAG, "insertEventList", "eventList.size=" + eventList.size());
        try
        {
            String sql = "INSERT  INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?)";
            mSDB.beginTransaction();// 开始事务
            SQLiteStatement stmt = mSDB.compileStatement(sql);// 编译  指定的sql语句  等同于execSql(String)语句
            for (int i = 0; i < eventList.size(); i++)
            {
                ScheduleEvent event = eventList.get(i);
                stmt.clearBindings();//清除所有的已经存在的绑定
                stmt.bindString(1, event.getId());// 将id绑定到index为1的位置上 
                stmt.bindString(2, event.getCreateTime());// 同上
                stmt.bindString(3, event.getDate());
                stmt.bindString(4, event.getBeginDate());
                stmt.bindString(5, event.getEndDate());
                stmt.bindString(6, event.getColor());
                stmt.bindString(7, event.getContent());
                stmt.bindString(8, event.getIsFinish());
                stmt.bindString(9, event.getRemark());
                stmt.bindString(10, event.getNotifyTime());
                stmt.executeInsert();// 执行插入insert语句
                
            }
            mSDB.setTransactionSuccessful();// 设置事务成功
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        finally
        {
            mSDB.endTransaction();// 结束事务
        }
    }

    /**
     * 更新一个事件
     * 
     * @param event
     */
    public void updateEvent(ScheduleEvent event)
    {
        ContentValues cv = new ContentValues();
        cv.put("createTime", event.getCreateTime());
        cv.put("date", event.getDate());
        cv.put("beginDate", event.getBeginDate());
        cv.put("endDate", event.getEndDate());
        cv.put("color", event.getColor());
        cv.put("content", event.getContent());
        cv.put("isFinish", event.getIsFinish());
        cv.put("remark", event.getRemark());
        cv.put("notifyTime", event.getNotifyTime());
        String[] args =
        { String.valueOf(event.getId()) };
        mSDB.update(TABLE_NAME, cv, "id=?", args);
    }

    /**
     * 更新一系列事件
     * 
     * @param event
     */
    public void updateEventList(ArrayList<ScheduleEvent> eventList)
    {
        if (eventList == null)
        {
            return;
        }
        LogUtil.i(TAG, "updateEventList", "eventList.size=" + eventList.size());
        try
        {
            mSDB.beginTransaction();
            for (int i = 0; i < eventList.size(); i++)// for循环 迭代需要update 更新的对象
            {
                ScheduleEvent event = eventList.get(i);
                ContentValues cv = new ContentValues();
                cv.put("createTime", event.getCreateTime());
                cv.put("date", event.getDate());
                cv.put("beginDate", event.getBeginDate());
                cv.put("endDate", event.getEndDate());
                cv.put("color", event.getColor());
                cv.put("content", event.getContent());
                cv.put("isFinish", event.getIsFinish());
                cv.put("remark", event.getRemark());
                cv.put("notifyTime", event.getNotifyTime());
                String[] args =
                { String.valueOf(event.getId()) };
                mSDB.update(TABLE_NAME, cv, "id=?", args);
            }
            mSDB.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        finally
        {
            mSDB.endTransaction();
        }
    }

    /**
     * 根据ID查询事件
     * 
     * @param id
     * @return
     */
    public ScheduleEvent queryEventById(String id)
    {
        LogUtil.i(TAG, "queryEventById", "id=" + id);
        ScheduleEvent eventData = null;
        String sql = "select * from " + TABLE_NAME + " where id = ? ";//根据主键查询
        String[] params = new String[]
        { id };
        Cursor c = mSDB.rawQuery(sql, params);
         
        while (c.moveToNext())
        {
            eventData = new ScheduleEvent();
            eventData.setId(c.getString(c.getColumnIndex("id")));
            eventData.setCreateTime(c.getString(c.getColumnIndex("createTime")));
            eventData.setDate(c.getString(c.getColumnIndex("date")));
            eventData.setBeginDate(c.getString(c.getColumnIndex("beginDate")));
            eventData.setEndDate(c.getString(c.getColumnIndex("endDate")));
            eventData.setColor(c.getString(c.getColumnIndex("color")));
            eventData.setContent(c.getString(c.getColumnIndex("content")));
            eventData.setIsFinish(c.getString(c.getColumnIndex("isFinish")));
            eventData.setRemark(c.getString(c.getColumnIndex("remark")));
            eventData.setNotifyTime(c.getString(c.getColumnIndex("notifyTime")));
        }
        if (c != null)
        {
            c.close();// 关闭 cursor
        }
        // LogUtil.i(TAG, "queryEventById", "eventData=" + eventData);
        return eventData;
    }

    /**
     * 根据日期查询事件
     * 
     * @param date
     * @return
     */
    public ArrayList<ScheduleEvent> queryEventByDate(String date)
    {
        LogUtil.i(TAG, "queryEventByDate", "date=" + date);
        ArrayList<ScheduleEvent> eventList = new ArrayList<ScheduleEvent>();
        String sql = "select * from " + TABLE_NAME + " where date = ? ";
        String[] params = new String[]
        { date };
        Cursor c = mSDB.rawQuery(sql, params);// 查询
        while (c.moveToNext())
        {
            ScheduleEvent eventData = new ScheduleEvent();
            eventData.setId(c.getString(c.getColumnIndex("id")));
            eventData.setCreateTime(c.getString(c.getColumnIndex("createTime")));
            eventData.setDate(c.getString(c.getColumnIndex("date")));
            eventData.setBeginDate(c.getString(c.getColumnIndex("beginDate")));
            eventData.setEndDate(c.getString(c.getColumnIndex("endDate")));
            eventData.setColor(c.getString(c.getColumnIndex("color")));
            eventData.setContent(c.getString(c.getColumnIndex("content")));
            eventData.setIsFinish(c.getString(c.getColumnIndex("isFinish")));
            eventData.setRemark(c.getString(c.getColumnIndex("remark")));
            eventData.setNotifyTime(c.getString(c.getColumnIndex("notifyTime")));
            eventList.add(eventData);
        }
        if (c != null)
        {
            c.close();
        }
        return eventList;
    }

    /**
     * 根据创建时间查询事件
     * 
     * @param createTime
     * @return
     */
    public ArrayList<ScheduleEvent> queryEventByCreateTime(String createTime)
    {
        LogUtil.i(TAG, "queryEventByCreateTime", "createTime=" + createTime);
        ArrayList<ScheduleEvent> eventList = new ArrayList<ScheduleEvent>();
        String sql = "select * from " + TABLE_NAME + " where createTime = ? ";
        String[] params = new String[]
        { createTime };
        Cursor c = mSDB.rawQuery(sql, params);
        while (c.moveToNext())
        {
            ScheduleEvent eventData = new ScheduleEvent();
            eventData.setId(c.getString(c.getColumnIndex("id")));
            eventData.setCreateTime(c.getString(c.getColumnIndex("createTime")));
            eventData.setDate(c.getString(c.getColumnIndex("date")));
            eventData.setBeginDate(c.getString(c.getColumnIndex("beginDate")));
            eventData.setEndDate(c.getString(c.getColumnIndex("endDate")));
            eventData.setColor(c.getString(c.getColumnIndex("color")));
            eventData.setContent(c.getString(c.getColumnIndex("content")));
            eventData.setIsFinish(c.getString(c.getColumnIndex("isFinish")));
            eventData.setRemark(c.getString(c.getColumnIndex("remark")));
            eventData.setNotifyTime(c.getString(c.getColumnIndex("notifyTime")));
            eventList.add(eventData);
        }
        if (c != null)
        {
            c.close();
        }
        return eventList;
    }
    /*
     * 查询所有的事件
     */
    public ArrayList<ScheduleEvent> queryAllEvent()
    {
        ArrayList<ScheduleEvent> data = new ArrayList<ScheduleEvent>();
        String sql = "select * from " + TABLE_NAME;
        Cursor c = mSDB.rawQuery(sql, null);
        LogUtil.i(TAG, "queryAllEvent()", "c=" + c);
        if (c != null)
        {
            while (c.moveToNext())
            {
                ScheduleEvent eventData = new ScheduleEvent();
                eventData.setId(c.getString(c.getColumnIndex("id")));
                eventData.setCreateTime(c.getString(c.getColumnIndex("createTime")));
                eventData.setDate(c.getString(c.getColumnIndex("date")));
                eventData.setBeginDate(c.getString(c.getColumnIndex("beginDate")));
                eventData.setEndDate(c.getString(c.getColumnIndex("endDate")));
                eventData.setColor(c.getString(c.getColumnIndex("color")));
                eventData.setContent(c.getString(c.getColumnIndex("content")));
                eventData.setIsFinish(c.getString(c.getColumnIndex("isFinish")));
                eventData.setRemark(c.getString(c.getColumnIndex("remark")));
                eventData.setNotifyTime(c.getString(c.getColumnIndex("notifyTime")));
                data.add(eventData);
                LogUtil.i(TAG, "queryAllEvent()", "id=" + c.getString(c.getColumnIndex("id")));
            }
            if (c != null)
            {
                c.close();
            }
        }
        return data;
    }

    /**
     * 删除一个事件
     * 
     * @param event
     */
    public void delectEventByCreateTime(String createTime)
    {
        String[] params = new String[]
        { createTime };
        mSDB.delete(TABLE_NAME, "createTime=?", params);
    }

    public void deleteAppTempTraffic()
    {
        String sql = "delete from " + TABLE_NAME;
        mSDB.execSQL(sql);// 执行删除语句   delete
    }
}
