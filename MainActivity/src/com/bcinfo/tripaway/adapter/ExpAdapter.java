package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.bean.Experience;
import com.bcinfo.tripaway.bean.ExperienceDetail;
import com.bcinfo.tripaway.bean.FamousComment;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.TextView1;

public class ExpAdapter extends BaseAdapter {
	private List<Experience> expList;

	private Context mContext;

	private LayoutInflater inflater;

	public ExpAdapter(Context mContext, List<Experience> expList) {
		this.mContext = mContext;
		this.expList = expList;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return expList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return expList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_journey_layout, null);
			holder.destName = (TextView) convertView.findViewById(R.id.journey_place_name);
			holder.expTimes = (TextView) convertView.findViewById(R.id.expTimes);
			holder.description = (TextView) convertView.findViewById(R.id.desc);
			holder.showAllContent = (TextView) convertView.findViewById(R.id.show_all_content);
			holder.showAllFellowship = (TextView) convertView.findViewById(R.id.show_all_fellowship_refer);
			holder.fellowship = (ListView) convertView.findViewById(R.id.fellowship_listview);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			holder.imageGone = (ImageView) convertView.findViewById(R.id.imageGone);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!StringUtils.isEmpty(expList.get(position).getDestName())) {
			holder.destName.setText(expList.get(position).getDestName());
		} else {
			holder.destName.setText("");
		}
		if (!StringUtils.isEmpty(expList.get(position).getDescription())) {
			holder.imageGone.setVisibility(View.GONE);
			((View) holder.description.getParent()).setVisibility(View.VISIBLE);
			holder.ll.setBackgroundResource(R.drawable.map_bg);
			holder.description.setText(expList.get(position).getDescription());
		} else {
			((View) holder.description.getParent()).setVisibility(View.GONE);
		}
		holder.expTimes.setText(expList.get(position).getExpTimes());

		if (expList.get(position).getExpDetail().size() == 0) {
			((View) holder.showAllFellowship.getParent()).setVisibility(View.GONE);
			ExpDetailAdapter expDetailAdapter = (ExpDetailAdapter) holder.fellowship.getAdapter();
			List<ExperienceDetail> expDetailList;
			if (expDetailAdapter != null) {
				expDetailList = expDetailAdapter.getExpDetailList();
				if (expDetailList.size() > 0) {
					expDetailList.clear();
					expDetailAdapter.notifyDataSetChanged();
				}
			}
		} else if (expList.get(position).showExpFlag == false) {

			if (expList.get(position).getExpDetail().size() <= 2) {
				((View) holder.showAllFellowship.getParent()).setVisibility(View.GONE);
				ExpDetailAdapter expDetailAdapter = (ExpDetailAdapter) holder.fellowship.getAdapter();
				List<ExperienceDetail> expDetailList;
				if (expDetailAdapter == null) {
					expDetailList = new ArrayList<ExperienceDetail>();
					expDetailList.addAll(expList.get(position).getExpDetail());
					expDetailAdapter = new ExpDetailAdapter(mContext, expDetailList);
					holder.fellowship.setAdapter(expDetailAdapter);
				} else {
					expDetailList = expDetailAdapter.getExpDetailList();
					expDetailList.clear();
					expDetailList.addAll(expList.get(position).getExpDetail());
					expDetailAdapter.notifyDataSetChanged();
				}
			} else {
				holder.showAllFellowship.setText("查看全部");
				List<ExperienceDetail> expDetailList;
				// 可以重用对象，不然界面图片会有卡顿现象
				ExpDetailAdapter expDetailAdapter = (ExpDetailAdapter) holder.fellowship.getAdapter();
				if (expDetailAdapter == null) {
					expDetailList = new ArrayList<ExperienceDetail>();
					int i = 0;
					for (ExperienceDetail expDetail : expList.get(position).getExpDetail()) {
						if (i == 2)
							break;
						expDetailList.add(expDetail);
						++i;
					}
					expDetailAdapter = new ExpDetailAdapter(mContext, expDetailList);
					holder.fellowship.setAdapter(expDetailAdapter);
				} else {
					expDetailList = expDetailAdapter.getExpDetailList();
					expDetailList.clear();
					int i = 0;
					for (ExperienceDetail expDetail : expList.get(position).getExpDetail()) {
						if (i == 2)
							break;
						expDetailList.add(expDetail);
						++i;
					}
					expDetailAdapter.notifyDataSetChanged();
				}
				((View) holder.showAllFellowship.getParent()).setVisibility(View.VISIBLE);
			}

		} else {
			holder.showAllFellowship.setText("收起");
			((View) holder.showAllFellowship.getParent()).setVisibility(View.VISIBLE);
			ExpDetailAdapter expDetailAdapter = (ExpDetailAdapter) holder.fellowship.getAdapter();
			expDetailAdapter.getExpDetailList().clear();
			for (ExperienceDetail expDetail : expList.get(position).getExpDetail()) {
				expDetailAdapter.getExpDetailList().add(expDetail);
			}
			expDetailAdapter.notifyDataSetChanged();
		}
		holder.showAllFellowship.setTag(position);
		holder.showAllFellowship.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position = (Integer) v.getTag();
				expList.get(position).showExpFlag = !expList.get(position).showExpFlag;
				ExpAdapter.this.notifyDataSetChanged();
			}
		});

		if (expList.get(position).showdesFlag) {
			holder.description.setEllipsize(null); // 展开
			holder.description.setMaxLines(100);
			holder.showAllContent.setText("收起");

			// tv.setSingleLine(flag);
		} else {
			holder.description.setMaxLines(2);
			holder.description.setEllipsize(TruncateAt.END); // 收缩
			holder.showAllContent.setText("显示全部");
			// tv.setSingleLine(flag);
		}

		final TextView description = holder.description;
		final TextView showAllContent = holder.showAllContent;
		ViewTreeObserver viewTreeObserver = description.getViewTreeObserver();
		description.setTag(position);
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				int position = (Integer) description.getTag();
				// int maxLines = description.getLineCount();
				//// if ((Boolean) description.getTag()) {
				//// return true;
				//// }
				// if (description.getEllipsize() != TruncateAt.END) {
				// if (maxLines > 2) {
				// showAllContent.setVisibility(View.VISIBLE);
				// } else {
				// showAllContent.setVisibility(View.GONE);
				// }
				// }
				if (!expList.get(position).showdesFlag) {
					if (!((TextView1) description).isOverFlowed()) {
						showAllContent.setVisibility(View.GONE);
					}
				}
				return true;
			}
		});
		showAllContent.setTag(position);
		showAllContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				expList.get(position).showdesFlag = !expList.get(position).showdesFlag;
				if (expList.get(position).showdesFlag) {
					description.setEllipsize(null); // 展开
					description.setMaxLines(100);
					((TextView) v).setText("收起");

					// tv.setSingleLine(flag);
				} else {
					description.setMaxLines(2);
					description.setEllipsize(TruncateAt.END); // 收缩
					((TextView) v).setText("显示全部");
					// tv.setSingleLine(flag);
				}
			}
		});

		return convertView;
	}

	private class ViewHolder {
		private TextView destName;
		private TextView expTimes;
		private TextView description;
		private TextView showAllFellowship;
		private TextView showAllContent;
		private ListView fellowship;
		private LinearLayout ll;

		ImageView imageGone;
	}

}
