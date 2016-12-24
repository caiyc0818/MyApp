package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 下午6:55:38
 */
public class TrainInfo implements Parcelable
{
    /**
     * 始发站
     */
    private String startingPoint;
    
    /**
     * 目的站
     */
    private String destPoint;
    
    /**
     * 行驶里程
     */
    private String distance;
    
    /**
     * 耗时
     */
    private String elapsedTime;
    
    private String trainType;
    
    public TrainInfo()
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
    
    public String getDistance()
    {
        return distance;
    }
    
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    
    public String getElapsedTime()
    {
        return elapsedTime;
    }
    
    public void setElapsedTime(String elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }
    
    public String getTrainType()
    {
        return trainType;
    }
    
    public void setTrainType(String trainType)
    {
        this.trainType = trainType;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<TrainInfo> CREATOR = new Parcelable.Creator<TrainInfo>()
    {
        public TrainInfo createFromParcel(Parcel source)
        {
            return new TrainInfo(source);
        }
        
        public TrainInfo[] newArray(int size)
        {
            return new TrainInfo[size];
        }
    };
    
    public TrainInfo(Parcel in)
    {
        startingPoint = in.readString();
        destPoint = in.readString();
        distance = in.readString();
        elapsedTime = in.readString();
        trainType = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(startingPoint);
        out.writeString(destPoint);
        out.writeString(distance);
        out.writeString(elapsedTime);
        out.writeString(trainType);
    }
}
