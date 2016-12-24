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
public class SubjectProductAdapter2 extends BaseAdapter implements OnClickListener {
	private List<PushResource> pushResourceList;

	private Activity mActivity;

	private LayoutInflater inflator;


	public SubjectProductAdapter2(List<PushResource> pushResourceList, Activity mActivity) {
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
	
			
			holder.pickedProductPriceTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_price_tv);
			holder.pickedProductNameTv = (TextView) convertView.findViewById(R.id.trip_picked_product_info_name_tv);
			holder.pickedProductLableTv = (TextView) convertView.findViewById(R.id.trip_picked_product_lable);
			holder.pickedProductIv = (ImageView) convertView.findViewById(R.id.trip_picked_product_iv);
			holder.pickedProductUnit = (TextView) convertView.findViewById(R.id.trip_picked_product_info_unit_tv);
		

//			AssetManager mgr = mActivity.getAssets();// 得到AssetManager
//			Typeface tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");// 根据路径得到Typeface
//			holder.pickedProductNameTv.setTypeface(tf);// 设置字体
//			holder.pickedProductPriceTv.setTypeface(tf);// 设置字体
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ProductNewInfo productNewInfo=(ProductNewInfo)pushResource.getObject();
		if(productNewInfo!=null){
			if (productNewInfo.getPrice() != null) {
				holder.pickedProductPriceTv
						.setText("￥" + productNewInfo.getPrice());
//				holder.pickedProductPriceTv.getPaint().setFakeBoldText(true);
			} 
			if (productNewInfo.getTitle() != null) {
				holder.pickedProductNameTv
						.setText( productNewInfo.getTitle());
//				holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
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
		
		}
		return convertView;
	}

	/**
	 * ViewHolder 内部类
	 * 
	 */
	class ViewHolder {
		

		
		public TextView pickedProductLableTv;

		public TextView pickedProductUnit;

		private LinearLayout pickedProductGroupLayout;

		private ImageView storeImg;
		private RelativeLayout pickedProductLayoutContainer;// 精选-普通产品 layout

		private ImageView pickedProductIv;// 精选-普通产品照片 imageview

		private TextView pickedProductPriceTv;// 精选-普通产品价格 textview

		private TextView pickedProductNameTv;// 精选-普通产品名字 textview

		// private FrameLayout themeProductLayout2;
	}

	@Override
	public void onClick(View view) {

	}

	public interface OperationListener {
		void addOrCancelStored(String productId, boolean flag, int position);
	}
}
