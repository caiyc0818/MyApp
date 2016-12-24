package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ExperienceDetail implements Parcelable
{

	private String id;
    
    private String travelTime;

    private List<HashMap<String,String>> appraise = new ArrayList<HashMap<String,String>>();
    
    private ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();

    private String scale;
    
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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<HashMap<String,String>>   getAppraise()
    {
        return appraise;
    }


    public String getScale()
    {
        return scale;
    }

    public void setScale(String scale)
    {
        this.scale = scale;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ExperienceDetail()
    {
        super();

    }

    public ExperienceDetail(Parcel in)
    {
        id = in.readString();
        scale = in.readString();
        description = in.readString();
        travelTime= in.readString();
    }

    public static final Parcelable.Creator<ExperienceDetail> CREATOR = new Parcelable.Creator<ExperienceDetail>()
    {

        @Override
        public ExperienceDetail createFromParcel(Parcel source)
        {

            return new ExperienceDetail(source);

        }

        @Override
        public ExperienceDetail[] newArray(int size)
        {

            return new ExperienceDetail[size];
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
        dest.writeString(id);
        dest.writeString(scale);
        dest.writeString(description);
        dest.writeString(travelTime);
    }

}
