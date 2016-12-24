package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
/**
 * 指引  pagerAdapter
 * @function
 * @author caihelin
 * @version 1.0 2015年3月25日  10:57:34
 *
 */
public class GuidePagerAdapter extends PagerAdapter
{
    private List<View> viewList;

    public GuidePagerAdapter(List<View> viewList)
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

        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(viewList.get(position), 0);
        return viewList.get(position);
    }

}
