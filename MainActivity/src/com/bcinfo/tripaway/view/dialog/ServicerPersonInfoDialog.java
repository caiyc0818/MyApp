package com.bcinfo.tripaway.view.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.PersonalInfoMoreActivity;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 产品-服务者信息对话框
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月19日 14:35:22
 */
@SuppressLint("Instantiatable")
public class ServicerPersonInfoDialog extends AlertDialog
{
    private Context mContext;
    
    private ImageView serviceHeadIcon;
    
    private String url = "http://img.my.csdn.net/uploads/201309/01/1378037127_1085.jpg";
    
    public ServicerPersonInfoDialog(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_servicer_info_dialog);
        this.getWindow().setGravity(Gravity.CENTER);// 对话框窗口居中
        serviceHeadIcon = (ImageView)findViewById(R.id.head_icon_iv);
        if(!StringUtils.isEmpty(url)){
        	ImageLoader.getInstance().displayImage(url, serviceHeadIcon);
        }
        initView();
    }
    
    private void initView()
    {
        ImageView layout_autoCloseIv = (ImageView)findViewById(R.id.layout_autoCloseIv);
        layout_autoCloseIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cancel();
            }
        });
        LinearLayout browser_mainPage_container = (LinearLayout)findViewById(R.id.browser_mainPage_container);
        browser_mainPage_container.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, PersonalInfoMoreActivity.class);
                mContext.startActivity(intent);
                cancel();
            }
        });
    }
}
