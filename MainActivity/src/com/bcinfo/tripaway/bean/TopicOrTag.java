package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TopicOrTag implements Parcelable {

	private String id;

	private String name;
	
	private String discuss;

	private String reads;
	private String cover;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getDiscuss() {
		return discuss;
	}

	public void setDiscuss(String discuss) {
		this.discuss = discuss;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}

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
		dest.writeString(discuss);
		dest.writeString(reads);
		dest.writeString(cover);
	}

	public TopicOrTag() {

	}

	public TopicOrTag(Parcel in) {
		id = in.readString();
		name = in.readString();
		reads = in.readString();
		discuss = in.readString();
		cover = in.readString();
	}

	public static final Parcelable.Creator<TopicOrTag> CREATOR = new Parcelable.Creator<TopicOrTag>() {
		public TopicOrTag createFromParcel(Parcel source) {
			return new TopicOrTag(source);
		}

		public TopicOrTag[] newArray(int size) {
			return new TopicOrTag[size];
		}
	};


}
