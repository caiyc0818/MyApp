package com.bcinfo.tripaway.bean;

/**
 * 结伴信息
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月28日 下午5:37:17
 */
public class GoWithInfo
{
    /**
     * 作者头像地址
     */
    private String authorPhotoUrl;
    /**
     * 作者名字
     */
    private String authorName;
    /**
     * 发布时间
     */
    private String createDate;
    /**
     * 被邀请人数
     */
    private String invitePeopleNum;
    /**
     * 响应人数
     */
    private String responcePeopleNum;
    /**
     * 结伴地址
     */
    private String goWithAddress;
    /**
     * 结伴描述
     */
    private String goWithDes;
    /**
     * 结伴状态
     */
    private String goWithState;
    /**
     * 是否为邀请
     */
    private boolean isComeFrom=false;

    public String getAuthorPhotoUrl()
    {
        return authorPhotoUrl;
    }

    public void setAuthorPhotoUrl(String authorPhotoUrl)
    {
        this.authorPhotoUrl = authorPhotoUrl;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getInvitePeopleNum()
    {
        return invitePeopleNum;
    }

    public void setInvitePeopleNum(String invitePeopleNum)
    {
        this.invitePeopleNum = invitePeopleNum;
    }

    public String getResponcePeopleNum()
    {
        return responcePeopleNum;
    }

    public void setResponcePeopleNum(String responcePeopleNum)
    {
        this.responcePeopleNum = responcePeopleNum;
    }

    public String getGoWithAddress()
    {
        return goWithAddress;
    }

    public void setGoWithAddress(String goWithAddress)
    {
        this.goWithAddress = goWithAddress;
    }

    public String getGoWithDes()
    {
        return goWithDes;
    }

    public void setGoWithDes(String goWithDes)
    {
        this.goWithDes = goWithDes;
    }

    public String getGoWithState()
    {
        return goWithState;
    }

    public void setGoWithState(String goWithState)
    {
        this.goWithState = goWithState;
    }

    public boolean isComeFrom()
    {
        return isComeFrom;
    }

    public void setComeFrom(boolean isComeFrom)
    {
        this.isComeFrom = isComeFrom;
    }
}
