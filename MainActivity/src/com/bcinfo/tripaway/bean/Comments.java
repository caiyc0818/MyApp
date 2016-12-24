package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-6-25 下午5:31:43
 */
public class Comments implements Parcelable
{
    private String id;
    
    private String createTime;
    
    private String content;
    
    private String averScore;
    
    private UserInfo user = new UserInfo();// 服务者信息
    
    // add by lij 2015/10/08 start
    private List<Replys> replys = new ArrayList<Replys>();
    // add by lij 2015/10/08 end
    public Comments()
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
    
    public UserInfo getUser()
    {
        return user;
    }
    
    public void setUser(UserInfo user)
    {
        this.user = user;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getAverScore()
    {
        return averScore;
    }
    
    public void setAverScore(String averScore)
    {
        this.averScore = averScore;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<Comments> CREATOR = new Parcelable.Creator<Comments>()
    {
        public Comments createFromParcel(Parcel source)
        {
            return new Comments(source);
        }
        
        public Comments[] newArray(int size)
        {
            return new Comments[size];
        }
    };
    
    public Comments(Parcel in)
    {
        id = in.readString();
        createTime = in.readString();
        content = in.readString();
        averScore = in.readString();
        user = in.readParcelable(UserInfo.class.getClassLoader());
        in.readTypedList(replys, Replys.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(createTime);
        out.writeString(content);
        out.writeString(averScore);
        out.writeParcelable(user, 0);
        out.writeTypedList(replys);
    }

	public List<Replys> getReplys() {
		return replys;
	}

	public void setReplys(List<Replys> replys) {
		this.replys = replys;
	}
    
}
