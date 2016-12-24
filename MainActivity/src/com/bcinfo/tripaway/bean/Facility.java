package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 汽车设备
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月18日 下午1:53:26
 */
public class Facility implements Parcelable
{
    /**
     * 标识
     */
    private String id;
    
    /**
     * 属性
     */
    private String facilityName;// "车载wifi"
    
    /**
     * 别名
     */
    private String facilityCode;// "is_wifi"
    
    /**
     * 是否存在
     */
    private String status;// "1"
    
    public Facility()
    {
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getFacilityName()
    {
        return facilityName;
    }
    
    public void setFacilityName(String facilityName)
    {
        this.facilityName = facilityName;
    }
    
    public String getFacilityCode()
    {
        return facilityCode;
    }
    
    public void setFacilityCode(String facilityCode)
    {
        this.facilityCode = facilityCode;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public final static Parcelable.Creator<Facility> CREATOR = new Parcelable.Creator<Facility>()
    {
        public Facility createFromParcel(Parcel source)
        {
            return new Facility(source);
        }
        
        public Facility[] newArray(int size)
        {
            return new Facility[size];
        }
    };
    
    public Facility(Parcel in)
    {
        id = in.readString();
        facilityName = in.readString();
        facilityCode = in.readString();
        status = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(facilityName);
        out.writeString(facilityCode);
        out.writeString(status);
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
