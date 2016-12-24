package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-7-16 下午5:16:53
 */
public class QueuesInfo implements Parcelable
{
    /**
     * 未读消息数
     */
    private String unread;
    
    /**
     * 队列logo
     */
    private String queueLogo;
    
    /**
     * 类型（private/team/system）
     */
    private String type;
    
    /**
     * 队列标题
     */
    private String queueTitle;
    
    /**
     * 队列标识
     */
    private String queueId;
    
    private MessageInfo messageInfo = new MessageInfo();
    
    private List<String> avatarsList = new ArrayList<String>();
    
    public QueuesInfo()
    {
        
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public String getUnread()
    {
        return unread;
    }
    
    public void setUnread(String unread)
    {
        this.unread = unread;
    }
    
    public String getQueueLogo()
    {
        return queueLogo;
    }
    
    public void setQueueLogo(String queueLogo)
    {
        this.queueLogo = queueLogo;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getQueueTitle()
    {
        return queueTitle;
    }
    
    public void setQueueTitle(String queueTitle)
    {
        this.queueTitle = queueTitle;
    }
    
    public String getQueueId()
    {
        return queueId;
    }
    
    public void setQueueId(String queueId)
    {
        this.queueId = queueId;
    }
    
    public MessageInfo getMessageInfo()
    {
        return messageInfo;
    }
    
    public void setMessageInfo(MessageInfo messageInfo)
    {
        this.messageInfo = messageInfo;
    }
    
    public List<String> getAvatarsList()
    {
        return avatarsList;
    }
    
    public void setAvatarsList(List<String> avatarsList)
    {
        this.avatarsList = avatarsList;
    }
    
    public static final Parcelable.Creator<QueuesInfo> CREATOR = new Parcelable.Creator<QueuesInfo>()
    {
        public QueuesInfo createFromParcel(Parcel source)
        {
            return new QueuesInfo(source);
        }
        
        public QueuesInfo[] newArray(int size)
        {
            return new QueuesInfo[size];
        }
    };
    
    public QueuesInfo(Parcel in)
    {
        queueId = in.readString();
        queueLogo = in.readString();
        queueTitle = in.readString();
        unread = in.readString();
        type = in.readString();
        messageInfo = in.readParcelable(MessageInfo.class.getClassLoader());
        in.readStringList(avatarsList);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(queueId);
        out.writeString(queueLogo);
        out.writeString(queueTitle);
        out.writeString(unread);
        out.writeString(type);
        out.writeParcelable(messageInfo, 0);
        out.writeStringList(avatarsList);
    }
    
}
