package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 目的地-栏目信息 (推荐主题 旅行时间)
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月16日 13:54:22
 */
public class PushColumn implements Parcelable
{
    /**
     * 栏目标识 id
     */
    private String pushCoulumnId;
    /**
     * 栏目名称
     */
    private String pushColumnName;
    /**
     * 栏目图片
     */
    private String pushColumnImg;
    /**
     * 栏目类型 (topic time)
     */
    private String pushColumnType;

    public String getPushCoulumnId()
    {
        return pushCoulumnId;
    }

    public void setPushCoulumnId(String pushCoulumnId)
    {
        this.pushCoulumnId = pushCoulumnId;
    }

    public String getPushColumnName()
    {
        return pushColumnName;
    }

    public void setPushColumnName(String pushColumnName)
    {
        this.pushColumnName = pushColumnName;
    }

    public String getPushColumnImg()
    {
        return pushColumnImg;
    }

    public void setPushColumnImg(String pushColumnImg)
    {
        this.pushColumnImg = pushColumnImg;
    }

    public String getPushColumnType()
    {
        return pushColumnType;
    }

    public void setPushColumnType(String pushColumnType)
    {
        this.pushColumnType = pushColumnType;
    }

    public PushColumn()
    {
        super();
    }

    public PushColumn(Parcel in)
    {
        pushCoulumnId = in.readString();
        pushColumnName = in.readString();
        pushColumnImg = in.readString();
        pushColumnType = in.readString();

    }

    public static final Parcelable.Creator<PushColumn> CREATOR = new Parcelable.Creator<PushColumn>()
    {

        @Override
        public PushColumn createFromParcel(Parcel source)
        {

            return new PushColumn(source);
        }

        @Override
        public PushColumn[] newArray(int size)
        {

            return new PushColumn[size];
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
        dest.writeString(pushCoulumnId);
        dest.writeString(pushColumnName);
        dest.writeString(pushColumnImg);
        dest.writeString(pushColumnType);

    }

}
