package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 上午9:46:19
 */
public class HouseServ implements Parcelable
{
    private String name;// "类目名"
    
    private String code;
    
    private ArrayList<Facility> facilityList = new ArrayList<Facility>();
    
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
    
    public ArrayList<Facility> getFacilityList()
    {
        return facilityList;
    }
    
    public void setFacilityList(ArrayList<Facility> facilityList)
    {
        this.facilityList = facilityList;
    }
    
    public final static Parcelable.Creator<HouseServ> CREATOR = new Parcelable.Creator<HouseServ>()
    {
        public HouseServ createFromParcel(Parcel source)
        {
            return new HouseServ(source);
        }
        
        public HouseServ[] newArray(int size)
        {
            return new HouseServ[size];
        }
    };
    
    public HouseServ(Parcel in)
    {
        name = in.readString();
        code = in.readString();
        in.readTypedList(facilityList, Facility.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(name);
        out.writeString(code);
        out.writeTypedList(facilityList);
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
