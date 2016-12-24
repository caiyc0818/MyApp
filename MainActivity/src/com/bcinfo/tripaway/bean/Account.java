package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 服务者 账号
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年6月2日 14:19:11
 * 
 */
public class Account implements Parcelable
{
    /**
     * 账号类型
     */
    private String type;
    /**
     * 默认账户
     */
    private String isdefault;
    /**
     * 账户标识
     */
    private String id;
    /**
     * 账户账号
     */
    private String no;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getIsdefault()
    {
        return isdefault;
    }

    public void setIsdefault(String isdefault)
    {
        this.isdefault = isdefault;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNo()
    {
        return no;
    }

    public void setNo(String no)
    {
        this.no = no;
    }

    public Account()
    {

    }

    public Account(Parcel in)
    {
        type = in.readString();
        isdefault = in.readString();
        id = in.readString();
        no = in.readString();
    }

    public static Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>()
    {

        @Override
        public Account createFromParcel(Parcel source)
        {

            return new Account(source);
        }

        @Override
        public Account[] newArray(int size)
        {

            return new Account[size];
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
        dest.writeString(type);
        dest.writeString(isdefault);
        dest.writeString(id);
        dest.writeString(no);

    }

}
