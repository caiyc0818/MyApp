package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 侧边栏列表适配
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class SidebarListAdapter extends BaseAdapter
{
    private Context mContext;
    private List<String> nameList;
    private ArrayList<Integer> logoList;

    public SidebarListAdapter(Context context, List<String> nameList, ArrayList<Integer> logoList)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.nameList = nameList;
        this.logoList = logoList;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return nameList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return nameList.get(position);
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
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.sidebar_list_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.functionName = (TextView) convertView.findViewById(R.id.function_name);
        holder.functionLogo = (ImageView) convertView.findViewById(R.id.function_logo);
        holder.functionName.setText(nameList.get(position));
        holder.functionLogo.setImageResource(logoList.get(position));
        return convertView;
    }
    public class ViewHolder
    {
        public TextView functionName;
        public ImageView functionLogo;
    }
}
