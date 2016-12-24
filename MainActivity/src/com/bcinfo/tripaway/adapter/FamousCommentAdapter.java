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
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.bean.FamousComment;

public class FamousCommentAdapter extends BaseAdapter
{
    private List<FamousComment> famousCommentList ;
    
    private Context mContext;
    
    private LayoutInflater inflater;
    
    public FamousCommentAdapter(Context mContext, List<FamousComment> famousCommentList)
    {
        this.mContext = mContext;
        this.famousCommentList = famousCommentList;
        inflater = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return famousCommentList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return famousCommentList.get(position);
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
            convertView = inflater.inflate(R.layout.item_refer_layout, null);
            holder.name = (TextView)convertView.findViewById(R.id.referrer_name);
            holder.comment = (TextView)convertView.findViewById(R.id.referrer_content);
            holder.line = (View)convertView.findViewById(R.id.item_refer_line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name.setText("来自"+famousCommentList.get(position).getName()+"的推荐");
        holder.comment.setText(famousCommentList.get(position).getComment());
        if(position==famousCommentList.size()-1){
        	holder.line.setVisibility(View.INVISIBLE);
        }else {
        	holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView name;
        private TextView comment;
        private View line;
    }
    
}
