package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Replys implements Parcelable{

	private String id;
	
	private String content;
	
	private String createTime;
	
	private UserInfo publisher;
	
	private UserInfo replyTo;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public UserInfo getPublisher() {
		return publisher;
	}

	public void setPublisher(UserInfo publisher) {
		this.publisher = publisher;
	}

	public UserInfo getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(UserInfo replyTo) {
		this.replyTo = replyTo;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public Replys() {
	}
	
	public Replys(Parcel in) {
		id = in.readString();
		content = in.readString();
		createTime = in.readString();
		publisher = in.readParcelable(UserInfo.class.getClassLoader());
		replyTo =  in.readParcelable(UserInfo.class.getClassLoader());
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(content);
		out.writeString(createTime);
		out.writeParcelable(publisher, 0);
		out.writeParcelable(replyTo, 0);
	}
	 public static final Parcelable.Creator<Replys> CREATOR = new Parcelable.Creator<Replys>(){

		@Override
		public Replys createFromParcel(Parcel source) {
			return new Replys(source);
		}

		@Override
		public Replys[] newArray(int size) {
			return new Replys[size];
		}
		 
	 };
}
