package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.HelpAndConsultAdapter;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.HelpInfo;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
public class HelpDetailActivity extends BaseActivity implements OnClickListener
{
    
    private  RelativeLayout askRelativeLayout;
    private  TextView titleTextView;
    private  TextView contentTextView;
    
    private String title;
    private String content;
    
    
    @Override
    protected void onCreate(Bundle bundle)
    {

        super.onCreate(bundle);

        setContentView(R.layout.help_and_consult_detail);
        setSecondTitle("问题详情");
      //  getHelp("100","1");
        Intent intent = getIntent();
        title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");
        
        findView();
        titleTextView.setText(title);
contentTextView.setText(content);        
        askRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HelpDetailActivity.this, ConsultOrComplaintActivity.class);
                intent.putExtra("type", "consultation");
                startActivity(intent);
                activityAnimationOpen();
			}
		});
        
   }

    private void findView(){
    	askRelativeLayout=(RelativeLayout)this.findViewById(R.id.ask);
    	titleTextView=(TextView)this.findViewById(R.id.title);
    	contentTextView=(TextView)this.findViewById(R.id.content);
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
