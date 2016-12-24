package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.HashMap;
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
public class UserInfo implements Parcelable
{
	private String certifyCode;
	private Tags tag;
	private String isFocus;
	private String orgCreator;
	private String servTimes;
	private String members;
    private String realName;
    private HashMap<String,String> exts;
    private String isGold;
    private String background;
    private String brief;
    private ArrayList<String> profession =new ArrayList<String>();
    private String occ;
    
    private String roleName;
   
    /**
     * 用户是否认证
     */
    private String isCertified ;
    
    private String isTalent;
    
    public String getIsTalent() {
		return isTalent;
	}

	public void setIsTalent(String isTalent) {
		this.isTalent = isTalent;
	}

	public String getIsCertified() {
		return isCertified;
	}

	public void setIsCertified(String isCertified) {
		this.isCertified = isCertified;
	}

	/**
     * 用户id
     */
    private String userId = "";
    
    /**
     * 用户的年纪
     */
    private String age;
    
    /**
     * 性别 0:女 1:男
     */
    private String gender;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
     * 用户的经历
     */
    private ArrayList<Experiences> experiencesList = new ArrayList<Experiences>();
    
    /**
     * 用户的职业
     */
    private String job;
    
    /**
     * 用户的教育程度
     */
    private String education;
    
    private String location;
    /**
     * 用户的语言
     */
    private ArrayList<String> languagesList = new ArrayList<String>();
    
    /**
     * 用户毕业的学校
     */
    private String school;
    
    /**
     * 用户的email
     */
    private String email;
    
    /**
     * 用户的地址
     */
    private String address;
    
    /**
     * 用户的电话号码
     */
    private String mobile;
    
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 挂靠单位
     */
    private String orgInfo;
    
    
    private OrgRole orgRole;
    
    public String getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(String orgInfo) {
		this.orgInfo = orgInfo;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
     * 个人介绍
     */
    private String introduction;
    
    /**
     * 邮箱验证
     */
    private String hasEmail;
    
    /**
     * 手机验证
     */
    private String hasMobile;
    
    /**
     * 身份验证
     */
    private String hasIdentity;
    private String userName;
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
     * 用户状态 (init/close)
     */
    private String status;
    
    /**
     * 用户标签 list集合
     */
    private ArrayList<String> tags = new ArrayList<String>();
    
    /**
     * 关注个数
     */
    private String focus;
    
    /**
     * 粉丝个数
     */
    private String fansCount;
    
    /**
     * 微游记数
     */
    private String storeNum;
    /**
     * 用户类型
     */
    private String userType;
    
    public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
     * 拥有产品的个数
     */
    private String productCount;
    
    private String usersIdentity;
    
    private String role;
    
    private String permission;
    
    private String account;
    
    private String serverPolicy;
    
    
    private List<FamousComment> famousCommentsList = new ArrayList<FamousComment>();
    public List<FamousComment> getFamousCommentsList()
    {
        return famousCommentsList;
    }
    
    public void setFamousCommentsList(List<FamousComment> famousCommentsList)
    {
        this.famousCommentsList = famousCommentsList;
    }
    
    public String getAccount()
    {
        return account;
    }
    
    public void setAccount(String account)
    {
        this.account = account;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    public void setPermission(String permission)
    {
        this.permission = permission;
    }
    
    public String getRole()
    {
        return role;
    }
    
    public void setRole(String role)
    {
        this.role = role;
    }
    
    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public String getGender()
    {
        return gender;
    }
    
    public void setGender(String gender)
    {
        this.gender = gender;
    }
    
    public String getAvatar()
    {
        return avatar;
    }
    
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }
    
    public String getNickname()
    {
        return nickname;
    }
    
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }
    
    public String getIntroduction()
    {
        return introduction;
    }
    
    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }
    
    public String getHasEmail()
    {
        return hasEmail;
    }
    
    public void setHasEmail(String hasEmail)
    {
        this.hasEmail = hasEmail;
    }
    
    public String getHasMobile()
    {
        return hasMobile;
    }
    
    public void setHasMobile(String hasMobile)
    {
        this.hasMobile = hasMobile;
    }
    
    public String getHasIdentity()
    {
        return hasIdentity;
    }
    
    public void setHasIdentity(String hasIdentity)
    {
        this.hasIdentity = hasIdentity;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public ArrayList<String> getTags()
    {
        return tags;
    }
    
    public void setTags(ArrayList<String> tags)
    {
        this.tags = tags;
    }
    
    public String getFocus()
    {
        return focus;
    }
    
    public void setFocus(String focus)
    {
        this.focus = focus;
    }
    
    public String getFansCount()
    {
        return fansCount;
    }
    
    public void setFansCount(String fansCount)
    {
        this.fansCount = fansCount;
    }
    
    public String getStoreNum()
    {
        return storeNum;
    }
    
    public void setStoreNum(String storeNum)
    {
        this.storeNum = storeNum;
    }
    
    public String getProductCount()
    {
        return productCount;
    }
    
    public void setProductCount(String productCount)
    {
        this.productCount = productCount;
    }
    
    public String getAge()
    {
        return age;
    }
    
    public void setAge(String age)
    {
        this.age = age;
    }
    
    public ArrayList<Experiences> getExperiencesList()
    {
        return experiencesList;
    }
    
    public void setExperiencesList(ArrayList<Experiences> experiencesList)
    {
        this.experiencesList = experiencesList;
    }
    
    public String getJob()
    {
        return job;
    }
    
    public void setJob(String job)
    {
        this.job = job;
    }
    
    public String getEducation()
    {
        return education;
    }
    
    public void setEducation(String education)
    {
        this.education = education;
    }
    
    public ArrayList<String> getLanguagesList()
    {
        return languagesList;
    }
    
    public void setLanguagesList(ArrayList<String> languagesList)
    {
        this.languagesList = languagesList;
    }
    
    public String getSchool()
    {
        return school;
    }
    
    public void setSchool(String school)
    {
        this.school = school;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getMobile()
    {
        return mobile;
    }
    
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
    public String getUsersIdentity()
    {
        return usersIdentity;
    }
    
    public void setUsersIdentity(String usersIdentity)
    {
        this.usersIdentity = usersIdentity;
    }
    
    public String getRealName()
    {
        return realName;
    }
    
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    
    private List<String> referPics = new ArrayList<String>();
    
    public List<String> getReferPics()
    {
        return this.referPics;
    }
    
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>()
    {
        
        @Override
        public UserInfo createFromParcel(Parcel source)
        {
            
            return new UserInfo(source);
        }
        
        @Override
        public UserInfo[] newArray(int size)
        {
            
            return new UserInfo[size];
        }
        
    };
    
    public UserInfo()
    {
        super();
        
    }
    
    public UserInfo(Parcel in)
    {
        userId = in.readString();
        age = in.readString();
        gender = in.readString();
        avatar = in.readString();
        nickname = in.readString();
        introduction = in.readString();
        hasEmail = in.readString();
        hasMobile = in.readString();
        hasIdentity = in.readString();
        status = in.readString();
        in.readStringList(tags);
        focus = in.readString();
        fansCount = in.readString();
        storeNum = in.readString();
        productCount = in.readString();
        job = in.readString();
        education = in.readString();
        in.readTypedList(experiencesList, Experiences.CREATOR);
        in.readStringList(languagesList);
        school = in.readString();
        isCertified= in.readString();
        email = in.readString();
        address = in.readString();
        mobile = in.readString();
        role = in.readString();
        permission = in.readString();
        usersIdentity = in.readString();
        account = in.readString();
        realName = in.readString();
        orgInfo= in.readString();
        userType = in.readString();
        serverPolicy = in.readString();
        orgRole = in.readParcelable(OrgRole.class.getClassLoader());
        location = in.readString();
        servTimes= in.readString();
        members = in.readString();
        orgCreator =in.readString();
        exts =in.readHashMap(HashMap.class.getClassLoader());
        isFocus = in.readString();
        tag =in.readParcelable(Tags.class.getClassLoader());
        certifyCode =in.readString();
        isGold = in.readString();
        background = in.readString();
        brief = in.readString();
        in.readStringList(profession);
        occ = in.readString();
        in.readTypedList(famousCommentsList, FamousComment.CREATOR);
        in.readStringList(referPics);
        isTalent=in.readString();
        
    }
    
    /**
     * 简化的userInfo 构造器
     * 
     * @param userId
     * @param gender
     * @param avatar
     * @param nickname
     */
    public UserInfo(String userId, String isCertified,String gender, String avatar, String nickname, String introduction, String status,
        String mobile, String email)
    {
        super();
        this.userId = userId;
        this.gender = gender;
        this.isCertified = isCertified;
        this.avatar = avatar;
        this.nickname = nickname;
        this.introduction = introduction;
        this.status = status;
        this.mobile = mobile;
        this.email = email;
    }
    
    @Override
    public int describeContents()
    {
        
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(userId);
        out.writeString(age);
        out.writeString(gender);
        out.writeString(avatar);
        out.writeString(nickname);
        out.writeString(introduction);
        out.writeString(hasEmail);
        out.writeString(hasMobile);
        out.writeString(hasIdentity);
        out.writeString(status);
        out.writeStringList(tags);
        out.writeString(focus);
        out.writeString(fansCount);
        out.writeString(storeNum);
        out.writeString(productCount);
        out.writeString(job);
        out.writeString(education);
        out.writeTypedList(experiencesList);
        out.writeStringList(languagesList);
        out.writeString(school);
        out.writeString(isCertified);
        out.writeString(email);
        out.writeString(address);
        out.writeString(mobile);
        out.writeString(role);
        out.writeString(permission);
        out.writeString(usersIdentity);
        out.writeString(account);
        out.writeString(realName);
        out.writeString(orgInfo);
        out.writeString(userType);
        out.writeString(serverPolicy);
        out.writeParcelable(orgRole, 0);
        out.writeString(location);
        out.writeString(servTimes);
        out.writeString(members);
        out.writeString(orgCreator);
        out.writeMap(exts);
        out.writeString(isFocus);
        out.writeParcelable(tag, 0);
        out.writeString(certifyCode);
        out.writeString(isGold);
        out.writeString(background);
        out.writeString(brief);
        out.writeStringList(profession);
        out.writeString(occ);
        out.writeTypedList(famousCommentsList);
        out.writeStringList(referPics);
        out.writeString(isTalent);
    }

	/**
	 * @return the serverPolicy
	 */
	public String getServerPolicy() {
		return serverPolicy;
	}

	/**
	 * @param serverPolicy the serverPolicy to set
	 */
	public void setServerPolicy(String serverPolicy) {
		this.serverPolicy = serverPolicy;
	}

	/**
	 * @return the orgRole
	 */
	public OrgRole getOrgRole() {
		return orgRole;
	}

	/**
	 * @param orgRole the orgRole to set
	 */
	public void setOrgRole(OrgRole orgRole) {
		this.orgRole = orgRole;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the servTimes
	 */
	public String getServTimes() {
		return servTimes;
	}

	/**
	 * @param servTimes the servTimes to set
	 */
	public void setServTimes(String servTimes) {
		this.servTimes = servTimes;
	}

	/**
	 * @return the members
	 */
	public String getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(String members) {
		this.members = members;
	}

	/**
	 * @return the orgCreator
	 */
	public String getOrgCreator() {
		return orgCreator;
	}

	/**
	 * @param orgCreator the orgCreator to set
	 */
	public void setOrgCreator(String orgCreator) {
		this.orgCreator = orgCreator;
	}

	/**
	 * @return the exts
	 */
	public HashMap<String,String> getExts() {
		return exts;
	}

	/**
	 * @param exts the exts to set
	 */
	public void setExts(HashMap<String,String> exts) {
		this.exts = exts;
	}

	/**
	 * @return the isFocus
	 */
	public String getIsFocus() {
		return isFocus;
	}

	/**
	 * @param isFocus the isFocus to set
	 */
	public void setIsFocus(String isFocus) {
		this.isFocus = isFocus;
	}

	/**
	 * @return the tag
	 */
	public Tags getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Tags tag) {
		this.tag = tag;
	}

	/**
	 * @return the certifyCode
	 */
	public String getCertifyCode() {
		return certifyCode;
	}

	/**
	 * @param certifyCode the certifyCode to set
	 */
	public void setCertifyCode(String certifyCode) {
		this.certifyCode = certifyCode;
	}

	/**
	 * @return the isGold
	 */
	public String getIsGold() {
		return isGold;
	}

	/**
	 * @param isGold the isGold to set
	 */
	public void setIsGold(String isGold) {
		this.isGold = isGold;
	}

	/**
	 * @return the background
	 */
	public String getBackground() {
		return background;
	}

	/**
	 * @param background the background to set
	 */
	public void setBackground(String background) {
		this.background = background;
	}

	/**
	 * @return the brief
	 */
	public String getBrief() {
		return brief;
	}

	/**
	 * @param brief the brief to set
	 */
	public void setBrief(String brief) {
		this.brief = brief;
	}

	/**
	 * @return the profession
	 */
	public ArrayList<String> getProfession() {
		return profession;
	}

	/**
	 * @param profession the profession to set
	 */
	public void setProfession(ArrayList<String> profession) {
		this.profession = profession;
	}

	/**
	 * @return the occ
	 */
	public String getOcc() {
		return occ;
	}

	/**
	 * @param occ the occ to set
	 */
	public void setOcc(String occ) {
		this.occ = occ;
	}


}
