package com.bcinfo.tripaway.bean;

import java.io.Serializable;


public class CashOrder implements Serializable
{
   private String couponCode;
   private String couponName;
   private String couponTypeName;
   private String discount;
   private String description;
   private String expireDate;
   private String faceValue;
   private String isExpired;
   private String isUniversal;
   private String isUsed;
   private String status;
public String getCouponCode() {
	return couponCode;
}
public void setCouponCode(String couponCode) {
	this.couponCode = couponCode;
}
public String getCouponName() {
	return couponName;
}
public void setCouponName(String couponName) {
	this.couponName = couponName;
}
public String getCouponTypeName() {
	return couponTypeName;
}
public void setCouponTypeName(String couponTypeName) {
	this.couponTypeName = couponTypeName;
}
public String getDiscount() {
	return discount;
}
public void setDiscount(String discount) {
	this.discount = discount;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getExpireDate() {
	return expireDate;
}
public void setExpireDate(String expireDate) {
	this.expireDate = expireDate;
}
public String getFaceValue() {
	return faceValue;
}
public void setFaceValue(String faceValue) {
	this.faceValue = faceValue;
}
public String getIsExpired() {
	return isExpired;
}
public void setIsExpired(String isExpired) {
	this.isExpired = isExpired;
}
public String getIsUniversal() {
	return isUniversal;
}
public void setIsUniversal(String isUniversal) {
	this.isUniversal = isUniversal;
}
public String getIsUsed() {
	return isUsed;
}
public void setIsUsed(String isUsed) {
	this.isUsed = isUsed;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public CashOrder(String couponCode, String couponName, String couponTypeName, String discount, String description,
		String expireDate, String faceValue, String isExpired, String isUniversal, String isUsed, String status) {
	super();
	this.couponCode = couponCode;
	this.couponName = couponName;
	this.couponTypeName = couponTypeName;
	this.discount = discount;
	this.description = description;
	this.expireDate = expireDate;
	this.faceValue = faceValue;
	this.isExpired = isExpired;
	this.isUniversal = isUniversal;
	this.isUsed = isUsed;
	this.status = status;
}
public CashOrder() {
	super();
	// TODO Auto-generated constructor stub
}



}
