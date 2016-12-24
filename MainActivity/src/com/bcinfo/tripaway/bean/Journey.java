package com.bcinfo.tripaway.bean;

import java.util.ArrayList;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 行程规划
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年5月15日 下午3:56:35
 */
public class Journey implements Parcelable
{
    private String id;
    private String title;
    private String rank;
    private String description;
    
    private ArrayList<ServResrouce> attractionsList = new ArrayList<ServResrouce>();
    private ArrayList<ServResrouce> trafficList = new ArrayList<ServResrouce>();
    private ArrayList<ServResrouce> stayList = new ArrayList<ServResrouce>();
    private ArrayList<AttractionAllInfo> attractionAllInfoList = new ArrayList<AttractionAllInfo>();
    
    public ArrayList<AttractionAllInfo> getAttractionAllInfoList() {
		return attractionAllInfoList;
	}

	public void setAttractionAllInfoList(ArrayList<AttractionAllInfo> attractionAllInfoList) {
		this.attractionAllInfoList = attractionAllInfoList;
	}

	public Journey()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ArrayList<ServResrouce> getAttractionsList()
    {
        return attractionsList;
    }

    public void setAttractionsList(ArrayList<ServResrouce> attractionsList)
    {
        this.attractionsList = attractionsList;
    }

    public ArrayList<ServResrouce> getTrafficList()
    {
        return trafficList;
    }

    public void setTrafficList(ArrayList<ServResrouce> trafficList)
    {
        this.trafficList = trafficList;
    }

    public ArrayList<ServResrouce> getStayList()
    {
        return stayList;
    }

    public void setStayList(ArrayList<ServResrouce> stayList)
    {
        this.stayList = stayList;
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    public static final Parcelable.Creator<Journey> CREATOR = new Parcelable.Creator<Journey>()
    {
        public Journey createFromParcel(Parcel source)
        {
            return new Journey(source);
        }

        public Journey[] newArray(int size)
        {
            return new Journey[size];
        }
    };

    public Journey(Parcel in)
    {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        in.readTypedList(attractionsList, ServResrouce.CREATOR);
        in.readTypedList(trafficList, ServResrouce.CREATOR);
        in.readTypedList(stayList, ServResrouce.CREATOR);
        in.readTypedList(attractionAllInfoList, AttractionAllInfo.CREATOR);
        
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(title);
        out.writeString(description);
        out.writeTypedList(attractionsList);
        out.writeTypedList(trafficList);
        out.writeTypedList(stayList);
        out.writeTypedList(attractionAllInfoList);
    }
}
