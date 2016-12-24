package com.bcinfo.tripaway.adapter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.bcinfo.tripaway.activity.OrgListActivity;
import com.bcinfo.tripaway.bean.Club;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

/**
 * 服务者 产品adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月13日 11:42:11
 */
public class SubjectClubAdapter extends BaseAdapter
{
    private Context mcontext;
    
    private List<PushResource> pushResourceList;
    
    public SubjectClubAdapter(Context mContext, List<PushResource> pushResourceList)
    {
        this.mcontext = mContext;
        this.pushResourceList = pushResourceList;
    }
    
    @Override
    public int getCount()
    {
        return pushResourceList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return pushResourceList.get(position);
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
    	PushResource pushResource = pushResourceList.get(position);
        if (convertView == null)
        {
            inflator = LayoutInflater.from(mcontext);
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_subject_club_layout, null); 
            holder.orgLogo = (ImageView)convertView.findViewById(R.id.club_icon_iv);
            holder.orgName = (TextView)convertView.findViewById(R.id.club_name);
            holder.reasonLayout=(LinearLayout) convertView
					.findViewById(R.id.reason_layout);
			holder.reason=(TextView) convertView
					.findViewById(R.id.reason);
			holder.tags=(FlowLayout) convertView
					.findViewById(R.id.tags);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if(!StringUtils.isEmpty(pushResource.getReason())){
			holder.reasonLayout.setVisibility(View.VISIBLE);
			holder.reason.setText(pushResource.getReason());
		}else {
			holder.reasonLayout.setVisibility(View.GONE);
		}
        Map<String, String> objectParam = pushResource.getObjectParam();
		// if (userInfo!=null)
		// {
		//
		// }
		if (objectParam != null) {
			if (objectParam.get("nickname") != null) {
				holder.orgName.setText(objectParam.get("nickname"));
			}
			if (objectParam.get("tags") != null) {
				List <String> tagList=new ArrayList<String>();
				if(objectParam.get("tags").contains(","))
				for(String str:objectParam.get("tags").split(",")){
					tagList.add(str);
				}
				else 
					if(objectParam.get("tags").contains("，"))
						for(String str:objectParam.get("tags").split("，")){
							tagList.add(str);
						}
				addFlowView(tagList, holder.tags);
			}
//			if (objectParam.get("avartar") != null) {
//				ImageLoader.getInstance().displayImage(
//						Urls.imgHost + objectParam.get("avartar"),
//						holder.orgLogo, AppConfig.options(R.drawable.default_photo));
//			}
		}
        UserInfo userInfo=(UserInfo)pushResource.getObject();
        if(userInfo!=null){
        	if (userInfo.getAvatar() != null) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + userInfo.getAvatar(),
						holder.orgLogo, AppConfig.options(R.drawable.default_photo));
			}
        }
        
        return convertView;
    }
    
    
    private class ViewHolder
    
    {
public LinearLayout reasonLayout;
		
		public TextView reason;
		
        ImageView orgLogo;
        
        TextView orgName;
        
        FlowLayout tags;
        
    }
    
	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		flowLayout.removeAllViews();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(mcontext);
			newView.setBackgroundResource(R.drawable.shape_club_tag);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(mcontext, 4);
			params.bottomMargin = DensityUtil.dip2px(mcontext, 4);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}
}
