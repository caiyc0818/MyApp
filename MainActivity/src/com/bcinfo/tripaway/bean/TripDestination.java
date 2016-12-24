package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 旅游目的地
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月16日 10:38:06
 */
public class TripDestination implements Parcelable  
{
    /**
     * 目的地 标识id
     */
    private String destId;
    /**
     * 目的地 中文名字
     */
    private String destName;
    /**
     * 目的地 英文名字
     */
    private String destNameEn;
    /**
     * 目的地 简介
     */
    private String destDescription;
    /**
     * 目的地 图标key值
     */
    private String destLogoKey;
    /**
     * 目的地 产品数量
     */
    private String destProNum;
    /**
     * 目的地 达人数量
     */
    private String destSupNum;
    
    private String destKeyWords;
private String keywords;

    public String getKeywords() {
	return keywords;
}

public void setKeywords(String keywords) {
	this.keywords = keywords;
}

	public String getDestKeyWords() {
		return destKeyWords;
	}

	public void setDestKeyWords(String destKeyWords) {
		this.destKeyWords = destKeyWords;
	}

	public String getDestId()
    {
        return destId;
    }

    public void setDestId(String destId)
    {
        this.destId = destId;
    }

    public String getDestName()
    {
        return destName;
    }

    public void setDestName(String destName)
    {
        this.destName = destName;
    }

    public String getDestNameEn()
    {
        return destNameEn;
    }

    public void setDestNameEn(String destNameEn)
    {
        this.destNameEn = destNameEn;
    }

    public String getDestDescription()
    {
        return destDescription;
    }

    public void setDestDescription(String destDescription)
    {
        this.destDescription = destDescription;
    }

    public String getDestLogoKey()
    {
        return destLogoKey;
    }

    public void setDestLogoKey(String destLogoKey)
    {
        this.destLogoKey = destLogoKey;
    }

    public String getDestProNum()
    {
        return destProNum;
    }

    public void setDestProNum(String destProNum)
    {
        this.destProNum = destProNum;
    }

    public String getDestSupNum()
    {
        return destSupNum;
    }

    public void setDestSupNum(String destSupNum)
    {
        this.destSupNum = destSupNum;
    }

    public TripDestination()
    {
        super();
    }

    public TripDestination(Parcel in)
    {
        destId = in.readString();
        destName = in.readString();
        destNameEn = in.readString();
        destDescription = in.readString();
        destLogoKey = in.readString();
        destProNum = in.readString();
        destSupNum = in.readString();
        destKeyWords=in.readString();
        keywords = in.readString();
    }

    public static final Parcelable.Creator<TripDestination> CREATOR = new Parcelable.Creator<TripDestination>()
    {

        @Override
        public TripDestination createFromParcel(Parcel source)
        {

            return new TripDestination(source);
        }

        @Override
        public TripDestination[] newArray(int size)
        {

            return new TripDestination[size];
        }
    };

    @Override
    public int describeContents()
    {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag)
    {
        dest.writeString(destId);
        dest.writeString(destName);
        dest.writeString(destNameEn);
        dest.writeString(destDescription);
        dest.writeString(destLogoKey);
        dest.writeString(destProNum);
        dest.writeString(destSupNum);
        dest.writeString(destKeyWords);
        dest.writeString(keywords);
        

    }

}
