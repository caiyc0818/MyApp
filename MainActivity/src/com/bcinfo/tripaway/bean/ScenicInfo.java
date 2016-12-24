package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 景点信息
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月27日 下午5:58:24
 */
public class ScenicInfo implements Parcelable
{
    private String sceneryAlias;//"景点别名"
    private String address;// "景点地址"
    private String businessHours;// "每天8:30-16:30,午间不休"
    /*
     * url : "imageUrl1"
     * desc : "图片描述1"
     */
    private ArrayList<HashMap<String, String>> imageList = new ArrayList<HashMap<String, String>>();

    public ScenicInfo()
    {
    }

    public String getSceneryAlias()
    {
        return sceneryAlias;
    }

    public void setSceneryAlias(String sceneryAlias)
    {
        this.sceneryAlias = sceneryAlias;
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

    public ArrayList<HashMap<String, String>> getImageList()
    {
        return imageList;
    }

    public void setImageList(ArrayList<HashMap<String, String>> imageList)
    {
        this.imageList = imageList;
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    public static final Parcelable.Creator<ScenicInfo> CREATOR = new Parcelable.Creator<ScenicInfo>()
    {
        public ScenicInfo createFromParcel(Parcel source)
        {
            return new ScenicInfo(source);
        }

        public ScenicInfo[] newArray(int size)
        {
            return new ScenicInfo[size];
        }
    };

    public ScenicInfo(Parcel in)
    {
        sceneryAlias = in.readString();
        address = in.readString();
        businessHours = in.readString();
        in.readArrayList(imageList.getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(sceneryAlias);
        out.writeString(address);
        out.writeString(businessHours);
        out.writeList(imageList);
    }
}
