package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.bcinfo.tripaway.bean.QueueMeb;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GrouponMemberGridViewAdapter extends BaseAdapter {
	// private static final String TAG = "ProductServiceGridAdapter";
	private Context mContext;

	private ArrayList<QueueMeb> mItemList;

	public GrouponMemberGridViewAdapter(Context context,
			ArrayList<QueueMeb> list) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mItemList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.groupon_member_gv_item,
					null);
			holder = new ViewHolder();
			holder.userName = (TextView) convertView
					.findViewById(R.id.groupon_user_name);
			holder.userPhoto = (ImageView) convertView
					.findViewById(R.id.groupon_user_photo);
			holder.userType = (TextView) convertView
					.findViewById(R.id.group_cust_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StringBuffer  newName =  new StringBuffer();
		StringBuffer mailName =  new StringBuffer();
		String name = mItemList.get(position).getInfo().getNickname();
		if(name.length()==11){
			String newName1 = name.substring(0, 3);
			String newName2 = name.substring(7, 11);
			String name3 ="****";
			newName.append(newName1);
			newName.append(name3);
			newName.append(newName2);
			holder.userName.setText(newName);
		}else if(name.contains(".com")){
			String[] newname= name.split(".");
			String new1 = name.substring(0,1);
			String new2 = "****.com";
			mailName.append(new1);
			mailName.append(new2);
			holder.userName.setText(mailName);	
			 
		}
		else{
			holder.userName.setText(name);
		}
		LogUtil.i("我要的手机号码", newName.toString(), newName.toString());
		if (!StringUtils.isEmpty(mItemList.get(position).getInfo().getAvatar())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost
							+ mItemList.get(position).getInfo().getAvatar(),
					holder.userPhoto,
					AppConfig.options(R.drawable.user_defult_photo));
		}

		if ("serv".equals(mItemList.get(position).getMemberType())) {
			holder.userType.setVisibility(View.VISIBLE);
			OrgRole role = mItemList.get(position).getInfo().getOrgRole();
			if ("guide".equals(role.getRoleCode())) {
				holder.userType.setText(role.getRoleName());
				holder.userType.setTextColor(Color.WHITE);
				holder.userType.setBackgroundColor(Color.parseColor("#F4B92D"));
			} else {
				holder.userType.setText(role.getRoleName());
				holder.userType.setTextColor(Color.WHITE);
				holder.userType.setBackgroundColor(Color.parseColor("#75B5FF"));
			}

		} else if ("cust".equals(mItemList.get(position).getMemberType())) {
			holder.userType.setVisibility(View.GONE);
		}
		holder.userPhoto.setTag(mItemList.get(position).getInfo());
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

	public class ViewHolder {
		/**
		 * 用户头像
		 */
		public ImageView userPhoto;

		/**
		 * 用户名称
		 */
		public TextView userName;

		public TextView userType;
	}
}
