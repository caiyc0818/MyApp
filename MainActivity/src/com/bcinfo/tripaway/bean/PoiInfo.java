package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-19 上午9:47:34
 */
public class PoiInfo implements Parcelable
{
    private String scenery_alias;
    
    private String address;
    
    private String businessHours;
    
    private String ticket;
    
    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
    
    public PoiInfo()
    {
        
    }
    
    public String getScenery_alias()
    {
        return scenery_alias;
    }
    
    public void setScenery_alias(String scenery_alias)
    {
        this.scenery_alias = scenery_alias;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getBusinessHours()
    {
        return businessHours;
    }
    
    public void setBusinessHours(String businessHours)
    {
        this.businessHours = businessHours;
    }
    
    public List<ImageInfo> getImageInfoList()
    {
        return imageInfoList;
    }
    
    public void setImageInfoList(List<ImageInfo> imageInfoList)
    {
        this.imageInfoList = imageInfoList;
    }
    
    public String getTicket()
    {
        return ticket;
    }
    
    public void setTicket(String ticket)
    {
        this.ticket = ticket;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<PoiInfo> CREATOR = new Parcelable.Creator<PoiInfo>()
    {
        public PoiInfo createFromParcel(Parcel source)
        {
            return new PoiInfo(source);
        }
        
        public PoiInfo[] newArray(int size)
        {
            return new PoiInfo[size];
        }
    };
    
    public PoiInfo(Parcel in)
    {
        scenery_alias = in.readString();
        address = in.readString();
        businessHours = in.readString();
        ticket = in.readString();
        in.readTypedList(imageInfoList, ImageInfo.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(scenery_alias);
        out.writeString(address);
        out.writeString(businessHours);
        out.writeString(ticket);
        out.writeTypedList(imageInfoList);
    }
}
