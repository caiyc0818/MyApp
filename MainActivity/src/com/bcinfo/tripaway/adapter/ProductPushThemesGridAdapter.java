package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 产品主题 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月14日 20:23:22
 */
public class ProductPushThemesGridAdapter extends BaseAdapter {
	private List<PushResource> themeList;

	public Context context;
	private Activity mActivity;
	private float imageViewRatio; 
	private ImageShowListener imageShowListener;
	public ProductPushThemesGridAdapter(Context context,
			List<PushResource> themeList,ImageShowListener imageShowListener) {
		this.context = context;
		this.themeList = themeList;
		this.imageShowListener=imageShowListener;
		DisplayMetrics dm = new DisplayMetrics();
	  	  ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
	  	float picWidth=(dm.widthPixels-DensityUtil.dip2px(context, 4))/3f;
	  	imageViewRatio=DensityUtil.dip2px(context, 200)/picWidth;
	}
	
	@Override
	public int getCount() {
		if (themeList.size() > 9) {
			return 9;
		} else {
			return themeList.size();
		}
	}

	@Override
	public Object getItem(int position) {

		return themeList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator = LayoutInflater.from(context);
		if (position == 8&&themeList.size()>9) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflator.inflate(
						R.layout.item_discovery_theme_list, null);
				holder.themeIconIv = (ImageView) convertView
						.findViewById(R.id.item_discovery_theme_iv);
				holder.themeNameTv = (TextView) convertView
						.findViewById(R.id.item_discovery_theme_tv);
				holder.themeDescTv = (TextView) convertView
						.findViewById(R.id.item_discovery_theme_desctv);
				holder.subjectIcon = (ImageView) convertView
						.findViewById(R.id.subject_icon);
				/*
				 * AssetManager mgr = context.getAssets();// 得到AssetManager
				 * Typeface tf = Typeface.createFromAsset(mgr,
				 * "fonts/cuyuan.ttf");// 根据路径得到Typeface
				 * holder.themeNameTv.setTypeface(tf);// 设置字体
				 */
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.themeIconIv.setImageResource(R.drawable.clubmeb_bg);
			holder.themeNameTv.setText("更多主题");
			holder.themeDescTv.setText("More...");
			return convertView;
		}
		PushResource themeItem = themeList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.item_discovery_theme_list,
					null);
			holder.subjectIcon = (ImageView) convertView
					.findViewById(R.id.subject_icon);
			holder.themeIconIv = (ImageView) convertView
					.findViewById(R.id.item_discovery_theme_iv);
			holder.themeNameTv = (TextView) convertView
					.findViewById(R.id.item_discovery_theme_tv);
			holder.themeDescTv = (TextView) convertView
					.findViewById(R.id.item_discovery_theme_desctv);
			/*
			 * AssetManager mgr = context.getAssets();// 得到AssetManager Typeface
			 * tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");//
			 * 根据路径得到Typeface holder.themeNameTv.setTypeface(tf);// 设置字体
			 */
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.themeIconIv.setImageResource(R.drawable.default_photo);

		if (!StringUtils.isEmpty(themeItem.getTitleImage())) {
//			BitmapUtils bit = new BitmapUtils(context);
//			bit.configDefaultLoadingImage(R.drawable.default_photo);
//			bit.display(holder.themeIconIv, Urls.imgHost + themeItem.getTitleImage());
			ImageLoader.getInstance().displayImage(
					Urls.imgHost + themeItem.getTitleImage(),
					holder.themeIconIv,
					AppConfig.options(R.drawable.default_photo),new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							imageShowListener.changView();
							float picRatio=loadedImage.getHeight()/(float)loadedImage.getWidth();
							if(Math.abs(picRatio-imageViewRatio)<0.2){
								((ImageView)view).setScaleType(ScaleType.FIT_XY);
							}else {
								((ImageView)view).setScaleType(ScaleType.CENTER_CROP);
							}
//							((ImageView)view).setImageBitmap(loadedImage);
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
		}
		if (themeItem.getObjectType().equals("topic")) {
			holder.themeNameTv.setText("#" + themeItem.getResTitle());
			holder.subjectIcon.setVisibility(View.GONE);
			holder.themeNameTv.setGravity(Gravity.LEFT);
			holder.themeDescTv.setGravity(Gravity.LEFT);
		} else if (themeItem.getObjectType().equals("subject")) {
			holder.themeNameTv.setText(themeItem.getResTitle());
			holder.themeNameTv.setGravity(Gravity.CENTER);
			holder.themeDescTv.setGravity(Gravity.CENTER);
			holder.subjectIcon.setVisibility(View.VISIBLE);
		}
//		holder.themeNameTv.getPaint().setFakeBoldText(true);
		if(!StringUtils.isEmpty(themeItem.getSubTitle())&&!themeItem.getSubTitle().equals("null"))
		holder.themeDescTv.setText(themeItem.getSubTitle());

		return convertView;
	}

	class ViewHolder {
		ImageView themeIconIv;// 主题iv

		TextView themeNameTv;// 主题名称tv

		TextView themeDescTv;// 主题desctv

		ImageView subjectIcon;

	}

	public interface  ImageShowListener{
		public void changView();
	}
}
