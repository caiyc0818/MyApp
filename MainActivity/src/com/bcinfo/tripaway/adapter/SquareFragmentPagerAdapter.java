package com.bcinfo.tripaway.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SquareFragmentPagerAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Fragment> fragments;
	private String[] mTitles =new  String[30];
	private FragmentManager fm;

	public SquareFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}

	public SquareFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,String[] mTitles) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
		this.mTitles = mTitles;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}
//	public void setFragments(ArrayList<Fragment> fragments) {
//		if (this.fragments != null) {
//			FragmentTransaction ft = fm.beginTransaction();
//			for (Fragment f : this.fragments) {
//				ft.remove(f);
//			}
//			ft.commit();
//			ft = null;
//			fm.executePendingTransactions();
//		}
//		this.fragments = fragments;
//		notifyDataSetChanged();
//	}

	
//	@Override
//	public Object instantiateItem(ViewGroup container, final int position) {
//		Object obj = super.instantiateItem(container, position);
//		return obj;
//	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
//		FragmentTransaction ft = fm.beginTransaction();
//		ft.detach((Fragment) object); // 把fragment从Activity中detach掉

	}

}