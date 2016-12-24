package com.bcinfo.tripaway.activity;

import java.util.ArrayList;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ThemeListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 结伴主题
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月31日 上午11:41:03
 */
public class GoWithThemeActivity extends BaseActivity implements OnClickListener
{
    private ListView themeListview;
    private ArrayList<String> themeArrList = new ArrayList<String>();
    private ThemeListAdapter themeListAdapter;

    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.go_with_theme_activity);
        themeListview = (ListView) findViewById(R.id.theme_listview);
        LinearLayout back_button = (LinearLayout) findViewById(R.id.layout_back_button);
        TextView commit_button = (TextView) findViewById(R.id.event_commit_button);
        back_button.setOnClickListener(mOnClickListener);
        commit_button.setOnClickListener(this);
        for (int i = 0; i < 15; i++)
        {
            String name = "春节"+i;
            themeArrList.add(name);
        }
        themeListAdapter = new ThemeListAdapter(this, themeArrList);
        themeListview.setAdapter(themeListAdapter);
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
           
            case R.id.event_commit_button:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("themeList", themeListAdapter.getSelectThemeList());
                setResult(RESULT_OK, intent);
                finish();
                activityAnimationClose();
                break;
            default:
                break;
        }
    }
}
