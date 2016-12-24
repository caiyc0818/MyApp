package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.AppManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 绑定支付宝成功
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月23日 16:48:22
 */
public class AlipayBindDoneActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout alipayBindDoneConfirmContainer;// 确定
    private LinearLayout alipayBindDoneCancelIv;

    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);
        setContentView(R.layout.activity_alipay_binded_done);
        AppManager.getAppManager().addActivity(this);
        alipayBindDoneConfirmContainer = (LinearLayout) findViewById(R.id.layout_bind_done_container);
        alipayBindDoneCancelIv = (LinearLayout) findViewById(R.id.alipay_bind_done_cancel_iv);
        alipayBindDoneConfirmContainer.setOnClickListener(this);
        alipayBindDoneCancelIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.alipay_bind_done_cancel_iv:
        case R.id.layout_bind_done_container:
            Intent resultIntent = new Intent(this, AliPayBindedActivity.class);
            setResult(300, resultIntent);
            finish();
            activityAnimationCloseUpDown();
            break;

        default:
            break;
        }

    }

}
