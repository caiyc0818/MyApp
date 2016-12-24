package com.bcinfo.tripaway.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.view.editText.DeletedEditText;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 找回密码-邮箱找回
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月17日 16:23:22
 */
public class FindPassWordByEmailActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout resetPassWordLayout;
    
    private DeletedEditText inputEmailRegText;// 输入注册时的邮箱
    
    private RelativeLayout the_first_title;
    
    boolean isPhone = isMobileNO("1684565463453");

    boolean isEmail = isEmail("dthtterge@trytryvv.com");

    boolean isnumber = isNumeric("564465");
    
    @Override
    protected void onCreate(Bundle bundle)
    {
        
        // TODO 自动生成的方法存根
        super.onCreate(bundle);
        
        this.setContentView(R.layout.findpassword_by_email_activity);
        
        this.initFirstTitle(false, true, false);
        regTextView.setVisibility(View.INVISIBLE);// 设置 邮箱找回 界面标题栏右边的控件不可见
        findOrResetLabel.setText(R.string.findPasswordByEmail);
        resetPassWordLayout = (LinearLayout)findViewById(R.id.resetPassWordLayout);
        inputEmailRegText = (DeletedEditText)findViewById(R.id.findpwdbyemail);
        the_first_title = (RelativeLayout)findViewById(R.id.the_first_title);
        the_first_title.getBackground().setAlpha(255);
        resetPassWordLayout.setOnClickListener(this);
    }
    public boolean isMobileNO(String mobiles) {
    	Pattern p = Pattern
    	.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    	Matcher m = p.matcher(mobiles);

    	return m.matches();
    	}
  //判断是否全是数字
    public boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
    return false;
    }
    return true;
    }
    	        //判断email格式是否正确
    	public boolean isEmail(String email) {
    	String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    	Pattern p = Pattern.compile(str);
    	Matcher m = p.matcher(email);

    	return m.matches();
    	}
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.resetPassWordLayout:// 发送重置密码邮件
                if (inputEmailRegText.getText().toString().trim() == null
                    || "".equals(inputEmailRegText.getText().toString().trim()))
                {
                    Toast.makeText(this, "邮箱不能为空!", 1000).show();
                    return;
                }else/* if(isNumeric(inputEmailRegText.getText().toString())){
                	Toast.makeText(this, "全是数字!", 1000).show();
                	 return;
                }*/
                	
                	if(isNumeric(inputEmailRegText.getText().toString())&&!isMobileNO(inputEmailRegText.getText().toString())){
                	Toast.makeText(this, "手机号码不合法!", 1000).show();
                	return;
                } 
                if(!isNumeric(inputEmailRegText.getText().toString())&&!isEmail(inputEmailRegText.getText().toString())){
                	Toast.makeText(this, "邮箱格式不合法!", 1000).show();
                	return;
                }
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("account", inputEmailRegText.getText().toString());
                    jsonObject.put("type", "reset");
                    StringEntity stringEntity = new StringEntity(jsonObject.toString());
                    HttpUtil.post(Urls.smsValidateUrl, stringEntity, new JsonHttpResponseHandler()
                    {
                        
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse)
                        {
                            // TODO Auto-generated method stub
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            
                            Toast.makeText(FindPassWordByEmailActivity.this, "邮箱格式不正确!", 1000).show();
                        }
                        
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                        {
                            // TODO Auto-generated method stub
                            super.onSuccess(statusCode, headers, response);
                            String code = response.optString("code");
                            if(code.equals("00012")){
                            	Toast.makeText(FindPassWordByEmailActivity.this, "邮箱不存在!", 1000).show();
                            	return;
                            }else{
                            LogUtil.i("FindPassWordByEmailActivity", "发送验证码接口=", response.toString());
                            Intent resetPwdIntent =
                                new Intent(FindPassWordByEmailActivity.this, ResetPassWordActivity.class);
                            resetPwdIntent.putExtra("account", inputEmailRegText.getText().toString());
                            startActivity(resetPwdIntent);
                            activityAnimationOpen();
                            finish();
                        }
                        }
                        
                    });
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                break;
            
            default:
                break;
        }
        
    }
    
}
