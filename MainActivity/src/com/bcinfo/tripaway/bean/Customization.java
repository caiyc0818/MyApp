package com.bcinfo.tripaway.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Customization implements Serializable
{
    /**
     * 
     */
	
    private static final long serialVersionUID = 1L;
    
    private  String desireId;
    
    public String getDesireId()
    {
        return desireId;
    }

    public void setDesireId(String desireId)
    {
        this.desireId = desireId;
    }
    
    private  String to;
    
    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }
    private String from;
    
    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }
    private String days;
    public String getDays()
    {
        return days;
    }

    public void setDays(String days)
    {
        this.days = days;
    }
    
    private  String startDate;
    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }
    
    
    private  String arrange;
    public String getArrange()
    {
        return arrange;
    }

    public void setArrange(String arrange)
    {
        this.arrange = arrange;
    }
    
    private  String adultNum;
    public String getAdultNum()
    {
        return adultNum;
    }

    public void setAdultNum(String adultNum)
    {
        this.adultNum = adultNum;
    }
    
    private  String childrenNum;
    public String getChildrenNum()
    {
        return childrenNum;
    }

    public void setChildrenNum(String childrenNum)
    {
        this.childrenNum = childrenNum;
    }
    
    private  String budget;
    public String getBudget()
    {
        return budget;
    }

    public void setBudget(String budget)
    {
        this.budget = budget;
    }
    
    private  String realName;
    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    private  String mobile;
    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
    private  String email;
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
    
    private  String status;
    
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    
  private  String reply;
    
    public String getReply()
    {
        return reply;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }
    
  private  String createTime;
    
    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    List<TraceLogs> traceLogs=new ArrayList<TraceLogs>();
    
    public List<TraceLogs> getTraceLogs()
    {
        return traceLogs;
    }
    
   List<UserInfo> recommendUsers=new ArrayList<UserInfo>();
    
    public List<UserInfo> getRecommendUsers()
    {
        return recommendUsers;
    }

    
 List<ProductNewInfo> recommendProducts=new ArrayList<ProductNewInfo>();
    
    public List<ProductNewInfo> getRecommendProducts()
    {
        return recommendProducts;
    }
    

}
