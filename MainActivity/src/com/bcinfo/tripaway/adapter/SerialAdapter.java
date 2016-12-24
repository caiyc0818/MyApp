package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.bcinfo.photoselector.model.PhotoModel;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.bean.RichText;
import com.bcinfo.tripaway.bean.TripArticle;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

/**
 * 
 * @ClassName: AppointmentsAdapter
 * @Description: TODO(发布商品的图片)
 * @author scene
 * @date 2015-4-17 上午11:36:50
 * 
 */
public class SerialAdapter extends BaseAdapter {
	private Context mContext;
	private List<TripArticle> mLists;
	private DisplayMetrics dm;

	public SerialAdapter(Context mContext, List<TripArticle> mLists) {
		this.mLists = mLists;
		this.mContext = mContext;
		dm = new DisplayMetrics();
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	@Override
	public int getCount() {
		return mLists == null ? 0 : mLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mLists == null ? null : mLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup group) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(
					R.layout.square_serial_item, null);
			holder.map_location = (ImageView) view.findViewById(R.id.map_location);
			holder.map_location_text = (TextView) view.findViewById(R.id.map_location_text);
			holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
			holder.tvDescription = (TextView) view.findViewById(R.id.tvDescription);
			holder.imageall = (LinearLayout) view.findViewById(R.id.imageall);
			holder.tagsFlow = (FlowLayout) view.findViewById(R.id.tags_flow);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		TripArticle tripArticle = mLists.get(position);

		if (tripArticle != null) {
			
			if (!StringUtils.isEmpty(tripArticle.getLocation())
					&& !tripArticle.getLocation().equals("不显示")) {
				holder.map_location_text.setText(tripArticle.getLocation());
				holder.map_location.setVisibility(View.VISIBLE);
				holder.map_location_text.setVisibility(View.VISIBLE);
			} else {
				holder.map_location.setVisibility(View.GONE);
				holder.map_location_text.setVisibility(View.GONE);
			}
			if(tripArticle.getType()==0){
				holder.imageall.setVisibility(View.VISIBLE);
				holder.imageall.removeAllViews();
				addImage( tripArticle,holder.imageall);
			}else{
				holder.imageall.setVisibility(View.GONE);
			}
			if (StringUtils.isEmpty(tripArticle.getDescription())) {
				holder.tvDescription.setText("");
			} else {
				List<RichText> richTexts = StringUtils.xmlToRichText(tripArticle
						.getDescription());
				StringUtils.initRichTextView1(holder.tvDescription, richTexts);

			}
			if(tripArticle.getTagList().size()>0){
				holder.tagsFlow.setVisibility(View.VISIBLE);
				addTagFlow(tripArticle.getTagList(),holder.tagsFlow); 	
			}else {
				holder.tagsFlow.setVisibility(View.GONE);
			}
			String time = DateUtil.setTime(tripArticle.getPublishTime());
			holder.tvTime.setText(StringUtils.isEmpty(time) ? "" : time);
		}
		return view;
	}

	class Holder {
		ImageView map_location;
		TextView map_location_text;
		TextView tvTime;
		TextView tvDescription;
		LinearLayout imageall;
		FlowLayout tagsFlow;
	}
	
	private void addImage(TripArticle tripArticle,LinearLayout imageall) {
		int gap=2*DensityUtil.dip2px(mContext, 11);
		for (int i = 0; i < tripArticle.getPictureList().size(); i++) {
			ImageView imageView = new ImageView(mContext);
			LinearLayout.LayoutParams ls = new LinearLayout.LayoutParams(
					dm.widthPixels - gap, (dm.widthPixels - gap)
							* Integer.parseInt(tripArticle.getPictureList()
									.get(i).getHeight())
							/ Integer.parseInt(tripArticle.getPictureList()
									.get(i).getWidth()));

			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(ls);
			imageView.setImageResource(R.drawable.ic_launcher);
			ImageLoader.getInstance()
					.displayImage(
							Urls.imgHost
									+ tripArticle.getPictureList().get(i)
											.getUrl(), imageView,
							AppConfig.options(R.drawable.ic_launcher));
			imageall.addView(imageView);
			if (i != tripArticle.getPictureList().size() - 1) {
				View view = new View(mContext);
				LinearLayout.LayoutParams lsview = new LinearLayout.LayoutParams(
						dm.widthPixels - 14, 6);
				view.setLayoutParams(lsview);
				imageall.addView(view);
			}
		}
	}
	
	private void addTagFlow(List<String> tagList, FlowLayout flowLayout) {
		for(String tag:tagList){
		TextView newView = new TextView(mContext);
		newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		newView.setText(tag);
		newView.setTypeface(TripawayApplication.normalTf);
		newView.setGravity(Gravity.CENTER);
		newView.setTextColor(Color.parseColor("#666666"));
		newView.setBackgroundResource(R.drawable.shape_tag_bg);
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.rightMargin = DensityUtil.dip2px(mContext, 8);
		newView.setLayoutParams(params);
		flowLayout.addView(newView);
		}
	}


}
