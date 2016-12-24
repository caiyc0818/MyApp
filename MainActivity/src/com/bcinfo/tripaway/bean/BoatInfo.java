package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 下午7:11:56
 */
public class BoatInfo implements Parcelable
{
    /**
     * 轮船类型
     */
    private String boatType;
    
    /**
     * boatdetail
     */
    private String boatdetail;
    
    private String startingPoint;
    
    private String destPoint;
    
    private String elapsedTime;
    
    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
    
    public BoatInfo()
    {
        
    }
    
    public String getBoatType()
    {
        return boatType;
    }
    
    public void setBoatType(String boatType)
    {
        this.boatType = boatType;
    }
    
    public String getBoatdetail()
    {
        return boatdetail;
    }
    
    public void setBoatdetail(String boatdetail)
    {
        this.boatdetail = boatdetail;
    }
    
    public List<ImageInfo> getImageInfoList()
    {
        return imageInfoList;
    }
    
    public void setImageInfoList(List<ImageInfo> imageInfoList)
    {
        this.imageInfoList = imageInfoList;
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
    
    public static final Parcelable.Creator<BoatInfo> CREATOR = new Parcelable.Creator<BoatInfo>()
    {
        public BoatInfo createFromParcel(Parcel source)
        {
            return new BoatInfo(source);
        }
        
        public BoatInfo[] newArray(int size)
        {
            return new BoatInfo[size];
        }
    };
    
    public BoatInfo(Parcel in)
    {
        boatType = in.readString();
        boatdetail = in.readString();
        startingPoint = in.readString();
        destPoint = in.readString();
        elapsedTime = in.readString();
        in.readTypedList(imageInfoList, ImageInfo.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(boatType);
        out.writeString(boatdetail);
        out.writeString(startingPoint);
        out.writeString(destPoint);
        out.writeString(elapsedTime);
        out.writeTypedList(imageInfoList);
    }
    
}
