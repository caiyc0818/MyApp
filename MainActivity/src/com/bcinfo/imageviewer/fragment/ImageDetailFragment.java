package com.bcinfo.imageviewer.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.imageviewer.view.PhotoViewAttacher;
import com.bcinfo.imageviewer.view.PhotoViewAttacher.OnPhotoTapListener;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 单张图片显示Fragment 可以双击放大，手势放大缩小 用于全屏展示
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月24日 下午7:50:18
 */
public class ImageDetailFragment extends Fragment
{
    private String mImageUrl;
    
    private ImageView mImageView;
    
    private ProgressBar progressBar;
    
    private PhotoViewAttacher mAttacher;
    
    private LinearLayout down_tv;
    
    private LinearLayout backBtn;
    
    public ImageDetailFragment newInstance(String imageUrl)
    {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView)v.findViewById(R.id.image);
        down_tv=(LinearLayout) getActivity().findViewById(R.id.down_tv);
        backBtn=(LinearLayout) getActivity().findViewById(R.id.back);
        mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("********************mImageViewckick");
				if(down_tv.getVisibility()==0){
					down_tv.setVisibility(4);
					backBtn.setVisibility(4);
				}else if(down_tv.getVisibility()==4){
					down_tv.setVisibility(0);
					backBtn.setVisibility(0);
				}
			}
		});
        
        
        
        
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener()
        {
            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2)
            {
                // getActivity().finish();
            	 System.out.println("********************mImageViewckick");
         				if(down_tv.getVisibility()==0){
         					down_tv.setVisibility(4);
         					backBtn.setVisibility(4);
         			        
         				}else if(down_tv.getVisibility()==4){
         					down_tv.setVisibility(0);
         					backBtn.setVisibility(0);
         				}
            }
        });
        progressBar = (ProgressBar)v.findViewById(R.id.loading);
        return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        ImageLoader.getInstance().displayImage(Urls.imgHost + mImageUrl, mImageView, new SimpleImageLoadingListener()
        {
        
            @Override
            public void onLoadingStarted(String imageUri, View view)
            {
                progressBar.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason)
            {
                String message = null;
                switch (failReason.getType())
                {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                
                progressBar.setVisibility(View.GONE);
            }
            
            
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });
    }
}
