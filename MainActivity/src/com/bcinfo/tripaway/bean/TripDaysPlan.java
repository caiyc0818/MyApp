package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每天行程计划
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年4月22日 上午10:20:32
 */
public class TripDaysPlan implements Parcelable
{
    /**
     * 次序
     */
    private String index;
    /**
     * 标题
     */
    private String title;
    /**
     * 标签列表
     */
    private ArrayList<String> labels = new ArrayList<String>();
    /**
     * 照片
     */
    private String photoUrl;
    /**
     * 介绍
     */
    private String description;
    /**
     * 价格
     */
    private String price;
    /**
     * 地址
     */
    private String address;
    /**
     * 营业时间
     */
    private String time;
    /**
     * 更多描述
     * key: 图片
     * value: 图片描述
     */
    private ArrayList<HashMap<String, String>> introduceList = new ArrayList<HashMap<String, String>>();

    public TripDaysPlan()
    {
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public ArrayList<String> getLabels()
    {
        return labels;
    }

    public void setLabels(ArrayList<String> labels)
    {
        this.labels = labels;
    }

    public String getPhotoUrl()
    {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public ArrayList<HashMap<String, String>> getIntroduceList()
    {
        return introduceList;
    }

    public void setIntroduceList(ArrayList<HashMap<String, String>> introduceList)
    {
        this.introduceList = introduceList;
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    public static final Parcelable.Creator<TripDaysPlan> CREATOR = new Parcelable.Creator<TripDaysPlan>()
    {
        public TripDaysPlan createFromParcel(Parcel source)
        {
            return new TripDaysPlan(source);
        }

        public TripDaysPlan[] newArray(int size)
        {
            return new TripDaysPlan[size];
        }
    };

    public TripDaysPlan(Parcel in)
    {
        index = in.readString();
        title = in.readString();
        in.readStringList(labels);
        photoUrl = in.readString();
        description = in.readString();
        price = in.readString();
        address = in.readString();
        time = in.readString();
        in.readList(introduceList, introduceList.getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(index);
        out.writeString(title);
        out.writeStringList(labels);
        out.writeString(photoUrl);
        out.writeString(description);
        out.writeString(price);
        out.writeString(address);
        out.writeString(time);
        out.writeList(introduceList);
    }
}
