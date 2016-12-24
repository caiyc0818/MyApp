package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 服务者 产品adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月13日 11:42:11
 */
public class ClubInfoAdapter extends BaseAdapter
{
    private Context mcontext;
    
    private List<UserInfo> userList;
    
    public ClubInfoAdapter(Context mContext, List<UserInfo> userList)
    {
        this.mcontext = mContext;
        this.userList = userList;
    }
    
    @Override
    public int getCount()
    {
        return userList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return userList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup container)
    {
        LayoutInflater inflator;
        ViewHolder holder = null;
        if (convertView == null)
        {
            inflator = LayoutInflater.from(mcontext);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_clubmeb_info_layout, null); // time 几天 千米
            holder.clubMebPhoto = (ImageView)convertView.findViewById(R.id.club_meb_photo);
            holder.clubMebName = (TextView)convertView.findViewById(R.id.club_meb_name);
            holder.clubMebSex = (TextView)convertView.findViewById(R.id.club_meb_sex);
            holder.clubMebJob = (TextView)convertView.findViewById(R.id.club_meb_job);
            holder.contact = (TextView)convertView.findViewById(R.id.contact);
            holder.phone = (TextView)convertView.findViewById(R.id.phone);
            holder.line= (View)convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        UserInfo user = userList.get(position);
        
        if (!StringUtils.isEmpty(user.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + user.getAvatar(),
                holder.clubMebPhoto,
                AppConfig.options(R.drawable.ic_launcher));
        }
        if (!StringUtils.isEmpty(user.getRealName()))
        {
        	 holder.clubMebName.setText(user.getRealName());
        }else{
        	holder.clubMebName.setText(user.getNickname());
        }
		if (!StringUtils.isEmpty(user.getGender())) {
			if (user.getGender().equals("1"))
				holder.clubMebSex.setText("男");
			else if (user.getGender().equals("0"))
				holder.clubMebSex.setText("女");
		}
		OrgRole role=user.getOrgRole();
		if(role!=null&&role.getRoleName()!=null){
			holder.clubMebJob.setText("（"+role.getRoleName()+"）");
		}
		if (!StringUtils.isEmpty(user.getMobile())) {
				holder.phone.setText(user.getMobile());
				holder.contact.setVisibility(View.VISIBLE);
				holder.contact.setTag(user.getMobile());
				holder.contact.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(v.getTag()!=null){
						String tel=(String)v.getTag();
						Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tel));  
		                mcontext.startActivity(intent); 
								}
					}
				});
		}else{
			holder.phone.setText("");
//			holder.contact.setVisibility(View.INVISIBLE);
			holder.contact.setTag(user);
			holder.contact.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(v.getTag()!=null){
					UserInfo user=(UserInfo)v.getTag();
					ActivityUtil.joinPrivateChat(mcontext, user);
							}
				}
			});
		}
		if(position==userList.size()-1){
			holder.line.setVisibility(View.GONE);
		}else {
			holder.line.setVisibility(View.VISIBLE);
		}
        return convertView;
    }
    
    private class ViewHolder
    {
        ImageView clubMebPhoto;
        
        TextView clubMebName;
        
        TextView clubMebJob;
        
        TextView clubMebSex;
        
        TextView phone;
        
        TextView contact;
        
        View line;
    }
}
