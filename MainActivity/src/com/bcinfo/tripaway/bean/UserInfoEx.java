package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月13日 14:08:01
 * 
 */
public class UserInfoEx implements Parcelable {
	/**
	 * 车辆信息，当为组织成员、个人服务者有效，当为组织创建者或管理员无效
	 */
	private CarExt carInfo;
	
	private UserInfo userInfo;
	
	 private ArrayList<Cats> cats;
	 
	 private String customService;

	public CarExt getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(CarExt carInfo) {
		this.carInfo = carInfo;
	}

	/**
	 * 相册信息
	 */
	private List<ImageInfo> images = new ArrayList<ImageInfo>();

	private int groupChatCount;

	/**
	 * 司机数
	 */
	private int driverNum;

	public int getDriverNum() {
		return driverNum;
	}

	public void setDriverNum(int driverNum) {
		this.driverNum = driverNum;
	}

	/**
	 * 领队数
	 */
	private int leaderNum;

	public int getLeaderNum() {
		return leaderNum;
	}

	public void setLeaderNum(int leaderNum) {
		this.leaderNum = leaderNum;
	}

	/**
	 * 领队数
	 */
	private int guideNum;

	public int getGuideNum() {
		return guideNum;
	}

	public void setGuideNum(int guideNum) {
		this.guideNum = guideNum;
	}


	public static final Parcelable.Creator<UserInfoEx> CREATOR = new Parcelable.Creator<UserInfoEx>() {

		@Override
		public UserInfoEx createFromParcel(Parcel source) {

			return new UserInfoEx(source);
		}

		@Override
		public UserInfoEx[] newArray(int size) {

			return new UserInfoEx[size];
		}

	};

	public UserInfoEx() {
		super();

	}

	public UserInfoEx(Parcel in) {
		userInfo = in.readParcelable(UserInfo.class.getClassLoader());
		carInfo = in.readParcelable(CarExt.class.getClassLoader());
		driverNum = in.readInt();
		leaderNum = in.readInt();
		guideNum = in.readInt();
		groupChatCount = in.readInt();
		   in.readTypedList(cats, Cats.CREATOR);
		   customService = in.readString();
		   in.readTypedList(images, ImageInfo.CREATOR);
	}


	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(userInfo, 0);
		out.writeParcelable(carInfo, 0);
		out.writeInt(driverNum);
		out.writeInt(leaderNum);
		out.writeInt(guideNum);
		out.writeInt(groupChatCount);
	    out.writeTypedList(cats);
	    out.writeString(customService);
	    out.writeTypedList(images);
	}

	/**
	 * @return the userInfo
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo the userInfo to set
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * @return the images
	 */
	public List<ImageInfo> getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(List<ImageInfo> images) {
		this.images = images;
	}

	/**
	 * @return the groupChatCount
	 */
	public int getGroupChatCount() {
		return groupChatCount;
	}

	/**
	 * @param groupChatCount the groupChatCount to set
	 */
	public void setGroupChatCount(int groupChatCount) {
		this.groupChatCount = groupChatCount;
	}
	

	public ArrayList<Cats> getCats() {
		return cats;
	}

	public void setCats(ArrayList<Cats> cats) {
		this.cats = cats;
	}

	/**
	 * @return the customService
	 */
	public String getCustomService() {
		return customService;
	}

	/**
	 * @param customService the customService to set
	 */
	public void setCustomService(String customService) {
		this.customService = customService;
	}

}
