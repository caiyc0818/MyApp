package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;

import com.bcinfo.tripaway.view.dialog.AffirmDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;




public class SubjectClubPicAdapter extends BaseAdapter
 {  
	 private Context mContext;  
	 private List<String> pics;
	private int width;
	   
	 public SubjectClubPicAdapter(Context c,List<String> pics )  
	 {  
	  mContext=c;  
	  this.pics=pics;
	  
	  width = DensityUtil.dip2px(mContext, 32);
	 }  
	 @Override  
	 public int getCount() {  
	  // TODO Auto-generated method stub  
	  return pics.size();  
	 }  
	@Override  
	 public Object getItem(int position) {  
	  // TODO Auto-generated method stub  
	  return pics.get(position);  
	 }  
	@Override  
	 public long getItemId(int position) {  
	  // TODO Auto-generated method stub  
	  return position;  
	 }  
	
	public List<String> getPicList(){
		return this.pics;
	}
	 @Override  
	 public View getView(int position, View convertView, ViewGroup parent) {  
	  // TODO Auto-generated method stub  
	    
	  ImageView imageview;  
	  if(convertView==null)  
	  {  
	   imageview=new ImageView(mContext);  
	   
	   imageview.setLayoutParams(new GridView.LayoutParams(width, width));  
	   imageview.setScaleType(ImageView.ScaleType.FIT_XY);  
//	   imageview.setPadding(8,8,8,8);  
	 }  
	  else  
	  {  
	   imageview=(ImageView) convertView;  
	  }  
	  if (!StringUtils.isEmpty(pics.get(position)))
      {
          ImageLoader.getInstance().displayImage(Urls.imgHost + pics.get(position),
        		 imageview,
              AppConfig.options(R.drawable.ic_launcher));
      }
	  return imageview;  
	  }  
	
	} 
