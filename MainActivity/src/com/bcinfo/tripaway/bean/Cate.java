package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 美食
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月27日 下午3:48:09
 */
public class Cate implements Parcelable
{
    private String foodAlias; // "美食别名"
    
    private String feature;// "非常麻辣,带劲"
    
    private String priceLevel;// "MIDDLE"
    
    private String address;// "美食地址"
    
    private String businessHours;// "每天8:30-16:30,午间不休"
    
    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
    
    /*
     * url : "imageUrl1" desc : "图片描述1"
     */
    
    public Cate()
    {
    }
    
    public String getFoodAlias()
    {
        return foodAlias;
    }
    
    public void setFoodAlias(String foodAlias)
    {
        this.foodAlias = foodAlias;
    }
    
    public String getFeature()
    {
        return feature;
    }
    
    public void setFeature(String feature)
    {
        this.feature = feature;
    }
    
    public String getPriceLevel()
    {
        return priceLevel;
    }
    
    public void setPriceLevel(String priceLevel)
    {
        this.priceLevel = priceLevel;
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
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<Cate> CREATOR = new Parcelable.Creator<Cate>()
    {
        public Cate createFromParcel(Parcel source)
        {
            return new Cate(source);
        }
        
        public Cate[] newArray(int size)
        {
            return new Cate[size];
        }
    };
    
    public Cate(Parcel in)
    {
        foodAlias = in.readString();
        feature = in.readString();
        priceLevel = in.readString();
        address = in.readString();
        businessHours = in.readString();
        in.readTypedList(imageInfoList, ImageInfo.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(foodAlias);
        out.writeString(feature);
        out.writeString(priceLevel);
        out.writeString(address);
        out.writeString(businessHours);
        out.writeTypedList(imageInfoList);
    }
}
