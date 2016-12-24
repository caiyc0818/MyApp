package com.bcinfo.tripaway.bean;

/**
 * 航班
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年4月16日 下午4:52:59
 */
public class FlightInfo
{
    /**
     * 是否选择了航班
     */
    private boolean haveFlight;
    /**
     * 飞机图标
     */
    private String flightPicture;
    /**
     * 航班路线
     */
    private String flightName;
    /**
     * 航班说明
     */
    private String flightContent;
    /**
     * 航空公司图标
     */
    private String airlinesLogo;
    /**
     * 航空公司名字
     */
    private String airlinesName;
    /**
     * 航班价格
     */
    private String flightPrice;
    /**
     * 航班总时间
     */
    private String flightTotalTime;
    /**
     * 起飞机场中文名称
     */
    private String startAirportCN;
    /**
     * 起飞机场英文名称
     */
    private String startAirportEN;
    /**
     * 降落机场中文名称
     */
    private String endAirportCN;
    /**
     * 降落机场英文名称
     */
    private String endAirportEN;
    /**
     * 起飞时间
     */
    private String startTime;
    /**
     * 降落时间
     */
    private String endTime;
    /**
     * 航班经过几天到达
     */
    private int flightDays=0;

    public boolean isHaveFlight()
    {
        return haveFlight;
    }

    public void setHaveFlight(boolean haveFlight)
    {
        this.haveFlight = haveFlight;
    }

    public String getFlightPicture()
    {
        return flightPicture;
    }

    public void setFlightPicture(String flightPicture)
    {
        this.flightPicture = flightPicture;
    }

    public String getFlightName()
    {
        return flightName;
    }

    public void setFlightName(String flightName)
    {
        this.flightName = flightName;
    }

    public String getFlightContent()
    {
        return flightContent;
    }

    public void setFlightContent(String flightContent)
    {
        this.flightContent = flightContent;
    }

    public String getAirlinesLogo()
    {
        return airlinesLogo;
    }

    public void setAirlinesLogo(String airlinesLogo)
    {
        this.airlinesLogo = airlinesLogo;
    }

    public String getAirlinesName()
    {
        return airlinesName;
    }

    public void setAirlinesName(String airlinesName)
    {
        this.airlinesName = airlinesName;
    }

    public String getFlightPrice()
    {
        return flightPrice;
    }

    public void setFlightPrice(String flightPrice)
    {
        this.flightPrice = flightPrice;
    }

    public String getFlightTotalTime()
    {
        return flightTotalTime;
    }

    public void setFlightTotalTime(String flightTotalTime)
    {
        this.flightTotalTime = flightTotalTime;
    }

    public String getStartAirportCN()
    {
        return startAirportCN;
    }

    public void setStartAirportCN(String startAirportCN)
    {
        this.startAirportCN = startAirportCN;
    }

    public String getStartAirportEN()
    {
        return startAirportEN;
    }

    public void setStartAirportEN(String startAirportEN)
    {
        this.startAirportEN = startAirportEN;
    }

    public String getEndAirportCN()
    {
        return endAirportCN;
    }

    public void setEndAirportCN(String endAirportCN)
    {
        this.endAirportCN = endAirportCN;
    }

    public String getEndAirportEN()
    {
        return endAirportEN;
    }

    public void setEndAirportEN(String endAirportEN)
    {
        this.endAirportEN = endAirportEN;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public int getFlightDays()
    {
        return flightDays;
    }

    public void setFlightDays(int flightDays)
    {
        this.flightDays = flightDays;
    }
}
