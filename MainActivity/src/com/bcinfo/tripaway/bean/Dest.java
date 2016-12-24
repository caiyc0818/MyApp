package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Dest implements Parcelable {

	private String destId;

	private String destName;

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(destId);
		dest.writeString(destName);
	}

	public Dest() {

	}

	public Dest(Parcel in) {
		destId = in.readString();
		destName = in.readString();
	}
	
	public Dest(String destId,String destName) {
		this.destId = destId;
		this.destName =destName;
	}


	public static final Parcelable.Creator<Dest> CREATOR = new Parcelable.Creator<Dest>() {
		public Dest createFromParcel(Parcel source) {
			return new Dest(source);
		}

		public Dest[] newArray(int size) {
			return new Dest[size];
		}
	};
}
