package com.bcinfo.tripaway.activity;

import com.bcinfo.tripaway.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 关于我们
 * @function
 * @author caihelin
 * @version 1.0 2015年3月10日  11:09:22
 */
public class SettingAboutUsActivity extends BaseActivity implements OnClickListener
{
    /**
     * 标题栏文本框
     */
    private TextView titleTv;
    
    /**
     * 返回按钮
     */
    private LinearLayout iv;
    public TextView getTitleTv()
    {
        return titleTv;
    }
    public void setTitleTv(TextView titleTv)
    {
        this.titleTv = titleTv;
    }
    public LinearLayout getIv()
    {
        return iv;
    }
    public void setIv(LinearLayout iv)
    {
        this.iv = iv;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        statisticsTitle="关于我们";
        setContentView(R.layout.setting_about_us_layout);
        titleTv=(TextView)findViewById(R.id.second_title_text);
        iv=(LinearLayout)findViewById(R.id.layout_back_button);
        titleTv.setText(R.string.setting_aboutUs_title);
        ((View)titleTv.getParent()).setBackgroundColor(getResources().getColor(R.color.title_bg));
        ((View)titleTv.getParent()).getBackground().setAlpha(255);
        iv.setOnClickListener(mOnClickListener);
    }
    @Override
    public void onClick(View v)
    {
       switch (v.getId())
    {
    case R.id.back_button:
        finish();
        this.overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
        break;

    default:
        break;
    }
        
    }

}
