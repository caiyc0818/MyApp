package com.bcinfo.tripaway.fragment;

import com.bcinfo.tripaway.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RightDrawerLayout extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.right_drawer, container,false);
		
		return v;
	}
}
