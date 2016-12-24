package com.bcinfo.tripaway.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Cash implements Serializable
{
	private String couponCode;
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

//现金券价格E
   private String cashMoney;
   //有效期
   private String cashDate;
   //券条件
   private String cashCon;
 //券名
   private String cashType;
   
   //名
   private String cashClub;
   	
   //现金券状态
   private String cashStatus;
   private String discount;

public String getDiscount() {
	return discount;
}

public void setDiscount(String discount) {
	this.discount = discount;
}

public String getCashStatus() {
	return cashStatus;
}

public void setCashStatus(String cashStatus) {
	this.cashStatus = cashStatus;
}

public String getCashMoney() {
	return cashMoney;
}

public void setCashMoney(String cashMoney) {
	this.cashMoney = cashMoney;
}

public String getCashDate() {
	return cashDate;
}

public void setCashDate(String cashDate) {
	this.cashDate = cashDate;
}

public String getCashCon() {
	return cashCon;
}

public void setCashCon(String cashCon) {
	this.cashCon = cashCon;
}

public String getCashType() {
	return cashType;
}

public void setCashType(String cashType) {
	this.cashType = cashType;
}

public String getCashClub() {
	return cashClub;
}

public void setCashClub(String cashClub) {
	this.cashClub = cashClub;
}


}
