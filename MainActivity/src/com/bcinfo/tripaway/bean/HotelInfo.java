package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 上午9:57:55
 */
public class HotelInfo implements Parcelable
{
    private String bedType;
    
    private String holdNum;
    
    private String grogshopAlias;
    
    private String address;
    
    private String businessHours;
    
    private String starLevel;
    
    private String checkTime;
    
    private List<ImageInfo> images = new ArrayList<ImageInfo>();
    
    /**
     * 服务类型信息
     */
    private List<CarServCategory> hotelServs = new ArrayList<CarServCategory>();
    
    public HotelInfo()
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
    
    public String getBedType()
    {
        return bedType;
    }
    
    public void setBedType(String bedType)
    {
        this.bedType = bedType;
    }
    
    public String getHoldNum()
    {
        return holdNum;
    }
    
    public void setHoldNum(String holdNum)
    {
        this.holdNum = holdNum;
    }
    
    public String getGrogshopAlias()
    {
        return grogshopAlias;
    }
    
    public void setGrogshopAlias(String grogshopAlias)
    {
        this.grogshopAlias = grogshopAlias;
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
    
    // public ArrayList<HashMap<String, String>> getImgList()
    // {
    // return imgList;
    // }
    //
    // public void setImgList(ArrayList<HashMap<String, String>> imgList)
    // {
    // this.imgList = imgList;
    // }
    
    public List<CarServCategory> getHouseServs()
    {
        return hotelServs;
    }
    
    public void setHouseServs(List<CarServCategory> houseServs)
    {
        this.hotelServs = houseServs;
    }
    
    public String getStarLevel()
    {
        return starLevel;
    }
    
    public void setStarLevel(String starLevel)
    {
        this.starLevel = starLevel;
    }
    
    public String getCheckTime()
    {
        return checkTime;
    }
    
    public void setCheckTime(String checkTime)
    {
        this.checkTime = checkTime;
    }
    
    public List<CarServCategory> getHotelServs()
    {
        return hotelServs;
    }
    
    public void setHotelServs(List<CarServCategory> hotelServs)
    {
        this.hotelServs = hotelServs;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public final static Parcelable.Creator<HotelInfo> CREATOR = new Parcelable.Creator<HotelInfo>()
    {
        public HotelInfo createFromParcel(Parcel source)
        {
            return new HotelInfo(source);
        }
        
        public HotelInfo[] newArray(int size)
        {
            return new HotelInfo[size];
        }
    };
    
    public HotelInfo(Parcel in)
    {
        bedType = in.readString();
        holdNum = in.readString();
        grogshopAlias = in.readString();
        address = in.readString();
        businessHours = in.readString();
        starLevel = in.readString();
        checkTime = in.readString();
        in.readTypedList(images, ImageInfo.CREATOR);
        in.readTypedList(hotelServs, CarServCategory.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(bedType);
        out.writeString(holdNum);
        out.writeString(grogshopAlias);
        out.writeString(address);
        out.writeString(businessHours);
        out.writeString(checkTime);
        out.writeString(starLevel);
        out.writeTypedList(hotelServs);
        out.writeTypedList(images);
    }
}
