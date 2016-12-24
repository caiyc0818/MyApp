package com.bcinfo.tripaway.adapter;

import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PlanThings;
import com.bcinfo.tripaway.bean.RoutePlan;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * 行程规划列表适配器
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月19日 上午10:24:56
 */
public class PlanExpandableListAdapter extends BaseExpandableListAdapter
{
    protected static final String TAG = "ProductListAdapter";
    private Context mContext;
    private List<RoutePlan> groupItem;
    private List<List<PlanThings>> childItem;
    private BitmapManager bitmapManager;

    public PlanExpandableListAdapter(Context context, List<RoutePlan> groupData, List<List<PlanThings>> childData)
    {
        mContext = context;
        groupItem = groupData;
        childItem = childData;
        bitmapManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
    }

    @Override
    public boolean hasStableIds()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return true;
    }

    /*************************group******************************************/
    @Override
    public Object getGroup(int groupPosition)
    {
        // TODO Auto-generated method stub
        return groupItem.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        // TODO Auto-generated method stub
        return groupItem.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final GroupViewHolder holder = new GroupViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.plan_group_item, null);
        holder.dateIndex = (TextView) convertView.findViewById(R.id.plan_date_index);
        holder.dateContent = (TextView) convertView.findViewById(R.id.plan_date_content);
        holder.dateIndex.setText(groupItem.get(groupPosition).getDateIndex());
        holder.dateContent.setText(groupItem.get(groupPosition).getContent());
        return convertView;
    }
    private class GroupViewHolder
    {
        public TextView dateIndex;
        public TextView dateContent;
    }

    /*************************child******************************************/
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        if (groupPosition >= childItem.size() || childItem.get(groupPosition) == null)
        {
            return null;
        }
        return childItem.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        // TODO Auto-generated method stub
        if (groupPosition >= childItem.size() || childItem.get(groupPosition) == null)
        {
            return 0;
        }
        return childItem.get(groupPosition).size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final ChildViewHolder holder = new ChildViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.plan_child_item, null);
        holder.thingsIndex = (TextView) convertView.findViewById(R.id.plan_things_index);
        holder.thingsContent = (TextView) convertView.findViewById(R.id.plan_things_content);
        holder.thingsIndex .setText(childItem.get(groupPosition).get(childPosition).getIndex());
        holder.thingsContent .setText(childItem.get(groupPosition).get(childPosition).getContent());
        return convertView;
    }
    private class ChildViewHolder
    {
        /**
         *事件序号
         */
        public TextView thingsIndex;
        /**
         *订购用户名字
         */
        public TextView thingsContent;
    }
}
