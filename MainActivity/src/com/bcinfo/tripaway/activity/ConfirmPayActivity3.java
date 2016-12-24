package com.bcinfo.tripaway.activity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AssociateAdapter;
import com.bcinfo.tripaway.adapter.AssociateAdapter.OnDelPartnerItemListener;
import com.bcinfo.tripaway.adapter.ProductDateAdapter;
import com.bcinfo.tripaway.bean.CashOrder;
import com.bcinfo.tripaway.bean.Invoice;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.IDCardUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.utils.alipay.AlipayUtile;
import com.bcinfo.tripaway.utils.alipay.Result;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.date.DayPicker;
import com.bcinfo.tripaway.view.dialog.ApplyExitDialog;
import com.bcinfo.tripaway.view.radiogroup.MyRadioGroup;
import com.bcinfo.tripaway.view.radiogroup.MyRadioGroup.OnCheckedChangeListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * [申请预订]的界面
 * 
 * @author hanweipeng
 * @date 2015-7-7 下午8:08:39
 */
public class ConfirmPayActivity3 extends BaseActivity
		implements OnClickListener, OnItemClickListener, OnCheckedChangeListener {
	/**
	 * 单产品选择时间布局
	 */
	private LinearLayout timeLayout;

	/**
	 * 开始时间
	 */
	private TextView beginTimeTxt;

	/**
	 * 结束时间
	 */
	private TextView endTimeTxt;

	/**
	 * 团产品选择时间布局
	 */
	private MyListView timeLst;

	/**
	 * 自己的姓名
	 */
	// private TextView userNameEdt;

	/**
	 * 自己的证件号
	 */
	// private TextView userNumberEdt;

	// private TextView memberCount;
	/**
	 * 添加同行人
	 */
	private ImageView addassociation;
	private RelativeLayout addassociate;

	/**
	 * 同行人列表
	 */
	private MyListView memberLst;

	/**
	 * 添加同行人按钮
	 */
	// private LinearLayout addMemberBtn;

	/**
	 * 产品价格
	 */
	private TextView productPriceTxt;

	/**
	 * 总价格
	 */
	private TextView totalPriceTxt;

	/**
	 * 留言标题
	 */
	private TextView leaveTitleTxt;

	/**
	 * 留言输入框
	 */
	private EditText leaveEdt;

	private ProductNewInfo productInfo;

	/**
	 * 日期选择时间（开始）对话框
	 */
	private AlertDialog begindialog;

	/**
	 * 日期选择时间（结束）对话框
	 * 
	 */
	private AlertDialog endDialog;

	/**
	 * 日期选择时间
	 */
	private DayPicker mdayPicker;

	/**
	 * 随行人列表
	 */
	private List<PartnerInfo> partnerInfos = new ArrayList<PartnerInfo>();

	/**
	 * 随行人适配器
	 */
	// private PartnerMemberAdapter memberAdapter;

	/**
	 * 同行人适配器
	 */
	private AssociateAdapter associateAdapter;

	private ProductDateAdapter dateAdapter;

	// private TextView countTxt;

	private int selectPosition = -1;

	/**
	 * 开始时间
	 */
	private String beginDate = "";

	/**
	 * 结束时间
	 */
	private String endDate = "";

	private final static String TAG = "ConfirmPayActivity3";

	private String orderNo;

	private String total;

	private String priceByDate;

	// 总价格
	private String totalPrice;

	// 总定金
	private String earnestMoney;

	private String pay_sfy_sf_price;
	/**
	 * 订购按钮
	 */
	private Button confirmPayBtn;

	/**
	 * 等级状态
	 */
	// private TextView levelTxt;
	private LinearLayout layout_back_button;
	private RelativeLayout choose_time;
	private TextView time_go;
	private TextView time_start_go;
	String time_back_go;
	private List<String> lists;
	String[] s;
	private List<String> lists2;
	String time;

	List<String> start_times;
	List<String> end_times;

	private ArrayList<String> priceList = new ArrayList<String>();
	private ApplyExitDialog exitDialog;

	// add by lij 2015/11/04 start
	private MyRadioGroup mMyRadioGroup;

	/**
	 * 定金
	 */
	private TextView mPayEarnestMoneyTv;

	/**
	 * 支付全款
	 */
	// private TextView mPayTotalMoneyTv;

	private TextView mTouristCountTv;

	/**
	 * 付款金额
	 */
	private TextView mMoneyTypeTv;

	/**
	 * 尾款
	 */
	private TextView pay_earnest_tv_end;

	private TextView mMoneyTypeNameTv;

	private CheckBox mIsAgreePolicy;

	private TextView mServerPolicy;

	// 支付方式，0-全款，1-定金，2-分期付
	private int paymentType;

	private TextView mInvoiceTv;

	/**
	 * 删除发票
	 */
	private ImageView deleteImage;

	private LinearLayout mImgInvoiceIv;
	/**
	 * 添加同行人
	 */
	private LinearLayout add_associate;

	private Invoice invoice;

	/**
	 * 联系人手机号
	 */
	// private EditText mTelEv;

	// private EditText mIdNoEv;

	private UserInfo getUserInfo;

	/**
	 * 出行游客人数
	 */
	private TextView tourists_num;
	/**
	 * 联系人
	 */
	private EditText order_name;
	/**
	 * 联系人手机号码
	 */
	private EditText order_tel;
	/**
	 * 紧急联系人
	 */
	private EditText emergency_order_name;
	/**
	 * 紧急联系人手机号码
	 */
	private EditText emergency_order_tel;

	private TextView productCode;

	private TextView productTitle;

	/**
	 * 支付订金界面显示
	 */
	private RelativeLayout payDepositRelative;
	/**
	 * 间隔线
	 */
	private TextView linespace;
	/**
	 * 优惠价格
	 */
	/**
	 * 市场价格
	 */
	private TextView oPrice;

	RelativeLayout layout_originalPrice;

	protected List<CashOrder> cashLists = new ArrayList<>();
	// private TextView actual_little_price;

	private ImageView cashLine;
	private ImageView cashLine2;
	private ImageView cashLine3;

	private TextView can_used;

	private TextView cash_num;

	private TextView favorablePrice;

	private TextView favorablePrice2;
	private TextView youhui;
	private TextView youhui2;

	private RelativeLayout cash_ll;

	private RelativeLayout layout_favorablePrice;

	private RelativeLayout layout_favorablePrice2;

	private List<CashOrder> seclist = new ArrayList<CashOrder>();

	DecimalFormat decimalFormat = new DecimalFormat("0.0");
	// 首付游
	private TextView linespace2;
	private RelativeLayout payPeriodizationRelative;
	private RadioButton pay_periodization;
	private RadioButton pay_totalMoney;
	private TextView periodization_text;
	private TextView tips;
	private TextView tourists_num_tips;

	// add by lij 2015/11/04 end
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.confirm_pay_layout3);
		statisticsTitle = "订购详情";
		layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		layout_back_button.getBackground().setAlpha(255);
		setSecondTitle("订购详情");
		RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.second_title);

		titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
		titleLayout.getBackground().setAlpha(255);
		initView();
		// getApplyPrice(productInfo.getId(), beginDate, endDate, 1);
		// getApplyPrice(productInfo.getId(), "2015-10-14", time_back_go, 1);
	}

	protected void initView() {
		tourists_num = (TextView) findViewById(R.id.tourists_num);
		order_name = (EditText) findViewById(R.id.order_name);
		order_tel = (EditText) findViewById(R.id.order_tel);
		emergency_order_name = (EditText) findViewById(R.id.emergency_order_name);
		emergency_order_tel = (EditText) findViewById(R.id.emergency_order_tel);
		deleteImage = (ImageView) findViewById(R.id.deleteImage);
		deleteImage.setOnClickListener(this);
		pay_earnest_tv_end = (TextView) findViewById(R.id.pay_earnest_tv_end);
		// actual_little_price = (TextView)
		// findViewById(R.id.actual_little_price);
		// mIdNoEv = (EditText) findViewById(R.id.edit_idno);
		// mTelEv = (EditText) findViewById(R.id.order_tel);
		choose_time = (RelativeLayout) findViewById(R.id.choose_time);
		time_go = (TextView) findViewById(R.id.time_go);
		time_start_go = (TextView) findViewById(R.id.time_start_go);
		choose_time.setOnClickListener(this);
		priceList = getIntent().getStringArrayListExtra("priceList");
		productInfo = getIntent().getParcelableExtra("productInfo");
		payDepositRelative = (RelativeLayout) findViewById(R.id.payDepositRelative);
		linespace = (TextView) findViewById(R.id.linespace);
		if (productInfo.getHasDeposit().equals("no")) {
			payDepositRelative.setVisibility(View.GONE);
			linespace.setVisibility(View.GONE);
		} else {
			payDepositRelative.setVisibility(View.VISIBLE);
			linespace.setVisibility(View.VISIBLE);
		}
		timeLayout = (LinearLayout) findViewById(R.id.comfire_time_lin);
		beginTimeTxt = (TextView) findViewById(R.id.comfire_date_begin);
		endTimeTxt = (TextView) findViewById(R.id.comfire_date_end);
		timeLst = (MyListView) findViewById(R.id.time_listview);
		// userNameEdt = (TextView) findViewById(R.id.user_name);
		// userNumberEdt = (TextView) findViewById(R.id.user_number);
		memberLst = (MyListView) findViewById(R.id.member_listview);
		// addMemberBtn = (LinearLayout) findViewById(R.id.add_member);
		productPriceTxt = (TextView) findViewById(R.id.pay_total_tv_total);
		oPrice = (TextView) findViewById(R.id.originalPrice);
		totalPriceTxt = (TextView) findViewById(R.id.pay_total_tv);
		// totalPriceTxt.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
		leaveTitleTxt = (TextView) findViewById(R.id.leave_title);
		// levelTxt = (TextView) findViewById(R.id.level);
		leaveEdt = (EditText) findViewById(R.id.leave_edt);
		// countTxt = (TextView)findViewById(R.id.member_count);
		confirmPayBtn = (Button) findViewById(R.id.comfir_pay_button);
		confirmPayBtn.setOnClickListener(this);
		beginTimeTxt.setOnClickListener(this);
		endTimeTxt.setOnClickListener(this);
		timeLst.setOnItemClickListener(this);
		// memberLst.setOnItemClickListener(this);
		// addMemberBtn.setOnClickListener(this);
		mMyRadioGroup = (MyRadioGroup) findViewById(R.id.pay_radiogroup);
		mMyRadioGroup.setOnCheckedChangeListener(this);
		mPayEarnestMoneyTv = (TextView) findViewById(R.id.pay_earnest_tv);
		// mPayTotalMoneyTv = (TextView) findViewById(R.id.pay_total_tv);
		mTouristCountTv = (TextView) findViewById(R.id.tourist_count);
		mMoneyTypeNameTv = (TextView) findViewById(R.id.pay_moneys_type_tv);
		mMoneyTypeTv = (TextView) findViewById(R.id.pay_moneys_tv);
		mIsAgreePolicy = (CheckBox) findViewById(R.id.isAgreePolicy);
		mServerPolicy = (TextView) findViewById(R.id.server_policy);
		mServerPolicy.setOnClickListener(this);
		mServerPolicy.setText(productInfo.getUser().getNickname() + "的服务协议");
		mServerPolicy.setTag(productInfo.getUser().getServerPolicy());

		// 首付游
		// 默认选择全款
		pay_totalMoney = (RadioButton) findViewById(R.id.pay_totalMoney);
		linespace2 = (TextView) findViewById(R.id.linespace2);
		payPeriodizationRelative = (RelativeLayout) findViewById(R.id.payPeriodizationRelative);
		pay_periodization = (RadioButton) findViewById(R.id.pay_periodization);
		periodization_text = (TextView) findViewById(R.id.periodization_text);
		tips = (TextView) findViewById(R.id.tips);
		tourists_num_tips = (TextView) findViewById(R.id.tourists_num_tips);
		if (!StringUtils.isEmpty(productInfo.getPrice())) {
			if (Integer.parseInt(productInfo.getPrice()) <= 24000) {
				tourists_num_tips.setVisibility(View.VISIBLE);
				tourists_num_tips.setText("游客数不超过2人，且预定30天之后的行程，可享用分期付款服务");
			} else {
				tourists_num_tips.setVisibility(View.GONE);
			}
		}
		// 取消合同显示 2016.6.3徐总要求
		// if (!StringUtils.isEmpty(productInfo.getUser().getServerPolicy())) {
		// LinearLayout linearLayout = (LinearLayout)
		// mIsAgreePolicy.getParent();
		// linearLayout.setVisibility(View.VISIBLE);
		// } else {
		// LinearLayout linearLayout = (LinearLayout)
		// mIsAgreePolicy.getParent();
		// linearLayout.setVisibility(View.GONE);
		// }
		LogUtil.i("我想要的KEY", productInfo.getUser().getServerPolicy(), productInfo.getUser().getServerPolicy());

		mInvoiceTv = (TextView) findViewById(R.id.my_invoice);
		mInvoiceTv.setOnClickListener(this);
		mInvoiceTv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String txt = mInvoiceTv.getText().toString();
				if (StringUtils.isEmpty(txt)) {
					deleteImage.setVisibility(View.GONE);
				} else {
					deleteImage.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		mImgInvoiceIv = (LinearLayout) findViewById(R.id.goto_invoice);
		// mInvoiceTv.setOnClickListener(this);
		mImgInvoiceIv.setOnClickListener(this);

		layout_originalPrice = (RelativeLayout) findViewById(R.id.layout_originalPrice);
		// 出发的天数=================================================
		// time_go.setText(productInfo.getDays()+"天");
		//
		// s
		// Intent intent = getIntent();
		// lists = intent.getExtras().getStringArrayList("tour_dates");
		// s = new String[lists.size()];
		// for (int i = 0; i < lists.size(); i++)
		// {
		// s[i] = lists.get(i);
		// System.out.println("ppppp"+lists.get(i));
		// }
		// =================================================

		if (productInfo.getProductType().equals("team")) {
			timeLayout.setVisibility(View.GONE);
			timeLst.setVisibility(View.GONE);
			dateAdapter = new ProductDateAdapter(ConfirmPayActivity3.this, productInfo.getExpPeriodList(),
					Integer.parseInt(productInfo.getDays()));
			timeLst.setAdapter(dateAdapter);
		} else {
			beginDate = DateUtil.getCurrentDateStr().replaceAll("/", "");
			beginTimeTxt.setText(DateUtil.getCurrentDateStr());
			if (productInfo.getProductType().equals("base")) {
				endDate = DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays()))
						.replaceAll("/", "");
				endTimeTxt.setText(DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays())));
			}
			timeLayout.setVisibility(View.GONE);
			timeLst.setVisibility(View.GONE);
		}
		getUserInfo = UserInfoDB.getInstance().queryUserInfoById(PreferenceUtil.getUserId());
		if (getUserInfo != null) {
			// 订购本人的姓名和身份证号
			// PartnerInfo partnerInfo = new PartnerInfo();
			// partnerInfo.setRealName(StringUtils.isEmpty(getUserInfo.getRealName())?getUserInfo.getNickname():getUserInfo.getRealName());
			// partnerInfo.setIdNo(StringUtils.isEmpty(getUserInfo.getUsersIdentity())?"":getUserInfo.getUsersIdentity());
			// partnerInfos.add(partnerInfo);
			// if (!StringUtils.isEmpty(getUserInfo.getRealName()))
			// userNameEdt.setText(getUserInfo.getRealName());
			// else
			// userNameEdt.setText(getUserInfo.getNickname());
			// userNameEdt.setText(getUserInfo.getRealName());
			// if (StringUtils.isEmpty(getUserInfo.getUsersIdentity())) {
			// userNumberEdt.setVisibility(View.GONE);
			// // mIdNoEv.setVisibility(View.VISIBLE);
			// } else {
			// userNumberEdt.setVisibility(View.VISIBLE);
			// // mIdNoEv.setVisibility(View.GONE);
			// userNumberEdt.setText(getUserInfo.getUsersIdentity());
			// }
		}
		// mIdNoEv.setTag(getUserInfo);
		// mIdNoEv.setOnEditorActionListener(new OnEditorActionListener() {
		//
		// @Override
		// public boolean onEditorAction(TextView v, int actionId, KeyEvent
		// event) {
		// if(EditorInfo.IME_ACTION_NEXT == actionId||EditorInfo.IME_ACTION_DONE
		// == actionId||EditorInfo.IME_ACTION_GO == actionId){
		// if(!StringUtils.isEmpty(v.getText().toString())){
		// userNumberEdt.setVisibility(View.VISIBLE);
		// userNumberEdt.setText(v.getText().toString());
		// mIdNoEv.setVisibility(View.GONE);
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// if (imm != null) {
		// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		// }
		// // 更新 用户信息
		// UserInfo info =(UserInfo) mIdNoEv.getTag();
		// info.setUsersIdentity(v.getText().toString());
		// userInfoEdit(info);
		// return true;
		// }
		// }
		// return false;
		// }
		// });

		// if (productInfo.getLevel().equals("super")) {
		// levelTxt.setText("非常严格");
		// } else if (productInfo.getLevel().equals("high") ||
		// productInfo.getLevel().contains("high")) {
		// levelTxt.setText("严格");
		// } else if (productInfo.getLevel().equals("middle")) {
		// levelTxt.setText("适中");
		// } else if (productInfo.getLevel().equals("low")) {
		// levelTxt.setText("灵活");
		// }
		associateAdapter = new AssociateAdapter(this, partnerInfos, new OnDelPartnerItemListener() {

			@Override
			public void deletePartner(int position) {
				partnerInfos.remove(position);
				associateAdapter.notifyDataSetChanged();
				if (time != null && time.length() >= 10)
					getApplyPrice(productInfo.getId(), time.substring(0, 10).replace("-", ""),
							time_back_go.replaceAll("-", ""), partnerInfos.size());
				tourists_num.setText("出行游客 (" + partnerInfos.size() + "人 )");
			}
		});
		add_associate = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.add_associate_linearlayout, null);
		memberLst.addFooterView(add_associate);
		addassociate = (RelativeLayout) add_associate.findViewById(R.id.addassociate);
		addassociation = (ImageView) add_associate.findViewById(R.id.addassociation);
		addassociate.setOnClickListener(this);
		addassociation.setOnClickListener(this);
		memberLst.setAdapter(associateAdapter);
		add_associate.setOnClickListener(this);
		associateAdapter.notifyDataSetChanged();
		// memberAdapter = new PartnerMemberAdapter(ConfirmPayActivity3.this,
		// partnerInfos,new OnDelPartnerItemListener() {
		//
		// @Override
		// public void deletePartner(int position) {
		// partnerInfos.remove(position);
		// memberAdapter.notifyDataSetChanged();
		// getApplyPrice(productInfo.getId(), time, time_back_go,
		// partnerInfos.size());
		// }
		// });
		// memberLst.setAdapter(memberAdapter);
		// countTxt.setText("(" +(partnerInfos.size())+ "/" +
		// productInfo.getMaxMember() + ")");
		leaveTitleTxt.setText("向" + productInfo.getUser().getNickname() + "打个招呼吧");
		float price = 0;
		if (!StringUtils.isEmpty(productInfo.getPrice())) {
			price = Float.parseFloat(productInfo.getPrice());
		}
		float price2 = 0;
		if (!StringUtils.isEmpty(productInfo.getOriginalPrice())) {
			price2 = Float.parseFloat(productInfo.getOriginalPrice());
		}
		if (!StringUtils.isEmpty(productInfo.getOriginalPrice()) && "0".equals(productInfo.getOriginalPrice()) == false
				&& price < price2) {
			layout_originalPrice.setVisibility(View.VISIBLE);
			oPrice.setText("￥" + productInfo.getOriginalPrice());
		}
		if (!StringUtils.isEmpty(productInfo.getPrice())) {
			productPriceTxt.setText("( ￥" + productInfo.getPrice() + "/人x" + 0 + " )");
			totalPriceTxt.setText("￥0.00");
			mTouristCountTv.setText((partnerInfos.size()) + "");
			totalPrice = "0.00";
			earnestMoney = "0.00";
			mMoneyTypeTv.setText("￥" + totalPrice);
		}
		productCode = (TextView) findViewById(R.id.productcode);
		productTitle = (TextView) findViewById(R.id.productTitle);
		if (!StringUtils.isEmpty(productInfo.getProductCode())) {
			productCode.setText("产品编号：" + productInfo.getProductCode());
		}
		if (!StringUtils.isEmpty(productInfo.getTitle())) {
			productTitle.setText(productInfo.getTitle());
		}

		cashLine = (ImageView) findViewById(R.id.cashLine);
		cashLine2 = (ImageView) findViewById(R.id.cashLine2);
		cashLine3 = (ImageView) findViewById(R.id.cashLine3);
		can_used = (TextView) findViewById(R.id.can_used);
		cash_num = (TextView) findViewById(R.id.cash_num);
		youhui = (TextView) findViewById(R.id.youhui);
		youhui2 = (TextView) findViewById(R.id.youhui2);
		favorablePrice = (TextView) findViewById(R.id.favorablePrice);
		favorablePrice2 = (TextView) findViewById(R.id.favorablePrice2);
		layout_favorablePrice = (RelativeLayout) findViewById(R.id.layout_favorablePrice);
		layout_favorablePrice2 = (RelativeLayout) findViewById(R.id.layout_favorablePrice2);
		cash_ll = (RelativeLayout) findViewById(R.id.cash_ll);
		cash_ll.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		time_go.setText(productInfo.getDays() + "天");

		Intent intent = getIntent();
		lists = intent.getExtras().getStringArrayList("tour_dates");
		s = new String[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			s[i] = lists.get(i);
			System.out.println("ppppp" + lists.get(i));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 请选择出发日期
		case R.id.choose_time:
			Intent intent_chooseTime = new Intent(ConfirmPayActivity3.this, InGroupDataDetail.class);
			lists2 = new ArrayList<String>();
			for (int i = 0; i < s.length; i++) {
				lists2.add(s[i]);
			}
			intent_chooseTime.putStringArrayListExtra("tour_dates", (ArrayList<String>) lists2);
			intent_chooseTime.putStringArrayListExtra("priceList", priceList);
			startActivityForResult(intent_chooseTime, 2015);
			activityAnimationOpen();
			break;

		// 支付界面
		case R.id.comfir_pay_button:
			if (!mIsAgreePolicy.isChecked()) {
				ToastUtil.showToast(getApplication(), "请同意服务协议!");
				return;
			}
			if (time_start_go.getText().toString().equals("请选择出发日期")) {
				// ToastUtil.showToast(ConfirmPayActivity3.this, "请选择开始时间");
				// return;
				AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPayActivity3.this);
				builder.setTitle("提示信息");
				builder.setMessage("请选择出发日期");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}

				});
				builder.show();
				return;

			}
			// if (StringUtils.isEmpty(userNameEdt.getText().toString())
			// || StringUtils.isEmpty(userNumberEdt.getText().toString())) {
			// // ToastUtil.showToast(ConfirmPayActivity3.this, "请将自己的信息填写完整");
			// // return;
			//
			// AlertDialog.Builder builder = new
			// AlertDialog.Builder(ConfirmPayActivity3.this);
			// builder.setTitle("提示信息");
			// builder.setMessage("请在设置页面,填写个人信息,再订购产品");
			// builder.setPositiveButton("确定", new
			// DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			// return;
			// }
			//
			// });
			// builder.show();
			// return;
			// }
			if (partnerInfos == null || partnerInfos.size() <= 0) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "请添加同行人");
				return;
			}
			if (StringUtils.isEmpty(order_name.getText().toString())) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "联系人必填");
				return;
			}
			if (StringUtils.isChinese(order_name.getText().toString()) == false
					|| order_name.getText().toString().length() < 2) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "请输入正确的联系人中文姓名");
				return;
			}
			if (StringUtils.isEmpty(order_tel.getText().toString())) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "联系人手机号码必填");
				return;
			}

			if (!StringUtils.isPhone(order_tel.getText().toString())) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "请输入正确的联系人手机号码");
				return;
			}

			for (int i = 0; i < partnerInfos.size(); i++) {
				if (!IDCardUtil.isIdCard(partnerInfos.get(i).getIdNo())) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "出行人身份证号格式不对");
					return;
				}
				if (StringUtils.isChinese(partnerInfos.get(i).getRealName()) == false
						|| partnerInfos.get(i).getRealName().length() < 2) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "请输入正确的出行人中文姓名");
					return;
				}
			}
			if (StringUtils.isEmpty(emergency_order_name.getText().toString())) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "紧急联系人必填");
				return;
			}
			if (StringUtils.isChinese(emergency_order_name.getText().toString()) == false
					|| emergency_order_name.getText().toString().length() < 2) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "请输入正确的紧急联系人中文姓名");
				return;
			}
			if (StringUtils.isEmpty(emergency_order_tel.getText().toString())) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "紧急联系人手机号码必填");
				return;
			}
			if (!StringUtils.isPhone(emergency_order_tel.getText().toString())) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "请输入正确的紧急联系人手机号码");
				return;
			}
			if ((emergency_order_tel.getText().toString()).equals(order_tel.getText().toString())) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "紧急联系人手机号码与联系人号码不可以一致");
				return;
			}

			// 提交订单
			getOrederInfo(productInfo.getId(), time.substring(0, 10).replaceAll("-", ""),
					time_back_go.replaceAll("-", ""), leaveEdt.getText().toString());
			break;
		case R.id.comfire_date_begin:
			dayPickerDialog();
			break;
		case R.id.comfire_date_end:
			dayEndPickerDialog();
			break;
		// case R.id.add_member:
		case R.id.addassociate:
			// case R.id.add_associate_linearlayout:
			// if(StringUtils.isEmpty(priceByDate)){
			// ToastUtil.showToast(ConfirmPayActivity3.this, "请选择出行日期");
			// return;
			// }
			// 默认非必填最大值出行人数为0 取无限制
			if (!"0".equals(productInfo.getMaxMember())) {
				if (Integer.parseInt(productInfo.getMaxMember()) <= (partnerInfos.size())) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "随行人已达到最大上限数");
					return;
				}
			}
			Intent intent = new Intent(this, MyPartenerAddActivity.class);
			intent.putExtra("flag", false);
			startActivityForResult(intent, 102);
			// Intent intent = new Intent(ConfirmPayActivity3.this,
			// MyInvoiceActivity.class);
			// intent.putExtra("isOrderFrom", true);
			// startActivityForResult(intent, 101);
			break;
		case R.id.ok_button_date: {
			Calendar calendar = mdayPicker.getCalendar();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String monthStr = (month + 1) + "";
			String dayStr = day + "";
			if (month < 9) {
				monthStr = "0" + (month + 1);
			}
			if (day < 10) {
				dayStr = "0" + day;
			}
			if (DateUtil.getcurrentTimeMillis() > DateUtil.getTimeInMillis(year + "" + monthStr + "" + dayStr)) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "选择的开始时间不能早于当前时间");
				return;
			}
			if (!productInfo.getProductType().equals("base")) {
				if (StringUtils.isEmpty(endDate)
						&& !DateUtil.compareTime(year + "" + monthStr + "" + dayStr, endDate)) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "选择的开始时间不能晚于结束时间");
					return;
				}
			}
			// if (!StringUtils.isEmpty(endDate))
			// {
			// // 标准产品受时间限制 单产品是根据天数计价的
			// if (productInfo.getProductType().equals("base")
			// && DateUtil.isBeyondMaxDay(year + "" + monthStr + "" + dayStr,
			// endDate,
			// Integer.parseInt(productInfo.getDays())))
			// {
			// ToastUtil.showToast(ConfirmPayActivity.this, "选择的天数不符合产品的周期");
			// return;
			// }
			// }
			beginDate = year + "" + monthStr + "" + dayStr;
			if (productInfo.getProductType().equals("base")) {
				endDate = DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays()))
						.replaceAll("/", "");
				endTimeTxt.setText(DateUtil.getProductEndDateStr(beginDate, Integer.parseInt(productInfo.getDays())));
			}
			String notify = year + "/" + (month + 1) + "/" + day;
			beginTimeTxt.setText(notify);
			beginTimeTxt.setTextColor(getResources().getColor(R.color.dark_gray));
			begindialog.cancel();
			// if (partnerInfos.size() == 0)
			// {
			// getApplyPrice(productInfo.getId(), beginDate, endDate, 1);
			// }
			// else
			// {
			// getApplyPrice(productInfo.getId(), beginDate, endDate,
			// partnerInfos.size() + 1);
			// }
			getApplyPrice(productInfo.getId(), time.substring(0, 10).replace("-", ""), time_back_go.replaceAll("-", ""),
					partnerInfos.size());
		}
			break;
		case R.id.ok_button_enddate: {
			Calendar calendar = mdayPicker.getCalendar();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String monthStr = (month + 1) + "";
			String dayStr = day + "";
			if (month < 9) {
				monthStr = "0" + (month + 1);
			}
			if (day < 10) {
				dayStr = "0" + day;
			}
			if (DateUtil.getcurrentTimeMillis() > DateUtil.getTimeInMillis(year + "" + monthStr + "" + dayStr)) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "选择的结束时间不能早于当前时间");
				return;
			}
			// 标准产品受时间限制 单产品是根据天数计价的
			if (productInfo.getProductType().equals("base") && DateUtil.isBeyondMaxDay(beginDate,
					year + "" + monthStr + "" + dayStr, Integer.parseInt(productInfo.getDays()))) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "选择的天数不符合产品的周期");
				return;
			}
			if (!productInfo.getProductType().equals("base")) {
				if (!DateUtil.compareTime(beginDate, year + "" + monthStr + "" + dayStr)) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "选择的结束时间不能早于开始时间");
					return;
				}
			}
			endDate = year + "" + monthStr + "" + dayStr;
			String notify = year + "/" + (month + 1) + "/" + day;
			endTimeTxt.setText(notify);
			endTimeTxt.setTextColor(getResources().getColor(R.color.dark_gray));
			endDialog.cancel();
			// if (partnerInfos.size() == 0)
			// {
			// getApplyPrice(productInfo.getId(), beginDate, endDate, 1);
			// }
			// else
			// {
			// getApplyPrice(productInfo.getId(), beginDate, endDate,
			// partnerInfos.size() + 1);
			// }
			getApplyPrice(productInfo.getId(), time.substring(0, 10).replace("-", ""), time_back_go.replaceAll("-", ""),
					partnerInfos.size());
		}
			break;
		case R.id.server_policy:

			String url = (String) v.getTag();
			if (!StringUtils.isEmpty(url)) {
				// intent = new Intent("android.intent.action.VIEW");
				intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				// intent.addCategory("android.intent.category.DEFAULT");

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				Uri uri = Uri.parse(Urls.imgHost + url);

				intent.setData(uri);

				boolean flag = isIntentAvailable(getApplication(), intent);
				if (flag) {
					startActivity(intent);
				} else {
					ToastUtil.showToast(getApplication(), "请安装OFFICE");
				}

			}
			break;
		case R.id.goto_invoice:
			Intent it = new Intent(ConfirmPayActivity3.this, MyInvoice2Activity.class);
			it.putExtra("isOrderFrom", true);
			startActivityForResult(it, 0);
			break;
		case R.id.my_invoice:
			Intent its = new Intent(ConfirmPayActivity3.this, MyInvoiceAddActivity.class);
			its.putExtra("flag1", true);
			startActivityForResult(its, 1);
			break;
		case R.id.deleteImage:
			mInvoiceTv.setText("");
			invoice = null;
			break;
		case R.id.addassociation:
			// 默认非必填最大值出行人数为0 取无限制
			if (!"0".equals(productInfo.getMaxMember())) {
				if (Integer.parseInt(productInfo.getMaxMember()) <= (partnerInfos.size())) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "随行人已达到最大上限数");
					return;
				}
			}
			Intent intent1 = new Intent(ConfirmPayActivity3.this, MyInvoiceActivity.class);
			intent1.putExtra("isOrderFrom", true);
			startActivityForResult(intent1, 101);
			break;

		case R.id.cash_ll:
			if (partnerInfos == null || partnerInfos.size() <= 0) {
				ToastUtil.showToast(ConfirmPayActivity3.this, "请添加出行人");
				return;
			}
			Intent cashintent = new Intent(ConfirmPayActivity3.this, ProductCashCardActivity.class);
			Bundle mBundle = new Bundle();
			seclist.clear();
			mBundle.putSerializable("cashList", (Serializable) cashLists);
			cashintent.putExtras(mBundle);
			startActivityForResult(cashintent, 818);
			activityAnimationOpen();
			break;

		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg1) {

		case 2015:
			start_times = new ArrayList<String>();
			end_times = new ArrayList<String>();
			priceByDate = arg2.getStringExtra("priceByDate");
			if ("请选择出发日期".equals(arg2.getStringExtra("result"))) {
				// time =arg2.getStringExtra("result");
				time_start_go.setText("请选择出发日期");
			} else {
				time = arg2.getStringExtra("result");
				// 取得优惠券数量
				if (!StringUtils.isEmpty(time)) {
					getSchedulets(time.replace("-", ""));
				}
				time_start_go.setText(time);

				// 结束时间
				time_back_go = DateUtil.getBeforeAfterDate(time, Integer.parseInt(productInfo.getDays())).toString();

				start_times.add(time);
				end_times.add(time_back_go);
				productPriceTxt.setText("( ¥" + priceByDate + "/人x" + (partnerInfos.size()) + " )");
				totalPrice = new BigDecimal(priceByDate).multiply(new BigDecimal(partnerInfos.size()))
						.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				Log.e("log", "价格：" + totalPrice);
				totalPriceTxt.setText("¥" + totalPrice);
				mTouristCountTv.setText((partnerInfos.size()) + "");
				mMoneyTypeTv.setText("¥" + totalPrice);

			}

			break;
		case 2:// 发票返回
			if (arg0 == 0) {
				invoice = arg2.getParcelableExtra("invoice");
				if (invoice != null) {
					String text = "抬头: " + invoice.getInvoiceTitle() + " 姓名: " + invoice.getUserName() + " 地址: "
							+ invoice.getAddress();
					mInvoiceTv.setText(text);
				}
			}
			break;
		case 1:
			if (arg0 == 101) {
				PartnerInfo info = arg2.getParcelableExtra("partner");
				if (null != info) {
					boolean flas = false;
					for (PartnerInfo partnerInfo : partnerInfos) {
						if (info.getId().equals(partnerInfo.getId())) {
							flas = true;
							break;
						}
					}
					if (!flas) {
						partnerInfos.add(info);
						// memberAdapter.notifyDataSetChanged();
						if (time != null && time.length() >= 10)
							getApplyPrice(productInfo.getId(), time.substring(0, 10).replace("-", ""),
									time_back_go.replaceAll("-", ""), partnerInfos.size());
					}
					tourists_num.setText("出行游客 (" + partnerInfos.size() + "人 )");
				}
				ArrayList<PartnerInfo> list = arg2.getParcelableArrayListExtra("partnerInfoList");
				if (list != null && list.size() > 0) {
					for (PartnerInfo partnerInfo : partnerInfos) {
						for (int i = 0; i < list.size(); i++) {
							if (partnerInfo.getId().equals(list.get(i).getId())) {
								partnerInfo.setIdNo(list.get(i).getIdNo());
								partnerInfo.setRealName(list.get(i).getRealName());
								partnerInfo.setEmail(list.get(i).getEmail());
								partnerInfo.setIdType(list.get(i).getIdType());
								partnerInfo.setMyself(list.get(i).getMyself());
								partnerInfo.setPassportNo(list.get(i).getPassportNo());
								partnerInfo.setTel(list.get(i).getTel());
								break;
							}
						}
					}
				}
				// if (partnerInfos.size() > 0) {
				// memberLst.setVisibility(View.VISIBLE);
				// } else {
				// memberLst.setVisibility(View.GONE);
				// }
				associateAdapter.notifyDataSetChanged();
			} else if (arg0 == 1) {
				invoice = arg2.getParcelableExtra("invoice");
				if (invoice != null) {
					String text = "抬头: " + invoice.getInvoiceTitle() + " 姓名: " + invoice.getUserName() + " 地址: "
							+ invoice.getAddress();
					mInvoiceTv.setText(text);
				}
			}
			break;
		case 1001:
			if (arg2 != null) {
				int index = 0;
				if (arg2.getStringExtra("action").equals("del")) {
					index = arg2.getIntExtra("index", 0);
					for (int i = 0; i < partnerInfos.size(); i++) {
						if (i == index) {
							partnerInfos.remove(i);
						}
					}
				} else if (arg2.getStringExtra("action").equals("add")) {
					PartnerInfo partnerInfo = arg2.getParcelableExtra("partner");
					partnerInfos.add(partnerInfo);
				} else if (arg2.getStringExtra("action").equals("modify")) {
					index = arg2.getIntExtra("index", 0);
					PartnerInfo partnerInfo = arg2.getParcelableExtra("partner");
					for (int i = 0; i < partnerInfos.size(); i++) {
						if (i == index) {
							partnerInfos.get(i).setRealName(partnerInfo.getRealName());
							partnerInfos.get(i).setIdNo(partnerInfo.getIdNo());
						}
					}
				}
				if (partnerInfos.size() > 0) {
					memberLst.setVisibility(View.VISIBLE);
				} else {
					memberLst.setVisibility(View.GONE);
				}

				// memberAdapter.notifyDataSetChanged();
				associateAdapter.notifyDataSetChanged();
				// countTxt.setText("(" + productInfo.getMaxMember() + "/" +
				// partnerInfos.size() + ")");
			}
			// if (partnerInfos.size() == 0)
			// {
			// getApplyPrice(productInfo.getId(), beginDate, endDate,
			// partnerInfos.size());
			// }
			// else
			// {
			// getApplyPrice(productInfo.getId(), beginDate, endDate,
			// partnerInfos.size());
			// }
			getApplyPrice(productInfo.getId(), time.substring(0, 10).replace("-", ""), time_back_go.replaceAll("-", ""),
					partnerInfos.size());

			break;
		// 现金券返回
		case 818:
			seclist = (List<CashOrder>) arg2.getSerializableExtra("result");
			getApplyPrice2(productInfo.getId(), time.substring(0, 10).replace("-", ""),
					time_back_go.replaceAll("-", ""), partnerInfos.size());
			break;
		default:
			break;
		}
		if (arg0 == 102 && arg1 == 1) {
			PartnerInfo info = arg2.getParcelableExtra("partner");
			if (null != info) {
				boolean flas = false;
				for (PartnerInfo partnerInfo : partnerInfos) {
					if (info.getId().equals(partnerInfo.getId())) {
						flas = true;
						break;
					}
				}
				if (!flas) {
					partnerInfos.add(info);
					// memberAdapter.notifyDataSetChanged();
					if (time != null && time.length() >= 10)
						getApplyPrice(productInfo.getId(), time.substring(0, 10).replace("-", ""),
								time_back_go.replaceAll("-", ""), partnerInfos.size());
				}
				tourists_num.setText("出行游客 (" + partnerInfos.size() + "人 )");
			}
			associateAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 起始日期选择方法
	 */
	private void dayPickerDialog() {
		begindialog = new AlertDialog.Builder(this).create();
		begindialog.show();
		Window window = begindialog.getWindow();
		window.setContentView(R.layout.day_picker_dialog);
		mdayPicker = (DayPicker) window.findViewById(R.id.alarm_date_picker);
		Button ok = (Button) window.findViewById(R.id.ok_button_date);
		Button cancel = (Button) window.findViewById(R.id.cancel_button_date);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				begindialog.cancel();
			}
		});
	}

	/**
	 * 终止日期选择方法
	 */
	private void dayEndPickerDialog() {
		endDialog = new AlertDialog.Builder(this).create();
		endDialog.show();
		Window window = endDialog.getWindow();
		window.setContentView(R.layout.day_picker_enddialog);
		mdayPicker = (DayPicker) window.findViewById(R.id.alarm_date_endpicker);
		Button ok = (Button) window.findViewById(R.id.ok_button_enddate);
		Button cancel = (Button) window.findViewById(R.id.cancel_button_enddate);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				endDialog.cancel();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.time_listview:
			if (DateUtil.getTimeInMillis(productInfo.getExpPeriodList().get(position).getBeginDate()) <= DateUtil
					.getcurrentTimeMillis()) {
				return;
			} else {
				dateAdapter.setSelectPosition(position);
			}
			selectPosition = position;
			beginDate = productInfo.getExpPeriodList().get(position).getBeginDate();
			endDate = productInfo.getExpPeriodList().get(position).getEndDate();
			break;
		case R.id.member_listview:
			Intent intent = new Intent(ConfirmPayActivity3.this, AddMemberActivity.class);
			intent.putExtra("isAdd", false);
			intent.putExtra("index", position);
			intent.putExtra("partner", partnerInfos.get(position));
			startActivityForResult(intent, 101);
			break;
		case R.id.my_invoice:
			// Intent it = new
			// Intent(ConfirmPayActivity3.this,MyInvoiceAddActivity.class);
			// it.putExtra("add_invoice", true);
			// startActivityForResult(it, 2016);
			break;
		default:
			break;
		}
	}

	// 获取价格接口
	private void getApplyPrice(String productId, String beginTime, String endTime, int partnerNum) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("productId", productId);
			jsonObject.put("beginTime", beginTime);
			jsonObject.put("endTime", endTime);
			jsonObject.put("partnerNum", partnerNum);
			jsonObject.put("includeSelf", "yes");
			// jsonObject.put("partnerNum",2);
			System.out.println("jsonObject=" + jsonObject.toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.apply_price_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					System.out.println("预订价格接口=" + response);
					if (!response.optString("code").equals("00000")) {
						// ToastUtil.showToast(ConfirmPayActivity3.this,
						// "error=" + response.optString("code"));
						return;
					}
					JSONObject dataJson;
					try {
						dataJson = response.getJSONObject("data");
						if (dataJson != null && !dataJson.equals("") && !dataJson.equals("null")) {
							// String toatlPrice =
							// dataJson.optString("totalPrice");
							String price = dataJson.optString("price");
							String amount = dataJson.optString("amount");
							String unit = dataJson.optString("unit");
							String is_instal = dataJson.optString("is_instal");
							String sfy_sf_price = dataJson.optString("sfy_sf_price");
							String period_num = dataJson.optString("period_num");
							pay_sfy_sf_price = sfy_sf_price;

							if ("yes".equals(is_instal)) {
								linespace2.setVisibility(View.VISIBLE);
								payPeriodizationRelative.setVisibility(View.VISIBLE);
								pay_periodization.setEnabled(true);
								periodization_text.setText("￥" + sfy_sf_price + "×" + period_num + "期");
								tips.setText("支付商品价格的1/" + period_num + "即可出发 ");
								if (pay_periodization.isChecked() == true) {
									mMoneyTypeNameTv.setText("支付金额");
									mMoneyTypeTv.setText("￥" + pay_sfy_sf_price);
								}
							} else {
								pay_totalMoney.setChecked(true);
								linespace2.setVisibility(View.GONE);
								payPeriodizationRelative.setVisibility(View.GONE);
							}

							// productPriceTxt.setText("¥" + price+"x"+amount);
							// totalPriceTxt.setText("¥" + toatlPrice);
							// 是否支持定金支付
							String supportDeposit = dataJson.optString("supportDeposit", "no");
							productPriceTxt.setText("( ￥" + priceByDate + "/人x" + (partnerInfos.size()) + " )");
							totalPrice = new BigDecimal(priceByDate).multiply(new BigDecimal(partnerInfos.size()))
									.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
							if (cashLists.size() > 0 && !"0.00".equals(totalPrice)) {
								cashLine.setVisibility(View.VISIBLE);
								cashLine2.setVisibility(View.VISIBLE);
								cashLine3.setVisibility(View.VISIBLE);
								cash_ll.setVisibility(View.VISIBLE);
								can_used.setText(" " + cashLists.size() + "张可用" + " ");
								if (seclist.size() > 0) {
									layout_favorablePrice.setVisibility(View.VISIBLE);
									if ("现金抵用券".equals(seclist.get(0).getCouponTypeName())) {
										youhui.setText("现金券优惠:");
										youhui2.setText("现金券优惠:");
										cash_num.setText("-￥" + seclist.get(0).getFaceValue());
										favorablePrice.setText("-￥" + seclist.get(0).getFaceValue());
										favorablePrice2.setText("-￥" + seclist.get(0).getFaceValue());
										totalPriceTxt.setText("￥" + (Double.parseDouble(totalPrice)
												- Double.parseDouble(seclist.get(0).getFaceValue())));
									} else if ("折扣券".equals(seclist.get(0).getCouponTypeName())) {
										youhui.setText("折扣券优惠:");
										youhui2.setText("折扣券优惠:");
										cash_num.setText((Double.parseDouble(seclist.get(0).getDiscount()) * 10) + "折");
										favorablePrice
												.setText((Double.parseDouble(seclist.get(0).getDiscount()) * 10) + "折");
										favorablePrice2
												.setText((Double.parseDouble(seclist.get(0).getDiscount()) * 10) + "折");
										totalPriceTxt.setText("￥" + decimalFormat.format((Double.parseDouble(totalPrice)
												* Double.parseDouble(seclist.get(0).getDiscount()))));
									}
								} else {
									totalPriceTxt.setText("￥" + totalPrice);
								}
							} else {
								// cashLine.setVisibility(View.GONE);
								// cashLine2.setVisibility(View.GONE);
								// cashLine3.setVisibility(View.GONE);
								// cash_ll.setVisibility(View.GONE);
								seclist.clear();
								cash_num.setText("");
								layout_favorablePrice.setVisibility(View.GONE);
								layout_favorablePrice2.setVisibility(View.GONE);

								totalPriceTxt.setText("￥" + totalPrice);
							}
							mTouristCountTv.setText((partnerInfos.size()) + "");
							// 定金
							if ("yes".equals(supportDeposit)) {
								earnestMoney = dataJson.optString("deposit");
								if (seclist.size() > 0 && !"0.00".equals(totalPrice)) {
									layout_favorablePrice2.setVisibility(View.VISIBLE);
									if ("现金抵用券".equals(seclist.get(0).getCouponTypeName())) {
										mPayEarnestMoneyTv.setText("￥" + (Double.parseDouble(earnestMoney)));
										Double payEnd = Double.parseDouble(totalPrice)
												- (Double.parseDouble(earnestMoney))
												- Double.parseDouble(seclist.get(0).getFaceValue());
										String price1 = payEnd.toString();
										pay_earnest_tv_end.setText("￥" + price1);
									} else if ("折扣券".equals(seclist.get(0).getCouponTypeName())) {
										mPayEarnestMoneyTv.setText("￥" + (Double.parseDouble(earnestMoney)));
										Double payEnd = (Double.parseDouble(totalPrice)
												* Double.parseDouble(seclist.get(0).getDiscount()))
												- (Double.parseDouble(earnestMoney));
										String price1 = decimalFormat.format(payEnd).toString();
										pay_earnest_tv_end.setText("￥" + price1);
									}
								} else {
									layout_favorablePrice2.setVisibility(View.GONE);
									mPayEarnestMoneyTv.setText("￥" + earnestMoney);
									Double payEnd = Double.parseDouble(totalPrice) - Double.parseDouble(earnestMoney);
									String price1 = payEnd.toString();
									pay_earnest_tv_end.setText("￥" + price1);
								}
							} else {
								// mMyRadioGroup.setVisibility(View.GONE);
							}
							if (paymentType == 0) {
								// 全额
								if (seclist.size() > 0 && !"0.00".equals(totalPrice)) {
									if ("现金抵用券".equals(seclist.get(0).getCouponTypeName())) {
										mMoneyTypeTv.setText("￥" + (Double.parseDouble(totalPrice)
												- Double.parseDouble(seclist.get(0).getFaceValue())));
									} else if ("折扣券".equals(seclist.get(0).getCouponTypeName())) {
										mMoneyTypeTv.setText("￥" + decimalFormat.format((Double.parseDouble(totalPrice)
												* Double.parseDouble(seclist.get(0).getDiscount()))));
									}
								} else {
									mMoneyTypeTv.setText("￥" + totalPrice);
								}
							} else if (paymentType == 1) {
								// 定金
								if (seclist.size() > 0 && !"0.00".equals(totalPrice)) {
									mMoneyTypeTv.setText("￥" + (Double.parseDouble(earnestMoney)));
								} else {
									mMoneyTypeTv.setText("￥" + earnestMoney);
								}
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getApplyPrice2(String productId, String beginTime, String endTime, int partnerNum) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("productId", productId);
			jsonObject.put("beginTime", beginTime);
			jsonObject.put("endTime", endTime);
			jsonObject.put("partnerNum", partnerNum);
			jsonObject.put("includeSelf", "yes");
			// jsonObject.put("partnerNum",2);
			System.out.println("jsonObject=" + jsonObject.toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.apply_price_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					System.out.println("预订价格接口=" + response);
					if (!response.optString("code").equals("00000")) {
						// ToastUtil.showToast(ConfirmPayActivity3.this,
						// "error=" + response.optString("code"));
						return;
					}
					JSONObject dataJson;
					try {
						dataJson = response.getJSONObject("data");
						if (dataJson != null && !dataJson.equals("") && !dataJson.equals("null")) {
							// String toatlPrice =
							// dataJson.optString("totalPrice");
							String price = dataJson.optString("price");
							String amount = dataJson.optString("amount");
							String unit = dataJson.optString("unit");
							String is_instal = dataJson.optString("is_instal");
							String sfy_sf_price = dataJson.optString("sfy_sf_price");
							String period_num = dataJson.optString("period_num");
							pay_sfy_sf_price = sfy_sf_price;
							if ("yes".equals(is_instal)) {
								linespace2.setVisibility(View.VISIBLE);
								pay_periodization.setEnabled(true);
								payPeriodizationRelative.setVisibility(View.VISIBLE);
								periodization_text.setText("￥" + sfy_sf_price + "×" + period_num);
								tips.setText("支付商品价格的1/" + period_num + "即可出发 ");
								if (pay_periodization.isChecked() == true) {
									mMoneyTypeNameTv.setText("支付金额");
									mMoneyTypeTv.setText("￥" + pay_sfy_sf_price);
								}
							} else {
								pay_totalMoney.setChecked(true);
								linespace2.setVisibility(View.GONE);
								payPeriodizationRelative.setVisibility(View.GONE);
							}

							// productPriceTxt.setText("¥" + price+"x"+amount);
							// totalPriceTxt.setText("¥" + toatlPrice);
							// 是否支持定金支付
							String supportDeposit = dataJson.optString("supportDeposit", "no");
							productPriceTxt.setText("( ￥" + priceByDate + "/人x" + (partnerInfos.size()) + " )");
							totalPrice = new BigDecimal(priceByDate).multiply(new BigDecimal(partnerInfos.size()))
									.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
							// totalPriceTxt.setText("￥" + totalPrice);
							if (seclist.size() > 0 && !"0.00".equals(totalPrice)) {
								layout_favorablePrice.setVisibility(View.VISIBLE);
								if ("现金抵用券".equals(seclist.get(0).getCouponTypeName())) {
									youhui.setText("现金券优惠:");
									youhui2.setText("现金券优惠:");
									cash_num.setText("-￥" + seclist.get(0).getFaceValue());
									favorablePrice.setText("-￥" + seclist.get(0).getFaceValue());
									favorablePrice2.setText("-￥" + seclist.get(0).getFaceValue());
									totalPriceTxt.setText("￥" + (Double.parseDouble(totalPrice)
											- Double.parseDouble(seclist.get(0).getFaceValue())));
								} else if ("折扣券".equals(seclist.get(0).getCouponTypeName())) {
									youhui.setText("折扣券优惠:");
									youhui2.setText("折扣券优惠:");
									cash_num.setText((Double.parseDouble(seclist.get(0).getDiscount()) * 10) + "折");
									favorablePrice
											.setText((Double.parseDouble(seclist.get(0).getDiscount()) * 10) + "折");
									favorablePrice2
											.setText((Double.parseDouble(seclist.get(0).getDiscount()) * 10) + "折");
									totalPriceTxt.setText("￥" + decimalFormat.format((Double.parseDouble(totalPrice)
											* Double.parseDouble(seclist.get(0).getDiscount()))));
								}

							} else {
								totalPriceTxt.setText("￥" + totalPrice);
							}
							mTouristCountTv.setText((partnerInfos.size()) + "");
							// 定金
							if ("yes".equals(supportDeposit)) {
								earnestMoney = dataJson.optString("deposit");
								// mMyRadioGroup.setVisibility(View.VISIBLE);
								if (seclist.size() > 0 && !"0.00".equals(totalPrice)) {
									layout_favorablePrice2.setVisibility(View.VISIBLE);
									if ("现金抵用券".equals(seclist.get(0).getCouponTypeName())) {
										mPayEarnestMoneyTv.setText("￥" + (Double.parseDouble(earnestMoney)));
										Double payEnd = Double.parseDouble(totalPrice)
												- (Double.parseDouble(earnestMoney))
												- Double.parseDouble(seclist.get(0).getFaceValue());
										String price1 = payEnd.toString();
										pay_earnest_tv_end.setText("￥" + price1);
									} else if ("折扣券".equals(seclist.get(0).getCouponTypeName())) {
										mPayEarnestMoneyTv.setText("￥" + (Double.parseDouble(earnestMoney)));
										Double payEnd = (Double.parseDouble(totalPrice)
												* Double.parseDouble(seclist.get(0).getDiscount()))
												- (Double.parseDouble(earnestMoney));
										String price1 = decimalFormat.format(payEnd).toString();
										pay_earnest_tv_end.setText("￥" + price1);
									}
								} else {
									mPayEarnestMoneyTv.setText("￥" + earnestMoney);
									Double payEnd = Double.parseDouble(totalPrice) - Double.parseDouble(earnestMoney);
									String price1 = payEnd.toString();
									pay_earnest_tv_end.setText("￥" + price1);
								}
							} else {
								// mMyRadioGroup.setVisibility(View.GONE);
							}
							if (paymentType == 0) {
								// 全额
								if (seclist.size() > 0 && !"0.00".equals(totalPrice)) {
									if ("现金抵用券".equals(seclist.get(0).getCouponTypeName())) {
										mMoneyTypeTv.setText("￥" + (Double.parseDouble(totalPrice)
												- Double.parseDouble(seclist.get(0).getFaceValue())));
									} else if ("折扣券".equals(seclist.get(0).getCouponTypeName())) {
										mMoneyTypeTv.setText("￥" + decimalFormat.format((Double.parseDouble(totalPrice)
												* Double.parseDouble(seclist.get(0).getDiscount()))));
									}
								} else {
									mMoneyTypeTv.setText("￥" + totalPrice);
								}
							} else if (paymentType == 1) {
								// 定金
								if (seclist.size() > 0 && !"0.00".equals(totalPrice)) {
									mMoneyTypeTv.setText("￥" + (Double.parseDouble(earnestMoney)));
								} else {
									mMoneyTypeTv.setText("￥" + earnestMoney);
								}
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 生成订单获取订单号接口
	private void getOrederInfo(String productId, String beginTime, String endTime, String leaveWord) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("productId", productId);
			jsonObject.put("beginTime", beginTime);
			jsonObject.put("endTime", endTime);
			jsonObject.put("leaveWord", leaveWord);
			jsonObject.put("partnerNum", partnerInfos.size());
			JSONArray partnerArray = new JSONArray();
			// String[] arrays = new String[partnerInfos.size() + 1];
			for (int i = 0; i < partnerInfos.size(); i++) {
				// JSONObject partnerJson = new JSONObject();
				// if (i == 0)
				// {
				//// partnerJson.put("name", userNameEdt.getText().toString());
				//// partnerJson.put("idNo",
				// userNumberEdt.getText().toString());
				//// partnerJson.put("myself", "yes");
				// partnerArray.put( userNumberEdt.getText().toString());
				// }
				// else
				// {
				partnerArray.put(partnerInfos.get(i).getId() + "");
				// partnerJson.put("name", partnerInfos.get(i -
				// 1).getRealName());
				// partnerJson.put("idNo", partnerInfos.get(i - 1).getIdNo());
				// partnerJson.put("myself", "no");
				// }
				// partnerArray.put(partnerJson);
			}
			jsonObject.put("partnerIds", partnerArray);
			if (paymentType == 0) {
				// 全额
				total = totalPrice;
				jsonObject.put("paymentType", "0");
				if (seclist.size() > 0) {
					String couponCode1 = seclist.get(0).getCouponCode();
					jsonObject.put("couponCode", couponCode1);
				} else {
					jsonObject.put("couponCode", "");

				}
			} else if (paymentType == 1) {
				// 定金
				total = earnestMoney;
				jsonObject.put("paymentType", "1");
				if (seclist.size() > 0) {
					String couponCode1 = seclist.get(0).getCouponCode();
					jsonObject.put("couponCode", couponCode1);
				} else {
					jsonObject.put("couponCode", "");

				}
			} else if (paymentType == 2) {
				// 定金
				total = pay_sfy_sf_price;
				jsonObject.put("paymentType", "2");

			}
			// 旧版本 目前全款 0 定金 1 分期 3
			// 过期订单
			jsonObject.put("orderNo", "");
			jsonObject.put("urgentContactNum", emergency_order_tel.getText().toString());
			jsonObject.put("urgentContactPerson", emergency_order_name.getText().toString());
			jsonObject.put("tel", order_tel.getText().toString());
			// 是否使用发票
			if (invoice != null) {
				String invoiceId = invoice.getInvoiceId();
				if (!StringUtils.isEmpty(invoiceId)) {
					jsonObject.put("invoiceId", invoiceId);
				} else {
					jsonObject.put("invoiceId", "");
				}
			} else {
				jsonObject.put("invoiceId", "");
			}
			jsonObject.put("contactName", order_name.getText().toString());
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.apply_orderInfo_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					LogUtil.i(TAG, "getOrederInfo", "statusCode=" + statusCode);
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
								orderNo = dataJson.optString("orderNo");
								total = dataJson.optString("total");
								if (StringUtils.isEmpty(total)) {
									return;
								}
								if (pay_periodization.isChecked() == true && paymentType == 2) {
									Intent intent2 = new Intent(ConfirmPayActivity3.this, ContentActivity.class);
									intent2.putExtra("path", Urls.pay + orderNo);
									intent2.putExtra("title", "首付游");
									ConfirmPayActivity3.this.startActivity(intent2);
								} else {
									// 去支付
									new Thread() {
										public void run() {

											String orderInfoStr = AlipayUtile.getNewOrderInfo(orderNo,
													productInfo.getTitle(), total);
											// 构造PayTask 对象
											PayTask alipay = new PayTask(ConfirmPayActivity3.this);
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

	private void Topaysuccess(String result) {
		Intent newIntent = new Intent(this, PaySuccessAcivity.class);
		newIntent.putExtra("orderNo", orderNo);
		newIntent.putExtra("total", total);
		newIntent.putExtra("result", result);
		// newIntent.putExtra("beginDate", time.toString().substring(0,
		// 10).replaceAll("-", "/"));
		// newIntent.putExtra("endDate", time_back_go.toString().replaceAll("-",
		// "/") );

		newIntent.putExtra("beginDate", time.toString().substring(0, 10).replaceAll("-", ""));
		newIntent.putExtra("endDate", time_back_go.toString().replaceAll("-", ""));

		newIntent.putExtra("leaveEdt", leaveEdt.getText().toString());
		newIntent.putExtra("name", productInfo.getTitle());
		newIntent.putExtra("num", String.valueOf(partnerInfos.size()));
		startActivity(newIntent);
		activityAnimationOpen();
		finish();
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Result result = new Result((String) msg.obj);
			if (msg.what == 0) {
				if (result.getResult().equals("9000")) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "支付成功");
					finish();
					activityAnimationClose();
				} else if (result.getResult().equals("4000")) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "系统异常");
				} else if (result.getResult().equals("4001")) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "订单参数错误");
				} else if (result.getResult().equals("6001")) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "用户取消支付");
				} else if (result.getResult().equals("6002")) {
					ToastUtil.showToast(ConfirmPayActivity3.this, "网络连接异常");
				} else {
					ToastUtil.showToast(ConfirmPayActivity3.this, "其他錯誤");
				}
				Topaysuccess(result.getResult());
			}
		}

	};

	@Override
	public void onCheckedChanged(MyRadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.pay_earnestMoney:
			paymentType = 1;
			mMoneyTypeNameTv.setText("支付订金");
			mMoneyTypeTv.setText("￥" + earnestMoney);
			break;
		case R.id.pay_totalMoney:
			paymentType = 0;
			mMoneyTypeNameTv.setText("支付全款");
			// mMoneyTypeTv.setText("￥" + totalPrice);
			// 全额
			if (seclist.size() > 0) {
				if ("现金抵用券".equals(seclist.get(0).getCouponTypeName())) {
					mMoneyTypeTv.setText(
							"￥" + (Double.parseDouble(totalPrice) - Double.parseDouble(seclist.get(0).getFaceValue())));
				} else if ("折扣券".equals(seclist.get(0).getCouponTypeName())) {
					mMoneyTypeTv.setText("￥" + decimalFormat.format(
							(Double.parseDouble(totalPrice) * (Double.parseDouble(seclist.get(0).getDiscount())))));
				}
			} else {
				mMoneyTypeTv.setText("￥" + totalPrice);
			}
			break;
		case R.id.pay_periodization:
			paymentType = 2;
			mMoneyTypeNameTv.setText("支付金额");
			mMoneyTypeTv.setText("￥" + pay_sfy_sf_price);
			break;

		default:
			break;
		}
	}

	/**
	 * 判断Intent 是否存在
	 * 
	 * @param context
	 * @param intent
	 * @return
	 */
	private boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
		return list.size() > 0;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				// if(mIdNoEv !=null&& mIdNoEv.getVisibility() == View.VISIBLE){
				// if(!StringUtils.isEmpty(mIdNoEv.getText().toString())){
				// userNumberEdt.setText(mIdNoEv.getText().toString());
				// userNumberEdt.setVisibility(View.VISIBLE);
				// mIdNoEv.setVisibility(View.GONE);
				// getUserInfo.setUsersIdentity(mIdNoEv.getText().toString());
				// userInfoEdit(getUserInfo);
				// }
				// }
				return true;
			}
		}
		return false;
	}

	private void userInfoEdit(final UserInfo userInfo) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("avatar", userInfo.getAvatar());
			jsonObject.put("nickname", userInfo.getNickname());
			jsonObject.put("realName", userInfo.getRealName());
			jsonObject.put("usersIdentity", userInfo.getUsersIdentity());
			jsonObject.put("sex", userInfo.getGender());
			jsonObject.put("address", userInfo.getAddress());
			jsonObject.put("introduction", userInfo.getIntroduction());
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.userinfo_edit_url, stringEntity, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					super.onFailure(statusCode, headers, responseString, throwable);
					// ToastUtil.showToast(ModifyInfoActivity.this, "修改失败
					// errorMessage=" + throwable.getMessage());
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
					LogUtil.e("ModifyInfoActivity", "设置个人信息接口", response.toString());
					if (response.optString("code").equals("00000")) {
						UserInfoDB.getInstance().insertData(userInfo);
					} else {
						// ToastUtil.showToast(ModifyInfoActivity.this, "修改失败
						// errorCode=" + response.optString("code"));
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getSchedulets(String productPeriod) {
		cashLists.clear();
		RequestParams params = new RequestParams();
		params.put("productPeriod", productPeriod);// 页码数
		params.put("productId", productInfo.getId());// 券类型
		HttpUtil.get(Urls.validCoupon, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				getApplyPrice(productInfo.getId(), time.substring(0, 10).replace("-", ""),
						time_back_go.replaceAll("-", ""), partnerInfos.size());
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				String code = response.optString("code");
				if (code.equals("00000")) {
					JSONArray dataJsonS = response.optJSONArray("data");
					if (dataJsonS != null && dataJsonS.length() > 0) {
						List<CashOrder> queuesInfos = new ArrayList<CashOrder>();
						for (int i = 0; i < dataJsonS.length(); i++) {
							JSONObject cashObj = dataJsonS.optJSONObject(i);
							CashOrder cashOrder = new CashOrder();
							if ("现金抵用券".equals(cashObj.optString("couponTypeName"))) {
								cashOrder.setDiscount(cashObj.optString("discount"));
								cashOrder.setCouponCode(cashObj.optString("couponCode"));
								cashOrder.setCouponName(cashObj.optString("couponName"));
								cashOrder.setCouponTypeName(cashObj.optString("couponTypeName"));
								cashOrder.setDescription(cashObj.optString("description"));
								cashOrder.setExpireDate(cashObj.optString("expireDate"));
								cashOrder.setFaceValue(cashObj.optString("faceValue"));
								cashOrder.setIsExpired(cashObj.optString("isExpired"));
								cashOrder.setIsUniversal(cashObj.optString("isUniversal"));
								cashOrder.setIsUsed(cashObj.optString("isUsed"));
								cashOrder.setStatus(cashObj.optString("status"));
								queuesInfos.add(cashOrder);
							}

						}
						cashLists.addAll(queuesInfos);
					}
				}
			}
		});
	}
}
