package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bcinfo.tripaway.R;

/**
 * 日期跳转按钮列表适配器
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年4月29日 下午4:01:08
 */
public class DayButtonListAdapter extends BaseAdapter
{
    protected static final String TAG = "DayButtonListAdapter";
    private Context mContext;
    private ArrayList<HashMap<String, String> > mItemList;

    public DayButtonListAdapter(Context context, ArrayList<HashMap<String, String> > list)
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
        HashMap<String, String> map=mItemList.get(position);
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.day_button_lv_item, null);
            holder.day_button = (CheckBox) convertView.findViewById(R.id.day_button);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.day_button.setText(map.get("day"));
        if(map.get("isChecked").equals("true"))
        {
            holder.day_button.setChecked(true);
        }
        else
        {
            holder.day_button.setChecked(false);
        }
        return convertView;
    }
    public class ViewHolder
    {
        public CheckBox day_button;
    }
}
