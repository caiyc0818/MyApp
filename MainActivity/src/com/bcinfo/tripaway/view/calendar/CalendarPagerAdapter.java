package com.bcinfo.tripaway.view.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
/**
 * 日历适配器
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月5日 上午10:13:57
 */
public class CalendarPagerAdapter extends FragmentStatePagerAdapter
{
    private static OnFocusChangeListener mOnFocusChangeListener;
    private static OnClickListener mOnClickListener;

    public CalendarPagerAdapter(FragmentManager fm, OnFocusChangeListener focusListener, OnClickListener clickListener)
    {
        super(fm);
        // TODO Auto-generated constructor stub
        mOnFocusChangeListener = focusListener;
        mOnClickListener = clickListener;
    }

    @Override
    public Fragment getItem(int position)
    {
        return CalendarPagerFragment.create(position, mOnFocusChangeListener,mOnClickListener);
    }
    
    @Override
    public int getItemPosition(Object object)
    {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }
    
    @Override
    public void notifyDataSetChanged()
    {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }
    
    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1)
    {
        // TODO Auto-generated method stub
        return super.instantiateItem(arg0, arg1);
    }

    @Override
    public int getCount()
    {
        int years = LunarCalendar.getMaxYear() - LunarCalendar.getMinYear();
        return years * 12;
    }
}
