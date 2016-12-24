package com.bcinfo.tripaway.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
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
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.HanziToPinyin;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MyListView;
import com.bcinfo.tripaway.view.SideBar;
import com.bcinfo.tripaway.view.SideBar.OnTouchingLetterChangedListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.socialize.utils.Log;

/**
 * @author
 * @date
 */
public class PersonalCustomizationStepThree extends BaseActivity implements
		OnClickListener {

	private LinearLayout fromLinearLayout;
	private LinearLayout toLinearLayout;
	private EditText nameEditText;
	private EditText emailEditText;
	private EditText phoneEditText;
	private TextView toText;
	private RadioGroup tripTimeRadioGroup;
	private RadioGroup adultNumRadioGroup;
	private RadioGroup childrenNumRadioGroup;
	private Button nextStepButton;
	private String days;
	private String adultNum;
	private String childrenNum;
	private String from;
	private String to;
	private String startDate;
	private String realName;
	private String mobile;
	private String email;
	private String budget;
	private String arrange;
	private LinearLayout backBtn;

	private static final String TAG = "PersonalCustomizationStepThree";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.personal_customization_step_three);
		// initLocation();
		initView();
		backBtn = (LinearLayout) findViewById(R.id.layout_back_button);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				finish();
			}
		});
		days=getIntent().getStringExtra("days");
		adultNum=getIntent().getStringExtra("adultNum");
		childrenNum=getIntent().getStringExtra("childrenNum");
		from=getIntent().getStringExtra("from");
		to=getIntent().getStringExtra("to");
		startDate=getIntent().getStringExtra("startDate");
		budget=getIntent().getStringExtra("budget");
		arrange=getIntent().getStringExtra("arrange");
		nameEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				setButtonEnable();
			}
		});
		
		
	phoneEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				setButtonEnable();
			}
		});
	emailEditText.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			setButtonEnable();
		}
	});
	}

	protected void initView() {
//		setSecondTitle("我的定制需求");
		nameEditText = (EditText) findViewById(R.id.name);
		emailEditText = (EditText) findViewById(R.id.email);
		phoneEditText = (EditText) findViewById(R.id.phone);
		
		nextStepButton = (Button) findViewById(R.id.nextStep);
		nextStepButton.setOnClickListener(this);

	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.nextStep:
			realName=nameEditText.getText().toString();
			email=emailEditText.getText().toString();
			mobile=phoneEditText.getText().toString();

			if (TextUtils.isEmpty(realName) || TextUtils.isEmpty(mobile)
					|| TextUtils.isEmpty(email)
					) {
				
			}else {
				
				if (!StringUtils.verifyIsPhone(mobile)) {
					ToastUtil.showToast(this, "号码格式不对!");
					return;
				}
				if (!StringUtils.verifyIsEmail(email)) {
					ToastUtil.showToast(this, "邮箱格式不对!");
					return;
				}
				RegUrl(from,to,days,startDate,arrange,adultNum,childrenNum,budget,realName,mobile,email);
			}

		}
	}

	
    private void RegUrl( String from,
    		String to,
    		String days,
    		String startDate,
    		String arrange,
    		String adultNum,
    		String childrenNum,
    		String budget,
    		String realName,
    		String mobile,
    		String email
)
    {
        try
        {
            MCryptUtil mcryptUtil = new MCryptUtil();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to", to);
            jsonObject.put("from", from);
            jsonObject.put("days", days);
            jsonObject.put("startDate", startDate);
            jsonObject.put("arrange", arrange);
            jsonObject.put("adultNum", adultNum);
            jsonObject.put("childrenNum", childrenNum);
            jsonObject.put("budget", budget);
            jsonObject.put("realName", realName);
            jsonObject.put("mobile", mobile);
            jsonObject.put("email", email);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(),HTTP.UTF_8);
            HttpUtil.post(Urls.submit_customization, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    LogUtil.d("PersonalCustomizationStepThree", "testClientCustomizationUrl", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    String code = response.optString("code");
                    if (code.equals("00000"))
                    {
                    	Intent cityIntent = new Intent(PersonalCustomizationStepThree.this,
                    			PersonalCustomizationStepEnd.class);
            			activityAnimationOpen();
            			startActivity(cityIntent);
                        ToastUtil.showToast(PersonalCustomizationStepThree.this, "提交成功");
                        finish();
                    }
                    else
                    {
                        ToastUtil.showToast(PersonalCustomizationStepThree.this, "提交失败" );
                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastUtil.showErrorToast(PersonalCustomizationStepThree.this, "提交失败"
                   );
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // MCryptUtil mcryptUtil = new MCryptUtil();
        // HttpPost regPost = new HttpPost(Urls.registerUrl);
        // Map<String, String> mapReg = new HashMap<String, String>();
        // try
        // {
        // mapReg.put("account", registerAccount);
        // mapReg.put("password", mcryptUtil.Encrypt(registerPassword));
        // mapReg.put("verifyCode", "");// 即使是邮箱注册 也需要加验证码这个参数的
        // JSONObject regObj = new JSONObject(mapReg);
        // StringEntity strEntity = new StringEntity(regObj.toString(), "utf-8");
        // strEntity.setContentEncoding("UTF-8");
        // strEntity.setContentType("application/json");
        // regPost.setEntity(strEntity);
        // Map<String, String> headerMap = HttpUtil.getHeadersMap(Urls.registerUrl);
        // for (Map.Entry<String, String> entry : headerMap.entrySet())
        // {
        // regPost.addHeader(entry.getKey(), entry.getValue()); // 添加post请求头
        //
        // }
        // HttpClient client = new DefaultHttpClient();
        // HttpResponse response = client.execute(regPost);
        // if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        // {
        // String responseBody = EntityUtils.toString(response.getEntity());
        // System.out.println("注册结果=" + responseBody);
        //
        // if (StringUtils.isEmpty(responseBody))
        // {
        // JSONObject jsonObject = new JSONObject(responseBody);
        // String code = jsonObject.optString("code");
        // if (code.equals("00000"))
        // {
        // ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_success));
        // }
        // else
        // {
        // ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_fail) + ":" + code);
        // }
        // }
        // }
        // }
        // catch (Exception e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
    
    private void  setButtonEnable(){
    	if (nameEditText.getText().length()==0 || emailEditText.getText().length()==0
				|| phoneEditText.getText().length()==0
				) {
    		nextStepButton.setEnabled(false);
    		nextStepButton.setTextColor(Color.parseColor("#BEE5D0"));
//			nextStepButton.setBackgroundResource(R.color.gray_little);
			}else{
				nextStepButton.setEnabled(true);
				nextStepButton.setTextColor(Color.WHITE);
//				nextStepButton.setBackgroundColor(Color.parseColor("#6599ff"));
			}
	 }

}
