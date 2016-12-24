package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.UserInfo;

/**
 * (旅行故事)
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月18日 14:10:12
 */
public class ArticleInfo implements Parcelable {

	/**
	 * 发布时间 (精确到 年月日时分秒)
	 */

	private String publishTime;

	private String abstracts;

	private String content;

	private String subTitle;

	private String title;

	private String type;

	private String source;

	private OrgInfo orgInfo;

	private String cover;

	private String comments;

	private String articleId;

	
	/**
	 * 发布者publisher
	 */
	private UserInfo publisher ;

	/**
	 * 资源图片
	 */
	private ArrayList<ImageInfo> pictureList ;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public OrgInfo getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrgInfo orgInfo) {
		this.orgInfo = orgInfo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public UserInfo getPublisher() {
		return publisher;
	}

	public void setPublisher(UserInfo publisher) {
		this.publisher = publisher;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArticleInfo() {
		super();

	}

	@Override
	public String toString() {
		return "ArticleInfo [publishTime=" + publishTime + ", abstracts=" + abstracts + ", content=" + content
				+ ", subTitle=" + subTitle + ", title=" + title + ", type=" + type + ", source=" + source + ", orgInfo="
				+ orgInfo + ", cover=" + cover + ", comments=" + comments + ", articleId=" + articleId + ", publisher="
				+ publisher + ", pictureList=" + pictureList + "]";
	}

	public ArticleInfo(Parcel in) {

		publishTime = in.readString();
		abstracts = in.readString();
		content = in.readString();
		subTitle = in.readString();
		title = in.readString();
		type = in.readString();
		source = in.readString();
		orgInfo=in.readParcelable(OrgInfo.class.getClassLoader());
		cover = in.readString();
		comments = in.readString();
		articleId = in.readString();

		// in.readArrayList(pictureList.getClass().getClassLoader());
//		in.readTypedList(pictureList, ImageInfo.CREATOR);
//		publisher=in.readParcelable(UserInfo.class.getClassLoader());
	}

	public static final Parcelable.Creator<ArticleInfo> CREATOR = new Parcelable.Creator<ArticleInfo>()

	{

		@Override
		public ArticleInfo createFromParcel(Parcel source) {

			return new ArticleInfo(source);
		}

		@Override
		public ArticleInfo[] newArray(int size) {

			return new ArticleInfo[size];
		}

	};

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(publishTime);
		dest.writeString(abstracts);
		dest.writeString(content);
		dest.writeString(subTitle);
		dest.writeString(title);
		dest.writeString(type);
		dest.writeString(source);
		dest.writeParcelable(orgInfo, 0);
		dest.writeString(cover);
		dest.writeString(comments);
		dest.writeString(articleId);
//		dest.writeTypedList(pictureList);
//		dest.writeParcelable(publisher,0);
	}

}
