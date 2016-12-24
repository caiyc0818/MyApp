package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-23 上午10:51:15
 */
public class TopicInfo implements Parcelable
{
    private String id;// 主题标识
    
    private String title;// 名称
    
    private String description;// 描述
    
    private String background;// 背景图
    
    public TopicInfo()
    {
        
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getBackground()
    {
        if (background.contains("http://"))
        {
            return background;
        }
        else
        {
            return background;
        }
    }
    
    public void setBackground(String background)
    {
        this.background = background;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<TopicInfo> CREATOR = new Parcelable.Creator<TopicInfo>()
    {
        public TopicInfo createFromParcel(Parcel source)
        {
            return new TopicInfo(source);
        }
        
        public TopicInfo[] newArray(int size)
        {
            return new TopicInfo[size];
        }
    };
    
    public TopicInfo(Parcel in)
    {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        background = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(title);
        out.writeString(description);
        out.writeString(background);
    }
    
}
