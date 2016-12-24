package com.bcinfo.tripaway.adapter;


import java.util.List;

import android.app.Activity;
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
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ClubMebAdapter extends BaseAdapter
{

    private Context mContext;
    
    private List<UserInfo> mUserList;
    
    private LayoutInflater mInflater;
    
    private OperationListen listener;
    
    public ClubMebAdapter(){
       
    }
    
    public ClubMebAdapter(Context mContext,List<UserInfo> mUserList,OperationListen listener){
        this.mContext = mContext;
        this.mUserList = mUserList;
        this.listener = listener;
    }
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mUserList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mUserList.get(position);
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
        ViewHolder viewHolder = null;
        mInflater = LayoutInflater.from(mContext);
      final  UserInfo info = mUserList.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_clubmeb_layout, null);
            viewHolder.mMebPhoto = (RoundImageView) convertView.findViewById(R.id.club_photo);
            viewHolder.mMebName = (TextView) convertView.findViewById(R.id.club_meb_name);
            viewHolder.mMebPro = (TextView) convertView.findViewById(R.id.club_tag);
            viewHolder.goldImg = (ImageView) convertView.findViewById(R.id.is_gold_pic);
            viewHolder.focusImg = (ImageView) convertView.findViewById(R.id.is_focus_pho);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  =(ViewHolder)convertView.getTag();
        }
        
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentForUserInfo;
			OrgRole	role =info.getOrgRole();
			if(role!=null){
        		if("leader".equals(role.getRoleCode())||"guide".equals(role.getRoleCode())){
        			intentForUserInfo = new Intent(mContext,
							ClubMebHomepageActivity.class);
					intentForUserInfo.putExtra("isDriverHomePage", false);
					 intentForUserInfo.putExtra("identifyId", info.getUserId());
	                    intentForUserInfo.putExtra("userInfo", info);
					 ((Activity)mContext).startActivity(intentForUserInfo);
        		}else 
        			if("driver".equals(role.getRoleCode())){
        				intentForUserInfo = new Intent(mContext,
								ClubMebHomepageActivity.class);
						intentForUserInfo.putExtra("isDriverHomePage", true);
					      intentForUserInfo.putExtra("identifyId", info.getUserId());
		                    intentForUserInfo.putExtra("userInfo", info);
		                    ((Activity)mContext).startActivity(intentForUserInfo);
        			}
			}
			}
		});
        OrgRole orgRole = info.getOrgRole();
    	if ( orgRole != null && orgRole.getRoleName() != null) {
			if ("driver".equals(orgRole.getRoleCode())) {
				viewHolder.mMebPro.setText("司机");
			} else if ("leader".equals(orgRole.getRoleCode())) {
				viewHolder.mMebPro.setText("领队");
			}
			if ("guide".equals(orgRole.getRoleCode())) {
				viewHolder.mMebPro.setText("导游");
			}
		}
//        if(StringUtils.isEmpty(info.getJob())){
//            viewHolder.mMebPro.setVisibility(View.GONE);
//        }else{
//            viewHolder.mMebPro.setText(info.getJob());
//            viewHolder.mMebPro.setVisibility(View.VISIBLE);
//        }
        String a = "";
        if(null != info.getTag() ){
        	List<String> interest = info.getTag().getInterests();
        	if(null != interest &&interest.size()>0){
        		for(int i = 0;i<interest.size();i++){
        			if(i == 0){
        				a += interest.get(i);
        			}else{
        				a += " · "+ interest.get(i);
        			}
        		}
        	}
        }
        viewHolder.mMebPro.setTag(a);
        viewHolder.mMebPhoto.setImageResource(R.drawable.user_defult_photo);
        if(!StringUtils.isEmpty(info.getAvatar())){
            ImageLoader.getInstance().displayImage(Urls.imgHost+info.getAvatar(), viewHolder.mMebPhoto,AppConfig.options(R.drawable.user_defult_photo));
        }
        viewHolder.mMebName.setText(info.getNickname());
        if("yes".equals(info.getIsGold())){
        	viewHolder.goldImg.setVisibility(View.VISIBLE);
        }else{
        	viewHolder.goldImg.setVisibility(View.GONE);
        }
        
        if("yes".equals(info.getIsFocus())){
        	 viewHolder.focusImg.setImageResource(R.drawable.focus2);
        }else if("all".equals(info.getIsFocus())){
        	viewHolder.focusImg.setImageResource(R.drawable.focus1);
        }else{
        	viewHolder.focusImg.setImageResource(R.drawable.focus3);
        }
        viewHolder.focusImg.setTag(R.id.tag_first,info);
        viewHolder.focusImg.setTag(R.id.tag_second,position);
        viewHolder.focusImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfo tag = (UserInfo) v.getTag(R.id.tag_first);
				int position = (Integer)v.getTag(R.id.tag_second);
				if("no".equals(tag.getIsFocus())){
					listener.addFocusByUserId(tag.getUserId(),position,false);
				}else{
					listener.addFocusByUserId(tag.getUserId(),position,true);
				}
			}
		});
        return convertView;
    }
    
    private class ViewHolder{
        
        RoundImageView mMebPhoto;
        
        TextView mMebName;
        
        TextView mMebPro;
        
        ImageView  goldImg;
        
        ImageView focusImg;
    }

    public interface OperationListen {
    	void addFocusByUserId(String userId,int position,boolean flag);
    }
}
