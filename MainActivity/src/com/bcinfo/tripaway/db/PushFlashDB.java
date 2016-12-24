package com.bcinfo.tripaway.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bcinfo.tripaway.bean.PushResource;

/**
 * @author hanweipeng
 * @date 2015-7-29 下午4:03:42
 */
public class PushFlashDB
{
    private final static String TAG = "PushFlashDB";
    
    private static PushFlashDB pushFlashDB;
    
    private SqliteDBHelper dbHelper;
    
    private SQLiteDatabase sqliteDatabase = SqliteDBHelper.getHelper().getWritableDatabase();
    
    private String PUSHFLASH_TAB_NAME = "pushResource";
    
    public static PushFlashDB getInstance()
    {
        if (pushFlashDB == null)
        {
            pushFlashDB = new PushFlashDB();
        }
        return pushFlashDB;
    }
    
    public synchronized void insertData(PushResource pushResource)
    {
        ContentValues contentValues = valuesFrom(pushResource);
        try
        {
            long result = sqliteDatabase.insert(PUSHFLASH_TAB_NAME, null, contentValues);
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
    
    public synchronized PushResource randomGetData()
    {
        PushResource pushResource = null;
        Cursor cursor = sqliteDatabase.rawQuery(" SELECT * FROM pushResource ORDER BY RANDOM() limit 1", null);
        if (null != cursor && cursor.moveToFirst())
        {
            pushResource = readFrom(cursor);
        }
        cursor.close();
        return pushResource;
    }
    
    private PushResource readFrom(Cursor cursor)
    {
        PushResource pushResource = new PushResource();
        pushResource.setId(cursor.getString(cursor.getColumnIndex("id")));
        pushResource.setResTitle(cursor.getString(cursor.getColumnIndex("resTitle")));
        pushResource.setSubTitle(cursor.getString(cursor.getColumnIndex("subTitle")));
        pushResource.setTitleImage(cursor.getString(cursor.getColumnIndex("titleImage")));
        pushResource.setObjectType(cursor.getString(cursor.getColumnIndex("objectType")));
        pushResource.setObjectId(cursor.getString(cursor.getColumnIndex("objectId")));
        pushResource.setObject(cursor.getString(cursor.getColumnIndex("object")));
        return pushResource;
    }
    
    private ContentValues valuesFrom(PushResource pushResource)
    {
        ContentValues values = new ContentValues();
        values.put("id", pushResource.getId());
        values.put("resTitle", pushResource.getResTitle());
        values.put("subTitle", pushResource.getSubTitle());
        values.put("titleImage", pushResource.getTitleImage());
        values.put("objectType", pushResource.getObjectType());
        values.put("objectId", pushResource.getObjectId());
        values.put("object", pushResource.getObject().toString());
        return values;
    }
    
    public void deleteAll()
    {
        sqliteDatabase.execSQL("drop table if exists " + PUSHFLASH_TAB_NAME);
        sqliteDatabase.execSQL("create table "
            + PUSHFLASH_TAB_NAME
            + " (id text primary key, resTitle text, subTitle text, titleImage text, objectType text, objectId text, object text)");
    }
}
