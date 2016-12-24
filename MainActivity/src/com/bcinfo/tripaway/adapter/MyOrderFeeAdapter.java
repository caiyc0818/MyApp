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
import com.bcinfo.tripaway.bean.ServiceInfo;

/**
 * 我的订单费用列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月28日 下午5:42:59
 */
public class MyOrderFeeAdapter extends BaseAdapter
{
    private static final String TAG = "MyOrderFeeAdapter";
    
    private Context mContext;
    
    private List<ServiceInfo> mItemList;
    
    public MyOrderFeeAdapter(Context context, List<ServiceInfo> list)
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
        ServiceInfo serviceInfo = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.my_order_fee_listitem, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.fee_logo = (ImageView)convertView.findViewById(R.id.fee_logo);
        holder.fee_name = (TextView)convertView.findViewById(R.id.fee_name);
        holder.fee_price = (TextView)convertView.findViewById(R.id.fee_price);
        
        holder.fee_logo.setImageResource(R.drawable.flag_date);
        holder.fee_name.setText(serviceInfo.getName());
        holder.fee_price.setText(serviceInfo.getPrice());
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 费用图标
         */
        public ImageView fee_logo;
        
        /**
         * 费用名称
         */
        public TextView fee_name;
        
        /**
         * 费用价格
         */
        public TextView fee_price;
    }
}
