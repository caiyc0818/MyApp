package com.bcinfo.tripaway.bean;

import java.io.Serializable;

/**
 * 日程事件
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月5日 下午8:58:42
 */
public class ScheduleEvent implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /*
     * 跨度多天事件的每天唯一ID
     */
    private String id;
    /*
     * 创建时间/作为创建事件ID
     */
    private String createTime;
    /*
     * 处理日期
     */
    private String date;
    /*
     * 开始日期
     */
    private String beginDate;
    /*
     * 结束日期
     */
    private String endDate;
    /*
     * 颜色
     */
    private String color;
    /*
     * 内容
     */
    private String content;
    /*
     * 是否完成
     */
    private String isFinish;
    /*
     * 备注
     */
    private String remark;
    /*
     * 提醒时间
     */
    private String notifyTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getNotifyTime()
    {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime)
    {
        this.notifyTime = notifyTime;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
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

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getIsFinish()
    {
        return isFinish;
    }

    public void setIsFinish(String isFinish)
    {
        this.isFinish = isFinish;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
