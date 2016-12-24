package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TraceInfo implements Parcelable {

	//跟踪描述
	private String desc;
	//跟踪时间
	private String traceTime;
	//状态
	private String status;
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TraceInfo() {

	}

	public TraceInfo(Parcel in) {
		desc =in.readString();
		traceTime = in.readString();
		status = in.readString();
	}

	public static final Parcelable.Creator<TraceInfo> CREATOR = new Parcelable.Creator<TraceInfo>() {
		public TraceInfo createFromParcel(Parcel source) {
			return new TraceInfo(source);
		}

		public TraceInfo[] newArray(int size) {
			return new TraceInfo[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(desc);
		dest.writeString(traceTime);
		dest.writeString(status);
	}

}
