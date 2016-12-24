package com.bcinfo.tripaway.view.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;

/**
 * 更改产品状态对话框
 * @function
 * @author     JiangCS  
 * @version   1.0, 2014年12月23日 下午8:55:16
 */
public class ChangeProductStateDialog extends BaseActivity implements OnClickListener
{
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.change_product_state_dialog);
        RelativeLayout  stateIssuet=(RelativeLayout)findViewById(R.id.state_issuet_layout);
        RelativeLayout  stateClose=(RelativeLayout)findViewById(R.id.state_close_layout);
        RelativeLayout  stateDelect=(RelativeLayout)findViewById(R.id.state_delect_layout);
        ImageView stateIssuetIMG=(ImageView)findViewById(R.id.state_issue_img);
        ImageView stateCloseIMG=(ImageView)findViewById(R.id.state_close_img);
        ImageView stateDelectIMG=(ImageView)findViewById(R.id.state_delect_img);
        stateIssuet.setOnClickListener(this);
        stateClose.setOnClickListener(this);
        stateDelect.setOnClickListener(this);
      String curState=  getIntent().getStringExtra("state");
      if(curState!=null)
      {
          if(curState.equals("issuet"))
          {
              stateIssuetIMG.setImageResource(R.drawable.change_state_select);
              stateCloseIMG.setImageResource(R.drawable.change_state_unselect);
              stateDelectIMG.setImageResource(R.drawable.change_state_unselect);
          }
          else if(curState.equals("close"))
          {
              stateIssuetIMG.setImageResource(R.drawable.change_state_unselect);
              stateCloseIMG.setImageResource(R.drawable.change_state_select);
              stateDelectIMG.setImageResource(R.drawable.change_state_unselect);
          }
          else
          {
              stateIssuetIMG.setImageResource(R.drawable.change_state_unselect);
              stateCloseIMG.setImageResource(R.drawable.change_state_unselect);
              stateDelectIMG.setImageResource(R.drawable.change_state_select);
          }
      }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.state_issuet_layout:
                finish();
                break;
            case R.id.state_close_layout:
                finish();
                break;
            case R.id.state_delect_layout:
                finish();
                break;
            default:
                break;
        }
    }
}
