package com.bcinfo.tripaway.bean;

import java.io.Serializable;

/**
 * 评论
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月30日 下午4:44:04
 */
public class Comment implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userPhotoUrl;
    private String userId;
    /**
     * 评论日期
     */
    private String date;
    /**
     * 评论内容
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

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPhotoUrl()
    {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl)
    {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
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
