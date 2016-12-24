package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每天计划的事情
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月26日 下午3:55:33
 */
public class PlanThings implements Parcelable
{
    /*
     * 序号
     */
    private String index;
    /*
     * 内容
     */
    private String content;

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
        dest.writeString(index);
        dest.writeString(content);
    }
    public static final Parcelable.Creator<PlanThings> CREATOR = new Creator<PlanThings>()
    {
        @Override
        public PlanThings createFromParcel(Parcel source)
        {
            PlanThings planThings = new PlanThings();
            planThings.index = source.readString();
            planThings.content = source.readString();
            return planThings;
        }

        @Override
        public PlanThings[] newArray(int size)
        {
            return new PlanThings[size];
        }
    };
}
