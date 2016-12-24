package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-18 上午11:18:40
 */
public class TipContentInfo implements Parcelable
{
    private String checked;
    
    private String catCode;
    
    private String catName;
    
    private String remarks;
    
    public TipContentInfo()
    {
        
    }
    
    public String getChecked()
    {
        return checked;
    }
    
    public void setChecked(String checked)
    {
        this.checked = checked;
    }
    
    public String getCatCode()
    {
        return catCode;
    }
    
    public void setCatCode(String catCode)
    {
        this.catCode = catCode;
    }
    
    public String getCatName()
    {
        return catName;
    }
    
    public void setCatName(String catName)
    {
        this.catName = catName;
    }
    
    public String getRemarks()
    {
        return remarks;
    }
    
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<TipContentInfo> CREATOR = new Parcelable.Creator<TipContentInfo>()
    {
        public TipContentInfo createFromParcel(Parcel source)
        {
            return new TipContentInfo(source);
        }
        
        public TipContentInfo[] newArray(int size)
        {
            return new TipContentInfo[size];
        }
    };
    
    public TipContentInfo(Parcel in)
    {
        checked = in.readString();
        catCode = in.readString();
        catName = in.readString();
        remarks = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(checked);
        out.writeString(catCode);
        out.writeString(catName);
        out.writeString(remarks);
    }
    
}
