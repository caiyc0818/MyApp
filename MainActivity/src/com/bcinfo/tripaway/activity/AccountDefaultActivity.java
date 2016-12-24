package com.bcinfo.tripaway.activity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Account;
import com.bcinfo.tripaway.dialog.DialogReminderLayout;
import com.bcinfo.tripaway.dialog.DialogReminderLayout.AccountUnbindInterface;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.view.MBProgressHUD.rotateProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 设置 账户 默认的activity
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月23日 20:05:34
 */
public class AccountDefaultActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener,
        AccountUnbindInterface
{

    private CheckBox isDefaultCheckBox;
    private LinearLayout backLayout;// 后退LinearLayout
    private RelativeLayout unbindLayout;// 解绑layout
    private DialogReminderLayout myAccountUnbindDialog;
    private Account accountItem;
    private String isDefaultStr; // 是否为默认账户
    private rotateProgressHUD accountDefaultChangeRotate;

    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);
        setContentView(R.layout.activity_account_default);
        AppManager.getAppManager().addActivity(this);
        isDefaultCheckBox = (CheckBox) findViewById(R.id.my_account_is_default_cb);
        backLayout = (LinearLayout) findViewById(R.id.layout_back_button);
        unbindLayout = (RelativeLayout) findViewById(R.id.layout_account_unbind_container);
        accountDefaultChangeRotate = (rotateProgressHUD) findViewById(R.id.account_default_change_rotate);

        accountItem = getIntent().getParcelableExtra("accountArg");
        isDefaultStr = accountItem.getIsdefault(); // 获得账户的绑定属性
        if (accountItem.getType() != null && !accountItem.getType().trim().equals(""))
        {
            if (accountItem.getType().equals("支付宝"))
            {
            	setSecondTitle("支付宝");
            }
        }
        if (accountItem.getIsdefault().equals("1")) // 是默认账户
        {
            isDefaultCheckBox.setChecked(true);
            accountItem.setIsdefault("1");

        }
        else
        { // 不是默认账户

            isDefaultCheckBox.setChecked(false);
            accountItem.setIsdefault("0");

        }
        backLayout.setOnClickListener(this);
        unbindLayout.setOnClickListener(this);
        // 设置checbox的check监听事件
        isDefaultCheckBox.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (isChecked)
        {
            accountItem.setIsdefault("1"); // 设置为默认
            isDefaultStr = "1";
        }
        else
        {

            accountItem.setIsdefault("0"); // 不设置为默认
            isDefaultStr = "0";

        }
        isDefaultCheckBox.setEnabled(false);
        unbindLayout.setEnabled(false);
        accountDefaultChangeRotate.setVisibility(View.VISIBLE);
        accountDefaultChangeRotate.startAnimation();
        testPaymentAccountIsDefaultUrl(accountItem.getId(), isDefaultStr);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.layout_back_button:
            Intent resultIntent = new Intent(this, MyAccountActivity.class);
            resultIntent.putExtra("Default", isDefaultStr);
            setResult(200, resultIntent);
            finish();
            activityAnimationClose();
            break;
        case R.id.layout_account_unbind_container:
            myAccountUnbindDialog = new DialogReminderLayout(this, this); // 账户解绑对话框
            myAccountUnbindDialog.show();
            myAccountUnbindDialog.setDailogText(getResources().getString(R.string.my_account_unbind_dialog_content_str));
            myAccountUnbindDialog.showRotate(true);
            break;
        default:
            break;
        }

    }

    /**
     * test 设置默认账号
     * 
     */
    private void testPaymentAccountIsDefaultUrl(String accountId, String isDefault)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("accountId", accountId);
            jsonObject.put("isDefault", isDefault);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
            HttpUtil.post(Urls.payment_account_isDefault_url, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    super.onSuccess(statusCode, headers, response);
                    accountDefaultChangeRotate.setVisibility(View.GONE);
                    accountDefaultChangeRotate.endAnimation();
                    isDefaultCheckBox.setEnabled(true);
                    unbindLayout.setEnabled(true);
                    if ("00000".equals(response.optString("code")))
                    {
                        Toast.makeText(getBaseContext(), "修改默认账户成功", Toast.LENGTH_SHORT).show();
                        setdefault(true);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {

                    super.onFailure(statusCode, headers, responseString, throwable);
                    throwable.printStackTrace();
                    setdefault(false);
                    accountDefaultChangeRotate.setVisibility(View.GONE);
                    accountDefaultChangeRotate.endAnimation();
                    isDefaultCheckBox.setEnabled(true);
                    unbindLayout.setEnabled(true);
                    Toast.makeText(getBaseContext(), "修改默认账户出错!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

    }

    @Override
    public void unbindOperate(int operateCode)
    {
        switch (operateCode)
        {
        case 0:// 取消
            break;
        case 1:// 确认
            testAccountDeleteUrl(); // 调用删除账户的接口
            break;
        default:
            break;
        }

    }

    /*
     * test 删除账户(解绑)接口
     */
    private void testAccountDeleteUrl()
    {
        try
        {
            HttpUtil.delete(Urls.payment_account_delete_url + accountItem.getId(), new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    super.onSuccess(statusCode, headers, response);
                    if ("00000".equals(response.optString("code")))
                    {
                        deleteresponse(true);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {

                    super.onFailure(statusCode, headers, responseString, throwable);
                    throwable.printStackTrace();
                    deleteresponse(false);
                }
            });
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }

    private void setdefault(boolean isdefault)
    {
        if (isdefault)
        {
            ;
        }
        else
        {
            Toast.makeText(getBaseContext(), "设置默认账号失败!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteresponse(boolean isdefault)
    {
        if (myAccountUnbindDialog.isShowing())
        {
            myAccountUnbindDialog.dismiss();
        }
        if (isdefault)
        {
            Intent resultIntent = new Intent(AccountDefaultActivity.this, MyAccountActivity.class);
            setResult(300, resultIntent);
            finish();
            activityAnimationClose();
        }
        else
        {
            Toast.makeText(getBaseContext(), "删除账号失败!", Toast.LENGTH_SHORT).show();
        }
    }

}
