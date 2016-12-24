package com.bcinfo.tripaway.view.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

public class FilterDialog extends Dialog implements OnClickListener {

	private Context mContext;

	private RelativeLayout infoRootLayout;

	private List<Dest> list;

	private List<Dest> priceList;

	private List<Dest> dayList;

	private CompoundButton currentDestinationView;

	ImageLoader imageLoader = ImageLoader.getInstance();

	private FlowLayout destinationFlowLayout;

	private LinearLayout destinationLayout;

	private FlowLayout priceFlowLayout;

	private LinearLayout priceLayout;

	private FlowLayout daysFlowLayout;

	private LinearLayout daysLayout;

	private CompoundButton currentPriceView;

	private CompoundButton currentDaysView;

	private OperationListener mOperationListener;

	private LayoutInflater mInflater;

	private LayoutParams mLp;

	private String destId="";

	private String price="";
	
	private boolean priceFlag=false;
	
	private boolean dayFlag = false;

	private String day="";
	
	private TextView mOkBtn;
	
	private TextView mCancelBtn;

	public FilterDialog(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * 
	 * @param context
	 * @param onAppariseListener
	 */
	public FilterDialog(Context context, OperationListener mOperationListener,
			List<Dest> list,String destId,String price,String day) {
		super(context, R.style.Dialog1);
		this.mContext = context;
		this.mOperationListener = mOperationListener;
		this.list = list;
		this.destId = destId;
		this.price = price;
		this.day = day;
		mLp = getWindow().getAttributes();
		mLp.gravity = Gravity.RIGHT;
		// mLp.dimAmount = (float) 0.75; // 去背景遮盖
		mLp.alpha = 1.0f;
		mLp.width = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(mLp);
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		getWindow().setAttributes(mLp);
		initList();
		// 初始化控件
		initDialog();
	}

	private void initDialog() {
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = mInflater.inflate(R.layout.filter1, null);
		setContentView(layout);
		destinationFlowLayout = (FlowLayout) findViewById(R.id.destination);
		destinationLayout = (LinearLayout) findViewById(R.id.destinationLayout);
		priceFlowLayout = (FlowLayout) findViewById(R.id.price);
		priceLayout = (LinearLayout) findViewById(R.id.priceLayout);
		daysFlowLayout = (FlowLayout) findViewById(R.id.days);
		daysLayout = (LinearLayout) findViewById(R.id.daysLayout);
		mOkBtn = (TextView) findViewById(R.id.ok);
		mOkBtn.setOnClickListener(this);
		mCancelBtn = (TextView) findViewById(R.id.cancel);
		mCancelBtn.setOnClickListener(this);
		addDestinationFlowView(list, destinationFlowLayout);
		addPriceFlowView(priceList, priceFlowLayout);
		addDaysFlowView(dayList, daysFlowLayout);
	}

	private void setDestinationLayoutGone() {
		destinationLayout.setVisibility(View.GONE);
	}

	private void addDestinationFlowView(final List<Dest> strs,
			FlowLayout flowLayout) {
		if (strs == null || strs.size() == 0) {
			setDestinationLayoutGone();
			return;
		}
		List<String> destArrayList = new ArrayList<>();
		if(!StringUtils.isEmpty(destId)){
			String[] arrays = destId.split(",");
			if(null != arrays ){
				for(int i = 0;i<arrays.length;i++){
					destArrayList.add(arrays[i]);
				}
			}
		}
		for (int i = 0; i < strs.size(); i++) {
			Dest dest = strs.get(i);
			CheckBox newView = new CheckBox(((Activity) mContext));
			newView.setBackgroundResource(R.drawable.square);
			newView.setText(dest.getDestName());
			newView.setButtonDrawable(android.R.color.transparent);
			newView.setTag(dest.getDestId());
			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setTextColor(Color.BLACK);
			if(destArrayList.contains(dest.getDestId())){
				//如果存在
				newView.setTextColor(Color.WHITE);
				newView.setChecked(true);
			}
			newView.setOnClickListener(new View. OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox box = (CheckBox) v;
					if(!box.isChecked()){
						((CheckBox)v).setTextColor(Color.BLACK);
					}
				}
			});
			newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton mCompoundButton,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						mCompoundButton.setTextColor(Color.WHITE);
//						if (currentDestinationView == null) {
//
//						} else {
//							currentDestinationView.setTextColor(Color.BLACK);
//							currentDestinationView.setChecked(false);
//
//						}
//						currentDestinationView = mCompoundButton;
					}
				}
			});
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					DensityUtil.dip2px(((Activity) mContext), 80),
					DensityUtil.dip2px(((Activity) mContext), 29));
			params.rightMargin = DensityUtil.dip2px(((Activity) mContext),
					5);
			params.bottomMargin = DensityUtil.dip2px(((Activity) mContext),
					5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	private void addPriceFlowView(final List<Dest> strs, FlowLayout flowLayout) {
		for (int i = 0; i < strs.size(); i++) {
			Dest dest = strs.get(i);
			CheckBox newView = new CheckBox(((Activity) mContext));
			newView.setBackgroundResource(R.drawable.square);
			newView.setText(dest.getDestName());
			newView.setButtonDrawable(android.R.color.transparent);
			newView.setTag(dest.getDestId());
			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setTextColor(Color.BLACK);
			if(dest.getDestId().equals(price)){
				newView.setTextColor(Color.WHITE);
				newView.setChecked(true);
				priceFlag =true;
			}
			newView.setOnClickListener(new View. OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox box = (CheckBox) v;
					if(!box.isChecked()){
						((CheckBox)v).setTextColor(Color.BLACK);
						currentPriceView = null;
					}
				}
			});
			newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton mCompoundButton,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						
						if(priceFlowLayout != null){
							int num = priceFlowLayout.getChildCount();
							String ids = (String)mCompoundButton.getTag();
							for(int i=0;i<num;i++){
								CheckBox boxs = (CheckBox) priceFlowLayout.getChildAt(i);
								if(!ids.equals((String)boxs.getTag())){
									boxs.setChecked(false);
									boxs.setTextColor(Color.BLACK);
								}
							}
						}
						
						mCompoundButton.setTextColor(Color.WHITE);
						if (currentPriceView == null) {

						} else {
							currentPriceView.setTextColor(Color.BLACK);
							currentPriceView.setChecked(false);
						}
						currentPriceView = mCompoundButton;
					}
				}
			});
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					DensityUtil.dip2px(((Activity) mContext), 80),
					DensityUtil.dip2px(((Activity) mContext), 29));
			params.rightMargin = DensityUtil.dip2px(((Activity) mContext),
					5);
			params.bottomMargin = DensityUtil.dip2px(((Activity) mContext),
					5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	private void addDaysFlowView(final List<Dest> strs, FlowLayout flowLayout) {
		for (int i = 0; i < strs.size(); i++) {
			Dest dest = strs.get(i);
			CheckBox newView = new CheckBox(((Activity) mContext));
			newView.setBackgroundResource(R.drawable.square);
			newView.setText(dest.getDestName());
			newView.setButtonDrawable(android.R.color.transparent);
			newView.setTag(dest.getDestId());
			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			newView.setTextColor(Color.BLACK);
			if(dest.getDestId().equals(day)){
				newView.setTextColor(Color.WHITE);
				newView.setChecked(true);
			}
			newView.setOnClickListener(new View. OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox box = (CheckBox) v;
					if(!box.isChecked()){
						((CheckBox)v).setTextColor(Color.BLACK);
						currentDaysView = null;
					}
				}
			});
			newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton mCompoundButton,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if(daysFlowLayout != null){
							int num = daysFlowLayout.getChildCount();
							String ids = (String)mCompoundButton.getTag();
							for(int i=0;i<num;i++){
								CheckBox boxs = (CheckBox) daysFlowLayout.getChildAt(i);
								if(!ids.equals((String)boxs.getTag())){
									boxs.setChecked(false);
									boxs.setTextColor(Color.BLACK);
								}
							}
						}
						mCompoundButton.setTextColor(Color.WHITE);
						if (currentDaysView == null) {

						} else {
							currentDaysView.setChecked(false);
							currentDaysView.setTextColor(Color.BLACK);
						}
						currentDaysView = mCompoundButton;
					}
				}
			});
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					DensityUtil.dip2px(((Activity) mContext), 80),
					DensityUtil.dip2px(((Activity) mContext), 29));
			params.rightMargin = DensityUtil.dip2px(((Activity) mContext),
					5);
			params.bottomMargin = DensityUtil.dip2px(((Activity) mContext),
					5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	public interface OperationListener {
		public void queryByTag(String dest,String price,String day);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
//			if(currentDestinationView != null){
//				destId = (String) currentDestinationView.getTag();
//			}
			
			if(destinationFlowLayout != null){
				destId = "";
				int num = destinationFlowLayout.getChildCount();
				for(int i =0;i<num;i++){
					CheckBox box = (CheckBox)destinationFlowLayout.getChildAt(i);
					if(box.isChecked()){
						if(i == 0){
							destId +=(String)box.getTag();
						}else{
							destId +=","+(String)box.getTag();
						}
					}
				}
			}
			
			if(currentDaysView != null){
				day = (String) currentDaysView.getTag();
			}else{
				day ="";
			}
			
			if(currentPriceView != null){
				price = (String) currentPriceView.getTag();
			}else{
				price ="";
			}
			mOperationListener.queryByTag(destId, price, day);
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		default:
			break;
		}
	}

	private void initList() {
		priceList = new ArrayList<>();
		priceList.add(new Dest("lt2000", "2000以内"));
		priceList.add(new Dest("2001-3000", "2001-3000"));
		priceList.add(new Dest("3001-5000", "3001-5000"));
		priceList.add(new Dest("5001-10000", "5001-1W"));
		priceList.add(new Dest("10001-50000", "1W-5W"));
		priceList.add(new Dest("gt50000", "5W以上"));

		dayList = new ArrayList<>();
		dayList.add(new Dest("lt5", "5日以内"));
		dayList.add(new Dest("5-10", "5-10日"));
		dayList.add(new Dest("10-15", "10-15日"));
		dayList.add(new Dest("gt15", "15日以上"));
	}

}
