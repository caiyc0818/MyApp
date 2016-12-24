package com.bcinfo.tripaway.view.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的旅程付款对话框
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月12日 上午11:46:20
 */
public class MyJourneyPayDialog extends BaseActivity implements OnClickListener
{
    private EditText mPasswordET;
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.my_journey_pay_dialog);
        mPasswordET=(EditText)findViewById(R.id.user_account_password);
        initData();
    }
    
    private void initData()
    {
        ProductInfo info=(ProductInfo) getIntent().getSerializableExtra("product");
        RoundImageView authorPhoto=(RoundImageView)findViewById(R.id.product_author_photo);
        TextView authorName=(TextView)findViewById(R.id.product_author_name);
        TextView productName=(TextView)findViewById(R.id.product_name);
        TextView productPrice=(TextView)findViewById(R.id.product_price);
        Button commitButton=(Button)findViewById(R.id.pay_commit_button);
        Button cancelButton=(Button)findViewById(R.id.pay_cancel_button);
        commitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
//        ImageLoader.getInstance().displayImage(info.getUser().getAvatar(), authorPhoto);
//        authorName.setText(info.getUser().getNickname());
//        productName.setText(info.getTitle());
        productPrice.setText(info.getPrice());
        
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.pay_commit_button:
                mPasswordET.getText().toString();
                Intent intent =new Intent(this, MyJourneyPaySuccessDialog.class);
                startActivity(intent);
                finish();
                break;
            case R.id.pay_cancel_button:
                finish();
                break;
            default:
                break;
        }
    }
}
