package com.bcinfo.imageviewer.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 单张图片显示Fragment 用于局部区域切换图片,点击全屏展示 不可以放大缩小
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月24日 下午7:50:18
 */
public class ImageCommonFragment extends Fragment
{
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    
    private String mImageUrl = null;
    
    private ImageView mImageView;
    
    private ProgressBar progressBar;
    
    public ImageCommonFragment newInstance(ArrayList<String> urls, int imageIndex)
    {
        final ImageCommonFragment fragment = new ImageCommonFragment();
        final Bundle args = new Bundle();
        args.putStringArrayList("urls", urls);
        args.putInt("imageIndex", imageIndex);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            ArrayList<String> urls = getArguments().getStringArrayList("urls");
            int imageIndex = getArguments().getInt("imageIndex");
            if (urls != null && urls.size() > imageIndex)
            {
                mImageUrl = urls.get(imageIndex);
                Log.i("ImageCommonFragment", "onCreate-->" + "mImageUrl=" + mImageUrl);
            }
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView)v.findViewById(R.id.image);
        mImageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                intent.putExtra(EXTRA_IMAGE_INDEX, getArguments() != null ? getArguments().getInt("imageIndex") : 0);
                intent.putExtra(EXTRA_IMAGE_URLS, getArguments() != null ? getArguments().getStringArrayList("urls")
                    : null);
                getActivity().startActivity(intent);
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
            }
        });
    }
}
