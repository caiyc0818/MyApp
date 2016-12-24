package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.CompanionListAdapter;
import com.bcinfo.tripaway.adapter.CompanionNewAdapter;
import com.bcinfo.tripaway.bean.DispatchInfo;
import com.bcinfo.tripaway.bean.Invoice;
import com.bcinfo.tripaway.bean.MyOrder;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.ServResrouce;
import com.bcinfo.tripaway.bean.ServiceInfo;
import com.bcinfo.tripaway.bean.TraceInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.enums.OrderStatusEnum;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.alipay.AlipayUtile;
import com.bcinfo.tripaway.utils.alipay.Result;
import com.bcinfo.tripaway.view.dialog.AppariseDialog;
import com.bcinfo.tripaway.view.dialog.AppariseDialog.OnAppariseListener;
import com.bcinfo.tripaway.view.dialog.UnsubcribeLeaveDialog;
import com.bcinfo.tripaway.view.dialog.UnsubcribeLeaveDialog.ApplyUnsubcribeListener;
import com.bcinfo.tripaway.view.pop.ComplainPopWindow;
import com.bcinfo.tripaway.view.pop.ComplainPopWindow.OperationListener;
import com.bcinfo.tripaway.view.textview.NormalTfTextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的订单详情
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月24日 下午3:55:29
 */
public class MyOrderDetailActivity extends BaseActivity implements OnClickListener {
	/**
	 * 退订策略
	 */
	private TextView levelTxt;

	private String orderId;

	/**
	 * 订单号
	 */
	private TextView orderNumTxt;

	/**
	 * 订单创建
	 */
	private TextView createTimeTxt;

	/**
	 * 总价
	 */
	// private TextView totalPriceTxt;

	/**
	 * 旅程开始时间
	 */
	private TextView orderStartTxt;

	/**
	 * 旅程结束时间
	 */
	private TextView orderEndTxt;

	/**
	 * 订单支付时间
	 */
	private TextView orderPayTime;

	/**
	 * 订单支付方式
	 */
	private TextView orderPayType;

	/**
	 * 订单状态
	 */
	private TextView orderStatusTxt;

	/**
	 * 预订人姓名
	 */
	// private TextView userNameTxt;

	/**
	 * 预订人电话
	 */
	// private TextView userTelTxt;

	/**
	 * 预订人地址
	 */
	// private TextView userAddressTxt;

	/**
	 * 预订人证件号
	 */
	// private TextView userCardNumTxt;

	/**
	 * 产品海报
	 */
	private ImageView productPosterImg;

	/**
	 * 产品名
	 */
	private TextView productNameTxt;

	/**
	 * 产品介绍
	 */
	private TextView productIntroduceTxt;

	/**
	 * 产品布局
	 */
	private LinearLayout productLayout;

	/**
	 * 费用列表
	 */
	// private ListView feeLst;

	/**
	 * 行程列表
	 */
	// private ListView tripLst;

	/**
	 * 同行人列表
	 */
	private ListView companionLst;

	/**
	 * 费用适配器
	 */
	// private MyOrderFeeAdapter feeAdapter;

	// private List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();

	/**
	 * 同行人列表适配器
	 */
	// private CompanionListAdapter companionAdapter;
	private CompanionNewAdapter companionAdapter;

	private List<PartnerInfo> partnerInfos = new ArrayList<PartnerInfo>();

	// private OrderServiceListAdapter serviceResourceAdapter;

	// private List<ServResrouce> servResrouces = new ArrayList<ServResrouce>();

	private MyOrder orderInfo;

	// private TextView productPriceTxt;

	private int paytype = -1;

	private RelativeLayout layout_title;
	/**
	 * 已支付订金
	 */
	private RelativeLayout retive2;
	// private ImageView order_phone_pic;
	private LinearLayout go_talk;

	private TextView mTraceStatusTv;

	private TextView mTraceTimeTv;

	private TextView mTraceDescTv;
	/**
	 * 待付订金:
	 */
	private TextView balance_d;

	// private TextView mOrderprefer;

	private LinearLayout mInvoiceLy;

	private ImageView mInvoiceImg;

	private LinearLayout mInvoiceInfoLy;

	private boolean isInvoiceShow = false;

	private TextView mInvoiceType;

	private TextView mInvoiceTitle;

	private TextView mInvoiceName;

	private TextView mInvoiceTel;

	private TextView mInvoiceAddress;

	private TextView mInvoicePostCode;

	private LinearLayout refundLayout;

	private LinearLayout appraiseLayout;

	private LinearLayout deleteLayout;

	private LinearLayout cancelLayout;

	private LinearLayout payLayout;

	// private LinearLayout finalpayLayout;

	private RelativeLayout statusLayout;

	// private String totalMon;

	private ImageView mComplainIv;
	/**
	 * 联系人姓名
	 */
	private TextView contact_name;
	/**
	 * 联系人证件号
	 */
	private TextView contact_num;
	/**
	 * 紧急联系人姓名
	 */
	private TextView emergency_contact_name;
	/**
	 * 紧急联系人证件号
	 */
	private TextView emergency_contact_num;
	/**
	 * 订金支付
	 */
	private LinearLayout price_all_p;
	/**
	 * 全款支付
	 */
	private LinearLayout price_all_l;

	/**
	 * 已支付订金
	 */
	private TextView deposite_d;
	/**
	 * 订金支付单价
	 */
	private TextView price_info_d;
	/**
	 * 订单金额
	 */
	private String totalPrice;
	/**
	 * 订金支付订单金额
	 */
	private TextView order_total_price_d;
	/**
	 * 全款支付订单金额
	 */
	private TextView order_total_price;
	/**
	 * 单价*单位
	 */
	private TextView price_info;
	/**
	 * 尾款
	 */
	private TextView balance_value_d;

	private boolean ccc;

	private TextView favorablePrice;

	private TextView favorablePrice2;

	private RelativeLayout layout_favorablePrice;

	private RelativeLayout layout_favorablePrice2;

	private NormalTfTextView product_num;
	// 分期
	private LinearLayout price_installment_p;
	private TextView installment_order_total_price_d;
	private TextView installment_price_info_d;
	private TextView installment_balance_value_d;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.my_order_detail_activity);
		statisticsTitle = "订单详情";
		AppManager.getAppManager().addActivity(this);
		orderId = getIntent().getStringExtra("orderId");
		LinearLayout layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		statusLayout = (RelativeLayout) findViewById(R.id.status_layout);
		layout_title.getBackground().setAlpha(255);
		layout_back_button.setOnClickListener(this);
		initView();
		queryOrderDetail();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		layout_title.getBackground().setAlpha(255);
	}

	protected void initView() {
		product_num = (NormalTfTextView) findViewById(R.id.product_num);
		contact_name = (TextView) findViewById(R.id.contact_name);
		contact_num = (TextView) findViewById(R.id.contact_num);
		emergency_contact_name = (TextView) findViewById(R.id.emergency_contact_name);
		emergency_contact_num = (TextView) findViewById(R.id.emergency_contact_num);
		price_all_p = (LinearLayout) findViewById(R.id.price_all_p);
		price_all_l = (LinearLayout) findViewById(R.id.price_all_l);
		deposite_d = (TextView) findViewById(R.id.deposite_d);
		price_info_d = (TextView) findViewById(R.id.price_info_d);
		order_total_price_d = (TextView) findViewById(R.id.order_total_price_d);
		balance_value_d = (TextView) findViewById(R.id.balance_value_d);
		order_total_price = (TextView) findViewById(R.id.order_total_price);
		price_info = (TextView) findViewById(R.id.price_info);
		balance_d = (TextView) findViewById(R.id.balance_d);
		retive2 = (RelativeLayout) findViewById(R.id.retive2);
		// order_phone_pic = (ImageView) findViewById(R.id.order_phone_pic);
		go_talk = (LinearLayout) findViewById(R.id.go_talk);
		levelTxt = (TextView) findViewById(R.id.level_txt);
		orderNumTxt = (TextView) findViewById(R.id.order_id);
		createTimeTxt = (TextView) findViewById(R.id.order_time);
		// productPriceTxt = (TextView)findViewById(R.id.order_product_price);
		// totalPriceTxt = (TextView) findViewById(R.id.order_total_price);
		orderStatusTxt = (TextView) findViewById(R.id.order_state);
		// userNameTxt = (TextView)findViewById(R.id.user_name);
		// userTelTxt = (TextView)findViewById(R.id.order_phone_num);
		// userAddressTxt = (TextView)findViewById(R.id.order_address);
		// userCardNumTxt = (TextView)findViewById(R.id.my_id_card);
		productPosterImg = (ImageView) findViewById(R.id.product_photo);
		productNameTxt = (TextView) findViewById(R.id.product_name);
		productIntroduceTxt = (TextView) findViewById(R.id.product_introduce);
		productLayout = (LinearLayout) findViewById(R.id.product_layout);
		productLayout.setOnClickListener(this);
		companionLst = (ListView) findViewById(R.id.companion_listview);
		go_talk.setOnClickListener(this);
		// companionAdapter = new CompanionListAdapter(this, partnerInfos);
		companionAdapter = new CompanionNewAdapter(this, partnerInfos);
		companionLst.setAdapter(companionAdapter);
		orderEndTxt = (TextView) findViewById(R.id.order_end_time);
		orderPayTime = (TextView) findViewById(R.id.order_pay_time);
		orderPayType = (TextView) findViewById(R.id.order_pay_type);
		orderStartTxt = (TextView) findViewById(R.id.order_start_time);
		mTraceStatusTv = (TextView) findViewById(R.id.trace_status_tv);
		mTraceTimeTv = (TextView) findViewById(R.id.trace_time_tv);
		mTraceDescTv = (TextView) findViewById(R.id.trace_desc_tv);
		// mOrderprefer = (TextView) findViewById(R.id.order_prefer);
		mInvoiceLy = (LinearLayout) findViewById(R.id.invoice_layout);
		mInvoiceImg = (ImageView) findViewById(R.id.invoice_btn);
		mInvoiceImg.setOnClickListener(this);
		mInvoiceInfoLy = (LinearLayout) findViewById(R.id.invoice_info_ly);
		mInvoiceType = (TextView) findViewById(R.id.invoice_type);
		mInvoiceAddress = (TextView) findViewById(R.id.invoice_address);
		mInvoiceName = (TextView) findViewById(R.id.invoice_name);
		mInvoiceTel = (TextView) findViewById(R.id.invoice_tel);
		mInvoicePostCode = (TextView) findViewById(R.id.invoice_postcode);
		mInvoiceTitle = (TextView) findViewById(R.id.invoice_title);
		refundLayout = (LinearLayout) findViewById(R.id.refund_order);
		appraiseLayout = (LinearLayout) findViewById(R.id.evalute_order);
		deleteLayout = (LinearLayout) findViewById(R.id.remove_order);
		cancelLayout = (LinearLayout) findViewById(R.id.cancel_order);
		payLayout = (LinearLayout) findViewById(R.id.pay_order);
		refundLayout.setOnClickListener(this);
		appraiseLayout.setOnClickListener(this);
		deleteLayout.setOnClickListener(this);
		cancelLayout.setOnClickListener(this);
		payLayout.setOnClickListener(this);
		// finalpayLayout = (LinearLayout) findViewById(R.id.finalpay_order);
		// finalpayLayout.setOnClickListener(this);
		mComplainIv = (ImageView) findViewById(R.id.complain_iv);
		mComplainIv.setOnClickListener(this);
		favorablePrice = (TextView) findViewById(R.id.favorablePrice);
		favorablePrice2 = (TextView) findViewById(R.id.favorablePrice2);
		layout_favorablePrice = (RelativeLayout) findViewById(R.id.layout_favorablePrice);
		layout_favorablePrice2 = (RelativeLayout) findViewById(R.id.layout_favorablePrice2);
		// 分期
		price_installment_p = (LinearLayout) findViewById(R.id.price_installment_p);
		installment_order_total_price_d = (TextView) findViewById(R.id.installment_order_total_price_d);
		installment_price_info_d = (TextView) findViewById(R.id.installment_price_info_d);
		installment_balance_value_d = (TextView) findViewById(R.id.installment_balance_value_d);
	}

	private void initData(MyOrder orderInfo) {
		// 初始化控件状态
		cancelLayout.setVisibility(View.GONE);
		deleteLayout.setVisibility(View.GONE);
		refundLayout.setVisibility(View.GONE);
		payLayout.setVisibility(View.GONE);
		appraiseLayout.setVisibility(View.GONE);
		orderNumTxt.setText(orderInfo.getNo());
		createTimeTxt.setText(DateUtil.getFormateDate(orderInfo.getCreateTime()));

		orderStartTxt.setText(DateUtil.formateDateToTime(orderInfo.getBeginDate()) + "-"
				+ DateUtil.formateDateToTime(orderInfo.getEndDate()));

		
		
//		
		
		
		// 支付时间 - 支付状态
		List<TraceInfo> traceList = orderInfo.getTraceInfoList();

		boolean flag = false;

		for (TraceInfo traceInfo : traceList) {
			if ("book_success".equals(traceInfo.getStatus())) {
				orderPayType.setText("支付全额");
				orderPayTime.setText(DateUtil.getFormateDate(traceInfo.getTraceTime()));
				flag = true;
				paytype = 0;

				break;
			}
		}
		if (!flag) {
			for (TraceInfo traceInfo : traceList) {
				if ("deposit_success".equals(traceInfo.getStatus())) {
					orderPayType.setText("支付订金");
					orderPayTime.setText(DateUtil.getFormateDate(traceInfo.getTraceTime()));
					paytype = 1;
					break;
				}
			}
		}
		ArrayList<DispatchInfo> temp = orderInfo.getDispatchInfos();
		// 状态跟踪
		if (traceList != null && traceList.size() > 0) {
			statusLayout.setVisibility(View.VISIBLE);
			statusLayout.setOnClickListener(this);
			if (temp != null && temp.size() > 0) {
				DispatchInfo dispatchInfo = temp.get(0);
				mTraceDescTv.setText("联系电话：" + (StringUtils.isEmpty(dispatchInfo.getDispatchTo().getMobile()) ? ""
						: dispatchInfo.getDispatchTo().getMobile()));
				String sex = "";
				if (dispatchInfo.getDispatchTo().getGender().equals("0")) {
					sex = "女";
				} else {
					sex = "男";
				}
				String dis = "";
				if (dispatchInfo.getDispatchTo().getOrgRole().getRoleName().equals("客服")) {
					dis = "已受理";
				} else if (dispatchInfo.getDispatchTo().getOrgRole().getRoleName().equals("导游")
						|| dispatchInfo.getDispatchTo().getOrgRole().getRoleName().equals("司机")
						|| dispatchInfo.getDispatchTo().getOrgRole().getRoleName().equals("领队")) {
					dis = "已接单";
				}
				mTraceStatusTv.setText(dispatchInfo.getDispatchTo().getRealName() + "(" + sex
						+ dispatchInfo.getDispatchTo().getOrgRole().getRoleName() + ")" + dis);
				mTraceTimeTv.setText(DateUtil.getFormateDate(dispatchInfo.getCreateTime()));
			} else {
				TraceInfo trace = traceList.get(0);
				mTraceDescTv.setText(trace.getDesc());
				mTraceStatusTv.setText(OrderStatusEnum.getFromStatus(trace.getStatus()));
				mTraceTimeTv.setText(DateUtil.getFormateDate(trace.getTraceTime()));
			}
		} else {
			statusLayout.setVisibility(View.GONE);
		}

		// 发票信息
		if (orderInfo.getInvoice() != null) {
			Invoice invoice = orderInfo.getInvoice();
			mInvoiceLy.setVisibility(View.VISIBLE);
			mInvoiceTitle.setText(invoice.getInvoiceTitle());
			if ("0".equals(invoice.getInvoiceType())) {
				mInvoiceType.setText("个人发票");
			} else if ("1".equals(invoice.getInvoiceType())) {
				mInvoiceType.setText("单位发票");
			}
			mInvoiceName.setText(invoice.getUserName());
			mInvoiceTel.setText(invoice.getTel());
			mInvoiceAddress.setText(invoice.getAddress());
			mInvoicePostCode.setText(invoice.getPostCode());
		}
		if (orderInfo.getRefundPolicy().equals("super")) {
			levelTxt.setText("非常严格");
		} else if (orderInfo.getRefundPolicy().equals("high")) {
			levelTxt.setText("严格");
		} else if (orderInfo.getRefundPolicy().equals("middle")) {
			levelTxt.setText("适中");
		} else if (orderInfo.getRefundPolicy().equals("low")) {
			levelTxt.setText("灵活");
		}
		String unitValue = StringUtils.isEmpty(orderInfo.getUnit()) ? "人" : orderInfo.getUnit();
		partnerInfos.addAll(orderInfo.getPartnerInfoList());
		if (!StringUtils.isEmpty(orderInfo.getTotalPrice())) {
			totalPrice = orderInfo.getTotalPrice();
		} else {
			totalPrice = new BigDecimal(orderInfo.getPrice()).multiply(new BigDecimal(partnerInfos.size()))
					.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}
		// 判断是定金支付还是全款支付+分期支付
		if (!StringUtils.isEmpty(orderInfo.getIs_instal()) && "yes".equals(orderInfo.getIs_instal())) {
			price_all_p.setVisibility(View.GONE);
			price_all_l.setVisibility(View.GONE);
			price_installment_p.setVisibility(View.VISIBLE);
			installment_order_total_price_d.setText("￥" + totalPrice);
			installment_price_info_d
					.setText("( ￥" + orderInfo.getPrice() + "/" + unitValue + "x " + orderInfo.getAmount() + " )");
			installment_balance_value_d
					.setText("￥" + orderInfo.getSfy_sf_price() + "×" + orderInfo.getPeriod_num() + "期");
		} else {
			if (!StringUtils.isEmpty(orderInfo.getFinalPayment()) && orderInfo.getFinalPayment().compareTo("0") > 0) {
				price_all_p.setVisibility(View.VISIBLE);
				price_all_l.setVisibility(View.GONE);
				price_installment_p.setVisibility(View.GONE);
				deposite_d
						.setText("￥" + (StringUtils.isEmpty(orderInfo.getDeposit()) ? "0.00" : orderInfo.getDeposit()));
				// price_info_d.setText("( ￥" + orderInfo.getPrice() + "/" +
				// unitValue + "x " + partnerInfos.size() + " )");
				price_info_d
						.setText("( ￥" + orderInfo.getPrice() + "/" + unitValue + "x " + orderInfo.getAmount() + " )");
				order_total_price_d.setText("￥" + totalPrice);
				// balance_value_d.setText("￥" + orderInfo.getFinalPayment());
			} else {
				price_all_p.setVisibility(View.GONE);
				price_all_l.setVisibility(View.VISIBLE);
				price_installment_p.setVisibility(View.GONE);
				order_total_price.setText("￥" + totalPrice);
				// price_info.setText("( ￥" + orderInfo.getPrice() + "/" +
				// unitValue
				// + "x " + partnerInfos.size() + " )");
				price_info
						.setText("( ￥" + orderInfo.getPrice() + "/" + unitValue + "x " + orderInfo.getAmount() + " )");
			}
		}
		// 预订单生成/待支付定金（全款）
		if ("apply_success".equals(orderInfo.getStatus())) {
			// 判断是定金支付还是全款支付+分期支付

			if (!StringUtils.isEmpty(orderInfo.getIs_instal()) && "yes".equals(orderInfo.getIs_instal())) {
				((TextView) payLayout.getChildAt(0)).setText("分期付款");
				paytype = 2;
				orderStatusTxt.setText("预订单生成/待分期付款");
			} else {
				if (!StringUtils.isEmpty(orderInfo.getFinalPayment())
						&& orderInfo.getFinalPayment().compareTo("0") > 0) {
					((TextView) payLayout.getChildAt(0)).setText("支付订金");
					paytype = 1;
					orderStatusTxt.setText("预订单生成/待支付订金");
					balance_d.setText("待付订金: ");
					retive2.setVisibility(View.GONE);
				} else {
					((TextView) payLayout.getChildAt(0)).setText("支付全额");
					paytype = 0;
					orderStatusTxt.setText("预订单生成/待支付全款");
				}
			}
			payLayout.setVisibility(View.VISIBLE);
			if ("enable".equals(orderInfo.getCancelBtn())) {
				cancelLayout.setVisibility(View.VISIBLE);
			} else {
				cancelLayout.setVisibility(View.GONE);
			}
			balance_value_d.setText("￥" + orderInfo.getDeposit());
		} else {
			orderStatusTxt.setText(OrderStatusEnum.getFromStatus(orderInfo.getStatus()));
		}
		// 预订成功-待支付尾款 已支付定金
		if ("deposit_success".equals(orderInfo.getStatus())) {
			if (orderInfo.getFinalPayButton().equals("enable")) {
				((TextView) payLayout.getChildAt(0)).setEnabled(true);
			} else if (orderInfo.getFinalPayButton().equals("disable")) {
				((TextView) payLayout.getChildAt(0)).setEnabled(false);
			}
			((TextView) payLayout.getChildAt(0)).setText("支付尾款");
			payLayout.setVisibility(View.VISIBLE);
			balance_d.setText("待付尾款: ");
			// 待付尾款的金额字体设置为红色
			balance_value_d.setTextColor(getResources().getColor(R.color.red));
			// 代付尾款的金额字体大小14sp
			balance_value_d.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			retive2.setVisibility(View.VISIBLE);
			if ("enable".equals(orderInfo.getCancelBtn())) {
				cancelLayout.setVisibility(View.VISIBLE);
			} else {
				cancelLayout.setVisibility(View.GONE);
			}
			// 退订按钮
			if (orderInfo.getRefundButton().equals("enable")) {
				refundLayout.setVisibility(View.VISIBLE);
				refundLayout.setEnabled(true);
			} else if (orderInfo.getRefundButton().equals("disable")) {
				refundLayout.setVisibility(View.GONE);
				refundLayout.setEnabled(false);
			}
			balance_value_d.setText("￥" + orderInfo.getFinalPayment());
		}

		if (!StringUtils.isEmpty(orderInfo.getCouponReducePrice()) && !"0.0".equals(orderInfo.getCouponReducePrice())) {
			favorablePrice.setText("-￥" + orderInfo.getCouponReducePrice());
			favorablePrice2.setText("-￥" + orderInfo.getCouponReducePrice());
			layout_favorablePrice.setVisibility(View.VISIBLE);
			layout_favorablePrice2.setVisibility(View.VISIBLE);
		}

		// 行程进行中
		if (orderInfo.getStatus().equals("service_running")) {
			payLayout.setVisibility(View.VISIBLE);
			payLayout.setBackgroundColor(getResources().getColor(R.color.white));
			payLayout.setEnabled(false);
			((TextView) payLayout.getChildAt(0)).setText("行程正在进行中，祝您旅途愉快！");
		}
		// 订单已取消
		if ("apply_cancel".equals(orderInfo.getStatus())) {
			if ("enable".equals(orderInfo.getCancelBtn())) {
				cancelLayout.setVisibility(View.VISIBLE);
			} else {
				cancelLayout.setVisibility(View.GONE);
			}
			payLayout.setVisibility(View.VISIBLE);
			((TextView) payLayout.getChildAt(0)).setText("订单已取消");
			((TextView) payLayout.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray_text));
			payLayout.setEnabled(false);
			payLayout.setBackgroundColor(getResources().getColor(R.color.price_btn_cancel));
		} else {
			payLayout.setEnabled(true);
			payLayout.setBackgroundColor(getResources().getColor(R.color.red));
		}
		// 订单过期，预订单生成/待支付定金（全款），退订成功，订单已取消，行程完成
		if ("book_expire".equals(orderInfo.getStatus()) || "apply_success".equals(orderInfo.getStatus())
				|| "refund_success".equals(orderInfo.getStatus()) || "apply_cancel".equals(orderInfo.getStatus())
				|| "service_end".equals(orderInfo.getStatus())) {
			if ("enable".equals(orderInfo.getDeleteBtn())) {
				deleteLayout.setVisibility(View.VISIBLE);
			} else {
				deleteLayout.setVisibility(View.GONE);
			}
		}
		// 订单成功
		if (orderInfo.getStatus().equals("book_success")) {
			// 退订按钮
			if (orderInfo.getRefundButton().equals("enable")) {
				refundLayout.setVisibility(View.VISIBLE);
				refundLayout.setEnabled(true);
			} else if (orderInfo.getRefundButton().equals("disable")) {
				refundLayout.setVisibility(View.GONE);
				refundLayout.setEnabled(false);
			}
			balance_d.setText("已付尾款: ");
			// 已付尾款的金额字体颜色#333333
			balance_value_d.setTextColor(getResources().getColor(R.color.gray_text));
			// 已付尾款的金额字体大小12sp
			balance_value_d.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		}
		// 行程完成
		if ("service_end".equals(orderInfo.getStatus())) {
			if ("enable".equals(orderInfo.getAppraiseBtn())) {
				appraiseLayout.setVisibility(View.VISIBLE);
			} else {
				appraiseLayout.setVisibility(View.GONE);
			}
		}
		// 退订中
		if ("refund_running".equals(orderInfo.getStatus())) {
			payLayout.setVisibility(View.VISIBLE);
			payLayout.setBackgroundColor(getResources().getColor(R.color.white));
			payLayout.setEnabled(false);
			((TextView) payLayout.getChildAt(0)).setText("订单正在退订中，请耐心等待！");
		}
		// 退订成功
		if ("refund_success".equals(orderInfo.getStatus())) {

		}
		// 联系人
		contact_name.setText(StringUtils.isEmpty(orderInfo.getContact_name()) ? "" : orderInfo.getContact_name());
		// 联系人号码
		contact_num.setText(StringUtils.isEmpty(orderInfo.getTel()) ? "" : orderInfo.getTel());
		// 紧急联系人
		emergency_contact_name.setText(
				StringUtils.isEmpty(orderInfo.getUrgentContactPerson()) ? "暂未填写" : orderInfo.getUrgentContactPerson());
		// 紧急联系人号码
		emergency_contact_num
				.setText(StringUtils.isEmpty(orderInfo.getUrgentContactNum()) ? "" : orderInfo.getUrgentContactNum());
		if (!StringUtils.isEmpty(orderInfo.getProduct().getTitleImg())) {
			ImageLoader.getInstance().displayImage(Urls.imgHost + orderInfo.getProduct().getTitleImg(),
					productPosterImg, AppConfig.options(R.drawable.ic_launcher));
		}
		product_num.setText("产品编号：" + orderInfo.getProduct().getProductCode());
		productNameTxt.setText(orderInfo.getProduct().getTitle());
		productIntroduceTxt.setText(orderInfo.getProduct().getDescription());
		productLayout.setTag(orderInfo.getProduct());
		companionAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back_button:
			finish();
			activityAnimationClose();
			break;
		case R.id.product_layout:
			ProductNewInfo productNewInfo = (ProductNewInfo) v.getTag();
			if (productNewInfo != null) {
				Intent intent = null;
				if (productNewInfo.getProductType().equals("single")) {
					if (productNewInfo.getServices().equals("traffic")) {
						intent = new Intent(MyOrderDetailActivity.this, CarProductDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					} else if (productNewInfo.getServices().equals("stay")) {
						intent = new Intent(MyOrderDetailActivity.this, HouseProductDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					} else {
						intent = new Intent(MyOrderDetailActivity.this, GrouponProductNewDetailActivity.class);
						intent.putExtra("productId", productNewInfo.getId());
					}
				} else if (productNewInfo.getProductType().equals("base")
						|| productNewInfo.getProductType().equals("customization")) {
					intent = new Intent(MyOrderDetailActivity.this, GrouponProductNewDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				} else if (productNewInfo.getProductType().equals("team")) {
					intent = new Intent(MyOrderDetailActivity.this, GrouponProductNewDetailActivity.class);
					intent.putExtra("productId", productNewInfo.getId());
				}
				intent.putExtra("productTitle", productNewInfo.getTitle());
				startActivity(intent);
				activityAnimationOpen();
			}
			break;
		case R.id.pay_order:
			getOrederInfo(orderInfo);
			// // 去支付
			// new Thread()
			// {
			// public void run()
			// {
			// String orderInfoStr =
			// AlipayUtile.getNewOrderInfo(orderInfo.getId(),
			// orderInfo.getProduct().getTitle(),
			// "0.01");
			// // 构造PayTask 对象
			// PayTask alipay = new PayTask(MyOrderDetailActivity.this);
			// // 调用支付接口
			// String result = alipay.pay(orderInfoStr);
			//
			// Log.i("MyOrderDetailActivity", "result = " + result);
			// Message msg = new Message();
			// msg.what = 0;
			// msg.obj = result;
			// mHandler.sendMessage(msg);
			// }
			// }.start();
			// else if (leftBtnTxt.getText().equals("退订"))
			// {
			//
			// }
			// else if (leftBtnTxt.getText().equals("申诉"))
			// {
			// applyComplain();
			// }
			break;
		case R.id.refund_order:
			// 退订
			new UnsubcribeLeaveDialog(MyOrderDetailActivity.this, unsubcribeListen).show();
			break;
		case R.id.evalute_order:
			// 评价订单
			new AppariseDialog(MyOrderDetailActivity.this, new OnAppariseListener() {
				@Override
				public void appariseOrder(String content, int descScore, int serviceScore, int satisScore) {
					evaProductComments(content, descScore, serviceScore, satisScore, orderInfo.getProduct().getId());
				}
			}).show();
			break;
		case R.id.remove_order:
			// 删除订单
			new AlertDialog.Builder(MyOrderDetailActivity.this).setTitle("确认删除吗？")
					.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							delOrderById(orderId);
							dialog.dismiss();
						}
					}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
			break;
		case R.id.cancel_order:
			// 取消订单
			new AlertDialog.Builder(MyOrderDetailActivity.this).setTitle("确认取消吗？")
					.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelOrderByOrderId(orderInfo.getId());
							dialog.dismiss();
						}
					}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
			break;
		case R.id.go_talk:
			joinPrivateChat();
			break;
		// case R.id.no_pay_layout:
		// break;
		case R.id.invoice_btn:
			isInvoiceShow = !isInvoiceShow;
			if (isInvoiceShow) {
				mInvoiceImg.setImageResource(R.drawable.up_arrow);
				mInvoiceInfoLy.setVisibility(View.VISIBLE);
			} else {
				mInvoiceImg.setImageResource(R.drawable.down_arrow);
				mInvoiceInfoLy.setVisibility(View.GONE);
			}
			break;
		case R.id.complain_iv:
			popupComplainView(v);
			break;
		case R.id.status_layout:
			Intent it = new Intent(this, OrderStatusFollowActivity.class);
			it.putParcelableArrayListExtra("trace", (ArrayList<TraceInfo>) orderInfo.getTraceInfoList());
			it.putParcelableArrayListExtra("dispatch", (ArrayList<DispatchInfo>) orderInfo.getDispatchInfos());
			startActivity(it);
			activityAnimationOpen();
			break;
		default:
			break;
		}
	}

	private void queryOrderDetail() {
		LogUtil.i("MyOrderDetailActivity", "queryOrderDetail", Urls.query_orderdetail_url + orderId);
		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		HttpUtil.get(Urls.query_orderdetail_url + orderId, null, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				System.out.println("旅程详情接口=" + response);
				if (response.optString("code").equals("00000")) {
					JSONObject dataJson = response.optJSONObject("data");
					if (dataJson != null && !dataJson.equals("")) {
						orderInfo = new MyOrder();

						// orderInfo.setRefundPolicy(dataJson.optString("refundPolicy"));

						orderInfo.setCreateTime(dataJson.optString("createTime"));
						JSONObject receiverJson = dataJson.optJSONObject("receiver");
						if (receiverJson != null && !receiverJson.equals("")) {
							UserInfo userInfo = new UserInfo();

							userInfo.setGender(receiverJson.optString("sex"));
							userInfo.setAddress(receiverJson.optString("address"));
							userInfo.setStatus(receiverJson.optString("status"));
							userInfo.setEmail(receiverJson.optString("email"));
							userInfo.setNickname(receiverJson.optString("nickName"));
							userInfo.setUserId(receiverJson.optString("userId"));
							userInfo.setRole(receiverJson.optString("role"));
							userInfo.setPermission(receiverJson.optString("permission"));
							userInfo.setAvatar(receiverJson.optString("avatar"));
							userInfo.setIntroduction(receiverJson.optString("introduction"));
							userInfo.setUsersIdentity(receiverJson.optString("usersIdentity"));
							userInfo.setMobile(receiverJson.optString("mobile"));
							JSONArray tagsJsonArray = receiverJson.optJSONArray("tags");
							if (tagsJsonArray != null && tagsJsonArray.length() > 0) {
								ArrayList<String> tags = new ArrayList<String>();
								for (int j = 0; j < tagsJsonArray.length(); j++) {
									tags.add(tagsJsonArray.optString(j));
								}
								userInfo.setTags(tags);
							}
							orderInfo.setReceiverInfo(userInfo);
						}
						// orderInfo.setRefundPolicy(dataJson.optString("refundPolicy"));

						orderInfo.setRemark(dataJson.optString("remark"));
						orderInfo.setStatus(dataJson.optString("status"));
						orderInfo.setNo(dataJson.optString("no"));
						orderInfo.setUnit(dataJson.optString("unit"));
						orderInfo.setLeaveword(dataJson.optString("leaveword"));
						orderInfo.setEndDate(dataJson.optString("endDate"));
						orderInfo.setBeginDate(dataJson.optString("beginDate"));
						orderInfo.setAmount(dataJson.optString("amount"));
						orderInfo.setPreferentialPrice(dataJson.optString("preferentialPrice"));
						orderInfo.setDeposit(dataJson.optString("deposit"));
						orderInfo.setFinalPayment(dataJson.optString("finalPayment"));
						// orderInfo.setDeposit("0.01");
						// orderInfo.setFinalPayment("0.01");
						orderInfo.setOriginalPrice(dataJson.optString("originalTotal"));

						// 分期
						orderInfo.setIs_instal(dataJson.optString("is_instal"));
						orderInfo.setPeriod_num(dataJson.optString("period_num"));
						orderInfo.setSfy_sf_price(dataJson.optString("sfy_sf_price"));
						JSONObject productJson = dataJson.optJSONObject("product");
						if (productJson != null && !productJson.equals("")) {
							JSONObject policy = productJson.optJSONObject("policy");
							orderInfo.setRefundPolicy(policy.optString("type"));
							ProductNewInfo productInfo = new ProductNewInfo();
							JSONArray topicJsonArray = productJson.optJSONArray("topics");
							if (topicJsonArray != null && !topicJsonArray.equals("")) {
								ArrayList<String> topics = new ArrayList<String>();
								for (int j = 0; j < topicJsonArray.length(); j++) {
									// LogUtil.i(TAG, "topicJsonArray",
									// topicJsonArray.optString(j));
									topics.add(topicJsonArray.optString(j));
								}
								productInfo.setTopics(topics);
							}
							productInfo.setExpStart(productJson.optString("expStart"));
							productInfo.setExpend(productJson.optString("expend"));
							JSONObject userJSON = productJson.optJSONObject("creater");
							if (userJSON != null && !userJSON.toString().equals("")) {
								UserInfo userInfo = new UserInfo();
								userInfo.setGender(userJSON.optString("sex"));
								userInfo.setAddress(userJSON.optString("address"));
								userInfo.setStatus(userJSON.optString("status"));
								userInfo.setEmail(userJSON.optString("email"));
								userInfo.setNickname(userJSON.optString("nickName"));
								userInfo.setUserId(userJSON.optString("userId"));
								userInfo.setRole(userJSON.optString("role"));
								userInfo.setPermission(userJSON.optString("permission"));
								userInfo.setAvatar(userJSON.optString("avatar"));
								userInfo.setIntroduction(userJSON.optString("introduction"));
								userInfo.setMobile(userJSON.optString("mobile"));
								userInfo.setUsersIdentity(userJSON.optString("usersIdentity"));
								JSONArray tagsJsonArray = userJSON.optJSONArray("tags");
								if (tagsJsonArray != null && tagsJsonArray.length() > 0) {
									ArrayList<String> tags = new ArrayList<String>();
									for (int j = 0; j < tagsJsonArray.length(); j++) {
										tags.add(tagsJsonArray.optString(j));
									}
									userInfo.setTags(tags);
								}
								productInfo.setUser(userInfo);
							}
							productInfo.setId(productJson.optString("id"));
							productInfo.setDistance(productJson.optString("distance"));
							productInfo.setProductCode(productJson.optString("productCode"));
							productInfo.setTitle(productJson.optString("title"));
							productInfo.setPoiCount(productJson.optString("poiCount"));
							productInfo.setPrice(productJson.optString("price"));
							productInfo.setDays(productJson.optString("days"));
							productInfo.setDescription(productJson.optString("description"));
							productInfo.setPriceMax(productJson.optString("priceMax"));
							productInfo.setTitleImg(productJson.optString("titleImg"));
							productInfo.setMaxMember(productJson.optString("maxMember"));
							productInfo.setProductType(productJson.optString("productType"));
							productInfo.setServices(productJson.optString("serviceCodes"));
							orderInfo.setProduct(productInfo);
						}
						orderInfo.setId(dataJson.optString("id"));
						orderInfo.setPrice(dataJson.optString("price"));
						orderInfo.setStatements(dataJson.optString("statements"));
						orderInfo.setRefundButton(dataJson.optString("refundButton"));
						orderInfo.setStaffButton(dataJson.optString("staffButton"));
						orderInfo.setCancelBtn(dataJson.optString("cancelButton"));
						orderInfo.setAppraiseBtn(dataJson.optString("appraiseButton"));
						orderInfo.setDeleteBtn(dataJson.optString("deleteButton"));
						orderInfo.setFinalPayButton(dataJson.optString("finalPayButton"));
						// 联系方式
						orderInfo.setTel(dataJson.optString("tel"));
						// 联系人
						orderInfo.setContact_name(dataJson.optString("contactName"));
						// 紧急联系人号码
						orderInfo.setUrgentContactNum(dataJson.optString("urgentContactNum"));
						// 紧急联系人
						orderInfo.setUrgentContactPerson(dataJson.optString("urgentContactPerson"));
						orderInfo.setCouponCode(dataJson.optString("couponCode"));
						orderInfo.setCouponReducePrice(dataJson.optString("couponReducePrice"));
						orderInfo.setDiscountPrice(dataJson.optString("discountPrice"));
						JSONArray serviceArray = dataJson.optJSONArray("service");
						if (serviceArray != null && serviceArray.length() > 0) {
							List<ServiceInfo> serviceList = new ArrayList<ServiceInfo>();
							for (int i = 0; i < serviceArray.length(); i++) {
								ServiceInfo serviceInfo = new ServiceInfo();
								JSONObject serviceJson = serviceArray.optJSONObject(i);
								serviceInfo.setCode(serviceJson.optString("code"));
								serviceInfo.setName(serviceJson.optString("name"));
								serviceInfo.setPrice(serviceJson.optString("price"));
								serviceInfo.setResource(serviceJson.optString("resource"));
								serviceList.add(serviceInfo);
							}
							orderInfo.setServiceInfoList(serviceList);
						}
						JSONArray partnerArray = dataJson.optJSONArray("partner");
						if (partnerArray != null && partnerArray.length() > 0) {
							partnerInfos.clear();
							List<PartnerInfo> partnerList = new ArrayList<PartnerInfo>();
							for (int i = 0; i < partnerArray.length(); i++) {
								PartnerInfo partnerInfo = new PartnerInfo();
								JSONObject partnerJson = partnerArray.optJSONObject(i);
								partnerInfo.setId(partnerJson.optString("id"));
								partnerInfo.setIdNo(partnerJson.optString("idNo"));
								partnerInfo.setIdType(partnerJson.optString("idType"));
								partnerInfo.setRealName(partnerJson.optString("realName"));
								partnerInfo.setTel(partnerJson.optString("tel"));
								partnerInfo.setEmail(partnerJson.optString("email"));
								partnerInfo.setMyself(partnerJson.optString("myself"));
								if (partnerInfo.getMyself().equals("no")) {
									partnerList.add(partnerInfo);
								}
							}
							orderInfo.setPartnerInfoList(partnerList);
						}
						JSONArray serviceResourceArray = dataJson.optJSONArray("resource");
						if (serviceResourceArray != null && serviceResourceArray.length() > 0) {
							List<ServResrouce> servResrouces = new ArrayList<ServResrouce>();
							for (int i = 0; i < serviceResourceArray.length(); i++) {
								JSONObject serviceResourceJson = serviceResourceArray.optJSONObject(i);
								ServResrouce servResrouce = new ServResrouce();
								servResrouce.setServId(serviceResourceJson.optString("servId"));
								servResrouce.setServName(serviceResourceJson.optString("servName"));
								servResrouce.setServAlias(serviceResourceJson.optString("servAlias"));
								servResrouce.setServType(serviceResourceJson.optString("servType"));
								servResrouce.setRank(serviceResourceJson.optString("rank"));
								servResrouce.setTitleImage(serviceResourceJson.optString("titleImg"));
								servResrouce.setServDesc(serviceResourceJson.optString("servDesc"));
								servResrouces.add(servResrouce);
							}
							orderInfo.setServResrouces(servResrouces);
						}
						orderInfo.setTotalPrice(dataJson.optString("totalPrice"));
						// 旅程详情跟踪
						JSONArray traceArray = dataJson.optJSONArray("trace");
						if (traceArray != null) {
							List<TraceInfo> list = new ArrayList<TraceInfo>();
							for (int i = 0; i < traceArray.length(); i++) {
								JSONObject obj = traceArray.optJSONObject(i);
								TraceInfo info = new TraceInfo();
								info.setDesc(obj.optString("desc"));
								info.setTraceTime(obj.optString("traceTime"));
								info.setStatus(obj.optString("status"));
								list.add(info);
							}
							orderInfo.setTraceInfoList(list);
						}
						// 后续状态跟踪
						JSONArray dispatchInfosArray = dataJson.optJSONArray("dispatchInfos");
						if (dispatchInfosArray != null) {
							ArrayList<DispatchInfo> temp = new ArrayList<>();
							for (int i = 0; i < dispatchInfosArray.length(); i++) {
								JSONObject json = dispatchInfosArray.optJSONObject(i);
								DispatchInfo dispatchInfo = new DispatchInfo();
								dispatchInfo.setCreateTime(json.optString("createTime"));
								UserInfo userInfo = new UserInfo();
								userInfo.setRealName(json.optJSONObject("dispatchTo").optString("realName"));
								userInfo.setMobile(json.optJSONObject("dispatchTo").optString("mobile"));
								userInfo.setGender(json.optJSONObject("dispatchTo").optString("sex"));
								OrgRole orgRole = new OrgRole();
								orgRole.setRoleName(json.optJSONObject("dispatchTo").optJSONObject("orgRole")
										.optString("roleName"));
								userInfo.setOrgRole(orgRole);
								dispatchInfo.setDispatchTo(userInfo);
								temp.add(dispatchInfo);
							}
							orderInfo.setDispatchInfos(temp);
						}
						// 发票
						JSONObject invoice = dataJson.optJSONObject("invoice");
						if (invoice != null) {
							Invoice info = new Invoice();
							info.setInvoiceId(invoice.optString("invoiceId"));
							info.setAddress(invoice.optString("address"));
							info.setAlias(invoice.optString("alias"));
							info.setInvoiceTitle(invoice.optString("invoiceTitle"));
							info.setInvoiceType(invoice.optString("invoiceType"));
							info.setArea(invoice.optString("area"));
							info.setEmail(invoice.optString("email"));
							info.setTel(invoice.optString("tel"));
							info.setIsDefault(invoice.optString("isDefault"));
							info.setPostCode(invoice.optString("postCode"));
							info.setUserName(invoice.optString("userName"));
							orderInfo.setInvoice(info);
						}
						initData(orderInfo);
					}
				} else {
					// ToastUtil.showErrorToast(MyOrderDetailActivity.this,
					// "error=" + response.optString("code"));
				}
			}

		});
	}

	/**
	 * 退订
	 * 
	 * @param orderId
	 *            订单ID
	 * @param leaveWord
	 *            留言
	 */
	private void applyUnsubscribe(String orderId, String leaveWord) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("orderId", orderId);
			jsonObject.put("leaveWord", leaveWord);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.apply_chargeback_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					// ToastUtil.showToast(MyOrderDetailActivity.this,
					// "statusCode=" + statusCode + "-->" +
					// throwable.getStackTrace());
					throwable.printStackTrace();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					LogUtil.i("MyOrderDetailActivity", "退订接口=", response.toString());
					if (!response.optString("code").equals("00000")) {
						// ToastUtil.showToast(MyOrderDetailActivity.this,
						// "errorCode=" + response.optString("code"));
						return;
					} else {
						queryOrderDetail();
					}
				}

			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 申诉
	 */
	private void applyComplain() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("orderId", orderId);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.order_staff_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					// ToastUtil.showToast(MyOrderDetailActivity.this,
					// "statusCode=" + statusCode + "-->" +
					// throwable.getStackTrace());
					throwable.printStackTrace();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					LogUtil.i("MyOrderDetailActivity", "申诉接口=", response.toString());
					if (!response.optString("code").equals("00000")) {
						// ToastUtil.showToast(MyOrderDetailActivity.this,
						// "errorCode=" + response.optString("code"));
						return;
					}
					queryOrderDetail();
				}

			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Result result = new Result((String) msg.obj);
			if (msg.what == 0) {
				if (result.getResult().equals("9000")) {
					// queryOrderDetail();
					ToastUtil.showToast(MyOrderDetailActivity.this, "支付成功");
				} else if (result.getResult().equals("4000")) {
					ToastUtil.showToast(MyOrderDetailActivity.this, "系统异常");
				} else if (result.getResult().equals("4001")) {
					ToastUtil.showToast(MyOrderDetailActivity.this, "订单参数错误");
				} else if (result.getResult().equals("6001")) {
					ToastUtil.showToast(MyOrderDetailActivity.this, "用户取消支付");
				} else if (result.getResult().equals("6002")) {
					ToastUtil.showToast(MyOrderDetailActivity.this, "网络连接异常");
				} else {
					ToastUtil.showToast(MyOrderDetailActivity.this, "其他錯誤");
				}
				Topaysuccess(result.getResult());
			}
		}
	};

	private void Topaysuccess(String result) {
		Intent newIntent = new Intent(this, PaySuccessAcivity.class);
		newIntent.putExtra("orderNo", orderInfo.getNo());
		newIntent.putExtra("total", orderInfo.getTotalPrice());
		newIntent.putExtra("result", result);
		newIntent.putExtra("beginDate", orderInfo.getBeginDate());
		newIntent.putExtra("endDate", orderInfo.getEndDate());
		newIntent.putExtra("leaveEdt", orderInfo.getLeaveword());
		newIntent.putExtra("name", orderInfo.getProduct().getTitle());
		newIntent.putExtra("num", String.valueOf(orderInfo.getPartnerInfoList().size()));
		startActivity(newIntent);
		activityAnimationOpen();
		finish();
	}

	ApplyUnsubcribeListener unsubcribeListen = new ApplyUnsubcribeListener() {

		@Override
		public void onUnsubcribe(String leaveWord) {
			// TODO Auto-generated method stub
			applyUnsubscribe(orderId, leaveWord);
		}
	};

	private void evaProductComments(String content, int descScore, int serviceScore, int satisScore, String productId) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 评论内容
			jsonObject.put("content", content);
			// 产品id
			jsonObject.put("productId", productId);
			// 评分
			jsonObject.put("descScore", descScore);
			jsonObject.put("serviceScore", serviceScore);
			jsonObject.put("satisfactionScore", satisScore);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.product_detail_starcomment, stringEntity, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if ("00000".equals(code)) {
						ToastUtil.showToast(getApplication(), "评价成功");
					}
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cancelOrderByOrderId(String orderId) {

		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		HttpUtil.get(Urls.mytrip_cancel_url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				String code = response.optString("code");

				if ("00000".equals(code)) {
					queryOrderDetail();
					Intent it = new Intent("com.bcinfo.cancelOrder");
					sendBroadcast(it);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	private void popupComplainView(View v) {
		ComplainPopWindow popView = new ComplainPopWindow(MyOrderDetailActivity.this, new OperationListener() {

			@Override
			public void complain(String orderId) {
				Intent intent1 = new Intent(MyOrderDetailActivity.this, ConsultOrComplaintActivity.class);
				intent1.putExtra("type", "complaint");
				intent1.putExtra("referId", orderId);
				startActivity(intent1);
				activityAnimationOpen();
			}
		}, orderInfo.getId());
		popView.show(v);
	}

	private void delOrderById(String orderId) {
		// 评论内容
		HttpUtil.delete(Urls.order_delete + orderId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Intent it = new Intent("com.bcinfo.delMyOrder");
				sendBroadcast(it);
				finish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	// 生成订单获取订单号接口
	private void getOrederInfo(final MyOrder orderInfo) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("productId", orderInfo.getProduct().getId());
			jsonObject.put("beginTime", orderInfo.getBeginDate());
			jsonObject.put("endTime", orderInfo.getEndDate());
			jsonObject.put("leaveWord", StringUtils.isEmpty(orderInfo.getLeaveword()) ? "" : orderInfo.getLeaveword());
			jsonObject.put("partnerNum", partnerInfos.size());
			JSONArray partnerArray = new JSONArray();
			// String[] arrays = new String[partnerInfos.size() + 1];
			for (int i = 0; i < partnerInfos.size(); i++) {
				partnerArray.put(partnerInfos.get(i).getId() + "");
			}
			jsonObject.put("partnerIds", partnerArray);
			if (paytype == 0) {
				// 全额
				jsonObject.put("paymentType", "0");
			} else if (paytype == 1) {
				// 定金
				jsonObject.put("paymentType", "1");
			} else if (paytype == 2) {
				// 定金
				jsonObject.put("paymentType", "2");
			}
			// 旧版本 目前全款 0 定金 1（暂无）
			// 过期订单
			jsonObject.put("orderNo", StringUtils.isEmpty(orderInfo.getNo()) ? "" : orderInfo.getNo());
			jsonObject.put("tel", orderInfo.getTel());
			// 是否使用发票
			if (orderInfo.getInvoice() != null) {
				String invoiceId = orderInfo.getInvoice().getInvoiceId();
				if (!StringUtils.isEmpty(invoiceId)) {
					jsonObject.put("invoiceId", invoiceId);
				} else {
					jsonObject.put("invoiceId", "");
				}
			} else {
				jsonObject.put("invoiceId", "");
			}
			jsonObject.put("urgentContactNum", emergency_contact_num.getText().toString());
			jsonObject.put("urgentContactPerson", emergency_contact_name.getText().toString());
			jsonObject.put("contactName", contact_name.getText().toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.apply_orderInfo_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					throwable.printStackTrace();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, throwable, errorResponse);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					System.out.println("生成订单接口=" + response);
					if (!response.optString("code").equals("00000")) {
						// ToastUtil.showToast(ConfirmPayActivity3.this,
						// "error=" + response.optString("code"));
						return;
					} else {
						try {
							JSONObject dataJson = response.getJSONObject("data");
							if (dataJson != null && !dataJson.equals("") && !dataJson.equals("null")) {
								final String orderNo = dataJson.optString("orderNo");
								final String total = dataJson.optString("total");
								// final String total = "0.01";
								if (StringUtils.isEmpty(total)) {
									return;
								}
								// 去支付

								if ("apply_success".equals(orderInfo.getStatus()) && paytype == 2) {
									Intent intent2 = new Intent(MyOrderDetailActivity.this, ContentActivity.class);
									intent2.putExtra("path", Urls.pay + orderNo);
									intent2.putExtra("title", "首付游");
									MyOrderDetailActivity.this.startActivity(intent2);
								} else {
									new Thread() {
										public void run() {

											String orderInfoStr = AlipayUtile.getNewOrderInfo(orderNo,
													orderInfo.getProduct().getTitle(), total);
											// 构造PayTask 对象
											PayTask alipay = new PayTask(MyOrderDetailActivity.this);
											// 调用支付接口
											String result = alipay.pay(orderInfoStr);

											Log.i("MyOrderDetailActivity", "result = " + result);
											Message msg = new Message();
											msg.what = 0;
											msg.obj = result;
											mHandler.sendMessage(msg);
										}
									}.start();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void joinPrivateChat() {
		if (!AppInfo.getIsLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("referType", "user");
		params.put("referId", orderInfo.getProduct().getUser().getUserId());
		HttpUtil.get(Urls.join_private_chat, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if (!"00000".equals(code)) {
					// Intent intent = new
					// Intent(GrouponProductNewDetailActivity.this,
					// LoginActivity.class);
					// startActivity(intent);
					return;
				}
				String queueId = response.optString("data");
				// if (!StringUtils.isEmpty(queueId)) {
				// // 查询队列详情并跳转
				// queryPrivateChatInfo(queueId);
				Intent talkIntent = new Intent(MyOrderDetailActivity.this, ChatActivity.class);
				talkIntent.putExtra("queueId", queueId);
				talkIntent.putExtra("receiveId", orderInfo.getProduct().getUser().getUserId());
				talkIntent.putExtra("title", orderInfo.getProduct().getUser().getNickname());
				talkIntent.putExtra("isTeam", false);
				talkIntent.putExtra("fromQueue", true);
				startActivity(talkIntent);
				activityAnimationOpen();
				// }

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	// private void joinPrivateChat(String productId) {
	// if (!AppInfo.getIsLogin()) {
	// Intent intent = new Intent(this, LoginActivity.class);
	// startActivity(intent);
	// return;
	// }
	// RequestParams params = new RequestParams();
	// params.put("referType", "product");
	// params.put("referId", productId);
	// HttpUtil.get(Urls.join_private_chat, params, new
	// JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(int statusCode, Header[] headers, JSONObject
	// response) {
	// super.onSuccess(statusCode, headers, response);
	// String code = response.optString("code");
	// if (!"00000".equals(code)) {
	// // Intent intent = new
	// // Intent(GrouponProductNewDetailActivity.this,
	// // LoginActivity.class);
	// // startActivity(intent);
	// return;
	// }
	// String queueId = response.optString("data");
	// if (!StringUtils.isEmpty(queueId)) {
	// // 查询队列详情并跳转
	// queryPrivateChatInfo(queueId);
	// }
	//
	// }
	//
	// @Override
	// public void onFailure(int statusCode, Header[] headers, String
	// responseString, Throwable throwable) {
	// super.onFailure(statusCode, headers, responseString, throwable);
	// }
	// });
	// }

	private void queryPrivateChatInfo(final String queueId) {
		RequestParams params = new RequestParams();
		params.put("queueId", queueId);
		params.put("pagenum", 1);// 当前页码
		params.put("pagesize", 10);// 页码数
		params.put("msgId", "");
		HttpUtil.get(Urls.message_queue_url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if (!"00000".equals(code)) {
					Intent intent = new Intent(MyOrderDetailActivity.this, LoginActivity.class);
					startActivity(intent);
					return;
				}
				JSONObject data = response.optJSONObject("data");
				JSONObject product = data.optJSONObject("product");
				Intent intent = new Intent(MyOrderDetailActivity.this, ChatActivity.class);
				intent.putExtra("queueId", queueId);
				// intent.putExtra("pattern", "chat");
				intent.putExtra("fromQueue", true);
				intent.putExtra("isTeam", false);
				intent.putExtra("receiveId", orderInfo.getProduct().getUser().getUserId());
				intent.putExtra("title", orderInfo.getProduct().getUser().getNickname());
				startActivity(intent);
				activityAnimationOpen();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}
}
