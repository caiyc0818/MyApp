package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.view.dialog.GrouponApplaySendDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 团购产品申请
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月8日 下午6:27:05
 */
public class GrouponProductApplyActivity extends BaseActivity implements OnClickListener
{
    private ProductInfo mProductInfo;

    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.groupon_product_apply_activity);
        setSecondTitle("报名确认");
        initProductInfo();
        TextView commitButton = (TextView) findViewById(R.id.commit_msg_button);
        commitButton.setOnClickListener(this);
    }

    private void initProductInfo()
    {
        mProductInfo = (ProductInfo) getIntent().getSerializableExtra("product");
        TextView productName = (TextView) findViewById(R.id.product_name);
        if (mProductInfo != null)
        {
            productName.setText(mProductInfo.getName());
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.layout_back_button:
                finish();
                break;
            case R.id.commit_msg_button:
                Intent intent = new Intent(this, GrouponApplySuccessActivity.class);
                startActivity(intent);
                finish();
                activityAnimationOpen();
                break;
            default:
                break;
        }
    }
}
