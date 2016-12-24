package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.bean.Experience;
import com.bcinfo.tripaway.bean.ExperienceDetail;
import com.bcinfo.tripaway.bean.FamousComment;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.fragment.VehicleInfoFragment;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

public class ExpDetailAdapter extends BaseAdapter {
	private List<ExperienceDetail> expDetailList;

	private Context mContext;

	private LayoutInflater inflater;

	public ExpDetailAdapter(Context mContext,
			List<ExperienceDetail> expDetailList) {
		this.mContext = mContext;
		this.expDetailList = expDetailList;
		inflater = LayoutInflater.from(mContext);
	}

	public List<ExperienceDetail> getExpDetailList(){
		return this.expDetailList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return expDetailList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return expDetailList.get(position);
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_fellowship_layout,
					null);
			holder.travelTime = (TextView) convertView
					.findViewById(R.id.travelTime);
			holder.scale = (TextView) convertView.findViewById(R.id.scale);
			holder.commentLlayout = (LinearLayout) convertView
					.findViewById(R.id.comment_layout);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.picLayout = (LinearLayout) convertView
					.findViewById(R.id.pic_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.travelTime.setText(DateUtil.getFormateDate2(expDetailList.get(
				position).getTravelTime()));
		if(!StringUtils.isEmpty(expDetailList.get(position).getScale()))
		holder.scale.setText(expDetailList.get(position).getScale() + "人团");
		if( expDetailList.get(position)
				.getAppraise().size()==0){
			((View)holder.commentLlayout.getParent()).setVisibility(View.GONE);
		}else{
			((View)holder.commentLlayout.getParent()).setVisibility(View.VISIBLE);
			addComment(holder.commentLlayout, expDetailList.get(position)
					.getAppraise());	
		}
		final List<ImageInfo> pics = expDetailList.get(position).getImages();
		holder.picLayout.removeAllViews();
		if (pics.size() == 0) {
			holder.pic.setVisibility(View.GONE);
			holder.picLayout.setVisibility(View.GONE);
		} else if (pics.size() == 1) {
			holder.pic.setVisibility(View.VISIBLE);
			ImageInfo pic = pics.get(0);
			holder.picLayout.setVisibility(View.GONE);
			int width = DensityUtil.dip2px(mContext, 220);
			if (Integer.parseInt(pic.getWidth()) < width) {
				width = Integer.parseInt(pic.getWidth());
			}
			int height = width * Integer.parseInt(pic.getHeight())
					/ Integer.parseInt(pic.getWidth());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					width, height);
			params.topMargin = DensityUtil.dip2px(mContext, 5);
			holder.pic.setLayoutParams(params);
			if (!StringUtils.isEmpty(pic.getUrl())) {
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + pic.getUrl(), holder.pic,
						AppConfig.options(R.drawable.ic_launcher));
				holder.pic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						   Intent intentForView = new Intent( mContext, ImageViewerActivity.class);
				            intentForView.putExtra("image_index", 0);
				            intentForView.putExtra("STATE_POSITION", 0);
				            intentForView.putParcelableArrayListExtra("images", (ArrayList<ImageInfo>)pics);
				            mContext.startActivity(intentForView);
					}
				});
			}
		} else {
			holder.pic.setVisibility(View.GONE);
			holder.picLayout.setVisibility(View.VISIBLE);
			addPic(holder.picLayout,  pics);
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView travelTime;
		private TextView scale;
		private LinearLayout commentLlayout;
		private ImageView pic;
		private LinearLayout picLayout;
	}

	private void addComment(LinearLayout commentLlayout,
			List<HashMap<String, String>> strs) {
		commentLlayout.removeAllViews();
		for (HashMap<String, String> str : strs) {
			TextView newView = new TextView(mContext);
			newView.setText(str.get("name") + "：" + str.get("content"));
			newView.setSingleLine();
			newView.setEllipsize(TruncateAt.END);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			newView.setTextColor(Color.parseColor("#666666"));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT,
					FlowLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin = DensityUtil.dip2px(mContext, 5);
			newView.setLayoutParams(params);
			commentLlayout.addView(newView);
		}
	}

	private void addPic(LinearLayout picLayout, final List<ImageInfo> pics) {
		picLayout.removeAllViews();
		int i = 0;
		for (ImageInfo pic : pics) {
			if (i == 3) {
				TextView newView = new TextView(mContext);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						DensityUtil.dip2px(mContext, 55), DensityUtil.dip2px(
								mContext, 55));
				newView.setText("更多");
				newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				newView.setGravity(Gravity.CENTER);
				newView.setBackgroundColor(Color.parseColor("#F0F0F0"));
				newView.setLayoutParams(params);
				picLayout.addView(newView);
				newView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							   Intent intentForView = new Intent( mContext, ImageViewerActivity.class);
					            intentForView.putExtra("image_index", 0);
					            intentForView.putExtra("STATE_POSITION", 0);
					            intentForView.putParcelableArrayListExtra("images", (ArrayList<ImageInfo>)pics);
					            mContext.startActivity(intentForView);
						}
					});
				break;

			} else {
				ImageView newView = new ImageView(mContext);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						DensityUtil.dip2px(mContext, 55), DensityUtil.dip2px(
								mContext, 55));
				params.rightMargin = DensityUtil.dip2px(mContext, 5);
				newView.setLayoutParams(params);
				newView.setScaleType(ScaleType.FIT_XY);
				newView.setTag(i);
				newView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						   Intent intentForView = new Intent( mContext, ImageViewerActivity.class);
				            intentForView.putExtra("image_index", Integer.parseInt(v.getTag().toString()));
				            intentForView.putExtra("STATE_POSITION", Integer.parseInt(v.getTag().toString()));
				            intentForView.putParcelableArrayListExtra("images", (ArrayList<ImageInfo>)pics);
				            mContext.startActivity(intentForView);
					}
				});
				ImageLoader.getInstance().displayImage(
						Urls.imgHost + pic.getUrl(), newView,
						AppConfig.options(R.drawable.ic_launcher));
				picLayout.addView(newView);
			}

			++i;
		}

	}

}
