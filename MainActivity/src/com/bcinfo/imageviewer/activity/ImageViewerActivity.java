package com.bcinfo.imageviewer.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.imageviewer.fragment.ImageDetailFragment;
import com.bcinfo.tripaway.R;

import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.utils.StringUtils;

/**
 * 图片查看器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月24日 下午7:40:09
 */
public class ImageViewerActivity extends FragmentActivity
{
    private static String STATE_POSITION = "STATE_POSITION";
    
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    
    public static final String EXTRA_IMAGE_URLS = "images";
    
    private ViewPager mPager;
    
    private LinearLayout backBtn;
    
    private int pagerPosition;
    
    private int startPosition;
    
    private TextView indicator;
    
    private TextView imageDesc;
    
    private LinearLayout down_tv;
    private LinearLayout desc_layout;
    private FrameLayout framelayout;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer_activity);
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        startPosition=getIntent().getIntExtra("STATE_POSITION", 0);
        final ArrayList<ImageInfo> productAllImgList = getIntent().getParcelableArrayListExtra(EXTRA_IMAGE_URLS);
        mPager = (ViewPager)findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), productAllImgList);
        mPager.setAdapter(mAdapter);
        backBtn=(LinearLayout) findViewById(R.id.back);
        imageDesc=(TextView) findViewById(R.id.image_desc);
        imageDesc.setText(StringUtils.isEmpty(productAllImgList.get(0).getDesc())?"":productAllImgList.get(0).getDesc());
        imageDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        down_tv=(LinearLayout) findViewById(R.id.down_tv);
        framelayout=(FrameLayout) findViewById(R.id.framelayout);
        backBtn.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {

//				Intent intent=new Intent(ImageViewerActivity.this,GrouponProductNewDetailActivity.class);
//				startActivity(intent);
				finish();
//				
			}
		});
        indicator = (TextView)findViewById(R.id.indicator);
        imageDesc=(TextView)findViewById(R.id.image_desc);
        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text);
        
        
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }
            
            @Override
            public void onPageSelected(int arg0)
            {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
           	    imageDesc.setText(StringUtils.isEmpty(productAllImgList.get(arg0).getDesc())?"":productAllImgList.get(arg0).getDesc());
              
                indicator.setText(text);
                
            }
        });
        if (savedInstanceState != null)
        {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mPager.setCurrentItem(startPosition);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }
    
    private class ImagePagerAdapter extends FragmentStatePagerAdapter
    {
        public ArrayList<ImageInfo> productAllImgList;
        
        public ImagePagerAdapter(FragmentManager fm, ArrayList<ImageInfo> productAllImgList)
        {
            super(fm);
            this.productAllImgList = productAllImgList;
        }
        
        @Override
        public int getCount()
        {
            return productAllImgList == null ? 0 : productAllImgList.size();
        }
        
		@Override
        public Fragment getItem(int position)
        {
        	
			
            String url = productAllImgList.get(position).getUrl();
            ImageDetailFragment fragment = new ImageDetailFragment();
            return fragment.newInstance(url);
        }
    }
}
