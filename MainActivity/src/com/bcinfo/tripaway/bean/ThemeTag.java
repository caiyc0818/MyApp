package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ThemeTag implements Parcelable {

	private String id;

	private String name;
	


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
	}

	public ThemeTag() {

	}

	public ThemeTag(Parcel in) {
		id = in.readString();
		name = in.readString();
	}

	public static final Parcelable.Creator<ThemeTag> CREATOR = new Parcelable.Creator<ThemeTag>() {
		public ThemeTag createFromParcel(Parcel source) {
			return new ThemeTag(source);
		}

		public ThemeTag[] newArray(int size) {
			return new ThemeTag[size];
		}
	};
}
