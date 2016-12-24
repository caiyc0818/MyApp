package com.bcinfo.tripaway.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 定制列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class CustomizationListAdapter extends BaseAdapter
{
    private static final String TAG = "CustomizationListAdapter";
    
    
    
    private Context mContext;
    
    
    
    private ArrayList<TraceLogs> traceLogsList;
    
    
    public CustomizationListAdapter(Context context, ArrayList<TraceLogs> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.traceLogsList=list;
        // bitmapManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(),
        // R.drawable.ic_launcher));
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return traceLogsList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return traceLogsList.get(position);
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
    	TraceLogs traceLog = traceLogsList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            // trip_detail_item
            convertView = inflater.inflate(R.layout.submit_list_item, null);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.summit_title);
            holder.createTime = (TextView)convertView.findViewById(R.id.summit_createTime);
            holder.status = (TextView)convertView.findViewById(R.id.summit_status);
            holder.step = (ImageView)convertView.findViewById(R.id.summit_step);
            holder.line1 = (ImageView)convertView.findViewById(R.id.line1);
            holder.line2 = (ImageView)convertView.findViewById(R.id.line2);
            // holder.taskListView = (LinearLayout)convertView.findViewById(R.id.trip_days_task_layout);
            // holder.serviceListView = (LinearLayout)convertView.findViewById(R.id.trip_days_service_layout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        // holder.trip_item_index.setText("D" + (position + 1));
        holder.title.setText(traceLog.getTitle()) ;  
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date;
		try {
			date = format.parse(traceLog.getCreateTime());
			SimpleDateFormat format1 = new SimpleDateFormat("(yyyy-MM-dd HH:mm:ss)");
			  holder.createTime.setText(format1.format(date)) ; 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status=traceLog.getStatus();
		if(!StringUtils.isEmpty(traceLog.getDesc()))
			holder.status.setText(traceLog.getDesc());
		else{
		if(status.equals("wait"))holder.status.setText("等待处理");
		else if(status.equals("success"))holder.status.setText("定制成功");
		else holder.status.setText("定制失败");
		}
        if(position==traceLogsList.size()-1){
        	holder.step.setImageResource(R.drawable.change_state_select);
        	if(position!=0)
        	holder.line2.setVisibility(View.GONE);
        } else {
        	holder.step.setImageResource(R.drawable.change_state_unselect);
        	holder.line2.setVisibility(View.VISIBLE);
        }
      
        if(position==0)holder.line1.setVisibility(View.GONE);
        else holder.line1.setVisibility(View.VISIBLE);
        
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
        
        TextView title;
        
        TextView createTime;
        
        TextView status;
        
        ImageView step;
        
        ImageView line1;
        
        ImageView line2;
        
        // LinearLayout taskListView;
        
        // LinearLayout serviceListView;
    }
    
}
