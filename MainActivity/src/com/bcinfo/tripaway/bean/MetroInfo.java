package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 下午7:48:15
 */
public class MetroInfo implements Parcelable
{
    /**
     * 始发站
     */
    private String startingPoint;
    
    /**
     * 目的站
     */
    private String destPoint;
    
    private String elapsedTime;
    
    public MetroInfo()
    {
        
    }
    
    public String getStartingPoint()
    {
        return startingPoint;
    }
    
    public void setStartingPoint(String startingPoint)
    {
        this.startingPoint = startingPoint;
    }
    
    public String getDestPoint()
    {
        return destPoint;
    }
    
    public void setDestPoint(String destPoint)
    {
        this.destPoint = destPoint;
    }
    
    public String getElapsedTime()
    {
        return elapsedTime;
    }
    
    public void setElapsedTime(String elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<MetroInfo> CREATOR = new Parcelable.Creator<MetroInfo>()
    {
        public MetroInfo createFromParcel(Parcel source)
        {
            return new MetroInfo(source);
        }
        
        public MetroInfo[] newArray(int size)
        {
            return new MetroInfo[size];
        }
    };
    
    public MetroInfo(Parcel in)
    {
        startingPoint = in.readString();
        destPoint = in.readString();
        elapsedTime = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(startingPoint);
        out.writeString(destPoint);
        out.writeString(elapsedTime);
    }
    
}
