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
import com.bcinfo.tripaway.bean.ServResrouce;

/**
 * 我的订单服务列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月28日 下午5:42:59
 */
public class OrderServiceListAdapter extends BaseAdapter
{
    private static final String TAG = "OrderServiceListAdapter";
    
    private Context mContext;
    
    private List<ServResrouce> mItemList;
    
    public OrderServiceListAdapter(Context context, List<ServResrouce> list)
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
        ServResrouce resource = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.order_service_listitem, null);
            holder = new ViewHolder();
            holder.service_logo = (ImageView)convertView.findViewById(R.id.service_logo);
            holder.service_name = (TextView)convertView.findViewById(R.id.service_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.service_logo.setImageResource(R.drawable.flag_date);
        holder.service_name.setText(resource.getName());
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 服务图标
         */
        public ImageView service_logo;
        
        /**
         * 服务名称
         */
        public TextView service_name;
    }
}
