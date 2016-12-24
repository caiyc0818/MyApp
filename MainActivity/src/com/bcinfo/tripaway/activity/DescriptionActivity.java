package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;

import android.os.Bundle;
import android.widget.TextView;

/**
 * 纯文字描述界面
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月27日 下午4:45:53
 */
public class DescriptionActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.product_description_activity);
        setSecondTitle(getIntent().getStringExtra("title"));
        TextView descriptionText = (TextView) findViewById(R.id.product_description_text);
        descriptionText.setText(getIntent().getStringExtra("description"));
    }
}
