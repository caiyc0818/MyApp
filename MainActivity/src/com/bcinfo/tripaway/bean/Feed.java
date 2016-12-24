package com.bcinfo.tripaway.bean;

import java.util.HashMap;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Feed implements Parcelable {

	private String publisher;

	private String publisherId;

	private String desc;

	private String publishTime;
	/**
	 * 目前支持  tripstory  微游记  story 游记  product 产品 user 关注
	 */
	private String feedType;

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	private HashMap<String, Object> map;

	public Feed() {

	}

	public Feed(Parcel in) {
		publisher = in.readString();
		publisherId = in.readString();
		desc = in.readString();
		publishTime = in.readString();
		feedType = in.readString();
		map = in.readHashMap(HashMap.class.getClassLoader());
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public HashMap<String, Object> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(publishTime);
		dest.writeString(publisher);
		dest.writeString(publisherId);
		dest.writeString(desc);
		dest.writeString(feedType);
		dest.writeMap(map);
	}

	public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {

		@Override
		public Feed createFromParcel(Parcel source) {

			return new Feed(source);
		}

		@Override
		public Feed[] newArray(int size) {

			return new Feed[size];
		}
	};
}
