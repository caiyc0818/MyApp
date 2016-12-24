package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Club implements Parcelable {

	private String orgId;

	private String orgName;
	
	private String introduce;
	
	private String orgLogo;
	
	private String products;
	
	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}
	

	private String drivers;
	
	public String getDrivers() {
		return drivers;
	}

	public void setDrivers(String drivers) {
		this.drivers = drivers;
	}
	private String leaders;
	public String getLeaders() {
		return leaders;
	}

	public void setLeaders(String leaders) {
		this.leaders = leaders;
	}
	private String guides;
	
	public String getGuides() {
		return guides;
	}

	public void setGuides(String guides) {
		this.guides = guides;
	}
	private String fans;
	
	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	private String isFocus;
	
	public String getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(String isFocus) {
		this.isFocus = isFocus;
	}


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(orgId);
		dest.writeString(orgName);
		dest.writeString(introduce);
		dest.writeString(orgLogo);
		dest.writeString(drivers);
		dest.writeString(leaders);
		dest.writeString(guides);
		dest.writeString(fans);
		dest.writeString(userId);
		dest.writeString(isFocus);
	}

	public Club() {

	}

	public Club(Parcel in) {
		orgId = in.readString();
		orgName = in.readString();
		introduce =in.readString();
		orgLogo = in.readString();
		products = in.readString();
		drivers = in.readString();
		leaders = in.readString();
		guides = in.readString();
		fans = in.readString();
		userId = in.readString();
		isFocus = in.readString();
	}

	public static final Parcelable.Creator<Club> CREATOR = new Parcelable.Creator<Club>() {
		public Club createFromParcel(Parcel source) {
			return new Club(source);
		}

		public Club[] newArray(int size) {
			return new Club[size];
		}
	};
}
