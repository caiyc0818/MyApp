package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.FlightInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 选择航班 adapter
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月16日 下午8:33:42
 */
public class SelectFlightAdapter extends BaseAdapter
{
    private Context context;
    
    private List<FlightInfo> mFlightInfoList;
    
    private LayoutInflater inflator;
    
    public SelectFlightAdapter(Context context, List<FlightInfo> flightInfoList)
    {
        this.context = context;
        this.mFlightInfoList = flightInfoList;
    }
    
    @Override
    public int getCount()
    {
        return mFlightInfoList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mFlightInfoList.get(position);
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
        FlightInfo info = mFlightInfoList.get(position);
        if (convertView == null)
        {
            inflator = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.select_flight_list_item, null);
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
        if (!StringUtils.isEmpty(info.getAirlinesLogo()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + info.getAirlinesLogo(), holder.flightIv);
        }
        
        holder.flightAgentNameTv.setText(info.getAirlinesName());
        holder.flightSchedualTime.setText(info.getFlightTotalTime());
        holder.flightPriceTv.setText(info.getFlightPrice());
        holder.airPortDepartzhNameTv.setText(info.getStartAirportCN());
        holder.airPortDepartenNameTv.setText(info.getStartAirportEN());
        holder.departTimeTv.setText(info.getStartTime());
        holder.airPortDestinatezhNameTv.setText(info.getEndAirportCN());
        holder.airPortDestinateEnNameTv.setText(info.getEndAirportEN());
        holder.endTimeTv.setText(info.getEndTime());
        if (info.getFlightDays() > 0)
        {
            holder.flightDelayTimeTv.setVisibility(View.VISIBLE);
            holder.flightDelayTimeTv.setText("+" + info.getFlightDays() + "天");
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
