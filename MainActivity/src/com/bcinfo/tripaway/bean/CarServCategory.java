package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-17 下午5:26:36
 */
public class CarServCategory implements Parcelable
{
    private String cateCode;
    
    private String name;
    
    private List<Facility> facilities = new ArrayList<Facility>();
    
    public CarServCategory()
    {
        
    }
    
    public String getCateCode()
    {
        return cateCode;
    }
    
    public void setCateCode(String cateCode)
    {
        this.cateCode = cateCode;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public List<Facility> getFacilities()
    {
        return facilities;
    }
    
    public void setFacilities(List<Facility> facilities)
    {
        this.facilities = facilities;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<CarServCategory> CREATOR = new Parcelable.Creator<CarServCategory>()
    {
        public CarServCategory createFromParcel(Parcel source)
        {
            return new CarServCategory(source);
        }
        
        public CarServCategory[] newArray(int size)
        {
            return new CarServCategory[size];
        }
    };
    
    public CarServCategory(Parcel in)
    {
        name = in.readString();
        cateCode = in.readString();
        in.readTypedList(facilities, Facility.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(name);
        out.writeString(cateCode);
        out.writeTypedList(facilities);
    }
    
}
