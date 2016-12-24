package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ScheduleEvent;
import com.bcinfo.tripaway.db.ScheduleEventDatabase;
import com.bcinfo.tripaway.utils.LogUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 日程事件列表适配
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年11月29日 上午10:04:40
 */
public class EventListAdapter extends BaseAdapter
{
    protected static final String TAG = "EventListAdapter";
    private Context mContext;
    private ArrayList<ScheduleEvent> scheduleEventList;
    private ScheduleEventDatabase mDatabase;

    public EventListAdapter(Context context, ArrayList<ScheduleEvent> scheduleEventList, ScheduleEventDatabase database)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.scheduleEventList = scheduleEventList;
        this.mDatabase = database;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return scheduleEventList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return scheduleEventList.get(position);
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
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.event_list_item, null);
        final ViewHolder holder = new ViewHolder();
        holder.eventColor = (ImageView) convertView.findViewById(R.id.event_color);
        holder.eventContent = (TextView) convertView.findViewById(R.id.event_content);
        holder.isFinish = (CheckBox) convertView.findViewById(R.id.is_finish);
        holder.eventContent.setText(scheduleEventList.get(position).getContent());
        holder.eventContent.setTextColor(mContext.getResources().getColor(R.color.gray_more_5));
        String color = scheduleEventList.get(position).getColor();
        final String createTime = scheduleEventList.get(position).getCreateTime();
        if (color != null && color.equals("green"))
        {
            holder.eventColor.setBackgroundColor(mContext.getResources().getColor(R.color.event_green));
        }
        else if (color != null && color.equals("gray"))
        {
            holder.eventColor.setBackgroundColor(mContext.getResources().getColor(R.color.event_gray));
        }
        else
        {
            holder.eventColor.setBackgroundColor(mContext.getResources().getColor(R.color.event_red));
        }
        LogUtil.i(TAG, "getView", "isChecked="+scheduleEventList.get(position).getIsFinish());
        if (scheduleEventList.get(position).getIsFinish().equals("true"))
        {
            holder.isFinish.setChecked(true);
            holder.eventContent.setTextColor(mContext.getResources().getColor(R.color.gray_little));
            holder.eventContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            holder.isFinish.setChecked(false);
        }
        holder.isFinish.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // TODO Auto-generated method stub
                LogUtil.i(TAG, "onCheckedChanged", "isChecked="+isChecked);
                if (isChecked)
                {
                    holder.eventContent.setTextColor(mContext.getResources().getColor(R.color.gray_little));
                    holder.eventContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else
                {
                    holder.eventContent.setTextColor(mContext.getResources().getColor(R.color.gray_more_5));
                    holder.eventContent.getPaint().setFlags(0);
                }
                ArrayList<ScheduleEvent> scheduleEventList = mDatabase.queryEventByCreateTime(createTime);
                for (int i = 0; i < scheduleEventList.size(); i++)
                {
                    ScheduleEvent scheduleEvent = scheduleEventList.get(i);
                    if (isChecked)
                    {
                        scheduleEvent.setIsFinish("true");
                    }
                    else
                    {
                        scheduleEvent.setIsFinish("false");
                    }
                }
                mDatabase.updateEventList(scheduleEventList);
            }
        });
        return convertView;
    }
    public class ViewHolder
    {
        /*
         * 事件颜色
         */
        public ImageView eventColor;
        /*
         * 事件内容
         */
        public TextView eventContent;
        /*
         * 是否完成
         */
        public CheckBox isFinish;
    }
}
