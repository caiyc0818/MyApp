package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;

/**
 * 我预订的订单(游客端)
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月14日 11：12：11
 */
public class MyPayOrder implements Parcelable
{
    
    /**
     * 订单标识
     */
    private String id;
    
    /**
     * 订单编号
     */
    private String no;
    
    /**
     * 订单状态 预订成功(待付款) 预订失败 订单成功 等待回复
     */
    private String status;
    
    /**
     * 订单定价
     */
    private String price;
    
    /**
     * 订单开始日期 旅行周期
     */
    private String beginDate;
    
    /**
     * 订单结束日期 旅行周期
     */
    private String endDate;
    
    /**
     * 订单创建日期
     */
    private String createTime;
    
    /**
     * 退订策略
     */
    private String refundPolicy;
    
    /**
     * 服务者 bean
     */
    private UserInfo serviceInfo = new UserInfo();
    
    /**
     * 产品 bean
     */
    private ProductNewInfo productInfo = new ProductNewInfo();
    
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
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    public String getBeginDate()
    {
        return beginDate;
    }
    
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getRefundPolicy()
    {
        return refundPolicy;
    }
    
    public void setRefundPolicy(String refundPolicy)
    {
        this.refundPolicy = refundPolicy;
    }
    
    public UserInfo getServiceInfo()
    {
        return serviceInfo;
    }
    
    public void setServiceInfo(UserInfo serviceInfo)
    {
        this.serviceInfo = serviceInfo;
    }
    
    public ProductNewInfo getProductInfo()
    {
        return productInfo;
    }
    
    public void setProductInfo(ProductNewInfo productInfo)
    {
        this.productInfo = productInfo;
    }
    
    public MyPayOrder()
    {
        super();
        
    }
    
    public MyPayOrder(Parcel in)
    {
        id = in.readString();
        no = in.readString();
        status = in.readString();
        price = in.readString();
        beginDate = in.readString();
        endDate = in.readString();
        createTime = in.readString();
        refundPolicy = in.readString();
        in.readValue(serviceInfo.getClass().getClassLoader());
        in.readValue(productInfo.getClass().getClassLoader());
        
    }
    
    public static final Parcelable.Creator<MyPayOrder> CREATOR = new Parcelable.Creator<MyPayOrder>()
    {
        
        @Override
        public MyPayOrder createFromParcel(Parcel source)
        {
            
            return new MyPayOrder(source);
        }
        
        @Override
        public MyPayOrder[] newArray(int size)
        {
            
            return new MyPayOrder[size];
        }
        
    };
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(no);
        dest.writeString(status);
        dest.writeString(price);
        dest.writeString(beginDate);
        dest.writeString(endDate);
        dest.writeString(createTime);
        dest.writeString(refundPolicy);
        dest.writeParcelable(serviceInfo, 0);
        dest.writeParcelable(productInfo, 0);
        
    }
    
}
