package com.bcinfo.tripaway.bean;

/**
 * 贴士
 * 
 * @function
 * @author weiCH
 * @version 1.0, 2015年5月27日 下午3:48:09
 */
public class TipsDetailInfo
{
    private String title; // 标题
    
    private String content;// 内容
    
    private int id; // id
    
    private String type;// tpye
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
}
