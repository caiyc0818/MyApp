package com.bcinfo.tripaway.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.ResetPassWordActivity;
import com.bcinfo.tripaway.dialog.FindPassWordResultDialog;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 找回密码-手机找回(默认)
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月22日 16：26：22
 */
public class FindPassWordActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout findPwdLayout;// 找回密码
    private FindPassWordResultDialog findPwdByPhoneDialog;
    private DeletedEditText registerName;
	private TextView secondTitleBarRightTv;

    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);
        setContentView(R.layout.activity_findpassword_by_phone);
        setSecondTitle("手机找回");
        registerName = (DeletedEditText) findViewById(R.id.registerName);
       secondTitleBarRightTv = (TextView) findViewById(R.id.event_commit_button);
        secondTitleBarRightTv.setVisibility(View.VISIBLE);
        secondTitleBarRightTv.setText("邮箱找回");
        secondTitleBarRightTv.setOnClickListener(this);
        findPwdLayout = (LinearLayout) findViewById(R.id.findPassWordLayout);
        findPwdLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
        case R.id.findPassWordLayout:
            
            if (TextUtils.isEmpty(registerName.getText().toString()))
            {
                Toast.makeText(this, "账号不能为空!", 0).show();
                break;
            }
            if (verifyIsPhone(registerName.getText().toString())) // 判断登录账号是否是手机号
            {
                checkAccount(registerName.getText().toString()); 
                
            }
            else
            {
                ToastUtil.showToast(this, "登录账号不是手机号!");
                break;
            }
            break;
        case R.id.dialog_reminder_single_confirm:
            if(findPwdByPhoneDialog!=null)
                findPwdByPhoneDialog.dismiss();
            Intent intent=new Intent(getBaseContext(), ResetPassWordActivity.class);
            intent.putExtra("account", registerName.getText().toString());
          startActivity(intent);
          activityAnimationOpen();
            break;
        case R.id.event_commit_button:
        	   intent=new Intent(getBaseContext(), FindPassWordByEmailActivity.class);
            startActivity(intent);
            activityAnimationOpen();
            break;
        default:
            break;
        }

    }
    /**
     * 用户输入是否为手机号码的方法
     */
    private boolean verifyIsPhone(String accountStr)
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
            jsonObject.put("type", "reset");
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
                        findPwdByPhoneDialog = FindPassWordResultDialog.createDialog(FindPassWordActivity.this,
                                R.layout.dialog_reminder_single_layout);
                         
                        findPwdByPhoneDialog.show();
                        LinearLayout validateConfirmLayout = (LinearLayout) findPwdByPhoneDialog
                                .findViewById(R.id.dialog_reminder_single_confirm);
                        validateConfirmLayout.setOnClickListener(FindPassWordActivity.this);
                        TextView dialogReminderSingleText = (TextView) findPwdByPhoneDialog
                                .findViewById(R.id.dialog_reminder_single_text);
                        dialogReminderSingleText.setText(R.string.dialog_findPwdByPhone_text);
                    }
                    else
                    {
                        ToastUtil.showToast(FindPassWordActivity.this, response.optString("msg"));
                    }
                    
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }
    
    
    private void checkAccount(final String registerAccount)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", registerAccount);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.checkAccount, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    LogUtil.d("RegisterActivity", "testClientLoginUrl", response.toString());
                    super.onSuccess(statusCode, headers, response);
                    String code = response.optString("code");
                    if (code.equals("00008"))
                    {
                        sendVerifyCode(registerAccount);
                    }
                    else
                    {
                    	if(code.equals("00000"))
                    		ToastUtil.showToast(FindPassWordActivity.this, "此账号未注册！");
                    	else 
                        ToastUtil.showToast(FindPassWordActivity.this, response.optString("msg"));
                    }
                    
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }
}
