package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户的经历
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月15日 11:31:11
 * 
 */
public class Experiences implements Parcelable
{
    /**
     * 经历 标识id
     */
    private String experienceId;
    /**
     * 经历 时间
     */
    private String travelTime;

   

	/**
     * 经历 旅行图集url
     */
    private ArrayList<String> imagesUrls = new ArrayList<String>();
    
    /**
     * 经历 旅行图集
     */
    private ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();

    /**
     * 排序
     */
    private String rank;
    
    public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}
    public ArrayList<ImageInfo> getImages() {
		return images;
	}

	public void setImages(ArrayList<ImageInfo> images) {
		this.images = images;
	}

	/**
     * 描述
     */
    private String description;

    public String getExperienceId()
    {
        return experienceId;
    }

    public void setExperienceId(String experienceId)
    {
        this.experienceId = experienceId;
    }

    public List<String> getImagesUrls()
    {
        return imagesUrls;
    }

    public void setImagesUrls(ArrayList<String> imagesUrls)
    {
        this.imagesUrls = imagesUrls;
    }

    public String getRank()
    {
        return rank;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Experiences()
    {
        super();

    }

    public Experiences(Parcel in)
    {
        experienceId = in.readString();
        in.readStringList(imagesUrls);
        rank = in.readString();
        description = in.readString();
        travelTime= in.readString();
    }

    public static final Parcelable.Creator<Experiences> CREATOR = new Parcelable.Creator<Experiences>()
    {

        @Override
        public Experiences createFromParcel(Parcel source)
        {

            return new Experiences(source);

        }

        @Override
        public Experiences[] newArray(int size)
        {

            return new Experiences[size];
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
        dest.writeString(experienceId);
        dest.writeString(rank);
        dest.writeString(description);
        dest.writeStringList(imagesUrls);
        dest.writeString(travelTime);
    }

}
