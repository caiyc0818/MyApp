package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义个人资料 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月30日 10:09:23
 * 
 */
public class PersonalContentPagerAdapter extends PagerAdapter
{
    private List<View> viewList;

    public List<View> getViewList()
    {
        return viewList;
    }

    public void setViewList(List<View> viewList)
    {
        this.viewList = viewList;
    }

    public PersonalContentPagerAdapter(List<View> viewList)
    {
        this.viewList = viewList;
    }

    @Override
    public int getCount()
    {

        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj)
    {

        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView(viewList.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View viewItem = viewList.get(position);

        ((ViewPager) container).addView(viewItem);

        return viewItem;

    }

}
