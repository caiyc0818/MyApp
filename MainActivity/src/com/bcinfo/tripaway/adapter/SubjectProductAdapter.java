package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
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
public class SubjectProductAdapter extends BaseAdapter implements OnClickListener {
	private List<PushResource> pushResourceList;

	private Activity mActivity;

	private LayoutInflater inflator;


	public SubjectProductAdapter(List<PushResource> pushResourceList, Activity mActivity) {
		this.pushResourceList = pushResourceList;
		this.mActivity = mActivity;
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
			convertView = (View) inflator.inflate(R.layout.subject_product_item2, null);
			holder = new ViewHolder();	
			holder.reasonLayout=(LinearLayout) convertView
					.findViewById(R.id.reason_layout);
			holder.reason=(TextView) convertView
					.findViewById(R.id.reason);
			holder.pickedProductPriceTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_price_tv);
			holder.pickedProductNameTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_name_tv);
			holder.pickedProductLableTv = (TextView) convertView.findViewById(R.id.trip_picked_product_lable);
			holder.pickedProductIv = (ImageView) convertView.findViewById(R.id.trip_picked_product_iv);
			holder.pickedProductUnit = (TextView) convertView.findViewById(R.id.trip_picked_product_info_unit_tv);
			holder.triangle= (Triangle) convertView.findViewById(R.id.triangle);
			holder.pickedProductGroupLayout = (LinearLayout) convertView
					.findViewById(R.id.trip_picked_product_group_layout);
//			AssetManager mgr = mActivity.getAssets();// 得到AssetManager
//			Typeface tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");// 根据路径得到Typeface
//			holder.pickedProductNameTv.setTypeface(TripawayApplication.tf);// 设置字体
//			holder.pickedProductPriceTv.setTypeface(TripawayApplication.tf);// 设置字体
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(!StringUtils.isEmpty(pushResource.getReason())){
			holder.reasonLayout.setVisibility(View.VISIBLE);
			holder.reason.setText(pushResource.getReason());
			holder.triangle.setVisibility(View.VISIBLE);
		}else {
			holder.reasonLayout.setVisibility(View.GONE);
			holder.triangle.setVisibility(View.GONE);
		}
		ProductNewInfo productNewInfo=(ProductNewInfo)pushResource.getObject();
		if(productNewInfo!=null){
			if (productNewInfo.getPrice() != null) {
				holder.pickedProductPriceTv
						.setText("￥" + productNewInfo.getPrice());
				holder.pickedProductPriceTv.getPaint().setFakeBoldText(true);
			} 
			if (productNewInfo.getTitle() != null) {
				holder.pickedProductNameTv
						.setText( productNewInfo.getTitle());
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
				 holder.pickedProductGroupLayout.setVisibility(View.VISIBLE);
			} else {
				holder.pickedProductUnit.setVisibility(View.GONE);
				holder.pickedProductGroupLayout.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	/**
	 * ViewHolder 内部类
	 * 
	 */
	class ViewHolder {
		
		public Triangle triangle;
		
		public LinearLayout reasonLayout;
		
		public TextView reason;
		
		public TextView pickedProductUnit;

		private LinearLayout pickedProductGroupLayout;

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
	}

	@Override
	public void onClick(View view) {

	}

	public interface OperationListener {
		void addOrCancelStored(String productId, boolean flag, int position);
	}
}
