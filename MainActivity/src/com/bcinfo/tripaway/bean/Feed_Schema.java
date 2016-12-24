package com.bcinfo.tripaway.bean;

import java.io.Serializable;

public class Feed_Schema {
	private String desc;
	private String feedType;// 全局标志
	private String publishTime;
	private String isFocus;
	private UserInfo publisher;

	// 推荐位数据
	// 产品时
	private String content;
	private String id;
	private String objectId;
	private String resTitle;
	private String obj;
	private String titleImage;
	private SquareProductNewInfo object;
	private objectParam objectParam;
	private UserInfo userInfo;
	private String defaltAvatar;
	private String defaltName;
	private String description;

	public Feed_Schema(String desc, String feedType, String publishTime, String isFocus, UserInfo publisher,
			String content, String id, String objectId, String resTitle, String obj, String titleImage,
			SquareProductNewInfo object, com.bcinfo.tripaway.bean.objectParam objectParam, UserInfo userInfo,
			String defaltAvatar, String defaltName, String description, SquareTripArticle rawData) {
		super();
		this.desc = desc;
		this.feedType = feedType;
		this.publishTime = publishTime;
		this.isFocus = isFocus;
		this.publisher = publisher;
		this.content = content;
		this.id = id;
		this.objectId = objectId;
		this.resTitle = resTitle;
		this.obj = obj;
		this.titleImage = titleImage;
		this.object = object;
		this.objectParam = objectParam;
		this.userInfo = userInfo;
		this.defaltAvatar = defaltAvatar;
		this.defaltName = defaltName;
		this.description = description;
		this.rawData = rawData;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaltAvatar() {
		return defaltAvatar;
	}

	public void setDefaltAvatar(String defaltAvatar) {
		this.defaltAvatar = defaltAvatar;
	}

	public String getDefaltName() {
		return defaltName;
	}

	public void setDefaltName(String defaltName) {
		this.defaltName = defaltName;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public objectParam getObjectParam() {
		return objectParam;
	}

	public void setObjectParam(objectParam objectParam) {
		this.objectParam = objectParam;
	}

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}

	public SquareProductNewInfo getObject() {
		return object;
	}

	public void setObject(SquareProductNewInfo object) {
		this.object = object;
	}

	public String getResTitle() {
		return resTitle;
	}

	public void setResTitle(String resTitle) {
		this.resTitle = resTitle;
	}

	private SquareTripArticle rawData;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(String isFocus) {
		this.isFocus = isFocus;
	}

	public UserInfo getPublisher() {
		return publisher;
	}

	public void setPublisher(UserInfo publisher) {
		this.publisher = publisher;
	}

	public SquareTripArticle getRawData() {
		return rawData;
	}

	public void setRawData(SquareTripArticle rawData) {
		this.rawData = rawData;
	}

	public Feed_Schema() {
		super();
		// TODO Auto-generated constructor stub
	}

}
