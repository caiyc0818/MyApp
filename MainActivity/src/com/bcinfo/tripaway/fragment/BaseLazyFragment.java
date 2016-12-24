package com.bcinfo.tripaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.AddGoWithActivity;
import com.bcinfo.tripaway.activity.DiscoverAllLocationActivity;
import com.bcinfo.tripaway.activity.DiscoverAllThemesActivity;
import com.bcinfo.tripaway.activity.DiscoveryDefaultActivity;
import com.bcinfo.tripaway.activity.LoginActivity;

/**
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月3日 下午5:26:20
 */
public abstract class BaseLazyFragment extends Fragment
{
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        
    }
    
    /**
     * 定义一个显示自定义toast的方法
     * 
     * @param view 要显示的view
     * 
     */
    public Toast showToast(View view)
    {
        
        Toast toast = new Toast(this.getActivity());
        
        toast.setView(view);
        
        return toast;
        
    }
    
    protected OnClickListener onMfragmentClick = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                /*case R.id.discovery_showAllThemes:
                    Intent intentForAllThemes =
                        new Intent(BaseFragment.this.getActivity(), DiscoverAllThemesActivity.class);
                    startActivity(intentForAllThemes);
                    animationOpen();
                    break;
                case R.id.discovery_showAllLocations:
                    Intent intentForAllLocations =
                        new Intent(BaseFragment.this.getActivity(), DiscoverAllLocationActivity.class);
                    startActivity(intentForAllLocations);
                    animationOpen();
                    break;*/
//                case R.id.add_go_with_button:
//                    Intent intentForGo = new Intent(getActivity(), AddGoWithActivity.class);
//                    startActivity(intentForGo);
//                    animationOpen();
//                    break;
//                case R.id.searchEditLayout:
//                    Intent intentForDiscovery = new Intent(getActivity(), DiscoveryDefaultActivity.class);
//                    startActivity(intentForDiscovery);
//                    animationOpen();
//                    break;
//                default:
//                    break;
            }
            
        }
    };
    
    /**
     * 页面启动动画
     */
    protected void animationOpen()
    {
        getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }
    
    /**
     * 页面关闭动画
     */
    public void animationClose()
    {
        getActivity().overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
    }
    
    public void goLoginActivity()
    {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        animationOpen();
        return;
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

     
     

	
	
	
}
