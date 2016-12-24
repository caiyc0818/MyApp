package com.bcinfo.tripaway.bean;

public class MyAccSubject {
private String point;
private String coupon;
private String totalBalance;
private String availableBalance;
private String waitAppraiseNum;
private String itineraryNum;
private String waitPayNum;
private String tripstoryNum;
private String fansNum;
private String focusNum;
private   UserInfo UserInfo;
public String getPoint()
{
    return point;
}
public void setPoint(String point)
{
    this.point = point;
}
public String getCoupon()
{
    return coupon;
}
public void setCoupon(String coupon)
{
    this.coupon = coupon;
}
public String getTotalBalance()
{
    return totalBalance;
}
public void setTotalBalance(String totalBalance)
{
    this.totalBalance = totalBalance;
}
public String getAvailableBalance()
{
    return availableBalance;
}
public void setAvailableBalance(String availableBalance)
{
    this.availableBalance = availableBalance;
}
public String getWaitAppraiseNum()
{
    return waitAppraiseNum;
}
public void setWaitAppraiseNum(String waitAppraiseNum)
{
    this.waitAppraiseNum = waitAppraiseNum;
}
public String getItineraryNum()
{
    return itineraryNum;
}
public void setItineraryNum(String itineraryNum)
{
    this.itineraryNum = itineraryNum;
}
public String getWaitPayNum()
{
    return waitPayNum;
}
public void setWaitPayNum(String waitPayNum)
{
    this.waitPayNum = waitPayNum;
}
public String getTripstoryNum()
{
    return tripstoryNum;
}
public void setTripstoryNum(String tripstoryNum)
{
    this.tripstoryNum = tripstoryNum;
}
public String getFansNum()
{
    return fansNum;
}
public void setFansNum(String fansNum)
{
    this.fansNum = fansNum;
}
public String getFocusNum()
{
    return focusNum;
}
public void setFocusNum(String focusNum)
{
    this.focusNum = focusNum;
}
public UserInfo getUserInfo()
{
    return UserInfo;
}
public void setUserInfo(UserInfo userInfo)
{
    UserInfo = userInfo;
}
@Override
public String toString()
{
    return "MyAccSubject [point=" + point + ", coupon=" + coupon + ", totalBalance=" + totalBalance + ", availableBalance="
            + availableBalance + ", waitAppraiseNum=" + waitAppraiseNum + ", itineraryNum=" + itineraryNum + ", waitPayNum="
            + waitPayNum + ", tripstoryNum=" + tripstoryNum + ", fansNum=" + fansNum + ", focusNum=" + focusNum + ", UserInfo="
            + UserInfo + "]";
}
public MyAccSubject(String point, String coupon, String totalBalance, String availableBalance, String waitAppraiseNum,
        String itineraryNum, String waitPayNum, String tripstoryNum, String fansNum, String focusNum,
        com.bcinfo.tripaway.bean.UserInfo userInfo)
{
    super();
    this.point = point;
    this.coupon = coupon;
    this.totalBalance = totalBalance;
    this.availableBalance = availableBalance;
    this.waitAppraiseNum = waitAppraiseNum;
    this.itineraryNum = itineraryNum;
    this.waitPayNum = waitPayNum;
    this.tripstoryNum = tripstoryNum;
    this.fansNum = fansNum;
    this.focusNum = focusNum;
    UserInfo = userInfo;
}
public MyAccSubject()
{
    super();
    // TODO Auto-generated constructor stub
}


}
