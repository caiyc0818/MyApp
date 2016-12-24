package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 下午5:31:33
 */
public class ImageInfo implements Parcelable
{
    private String url;
    
    private String width;
    
    private String height;
    
    private String desc;
    
    public ImageInfo()
    {
        
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getWidth()
    {
        return width;
    }
    
    public void setWidth(String width)
    {
        this.width = width;
    }
    
    public String getHeight()
    {
        return height;
    }
    
    public void setHeight(String height)
    {
        this.height = height;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public final static Parcelable.Creator<ImageInfo> CREATOR = new Parcelable.Creator<ImageInfo>()
    {
        public ImageInfo createFromParcel(Parcel source)
        {
            return new ImageInfo(source);
        }
        
        public ImageInfo[] newArray(int size)
        {
            return new ImageInfo[size];
        }
    };
    
    public ImageInfo(Parcel in)
    {
        url = in.readString();
        width = in.readString();
        height = in.readString();
        desc = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(url);
        out.writeString(width);
        out.writeString(height);
        out.writeString(desc);
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
