package com.bcinfo.tripaway.adapter;

import java.util.List;
import java.util.Map;

import com.bcinfo.tripaway.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 小贴士 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月23日 17:30:23
 */
public class TipItemListAdapter extends BaseAdapter
{
    private Context context;
    private List<Map<String, Object>> tipList;
    private LayoutInflater inflator;

    public List<Map<String, Object>> getTipList()
    {
        return tipList;
    }

    public void setTipList(List<Map<String, Object>> tipList)
    {
        this.tipList = tipList;
    }

    public TipItemListAdapter(Context context, List<Map<String, Object>> tipList)
    {
        super();
        this.context = context;
        this.tipList = tipList;
    }

    @Override
    public int getCount()
    {

        return tipList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return tipList.get(position);
    }

    @Override
    public long getItemId(int position)
    {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            inflator = LayoutInflater.from(context);
            convertView = inflator.inflate(R.layout.item_tip_service_layout, null);
            holder.tipElementTv = (TextView) convertView.findViewById(R.id.element_tip_tv);
            holder.tipContentTv = (TextView) convertView.findViewById(R.id.content_tip_tv);
            convertView.setTag(holder);
        }
        else
        {

            holder = (ViewHolder) convertView.getTag();
        }
        holder.tipElementTv.setText((String) ((Map<String, Object>) tipList.get(position)).get("tipElement"));
        holder.tipContentTv.setText((String) ((Map<String, Object>) tipList.get(position)).get("tipContent"));

        return convertView;
    }

    private class ViewHolder
    {
        private TextView tipElementTv;// 贴士元素
        private TextView tipContentTv;// 贴士内容
    }

}
