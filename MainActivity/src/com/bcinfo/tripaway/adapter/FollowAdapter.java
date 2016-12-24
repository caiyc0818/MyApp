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
 * 新个人资料-粉丝adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月30日 10:51:11
 */
public class FollowAdapter extends BaseAdapter
{
    private Context context;
    
    private List<UserInfo> fansList;
    
    public List<UserInfo> getFansList()
    {
        return fansList;
    }
    
    public void setFansList(List<UserInfo> fansList)
    {
        this.fansList = fansList;
    }
    
    public FollowAdapter(Context context, List<UserInfo> fansList)
    {
        this.context = context;
        this.fansList = fansList;
        
    }
    
    @Override
    public int getCount()
    {
        
        return fansList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        
        return fansList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        ViewHolder holder;
        LayoutInflater inflator;
        UserInfo fansUserInfo = fansList.get(position);
        if (convertView == null)
        {
            inflator = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.layout_item_fans_individual, null);
            holder.fansHeaderIv = (RoundImageView)convertView.findViewById(R.id.individual_fans_headIcon_iv);
            holder.fansHeaderNameTv = (TextView)convertView.findViewById(R.id.individual_fans_headName_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty(fansUserInfo.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + fansUserInfo.getAvatar(),
                holder.fansHeaderIv,
                AppConfig.options(R.drawable.ic_launcher));// 粉丝的头像
        }
        holder.fansHeaderNameTv.setText(fansUserInfo.getNickname()); // 粉丝的姓名
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private RoundImageView fansHeaderIv; // 粉丝-圆形头像
        
        private TextView fansHeaderNameTv;// 粉丝-姓名textview
        
    }
    
}
