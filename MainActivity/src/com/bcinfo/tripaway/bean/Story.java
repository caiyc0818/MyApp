package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 游记
 * 
 * @function
 * @author 
 * @version 1.0 2015年12月9日 17:02:12
 */
public class Story implements Parcelable{

	/**
     * 游记 发布时间 (精确到 年月日时分秒)
     */
    private String publishTime;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 摘要
     */
    private String abstracts;
    /**
     * 游记id
     */
    private String articleId;
    /**
     * 封面图
     */
    private String cover;
    


	/**
     * 游记 发布者publisher
     */
    private UserInfo publisher = new UserInfo();
    
    /**
     * 游记 资源图片
     */
    private ArrayList<ImageInfo> pictureList = new ArrayList<ImageInfo>();
    
    /**
     * 评论条数
     */
    private String comments;
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Story()
    {
        super();
        
    }
    
    public Story(Parcel in)
    {
    	publishTime = in.readString();
        title = in.readString();
        abstracts = in.readString();
        articleId = in.readString();
        cover = in.readString();
        in.readValue(publisher.getClass().getClassLoader());
        in.readTypedList(pictureList, ImageInfo.CREATOR);
    }
    
    public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
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

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public UserInfo getPublisher() {
		return publisher;
	}

	public void setPublisher(UserInfo publisher) {
		this.publisher = publisher;
	}

	public ArrayList<ImageInfo> getPictureList() {
		return pictureList;
	}

	public void setPictureList(ArrayList<ImageInfo> pictureList) {
		this.pictureList = pictureList;
	}
	
    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {

		@Override
		public Story createFromParcel(Parcel source) {

			return new Story(source);
		}

		@Override
		public Story[] newArray(int size) {

			return new Story[size];
		}
	};
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(publishTime);
		dest.writeString(title);
		dest.writeString(abstracts);
		dest.writeString(articleId);
		dest.writeString(cover);
		dest.writeValue(publisher);
        dest.writeTypedList(pictureList);		
	}

}
