package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 美食
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月27日 下午3:48:09
 */
public class Tags implements Parcelable {

	private List<String> interests=new ArrayList<String>();// 兴趣类标签

	private List<String> clubTypes=new ArrayList<String>();;// 企业类别类标签

	private List<String> spheres=new ArrayList<String>();// 领域类标签

	public List<String> getInterests() {
		return interests;
	}

	public void setInterests(List<String> interests) {
		this.interests = interests;
	}

	public List<String> getClubTypes() {
		return clubTypes;
	}

	public void setClubTypes(List<String> clubTypes) {
		this.clubTypes = clubTypes;
	}

	public List<String> getSpheres() {
		return spheres;
	}

	public void setSpheres(List<String> spheres) {
		this.spheres = spheres;
	}

	public List<String> getFootprints() {
		return footprints;
	}

	public void setFootprints(List<String> footprints) {
		this.footprints = footprints;
	}

	public List<String> getServAreas() {
		return servAreas;
	}

	public void setServAreas(List<String> servAreas) {
		this.servAreas = servAreas;
	}

	private List<String> footprints=new ArrayList<String>();// 足迹类标签

	private List<String> servAreas=new ArrayList<String>();// 服务类别类标签

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<Tags> CREATOR = new Parcelable.Creator<Tags>() {
		public Tags createFromParcel(Parcel source) {
			return new Tags(source);
		}

		public Tags[] newArray(int size) {
			return new Tags[size];
		}
	};

	public Tags() {

	}

	public Tags(Parcel in) {

		in.readStringList(interests);
		in.readStringList(clubTypes);
		in.readStringList(spheres);
		in.readStringList(footprints);
		in.readStringList(servAreas);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeStringList(interests);
		out.writeStringList(clubTypes);
		out.writeStringList(spheres);
		out.writeStringList(footprints);
		out.writeStringList(servAreas);
	}
}
