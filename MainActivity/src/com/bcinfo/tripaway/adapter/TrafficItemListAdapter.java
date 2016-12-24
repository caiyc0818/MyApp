package com.bcinfo.tripaway.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 交通 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月20日 15:42:11
 * 
 */
public class TrafficItemListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Map<String, Object>> itemList;
    
    private LayoutInflater inflator;
    
    public List<Map<String, Object>> getItemList()
    {
        return itemList;
    }
    
    public void setItemList(List<Map<String, Object>> itemList)
    {
        this.itemList = itemList;
    }
    
    public TrafficItemListAdapter(Context context, List<Map<String, Object>> itemList)
    {
        super();
        this.context = context;
        this.itemList = itemList;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return itemList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return itemList.get(position);
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
        ViewHolder holder = null;
        Map<String, Object> mapItem = itemList.get(position);
        if (convertView == null)
        {
            inflator = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_traffic_service_layout, null);
            holder.trafficImgView = (ImageView)convertView.findViewById(R.id.traffic_item_ImageView);
            holder.trafficNameTv = (TextView)convertView.findViewById(R.id.traffic_item_name);
            holder.trafficYearTv = (TextView)convertView.findViewById(R.id.traffic_item_year);
            holder.seatedCountTv = (TextView)convertView.findViewById(R.id.traffic_item_seated_count);
            holder.trafficCategoryTv = (TextView)convertView.findViewById(R.id.traffic_item_category);
            holder.trafficWifiStateTv = (TextView)convertView.findViewById(R.id.traffic_item_wifi_state);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            
        }
        if (!StringUtils.isEmpty((String)mapItem.get("carUrl")))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("carUrl"), holder.trafficImgView);
        }
        
        holder.trafficYearTv.setText((String)mapItem.get("carBuyYear"));
        holder.trafficNameTv.setText((String)mapItem.get("carName"));
        holder.seatedCountTv.setText((String)mapItem.get("carSeatedCount"));
        holder.trafficCategoryTv.setText((String)mapItem.get("carCategory"));
        if (((Boolean)mapItem.get("carWifiState")))
        {
            holder.trafficWifiStateTv.setText("车载wifi");
        }
        else
        {
            holder.trafficWifiStateTv.setText("车无wifi");
            
        }
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private ImageView trafficImgView;
        
        private TextView trafficNameTv;
        
        private TextView trafficYearTv;
        
        private TextView seatedCountTv;
        
        private TextView trafficCategoryTv;
        
        private TextView trafficWifiStateTv;
    }
    
}
