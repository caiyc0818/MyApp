package com.bcinfo.tripaway.bean;

import java.io.Serializable;

/**
 * 旅游产品主题类
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月31日 13:57:12
 * 
 */
public class ProductTheme implements Serializable
{
     
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 主题id标识
     */
    private String themeId;
   

    /**
     * 主题名字
     */
    private String themeName;
    /**
     * 主题背景图片
     * 
     */
    private String themeUri;
    public String getThemeId()
    {
        return themeId;
    }

    public void setThemeId(String themeId)
    {
        this.themeId = themeId;
    }
    

    public String getThemeName()
    {
        return themeName;
    }

    public void setThemeName(String themeName)
    {
        this.themeName = themeName;
    }

    public String getThemeUri()
    {
        return themeUri;
    }

    public void setThemeUri(String themeUri)
    {
        this.themeUri = themeUri;
    }

}
