package com.bcinfo.tripaway.bean;

import java.util.HashMap;

import com.baidu.mobstat.co;
import com.bcinfo.tripaway.utils.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 关联资源 bean 关联了 产品, 目的地 达人, 子栏目
 * 
 * @author asus-pc
 * 
 */
public class PushResource implements Parcelable
{
    /**
     * 标识
     */
    private String id;
    
    /**
     * 标题
     */
    private String resTitle;
    
    /**
     * 副标题
     */
    private String subTitle;
    
    private String description;
    
    /**
     * 标题图
     */
    private String titleImage;
    
    /**
     * 关联对象的类型
     */
    private String objectType;
    
    /**
     * 关联对象的Id
     */
    private String objectId;
    /**
     * 关联对象的Id
     */
    private String keywords;
    
    public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	 /**
     * 原因
     */
    private String reason;
    
    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	  private String relationId="";
	    
	    public String getRelationId() {
			return relationId;
		}

		public void setRelationId(String relationId) {
			this.relationId = relationId;
		}
	
	   /**
     * 关联对象的objectParam
     */
    private HashMap<String, String> objectParam=new HashMap<String, String>();
    
    public HashMap<String, String> getObjectParam() {
		return objectParam;
	}

	public void setObjectParam(HashMap<String, String> objectParam) {
		this.objectParam = objectParam;
	}

	/**
     * 关联对象
     */
    private Object object = "null";
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getResTitle()
    {
        return resTitle;
    }
    
    public void setResTitle(String resTitle)
    {
        this.resTitle = resTitle;
    }
    
    public String getSubTitle()
    {
        return subTitle;
    }
    
    public void setSubTitle(String subTitle)
    {
        this.subTitle = subTitle;
    }
    
    public String getTitleImage()
    {
        return titleImage;
    }
    
    public void setTitleImage(String titleImage)
    {
        this.titleImage = titleImage;
    }
    
    public String getObjectType()
    {
        return objectType;
    }
    
    public void setObjectType(String objectType)
    {
        this.objectType = objectType;
    }
    
    public String getObjectId()
    {
        return objectId;
    }
    
    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }
    
    public Object getObject()
    {
        return object;
    }
    
    public void setObject(Object object)
    {
        this.object = object;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public PushResource()
    {
        super();
    }
    public PushResource(Parcel in)
    {
        id = in.readString();
        resTitle = in.readString();
        subTitle = in.readString();
        titleImage = in.readString();
        objectType = in.readString();
        objectId = in.readString();
        keywords= in.readString();
        description = in.readString();
        if(!StringUtils.isEmpty(objectType)){
        	if(objectType.equals("article")||objectType.equals("link")||objectType.equals("activity"))
        	 object =in.readString();
        	else{
        		if(objectType.equals("user"))
        		object =in.readParcelable(UserInfo.class.getClassLoader());	
        		else if(objectType.equals("destination"))
            	object =in.readParcelable(TripDestination.class.getClassLoader());
        		else if(objectType.equals("softtext")||objectType.equals("story"))
                	object =in.readParcelable(ArticleInfo.class.getClassLoader());	
        		else if(objectType.equals("subject"))
                	object =in.readParcelable(SubjectInfo.class.getClassLoader());	
        		else if(objectType.equals("product"))
                	object =in.readParcelable(ProductNewInfo.class.getClassLoader());	
        		else if(objectType.equals("topic"))
                	object =in.readParcelable(TopicInfo.class.getClassLoader());	
        	}
        }
       
//        objectParam=in.readHashMap(HashMap.class.getClassLoader());
        reason=in.readString();
        relationId=in.readString();
        
    }
    
    public static final Parcelable.Creator<PushResource> CREATOR = new Parcelable.Creator<PushResource>()
    {
        
        @Override
        public PushResource createFromParcel(Parcel source)
        {
            
            return new PushResource(source);
        }
        
        @Override
        public PushResource[] newArray(int size)
        {
            
            return new PushResource[size];
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
        dest.writeString(id);
        dest.writeString(resTitle);
        dest.writeString(subTitle);
        dest.writeString(titleImage);
        dest.writeString(objectType);
        dest.writeString(objectId);
        dest.writeString(keywords);
        dest.writeString(description);
        if(object instanceof Parcelable)
        dest.writeParcelable((Parcelable) object, 0);
        else 
        	if(object instanceof String)
        		 dest.writeString((String)object);		
//        dest.writeMap(objectParam);
        dest.writeString(reason);
        dest.writeString(relationId);
        
    }
    
}
