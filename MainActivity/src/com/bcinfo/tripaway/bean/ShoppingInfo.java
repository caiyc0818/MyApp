package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 上午9:21:48
 */
public class ShoppingInfo implements Parcelable
{
    /**
     * 购物别名
     */
    private String shoppingAlias;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 营业时间
     */
    private String businessHours;
    
    /**
     * 特色
     */
    private String feature;
    
    /*
     * url : "imageUrl1" desc : "图片描述1"
     */
    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
    
    public ShoppingInfo()
    {
        
    }
    
    public String getFeature()
    {
        return feature;
    }
    
    public void setFeature(String feature)
    {
        this.feature = feature;
    }
    
    public String getShoppingAlias()
    {
        return shoppingAlias;
    }
    
    public void setShoppingAlias(String shoppingAlias)
    {
        this.shoppingAlias = shoppingAlias;
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
    
    public static final Parcelable.Creator<ShoppingInfo> CREATOR = new Parcelable.Creator<ShoppingInfo>()
    {
        public ShoppingInfo createFromParcel(Parcel source)
        {
            return new ShoppingInfo(source);
        }
        
        public ShoppingInfo[] newArray(int size)
        {
            return new ShoppingInfo[size];
        }
    };
    
    public ShoppingInfo(Parcel in)
    {
        shoppingAlias = in.readString();
        address = in.readString();
        businessHours = in.readString();
        feature = in.readString();
        in.readTypedList(imageInfoList, ImageInfo.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(shoppingAlias);
        out.writeString(address);
        out.writeString(businessHours);
        out.writeString(feature);
        out.writeTypedList(imageInfoList);
    }
    
}
