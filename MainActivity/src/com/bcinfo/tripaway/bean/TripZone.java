package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.bcinfo.tripaway.bean.UserInfo;

/**
 * 圈子item
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月15日 17:03:22
 * 
 */
public class TripZone implements Parcelable
{
    /**
     * 圈子item的类型 tripstory 微游记; product 产品; together 结伴
     */
    private String objectType;
    
    /**
     * 圈子item对象
     */
    private Object object = new Object();
    
    /**
     * 圈子 是否点赞 0:未赞;1:已赞
     */
    private String isLike;
    
    /**
     * 点赞数
     */
    private String likes;
    
    /**
     * 发布时间
     */
    private String publishTime;
    
    /**
     * 位置
     */
    private String location;
    
    /**
     * 发布者信息
     */
    private UserInfo publisher = new UserInfo();
    
    public String getObjectType()
    {
        return objectType;
    }
    
    public void setObjectType(String objectType)
    {
        this.objectType = objectType;
    }
    
    public Object getObject()
    {
        return object;
    }
    
    public void setObject(Object object)
    {
        this.object = object;
    }
    
    public String getIsLike()
    {
        return isLike;
    }
    
    public void setIsLike(String isLike)
    {
        this.isLike = isLike;
    }
    
    public String getLikes()
    {
        return likes;
    }
    
    public void setLikes(String likes)
    {
        this.likes = likes;
    }
    
    public String getPublishTime()
    {
        return publishTime;
    }
    
    public void setPublishTime(String publishTime)
    {
        this.publishTime = publishTime;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public TripZone()
    {
        super();
        
    }
    
    public UserInfo getPublisher()
    {
        return publisher;
    }
    
    public void setPublisher(UserInfo publisher)
    {
        this.publisher = publisher;
    }
    
    public TripZone(Parcel in)
    {
        objectType = in.readString();
        in.readValue(object.getClass().getClassLoader());
        isLike = in.readString();
        likes = in.readString();
        publishTime = in.readString();
        location = in.readString();
        in.readValue(publisher.getClass().getClassLoader());
    }
    
    public static final Parcelable.Creator<TripZone> CREATOR = new Parcelable.Creator<TripZone>()
    {
        
        @Override
        public TripZone createFromParcel(Parcel source)
        {
            
            return new TripZone(source);
        }
        
        @Override
        public TripZone[] newArray(int size)
        {
            
            return new TripZone[size];
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
        dest.writeString(objectType);
        dest.writeValue(object);
        dest.writeString(isLike);
        dest.writeString(likes);
        dest.writeString(publishTime);
        dest.writeString(location);
        dest.writeValue(publisher);
    }
    
}
