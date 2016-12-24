package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-17 下午3:59:16
 */
public class ProductServiceResource implements Parcelable
{
    /**
     * 服务id
     */
    private String id;
    
    // private String
    
    /**
     * 服务类型
     */
    private String servType;
    
    /**
     * 标题
     */
    private String servName; // 曼谷
    
    /**
     * 别名
     */
    private String servAlias;
    
    /**
     * 描述
     */
    private String servDesc;
    
    /**
     * 资源标签,字符串数组
     */
    private ArrayList<String> tags = new ArrayList<String>();
    
    /**
     * 资费
     */
    private String fee;
    
    /**
     * 资费类型（0-资源价格1-建议消费）
     */
    private String feeType;
    
    /**
     * 标题图
     */
    private String titleImage;
    
    /**
     * 库存
     */
    private String inventory;
    
    private String rank;
    
    /**
     * 资源扩展信息
     */
    private String resourceExt;
    
    /*
     * beginDate endDate
     */
    private List<AvailableTime> availableTimes = new ArrayList<AvailableTime>();
    
    private ScenicInfo scenicInfo = new ScenicInfo();
    
    public ProductServiceResource()
    {
        
    }
    
    public String getRank()
    {
        return rank;
    }
    
    public void setRank(String rank)
    {
        this.rank = rank;
    }
    
    public List<AvailableTime> getAvailableTimes()
    {
        return availableTimes;
    }
    
    public void setAvailableTimes(List<AvailableTime> availableTimes)
    {
        this.availableTimes = availableTimes;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getServType()
    {
        return servType;
    }
    
    public void setServType(String servType)
    {
        this.servType = servType;
    }
    
    public String getServName()
    {
        return servName;
    }
    
    public void setServName(String servName)
    {
        this.servName = servName;
    }
    
    public String getServAlias()
    {
        return servAlias;
    }
    
    public void setServAlias(String servAlias)
    {
        this.servAlias = servAlias;
    }
    
    public String getServDesc()
    {
        return servDesc;
    }
    
    public void setServDesc(String servDesc)
    {
        this.servDesc = servDesc;
    }
    
    public ArrayList<String> getTags()
    {
        return tags;
    }
    
    public void setTags(ArrayList<String> tags)
    {
        this.tags = tags;
    }
    
    public String getFee()
    {
        return fee;
    }
    
    public void setFee(String fee)
    {
        this.fee = fee;
    }
    
    public String getFeeType()
    {
        return feeType;
    }
    
    public void setFeeType(String feeType)
    {
        this.feeType = feeType;
    }
    
    public String getTitleImage()
    {
        return titleImage;
    }
    
    public void setTitleImage(String titleImage)
    {
        this.titleImage = titleImage;
    }
    
    public String getInventory()
    {
        return inventory;
    }
    
    public void setInventory(String inventory)
    {
        this.inventory = inventory;
    }
    
    public String getResourceExt()
    {
        return resourceExt;
    }
    
    public void setResourceExt(String resourceExt)
    {
        this.resourceExt = resourceExt;
    }
    
    public ScenicInfo getScenicInfo()
    {
        return scenicInfo;
    }
    
    public void setScenicInfo(ScenicInfo scenicInfo)
    {
        this.scenicInfo = scenicInfo;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<ProductServiceResource> CREATOR =
        new Parcelable.Creator<ProductServiceResource>()
        {
            public ProductServiceResource createFromParcel(Parcel source)
            {
                return new ProductServiceResource(source);
            }
            
            public ProductServiceResource[] newArray(int size)
            {
                return new ProductServiceResource[size];
            }
        };
    
    public ProductServiceResource(Parcel in)
    {
        id = in.readString();
        servType = in.readString();
        servName = in.readString();
        servAlias = in.readString();
        servDesc = in.readString();
        in.readStringList(tags);
        fee = in.readString();
        feeType = in.readString();
        titleImage = in.readString();
        inventory = in.readString();
        resourceExt = in.readString();
        rank = in.readString();
        in.readParcelable(scenicInfo.getClass().getClassLoader());
        in.readTypedList(availableTimes, AvailableTime.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(servType);
        out.writeString(servName);
        out.writeString(servAlias);
        out.writeString(servDesc);
        out.writeStringList(tags);
        out.writeString(fee);
        out.writeString(feeType);
        out.writeString(titleImage);
        out.writeString(inventory);
        out.writeString(resourceExt);
        out.writeString("rank");
        out.writeTypedList(availableTimes);
        out.writeParcelable(scenicInfo, 0);
    }
    
}
