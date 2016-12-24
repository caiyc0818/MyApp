package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AirplaneInfo;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 行程规划详情航班列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class TripPlanDetaiAirListAdapter extends BaseAdapter
{
    private static final String TAG = "TripPlanDetaiAirListAdapter";
    
    private Context mContext;
    
    private ArrayList<ProductServiceResource> mItemList;
    
    public TripPlanDetaiAirListAdapter(Context context, ArrayList<ProductServiceResource> list)
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
        ProductServiceResource resource = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.trip_plan_detail_air_listitem, null);
            holder = new ViewHolder();
            holder.airline_name = (TextView)convertView.findViewById(R.id.airline_name);
            holder.airline_logo = (ImageView)convertView.findViewById(R.id.airline_logo);
            holder.flight_description = (TextView)convertView.findViewById(R.id.flight_description);
            holder.start_city_cn = (TextView)convertView.findViewById(R.id.start_city_cn);
            holder.start_city_en = (TextView)convertView.findViewById(R.id.start_city_en);
            holder.end_city_cn = (TextView)convertView.findViewById(R.id.end_city_cn);
            holder.end_city_en = (TextView)convertView.findViewById(R.id.end_city_en);
            holder.flight_total_time = (TextView)convertView.findViewById(R.id.flight_total_time);
            holder.plane_name = (TextView)convertView.findViewById(R.id.plane_name);
            holder.flight_price = (TextView)convertView.findViewById(R.id.flight_price);
            holder.start_airport = (TextView)convertView.findViewById(R.id.start_airport);
            holder.end_airport = (TextView)convertView.findViewById(R.id.end_airport);
            holder.start_time = (TextView)convertView.findViewById(R.id.start_time);
            holder.end_time = (TextView)convertView.findViewById(R.id.end_time);
            holder.flight_days = (TextView)convertView.findViewById(R.id.flight_days);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.airline_name.setText(resource.getServName());
        if (!StringUtils.isEmpty(resource.getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(), holder.airline_logo);
        }
        
        holder.flight_description.setText(resource.getServDesc());
        holder.flight_price.setText(resource.getFee());
        AirplaneInfo flight = JsonUtil.getAirplaneInfo(resource.getResourceExt());
        if (flight != null)
        {
            // holder.start_city_cn.setText(flight.getStartingpoint());
            // holder.start_city_en.setText(flight.getStartingPointAlias());
            // holder.end_city_cn.setText(flight.getDescPoint());
            // holder.end_city_en.setText(flight.getDescPointAlias());
            holder.flight_total_time.setText(flight.getArriveTime());
            holder.plane_name.setText(flight.getAirline());
            holder.start_airport.setText(flight.getStartingPoint());
            holder.end_airport.setText(flight.getDescPoint());
            holder.start_time.setText(flight.getDepartureTime());
            holder.end_time.setText(flight.getArriveTime());
            // holder.flight_days.setText(flight.getArriveTime());
        }
        return convertView;
    }
    
    public class ViewHolder
    {
        TextView airline_name;
        
        ImageView airline_logo;
        
        TextView flight_description;
        
        TextView start_city_cn;
        
        TextView start_city_en;
        
        TextView end_city_cn;
        
        TextView end_city_en;
        
        TextView flight_total_time;
        
        TextView plane_name;
        
        TextView flight_price;
        
        TextView start_airport;
        
        private TextView end_airport;
        
        private TextView start_time;
        
        private TextView end_time;
        
        TextView flight_days;
    }
}
