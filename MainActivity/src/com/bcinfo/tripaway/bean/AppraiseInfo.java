package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AppraiseInfo implements Parcelable
{

    private String name;
    private String content;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public AppraiseInfo()
    {
        super();
    }
    
    
    public AppraiseInfo(Parcel in){
        name = in.readString();
        content = in.readString();
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
        dest.writeString(name);
        dest.writeString(content);
    }

    public final static Parcelable.Creator<AppraiseInfo> CREATOR = new Parcelable.Creator<AppraiseInfo>()
    {
        public AppraiseInfo createFromParcel(Parcel source)
        {
            return new AppraiseInfo(source);
        }
        
        public AppraiseInfo[] newArray(int size)
        {
            return new AppraiseInfo[size];
        }
    };
    
    public String toString() {
       return "content:"+this.content+",name:"+this.name; 
    };
}
