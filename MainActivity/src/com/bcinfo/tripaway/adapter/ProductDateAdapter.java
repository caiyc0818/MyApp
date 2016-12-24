package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.utils.DateUtil;

/**
 * @author hanweipeng
 * @date 2015-7-9 下午7:01:14
 */
public class ProductDateAdapter extends BaseAdapter
{
    private Context mContext;
    
    private List<AvailableTime> timeList;
    
    private LayoutInflater inflater;
    
    private int selectPosition = -1;
    
    private int day = 0;
    
    public ProductDateAdapter(Context mContext, List<AvailableTime> timeList, int day)
    {
        this.mContext = mContext;
        this.timeList = timeList;
        this.day = day;
        inflater = LayoutInflater.from(mContext);
    }
    
    public void setSelectPosition(int selectPosition)
    {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return timeList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return timeList.get(position);
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
            convertView = inflater.inflate(R.layout.product_date_item, null);
            holder.dateLayout = (RelativeLayout)convertView.findViewById(R.id.date_layout);
            holder.beginDateTxt = (TextView)convertView.findViewById(R.id.begindate_txt);
            holder.endDateTxt = (TextView)convertView.findViewById(R.id.enddate_txt);
            holder.dayTxt = (TextView)convertView.findViewById(R.id.day_txt);
            holder.lineView = (View)convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        AvailableTime date = timeList.get(position);
        holder.dateLayout.setBackgroundResource(R.color.white);
        holder.beginDateTxt.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
        holder.endDateTxt.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
        holder.dayTxt.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
        holder.lineView.setBackgroundResource(R.color.dark_gray);
        // 当前时间大于产品开始时间 背景置灰不允许点击
        if (DateUtil.getTimeInMillis(date.getBeginDate()) < DateUtil.getcurrentTimeMillis())
        {
            holder.dateLayout.setBackgroundResource(R.color.gray);
        }
        if (position == selectPosition)
        {
            holder.dateLayout.setBackgroundResource(R.color.title_bg);
            holder.beginDateTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.endDateTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.dayTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.lineView.setBackgroundResource(R.color.white);
        }
        holder.beginDateTxt.setText(DateUtil.formateDateToTime(date.getBeginDate()));
        holder.endDateTxt.setText(DateUtil.formateDateToTime(date.getEndDate()));
        holder.dayTxt.setText(day + "");
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView beginDateTxt;
        
        private TextView endDateTxt;
        
        private TextView dayTxt;
        
        private View lineView;
        
        private RelativeLayout dateLayout;
        
    }
}
