package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.utils.DateUtil;

/**
 * @author hanweipeng
 * @date 2015-7-8 下午4:31:52
 */
public class ExpPeriodAdapter extends BaseAdapter
{
    private Context mContext;
    
    private List<AvailableTime> exPeriodList = new ArrayList<AvailableTime>();
    
    private LayoutInflater inflator;
    
    public ExpPeriodAdapter(Context mContext, List<AvailableTime> exPeriodList)
    {
        this.mContext = mContext;
        this.exPeriodList = exPeriodList;
        inflator = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        if (exPeriodList.size() > 2)
        {
            return 2;
        }
        return exPeriodList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return exPeriodList.get(position);
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
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.time_item, null);
            holder.textView = (TextView)convertView.findViewById(R.id.experience_time);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        AvailableTime availableTime = exPeriodList.get(position);
        holder.textView.setText(DateUtil.formateDateToTime(availableTime.getBeginDate()) + "-"
            + DateUtil.formateDateToTime(availableTime.getEndDate()));
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView textView;// 粉丝-姓名textview
        
    }
}
