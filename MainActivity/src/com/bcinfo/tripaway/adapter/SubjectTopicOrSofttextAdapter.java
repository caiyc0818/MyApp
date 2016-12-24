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
import com.bcinfo.tripaway.bean.ArticleInfo;
import com.bcinfo.tripaway.bean.Club;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.SubjectInfo;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wefika.flowlayout.FlowLayout;

/**
 * 服务者 产品adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月13日 11:42:11
 */
public class SubjectTopicOrSofttextAdapter extends BaseAdapter {
	private Context mcontext;

	private List<PushResource> pushResourceList;
	public SubjectTopicOrSofttextAdapter(Context mContext,
			List<PushResource> pushResourceList) {
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
			convertView = inflator.inflate(R.layout.item_subject_topic_layout,
					null);
			holder.topicLogo = (ImageView) convertView
					.findViewById(R.id.topic_icon_iv);
			holder.topicTitle = (TextView) convertView
					.findViewById(R.id.topic_title);
//			holder.reasonLayout = (LinearLayout) convertView
//					.findViewById(R.id.reason_layout);
//			holder.reason = (TextView) convertView.findViewById(R.id.reason);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		if (!StringUtils.isEmpty(pushResource.getReason())) {
//			holder.reasonLayout.setVisibility(View.VISIBLE);
//			holder.reason.setText(pushResource.getReason());
//		} else {
//			holder.reasonLayout.setVisibility(View.GONE);
//		}
		Object object = pushResource.getObject();
		LogUtil.i("专题的主题", object.toString(),object.toString());
		if (object != null) {
			if (object instanceof TopicInfo) {
				TopicInfo topicInfo = (TopicInfo) object;
				if (!StringUtils.isEmpty(topicInfo.getTitle())) {
					holder.topicTitle.setText(topicInfo.getTitle());
				}
				if (!StringUtils.isEmpty(topicInfo.getBackground())) {
					ImageLoader.getInstance().displayImage(
							Urls.imgHost + topicInfo.getBackground(),
							holder.topicLogo,
							AppConfig.options(R.drawable.ic_launcher));
				}

			} else if (object instanceof ArticleInfo) {
				ArticleInfo articleInfo = (ArticleInfo) object;
				if (!StringUtils.isEmpty(articleInfo.getTitle())) {
					holder.topicTitle.setText(articleInfo.getTitle());
				}
				if (!StringUtils.isEmpty(articleInfo.getCover())) {
					ImageLoader.getInstance().displayImage(
							Urls.imgHost + articleInfo.getCover(),
							holder.topicLogo,
							AppConfig.options(R.drawable.ic_launcher));
				}
			} else if (object instanceof SubjectInfo) {
				SubjectInfo subjectInfo = (SubjectInfo) object;
				if (!StringUtils.isEmpty(subjectInfo.getTitle())) {
					holder.topicTitle.setText(subjectInfo.getTitle());
				}
				if (!StringUtils.isEmpty(subjectInfo.getCover())) {
					 
					ImageLoader.getInstance().displayImage(
							Urls.imgHost + subjectInfo.getCover(),
							holder.topicLogo,
							AppConfig.options(R.drawable.ic_launcher));
				}
			}
		}
		return convertView;
	}

	private class ViewHolder

	{
//		public LinearLayout reasonLayout;

//		public TextView reason;

		ImageView topicLogo;

		TextView topicTitle;

	}

}
