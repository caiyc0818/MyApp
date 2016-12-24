package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.OrgGridViewAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Club;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClubListAdapter extends BaseAdapter {

	private ArrayList<Club> clubList = new ArrayList<>();
	private Context mContext;

	public ClubListAdapter(Context mContext, ArrayList<Club> clubList) {
		this.mContext = mContext;
		this.clubList = clubList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clubList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return clubList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.club_list_adapter_item, null);
			holder = new ViewHolder();
			holder.orgPhoto = (RoundImageView) convertView.findViewById(R.id.org_photo);
			holder.orgName = (TextView) convertView.findViewById(R.id.org_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.orgName.setText(
				StringUtils.isEmpty(clubList.get(position).getOrgName()) ? "" : clubList.get(position).getOrgName());
		if (!StringUtils.isEmpty(clubList.get(position).getOrgLogo())) {
			if (holder.orgName.getTag() != null) {
				int p = (Integer) holder.orgName.getTag();
				if (p != position) {
					holder.orgPhoto.setImageResource(R.drawable.more_org);
				}
			} else {
				holder.orgPhoto.setImageResource(R.drawable.more_org);
			}
			ImageLoader.getInstance().displayImage(Urls.imgHost + clubList.get(position).getOrgLogo(), holder.orgPhoto,
					AppConfig.options(R.drawable.user_defult_photo));
		} else {
			holder.orgPhoto.setImageResource(R.drawable.more_org);
		}
		holder.orgName.setTag(position);
		return convertView;
	}

	class ViewHolder {
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
