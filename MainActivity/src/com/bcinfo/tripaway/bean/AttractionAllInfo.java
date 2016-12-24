package com.bcinfo.tripaway.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author chengwl
 *行程景点的所有信息
 */
public class AttractionAllInfo implements Parcelable{

	private String fee;
	private String id;
	/**
	 * 景点中文名称
	 */
	private String servName;
	/**
	 * 景点排序
	 */
	private String rank;
	/**
	 * 景点英文名称
	 */
	private String servAlias;
	/**
	 * 景点标题图片
	 */
	private String titleImage;
	private String feeType;
	/**
	 * 景点门票
	 */
	private String ticket;
	/**
	 * 景点营业时间
	 */
	private String business_hours;
	/**
	 * 景点介绍（非行程日介绍）
	 */
	private String servDesc;
	private String availableTime;
	/**
	 * 景点类型（poi，cate，shopping）
	 */
	private String servType;
	private String resourceExt;
	public String getResourceExt() {
		return resourceExt;
	}
	public void setResourceExt(String resourceExt) {
		this.resourceExt = resourceExt;
	}
	/**
	 * 标签数组
	 */
	private ArrayList<String> tags=new ArrayList<String>();
	/**
	 * 景点图片数组
	 */
	private ArrayList<ImageInfo> images=new ArrayList<ImageInfo>();
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		out.writeString(fee);
		out.writeString(id);
		out.writeString(servName);
		out.writeString(rank);
		out.writeString(servAlias);
		out.writeString(titleImage);
		out.writeString(feeType);
		out.writeString(ticket);
		out.writeString(business_hours);
		out.writeString(servDesc);
		out.writeString(availableTime);
		out.writeString(servType);
		out.writeString(resourceExt);
		
		out.writeStringList(tags);
		out.writeTypedList(images);
		
	}
	public static final Parcelable.Creator<AttractionAllInfo> CREATOR=new Parcelable.Creator<AttractionAllInfo>(){

		@Override
		public AttractionAllInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new AttractionAllInfo(source);
		}

		@Override
		public AttractionAllInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AttractionAllInfo[size];
		}
		 
	 };
	 public AttractionAllInfo(Parcel in)
	    {
		 fee=in.readString();
		 id = in.readString();
		 servName= in.readString();
		 rank= in.readString();
		 servAlias= in.readString();
		 titleImage= in.readString();
		 feeType= in.readString();
		 ticket= in.readString();
		 business_hours  = in.readString();
		 servDesc = in.readString();
		 availableTime = in.readString();
		 servType = in.readString();
		 resourceExt = in.readString();
	     in.readStringList(tags);
	     in.readTypedList(images, ImageInfo.CREATOR);
		}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServName() {
		return servName;
	}
	public void setServName(String servName) {
		this.servName = servName;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getServAlias() {
		return servAlias;
	}
	public void setServAlias(String servAlias) {
		this.servAlias = servAlias;
	}
	public String getTitleImage() {
		return titleImage;
	}
	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getBusiness_hours() {
		return business_hours;
	}
	public void setBusiness_hours(String business_hours) {
		this.business_hours = business_hours;
	}
	public String getServDesc() {
		return servDesc;
	}
	public void setServDesc(String servDesc) {
		this.servDesc = servDesc;
	}
	public String getAvailableTime() {
		return availableTime;
	}
	public void setAvailableTime(String availableTime) {
		this.availableTime = availableTime;
	}
	public String getServType() {
		return servType;
	}
	public void setServType(String servType) {
		this.servType = servType;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public ArrayList<ImageInfo> getImages() {
		return images;
	}
	public void setImages(ArrayList<ImageInfo> images) {
		this.images = images;
	}
	public AttractionAllInfo() {
		//super();不用？？？
	}
	
	
	
	
}
