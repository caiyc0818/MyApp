package com.bcinfo.tripaway.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hanweipeng
 * @date 2015-7-22 下午5:05:48
 */
public class NewMessage implements Parcelable
{
    private String title;
    
    private String content;
    
    private String image;
    
    private String pattern;
    
    private String productId;
    
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
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
    }
    
    public String getPattern()
    {
        return pattern;
    }
    
    public void setPattern(String pattern)
    {
        this.pattern = pattern;
    }
    
    public String getProductId()
    {
        return productId;
    }
    
    public void setProductId(String productId)
    {
        this.productId = productId;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public NewMessage(Parcel in)
    {
        title = in.readString();
        content = in.readString();
        image = in.readString();
        pattern = in.readString();
        productId = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(title);
        out.writeString(content);
        out.writeString(image);
        out.writeString(pattern);
        out.writeString(productId);
    }
    
}
