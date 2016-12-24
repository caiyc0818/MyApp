package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Cust implements Parcelable {

	private String catId;

	private String custTitle;
	
	private String cover;
	
	private String lineNum;
	
	private ProductNewInfo product;


	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getCustTitle() {
		return custTitle;
	}

	public void setCustTitle(String custTitle) {
		this.custTitle = custTitle;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public ProductNewInfo getProduct() {
		return product;
	}

	public void setProduct(ProductNewInfo product) {
		this.product = product;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(catId);
		dest.writeString(custTitle);
		dest.writeString(cover);
		dest.writeString(lineNum);
		dest.writeParcelable(product, 0);
	}

	public Cust() {

	}

	public Cust(Parcel in) {
		catId = in.readString();
		custTitle = in.readString();
		cover =in.readString();
		lineNum = in.readString();
		product = in.readParcelable(ProductNewInfo.class.getClassLoader());
	}

	public static final Parcelable.Creator<Cust> CREATOR = new Parcelable.Creator<Cust>() {
		public Cust createFromParcel(Parcel source) {
			return new Cust(source);
		}

		public Cust[] newArray(int size) {
			return new Cust[size];
		}
	};
}
