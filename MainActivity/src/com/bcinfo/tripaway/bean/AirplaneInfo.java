package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 下午8:24:25
 */
public class AirplaneInfo implements Parcelable
{
    /**
     * 飞机类型
     */
    private String planeType;
    
    /**
     * 飞机详情
     */
    private String planeDetail;
    
    /**
     * 出发机场代码
     */
    private String startingPointAlias;
    
    /**
     * 起飞时间
     */
    private String arriveTime;
    
    /**
     * 目的机场代码
     */
    private String descPointAlias;
    
    /**
     * 目的地
     */
    private String descPoint;
    
    /**
     * 到达时间
     */
    private String departureTime;
    
    /**
     * 出发地
     */
    private String startingPoint;
    
    private String airline;
    
    private String elapsedTime;
    
    public AirplaneInfo()
    {
        
    }
    
    public String getAirline()
    {
        return airline;
    }
    
    public void setAirline(String airline)
    {
        this.airline = airline;
    }
    
    public String getPlaneType()
    {
        return planeType;
    }
    
    public void setPlaneType(String planeType)
    {
        this.planeType = planeType;
    }
    
    public String getPlaneDetail()
    {
        return planeDetail;
    }
    
    public void setPlaneDetail(String planeDetail)
    {
        this.planeDetail = planeDetail;
    }
    
    public String getStartingPointAlias()
    {
        return startingPointAlias;
    }
    
    public void setStartingPointAlias(String startingPointAlias)
    {
        this.startingPointAlias = startingPointAlias;
    }
    
    public String getArriveTime()
    {
        return arriveTime;
    }
    
    public void setArriveTime(String arriveTime)
    {
        this.arriveTime = arriveTime;
    }
    
    public String getDescPointAlias()
    {
        return descPointAlias;
    }
    
    public void setDescPointAlias(String descPointAlias)
    {
        this.descPointAlias = descPointAlias;
    }
    
    public String getDescPoint()
    {
        return descPoint;
    }
    
    public void setDescPoint(String descPoint)
    {
        this.descPoint = descPoint;
    }
    
    public String getDepartureTime()
    {
        return departureTime;
    }
    
    public void setDepartureTime(String departureTime)
    {
        this.departureTime = departureTime;
    }
    
    public String getStartingPoint()
    {
        return startingPoint;
    }
    
    public void setStartingPoint(String startingPoint)
    {
        this.startingPoint = startingPoint;
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
    
    public static final Parcelable.Creator<AirplaneInfo> CREATOR = new Parcelable.Creator<AirplaneInfo>()
    {
        public AirplaneInfo createFromParcel(Parcel source)
        {
            return new AirplaneInfo(source);
        }
        
        public AirplaneInfo[] newArray(int size)
        {
            return new AirplaneInfo[size];
        }
    };
    
    public AirplaneInfo(Parcel in)
    {
        planeType = in.readString();
        planeDetail = in.readString();
        startingPointAlias = in.readString();
        arriveTime = in.readString();
        descPointAlias = in.readString();
        descPoint = in.readString();
        departureTime = in.readString();
        startingPoint = in.readString();
        airline = in.readString();
        elapsedTime = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(planeType);
        out.writeString(planeDetail);
        out.writeString(startingPointAlias);
        out.writeString(arriveTime);
        out.writeString(descPointAlias);
        out.writeString(descPoint);
        out.writeString(departureTime);
        out.writeString(startingPoint);
        out.writeString(airline);
        out.writeString(elapsedTime);
    }
    
}
