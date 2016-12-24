package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Invoice implements Parcelable {

	private String invoiceId;

	private String invoiceTitle;

	private String invoiceType;

	private String address;

	private String alias;

	private String area;

	private String email;

	private String tel;

	private String isDefault;

	private String postCode;

	private String userName;

	public Invoice() {

	}

	public Invoice(Parcel in) {
		invoiceId = in.readString();
		invoiceTitle = in.readString();
		invoiceType = in.readString();
		address = in.readString();
		alias = in.readString();
		area = in.readString();
		email = in.readString();
		tel = in.readString();
		isDefault = in.readString();
		postCode = in.readString();
		userName = in.readString();
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public final static Parcelable.Creator<Invoice> CREATOR = new Parcelable.Creator<Invoice>() {
		public Invoice createFromParcel(Parcel source) {
			return new Invoice(source);
		}

		public Invoice[] newArray(int size) {
			return new Invoice[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(invoiceId);
		dest.writeString(invoiceTitle);
		dest.writeString(invoiceType);
		dest.writeString(address);
		dest.writeString(alias);
		dest.writeString(area);
		dest.writeString(email);
		dest.writeString(tel);
		dest.writeString(isDefault);
		dest.writeString(postCode);
		dest.writeString(userName);
	}

}
