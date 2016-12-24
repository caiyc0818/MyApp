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
 * 新个人主页-关注 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月13日 14:38:11
 */
public class FocusPagerAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<UserInfo> focusList;
    
    public List<UserInfo> getFocusList()
    {
        return focusList;
    }
    
    public void setFocusList(List<UserInfo> focusList)
    {
        this.focusList = focusList;
    }
    
    public FocusPagerAdapter(Context context, List<UserInfo> focusList)
    {
        this.context = context;
        this.focusList = focusList;
        
    }
    
    @Override
    public int getCount()
    {
        
        return focusList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        
        return focusList.get(position);
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
        UserInfo userFocusInfo = focusList.get(position);
        if (convertView == null)
        {
            inflator = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.layout_item_focus_individual, null);
            holder.focusHeaderIv = (RoundImageView)convertView.findViewById(R.id.individual_focus_headIcon_iv);
            holder.focusHeaderNameTv = (TextView)convertView.findViewById(R.id.individual_focus_headName_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty(userFocusInfo.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + userFocusInfo.getAvatar(),
                holder.focusHeaderIv,
                AppConfig.options(R.drawable.ic_launcher));// 关注者的头像
        }
        
        holder.focusHeaderNameTv.setText(userFocusInfo.getNickname()); // 关注者的姓名
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private RoundImageView focusHeaderIv; // 关注-圆形头像
        
        private TextView focusHeaderNameTv;// 关注-姓名textview
        
    }
    
}
