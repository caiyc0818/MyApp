package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 详细行程
 * @author  
 *
 */
public class TripDetailInfo implements Parcelable
{
    /**
     * 第几天
     */
    private String index;
    /**
     * 游览计划标题
     */
    private String title;
    /**
     * 当天旅游路线计划
     */
    private ArrayList<DayTask> dayTaskList = new ArrayList<DayTask>();
    /**
     *提供设施列表
     */
    private ArrayList<String> deviceList = new ArrayList<String>();

    public TripDetailInfo()
    {
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public ArrayList<DayTask> getDayTaskList()
    {
        return dayTaskList;
    }

    public void setDayTaskList(ArrayList<DayTask> dayTaskList)
    {
        this.dayTaskList = dayTaskList;
    }

    public ArrayList<String> getDeviceList()
    {
        return deviceList;
    }

    public void setDeviceList(ArrayList<String> deviceList)
    {
        this.deviceList = deviceList;
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    public static final Parcelable.Creator<TripDetailInfo> CREATOR = new Parcelable.Creator<TripDetailInfo>()
    {
        public TripDetailInfo createFromParcel(Parcel source)
        {
            return new TripDetailInfo(source);
        }

        public TripDetailInfo[] newArray(int size)
        {
            return new TripDetailInfo[size];
        }
    };

    public TripDetailInfo(Parcel in)
    {
        index = in.readString();
        title = in.readString();
        in.readTypedList(dayTaskList, DayTask.CREATOR);
        in.readStringList(deviceList);
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(index);
        out.writeString(title);
        out.writeTypedList(dayTaskList);
        out.writeStringList(deviceList);
    }
}
