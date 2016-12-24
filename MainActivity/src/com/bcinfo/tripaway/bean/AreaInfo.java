package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-7-17 下午6:09:25
 */
public class AreaInfo implements Parcelable
{
    private String areaId;
    
    private String areaName;
    
    private String areaLevel;
    
    private String areaPid;
    
    private String sortLetter;  //显示数据拼音的首字母  
    
    public AreaInfo()
    {
        
    }
    
    public String getAreaId()
    {
        return areaId;
    }
    
    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }
    
    public String getAreaName()
    {
        return areaName;
    }
    
    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }
    
    public String getAreaLevel()
    {
        return areaLevel;
    }
    
    public void setAreaLevel(String areaLevel)
    {
        this.areaLevel = areaLevel;
    }
    
    public String getAreaPid()
    {
        return areaPid;
    }
    
    public void setAreaPid(String areaPid)
    {
        this.areaPid = areaPid;
    }
    
    public String getSortLetter()
    {
        return sortLetter;
    }
    
    public void setSortLetter(String sortLetter)
    {
        this.sortLetter = sortLetter;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<AreaInfo> CREATOR = new Parcelable.Creator<AreaInfo>()
    {
        public AreaInfo createFromParcel(Parcel source)
        {
            return new AreaInfo(source);
        }
        
        public AreaInfo[] newArray(int size)
        {
            return new AreaInfo[size];
        }
    };
    
    public AreaInfo(Parcel in)
    {
        areaId = in.readString();
        areaName = in.readString();
        areaLevel = in.readString();
        areaPid = in.readString();
        sortLetter=in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(areaId);
        out.writeString(areaName);
        out.writeString(areaLevel);
        out.writeString(areaPid);
        out.writeString(sortLetter);
    }
}
