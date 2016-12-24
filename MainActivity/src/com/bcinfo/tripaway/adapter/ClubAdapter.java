package com.bcinfo.tripaway.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.OrgListActivity;
import com.bcinfo.tripaway.bean.Club;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 服务者 产品adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月13日 11:42:11
 */
public class ClubAdapter extends BaseAdapter
{
    private Context mcontext;
    
    private List<UserInfo> clubList;
    
    public ClubAdapter(Context mContext, List<UserInfo> clubList)
    {
        this.mcontext = mContext;
        this.clubList = clubList;
    }
    
    @Override
    public int getCount()
    {
        return clubList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return clubList.get(position);
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
            convertView = inflator.inflate(R.layout.item_club_layout, null); 
            holder.orgLogo = (ImageView)convertView.findViewById(R.id.club_icon_iv);
            holder.orgName = (TextView)convertView.findViewById(R.id.club_name);
            holder.drivers = (TextView)convertView.findViewById(R.id.driver_num);
            holder.guides = (TextView)convertView.findViewById(R.id.guide_num);
            holder.fans = (TextView)convertView.findViewById(R.id.fans_num);
            holder.products = (TextView)convertView.findViewById(R.id.product_num);
            holder.isFocus = (TextView)convertView.findViewById(R.id.focous);
            holder.introduce = (TextView)convertView.findViewById(R.id.desc);
            holder.line1 = (TextView)convertView.findViewById(R.id.line1);
            holder.line2 = (TextView)convertView.findViewById(R.id.line2);
            holder.line3 = (TextView)convertView.findViewById(R.id.line3);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        UserInfo club = clubList.get(position);
        if (!StringUtils.isEmpty(club.getRealName()))
        holder.orgName.setText(club.getRealName());
        else{
        	holder.orgName.setText(club.getNickname());
        }
        if (!StringUtils.isEmpty(club.getAvatar()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + club.getAvatar(),
                holder.orgLogo,
                AppConfig.options(R.drawable.user_defult_photo));
        }
        String isFocus=club.getIsFocus();
        if(isFocus!=null){
        	if(isFocus.equals("yes")){
        		holder.isFocus.setText("已关注");
        		holder.isFocus.setBackgroundResource(R.drawable.shape_focous_bg);
        		holder.isFocus.setTextColor(Color.parseColor("#b5b5b5"));
        	}else {
        		holder.isFocus.setText("+关注");
        		holder.isFocus.setBackgroundResource(R.drawable.shape_unfocous_bg);
        		holder.isFocus.setTextColor(Color.parseColor("#1cce6f"));
        	}
        	
        }else {
        	holder.isFocus.setText("+关注");
    		holder.isFocus.setBackgroundResource(R.drawable.shape_unfocous_bg);
    		holder.isFocus.setTextColor(Color.parseColor("#1cce6f"));
        }
        holder.isFocus.setTag(position);
        holder.isFocus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position=(Integer)v.getTag();
				if(clubList.get(position).getIsFocus().equals("yes")){
					addOrCancelFocus(true, clubList.get(position).getUserId(), (TextView)v, position);
				}else 
					if(clubList.get(position).getIsFocus().equals("no")){
						addOrCancelFocus(false, clubList.get(position).getUserId(), (TextView)v, position);
					}
			}
		});
        String drivers="0";
        String guides="0";
        String products=club.getProductCount();
        String fans=club.getFansCount();
        if((drivers==null||drivers.equals("0"))&&(guides==null||guides.equals("0"))&&(products==null||products.equals("0"))&&(fans==null||fans.equals("0"))){
        	((View)(holder.drivers.getParent().getParent())).setVisibility(View.GONE);
        }else {
        	((View)(holder.drivers.getParent().getParent())).setVisibility(View.VISIBLE);
        }
        if(drivers==null||drivers.equals("0")){
        	((View)(holder.drivers.getParent())).setVisibility(View.GONE);
        	holder.line1.setVisibility(View.GONE);
        }else {
        	((View)(holder.drivers.getParent())).setVisibility(View.VISIBLE);
        	holder.line1.setVisibility(View.VISIBLE);
        	holder.drivers.setText(drivers);
        }
        if(guides==null||guides.equals("0")){
        	((View)(holder.guides.getParent())).setVisibility(View.GONE);
        	holder.line2.setVisibility(View.GONE);
        }else {
        	((View)(holder.guides.getParent())).setVisibility(View.VISIBLE);
        	holder.line2.setVisibility(View.VISIBLE);
        	holder.guides.setText(guides);
        }
        
        if(products==null||products.equals("0")){
        	((View)(holder.products.getParent())).setVisibility(View.GONE);
        	holder.line3.setVisibility(View.GONE);
        }else {
        	((View)(holder.products.getParent())).setVisibility(View.VISIBLE);
        	holder.line3.setVisibility(View.VISIBLE);
        	holder.products.setText(products);
        }
        if(fans==null||fans.equals("0")){
        	((View)(holder.fans.getParent())).setVisibility(View.GONE);
        	holder.line3.setVisibility(View.GONE);
        }else {
        	((View)(holder.fans.getParent())).setVisibility(View.VISIBLE);
        	if(!(products==null||products.equals("0")))
        	holder.line3.setVisibility(View.VISIBLE);
        	holder.fans.setText(fans);
        }
    
        if (!StringUtils.isEmpty(club.getIntroduction()))
        {
           holder.introduce.setText("简介："+club.getIntroduction());
        }
        
        return convertView;
    }
    
    private void addOrCancelFocus(boolean flag, String userId,final  TextView isFocus,final int position) {
		if (!AppInfo.getIsLogin()) {
			ToastUtil.showToast(mcontext, "请登录");
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
									isFocus.setText("已关注");
									isFocus.setBackgroundResource(R.drawable.shape_focous_bg);
					        		isFocus.setTextColor(Color.parseColor("#b5b5b5"));
					        		clubList.get(position).setIsFocus("yes");
					        		ToastUtil.showToast(mcontext, "已关注");
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
								isFocus.setText("+关注");
								isFocus.setBackgroundResource(R.drawable.shape_unfocous_bg);
				        		isFocus.setTextColor(Color.parseColor("#1cce6f"));
				        		clubList.get(position).setIsFocus("no");
				        		ToastUtil.showToast(mcontext, "取消关注");
							}
						}
					});

		}
	}
    
    private class ViewHolder
    {
        ImageView orgLogo;
        
        TextView orgName;
        
        TextView products;
        
        TextView drivers;
        
         TextView fans;
        
         TextView isFocus;
        
        TextView introduce;
        
        TextView guides;
        
        TextView line1;
        
        TextView line2;
        
        TextView line3;
    }
}
