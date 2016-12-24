package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Facility;

/**
 * 汽车详情列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月23日 下午2:20:09
 */
public class CarInfoGridViewAdapter extends BaseAdapter
{
    protected static final String TAG = "CarInfoGridViewAdapter";
    
    private Context mContext;
    
    private List<Facility> mItemList;
    
    public CarInfoGridViewAdapter(Context context, List<Facility> list)
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        Facility facility = mItemList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.car_info_grid_item, null);
            holder.isContain = (ImageView)convertView.findViewById(R.id.is_contain);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name.setText(facility.getFacilityName());
        if (facility.getStatus().equals("1"))
        {
            holder.isContain.setImageResource(R.drawable.have);
        }
        else
        {
            holder.isContain.setImageResource(R.drawable.forbid);
            holder.name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return convertView;
    }
    
    public class ViewHolder
    {
        /*
         * 是否包含
         */
        public ImageView isContain;
        
        /*
         * 名称
         */
        public TextView name;
    }
}
