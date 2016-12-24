package com.bcinfo.tripaway.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;

import com.bcinfo.tripaway.activity.GrouponProductNewDetailActivity;
import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.adapter.PickListAdapter.OperationListener;
import com.bcinfo.tripaway.adapter.PickListAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.fragment.SettingFragment1;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.ApplyExitDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.view.FriendSelView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCollectionAdapter extends BaseAdapter implements OnClickListener{
	private List<ProductNewInfo> infoList;
	
	private Activity mActivity;

	private ApplyExitDialog exitDialog;

	private LinearLayout exitConfirmLayout;
	
	private OperationListener listener;
	
	public MyCollectionAdapter(List<ProductNewInfo> info,final Activity mActivity ,final OperationListener listener){
		this.infoList = info;
        this.mActivity = mActivity;
        this.listener=listener;
        exitDialog =
                ApplyExitDialog.createDialog(mActivity, R.layout.setting_exit_dialog);
            
            LinearLayout exitCancelLayout =
                (LinearLayout)exitDialog.findViewById(R.id.setting_exitDialog_cancel_layout);
            exitCancelLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					exitDialog.dismiss();	
				}
			});
             exitConfirmLayout =
                (LinearLayout)exitDialog.findViewById(R.id.setting_exitDialog_confirm_layout);
             TextView tv =
                     (TextView)exitDialog.findViewById(R.id.tip);
             tv.setText("确认要取消收藏吗？");
            exitConfirmLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String productId = (String) v.getTag(R.id.tag_first);
					boolean falg = (Boolean) v.getTag(R.id.tag_second);
					int position = (Integer) v.getTag(R.id.tag_third);

					if (falg) {
						ToastUtil.showToast(mActivity, "收藏已取消");
					} else {
						ToastUtil.showToast(mActivity, "已收藏");
					}
					exitDialog.dismiss();
					listener.addOrCancelStored(productId, falg, position);
				}
			});
           
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ProductNewInfo info =infoList.get(position);
		ViewHolder holder = null;
		  if (convertView == null)
	        {
			  holder = new ViewHolder();
	            convertView = (View)LayoutInflater.from(mActivity).inflate(R.layout.trip_collected_item, null);
	            holder.pickedProductLayoutContainer =
	                    (RelativeLayout)convertView.findViewById(R.id.layout_trip_picked_product_container);
	                holder.pickedProductUnit = (TextView)convertView.findViewById(R.id.trip_picked_product_info_unit_tv);
	                holder.pickedProductGroupLayout =
	                    (LinearLayout)convertView.findViewById(R.id.trip_picked_product_group_layout);
	                
	                holder.pickedProductIv = (ImageView)convertView.findViewById(R.id.trip_picked_product_iv);
	                holder.pickedProductPriceTv = (TextView)convertView.findViewById(R.id.trip_picked_product_info_price_tv);
	                holder.pickedProductNameTv = (TextView)convertView.findViewById(R.id.trip_picked_product_info_name_tv);
//	                AssetManager mgr = mActivity.getAssets();// 得到AssetManager
//	                Typeface tf = Typeface.createFromAsset(mgr, "fonts/cuyuan.ttf");// 根据路径得到Typeface
//	                holder.pickedProductNameTv.setTypeface(tf);// 设置字体
//	                holder.pickedProductPriceTv.setTypeface(tf);// 设置字体
	            	holder.peoLayout = (LinearLayout) convertView.findViewById(R.id.trip_picked_product_peo_layout);
	                holder.pickedProductTimeSchedualTv =
	                    (TextView)convertView.findViewById(R.id.trip_picked_product_timeSchedual_tv);
	                holder.pickedProductDistanceTv = (TextView)convertView.findViewById(R.id.trip_picked_product_distance_tv);
	                holder.pickedProductLableTv = (TextView)convertView.findViewById(R.id.trip_picked_product_lable);
	                // add by lij 2015/09/25 start
	                holder.pickedProductPeoIv = (ImageView)convertView.findViewById(R.id.trip_picked_product_peo_iv); // 参与人数图片
	                holder.pickedProductPeoTv = (TextView)convertView.findViewById(R.id.trip_picked_product_peo_join_tv);// 浏览人数
	                // add by lij 2015/09/25 end
	                holder.pickedTag1 = (TextView) convertView.findViewById(R.id.trip_picked_tag1_lable);
	    			holder.pickedTag2 = (TextView) convertView.findViewById(R.id.trip_picked_tag2_lable);
	    			holder.pickedTag3 = (TextView) convertView.findViewById(R.id.trip_picked_tag3_lable);
	    			holder.pickedTag4 = (TextView) convertView.findViewById(R.id.trip_picked_tag4_lable);
	    			holder.originalPrice = (TextView) convertView.findViewById(R.id.originalPrice);
	    			holder.layout_originalPrice = (RelativeLayout) convertView.findViewById(R.id.layout_originalPrice);
	                convertView.setTag(holder);
	        } else
	        {
	            holder = (ViewHolder)convertView.getTag();
	        }
		  
		  if("member".equals(info.getPriceFrequency())){
			  holder.pickedProductUnit.setText("/人");
		  }else if("times".equals(info.getPriceFrequency())){
			  holder.pickedProductUnit.setText("/次");
		  }else if("day".equals(info.getPriceFrequency())){
			  holder.pickedProductUnit.setText("/天");
		  }else{
			  holder.pickedProductUnit.setText("");
		  }
		 
		  if(!StringUtils.isEmpty(info.getTitleImg())){
			  ImageLoader.getInstance().displayImage(Urls.imgHost+info.getTitleImg(), holder.pickedProductIv, AppConfig.options(R.drawable.ic_launcher));
		  }
		  if(StringUtils.isEmpty(info.getPrice())){
			  holder.pickedProductPriceTv.setText("￥888");
		  }else{
			  
			  holder.pickedProductPriceTv.setText("￥"+info.getPrice());
		  }
		  holder.pickedProductNameTv.getPaint().setFakeBoldText(true);
		  holder.pickedProductNameTv.setText(info.getTitle()==null?"":info.getTitle());
		  if(!StringUtils.isEmpty(info.getDays())){
			  holder.pickedProductTimeSchedualTv.setText(info.getDays()+"天");
		  }else{
			  holder.pickedProductTimeSchedualTv.setText("");
		  }
		  if(!StringUtils.isEmpty(info.getDistance())){
			  holder.pickedProductDistanceTv.setText(info.getDistance() +"km");
		  }else{
			  holder.pickedProductDistanceTv.setText("");
		  }
		  //参团隐藏
//		  if("team".equals(info.getProductType())){
//			  holder.pickedProductGroupLayout.setVisibility(View.VISIBLE);
//		  }else{
//			  holder.pickedProductGroupLayout.setVisibility(View.GONE);
//		  }
		  float price =0;
			if (!StringUtils.isEmpty(info.getPrice())) {
				price = Float.parseFloat(info.getPrice());
			}
			float price2 = 0;
			if (!StringUtils.isEmpty(info.getOriginalPrice())) {
				price2 = Float.parseFloat(info.getOriginalPrice());
			}
			if (!StringUtils.isEmpty(info.getOriginalPrice())&&"0".equals(info.getOriginalPrice())==false&&
					(price<price2)) {
				holder.layout_originalPrice.setVisibility(View.VISIBLE);
				holder.originalPrice.setText("¥" + info.getOriginalPrice());
			}else {
				holder.layout_originalPrice.setVisibility(View.GONE);
			}

			HashMap<String, String> exts = info.getExts();
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
					}else{
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
				for (int i = 0; i < info.getTopics().size(); i++) {
					if (i == info.getTopics().size() - 1) {
						topics = topics + info.getTopics().get(i);
					} else {
						topics = topics + info.getTopics().get(i) + " · ";
					}
				}
				holder.pickedProductLableTv.setText(topics);
				holder.pickedTag1.setVisibility(View.GONE);
				holder.pickedTag2.setVisibility(View.GONE);
				holder.pickedTag4.setVisibility(View.GONE);
				holder.pickedTag3.setVisibility(View.GONE);
			}

		  holder.pickedProductLayoutContainer.setTag(info.getId());
		  holder.pickedProductLayoutContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String id =(String) v.getTag();
				Intent intentForProductDetail = new Intent(mActivity, GrouponProductNewDetailActivity.class);
                intentForProductDetail.putExtra("productId",id);
                mActivity.startActivity(intentForProductDetail);
			}
		});
		  if(info.getIsFav().equals("no"))convertView.setVisibility(View.GONE);
		  else convertView.setVisibility(View.VISIBLE);
		  
		  
		  
//		  设置浏览人数
		  if (!StringUtils.isEmpty(info.getPv())) {
				holder.peoLayout.setVisibility(View.VISIBLE);
				holder.pickedProductPeoTv.setText(info.getPv());
			}else{
				holder.peoLayout.setVisibility(View.GONE);
			}
//		  if (!StringUtils.isEmpty(info.getMemberJoinCount())) {
//				holder.peoLayout.setVisibility(View.VISIBLE);
//				holder.pickedProductPeoTv.setText(info.getMemberJoinCount()+"人浏览");
//			}else{
//				holder.peoLayout.setVisibility(View.GONE);
//			}
		  if ("yes".equals(info.getIsFav())) {
			  holder.pickedProductLayoutContainer.setTag(R.id.tag_second, true);
			} else {
				holder.pickedProductLayoutContainer.setTag(R.id.tag_second, false);
			}
		  holder.pickedProductLayoutContainer.setTag(R.id.tag_first, info.getId());
		  holder.pickedProductLayoutContainer.setTag(R.id.tag_third, position);
		  holder.pickedProductLayoutContainer.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				 // 显示自定义Dialog
				String productId = (String) v.getTag(R.id.tag_first);
				boolean falg = (Boolean) v.getTag(R.id.tag_second);
				int position = (Integer) v.getTag(R.id.tag_third);
				exitConfirmLayout.setTag(R.id.tag_second, falg);
				exitConfirmLayout.setTag(R.id.tag_first, productId);
				exitConfirmLayout.setTag(R.id.tag_third, position);
	            exitDialog.show();
				return false;
			}
		});
		  return convertView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Map<String, Object> mapItem = (Map<String, Object>)v.getTag();
        Intent intentForProductDetail = null;
        ProductNewInfo productNewInfo = (ProductNewInfo)mapItem.get("objectValue");
        // 单产品
        if (productNewInfo.getProductType().equals("base")
            || productNewInfo.getProductType().equals("customization"))
        {
            intentForProductDetail = new Intent(mActivity, GrouponProductNewDetailActivity.class);
            intentForProductDetail.putExtra("productId", productNewInfo.getId());
            // intentForProductDetail.putExtra("product", productNewInfo);
        }
        else if (productNewInfo.getProductType().equals("single")
            && (((ProductNewInfo)mapItem.get("objectValue")).getServices().equals("traffic")))
        {
            intentForProductDetail = new Intent(mActivity, CarProductDetailActivity.class);
            intentForProductDetail.putExtra("productId", productNewInfo.getId());
            // intentForProductDetail.putExtra("product",
            // productNewInfo);
        }
        else if (productNewInfo.getProductType().equals("single")
            && (((ProductNewInfo)mapItem.get("objectValue")).getServices().equals("stay")))
        {
            intentForProductDetail = new Intent(mActivity, HouseProductDetailActivity.class);
            intentForProductDetail.putExtra("productId", productNewInfo.getId());
        }
        // 团产品
        else if (productNewInfo.getProductType().equals("team"))
        {
            
            intentForProductDetail = new Intent(mActivity, GrouponProductNewDetailActivity.class);
            intentForProductDetail.putExtra("productId", productNewInfo.getId());
            // intentForProductDetail.putExtra("product",
            // productNewInfo);
        }
        else
        {
            intentForProductDetail = new Intent(mActivity, CarProductDetailActivity.class);
            intentForProductDetail.putExtra("productId", productNewInfo.getId());
            // intentForProductDetail.putExtra("product", productNewInfo);
        }
        mActivity.startActivity(intentForProductDetail);
	}
	class ViewHolder{
public TextView pickedProductUnit;
        
        private LinearLayout pickedProductGroupLayout;
        
        private RelativeLayout pickedProductLayoutContainer;// 精选-普通产品 layout
        
        private ImageView pickedProductIv;// 精选-普通产品照片 imageview
        
        private TextView pickedProductPriceTv;// 精选-普通产品价格 textview
        
        private TextView pickedProductNameTv;// 精选-普通产品名字 textview
        
        private TextView pickedProductTimeSchedualTv;// 精选-普通产品天数 textview
        
        private TextView pickedProductDistanceTv;// 精选-普通产品里程距离 textview
        
        private TextView pickedProductLableTv;// 精选-普通产品 地点 国家 textview
        
        // add by lij 2015/09/25 start
        private ImageView pickedProductPeoIv;//精选-普通产品参与人数图片 imageview
        
        private TextView pickedProductPeoTv;//精选-普通产品浏览人数数量 textview
        // add by lij 2015/09/25 end
        

    	private TextView pickedTag1;

		private TextView pickedTag2;

		private TextView pickedTag3;
		private TextView pickedTag4;
		private TextView originalPrice;

		RelativeLayout layout_originalPrice;
		private LinearLayout peoLayout;
	}
	
	public interface OperationListener {
		void addOrCancelStored(String productId, boolean flag, int position);
	}
}
