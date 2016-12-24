package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 当地旅游达人 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月15日 16：01：12
 * 
 */
public class TravelAchiveAdapter extends BaseAdapter
{
    private Context mContext;
    
    private List<UserInfo> achiveList;
    
    public TravelAchiveAdapter(Context mContext, List<UserInfo> achiveList)
    {
        super();
        this.mContext = mContext;
        this.achiveList = achiveList;
    }
    
    @Override
    public int getCount()
    {
        return achiveList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return achiveList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflator = LayoutInflater.from(mContext);
        ViewHolder holder;
        UserInfo userInfo = achiveList.get(position);
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.layout_item_current_travel_achivement, null);
            holder.achiveIv =
                (RoundImageView)convertView.findViewById(R.id.location_current_travel_achivement_headIcon_iv);
            holder.achiveTv = (TextView)convertView.findViewById(R.id.location_current_travel_achivement_headName_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty((String)userInfo.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + (String)userInfo.getAvatar(),
                holder.achiveIv,
                AppConfig.options(R.drawable.ic_launcher));
        }
        
        holder.achiveTv.setText(userInfo.getNickname());
        return convertView;
    }
    
    private class ViewHolder
    {
        RoundImageView achiveIv;// 旅游达人iv
        
        TextView achiveTv;// 达人tv
    }
}
