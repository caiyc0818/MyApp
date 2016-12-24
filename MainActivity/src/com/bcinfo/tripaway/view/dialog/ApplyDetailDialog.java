package com.bcinfo.tripaway.view.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 申请详情对话框
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月24日 下午2:54:54
 */
public class ApplyDetailDialog extends BaseActivity implements OnClickListener
{
    private ImageView userPhoto;
    private TextView userName;
    private TextView userAddress;
    private TextView leaveWords;
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.apply_detail_dialog);
        userPhoto=(ImageView)findViewById(R.id.detail_user_photo);
        userName=(TextView)findViewById(R.id.detail_user_name);
        userAddress=(TextView)findViewById(R.id.detail_user_address);
        leaveWords=(TextView)findViewById(R.id.detail_leave_words);
        Button agreeButton=(Button)findViewById(R.id.detail_agree_button);
        Button refuseButton=(Button)findViewById(R.id.detail_refuse_button);
        agreeButton.setOnClickListener(this);
        refuseButton.setOnClickListener(this);
        
        String photoUrl="http://img3.3lian.com/2014/c1/51/d/34.jpg";
        ImageLoader.getInstance().displayImage(photoUrl, userPhoto);
    }
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent=new Intent(this, AffirmDialog.class);
        switch (v.getId())
        {
            case R.id.detail_agree_button:
                intent.putExtra("isAgree", true);
                break;
            case R.id.detail_refuse_button:
                intent.putExtra("isAgree", false);
                break;
            default:
                break;
        }
        startActivity(intent);
        finish();
    }
}
