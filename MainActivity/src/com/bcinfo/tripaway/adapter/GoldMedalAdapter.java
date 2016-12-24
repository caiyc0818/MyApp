package com.bcinfo.tripaway.adapter;

import java.math.BigDecimal;
import java.util.List;

import com.bcinfo.tripaway.fragment.BaseFragment;
import com.bcinfo.tripaway.utils.DensityUtil;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GoldMedalAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> list;
	
	private int mChildCount;
	private Activity mContext;
	public GoldMedalAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public GoldMedalAdapter(FragmentManager fm,List<BaseFragment> list,Activity mContext) {
		super(fm);
		this.list = list;
		this.mContext = mContext;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}
	
	@Override
	public float getPageWidth(int position) {
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
    	int width = dm.widthPixels;
    	return new BigDecimal(DensityUtil.dip2px(mContext, 115)).divide(new BigDecimal(width),1,BigDecimal.ROUND_HALF_UP).floatValue();
	}
}
