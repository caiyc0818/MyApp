package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * 个人资料照片adapter
 * @function
 * @author caihelin
 * @version 1.0 2014年12月27日 11:09:22
 */
public class PersonalPhotoAdapter extends PagerAdapter
{
    /**
     * 存放View的list集合
     */
    private List<View> photoList;
   /**
    * 无参数的构造方法
    */
    public PersonalPhotoAdapter(){
        
    }
    /**
     * 带参数的构造方法
     */
    public PersonalPhotoAdapter(List<View> photoList){
        this.photoList=photoList;
    }
    @Override
    public int getCount()
    {
        
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj)
    {
        
        return view==obj;
    }

    @Override
    public void destroyItem(View container, int position, Object object)
    {
        ViewPager viewPager=(ViewPager)container;
        viewPager.removeView(photoList.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position)
    {
        // 向ViewPager中添加View
         ((ViewPager)container).addView(photoList.get(position));
         
         return photoList.get(position);
         
    }
    

}
