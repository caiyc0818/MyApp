package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MyAccFas implements Parcelable {
	private String buyer;
	private String orderNo;
	private String title;
	private String amount;
	private String direction;
	private String recordTime;
	private String status;
	private String payType;
	private String actionType;
	private String focusNum;
	private String orderId;

	public String getBuyer() {
		return buyer;
	}

	public MyAccFas(Parcel in) {
		buyer = in.readString();
		orderNo = in.readString();
		title = in.readString();
		amount = in.readString();
		direction = in.readString();
		recordTime = in.readString();
		status = in.readString();
		payType = in.readString();
		actionType = in.readString();
		focusNum = in.readString();
		orderId = in.readString();

	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(buyer);
		dest.writeString(orderNo);
		dest.writeString(amount);
		dest.writeString(direction);

		dest.writeString(recordTime);
		dest.writeString(status);
		dest.writeString(payType);
		dest.writeString(actionType);
		dest.writeString(focusNum);
		dest.writeString(orderId);
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getFocusNum() {
		return focusNum;
	}

	public void setFocusNum(String focusNum) {
		this.focusNum = focusNum;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public MyAccFas(String buyer, String orderNo, String title, String amount, String direction, String recordTime,
			String status, String payType, String actionType, String focusNum,String orderId) {
		super();
		this.buyer = buyer;
		this.orderNo = orderNo;
		this.title = title;
		this.amount = amount;
		this.direction = direction;
		this.recordTime = recordTime;
		this.status = status;
		this.payType = payType;
		this.actionType = actionType;
		this.focusNum = focusNum;
		this.orderId = orderId;
	}

	public MyAccFas() {
		super();
		// TODO Auto-generated constructor stub
	}

}
