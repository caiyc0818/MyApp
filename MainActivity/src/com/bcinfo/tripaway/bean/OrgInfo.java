package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class OrgInfo implements Parcelable {

	private String orgBackground;
	
	private String isFocus;
	
	private String introduce;
	
	private String foundDay;
	public String getOrgBackground() {
		return orgBackground;
	}

	public void setOrgBackground(String orgBackground) {
		this.orgBackground = orgBackground;
	}
	
	private String orgName;
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	private String productCount;
	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	
	private String articleNum;
	public String getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(String articleNum) {
		this.articleNum = articleNum;
	}

	private String fansCount;
	public String getFansCount() {
		return fansCount;
	}

	public void setFansCount(String fansCount) {
		this.fansCount = fansCount;
	}
	
	private String members;
	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}
	
	private List<String> tags=new ArrayList<String>();
	public List<String> getTags() {
		return tags;
	}
	
	public String getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(String isFocus) {
		this.isFocus = isFocus;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getFoundDay() {
		return foundDay;
	}

	public void setFoundDay(String foundDay) {
		this.foundDay = foundDay;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(orgBackground);
		dest.writeString(isFocus);
		dest.writeString(foundDay);
		dest.writeString(introduce);
		dest.writeString(orgName);
		dest.writeString(id);
		dest.writeString(productCount);
		dest.writeString(articleNum);
		dest.writeString(fansCount);
		dest.writeString(members);
		dest.writeList(tags);
	}

	public OrgInfo(){
		
	}
	public OrgInfo(Parcel in) {
		orgBackground = in.readString();
		isFocus = in.readString();
		foundDay = in.readString();
		introduce = in.readString();
		orgName = in.readString();
		id = in.readString();
		productCount = in.readString();
		articleNum = in.readString();
		fansCount = in.readString();
		members = in.readString();
		in.readStringList(tags);
	}
	
    public static final Parcelable.Creator<OrgInfo> CREATOR = new Parcelable.Creator<OrgInfo>()
    {
        
        @Override
        public OrgInfo createFromParcel(Parcel source)
        {
            
            return new OrgInfo(source);
        }
        
        @Override
        public OrgInfo[] newArray(int size)
        {
            
            return new OrgInfo[size];
        }
        
    };
}