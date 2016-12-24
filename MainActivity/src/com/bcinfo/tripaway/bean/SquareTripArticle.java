package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.hardware.ConsumerIrManager;
import android.os.Parcel;
import android.os.Parcelable;

import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.UserInfo;

/**
 * 微游记 (旅行故事)
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月18日 14:10:12
 */
public class SquareTripArticle implements Parcelable {

	/**
	 * 微游记 发布时间 (精确到 年月日时分秒)
	 */
	private String publishTime;

	/**
	 * 微游记 标识id
	 */
	private String photoId;

	/**
	 * 微游记 赞美标记位 (0:未赞 ; 1:已赞)
	 */
	private String isLike;
	/**
	 * 微游记 赞美数
	 */
	private String likes;

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	/**
	 * 微游记 描述
	 */
	private String description;
	/**
	 * 微游记 评论数
	 */
	private String comments;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * 微游记 位置
	 */
	private String location;

	/**
	 * 微游记 发布者publisher
	 */
	private UserInfo publisher = new UserInfo();
	/**
	 * 微游记评论
	 */
	private ArrayList<Comments> commentList = new ArrayList<>();

	public ArrayList<Comments> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comments> commentList) {
		this.commentList = commentList;
	}

	/**
	 * 微游记 资源图片
	 */
	private ArrayList<ImageInfo> pictureList = new ArrayList<ImageInfo>();

	// 浏览数
	private String views;

	// 产品属性

	private String productTitle;
	private String productId;
	private String titleImg;
private String desc;

private String cover;
private String abstracts;
private String articleId;
private String access;

public String getAccess() {
	return access;
}

public void setAccess(String access) {
	this.access = access;
}

public String getArticleId() {
	return articleId;
}

public void setArticleId(String articleId) {
	this.articleId = articleId;
}

public String getAbstracts() {
	return abstracts;
}

public void setAbstracts(String abstracts) {
	this.abstracts = abstracts;
}

public String getCover() {
	return cover;
}

public void setCover(String cover) {
	this.cover = cover;
}

private ArrayList<String> tagNames = new ArrayList<>();

private  String title;

	public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

	public ArrayList<String> getTagNames() {
	return tagNames;
}

public void setTagNames(ArrayList<String> tagNames) {
	this.tagNames = tagNames;
}

	public String getDesc() {
	return desc;
}

public void setDesc(String desc) {
	this.desc = desc;
}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTitleImg() {
		return titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public ArrayList<ImageInfo> getPictureList() {
		return pictureList;
	}

	public void setPictureList(ArrayList<ImageInfo> pictureList) {
		this.pictureList = pictureList;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getIsLike() {
		return isLike;
	}

	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public UserInfo getPublisher() {
		return publisher;
	}

	public void setPublisher(UserInfo publisher) {
		this.publisher = publisher;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public SquareTripArticle() {
		super();

	}

	public SquareTripArticle(Parcel in) {
		comments = in.readString();
		publishTime = in.readString();
		isLike = in.readString();
		likes = in.readString();
		description = in.readString();
		location = in.readString();
		photoId = in.readString();
		// in.readArrayList(pictureList.getClass().getClassLoader());
		in.readTypedList(pictureList, ImageInfo.CREATOR);
		publisher = in.readParcelable(UserInfo.class.getClassLoader());
		in.readTypedList(commentList, Comments.CREATOR);
		in.readStringList(tagNames);
		views = in.readString();
		productTitle = in.readString();
		productId = in.readString();
		titleImg = in.readString();
		desc = in.readString();
		title = in.readString();
		cover = in.readString();
		abstracts = in.readString();
		 articleId = in.readString();
		 access = in.readString();
		
	}

	public static final Parcelable.Creator<SquareTripArticle> CREATOR = new Parcelable.Creator<SquareTripArticle>()

	{

		@Override
		public SquareTripArticle createFromParcel(Parcel source) {

			return new SquareTripArticle(source);
		}

		@Override
		public SquareTripArticle[] newArray(int size) {

			return new SquareTripArticle[size];
		}

	};

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(comments);
		dest.writeString(publishTime);
		dest.writeString(isLike);
		dest.writeString(likes);
		dest.writeString(description);
		dest.writeString(location);
		dest.writeString(photoId);
		dest.writeTypedList(pictureList);
		dest.writeParcelable(publisher, 0);
		dest.writeTypedList(commentList);
		dest.writeStringList(tagNames);
		dest.writeString(views);
		dest.writeString(productTitle);
		dest.writeString(productId);
		dest.writeString(titleImg);
		dest.writeString(desc);
		dest.writeString(title);
		dest.writeString(cover);
		dest.writeString(abstracts);
		dest.writeString(articleId);
		dest.writeString(access);
		
	
	}

}
