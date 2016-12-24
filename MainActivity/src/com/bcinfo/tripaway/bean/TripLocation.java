package com.bcinfo.tripaway.bean;

import java.io.Serializable;
/**
 * 旅游目的地
 * @function
 * @author caihelin
 * @version 1.0 2015年1月31日 16:33:33
 */
public class TripLocation implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 目的地Id标识
     */
    private Integer locationId;
    /**
     * 目的地名称(中文)
     */
    private String locationZhCnName;
    /**
     * 目的地名称(英文)
     * 
     */
    private String locationEnUsName;
    /**
     * 目的地类型  type 1:国家   2：城市
     */
    private Integer type;
   
    /**
     * 目的地国旗照片url
     *  
     */
    private String flagUrl;
    
    public String getFlagUrl()
    {
        return flagUrl;
    }
    public void setFlagUrl(String flagUrl)
    {
        this.flagUrl = flagUrl;
    }
    public Integer getLocationId()
    {
        return locationId;
    }
    public void setLocationId(Integer locationId)
    {
        this.locationId = locationId;
    }
    public String getLocationZhCnName()
    {
        return locationZhCnName;
    }
    public void setLocationZhCnName(String locationZhCnName)
    {
        this.locationZhCnName = locationZhCnName;
    }
    public String getLocationEnUsName()
    {
        return locationEnUsName;
    }
    public void setLocationEnUsName(String locationEnUsName)
    {
        this.locationEnUsName = locationEnUsName;
    }
    public Integer getType()
    {
        return type;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }
   
}
