package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 结伴响应
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月26日 下午5:03:38
 */
public class Intention implements Parcelable
{
    private String content;// 内容
    
    private String status;// "wait"
    
    private String createTime;// "20150620120400"
    
    private UserInfo user = new UserInfo();
    
    public Intention()
    {
        
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public UserInfo getUser()
    {
        return user;
    }
    
    public void setUser(UserInfo user)
    {
        this.user = user;
    }
    
    public static final Parcelable.Creator<Intention> CREATOR = new Parcelable.Creator<Intention>()
    {
        public Intention createFromParcel(Parcel source)
        {
            return new Intention(source);
        }
        
        public Intention[] newArray(int size)
        {
            return new Intention[size];
        }
    };
    
    public Intention(Parcel in)
    {
        content = in.readString();
        status = in.readString();
        createTime = in.readString();
        user = in.readParcelable(UserInfo.class.getClassLoader());
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(content);
        out.writeString(status);
        out.writeString(createTime);
        out.writeParcelable(user, 0);
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
