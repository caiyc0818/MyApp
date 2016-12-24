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

/**
 * @author hanweipeng
 * @date 2015-7-14 上午10:12:50
 */
public class AreaAdapter extends BaseAdapter
{
    private List<AreaInfo> areaList = new ArrayList<AreaInfo>();
    
    private Context mContext;
    
    private LayoutInflater inflater;
    
    public AreaAdapter(Context mContext, List<AreaInfo> areaList)
    {
        this.mContext = mContext;
        this.areaList = areaList;
        inflater = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return areaList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return areaList.get(position);
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
            convertView = inflater.inflate(R.layout.area_item, null);
            holder.areaTxt = (TextView)convertView.findViewById(R.id.area_txt);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.areaTxt.setText(areaList.get(position).getAreaName());
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView areaTxt;
    }
    
    /**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = areaList.get(i).getSortLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
}
