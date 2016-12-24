package com.bcinfo.tripaway.bean;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

	private String articleId;

	private String title;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public List<ImageInfo> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageInfo> imageList) {
		this.imageList = imageList;
	}

	private String abstracts;

	private List<ImageInfo> imageList;
	
	private String cover;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(articleId);
		dest.writeString(title);
		dest.writeString(abstracts);
		dest.writeTypedList(imageList);
		dest.writeString(cover);
	}

	public Article() {

	}

	public Article(Parcel in) {
		articleId = in.readString();
		title = in.readString();
		abstracts = in.readString();
		in.readTypedList(imageList, ImageInfo.CREATOR);
		cover = in.readString();
	}

	/**
	 * @return the cover
	 */
	public String getCover() {
		return cover;
	}

	/**
	 * @param cover the cover to set
	 */
	public void setCover(String cover) {
		this.cover = cover;
	}

	public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
		public Article createFromParcel(Parcel source) {
			return new Article(source);
		}

		public Article[] newArray(int size) {
			return new Article[size];
		}
	};
}
