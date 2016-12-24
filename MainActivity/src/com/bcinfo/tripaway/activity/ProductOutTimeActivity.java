package com.bcinfo.tripaway.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;

/**
 * 产品下架的界面
 * @author cuimingmin
 *
 */
public class ProductOutTimeActivity extends BaseActivity {

	
	LinearLayout back_no;
	RelativeLayout back_content;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.product_outtime);
		back_no = (LinearLayout) findViewById(R.id.back_no);
		back_content = (RelativeLayout) findViewById(R.id.back_content);
		back_content.getBackground().setAlpha(255);
		back_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				activityAnimationClose();
			}
		});
	}
	

}
