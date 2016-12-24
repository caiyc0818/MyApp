package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车辆信息
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月27日 下午4:18:39
 */
public class CarExt implements Parcelable
{
    private String carName;// 车名称
    
    private String license;// 车牌号
    
    private String distance;// 行驶里程 "30000"
    
    private String shoppingTime;// 购买时间 "20130601"
    
    private String seatNum;// 座位数
    
    private String carType;// 车型
    
    private String capacity; // "506"
    
    private String carPlace;// 产地
    
    private String capacityBig;
    
    private String capacitySmall;
    
    private String desc;
    
    private List<ImageInfo> carPics = new ArrayList<ImageInfo>();
    
    private List<CarImageCategory> carImages = new ArrayList<CarImageCategory>();
    
    private List<CarServCategory> carServers = new ArrayList<CarServCategory>();
    
    private List<String> facilities=new ArrayList<String>();
    
    public CarExt()
    {
    }
    
    public List<ImageInfo> getCarPics()
    {
        return carPics;
    }
    
    
    public List<String> getFacilities()
    {
        return facilities;
    }
    
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    
    public String getCapacityBig()
    {
        return capacityBig;
    }
    
    public void setCapacityBig(String capacityBig)
    {
        this.capacityBig = capacityBig;
    }
    
    
    public String getCapacitySmall()
    {
        return capacitySmall;
    }
    
    public void setCapacitySmall(String capacitySmall)
    {
        this.capacitySmall = capacitySmall;
    }
    
    public String getCarPlace()
    {
        return carPlace;
    }
    
    public void setCarPlace(String carPlace)
    {
        this.carPlace = carPlace;
    }
    
    public String getCarName()
    {
        return carName;
    }
    
    public void setCarName(String carName)
    {
        this.carName = carName;
    }
    
    public String getLicense()
    {
        return license;
    }
    
    public void setLicense(String license)
    {
        this.license = license;
    }
    
    public String getDistance()
    {
        return distance;
    }
    
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    
    public String getShoppingTime()
    {
        return shoppingTime;
    }
    
    public void setShoppingTime(String shoppingTime)
    {
        this.shoppingTime = shoppingTime;
    }
    
    public String getSeatNum()
    {
        return seatNum;
    }
    
    public void setSeatNum(String seatNum)
    {
        this.seatNum = seatNum;
    }
    
    public String getCarType()
    {
        return carType;
    }
    
    public void setCarType(String carType)
    {
        this.carType = carType;
    }
    
    public String getCapacity()
    {
        return capacity;
    }
    
    public void setCapacity(String capacity)
    {
        this.capacity = capacity;
    }
    
    public List<CarImageCategory> getCarImages()
    {
        return carImages;
    }
    
    public void setCarImages(List<CarImageCategory> carImages)
    {
        this.carImages = carImages;
    }
    
    public List<CarServCategory> getCarServers()
    {
        return carServers;
    }
    
    public void setCarServers(List<CarServCategory> carServers)
    {
        this.carServers = carServers;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<CarExt> CREATOR = new Parcelable.Creator<CarExt>()
    {
        public CarExt createFromParcel(Parcel source)
        {
            return new CarExt(source);
        }
        
        public CarExt[] newArray(int size)
        {
            return new CarExt[size];
        }
    };
    
    public CarExt(Parcel in)
    {
        carName = in.readString();
        license = in.readString();
        distance = in.readString();
        shoppingTime = in.readString();
        seatNum = in.readString();
        carType = in.readString();
        capacity = in.readString();
        carPlace = in.readString();
        capacitySmall=in.readString();
        in.readTypedList(carImages, CarImageCategory.CREATOR);
        in.readTypedList(carServers, CarServCategory.CREATOR);
        
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(carName);
        out.writeString(license);
        out.writeString(distance);
        out.writeString(shoppingTime);
        out.writeString(seatNum);
        out.writeString(carType);
        out.writeString(capacity);
        out.writeString(carPlace);
        out.writeTypedList(carImages);
        out.writeTypedList(carServers);
        out.writeString(capacitySmall);
        out.writeString(capacityBig);
    }
}
