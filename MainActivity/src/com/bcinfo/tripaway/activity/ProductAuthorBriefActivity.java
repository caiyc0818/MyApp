package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.utils.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 行程单作者简介
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年4月30日 下午2:35:16
 */
public class ProductAuthorBriefActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "ProductAuthorBriefActivity";
    private TextView button_telphone;

    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.product_author_brief_activity);
        TextView button_operation = (TextView) findViewById(R.id.button_operation);
        LinearLayout layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
        button_operation.setOnClickListener(this);
        layout_back_button.setOnClickListener(this);
        init();
    }

    private void init()
    {
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        TextView author_name = (TextView) findViewById(R.id.author_name);
//                author_name.setTypeface(iconfont);
        button_telphone = (TextView) findViewById(R.id.button_telphone);
        button_telphone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.button_operation:
                break;
            case R.id.layout_back_button:
                finish();
                break;
            case R.id.button_telphone:
                String telNum = button_telphone.getText().toString();
                LogUtil.i(TAG, "button_telphone", "telNum="+ telNum.trim());
                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" +  telNum.trim()));
                // 启动 
                startActivity(phoneIntent);
                break;
            default:
                break;
        }
    }
}
