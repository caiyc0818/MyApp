package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.TouristHomepageActivity;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.ActivityUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FollowOrFansAdapter extends BaseAdapter {

	ArrayList<UserInfo> list = new ArrayList<>();
	private Context mContext;

	public FollowOrFansAdapter(Context mContext, ArrayList<UserInfo> list) {
		this.list = list;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		UserInfo userinfo = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.follow_fans_item, null);
			holder = new ViewHolder();
			holder.focusFansphoto = (RoundImageView) convertView
					.findViewById(R.id.focusFansphoto);
			holder.brief = (TextView) convertView.findViewById(R.id.brief);
			holder.focusrelative = (RelativeLayout) convertView
					.findViewById(R.id.focusrelative);
			holder.nofocusrelative = (RelativeLayout) convertView
					.findViewById(R.id.nofocusrelative);
			holder.mutualrelative = (RelativeLayout) convertView
					.findViewById(R.id.mutualrelative);
			holder.focusrelative1 = (RelativeLayout) convertView
					.findViewById(R.id.focusrelative1);
			holder.nofocusrelative1 = (RelativeLayout) convertView
					.findViewById(R.id.nofocusrelative1);
			holder.mutualrelative1 = (RelativeLayout) convertView
					.findViewById(R.id.mutualrelative1);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!StringUtils.isEmpty(userinfo.getAvatar())) {
			if (holder.focusFansphoto.getTag() != null) {
				int i = (Integer) holder.focusFansphoto.getTag();
				if (i != position) {
					holder.focusFansphoto
							.setImageResource(R.drawable.ic_launcher);
				}
			} else {
				holder.focusFansphoto.setImageResource(R.drawable.ic_launcher);
			}
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + userinfo.getAvatar(), holder.focusFansphoto,
					AppConfig.options(R.drawable.ic_launcher));
		} else {
			holder.focusFansphoto.setImageResource(R.drawable.ic_launcher);
		}
		holder.focusFansphoto.setTag(position);

		holder.name
				.setText(StringUtils.isEmpty(userinfo.getNickname()) ? (StringUtils
						.isEmpty(userinfo.getRealName()) ? (StringUtils
						.isEmpty(userinfo.getUserName()) ? "" : userinfo
						.getUserName()) : userinfo.getRealName()) : userinfo
						.getNickname());

		holder.brief
				.setText(StringUtils.isEmpty(userinfo.getBrief()) ? "个性签名个性签名"
						: userinfo.getBrief());

		holder.nofocusrelative.setVisibility(View.GONE);
		holder.focusrelative.setVisibility(View.GONE);
		holder.mutualrelative.setVisibility(View.GONE);
		String isFocus = userinfo.getIsFocus();
		if (!PreferenceUtil.getUserId().equals(userinfo.getUserId())) {
			if (isFocus.equals("no")) {
				holder.nofocusrelative.setVisibility(View.VISIBLE);
			} else if (isFocus.equals("yes")) {
				holder.focusrelative.setVisibility(View.VISIBLE);
			} else if (isFocus.equals("all")) {
				holder.mutualrelative.setVisibility(View.VISIBLE);
			}
		}
		final ViewHolder tempHolder = holder;
		final String userid = userinfo.getUserId();
		holder.focusrelative1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteFocus(userid, tempHolder);
			}
		});
		holder.nofocusrelative1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setFocus(userid, tempHolder);
			}
		});
		holder.mutualrelative1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteFocus(userid, tempHolder);
			}
		});
		final UserInfo tempuserInfo = userinfo;
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			ActivityUtil.squareToPersonHomePage(tempuserInfo, mContext);
			}
		});
		return convertView;
	}

	private void setFocus(final String userId, final ViewHolder holder) {
		try {
			JSONObject params = new JSONObject();
			params.put("userId", userId);
			StringEntity entity = new StringEntity(params.toString(),
					HTTP.UTF_8);
			HttpUtil.post(Urls.friend_list_url, entity,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							String code = response.optString("code");
							if ("00000".equals(code)) {
								holder.focusrelative
										.setVisibility(View.VISIBLE);
								holder.nofocusrelative.setVisibility(View.GONE);
								holder.mutualrelative.setVisibility(View.GONE);
								Intent intent = new Intent(
										"com.bcinfo.refreshFocus");
								intent.putExtra("userId", userId);
								intent.putExtra("isFocus", true);
								mContext.sendBroadcast(intent);
							}
						}
					});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void deleteFocus(final String userId, final ViewHolder holder) {
		try {
			HttpUtil.delete(Urls.friend_list_url + "/" + userId,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							String code = response.optString("code");
							if ("00000".equals(code)) {
								holder.focusrelative.setVisibility(View.GONE);
								holder.nofocusrelative
										.setVisibility(View.VISIBLE);
								holder.mutualrelative.setVisibility(View.GONE);
								Intent intent = new Intent(
										"com.bcinfo.refreshFocus");
								intent.putExtra("userId", userId);
								intent.putExtra("isFocus", false);
								mContext.sendBroadcast(intent);
							}
						}
					});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	class ViewHolder {
		RoundImageView focusFansphoto;
		RelativeLayout focusrelative;
		RelativeLayout nofocusrelative;
		RelativeLayout mutualrelative;
		RelativeLayout focusrelative1;
		RelativeLayout nofocusrelative1;
		RelativeLayout mutualrelative1;
		TextView brief;
		TextView name;
	}
}
