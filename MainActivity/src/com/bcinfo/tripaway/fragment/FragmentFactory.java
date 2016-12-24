package com.bcinfo.tripaway.fragment;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

public class FragmentFactory {
	private static volatile FragmentFactory fragmentFactory;

	public static FragmentFactory getInstance() {
		if (null == fragmentFactory) {
			fragmentFactory = new FragmentFactory();
		}
		return fragmentFactory;
	}

	public static final int PICK = 1;

	public static final int DISCOVER = 0;

	public static final int MESSAGE = 2;

	public static final int ORDER = 3;

	public static final int SETTING = 4;


	public static final int MYINFO = 7;

	public static final int CUSTOMIZE = 6;
	/**
	 * 新版本微游记
	 */
	public static final int NEWMICROTRAVEL = 8;
	public static final int NEWMICROTRAVEL2 = 9;
	
	

	private boolean isInit;

	private SparseArray<BaseFragment> fragmentCache;


	private FragmentFactory() {
		fragmentCache = new SparseArray<BaseFragment>(7);
	}


	public BaseFragment getFragmentInCache(int position) {
		BaseFragment fragment = fragmentCache.get(position);
		if (fragment == null) {
			fragment = getFragment(position);
			fragmentCache.put(position, fragment);
		}

		return fragment;
	}

	/**
	 * 对所有Fragment进行统一操作
	 * 
	 * @param operFlag
	 *            true:去初始化; false:退出前停止
	 */
	public void operateFragment(boolean operFlag) {
		for (int i = 0; i < fragmentCache.size(); i++) {
			Fragment fragment = fragmentCache.valueAt(i);
			if (null == fragment) {
				continue;
			}

			if (operFlag) {
				unInit();
			} else {
				stopBeforeExit();
			}
		}
	}

	private BaseFragment getFragment(int position) {
		BaseFragment fragment = null;

		switch (position) {
		case PICK:
			fragment = new PickedFragment();
			break;

		case DISCOVER:
//			fragment = new LocateDestinationFragment();
			fragment = new HomeFragment();
			break;

		case MESSAGE:
			fragment = new MessageFragment();
			break;
		case ORDER:
			fragment = new MyTravelOrderFragment();
			break;
		case SETTING:
			fragment = new SettingFragment();
			break;
		case MYINFO:
			fragment = new MyInfoFragment();
			break;
//		case CUSTOMIZE:
//			fragment = new CustomizeRequireFragment();
//			break;
//		case NEWMICROTRAVEL:
//			fragment = new MicroBlogsNewFragment();
		case NEWMICROTRAVEL:
			fragment = new com.bcinfo.tripaway.fragment.Fragment1();
			break;
		case NEWMICROTRAVEL2:
			
			fragment = new com.bcinfo.tripaway.fragment.Fragment2();
			break;
		}

		return fragment;
	}

	/**
	 * 退出应用，卸载组件前的停止工作
	 */
	public void stopBeforeExit() {

	}

	/**
	 * 去初始化工作，必须在onDestroyView之后执行
	 */
	public void unInit() {
		isInit = false;
	}
}
