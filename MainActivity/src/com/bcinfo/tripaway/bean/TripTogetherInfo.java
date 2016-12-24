package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.bcinfo.tripaway.bean.UserInfo;

/**
 * 结伴
 * 
 * @function
 * 
 * @author caihelin
 * @version 1.0 2015年5月15日 15:32:11
 * 
 */
public class TripTogetherInfo implements Parcelable
{
    /**
     * 结伴标识 id
     */
    private String togetherId;
    
    /**
     * 结伴标题
     */
    private String togetherTitle;
    
    /**
     * 结伴描述
     */
    private String togetherDescription;
    
    /**
     * 结伴出发地
     */
    private String togetherStartPoint;
    
    /**
     * 结伴目的地
     */
    private String togetherEndPoint;
    
    /**
     * 结伴计划时间
     */
    private String togetherPlanTime;
    
    /**
     * 结伴发起人
     */
    private UserInfo togetherSponsor = new UserInfo();
    
    public String getTogetherId()
    {
        return togetherId;
    }
    
    public void setTogetherId(String togetherId)
    {
        this.togetherId = togetherId;
    }
    
    public String getTogetherTitle()
    {
        return togetherTitle;
    }
    
    public void setTogetherTitle(String togetherTitle)
    {
        this.togetherTitle = togetherTitle;
    }
    
    public String getTogetherDescription()
    {
        return togetherDescription;
    }
    
    public void setTogetherDescription(String togetherDescription)
    {
        this.togetherDescription = togetherDescription;
    }
    
    public String getTogetherStartPoint()
    {
        return togetherStartPoint;
    }
    
    public void setTogetherStartPoint(String togetherStartPoint)
    {
        this.togetherStartPoint = togetherStartPoint;
    }
    
    public String getTogetherEndPoint()
    {
        return togetherEndPoint;
    }
    
    public void setTogetherEndPoint(String togetherEndPoint)
    {
        this.togetherEndPoint = togetherEndPoint;
    }
    
    public String getTogetherPlanTime()
    {
        return togetherPlanTime;
    }
    
    public void setTogetherPlanTime(String togetherPlanTime)
    {
        this.togetherPlanTime = togetherPlanTime;
    }
    
    public UserInfo getTogetherSponsor()
    {
        return togetherSponsor;
    }
    
    public void setTogetherSponsor(UserInfo togetherSponsor)
    {
        this.togetherSponsor = togetherSponsor;
    }
    
    public TripTogetherInfo()
    {
        super();
    }
    
    public TripTogetherInfo(Parcel in)
    {
        togetherId = in.readString();
        togetherTitle = in.readString();
        togetherDescription = in.readString();
        togetherStartPoint = in.readString();
        togetherEndPoint = in.readString();
        togetherPlanTime = in.readString();
        in.readValue(togetherSponsor.getClass().getClassLoader());
        
    }
    
    public static final Parcelable.Creator<TripTogetherInfo> CREATOR = new Parcelable.Creator<TripTogetherInfo>()
    {
        
        @Override
        public TripTogetherInfo createFromParcel(Parcel source)
        {
            
            return new TripTogetherInfo(source);
        }
        
        @Override
        public TripTogetherInfo[] newArray(int size)
        {
            
            return new TripTogetherInfo[size];
        }
    };
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(togetherId);
        dest.writeString(togetherTitle);
        dest.writeString(togetherDescription);
        dest.writeString(togetherStartPoint);
        dest.writeString(togetherEndPoint);
        dest.writeString(togetherPlanTime);
        dest.writeParcelable(togetherSponsor, 0);
        
    }
    
}
