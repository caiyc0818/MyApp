package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.GrouponJoinMemGridViewAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.QueueMeb;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrgGridViewAdapter extends BaseAdapter
{
	 // private static final String TAG = "ProductServiceGridAdapter";
    private Context mContext;
    
    private List<PushResource> orgList;
    
    public OrgGridViewAdapter(Context context, List<PushResource> orgList)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.orgList = orgList;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return orgList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return orgList.get(position);
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
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.org_gv_item, null);
            holder = new ViewHolder();
        
            holder.orgPhoto = (RoundImageView)convertView.findViewById(R.id.org_photo);
            holder.orgName = (TextView)convertView.findViewById(R.id.org_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        String name = orgList.get(position).getResTitle();
        holder.orgName.setText(name);
        if(position<5){
        if (!StringUtils.isEmpty(orgList.get(position).getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + orgList.get(position).getTitleImage(),
                holder.orgPhoto,
                AppConfig.options(R.drawable.user_defult_photo));
        }
        }else { holder.orgPhoto.setImageResource(R.drawable.more_org);}
        
        
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 用户头像
         */
        public RoundImageView orgPhoto;
        
        /**
         * 用户名称
         */
        public TextView orgName;
        
    }
}
