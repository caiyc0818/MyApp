package com.bcinfo.tripaway.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;

import com.bcinfo.tripaway.fragment.BaseFragment;
import com.bcinfo.tripaway.utils.DensityUtil;

public class CatsAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> list;
	
	private int mChildCount;
	private Activity mContext;
	public CatsAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public CatsAdapter(FragmentManager fm,List<BaseFragment> list,Activity mContext) {
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
    	return new BigDecimal(DensityUtil.dip2px(mContext,70)).divide(new BigDecimal(width),1,BigDecimal.ROUND_HALF_UP).floatValue();
	}
}
