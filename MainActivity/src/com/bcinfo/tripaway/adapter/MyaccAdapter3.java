package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.activity;
import com.bcinfo.tripaway.bean.articleExtInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MyaccAdapter3 extends BaseAdapter {
	public List<activity> list;

	public LayoutInflater layoutInflater;
	public Context contexts;

	public Context getContexts() {
		return contexts;
	}

	public void setContexts(Context contexts) {
		this.contexts = contexts;
	}

	public List<activity> getList() {
		return list;
	}

	public void setList(List<activity> list) {
		this.list = list;
	}

	public LayoutInflater getLayoutInflater() {
		return layoutInflater;
	}

	public void setLayoutInflater(LayoutInflater layoutInflater) {
		this.layoutInflater = layoutInflater;
	}

	public MyaccAdapter3(Context context, List<activity> list) {
		this.list = list;
		contexts = context;
		layoutInflater = LayoutInflater.from(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(contexts).inflate(R.layout.item_acc2, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.r1 = (TextView) convertView.findViewById(R.id.r1);
			holder.r2 = (TextView) convertView.findViewById(R.id.r2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		activity sy = list.get(position);

		if (sy.getArticleExt().size() > 0) {
			for (int i = 0; i < sy.getArticleExt().size(); i++) {
				holder.address.setText("地点：" + sy.getArticleExt().get(i).getInfo());
			}
		}
		if (sy.getStatus() != null) {
			String status = sy.getStatus();
			LogUtil.i("========", status, status);
			if ("invalid".equals(status)) {
				holder.r2.setVisibility(View.VISIBLE);
			}
			if ("valid".equals(status)) {
				holder.r1.setVisibility(View.VISIBLE);

			}
		}

		if (sy.getCover() != null) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + sy.getCover(), holder.image,
					AppConfig.options(R.drawable.ic_launcher));
		}

		if (sy.getTitle() != null) {
			holder.title.setText(sy.getTitle());
		}
		return convertView;
	}

	class ViewHolder {

		private TextView title;
		TextView address;
		ImageView image;
		TextView r1;
		TextView r2;

	}

}
