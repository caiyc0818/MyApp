package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 行程规划服务
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月15日 下午4:18:24
 */
public class ServResrouce implements Parcelable
{
    /********* 产品详情行程规划简介 ***********/
    private String servId;
    
    private String servName;// 资源标题
    
    private String servType;
    
    private String servAlias;// 别名
    
    private String rank; // 排序
    
    /********** 行程规划详情 ****************/
    private String price;// 费用
    
    private String code;
    
    private String name;
    
    private String titleImage;
    
    private String servDesc;
    
    private boolean isChecked = false;;
    
    private ArrayList<ProductServiceResource> resourceList = new ArrayList<ProductServiceResource>();
    
    public ServResrouce()
    {
    }
    
    public boolean isChecked()
    {
        return isChecked;
    }
    
    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }
    
    public String getServDesc()
    {
        return servDesc;
    }
    
    public void setServDesc(String servDesc)
    {
        this.servDesc = servDesc;
    }
    
    public String getTitleImage()
    {
        return titleImage;
    }
    
    // 七牛的 一个图片展示的方法（imgHost） + 原有 的图片的 名字
    public void setTitleImage(String titleImage)
    {
        this.titleImage = titleImage;
    }
    
    public String getServId()
    {
        return servId;
    }
    
    public void setServId(String servId)
    {
        this.servId = servId;
    }
    
    public String getServName()
    {
        return servName;
    }
    
    public void setServName(String servName)
    {
        this.servName = servName;
    }
    
    public String getServType()
    {
        return servType;
    }
    
    public void setServType(String servType)
    {
        this.servType = servType;
    }
    
    public String getServAlias()
    {
        return servAlias;
    }
    
    public void setServAlias(String servAlias)
    {
        this.servAlias = servAlias;
    }
    
    public String getRank()
    {
        return rank;
    }
    
    public void setRank(String rank)
    {
        this.rank = rank;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public ArrayList<ProductServiceResource> getResourceList()
    {
        return resourceList;
    }
    
    public void setResourceList(ArrayList<ProductServiceResource> resourceList)
    {
        this.resourceList = resourceList;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<ServResrouce> CREATOR = new Parcelable.Creator<ServResrouce>()
    {
        public ServResrouce createFromParcel(Parcel source)
        {
            return new ServResrouce(source);
        }
        
        public ServResrouce[] newArray(int size)
        {
            return new ServResrouce[size];
        }
    };
    
    public ServResrouce(Parcel in)
    {
        servId = in.readString();
        servName = in.readString();
        servAlias = in.readString();
        rank = in.readString();
        servType = in.readString();
        price = in.readString();
        code = in.readString();
        name = in.readString();
        titleImage = in.readString();
        servDesc = in.readString();
        in.readTypedList(resourceList, ProductServiceResource.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(servId);
        out.writeString(servName);
        out.writeString(servAlias);
        out.writeString(rank);
        out.writeString(servType);
        out.writeString(price);
        out.writeString(code);
        out.writeString(name);
        out.writeString(titleImage);
        out.writeString(servDesc);
        out.writeTypedList(resourceList);
    }
}
