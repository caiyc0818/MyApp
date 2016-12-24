package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class CarImage implements Parcelable
{
    private String name;// "carImage类目名1"
    
    /*
     * url : "imageUrl1" desc : "图片描述1"
     */
    private ArrayList<HashMap<String, String>> imgList = new ArrayList<HashMap<String, String>>();
    
    public CarImage()
    {
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public ArrayList<HashMap<String, String>> getImgList()
    {
        return imgList;
    }
    
    public void setImgList(ArrayList<HashMap<String, String>> imgList)
    {
        this.imgList = imgList;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<CarImage> CREATOR = new Parcelable.Creator<CarImage>()
    {
        public CarImage createFromParcel(Parcel source)
        {
            return new CarImage(source);
        }
        
        public CarImage[] newArray(int size)
        {
            return new CarImage[size];
        }
    };
    
    public CarImage(Parcel in)
    {
        name = in.readString();
        in.readArrayList(imgList.getClass().getClassLoader());
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(name);
        out.writeList(imgList);
    }
}