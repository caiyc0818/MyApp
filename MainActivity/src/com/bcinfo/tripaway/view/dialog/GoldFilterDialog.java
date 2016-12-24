package com.bcinfo.tripaway.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.wefika.flowlayout.FlowLayout;

public class GoldFilterDialog extends Dialog implements OnClickListener {

	private Context mContext;

	private OperationListener mOperationListener;

	private String type = "all";
	private String isGold ="no";

	private CompoundButton currentView;

	private CheckBox radioBtn;

	private FlowLayout flayout;
	private TextView submitLy;

	private LayoutInflater mInflater;

	private LayoutParams mLp;

	public GoldFilterDialog(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * 
	 * @param context
	 * @param onAppariseListener
	 */
	public GoldFilterDialog(Context context,
			OperationListener mOperationListener, String isGold, String type) {
		super(context, R.style.Dialog);
		this.mContext = context;
		this.mOperationListener = mOperationListener;
		this.type=type;
		this.isGold = isGold;
		mLp = getWindow().getAttributes();
		Window win = getWindow();
		mLp.gravity = Gravity.TOP;
		// mLp.dimAmount = (float) 0.75; // 去背景遮盖
		mLp.alpha = 1.0f;
		mLp.width = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(mLp);
		mLp.y = DensityUtil.dip2px(mContext, 54);
		win.getDecorView()
				.setPadding(0,0, 0, 0);
		getWindow().setAttributes(mLp);
		// 初始化控件
		initDialog();
	}

	private void initDialog() {
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.gold_filter, null);
		setContentView(view);
		flayout = (FlowLayout) view.findViewById(R.id.destination);
		radioBtn = (CheckBox) view.findViewById(R.id.gold_radio);
		submitLy = (TextView) view.findViewById(R.id.submit);
		submitLy.setOnClickListener(this);
		addPriceFlowView(flayout);
		
		radioBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
				if(isChecked){
					
				
					isGold ="yes";
				}else{
					isGold ="no";
				}
			}
		});
		initParams();
	}

	private void initParams() {
		RadioButton btn = null;
		if ("all".equals(type)) {
			btn = (RadioButton) flayout.getChildAt(0);
			btn.setChecked(true);
		} else if ("guide".equals(type)) {
			btn = (RadioButton) flayout.getChildAt(1);
			btn.setChecked(true);
		} else if ("leader".equals(type)) {
			btn = (RadioButton) flayout.getChildAt(2);
			btn.setChecked(true);
		} else if ("driver".equals(type)) {
			btn = (RadioButton) flayout.getChildAt(3);
			btn.setChecked(true);
		}
		
		if("yes".equals(isGold)){
			radioBtn.setChecked(true);
		}else{
			radioBtn.setChecked(false);
		}
	}

	private void addPriceFlowView(FlowLayout flowLayout) {
		for (int i = 0; i < 4; i++) {
			RadioButton newView = new RadioButton(((Activity) mContext));
			newView.setBackgroundResource(R.drawable.square);
			if (i == 0) {
				newView.setText("全部");
				newView.setTag("all");
			} else if (i == 1) {
				newView.setText("导游");
				newView.setTag("guide");
			} else if (i == 2) {
				newView.setText("领队");
				newView.setTag("leader");
			} else {
				newView.setText("司机");
				newView.setTag("driver");
			}

			newView.setButtonDrawable(android.R.color.transparent);

			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			newView.setTextColor(Color.BLACK);

			newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton mCompoundButton,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						mCompoundButton.setTextColor(Color.WHITE);
						 type = (String) mCompoundButton.getTag();
						if (currentView == null) {

						} else {
							currentView.setTextColor(Color.BLACK);
							currentView.setChecked(false);
						}
						currentView = mCompoundButton;
					}
				}
			});
			FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					DensityUtil.dip2px(((Activity) mContext), 60),
					DensityUtil.dip2px(((Activity) mContext), 29));
			params.rightMargin = DensityUtil.dip2px(((Activity) mContext), 5);
			params.bottomMargin = DensityUtil.dip2px(((Activity) mContext), 5);
			newView.setLayoutParams(params);
			flowLayout.addView(newView);
		}
	}

	public interface OperationListener {
		  public void queryMeb(String type,String isGold);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			
			mOperationListener.queryMeb(type, isGold);
			dismiss();
			break;
		default:
			break;
		}
	}

}
