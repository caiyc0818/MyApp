package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.MyInvoiceActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class CommonInfoActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout mInvoice;
	private RelativeLayout mCommonPartner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_info);
		setSecondTitle("我的");
		statisticsTitle="常用信息";
		mInvoice=(RelativeLayout) findViewById(R.id.my_invoice_layout);
		mCommonPartner=(RelativeLayout) findViewById(R.id.common_partner_layout);
		mInvoice.setOnClickListener(this);
		mCommonPartner.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent it;
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.my_invoice_layout:
			it = new Intent(CommonInfoActivity.this,MyInvoice2Activity.class);
			startActivity(it);
			animationOpen();
			break;
		case R.id.common_partner_layout:
			it = new Intent(CommonInfoActivity.this,MyInvoiceActivity.class);
			startActivity(it);
			animationOpen();
			break;
		default:
			break;
		}
	}
	 protected void animationOpen()
	    {
	        CommonInfoActivity.this.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
	    }
	
}
