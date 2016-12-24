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
public class Experience implements Parcelable
{
    /**
     * 经历 标识id
     */
    private String experienceId;
    
    private String destName;
    
    public boolean showExpFlag=false;
    public boolean showdesFlag=false;

    private ArrayList<ExperienceDetail> expDetail = new ArrayList<ExperienceDetail>();
    

    private String expTimes;
    
    public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}
    public ArrayList<ExperienceDetail> getExpDetail() {
		return expDetail;
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


    public String getExpTimes()
    {
        return expTimes;
    }

    public void setExpTimes(String expTimes)
    {
        this.expTimes = expTimes;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Experience()
    {
        super();

    }

    public Experience(Parcel in)
    {
        experienceId = in.readString();
        in.readTypedList(expDetail, ExperienceDetail.CREATOR);
        expTimes = in.readString();
        description = in.readString();
        destName= in.readString();
    }

    public static final Parcelable.Creator<Experience> CREATOR = new Parcelable.Creator<Experience>()
    {

        @Override
        public Experience createFromParcel(Parcel source)
        {

            return new Experience(source);

        }

        @Override
        public Experience[] newArray(int size)
        {

            return new Experience[size];
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
        dest.writeString(expTimes);
        dest.writeString(description);
        dest.writeTypedList(expDetail);
        dest.writeString(destName);
    }

}
