package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Journey;
import com.bcinfo.tripaway.bean.ServResrouce;

/**
 * 产品详情的行程规划预览列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class NewDayListToListAdapter extends BaseAdapter
{
    private static final String TAG = "ProductTripPlanListAdapter";
    
    private Context mContext;
    
    private ArrayList<Journey> mItemList = new ArrayList<Journey>();
    
    public NewDayListToListAdapter(Context context, ArrayList<Journey> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
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
            convertView = inflater.inflate(R.layout.a_day_line_list_item, null);
            holder = new ViewHolder();
            // 详细行程标题
            holder.trip_item_index = (TextView)convertView.findViewById(R.id.trip_item_index);
            
            holder.trip_item_title = (TextView)convertView.findViewById(R.id.trip_item_title);
           // holder.taskListView = (LinearLayout)convertView.findViewById(R.id.trip_days_task_layout);
            //holder.serviceListView = (LinearLayout)convertView.findViewById(R.id.trip_days_service_layout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.trip_item_index.setText("D" + (position + 1));
        holder.trip_item_title.setText(journey.getTitle());// 标题
       // holder.taskListView.removeAllViews();
       // holder.serviceListView.removeAllViews();
        //设置小12345
        if (journey.getAttractionsList() != null)
        {
            for (int j = 0; j < journey.getAttractionsList().size(); j++)
            {
                ServResrouce service = journey.getAttractionsList().get(j);
                String type = service.getServType();
                LinearLayout taskItem = (LinearLayout)inflater.inflate(R.layout.trip_days_task_item, null);
                
                TextView trip_task_index = (TextView)taskItem.findViewById(R.id.trip_task_index);
                TextView trip_task_content = (TextView)taskItem.findViewById(R.id.trip_task_content);
                trip_task_index.setText((j + 1) + "");
                trip_task_content.setText(service.getServName());
               // holder.taskListView.addView(taskItem);
            }
        }
        if (journey.getTrafficList() != null)
        {
            for (int j = 0; j < journey.getTrafficList().size(); j++)
            {
                ServResrouce service = journey.getTrafficList().get(j);
                String type = service.getServType();
                LinearLayout serviceItem = (LinearLayout)inflater.inflate(R.layout.trip_days_service_item, null);
                TextView trip_service_content = (TextView)serviceItem.findViewById(R.id.trip_service_content);
                trip_service_content.setText(service.getServName());
               // holder.serviceListView.addView(serviceItem);
            }
        }
        if (journey.getStayList() != null)
        {
            for (int j = 0; j < journey.getStayList().size(); j++)
            {
                ServResrouce service = journey.getStayList().get(j);
                String type = service.getServType();
                LinearLayout serviceItem = (LinearLayout)inflater.inflate(R.layout.trip_days_service_item, null);
                TextView trip_service_content = (TextView)serviceItem.findViewById(R.id.trip_service_content);
                trip_service_content.setText(service.getServName());
               // holder.serviceListView.addView(serviceItem);
            }
        }
        holder.trip_item_index.setText("D" + (position + 1));
        holder.trip_item_index.setBackgroundColor(mContext.getResources().getColor(R.color.title_bg));
        return convertView;
    }
    
    public class ViewHolder
    {
        TextView trip_item_index;
        
        TextView trip_item_title;
        
       // LinearLayout taskListView;
        
      //  LinearLayout serviceListView;
    }
}
