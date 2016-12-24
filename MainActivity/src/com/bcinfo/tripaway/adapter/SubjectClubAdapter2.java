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
 * 修改过的User adapter
 * 
 * @function
 */
public class SubjectClubAdapter2 extends BaseAdapter {
	private Context mcontext;

	private List<PushResource> pushResourceList;

	public SubjectClubAdapter2(Context mContext, List<PushResource> pushResourceList) {
		this.mcontext = mContext;
		this.pushResourceList = pushResourceList;
	}

	@Override
	public int getCount() {
		return pushResourceList.size();
	}

	@Override
	public Object getItem(int position) {
		return pushResourceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		LayoutInflater inflator;
		ViewHolder holder = null;
		PushResource pushResource = pushResourceList.get(position);
		if (convertView == null) {
			inflator = LayoutInflater.from(mcontext);
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.item_club_layout2, null);
			holder.orgLogo = (ImageView) convertView.findViewById(R.id.club_icon_iv);
			holder.orgName = (TextView) convertView.findViewById(R.id.club_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> objectParam = pushResource.getObjectParam();
		UserInfo userInfo = (UserInfo) pushResource.getObject();
		Integer i = (Integer) holder.orgLogo.getTag();
		if (i == null || i != position) {
			holder.orgLogo.setImageResource(R.drawable.user_defult_photo);
		}

		if (objectParam != null) {
			if (objectParam.get("nickname") != null) {
				holder.orgName.setText(objectParam.get("nickname"));
			} 
			if (objectParam.get("avartar") != null) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + objectParam.get("avartar"), holder.orgLogo,
						AppConfig.options(R.drawable.user_defult_photo));
			}
		}else{
			 if (userInfo != null) {
				holder.orgName.setText(userInfo.getNickname());
				ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getAvatar(), holder.orgLogo,
						AppConfig.options(R.drawable.user_defult_photo));
			}
			 
		}
		holder.orgLogo.setTag(position);

		return convertView;
	}

	private class ViewHolder

	{

		ImageView orgLogo;

		TextView orgName;

	}

}
