package com.bcinfo.tripaway.fragment;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.bean.UserInfoEx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 活动 fragment
 * 
 */
public class ActionFragment extends Fragment
{
    private Button button1;
    private Button button2;
    private Button button3;
	private Context mContext;
	private UserInfo userInfo = new UserInfo();

    private AllActionsFragment alltravFragment;
    private OverActionsFragment overtravFragment;
    private ValidFragment nottravFragment;

	public ActionFragment(Context mContext, UserInfo userInfo) {
		this.mContext = mContext;
		this.userInfo = userInfo;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.acceptedfragment, null);
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
  
        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);


        init_date();
        button1.setOnClickListener(new OnClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v)
            {
            	
            	
            
                button1.setSelected(true);
                button2.setSelected(false);
                button3.setSelected(false);
               
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                hideFragments(transaction);
                if (null == alltravFragment)
                {
                    alltravFragment = new AllActionsFragment(getActivity(),userInfo);
                    transaction.add(R.id.fragment1, alltravFragment);
                }
                transaction.show(alltravFragment);
                // Commit the transaction
                transaction.commitAllowingStateLoss();
            }
        });
        button2.setOnClickListener(new OnClickListener()
        {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v)
            {
                button2.setSelected(true);
                button1.setSelected(false);
                button3.setSelected(false);
         
                // TODO Auto-generated method stub
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                hideFragments(transaction);
                if (null == nottravFragment)
                {
                    nottravFragment = new ValidFragment(getActivity(),userInfo);
                    transaction.add(R.id.fragment1, nottravFragment);
                }
                transaction.show(nottravFragment);
                // Commit the transaction
                transaction.commitAllowingStateLoss();
            }
        });
        button3.setOnClickListener(new OnClickListener()
        {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v)
            {
                button3.setSelected(true);
                button2.setSelected(false);
                button1.setSelected(false);
       
                // TODO Auto-generated method stub
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                hideFragments(transaction);
                if (overtravFragment == null) {
                	overtravFragment = new OverActionsFragment(getActivity(),userInfo);
                    transaction.add(R.id.fragment1, overtravFragment);
                } else {
                    transaction.show(overtravFragment);
                }
                // Commit the transaction
                transaction.commitAllowingStateLoss();
            }
        });

        return view;

    }

    @SuppressLint("NewApi")
    private void init_date()
    {
        // TODO Auto-generated method stub
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        alltravFragment = new AllActionsFragment(getActivity(),userInfo);
        transaction.replace(R.id.fragment1, alltravFragment);
        transaction.commit();

    }

    // @Override
    // public void onDetach()
    // {
    // super.onDetach();
    // try
    // {
    // Field childFragmentManager =
    // Fragment.class.getDeclaredField("mChildFragmentManager");
    // childFragmentManager.setAccessible(true);
    // childFragmentManager.set(this, null);
    //
    // }
    // catch (NoSuchFieldException e)
    // {
    // throw new RuntimeException(e);
    // }
    // catch (IllegalAccessException e)
    // {
    // throw new RuntimeException(e);
    // }
    // }
    private void hideFragments(FragmentTransaction transaction) {
        if (alltravFragment != null) {
            transaction.hide(alltravFragment);
        }

        if (nottravFragment != null) {
            transaction.hide(nottravFragment);
        }
        if (overtravFragment != null) {
            transaction.hide(overtravFragment);
        }
     
     
    }
}
