package com.bcinfo.tripaway.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.ToastUtil;

public class AppariseDialog extends Dialog implements OnClickListener{

	private Context context;
	
	private OnAppariseListener onAppariseListener;
	
	private  LayoutInflater mInflater;
	
	private LayoutParams mLp;
	
	private int satScore = 0;
	
	private int descScore = 0;
	
	private int serScore = 0;
	
	private boolean satFlag = false;
	
	private boolean satFlag1 = false;
	
	private boolean satFlag2 = false;
	
	private boolean satFlag3 = false;
	
	private boolean satFlag4 = false;
	
	private boolean satOthFlag = false;
	
	private boolean satOthFlag1 = false;
	
	private boolean satOthFlag2 = false;
	
	private boolean satOthFlag3 = false;
	
	
	private boolean descFlag = false;
	
	private boolean descFlag1 = false;
	
	private boolean descFlag2 = false;
	
	private boolean descFlag3 = false;
	
	private boolean descFlag4 = false;
	
	private boolean descOthFlag = false;
	
	private boolean descOthFlag1 = false;
	
	private boolean descOthFlag2 = false;
	
	private boolean descOthFlag3 = false;
	
	private boolean serFlag = false;

	private boolean serFlag1 = false;
	
	private boolean serFlag2 = false;
	
	private boolean serFlag3 = false;
	
	private boolean serFlag4 = false;
	
	private boolean serOthFlag = false;
	
	private boolean serOthFlag1 = false;
	
	private boolean serOthFlag2 = false;
	
	private boolean serOthFlag3 = false;
	
	/**
	 * 满意按钮
	 */
	private ImageView mSatBtn1;
	
	private ImageView mSatBtn2;
	
	private ImageView mSatBtn3;
	
	private ImageView mSatBtn4;
	
	private ImageView mSatBtn5;
	
	/**
	 * 描述按钮
	 */
	private ImageView mDescBtn1;
	
	private ImageView mDescBtn2;
	
	private ImageView mDescBtn3;
	
	private ImageView mDescBtn4;
	
	private ImageView mDescBtn5;
	
	
	/**
	 * 服务按钮
	 */
	private ImageView mSerBtn1;
	
	private ImageView mSerBtn2;
	
	private ImageView mSerBtn3;
	
	private ImageView mSerBtn4;
	
	private ImageView mSerBtn5;
	
	private TextView mSatTv;
	
	private TextView mDescTv;
	
	private TextView mSerTv;
	
	/**
	 * 字数限制
	 */
	private TextView mEditTv;
	
	private EditText mApprEt;
	
	private Button mCancel;
	
	private Button mSubmit;
	
	/**
	 * 
	 * @param context
	 * @param onAppariseListener
	 */
	public AppariseDialog(Context context,OnAppariseListener onAppariseListener) {
		super(context,R.style.Dialog);
		this.context = context;
		this.onAppariseListener = onAppariseListener;
		mLp = getWindow().getAttributes();
		mLp.gravity = Gravity.CENTER_VERTICAL;
//		mLp.dimAmount = (float) 0.75; // 去背景遮盖
		mLp.alpha = 1.0f;
		mLp.width = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(mLp);
		//初始化控件
		initDialog();
	}
	
	private void initDialog(){
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = mInflater.inflate(R.layout.my_jour_appraise_dialog, null);
		setContentView(layout);
		mSatBtn1 = (ImageView) findViewById(R.id.sat_btn_1);
		mSatBtn2 = (ImageView) findViewById(R.id.sat_btn_2);
		mSatBtn3 = (ImageView) findViewById(R.id.sat_btn_3);
		mSatBtn4 = (ImageView) findViewById(R.id.sat_btn_4);
		mSatBtn5 = (ImageView) findViewById(R.id.sat_btn_5);
		mSatBtn1.setOnClickListener(this);
		mSatBtn2.setOnClickListener(this);
		mSatBtn3.setOnClickListener(this);
		mSatBtn4.setOnClickListener(this);
		mSatBtn5.setOnClickListener(this);
		
		mDescBtn1 = (ImageView) findViewById(R.id.desc_btn_1);
		mDescBtn2 = (ImageView) findViewById(R.id.desc_btn_2);
		mDescBtn3 = (ImageView) findViewById(R.id.desc_btn_3);
		mDescBtn4 = (ImageView) findViewById(R.id.desc_btn_4);
		mDescBtn5 = (ImageView) findViewById(R.id.desc_btn_5);
		mDescBtn1.setOnClickListener(this);
		mDescBtn2.setOnClickListener(this);
		mDescBtn3.setOnClickListener(this);
		mDescBtn4.setOnClickListener(this);
		mDescBtn5.setOnClickListener(this);
		
		mSerBtn1 = (ImageView) findViewById(R.id.ser_btn_1);
		mSerBtn2 = (ImageView) findViewById(R.id.ser_btn_2);
		mSerBtn3 = (ImageView) findViewById(R.id.ser_btn_3);
		mSerBtn4 = (ImageView) findViewById(R.id.ser_btn_4);
		mSerBtn5 = (ImageView) findViewById(R.id.ser_btn_5);
		mSerBtn1.setOnClickListener(this);
		mSerBtn2.setOnClickListener(this);
		mSerBtn3.setOnClickListener(this);
		mSerBtn4.setOnClickListener(this);
		mSerBtn5.setOnClickListener(this);
		
		mSatTv = (TextView) findViewById(R.id.sat_text_info);
		mDescTv = (TextView) findViewById(R.id.desc_text_info);
		mSerTv = (TextView) findViewById(R.id.ser_text_info);
		mEditTv = (TextView) findViewById(R.id.appraise_text_count);
 		
		mCancel = (Button) findViewById(R.id.app_cancel);
		mSubmit = (Button) findViewById(R.id.app_submit);
		mCancel.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		
		mApprEt = (EditText) findViewById(R.id.appaise_edittext);
	}
	

	public interface OnAppariseListener{
		public void appariseOrder(String content,int descScore,int serviceScore,int satisScore);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sat_btn_1:
			satFlag1 = false;
			satFlag2 = false;
			satFlag3 = false;
			satFlag4 = false;
			satOthFlag1 = false;
			satOthFlag2 = false;
			satOthFlag3 = false;
			
			if(!satFlag||satOthFlag){
				mSatBtn1.setImageResource(R.drawable.yellow_star);
				mSatBtn2.setImageResource(R.drawable.gray_star);
				mSatBtn3.setImageResource(R.drawable.gray_star);
				mSatBtn4.setImageResource(R.drawable.gray_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 1;
				mSatTv.setText("一般");
			}else{
				mSatBtn1.setImageResource(R.drawable.gray_star);
				mSatBtn2.setImageResource(R.drawable.gray_star);
				mSatBtn3.setImageResource(R.drawable.gray_star);
				mSatBtn4.setImageResource(R.drawable.gray_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 0;
				mSatTv.setText("请选择");
			}
			if(!satOthFlag){
				satFlag =!satFlag;
			}
			satOthFlag = false;
			break;
		case R.id.sat_btn_2:
			satFlag =true;
			satFlag2 = false;
			satFlag3 = false;
			satFlag4 = false;
			satOthFlag = true;
			satOthFlag2 = false;
			satOthFlag3 = false;
			if(!satFlag1||satOthFlag1){
				mSatBtn2.setImageResource(R.drawable.yellow_star);
				mSatBtn1.setImageResource(R.drawable.yellow_star);
				mSatBtn3.setImageResource(R.drawable.gray_star);
				mSatBtn4.setImageResource(R.drawable.gray_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 2;
				mSatTv.setText("一般");
			}else{
				mSatBtn2.setImageResource(R.drawable.gray_star);
				mSatBtn3.setImageResource(R.drawable.gray_star);
				mSatBtn4.setImageResource(R.drawable.gray_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 1;
				mSatTv.setText("一般");
			}
			if(!satOthFlag1){
				satFlag1 =!satFlag1;
			}
			satOthFlag1 = false;
			break;
		case R.id.sat_btn_3:
			satFlag =true;
			satFlag1 = true;
			satFlag3 = false;
			satFlag4 = false;
			satOthFlag = true;
			satOthFlag1 = true;
			satOthFlag3 = false;
			if(!satFlag2||satOthFlag2){
				mSatBtn3.setImageResource(R.drawable.yellow_star);
				mSatBtn2.setImageResource(R.drawable.yellow_star);
				mSatBtn1.setImageResource(R.drawable.yellow_star);
				mSatBtn4.setImageResource(R.drawable.gray_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 3;
				mSatTv.setText("一般");
			}else{
				mSatBtn3.setImageResource(R.drawable.gray_star);
				mSatBtn4.setImageResource(R.drawable.gray_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 2;
				mSatTv.setText("一般");
			}
			if(!satOthFlag2){
				satFlag2 =!satFlag2;
			}
			satOthFlag2= false;
			break;
		case R.id.sat_btn_4:
			satFlag =true;
			satFlag1 = true;
			satFlag2 = true;
			satFlag4 = false;
			satOthFlag = true;
			satOthFlag1 = true;
			satOthFlag2 = true;
			if(!satFlag3||satOthFlag3){
				mSatBtn4.setImageResource(R.drawable.yellow_star);
				mSatBtn3.setImageResource(R.drawable.yellow_star);
				mSatBtn2.setImageResource(R.drawable.yellow_star);
				mSatBtn1.setImageResource(R.drawable.yellow_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 4;
				mSatTv.setText("满意");
			}else{
				mSatBtn4.setImageResource(R.drawable.gray_star);
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 3;
				mSatTv.setText("一般");
			}
			if(!satOthFlag3){
				satFlag3 =!satFlag3;
			}
			satOthFlag3 = false;
			break;
		case R.id.sat_btn_5:
			satFlag =true;
			satFlag1 = true;
			satFlag2 = true;
			satFlag3 = true;
			satOthFlag = true;
			satOthFlag1 = true;
			satOthFlag2 = true;
			satOthFlag3 = true;
			if(!satFlag4){
				mSatBtn5.setImageResource(R.drawable.yellow_star);
				mSatBtn4.setImageResource(R.drawable.yellow_star);
				mSatBtn3.setImageResource(R.drawable.yellow_star);
				mSatBtn2.setImageResource(R.drawable.yellow_star);
				mSatBtn1.setImageResource(R.drawable.yellow_star);
				satScore = 5;
				mSatTv.setText("非常满意");
			}else{
				mSatBtn5.setImageResource(R.drawable.gray_star);
				satScore = 4;
				mSatTv.setText("满意");
			}
			satFlag4 =!satFlag4;
			break;
		case R.id.desc_btn_1:
			descFlag1 = false;
			descFlag2 = false;
			descFlag3 = false;
			descFlag4 = false;
			descOthFlag1 = false;
			descOthFlag2 = false;
			descOthFlag3 = false;
			if(!descFlag||descOthFlag){
				mDescBtn1.setImageResource(R.drawable.yellow_star);
				mDescBtn2.setImageResource(R.drawable.gray_star);
				mDescBtn3.setImageResource(R.drawable.gray_star);
				mDescBtn4.setImageResource(R.drawable.gray_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 1;
				mDescTv.setText("一般");
			}else{
				mDescBtn1.setImageResource(R.drawable.gray_star);
				mDescBtn2.setImageResource(R.drawable.gray_star);
				mDescBtn3.setImageResource(R.drawable.gray_star);
				mDescBtn4.setImageResource(R.drawable.gray_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 0;
				mDescTv.setText("请选择");
			}
			if(!descOthFlag){
				descFlag =!descFlag;
			}
			descOthFlag= false;
			break;
		case R.id.desc_btn_2:
			descFlag = true;
			descFlag2 = false;
			descFlag3 = false;
			descFlag4 = false;
			descOthFlag = true;
			descOthFlag2 = false;
			descOthFlag3 = false;
			if(!descFlag1||descOthFlag1){
				mDescBtn2.setImageResource(R.drawable.yellow_star);
				mDescBtn1.setImageResource(R.drawable.yellow_star);
				mDescBtn3.setImageResource(R.drawable.gray_star);
				mDescBtn4.setImageResource(R.drawable.gray_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 2;
				mDescTv.setText("一般");
			}else{
				mDescBtn2.setImageResource(R.drawable.gray_star);
				mDescBtn3.setImageResource(R.drawable.gray_star);
				mDescBtn4.setImageResource(R.drawable.gray_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 1;
				mDescTv.setText("一般");
			}
			if(!descOthFlag1){
				descFlag1 =!descFlag1;
			}
			descOthFlag1= false;
			break;
		case R.id.desc_btn_3:
			descFlag1 = true;
			descFlag = true;
			descFlag3 = false;
			descFlag4 = false;
			descOthFlag = true;
			descOthFlag1 = true;
			descOthFlag3 = false;
			if(!descFlag2||descOthFlag2){
				mDescBtn3.setImageResource(R.drawable.yellow_star);
				mDescBtn1.setImageResource(R.drawable.yellow_star);
				mDescBtn2.setImageResource(R.drawable.yellow_star);
				mDescBtn4.setImageResource(R.drawable.gray_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 3;
				mDescTv.setText("一般");
			}else{
				mDescBtn3.setImageResource(R.drawable.gray_star);
				mDescBtn4.setImageResource(R.drawable.gray_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 2;
				mDescTv.setText("一般");
			}
			if(!descOthFlag2){
				descFlag2 =!descFlag2;
			}
			descOthFlag2= false;
			break;
		case R.id.desc_btn_4:
			descFlag1 = true;
			descFlag = true;
			descFlag2 = true;
			descFlag4 = false;
			descOthFlag = true;
			descOthFlag1 = true;
			descOthFlag2 = true;
			if(!descFlag3||descOthFlag3){
				mDescBtn4.setImageResource(R.drawable.yellow_star);
				mDescBtn1.setImageResource(R.drawable.yellow_star);
				mDescBtn2.setImageResource(R.drawable.yellow_star);
				mDescBtn3.setImageResource(R.drawable.yellow_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 4;
				mDescTv.setText("满意");
			}else{
				mDescBtn4.setImageResource(R.drawable.gray_star);
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 3;
				mDescTv.setText("一般");
			}
			if(!descOthFlag3){
				descFlag3 =!descFlag3;
			}
			descOthFlag3= false;
			break;
		case R.id.desc_btn_5:
			descFlag1 = true;
			descFlag = true;
			descFlag2 = true;
			descFlag3 = true;
			descOthFlag = true;
			descOthFlag1 = true;
			descOthFlag2 = true;
			descOthFlag3 = true;
			if(!descFlag4){
				mDescBtn5.setImageResource(R.drawable.yellow_star);
				mDescBtn4.setImageResource(R.drawable.yellow_star);
				mDescBtn1.setImageResource(R.drawable.yellow_star);
				mDescBtn2.setImageResource(R.drawable.yellow_star);
				mDescBtn3.setImageResource(R.drawable.yellow_star);
				descScore = 5;
				mDescTv.setText("非常满意");
			}else{
				mDescBtn5.setImageResource(R.drawable.gray_star);
				descScore = 4;
				mDescTv.setText("满意");
			}
			descFlag4 =!descFlag4;
			break;
		case R.id.ser_btn_1:
			serFlag1 = false;
			serFlag4 = false;
			serFlag2 = false;
			serFlag3 = false;
			serOthFlag1 = false;
			serOthFlag2 = false;
			serOthFlag3 = false;
			if(!serFlag||serOthFlag){
				mSerBtn1.setImageResource(R.drawable.yellow_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				mSerBtn4.setImageResource(R.drawable.gray_star);
				mSerBtn2.setImageResource(R.drawable.gray_star);
				mSerBtn3.setImageResource(R.drawable.gray_star);
				serScore = 1;
				mSerTv.setText("一般");
			}else{
				mSerBtn1.setImageResource(R.drawable.gray_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				mSerBtn4.setImageResource(R.drawable.gray_star);
				mSerBtn2.setImageResource(R.drawable.gray_star);
				mSerBtn3.setImageResource(R.drawable.gray_star);
				serScore = 0;
				mSerTv.setText("请选择");
			}
			if(!serOthFlag){
				serFlag =!serFlag;
			}
			serOthFlag = false;
			break;
		case R.id.ser_btn_2:
			serFlag = true;
			serFlag4 = false;
			serFlag2 = false;
			serFlag3 = false;
			serOthFlag = true;
			serOthFlag2 = false;
			serOthFlag3 = false;
			if(!serFlag1||serOthFlag1){
				mSerBtn2.setImageResource(R.drawable.yellow_star);
				mSerBtn1.setImageResource(R.drawable.yellow_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				mSerBtn4.setImageResource(R.drawable.gray_star);
				mSerBtn3.setImageResource(R.drawable.gray_star);
				serScore = 2;
				mSerTv.setText("一般");
			}else{
				mSerBtn2.setImageResource(R.drawable.gray_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				mSerBtn4.setImageResource(R.drawable.gray_star);
				mSerBtn3.setImageResource(R.drawable.gray_star);
				serScore = 1;
				mSerTv.setText("一般");
			}
			if(!serOthFlag){
				serFlag1 =!serFlag1;
			}
			serOthFlag =false;
			break;
		case R.id.ser_btn_4:
			serFlag = true;
			serFlag4 = false;
			serFlag1 = true;
			serFlag2 = true;
			serOthFlag1 = true;
			serOthFlag2 = true;
			serOthFlag = true;
			if(!serFlag3||serOthFlag3){
				mSerBtn4.setImageResource(R.drawable.yellow_star);
				mSerBtn2.setImageResource(R.drawable.yellow_star);
				mSerBtn1.setImageResource(R.drawable.yellow_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				mSerBtn3.setImageResource(R.drawable.yellow_star);
				serScore = 4;
				mSerTv.setText("满意");
			}else{
				mSerBtn4.setImageResource(R.drawable.gray_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				serScore = 3;
				mSerTv.setText("一般");
			}
			if(!serOthFlag3){
				serFlag3 =!serFlag3;
			}
			serOthFlag3 = false;
			break;
		case R.id.ser_btn_3:
			serFlag = true;
			serFlag4 = false;
			serFlag1 = true;
			serFlag3 = false;
			serOthFlag1 = true;
			serOthFlag3 = false;
			serOthFlag = true;
			if(!serFlag2||serOthFlag2){
				mSerBtn3.setImageResource(R.drawable.yellow_star);
				mSerBtn2.setImageResource(R.drawable.yellow_star);
				mSerBtn1.setImageResource(R.drawable.yellow_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				mSerBtn4.setImageResource(R.drawable.gray_star);
				serScore = 3;
				mSerTv.setText("一般");
			}else{
				mSerBtn3.setImageResource(R.drawable.gray_star);
				mSerBtn5.setImageResource(R.drawable.gray_star);
				mSerBtn4.setImageResource(R.drawable.gray_star);
				serScore = 2;
				mSerTv.setText("一般");
			}
			if(!serOthFlag2){
				serFlag2 =!serFlag2;
			}
			serOthFlag2 = false;
			break;
		case R.id.ser_btn_5:
			serFlag = true;
			serFlag2 = true;
			serFlag1 = true;
			serFlag3 = true;
			serOthFlag1 = true;
			serOthFlag2 = true;
			serOthFlag3 = true;
			serOthFlag = true;
			if(!serFlag4){
				mSerBtn5.setImageResource(R.drawable.yellow_star);
				mSerBtn3.setImageResource(R.drawable.yellow_star);
				mSerBtn2.setImageResource(R.drawable.yellow_star);
				mSerBtn1.setImageResource(R.drawable.yellow_star);
				mSerBtn4.setImageResource(R.drawable.yellow_star);
				serScore = 5;
				mSerTv.setText("非常满意");
			}else{
				mSerBtn5.setImageResource(R.drawable.gray_star);
				serScore = 4;
				mSerTv.setText("满意");
			}
			serFlag4 =!serFlag4;
			break;
		case R.id.app_cancel:
			this.dismiss();
			break;
		case R.id.app_submit:
			String content=mApprEt.getText().toString();
			onAppariseListener.appariseOrder(content,descScore,serScore,satScore);
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
