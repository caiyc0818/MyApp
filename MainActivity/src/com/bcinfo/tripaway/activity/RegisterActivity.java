package com.bcinfo.tripaway.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.bcinfo.tripaway.view.text.NoLineClickSpan;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 注册页面
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月11日 14:59:33
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener
{
    private int verifyFlag; // 验证标识
    
    /**
     * 注册协议说明文本框
     */
    private TextView applyRegisterTv;
    
    /**
     * 注册 手机或者邮箱账号
     */
    private DeletedEditText registerNameTv;
    
    /**
     * 注册 账号密码
     */
    private DeletedEditText registerPwdTv;
    
    /**
     * 注册 按钮
     * 
     */
    private LinearLayout regBtnLayout;
    private RelativeLayout the_first_title;
    
    private static final String FINISH="com.bcinfo.finish";
    public TextView getApplyRegisterTv()
    {
        return applyRegisterTv;
    }
    
    public void setApplyRegisterTv(TextView applyRegisterTv)
    {
        this.applyRegisterTv = applyRegisterTv;
    }
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        super.onCreate(bundle);
        statisticsTitle="注册";
        setContentView(R.layout.register_main);
        AppManager.getAppManager().addActivity(this);
        initFirstTitle(false, false, true);
        the_first_title = (RelativeLayout)findViewById(R.id.the_first_title);
        the_first_title.getBackground().setAlpha(255);
        applyRegisterTv = (TextView)findViewById(R.id.applyConversionTv);
        regBtnLayout = (LinearLayout)findViewById(R.id.registerBtn);
        registerNameTv = (DeletedEditText)findViewById(R.id.registerName);
        registerPwdTv = (DeletedEditText)findViewById(R.id.registerPassWord);
        regBtnLayout.setOnClickListener(this);
        initRegisterApplicate();
        IntentFilter filter = new IntentFilter();  
        filter.addAction(FINISH);  
        registerReceiver(mReceiver, filter);
        
    }
    
    /**
     * test 邮箱注册接口 (手机号注册 短信验证服务端暂时没有开通)
     * 
     * @param verifyFlag 标识 判断注册的账号是手机号 还是邮箱; 0 表示手机号;1 表示邮箱
     */
    private void testRegUrl(String registerAccount, String registerPassword, int verifyFlag)
    {
        try
        {
            MCryptUtil mcryptUtil = new MCryptUtil();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", registerAccount);
            jsonObject.put("password", mcryptUtil.Encrypt(registerPassword));
            jsonObject.put("type", "Client");
            jsonObject.put("verifyCode", "");
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
                    	testClientUserLoginUrl();
                    }
                    else
                    {
                        ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_fail) + ":" + response.optString("msg"));
                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastUtil.showErrorToast(RegisterActivity.this, getString(R.string.register_fail) + ":"
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
    
    /*
     * test 第三方注册
     */
    
    /**
     * 初始化注册协议说明
     */
    private void initRegisterApplicate()
    {
        /** 服务条款 */
        String contentStr1 = this.getResources().getString(R.string.contentStr1);
        SpannableString spanStr1 = new SpannableString(contentStr1);
        /**
         * 抽象类 点击超链接
         */
        NoLineClickSpan clickSpan1 = new NoLineClickSpan(getResources().getColor(R.color.pink))
        {
            @Override
            public void onClick(View widget)
            {

				Intent it = new Intent(RegisterActivity.this,
						ContentActivity.class);
				it.putExtra("title", "远行平台服务条款");
				it.putExtra("path", Urls.policy);
				activityAnimationOpen();
				startActivity(it);
                
            }
            
        };
        spanStr1.setSpan(clickSpan1, 4, 12, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        // 将spanStr1追加到文本框中
        applyRegisterTv.append(spanStr1);
        
        
        applyRegisterTv.setMovementMethod(LinkMovementMethod.getInstance());
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.registerBtn: // 注册按钮
                if (TextUtils.isEmpty(registerNameTv.getText().toString().trim()))
                {
                    Toast.makeText(this, "注册账号不能为空!", 0).show();
                    return;
                }
                if (TextUtils.isEmpty(registerPwdTv.getText().toString().trim()))
                {
                    Toast.makeText(this, "注册密码不能为空!", 0).show();
                    return;
                }
                String regNameStr = registerNameTv.getText().toString().trim();
                String regPwdStr = registerPwdTv.getText().toString().trim();
			if (!(Pattern.compile("^[a-zA-Z\\d]{6,16}$").matcher(regPwdStr)
					.matches())) {
				Toast.makeText(this, "注册密码为6-16位数字或字母!", 0).show();
				return;
			}
                if (isMobileNo(regNameStr) || isEmailAddressFormat(regNameStr))
                {
                    if (isMobileNo(regNameStr))
                    {
                        verifyFlag = 0;
                    }
                    else
                    {
                        verifyFlag = 1;
                    }
                    if(verifyFlag==0){
                    	Intent mIntent=new Intent(RegisterActivity.this,VerifyCodeActivity.class);
                    	mIntent.putExtra("account", regNameStr);
                    	mIntent.putExtra("password", regPwdStr);
                    	startActivity(mIntent);
                    	finish();
                    }
                    // new Thread(new Runnable()
                    // {
                    //
                    // @Override
                    // public void run()
                    // {
                    if(verifyFlag == 1)
                    testRegUrl(registerNameTv.getText().toString().trim(),
                        registerPwdTv.getText().toString().trim(),
                        verifyFlag);
                    // }
                    //
                    // }).start();
                }
                else
                {
                    
                    Toast.makeText(this, "请输入正确的账号!", 0).show();
                    return;
                    
                }
                
                break;
            
            default:
                break;
        }
        
    }
    
    /**
     * 判断用户输入的注册账号是否是手机号码
     */
    private boolean isMobileNo(String regNameStr)
    {
    	Pattern p = Pattern.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13\\d{9}|14[57]\\d{8}|15\\d{9}|18\\d{9}$");
        Matcher matcher = p.matcher(regNameStr);
        return matcher.matches();
    }
    
    /**
     * 判断用户输入的注册账号是否是邮箱
     */
    private boolean isEmailAddressFormat(String regNameStr)
    {
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher matcher = p.matcher(regNameStr);
        return matcher.matches();
    }
    
    /**
     * 获得验证码
     * 
     * 
     */
    private void sendVerifyCode(String registerAccount)
    {
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
//                    if (code.equals("00000"))
//                    {
//                        ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_success));
//                        finish();
//                    }
//                    else
//                    {
//                        ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_fail) + ":" + code);
//                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastUtil.showErrorToast(RegisterActivity.this, getString(R.string.register_fail) + ":"
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
    
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {  
        
        @Override  
        public void onReceive(Context context, Intent intent) {  
            if(intent.getAction().equals(FINISH)){  
                finish();  
            }  
        }  
    }; 
    
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
    };
    /*
     * 服务端 用户登录的url
     */
    private void testClientUserLoginUrl()
    {
        try
        {
            MCryptUtil mcryptUtil = new MCryptUtil();
            String account=registerNameTv.getText().toString();
            String password=registerPwdTv.getText().toString();
          String   loginPwdString = mcryptUtil.Encrypt(password);
            String clientId = PreferenceUtil.getClientId();
            System.out.println("这个id=" + clientId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientid", clientId);
            jsonObject.put("account", account);
            jsonObject.put("password", loginPwdString);
            jsonObject.put("type", "Client");
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.loginUrl, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
//                    LogUtil.d(TAG, "testClientLoginUrl", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    if ("00000".equals(response.optString("code")))
                    {

                        UserInfo myInfo = new UserInfo();
                        String account=registerNameTv.getText().toString();
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
                        Intent intentForMain = new Intent(RegisterActivity.this, MainActivity.class);
                        intentForMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentForMain.putExtra("userInfoValue", myInfo);
                        ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_success));
                        // intentForMain.putExtra("unreadMsgCount",
                        // String.valueOf(response.optJSONObject("data").opt("msgCount")));
                        AppManager.getAppManager().finishActivity(RegisterActivity.class);
                        Intent intent = new Intent("com.bcinfo.haveLogin");
                        sendBroadcast(intent);
                        startActivity(intentForMain);
                        finish();
                        activityAnimationClose();
                        // testUnReadMsgUrl(myInfo);

                    }
//                    else if ("00009".equals(response.optString("code")))
//                    {
//                        ToastUtil.showToast(RegisterActivity.this, getString(R.string.errorInfo));
//                    }
//                    else
//                    {
//                        ToastUtil.showToast(RegisterActivity.this, ":" + response.optString("code"));
//                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {

                    super.onFailure(statusCode, headers, responseString, throwable);
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
