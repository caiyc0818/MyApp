package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.GoWithNew;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 结伴列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月28日 下午5:42:59
 */
public class GoWithListAdapter extends BaseAdapter
{
    private static final String TAG = "GoWithListAdapter";
    
    private Context mContext;
    
    private ArrayList<GoWithNew> mItemList;
    
    public GoWithListAdapter(Context context, ArrayList<GoWithNew> list)
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        GoWithNew goWith = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.go_with_list_item, null);
            holder = new ViewHolder();
            holder.authorPhoto = (ImageView)convertView.findViewById(R.id.author_photo);
            holder.authorName = (TextView)convertView.findViewById(R.id.author_name);
            holder.createDate = (TextView)convertView.findViewById(R.id.create_date);
            holder.invitePeopleNumber = (TextView)convertView.findViewById(R.id.invite_people_number);
            holder.responcePeopleNumber = (TextView)convertView.findViewById(R.id.responce_people_number);
            holder.goWithTitle = (TextView)convertView.findViewById(R.id.go_with_title);
            holder.goWithDes = (TextView)convertView.findViewById(R.id.go_with_description);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!StringUtils.isEmpty(goWith.getUser().getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + goWith.getUser().getAvatar(),
                holder.authorPhoto,
                AppConfig.options(R.drawable.user_defult_photo));
        }
        
        holder.authorName.setText(goWith.getUser().getNickname());
        holder.createDate.setText(goWith.getCreateTime());
        holder.invitePeopleNumber.setText(goWith.getJoinNum());
        holder.responcePeopleNumber.setText(goWith.getApplyNum());
        holder.goWithTitle.setText(goWith.getTitle());
        holder.goWithDes.setText(goWith.getDescription());
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 用户头像
         */
        public ImageView authorPhoto;
        
        /**
         * 用户名字
         */
        public TextView authorName;
        
        /**
         * 发布时间
         */
        public TextView createDate;
        
        /**
         * 邀请人数
         */
        public TextView invitePeopleNumber;
        
        /**
         * 响应人数
         */
        public TextView responcePeopleNumber;
        
        /**
         * 结伴地址
         */
        public TextView goWithTitle;
        
        /**
         * 结伴说明
         */
        public TextView goWithDes;
    }
}
