package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.TripDestination;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 目的地 adapter适配器
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月14日 20:11:11
 */
public class ProductLocationsGridAdapter extends BaseAdapter
{
    private List<TripDestination> locationList;
    
    public Context context;
    public ProductLocationsGridAdapter(Context context, List<TripDestination> locationList)
    {
        this.context = context;
        this.locationList = locationList;
        System.out.println("locationList==" + locationList);
    }
    
    @Override
    public int getCount()
    {
        return locationList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return locationList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflator = LayoutInflater.from(context);
        TripDestination locationItem = (TripDestination)locationList.get(position);
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflator.inflate(R.layout.item_discovery_locations, null);
            holder = new ViewHolder();
            holder.locationFlagIv = (ImageView)convertView.findViewById(R.id.item_location_flag_iv);
            holder.locationenNameTv = (TextView)convertView.findViewById(R.id.item_location_enUSname_tv);
            holder.locationzhNameTv = (TextView)convertView.findViewById(R.id.item_location_zhCNname_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty(locationItem.getDestLogoKey()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + locationItem.getDestLogoKey(),
                holder.locationFlagIv,
                AppConfig.options(R.drawable.ic_launcher));
        }
        if (!StringUtils.isEmpty(locationItem.getDestNameEn()))
        holder.locationenNameTv.setText(locationItem.getDestNameEn());
        if (!StringUtils.isEmpty(locationItem.getDestName()))
        holder.locationzhNameTv.setText(locationItem.getDestName());
        return convertView;
    }
    
    class ViewHolder
    {
        private ImageView locationFlagIv;// 目的地国旗iv
        
        private TextView locationzhNameTv;// 目的地中文名tv
        
        private TextView locationenNameTv;// 目的地英文名tv
    }
}
