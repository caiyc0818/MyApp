package com.bcinfo.tripaway.bean;

import java.util.List;

public class activity {
private String articleId;
private String cover;
private String status;
private String title;
private  List<articleExtInfo> articleExt;
public String getArticleId()
{
    return articleId;
}
public void setArticleId(String articleId)
{
    this.articleId = articleId;
}
public String getCover()
{
    return cover;
}
public void setCover(String cover)
{
    this.cover = cover;
}
public String getStatus()
{
    return status;
}
public void setStatus(String status)
{
    this.status = status;
}
public List<articleExtInfo> getArticleExt()
{
    return articleExt;
}
public void setArticleExt(List<articleExtInfo> articleExt)
{
    this.articleExt = articleExt;
}
public String getTitle()
{
    return title;
}
public void setTitle(String title)
{
    this.title = title;
}

public activity(String articleId, String cover, String status, String title, List<articleExtInfo> articleExt)
{
    super();
    this.articleId = articleId;
    this.cover = cover;
    this.status = status;
    this.title = title;
    this.articleExt = articleExt;
}
public activity()
{
    super();
    // TODO Auto-generated constructor stub
}










}
