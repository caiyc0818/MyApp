package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DispatchInfo implements Parcelable {

	private int dispatchId;
	
	private UserInfo creator;
	
	private UserInfo dispatchTo;
	
	private String remark;
	
	private int status;
	
	private String createTime;
	
	private String updateTime;
	
	public DispatchInfo(){
		
	}
	
	public DispatchInfo(Parcel in) {
		dispatchId = in.readInt();
		creator = in.readParcelable(UserInfo.class.getClassLoader());
		dispatchTo = in.readParcelable(UserInfo.class.getClassLoader());
		remark = in.readString();
		status = in.readInt();
		createTime = in.readString();
		updateTime = in.readString();
	}
	
	public int getDispatchId() {
		return dispatchId;
	}

	public void setDispatchId(int dispatchId) {
		this.dispatchId = dispatchId;
	}

	public UserInfo getCreator() {
		return creator;
	}

	public void setCreator(UserInfo creator) {
		this.creator = creator;
	}

	public UserInfo getDispatchTo() {
		return dispatchTo;
	}

	public void setDispatchTo(UserInfo dispatchTo) {
		this.dispatchTo = dispatchTo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(dispatchId);
		dest.writeParcelable(creator, 0);
		dest.writeParcelable(dispatchTo, 0);
		dest.writeString(remark);
		dest.writeInt(status);
		dest.writeString(createTime);
		dest.writeString(updateTime);
	}
	
	public static final Parcelable.Creator<DispatchInfo> CREATOR = new Creator<DispatchInfo>() {
		
		@Override
		public DispatchInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DispatchInfo[size];
		}
		
		@Override
		public DispatchInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new DispatchInfo(source);
		}
	};

}
