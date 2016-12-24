package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;
import com.bcinfo.tripaway.activity.ClubFirstPageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;

import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.PersonalInfoNewActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.ThemeProductListActivity;
import com.bcinfo.tripaway.adapter.ClubTravelsAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.PushResource;
import com.bcinfo.tripaway.bean.Tags;
import com.bcinfo.tripaway.bean.TopicInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.CacheUtils;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.Triangle;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 旅游产品精选adapter
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2014年12月18日 19:18:09
 * 
 */
public class commendProductAdapter extends BaseAdapter implements OnClickListener {
	private List<PushResource> pushResourceList;

	private Activity mActivity;

	private LayoutInflater inflator;
	CacheUtils utils;

	public commendProductAdapter(List<PushResource> pushResourceList, Activity mActivity) {
		this.pushResourceList = pushResourceList;
		this.mActivity = mActivity;
		// utils = CacheUtils.getInstance();

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
	public View getView(int position, View convertView, ViewGroup parent) {
		inflator = LayoutInflater.from(mActivity);

		ViewHolder holder;
		PushResource pushResource = pushResourceList.get(position);
		if (convertView == null) {
			convertView = (View) inflator.inflate(R.layout.subject_product_item, null);
			holder = new ViewHolder();

			holder.view = (LinearLayout) convertView.findViewById(R.id.view);

			holder.reasonLayout = (LinearLayout) convertView.findViewById(R.id.reason_layout);
			holder.reason = (TextView) convertView.findViewById(R.id.reason);
			holder.pickedProductPriceTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_price_tv);
			holder.pickedProductNameTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_name_tv);
			holder.pickedProductLableTv = (TextView) convertView.findViewById(R.id.trip_picked_product_lable);
			holder.pickedProductIv = (ImageView) convertView.findViewById(R.id.trip_picked_product_iv);
			holder.pickedProductUnit = (TextView) convertView.findViewById(R.id.trip_picked_product_info_unit_tv);
			holder.triangle = (Triangle) convertView.findViewById(R.id.triangle);
			holder.pickedTag1 = (TextView) convertView.findViewById(R.id.trip_picked_tag1_lable);
			holder.pickedTag2 = (TextView) convertView.findViewById(R.id.trip_picked_tag2_lable);
			holder.pickedTag3 = (TextView) convertView.findViewById(R.id.trip_picked_tag3_lable);
			holder.pickedTag4 = (TextView) convertView.findViewById(R.id.trip_picked_tag4_lable);
			holder.originalPrice = (TextView) convertView.findViewById(R.id.originalPrice);
			holder.layout_originalPrice = (RelativeLayout) convertView.findViewById(R.id.layout_originalPrice);
			// holder.pickedProductNameTv.setTypeface(TripawayApplication.tf);//
			// 设置字体
			// holder.pickedProductPriceTv.setTypeface(TripawayApplication.tf);//
			// 设置字体
			holder.pickedProductPeoTv = (TextView) convertView.findViewById(R.id.trip_picked_product_peo_join_tv);// 浏览人数

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!StringUtils.isEmpty(pushResource.getReason())) {
			holder.view.setVisibility(View.VISIBLE);
			holder.reasonLayout.setVisibility(View.VISIBLE);
			holder.reason.setText(pushResource.getReason());
			holder.triangle.setVisibility(View.VISIBLE);
		} else {
			holder.reasonLayout.setVisibility(View.GONE);
			holder.triangle.setVisibility(View.GONE);
		}
		ProductNewInfo productNewInfo = (ProductNewInfo) pushResource.getObject();
		if (productNewInfo != null) {
			float price = 0;
			if (!StringUtils.isEmpty(productNewInfo.getPrice())) {
				price = Float.parseFloat(productNewInfo.getPrice());
			}
			float price2 = 0;
			if (!StringUtils.isEmpty(productNewInfo.getOriginalPrice())) {
				price2 = Float.parseFloat(productNewInfo.getOriginalPrice());
			}
			if (!StringUtils.isEmpty(productNewInfo.getOriginalPrice())
					&& "0".equals(productNewInfo.getOriginalPrice()) == false && (price < price2)) {
				holder.layout_originalPrice.setVisibility(View.VISIBLE);
				holder.originalPrice.setText("¥" + productNewInfo.getOriginalPrice());
			} else {
				holder.layout_originalPrice.setVisibility(View.GONE);
			}

			HashMap<String, String> exts = productNewInfo.getExts();
			String tag;

			if (exts != null && exts.size() > 0) {
				tag = exts.get("refer_tags");
				if (!StringUtils.isEmpty(tag)) {
					holder.pickedProductLableTv.setText(tag.replaceAll(";", " · "));
				}
				String bigTag = exts.get("big_refer_tags");
				if (bigTag != null) {
					if (bigTag.contains("小团游")) {
						holder.pickedTag1.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag1.setVisibility(View.GONE);
					}
					if (bigTag.contains("深度游")) {
						holder.pickedTag2.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag2.setVisibility(View.GONE);
					}
					if (bigTag.contains("可定制")) {
						holder.pickedTag3.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag3.setVisibility(View.GONE);
					}
					if (bigTag.contains("跟团游")) {
						holder.pickedTag4.setVisibility(View.VISIBLE);
					} else {
						holder.pickedTag4.setVisibility(View.GONE);
					}
				} else {
					holder.pickedTag4.setVisibility(View.GONE);
					holder.pickedTag1.setVisibility(View.GONE);
					holder.pickedTag2.setVisibility(View.GONE);
					holder.pickedTag3.setVisibility(View.GONE);
				}
			} else {
				String topics = "";
				for (int i = 0; i < productNewInfo.getTopics().size(); i++) {
					if (i == productNewInfo.getTopics().size() - 1) {
						topics = topics + productNewInfo.getTopics().get(i);
					} else {
						topics = topics + productNewInfo.getTopics().get(i) + " · ";
					}
				}
				holder.pickedProductLableTv.setText(topics);
				holder.pickedTag1.setVisibility(View.GONE);
				holder.pickedTag2.setVisibility(View.GONE);
				holder.pickedTag4.setVisibility(View.GONE);
				holder.pickedTag3.setVisibility(View.GONE);
			}
			if (!StringUtils.isEmpty(productNewInfo.getPv())) {
				holder.pickedProductPeoTv.setText(productNewInfo.getPv());
			}
			if (productNewInfo.getPrice() != null) {
				holder.pickedProductPriceTv.setText("￥" + productNewInfo.getPrice());
				holder.pickedProductPriceTv.getPaint().setFakeBoldText(true);
			}
			if (productNewInfo.getTitle() != null) {
				holder.pickedProductNameTv.setText(productNewInfo.getTitle());
				holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
			}

			List<String> topicNameTvArray = productNewInfo.getTopics();
			if (topicNameTvArray != null && topicNameTvArray.size() != 0) {
				String lable = "";
				for (int i = 0; i < topicNameTvArray.size(); i++) {
					if (i != topicNameTvArray.size() - 1) {
						lable = lable + topicNameTvArray.get(i) + " · ";
					} else {
						lable = lable + topicNameTvArray.get(i);
					}
				}
				holder.pickedProductLableTv.setText(lable);
			}

			if (!StringUtils.isEmpty(productNewInfo.getTitleImg())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + productNewInfo.getTitleImg(),
						holder.pickedProductIv, AppConfig.options(R.drawable.ic_launcher));
			}
			if (productNewInfo.getProductType().equals("team")) {
				holder.pickedProductUnit.setVisibility(View.VISIBLE);

			} else {
				holder.pickedProductUnit.setVisibility(View.GONE);

			}
		}
		return convertView;
	}

	/**
	 * ViewHolder 内部类
	 * 
	 */
	class ViewHolder {
		private LinearLayout view;

		public Triangle triangle;

		public LinearLayout reasonLayout;

		public TextView reason;

		public TextView pickedProductUnit;

		private ImageView storeImg;
		private RelativeLayout pickedProductLayoutContainer;// 精选-普通产品 layout

		private ImageView pickedProductIv;// 精选-普通产品照片 imageview

		private TextView pickedProductPriceTv;// 精选-普通产品价格 textview

		private TextView pickedProductNameTv;// 精选-普通产品名字 textview

		private TextView pickedProductTimeSchedualTv;// 精选-普通产品天数 textview

		private TextView pickedProductDistanceTv;// 精选-普通产品里程距离 textview

		private TextView pickedProductLableTv;// 精选-普通产品 地点 国家 textview

		// add by lij 2015/09/25 start
		private ImageView pickedProductPeoIv;// 精选-普通产品参与人数图片 imageview

		private TextView pickedProductPeoTv;// 精选-普通产品参与人数数量 textview
		// add by lij 2015/09/25 end

		// private TextView pickedProductLabelTv1;// 精选-普通产品 标签1 textview
		//
		// private TextView pickedProductLabelTv2;// 精选-普通产品 标签2 textview
		//
		// private TextView pickedProductAreaTv;// 精选-普通产品 地点 地区 textview

		private FrameLayout pickedServicerLayoutContainer;// 精选-服务者 layout

		private ImageView pickedServicerIv;// 精选-服务者 照片

		private RoundImageView pickedServicerHeadIconIv;// 精选-服务者头像iv

		private TextView pickedServicerNameTv;// 精选-服务者姓名 tv

		private TextView pickedServicerBriefTv;// 精选-服务者简介tv

		private LinearLayout peoLayout;

		private LinearLayout pickedProductThemeLayoutContainer;// 精选-产品主题 layout

		private ImageView pickedProductThemeIv1;// 精选-产品主题1 iv

		private TextView pickedProductThemeNameTv1;// 精选-产品主题名字 textview

		// private ImageView pickedProductThemeIv2;// 精选-产品主题2 iv

		// private TextView pickedProductThemeNameTv2;// 精选-产品主题名字 textview

		private FrameLayout themeProductLayout1;

		// private FrameLayout themeProductLayout2;
		private TextView pickedTag1;

		private TextView pickedTag2;

		private TextView pickedTag3;
		private TextView pickedTag4;
		private TextView originalPrice;
		RelativeLayout layout_originalPrice;
	}

	@Override
	public void onClick(View view) {

	}

	public interface OperationListener {
		void addOrCancelStored(String productId, boolean flag, int position);
	}
}
