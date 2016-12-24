package com.bcinfo.tripaway.view.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 报名确认（同意/拒绝）对话框
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月23日 下午7:04:11
 */
public class AffirmDialog extends BaseActivity implements OnClickListener
{
    private  ImageView userPhoto;
    private TextView userName;
    private TextView userSign;
    private TextView userDescription;
    private   EditText responseReason;
    private Button commitButton;
    private Button cancelButton;
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.affirm_dialog);
        userPhoto=(ImageView)findViewById(R.id.response_user_photo);
        userName=(TextView)findViewById(R.id.response_user_name);
        userSign=(TextView)findViewById(R.id.response_user_sign);
        userDescription=(TextView)findViewById(R.id.response_user_description);
        responseReason=(EditText)findViewById(R.id.response_reason);
        commitButton=(Button)findViewById(R.id.response_commit_button);
        cancelButton=(Button)findViewById(R.id.response_cancel_button);
        commitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        initData();
    }
    
    private void initData()
    {
        String photoUrl="http://img3.3lian.com/2014/c1/51/d/34.jpg";
        ImageLoader.getInstance().displayImage(photoUrl, userPhoto);
        boolean isAgree=getIntent().getBooleanExtra("isAgree", false);
        if(isAgree)
        {
            responseReason.setHint("请输入同意理由...");
            commitButton.setText("同意");
            commitButton.setBackgroundResource(R.drawable.btn_bg_green);
        }
        else
        {
            responseReason.setHint("请输入拒绝理由...");
            commitButton.setText("善意拒绝");
            commitButton.setBackgroundResource(R.drawable.btn_bg_red);
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.response_commit_button:
                finish();
                break;
            case R.id.response_cancel_button:
                finish();
                break;
            default:
                break;
        }
    }
}
