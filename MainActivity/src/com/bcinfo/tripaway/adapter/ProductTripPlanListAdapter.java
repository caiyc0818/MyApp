package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

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
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 产品详情的行程规划预览列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class ProductTripPlanListAdapter extends BaseAdapter
{
    private static final String TAG = "ProductTripPlanListAdapter";
    
    private ImageView[] imageViews = null;
    
    
    private Context mContext;
    
    private int startPosition;
    
    private BitmapManager bitmapManager;
    
    private ArrayList<ImageInfo> productTitleImgList;
    
    private ArrayList<Journey> mItemList = new ArrayList<Journey>();
    
    private String TimeUnit="天";
    		
    private int w;		
    
    public ProductTripPlanListAdapter(Context context, ArrayList<Journey> list,ArrayList<ImageInfo> productTitleImgList)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
        this.productTitleImgList=productTitleImgList;
        // bitmapManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(),
        // R.drawable.ic_launcher));
        DisplayMetrics  dm = new DisplayMetrics();
    	  ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    	  w=(dm.widthPixels-DensityUtil.dip2px(context, 80))/3;
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
        Journey journey = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            // trip_detail_item
            convertView = inflater.inflate(R.layout.a_day_line_list_item, null);
            holder = new ViewHolder();
            // 详细行程标题
            // holder.trip_item_index = (TextView)convertView.findViewById(R.id.trip_item_index);
            holder.trip_item_title = (TextView)convertView.findViewById(R.id.trip_item_title);
            holder.trip_item_content = (TextView)(TextView)convertView.findViewById(R.id.work_list_item_content);
            
            holder.taskLisTitlimag = (LinearLayout)convertView.findViewById(R.id.scenic_spots_photo_layout);
            // holder.taskListView = (LinearLayout)convertView.findViewById(R.id.trip_days_task_layout);
            // holder.serviceListView = (LinearLayout)convertView.findViewById(R.id.trip_days_service_layout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        // holder.trip_item_index.setText("D" + (position + 1));
        if(journey.getTitle().contains("第" + (position + 1) + TimeUnit+"："))
        	  holder.trip_item_title.setText(journey.getTitle());// 标题
        else 
        holder.trip_item_title.setText("第" + (position + 1) + TimeUnit+"：" + journey.getTitle());// 标题
        
        // holder.taskListView.removeAllViews();
        // holder.serviceListView.removeAllViews();
        // if (journey.getAttractionsList() != null)
        // {
        // for (int j = 0; j < journey.getAttractionsList().size(); j++)
        // {
        // ServResrouce service = journey.getAttractionsList().get(j);
        // String type = service.getServType();
        // LinearLayout taskItem = (LinearLayout)inflater.inflate(R.layout.trip_days_task_item, null);
        // TextView trip_task_index = (TextView)taskItem.findViewById(R.id.trip_task_index);
        // TextView trip_task_content = (TextView)taskItem.findViewById(R.id.trip_task_content);
        // trip_task_index.setText((j + 1) + "");
        // trip_task_content.setText(service.getServName());
        // // holder.taskListView.addView(taskItem);
        // }
        // }
        // if (journey.getTrafficList() != null)
        // {
        // for (int j = 0; j < journey.getTrafficList().size(); j++)
        // {
        // ServResrouce service = journey.getTrafficList().get(j);
        // String type = service.getServType();
        // LinearLayout serviceItem = (LinearLayout)inflater.inflate(R.layout.trip_days_service_item, null);
        // TextView trip_service_content = (TextView)serviceItem.findViewById(R.id.trip_service_content);
        // trip_service_content.setText(service.getServName());
        // // holder.serviceListView.addView(serviceItem);
        // }
        // }
        if (journey.getAttractionsList() != null)// &&journey.getAttractionsList().size()<3
        {
            int size = journey.getAttractionsList().size();
            if (size > 3)
            {
                size = 3;
                
                imageViews = new ImageView[size];
                holder.taskLisTitlimag.setVisibility(View.VISIBLE);
                // journey.getAttractionsList().size()
            }
            else if (size != 0)
            {
                imageViews = new ImageView[size]; // journey.getAttractionsList().size()
                
            }
            else
            {
                
                // holder.taskLisTitlimag.setVisibility(View.GONE);
                
                holder.taskLisTitlimag.removeAllViews();
            }
            
            holder.taskLisTitlimag.removeAllViews();
            // journey.getAttractionsList().size()
            for (int j = 0; j < size; j++)
            {
                
                 final ServResrouce service = journey.getAttractionsList().get(j); // 一个对象
                // LinearLayout taskItem = (LinearLayout)inflater.inflate(R.layout.scenic_spots_gridview_item, null);
                // ImageView scenic_spots_photo = (ImageView)findViewById(R.id.scenic_spots_photo);
                // ImageLoader.getInstance().displayImage(Urls.imgHost+service.getTitleImage(), scenic_spots_photo);
                // holder.taskLisTitlimag.addView(scenic_spots_photo);
                View view = new View(mContext);
                view.setLayoutParams(new LayoutParams(DensityUtil.dip2px(mContext, 5),w)); // 图片之间的间距
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(w,w)); // 图片 宽高
                imageView.setScaleType(ScaleType.FIT_XY);
                imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						//Toast.makeText(mContext, "点击到了"+productTitleImgList.size(), Toast.LENGTH_SHORT).show();
						 if (productTitleImgList.size() > 0)
			                {
							 
							 //System.out.println("***********"+service.getTitleImage()+"***********"+productTitleImgList.get(1).getUrl());
							 for(int i=0;i<productTitleImgList.size();i++){
			                    	if(service.getTitleImage().equals(productTitleImgList.get(i).getUrl())){
			                    		startPosition=i;
			                    		System.out.println("***********"+startPosition);
			                    	}
			                    }			                    
							    Intent intentForView = new Intent(mContext, ImageViewerActivity.class);
			                    intentForView.putExtra("image_index", 0);
			                    intentForView.putExtra("STATE_POSITION", startPosition);
			                    intentForView.putParcelableArrayListExtra("images", productTitleImgList);
			                    mContext.startActivity(intentForView);
			                }
			                

						
						
						
						
					}
				});
                imageViews[j] = imageView;
                // imageView.setBackground(background)
                // 见到空指针就 判断 非空 非 null 再做逻辑处理。
                if (!service.getTitleImage().equals(null) || service.getTitleImage() != " ")
                {
                    // holder.taskLisTitlimag.setVisibility(View.GONE);
                    
                    // 这个一图片处理的类，两个参数，分别是 容器 url 路径
                    //finalBitmap.display(imageView, Urls.imgHost + service.getTitleImage());
                    ImageLoader.getInstance().displayImage(Urls.imgHost +service.getTitleImage(), imageView);
                    // 之前他自己写的大图片的一个图片管理，参数是一个 url 和一个默认的图片
                    // bitmapManager.loadBitmap(service.getTitleImage(), imageView, null, 300, 300);
                    
                    holder.taskLisTitlimag.addView(imageView);
                    holder.taskLisTitlimag.addView(view);// 给图片之间加个空白间距
                }/*
                  * else{ holder.taskLisTitlimag.setVisibility(View.GONE); }
                  */
                
            }
        }
        
        // holder.trip_item_index.setText("第" + (position + 1)+"天：");
        holder.trip_item_content.setText(journey.getDescription());
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
        
        TextView trip_item_title;
        
        TextView trip_item_content;
        
        LinearLayout taskLisTitlimag;
        
        // LinearLayout taskListView;
        
        // LinearLayout serviceListView;
    }
    
    public void setTimeUnit(String TimeUnit){
    	this.TimeUnit=TimeUnit;
    }
    public static void main(String[] args)
    {
        
    }
}
