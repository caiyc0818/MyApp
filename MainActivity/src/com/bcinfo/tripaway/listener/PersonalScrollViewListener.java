package com.bcinfo.tripaway.listener;

import android.widget.ScrollView;

/**
 * 个人资料-垂直滚动条接口
 * 
 * @function
 * @author caihelin
 * @version 1.0 2014年12月30日 17:03:22
 */
public interface PersonalScrollViewListener
{
    // 抽象方法 当开始滚动的时候执行下面的方法
    public void onScrollChanged(ScrollView view, int positionX, int positionY, int prePositionX, int prePositionY);
}
