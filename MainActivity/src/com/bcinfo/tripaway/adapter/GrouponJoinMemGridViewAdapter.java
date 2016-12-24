package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.CustomerHomePageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.PersonalInfoNewActivity;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 团购产品人员列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月5日 上午11:30:10
 */
public class GrouponJoinMemGridViewAdapter extends BaseAdapter
{
   
    // private static final String TAG = "ProductServiceGridAdapter";
    private Context mContext;
    
    private List<UserInfo> mItemList;
    
    public GrouponJoinMemGridViewAdapter(Context context, List<UserInfo> mMemberList)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = mMemberList;
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
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.groupon_join_gv_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.userPhoto = (ImageView)convertView.findViewById(R.id.join_user_photo);
        holder.userName = (TextView)convertView.findViewById(R.id.join_user_name);
        holder.userAge = (TextView)convertView.findViewById(R.id.join_user_age);
        holder.userSex = (ImageView)convertView.findViewById(R.id.join_user_age_photo);        
        String name = mItemList.get(position).getNickname();
        holder.userName.setText(name);
        if (!StringUtils.isEmpty(mItemList.get(position).getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + mItemList.get(position).getAvatar(),
                holder.userPhoto,
                AppConfig.options(R.drawable.user_defult_photo));
        }else{
        	holder.userPhoto.setImageResource(R.drawable.user_defult_photo);
        }
        if(!StringUtils.isEmpty(mItemList.get(position).getAge())){
        	holder.userAge.setVisibility(View.VISIBLE);
        	holder.userAge.setText( mItemList.get(position).getAge()+"岁");
        }else{
        	holder.userAge.setVisibility(View.GONE);
        }
        if("0".equals(mItemList.get(position).getGender())){
        	holder.userSex.setImageResource(R.drawable.girl_sex);
        }else if("1".equals(mItemList.get(position).getGender())){
        	holder.userSex.setImageResource(R.drawable.boy_sex);
        }else{
        	holder.userSex.setVisibility(View.GONE);
        }
        holder.userPhoto.setTag(mItemList.get(position));
        holder.userPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfo userInfo = (UserInfo) v.getTag();
				Intent intentForUserInfo;
				if("customer".equals(userInfo.getUserType())){
					intentForUserInfo = new Intent(mContext,CustomerHomePageActivity.class);
					intentForUserInfo.putExtra("userInfo",userInfo);
					mContext.startActivity(intentForUserInfo);
					return;
				}
				OrgRole orgRole = userInfo.getOrgRole();
				if (orgRole != null) {
					if ("admin".equals(orgRole.getRoleCode())) {
						intentForUserInfo = new Intent(mContext,
								ClubFirstPageActivity.class);
						intentForUserInfo.putExtra("userInfo", userInfo);
						mContext.startActivity(intentForUserInfo);
						return;
					}
					if ("leader".equals(orgRole.getRoleCode())
							|| "guide".equals(orgRole.getRoleCode())) {
						intentForUserInfo = new Intent(mContext,
								ClubMebHomepageActivity.class);
						intentForUserInfo.putExtra("isDriverHomePage", false);
						intentForUserInfo.putExtra("identifyId",
								userInfo.getUserId());
						intentForUserInfo.putExtra("userInfo", userInfo);
						mContext.startActivity(intentForUserInfo);
						return;

					} else if ("driver".equals(orgRole.getRoleCode())) {
						intentForUserInfo = new Intent(mContext,
								ClubMebHomepageActivity.class);
						intentForUserInfo.putExtra("isDriverHomePage", true);
						intentForUserInfo.putExtra("identifyId",
								userInfo.getUserId());
						intentForUserInfo.putExtra("userInfo", userInfo);
						mContext.startActivity(intentForUserInfo);
						return;
					}
				}

				String profession = userInfo.getPermission();
				intentForUserInfo = new Intent(mContext,
						PersonalInfoNewActivity.class);
				if (profession != null && profession.contains("专业司机")) {
					intentForUserInfo = new Intent(mContext,
							ClubMebHomepageActivity.class);
					intentForUserInfo.putExtra("isDriverHomePage", true);
					intentForUserInfo.putExtra("identifyId",
							userInfo.getUserId());
					intentForUserInfo.putExtra("userInfo", userInfo);
					mContext.startActivity(intentForUserInfo);
				} else if (profession != null
						&& (profession.contains("资深领队") || profession
								.contains("达人导游")))

				{
					intentForUserInfo = new Intent(mContext,
							ClubMebHomepageActivity.class);
					intentForUserInfo.putExtra("isDriverHomePage", false);
					intentForUserInfo.putExtra("identifyId",
							userInfo.getUserId());
					intentForUserInfo.putExtra("userInfo", userInfo);
					mContext.startActivity(intentForUserInfo);
				}
			}
		});
        return convertView;
    }
    
    public class ViewHolder
    {
        /**
         * 用户头像
         */
        public ImageView userPhoto;
        
        /**
         * 用户名称
         */
        public TextView userName;
        
        /**
         * 用户性别
         */
        public ImageView userSex;
    
        /**
         * 用户年龄
         */
        public TextView userAge;
    }
}
