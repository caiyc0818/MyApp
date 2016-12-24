package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OrgRole implements Parcelable {

	private String roleName;
	
	private String roleCode;
	
	private String permission;
	
	private String roleType;
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(roleName);
		dest.writeString(roleCode);
		dest.writeString(roleType);
		dest.writeString(permission);
	}

	public OrgRole(){
		
	}
	public OrgRole(Parcel in) {
		roleName = in.readString();
		roleCode = in.readString();
		roleType = in.readString();
		permission = in.readString();
	}
	
    public static final Parcelable.Creator<OrgRole> CREATOR = new Parcelable.Creator<OrgRole>()
    {
        
        @Override
        public OrgRole createFromParcel(Parcel source)
        {
            
            return new OrgRole(source);
        }
        
        @Override
        public OrgRole[] newArray(int size)
        {
            
            return new OrgRole[size];
        }
        
    };
}