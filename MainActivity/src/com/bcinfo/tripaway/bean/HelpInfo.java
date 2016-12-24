package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HelpInfo  implements Parcelable {
	private int level;
	private String title;
	private String content;
	
	   public String getTitle()
	    {
	        return title;
	    }
	    
	    public void setTitle(String title)
	    {
	        this.title = title;
	    }

	    
	    public String getContent()
	    {
	        return content;
	    }
	    
	    public void setLevel(int level)
	    {
	        this.level = level;
	    }
	    
	    public int getLevel()
	    {
	        return level;
	    }
	    
	    public void setContent(String content)
	    {
	        this.content = content;
	    }
	    
	    public static final Parcelable.Creator<HelpInfo> CREATOR = new Parcelable.Creator<HelpInfo>()
	    	    {
	    	        public HelpInfo createFromParcel(Parcel source)
	    	        {
	    	            return new HelpInfo(source);
	    	        }
	    	        
	    	        public HelpInfo[] newArray(int size)
	    	        {
	    	            return new HelpInfo[size];
	    	        }
	    	    };
	    	    
	    	    public HelpInfo(Parcel in)
	    	    {
	    	        title = in.readString();
	    	        content = in.readString();
	    	        level = in.readInt();
	    	  
	    	    }
	    	    
	    	    public HelpInfo() {
					// TODO Auto-generated constructor stub
				}

				@Override
	    	    public void writeToParcel(Parcel out, int flags)
	    	    {
	    	        // TODO Auto-generated method stub
	    	        out.writeString(title);
	    	        out.writeString(content);
	    	        out.writeInt(level);
	    	       
	    	    }

				@Override
				public int describeContents() {
					// TODO Auto-generated method stub
					return 0;
				}
}
