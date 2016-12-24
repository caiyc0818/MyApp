package com.bcinfo.tripaway.adapter;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 服务者 产品adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月13日 11:42:11
 */
public class CatsProductAdapter extends BaseAdapter {
	private Context mcontext;

	private List<ProductNewInfo> productList;

	public CatsProductAdapter(Context mContext, List<ProductNewInfo> productList) {
		this.mcontext = mContext;
		this.productList = productList;
	}

	@Override
	public int getCount() {
		return productList.size();
	}

	@Override
	public Object getItem(int position) {
		return productList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		LayoutInflater inflator;
		ViewHolder holder = null;
		if (convertView == null) {
			inflator = LayoutInflater.from(mcontext);
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.item_cats_layout, null); // time
																				// 几天
																				// 千米
			holder.productIv = (ImageView) convertView.findViewById(R.id.product_servicer_icon_iv);
			holder.productTitleTv = (TextView) convertView.findViewById(R.id.product_servicer_introduct__title_tv);
			holder.productTopicTv = (TextView) convertView.findViewById(R.id.trip_picked_product_lable);
			holder.productPriceTv = (TextView) convertView.findViewById(R.id.simillar_product_price_tv);
			// holder.productDaySchedualTv =
			// (TextView)convertView.findViewById(R.id.product_servicer_timeSchedual_tv);
			// holder.productDistanceTv =
			// (TextView)convertView.findViewById(R.id.product_servicer_distance_tv);
			holder.productPriceType = (TextView) convertView.findViewById(R.id.simillar_product_price_type);
			holder.servTimes = (TextView) convertView.findViewById(R.id.simillar_product_serTimes);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.productIntroduceTv = (TextView) convertView.findViewById(R.id.product_servicer_introduce_tv);
			holder.pickedTag1 = (TextView) convertView.findViewById(R.id.trip_picked_tag1_lable);
			holder.pickedTag2 = (TextView) convertView.findViewById(R.id.trip_picked_tag2_lable);
			holder.pickedTag3 = (TextView) convertView.findViewById(R.id.trip_picked_tag3_lable);
			holder.pickedTag4 = (TextView) convertView.findViewById(R.id.trip_picked_tag4_lable);
			holder.originalPrice = (TextView) convertView.findViewById(R.id.originalPrice);
			holder.layout_originalPrice = (RelativeLayout) convertView.findViewById(R.id.layout_originalPrice);
			holder.productIntroduceTv.setEllipsize(TruncateAt.END);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ProductNewInfo productNewInfo = productList.get(position);

		holder.productIv.setImageResource(R.drawable.ic_launcher);
		if (!StringUtils.isEmpty(productNewInfo.getTitleImg())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + productNewInfo.getTitleImg(), holder.productIv,
					AppConfig.options(R.drawable.ic_launcher));
		}
		if (!StringUtils.isEmpty(productNewInfo.getPv())) {
			holder.num.setText(productNewInfo.getPv());
		}else{
			holder.num.setText("");
		}

		holder.productTitleTv.setText(productNewInfo.getTitle());
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
				holder.productTopicTv.setText(tag.replaceAll(";", " · "));
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
				holder.pickedTag1.setVisibility(View.GONE);
				holder.pickedTag2.setVisibility(View.GONE);
				holder.pickedTag3.setVisibility(View.GONE);
				holder.pickedTag4.setVisibility(View.GONE);
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
			holder.productTopicTv.setText(topics);
			holder.pickedTag1.setVisibility(View.GONE);
			holder.pickedTag2.setVisibility(View.GONE);
			holder.pickedTag3.setVisibility(View.GONE);
			holder.pickedTag4.setVisibility(View.GONE);
		}

		// holder.productDaySchedualTv.setText(productNewInfo.getDays() + "天");
		// holder.productDistanceTv.setText(productNewInfo.getDistance() +
		// "km");
		if (productNewInfo.getPrice() != null && !productNewInfo.getPrice().equals("")) {
			holder.productPriceTv.setText("¥" + productNewInfo.getPrice());
		} else {
			holder.productPriceTv.setText("¥8888");
		}

		if ("member".equals(productNewInfo.getPriceFrequency())) {
			holder.productPriceType.setText("/人");
		} else if ("day".equals(productNewInfo.getPriceFrequency())) {
			holder.productPriceType.setText("/天");
		} else if ("times".equals(productNewInfo.getPriceFrequency())) {
			holder.productPriceType.setText("/次");
		}
		if (StringUtils.isEmpty(productNewInfo.getServTime()) || "0".equals(productNewInfo.getServTime())) {
			holder.servTimes.setText("");
		} else {
			holder.servTimes.setText(productNewInfo.getServTime() + "已预订");
		}
		holder.productIntroduceTv.setText(productNewInfo.getDescription().replaceAll("\\s*", ""));
		holder.productIntroduceTv.setEllipsize(TruncateAt.END);
		return convertView;
	}

	private class ViewHolder {
		ImageView productIv;// 产品icon

		TextView productTitleTv;// 产品标题title

		TextView productTopicTv;// 产品主题

		TextView productPriceTv;// 产品价格
		TextView num;// 产品价格

		// TextView productDaySchedualTv;// 产品 天数

		// TextView productDistanceTv;// 产品 里程距离

		TextView productPriceType;

		TextView servTimes;

		TextView productIntroduceTv;// 产品 介绍 说明
		private TextView pickedTag1;

		private TextView pickedTag2;

		private TextView pickedTag3;

		private TextView pickedTag4;
		private TextView originalPrice;

		RelativeLayout layout_originalPrice;

	}
}
