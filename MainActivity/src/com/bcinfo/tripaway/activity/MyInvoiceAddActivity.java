package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Invoice;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MyInvoiceAddActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout mSaveBtn;
	
	private RadioGroup mTypeGp;
	
	private EditText mInvoiceTitleTv;
	
	private EditText mInvoiceNameTv;
	
	private EditText mInvoiceTelTv;
	
	private EditText mInvoiceAddressTv;
	
	private EditText mInvoicePostCodeTv;
	
	private boolean flag =false;
	
	private RelativeLayout mRelativeLayout;
	
	private Invoice invoice;
	
	private int position;
	
	private boolean isAddInvoice = false;
	
	private boolean flag1 =false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinvoice_add);
		initView();
	}
	
	@Override
	protected void initView() {
		super.initView();
		mTypeGp = (RadioGroup) findViewById(R.id.invoice_group);
		mInvoiceTitleTv = (EditText) findViewById(R.id.invoice_title);
		mInvoiceTelTv =(EditText) findViewById(R.id.invoice_tel);
		mInvoiceNameTv = (EditText) findViewById(R.id.invoice_name);
		mInvoiceAddressTv = (EditText) findViewById(R.id.invoice_address);
		mInvoicePostCodeTv = (EditText) findViewById(R.id.invoice_postCode);
		mSaveBtn = (LinearLayout) findViewById(R.id.invoice_save);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.second_title);
		mRelativeLayout.setBackgroundColor(Color.parseColor(getString(R.color.title_bg)));
		mSaveBtn.setOnClickListener(this);
		Intent it = getIntent();
		flag = it.getBooleanExtra("flag", false);
		flag1 =  it.getBooleanExtra("flag1", false);
		isAddInvoice = it.getBooleanExtra("add_invoice", false);
		if(!flag){
			setSecondTitle("添加发票信息");
		}else{
			setSecondTitle("修改发票信息");
			position = it.getIntExtra("position", -1);
		}
		invoice = it.getParcelableExtra("invoice");
		if(null != invoice){
			mInvoiceTitleTv.setText(invoice.getInvoiceTitle());
			mInvoiceTelTv.setText(invoice.getTel());	
			mInvoiceNameTv.setText(invoice.getUserName());
			mInvoiceAddressTv.setText(invoice.getAddress());
			mInvoicePostCodeTv.setText(invoice.getPostCode());
			String id = invoice.getInvoiceType();
			if("0".equals(id)){
				mTypeGp.check(R.id.person_invoice);
			}else if("1".equals(id)){
				mTypeGp.check(R.id.company_invoice);
			}
			
		}
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.invoice_save:
			String title = mInvoiceTitleTv.getText().toString();
			if(StringUtils.isEmpty(title)){
				ToastUtil.showToast(getApplicationContext(), "发票抬头不能为空");
				return;
			}
			String address  = mInvoiceAddressTv.getText().toString();
			String userName = mInvoiceNameTv.getText().toString();
			String tel = mInvoiceTelTv.getText().toString();
			String postCode = mInvoicePostCodeTv.getText().toString();
			
			if(!verifyIsPhone(tel)){
				ToastUtil.showToast(MyInvoiceAddActivity.this, "请输入正确的手机号");
				return;
			}
			if(!StringUtils.isNumeric(postCode)){
				ToastUtil.showToast(MyInvoiceAddActivity.this, "请输入正确邮编");
				return;
			}
			Invoice info = new Invoice();
			if(!flag){
				info.setInvoiceId("");
			}else{
				info.setInvoiceId(invoice.getInvoiceId());
			}
			info.setInvoiceTitle(title);
			info.setPostCode(postCode);
			info.setTel(tel);
			info.setUserName(userName);
			info.setAddress(address);
			
			//发票类型
			int id = mTypeGp.getCheckedRadioButtonId();
			if(id == R.id.person_invoice){
				info.setInvoiceType("0");
			}else if(id == R.id.company_invoice){
				info.setInvoiceType("1");
			}
			addOrModInvoice(info,flag1);
			break;
		default:
			break;
		}
	}
	
	private void addOrModInvoice(final Invoice info,final boolean flags){
		try {
			JSONObject obj = new JSONObject();
			obj.put("invoiceId", info.getInvoiceId());
			obj.put("invoiceTitle", info.getInvoiceTitle());
			obj.put("invoiceType", info.getInvoiceType());
			obj.put("userName", info.getUserName());
			obj.put("area", info.getArea()==null?"":info.getArea());
			obj.put("address", info.getAddress());
			obj.put("tel", info.getTel());
			obj.put("email", info.getEmail()==null?"":info.getEmail());
			obj.put("alias", info.getAlias()==null?"":info.getAlias());
			obj.put("postCode", info.getPostCode());
			obj.put("isDefault", "yes");
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.my_invoice, entity, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if("00000".equals(code)){
						Intent it = new Intent(MyInvoiceAddActivity.this,MyInvoice2Activity.class);
						if(!flag){
							if(flags){
								String ids =  response.optString("data");
								info.setInvoiceId(ids);
								Intent its = new Intent(MyInvoiceAddActivity.this,ConfirmPayActivity3.class);
								its.putExtra("invoice", info);
								setResult(1, its);
							}else{
								//新增
								setResult(1, it);
							}
						}else{
							//更新
							it.putExtra("position", position);
							it.putExtra("invoice", info);
							setResult(2, it);
						}
						activityAnimationClose();
						finish();
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	   public  boolean verifyIsPhone(String accountStr)
	    {
	    	Pattern pattern = Pattern.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13\\d{9}|14[57]\\d{8}|15\\d{9}|18\\d{9}$");
	        Matcher matcher = pattern.matcher(accountStr);
	        if (matcher.matches())
	        {
	            return true;
	        }
	        else
	        {
	            return false;
	        }
	    }
}