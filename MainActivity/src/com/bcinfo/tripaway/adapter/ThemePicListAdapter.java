package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ThemePicListAdapter extends BaseAdapter
{
    private static final String TAG = "ThemePicListAdapter";
    
    List<ImageInfo> pics;
    
    
    private Context mContext;

	private DisplayMetrics dm;
    
    
    
    
    
    public ThemePicListAdapter(Context context, List<ImageInfo> pics)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.pics=pics;
         dm = new DisplayMetrics();
  	  ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return pics.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return pics.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
    	ImageInfo pic = pics.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            // trip_detail_item
            convertView = inflater.inflate(R.layout.theme_pic_list_item, null);
            holder = new ViewHolder();
            holder.desc = (TextView)convertView.findViewById(R.id.pic_desc);
            holder.pic = (ImageView)convertView.findViewById(R.id.pic);
            
            // holder.taskListView = (LinearLayout)convertView.findViewById(R.id.trip_days_task_layout);
            // holder.serviceListView = (LinearLayout)convertView.findViewById(R.id.trip_days_service_layout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        LinearLayout.LayoutParams ls=new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels*Integer.parseInt(pic.getHeight())/Integer.parseInt(pic.getWidth()));
    		   holder.pic.setScaleType(ScaleType.CENTER_CROP);	   
        holder.pic.setLayoutParams(ls);
        holder.pic.setImageResource(R.drawable.ic_launcher);
        if (!StringUtils.isEmpty(pic.getDesc())) {
        holder.desc.setText(pic.getDesc());
        }
    	if (!StringUtils.isEmpty(pic.getUrl())) {
    		holder.pic.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(Urls.imgHost + pic.getUrl(), holder.pic,
					AppConfig.options(R.drawable.ic_launcher));
		}else  {
			holder.pic.setVisibility(View.GONE);
		}
        return convertView;
    }
    
    
    public class ViewHolder
    {
        
        TextView desc;
        
        ImageView pic;
        
    }
    
}
