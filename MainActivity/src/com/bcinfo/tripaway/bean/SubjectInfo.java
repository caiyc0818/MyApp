package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SubjectInfo  implements Parcelable {
	private String title;
	private String subTitle;
	private String cover;
	private String subjectId;
	private String type;
	private String url;
	private PushResource pushResource;
	public PushResource getPushResource() {
		return pushResource;
	}

	public void setPushResource(PushResource pushResource) {
		this.pushResource = pushResource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public  String getType() {
		return type;
	}

	public  void setType(String type) {
		this.type = type;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	private String description;
	
	   public String getTitle()
	    {
	        return title;
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
	    
	    public static final Parcelable.Creator<SubjectInfo> CREATOR = new Parcelable.Creator<SubjectInfo>()
	    	    {
	    	        public SubjectInfo createFromParcel(Parcel source)
	    	        {
	    	            return new SubjectInfo(source);
	    	        }
	    	        
	    	        public SubjectInfo[] newArray(int size)
	    	        {
	    	            return new SubjectInfo[size];
	    	        }
	    	    };
	    	    
	    	    public SubjectInfo(Parcel in)
	    	    {
	    	        title = in.readString();
	    	        subTitle=in.readString();
	    	        cover=in.readString();
	    	        description = in.readString();
	    	        subjectId=in.readString();
	    	        type=in.readString();
	    	        url=in.readString();
	    	        pushResource=in.readParcelable(PushResource.class.getClassLoader());
	    	  
	    	    }
	    	    
	    	    public SubjectInfo() {
					// TODO Auto-generated constructor stub
				}

				@Override
	    	    public void writeToParcel(Parcel out, int flags)
	    	    {
	    	        // TODO Auto-generated method stub
	    	        out.writeString(title);
	    	        out.writeString(subTitle);
	    	        out.writeString(cover);
	    	        out.writeString(description);
	    	        out.writeString(subjectId);
	    	        out.writeString(type);
	    	        out.writeString(url);
	    	        out.writeParcelable(pushResource, 0);
	    	    }

				@Override
				public int describeContents() {
					// TODO Auto-generated method stub
					return 0;
				}
}
