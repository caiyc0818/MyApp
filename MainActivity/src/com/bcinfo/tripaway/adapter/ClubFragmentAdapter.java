package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.bcinfo.tripaway.fragment.BaseFragment;
import com.bcinfo.tripaway.utils.ToastUtil;

public class ClubFragmentAdapter extends FragmentPagerAdapter {
	
	private List<BaseFragment> list;
	
	private int currentIndex = -1;
	
	private int mCounts;

	public void setCurrentIndex(int currentIndex){
		this.currentIndex = currentIndex;
	}
	public ClubFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public ClubFragmentAdapter(FragmentManager fm,List<BaseFragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
}
