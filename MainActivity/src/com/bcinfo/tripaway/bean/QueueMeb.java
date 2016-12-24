package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class QueueMeb implements Parcelable {

	private String memberType;

	private UserInfo info;

	public QueueMeb() {

	}

	public QueueMeb(Parcel in) {
		memberType = in.readString();
		info = in.readParcelable(UserInfo.class.getClassLoader());
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public UserInfo getInfo() {
		return info;
	}

	public void setInfo(UserInfo info) {
		this.info = info;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(memberType);
		dest.writeParcelable(info, 0);
	}

	public static final Parcelable.Creator<QueueMeb> CREATOR = new Parcelable.Creator<QueueMeb>() {
		public QueueMeb createFromParcel(Parcel source) {
			return new QueueMeb(source);
		}

		public QueueMeb[] newArray(int size) {
			return new QueueMeb[size];
		}
	};

}
