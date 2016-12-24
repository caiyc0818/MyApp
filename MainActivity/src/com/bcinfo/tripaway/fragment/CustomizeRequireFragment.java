package com.bcinfo.tripaway.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.LocationSelectAreaActivity;
import com.bcinfo.tripaway.activity.PersonalCustomizationStepOne;
import com.bcinfo.tripaway.activity.PersonalCustomizationStepTwo;
import com.bcinfo.tripaway.view.wheelview.OnWheelScrollListener;
import com.bcinfo.tripaway.view.wheelview.WheelView;
import com.bcinfo.tripaway.view.wheelview.adapter.NumericWheelAdapter;
import com.umeng.socialize.utils.Log;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker.OnDateChangedListener;

public class CustomizeRequireFragment extends BaseActivity implements
		OnClickListener {

	private LinearLayout fromLinearLayout;
	private LinearLayout toLinearLayout;
	private LinearLayout dateLinearLayout;
	private TextView fromText;
	private TextView toText;
	private TextView dateTextView;
	private RadioGroup tripTimeRadioGroup;
	private RadioGroup adultNumRadioGroup;
	private RadioGroup childrenNumRadioGroup;
	private Button nextStepButton;
	private String tripTime;
	private String adultNum;
	private String childrenNum="0";
	private String from;
	private String to;
	private String startDate;
	private AlertDialog dateTimeDialog;
	private DatePicker datePicker;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private LinearLayout layout_back_button;

	private static final String TAG = "CustomizeRequireFragment";


	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	    	statisticsTitle="发布定制";
	    	setContentView(R.layout.personal_customization_step_one);
			initView();
			initCheckedChangeListener();
			
	    }
	 
	protected void initView() {
		// setSecondTitle("发布定制");
		 RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
		 titleLayout.setVisibility(View.VISIBLE);
	        titleLayout.setAlpha(1);
	        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
		fromLinearLayout = (LinearLayout) findViewById(R.id.from);
		layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		layout_back_button.setOnClickListener(this);
		toLinearLayout = (LinearLayout) findViewById(R.id.to);
		dateLinearLayout = (LinearLayout) findViewById(R.id.date);
		fromText = (TextView) findViewById(R.id.from_text);
		toText = (TextView) findViewById(R.id.to_text);
		dateTextView = (TextView) findViewById(R.id.dateTextView);
		tripTimeRadioGroup = (RadioGroup) findViewById(R.id.trip_time);
		adultNumRadioGroup = (RadioGroup) findViewById(R.id.audlt_num);
		childrenNumRadioGroup = (RadioGroup) findViewById(R.id.children_num);
		nextStepButton = (Button) findViewById(R.id.nextStep);
		fromLinearLayout.setOnClickListener(this);
		toLinearLayout.setOnClickListener(this);
		dateLinearLayout.setOnClickListener(this);
		nextStepButton.setOnClickListener(this);
		

	}

	public void initData() {
		fromText.setText("");
		toText.setText("");
		dateTextView.setText("");
		for (int i = 0; i < tripTimeRadioGroup.getChildCount(); i++) {
			if (((RadioButton) tripTimeRadioGroup.getChildAt(i)).isChecked()) {
				((RadioButton) tripTimeRadioGroup.getChildAt(i))
						.setChecked(false);
			}
		}
		tripTimeRadioGroup.clearCheck();
		for (int i = 0; i < adultNumRadioGroup.getChildCount(); i++) {
			if (((RadioButton) adultNumRadioGroup.getChildAt(i)).isChecked()) {

				((RadioButton) adultNumRadioGroup.getChildAt(i))
						.setChecked(false);
			}
		}
		adultNumRadioGroup.clearCheck();
		for (int i = 0; i < childrenNumRadioGroup.getChildCount(); i++) {
			if (((RadioButton) childrenNumRadioGroup.getChildAt(i)).isChecked()) {

				((RadioButton) childrenNumRadioGroup.getChildAt(i))
						.setChecked(false);
			}
		}
		childrenNumRadioGroup.clearCheck();
		 tripTime="";
		 adultNum="";
		childrenNum="0";
		 from="";
		 to="";
	 startDate=null;
		nextStepButton.setEnabled(false);
		nextStepButton.setBackgroundColor(Color.parseColor("#CCCCCC"));
	}

	protected void initCheckedChangeListener() {
		for (int i = 0; i < tripTimeRadioGroup.getChildCount(); i++) {
			((RadioButton) tripTimeRadioGroup.getChildAt(i))
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {
								tripTime = buttonView.getText().toString();
								buttonView.setTextColor(getResources().getColor(R.color.white));
								if("14".equals(tripTime)){
									tripTime = "以上";
								}
								for(int j = 0;j<tripTimeRadioGroup.getChildCount(); j++){
									String txt = ((RadioButton) tripTimeRadioGroup.getChildAt(j)).getText().toString();
									if(!tripTime.equals(txt)){
										((RadioButton) tripTimeRadioGroup.getChildAt(j)).setTextColor(Color.parseColor("#333333"));
									}
								}
								if(tripTime.equals("以上"))
									tripTime="14";
								Log.v(TAG + "tripTime", tripTime);
								setButtonEnable();
							}
						}
					});
		}

		for (int i = 0; i < adultNumRadioGroup.getChildCount(); i++) {
			((RadioButton) adultNumRadioGroup.getChildAt(i))
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {
								adultNum = buttonView.getText().toString();
								buttonView.setTextColor(getResources().getColor(R.color.white));
								if("14".equals(adultNum)){
									adultNum = "以上";
								}
								for(int j = 0;j<adultNumRadioGroup.getChildCount(); j++){
									String txt = ((RadioButton) adultNumRadioGroup.getChildAt(j)).getText().toString();
									if(!adultNum.equals(txt)){
										((RadioButton) adultNumRadioGroup.getChildAt(j)).setTextColor(Color.parseColor("#333333"));
									}
								}
							if(adultNum.equals("以上"))
								adultNum="14";
								Log.v(TAG + "adultNum", adultNum);
								setButtonEnable();
							}
						}
					});
		}
		for (int i = 0; i < childrenNumRadioGroup.getChildCount(); i++) {
			((RadioButton) childrenNumRadioGroup.getChildAt(i))
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {
								childrenNum = buttonView.getText().toString();
								buttonView.setTextColor(getResources().getColor(R.color.white));
								if("14".equals(childrenNum)){
									childrenNum = "以上";
								}
								for(int j = 0;j<childrenNumRadioGroup.getChildCount(); j++){
									String txt = ((RadioButton) childrenNumRadioGroup.getChildAt(j)).getText().toString();
									if(!childrenNum.equals(txt)){
										((RadioButton) childrenNumRadioGroup.getChildAt(j)).setTextColor(Color.parseColor("#333333"));
									}
								}
								if(childrenNum.equals("以上"))
									childrenNum="14";
								Log.v(TAG + "childrenNum", childrenNum);
								setButtonEnable();
							}
						}
					});
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.from:
			Intent cityIntent = new Intent(CustomizeRequireFragment.this,
					LocationSelectAreaActivity.class);
			if (!TextUtils.isEmpty(fromText.getText()))
				cityIntent.putExtra("areaAddress", fromText.getText()
						.toString());
			startActivityForResult(cityIntent, 105);
			animationOpen();
			break;

		case R.id.to:
			Intent cityIntent1 = new Intent(CustomizeRequireFragment.this,
					LocationSelectAreaActivity.class);
			if (!TextUtils.isEmpty(fromText.getText()))
				cityIntent1
						.putExtra("areaAddress", toText.getText().toString());
			startActivityForResult(cityIntent1, 106);
			animationOpen();
			break;
		case R.id.date:
			dateTimePickerDialog();
			break;
		case R.id.layout_back_button:
			setResult(1002);
			finish();
			break;
		case R.id.ok_button:
			int startYear=year.getCurrentItem()+2015;
			int startMonth=month.getCurrentItem()+1;
			int startDay=day.getCurrentItem()+1;
			startDate=startYear+""+String.format("%02d", startMonth)+""+String.format("%02d", startDay);
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			if (startDate == null) {
//				startDate = format.format(new Date());
//			}
//			Date date;
//			try {
//				date = format.parse(startDate);
//				SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
//				dateTextView.setText(format1.format(date));
//				dateTimeDialog.cancel();
//				setButtonEnable();
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			setButtonEnable();
			dateTextView.setText((startYear+1)+"年"+String.format("%02d", startMonth)+"月"+String.format("%02d", startDay)+"日");
			dateTimeDialog.cancel();
			break;
		case R.id.nextStep:
			if (TextUtils.isEmpty(tripTime) || TextUtils.isEmpty(adultNum)
					|| TextUtils.isEmpty(childrenNum)
					|| TextUtils.isEmpty(from) || TextUtils.isEmpty(to)
					|| TextUtils.isEmpty(startDate)) {
				// Intent intent = new Intent(CustomizeRequireFragment.this,
				// PersonalCustomizationStepTwo.class);
				// intent.putExtra("days", tripTime);
				// intent.putExtra("adultNum", adultNum);
				// intent.putExtra("childrenNum", childrenNum);
				// intent.putExtra("from", from);
				// intent.putExtra("to", to);
				// intent.putExtra("startDate", startDate);
				// startActivity(intent);
				// animationOpen();
			} else {
				Intent intent = new Intent(CustomizeRequireFragment.this,
						PersonalCustomizationStepTwo.class);
				intent.putExtra("days", tripTime);
				intent.putExtra("adultNum", adultNum);
				intent.putExtra("childrenNum", childrenNum);
				intent.putExtra("from", from);
				intent.putExtra("to", to);
				intent.putExtra("startDate", startDate);
				startActivity(intent);
				animationOpen();
			}

		}
	}

	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {

		case 105:
			if (arg1 == -1) {
				// LogUtil.i(TAG, "area", arg2.getStringExtra("area"));
				String areaAddress = arg2.getStringExtra("area");
				fromText.setText(areaAddress);
				from = areaAddress;
				setButtonEnable();
			}
			break;
		case 106:
			if (arg1 == -1) {
				// LogUtil.i(TAG, "area", arg2.getStringExtra("area"));
				String areaAddress = arg2.getStringExtra("area");
				toText.setText(areaAddress);
				to = areaAddress;
				setButtonEnable();
			}
			break;
		default:
			break;
		}
	}

	@SuppressLint("ResourceAsColor")
	private void dateTimePickerDialog() {
		dateTimeDialog = new AlertDialog.Builder(CustomizeRequireFragment.this).create();
		dateTimeDialog.show();
		Window window = dateTimeDialog.getWindow();
		WindowManager manager = (WindowManager) CustomizeRequireFragment.this.getSystemService(Activity.WINDOW_SERVICE);
	    int width;

	    if (Build.VERSION.SDK_INT > VERSION_CODES.FROYO) {
	        width = manager.getDefaultDisplay().getWidth();
	    } else {
	        Point point = new Point();
	        manager.getDefaultDisplay().getSize(point);
	        width = point.x;
	    }
		 window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
		 WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		    lp.copyFrom(dateTimeDialog.getWindow().getAttributes());
		    lp.width = width;
		    dateTimeDialog.getWindow().setAttributes(lp);
		window.setContentView(R.layout.wheel_date_picker);
//		datePicker = (DatePicker) window.findViewById(R.id.datePicker);
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
year = (WheelView) window.findViewById(R.id.year);
		NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(CustomizeRequireFragment.this,norYear, 2050); 
//		numericWheelAdapter1.setTextSize(2);
//		numericWheelAdapter1.setTextColor(Color.BLACK);
		numericWheelAdapter1.setLabel("年");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(false);//是否可循环滑动
		year.addScrollingListener(scrollListener);
		year.setBackgroundColor(Color.WHITE);
		month = (WheelView) window.findViewById(R.id.month);
		NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(CustomizeRequireFragment.this,1, 12, "%02d"); 
		numericWheelAdapter2.setTextColor(Color.BLACK);
		numericWheelAdapter2.setLabel("月");
//		numericWheelAdapter2.setTextColor(Color.BLACK);
		month.setBackgroundColor(Color.WHITE);
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(false);
		month.addScrollingListener(scrollListener);
		
		day = (WheelView) window.findViewById(R.id.day);
		initDay(norYear,curMonth);
		day.setCyclic(false);
		
		year.setVisibleItems(5);//设置显示行数
		month.setVisibleItems(5);
		day.setVisibleItems(5);

		year.setCurrentItem(norYear - 2015);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		Button ok = (Button) window.findViewById(R.id.ok_button);
		Button cancel = (Button) window.findViewById(R.id.cancel_button);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dateTimeDialog.cancel();
			}
		});
//		Calendar c = Calendar.getInstance();
//		// 初始化DatePicker组件，初始化时指定监听器
//		datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
//				c.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
//
//					@Override
//					public void onDateChanged(DatePicker arg0, int year,
//							int month, int day) {
//
//						SimpleDateFormat format = new SimpleDateFormat(
//								"yyyyMMdd");
//						Calendar c = Calendar.getInstance();
//
//						c.set(year, month, day, 0, 0, 0);
//						startDate = format.format(c.getTime());
//						Log.v(TAG, startDate);
//						setButtonEnable();
//					}
//				});
	}

	
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 2015;//年
			int n_month = month.getCurrentItem() + 1;//月
			
			initDay(n_year,n_month);
			
		}
	};
	
	/**
	 * 页面启动动画
	 */
	protected void animationOpen() {
		CustomizeRequireFragment.this.overridePendingTransition(R.anim.activity_new,
				R.anim.activity_out);
	}

	private void setButtonEnable() {
		if (TextUtils.isEmpty(tripTime) || TextUtils.isEmpty(adultNum)
				|| TextUtils.isEmpty(childrenNum) || TextUtils.isEmpty(from)
				|| TextUtils.isEmpty(to) || TextUtils.isEmpty(startDate)) {
		} else {
			nextStepButton.setEnabled(true);
			nextStepButton.setBackgroundColor(Color.parseColor("#6599ff"));
		}
	}
	
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(CustomizeRequireFragment.this,1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
//		numericWheelAdapter.setTextColor(Color.BLACK);
		day.setBackgroundColor(Color.WHITE);
		day.setViewAdapter(numericWheelAdapter);
	}
}
