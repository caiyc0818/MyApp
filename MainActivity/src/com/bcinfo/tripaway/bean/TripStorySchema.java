package com.bcinfo.tripaway.bean;

import java.util.List;

public class TripStorySchema {
	private String photoId;
	private String description;
	private String publishTime;
	private String location;
	private String publisher;
	
	private String isLike;
	private String likes;
	
	private boolean isCheck=false;
	public boolean isCheck() {
		return isCheck;
	}


	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}


	private int picCount=0;
public int getPicCount() {
		return picCount;
	}


	public void setPicCount(int picCount) {
		this.picCount = picCount;
	}


private List<Comments> comments;


public String getPhotoId() {
	return photoId;
}


public void setPhotoId(String photoId) {
	this.photoId = photoId;
}


public String getDescription() {
	return description;
}


public void setDescription(String description) {
	this.description = description;
}


public String getPublishTime() {
	return publishTime;
}


public void setPublishTime(String publishTime) {
	this.publishTime = publishTime;
}


public String getLocation() {
	return location;
}


public void setLocation(String location) {
	this.location = location;
}


public String getPublisher() {
	return publisher;
}


public void setPublisher(String publisher) {
	this.publisher = publisher;
}


public String getIsLike() {
	return isLike;
}


public void setIsLike(String isLike) {
	this.isLike = isLike;
}


public String getLikes() {
	return likes;
}


public void setLikes(String likes) {
	this.likes = likes;
}


public List<Comments> getComments() {
	return comments;
}


public void setComments(List<Comments> comments) {
	this.comments = comments;
}


public List<ImageInfo> getPicture() {
	return picture;
}


public void setPicture(List<ImageInfo> picture) {
	this.picture = picture;
}


private List<ImageInfo> picture;


}
