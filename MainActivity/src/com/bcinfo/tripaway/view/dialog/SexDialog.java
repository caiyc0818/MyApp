package com.bcinfo.tripaway.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.UserInfo;

/**
 * @author hanweipeng
 * @date 2015-7-13 下午5:22:10
 */
public class SexDialog extends Dialog implements android.view.View.OnClickListener
{
    private RelativeLayout manLayout;
    
    private RelativeLayout womanLayout;
    
    private ImageView manSelectImg;
    
    private ImageView womanSelectImg;
    
    private SexSelectListener listener;
    
    public SexDialog(Context context, SexSelectListener listener, UserInfo userInfo)
    {
        super(context, R.style.sex_select_dialog);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.sex_dialog);
        this.listener = listener;
        initView(userInfo);
    }
    
    private void initView(UserInfo userInfo)
    {
        manLayout = (RelativeLayout)findViewById(R.id.man_layout);
        womanLayout = (RelativeLayout)findViewById(R.id.woman_layout);
        manSelectImg = (ImageView)findViewById(R.id.man_select);
        womanSelectImg = (ImageView)findViewById(R.id.woman_select);
        if (userInfo.getGender().equals("0"))
        {
            womanSelectImg.setBackgroundResource(R.drawable.choose_yes);
            manSelectImg.setBackgroundResource(R.drawable.choose_no);
        }
        else
        {
            manSelectImg.setBackgroundResource(R.drawable.choose_yes);
            womanSelectImg.setBackgroundResource(R.drawable.choose_no);
        }
        manLayout.setOnClickListener(this);
        womanLayout.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.man_layout:
                manSelectImg.setBackgroundResource(R.drawable.choose_yes);
                womanSelectImg.setBackgroundResource(R.drawable.choose_no);
                listener.selectSex(1);
                cancel();
                break;
            case R.id.woman_layout:
                womanSelectImg.setBackgroundResource(R.drawable.choose_yes);
                manSelectImg.setBackgroundResource(R.drawable.choose_no);
                listener.selectSex(2);
                cancel();
                break;
            default:
                break;
        }
    }
    
    public interface SexSelectListener
    {
        public void selectSex(int type);
    }
}
