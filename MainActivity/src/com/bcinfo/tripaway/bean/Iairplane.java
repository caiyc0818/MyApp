package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-17 下午5:45:36
 */
public class Iairplane implements Parcelable
{
    /**
     * 座舱标准
     */
    private String standard;
    
    /**
     * 航空公司
     */
    private String airline;
    
    /**
     * 航班号
     */
    private String flightnumber;
    
    /**
     * 出发地
     */
    private String startingpoint;
    
    /**
     * 出发机场代码
     */
    private String startingPointAlias;
    
    /**
     * 目的地
     */
    private String descPoint;
    
    /**
     * 目的机场代码
     */
    private String descPointAlias;
    
    /**
     * 飞行里程
     */
    private String distance;
    
    /**
     * 起飞时间
     */
    private String arriveTime;
    
    /**
     * 到达时间
     */
    private String departureTime;
    
    /**
     * 
     * 航空公司logo
     */
    private String airLineLogo;
    
    public Iairplane()
    {
        
    }
    
    public String getAirLineLogo()
    {
        return airLineLogo;
    }
    
    public void setAirLineLogo(String airLineLogo)
    {
        this.airLineLogo = airLineLogo;
    }
    
    public String getStandard()
    {
        return standard;
    }
    
    public void setStandard(String standard)
    {
        this.standard = standard;
    }
    
    public String getAirline()
    {
        return airline;
    }
    
    public void setAirline(String airline)
    {
        this.airline = airline;
    }
    
    public String getFlightnumber()
    {
        return flightnumber;
    }
    
    public void setFlightnumber(String flightnumber)
    {
        this.flightnumber = flightnumber;
    }
    
    public String getStartingpoint()
    {
        return startingpoint;
    }
    
    public void setStartingpoint(String startingpoint)
    {
        this.startingpoint = startingpoint;
    }
    
    public String getStartingPointAlias()
    {
        return startingPointAlias;
    }
    
    public void setStartingPointAlias(String startingPointAlias)
    {
        this.startingPointAlias = startingPointAlias;
    }
    
    public String getDescPoint()
    {
        return descPoint;
    }
    
    public void setDescPoint(String descPoint)
    {
        this.descPoint = descPoint;
    }
    
    public String getDescPointAlias()
    {
        return descPointAlias;
    }
    
    public void setDescPointAlias(String descPointAlias)
    {
        this.descPointAlias = descPointAlias;
    }
    
    public String getDistance()
    {
        return distance;
    }
    
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    
    public String getArriveTime()
    {
        return arriveTime;
    }
    
    public void setArriveTime(String arriveTime)
    {
        this.arriveTime = arriveTime;
    }
    
    public String getDepartureTime()
    {
        return departureTime;
    }
    
    public void setDepartureTime(String departureTime)
    {
        this.departureTime = departureTime;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<Iairplane> CREATOR = new Parcelable.Creator<Iairplane>()
    {
        public Iairplane createFromParcel(Parcel source)
        {
            return new Iairplane(source);
        }
        
        public Iairplane[] newArray(int size)
        {
            return new Iairplane[size];
        }
    };
    
    public Iairplane(Parcel in)
    {
        standard = in.readString();
        airline = in.readString();
        flightnumber = in.readString();
        startingpoint = in.readString();
        startingPointAlias = in.readString();
        descPoint = in.readString();
        descPointAlias = in.readString();
        distance = in.readString();
        arriveTime = in.readString();
        departureTime = in.readString();
        airLineLogo = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(standard);
        out.writeString(airline);
        out.writeString(flightnumber);
        out.writeString(startingpoint);
        out.writeString(startingPointAlias);
        out.writeString(descPoint);
        out.writeString(descPointAlias);
        out.writeString(distance);
        out.writeString(arriveTime);
        out.writeString(departureTime);
        out.writeString(airLineLogo);
    }
}
