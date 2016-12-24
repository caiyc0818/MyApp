package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
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
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;
import com.bcinfo.tripaway.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExperienceListAdapter extends BaseAdapter
{
    private static final String TAG = "ExperienceListAdapter";
    
    private ImageView[] imageViews = null;
    
    
    private Context mContext;
    
    private int startPosition;
    
    private BitmapManager bitmapManager;
    
    private ArrayList<ImageInfo> productTitleImgList;
    
    private ArrayList<Experiences> mItemList ;
    
    public ExperienceListAdapter(Context context, ArrayList<Experiences> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
        // bitmapManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(),
        // R.drawable.ic_launcher));
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mItemList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mItemList.get(position);
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
    	Experiences experience = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            // trip_detail_item
            convertView = inflater.inflate(R.layout.item_experience_layout, null);
            holder = new ViewHolder();
            // 详细行程标题
            // holder.trip_item_index = (TextView)convertView.findViewById(R.id.trip_item_index);
            holder.day = (TextView)convertView.findViewById(R.id.day);
            holder.month = (TextView)(TextView)convertView.findViewById(R.id.month);
            holder.desc = (TextView)(TextView)convertView.findViewById(R.id.desc);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.gridview=(MyGridView)convertView.findViewById(R.id.gridview);
            // holder.taskListView = (LinearLayout)convertView.findViewById(R.id.trip_days_task_layout);
            // holder.serviceListView = (LinearLayout)convertView.findViewById(R.id.trip_days_service_layout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        // holder.trip_item_index.setText("D" + (position + 1));
        String travelTime=experience.getTravelTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		 Date date;
		try {
			date = format.parse(travelTime);
			  Calendar c2 = Calendar.getInstance();
			  c2.setTime(date);
			int month =c2.get(Calendar.MONTH)+1;
			int day =c2.get(Calendar.DAY_OF_MONTH);
			 SimpleDateFormat format1 = new SimpleDateFormat("yyyy年M月dd日");
			 holder.date.setText(format1.format(date));
			 holder.day.setText(Integer.toString(day));
			 holder.month.setText(month+"月");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.desc.setText(experience.getDescription());
		 ArrayList<ImageInfo> images=(ArrayList<ImageInfo>) experience.getImages();
		 if(images.size()>0){
		 ExpericeImageAdapter adapter=new ExpericeImageAdapter(mContext, images);
		 holder.gridview.setAdapter(adapter);
		 }
        
        // journey.getTrafficList().get(position).getTitleImage();
        // holder.trip_item_index.setBackgroundColor(mContext.getResources().getColor(R.color.title_bg));
        return convertView;
    }
    
    private ImageView findViewById(int scenicSpotsPhoto)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public class ViewHolder
    {
        // TextView trip_item_index;
        
        TextView day;
        
        TextView month;
        
        TextView date;
        TextView desc;
        
        MyGridView gridview;
        
        // LinearLayout taskListView;
        
        // LinearLayout serviceListView;
    }
    
    public static void main(String[] args)
    {
        
    }
}
