package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bcinfo.tripaway.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设置-帮助
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月15日 14:49:22
 */
public class SettingHelpActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout iv;
    private TextView tv;

    private String[] questionArray1;

    private String[] questionArray2;
    private TextView[] guideTv; // 新手问题
    private TextView[] accountTv;// 账号问题

    public String[] getQuestionArray2()
    {
        return questionArray2;
    }

    public void setQuestionArray2(String[] questionArray2)
    {
        this.questionArray2 = questionArray2;
    }

    public String[] getQuestionArray1()
    {
        return questionArray1;
    }

    public void setQuestionArray1(String[] questionArray1)
    {
        this.questionArray1 = questionArray1;
    }

    public LinearLayout getIv()
    {
        return iv;
    }

    public void setIv(LinearLayout iv)
    {
        this.iv = iv;
    }

    public TextView getTv()
    {
        return tv;
    }

    public void setTv(TextView tv)
    {
        this.tv = tv;
    }

    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);

        setContentView(R.layout.setting_help_layout);

        iv = (LinearLayout) findViewById(R.id.layout_back_button);
        tv = (TextView) findViewById(R.id.second_title_text);

        questionArray1 = this.getResources().getStringArray(R.array.questionArray1); // 新手问题
        questionArray2 = this.getResources().getStringArray(R.array.questionArray2); // 账号问题
        initGuideTv();
        initAccountTv();
        tv.setText(R.string.setting_title_name);
        iv.setOnClickListener(mOnClickListener);

    }

    /**
     * 初始化新手问题 文本框
     */
    private void initGuideTv()
    {
        guideTv = new TextView[questionArray1.length];
        guideTv[0] = (TextView) findViewById(R.id.guide_item1);
        guideTv[1] = (TextView) findViewById(R.id.guide_item2);
        guideTv[2] = (TextView) findViewById(R.id.guide_item3);
        guideTv[3] = (TextView) findViewById(R.id.guide_item4);
        guideTv[4] = (TextView) findViewById(R.id.guide_item5);
        guideTv[5] = (TextView) findViewById(R.id.guide_item6);
        guideTv[6] = (TextView) findViewById(R.id.guide_item7);

        for (int i = 0; i < questionArray1.length; i++)
        {
            guideTv[i].setText(questionArray1[i]);
        }
    }

    /**
     * 初始化账号问题 文本框
     */
    private void initAccountTv()
    {
        accountTv = new TextView[questionArray2.length];
        accountTv[0] = (TextView) findViewById(R.id.account_item1);
        accountTv[1] = (TextView) findViewById(R.id.account_item2);
        accountTv[2] = (TextView) findViewById(R.id.account_item3);
        for (int i = 0; i < questionArray2.length; i++)
        {
            accountTv[i].setText(questionArray2[i]);
        }
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
