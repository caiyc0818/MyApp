package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-17 下午5:15:28
 */
public class CarImageCategory implements Parcelable
{
    private String name;
    
    private String cateCode;
    
    private List<ImageInfo> carImages = new ArrayList<ImageInfo>();
    
    public CarImageCategory()
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
    
    public String getCateCode()
    {
        return cateCode;
    }
    
    public void setCateCode(String cateCode)
    {
        this.cateCode = cateCode;
    }
    
    public List<ImageInfo> getCarImageList()
    {
        return carImages;
    }
    
    public void setCarImageList(List<ImageInfo> carImages)
    {
        this.carImages = carImages;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<CarImageCategory> CREATOR = new Parcelable.Creator<CarImageCategory>()
    {
        public CarImageCategory createFromParcel(Parcel source)
        {
            return new CarImageCategory(source);
        }
        
        public CarImageCategory[] newArray(int size)
        {
            return new CarImageCategory[size];
        }
    };
    
    public CarImageCategory(Parcel in)
    {
        name = in.readString();
        cateCode = in.readString();
        in.readTypedList(carImages, ImageInfo.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(name);
        out.writeString(cateCode);
        out.writeTypedList(carImages);
    }
}
