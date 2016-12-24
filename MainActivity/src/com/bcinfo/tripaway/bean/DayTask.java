package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 旅游每天计划任务
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年4月23日 下午5:50:36
 */
public class DayTask implements Parcelable
{
    private String index;
    private String teskName;

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getTeskName()
    {
        return teskName;
    }

    public void setTeskName(String teskName)
    {
        this.teskName = teskName;
    }
    public  DayTask()
    {
        
    }
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    public static final Parcelable.Creator<DayTask> CREATOR = new Parcelable.Creator<DayTask>()
    {
        public DayTask createFromParcel(Parcel source)
        {
            return new DayTask(source);
        }

        public DayTask[] newArray(int size)
        {
            return new DayTask[size];
        }
    };

    public DayTask(Parcel in)
    {
        index = in.readString();
        teskName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(index);
        out.writeString(teskName);
    }
}
