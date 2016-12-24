package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.net.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;


public class TravelExperienceAdapter extends BaseAdapter
{
    
    
    private Context mContext;
    
    private ImageView[] imageViews = null;
    
    private List<Experiences> Experiences;
    
    public TravelExperienceAdapter(Context mContext, List<Experiences> Experiences)
    {
        this.mContext = mContext;
        this.Experiences = Experiences;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return Experiences.size();
        
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return Experiences.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // System.out.println("****************getView");
        
        LayoutInflater inflator;
        ViewHolder holder = null;
        Experiences experience = Experiences.get(position);
        System.out.println("****************getView" + Experiences.size());
        if (convertView == null)
        {
            inflator = LayoutInflater.from(mContext);
            holder = new ViewHolder();
            
            convertView = inflator.inflate(R.layout.travel_experience_item, null);
            holder.experienceDesc = (TextView)convertView.findViewById(R.id.experience_desc);
            holder.TravelImageLayout = (LinearLayout)convertView.findViewById(R.id.travel_image_layout);
            holder.TravelTimeDateDay = (TextView)convertView.findViewById(R.id.date_day);
            holder.TravelTimeDateMonth = (TextView)convertView.findViewById(R.id.date_month);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.experienceDesc.setText(experience.getDescription());
        
        if (experience.getImages() != null)
        {
            
            int size = experience.getImages().size();
            // System.out.println("****************foroutside"+size);
            if (size > 3)
            {
                size = 3;
                imageViews = new ImageView[size];
                holder.TravelImageLayout.setVisibility(View.VISIBLE);
            }
            else if (size != 0)
            {
                imageViews = new ImageView[size];
                holder.TravelImageLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.TravelImageLayout.removeAllViews();
            }
            holder.TravelImageLayout.removeAllViews();
            for (int j = 0; j < size; j++)
            {
                // System.out.println("****************"+size);
                
                ImageInfo image = experience.getImages().get(j);
                View view = new View(mContext);
                view.setLayoutParams(new LayoutParams(5, 300)); // 图片之间的间距
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(300, 300)); // 图片 宽高
                imageView.setScaleType(ScaleType.FIT_XY);
                imageViews[j] = imageView;
                // 见到空指针就 判断 非空 非 null 再做逻辑处理。
                if (!image.getUrl().equals(null) || image.getUrl() != " ")
                {
                    // holder.taskLisTitlimag.setVisibility(View.GONE);
                    
                    // 这个一图片处理的类，两个参数，分别是 容器 url 路径
                    ImageLoader.getInstance().displayImage(Urls.imgHost + image.getUrl(), imageView);
                    // ImageLoader.getInstance().displayImage(image.getUrl(),
                    // imageView);
                    // 之前他自己写的大图片的一个图片管理，参数是一个 url 和一个默认的图片
                    // bitmapManager.loadBitmap(service.getTitleImage(),
                    // imageView, null, 300, 300);
                    
                    holder.TravelImageLayout.addView(imageView);
                    holder.TravelImageLayout.addView(view);// 给图片之间加个空白间距
                }
            }
            
        }
        
        return convertView;
    }
    
    private class ViewHolder
    {
        ImageView experienceTitleTitleImage;// 经历标题图片
        
        // TextView experienceTitle;// 经历标题
        TextView TravelTimeDateDay;// 经历时间日
        
        // TextView productTopicTv;// 产品主题
        TextView TravelTimeDateMonth;// 经历时间月
        
        TextView experienceDesc;// 经历描述
        
        LinearLayout TravelImageLayout;
        // TextView productDaySchedualTv;// 产品 天数
        
        // TextView productDistanceTv;// 产品 里程距离
        
        // TextView productIntroduceTv;// 产品 介绍 说明
    }
}
