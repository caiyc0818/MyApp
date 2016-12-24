package com.bcinfo.tripaway.bean;

import com.bcinfo.tripaway.bean.ProductNewInfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的行程单
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月15日 16:45:11
 * 
 */
public class MyItinerary implements Parcelable
{
    /**
     * 行程单id 标识
     */
    private String itineraryId;

    /**
     * 行程单标题
     */
    private String itineraryTitle;
    /**
     * 行程单出发日期
     */
    private String itineraryBeginDate;
    /**
     * 产品信息
     */
    private ProductNewInfo productInfo = new ProductNewInfo();

    public String getItineraryId()
    {
        return itineraryId;
    }

    public void setItineraryId(String itineraryId)
    {
        this.itineraryId = itineraryId;
    }

    public String getItineraryTitle()
    {
        return itineraryTitle;
    }

    public void setItineraryTitle(String itineraryTitle)
    {
        this.itineraryTitle = itineraryTitle;
    }

    public String getItineraryBeginDate()
    {
        return itineraryBeginDate;
    }

    public void setItineraryBeginDate(String itineraryBeginDate)
    {
        this.itineraryBeginDate = itineraryBeginDate;
    }

    public ProductNewInfo getProductInfo()
    {
        return productInfo;
    }

    public void setProductInfo(ProductNewInfo productInfo)
    {
        this.productInfo = productInfo;
    }

    public MyItinerary()
    {
        super();
    }

    public MyItinerary(Parcel in)
    {
        itineraryId = in.readString();
        itineraryTitle = in.readString();
        itineraryBeginDate = in.readString();
        in.readValue(productInfo.getClass().getClassLoader());
    }

    public static final Parcelable.Creator<MyItinerary> CREATOR = new Parcelable.Creator<MyItinerary>()
    {

        @Override
        public MyItinerary createFromParcel(Parcel source)
        {

            return new MyItinerary(source);
        }

        @Override
        public MyItinerary[] newArray(int size)
        {

            return new MyItinerary[size];

        }

    };

    @Override
    public int describeContents()
    {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(itineraryId);
        dest.writeString(itineraryTitle);
        dest.writeString(itineraryBeginDate);
        dest.writeParcelable(productInfo, 0);

    }

}
