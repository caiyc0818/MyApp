package com.bcinfo.tripaway.camera;

import java.util.List;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class ImageBucket
{
    
    private int count = 0;
    
    private String bucketName;
    
    private List<ImageItem> imageList;
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
    public String getBucketName()
    {
        return bucketName;
    }
    
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    public List<ImageItem> getImageList()
    {
        return imageList;
    }
    
    public void setImageList(List<ImageItem> imageList)
    {
        this.imageList = imageList;
    }
}
