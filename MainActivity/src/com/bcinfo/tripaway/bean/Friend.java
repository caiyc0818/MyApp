package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 好友
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月29日 14:00:12
 * 
 */
public class Friend implements Parcelable
{
    /**
     * 好友 id
     */
    private String userId;
    
    /**
     * 好友昵称
     */
    private String nickName;
    
    /**
     * 好友性别
     */
    private String gender;
    
    /**
     * 好友头像
     */
    private String avatar;
    
    /**
     * 好友所在的地区
     */
    private String area;
    
    /**
     * 好友状态 (单向关注 双向关注)
     */
    private String status;
    
    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public String getNickName()
    {
        return nickName;
    }
    
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    
    public String getGender()
    {
        return gender;
    }
    
    public void setGender(String gender)
    {
        this.gender = gender;
    }
    
    public String getAvatar()
    {
        return avatar;
    }
    
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }
    
    public String getArea()
    {
        return area;
    }
    
    public void setArea(String area)
    {
        this.area = area;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public Friend()
    {
        super();
    }
    
    public Friend(Parcel in)
    {
        userId = in.readString();
        nickName = in.readString();
        gender = in.readString();
        avatar = in.readString();
        area = in.readString();
        status = in.readString();
    }
    
    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>()
    {
        
        @Override
        public Friend createFromParcel(Parcel source)
        {
            
            return new Friend(source);
        }
        
        @Override
        public Friend[] newArray(int size)
        {
            
            return new Friend[size];
        }
    };
    
    @Override
    public int describeContents()
    {
        
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flag)
    {
        dest.writeString(userId);
        dest.writeString(nickName);
        dest.writeString(gender);
        dest.writeString(avatar);
        dest.writeString(area);
        dest.writeString(status);
    }
    
}
