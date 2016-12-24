package com.bcinfo.tripaway.bean;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Function：确认订单的Bean信息
 * @author： tcyangpeng
 * @Time：2015年4月2日下午5:45:24
 * @Version： 1.0
 */
public class ComfireInfo
{
    private String photoUrl;
    /**
     * 是否已选择
     */
    private Boolean isChecked;
    private String title;
    private String content;
    /**
     * 类型
     */
    private String type;
    /**
     * 预订使用时间
     */
    private String useTime;

    public String getPhotoUrl()
    {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }

    public Boolean getIsChecked()
    {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked)
    {
        this.isChecked = isChecked;
    }

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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getUseTime()
    {
        return useTime;
    }

    public void setUseTime(String useTime)
    {
        this.useTime = useTime;
    }
}
