package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 结伴
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月21日 上午11:35:24
 */
public class GoWithNew implements Parcelable
{
    private String id;
    
    private String title;
    
    private String createTime;// 创建时间
    
    private String description;// 描述
    
    private String applyNum;
    
    private String joinNum;
    
    private String startPoint;// 出发地
    
    private String endPoint;// 目的地
    
    private String status;
    
    private String planTime;// 计划时间
    
    private String comeFrom;// 来自xx邀请
    
    private List<TopicInfo> topicInfos = new ArrayList<TopicInfo>();
    
    private UserInfo user = new UserInfo();// 发起人
    
    public GoWithNew()
    {
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
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getApplyNum()
    {
        return applyNum;
    }
    
    public void setApplyNum(String applyNum)
    {
        this.applyNum = applyNum;
    }
    
    public String getJoinNum()
    {
        return joinNum;
    }
    
    public void setJoinNum(String joinNum)
    {
        this.joinNum = joinNum;
    }
    
    public String getStartPoint()
    {
        return startPoint;
    }
    
    public void setStartPoint(String startPoint)
    {
        this.startPoint = startPoint;
    }
    
    public String getEndPoint()
    {
        return endPoint;
    }
    
    public void setEndPoint(String endPoint)
    {
        this.endPoint = endPoint;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getPlanTime()
    {
        return planTime;
    }
    
    public void setPlanTime(String planTime)
    {
        this.planTime = planTime;
    }
    
    public String getComeFrom()
    {
        return comeFrom;
    }
    
    public void setComeFrom(String comeFrom)
    {
        this.comeFrom = comeFrom;
    }
    
    public UserInfo getUser()
    {
        return user;
    }
    
    public void setUser(UserInfo user)
    {
        this.user = user;
    }
    
    public static final Parcelable.Creator<GoWithNew> CREATOR = new Parcelable.Creator<GoWithNew>()
    {
        public GoWithNew createFromParcel(Parcel source)
        {
            return new GoWithNew(source);
        }
        
        public GoWithNew[] newArray(int size)
        {
            return new GoWithNew[size];
        }
    };
    
    public GoWithNew(Parcel in)
    {
        id = in.readString();
        title = in.readString();
        createTime = in.readString();
        description = in.readString();
        applyNum = in.readString();
        joinNum = in.readString();
        startPoint = in.readString();
        endPoint = in.readString();
        status = in.readString();
        planTime = in.readString();
        comeFrom = in.readString();
        user = in.readParcelable(UserInfo.class.getClassLoader());
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(title);
        out.writeString(createTime);
        out.writeString(description);
        out.writeString(applyNum);
        out.writeString(joinNum);
        out.writeString(startPoint);
        out.writeString(endPoint);
        out.writeString(status);
        out.writeString(planTime);
        out.writeString(comeFrom);
        out.writeParcelable(user, 0);
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
