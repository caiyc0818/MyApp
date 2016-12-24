package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.MyOrder;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.dialog.AffirmDialog;
import com.bcinfo.tripaway.view.dialog.AppariseDialog;
import com.bcinfo.tripaway.view.dialog.AppariseDialog.OnAppariseListener;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的订单列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月9日 下午3:55:10
 */
public class MyOrdersListAdapter extends BaseAdapter {
	private static final String TAG = "MyOrdersListAdapter";

	private Context mContext;

	private ArrayList<MyOrder> mItemList;

	private OnOrderListener onOrderListener;

	public MyOrdersListAdapter(Context context, ArrayList<MyOrder> list, OnOrderListener onOrderListener) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mItemList = list;
		this.onOrderListener = onOrderListener;
	}

	public MyOrdersListAdapter(Context context, ArrayList<MyOrder> list) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mItemList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyOrder myOrder = mItemList.get(position);
		ViewHolder holder = null;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.my_journey_list_item, null);
			holder = new ViewHolder();
			holder.product_photo = (ImageView) convertView.findViewById(R.id.product_photo);
			holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
			holder.product_introduce = (TextView) convertView.findViewById(R.id.product_introduce);
			holder.trip_time = (TextView) convertView.findViewById(R.id.trip_time);
			holder.authorPhoto = (RoundImageView) convertView.findViewById(R.id.product_author);
			holder.produce_price = (TextView) convertView.findViewById(R.id.produce_price);
			holder.trip_state = (TextView) convertView.findViewById(R.id.trip_state);
			// holder.contact_author =
			// (ImageView)convertView.findViewById(R.id.contact_author);
			holder.levelTxt = (TextView) convertView.findViewById(R.id.level);
			holder.productAuthorName = (TextView) convertView.findViewById(R.id.product_author_name);
			holder.commentBtn = (Button) convertView.findViewById(R.id.comment_myjour_order);
			holder.delOrderBtn = (Button) convertView.findViewById(R.id.del_myjour_order);
			holder.productCode = (TextView) convertView.findViewById(R.id.product_code);
			holder.orderTime = (TextView) convertView.findViewById(R.id.order_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ProductNewInfo product = myOrder.getProduct();
		if (!StringUtils.isEmpty(myOrder.getCreateTime())) {
			holder.orderTime.setText(DateUtil.getFormateDate(myOrder.getCreateTime()));
		}
		if (!StringUtils.isEmpty(myOrder.getNo())) {
			holder.productCode.setText("订单编号：" + myOrder.getNo());
		}
		if (product != null) {

			holder.product_name.setText(product.getTitle());
			holder.product_introduce.setText(product.getDescription());
			if (!StringUtils.isEmpty(myOrder.getProduct().getTitleImg())) {
				ImageLoader.getInstance().displayImage(Urls.imgHost + myOrder.getProduct().getTitleImg(),
						holder.product_photo);
			}

			UserInfo info = myOrder.getProduct().getUser();

			if (info != null && !StringUtils.isEmpty(info.getAvatar())) {
				// 设置用户名
				holder.productAuthorName.setText(info.getNickname() == null ? "" : info.getNickname());
				ImageLoader.getInstance().displayImage(Urls.imgHost + info.getAvatar(), holder.authorPhoto,
						AppConfig.options(R.drawable.user_defult_photo));
			}
		}
		holder.trip_time.setText(DateUtil.formateDateToTime(myOrder.getBeginDate()) + "-"
				+ DateUtil.formateDateToTime(myOrder.getEndDate()));
		holder.produce_price.setText("￥" + myOrder.getPrice() + "x" + myOrder.getAmount());
		if (myOrder.getRefundPolicy().equals("super")) {
			holder.levelTxt.setText("非常严格");
		} else if (myOrder.getRefundPolicy().equals("high")) {
			holder.levelTxt.setText("严格");
		} else if (myOrder.getRefundPolicy().equals("middle")) {
			holder.levelTxt.setText("适中");
		} else if (myOrder.getRefundPolicy().equals("low")) {
			holder.levelTxt.setText("灵活");
		}
		String orderState = myOrder.getStatus();
		// holder.contact_author.setVisibility(View.GONE);
		if (orderState != null) {
			if (orderState.equals("apply_success")) {
				if (!StringUtils.isEmpty(myOrder.getIs_instal()) && "yes".equals(myOrder.getIs_instal())) {
					holder.trip_state.setText("预订单生成/待分期付款");
				} else {
					if (!StringUtils.isEmpty(myOrder.getFinalPayment())
							&& myOrder.getFinalPayment().compareTo("0") > 0) {

						holder.trip_state.setText("预订单生成/待支付订金");
					} else {

						holder.trip_state.setText("预订单生成/待支付全款");
					}
				}
				// holder.trip_state.setText("(预订成功)待付款");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.event_red));
			} else if (orderState.equals("apply_cancel")) {
				holder.trip_state.setText("订单已取消");
				// holder.trip_state.setText("(预订成功)待付款");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
			} else if (orderState.equals("book_success")) {
				// holder.trip_state.setText("订购成功");
				holder.trip_state.setText("订单成功");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
			} else if (orderState.equals("book_expire")) {
				// holder.trip_state.setText("订单已过期");
				holder.trip_state.setText("订单过期");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
			} else if (orderState.equals("deposit_success")) {
				holder.trip_state.setText("预订成功-待支付尾款");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.event_red));
			}
			// else if (orderState.equals("refund_failure"))
			// {
			// holder.trip_state.setText("退订被拒绝");
			// holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.event_red));
			// // holder.contact_author.setVisibility(View.VISIBLE);
			// }
			// else if (orderState.equals("apply_refund"))
			// {
			// holder.trip_state.setText("等待对方处理");
			// holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.event_red));
			// // holder.contact_author.setVisibility(View.VISIBLE);
			// }
			else if (orderState.equals("service_running")) {
				holder.trip_state.setText("行程进行中");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.event_red));
			} else if (orderState.equals("service_end")) {
				holder.trip_state.setText("行程完成");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
			} else if (orderState.equals("refund_running")) {
				holder.trip_state.setText("退订中");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.event_red));
			} else if (orderState.equals("refund_success")) {
				holder.trip_state.setText("退订成功");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
			} else if ("delete".equals(orderState)) {
				holder.trip_state.setText("订单已删除");
				holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
			}
			// else if (orderState.equals("staff_handle"))
			// {
			// holder.trip_state.setText("客服处理中");
			// holder.trip_state.setTextColor(mContext.getResources().getColor(R.color.event_red));
			// }

			if (("book_expire".equals(orderState) || "apply_success".equals(orderState)
					|| "apply_cancel".equals(orderState) || "refund_success".equals(orderState)
					|| "service_end".equals(orderState)) && "enable".equals(myOrder.getDeleteBtn())) {
				// 订单过期、待付款、退订成功、服务结束状态的订单可删除
				holder.delOrderBtn.setVisibility(View.VISIBLE);
				holder.delOrderBtn.setTag(myOrder.getId());
				holder.delOrderBtn.setOnClickListener(mOnClickListener);
			} else {
				holder.delOrderBtn.setVisibility(View.GONE);
			}

			if ("service_end".equals(orderState) && "enable".equals(myOrder.getAppraiseBtn())) {
				// 订单状态为service_end，并且没有评价过
				holder.commentBtn.setVisibility(View.VISIBLE);
				holder.commentBtn.setTag(myOrder.getId());
				holder.commentBtn.setOnClickListener(mOnClickListener);
			} else {
				holder.commentBtn.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, AffirmDialog.class);
			switch (v.getId()) {
			case R.id.acept_button:
				intent.putExtra("isAgree", true);
				mContext.startActivity(intent);
				break;
			case R.id.refuse_button:
				intent.putExtra("isAgree", false);
				mContext.startActivity(intent);
				break;
			case R.id.comment_myjour_order:
				final String productId = v.getTag().toString();
				new AppariseDialog(mContext, new OnAppariseListener() {
					@Override
					public void appariseOrder(String content, int descScore, int serviceScore, int satisScore) {
						onOrderListener.appariseOrder(content, descScore, serviceScore, satisScore, productId);
					}
				}).show();
				break;
			case R.id.del_myjour_order:
				final String orderId = v.getTag().toString();
				onOrderListener.delOrder(orderId);
				break;
			default:
				break;
			}
		}
	};

	public class ViewHolder {
		/**
		 * 预订产品照片
		 */
		public ImageView product_photo;

		/**
		 * 预订产品介绍
		 */
		public TextView product_introduce;

		/**
		 * 预订产品名称
		 */
		public TextView product_name;

		/**
		 * 预订产品旅行周期
		 */
		public TextView trip_time;

		/**
		 * 预订产品发布者头像
		 */
		public RoundImageView authorPhoto;

		/**
		 * 预订产品价格
		 */
		public TextView produce_price;

		// /**
		// * 联系卖家
		// */
		// public ImageView contact_author;

		/**
		 * 预订产品旅行状态
		 */
		public TextView trip_state;

		public TextView levelTxt;

		/**
		 * 卖家名称
		 */
		public TextView productAuthorName;

		/**
		 * 评价按钮
		 */
		public Button commentBtn;

		/**
		 * 删除订单按钮
		 */
		public Button delOrderBtn;

		public TextView productCode;

		public TextView orderTime;
	}

	public interface OnOrderListener {
		public void appariseOrder(String content, int descScore, int serviceScore, int satisScore, String productId);

		public void delOrder(String orderId);
	}
}
