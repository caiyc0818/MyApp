package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

/**
 * 旅游产品类
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月29日 14:35:47
 * 
 * 
 */
public class TripProduct
{
    /**
     * 旅游产品图片 url 网络图片形式
     */
    private int drawableRes;

    /**
     * 旅游产品种类标识 flag为0 表示个人订购产品;flag为1 表示团购产品
     */
    private int flag;

    /**
     * 旅游产品名称 (标题)
     */
    private String productName;
    /**
     * 旅游产品评价数量
     * 
     */
    private Long commentsCount;
    /**
     * 旅游产品服务商
     * 
     */
    private String productServicerName;

    /**
     * 旅游产品价格
     * 
     */
    private Long productPrize;
    /**
     * 旅游产品地点
     * 
     */
    private String productLocation;
    /**
     * 产品的开始时间
     * 
     */
    private String productStartTime;

    /**
     * 产品的结束时间
     * 
     */
    private String productEndTime;

    public String getProductEndTime()
    {
        return productEndTime;
    }

    public void setProductEndTime(String productEndTime)
    {
        this.productEndTime = productEndTime;
    }

    public String getProductStartTime()
    {
        return productStartTime;
    }

    public void setProductStartTime(String productStartTime)
    {
        this.productStartTime = productStartTime;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public String getProductLocation()
    {
        return productLocation;
    }

    public int getDrawableRes()
    {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes)
    {
        this.drawableRes = drawableRes;
    }

    public void setProductLocation(String productLocation)
    {
        this.productLocation = productLocation;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Long getCommentsCount()
    {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount)
    {
        this.commentsCount = commentsCount;
    }

    public String getProductServicerName()
    {
        return productServicerName;
    }

    public void setProductServicerName(String productServicerName)
    {
        this.productServicerName = productServicerName;
    }

    public Long getProductPrize()
    {
        return productPrize;
    }

    public void setProductPrize(Long productPrize)
    {
        this.productPrize = productPrize;
    }

    public TripProduct()
    {

    }

    public TripProduct(int drawableRes, int flag, String productName, Long commentsCount, String productServicerName,
            Long productPrize)
    {
        super();
        this.drawableRes = drawableRes;
        this.flag = flag;
        this.productName = productName;
        this.commentsCount = commentsCount;
        this.productServicerName = productServicerName;
        this.productPrize = productPrize;

    }

    public TripProduct(int drawableRes, int flag, String productName, Long commentsCount, String productServicerName,
            Long productPrize, String productLocation)
    {
        super();
        this.drawableRes = drawableRes;
        this.flag = flag;
        this.productName = productName;
        this.commentsCount = commentsCount;
        this.productServicerName = productServicerName;
        this.productPrize = productPrize;
        this.productLocation = productLocation;
    }

    public TripProduct(int drawableRes, int flag, String productName, Long commentsCount, String productServicerName,
            Long productPrize, String productLocation, String productStartTime, String productEndTime)
    {
        super();
        this.drawableRes = drawableRes;
        this.flag = flag;
        this.productName = productName;
        this.commentsCount = commentsCount;
        this.productServicerName = productServicerName;
        this.productPrize = productPrize;
        this.productLocation = productLocation;
        this.productStartTime = productStartTime;
        this.productEndTime = productEndTime;
    }

    public TripProduct(int drawableRes, int flag, String productName, Long commentsCount, String productServicerName,
            Long productPrize, String productStartTime, String productEndTime)
    {
        super();
        this.drawableRes = drawableRes;
        this.flag = flag;
        this.productName = productName;
        this.commentsCount = commentsCount;
        this.productServicerName = productServicerName;
        this.productPrize = productPrize;
        this.productStartTime = productStartTime;
        this.productEndTime = productEndTime;
    }

    public TripProduct(int drawableRes, int flag, String productName, String productStartTime, String productEndTime)
    {
        super();
        this.drawableRes = drawableRes;
        this.flag = flag;
        this.productName = productName;
        this.productStartTime = productStartTime;
        this.productEndTime = productEndTime;
    }

}
