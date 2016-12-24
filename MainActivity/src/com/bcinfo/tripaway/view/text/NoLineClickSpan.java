package com.bcinfo.tripaway.view.text;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * 无下划线自定义跳转文本
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月8日 下午8:55:12
 */
public abstract class NoLineClickSpan extends ClickableSpan
{
    private int linkColor;

    public NoLineClickSpan(int linkColor)
    {
        super();
        this.linkColor = linkColor;
    }

    @Override
    public void updateDrawState(TextPaint ds)
    {
        ds.setColor(linkColor);
        ds.setUnderlineText(false); //去掉下划线
    }
}
