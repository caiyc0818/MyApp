package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 产品特点，特色
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年5月13日 下午4:50:48
 */
public class FeatureInfo implements Parcelable
{
    private String id;//产品特色标识
    private String title;//产品特色标题
    private String desc;//产品特色简介
    public List<ImageInfo> getImages() {
		return images;
	}

	public void setImages(List<ImageInfo> images) {
		this.images = images;
	}
	private List<ImageInfo> images = new ArrayList<ImageInfo>();//图片URL列表

    public FeatureInfo()
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

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }


    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    public static final Parcelable.Creator<FeatureInfo> CREATOR = new Parcelable.Creator<FeatureInfo>()
    {
        public FeatureInfo createFromParcel(Parcel source)
        {
            return new FeatureInfo(source);
        }

        public FeatureInfo[] newArray(int size)
        {
            return new FeatureInfo[size];
        }
    };

    public FeatureInfo(Parcel in)
    {
        id = in.readString();
        title = in.readString();
        desc = in.readString();
        in.readTypedList(images,ImageInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(title);
        out.writeString(desc);
        out.writeTypedList(images);
    }
}
