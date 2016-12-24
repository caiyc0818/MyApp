package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MyInfo implements Parcelable{

	//个人信息
	private UserInfo userInfo;
	
	//关注数
	private String focusNum;

	//粉丝数
	private String fansNum;
	
	//游记数
	private String tripstoryNum;
	
	//待支付数
	private String waitPayNum;
	
	//待出行数
	private String waitDepartNum;
	
	//未评价数
	private String waitAppraiseNum;
	
	//出行单条数
		private String itineraryNum;
		
	public String getItineraryNum() {
			return itineraryNum;
		}

		public void setItineraryNum(String itineraryNum) {
			this.itineraryNum = itineraryNum;
		}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getFocusNum() {
		return focusNum;
	}

	public void setFocusNum(String focusNum) {
		this.focusNum = focusNum;
	}

	public String getFansNum() {
		return fansNum;
	}

	public void setFansNum(String fansNum) {
		this.fansNum = fansNum;
	}

	public String getTripstoryNum() {
		return tripstoryNum;
	}

	public void setTripstoryNum(String tripstoryNum) {
		this.tripstoryNum = tripstoryNum;
	}

	public String getWaitPayNum() {
		return waitPayNum;
	}

	public void setWaitPayNum(String waitPayNum) {
		this.waitPayNum = waitPayNum;
	}

	public String getWaitDepartNum() {
		return waitDepartNum;
	}

	public void setWaitDepartNum(String waitDepartNum) {
		this.waitDepartNum = waitDepartNum;
	}

	public String getWaitAppraiseNum() {
		return waitAppraiseNum;
	}

	public void setWaitAppraiseNum(String waitAppraiseNum) {
		this.waitAppraiseNum = waitAppraiseNum;
	}

	public MyInfo(){
		
	}
	
	public MyInfo(Parcel in){
		userInfo = in.readParcelable(UserInfo.class.getClassLoader());
		focusNum = in.readString();
		fansNum =in.readString();
		tripstoryNum = in.readString();
		waitPayNum = in.readString();
		waitDepartNum =in.readString();
		waitAppraiseNum = in.readString();
		itineraryNum=in.readString();
	}
	
	public static final Parcelable.Creator<MyInfo> CREATOR = new Parcelable.Creator<MyInfo>()
		    {
		        public MyInfo createFromParcel(Parcel source)
		        {
		            return new MyInfo(source);
		        }
		        
		        public MyInfo[] newArray(int size)
		        {
		            return new MyInfo[size];
		        }
		    };
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(userInfo, 0);
		dest.writeString(focusNum);
		dest.writeString(fansNum);
		dest.writeString(tripstoryNum);
		dest.writeString(waitPayNum);
		dest.writeString(waitDepartNum);
		dest.writeString(waitAppraiseNum);
		dest.writeString(itineraryNum);
	}

}
