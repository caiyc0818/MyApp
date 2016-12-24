package com.bcinfo.tripaway.bean;

import java.io.Serializable;


public class TraceLogs implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private  String title;
    
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
    private String createTime;
    
    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    private String status;
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status){
    	this.status=status;
    }
   
    private String desc;
    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc){
    	this.desc=desc;
    }
}
