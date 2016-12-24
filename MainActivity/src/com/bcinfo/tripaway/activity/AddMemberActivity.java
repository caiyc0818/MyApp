package com.bcinfo.tripaway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.PartnerInfo;
import com.bcinfo.tripaway.utils.ToastUtil;

/**
 * @author hanweipeng
 * @date 2015-7-9 下午2:07:31
 */
public class AddMemberActivity extends BaseActivity implements OnClickListener
{
    /**
     * 同行人姓名
     */
    private EditText memberNameEdt;
    
    /**
     * 同行人证件号
     */
    private EditText memberNumberEdt;
    
    /**
     * 删除按钮
     */
    private TextView saveBtn;
    
    private TextView delBtn;
    
    private PartnerInfo partnerInfo;
    
    private boolean isAdd = true;
    private RelativeLayout second_title;
    private int index = 0;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.add_member_activity);
        second_title= (RelativeLayout)findViewById(R.id.second_title);
        second_title.getBackground().setAlpha(255);
        initTitleLayout("添加同行人", "删除");
        initView();
    }
    
    private void initTitleLayout(String title, String rightText)
    {
        TextView titleTV = (TextView)findViewById(R.id.second_title_text);
        LinearLayout backButton = (LinearLayout)findViewById(R.id.layout_back_button);
        delBtn = (TextView)findViewById(R.id.event_commit_button);
        titleTV.setText(title);
        delBtn.setText(rightText);
        delBtn.setOnClickListener(this);
        backButton.setOnClickListener(mOnClickListener);
    }
    
    protected void initView()
    {
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        memberNameEdt = (EditText)findViewById(R.id.member_name);
        memberNumberEdt = (EditText)findViewById(R.id.member_number);
        saveBtn = (TextView)findViewById(R.id.save_btn);
        saveBtn.getBackground().setAlpha(255);
        saveBtn.setOnClickListener(this);
        if (isAdd)
        {
            partnerInfo = new PartnerInfo();
            delBtn.setVisibility(View.GONE);
        }
        else
        {
            index = getIntent().getIntExtra("index", 0);
            partnerInfo = getIntent().getParcelableExtra("partner");
            delBtn.setVisibility(View.VISIBLE);
            memberNameEdt.setText(partnerInfo.getRealName());
            memberNumberEdt.setText(partnerInfo.getIdNo());
        }
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.event_commit_button:
                Intent delIntent = new Intent();
                delIntent.putExtra("action", "del");
                delIntent.putExtra("index", index);
                setResult(1001, delIntent);
                finish();
                activityAnimationClose();
                break;
            case R.id.save_btn:
                if (memberNameEdt.getText().toString().equals(""))
                {
                    ToastUtil.showToast(AddMemberActivity.this, "同行人姓名不能为空");
                    return;
                }
                else if (memberNumberEdt.getText().toString().equals(""))
                {
                    ToastUtil.showToast(AddMemberActivity.this, "同行人证件号不能为空");
                    return;
                }
                else
                {
                    partnerInfo.setRealName(memberNameEdt.getText().toString());
                    partnerInfo.setIdNo(memberNumberEdt.getText().toString());
                }
                if (isAdd)
                {
                    Intent addIntent = new Intent();
                    addIntent.putExtra("action", "add");
                    addIntent.putExtra("partner", partnerInfo);
                    setResult(1001, addIntent);
                }
                else
                {
                    Intent addIntent = new Intent();
                    addIntent.putExtra("action", "modify");
                    addIntent.putExtra("index", index);
                    addIntent.putExtra("partner", partnerInfo);
                    setResult(1001, addIntent);
                }
                finish();
                activityAnimationClose();
                break;
            default:
                break;
        }
    }
}
