package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 上午9:31:12
 */
public class HouseInfo implements Parcelable
{
    /**
     * 国家
     */
    private String country;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 房屋类型
     */
    private String houseType;
    
    /**
     * 可住人数
     */
    private String holdnum;
    
    /**
     * 卧室数
     */
    private String unit;
    
    /**
     * 卫生间数
     */
    private String toilet;
    
    /**
     * 床位数
     */
    private String bed;
    
    /**
     * 床类型
     */
    private String bedType;
    
    /**
     * 随车服务信息
     */
    private String houseServ;
    
    /**
     * 服务类型信息
     */
    private List<CarServCategory> houseServs = new ArrayList<CarServCategory>();
    
    private List<ImageInfo> images = new ArrayList<ImageInfo>();
    
    private String checkTime;
    
    public HouseInfo()
    {
        
    }
    
    public List<ImageInfo> getImages()
    {
        return images;
    }
    
    public void setImages(List<ImageInfo> images)
    {
        this.images = images;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getHouseType()
    {
        return houseType;
    }
    
    public void setHouseType(String houseType)
    {
        this.houseType = houseType;
    }
    
    public String getHoldnum()
    {
        return holdnum;
    }
    
    public void setHoldnum(String holdnum)
    {
        this.holdnum = holdnum;
    }
    
    public String getUnit()
    {
        return unit;
    }
    
    public void setUnit(String unit)
    {
        this.unit = unit;
    }
    
    public String getToilet()
    {
        return toilet;
    }
    
    public void setToilet(String toilet)
    {
        this.toilet = toilet;
    }
    
    public String getBed()
    {
        return bed;
    }
    
    public void setBed(String bed)
    {
        this.bed = bed;
    }
    
    public String getBedType()
    {
        return bedType;
    }
    
    public void setBedType(String bedType)
    {
        this.bedType = bedType;
    }
    
    public String getHouseServ()
    {
        return houseServ;
    }
    
    public void setHouseServ(String houseServ)
    {
        this.houseServ = houseServ;
    }
    
    public List<CarServCategory> getHouseServs()
    {
        return houseServs;
    }
    
    public void setHouseServs(List<CarServCategory> houseServs)
    {
        this.houseServs = houseServs;
    }
    
    public String getCheckTime()
    {
        return checkTime;
    }
    
    public void setCheckTime(String checkTime)
    {
        this.checkTime = checkTime;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public final static Parcelable.Creator<HouseInfo> CREATOR = new Parcelable.Creator<HouseInfo>()
    {
        public HouseInfo createFromParcel(Parcel source)
        {
            return new HouseInfo(source);
        }
        
        public HouseInfo[] newArray(int size)
        {
            return new HouseInfo[size];
        }
    };
    
    public HouseInfo(Parcel in)
    {
        country = in.readString();
        address = in.readString();
        houseType = in.readString();
        holdnum = in.readString();
        unit = in.readString();
        toilet = in.readString();
        bed = in.readString();
        bedType = in.readString();
        houseServ = in.readString();
        checkTime = in.readString();
        in.readTypedList(images, ImageInfo.CREATOR);
        in.readTypedList(houseServs, CarServCategory.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(country);
        out.writeString(address);
        out.writeString(houseType);
        out.writeString(holdnum);
        out.writeString(unit);
        out.writeString(toilet);
        out.writeString(bed);
        out.writeString(bedType);
        out.writeString(houseServ);
        out.writeTypedList(houseServs);
        out.writeTypedList(images);
        out.writeString(checkTime);
    }
}
