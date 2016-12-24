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
import com.bcinfo.tripaway.R.string;
import com.bcinfo.tripaway.activity.OrgListActivity;
import com.bcinfo.tripaway.bean.Club;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.CacheUtils;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

/**
 * 推荐 userclub adapter适配器
 * 
 */
public class CommendClubAdapter extends BaseAdapter {
	private List<PushResource> locationList;

	public Context context;
	CacheUtils utils;

	public CommendClubAdapter(Context context, List<PushResource> locationList) {
		this.context = context;
		this.locationList = locationList;
		utils = CacheUtils.getInstance();
		System.out.println("locationList==" + locationList);
	}

	@Override
	public int getCount() {
		return locationList.size();
	}

	@Override
	public Object getItem(int position) {
		return locationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator = LayoutInflater.from(context);
		PushResource pushResource = (PushResource) locationList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.item_recomend_serviser, null);
			holder = new ViewHolder();
			holder.commndIv = (ImageView) convertView.findViewById(R.id.commend_image);
			holder.NameTv = (TextView) convertView.findViewById(R.id.commend_club_name);
			holder.commend_reson = (TextView) convertView.findViewById(R.id.commend_reson);
			holder.resonll = (LinearLayout) convertView.findViewById(R.id.llreson);
			holder.tags = (FlowLayout) convertView.findViewById(R.id.tags);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> objectParam = pushResource.getObjectParam();
		UserInfo userInfo = (UserInfo) pushResource.getObject();
		if (!StringUtils.isEmpty(pushResource.getReason())) {
			holder.resonll.setVisibility(View.VISIBLE);
			holder.commend_reson.setText(pushResource.getReason());
		}
		if (objectParam != null) {
			if (objectParam.get("nickname") != null) {
				holder.NameTv.setText(objectParam.get("nickname"));
			}
			if (objectParam.get("avartar") != null) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + objectParam.get("avartar"), holder.commndIv,
						AppConfig.options(R.drawable.ic_launcher));
			}
		} else {
			if (userInfo != null) {
				holder.NameTv.setText(userInfo.getNickname());
				ImageLoader.getInstance().displayImage(Urls.imgHost + userInfo.getAvatar(), holder.commndIv,
						AppConfig.options(R.drawable.ic_launcher));
			}

		}

		Tags tagsArray = userInfo.getTag();
		List<String> list = new ArrayList<String>();
		if (userInfo.getTag() != null) {
			holder.ll.setVisibility(View.VISIBLE);
			if (tagsArray.getClubTypes() != null && tagsArray.getClubTypes().size() >= 2) {
				for (int i = 0; i < 2; i++) {
					if (list.contains(tagsArray.getClubTypes().get(i))) {
						continue;
					} else {
						list.add(tagsArray.getClubTypes().get(i));
					}
				}
			}

			if (tagsArray.getClubTypes() != null && tagsArray.getClubTypes().size() == 1) {
				list.add(tagsArray.getClubTypes().get(0));
			}

			if (tagsArray.getFootprints() != null && tagsArray.getFootprints().size() >= 2) {
				for (int i = 0; i < 2; i++) {
					if (list.contains(tagsArray.getFootprints().get(i))) {
						continue;
					} else {
						list.add(tagsArray.getFootprints().get(i));
					}
				}
			}
			if (tagsArray.getFootprints() != null && tagsArray.getFootprints().size() == 1) {
				list.add(tagsArray.getFootprints().get(0));
			}

			if (tagsArray.getServAreas() != null && tagsArray.getServAreas().size() >= 2) {
				for (int i = 0; i < 2; i++) {
					if (list.contains(tagsArray.getServAreas().get(i))) {
						continue;
					} else {
						list.add(tagsArray.getServAreas().get(i));
					}
				}
			}
			if (tagsArray.getServAreas() != null && tagsArray.getServAreas().size() == 1) {
				list.add(tagsArray.getServAreas().get(0));
			}
			LogUtil.i("list的数量", list.toString(), list.toString());
			addFlowView(list, holder.tags);
			if (list.size() == 0) {
				holder.ll.setVisibility(View.GONE);
			}

		} else if (objectParam.get("tags") != null) {
			List<String> tagList = new ArrayList<String>();
			for (String str : objectParam.get("tags").split("，")) {
				tagList.add(str);
			}
			addFlowView(tagList, holder.tags);
		}

		return convertView;
	}

	class ViewHolder {
		private ImageView commndIv;// 推荐图标
		private TextView NameTv;// 中文名tv
		FlowLayout tags;
		private TextView commend_reson;// 推荐原因
		LinearLayout resonll;
		LinearLayout ll;
	}

	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		flowLayout.removeAllViews();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(context);
			newView.setBackgroundResource(R.drawable.shape_club_tag);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextColor(Color.parseColor("#666666"));
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = DensityUtil.dip2px(context, 2);
			params.bottomMargin = DensityUtil.dip2px(context, 2);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}
}
