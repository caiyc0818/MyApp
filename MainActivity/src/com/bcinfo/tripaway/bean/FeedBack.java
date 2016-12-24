package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 好友
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年5月29日 14:00:12
 * 
 */
public class FeedBack implements Parcelable
{
  
    private String feedBackId;
    
   
    private String content;
    
  
    private String createTime;
  
    private String type;
    
   
    private String referType;
    private String  userName;
    
  
    private String status;
    
    private String operatorUserId;
    
   private String operatorNickName;
    
    public String getOperatorNickName()
    {
        return operatorNickName;
    }
    
    public void setOperatorNickName(String operatorNickName)
    {
        this.operatorNickName = operatorNickName;
    }
    
    
    public String getOperatorUserId()
    {
        return operatorUserId;
    }
    
    public void setOperatorUserId(String operatorUserId)
    {
        this.operatorUserId = operatorUserId;
    }
    
    public String getFeedBackId()
    {
        return feedBackId;
    }
    
    public void setFeedBackId(String feedBackId)
    {
        this.feedBackId = feedBackId;
    }
    
    public String getNickName()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getContent()
    {
    	return content;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getReferType()
    {
        return referType;
    }
    
    public void setReferType(String referType)
    {
        this.referType = referType;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public FeedBack()
    {
        super();
    }
    
    
    public FeedBack(Parcel in)
    {
    	feedBackId = in.readString();
    	content = in.readString();
    	createTime = in.readString();
    	type = in.readString();
        referType = in.readString();
        status = in.readString();
        userName=in.readString();
    }
    
    public static final Parcelable.Creator<FeedBack> CREATOR = new Parcelable.Creator<FeedBack>()
    {
        
        @Override
        public FeedBack createFromParcel(Parcel source)
        {
            
            return new FeedBack(source);
        }
        
        @Override
        public FeedBack[] newArray(int size)
        {
            
            return new FeedBack[size];
        }
    };
    
    @Override
    public int describeContents()
    {
        
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flag)
    {
        dest.writeString(feedBackId);
        dest.writeString(content);
        dest.writeString(createTime);
        dest.writeString(referType);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(userName);
    }
    
}
