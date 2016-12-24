package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Cats implements Parcelable {

	private String custTitle;

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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	private String cover;

	private String catId;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(custTitle);
		dest.writeString(cover);
		dest.writeString(catId);
	}

	public Cats() {

	}

	public Cats(Parcel in) {
		custTitle = in.readString();
		cover = in.readString();
		catId = in.readString();
	}

	public static final Parcelable.Creator<Cats> CREATOR = new Parcelable.Creator<Cats>() {
		public Cats createFromParcel(Parcel source) {
			return new Cats(source);
		}

		public Cats[] newArray(int size) {
			return new Cats[size];
		}
	};
}
