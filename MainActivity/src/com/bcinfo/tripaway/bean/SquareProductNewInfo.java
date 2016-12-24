package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umeng.socialize.controller.LikeService;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的产品
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月24日 下午2:26:49
 */
public class SquareProductNewInfo implements Parcelable
{
    private List<String> topics = new ArrayList<String>();// 主题列表
    
    private String id;// 产品ID
    
    private String title;// 产品标题
    
    private String total;// 产品标题
    
    private String description;// 产品描述
    
    private String productType;// 产品类型
    
    private ArrayList<String> pictures = new ArrayList<String>();// 产品logo URL
    
    private String titleImg;// 标题大图
    
    private String hasDeposit;//是否支付定金
    
    public String getHasDeposit() {
		return hasDeposit;
	}

	public void setHasDeposit(String hasDeposit) {
		this.hasDeposit = hasDeposit;
	}

	private List<String> location = new ArrayList<String>();// 目的地
    
    private String days;// 天数
    
    private String distance;// 距离
    
    private String beginTime;// 出发时间（团产品）招募
    
    private String endTime;// 结束时间（团产品)招募
    
    private String expStart;// 体验开始时间（团产品）
    
    private String expend;// 体验结束时间（团产品）
    
    private String customFor;// 订制给某人
    
    private String services;// 产品提供服务,名称用逗号隔开
    
    private String price;// 最低价格
    
    private String priceMax;// 最高价格
    
    private String maxMember;// 最大成员数（团产品）
    
    private String poiCount;// 景点个数
    
    private String quickPayment;
    
    private String level;
    
    private String priceFrequency;
    
    private String isFav;
    
    private String policyContent;
    /**
     * 原价（市场价）
     */
    private String originalPrice;
    /**
     * 浏览人数
     */
    private  String pv;
    private  String topbackground;
    private  String topid;
    private  String topsubTitle;
    private  String toptitle;
   
    
    
    
    
 
    
    public String getTopbackground() {
		return topbackground;
	}

	public void setTopbackground(String topbackground) {
		this.topbackground = topbackground;
	}

	public String getTopid() {
		return topid;
	}

	public void setTopid(String topid) {
		this.topid = topid;
	}

	public String getTopsubTitle() {
		return topsubTitle;
	}

	public void setTopsubTitle(String topsubTitle) {
		this.topsubTitle = topsubTitle;
	}

	public String getToptitle() {
		return toptitle;
	}

	public void setToptitle(String toptitle) {
		this.toptitle = toptitle;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	private HashMap<String,String> exts;
    
    public String getPolicyContent() {
		return policyContent;
	}

	public void setPolicyContent(String policyContent) {
		this.policyContent = policyContent;
	}

	/*
     * 可用时间信息key: beginDate 开始时间value: endDate 结束时间
     */
    private HashMap<String, String> availableTime = new HashMap<String, String>();
    
    private ArrayList<FeatureInfo> features = new ArrayList<FeatureInfo>();// 产品特色列表
    
    private UserInfo user = new UserInfo();// 服务者信息
    
    private List<AvailableTime> expPeriodList = new ArrayList<AvailableTime>();
    
    // add by lij 2015/09/25 start
    private String memberJoinCount;// 参与人数
    // add by lij 2015/09/25 end
    
    private String servTime;
    
    private String showButton;
    
    private String feature;
    
    private String productCode;
    
    public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public UserInfo getUser()
    {
        return user;
    }
    
    public void setUser(UserInfo user)
    {
        this.user = user;
    }
    
    public SquareProductNewInfo()
    {
    }
    
    public String getLevel()
    {
        return level;
    }
    
    public void setLevel(String level)
    {
        this.level = level;
    }
    
    public List<String> getTopics()
    {
        return topics;
    }
    
    public void setTopics(List<String> topics)
    {
        this.topics = topics;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTotal(String total)
    {
        this.total = total;
    }
    
    public String getTotal()
    {
        return total;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getProductType()
    {
        return productType;
    }
    
    public void setProductType(String productType)
    {
        this.productType = productType;
    }
    
    public ArrayList<String> getPictures()
    {
        return pictures;
    }
    
    public void setPictures(ArrayList<String> pictures)
    {
        this.pictures = pictures;
    }
    
    public String getTitleImg()
    {
        return titleImg;
    }
    
    public void setTitleImg(String titleImg)
    {
        this.titleImg = titleImg;
    }
    
    public List<String> getLocation()
    {
        return location;
    }
    
    public void setLocation(List<String> location)
    {
        this.location = location;
    }
    
    public String getDays()
    {
        return days;
    }
    
    public void setDays(String days)
    {
        this.days = days;
    }
    
    public String getDistance()
    {
        return distance;
    }
    
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    
    public String getBeginTime()
    {
        return beginTime;
    }
    
    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public String getExpStart()
    {
        return expStart;
    }
    
    public void setExpStart(String expStart)
    {
        this.expStart = expStart;
    }
    
    public String getExpend()
    {
        return expend;
    }
    
    public void setExpend(String expend)
    {
        this.expend = expend;
    }
    
    public String getCustomFor()
    {
        return customFor;
    }
    
    public void setCustomFor(String customFor)
    {
        this.customFor = customFor;
    }
    
    public String getServices()
    {
        return services;
    }
    
    public void setServices(String services)
    {
        this.services = services;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    public String getPriceMax()
    {
        return priceMax;
    }
    
    public void setPriceMax(String priceMax)
    {
        this.priceMax = priceMax;
    }
    
    public String getMaxMember()
    {
        return maxMember;
    }
    
    public void setMaxMember(String maxMember)
    {
        this.maxMember = maxMember;
    }
    
    public String getPoiCount()
    {
        return poiCount;
    }
    
    public void setPoiCount(String poiCount)
    {
        this.poiCount = poiCount;
    }
    
    public HashMap<String, String> getAvailableTime()
    {
        return availableTime;
    }
    
    public void setAvailableTime(HashMap<String, String> availableTime)
    {
        this.availableTime = availableTime;
    }
    
    public ArrayList<FeatureInfo> getFeatures()
    {
        return features;
    }
    
    public void setFeatures(ArrayList<FeatureInfo> features)
    {
        this.features = features;
    }
    
    public String getQuickPayment()
    {
        return quickPayment;
    }
    
    public void setQuickPayment(String quickPayment)
    {
        this.quickPayment = quickPayment;
    }
    
    public List<AvailableTime> getExpPeriodList()
    {
        return expPeriodList;
    }
    
    public void setExpPeriodList(List<AvailableTime> expPeriodList)
    {
        this.expPeriodList = expPeriodList;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<SquareProductNewInfo> CREATOR = new Parcelable.Creator<SquareProductNewInfo>()
    {
        public SquareProductNewInfo createFromParcel(Parcel source)
        {
            return new SquareProductNewInfo(source);
        }
        
        public SquareProductNewInfo[] newArray(int size)
        {
            return new SquareProductNewInfo[size];
        }
    };
    
    public SquareProductNewInfo(Parcel in)
    {
        in.readStringList(topics);
        id = in.readString();
        title = in.readString();
        description = in.readString();
        productType = in.readString();
        in.readStringList(pictures);
        titleImg = in.readString();
        in.readStringList(location);
        days = in.readString();
        distance = in.readString();
        beginTime = in.readString();
        endTime = in.readString();
        expStart = in.readString();
        expend = in.readString();
        customFor = in.readString();
        services = in.readString();
        price = in.readString();
        total=in.readString();
        priceMax = in.readString();
        maxMember = in.readString();
        poiCount = in.readString();
        quickPayment = in.readString();
        level = in.readString();
        in.readHashMap(availableTime.getClass().getClassLoader());
        in.readTypedList(features, FeatureInfo.CREATOR);
        in.readTypedList(expPeriodList, AvailableTime.CREATOR);
        user = in.readParcelable(UserInfo.class.getClassLoader());
        servTime = in.readString();
        priceFrequency = in.readString();
        isFav = in.readString();
        showButton = in.readString();
        timeUnit=in.readString();
        feature = in.readString();
        policyContent = in.readString();
        productCode=in.readString();
        exts =in.readHashMap(HashMap.class.getClassLoader());
        hasDeposit = in.readString();
        originalPrice = in.readString();
        pv=in.readString(); 
        topbackground = in.readString();
        topid = in.readString();
        topsubTitle=in.readString();
        toptitle=in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeStringList(topics);
        out.writeString(id);
        out.writeString(title);
        out.writeString(description);
        out.writeString(productType);
        out.writeStringList(pictures);
        out.writeString(titleImg);
        out.writeStringList(location);
        out.writeString(days);
        out.writeString(distance);
        out.writeString(beginTime);
        out.writeString(endTime);
        out.writeString(expStart);
        out.writeString(expend);
        out.writeString(customFor);
        out.writeString(services);
        out.writeString(price);
        out.writeString(total);
        out.writeString(priceMax);
        out.writeString(maxMember);
        out.writeString(poiCount);
        out.writeString(quickPayment);
        out.writeString(level);
        out.writeMap(availableTime);
        out.writeTypedList(features);
        out.writeTypedList(expPeriodList);
        out.writeParcelable(user, 0);
        out.writeString(servTime);
        out.writeString(priceFrequency);
        out.writeString(isFav);
        out.writeString(showButton);
        out.writeString(timeUnit);
        out.writeString(feature);
        out.writeString(policyContent);
        out.writeString(productCode);
        out.writeMap(exts);
        out.writeString(hasDeposit);
        out.writeString(originalPrice);
        out.writeString(pv);
        out.writeString(topbackground);
        out.writeString(topid);
        out.writeString(topsubTitle);
        out.writeString(topsubTitle);
    }

	public String getMemberJoinCount() {
		return memberJoinCount;
	}

	public void setMemberJoinCount(String memberJoinCount) {
		this.memberJoinCount = memberJoinCount;
	}

	public String getServTime() {
		return servTime;
	}

	public void setServTime(String servTime) {
		this.servTime = servTime;
	}

	/**
	 * @return the priceFrequency
	 */
	public String getPriceFrequency() {
		return priceFrequency;
	}

	/**
	 * @param priceFrequency the priceFrequency to set
	 */
	public void setPriceFrequency(String priceFrequency) {
		this.priceFrequency = priceFrequency;
	}

	/**
	 * @return the isFav
	 */
	public String getIsFav() {
		return isFav;
	}

	/**
	 * @param isFav the isFav to set
	 */
	public void setIsFav(String isFav) {
		this.isFav = isFav;
	}

	/**
	 * @return the showButton
	 */
	public String getShowButton() {
		return showButton;
	}

	/**
	 * @param showButton the showButton to set
	 */
	public void setShowButton(String showButton) {
		this.showButton = showButton;
	}
	
	
	 private String timeUnit;
	 
	 
		/**
		 * @return the timeUnit
		 */
		public String getTimeUnit() {
			return timeUnit;
		}

		/**
		 * @param timeUnit the timeUnit to set
		 */
		public void setTimeUnit(String timeUnit) {
			this.timeUnit = timeUnit;
		}

		/**
		 * @return the feature
		 */
		public String getFeature() {
			return feature;
		}

		/**
		 * @param feature the feature to set
		 */
		public void setFeature(String feature) {
			this.feature = feature;
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
		
}
