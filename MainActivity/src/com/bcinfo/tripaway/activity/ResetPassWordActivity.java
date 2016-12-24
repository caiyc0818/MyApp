package com.bcinfo.tripaway.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.CustomProgressDialog;
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 定义一个"重置密码"的Activity
 * 
 * @function
 * 
 * @author caihelin
 * 
 * @version 1.0 2015年4月22日 17:39:22
 * 
 */
public class ResetPassWordActivity extends BaseActivity implements OnClickListener
{
    /**
     * 自定义Dialog的子类
     * 
     */
    private CustomProgressDialog resetDialog;
    
    private String account;
    
    /**
     * 重置密码
     * 
     */
    private LinearLayout resetPwdBtn;
    
    /**
     * 手机验证码
     */
    private DeletedEditText phoneAuthCodeTv;
    
    /**
     * 新密码
     * 
     */
    private DeletedEditText passwordTv;
    
    /**
     * 重复新密码
     * 
     */
    private DeletedEditText newPasswordTv;
    
    public CustomProgressDialog getResetDialog()
    {
        return resetDialog;
    }
    
    public void setResetDialog(CustomProgressDialog resetDialog)
    {
        this.resetDialog = resetDialog;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.resetpwd);
        account = getIntent().getStringExtra("account").trim();// 欲重置的邮箱
        this.initFirstTitle(false, true, false);
        regTextView.setVisibility(View.INVISIBLE);
        findOrResetLabel.setText(R.string.resetPwd);
        phoneAuthCodeTv = (DeletedEditText)findViewById(R.id.inputPhoneAuthCode);
        passwordTv = (DeletedEditText)findViewById(R.id.inputNewPwd);
        newPasswordTv = (DeletedEditText)findViewById(R.id.inputNewPwdAgain);
        resetPwdBtn = (LinearLayout)this.findViewById(R.id.resetBtn);
        
        resetPwdBtn.setOnClickListener(this);
        
    }
    
    /*
     * 测试 重置密码接口 返回值 0:表示重置密码成功 ; 1:表示重置密码失败
     */
    private void testPwdResetUrl()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            MCryptUtil mCryptUtil = new MCryptUtil();
            jsonObject.put("account", account);
            jsonObject.put("newPassword", mCryptUtil.Encrypt(newPasswordTv.getText().toString()));
            jsonObject.put("verifyCode", phoneAuthCodeTv.getText().toString());
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.patch(Urls.resetPwdUrl, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(ResetPassWordActivity.this, "密码重置失败，验证码错误！", 1000).show();
                    LogUtil.i("FindPassWordByEmailActivity", "重置密码失败接口=", errorResponse.toString());
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.i("FindPassWordByEmailActivity", "重置密码接口=", response.toString());
                    String code = response.optString("code");
                    if (code.equals("00000")){
                    Toast.makeText(ResetPassWordActivity.this, "密码重置成功", 1000).show();
                    Intent intent = new Intent(ResetPassWordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    activityAnimationOpen();
                    finish();
                    activityAnimationClose();}
                    else{
                    	ToastUtil.showToast(ResetPassWordActivity.this, response.optString("msg"));
                    }
                }
                
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        /*
         * RequestParams params=new RequestParams(); params.put("account", emailResetedValue); params.put("newPassword",
         * newPasswordTv.getText().toString().trim()); params.put("verifyCode", ""); HttpUtil.get(Urls.resetPwdUrl,
         * params, new JsonHttpResponseHandler(){
         * 
         * @Override public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
         * 
         * super.onSuccess(statusCode, headers, response); System.out.println(statusCode);
         * System.out.println(response.toString()); }
         * 
         * @Override public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
         * {
         * 
         * super.onFailure(statusCode, headers, responseString, throwable); System.out.println(statusCode);
         * System.out.println(responseString.toString()); } });
         */
        // MCryptUtil mCryptUtil = new MCryptUtil();
        // HttpPatch httpPatch = new HttpPatch(Urls.resetPwdUrl);
        //
        // Map<String, String> mapParams;
        // try
        // {
        //
        // Map<String, String> mapParam = new HashMap<String, String>();
        // mapParam.put("account", emailResetedValue);
        // mapParam.put("newPassword", mCryptUtil.Encrypt(newPasswordTv.getText().toString().trim()));
        // mapParam.put("verifyCode", "");
        // JSONObject resultObj = new JSONObject(mapParam);
        // StringEntity strEntity = new StringEntity(resultObj.toString(), "utf-8");
        // strEntity.setContentEncoding("UTF-8");
        // strEntity.setContentType("application/json");
        // httpPatch.setEntity(strEntity);
        // mapParams = HttpUtil.getHeadersMap(Urls.resetPwdUrl);
        // for (Map.Entry<String, String> entry : mapParams.entrySet())
        // {
        // // 添加请求头信息
        //
        // httpPatch.addHeader(entry.getKey(), entry.getValue());
        //
        // }
        // HttpClient client = new DefaultHttpClient();
        // HttpResponse resp = client.execute(httpPatch);
        // if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        // {
        // String strResult = EntityUtils.toString(resp.getEntity());
        // System.out.println("statisCode 代码:" + resp.getStatusLine().getStatusCode());
        // System.out.println("result 结果:" + strResult);
        //
        // JSONObject respResultObj = new JSONObject(strResult);
        // if ("00000".equals(respResultObj.optString("code")))
        // {
        // return 0;
        // }
        // else
        // {
        //
        // return 1;
        // }
        //
        // }
        // else
        // {
        // return 1;
        // }
        // }
        // catch (Exception e)
        // {
        //
        // e.printStackTrace();
        // return 1;
        // }
        //
    }
    
    /**
     * 重写 activity的ondestroy()方法
     * 
     */
    @Override
    protected void onDestroy()
    {
        
        super.onDestroy();
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.resetBtn:
                if (TextUtils.isEmpty(passwordTv.getText().toString()))
                {
                    Toast.makeText(this, "新密码不能为空!", 0).show();
                    return;
                }
                if (TextUtils.isEmpty(newPasswordTv.getText().toString()))
                {
                    Toast.makeText(this, "新密码不能为空哦!", 0).show();
                    return;
                }
                if (!passwordTv.getText().toString().equals(newPasswordTv.getText().toString()))
                {
                    Toast.makeText(this, "新旧密码不一致!", 0).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneAuthCodeTv.getText().toString()))
                {
                    Toast.makeText(this, "验证码不能为空!", 0).show();
                    return;
                }
                testPwdResetUrl();
                break;
            
            default:
                break;
        }
        
    }
}
