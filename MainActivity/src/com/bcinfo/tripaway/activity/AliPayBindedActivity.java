package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.AccountTypeInfo;
import com.bcinfo.tripaway.editText.DeletedEditText;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
import com.bcinfo.tripaway.utils.MCryptUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.MBProgressHUD.rotateProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 绑定第三方支付 (当前仅限于支付宝)
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月23日 15:33:11
 */
public class AliPayBindedActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout alipayBindCommitContainer;
    private ArrayList<AccountTypeInfo> myaccount;
    // 账号
    private DeletedEditText alipayBindedAccount;
    private rotateProgressHUD bindAlipayRotate;

    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);
        setContentView(R.layout.activity_bind_alipay);
        setSecondTitle("绑定第三方支付");
        AppManager.getAppManager().addActivity(this);
        bindAlipayRotate = (rotateProgressHUD) findViewById(R.id.bind_alipay_rotate);
        alipayBindCommitContainer = (LinearLayout) findViewById(R.id.layout_alipay_binded_commit_container);
        alipayBindedAccount = (DeletedEditText) findViewById(R.id.alipay_binded_account_tv);
        alipayBindCommitContainer.setOnClickListener(this);
        // testAccountCategoryUrl();
    }

    /*
     * test 账户类型 接口
     */
    private void testAccountCategoryUrl()
    {
        HttpUtil.get(Urls.payment_account_category_url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {

                super.onSuccess(statusCode, headers, response);
                System.out.println(response);
                if ("00000".equals(response.optString("code")))
                {
                    getAllAccount(response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {

                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(responseString);
            }
        });
    }

    private void getAllAccount(JSONObject response)
    {
        JSONArray accout = response.optJSONArray("data");
        if (accout == null)
        {
            return;
        }
        myaccount = new ArrayList<AccountTypeInfo>();
        for (int i = 0; i < accout.length(); i++)
        {
            AccountTypeInfo accountItem = new AccountTypeInfo();
            accountItem.setId(accout.optJSONObject(i).optString("id")); // id
            accountItem.setName(accout.optJSONObject(i).optString("name")); // 类型名
            accountItem.setLogo(accout.optJSONObject(i).optString("logo")); // 缩略图
            myaccount.add(accountItem);
        }
    }

    @Override
    public void onClick(View v)
    {
        String BindedAccount;
        switch (v.getId())
        {
        case R.id.layout_alipay_binded_commit_container:
            BindedAccount = alipayBindedAccount.getText().toString().trim();
            if (TextUtils.isEmpty(BindedAccount))
            {
                Toast.makeText(this, "账号不能为空!", 0).show();
                return;
            }
            if (StringUtils.isEmail(BindedAccount)||StringUtils.isPhone(BindedAccount)) // 判断登录账号是否是邮箱,手机号登陆1.0版本没有，函数在下面，之后可加
            {
                alipayBindCommitContainer.setEnabled(false);
                bindAlipayRotate.setVisibility(View.VISIBLE);
                bindAlipayRotate.startAnimation();
                testPaymentAccountAddUrl(BindedAccount);
                hideInputManager1(alipayBindedAccount);

            }
            else
            {
                ToastUtil.showToast(this, "账号不合法!");
                return;
            }
            break;

        default:
            break;
        }

    }

    /*
     * test 添加账户 接口
     */
    private void testPaymentAccountAddUrl(String accountNo)
    {
        try
        {
            MCryptUtil mcryptUtil = new MCryptUtil();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "alipay");
            jsonObject.put("accountNo", accountNo);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            
            HttpUtil.post(Urls.payment_account_add_url, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    super.onSuccess(statusCode, headers, response);
                    alipayBindCommitContainer.setEnabled(true);
                    bindAlipayRotate.setVisibility(View.GONE);
                    bindAlipayRotate.endAnimation();
                    if ("00000".equals(response.optString("code")))
                    {
                        activityjump();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {

                    super.onFailure(statusCode, headers, responseString, throwable);
                    alipayBindCommitContainer.setEnabled(true);
                    bindAlipayRotate.setVisibility(View.GONE);
                    bindAlipayRotate.endAnimation();
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

    }

    // 跳转到绑定支付宝成功界面
    private void activityjump()
    {
        Intent Alipaybinded = new Intent(this, AlipayBindDoneActivity.class);
        startActivityForResult(Alipaybinded, 120);
        activityAnimationOpenUpDown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 120 && resultCode == 300)
        {
            Intent resultIntent = new Intent(this, MyAccountActivity.class);
            setResult(500, resultIntent);
            finish();
        }
    }

}
