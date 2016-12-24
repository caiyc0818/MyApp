package com.bcinfo.tripaway.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyCodeActivity extends BaseActivity implements OnClickListener {

	private String account;
	private String password;
	private String verifyCode;

	private LinearLayout sendVerifyCodeBtn;
	private TextView sendVerifyCodeTextView;
	private TextView verifyCodeTipTextView;
	private EditText verifyCodeEditView;
	private LinearLayout verifyCodeTip;
	private LinearLayout verifyCodeOkBtn;
	private LinearLayout layout_back_button;
	private int time = 61;
	private static final String FINISH="com.bcinfo.finish";
	private static final String TAG = "VerifyCodeActivity";
	

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.verify_code);
		Intent intent = getIntent();
		account = intent.getStringExtra("account");
		password = intent.getStringExtra("password");
		findView();

		sendVerifyCodeBtn.setOnClickListener(this);
		verifyCodeOkBtn.setOnClickListener(this);
		layout_back_button.setOnClickListener(this);
		sendVerifyCode(account);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendVerifyCodeBtn:
			sendVerifyCode(account);
			break;
		case R.id.verifyCodeOkBtn:
			verifyCode=verifyCodeEditView.getText().toString().trim();
			 if (TextUtils.isEmpty(verifyCode))
             {
                 Toast.makeText(this, "验证码不能为空!", 0).show();
                 return;
             }
			requestReg(account,password,verifyCode);
			break;	
		case R.id.layout_back_button:
			finish();
			activityAnimationClose();
			break;
		}

	}

	private void findView() {
		sendVerifyCodeBtn = (LinearLayout) findViewById(R.id.sendVerifyCodeBtn);
		sendVerifyCodeTextView = (TextView) sendVerifyCodeBtn.getChildAt(0);
		verifyCodeTip = (LinearLayout) findViewById(R.id.verifyCodeTip);
		verifyCodeTipTextView = (TextView) verifyCodeTip.getChildAt(1);
		verifyCodeOkBtn = (LinearLayout) findViewById(R.id.verifyCodeOkBtn);
		verifyCodeEditView=(EditText) findViewById(R.id.verifyCode);
		layout_back_button= (LinearLayout)findViewById(R.id.layout_back_button);
		
	}

	/**
	 * 获得验证码
	 * 
	 * 
	 */
	private void sendVerifyCode(String registerAccount)
    {
		verifyCodeTip.setVisibility(View.INVISIBLE);
		new Thread(new TimeThread()).start();
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", registerAccount);
            jsonObject.put("type", "register");
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.verify_code, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    LogUtil.d("RegisterActivity", "testClientLoginUrl", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    String code = response.optString("code");
                    if (code.equals("00000"))
                    {
                    	verifyCodeTip.setVisibility(View.VISIBLE);
        				verifyCodeTipTextView.setText(account);
//                        ToastUtil.showToast(VerifyCodeActivity.this, getString(R.string.register_success));
//                        goLoginActivity();
//                        finish();
                    }
                    else
                    {
//                        ToastUtil.showToast(VerifyCodeActivity.this, getString(R.string.register_fail) + ":" + code);
//                        goLoginActivity();
//                        finish();
                    }
                    
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastUtil.showErrorToast(VerifyCodeActivity.this, getString(R.string.register_fail) 
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
       
    }

    /**
     * test 手机号注册 短信验证服务端
     * 
     * @param verifyFlag 标识 判断注册的账号是手机号 还是邮箱; 0 表示手机号;1 表示邮箱
     */
    private void requestReg(String registerAccount, String registerPassword,String verifyCode)
    {
        try
        {
            MCryptUtil mcryptUtil = new MCryptUtil();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", registerAccount);
            jsonObject.put("password", mcryptUtil.Encrypt(registerPassword));
            jsonObject.put("type", "Client");
            jsonObject.put("verifyCode", verifyCode);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.registerUrl, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    LogUtil.d("RegisterActivity", "testClientLoginUrl", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    String code = response.optString("code");
                    if (code.equals("00000"))
                    {
                    	
                        // testUnReadMsgUrl(myInfo);
                    	//verifySucess(response);
                    	 testClientUserLoginUrl();
                        
                        
                    }
                    else
                    {
//                    	verifySucess(response);
                        ToastUtil.showToast(VerifyCodeActivity.this, getString(R.string.register_fail) + ":" + response.optString("msg"));
//                        Intent intent = new Intent();  
//                        intent.setAction(FINISH);  
//                        sendBroadcast(intent);
//                        goLoginActivity();
//                        finish();
                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastUtil.showErrorToast(VerifyCodeActivity.this, getString(R.string.register_fail) + ":"
                        + statusCode);
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	// handler类接收数据
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
//			if(sendVerifyCodeBtn.getVisibility()==View.GONE)
//			sendVerifyCodeBtn.setVisibility(View.VISIBLE);
			switch (msg.what) {
			
			case 1:
				sendVerifyCodeBtn
						.setBackgroundResource(R.drawable.shape_verify_dialog_disable);
				sendVerifyCodeTextView.setText(time + "秒后重新发送验证码");
				sendVerifyCodeBtn.setEnabled(false);
			
				sendVerifyCodeBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				sendVerifyCodeBtn
						.setBackgroundResource(R.drawable.shape_verify_dialog_normal);
				sendVerifyCodeTextView.setText("重新发送验证码");
				sendVerifyCodeBtn.setEnabled(true);
				verifyCodeTip.setVisibility(View.INVISIBLE);
				sendVerifyCodeBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				sendVerifyCodeTextView.setText(time + "秒后重新发送验证码");
				sendVerifyCodeBtn.setVisibility(View.VISIBLE);
				break;
			default:
			}
		};
	};

	// 线程类
	class TimeThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					--time;
					Message msg = new Message();
					if (time == 60) {
						msg.what = 1;
					} else if (time == 0) {
						msg.what = 2;
						time=61;
						handler.sendMessage(msg);
						break;
					} else if (time < 60){
						msg.what = 3;
					}
					handler.sendMessage(msg);
					Thread.sleep(1000);
				
					

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("thread error...");
				}
			}
		}
	}
	
	private void verifySucess(JSONObject response){
		UserInfo myInfo = new UserInfo();
        
        JSONObject valueObj = response.optJSONObject("data");
        PreferenceUtil.setAccount(account);
        PreferenceUtil.setUserId(valueObj.optJSONObject("userInfo").optString("userId")); // 向preferences.xml中写入userId
        PreferenceUtil.setToken(valueObj.optString("token"));// 向preferences.xml写入token
        PreferenceUtil.setSession(valueObj.optString("session"));// 向preferences.xml写入session
        PreferenceUtil.setString("avatar", valueObj.optJSONObject("userInfo").optString("avatar"));
        myInfo.setUserId(valueObj.optJSONObject("userInfo").optString("userId"));// 设置个人id
        myInfo.setAvatar(valueObj.optJSONObject("userInfo").optString("avatar"));// 用户头像
        myInfo.setNickname(valueObj.optJSONObject("userInfo").optString("nickName")); // 用户昵称
        myInfo.setMobile(valueObj.optJSONObject("userInfo").optString("mobile")); // 用户手机号
        myInfo.setEmail(valueObj.optJSONObject("userInfo").optString("email")); // 用户email
        myInfo.setStatus(valueObj.optJSONObject("userInfo").optString("status"));
        myInfo.setGender(valueObj.optJSONObject("userInfo").optString("sex"));// 用户性别
        myInfo.setIntroduction(valueObj.optJSONObject("userInfo").optString("introduction")); // 用户介绍
        myInfo.setAddress(valueObj.optJSONObject("userInfo").optString("address")); // 地址
        myInfo.setUsersIdentity(valueObj.optJSONObject("userInfo").optString("usersIdentity")); // 身份证
        UserInfoDB.getInstance().insertData(myInfo);
        Intent intentForMain = new Intent(VerifyCodeActivity.this, MainActivity.class);
        intentForMain.putExtra("userInfoValue", myInfo);
        // intentForMain.putExtra("unreadMsgCount",
        // String.valueOf(response.optJSONObject("data").opt("msgCount")));
        Intent intent = new Intent("com.bcinfo.haveLogin");
        sendBroadcast(intent);
        setResult(200, intentForMain);
        finish();
        activityAnimationClose();
	}
	
	/*
     * 服务端 用户登录的url
     */
    private void testClientUserLoginUrl()
    {
        try
        {
            MCryptUtil mcryptUtil = new MCryptUtil();
          String   loginPwdString = mcryptUtil.Encrypt(password);
            String clientId = PreferenceUtil.getClientId();
            System.out.println("这个id=" + clientId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientid", clientId);
            jsonObject.put("account", account);
            jsonObject.put("password", loginPwdString);
            jsonObject.put("type", "Client");
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            LogUtil.d(TAG, "testClientLoginUrl", stringEntity.toString());
            HttpUtil.post(Urls.loginUrl, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    LogUtil.d(TAG, "testClientLoginUrl", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    if ("00000".equals(response.optString("code")))
                    {

                        UserInfo myInfo = new UserInfo();

                        JSONObject valueObj = response.optJSONObject("data");
                        PreferenceUtil.setAccount(account);
                        PreferenceUtil.setUserId(valueObj.optJSONObject("userInfo").optString("userId")); // 向preferences.xml中写入userId
                        PreferenceUtil.setToken(valueObj.optString("token"));// 向preferences.xml写入token
                        PreferenceUtil.setSession(valueObj.optString("session"));// 向preferences.xml写入session
                        PreferenceUtil.setString("avatar", valueObj.optJSONObject("userInfo").optString("avatar"));
                        myInfo.setUserId(valueObj.optJSONObject("userInfo").optString("userId"));// 设置个人id
                        myInfo.setAvatar(valueObj.optJSONObject("userInfo").optString("avatar"));// 用户头像
                        myInfo.setNickname(valueObj.optJSONObject("userInfo").optString("nickName")); // 用户昵称
                        myInfo.setRealName(valueObj.optJSONObject("userInfo").optString("realName")); // 用户姓名
                        myInfo.setMobile(valueObj.optJSONObject("userInfo").optString("mobile")); // 用户手机号
                        myInfo.setEmail(valueObj.optJSONObject("userInfo").optString("email")); // 用户email
                        myInfo.setStatus(valueObj.optJSONObject("userInfo").optString("status"));
                        myInfo.setGender(valueObj.optJSONObject("userInfo").optString("sex"));// 用户性别
                        myInfo.setIntroduction(valueObj.optJSONObject("userInfo").optString("introduction")); // 用户介绍
                        myInfo.setAddress(valueObj.optJSONObject("userInfo").optString("address")); // 地址
                        UserInfoDB.getInstance().insertData(myInfo);
                        // Intent intentForMain = new Intent(LoginActivity.this,
                        // LoginActivity.class);
                        // intentForMain.putExtra("userInfoValue", myInfo);
                        // // intentForMain.putExtra("unreadMsgCount",
                        // //
                        // String.valueOf(response.optJSONObject("data").opt("msgCount")));
                        // setResult(200, intentForMain);
                        // finish();
                        Intent intentForMain = new Intent(VerifyCodeActivity.this, MainActivity.class);
                        intentForMain.putExtra("userInfoValue", myInfo);
                        intentForMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // intentForMain.putExtra("unreadMsgCount",
                        // String.valueOf(response.optJSONObject("data").opt("msgCount")));
                        AppManager.getAppManager().finishActivity(RegisterActivity.class);
//                        AppManager.getAppManager().finishActivity(LoginActivity.class);
                        Intent intent = new Intent("com.bcinfo.haveLogin");
                        sendBroadcast(intent);
                        setResult(200, intentForMain);
                        finish();
                        activityAnimationClose();
                        // testUnReadMsgUrl(myInfo);

                    }
                    else if ("00009".equals(response.optString("code")))
                    {
                        ToastUtil.showToast(VerifyCodeActivity.this, getString(R.string.errorInfo));
                    }
                    else
                    {
                        ToastUtil.showToast(VerifyCodeActivity.this,response.optString("msg"));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {

                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastUtil.showToast(VerifyCodeActivity.this, ":" + statusCode);
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

    }
	
}
