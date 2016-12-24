package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AppraiseInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReviewAdapter extends BaseAdapter
{
    ArrayList<AppraiseInfo> appraiseList = new ArrayList<AppraiseInfo>();

    public ArrayList<AppraiseInfo> getAppraiseList()
    {
        return appraiseList;
    }

    public void setAppraiseList(ArrayList<AppraiseInfo> appraiseList)
    {
        this.appraiseList = appraiseList;
    }

    private Context mContext;

    public ReviewAdapter(Context mContext, ArrayList<AppraiseInfo> appraiseList)
    {
        this.mContext = mContext;
        this.appraiseList = appraiseList;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return appraiseList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return appraiseList.get(position);
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
        ViewHolder holder;
        AppraiseInfo appraise = appraiseList.get(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.review_linearlayout, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(appraise.getName());
        holder.tvContent.setText(appraise.getContent());
        return convertView;
    }

    class ViewHolder
    {
        TextView tvName;
        TextView tvContent;
    }
}
