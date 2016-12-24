package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-7-1 下午3:57:50
 */
public class PartnerInfo implements Parcelable
{
    /**
     * 同伴证件号
     */
    private String idNo;
    
    /**
     * 同行人id
     */
    private String id;
    
    /**
     * 证件类型
     */
    private String idType;
    
    /**
     * 同行人姓名
     */
    private String realName;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 电话
     */
    private String tel;
    
    private String myself;
    
    private String passportNo;
    
    public PartnerInfo()
    {
        
    }
    
    public String getIdNo()
    {
        return idNo;
    }
    
    public void setIdNo(String idNo)
    {
        this.idNo = idNo;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getIdType()
    {
        return idType;
    }
    
    public void setIdType(String idType)
    {
        this.idType = idType;
    }
    
    public String getRealName()
    {
        return realName;
    }
    
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getTel()
    {
        return tel;
    }
    
    public void setTel(String tel)
    {
        this.tel = tel;
    }
    
    public String getMyself()
    {
        return myself;
    }
    
    public void setMyself(String myself)
    {
        this.myself = myself;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<PartnerInfo> CREATOR = new Parcelable.Creator<PartnerInfo>()
    {
        public PartnerInfo createFromParcel(Parcel source)
        {
            return new PartnerInfo(source);
        }
        
        public PartnerInfo[] newArray(int size)
        {
            return new PartnerInfo[size];
        }
    };
    
    public PartnerInfo(Parcel in)
    {
        idNo = in.readString();
        id = in.readString();
        idType = in.readString();
        realName = in.readString();
        email = in.readString();
        tel = in.readString();
        myself = in.readString();
        passportNo = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(idNo);
        out.writeString(id);
        out.writeString(idType);
        out.writeString(realName);
        out.writeString(email);
        out.writeString(tel);
        out.writeString(myself);
        out.writeString(passportNo);
    }

	/**
	 * @return the passportNo
	 */
	public String getPassportNo() {
		return passportNo;
	}

	/**
	 * @param passportNo the passportNo to set
	 */
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
}
