package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.os.Parcel;
import android.os.Parcelable;

public class ArticlleInfo1 implements Parcelable {

	private String abstracts;
	private String title;
	private String cover;

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public ArticlleInfo1(String abstracts, String title, String cover) {
		super();
		this.abstracts = abstracts;
		this.title = title;
		this.cover = cover;
	}

	public ArticlleInfo1() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArticlleInfo1(Parcel in) {

		abstracts = in.readString();

		title = in.readString();

		cover = in.readString();
	}

	public static final Parcelable.Creator<ArticlleInfo1> CREATOR = new Parcelable.Creator<ArticlleInfo1>()

	{

		@Override
		public ArticlleInfo1 createFromParcel(Parcel source) {

			return new ArticlleInfo1(source);
		}

		@Override
		public ArticlleInfo1[] newArray(int size) {

			return new ArticlleInfo1[size];
		}

	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(abstracts);
		dest.writeString(title);
		dest.writeString(cover);
	}

	public void ArticlleInfo11(Parcel in) {

		abstracts = in.readString();

		title = in.readString();

		// in.readArrayList(pictureList.getClass().getClassLoader());

		cover = in.readString();
	}

}
