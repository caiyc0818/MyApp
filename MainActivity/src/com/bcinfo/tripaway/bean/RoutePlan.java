package com.bcinfo.tripaway.bean;

import java.io.Serializable;

/**
 * 行程规划
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月26日 下午3:45:15
 */
public class RoutePlan implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String id;
    /**
     * 第几天
     */
    private String dateIndex;
    /**
     * 当天计划
     */
    private String content;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDateIndex()
    {
        return dateIndex;
    }

    public void setDateIndex(String dateIndex)
    {
        this.dateIndex = dateIndex;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
