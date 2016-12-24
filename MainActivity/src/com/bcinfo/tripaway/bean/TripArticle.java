package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.hardware.ConsumerIrManager;
import android.os.Parcel;
import android.os.Parcelable;

import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.UserInfo;

/**
 * 微游记 (旅行故事)
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月18日 14:10:12
 */
public class TripArticle implements Parcelable
{
	 /**
     * 0表示晒图，1表示连载
     */
	private byte type=0;
	
    public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	/**
     * 微游记 发布时间 (精确到 年月日时分秒)
     */
    private String publishTime;
    
    /**
     * 微游记 标识id
     */
    private String photoId;
    
    /**
     * 微游记 赞美标记位 (0:未赞 ; 1:已赞)
     */
    private String isLike;
    /**
     * 微游记 赞美数
     */
    private String likes;
    
    public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}
	
	/**
     * 阅读量
     */
	private int viewNum;

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	/**
     * 微游记 描述
     */
    private String description;
    /**
     * 微游记 评论数
     */
    private String comments;
    
    public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
     * 微游记 位置
     */
    private String location;
    
    private List<String> tagList=new ArrayList<String>();
    
    public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	/**
     * 微游记 发布者publisher
     */
    private UserInfo publisher = new UserInfo();
    /**
     * 微游记评论
     */
    private ArrayList<Comments> commentList = new ArrayList<>();

	public ArrayList<Comments> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comments> commentList) {
		this.commentList = commentList;
	}

	private String title;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	private String cover;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
	private String access;

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	private String isFocus;
	
	public String getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(String isFocus) {
		this.isFocus = isFocus;
	}

	/**
     * 微游记 资源图片
     */
    private ArrayList<ImageInfo> pictureList = new ArrayList<ImageInfo>();
    
    public ArrayList<ImageInfo> getPictureList()
    {
        return pictureList;
    }
    
    public void setPictureList(ArrayList<ImageInfo> pictureList)
    {
        this.pictureList = pictureList;
    }
    
    public String getPublishTime()
    {
        return publishTime;
    }
    
    public void setPublishTime(String publishTime)
    {
        this.publishTime = publishTime;
    }
    
    public String getIsLike()
    {
        return isLike;
    }
    
    public void setIsLike(String isLike)
    {
        this.isLike = isLike;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public UserInfo getPublisher()
    {
        return publisher;
    }
    
    public void setPublisher(UserInfo publisher)
    {
        this.publisher = publisher;
    }
    
    public String getPhotoId()
    {
        return photoId;
    }
    
    public void setPhotoId(String photoId)
    {
        this.photoId = photoId;
    }
    
    public TripArticle()
    {
        super();
        
    }
    
    public TripArticle(Parcel in)
    {
    	comments= in.readString();
        publishTime = in.readString();
        isLike = in.readString();
        likes = in.readString();
        description = in.readString();
        location = in.readString();
        photoId = in.readString();
        // in.readArrayList(pictureList.getClass().getClassLoader());
        in.readTypedList(pictureList, ImageInfo.CREATOR);
        publisher = in.readParcelable(UserInfo.class.getClassLoader());
        in.readTypedList(commentList,Comments.CREATOR);
        in.readStringList(tagList);
        type=in.readByte();
        viewNum=in.readInt();
        title=in.readString();
        cover=in.readString();
        access=in.readString();
        isFocus=in.readString();
    }
    
    public static final Parcelable.Creator<TripArticle> CREATOR = new Parcelable.Creator<TripArticle>()
    
    {
        
        @Override
        public TripArticle createFromParcel(Parcel source)
        {
            
            return new TripArticle(source);
        }
        
        @Override
        public TripArticle[] newArray(int size)
        {
            
            return new TripArticle[size];
        }
        
    };
    
    @Override
    public int describeContents()
    {
        
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
    	dest.writeString(comments);
        dest.writeString(publishTime);
        dest.writeString(isLike);
        dest.writeString(likes);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(photoId);
        dest.writeTypedList(pictureList);
        dest.writeParcelable(publisher, 0);
        dest.writeTypedList(commentList);
        dest.writeStringList(tagList);
        dest.writeByte(type);
        dest.writeInt(viewNum);
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(access);
        dest.writeString(isFocus);
    }
    
}
