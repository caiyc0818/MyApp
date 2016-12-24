package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FamousComment implements Parcelable {

	private String comment;

	private String name;
	


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
		dest.writeString(comment);
		dest.writeString(name);
	}

	public FamousComment() {

	}
	
	public FamousComment(String name,String comment) {
this.name=name;
this.comment=comment;
	}

	public FamousComment(Parcel in) {
		comment = in.readString();
		name = in.readString();
	}

	public static final Parcelable.Creator<FamousComment> CREATOR = new Parcelable.Creator<FamousComment>() {
		public FamousComment createFromParcel(Parcel source) {
			return new FamousComment(source);
		}

		public FamousComment[] newArray(int size) {
			return new FamousComment[size];
		}
	};
}
