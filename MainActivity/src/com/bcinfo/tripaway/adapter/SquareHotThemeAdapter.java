package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
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
import com.bcinfo.tripaway.bean.TopicOrTag;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.dialog.AffirmDialog;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 广场更多热门话题适配器
 */
public class SquareHotThemeAdapter extends BaseAdapter {
	private Context mContext;

	private ArrayList<TopicOrTag> themetags = new ArrayList<>();

	public SquareHotThemeAdapter(Context context, ArrayList<TopicOrTag> themetags) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.themetags = themetags;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return themetags.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return themetags.get(position);
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
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.square_hot_theme, null);
			holder.discuss_num = (TextView) convertView.findViewById(R.id.discuss_num);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.read_name = (TextView) convertView.findViewById(R.id.read_num);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TopicOrTag topicOrTag = themetags.get(position);
		if (!StringUtils.isEmpty(topicOrTag.getName())) {
			holder.title.setText(topicOrTag.getName());
		}
		if (!StringUtils.isEmpty(topicOrTag.getReads())) {
			holder.read_name.setText(topicOrTag.getReads()+"人阅读");
		}
		if (!StringUtils.isEmpty(topicOrTag.getDiscuss())) {
			holder.discuss_num.setText(topicOrTag.getDiscuss()+"人讨论");
		}
		if (!StringUtils.isEmpty(topicOrTag.getCover())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + topicOrTag.getCover(),
					holder.image, AppConfig.options(R.drawable.ic_launcher));
		}

		return convertView;
	}

	public class ViewHolder {
		public TextView title;
		public TextView read_name;
		public ImageView image;
		public TextView discuss_num;

	}
}
