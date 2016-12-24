package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.utils.DateUtil;

/**
 * @author hanweipeng
 * @date 2015-7-10 下午7:22:47
 */
public class MoreExperiodTimeAdapter extends BaseAdapter
{
    
    private Context mContext;
    
    private List<AvailableTime> exPeriodList = new ArrayList<AvailableTime>();
    
    private LayoutInflater inflator;
    
    public MoreExperiodTimeAdapter(Context mContext, List<AvailableTime> exPeriodList)
    {
        this.mContext = mContext;
        this.exPeriodList = exPeriodList;
        inflator = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
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
            convertView = inflator.inflate(R.layout.more_time_item, null);
            holder.titleLayout = (LinearLayout)convertView.findViewById(R.id.title_layout);
            holder.monthTitle = (TextView)convertView.findViewById(R.id.month_title);
            holder.timeItem = (TextView)convertView.findViewById(R.id.time_item);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.titleLayout.setVisibility(View.GONE);
        if (position == 0)
        {
            holder.titleLayout.setVisibility(View.VISIBLE);
            holder.monthTitle.setText(getMonthStr(exPeriodList.get(position).getBeginDate()));
        }
        else
        {
            if (!getMonthStr(exPeriodList.get(position - 1).getBeginDate()).equals(getMonthStr(exPeriodList.get(position)
                .getBeginDate())))
            {
                holder.titleLayout.setVisibility(View.VISIBLE);
                holder.monthTitle.setText(getMonthStr(exPeriodList.get(position).getBeginDate()));
            }
        }
        
        AvailableTime availableTime = exPeriodList.get(position);
        holder.timeItem.setText(DateUtil.formateDateToTime(availableTime.getBeginDate()) + "-"
            + DateUtil.formateDateToTime(availableTime.getEndDate()));
        
        return convertView;
    }
    
    private String getMonthStr(String dateStr)
    {
        String monthStr = dateStr.substring(4, 6);
        return monthStr;
    }
    
    private class ViewHolder
    {
        private LinearLayout titleLayout;
        
        private TextView monthTitle;
        
        private TextView timeItem;
        
    }
}
