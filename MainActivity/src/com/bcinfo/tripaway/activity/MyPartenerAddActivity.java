package com.bcinfo.tripaway.activity;

import java.io.UnsupportedEncodingException;

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
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.IDCardUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MyPartenerAddActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout mSaveBtn;
	
	private EditText mNameTv;
	
	private EditText mIdNoTv;
	
	private EditText mPassportTv;
	
	private boolean flag =false;
	
	private RelativeLayout mRelativeLayout;
	
	private PartnerInfo partnerInfo;
	
	private int position;

	private EditText mEmailTv;

	private EditText mTelTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypartener_add);
		initView();
	}
	
	@Override
	protected void initView() {
		super.initView();
		mNameTv = (EditText) findViewById(R.id.partner_name);
		mIdNoTv =(EditText) findViewById(R.id.partner_idno);
		mPassportTv = (EditText) findViewById(R.id.partner_passport);
		mEmailTv =(EditText) findViewById(R.id.email);
		mTelTv = (EditText) findViewById(R.id.tel);
		mSaveBtn = (LinearLayout) findViewById(R.id.partner_save);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.second_title);
		mRelativeLayout.setBackgroundColor(Color.parseColor(getString(R.color.title_bg)));
		mSaveBtn.setOnClickListener(this);
		Intent it = getIntent();
		flag = it.getBooleanExtra("flag", false);
		if(!flag){
			setSecondTitle("添加同行人信息");
		}else{
			setSecondTitle("修改同行人信息");
			position = it.getIntExtra("position", -1);
		}
		partnerInfo = it.getParcelableExtra("partner");
		if(null != partnerInfo){
			mNameTv.setText(partnerInfo.getRealName());
			mIdNoTv.setText(partnerInfo.getIdNo());
			mPassportTv.setText(partnerInfo.getPassportNo());
			mEmailTv.setText(partnerInfo.getEmail());
			mTelTv.setText(partnerInfo.getTel());
		}
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.partner_save:
			String name = mNameTv.getText().toString();
			if(StringUtils.isEmpty(name)){
				ToastUtil.showToast(getApplicationContext(), "姓名不能为空");
				return;
			}
			if(name.length()<2||!StringUtils.isChinese(name)){
				ToastUtil.showToast(getApplicationContext(), "请输入正确的中文姓名");
				return;
			}
			String idNo  = mIdNoTv.getText().toString();
			String passPort = mPassportTv.getText().toString();
			
//			if(StringUtils.isEmpty(idNo)&&StringUtils.isEmpty(passPort)){
//				ToastUtil.showToast(getApplicationContext(), "证件号必须一个");
//				return;
//			}
			
			PartnerInfo info = new PartnerInfo();
			if(!flag){
				info.setId("");
			}else{
				info.setId(partnerInfo.getId());
			}
			info.setRealName(name);
			if(!idNo.matches("[0-9a-zA-Z]*")){
				ToastUtil.showToast(MyPartenerAddActivity.this, "身份证存在非法字符");
				return;
			}
			if(!IDCardUtil.isIdCard(idNo)){
				ToastUtil.showToast(MyPartenerAddActivity.this, "身份证号格式不对");
				return;
			}
			if(!passPort.matches("[0-9a-zA-Z]*")){
				ToastUtil.showToast(MyPartenerAddActivity.this, "证件号存在非法字符");
				return;
			}
			if(StringUtils.isChinese(idNo)==false&&idNo.length()<2){
				ToastUtil.showToast(MyPartenerAddActivity.this, "姓名格式不对");
				return;
			}
			String email=mEmailTv.getText().toString();
			String tel=mTelTv.getText().toString();
			if(!StringUtils.isEmail(email)){
				ToastUtil.showToast(MyPartenerAddActivity.this, "邮箱格式不对");
				return;
			}
			if(!StringUtils.isPhone(tel)){
				ToastUtil.showToast(MyPartenerAddActivity.this, "电话格式不对");
				return;
			}
			info.setIdNo(idNo);
			info.setPassportNo(passPort);
			info.setEmail(email);
			info.setTel(tel);
			addOrModPartner(info);
			break;
		default:
			break;
		}
	}
	
	private void addOrModPartner(final PartnerInfo info){
		try {
			JSONObject obj = new JSONObject();
			obj.put("partnerId", info.getId());
			obj.put("realName", info.getRealName());
			obj.put("passportNo", info.getPassportNo());
			obj.put("idNo", info.getIdNo());
			obj.put("email", info.getEmail());
			obj.put("tel", info.getTel());
			StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
			HttpUtil.post(Urls.my_partner, entity, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String code = response.optString("code");
					if("00000".equals(code)){
						if(!StringUtils.isEmpty(response.optString("data"))){
							info.setId(response.optString("data"));
						}
						Intent it = new Intent(MyPartenerAddActivity.this,MyInvoiceActivity.class);
						if(!flag){
							//新增
							it.putExtra("partner", info);
							setResult(1, it);
						}else{
							//更新
							it.putExtra("position", position);
							it.putExtra("partner", info);
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
}