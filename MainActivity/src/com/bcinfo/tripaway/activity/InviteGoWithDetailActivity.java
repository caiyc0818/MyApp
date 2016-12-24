package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.view.image.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 邀请结伴详情
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年4月28日 下午6:46:56
 */
public class InviteGoWithDetailActivity extends BaseActivity implements OnClickListener
{
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.invite_go_with_detail_activity);
        setSecondTitle("来自mebol的邀请");
        LinearLayout button_agree = (LinearLayout) findViewById(R.id.button_agree);
        LinearLayout button_refuse = (LinearLayout) findViewById(R.id.button_refuse);
        button_agree.setOnClickListener(this);
        button_refuse.setOnClickListener(this);
        initGoWithInfo();
    }

    private void initGoWithInfo()
    {
        RoundImageView author_photo = (RoundImageView) findViewById(R.id.author_photo);
        TextView author_name = (TextView) findViewById(R.id.author_name);
        TextView issue_time = (TextView) findViewById(R.id.issue_time);
        TextView go_with_title = (TextView) findViewById(R.id.go_with_title);
        LinearLayout layout_lable = (LinearLayout) findViewById(R.id.layout_lable);
        TextView predict_time = (TextView) findViewById(R.id.predict_time);
        TextView start_address = (TextView) findViewById(R.id.start_address);
        TextView destination_address = (TextView) findViewById(R.id.destination_address);
        TextView go_with_description = (TextView) findViewById(R.id.go_with_description);
        TextView them_responce_number = (TextView) findViewById(R.id.them_responce_number);
        layout_lable.addView(getLable("多发点"));
        layout_lable.addView(getLable("旅游达人"));
        layout_lable.addView(getLable("吃货达人"));
        layout_lable.addView(getLable("购物达人"));
        String url = "http://img3.3lian.com/2013/c1/10/107.jpg";
        ImageLoader.getInstance().displayImage(url, author_photo);
    }

    private LinearLayout getLable(String lable)
    {
        LinearLayout lableLayout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.go_with_lable_layout, null);
        TextView lableText = (TextView) lableLayout.findViewById(R.id.go_with_lable_text);
        lableText.setText(lable);
        return lableLayout;
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.button_agree:
                break;
            case R.id.button_refuse:
                break;
            default:
                break;
        }
    }
}
