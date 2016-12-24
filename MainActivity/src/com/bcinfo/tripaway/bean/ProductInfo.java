package com.bcinfo.tripaway.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 我的产品
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月24日 下午2:26:49
 */
public class ProductInfo implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 产品ID
     */
    private String id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品logo URL
     */
    private ArrayList<String> logoUrls;
    /**
     * 产品所在地
     */
    private String address;
    /**
     * 产品说明
     */
    private String description;
    /**
     * 产品提供服务
     */
    private String service;
    /**
     * 产品价格
     */
    private String price;
    /**
     * 产品团购日期
     */
    private String buyDate;
    /**
     * 团购产品招募日期
     */
    private String recruitDate;
    /**
     * 团购产品体验日期
     */
    private String experienceDate;
    /**
     * 团购产品报价说明
     */
    private String offerExplain;
    /**
     * 产品类型
     * 1:标准产品
     * 2:团购产品
     * 3:定制产品
     */
    private String type;
    /**
     * 订购数量
     */
    private String orderNumber;
    /**
     * 是否可订购
     */
    private boolean isEnable;
    /**
     * 产品评分
     */
    private String evaluate;
    /**
     * 产品评分数量
     */
    private String evaluateNum;
    /**
     * 团购产品成员容量
     */
    private String totalPople;
    /**
     * 团购产品拟招成员构成
     */
    private String memberCompose;
    /**
     * 团购产品特别提醒
     */
    private String specialRemind;
    /**
     * 发布者名字
     */
    private String authorName;
    /**
     * 发布者头像URL
     */
    private String authorPhotoUrl;
    /**
     * 发布者联系方式
     */
    private String authorContact;
    /**
     * 订购状态
     * 1:预订成功
     * 2:预订失败
     * 3:等待确认
     * 4:等待回复
     */
    private String orderState;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<String> getLogoUrls()
    {
        return logoUrls;
    }

    public void setLogoUrls(ArrayList<String> logoUrls)
    {
        this.logoUrls = logoUrls;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getBuyDate()
    {
        return buyDate;
    }

    public void setBuyDate(String buyDate)
    {
        this.buyDate = buyDate;
    }

    public String getRecruitDate()
    {
        return recruitDate;
    }

    public void setRecruitDate(String recruitDate)
    {
        this.recruitDate = recruitDate;
    }

    public String getExperienceDate()
    {
        return experienceDate;
    }

    public void setExperienceDate(String experienceDate)
    {
        this.experienceDate = experienceDate;
    }

    public String getOfferExplain()
    {
        return offerExplain;
    }

    public void setOfferExplain(String offerExplain)
    {
        this.offerExplain = offerExplain;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public boolean isEnable()
    {
        return isEnable;
    }

    public void setEnable(boolean isEnable)
    {
        this.isEnable = isEnable;
    }

    public String getEvaluate()
    {
        return evaluate;
    }

    public void setEvaluate(String evaluate)
    {
        this.evaluate = evaluate;
    }

    public String getEvaluateNum()
    {
        return evaluateNum;
    }

    public void setEvaluateNum(String evaluateNum)
    {
        this.evaluateNum = evaluateNum;
    }

    public String getTotalPople()
    {
        return totalPople;
    }

    public void setTotalPople(String totalPople)
    {
        this.totalPople = totalPople;
    }

    public String getMemberCompose()
    {
        return memberCompose;
    }

    public void setMemberCompose(String memberCompose)
    {
        this.memberCompose = memberCompose;
    }

    public String getSpecialRemind()
    {
        return specialRemind;
    }

    public void setSpecialRemind(String specialRemind)
    {
        this.specialRemind = specialRemind;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }

    public String getAuthorPhotoUrl()
    {
        return authorPhotoUrl;
    }

    public void setAuthorPhotoUrl(String authorPhotoUrl)
    {
        this.authorPhotoUrl = authorPhotoUrl;
    }

    public String getAuthorContact()
    {
        return authorContact;
    }

    public void setAuthorContact(String authorContact)
    {
        this.authorContact = authorContact;
    }

    public String getOrderState()
    {
        return orderState;
    }

    public void setOrderState(String orderState)
    {
        this.orderState = orderState;
    }
}
