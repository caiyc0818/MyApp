package com.bcinfo.tripaway.bean;

import com.bcinfo.tripaway.net.Urls;

/**
 * 账户类型
 * @function
 * @author     魏常航  
 * @version   1.0, 2015年7月13日 下午3:44:04
 */
public class AccountTypeInfo 
{
    /**
     * 类型标识id
     */
    private String id ;
    /**
     * 类型名
     */
    private String name;
    /**
     * 缩略图
     */
    private String logo;


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

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        if(logo.contains("http")){
            this.logo = logo;
        }else{
            this.logo=Urls.imgHost+logo;
        }
    }
}
