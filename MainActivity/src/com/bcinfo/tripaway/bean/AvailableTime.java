package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-17 下午4:05:31
 */
public class AvailableTime implements Parcelable
{
    
    /**
     * 开始时间
     */
    private String beginDate;
    
    /**
     * 结束时间
     */
    private String endDate;
    
    /**
     * 价格
     */
    private String price;
    
    public AvailableTime()
    {
        
    }
    
    public String getBeginDate()
    {
        return beginDate;
    }
    
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate.replaceAll(" ", "");
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate.replaceAll(" ", "");
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<AvailableTime> CREATOR = new Parcelable.Creator<AvailableTime>()
    {
        public AvailableTime createFromParcel(Parcel source)
        {
            return new AvailableTime(source);
        }
        
        public AvailableTime[] newArray(int size)
        {
            return new AvailableTime[size];
        }
    };
    
    public AvailableTime(Parcel in)
    {
        beginDate = in.readString();
        endDate = in.readString();
        price = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(beginDate);
        out.writeString(endDate);
        out.writeString(price);
    }

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
    
}
