package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Cats;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CatsInfoAdapter extends BaseAdapter {

	private Context mContext;

	private OperationListener operationListener;

	private ArrayList<Cats> cat;

	private RoundImageView mCatsIv;

	private TextView mCatsName;

	public CatsInfoAdapter() {

	}

	public CatsInfoAdapter(Context mContext, ArrayList<Cats> cat,
			OperationListener operationListener) {
		this.mContext = mContext;
		this.cat = cat;
		this.operationListener = operationListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cat.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cat.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Cats catInfo = cat.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.club_cats_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title = (TextView) convertView.findViewById(R.id.cats_name);
		holder.imageView = (RoundImageView) convertView
				.findViewById(R.id.cats_photo);
		holder.title.setText(catInfo.getCustTitle());
		holder.imageView.setImageResource(R.drawable.ic_launcher);
		if (!StringUtils.isEmpty(catInfo.getCover())) {
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + catInfo.getCover(), holder.imageView,
					AppConfig.options(R.drawable.ic_launcher));
		}

		holder.imageView.setTag(catInfo.getCatId());

		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String id = (String) v.getTag();
				operationListener.queryProductById(id);
			}
		});
		return convertView;
	}

	class ViewHolder {

		private RoundImageView imageView;

		private TextView title;
	}

	public interface OperationListener {
		void queryProductById(String id);
	}
}
