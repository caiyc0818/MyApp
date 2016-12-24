package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-27 上午11:31:16
 */
public class ServiceInfo implements Parcelable
{
    private String name;
    
    private String code;
    
    private String price;
    
    private String resource;
    
    public ServiceInfo()
    {
        
    }
    
    public String getResource()
    {
        return resource;
    }
    
    public void setResource(String resource)
    {
        this.resource = resource;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<ServiceInfo> CREATOR = new Parcelable.Creator<ServiceInfo>()
    {
        public ServiceInfo createFromParcel(Parcel source)
        {
            return new ServiceInfo(source);
        }
        
        public ServiceInfo[] newArray(int size)
        {
            return new ServiceInfo[size];
        }
    };
    
    public ServiceInfo(Parcel in)
    {
        price = in.readString();
        code = in.readString();
        name = in.readString();
        resource = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(price);
        out.writeString(code);
        out.writeString(name);
        out.writeString(resource);
    }
}
