package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.TipsDetailInfo;

/**
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月9日 下午3:55:10
 */
public class TipsDetailAdapter extends BaseAdapter
{
    private static final String TAG = "TipsDetailAdapter";
    
    private Context mContext;
    
    private ArrayList<TipsDetailInfo> mItemList;
    
    public TipsDetailAdapter(ArrayList<TipsDetailInfo> list, Context context)
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
        TipsDetailInfo tipdetail = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.tips_detail_list_item, null);
            holder = new ViewHolder();
            holder.Title = (TextView)convertView.findViewById(R.id.tips_detail_title);
            holder.content = (TextView)convertView.findViewById(R.id.tips_detail_content);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.Title.setText(tipdetail.getTitle());
        holder.content.setText(tipdetail.getContent());
        return convertView;
    }
    
    public class ViewHolder
    {
        public TextView Title;
        
        public TextView content;
        
    }
}
