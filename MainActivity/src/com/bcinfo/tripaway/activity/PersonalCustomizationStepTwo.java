package com.bcinfo.tripaway.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.AreaAdapter;
import com.bcinfo.tripaway.bean.AreaInfo;
import com.bcinfo.tripaway.db.SqliteDBHelper;
import com.bcinfo.tripaway.utils.HanziToPinyin;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.SideBar;
import com.bcinfo.tripaway.view.SideBar.OnTouchingLetterChangedListener;
import com.bcinfo.tripaway.view.wheelview.OnWheelScrollListener;
import com.bcinfo.tripaway.view.wheelview.WheelView;
import com.bcinfo.tripaway.view.wheelview.adapter.ArrayWheelAdapter;
import com.umeng.socialize.utils.Log;

/**
 * @author
 * @date
 */
public class PersonalCustomizationStepTwo extends BaseActivity implements
		OnClickListener {

	private LinearLayout fromLinearLayout;
	private LinearLayout toLinearLayout;
	private TextView fromText;
	private TextView toText;
	private RadioButton moreRadioButton;
	private RadioButton littleRadioButton;
	private RadioButton lessRadioButton;
	private RadioButton allRadioButton;
	private Button nextStepButton;
	private String arrange;
	private static final String budgets[]=new String[]{
			"0-1000","1000-3000","3000-5000","5000-8000","8000以上"
	};
	private String budget;
	private String from;
	private String to;
	private String startDate;
	private String days;
	private String adultNum;
	private String childrenNum;
	
	private WheelView wva;

	private static final String TAG = "PersonalCustomizationStepTwo";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.personal_customization_step_two);
		// initLocation();
		initView();
		initCheckedChangeListener();
		days=getIntent().getStringExtra("days");
		adultNum=getIntent().getStringExtra("adultNum");
		childrenNum=getIntent().getStringExtra("childrenNum");
		from=getIntent().getStringExtra("from");
		to=getIntent().getStringExtra("to");
		startDate=getIntent().getStringExtra("startDate");
	}

	protected void initView() {
		setSecondTitle("我的定制需求");
		moreRadioButton = (RadioButton) findViewById(R.id.more);
		littleRadioButton = (RadioButton) findViewById(R.id.little);
		lessRadioButton = (RadioButton) findViewById(R.id.less);
		allRadioButton = (RadioButton) findViewById(R.id.all);

		nextStepButton = (Button) findViewById(R.id.nextStep);
		wva=(WheelView)this.findViewById(R.id.wheel_view);
		ArrayWheelAdapter arrayWheelAdapter=new ArrayWheelAdapter<String>(this, budgets);
		arrayWheelAdapter.setTextColor(Color.parseColor("#333333"));
		arrayWheelAdapter.setTextSize(15);
		wva.setViewAdapter(arrayWheelAdapter);
		wva.setVisibleItems(5);
		wva.setCurrentItem(2);
		budget=budgets[2];
		arrange = "紧凑点";
		setButtonEnable();	
		wva.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				budget=budgets[wva.getCurrentItem()];
            	setButtonEnable();	
			}
		});
//        wva.setOffset(1);
//        wva.setItems(Arrays.asList(budgets));
//        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//            	budget=budgets[selectedIndex-1];
//            	setButtonEnable();
//            	Log.v(TAG+"budget", budget);
//            }
//        });
        nextStepButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nextStep:
			if (TextUtils.isEmpty(budget) || TextUtils.isEmpty(arrange)
					) {
//				Intent intent = new Intent(PersonalCustomizationStepTwo.this,
//						PersonalCustomizationStepThree.class);
//				intent.putExtra("days", days);
//				intent.putExtra("adultNum", adultNum);
//				intent.putExtra("childrenNum", childrenNum);
//				intent.putExtra("from", from);
//				intent.putExtra("to", to);
//				intent.putExtra("budget", budget);
//				intent.putExtra("arrange", arrange);
//				intent.putExtra("startDate", startDate);
//				startActivity(intent);
//				activityAnimationOpen();
				
			}else {
				Intent intent = new Intent(PersonalCustomizationStepTwo.this,
						PersonalCustomizationStepThree.class);
				intent.putExtra("days", days);
				intent.putExtra("adultNum", adultNum);
				intent.putExtra("childrenNum", childrenNum);
				intent.putExtra("from", from);
				intent.putExtra("to", to);
				intent.putExtra("budget", budget);
				intent.putExtra("arrange", arrange);
				intent.putExtra("startDate", startDate);
				startActivity(intent);
				activityAnimationOpen();
			}
			break;
		}
	}

	private void initCheckedChangeListener() {
		moreRadioButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							arrange = "紧凑点";
							littleRadioButton.setChecked(false);
							lessRadioButton.setChecked(false);
							allRadioButton.setChecked(false);
							setButtonEnable();
						}
					}
				});

		littleRadioButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							arrange = "适中";
							moreRadioButton.setChecked(false);
							lessRadioButton.setChecked(false);
							allRadioButton.setChecked(false);
							setButtonEnable();
						}
					}
				});
		lessRadioButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							arrange = "宽松";
							moreRadioButton.setChecked(false);
							littleRadioButton.setChecked(false);
							allRadioButton.setChecked(false);
							setButtonEnable();
						}
					}
				});

		allRadioButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							arrange = "都可以";
							moreRadioButton.setChecked(false);
							littleRadioButton.setChecked(false);
							lessRadioButton.setChecked(false);
							setButtonEnable();
						}
					}
				});

	}

	private void initRadioButton() {
		moreRadioButton.setChecked(false);
		littleRadioButton.setChecked(false);
		lessRadioButton.setChecked(false);
		allRadioButton.setChecked(false);
	}
	
	 private void  setButtonEnable(){
		 if (TextUtils.isEmpty(budget) || TextUtils.isEmpty(arrange)
					) {
			}else{
				nextStepButton.setEnabled(true);
				nextStepButton.setBackgroundColor(Color.parseColor("#6599ff"));
			}
	 }

}
