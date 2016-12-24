package com.bcinfo.tripaway.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ClubHomepageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GoldMedalFragment extends BaseFragment {

	private Context mContext;
	
	private UserInfo userInfo;
	
	private RoundImageView mGoldIv;
	
	private TextView mGoldName;
	
	private TextView mGoldIntro;
	
	private TextView mGoldCount;
	
	private TextView mGoldAdd;
	
	private String isFirst = "no";
	
	private boolean isFocused = false;
	
	private ImageView mGoldImg;
	
	public GoldMedalFragment(){
		
	}
	
	public GoldMedalFragment(Context mContext,UserInfo userInfo){
		this.mContext  = mContext;
		this.userInfo = userInfo;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	public void setFocusText(boolean isFocus){
		if (!isFocus) {
			isFocused = false;
			mGoldAdd.setText("+关注");
			mGoldAdd.setTextColor(getResources().getColor(R.color.title_bg));
		} else {
			isFocused = true;
			mGoldAdd.setText("已关注");
			mGoldAdd.setTextColor(getResources().getColor(R.color.gray_text));
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle =getArguments();
		if(bundle != null){
			isFirst = bundle.getString("isFirst","no");
		}

		View view =inflater.inflate(R.layout.pic_item, null);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentForUserInfo;
			OrgRole	role =userInfo.getOrgRole();
			if(role!=null){
        		if("leader".equals(role.getRoleCode())||"guide".equals(role.getRoleCode())){
        			intentForUserInfo = new Intent(getActivity(),
							ClubMebHomepageActivity.class);
					intentForUserInfo.putExtra("isDriverHomePage", false);
					 intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
	                    intentForUserInfo.putExtra("userInfo", userInfo);
					  startActivity(intentForUserInfo);
        		}else 
        			if("driver".equals(role.getRoleCode())){
        				intentForUserInfo = new Intent(getActivity(),
								ClubMebHomepageActivity.class);
						intentForUserInfo.putExtra("isDriverHomePage", true);
					      intentForUserInfo.putExtra("identifyId", userInfo.getUserId());
		                    intentForUserInfo.putExtra("userInfo", userInfo);
						  startActivity(intentForUserInfo);
        			}
			}
			}
		});
		mGoldImg = (ImageView) view.findViewById(R.id.gold_pic);
		mGoldName = (TextView) view.findViewById(R.id.goldmedal_name);
		mGoldIv = (RoundImageView) view.findViewById(R.id.goldmedal_photo);
		mGoldIntro = (TextView) view.findViewById(R.id.goldmedal_intro);
		mGoldCount = (TextView) view.findViewById(R.id.goldmedal_count);
		mGoldAdd = (TextView) view.findViewById(R.id.goldmedal_add);
		if("no".equals(userInfo.getIsFocus())){
			isFocused = false;
			mGoldAdd.setText("+关注");
			mGoldAdd.setTextColor(getResources().getColor(R.color.title_bg));
		}else{
			isFocused = true;
			mGoldAdd.setText("已关注");
			mGoldAdd.setTextColor(getResources().getColor(R.color.gray_text));
		}
		mGoldAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addOrCancelFocus(isFocused,userInfo.getUserId());
			}
		});
		if(StringUtils.isEmpty(userInfo.getRealName()))
		{
			if(!StringUtils.isEmpty(userInfo.getNickname()))
			{
				mGoldName.setText(userInfo.getNickname());
			}else{
				mGoldName.setText(userInfo.getUserName());
			}
		}else{
			mGoldName.setText(userInfo.getRealName());
		}
		
		mGoldIv.setTag(userInfo);
		mGoldIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfo info = (UserInfo) v.getTag();
//				ToastUtil.showToast(mContext, info.getOrgRole().getRoleType());
			}
		});
		mGoldIv.setImageResource(R.drawable.user_defult_photo);
		if(!StringUtils.isEmpty(userInfo.getAvatar())){
			ImageLoader.getInstance().displayImage(Urls.imgHost+userInfo.getAvatar(), mGoldIv,AppConfig.options(R.drawable.user_defult_photo));
		}
		if("yes".equals(userInfo.getIsGold())){
			mGoldImg.setVisibility(View.VISIBLE);
		}else{
			mGoldImg.setVisibility(View.GONE);
		}
		List<String> interList = new ArrayList<>();
		String a ="";
		if(userInfo.getTag() != null){
			interList = userInfo.getTag().getInterests();
			
			if(interList != null && interList.size()>0){
				for (int i = 0; i < 1; i++) {
					if( i == 0){
						 a += interList.get(i);
					}else{
						 a += "·" + interList.get(i);
					}
				}
			}
		}
		if(StringUtils.isEmpty(a)){
			mGoldIntro.setText(userInfo.getOrgRole().getRoleName());
		}else{
			mGoldIntro.setText(userInfo.getOrgRole().getRoleName()+"/"+a);
		}
		mGoldCount.setText("粉丝: "+userInfo.getFansCount());
		if("yes".equals(isFirst)){
			LinearLayout lay = (LinearLayout)view.findViewById(R.id.photo_bg);
			lay.setBackgroundColor(Color.parseColor("#1CCE6F"));
			mGoldName.setTextColor(Color.WHITE);
			mGoldIntro.setTextColor(Color.WHITE);
			mGoldCount.setTextColor(Color.WHITE);
		}
		return view;
	}
	
	private void addOrCancelFocus(boolean flag, String userId) {
		if (!AppInfo.getIsLogin()) {
			ToastUtil.showToast(mContext, "请登录");
			return;
		}
		if (!flag) {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", userId);
				StringEntity entity = new StringEntity(params.toString(),
						HTTP.UTF_8);
				HttpUtil.post(Urls.friend_list_url, entity,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								super.onSuccess(statusCode, headers, response);
								String code = response.optString("code");
								if ("00000".equals(code)) {
									mGoldAdd.setText("已关注");
									mGoldAdd.setTextColor(getResources().getColor(R.color.gray_text));
									isFocused = true;
								}
							}
						});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			HttpUtil.delete(Urls.friend_list_url + "/" + userId,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							String code = response.optString("code");
							if ("00000".equals(code)) {
								mGoldAdd.setText("+关注");
								mGoldAdd.setTextColor(getResources().getColor(R.color.title_bg));
								isFocused = false;
							}
						}
					});
		}
	}
}
