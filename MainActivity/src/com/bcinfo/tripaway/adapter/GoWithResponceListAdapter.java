package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Intention;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 结伴详情回应列表适配
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class GoWithResponceListAdapter extends BaseAdapter
{
    private static final String TAG = "GoWithResponceListAdapter";
    
    private Context mContext;
    
    private ArrayList<Intention> mItemList;
    
    public GoWithResponceListAdapter(Context context, ArrayList<Intention> list)
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
        // TODO Auto-generated method stub
        Intention info = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.go_with_responce_list_item, null);
            holder = new ViewHolder();
            holder.userPhoto = (RoundImageView)convertView.findViewById(R.id.user_photo);
            holder.userName = (TextView)convertView.findViewById(R.id.user_name);
            holder.responceDate = (TextView)convertView.findViewById(R.id.responce_date);
            holder.responceContent = (TextView)convertView.findViewById(R.id.responce_content);
            holder.responce_state = (TextView)convertView.findViewById(R.id.responce_state);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty(info.getUser().getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + info.getUser().getAvatar(), holder.userPhoto);
        }
        
        holder.userName.setText(info.getUser().getNickname());
        holder.responceDate.setText(info.getCreateTime());
        holder.responceContent.setText(info.getContent());
        
        if (info.getStatus() != null && !info.getStatus().isEmpty())
        {
            holder.responce_state.setText(info.getStatus());
            // holder.responce_state.setVisibility(View.VISIBLE);
            if (info.getStatus().equals("agree"))
            {
                holder.responce_state.setTextColor(mContext.getResources().getColor(R.color.title_bg));
            }
            else
            {
                holder.responce_state.setTextColor(mContext.getResources().getColor(R.color.gray_more));
            }
            holder.responce_state.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                }
            });
        }
        else
        {
            holder.responce_state.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 用户头像
         */
        public RoundImageView userPhoto;
        
        /**
         * 用户名字
         */
        public TextView userName;
        
        /**
         * 回应时间
         */
        public TextView responceDate;
        
        /**
         * 回应内容
         */
        public TextView responceContent;
        
        /**
         * 回应状态
         */
        public TextView responce_state;
    }
}
