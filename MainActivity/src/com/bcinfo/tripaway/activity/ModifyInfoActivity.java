package com.bcinfo.tripaway.activity;

import im.yixin.sdk.util.StringUtil;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.UserInfoDB;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @author hanweipeng
 * @date 2015-7-13 下午4:05:34
 */
public class ModifyInfoActivity extends BaseActivity implements OnClickListener
{
    private EditText infoEdt;
    
    private TextView limitTxt;
    
    private TextView saveBtn;
    
    private int type = -1;
    
    private UserInfo userInfo=null;
    
    private RelativeLayout secondTitle;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.modify_info_activity);
        secondTitle = (RelativeLayout) findViewById(R.id.second_title);
        secondTitle.getBackground().setAlpha(255);
        initView();
    }
   
    protected void initView()
    {
        userInfo = getIntent().getParcelableExtra("userInfo");
        infoEdt = (EditText)findViewById(R.id.info_edt);
        
        limitTxt = (TextView)findViewById(R.id.limit_tip);
        limitTxt.setVisibility(View.GONE);
        type = getIntent().getIntExtra("type", -1);
        switch (type)
        {
            case 0:
            	if(userInfo!=null)
            	{
                	
            		infoEdt.setText(StringUtils.isEmptyReturn(userInfo.getNickname()));
            	}
            	
                initTitleLayout("昵称", "保存");
                break;
            case 1:
            	if(userInfo!=null)
            	{
                	
            		infoEdt.setText(StringUtils.isEmptyReturn(userInfo.getRealName()));
            	}
                initTitleLayout("真实姓名", "保存");
                break;
            case 2:
            	if(userInfo!=null)
            	{
                	
            		infoEdt.setText(StringUtils.isEmptyReturn(userInfo.getUsersIdentity()));
            	}
                initTitleLayout("身份证号", "保存");
                break;
            case 3:
            	if(userInfo!=null)
            	{
                	
            		infoEdt.setText(StringUtils.isEmptyReturn(userInfo.getIntroduction()));
            	}
                initTitleLayout("自我介绍", "保存");
                limitTxt.setVisibility(View.VISIBLE);
                infoEdt.addTextChangedListener(new TextWatcher()
                {
                    
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        // TODO Auto-generated method stub
                        int length = s.length();
                        limitTxt.setText((30 - length) + "字");
                    }
                });
                break;
            default:
                break;
        }
    }
    
    private void initTitleLayout(String title, String rightText)
    {
        TextView titleTV = (TextView)findViewById(R.id.second_title_text);
        LinearLayout backButton = (LinearLayout)findViewById(R.id.layout_back_button);
        saveBtn = (TextView)findViewById(R.id.event_commit_button);
        saveBtn.setVisibility(View.VISIBLE);
        titleTV.setText(title);
        saveBtn.setText(rightText);
        saveBtn.setOnClickListener(this);
        backButton.setOnClickListener(mOnClickListener);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.event_commit_button:
                if (infoEdt.getText().toString().replaceAll(" ", "").equals(""))
                {
                    ToastUtil.showToast(ModifyInfoActivity.this, "输入的内容不能为空");
                    return;
                }
                if (type == 0)
                {
                    userInfo.setNickname(infoEdt.getText().toString());
                }
                else if (type == 1)
                {
                    userInfo.setRealName(infoEdt.getText().toString());
                }
                else if (type == 2)
                {
                    userInfo.setUsersIdentity(infoEdt.getText().toString());
                }
                else if (type == 3)
                {
                    userInfo.setIntroduction(infoEdt.getText().toString());
                }
                userInfoEdit();
                break;
            default:
                break;
        }
    }
    
    /**
     * 修改个人信息接口
     */
    private void userInfoEdit()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("avatar", userInfo.getAvatar());
            jsonObject.put("nickname", userInfo.getNickname());
            jsonObject.put("realName", userInfo.getRealName());
            jsonObject.put("usersIdentity", userInfo.getUsersIdentity());
            jsonObject.put("sex", userInfo.getGender());
            jsonObject.put("address", userInfo.getAddress());
            jsonObject.put("introduction", userInfo.getIntroduction());
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            HttpUtil.post(Urls.userinfo_edit_url, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
//                    ToastUtil.showToast(ModifyInfoActivity.this, "修改失败  errorMessage=" + throwable.getMessage());
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.e("ModifyInfoActivity", "设置个人信息接口", response.toString());
                    if (response.optString("code").equals("00000"))
                    {
                        ToastUtil.showToast(ModifyInfoActivity.this, "修改成功");
                        UserInfoDB.getInstance().insertData(userInfo);
                        if (type == 0)
                        {
                            Intent intent = new Intent();
                            intent.putExtra("nickName", infoEdt.getText().toString());
                            setResult(1001, intent);
                            finish();
                            activityAnimationClose();
                        }
                        else if (type == 1)
                        {
                            Intent intent = new Intent();
                            intent.putExtra("realName", infoEdt.getText().toString());
                            setResult(1002, intent);
                            finish();
                            activityAnimationClose();
                        }
                        else if (type == 2)
                        {
                            Intent intent = new Intent();
                            intent.putExtra("usersIdentity", infoEdt.getText().toString());
                            setResult(1003, intent);
                            finish();
                            activityAnimationClose();
                        }
                        else if (type == 3)
                        {
                            Intent intent = new Intent();
                            intent.putExtra("introduction", infoEdt.getText().toString());
                            setResult(1004, intent);
                            finish();
                            activityAnimationClose();
                        }
                        Intent intent = new Intent("com.bcinfo.modifyUserInfo");
                        sendBroadcast(intent);
                    }
                    else
                    {
//                        ToastUtil.showToast(ModifyInfoActivity.this, "修改失败  errorCode=" + response.optString("code"));
                    }
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
