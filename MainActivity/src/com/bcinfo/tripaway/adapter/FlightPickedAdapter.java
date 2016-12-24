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
 * 选择航班 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月31日 11:19:33
 */
public class FlightPickedAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Map<String, Object>> mapList;
    
    private LayoutInflater inflator;
    
    public FlightPickedAdapter(Context context, List<Map<String, Object>> mapList)
    {
        this.context = context;
        this.mapList = mapList;
    }
    
    @Override
    public int getCount()
    {
        return mapList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mapList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Map<String, Object> mapItem = mapList.get(position);
        if (convertView == null)
        {
            inflator = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_flight_picked_listview, null);
            holder.flightIv = (ImageView)convertView.findViewById(R.id.flight_agent_icon_iv);
            holder.flightAgentNameTv = (TextView)convertView.findViewById(R.id.flight_agent_name_tv);
            holder.flightSchedualTime = (TextView)convertView.findViewById(R.id.flight_time_tv);
            holder.flightPriceTv = (TextView)convertView.findViewById(R.id.flight_price_tv);
            holder.airPortDepartzhNameTv = (TextView)convertView.findViewById(R.id.flight_airport_departure_zhCNName);
            holder.airPortDepartenNameTv = (TextView)convertView.findViewById(R.id.flight_airport_departure_enUSName);
            holder.departTimeTv = (TextView)convertView.findViewById(R.id.flight_departure_startTime);
            holder.airPortDestinatezhNameTv =
                (TextView)convertView.findViewById(R.id.flight_airport_destination_zhCNName);
            holder.airPortDestinateEnNameTv =
                (TextView)convertView.findViewById(R.id.flight_airport_destination_enUSName);
            holder.endTimeTv = (TextView)convertView.findViewById(R.id.flight_destination_endTime);
            holder.flightDelayTimeTv = (TextView)convertView.findViewById(R.id.flight_delay_days_attach_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty((String)mapItem.get("flightUrl")))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + (String)mapItem.get("flightUrl"), holder.flightIv);
        }
        
        holder.flightAgentNameTv.setText((String)mapItem.get("flightName"));
        holder.flightSchedualTime.setText((String)mapItem.get("schedualTime"));
        holder.flightPriceTv.setText((String)mapItem.get("flightPrice"));
        holder.airPortDepartzhNameTv.setText((String)mapItem.get("airPortDeparturezhName"));
        holder.airPortDepartenNameTv.setText((String)mapItem.get("airPortDepartureEnName"));
        holder.departTimeTv.setText((String)mapItem.get("departureTime"));
        holder.airPortDestinatezhNameTv.setText((String)mapItem.get("airPortDestinatezhName"));
        holder.airPortDestinateEnNameTv.setText((String)mapItem.get("airPortDestinateEnName"));
        holder.endTimeTv.setText((String)mapItem.get("destinateTime"));
        if ((Boolean)mapItem.get("flightIsDay"))
        {
            holder.flightDelayTimeTv.setVisibility(View.VISIBLE);
            holder.flightDelayTimeTv.setText("+1天");
        }
        else
        {
            holder.flightDelayTimeTv.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    private class ViewHolder
    {
        private ImageView flightIv;// 航班imageview
        
        private TextView flightAgentNameTv;// 航班机构名称
        
        private TextView flightSchedualTime;// 航班计划时间
        
        private TextView flightPriceTv;// 航班价格
        
        private TextView airPortDepartzhNameTv;// 出发地机场名称(中文)
        
        private TextView airPortDepartenNameTv;// 出发地机场名称(英文)
        
        private TextView departTimeTv;// 出发时间
        
        private TextView airPortDestinatezhNameTv;// 目的地机场名称(中文)
        
        private TextView airPortDestinateEnNameTv;// 目的地机场名称(英文)
        
        private TextView endTimeTv;// 结束时间
        
        private TextView flightDelayTimeTv;// 航班延迟到达的时间
    }
}
