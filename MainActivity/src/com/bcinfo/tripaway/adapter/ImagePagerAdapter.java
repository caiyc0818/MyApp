package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bcinfo.imageviewer.fragment.ImageCommonFragment;

/**
 * 图片浏览ViewPage适配器
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月26日 上午11:42:07
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter
{
    private ArrayList<String> fileList;

    public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList)
    {
        super(fm);
        this.fileList = fileList;
    }

    @Override
    public int getCount()
    {
        return fileList == null ? 0 : fileList.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        ImageCommonFragment fragment=new ImageCommonFragment();
        return fragment.newInstance(fileList, position);
    }
}
