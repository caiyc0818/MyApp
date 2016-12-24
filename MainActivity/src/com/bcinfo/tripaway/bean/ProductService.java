package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 产品提供的服务
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月5日 上午11:22:27
 */
public class ProductService implements Parcelable
{
    /**
     * 产品名称
     */
    private String name;
    
    /**
     * 产品服务编码
     */
    private String code;
    
    private List<ProductServiceResource> productServiceResources = new ArrayList<ProductServiceResource>();
    
    public ProductService()
    {
        
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public List<ProductServiceResource> getProductServiceResources()
    {
        return productServiceResources;
    }
    
    public void setProductServiceResources(List<ProductServiceResource> productServiceResources)
    {
        this.productServiceResources = productServiceResources;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<ProductService> CREATOR = new Parcelable.Creator<ProductService>()
    {
        public ProductService createFromParcel(Parcel source)
        {
            return new ProductService(source);
        }
        
        public ProductService[] newArray(int size)
        {
            return new ProductService[size];
        }
    };
    
    public ProductService(Parcel in)
    {
        code = in.readString();
        name = in.readString();
        in.readTypedList(productServiceResources, ProductServiceResource.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(code);
        out.writeString(name);
        out.writeTypedList(productServiceResources);
    }
}
