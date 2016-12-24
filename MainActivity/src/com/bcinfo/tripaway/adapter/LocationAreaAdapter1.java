package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.LocationListActivity;
import com.bcinfo.tripaway.activity.LocationSelectAreaActivity;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;

/**
 * @author hanweipeng
 * @date 2015-7-14 上午10:12:50
 */
public class LocationAreaAdapter1 extends BaseAdapter
{
    private List<AreaInfo> areaList = new ArrayList<AreaInfo>();
    
    private Context mContext;
    
    private LayoutInflater inflater;
    
    public LocationAreaAdapter1(Context mContext, List<AreaInfo> areaList)
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
            holder.arrow = (ImageView)convertView.findViewById(R.id.arrow);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
//		if (newAreaList == null || newAreaList.size() == 0) {
//			holder.arrow .setVisibility(View.INVISIBLE);	
//		}
        holder.arrow .setVisibility(View.GONE);	
        holder.areaTxt.setText(areaList.get(position).getAreaName());
        holder.areaTxt.setTag(position);
        holder.areaTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Integer position=(Integer)v.getTag();
				Intent intent = new Intent();
				intent.putExtra("area", areaList.get(position).getAreaName());
				((LocationSelectAreaActivity)mContext).setResult(Activity.RESULT_OK, intent);
				((LocationSelectAreaActivity)mContext).finish();
				((LocationSelectAreaActivity)mContext).activityAnimationClose();
			}
		});
//        holder.arrow.setTag(position);
//        holder.arrow.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Integer position=(Integer)v.getTag();
//				Intent intent = new Intent(((LocationSelectAreaActivity)mContext),
//						LocationListActivity.class);
//				intent.putParcelableArrayListExtra("areaList", newAreaList);
//				((LocationSelectAreaActivity)mContext).startActivityForResult(intent, 101);
//				((LocationSelectAreaActivity)mContext).activityAnimationOpen();
//			}
//		});
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView areaTxt;
        private ImageView arrow;
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
