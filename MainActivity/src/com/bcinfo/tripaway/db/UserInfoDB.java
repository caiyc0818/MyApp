package com.bcinfo.tripaway.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bcinfo.tripaway.bean.UserInfo;

/**
 * @author hanweipeng
 * @date 2015-6-24 下午4:48:24
 */
public class UserInfoDB
{
    private final static String TAG = "UserInfoDB";
    
    private SQLiteDatabase mSDB = null;
    
    private static UserInfoDB userInfoDB;
    
    private SqliteDBHelper dbHelper;
    
    private SQLiteDatabase sqliteDatabase;
    
    private String USERINFO_TAB_NAME = "userinfo";
    
    public static UserInfoDB getInstance()
    {
        if (userInfoDB == null)
        {
            userInfoDB = new UserInfoDB();
        }
        return userInfoDB;
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
    
    public synchronized void insertData(UserInfo userInfo)
    {
        UserInfo oldInfo = queryUserInfoById(userInfo.getUserId());
        if (null != oldInfo)
        {
            updateUserinfo(userInfo);
        }
        else
        {
            ContentValues contentValues = valuesFrom(userInfo);
            try
            {
                long result = getDataBase().insert(USERINFO_TAB_NAME, null, contentValues);
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
    
    public synchronized void updateUserinfo(UserInfo userInfo)
    {
        if (null == userInfo.getUserId() || 0 == userInfo.getUserId().length())
        {
            return;
        }
        ContentValues values = valuesFrom(userInfo);
        getDataBase().update(USERINFO_TAB_NAME, values, "id" + " = ?", new String[] {userInfo.getUserId()});
    }
    
    private UserInfo readFrom(Cursor cursor)
    {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(cursor.getString(cursor.getColumnIndex("id")));
        userInfo.setGender(cursor.getString(cursor.getColumnIndex("sex")));
        userInfo.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
        userInfo.setNickname(cursor.getString(cursor.getColumnIndex("nickName")));
        userInfo.setIntroduction(cursor.getString(cursor.getColumnIndex("introduction")));
        userInfo.setStatus(cursor.getString(cursor.getColumnIndex("status")));
        userInfo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
        userInfo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        userInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        userInfo.setRealName(cursor.getString(cursor.getColumnIndex("realName")));
        userInfo.setUsersIdentity(cursor.getString(cursor.getColumnIndex("usersIdentity")));
        return userInfo;
    }
    
    public synchronized UserInfo queryUserInfoById(String id)
    {
        Cursor cursor =
            getDataBase().rawQuery("select * from " + USERINFO_TAB_NAME + " where " + "id" + " = ?",
                new String[] {"" + id});
        UserInfo userInfo = null;
        if (null != cursor && cursor.moveToFirst())
        {
            userInfo = readFrom(cursor);
        }
        cursor.close();
        // sqliteDatabase.close();
        return userInfo;
    }
    
    private ContentValues valuesFrom(UserInfo userInfo)
    {
        ContentValues values = new ContentValues();
        values.put("id", userInfo.getUserId());
        values.put("sex", userInfo.getGender());
        values.put("avatar", userInfo.getAvatar());
        values.put("nickName", userInfo.getNickname());
        values.put("introduction", userInfo.getIntroduction());
        values.put("status", userInfo.getStatus());
        values.put("mobile", userInfo.getMobile());
        values.put("email", userInfo.getEmail());
        values.put("tags", "");
        values.put("address", userInfo.getAddress());
        values.put("realName", userInfo.getRealName());
        values.put("usersIdentity", userInfo.getUsersIdentity());
        return values;
    }
    
    public void deleteAll()
    {
        getDataBase().execSQL("drop table if exists " + USERINFO_TAB_NAME);
        // db.execSQL("drop table if exists " + SCHEDULEEVENT_TAB_NAME);
        // db.execSQL(createMessageDB);
        getDataBase().execSQL("create table "
            + USERINFO_TAB_NAME
            + " (id text not null, sex text, avatar text, nickName text, introduction text, status text, mobile text, email text, tags text, address text, realName text, usersIdentity text)");
    }
    
}
